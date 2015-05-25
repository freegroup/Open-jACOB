/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Dec 06 14:35:38 CET 2006
 */
package jacob.event.ui.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
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
 public class RuleTablealias extends ITextFieldEventHandler implements IAutosuggestProvider
 {
	static public final transient String RCS_ID = "$Id: RuleTablealias.java,v 1.1 2007/02/02 22:26:48 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

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
		List result = new ArrayList();
		IApplicationDefinition app = context.getApplicationDefinition();
		Iterator aliasIter = app.getTableAliases().iterator();
		while(aliasIter.hasNext()) 
		{
			ITableAlias alias = (ITableAlias)aliasIter.next();
			if(alias.getName().startsWith(userInputFragment))
				result.add(new AutosuggestItem(alias.getName(),alias.getName()));
		}
		return (AutosuggestItem[])result.toArray(new AutosuggestItem[0]);
	}

	public void suggestSelected(IClientContext context, AutosuggestItem selectedEntry) throws Exception 
	{
	}
	
	
}
