package jacob.event.ui.ReportWriter;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

public class AbstractConfigureButton extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: AbstractConfigureButton.java,v 1.1 2009-12-24 10:02:21 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    context.getForm().setVisible(false);
    context.getDomain().findByName("ReportWriter_configure").setVisible(true);
    context.setCurrentForm("ReportWriter_configure");
  }

}
