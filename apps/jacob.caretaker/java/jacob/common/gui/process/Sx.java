/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Aug 22 13:53:55 CEST 2007
 */
package jacob.common.gui.process;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;
import jacob.common.AppLogger;
import jacob.common.data.DataUtils;
import jacob.common.data.Routing;
import jacob.model.Appprofile;

import org.apache.commons.logging.Log;


/**
 * The event handler for the Sx search button.<br>
 * 
 * @author achim
 */
public class Sx extends ISearchActionEventHandler 
{
	static public final transient String RCS_ID = "$Id: Sx.java,v 1.2 2008/04/29 16:50:29 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the SEARCH action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
    IDataTable categoryTable = context.getDataTable("category");
    if (categoryTable.recordCount()!=1)
    {
        alert( "Bitte wählen Sie erst ein Gewerk aus");
        return false;
    }
    
    IDataTable siteTable     = context.getDataTable("faplissite"); 
    IDataTable locationTable = context.getDataTable("location");
    if (categoryTable.getRecord(0).getValue("locationrequired").equals("Ja") && (siteTable.recordCount()!=1 || locationTable.recordCount()!=1))
    {
        alert("Störungsort muss mindestens mit Angabe eines Werkes ausgewählt werden.");
        return false;      
    }
    
    String contractKey = Routing.getContractKey(locationTable.getRecord(0));
    if (contractKey == null)
    {
      contractKey = DataUtils.getAppprofileValue(context, Appprofile.contract_key);
    }
    // setzen Constraint von processProcessname
    String processName = context.getGroup().getInputFieldValue("processProcessname");
    if (processName != null) context.getDataTable().qbeSetValue("processname",processName);
    
    IDataBrowser contextBrowser = Routing.getProcessBrowser(context.getDataAccessor(),categoryTable.getRecord(0).getStringValue("pkey"),contractKey);
   
    context.getGUIBrowser().setData(context,contextBrowser);
    //  Frage: warum liefert diese Funktion IMMER 'false' zurück. Darf die Action den nie ausgeführt werden?
    //        Man könnte dann einen normalen Button anstatt einen Actionbutton verwenden.
    // Antwort: Wegen der Kompatibilität zum FatClient
    return false;
	}

	/**
	 * This event method will be called, if the SEARCH action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
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
