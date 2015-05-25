/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Oct 19 12:01:33 CEST 2005
 */
package jacob.event.ui.doctemplate;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 * Event handler to switch specific fields on/off.
 * 
 * @author andreas
 */
public class DoctemplateUsedin extends IComboBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: DoctemplateUsedin.java,v 1.3 2005/10/19 11:28:25 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Called, if the user changed the selection during the NEW or UPDATE state of
   * the related table record.
   * 
   * @param context
   *          The current work context of the jACOB application.
   * @param emitter
   *          The emitter of the event.
   */
  public void onSelect(IClientContext context, IComboBox emitter) throws Exception
  {
    setEnableContractElements(context, emitter);
  }

  private static void setEnableContractElements(IClientContext context, IComboBox combobox) throws Exception
  {
    boolean enable = true;
    if (context.getGroup().getDataStatus() == IGuiElement.NEW || context.getGroup().getDataStatus() == IGuiElement.UPDATE)
    {
      enable = "Contract".equals(combobox.getValue());
    }
    context.getGroup().findByName("doctemplateContracttype").setEnable(enable);
    context.getGroup().findByName("doctemplateProduct").setEnable(enable);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state, IGuiElement emitter) throws Exception
  {
    setEnableContractElements(context, (IComboBox) emitter);
  }
}
