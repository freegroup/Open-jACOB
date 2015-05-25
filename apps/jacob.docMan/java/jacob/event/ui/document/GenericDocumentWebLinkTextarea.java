/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 06 13:52:12 CEST 2010
 */
package jacob.event.ui.document;

import java.util.Properties;

import jacob.common.DocumentUtil;
import jacob.entrypoint.cmd.Download;
import jacob.model.Document;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.entrypoint.CmdEntryPointManager;
import de.tif.jacob.entrypoint.EntryPointUrl;
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
 public class GenericDocumentWebLinkTextarea extends ITextFieldEventHandler
 {
	static public final transient String RCS_ID = "$Id: GenericDocumentWebLinkTextarea.java,v 1.1 2010-09-17 08:42:22 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


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
	public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
	{
		text.setEditable(true);
		IDataTableRecord documentRecord = context.getDataTable(Document.NAME).getSelectedRecord();
		if(documentRecord==null)
		  text.setValue("");
		else
		  text.setValue(DocumentUtil.getUrl(context, documentRecord));
	}
  
  
}
