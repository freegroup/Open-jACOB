/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.exception.BusinessException;

import java.math.BigDecimal;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.i18n.ApplicationMessage;

/**
 * 
 * @author andreas
 */
public class QuotelineitemTableRecord extends DataTableRecordEventHandler
{
  private static final BigDecimal ZERO = BigDecimal.valueOf(0);
  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord quotelineitem, IDataTransaction trans) throws Exception
  {
    if (quotelineitem.isDeleted())
    {
      IDataTableRecord quoteheader = quotelineitem.getLinkedRecord("quoteheader");
      if (!quoteheader.isDeleted())
      {
        BigDecimal baseprice = quotelineitem.getSaveDecimalValue("baseprice");
        BigDecimal positionamount = quotelineitem.getSaveDecimalValue("position_amount");
        BigDecimal discountamount = quotelineitem.getSaveDecimalValue("discount_amount");

        quoteheader.setDecimalValue(trans, "base_amount", quoteheader.getSaveDecimalValue("base_amount").subtract(baseprice));
        quoteheader.setDecimalValue(trans, "position_amount", quoteheader.getSaveDecimalValue("position_amount").subtract(positionamount));
        quoteheader.setDecimalValue(trans, "pos_discount_amount", quoteheader.getSaveDecimalValue("pos_discount_amount").subtract(discountamount));

        QuoteheaderTableRecord.calcTotalDiscount(quoteheader);
      }

      return;
    }

    // get list price and uom from order part
    //
    if (quotelineitem.hasChangedValue("orderpart_key"))
    {
      IDataTableRecord orderpart = quotelineitem.getLinkedRecord("quoteOrderpart");
      if (orderpart != null)
      {
        quotelineitem.setValue(trans, "price", orderpart.getValue("price"));
        quotelineitem.setValue(trans, "uom", orderpart.getValue("uom"));
        if (quotelineitem.hasNullValue("description"))
          quotelineitem.setStringValueWithTruncation(trans, "description", orderpart.getStringValue("description"));
      }
    }

    // Discount/Price Calculating
    //
    if (quotelineitem.hasChangedValue("price") || quotelineitem.hasChangedValue("quantity") || quotelineitem.hasChangedValue("discount_percent")
        || quotelineitem.hasChangedValue("discount_fix"))
    {
      BigDecimal listprice = quotelineitem.getSaveDecimalValue("price");
      BigDecimal quantity = BigDecimal.valueOf(quotelineitem.getintValue("quantity"));
      BigDecimal positionamount = ZERO;
      BigDecimal discountamount = ZERO;
      BigDecimal baseprice = ZERO;
      BigDecimal discountpercent = quotelineitem.getSaveDecimalValue("discount_percent");
      BigDecimal discountfix = quotelineitem.getSaveDecimalValue("discount_fix");
      baseprice = baseprice.add(listprice).multiply(quantity);

      positionamount = positionamount.add(baseprice).subtract(discountfix).multiply(HUNDRED.subtract(discountpercent)).divide(HUNDRED, 7);
      discountamount = discountamount.add(baseprice).subtract(positionamount);

      // Check discount allowance
      //
      BigDecimal discountallowance = (BigDecimal) trans.getUser().getProperty("discount");
      BigDecimal alloweddiscount = baseprice.subtract(baseprice.multiply(HUNDRED.subtract(discountallowance)).divide(HUNDRED, 7));
      if (alloweddiscount.compareTo(discountamount) < 0)
      {
        throw new BusinessException(new ApplicationMessage("MAX_TOTAL_DISCOUNT_EXCEEDED", alloweddiscount, discountallowance));
      }
      quotelineitem.setValue(trans, "baseprice", baseprice);
      quotelineitem.setValue(trans, "position_amount", positionamount);
      quotelineitem.setValue(trans, "discount_amount", discountamount);

      // Set values in quote header
      //

      //      // First add current lineitem
      //      BigDecimal h_baseprice =
      // quotelineitem.getSaveDecimalValue("baseprice");
      //      BigDecimal h_positionamount =
      // quotelineitem.getSaveDecimalValue("position_amount");
      //      BigDecimal h_discount_amount =
      // quotelineitem.getSaveDecimalValue("discount_amount");
      //
      //      // Now all the positions
      //      IDataTable q_line =
      // quotelineitem.getAccessor().getTable("quotelineitem");
      //      q_line.qbeSetValue("pkey", "!" +
      // quotelineitem.getSaveStringValue("pkey"));
      //      q_line.qbeSetKeyValue("quoteheader_key",
      // quotelineitem.getSaveStringValue("quoteheader_key"));
      //      q_line.search();
      //
      //      for (int i = 0; i < q_line.recordCount(); i++)
      //      {
      //        h_baseprice =
      // h_baseprice.add(q_line.getRecord(i).getSaveDecimalValue("baseprice"));
      //        h_positionamount =
      // h_positionamount.add(q_line.getRecord(i).getSaveDecimalValue("position_amount"));
      //        h_discount_amount =
      // h_discount_amount.add(q_line.getRecord(i).getSaveDecimalValue("discount_amount"));
      //      }

      BigDecimal old_baseprice = quotelineitem.getOldSaveDecimalValue("baseprice");
      BigDecimal old_position_amount = quotelineitem.getOldSaveDecimalValue("position_amount");
      BigDecimal old_discount_amount = quotelineitem.getOldSaveDecimalValue("discount_amount");
      IDataTableRecord quoteheader = quotelineitem.getLinkedRecord("quoteheader");
      quoteheader.setDecimalValue(trans, "base_amount", quoteheader.getSaveDecimalValue("base_amount").add(baseprice).subtract(old_baseprice));
      quoteheader.setDecimalValue(trans, "position_amount", quoteheader.getSaveDecimalValue("position_amount").add(positionamount)
          .subtract(old_position_amount));
      quoteheader.setDecimalValue(trans, "pos_discount_amount", quoteheader.getSaveDecimalValue("pos_discount_amount").add(discountamount).subtract(
          old_discount_amount));

      QuoteheaderTableRecord.calcTotalDiscount(quoteheader);
    }
  }
}
