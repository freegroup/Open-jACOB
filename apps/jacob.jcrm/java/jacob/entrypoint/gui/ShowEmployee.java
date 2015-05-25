/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Oct 18 12:18:43 CEST 2005
 */
package jacob.entrypoint.gui;

import jacob.common.AppLogger;

import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;

/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * 
 * You can access this entry point within an WebBrowser with the URL:
 * http://localhost:8080/jacob/enter?entry=ShowEmployee&app=jcrm&user=USERNAME&pwd=PASSWORD&param1=abc
 * 
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and
 * password of the application. 2. Replace localhost:8080 with the real
 * servername and port. 3. You can add any additional parameters to the url. The
 * jACOB application servers will provide them for you via the
 * properties.getProperty("...") method.
 * 
 * @author {user}
 */
public class ShowEmployee extends IGuiEntryPoint
{
  static public final transient String RCS_ID = "$Id: ShowEmployee.java,v 1.1 2005/10/18 12:11:32 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  static public final String EMPLOYEEKEY_PROP = "employeeKey";

  /*
   * The main method for the entry point.
   *  
   */
  public void enter(IClientContext context, Properties props) throws Exception
  {
    String employeeKey = props.getProperty(EMPLOYEEKEY_PROP);

    // search employee
    //
    IDataTable employeeTable = context.getDataTable("employee");
    employeeTable.qbeSetKeyValue("pkey", employeeKey);
    if (employeeTable.search() == 0)
      return;

    // and propagate record
    //
    context.getDataAccessor().propagateRecord(employeeTable.getRecord(0), "r_employee", Filldirection.BACKWARD);
  }

  /**
   * Returns the domain for the GUI entry point.
   */
  public String getDomain()
  {
    return "f_invisible";
  }

  /**
   * Returns the name of a form within the returned domain.
   */
  public String getForm()
  {
    return "employeeReadonly";
  }

  /**
   * @return <code>false</code>, if the GUI entry point has no left side
   *         navigation.
   */
  public boolean hasNavigation()
  {
    return false;
  }

  /**
   * @return <code>false</code>, if the GUI entry point has no search
   *         browser.
   */
  public boolean hasSearchBrowser()
  {
    return false;
  }

  /**
   * @return <code>false</code>, if the GUI entry point has no toolbar a the
   *         top.
   */
  public boolean hasToolbar()
  {
    return false;
  }
}
