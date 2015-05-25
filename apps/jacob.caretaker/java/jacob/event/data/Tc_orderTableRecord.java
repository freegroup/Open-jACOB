/*
 * Created on 22.03.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.Yan;
import jacob.common.tc.TC;
import jacob.exception.BusinessException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.screen.IClientContext;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Tc_orderTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: Tc_orderTableRecord.java,v 1.11 2008/03/16 03:22:20 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.11 $";

  static protected final transient Log logger = AppLogger.getLogger();

  private static final String STORNO_PROPERTY = "Tc_orderTableRecord_storno";
  
  protected static boolean isOrderStornoActive()
  {
    return Context.getCurrent().getProperty(STORNO_PROPERTY) != null;
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // just to mark, that we are going to delete the order
    Context.getCurrent().setPropertyForRequest(STORNO_PROPERTY, Boolean.TRUE);
    
    IDataTableRecord capacityRecord = tableRecord.getLinkedRecord("tc_capacity");
    // unlink records
    if (false == capacityRecord.setValue(transaction, "tc_order_key", null))
    {
      // Kann nur vorkommen, wenn eine Buchung angelegt wird und gleich wieder
      // storniert wird.
      // Dann nochmals laden und Änderung durchführen.
      //
      capacityRecord = capacityRecord.getTable().loadRecord(capacityRecord.getPrimaryKeyValue());
      capacityRecord.setValue(transaction, "tc_order_key", null);
    }
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // Initialize order with email of customer
    //
    IDataTableRecord customerRecord = tableRecord.getAccessor().getTable("tc_customer").getSelectedRecord();
    if (customerRecord != null)
      tableRecord.setValue(transaction, "customerEmail", customerRecord.getValue("emailcorr"));
  }

  public void beforeCommitAction(IDataTableRecord orderRecord, IDataTransaction transaction) throws Exception
  {
    IDataTableRecord ticketRecord = null;
    if (orderRecord.isDeleted())
    {
      // Stornoticket erzeugen
      ticketRecord = TC.createStornoCall(Context.getCurrent(), transaction, orderRecord.getLinkedRecord("tc_capacity").getTimestampValue("slot"));
 
      // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
      // SAP - Neuen Call in die Übertragungstabelle Schreiben
      // ------------------------------------------------------------

      jacob.event.data.CallTableRecord.WriteNewSapRec(ticketRecord); 

      // Hier Stornobenachrichtigung erzeugen, da in afterCommitAction() Record nicht
      // mehr in der DB existiert
      // und Yan.fillDBFields() Probleme bekommt.
      sendNotification(orderRecord, transaction, "Räderwechselstorno");
    }
    else if (orderRecord.isNew())
    {
      // Agent setzen
      orderRecord.setValue(transaction, "tc_agent_key", transaction.getUser().getKey());

      // Kapazitätseintrag sperren
      //
      IDataTableRecord capacityRecord = orderRecord.getLinkedRecord("tc_capacity");
      if (capacityRecord != null)
      {
        // Überprüfen, ob der Kapazitätseintrag gerade für eine andere Buchung
        // vergeben wurde.
        // Anmerkung: Record erst einmal sperren und anschließend nochmals von
        // der Datenbank lesen.
        //
        transaction.lock(capacityRecord);
        capacityRecord = capacityRecord.getTable().loadRecord(capacityRecord.getPrimaryKeyValue());
        if (capacityRecord.getValue("tc_order_key") != null)
        {
          throw new BusinessException("Der gewünschte Termin wurde in der Zwischenzeit bereits vergeben.");
        }

        // double link records
        capacityRecord.setValue(transaction, "tc_order_key", orderRecord.getValue("pkey"));

        // Buchungsticket erzeugen
        ticketRecord = TC.createOrderCall(Context.getCurrent(), transaction, capacityRecord.getTimestampValue("slot"));
 
        // ID des Tickets in Order merken
        orderRecord.setValue(transaction, "tc_call_key", ticketRecord.getValue("pkey"));
        jacob.event.data.CallTableRecord.WriteNewSapRec(ticketRecord);
        // -------------------------------------<<<<<<<<<<<<<<<<<<<<-----------------------
        // SAP - Neuen Call in die Übertragungstabelle Schreiben
        // ------------------------------------------------------------
      }
    }
    else if (orderRecord.isUpdated())
    {
      if (orderRecord.hasChangedValue("tc_capacity_key"))
      {
        throw new BusinessException("Die Buchung muß storniert werden, um den Termin zu ändern");
      }
      if (orderRecord.hasChangedValue("tc_customer_key"))
      {
        throw new BusinessException("Die Buchung muß storniert werden, um den Kunden zu ändern");
      }
      if (orderRecord.hasChangedValue("tc_object_key"))
      {
        throw new BusinessException("Die Buchung muß storniert werden, um das Fahrzeug zu ändern");
      }
    }
    
    // Ticket in Context zwischenmerken, damit die Koordinationszeit noch per
    // Benutzerinteraktion eingetragen werden kann.
    // Note: Wird so realisiert, da es noch kein IActionEmitter.resumeAction() gibt!
    //
    if (ticketRecord != null)
    {
      Context context = Context.getCurrent();
      if (context instanceof IClientContext)
      {
        context.setPropertyForRequest(TICKET_PROPERTY, ticketRecord);
      }
    }
  }
  
  private static final String TICKET_PROPERTY = "Tc_orderTableRecord_ticket";
  
  public static IDataTableRecord getOrderTicket(IClientContext context)
  {
    return (IDataTableRecord) context.getProperty(TICKET_PROPERTY);
  }

  private static final DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yy");
  private static final DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
  
  private static void sendNotification(IDataTableRecord orderRecord, IDataTransaction transaction, String type) throws Exception
  {
    String email = orderRecord.getStringValue("customerEmail");
    if (email != null)
    {
      IDataTable table = orderRecord.getAccessor().getTable("doc_template");
      table.qbeClear();
      table.qbeSetKeyValue("use_in", type);
      table.setMaxRecords(1);
      table.search();
      IDataTableRecord docTemplate = table.getSelectedRecord();
      if (docTemplate == null)
      {
        logger.warn("There is no template of type '" + type + "'");
        return;
      }

      Context context = Context.getCurrent();

      // XML-Vorlage für die Buchung holen und mit den Datenbankfeldern
      // auffüllen
      //
      String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
      template = Yan.fillDBFields(context, orderRecord, docTemplate, template, true, null);

      // Hack: Because we could not add a format to db_field(field, format)
      //
      Date slot = orderRecord.getLinkedRecord("tc_capacity").getTimestampValue("slot");
      template = StringUtils.replace(template, "$SLOTDATE$", dateFormatter.format(slot));
      template = StringUtils.replace(template, "$SLOTTIME$", timeFormatter.format(slot));

      Yan.createInstance(context, transaction, template, "email://" + email, docTemplate.getStringValue("email_xsl"));
    }
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    if (!tableRecord.isDeleted())
    {
      // Benachrichtigung schicken, wenn neue Buchung oder Email von Buchung geändert
      if (tableRecord.hasChangedValue("customerEmail"))
      {
        IDataTransaction transaction = tableRecord.getAccessor().newTransaction();
        try
        {
          sendNotification(tableRecord, transaction, "Räderwechsel");
          transaction.commit();
        }
        finally
        {
          transaction.close();
        }
      }
    }
  }
}
