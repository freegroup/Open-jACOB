/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Aug 14 17:47:12 CEST 2010
 */
package jacob.event.ui.article;

import jacob.model.Article;
import jacob.model.Chapter;
import jacob.model.Chapter01;
import jacob.relationset.ArticleRelationset;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.impl.html.Group;


/**
 * The event handler for the ArticleCreateArticleButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class AbstractCreateArticleButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: AbstractCreateArticleButton.java,v 1.3 2010-09-29 13:29:52 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
	  context.createAskDialog("Titel des Artikel", new IAskDialogCallback()
    {
      public void onOk(IClientContext context, String value) throws Exception
      {
        context.clearGroup();
        IDataTableRecord article = context.getGroup().newRecord(context);
        IDataTransaction trans = article.getCurrentTransaction();
        article.setValue(trans, Article.title, value);
        trans.commit();
        trans.close();

        // muss dummerweise in 2 Phase gemacht werden
        IDataTableRecord chapter = Chapter01.newRecord(context.getDataAccessor().newAccessor());
        trans = chapter.getCurrentTransaction();
        chapter.setValue(trans, Chapter.title, "Allgemein");
        chapter.setValue(trans, Chapter.article_key, article.getValue(Article.pkey));
        article.setValue(trans,Article.chapter01_key, chapter.getValue(Chapter01.pkey));
        trans.commit();
        trans.close();

        context.getGUIBrowser().add(context, article);
        context.getDataAccessor().propagateRecord(article,ArticleRelationset.NAME,Filldirection.BOTH);
        article = context.getSelectedRecord();
 
        trans = context.getDataAccessor().newTransaction();

        
        // bring the record in the update mode
        article.setValue(trans, Article.change_date, "now");
        // TODO: warum geht die Gruppe hier nicht sync. mit der Datenschicht?!
        ((Group)context.getGroup()).setDataStatus(context, IGroup.UPDATE);
      }
      
      public void onCancel(IClientContext context) throws Exception
      {
      }
    }).show();
	}

}
