/*
 * Created on 19.07.2004
 *
 */
package jacob.common.gui.call;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 * Zeigt die Daten der Haupmeldung im selben Fenster an
 */
public class callMasterDetails extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: callMasterDetails.java,v 1.3 2004/08/17 16:31:06 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/* 
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)  throws Exception
	{
	  String masterKey = context.getSelectedRecord().getStringValue("mastercall_key");
	  context.clearDomain();
	  IDataBrowser browser =context.getDataBrowser();
	  IDataTable   callTable=context.getDataTable();
	  callTable.qbeSetValue("pkey",masterKey);
	  browser.search("r_call", Filldirection.BOTH);
	  context.getGUIBrowser().setData(context, browser);
	}

	
	/* 
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
	  emitter.setEnable(status==IGuiElement.SELECTED &&(context.getSelectedRecord().getValue("mastercall_key")!=null));
	}
}
