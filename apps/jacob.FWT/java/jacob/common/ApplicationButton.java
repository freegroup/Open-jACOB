package jacob.common;

import java.util.TreeMap;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

public class ApplicationButton extends IButtonEventHandler
{

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    TreeMap application = (TreeMap) context.getUser().getProperty("applications");
    String appName=(String)application.get(button.getLabel());
    Access.login(appName);

  }

 

}
