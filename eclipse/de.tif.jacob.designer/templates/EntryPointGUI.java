/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import java.util.Properties;

import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * <p>
 * You can access this entry point within an web browser with the URL:
 * http://localhost:8080/jacob/enter?entry={class}&app={application}&user=USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 *
 * @author {user}
 */
public class {class} extends IGuiEntryPoint
{
	static public final transient String RCS_ID = "$Id: EntryPointGUI.java,v 1.1 2007/05/18 16:13:26 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/*
	 * The main method for the entry point.
	 * 
	 */
	public void enter(IClientContext context, Properties props) throws Exception
	{
	  String param1 = props.getProperty("param1");
	}

	/**
	 * Returns the domain for the GUI entry point.
	 */
	public String getDomain()
	{
		return "<unknown>"; // <== EDIT this value!!!!
	}

	/**
	 * Returns the name of a form within the returned domain. 
	 */
	public String getForm()
	{
		return "<unknown>"; // <== EDIT this value!!!!
	}

	/**
	 * @return <code>false</code>, if the GUI entry point has no left side navigation.
	 */
	public boolean hasNavigation()
	{
		return true;
	}

	/** 
	 * @return <code>false</code>, if the GUI entry point has no search browser.
	 */
	public boolean hasSearchBrowser()
	{
		return true;
	}

	/** 
	 * @return <code>false</code>, if the GUI entry point has no toolbar at the top.
	 */
	public boolean hasToolbar()
	{
		return true;
	}
}
