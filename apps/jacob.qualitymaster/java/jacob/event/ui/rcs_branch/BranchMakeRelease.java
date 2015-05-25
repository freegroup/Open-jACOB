/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Feb 02 15:32:09 CET 2006
 */
package jacob.event.ui.rcs_branch;

import jacob.event.data.Rcs_branchTableRecord;
import jacob.model.Rcs_branch;
import jacob.model.Request;
import jacob.model.Requestbranch;
import jacob.resources.I18N;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the BranchMakeRelease record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class BranchMakeRelease extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: BranchMakeRelease.java,v 1.5 2009-01-30 22:09:58 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  private static class RequesttagCallBack implements IFormDialogCallback
  {
    private final IDataTableRecord record;

    private RequesttagCallBack(IDataTableRecord record)
    {
      this.record = record;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext,
     *      java.lang.String, java.util.Map)
     */
    public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
    {
      // validate version
      //
      String version = (String) formValues.get("version");
      Pattern pattern = Pattern.compile("((\\d+)(\\.\\d+){0,2})");
      Matcher matcher = pattern.matcher(version);
      if (!matcher.matches())
        throw new UserException(new ApplicationMessage("BranchMakeRelease.IllegalVersion"));
      version = normalizeVersion(version);

      // calculate default tag
      //
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      boolean isFromHead = Rcs_branchTableRecord.HEAD.equals(this.record.getValue(Rcs_branch.version));
      String tag = "R_" + format.format(new Date()) + "_" + (isFromHead ? "V" : "BV") + version.replace('.', '_');

      // and ask user to responded
      //
      FormLayout layout = new FormLayout("10dlu,p,6dlu,300dlu,10dlu", // columns
          "6dlu,20dlu,6dlu"); // rows
      IFormDialog dialog = context.createFormDialog(ApplicationMessage.getLocalized("BranchMakeRelease.MakeRelease"), layout, new CreateReleaseCallBack(record, version));
      CellConstraints cc = new CellConstraints();
      dialog.addLabel("RCS Tag", cc.xy(1, 1));
      dialog.addTextField("tag", tag, cc.xy(3, 1));
      dialog.addSubmitButton("Submit", ApplicationMessage.getLocalized("BranchMakeRelease.Create"));
      dialog.setCancelButton(ApplicationMessage.getLocalized("BranchMakeRelease.Cancel"));
      dialog.show();
    }
  }

  /**
   * Normalizes version string.
   * <p>
   * Example:<br>
   * <li><code>"2.01"</code> gets <code>"2.1"</code>
   * <li><code>"04"</code> gets <code>"4.0"</code>
   * 
   * @param version
   * @return
   */
  private static String normalizeVersion(String version)
  {
    int major = 0;
    int minor = 0;
    int fix = 0;

    int i = 0;
    while (i < version.length())
    {
      char c = version.charAt(i++);
      if (c == '.')
        break;
      major = major * 10 + (c - '0');
    }
    while (i < version.length())
    {
      char c = version.charAt(i++);
      if (c == '.')
        break;
      minor = minor * 10 + (c - '0');
    }
    while (i < version.length())
    {
      char c = version.charAt(i++);
      fix = fix * 10 + (c - '0');
    }
    return fix == 0 ? major + "." + minor : major + "." + minor + "." + fix;
  }

  private static class CreateReleaseCallBack implements IFormDialogCallback
  {
    private final IDataTableRecord record;
    private final String version;

    private CreateReleaseCallBack(IDataTableRecord record, String version)
    {
      this.record = record;
      this.version = version;
    }

    public void linkOpenRequestsToHead(IDataTransaction trans, IDataTableRecord headRecord) throws Exception
    {
      IDataAccessor accessor = headRecord.getAccessor().newAccessor();

      IDataTable requestBranch = accessor.getTable(Requestbranch.NAME);
      requestBranch.qbeSetKeyValue(Requestbranch.rcs_branch_key, record.getValue(Rcs_branch.pkey));
      //
      String statusConstraint = Request.state_ENUM._New + "|" + Request.state_ENUM._Proved;
      IDataTable requestTable = accessor.getTable(Request.NAME);
      requestTable.qbeSetValue(Request.state, statusConstraint);

      requestBranch.search("requestRelationset");
      String headKey = headRecord.getSaveStringValue("pkey");
      int recCount = requestBranch.recordCount();
      IDataTableRecord[] rb_rec = new IDataTableRecord[recCount];

      // die betroffenen Datensätze merken
      for (int i = 0; i < recCount; i++)
      {
        rb_rec[i] = requestBranch.getRecord(i);
      }

      for (int i = 0; i < recCount; i++)
      {
        // delete the old one and create a new linked to headRecord
        String requestKey = rb_rec[i].getSaveStringValue(Requestbranch.request_key);

        rb_rec[i].delete(trans);
        IDataTableRecord new_rbRec = requestBranch.newRecord(trans); // alias.newRecord
        // zerstört
        // die
        // vorherige
        // Suche
        new_rbRec.setValue(trans, Requestbranch.request_key, requestKey);
        new_rbRec.setValue(trans, Requestbranch.rcs_branch_key, headKey);
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext,
     *      java.lang.String, java.util.Map)
     */
    public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
    {
      String rcs_tag = (String) formValues.get("tag");

      IDataTransaction trans = record.getTable().startNewTransaction();
      try
      {
        // Attention: the original record must be modified first, otherwise we
        // will get a unique constraint violation from the database.
        record.setValue(trans, Rcs_branch.status, Rcs_branch.status_ENUM._Release);

        IDataAccessor accessor = context.getDataAccessor();
        IDataTableRecord clonedRecord = accessor.cloneRecord(trans, record, Rcs_branch.NAME);
        clonedRecord.setValue(trans, Rcs_branch.status, Rcs_branch.status_ENUM._Open);

        record.setValue(trans, Rcs_branch.version, version);
        record.setValue(trans, Rcs_branch.versionnbr, versionToNbr(version));
        record.setValue(trans, Rcs_branch.rcs_tag, rcs_tag);

        linkOpenRequestsToHead(trans, clonedRecord);
        trans.commit();
        accessor.propagateRecord(record, Filldirection.BACKWARD);
      }
      finally
      {
        trans.close();
      }
    }
  }

  /**
   * Converts version to version number.
   * <p>
   * Example: <code>"2.5.4"</code> gets <code>2.005004</code>
   * 
   * @param version
   * @return
   */
  private static BigDecimal versionToNbr(String version)
  {
    int major;
    int minor = 0;
    int fix = 0;

    int idx = version.indexOf('.');
    if (-1 == idx)
    {
      major = Integer.parseInt(version);
    }
    else
    {
      major = Integer.parseInt(version.substring(0, idx));
      version = version.substring(idx + 1);
      idx = version.indexOf('.');
      if (-1 == idx)
      {
        minor = Integer.parseInt(version);
      }
      else
      {
        minor = Integer.parseInt(version.substring(0, idx));
        fix = Integer.parseInt(version.substring(idx + 1));
      }
    }

    return new BigDecimal(Integer.toString(1000 * (1000 * major + minor) + fix)).movePointLeft(6);
  }

  private static class ConfirmReleaseForUntestedCodeCallback implements IOkCancelDialogCallback
  {
    private final IDataTableRecord branchRecord;

    private ConfirmReleaseForUntestedCodeCallback(IDataTableRecord branchRecord)
    {
      this.branchRecord = branchRecord;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext context) throws Exception
    {
      IOkCancelDialog dialog = context.createOkCancelDialog(I18N.MESSAGE_BRANCHMAKERELEASE_CHECKUNTESTEDCODE.get(context), new CheckRequestsCallback(branchRecord.getValue(Rcs_branch.pkey), false));
      dialog.show();
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onOk(de.tif.jacob.screen.IClientContext)
     */
    public void onOk(IClientContext context) throws Exception
    {
      askVersion(context, branchRecord);
    }
  }
  
  private static class CheckRequestsCallback implements IOkCancelDialogCallback
  {
    private final Object branchKey;
    private final boolean showInProgressOnly;

    private CheckRequestsCallback(Object branchKey, boolean showInProgressOnly)
    {
      this.branchKey = branchKey;
      this.showInProgressOnly = showInProgressOnly;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext context) throws Exception
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onOk(de.tif.jacob.screen.IClientContext)
     */
    public void onOk(IClientContext context) throws Exception
    {
      context.setCurrentForm("qualitymaster", "request");
      // Da die Domain/Form gewechselt wurde (setForm(...)), arbeitet man ab jetzt
      // auch in dem Context dieser Form und kann sich am Context dann von dieser neuen Domain/Form
      // geben lassen.
      //
      IDataAccessor accessor = context.getDataAccessor();
      accessor.clear();

      // set the constraints and search
      //
      accessor.getTable(Requestbranch.NAME).qbeSetKeyValue(Requestbranch.rcs_branch_key, branchKey);
      String statusConstraint = this.showInProgressOnly //
          ? Request.state_ENUM._In_progress //
          : Request.state_ENUM._In_progress + "|" + Request.state_ENUM._QA;
      IDataTable requestTable = accessor.getTable(Request.NAME);
      requestTable.qbeSetValue(Request.state, statusConstraint);

      IDataBrowser requestBrowser = accessor.getBrowser("requestBrowser");
      requestBrowser.search("requestRelationset");

      // propagate if only one
      if (requestBrowser.recordCount() == 1)
      {
        requestBrowser.setSelectedRecordIndex(0);
        requestBrowser.propagateSelections();
      }
    }
  }

  private boolean isAllowed(IClientContext context, IDataTableRecord branchRecord) throws Exception
  {
    Object branchKey = branchRecord.getValue(Rcs_branch.pkey);
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    accessor.getTable(Requestbranch.NAME).qbeSetKeyValue(Requestbranch.rcs_branch_key, branchKey);
    String statusConstraint = Request.state_ENUM._In_progress + "|" + Request.state_ENUM._QA;
    IDataTable requestTable = accessor.getTable(Request.NAME);
    IDataBrowser requestBrowser = accessor.getBrowser("requestBrowser");
    requestTable.qbeSetValue(Request.state, statusConstraint);
    requestBrowser.search("requestRelationset", Filldirection.BOTH);

    if (requestBrowser.recordCount() != 0)
    {
      // does there exist unfinished code, i.e. at least one request in state in_progress?
      //
      for (int i=0; i<requestBrowser.recordCount();i++)
      {
        if (Request.state_ENUM._In_progress.equals(requestBrowser.getRecord(i).getStringValue("browserStatus")))
        {
          IOkCancelDialog dialog = context.createOkCancelDialog(I18N.MESSAGE_BRANCHMAKERELEASE_CHECKUNFINISHEDCODE.get(context), new CheckRequestsCallback(branchKey, true));
          dialog.show();
          return false;
        }
      }
      
      IOkCancelDialog dialog = context.createOkCancelDialog(I18N.MESSAGE_BRANCHMAKERELEASE_RELEASEUNTESTEDCODE.get(context), new ConfirmReleaseForUntestedCodeCallback(branchRecord));
      dialog.show();
      return false;
    }

    return true;
  }
  
  private static void askVersion(IClientContext context, IDataTableRecord record)
  {
    FormLayout layout = new FormLayout("10dlu,p,6dlu,200dlu,10dlu", // columns
        "6dlu,20dlu,6dlu"); // rows
    IFormDialog dialog = context.createFormDialog(ApplicationMessage.getLocalized("BranchMakeRelease.MakeRelease"), layout, new RequesttagCallBack(record));
    CellConstraints cc = new CellConstraints();
    dialog.addLabel("Version", cc.xy(1, 1));
    dialog.addTextField("version", "", cc.xy(3, 1));
    dialog.addSubmitButton("Submit", ApplicationMessage.getLocalized("BranchMakeRelease.Proceed"));
    dialog.setCancelButton(ApplicationMessage.getLocalized("BranchMakeRelease.Cancel"));
    dialog.show();
  }

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord record = context.getSelectedRecord();
    if (isAllowed(context, record))
    {
      askVersion(context, record);
    }
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    if (status == IGuiElement.SELECTED)
    {
      IDataTableRecord record = context.getSelectedRecord();
      button.setEnable(Rcs_branch.status_ENUM._Open.equals(record.getValue(Rcs_branch.status)));
    }
    else
      button.setEnable(false);
  }
}
