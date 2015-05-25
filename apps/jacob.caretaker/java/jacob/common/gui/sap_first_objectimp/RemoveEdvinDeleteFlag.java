/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 28 09:33:04 CET 2007
 */
package jacob.common.gui.sap_first_objectimp;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.gui.sap_first_objectimp.Delete_all.MyCallback;
import jacob.model.Object;
import jacob.model.Sap_first_objectimp;
import jacob.model.Sap_object;
import jacob.model.Sap_object.objstatus_ENUM;

import org.apache.commons.logging.Log;


/**
 * The event handler for the RemoveEdvinDeleteFlag generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class RemoveEdvinDeleteFlag extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: RemoveEdvinDeleteFlag.java,v 1.1 2007/12/18 14:17:19 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();
  
  static class MyCallback implements IOkCancelDialogCallback
  {
    public void onOk(IClientContext context) throws Exception
    {
      IDataAccessor acc = context.getDataAccessor().newAccessor();
      IDataTransaction transaction = acc.newTransaction();
      IDataTable urimport = acc.getTable(Sap_first_objectimp.NAME);
      IDataTable object = acc.getTable(Sap_object.NAME);
      urimport.setMaxRecords(urimport.UNLIMITED_RECORDS);
      urimport.qbeSetValue(Sap_first_objectimp.error, "null");
      urimport.search();
      System.out.println(urimport.recordCount() + " Sätze gefunden");
      int j = 0;
      try 
      {
        for (int i = 0; i < urimport.recordCount(); i++) 
        {
          IDataTableRecord urimprec = urimport.getRecord(i);
          object.qbeClear();
          object.qbeSetKeyValue(Sap_object.external_id, urimprec.getValue(Sap_first_objectimp.edvin_object));
          object.search();
          if (object.recordCount()==1)
          {
            j=j+1;
            IDataTableRecord objrec = object.getRecord(0);
            if (objrec.getintValue(Sap_object.ext_system_key)==11)
            {
              System.out.println("Objektist EdvinEV: " + Sap_first_objectimp.edvin_object);
            }
            else 
            if (objrec.hasNullValue(Sap_object.sap_old_edvin_status))
            {
              objrec.setValue(transaction, Sap_object.objstatus, Sap_object.objstatus_ENUM._in_Betrieb);
              System.out.println("Objekt hat keinen Altstatus: " + Sap_first_objectimp.edvin_object);
            }
            else {
              objrec.setValue(transaction, Sap_object.objstatus, objrec.getValue(Sap_object.sap_old_edvin_status));
            }
           
          }
          else {
            System.out.println("Objekt nicht gefunden oder nicht eindeutig: " + Sap_first_objectimp.edvin_object);
          }
          
        }
        System.out.println(j + " Records found to reset");
        
       transaction.commit();
      } 
      finally
      {
        transaction.close();
        }
    
    }

    public void onCancel(IClientContext context) throws Exception
    {

    }
  }

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    IOkCancelDialog dialog = context.createOkCancelDialog("Löschkennzeichen aller Edvinobjekte in dieser Datei werden zurückgesetzt\nFortfahren?", new MyCallback());
    dialog.show();
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}
