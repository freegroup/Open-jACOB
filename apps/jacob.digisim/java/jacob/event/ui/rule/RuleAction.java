/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Dec 06 14:35:46 CET 2006
 */
package jacob.event.ui.rule;

import de.tif.jacob.core.Context;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class RuleAction extends ITextFieldEventHandler implements IAutosuggestProvider
 {
	static public final transient String RCS_ID = "$Id: RuleAction.java,v 1.1 2007/02/02 22:26:48 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	static final AutosuggestItem[] SUGGEST;
	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	static
	{
		SUGGEST = new AutosuggestItem[4];
		SUGGEST[0]= new AutosuggestItem("afterCommitAction","afterCommitAction");
		SUGGEST[1]= new AutosuggestItem("afterDeleteAction","afterDeleteAction");
		SUGGEST[2]= new AutosuggestItem("afterNewAction","afterNewAction");
		SUGGEST[3]= new AutosuggestItem("beforeCommitAction","beforeCommitAction");
	}
	
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IText emitter) throws Exception
	{
		// insert your code here
	}

	public AutosuggestItem[] suggest(Context context, String userInputFragment, int caretPosition) throws Exception 
	{
		return SUGGEST;
	}

	public void suggestSelected(IClientContext context, AutosuggestItem selectedEntry) throws Exception 
	{
	}
}
