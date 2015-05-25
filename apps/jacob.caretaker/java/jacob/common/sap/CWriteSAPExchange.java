/**
 * 
 */
package jacob.common.sap;

import jacob.model.Sapcallexchange;
import jacob.model.Task;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * @author Achim Böken
 * 
 */

public class CWriteSAPExchange
{
  /**
   * 
   */
  public CWriteSAPExchange()
  {
  }

  /**
   * Erzeugt einen Eintrag in der SAPExchange Tabelle zur Erstellung eines neuen
   * SSLE
   * 
   * @param call,
   *          IDataTableRecord, der zu erstellende Datensatz
   * @exception Exception
   */
  public static void createExchangeCall(IDataTableRecord call) throws Exception
  {
    // TODO Prüfen ob sapnummer vorhanden??
    IDataAccessor acc = call.getAccessor().newAccessor();
    IDataTransaction transaction = call.getCurrentTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "create");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "call");
    exchangerec.setValue(transaction, "ttscallid", call.getSaveStringValue("pkey"));
  }

  /**
   * Erzeugt einen Eintrag in der SAPExchange Tabelle zur Änderung eines
   * bestehenden SSLE
   * 
   * @param call,
   *          IDataTableRecord, der zu ändernde Datensatz
   * @exception Exception
   */
  public static void updateExchangeCall(IDataTableRecord call) throws Exception
  {
    IDataAccessor acc = call.getAccessor().newAccessor();

    IDataTransaction transaction = call.getCurrentTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "update");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "call");
    exchangerec.setValue(transaction, "ttscallid", call.getSaveStringValue("pkey"));
    exchangerec.setValue(transaction, "sapssleid", call.getSaveStringValue("sap_ssle_nr"));
    exchangerec.setValue(transaction, "sapsmid", call.getSaveStringValue("sap_sm_nr"));
  }

  public static void updateSaveExchangeCall(IDataTableRecord call) throws Exception
  {
    IDataAccessor acc = call.getAccessor().newAccessor();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTransaction transaction = sapexchange.startNewTransaction();

    try
    {
      IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
      exchangerec.setValue(transaction, "action", "update");
      exchangerec.setValue(transaction, "type", "export");
      exchangerec.setValue(transaction, "exc_table", "call");
      exchangerec.setValue(transaction, "ttscallid", call.getSaveStringValue("pkey"));
      exchangerec.setValue(transaction, "sapssleid", call.getSaveStringValue("sap_ssle_nr"));
      exchangerec.setValue(transaction, "sapsmid", call.getSaveStringValue("sap_sm_nr"));
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
  }

  public static void closeExchangeCall(IDataTableRecord call) throws Exception
  {
    IDataAccessor acc = call.getAccessor().newAccessor();
    IDataTransaction transaction = call.getCurrentTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "close");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "call");
    exchangerec.setValue(transaction, "ttscallid", call.getSaveStringValue("pkey"));
    exchangerec.setValue(transaction, "sapssleid", call.getSaveStringValue("sap_ssle_nr"));
  }

  public static void closeSaveExchangeCall(IDataTableRecord call) throws Exception
  {
    IDataAccessor acc = call.getAccessor().newAccessor();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTransaction transaction = sapexchange.startNewTransaction();
    try
    {
      IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
      exchangerec.setValue(transaction, "action", "close");
      exchangerec.setValue(transaction, "type", "export");
      exchangerec.setValue(transaction, "exc_table", "call");
      exchangerec.setValue(transaction, "ttscallid", call.getSaveStringValue("pkey"));
      exchangerec.setValue(transaction, "sapssleid", call.getSaveStringValue("sap_ssle_nr"));
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
  }

  /**
   * Erzeugt einen Eintrag in der SAPExchange Tabelle zur Stornierung eines SSLE
   * 
   * @param call,
   *          IDataTableRecord, der zu erstellende Datensatz
   * @exception Exception
   */
  public static void deleteExchangeCall(IDataTableRecord call) throws Exception
  {
    IDataAccessor acc = call.getAccessor().newAccessor();
    IDataTransaction transaction = call.getCurrentTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "delete");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "call");
    exchangerec.setValue(transaction, "ttscallid", call.getSaveStringValue("pkey"));
    exchangerec.setValue(transaction, "sapssleid", call.getSaveStringValue("sap_ssle_nr"));
  }

  /**
   * Erzeugt einen Eintrag in der SAPExchange Tabelle zur Erstellung eines neuen
   * Auftrages
   * 
   * @param call,
   *          IDataTableRecord, der zu erstellende Datensatz
   * @exception Exception
   */
  public static void createExchangeTask(IDataTableRecord task) throws Exception
  {
    IDataAccessor acc = task.getAccessor().newAccessor();
    IDataTransaction transaction = task.getCurrentTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "create");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "task");
    exchangerec.setValue(transaction, "ttstaskno", task.getSaveStringValue("taskno"));
    exchangerec.setValue(transaction, "ttstaskpkey", task.getSaveStringValue("pkey"));
    exchangerec.setValue(transaction, "ttscallid", task.getSaveStringValue("calltask"));
  }

  public static void updateExchangeTask(IDataTableRecord task) throws Exception
  {
    IDataAccessor acc = task.getAccessor().newAccessor();
    IDataTransaction transaction = task.getCurrentTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "update");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "task");
    exchangerec.setValue(transaction, "ttstaskno", task.getSaveStringValue("taskno"));
    exchangerec.setValue(transaction, "ttstaskpkey", task.getSaveStringValue("pkey"));
    exchangerec.setValue(transaction, "ttscallid", task.getSaveStringValue("calltask"));
    exchangerec.setValue(transaction, Sapcallexchange.saptaskid, task.getSaveStringValue(Task.sap_auftrag));
  }

  public static void closeExchangeTask(IDataTableRecord task) throws Exception
  {
    IDataAccessor acc = task.getAccessor().newAccessor();
    IDataTransaction transaction = task.getCurrentTransaction();
    IDataTable sapexchange = acc.getTable("sapcallexchange");
    IDataTableRecord exchangerec = sapexchange.newRecord(transaction);
    exchangerec.setValue(transaction, "action", "close");
    exchangerec.setValue(transaction, "type", "export");
    exchangerec.setValue(transaction, "exc_table", "task");
    exchangerec.setValue(transaction, "ttstaskno", task.getSaveStringValue("taskno"));
    exchangerec.setValue(transaction, "ttstaskpkey", task.getSaveStringValue("pkey"));
    exchangerec.setValue(transaction, "ttscallid", task.getSaveStringValue("calltask"));
    exchangerec.setValue(transaction, Sapcallexchange.saptaskid, task.getSaveStringValue(Task.sap_auftrag));
  }
}
