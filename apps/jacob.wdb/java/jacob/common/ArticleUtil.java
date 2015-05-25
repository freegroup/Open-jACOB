package jacob.common;

import jacob.model.Active_menutree;
import jacob.model.Article;
import jacob.relationset.ArticleRelationset;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.html.JacobGroup;

public class ArticleUtil
{
  public static long count(Context context) throws Exception
  {
    IDataTable table = context.getDataAccessor().newAccessor().getTable(Article.NAME);
    return table.count();
  }

  public static long childrenCount(Context context, String parentPkey) throws Exception
  {
    IDataTable table = context.getDataAccessor().newAccessor().getTable(Article.NAME);
    table.qbeSetKeyValue(Article.article_parent_key, parentPkey);
    
    return table.count();
  }

  public static IDataTableRecord findByPkey(Context context, String pkey) throws Exception
  {
    if(pkey==null)
      return null;
    
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Article.NAME);
    boTable.qbeSetKeyValue(Article.pkey, pkey);
    boTable.search(IRelationSet.LOCAL_NAME);
    return boTable.getSelectedRecord();
  }

  public static String calculatePath(IDataTableRecord articleRecord) throws Exception
  {
    if (articleRecord == null)
      return null;

    IDataKeyValue parentPKey = articleRecord.getKeyValue("article_parent_FKey");
    if (parentPKey == null)
      return " / " + articleRecord.getSaveStringValue(Article.title);

    // get the data table of the same alias and get the parent record (might be
    // already cached)
    return calculatePath(articleRecord.getTable().getRecord(parentPKey)) + " / " + articleRecord.getSaveStringValue(Article.title);
  }

  public static boolean exists(Context context, String pkey) throws Exception
  {
    if(pkey==null)
      return false;
    
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Article.NAME);
    boTable.qbeSetKeyValue(Article.pkey, pkey);
    return boTable.count(IRelationSet.LOCAL_NAME)>0;
  }
  
  public static void showInAdministration(IClientContext context, String pkey) throws Exception
  {
    context.setCurrentForm("verwaltung","verwaltung_article");
    context.getForm().getCurrentBrowser().add(context, findByPkey(context, pkey), true);
  }
  
  public static void show(IClientContext context, String pkey) throws Exception
  {
    context.setCurrentForm("common", "common_search");
    IDataAccessor acc = context.getDataAccessor();
    acc.clear();
    IDataTable articleTable = context.getDataTable(Article.NAME);
    articleTable.qbeSetKeyValue(Article.pkey, pkey);
    articleTable.search(ArticleRelationset.NAME);
    if(articleTable.getSelectedRecord()!=null)
    {
      // Falls dieser Code durch einen EntryPoint aufgerufen wurde, dann ist keine "auslösende" Gruppe
      // bekannt., Diese muss manuell gesetzt werden.
      JacobGroup group = ((JacobGroup)context.getForm().findByName("globalcontentGroup"));
      ((HTTPClientContext)context).setGroup(group);
      
      acc.propagateRecord(articleTable.getSelectedRecord(), Filldirection.BOTH);
      ITabContainer container = (ITabContainer)context.getForm().findByName("globalcontentContainer");
      // Bei einem Entrypoint wird der Datastatus nach dem durchlaufen des Entrypoint anhand des selktierten Rekord ermittelt.
      // Da hier allerdings die Suchmaske verwendet wrid ist kein Rekord selektiert sondern nur die entsprechenden TaPPanes gefüllt.
      // => Datastatus jetzt manuell setzten damit der später durchlaufene "onGroupStatusChanged" nicht wieder den leeren TabPane
      // anzeigt.
      group.setDataStatus(context, IGroup.SEARCH);
      
      // TabPane mit dem Artikel anziegen. Anmerkung: Es ist KEIN globalcontent Record zurückgefüllt. Somit ist der DataStatus==SEARCH
      container.setActivePane(context, 1);
    }
  }

  public static boolean isLinkedInAnyMenu(Context context, IDataTableRecord articleRecord) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Active_menutree.NAME);
    boTable.qbeSetKeyValue(Active_menutree.article_key, articleRecord.getStringValue(Article.pkey));
    return boTable.count(IRelationSet.LOCAL_NAME)>0;
  }

}
