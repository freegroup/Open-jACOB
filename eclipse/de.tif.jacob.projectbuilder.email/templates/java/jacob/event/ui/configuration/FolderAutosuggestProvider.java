package jacob.event.ui.configuration;

import jacob.common.AppLogger;
import jacob.model.Folder;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

public class FolderAutosuggestProvider extends ITextFieldEventHandler implements IAutosuggestProvider 
{
	static public final transient String RCS_ID = "$Id: FolderAutosuggestProvider.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
	}

	public AutosuggestItem[] suggest(Context context, String userInputFragment, int caretPosition)throws Exception 
	{
		// use a new DataAccessor to avoid a side-effect with the UI
		//
		IDataTable folderTable = context.getDataAccessor().newAccessor().getTable(Folder.NAME);
		folderTable.qbeSetValue(Folder.name,userInputFragment);
		folderTable.search();
		AutosuggestItem[] items = new AutosuggestItem[folderTable.recordCount()];
		for (int i = 0; i < items.length; i++) 
		{
			String folderName = folderTable.getRecord(i).getSaveStringValue(Folder.name);
			items[i] = new AutosuggestItem(folderName,folderName);
		}
		return items;
	}

	public void suggestSelected(IClientContext context, AutosuggestItem selectedEntry) throws Exception 
	{
		// do nothing
	}
}
