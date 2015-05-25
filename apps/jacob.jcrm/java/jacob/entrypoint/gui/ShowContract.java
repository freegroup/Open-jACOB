package jacob.entrypoint.gui;


/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * 
 * You can access this entry point within an WebBrowser with the URL:
 * http://localhost:8080/jacob/enter?entry=ShowActivity&app=jCRMx&user=USERNAME&pwd=PASSWORD&param1=abc
 *  
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and password of the application.
 *       2. Replace localhost:8080 with the real servername and port.
 *       3. You can add any additional parameters to the url. The jACOB application servers will provide them
 *          for you via the properties.getProperty("...") method.
 *
 * @author {user}
 *
 */
public class ShowContract extends ShowReminderItem
{
  /* (non-Javadoc)
   * @see jacob.entrypoint.gui.ShowReminderItem#getTableAlias()
   */
  protected String getTableAlias()
  {
    return "contract";
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.entrypoint.IGuiEntryPoint#getDomain()
   */
  public String getDomain()
  {
    return "f_contract";
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.entrypoint.IGuiEntryPoint#getForm()
   */
  public String getForm()
  {
    return "contract";
  }
}
