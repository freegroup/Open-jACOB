/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Oct 07 12:48:26 CEST 2010
 */
package jacob.entrypoint.gui;

import jacob.common.AppLogger;
import jacob.common.MenutreeUtil;
import jacob.model.Active_menutree;

import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;

/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * <p>
 * You can access this entry point within an web browser with the URL:
 * http://localhost:8080/jacob/enter?entry=ShowMenutreeEntry&app=wdb&user=USERNAME&pwd=PASSWORD&ref_name=F_Gewerk12345
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and
 * password of the application.
 * <li>
 * 2. Replace localhost:8080 with the real server name and port.
 * <li>
 * 3. You can add any additional parameters to the url. The jACOB application
 * server will provide them for you via the
 * <code>properties.getProperty("...")</code> method.
 * <li>
 * 
 * @author Andreas Sonntag
 */
public class ShowMenutreeEntry extends IGuiEntryPoint
{
  static public final transient String RCS_ID = "$Id: ShowMenutreeEntry.java,v 1.2 2010-10-17 14:49:55 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  static private final transient Log logger = AppLogger.getLogger();

  /*
   * The main method for the entry point.
   */
  public void enter(IClientContext context, Properties props) throws Exception
  {
    String refName = props.getProperty("ref_name");
    if (refName != null)
    {
      IDataTable menutreeTable = context.getDataTable(Active_menutree.NAME);
      menutreeTable.qbeClear();
      menutreeTable.qbeSetKeyValue(Active_menutree.external_ref_name, refName);
      menutreeTable.search();
      if (menutreeTable.recordCount() > 0)
      {
        IDataTableRecord menutreeRec = menutreeTable.getRecord(0);
        MenutreeUtil.show(context, menutreeRec.getStringValue(Active_menutree.pkey));
        return;
      }
    }
    logger.warn("No menutree entry with reference name '" + refName + "' found");
    alert("Artikel mit Referenznamen \"" + refName + "\" ist nicht vorhanden oder gesperrt");
  }

  /**
   * Returns the domain for the GUI entry point.
   */
  public String getDomain()
  {
    return "artikel";
  }

  /**
   * Returns the name of a form within the returned domain.
   */
  public String getForm()
  {
    return "artikel_menutree";
  }

  /**
   * @return <code>false</code>, if the GUI entry point has no left side
   *         navigation.
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
   * @return <code>false</code>, if the GUI entry point has no toolbar at the
   *         top.
   */
  public boolean hasToolbar()
  {
    // Toolbar wird für die Suchleiste gebraucht
    return true;
  }
}
