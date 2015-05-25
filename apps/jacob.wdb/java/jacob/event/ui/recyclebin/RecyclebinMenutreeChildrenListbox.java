/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 20 00:38:19 CEST 2010
 */
package jacob.event.ui.recyclebin;

import jacob.model.Menutree_no_condition;
import jacob.model.Recyclebin;

import java.util.Collection;
import java.util.TreeSet;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IMutableListBox;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableListBoxEventHandler;

/**
 * 
 * @author andreas
 */
public final class RecyclebinMenutreeChildrenListbox extends IMutableListBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: RecyclebinMenutreeChildrenListbox.java,v 1.1 2010-10-20 21:00:49 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  @Override
  public void onSelect(IClientContext context, IMutableListBox emitter) throws Exception
  {
    // no action
  }

  private static final class Child implements Comparable<Child>
  {
    private final String path;
    private final IDataTableRecord record;

    private Child(String path, IDataTableRecord record)
    {
      this.path = path;
      this.record = record;
    }

    public int compareTo(Child o)
    {
      return this.path.compareTo(o.path);
    }
  }

  private static void collectChildren(Collection<Child> children, IDataTableRecord menutreeRecord, String rootPath) throws Exception
  {
    IDataAccessor acc = menutreeRecord.getAccessor().newAccessor();
    IDataTable table = acc.getTable(Menutree_no_condition.NAME);
    table.qbeClear();
    table.setUnlimitedRecords();
    table.qbeSetKeyValue(Menutree_no_condition.menutree_parent_key, menutreeRecord.getValue(Menutree_no_condition.pkey));
    table.qbeSetKeyValue(Menutree_no_condition.lifecycle, Menutree_no_condition.lifecycle_ENUM._recyclebin2);
    table.search();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord childRecord = table.getRecord(i);
      String path = rootPath + childRecord.getValue(Menutree_no_condition.title);
      children.add(new Child(path, childRecord));
      collectChildren(children, childRecord, path + " / ");
    }
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, IMutableListBox listBox) throws Exception
  {
    listBox.setEditable(false);
    listBox.setEnable(true);
    listBox.removeOptions();
    IDataTableRecord recyclebinRecord = context.getSelectedRecord();
    if (recyclebinRecord != null && Recyclebin.object_type_ENUM._menutree.equals(recyclebinRecord.getStringValue(Recyclebin.object_type)))
    {
      IDataTable table = context.getDataTable(Menutree_no_condition.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Menutree_no_condition.pkey, recyclebinRecord.getValue(Recyclebin.object_pkey));
      table.search();
      IDataTableRecord menutreeRecord = table.getSelectedRecord();
      if (menutreeRecord != null)
      {
        Collection<Child> children = new TreeSet<Child>();
        collectChildren(children, menutreeRecord, "");
        for (Child child : children)
        {
          listBox.addOption(child.path, //
            child.record.getStringValue(Menutree_no_condition.pkey), //
            Icon.folder, "");
        }
      }
    }
  }
}
