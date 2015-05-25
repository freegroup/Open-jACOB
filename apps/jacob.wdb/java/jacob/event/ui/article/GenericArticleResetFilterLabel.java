/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 30 13:55:27 CEST 2010
 */
package jacob.event.ui.article;

import jacob.model.Article;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ILabelEventHandler;
import de.tif.jacob.screen.event.IOnClickEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class GenericArticleResetFilterLabel extends ILabelEventHandler  implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: GenericArticleResetFilterLabel.java,v 1.4 2010-10-20 21:00:48 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";


  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    context.getGUIBrowser().setEnableTreeView(context, true);

    IDataBrowser browser = context.getDataBrowser();
    
    context.getDataAccessor().qbeClearAll();
    IDataTable articleTable = context.getDataTable();
    articleTable.qbeClear();
    articleTable.qbeSetValue(Article.article_parent_key, "null");
    browser.search(IRelationSet.DEFAULT_NAME, Filldirection.BOTH);
    if(browser.recordCount()>0)
    {
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
    }
    context.getGroup().findByName("articleResetFilterLabel1").setVisible(false);
    context.getGroup().findByName("articleResetFilterLabel2").setVisible(false);
    context.getGroup().findByName("articleResetFilterLabel3").setVisible(false);
    context.getGroup().findByName("articleResetFilterLabel4").setVisible(false);
    
    context.showTransparentMessage("Filter / Suchkriterien zurückgesetzt");
  }

  
}
