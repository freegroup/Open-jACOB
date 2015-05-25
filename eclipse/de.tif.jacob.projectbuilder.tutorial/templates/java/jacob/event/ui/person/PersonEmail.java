/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 11 19:57:56 CET 2006
 */
package jacob.event.ui.person;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.event.ITextFieldEventHandler;
import de.tif.jacob.screen.event.IAutosuggestProvider.AutosuggestItem;

import jacob.common.AppLogger;
import jacob.model.Person;

import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class PersonEmail extends ITextFieldEventHandler implements IAutosuggestProvider
 {
	static public final transient String RCS_ID = "$Id: PersonEmail.java,v 1.1 2007/11/25 22:19:39 freegroup Exp $";
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
	 * @param emitter The corresponding GUI element of this event handler
	 * 
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		// insert your code here
	}

	public AutosuggestItem[] suggest(Context context, String userInputFragment, int carePosition) throws Exception
	{
		if(userInputFragment==null || userInputFragment.length()==0)
			return new AutosuggestItem[0];

		IDataTable personTable = context.getDataTable(Person.NAME);
		personTable.qbeSetValue(Person.email,userInputFragment);
			
		int count = personTable.search();
		AutosuggestItem[] items = new AutosuggestItem[count];
		for(int i=0;i<count;i++)
		{
			items[i]= new AutosuggestItem(personTable.getRecord(i).getSaveStringValue(Person.email),personTable.getRecord(i));
		}
		return items;
	}

	public void suggestSelected(IClientContext context, AutosuggestItem selectedItem) throws Exception
	{
			context.getDataAccessor().propagateRecord((IDataTableRecord)selectedItem.getUserObj(),Filldirection.BOTH);
	}
}
