/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 27 17:39:42 CEST 2010
 */
package jacob.event.ui.document.search;

import jacob.common.BoUtil;
import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.Globalcontent;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class DocumentPathLabel extends ILabelEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: DocumentPathLabel.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  /**
   * Will be called, if the user selects a record or presses the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    IDataTableRecord record = context.getSelectedRecord();
    if(record!=null)
    {
      String alias = record.getSaveStringValue(Globalcontent.tablealias);
      if(alias.equals(Document.NAME))
      {
        IDataKeyValue pkey = context.getDataTable(alias).getTableAlias().getTableDefinition().getPrimaryKey().convertStringToKeyValue(record.getSaveStringValue(Globalcontent.primarykey));
        IDataTable table = context.getDataTable(alias);
        IDataTableRecord searchRecord = table.loadRecord(pkey);
        label.setLabel(BoUtil.calculatePath(context, BoUtil.findByPkey(context, searchRecord.getStringValue(Document.pkey))));
      }
    }
  }

}
