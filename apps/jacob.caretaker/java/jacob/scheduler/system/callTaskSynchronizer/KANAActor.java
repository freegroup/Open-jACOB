/*
 * Created on Jun 29, 2004
 *
 */
package jacob.scheduler.system.callTaskSynchronizer;

import jacob.exception.BusinessException;
import jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType;
import jacob.scheduler.system.callTaskSynchronizer.castor.CallUpdateType;
import jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType;
import jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType;

import java.util.Date;

import org.apache.commons.lang.SystemUtils;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.util.StringUtil;

/**
 * Diese Klasse implementiert die Schnitstelle zum Cluster 4 Kleinprojekte.<br><br>
 * Eine zugewiesene oder angenommene TTS-Meldung wird in KANA weitergeführt und
 * die TTS Meldung auf Status Dokumentiert gesetzt mit dem Hinweis auf die 
 * KANA-Auftragsnummer. Alle normalen Businessregeln werden dabei umgangen.
 * 
 * @author Mike Döring
 *
 */
public class KANAActor  implements IActor
{
  static public final transient String RCS_ID = "$Id: KANAActor.java,v 1.9 2005/03/03 15:38:15 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";
  static private final String cr = SystemUtils.LINE_SEPARATOR;
  /* 
   * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.TaskUpdateType)
   */
  public void proceed(TaskContextSystem context,TaskUpdateType update) throws Exception
  {
   	throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (task.update) verweigert."); 	
  }

  /* 
   * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.TaskInsertType)
   */
  public void proceed(TaskContextSystem context,TaskInsertType insert) throws Exception
  {
   	throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (task.insert) verweigert."); 	
  }

  /* 
   * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.CallUpdateType)
   */
  public void proceed(TaskContextSystem context,CallUpdateType update) throws Exception
  {
  	
  
  	IDataTable callTable = context.getDataTable("callmaster"); // Alias callmaster da dort keine Businessregel dranhängt
    String   pkey           = "" + update.getPkey();
    callTable.qbeClear();
    callTable.qbeSetValue("pkey",pkey);
    callTable.search();
    if (callTable.recordCount()!=1)
    {
    	throw new BusinessException("Meldung mit pkey=" + pkey + " ist nicht eindeutig");
    }
    //hier die KANA Infos übernehmen und commiten und anschließend erst den neuen Status commiten
  	IDataTableRecord callRecord = callTable.getRecord(0);
  	IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
  	try
		{
  	callRecord.setStringValue(currentTransaction,"externalorderid", update.getExternalId());
		// falls Gewährleistungsfall dann kenntlich machen:
		if (callRecord.getValue("warrentystatus")!=null)
		{
			callRecord.setValue(currentTransaction,"warrentystatus","wird verfolgt");
		}
		currentTransaction.commit();
		}
  	finally
		{
  		currentTransaction.close();
  	}
		
  	// CallStatus darf nur im Status AK zugewiesen oder Angenommen sein
  	String currentStatus = callRecord.getStringValue("callstatus");
  	
  	if (!(currentStatus.equals("AK zugewiesen") || currentStatus.equals("Angenommen")))
  	{
    	throw new BusinessException("Meldung ist im Status "+currentStatus+" und nicht AK zugewiesen oder Angenommen"); 		
  	}
  	// nur Meldungen ohne Aufträge dürfen nach KANA weitergeleitet werden
  	Integer opentaskcountObj = callRecord.getIntegerValue("opentaskcount");
  	int opentaskcount=opentaskcountObj!=null?opentaskcountObj.intValue():0;
  	Integer closedtaskcountObj = callRecord.getIntegerValue("closedtaskcount");
  	int closedtaskcount=closedtaskcountObj!=null?closedtaskcountObj.intValue():0; 	
  	if (opentaskcount>0 || closedtaskcount>0)
		{
    	throw new BusinessException("Meldung hat schon Aufträge"); 			
		}
  	
  	currentTransaction = callTable.startNewTransaction();
  	try
		{ 		 	
	  	Date   dateModified   = update.getDatemodified();
	  	if (currentStatus.equals("AK zugewiesen"))
			{
				callRecord.setDateValue(currentTransaction,"dateowned",dateModified);
			} 
			callRecord.setDateValue(currentTransaction,"dateresolved",dateModified);
			callRecord.setDateValue(currentTransaction,"datedocumented",dateModified);
			callRecord.setDateValue(currentTransaction,"datemodified",dateModified);
			callRecord.setStringValue(currentTransaction,"callstatus","Dokumentiert");
			callRecord.setStringValue(currentTransaction,"callbackmethod","Keine");
			if (callRecord.getStringValue("coordinationtime_h")==null)
			{
				callRecord.setStringValue(currentTransaction,"coordinationtime_h","0");
			}
			if (callRecord.getStringValue("coordinationtime_m")==null)
			{
				callRecord.setStringValue(currentTransaction,"coordinationtime_m","0");
			}
			// falls Gewährleistungsfall dann kenntlich machen:
			if (callRecord.getValue("warrentystatus")!=null)
			{
				callRecord.setValue(currentTransaction,"warrentystatus","wird verfolgt");
			}
			long   notSlRelevant  = update.hasNotslrelevant()?update.getNotslrelevant():0;
			callRecord.setLongValue(currentTransaction,"notslrelevant",notSlRelevant);
			
		 	String currentAction = StringUtil.toSaveString(callRecord.getStringValue("action"));
		 	currentAction = currentAction +" Auftrag wird in Kana unter ID:"+update.getExternalId()+" weitergeführt";
		 	callRecord.setStringValueWithTruncation(currentTransaction,"action", currentAction);
		 	String history = callRecord.getStringValue("history");
		 	history = history +cr+" Auftrag wird in Kana unter ID:"+update.getExternalId()+" weitergeführt"+cr;
	  	callRecord.setStringValue(currentTransaction,"history", history);
		 	String problemtext = callRecord.getStringValue("problemtext");
		 	problemtext = problemtext +cr+" Auftrag wird in Kana unter ID:"+update.getExternalId()+" weitergeführt"+cr;
	  	callRecord.setStringValue(currentTransaction,"problemtext", problemtext);
	  	
	  	currentTransaction.commit();
		}
		finally
		{
			currentTransaction.close();
		}
  }

  /* 
   * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.CallInsertType)
   */
  public void proceed(TaskContextSystem context,CallInsertType insert) throws Exception
  {
  	throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (call.insert) verweigert."); 	
  }

}
