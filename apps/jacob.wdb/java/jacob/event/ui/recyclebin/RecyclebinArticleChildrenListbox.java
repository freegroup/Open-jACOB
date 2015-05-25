/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 20 11:33:48 CEST 2010
 */
package jacob.event.ui.recyclebin;

import jacob.model.Article_no_condition;
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
public class RecyclebinArticleChildrenListbox extends IMutableListBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: RecyclebinArticleChildrenListbox.java,v 1.1 2010-10-20 21:00:49 sonntag Exp $";
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

  private static void collectChildren(Collection<Child> children, IDataTableRecord articleRecord, String rootPath) throws Exception
  {
    IDataAccessor acc = articleRecord.getAccessor().newAccessor();
    IDataTable table = acc.getTable(Article_no_condition.NAME);
    table.qbeClear();
    table.setUnlimitedRecords();
    table.qbeSetKeyValue(Article_no_condition.article_parent_key, articleRecord.getValue(Article_no_condition.pkey));
    table.qbeSetKeyValue(Article_no_condition.lifecycle, Article_no_condition.lifecycle_ENUM._recyclebin2);
    table.search();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord childRecord = table.getRecord(i);
      String path = rootPath + childRecord.getValue(Article_no_condition.title);
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
    if (recyclebinRecord != null && Recyclebin.object_type_ENUM._article.equals(recyclebinRecord.getStringValue(Recyclebin.object_type)))
    {
      IDataTable table = context.getDataTable(Article_no_condition.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Article_no_condition.pkey, recyclebinRecord.getValue(Recyclebin.object_pkey));
      table.search();
      IDataTableRecord articleRecord = table.getSelectedRecord();
      if (articleRecord != null)
      {
        Collection<Child> children = new TreeSet<Child>();
        collectChildren(children, articleRecord, "");
        for (Child child : children)
        {
          listBox.addOption(child.path, //
            child.record.getStringValue(Article_no_condition.pkey), //
            Icon.table, "");
        }
      }
    }
  }
}
