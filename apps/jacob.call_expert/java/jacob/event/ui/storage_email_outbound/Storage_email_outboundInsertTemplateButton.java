/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Jun 17 10:01:01 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;

import jacob.common.htmleditor.HTMLEditorHelper;
import jacob.model.Account;
import jacob.model.Project;
import jacob.model.Textmodule;
import jacob.relationset.ProjectLanguageRelationset;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.RecordLabelProvider;
import de.tif.jacob.screen.dialogs.IRecordTreeDialog;
import de.tif.jacob.screen.dialogs.IRecordTreeDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Storage_email_outboundGenericTestTreeButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author achim
 */
public class Storage_email_outboundInsertTemplateButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Storage_email_outboundInsertTemplateButton.java,v 1.2 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    static private class MyCallback implements IRecordTreeDialogCallback
    {
        final int caretPosition;
        final int selectionStart;
        final int selectionEnd;

        public MyCallback(int caretPosition, int selectionStart, int selectionEnd)
        {
            this.caretPosition = caretPosition;
            this.selectionStart = selectionStart;
            this.selectionEnd = selectionEnd;
        }

        public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
        {


            String alias = ((IDataTableRecord) record).getTableAlias().getName();
            if (alias.equals(Textmodule.NAME))
            {
                String textBlock = record.getSaveStringValue(Textmodule.textmodule);
                HTMLEditorHelper.insertTextBlock(context,caretPosition, selectionStart,selectionEnd, textBlock);
            }
        }
    }

    static private class MyLabelProvider extends RecordLabelProvider
    {
        public String getImage(IClientContext context, Object record)
        {
            String alias = ((IDataTableRecord) record).getTableAlias().getName();
            if (alias.equals(Textmodule.NAME))
            {
                return "texticon";
            }
            else
            {
                return "folder";
            }
        }
    }



    /**
     * The user has clicked on the corresponding button.<br>
     * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
     *             if the button has not the [selected] flag.<br>
     *             The selected flag assures that the event can only be fired,<br>
     *             if <code>selectedRecord!=null</code>.<br>
     *
     * @param context The current client context
     * @param button  The corresponding button to this event handler
     * @throws Exception
     */
    public void onClick(IClientContext context, IGuiElement emitter) throws Exception
    {
        IInFormLongText textarea = (IInFormLongText) context.getGroup().findByName("storage_email_outboundText_body");
        int pos =  textarea.getCaretPosition();
        int start =  textarea.getSelectionStart();
        int end = textarea.getSelectionEnd();

        IDataTable project = context.getDataTable(Project.NAME);

        project.qbeSetKeyValue(Project.application, context.getApplicationDefinition().getName());
        project.search();
        if (project.recordCount()>0)
        {
            IDataTable account = context.getDataTable(Account.NAME);
            account.qbeSetKeyValue(Account.pkey, project.getRecord(0).getValue(Project.account_key));
            account.search();
            IDataTableRecord rootRecord = account.getRecord(0);
            IRelationSet relationSet = context.getApplicationDefinition().getRelationSet(ProjectLanguageRelationset.NAME);
            IRecordTreeDialog dialog=context.createRecordTreeDialog(emitter, rootRecord, relationSet, Filldirection.BOTH,new MyCallback(pos, start,end));
            dialog.setLabelProvider(new MyLabelProvider());
            dialog.show();
        }
        else
        {
            alert("Display a nice internationalised Message, that says that there is nothing");
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
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
    {
        emitter.setEnable(status.equals(IGroup.NEW)||status.equals(IGroup.UPDATE));
    }
}
