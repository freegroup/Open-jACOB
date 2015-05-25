/*
 * ConnManager.java
 * Created: 23.05.2007
 * $Log: ConnManager.java,v $
 * Revision 1.3  2007/08/13 14:54:04  achim
 * €nderungen Conection zum Testen eines Pools
 *
 * Revision 1.2  2007/08/08 11:38:31  sonntag
 * Überarbeitung Fehlerbegandlung, Exceptionhandling, etc.
 *
 * Revision 1.1  2007/08/01 13:40:03  achim
 * SAP
 * 
 */
package jacob.common.sap;

import jacob.model.Sapadmin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sap.mw.jco.JCO;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;

/**
 * Klasse die einen JCo-Connection erzeugt. Die Parameter des Pools werden
 * aus der Tabelle <b>sapadmin</b> ausgelesen. Benutzt wird der erste
 * Datensatz in dem das Flag <b><i>active</i></b> den Wert eins hat.
 *
 * @author Bernd.Clemenz
 * @version $Id: ConnManager.java,v 1.3 2007/08/13 14:54:04 achim Exp $
 * @since 
 */
public final class ConnManager
{
	private static final String LOG_PREFIX = "SAP-CON-MAN: ";
	private static final Log LOGGER = LogFactory.getLog(ConnManager.class.getName());
  
  private String m_sPoolName;
  //----------------------------------------------------------------------------
	/**
	 * Default Constructor.
   * @throws UserException wenn der Pool nicht erzeugt werden kann.
   * @throws NoSuchFieldException wenn beim Lesen der Poolparameter ein
   * Fehler auftritt.
	 */
	public ConnManager() throws Exception
  {
    super();

    final IDataTableRecord oAdminRec = readPoolDescriptor();
    createPool(oAdminRec);
  }
  // ----------------------------------------------------------------------------
	private IDataTableRecord readPoolDescriptor() throws Exception
  {
    final Context oCtxt = Context.getCurrent();
    final IDataTable oSapAdm = oCtxt.getDataTable(Sapadmin.NAME);
    oSapAdm.qbeClear();
    oSapAdm.qbeSetValue(Sapadmin.active, "1");
    oSapAdm.search();
    if (oSapAdm.recordCount() > 1)
    {
      throw new UserException("Es gibt mehrere aktive SAP-Verbindungsdatensätze. \\\n Bitte beheben Sie das Problem");
    }
    if (oSapAdm.recordCount() == 0)
    {
      throw new UserException("Es gibt keinen aktiven SAP-Verbindungsdatensatz. \\\n Bitte beheben Sie das Problem");
    }
    return oSapAdm.getRecord(0);
  }
	// ----------------------------------------------------------------------------
  /**
   * Beendet die Nutzung des Pools.
   */
	public void done()
	{
    if(LOGGER.isTraceEnabled())
		  LOGGER.trace(LOG_PREFIX + " shutting pool down.");
	 
    // TODO: SAP: wer ruft done() auf?
    
    // TODO: SAP: System.out weg
    
	  JCO.removeClientPool(m_sPoolName);
    //System.out.println("*SAP-CONNECTOR Pool '" + m_sPoolName + "' wurde entfernt");
    
	}
	//----------------------------------------------------------------------------
  private void createPool
               (
	               final IDataTableRecord oCurrent
               )
  throws Exception
  {
  		createPool
  		(
  		  oCurrent.getSaveStringValue(Sapadmin.name),
  		  oCurrent.getintValue(Sapadmin.maxconnections),
  		  oCurrent.getSaveStringValue(Sapadmin.sap_client),
  		  oCurrent.getSaveStringValue(Sapadmin.sap_user),
  		  oCurrent.getSaveStringValue(Sapadmin.pwd),
  		  oCurrent.getSaveStringValue(Sapadmin.language),
  		  oCurrent.getSaveStringValue(Sapadmin.host),
  		  oCurrent.getSaveStringValue(Sapadmin.sytemid)
  		);
  }
  //----------------------------------------------------------------------------
  private void createPool
               (
      			     final String sPoolName,
      			     final int    iMaxCon,
      			     final String sMandant,
      			     final String sUser,
      			     final String sPwd,
      			     final String sLang,
      			     final String sHost,
      			     final String sSysId
      		     )
  {
    //System.out.println("*SAP-CONNECTOR INIT");
	  if(LOGGER.isTraceEnabled())
	    LOGGER.trace(LOG_PREFIX + " start init.");	  
	  
	  if(LOGGER.isDebugEnabled())
	    LOGGER.debug(LOG_PREFIX + "try to create pool: " + sPoolName);
	  
	  if(null == sPoolName || "".equals(sPoolName))
		  throw new RuntimeException("Need a pool name");
	  
	  m_sPoolName = sPoolName;
	  
	  final JCO.Pool oPool = JCO.getClientPoolManager().getPool(sPoolName);
	  if(null != oPool)
	  {
      //System.out.println("*SAP-CONNECTOR Pool war vorhanden");
		  return;
	  }
	  
	  JCO.addClientPool
	      (
  		    sPoolName,
  		    iMaxCon,
  		    sMandant,
  		    sUser,
  		    sPwd,
  		    sLang,
  		    sHost,
  		    sSysId
  		  );
     //System.out.println("*SAP-CONNECTOR Pool '" + sPoolName + "' für User '" + sUser + "' wurde erzeugt");
	 
	  LOGGER.info(LOG_PREFIX + "init done");
  }
  //----------------------------------------------------------------------------
  /**
   * @return true wenn der JCo-Conectionpool mit dem gesetzten Namen
   * verf&uuml;gbar ist.
   */
  public boolean isPoolAvailable()
  {
    return null != JCO.getClientPoolManager().getPool(m_sPoolName);
  }
  //----------------------------------------------------------------------------
  /**
   * @return den aktuell gesetzten Namen des SAP-Connection-Pools.
   */
  public String getPoolName() { return m_sPoolName; }
}
