/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Oct 29 18:23:48 CET 2009
 */
package jacob.event.ui.jan_queue;

import jacob.model.Jan_queue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 * 
 * @author andreas
 */
 public class Jan_queueUrlComplete extends ITextFieldEventHandler
 {
	static public final transient String RCS_ID = "$Id: Jan_queueUrlComplete.java,v 1.1 2009-10-29 20:36:41 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
	{
    // Note: because of downward compatibility -> fill optional urlcomplete
    // field
    IDataTableRecord record = context.getSelectedRecord();
    if (record != null && record.hasNullValue(Jan_queue.urlcomplete))
    {
      text.setValue(record.getSaveStringValue(Jan_queue.url));

      IDataTransaction trans = record.getCurrentTransaction();
      if (trans != null)
        record.setValue(trans, Jan_queue.urlcomplete, record.getValue(Jan_queue.url));
    }
	}
}
