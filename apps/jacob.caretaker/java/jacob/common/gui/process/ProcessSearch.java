/*
 * Created on 19.04.2004
 * by mike
 *
 */
package jacob.common.gui.process;

import jacob.common.data.DataUtils;
import jacob.common.data.Routing;
import jacob.model.Appprofile;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * 
 * @author mike
 *
 */
public  class ProcessSearch extends IActionButtonEventHandler 
{
	 static public final transient String RCS_ID = "$Id: ProcessSearch.java,v 1.16 2008/04/29 16:50:29 sonntag Exp $";
	 static public final transient String RCS_REV = "$Revision: 1.16 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
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
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status,IGuiElement button) 
	{
	}
}
