/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Sep 29 15:31:40 CEST 2010
 */
package jacob.common.ui;

import jacob.common.MenutreeUtil;
import jacob.model.Article;
import jacob.model.Menutree;

import java.util.ArrayList;
import java.util.Collection;

import de.tif.jacob.core.Context;
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
 * @author andherz
 */
public class ArticleMenuReferenceListboxEventHandler extends IMutableListBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: ArticleMenuReferenceListboxEventHandler.java,v 1.2 2010-10-20 21:00:48 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  @Override
  public void onSelect(IClientContext context, IMutableListBox emitter) throws Exception
  {
    MenutreeUtil.show(context, emitter.getSelection()[0]);
  }

  private static Collection<IDataTableRecord> findByArticle(Context context, IDataTableRecord articleRecord) throws Exception
  {
    Collection<IDataTableRecord> result = new ArrayList<IDataTableRecord>();

    IDataAccessor acc = context.getDataAccessor().newAccessor();
    // alias Menutree (und nicht Active_menutree) nehmen, da wir in der
    // Admin-Sicht auch die gesperrten Einträge sehen wollen
    IDataTable menutreeTable = acc.getTable(Menutree.NAME);
    menutreeTable.setUnlimitedRecords();
    menutreeTable.qbeSetKeyValue(Menutree.article_key, articleRecord.getStringValue(Article.pkey));
    menutreeTable.search();
    for (int i = 0; i < menutreeTable.recordCount(); i++)
    {
      result.add(menutreeTable.getRecord(i));
    }
    return result;
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, IMutableListBox listBox) throws Exception
  {
    listBox.setEditable(false);
    listBox.setEnable(true);
    listBox.removeOptions();
    IDataTableRecord articleRecord = context.getSelectedRecord();
    if (articleRecord != null)
    {
      // Alle Verweise in den Menüs auf diesen Artikel suchen und in die Listbox
      // einfügen
      Collection<IDataTableRecord> menuItems = findByArticle(context, articleRecord);
      for (IDataTableRecord entry : menuItems)
      {
        boolean isLocked = MenutreeUtil.isLocked(context, entry);

        listBox.addOption(MenutreeUtil.calculatePath(entry), //
          entry.getStringValue(Menutree.pkey), //
          isLocked ? Icon.lock : Icon.folder_table, "");
      }
    }
  }
}
