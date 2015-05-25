/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 28 08:51:44 CEST 2010
 */
package jacob.entrypoint.gui;

import java.util.Properties;

import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;
import jacob.common.AppLogger;
import jacob.common.ArticleUtil;

import org.apache.commons.logging.Log;

/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * <p>
 * You can access this entry point within an web browser with the URL:
 * http://localhost:8080/jacob/enter?entry=ShowArticle&app=wdb&user=USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 *
 * @author {user}
 */
public class ShowArticle extends IGuiEntryPoint
{
	static public final transient String RCS_ID = "$Id: ShowArticle.java,v 1.1 2010-09-28 07:33:39 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


	/*
	 * The main method for the entry point.
	 * 
	 */
	public void enter(IClientContext context, Properties props) throws Exception
	{
	  ArticleUtil.show(context, props.getProperty("pkey"));
	}

	/**
	 * Returns the domain for the GUI entry point.
	 */
	public String getDomain()
	{
		return "common"; // <== EDIT this value!!!!
	}

	/**
	 * Returns the name of a form within the returned domain. 
	 */
	public String getForm()
	{
		return "common_search"; // <== EDIT this value!!!!
	}

	/**
	 * @return <code>false</code>, if the GUI entry point has no left side navigation.
	 */
	public boolean hasNavigation()
	{
		return false;
	}

	/** 
	 * @return <code>false</code>, if the GUI entry point has no search browser.
	 */
	public boolean hasSearchBrowser()
	{
		return false;
	}

	/** 
	 * @return <code>false</code>, if the GUI entry point has no toolbar at the top.
	 */
	public boolean hasToolbar()
	{
		return false;
	}
}
