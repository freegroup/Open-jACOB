/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 18 15:50:52 CEST 2006
 */
package jacob.common.gui.tc_platform;

import jacob.common.AppLogger;
import jacob.common.tc.Slot;
import jacob.common.tc.TC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.DatetimeUtil;

/**
 * The event handler for the GenerateCapacity record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class GenerateCapacity extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: GenerateCapacity.java,v 1.3 2006/08/04 11:18:57 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  static private final transient Log logger = AppLogger.getLogger();

  public static final DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

  /**
   * Template select callback
   */
  private static class DateRangeSelectCallback implements IFormDialogCallback
  {
    private final IDataTableRecord platform;
    private final IDataTableRecord templateRecord;
    private final IDataTableRecord activeCampaign;

    private DateRangeSelectCallback(IDataTableRecord platform, IDataTableRecord activeCampaign, IDataTableRecord templateRecord)
    {
      this.platform = platform;
      this.activeCampaign = activeCampaign;
      this.templateRecord = templateRecord;
    }

    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      String from = (String) values.get("from");
      String to = (String) values.get("to");

      if (from == null || to == null)
      {
        selectDateRange(context, platform, activeCampaign, templateRecord, from, to);
        return;
      }

      Date fromDate, toDate;
      try
      {
        fromDate = DatetimeUtil.convertToDate(from);
        toDate = DatetimeUtil.convertToDate(to);
      }
      catch (InvalidExpressionException ex)
      {
        selectDateRange(context, platform, activeCampaign, templateRecord, from, to);
        return;
      }

      // toDate should not be before fromDate
      //
      if (fromDate.getTime() > toDate.getTime())
      {
        selectDateRange(context, platform, activeCampaign, templateRecord, from, to);
        return;
      }

      // toDate and fromDate must be between campaign interval
      //
      Date fromCampaign = this.activeCampaign.getDateValue("from");
      Date toCampaign = this.activeCampaign.getDateValue("to");
      if (fromDate.getTime() < fromCampaign.getTime() || toDate.getTime() > toCampaign.getTime())
      {
        selectDateRange(context, platform, activeCampaign, templateRecord, from, to);
        return;
      }

      // retrieve relevant holidays
      //
      IDataAccessor accessor = this.activeCampaign.getAccessor();
      IDataTable holidayTable = accessor.getTable("tc_holiday");
      holidayTable.qbeClear();
      holidayTable.qbeSetValue("date", fromDate + ".." + toDate);
      holidayTable.search();
      Set holidays = new HashSet();
      for (int i = 0; i < holidayTable.recordCount(); i++)
      {
        holidays.add(holidayTable.getRecord(i).getDateValue("date"));
      }

      // delete old capacity records and create new ones
      //
      IDataTransaction trans = accessor.newTransaction();
      try
      {
        IDataTable capacityTable = accessor.getTable("tc_capacity");
        capacityTable.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);

        // // remove unbooked capacity records which are outside the new time
        // range
        // //
        // capacityTable.qbeClear();
        // capacityTable.qbeSetKeyValue("tc_platform_key",
        // this.platform.getValue("pkey"));
        // capacityTable.qbeSetKeyValue("tc_campaign_key",
        // this.activeCampaign.getValue("pkey"));
        // // example: !11.7.2006..14.7.2006 which excludes 14.7.2006 14:45!
        // capacityTable.qbeSetValue("slot", "!" + from + ".." + to);
        // capacityTable.qbeSetValue("tc_order_key", "null");
        // int delRecNum = capacityTable.searchAndDelete(trans);
        int delRecNum = 0;

        // fetch already existing capacity records which are inside the new time
        // range
        //
        capacityTable.qbeClear();
        capacityTable.qbeSetKeyValue("tc_platform_key", this.platform.getValue("pkey"));
        capacityTable.qbeSetKeyValue("tc_campaign_key", this.activeCampaign.getValue("pkey"));
        capacityTable.qbeSetValue("slot", from + ".." + to);
        int recNum = capacityTable.search();
        Map recMap = new HashMap();
        for (int i = 0; i < recNum; i++)
        {
          IDataTableRecord capacityRecord = capacityTable.getRecord(i);
          recMap.put(capacityRecord.getDateValue("slot"), capacityRecord);
        }

        // create new capacity records
        //
        int newRecNum = 0;
        Calendar cal = new GregorianCalendar();
        cal.setTime(fromDate);
        do
        {
          // no capacity on sunday or saturday
          if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
          {
            // regard holidays
            if (holidays.contains(cal.getTime()) == false)
            {
              // create entries for all template slots
              //
              for (int i = 0; i < Slot.SLOTS.length; i++)
              {
                Slot slot = Slot.SLOTS[i];

                // slot configured in template?
                if (this.templateRecord.getintValue(slot.dbName) != 0)
                {
                  Date slotTime = slot.getTime(cal);

                  // does a capacity record already exists for that slot?
                  if (recMap.containsKey(slotTime))
                  {
                    // Yes

                    // remove entry because we do not want to deleted it
                    // afterwards
                    recMap.remove(slotTime);
                  }
                  else
                  {
                    // No

                    // create it
                    IDataTableRecord capacityRecord = capacityTable.newRecord(trans);
                    capacityRecord.setLinkedRecord(trans, this.activeCampaign);
                    capacityRecord.setLinkedRecord(trans, this.platform);
                    capacityRecord.setDateValue(trans, "slot", slotTime);
                    newRecNum++;
                  }
                }
              }
            }
          }

          // add one day
          cal.add(Calendar.DATE, 1);
        }
        while (cal.getTimeInMillis() <= toDate.getTime());

        // remove remaining unbooked capacity records
        //
        Iterator iter = recMap.values().iterator();
        while (iter.hasNext())
        {
          IDataTableRecord capacityRecord = (IDataTableRecord) iter.next();

          // delete unbooked capacity records only
          if (capacityRecord.getValue("tc_order_key") == null)
          {
            capacityRecord.delete(trans);
            delRecNum++;
          }
        }

        // and flush the changes to the database
        //
        trans.commit();

        if (delRecNum == 0)
          context.createMessageDialog("Es wurden " + newRecNum + " Kapazitätseinträge angelegt.").show();
        else
          context.createMessageDialog("Es wurden " + newRecNum + " Kapazitätseinträge angelegt und " + delRecNum + " entfernt.").show();

        // redraw charts
        //
        RefreshCharts.doit(context);
      }
      finally
      {
        trans.close();
      }
    }
  }

  private static void selectDateRange(IClientContext context, IDataTableRecord platform, IDataTableRecord activeCampaign, IDataTableRecord templateRecord,
      String from, String to) throws Exception
  {
    FormLayout layout = new FormLayout("10dlu,p,10dlu,100dlu,20dlu,p,10dlu,100dlu,10dlu", "20dlu,p,20dlu");

    IFormDialog dialog = context.createFormDialog("Buchungsinterval angeben", layout, new DateRangeSelectCallback(platform, activeCampaign, templateRecord));
    CellConstraints c = new CellConstraints();
    dialog.addLabel("Von", c.xy(1, 1));
    dialog.addTextField("from", from == null ? "" : from, c.xy(3, 1));
    dialog.addLabel("Bis", c.xy(5, 1));
    dialog.addTextField("to", to == null ? "" : to, c.xy(7, 1));

    dialog.addSubmitButton("ok", "Kapazität erstellen");
    dialog.setCancelButton("Abbrechen");

    dialog.show(350, 120);
  }

  /**
   * Template select callback
   */
  private static class TemplateSelectCallback implements IFormDialogCallback
  {
    private final IDataTable templateTable;
    private final IDataTableRecord activeCampaign;
    private final IDataTableRecord platform;

    private TemplateSelectCallback(IDataTableRecord platform, IDataTableRecord activeCampaign, IDataTable templateTable)
    {
      this.platform = platform;
      this.activeCampaign = activeCampaign;
      this.templateTable = templateTable;
    }

    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      if ("continue".equals(buttonId))
      {
        int index = -1;
        try
        {
          index = Integer.parseInt((String) values.get("templateIndex"));
        }
        catch (Exception ex)
        {
          // ignore
        }

        if (index == -1)
        {
          context.createMessageDialog("Keine Buchungsvorlage ausgewählt").show();
        }
        else
        {
          String from = dateFormatter.format(this.activeCampaign.getDateValue("from"));
          String to = dateFormatter.format(this.activeCampaign.getDateValue("to"));

          IDataTableRecord templateRecord = templateTable.getRecord(index);
          selectDateRange(context, platform, activeCampaign, templateRecord, from, to);
        }
      }
    }
  }

  private static void selectTemplate(IClientContext context, IDataTableRecord platform, IDataTableRecord activeCampaign) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTable templateTable = accessor.getTable("tc_dispoTemplate");
    templateTable.search();
    if (templateTable.recordCount() == 0)
    {
      context.createMessageDialog("Es sind bisher keine Buchungsvorlagen vorhanden!").show();
      return;
    }

    String[] entries = new String[templateTable.recordCount()];
    for (int i = 0; i < templateTable.recordCount(); i++)
    {
      IDataTableRecord templateRecord = templateTable.getRecord(i);
      entries[i] = templateRecord.getStringValue("description");
    }

    // create dialog to select template
    FormLayout layout = new FormLayout("10dlu,grow,10dlu", // columns
        "10dlu,p,10dlu,grow,10dlu"); // rows
    CellConstraints cc = new CellConstraints();
    IFormDialog dialog = context.createFormDialog("Buchungsvorlage", layout, new TemplateSelectCallback(platform, activeCampaign, templateTable));
    dialog.addLabel("Buchungsvorlage auswählen:", cc.xy(1, 1));
    dialog.addListBox("template", entries, entries.length == 1 ? 0 : -1, cc.xy(1, 3));
    dialog.addSubmitButton("continue", "Weiter");

    // Show the dialog with a prefered size. The dialog trys to resize to the
    // optimum size!
    dialog.show(250, 300);
  }

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // gleich aktive Kampagne holen, damit sofort eine Fehlermeldung ausgegeben
    // wird, sofern noch keine vorhanden.
    selectTemplate(context, context.getSelectedRecord(), TC.getActiveCampagne(context));
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
