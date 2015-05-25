/*
 * Created on 23.08.2004
 * by mike
 *
 */
package jacob.common.gui.task;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;

/**
 * Setzt diverse Constraints f�r die Foreignfields in Task
 * @author mike
 *
 */
public class ConstraintForeignSearch extends IForeignFieldEventHandler
{
static public final transient String RCS_ID = "$Id: ConstraintForeignSearch.java,v 1.3 2005/03/03 15:38:15 sonntag Exp $";
static public final transient String RCS_REV = "$Revision: 1.3 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IForeignFieldEventHandler#beforeSearch(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IForeignField)
	 */
	public boolean beforeSearch(IClientContext context, IForeignField foreingField) throws Exception
	{
		/*
		 * Mike:
		 * Im Fat Client steht im Sript TableSearch bl�dsinn
		 * allerdings ist diese Version zu zeitraubend.
		 * Ich habe es korrigiert, so dass es nun ist wie im Fatclient 
		 * 
		 */
		// externes System auf "systemstatus", "aktiv" einschr�nken
/*
		IDataTable extSystem = context.getDataTable("ext_system");
		if (extSystem.recordCount()!=1)
		{
			extSystem.qbeSetValue( "systemstatus", "aktiv");
		}
*/
		// Mandanteninfo setzen
		IDataTable hwgtasktype = context.getDataTable("hwgtasktype");
		IDataTable taskworkgroup = context.getDataTable("taskworkgroup");

		if (hwgtasktype.recordCount()!=1 && taskworkgroup.recordCount()==1 )
		{
			hwgtasktype.qbeSetValue( "taskworkgroup_key", taskworkgroup.getRecord(0).getStringValue("pkey"));
		}
		// nur HWGs des AKs
		IDataTable callworkgroup = context.getDataTable("callworkgroup");
		if (callworkgroup.recordCount()==1)
		{
			IDataTable workgrouphwg = context.getDataTable("workgrouphwg");
			workgrouphwg.qbeSetValue("workgroup_key",callworkgroup.getRecord(0).getStringValue("pkey"));
		}
/*
		// Auftragsart einschr�nken
		IDataTable tasktype = context.getDataTable("tasktype");
		if (tasktype.recordCount()!=1)
		{
			tasktype.qbeSetValue( "tasktypestatus", "g�ltig");
		}
*/
		// Kostenstelle des Auftragsobjekts einschr�nken
		IDataTable objaccountingcode = context.getDataTable("objaccountingcode");
		if (objaccountingcode.recordCount()!=1)
		{
			objaccountingcode.qbeSetValue( "accountingstatus", "g�ltig|NULL");
		}
/*
		// Kostenstelle des Meldungsobjekts einschr�nken
		IDataTable accountingcode = context.getDataTable("accountingcode");
		if (accountingcode.recordCount()!=1)
		{
			accountingcode.qbeSetValue( "accountingstatus", "g�ltig");
		}
		// Objekt des Auftrages einschr�nken
		IDataTable taskobject = context.getDataTable("taskobject");
		if (taskobject.recordCount()!=1)
		{
			taskobject.qbeSetValue( "objstatus", "in Betrieb|in Reparatur");
		}
*/

		return true;
	}
}
