/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 11 13:03:14 CEST 2006
 */
package jacob.common.gui.tc_platform;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Tc_calculateTotalViewButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Tc_calculateTotalViewButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Tc_calculateTotalViewButton.java,v 1.1 2006/10/12 09:32:10 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    // TODO: Hack, da Chart-Element immer einen selektierten Record braucht
    if (context.getSelectedRecord() == null)
    {
      IDataBrowser platformBrowser = context.getDataBrowser();
      if (platformBrowser.recordCount() == 0)
      {
        context.getDataAccessor().qbeClearAll();
        platformBrowser.search("r_tc_platform");
        if (platformBrowser.recordCount() == 0)
        {
          throw new UserException("Keine Bühnen vorhanden");
        }
      }
      
      platformBrowser.setSelectedRecordIndex(0);
      platformBrowser.propagateSelections();
    }
    
    PlatformChartData.enableOverall(context);
    ((IChart) context.getGroup().findByName("freeCapacityChart")).refresh();
    ((IChart) context.getGroup().findByName("workloadChart")).refresh();
    ((IText) context.getGroup().findByName("tc_platformName")).setValue("Alle Bühnen");
	}
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}
