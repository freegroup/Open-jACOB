/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 08 18:01:06 CEST 2010
 */
package jacob.event.ui.globalcontent;

import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.Globalcontent;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class GlobalcontentContainer extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: GlobalcontentContainer.java,v 1.1 2010-09-17 08:42:25 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
  {
    element.hideTabStrip(true);
    element.setActivePane(context, 0);
    
    IDataTableRecord record = context.getSelectedRecord();
    if(record!=null)
    {
      String alias = record.getSaveStringValue(Globalcontent.tablealias);
      IDataKeyValue pkey = context.getDataTable(alias).getTableAlias().getTableDefinition().getPrimaryKey().convertStringToKeyValue(record.getSaveStringValue(Globalcontent.primarykey));
      IDataTable table = context.getDataTable(alias);
      table.setSelectedRecord(pkey);
      if(alias.equals(Folder.NAME))
        element.setActivePane(context,1);
      else if(alias.equals(Document.NAME))
        element.setActivePane(context,2);
    }
  }
}
