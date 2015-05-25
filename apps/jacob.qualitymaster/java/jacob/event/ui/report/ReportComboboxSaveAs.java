/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 17 16:39:48 CET 2009
 */
package jacob.event.ui.report;

import jacob.common.AppLogger;
import jacob.event.ui.ReportWriter.ReportProvider;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;

import de.tif.jacob.report.impl.Report;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableComboBoxEventHandler;

/**
 * 
 * @author achim
 */
public class ReportComboboxSaveAs extends IMutableComboBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: ReportComboboxSaveAs.java,v 1.1 2009-12-24 10:02:23 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Called, if the user changed the selection during the NEW or UPDATE state of the related table record.
   * 
   * @param context
   *          The current work context of the jACOB application.
   * @param comboBox
   *          The emitter of the event.
   */
  public void onSelect(IClientContext context, IMutableComboBox comboBox) throws Exception
  {
    Report report = ReportProvider.get(context);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String[] mimeSelection = comboBox.getSelection();
    String mimeType = mimeSelection[0];
    if (mimeType.equals("Save As ..."))
    {
      return;
    }
    report.render(out, mimeType);
    if ("text/formatted".equals(mimeType))
      context.createDocumentDialog("text/plain", report.getName() + ".txt", out.toByteArray()).show();
    else if ("text/plain".equals(mimeType))
      context.createDocumentDialog(mimeType, report.getName() + ".txt", out.toByteArray()).show();
    else if ("application/excel".equals(mimeType))
      context.createDocumentDialog(mimeType, report.getName() + ".xls", out.toByteArray()).show();
    else if ("application/pdf".equals(mimeType))
      context.createDocumentDialog(mimeType, report.getName() + ".pdf", out.toByteArray()).show();
    else
      context.createDocumentDialog(mimeType, report.getName(), out.toByteArray()).show();
    comboBox.clearSelection();
    comboBox.selectOption("Save As ...");
    comboBox.setEnable(true);
  }

  /**
   * The event handler if the group status has been changed.<br>
   * This is a good place to enable/disable some list box entries in relation to the state of the selected record.<br>
   * <br>
   * Note: You can only enable/disable <b>valid</b> enum values of the corresponding table field.<br>
   * <br>
   * 
   * @param context
   *          The current work context of the jACOB application.
   * @param status
   *          The new state of the group.
   * @param comboBox
   *          The emitter of the event.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IMutableComboBox comboBox) throws Exception

  {
    Report report = ReportProvider.get(context);
    if (report!=null)
    {
      comboBox.setVisible(true);
      comboBox.setEnable(true);
      comboBox.setMultichoice(false);
      comboBox.removeOptions();
      comboBox.addOption("Save As ...");
      comboBox.addOption("text/plain");
      comboBox.addOption("text/formatted");
      comboBox.addOption("application/excel");
      comboBox.addOption("application/pdf");
      comboBox.selectOption("Save As ...");
    }
    else 
    {
      comboBox.setVisible(false);
    }

  }
}
