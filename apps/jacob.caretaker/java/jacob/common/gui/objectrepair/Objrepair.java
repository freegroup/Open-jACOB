/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jul 09 09:48:33 CEST 2008
 */
package jacob.common.gui.objectrepair;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Object;

import org.apache.commons.logging.Log;


/**
 * The event handler for the Objrepair generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class Objrepair extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Objrepair.java,v 1.1 2008/07/24 13:15:42 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

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
    //Diese Methode liest feherhaft in SAP umbenannte Objekte,
    // macht diese ungültig, benennt das Objekt um, 
    //sucht das alte Objekt und benennt dieses in das neue um.
		IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable objectalt = acc.getTable(Object.NAME);
    IDataAccessor acc1 = context.getDataAccessor().newAccessor();
    IDataTable objectneu = acc1.getTable(Object.NAME);
    IDataAccessor acc2 = context.getDataAccessor().newAccessor();
    IDataTable objrepair = acc2.getTable("objectrepair");
    objrepair.qbeClear();
    objrepair.qbeSetKeyValue("status", "todo");
    objrepair.setMaxRecords(IDataTable.UNLIMITED_RECORDS);
    objrepair.search();
    for (int i = 0; i < objrepair.recordCount(); i++) 
    {
      IDataTableRecord objreprecord = objrepair.getRecord(i);
      IDataTransaction transaction = acc.newTransaction();
      // Suche neues Object
      objectneu.qbeClear();
      String objname = objreprecord.getSaveStringValue("objneu");
      objectneu.qbeSetKeyValue(Object.external_id, objname);
      objectneu.search();
      if (objectneu.recordCount()==1)
      {
        IDataTableRecord objectneurec = objectneu.getRecord(0);
        //Suche altes Object
        System.out.println("*-"+objreprecord.getSaveStringValue("objalt")+"-0");
        objectalt.qbeClear();
        objectalt.qbeSetKeyValue(Object.external_id, objreprecord.getSaveStringValue("objalt"));
        objectalt.search();
        if (objectalt.recordCount()==1)
        {
          System.out.println("Beide Objekte gefunden");
          IDataTableRecord objectaltrec = objectalt.getRecord(0);
          objectneurec.setValue(transaction, Object.objstatus, Object.objstatus_ENUM._geloescht);
          
          objectneurec.setStringValueWithTruncation(transaction, Object.external_id, objname + "-SAP-Umbenannt");
          objectaltrec.setValue(transaction, Object.external_id, objname);
          objreprecord.setValue(transaction, "status","Done");
        }
        else
        {
          System.out.println("Altes Objekt nicht gefunden - " +i);
          objreprecord.setValue(transaction, "status","Altes Objekt nicht gefunden");
        }
        
      }
      else 
      {
        System.out.println("Neues Objekt nicht gefunden - " +i);
        objreprecord.setValue(transaction, "status","Neues Objekt nicht gefunden");
      }
      try 
      {
        
        System.out.println("Erfolgreich - " +i);
        transaction.commit();
      } 
      catch (Exception e) 
      {
        // TODO: handle exception
        System.out.println(e.getMessage());
      }
      transaction.close();
      
    }
    System.out.println("fertig");
		
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
