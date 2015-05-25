/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 16:56:11 CEST 2010
 */
package jacob.event.ui.article;


import jacob.model.Article;
import jacob.model.Chapter;

import java.util.List;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;
import de.tif.jacob.screen.impl.html.GuiHtmlElement;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class ArticleCreateChapterImage extends IStaticImageEventHandler implements IOnClickEventHandler
{

  private final class CreateChapterCallback implements IAskDialogCallback
  {
    private final String alias;
    private final String field;
    private final IGuiElement parentContainer;

    public CreateChapterCallback(IGuiElement parentContainer, String alias, String field)
    {
      this.alias = alias;
      this.field = field;
      this.parentContainer = parentContainer;
    }

    public void onOk(IClientContext context, String value) throws Exception
    {
      IDataTableRecord article = context.getSelectedRecord();
      IDataTransaction trans = article.getCurrentTransaction();

      IDataTableRecord chapter = context.getDataTable(alias).newRecord(trans);
      chapter.setValue(trans, Chapter.title, value);
      chapter.setValue(trans, Chapter.article_key, article.getValue(Article.pkey));
      article.setLinkedRecord(trans, chapter);
      ITabContainer container= (ITabContainer)parentContainer.findByName("articleTextblockContainer");
      
      // Es ist jetzt mind. ein Tab vorhanden. Der TabStrip muss jetzt angezeigt werden
      container.hideTabStrip(false);
      // "Kein Inhalt hinterlegt" kann jetzt ausgeblendet werden
      container.getPane(0).setVisible(false);
      
      for (ITabPane pane: (List<ITabPane>) container.getPanes())
      {
        if(pane.getPaneTableAlias().equals(alias))
        {
          pane.setVisible(true);
          pane.setActive(context);
          break;
        }
      }
      /*
      ((GuiHtmlElement)container).resetCache();
      IGuiElement e= container.getParent();
      while(e!=null)
      {
        if(e instanceof GuiHtmlElement)
          ((GuiHtmlElement)e).resetCache();
        e = e.getParent();
      }
      */
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }
  }

  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
   IDataTableRecord article = context.getSelectedRecord();
   
   // ein freien slot im artikel finden wo man das neue chapter einfügen kann
   //
   String field_pre = "chapter0";
   String field_suf = "_key";
   String field = null;
   String alias = null;
   int i=6;
   for(;i>0;i--)
   {
     Object fkey = article.getValue(field_pre+i+field_suf);
     if(fkey!=null)
       break;
     field = field_pre+i+field_suf;
     alias = "chapter0"+i;
   }
   
   if(field==null)
     context.createMessageDialog("Artikel enthält bereits die maximal Anzahl von Kapitel. Keine weiteren Kapitel mehr möglich");
   
   context.createAskDialog("Kapitelüberschrift", new CreateChapterCallback(element.getParent(), alias, field)).show();
  }
}

