package jacob.common;

import jacob.model.Textblock;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.impl.html.TableListBox;
import de.tif.jacob.util.StringUtil;

public class TextblockEditController
{
  private static String CURRENT_EDIT_CONTAINER = "74328672648549684576372445772";
  
  public static void startEdit(IClientContext context,ITabContainer container,IBrowser browser, IDataTableRecord textblockRecord) throws Exception
  {
    browser.setSelectedRecord(context,textblockRecord);

    container.setInputFieldValue("chapterEditorInformLongtext", textblockRecord.getSaveStringValue(Textblock.content));
    container.setInputFieldValue("chapterEditorTitelText", textblockRecord.getSaveStringValue(Textblock.title));
    container.setInputFieldValue("chapterEditorQuelleText", textblockRecord.getSaveStringValue(Textblock.source));
    container.setActivePane(context, 1);
    
    context.setPropertyForWindow(CURRENT_EDIT_CONTAINER,container);
  }
  
  public static boolean endEdit(IClientContext context) throws Exception
  {
    ITabContainer chapterContainer = (ITabContainer)context.getProperty(CURRENT_EDIT_CONTAINER);
    
    // not in edit mode
    if(chapterContainer==null)
      return true;
    
    IDataTransaction trans = context.getSelectedRecord().getCurrentTransaction();
    String value = chapterContainer.getInputFieldValue("chapterEditorInformLongtext");
    String titel = chapterContainer.getInputFieldValue("chapterEditorTitelText");
    String quelle = chapterContainer.getInputFieldValue("chapterEditorQuelleText");
    if(StringUtil.emptyOrNull(titel))
    {
      IGuiElement titelInput = chapterContainer.findByName("chapterEditorTitelText");
      titelInput.setErrorDecoration(context, "Pflichtfeld nicht ausgefüllt");
      context.createMessageDialog("Pflichtfelder nicht ausgefüllt").show();
      return false;
    }
    chapterContainer.setActivePane(context,0);
    TableListBox listbox = (TableListBox)chapterContainer.getParent().findByName("chapterTextblockListbox");
    IDataTableRecord record = listbox.getSelectedRecord(context);
    record.setValue(trans, Textblock.content, value);
    record.setValue(trans, Textblock.title, titel);
    record.setValue(trans, Textblock.source, quelle);
    listbox.refresh(context, record);
    // HACK
    listbox.resetCache();
    context.setPropertyForWindow(CURRENT_EDIT_CONTAINER,null);
    return true;
  }

  public static void cancelEdit(IClientContext context) throws Exception
  {
    ITabContainer chapterContainer = (ITabContainer)context.getProperty(CURRENT_EDIT_CONTAINER);
    TableListBox listbox = (TableListBox)chapterContainer.getParent().findByName("chapterTextblockListbox");
    IDataTableRecord record = listbox.getSelectedRecord(context);
    if(record.isNew())
    {
      listbox.remove(context, record);
      record.delete(record.getCurrentTransaction());
    }
    chapterContainer.setActivePane(context,0);
    context.setPropertyForWindow(CURRENT_EDIT_CONTAINER,null);
  }
}
