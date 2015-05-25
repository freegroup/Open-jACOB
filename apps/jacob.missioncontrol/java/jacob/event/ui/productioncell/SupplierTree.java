package jacob.event.ui.productioncell;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Jul 27 14:32:46 CEST 2005
 *
 */
import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.RecordLabelProvider;
import de.tif.jacob.screen.dialogs.IRecordTreeDialog;
import de.tif.jacob.screen.dialogs.IRecordTreeDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

/**
 * The Event handler for the SupplierTree-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author achim
 * 
 */
public class SupplierTree extends IButtonEventHandler
{
  private static final Map aliasNameMap = new HashMap();
  static
  {
    aliasNameMap.put("main_supplier", "supplier_org");
    aliasNameMap.put("sub_sp_one", "sub_supplier1");
    aliasNameMap.put("sub_sp_two", "sub_supplier2");
    aliasNameMap.put("sub_sp_three", "sub_supplier3");

  }
  static class MyCallback implements IRecordTreeDialogCallback
  {
    public void onSelect(IClientContext context, IDataTableRecord record) throws Exception
    {
      // Im der Calldomain kein Sprung  
      if ("productioncell".equals(context.getForm().getName()))
          return;
      context.getDataAccessor().propagateRecord(record,Filldirection.BOTH);

    }
  }
  static class MyLabelProvider extends RecordLabelProvider
  {
    public String getName(IClientContext context, IDataTableRecord record) throws Exception
    {
      String alias = record.getTableAlias().getName();
      if (alias.equals("productioncell"))
      {
        return record.getSaveStringValue("id");
      }
      return record.getLinkedRecord((String) aliasNameMap.get(alias)).getSaveStringValue("name");

    }

    public String getImage(IClientContext context, Object record)
    {
      return ((IDataTableRecord) record).getTableAlias().getName() + "_tree";
    }

    public String getText(IClientContext context, Object record)
    {

      try
      {
        return "  " + getName(context, (IDataTableRecord) record);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return "false";
      }


    }
  }
  static public final transient String RCS_ID = "$Id$";
  static public final transient String RCS_REV = "$Revision$";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * The user has been click on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    IRelationSet relationSet = context.getApplicationDefinition().getRelationSet("supplierTree");
    final Filldirection filldirection = Filldirection.BOTH;

    // context.createRecordTreeDialog(button, currentRecord, relationSet,
    // filldirection, new IRecordTreeDialogCallback()
    // {
    // public void onSelect(IClientContext context, IDataTableRecord record)
    // throws Exception
    // {
    // alert("Selected");
    // }
    // }).show();
    IRecordTreeDialog dialog = context.createRecordTreeDialog(button, currentRecord, relationSet, filldirection, new MyCallback());
    dialog.setLabelProvider(new MyLabelProvider());
    dialog.show();

  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
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
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {

  }
}
