package jacob.event.ui.document;

import jacob.common.BoUtil;
import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Recyclebin;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

public class GenericDeleteDocumentButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: GenericDeleteDocumentButton.java,v 1.1 2010-09-17 08:42:23 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord documentRecord = context.getDataTable(Document.NAME).getSelectedRecord();
    IDataTableRecord boRecord = BoUtil.findByPkey(context, documentRecord.getStringValue(Document.pkey));
    
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      IDataTableRecord recycleRecord = Recyclebin.newRecord(context, trans);
      recycleRecord.setValue(trans, Recyclebin.content, documentRecord.getValue(Document.content));
      recycleRecord.setValue(trans, Recyclebin.name, documentRecord.getValue(Document.name));
      recycleRecord.setValue(trans, Recyclebin.original_path, BoUtil.calculatePath(context, boRecord));
      recycleRecord.setValue(trans, Recyclebin.type, boRecord.getValue(Bo.type));
      documentRecord.delete(trans);
      trans.commit();
      context.getDataBrowser().removeRecord(context.getGUIBrowser().getSelectedBrowserRecord(context));
      context.getGroup().clear(context, false);
    }
    finally
    {
      trans.close();
    }
  }
}
