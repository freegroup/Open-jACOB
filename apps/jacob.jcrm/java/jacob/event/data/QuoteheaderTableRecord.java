/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.exception.BusinessException;
import jacob.reminder.IReminder;
import jacob.reminder.ReminderFactory;

import java.math.BigDecimal;
import java.util.Calendar;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.i18n.ApplicationMessage;

/**
 * 
 * @author andreas
 */
public class QuoteheaderTableRecord extends DataTableRecordEventHandler
{
  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    // activated reminder, if necessary
    //
    if (tableRecord.isDeleted())
    {
      IReminder myReminder = ReminderFactory.getReminder(tableRecord);
      myReminder.delete(tableRecord);
    }
    else
    {
      if (tableRecord.hasChangedValue("valid_to") && "Open".equals(tableRecord.getValue("status")))
      {
        IReminder myReminder = ReminderFactory.getReminder(tableRecord);

        if (tableRecord.hasNullValue("valid_to"))
        {
          myReminder.delete(tableRecord);
        }
        else
        {
          Calendar validto = Calendar.getInstance();
          validto.setTime(tableRecord.getDateValue("valid_to"));
          // release alert 3 days before
          validto.add(Calendar.DATE, -3);
          myReminder.setWhen(validto.getTime());
          myReminder.setMethod(IReminder.ALERT);
          myReminder.setRecipient(jacob.common.DataUtils.getUserRecord());
          myReminder.setMsg(ApplicationMessage
              .getLocalized("QuoteReminder.Text", tableRecord.getStringValue("pkey"), tableRecord.getStringValue("description")));
          myReminder.schedule();
        }
      }
      else
      {
        if (tableRecord.hasChangedValue("status") && "Open".equals(tableRecord.getOldValue("status")))
        {
          IReminder myReminder = ReminderFactory.getReminder(tableRecord);
          myReminder.delete(tableRecord);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // delete all line items as well
    //
    IDataTable quotelineitemTable = tableRecord.getAccessor().getTable("quotelineitem");
    quotelineitemTable.qbeClear();
    quotelineitemTable.qbeSetKeyValue("quoteheader_key", tableRecord.getValue("pkey"));
    quotelineitemTable.searchAndDelete(transaction);
    
    // delete all documents as well
    //
    IDataTable documentTable = tableRecord.getAccessor().getTable("document");
    documentTable.qbeClear();
    documentTable.qbeSetKeyValue("quoteheader_key", tableRecord.getValue("pkey"));
    documentTable.searchAndDelete(transaction);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord quote, IDataTransaction transaction) throws Exception
  {
    // Set agent, i.e. the current user
    //
    IDataTable agent = quote.getAccessor().getTable("quoteAgent");
    agent.qbeClear();
    agent.qbeSetKeyValue("pkey", transaction.getUser().getKey());
    agent.search();
    if (agent.recordCount() == 1)
    {
      quote.setLinkedRecord(transaction, agent.getRecord(0));
    }
  }

  public static void calcTotalDiscount(IDataTableRecord quoteheader) throws Exception
  {
    IDataTransaction trans = quoteheader.getCurrentTransaction();
    BigDecimal discountallowance = (BigDecimal) Context.getCurrent().getUser().getProperty("discount");
    //At last Calculate the Quote/Order Header Discount

    BigDecimal h_positionamount = quoteheader.getSaveDecimalValue("position_amount");
    BigDecimal h_baseprice = quoteheader.getSaveDecimalValue("base_amount");
    BigDecimal h_discountfix = quoteheader.getSaveDecimalValue("discount_fix");
    BigDecimal h_discountpercent = quoteheader.getSaveDecimalValue("discount_percent");
    BigDecimal h_amount = h_positionamount.subtract(h_discountfix).multiply(HUNDRED.subtract(h_discountpercent)).divide(HUNDRED, 7);
    BigDecimal h_totaldiscount = h_baseprice.subtract(h_amount);
    
    // Check total discount allowance
    //
    BigDecimal h_alloweddiscount = h_baseprice.subtract(h_baseprice.multiply(HUNDRED.subtract(discountallowance)).divide(HUNDRED, 7));
    if (h_alloweddiscount.compareTo(h_totaldiscount) < 0)
    {
      throw new BusinessException(new ApplicationMessage("MAX_TOTAL_DISCOUNT_EXCEEDED", h_alloweddiscount, discountallowance));
    }
    quoteheader.setDecimalValue(trans, "amount", h_amount);
    quoteheader.setDecimalValue(trans, "discount_amount", h_totaldiscount);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (tableRecord.isDeleted())
    {
      return;
    }

    calcTotalDiscount(tableRecord);
  }
}
