/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import java.math.BigDecimal;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andreas
 */
public class OrderlineitemTableRecord extends DataTableRecordEventHandler
{
  private static final BigDecimal ZERO = BigDecimal.valueOf(0);
  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord orderlineitem, IDataTransaction trans) throws Exception
	{
    IDataTableRecord orderheader = orderlineitem.getLinkedRecord("orderheader");
    if (orderlineitem.isDeleted())
    {
      if (!orderheader.isDeleted())
      {
        BigDecimal positionamount = orderlineitem.getSaveDecimalValue("position_amount");
        
        orderheader.setDecimalValue(trans, "amount", orderheader.getSaveDecimalValue("amount").subtract(positionamount));
      }
      
      return;
    }

    // Note: Do not fetch list price from order item or do price calculating, if
    // line item was created from an existing quote
    boolean orderlineitemCreatedFromQuote = orderlineitem.isNew() && orderheader!=null && orderheader.hasLinkedRecord("quoteheader");
    if (!orderlineitemCreatedFromQuote)
    {
      // get list price and uom from order part
      //
      if (orderlineitem.hasChangedValue("orderpart_key") && !orderlineitemCreatedFromQuote)
      {
        IDataTableRecord orderpart = orderlineitem.getLinkedRecord("orderOrderpart");
        if (orderpart != null)
        {
          orderlineitem.setValue(trans, "price", orderpart.getValue("price"));
          orderlineitem.setValue(trans, "uom", orderpart.getValue("uom"));
          if (orderlineitem.hasNullValue("description"))
            orderlineitem.setStringValueWithTruncation(trans, "description", orderpart.getStringValue("description"));
        }
      }

      // Price Calculating
      //
      if (orderlineitem.hasChangedValue("price") || orderlineitem.hasChangedValue("quantity"))
      {
        BigDecimal listprice = orderlineitem.getSaveDecimalValue("price");
        BigDecimal quantity = BigDecimal.valueOf(orderlineitem.getintValue("quantity"));
        BigDecimal positionamount = listprice.multiply(quantity);

        orderlineitem.setValue(trans, "position_amount", positionamount);

        BigDecimal old_position_amount = orderlineitem.getOldSaveDecimalValue("position_amount");
        orderheader.setDecimalValue(trans, "amount", orderheader.getSaveDecimalValue("amount").add(positionamount).subtract(old_position_amount));
      }
    }
	}
}
