/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jun 02 16:26:37 CEST 2009
 */
package jacob.event.ui.storage_email;

import jacob.common.AppLogger;
import jacob.common.htmleditor.HTMLEditorHelper;
import jacob.model.Storage_email;
import jacob.model.Storage_email_attachment;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * 
 * @author achim
 */
public class Storage_emailContainer extends ITabContainerEventHandler
{
  static public final transient String RCS_ID = "$Id: Storage_emailContainer.java,v 1.4 2009/10/08 10:32:01 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  private static String replaceScript(String sHtml)
  {
    // sHtml = sHtml.replaceAll("</script", "&lt;/script");
    // sHtml = sHtml.replaceAll("<script", "&lt;script");
    // sHtml = sHtml.replaceAll("</SCRIPT", "&lt;/SCRIPT");
    // sHtml = sHtml.replaceAll("<SCRIPT", "&lt;SCRIPT");
    // sHtml = sHtml.replaceAll("</Script", "&lt;/Script");
    // sHtml = sHtml.replaceAll("<Script", "&lt;Script");
    sHtml = sHtml.replaceAll("(?i)</ScRiPt", "&lt;/script");
    sHtml = sHtml.replaceAll("(?i)<ScRiPt", "&lt;script");
    return sHtml;
  }

  /*
   * This event method will be called, if the status of the corresponding group
   * has been changed. Derived event handlers could overwrite this method, e.g.
   * to enable/disable GUI elements in relation to the group state. <br>
   * Possible group state values are defined in {@link IGuiElement}:<br> <ul>
   * <li>{@link IGuiElement#UPDATE}</li> <li>{@link IGuiElement#NEW}</li>
   * <li>{@link IGuiElement#SEARCH}</li> <li>{@link IGuiElement#SELECTED}</li>
   * </ul>
   * 
   * @param context The current client context
   * 
   * @param state The new group state
   * 
   * @param element The corresponding GUI element to this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    element.hideTabStrip(true);
    // Contrl the Tab C>ontainer for TEXT/HTML or TEXT/PLAIN Messages

    context.getGroup().findByName("storage_emailStyledText").setLabel("");
    if (state.equals(IGroup.SELECTED))
    {
      IDataTableRecord rec = context.getSelectedRecord();
      IDataTable attachments = context.getDataTable(Storage_email_attachment.NAME);
      attachments.qbeClear();
      attachments.qbeSetValue(Storage_email_attachment.mime_type, "text/html");
      attachments.qbeSetValue(Storage_email_attachment.document, "unknown.txt");
      attachments.qbeSetKeyValue(Storage_email_attachment.storage_email_key, rec.getValue(Storage_email.pkey));
      attachments.search();
      if (attachments.recordCount() == 1)
      {
        IDataTableRecord attrec = attachments.getRecord(0);
        DataDocumentValue htmldoc = attrec.getDocumentValue(Storage_email_attachment.document);
        String sHtml = new String(htmldoc.getContent());
        sHtml = replaceScript(sHtml);
        sHtml = HTMLEditorHelper.setScrollBar(sHtml);
        context.getGroup().findByName("storage_emailStyledText").setLabel(sHtml);
        element.setActivePane(context, 1);
      }
      else if (rec.getSaveStringValue(Storage_email.emailmimetype).equalsIgnoreCase("text/html"))
      {
        String sHtml = rec.getSaveStringValue(Storage_email.text_body);
        sHtml = replaceScript(sHtml);
        sHtml = HTMLEditorHelper.setScrollBar(sHtml);
        context.getGroup().findByName("storage_emailStyledText").setLabel(sHtml);

        element.setActivePane(context, 1);
      }
      else
      {
        element.setActivePane(context, 0);
      }
    }

  }
}
