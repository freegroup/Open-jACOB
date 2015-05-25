package jacob.event.ui.activity;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Oct 05 23:55:13 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.entrypoint.gui.ShowEmployee;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.RecordLabelProvider;
import de.tif.jacob.screen.dialogs.IRecordTreeDialog;
import de.tif.jacob.screen.dialogs.IRecordTreeDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the ShowTree-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author andreas
 *
 */
public class ShowTree extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ShowTree.java,v 1.3 2006/11/10 07:51:58 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();
  
  /**
   * Maps an alias name to an instance of <code>IRecordTreeDialogCallback</code>
   */
  static private final Map onRecordSelectCallbackMap = new HashMap();
  
  /**
   * Callback which backfills a sales project, if not already backfilled.
   */
  static private final IRecordTreeDialogCallback salesprojectCallback = new IRecordTreeDialogCallback()
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      backfillSalesRecord(context, record, "salesproject", "salesprojectBrowser", "salesproject");
    }
  };
  
  /**
   * Callback which backfills a sales activity, if not already backfilled.
   */
  static private final IRecordTreeDialogCallback activityCallback = new IRecordTreeDialogCallback()
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      backfillSalesRecord(context, record, "activity", "activityBrowser", "activity");
    }
  };
  
  /**
   * Callback which backfills a sales contact, if not already backfilled.
   */
  static private final IRecordTreeDialogCallback contactCallback = new IRecordTreeDialogCallback()
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      backfillSalesRecord(context, record, "activityContact", "activityContactBrowser", "activityContact");
    }
  };
  
  /**
   * Callback which backfills a customer, if not already backfilled.
   */
  static private final IRecordTreeDialogCallback organizationCallback = new IRecordTreeDialogCallback()
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      backfillSalesRecord(context, record, "salesprojectOrganization", "salesprojectOrganizationBrowser", "salesprojectOrganization");
    }
  };
  
  /**
   * Callback which popups an employee information dialog
   */
  static private final IRecordTreeDialogCallback employeeCallback = new IRecordTreeDialogCallback()
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      // and popup new window to show employee information
      //
      Properties props = new Properties();
      props.put(ShowEmployee.EMPLOYEEKEY_PROP, record.getSaveStringValue("pkey"));
      EntryPointUrl.popup(context, new ShowEmployee(), props);
    }
  };
  
  /**
   * Callback which backfills a sales product, if not already backfilled.
   */
  static private final IRecordTreeDialogCallback productCallback = new IRecordTreeDialogCallback()
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      backfillSalesRecord(context, record, "product", "productBrowser", "salesPipeline");
    }
  };
  
  /**
   * Callback which downloads the respective document
   */
  static private final IRecordTreeDialogCallback documentCallback = new IRecordTreeDialogCallback()
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      // simply open document dialog
      context.createDocumentDialog(record.getDocumentValue("docfile")).show();
    }
  };
  
  /**
   * Callback which backfills an incident, if not already backfilled.
   */
  static private final IRecordTreeDialogCallback incidentCallback = new IRecordTreeDialogCallback()
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      IDomain incidentDomain = context.getDomain("f_incident");
      IDataTable incidentTable = context.getDataAccessor().getTable("incident");
      
      // record already backfilled?
      //
      IDataTableRecord selectedRecord = incidentTable.getSelectedRecord();
      if (selectedRecord == null || !selectedRecord.getPrimaryKeyValue().equals(record.getPrimaryKeyValue()))
      {
        // clear complete domain to fresh with given record
        incidentDomain.clear(context);

        // constrain search with primary key of the given incident
        //
        incidentTable.qbeSetPrimaryKeyValue(record.getPrimaryKeyValue());

        // search and propagate the record
        //
        IDataBrowser incidentBrowser = context.getDataAccessor().getBrowser("incidentBrowser");
        incidentBrowser.search("r_incident", Filldirection.BOTH);
        if (incidentBrowser.recordCount() == 0)
        {
          // just in case the record has been deleted in the mean while
          throw new RecordNotFoundException(record.getId());
        }
        incidentBrowser.setSelectedRecordIndex(0);
        incidentBrowser.propagateSelections();
      }
      
      context.setCurrentForm("f_incident", "incident");
    }
  };
  
  static private void backfillSalesRecord(IClientContext context, IDataTableRecord record, String alias, String browserName, String form) throws Exception
  {
    IDomain domain = context.getDomain();
    IDataTable table = context.getDataTable(alias);
    
    // record already backfilled?
    //
    IDataTableRecord selectedRecord = table.getSelectedRecord();
    if (selectedRecord == null || !selectedRecord.getPrimaryKeyValue().equals(record.getPrimaryKeyValue()))
    {
      // clear complete domain to fresh with given record
      domain.clear(context);

      // constrain search with primary key of the given incident
      //
      table.qbeSetPrimaryKeyValue(record.getPrimaryKeyValue());

      // search and propagate the record
      //
      IDataBrowser browser = context.getDataAccessor().getBrowser(browserName);
      browser.search("r_sales", Filldirection.BOTH);
      if (browser.recordCount() == 0)
      {
        // just in case the record has been deleted in the mean while
        throw new RecordNotFoundException(record.getId());
      }
      browser.setSelectedRecordIndex(0);
      browser.propagateSelections();
    }
    
    context.setCurrentForm(form);
  }
  
  static
  {
    // initialize callback map
    //
    onRecordSelectCallbackMap.put("activity", activityCallback);
    
    onRecordSelectCallbackMap.put("salesproject", salesprojectCallback);
    onRecordSelectCallbackMap.put("salesprojectOrganizationOpenSalesproject", salesprojectCallback);
    
    onRecordSelectCallbackMap.put("product", productCallback);
    
    onRecordSelectCallbackMap.put("incident", incidentCallback);
    
    onRecordSelectCallbackMap.put("product", productCallback);
    
    onRecordSelectCallbackMap.put("activityDocument", documentCallback);
    onRecordSelectCallbackMap.put("salesprojectDocument", documentCallback);
    
    onRecordSelectCallbackMap.put("activityContact", contactCallback);
    onRecordSelectCallbackMap.put("salesprojectContact", contactCallback);
    onRecordSelectCallbackMap.put("salesprojectOrganizationContact", contactCallback);
    
    onRecordSelectCallbackMap.put("activityContactOrganization", organizationCallback);
    onRecordSelectCallbackMap.put("salesprojectOrganization", organizationCallback);
    
    onRecordSelectCallbackMap.put("activityAgent", employeeCallback);
    onRecordSelectCallbackMap.put("activityOwner", employeeCallback);
    onRecordSelectCallbackMap.put("salesprojectAgent", employeeCallback);
    onRecordSelectCallbackMap.put("salesrep", employeeCallback);
  }
  
  static private class MyCallback implements IRecordTreeDialogCallback
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      // route tree request to alias specific callback
      //
      IRecordTreeDialogCallback recordCallback = (IRecordTreeDialogCallback) onRecordSelectCallbackMap.get(record.getTableAlias().getName());
      if (recordCallback != null)
      {
        recordCallback.onSelect(context, record);
      }
    }
  }
  
  static private class MyLabelProvider extends RecordLabelProvider
  {
    public String getImage(IClientContext context, Object record)
    {
      String alias = ((IDataTableRecord) record).getTableAlias().getName();
      return alias + "_tree";
    }

    public String getText(IClientContext context, Object record)
    {
      String alias = ((IDataTableRecord) record).getTableAlias().getName();
      return " [" + alias + "] " + super.getText(context, record);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
   *      de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    IRelationSet relationSet = context.getApplicationDefinition().getRelationSet("r_sales_tree");
    IRecordTreeDialog dialog = context.createRecordTreeDialog(button, currentRecord, relationSet, Filldirection.BOTH, new MyCallback());
    dialog.setLabelProvider(new MyLabelProvider());
    dialog.show();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
