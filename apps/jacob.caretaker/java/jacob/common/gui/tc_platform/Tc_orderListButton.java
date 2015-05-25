/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 08 13:59:34 CEST 2006
 */
package jacob.common.gui.tc_platform;

import jacob.common.gui.tc_capacity.Capacity;
import jacob.common.tc.TC;
import jacob.exception.BusinessException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IExcelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;


/**
 * The event handler for the Tc_orderListButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Tc_orderListButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Tc_orderListButton.java,v 1.4 2007/03/07 22:50:25 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

  private static class Callback implements IOkCancelDialogCallback
  {
    private final String dateStr;

    private Callback(String dateStr)
    {
      this.dateStr = dateStr;
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context) throws Exception
    {
      IDataTableRecord activeCampaign = TC.getActiveCampagne(context);

      // retrieve capacity records
      //
      // Achtung: Da Oracle 8 scheinbar keine mehrstufigen OUTER JOINS kann,
      // werden erst alle Kapazitäten ohne Buchungsinfos (capacity0Browser)
      // und anschließend alle Kapazitäten mit Buchungen geholt
      // (capacityBrowser).
      //
      IDataAccessor accessor = context.getDataAccessor().newAccessor();
      IDataTable capacityTable = accessor.getTable("tc_capacity");
      capacityTable.qbeClear();
      capacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
      capacityTable.qbeSetValue("slot", dateStr);
      IDataBrowser capacity0Browser = accessor.getBrowser("tc_capacityOrderList0Browser");
      capacity0Browser.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
      capacity0Browser.search("r_tc_capacity_platform");

      // check whether this date is alright
      //
      if (capacity0Browser.recordCount() == 0)
      {
        throw new BusinessException("Für den angegebenen Tag sind keine Buchungen möglich!");
      }

      /* 07.03.2007: Keine Buchungsliste erzeugen
      // Map(IDataKeyValue->IDataBrowserRecord)
      Map bookedCapacities = new HashMap();
      IDataBrowser capacityBrowser = accessor.getBrowser("tc_capacityOrderListBrowser");
      capacityBrowser.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
      capacityBrowser.search("r_tc_platform");
      for (int j = 0; j < capacityBrowser.recordCount(); j++)
      {
        IDataBrowserRecord browserRec = capacityBrowser.getRecord(j);
        bookedCapacities.put(browserRec.getPrimaryKeyValue(), browserRec);
      }
      */

      // lock capacity entries
      //
      Capacity.lock(context, dateStr);

      /* 07.03.2007: Keine Buchungsliste erzeugen
      IBrowserDefinition browserDef = accessor.getApplication().getBrowserDefinition(capacityBrowser.getName());
      List browserFields = browserDef.getBrowserFields();
      int browserFieldSize = browserFields.size();
      IBrowserDefinition browser0Def = accessor.getApplication().getBrowserDefinition(capacity0Browser.getName());
      int browser0FieldSize = browser0Def.getBrowserFields().size();

      // build excel header
      //
      // Achtung: Hier wird noch eine zusätzliche Spalte eingefügt, um den Slot-Zeitstempel in 
      //          Datum und Uhrzeit zu trennen.
      //
      final int ADDITIONAL_COLUMNS = 1;
      int columnNumber = browserFields.size() + ADDITIONAL_COLUMNS;
      String[] header = new String[columnNumber];
      for (int i = 0; i < browserFields.size(); i++)
      {
        IBrowserField browserField = (IBrowserField) browserFields.get(i);
        switch (i)
        {
          case 0:
            header[0] = browserField.getLabel();
            break;
          case 1:
            header[1] = "Datum";
            header[2] = "Uhrzeit";
            break;
          default:
            header[i + ADDITIONAL_COLUMNS] = browserField.getLabel();
            break;
        }
      }

      // build excel data
      //
      Locale locale = context.getApplicationLocale();
      String[][] data = new String[capacity0Browser.recordCount()][columnNumber];
      for (int j = 0; j < capacity0Browser.recordCount(); j++)
      {
        IDataBrowserRecord browserRec = capacity0Browser.getRecord(j);
        IDataBrowserRecord bookedRec = (IDataBrowserRecord) bookedCapacities.get(browserRec.getPrimaryKeyValue());
        for (int i = 0; i < browser0FieldSize; i++)
        {
          String value = browserRec.getSaveStringValue(i, locale); 
          switch (i)
          {
            case 0:
              data[j][i] = value;
              break;
            case 1:
              // separate date: "16.04.2007 12:45" -> "16.04.2007","12:45"
              int pos = value.indexOf(" ");
              data[j][1] = value.substring(0, pos);
              data[j][2] = value.substring(pos + 1);
              break;
            default:
              data[j][i + ADDITIONAL_COLUMNS] = value;
              break;
          }
        }
        for (int i = browser0FieldSize; i < browserFieldSize; i++)
        {
          if (bookedRec == null)
          {
            data[j][i + ADDITIONAL_COLUMNS] = (i == browserFieldSize - 1) ? "nicht gebucht" : "";
          }
          else
          {
            data[j][i + ADDITIONAL_COLUMNS] = bookedRec.getSaveStringValue(i, locale);
          }
        }
      }

      // create excel dialog
      //
      IExcelDialog dialog = context.createExcelDialog();
      dialog.setHeader(header);
      dialog.setData(data);
      dialog.show();
      */
    }
  }

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // determine order list date
    //
    ISingleDataGuiElement lockDateElement = (ISingleDataGuiElement) context.getGroup().findByName("tc_orderListDate");
    String dateStr = StringUtil.toSaveString(lockDateElement.getValue()).trim();
    if (dateStr.length() == 0)
      throw new BusinessException("Kein Tag ausgewählt!");

    context.createOkCancelDialog(
        "Beim Ausführen dieser Aktion werden alle Termine vom " + dateStr + " gesperrt,\\n damit keine weiteren Buchungen vorgenommen werden können. Fortfahren?",
        new Callback(dateStr)).show();
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
