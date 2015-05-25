/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Sep 29 17:32:06 CEST 2010
 */
package jacob.common;

import java.util.Locale;

import jacob.model.Article;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the ArticleShowInAdministrationButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class AbstractArticleShowInAdministrationButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: AbstractArticleShowInAdministrationButton.java,v 1.1 2010-09-29 15:56:34 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTableRecord articleRecord = context.getDataTable(Article.NAME).getSelectedRecord();
		if(articleRecord!=null)
		{
		  ArticleUtil.showInAdministration(context,articleRecord.getStringValue(Article.pkey));
		}
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		button.setVisible(context.getApplication().findByName("verwaltung_article")!=null);
	}
	
	public static void main(String[] args)
  {
    Locale l = new Locale("de",null);
    System.out.println(l);
  }
}

