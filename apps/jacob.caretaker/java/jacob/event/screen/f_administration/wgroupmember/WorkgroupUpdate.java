/*
 * Created on 09.06.2004
 *
 */
package jacob.event.screen.f_administration.wgroupmember;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * Gewährleistet dass das Feld hwg_name eindeutig in der Datenbank ist.<br>
 * <br>
 * Es ist mir allerdings nicht klar warum dies nicht als Constraint in der
 * Datenbank modeliert worden ist (Altlast?- Ja!).
 *              
 * @author Andreas Herz
 *
 */
public final class WorkgroupUpdate extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: WorkgroupUpdate.java,v 1.10 2005/03/03 15:38:14 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.10 $";

  /**
   * Wird ausgelöst wenn der Speichern Button gedrückt wird.
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button)throws Exception 
	{
    // Der Update-Button ist ein Tri-State Button. Man mus sich den Status der
    // Gruppe ansehen.
    //
    if(context.getGroup().getDataStatus()==IGuiElement.UPDATE)
    {  
		  String hwg_name     = context.getGroup().getInputFieldValue("workgroupHwg_name");
		  String selectedPkey = context.getSelectedRecord().getStringValue("pkey");
		  
		  // Neuen DatenAccessor anlegen um die derzeitigen Änderungen/Vorgängen in dem 
		  // Standart Accessor nicht zu verändern. SEHR wichtig!
		  //
		  IDataAccessor accessor=context.getDataAccessor().newAccessor();
		  IDataTable workgroup=accessor.getTable("workgroup");
		  
		  // Gibt es eine HWG-Gruppe mit dem selben Namen und ist es NICHT der gerade
		  // selektierte Datensatz?
		  //
		  if(hwg_name!=null && hwg_name.length()>0)
		  { 
			  workgroup.qbeSetKeyValue("hwg_name",hwg_name);
			  workgroup.qbeSetValue("pkey","!"+selectedPkey);
			  if(workgroup.search()>0)
			  {
			    // Fehlermeldung anzeigen .....
		      IMessageDialog dialog =context.createMessageDialog("Das Feld 'HWG-EDVIN' muß eindeutig sein. Eintrag ist bereits vergeben.");
		      dialog.show();
		      // .... und speichern verhindern.
		      return false;
			  }
			 IDataTableRecord record = context.getSelectedRecord();
			 if (record.getValue("activitytype") == null || record.getValue("accountingcode") == null || record.getValue("hwg_rate") == null )
			 {
		    // Fehlermeldung anzeigen .....
	      IMessageDialog dialog =context.createMessageDialog("Das Feld 'Werks-ID','Leistungsart' und 'Kostenstelle' müssen für 'HWG-EDVIN' gefüllt sein");
	      dialog.show();
	      // .... und speichern verhindern.
	      return false;
		 	
			 }
		  }
    }
	 
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext arg0, IGuiElement arg1) 
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext arg0, IGuiElement.GroupState arg1,	IGuiElement arg2) throws Exception 
	{
	}
}
