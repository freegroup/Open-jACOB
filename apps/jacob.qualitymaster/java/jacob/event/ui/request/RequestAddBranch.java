/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Feb 02 15:38:29 CET 2006
 */
package jacob.event.ui.request;

import jacob.model.Rcs_branch;
import jacob.model.Request;
import jacob.model.RequestCategory;
import jacob.model.Requestbranch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the RequestAddBranch record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class RequestAddBranch extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RequestAddBranch.java,v 1.4 2008/04/30 17:39:03 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  public interface IBranchSelectCallback
  {
    public boolean onSelect(IClientContext context, IDataTableRecord branchRecord) throws Exception;
    
    public void onFinish(IClientContext context, List selectedBranchRecords) throws Exception;
  }
  
  private static class BranchSelectStatus
  {
    private final IGuiElement ankerGuiElement;
    private final IDataTableRecord requestRecord;
    private final List branchRecordsToSelect;
    private final List branchRecordsSelected;
    private final IBranchSelectCallback callback;
    
    private BranchSelectStatus(IDataTableRecord requestRecord, List branchRecordsToSelect, IGuiElement ankerGuiElement, IBranchSelectCallback callback)
    {
      this.requestRecord = requestRecord;
      this.branchRecordsToSelect = branchRecordsToSelect;
      this.branchRecordsSelected = new ArrayList();
      this.ankerGuiElement = ankerGuiElement;
      this.callback = callback;
    }
  }
  
  private static class OkCancelDialogCallback implements IOkCancelDialogCallback
  {
    private final BranchSelectStatus status;
    
    private OkCancelDialogCallback(BranchSelectStatus status)
    {
      this.status = status;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext context) throws Exception
    {
      this.status.callback.onFinish(context, this.status.branchRecordsSelected);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onOk(de.tif.jacob.screen.IClientContext)
     */
    public void onOk(IClientContext context) throws Exception
    {
      createBranchSelectDialog(context, status);
    }
  }
  
  /**
   * Callback if one branch is selected from grid list.
   * 
   * @author Andreas Sonntag
   */
  private static class BranchSelectCallback implements IBranchSelectCallback
  {
    private IDataTableRecord requestRecord;
    
    private BranchSelectCallback(IDataTableRecord requestRecord)
    {
      this.requestRecord = requestRecord;
    }
    
    public boolean onSelect(IClientContext context, IDataTableRecord branchRecord) throws Exception
    {
      IDataTable requestBranch = context.getDataTable(Requestbranch.NAME);
      IDataTransaction trans = requestRecord.getCurrentTransaction();
      if (trans == null)
      {
        trans = requestBranch.startNewTransaction();
        try
        {
          IDataTableRecord rb_Rec = requestBranch.newRecord(trans);
          rb_Rec.setValue(trans, Requestbranch.request_key, requestRecord.getValue("pkey"));
          rb_Rec.setValue(trans, Requestbranch.rcs_branch_key, branchRecord.getValue("pkey"));
          trans.commit();
        }
        finally
        {
          trans.close();
        }
        
        // propagate changes to display added branch
        //
        context.getDataAccessor().propagateRecord(requestRecord, "requestRelationset", Filldirection.BOTH);
        
        return true;
      }
      else
      {
        // Im Update Modus geht es noch nicht, weil die beforeCommit Prüfung
        // fehlschlägt
        IDataTableRecord rb_Rec = requestBranch.newRecord(trans);
        rb_Rec.setValue(trans, Requestbranch.request_key, requestRecord.getValue("pkey"));
        rb_Rec.setValue(trans, Requestbranch.rcs_branch_key, branchRecord.getValue("pkey"));
        
        return false;
      }
    }

    /* (non-Javadoc)
     * @see jacob.event.ui.request.RequestAddBranch.IBranchSelectCallback#onFinish(de.tif.jacob.screen.IClientContext, java.util.List)
     */
    public void onFinish(IClientContext context, List selectedBranchRecords) throws Exception
    {
      // do nothing here
    }
  }

  private static class GridCallBack implements IGridTableDialogCallback
  {
    private final BranchSelectStatus status;
    
    private GridCallBack(BranchSelectStatus status)
    {
      this.status = status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.dialogs.IGridTableDialogCallback#onSelect(de.tif.jacob.screen.IClientContext,
     *      int, java.util.Properties)
     */
    public void onSelect(IClientContext context, int index, Properties values) throws Exception
    {
      IDataTableRecord branchRecord = (IDataTableRecord) this.status.branchRecordsToSelect.get(index);

      if (this.status.callback.onSelect(context, branchRecord))
      {
        this.status.branchRecordsSelected.add(this.status.branchRecordsToSelect.remove(index));

        // popup selection list again if branches exist
        //
        if (this.status.branchRecordsToSelect.size() != 0)
        {
          context.createOkCancelDialog(new ApplicationMessage("RequestAddBranch.AddFurther"),
              new OkCancelDialogCallback(status)).show();
        }
        else
        {
          this.status.callback.onFinish(context, this.status.branchRecordsSelected);
        }
      }
    }
  }

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord requestRecord = context.getSelectedRecord();
    if (!selectBranches(context, button, requestRecord, new BranchSelectCallback(requestRecord)))
    {
      alert(ApplicationMessage.getLocalized("RequestAddBranch.NoBranches"));
    }
  }

  /**
   * Searches for all opened non assigned branches and displayes them for
   * selection.
   * 
   * @param context
   * @param ankerGuiElement
   * @param requestRecord
   * @return
   * @throws Exception
   */
  private static boolean selectBranches(IClientContext context, IGuiElement ankerGuiElement, IDataTableRecord requestRecord, IBranchSelectCallback callback) throws Exception
  {
    // search and collect already assigned branches first
    //
    Set existingBranches = new HashSet();
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTable requestbranchTable = accessor.getTable(Requestbranch.NAME);
    requestbranchTable.qbeSetKeyValue(Requestbranch.request_key, requestRecord.getValue(Request.pkey));
    requestbranchTable.search();
    for (int i = 0; i < requestbranchTable.recordCount(); i++)
    {
      existingBranches.add(requestbranchTable.getRecord(i).getValue(Requestbranch.rcs_branch_key));
    }

    // set constraints (project and status) and search
    //
    accessor.qbeClearAll();
    IDataTable branchTable = accessor.getTable(Rcs_branch.NAME);
    branchTable.qbeSetValue(Rcs_branch.status, Rcs_branch.status_ENUM._Open);
    List brancheRecordsToSelect = new ArrayList();
    if (requestRecord.hasLinkedRecord(RequestCategory.NAME))
    {
      // reload record to reflect changes made in administration after backfilling the request
      IDataTableRecord reloadedCategoryRecord = accessor.getTable(RequestCategory.NAME).loadRecord(requestRecord.getLinkedRecord(RequestCategory.NAME).getPrimaryKeyValue()); 
      String projectKey = reloadedCategoryRecord.getStringValue(RequestCategory.project_key);
      if (projectKey != null)
      {
        branchTable.qbeSetValue(Rcs_branch.rcs_project_key, projectKey);
        branchTable.search("rcs_branchRelationset");
        for (int i = 0; i < branchTable.recordCount(); i++)
        {
          IDataTableRecord branchRecord = branchTable.getRecord(i);
          if (!existingBranches.contains(branchRecord.getValue(Rcs_branch.pkey)))
          {
            brancheRecordsToSelect.add(branchRecord);
          }
        }
      }
    }
    
    // sort branches
    Collections.sort(brancheRecordsToSelect, new Comparator()
    {
      public int compare(Object o1, Object o2)
      {
        IDataTableRecord branchRecord1 = (IDataTableRecord) o1;
        IDataTableRecord branchRecord2 = (IDataTableRecord) o2;

        try
        {
          // compare the other way round because we want bigger version number
          // first
          return branchRecord2.getDecimalValue(Rcs_branch.versionnbr).compareTo(branchRecord1.getDecimalValue(Rcs_branch.versionnbr));
        }
        catch (NoSuchFieldException ex)
        {
          // should never occur
          throw new RuntimeException(ex);
        }
      }
    });
    
    // Note: If no search has been performed, the browser will be empty because
    // we
    // have created a new accessor
    //
    if (brancheRecordsToSelect.size() == 0)
    {
      return false;
    }

    createBranchSelectDialog(context, new BranchSelectStatus(requestRecord, brancheRecordsToSelect, ankerGuiElement, callback));
    return true;
  }
  
  private static void createBranchSelectDialog(IClientContext context, BranchSelectStatus status) throws Exception
  {
    // create grid dialog to show
    //
    IGridTableDialog dialog = context.createGridTableDialog(status.ankerGuiElement, new GridCallBack(status));
    String[] header =
    { "Branch" };
    String[][] data = new String[status.branchRecordsToSelect.size()][1];
    for (int i = 0; i < status.branchRecordsToSelect.size(); i++)
    {
      IDataTableRecord branchRecord = (IDataTableRecord) status.branchRecordsToSelect.get(i);
      data[i][0] = branchRecord.getSaveStringValue(Rcs_branch.name);
    }
    dialog.setHeader(header);
    dialog.setData(data);

    dialog.show();
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // button.setEnable(!(status==IGuiElement.SEARCH||status==IGuiElement.UNDEFINED));
    // Im Update Modus geht es nicht, weil die beforeCommit Prüfung fehlschlägt
    button.setEnable((status == IGuiElement.SELECTED));
  }
}
