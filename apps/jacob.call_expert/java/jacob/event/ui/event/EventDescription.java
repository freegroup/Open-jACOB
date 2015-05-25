/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Fri Nov 14 12:50:40 CET 2008
 */
package jacob.event.ui.event;

import jacob.common.AppLogger;
import jacob.model.Event;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.event.ITextFieldEventHandler;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 *
 * @author achim
 */
public class EventDescription extends ITextFieldEventHandler  implements IAutosuggestProvider
{
    public AutosuggestItem[] suggest(Context context, String userInputFragment, int caretPosition) throws Exception
    {


        if (userInputFragment == null || userInputFragment.length() == 0)
            return new AutosuggestItem[0];

        IDataTable event = context.getDataAccessor().newAccessor().getTable(Event.NAME);
        event.qbeSetValue(Event.description, userInputFragment);
        int count = event.search();
        Set eventtag = new HashSet();
        for (int i = 0; i < count; i++)
        {
            String n_tag = event.getRecord(i).getSaveStringValue(Event.description);
            if (!eventtag.contains(n_tag))
            {
                eventtag.add(n_tag);
            }
        }
        String[] distort = new String[eventtag.size()];

        eventtag.toArray(distort);

        AutosuggestItem[] items = new AutosuggestItem[eventtag.size()];
        for (int i = 0; i < eventtag.size(); i++)
        {

            items[i] = new AutosuggestItem(distort[i], null);
        }

        return items;

    }

    public void suggestSelected(IClientContext context, AutosuggestItem selectedEntry) throws Exception
    {
        //alert(selectedEntry.getLabel());
        IDataTable event = context.getDataAccessor().newAccessor().getTable(Event.NAME);
        event.qbeSetValue(Event.description, selectedEntry.getLabel());
        event.search();
        if (event.recordCount()>0)
        {

            String question = event.getRecord(0).getSaveStringValue(Event.detail);
            String answer = event.getRecord(0).getSaveStringValue(Event.answer);
            alert(question);
            //context.getSelectedRecord().setValue(context.getSelectedRecord().getCurrentTransaction(), Event.detail, question);
            context.getGroup().setInputFieldValue("eventDetail", question);
            context.getGroup().setInputFieldValue("eventAnswer", answer);
        }

    }

    static public final transient String RCS_ID = "$Id: EventDescription.java,v 1.2 2009/11/23 11:33:44 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

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
    public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
    {
        // insert your code here e.g.
        //
        // text.setEditable(state.equals(IGuiElement.NEW));
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
