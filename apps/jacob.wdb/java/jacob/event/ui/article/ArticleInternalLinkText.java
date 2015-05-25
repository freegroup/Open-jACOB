/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 28 11:54:04 CEST 2010
 */
package jacob.event.ui.article;

import jacob.model.Article;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;
import de.tif.jacob.util.StringUtil;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 * 
 * @author andherz
 */
 public class ArticleInternalLinkText extends ITextFieldEventHandler // implements IAutosuggestProvider, IHotkeyEventHandler
 {
	static public final transient String RCS_ID = "$Id: ArticleInternalLinkText.java,v 1.2 2010-10-21 06:28:10 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	@Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
  {
    IDataTableRecord articleRecord = context.getSelectedRecord();
    if (articleRecord != null)
    {
      text.setValue("[[artikel:" + articleRecord.getStringValue(Article.pkey) + "|" + StringUtil.abbreviate(articleRecord.getStringValue(Article.title), 60) + "]]");
    }
    text.setEditable(false);
  }
  
  /**
   * Eventhandler for hot keys like ENTER.
   * You must implement the interface "HotkeyEventHandler" if you want receive this events.
   * 
  public void keyPressed(IClientContext context, KeyEvent e)
  {
    System.out.println("pressed");
  }
  
  public int getKeyMask(IClientContext context)
  {
    return KeyEvent.VK_ENTER;
  }
  */
  
}
