package jacob.event.ui.request;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Aug 26 15:20:31 CEST 2005
 *
 */
import jacob.event.data.Rcs_branchTableRecord;
import jacob.model.Rcs_branch;
import jacob.model.Request;
import jacob.model.RequestCategory;
import jacob.model.Requestbranch;
import jacob.resources.I18N;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.security.IUser;

/**
 * This is an event handler for a update button.
 * 
 * @author andherz
 * 
 */
public class RequestUpdate extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RequestUpdate.java,v 1.7 2009-01-30 22:09:58 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  /**
   * This event handler will be called if the corresponding button has been
   * pressed. You can prevent the execution of the update action if you return
   * [false].<br>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action
   *         else return [true]
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    GroupState state = context.getGroup().getDataStatus();
    if (state == IGuiElement.UPDATE || state == IGuiElement.NEW)
    {
      // Mandanten dürfen die Organization nicht ändern!
      IUser user = context.getUser();
      if (user.getMandatorId() != null)
      {
        // hat sich der Mandant geändert?
        if (!user.getMandatorId().equals(context.getSelectedRecord().getStringValue("organization_key")))
        {
          alert(I18N.MESSAGE_REQUESTUPDATE_ORGCHANGENOTALLOWED.get(context));
          
          // Mandant zurücksetzen
          IDataTable organization = context.getDataTable("organization");
          organization.qbeClear();
          organization.qbeSetValue("pkey", user.getMandatorId());
          organization.search();
        }
      }

      String status = ((ISingleDataGuiElement) context.getGroup().findByName("requestRequeststatus")).getValue();
      IDataTable tester = context.getDataTable("tester");
      IDataTable owner = context.getDataTable("owner");

      if (Request.state_ENUM._QA.equals(status) && tester.getSelectedRecord() == null)
      {
        context.createMessageDialog(I18N.MESSAGE_REQUESTUPDATE_NOTESTER.get(context)).show();
        return false;
      }
      else if (!Request.state_ENUM._New.equals(status) && owner.getSelectedRecord() == null)
      {
        context.createMessageDialog(I18N.MESSAGE_REQUESTUPDATE_NOOWNER.get(context)).show();
        return false;
      }
      
      // Im Status "inProgress" oder "QA" prüfen ob ein Branch gelinkt ist
      //
      IDataTableRecord requestRecord = context.getSelectedRecord();
      boolean statusChange = !requestRecord.getOldSaveStringValue(Request.state).equals(status);
      if (statusChange && (Request.state_ENUM._QA.equals(status) || Request.state_ENUM._In_progress.equals(status)))
      {
        // if the assigned category has no RCS project assigned, no branches could be defined anyhow
        IDataTableRecord categoryRecord = requestRecord.getLinkedRecord(RequestCategory.NAME);
        if (categoryRecord.hasNullValue(RequestCategory.project_key))
        {
          return true;
        }
        
        IDataTable requestBranchTable = context.getDataAccessor().getTable(Requestbranch.NAME);
        
        // are there already branches linked?
        if (!requestBranchTable.exists(Requestbranch.request_key, requestRecord.getValue(Request.pkey)))
        {
          // no
          
          // add HEAD branch to request
          //
          IDataTable branchTable = context.getDataAccessor().getTable(Rcs_branch.NAME);
          branchTable.qbeClear();
          branchTable.qbeSetValue(Rcs_branch.status, Rcs_branch.status_ENUM._Open);
          branchTable.qbeSetValue(Rcs_branch.version, Rcs_branchTableRecord.HEAD);
          branchTable.qbeSetValue(Rcs_branch.rcs_project_key, categoryRecord.getValue(RequestCategory.project_key));
          if (branchTable.search() == 1)
          {
            IDataTransaction transaction = requestRecord.getCurrentTransaction();
            if (transaction != null)
            {
              IDataTableRecord rb_Rec = requestBranchTable.newRecord(transaction);
              rb_Rec.setValue(transaction, Requestbranch.request_key, requestRecord.getValue(Request.pkey));
              rb_Rec.setValue(transaction, Requestbranch.rcs_branch_key, branchTable.getSelectedRecord().getValue(Rcs_branch.pkey));
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * This event method will be called if the update action has been successfully
   * done.<br>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button)
  {
    // the record has been successfull switch to the update mode or has been
    // successfull saved
    //
  }

  /**
   * The event handle if the status of the group has been changed. This is a
   * good place to enable/disable the button on relation to the group state.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresbonding button to this event handler
   * 
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
  {
    // you can enable/disable the update button
    //
    // button.setEnable(true/false);
  }
}
