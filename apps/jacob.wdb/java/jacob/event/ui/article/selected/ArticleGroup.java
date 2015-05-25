/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Sep 29 15:58:40 CEST 2010
 */
package jacob.event.ui.article.selected;


import jacob.common.AppLogger;
import jacob.common.ArticleUtil;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabPaneEventListener;



/**
 *
 * @author andherz
 */
public class ArticleGroup extends ITabPaneEventListener
{
	static public final transient String RCS_ID = "$Id: ArticleGroup.java,v 1.1 2010-09-29 14:03:38 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

   /*
    * This event method will be called, if the status of the corresponding group
    * has been changed. Derived event handlers could overwrite this method, e.g.
    * to enable/disable GUI elements in relation to the group state. <br>
    * Possible group state values are defined in
    * {@link IGuiElement}:<br>
    * <ul>
    *     <li>{@link IGuiElement#UPDATE}</li>
    *     <li>{@link IGuiElement#NEW}</li>
    *     <li>{@link IGuiElement#SEARCH}</li>
    *     <li>{@link IGuiElement#SELECTED}</li>
    * </ul>
    * 
    * @param context
    *          The current client context
    * @param state
    *          The new group state
    * @param pane
    *          The corresponding GUI element to this event handler
    */
    public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
    {
      IDataTableRecord articleRecord = context.getSelectedRecord();
      pane.setVisible(articleRecord!=null && ArticleUtil.isLinkedInAnyMenu(context, articleRecord));
    }
}
