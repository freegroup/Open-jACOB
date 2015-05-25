/**
 * 
 */
package jacob.common.sap;

import jacob.exception.SapProcessingException;
import jacob.model.Sapcallexchange;
import jacob.model.Task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * Importiert die Liste der in SAP geänderterten Störstellenlisteneinträge
 * (SSLE)
 * 
 * @author Achim Böken
 */
public final class CImportCalls_Tasks
{
  private static final Map SAP_JCOB_SSLE = new HashMap();
  static
  // Mapping für SSLE
  {
    SAP_JCOB_SSLE.put("ZMNUM", Sapcallexchange.sapssleid);
    SAP_JCOB_SSLE.put("LTEXT", Sapcallexchange.imp_longtext);
    SAP_JCOB_SSLE.put("ZEQUNR", Sapcallexchange.imp_equipment);
    SAP_JCOB_SSLE.put("ZTPLNR", Sapcallexchange.imp_techplatz);
    SAP_JCOB_SSLE.put("ZBEREICH", Sapcallexchange.imp_location);
    SAP_JCOB_SSLE.put("ZPRIO", Sapcallexchange.imp_priority);
    SAP_JCOB_SSLE.put("ZSTATUS", Sapcallexchange.imp_callstatus);
    SAP_JCOB_SSLE.put("Z_VERAN", Sapcallexchange.imp_ak);
    SAP_JCOB_SSLE.put("Z_CALLNUMBER", Sapcallexchange.ttscallid);

  }
  private static final Map SAP_JCOB_SM = new HashMap();
  static
  // Mapping für SM
  {
    SAP_JCOB_SM.put("ZMNUM", Sapcallexchange.sapssleid);
    SAP_JCOB_SM.put("QMNUM", Sapcallexchange.sapsmid);
    SAP_JCOB_SM.put("LTEXT", Sapcallexchange.imp_longtext);
    SAP_JCOB_SM.put("ZEQUNR", Sapcallexchange.imp_equipment);
    SAP_JCOB_SM.put("ZTPLNR", Sapcallexchange.imp_techplatz);
    SAP_JCOB_SM.put("ZSTATUS", Sapcallexchange.imp_callstatus);
    SAP_JCOB_SM.put("AUSBS", Sapcallexchange.imp_callenddate);
    SAP_JCOB_SM.put("AUZTB", Sapcallexchange.imp_callendtime);
    SAP_JCOB_SM.put("KOSTL", Sapcallexchange.imp_costcenter);
    SAP_JCOB_SM.put("Z_CALLNUMBER", Sapcallexchange.ttscallid);

  }
  private static final Map SAP_JCOB_TASK = new HashMap();
  static
  // Mapping für Auftrag
  {
    SAP_JCOB_TASK.put("AUFNR", Sapcallexchange.saptaskid);
    SAP_JCOB_TASK.put("QMNUM", Sapcallexchange.sapsmid);
    SAP_JCOB_TASK.put("ZMNUM", Sapcallexchange.sapssleid);
    SAP_JCOB_TASK.put("Z_CALLNUMBER", Sapcallexchange.ttscallid);
    SAP_JCOB_TASK.put("ZTPLNR", Sapcallexchange.imp_techplatz);
    SAP_JCOB_TASK.put("ZEQUNR", Sapcallexchange.imp_equipment);
    SAP_JCOB_TASK.put("KOSTL", Sapcallexchange.imp_costcenter);
    SAP_JCOB_TASK.put("VAPLZ", Sapcallexchange.imp_t_hwg);
    // daten
    SAP_JCOB_TASK.put("LTEXT", Sapcallexchange.imp_longtext);
    SAP_JCOB_TASK.put("KTEXT", Sapcallexchange.imp_description);
    SAP_JCOB_TASK.put("ZSTATUS", Sapcallexchange.imp_callstatus);
    SAP_JCOB_TASK.put("AUSVN", Sapcallexchange.imp_t_probstartdate);
    SAP_JCOB_TASK.put("AUSBS", Sapcallexchange.imp_t_probenddate);
    SAP_JCOB_TASK.put("AUZTV", Sapcallexchange.imp_t_probstarttime);
    SAP_JCOB_TASK.put("AUZTB", Sapcallexchange.imp_t_probendtime);
    SAP_JCOB_TASK.put("IDAT1_FREIGABE", Sapcallexchange.imp_t_datereleased);
    SAP_JCOB_TASK.put("IDAT2_TECH_ABSCHL", Sapcallexchange.imp_t_datetechclosed);
    SAP_JCOB_TASK.put("IDAT3_ABSCHL", Sapcallexchange.imp_t_dateclosed);
    SAP_JCOB_TASK.put("GAUZT", Sapcallexchange.imp_t_totaltimespent);
    SAP_JCOB_TASK.put("GAUEH", Sapcallexchange.imp_t_tottimesp_uom);

  }

  // ----------------------------------------------------------------------------
  /**
   * Prüft, ob der Auftrag bereits im TTS vorhanden ist (Import/Update) oderob
   * es ein in SAP angelegter Auftrag ist (Create)
   * 
   * @param Context,
   *          Context
   * @param saptaskid,
   *          String, Auftragsnummer in SAP
   * @exception UserException
   */
  private static boolean isTTSOrder(Context context, String saptaskid) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor();
    IDataTable task = acc.getTable(Task.NAME);
    task.qbeClear();
    task.qbeSetKeyValue(Task.sap_auftrag, saptaskid);
    task.search();
    if (task.recordCount() == 1)
    {
      return true;
    }
    return false;
  }

  // ----------------------------------------------------------------------------
  /**
   * Import und Verarbeitung der in SAP geänderten Störstellenlisteneinträge
   * 
   * @param oCon,
   *          ConnManager, JCo Connection
   * @exception SapProcessingException
   */
  public static void getSAPSSLEUpdates(final ConnManager oCon) throws SapProcessingException
  {
    /*
     * //Wieder entfernen try{ if (CSAPHelperClass.checkDebug()) // {
     * Context.getCurrent().setPropertyForSession("SAPdebug", "1"); } else {
     * Context.getCurrent().setPropertyForSession("SAPdebug", "0"); } } catch
     * (Exception e) { // TODO: handle exception } // Ende entfernen
     */
    Map oRFCInParam = new HashMap();
    Context context = Context.getCurrent();
    IDataAccessor acc = context.getDataAccessor();
    IDataTable exchange = acc.getTable(Sapcallexchange.NAME);

    try
    {
      oRFCInParam.put("ID_CHANGE_MODE", "C");
      oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für getSAPSSLEUpdates ");
      // RFC Aufruf
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult = oFct.exec("Z_050_TTS_RFC_NOTIF_UPDATE", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN"), new CSapReturn(CSapReturn.TYPE_TABLE, "IT_050_TTS_IMEL_CHANGES") });

      if (!oResult.isSuccess())// Fehler
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   * No Success: OutputParameter für getSAPSSLEUpdates ");
        }
        //Hier soll sysout sein da meiner MEinung nach ein Exception den ganzen job abbricht, das soll nicht sein
        CSAPHelperClass.printIntoSaplog("***SAP-SCHNITTSTELLE: Fehler beim Import(SSLE)");
        // TODO: SAP Überlegen ob Exception????? oder Logrecord schreibenoder bei
        // sysout belassen

      }
      else
      {
        CTable oTbl = oResult.getPrimaryTableResult();

        CSAPHelperClass.printDebug(" **SAPExchange:" + oTbl.getNumRows() + " SSLE-Records to import **");
        for (int i = 0; i < oTbl.getNumRows(); ++i)
        {

          IDataTransaction transaction = acc.newTransaction();
          try
          {
            IDataTableRecord imprec = exchange.newRecord(transaction);

            // Sätze 1:1 schreiben
            Map oJcobData = oTbl.transferFields(SAP_JCOB_SSLE, i);
            Iterator oIter = oJcobData.entrySet().iterator();
            while (oIter.hasNext())
            {
              Map.Entry oData = (Map.Entry) oIter.next();
              imprec.setValue(transaction, (String) oData.getKey(), (String) oData.getValue());

            } // while
            imprec.setValue(transaction, Sapcallexchange.action, "import");
            imprec.setValue(transaction, Sapcallexchange.type, Sapcallexchange.type_ENUM._import);
            imprec.setValue(transaction, Sapcallexchange.exc_table, "call");
            String sProblem = oTbl.getValue("LTEXT", i);
            String sProbshort = sProblem;
            if (sProblem.length() > 237)
            {
              CSAPHelperClass.printDebug("***SAP Exchange: text gekürzt***********");
              sProbshort = sProblem.substring(0, 237);
            }
            imprec.setValue(transaction, Sapcallexchange.imp_description, sProbshort);
            transaction.commit();
          }
          catch (Exception e)
          {
            throw new Exception("***SAP-SCHNITTSTELLE: Fehler beim Schreiben der Importdatei", e);
          }
          finally
          {
            transaction.close();
          }

          // Werte Einzeln schreiben
          // String sSapValue = oTbl.getValue("sapfield",i);

          // for (int j = 0; j < oTbl.getNumCols(); ++j) {
          // String sColName = oTbl.getFieldName(j);
          // String sValue = oTbl.getValue(i, j);

          // }

        }
      }
    }
    catch (Exception e)
    {
      throw new SapProcessingException(e);
    }
  }

  // ----------------------------------------------------------------------------
  /**
   * Import und Verarbeitung der in SAP geänderten Störmeldungen
   * 
   * @param oCon,
   *          ConnManager, JCo Connection
   * @exception SapProcessingException
   */
  public static void getSAPSMUpdates(final ConnManager oCon) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    Context context = Context.getCurrent();
    IDataAccessor acc = context.getDataAccessor();
    IDataTable exchange = acc.getTable(Sapcallexchange.NAME);

    try
    {
      oRFCInParam.put("ID_CHANGE_MODE", "N");
      oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für getSAPSMUpdates ");
      // RFC Aufruf
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult = oFct.exec("Z_050_TTS_RFC_SAP_MELD", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_TABLE, "ET_050_TTS_MEL_CHANGES"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });
      if (!oResult.isSuccess())// Fehler
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success: OutputParameter für getSAPSMUpdates ");
        }
        //Hier soll sysout sein s.u.
        CSAPHelperClass.printIntoSaplog("***SAP-SCHNITTSTELLE: Fehler beim Import (SM)");
        // TODO: SAP s. oÜberlegen
      }
      else
      {
        CTable oTbl = oResult.getPrimaryTableResult();
        CSAPHelperClass.printDebug(" **SAPExchange:" + oTbl.getNumRows() + " SM-Records to import **");
        for (int i = 0; i < oTbl.getNumRows(); ++i)
        {
          IDataTransaction transaction = acc.newTransaction();
          try
          {
            IDataTableRecord imprec = exchange.newRecord(transaction);

            // Sätze 1:1 schreiben
            Map oJcobData = oTbl.transferFields(SAP_JCOB_SM, i);
            Iterator oIter = oJcobData.entrySet().iterator();
            while (oIter.hasNext())
            {
              Map.Entry oData = (Map.Entry) oIter.next();
              imprec.setValue(transaction, (String) oData.getKey(), (String) oData.getValue());

            } // while
            imprec.setValue(transaction, Sapcallexchange.action, "import");
            imprec.setValue(transaction, Sapcallexchange.type, Sapcallexchange.type_ENUM._import);
            imprec.setValue(transaction, Sapcallexchange.exc_table, "call");
            String sProblem = oTbl.getValue("LTEXT", i);
            String sProbshort = sProblem;
            if (sProblem.length() > 237)
            {
              CSAPHelperClass.printDebug("*** SAP-Schnittstelle text gekürzt***********");
              sProbshort = sProblem.substring(0, 237);
            }
            imprec.setValue(transaction, Sapcallexchange.imp_description, sProbshort);
            transaction.commit();
          }
          finally
          {
            transaction.close();
          }
        }
      }
    }
    catch (Exception e)
    {
      throw new SapProcessingException(e);
    }
  }

  // ----------------------------------------------------------------------------
  /**
   * Import und Verarbeitung der in SAP geänderten und neu angelegten Aufträge
   * 
   * @param oCon,
   *          ConnManager, JCo Connection
   * @exception SapProcessingException
   */
  public static void getSAPOrders(final ConnManager oCon) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    Context context = Context.getCurrent();
    IDataAccessor acc = context.getDataAccessor();
    IDataTable exchange = acc.getTable(Sapcallexchange.NAME);

    try
    {
      oRFCInParam.put("ID_CHANGE_MODE", "O");
      oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für getSAPOrders ");
      // RFC Aufruf
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult = oFct.exec("Z_050_TTS_RFC_SAP_MELD", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_TABLE, "ET_050_TTS_AUF_CHANGES"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });
      if (!oResult.isSuccess())
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   * No Success: OutputParameter für getSAPOrders ");
        }
        //Hier soll sysout sein s.u.
        CSAPHelperClass.printIntoSaplog("***SAP-SCHNITTSTELLE: Fehler beim Import (Aufträge)");
        // TODO S.o.
      }
      else
      {
        CTable oTbl = oResult.getPrimaryTableResult();
        CSAPHelperClass.printDebug(" **SAPExchange:" + oTbl.getNumRows() + " Order-Records to import **");
        for (int i = 0; i < oTbl.getNumRows(); ++i)
        {
          IDataTransaction transaction = acc.newTransaction();
          try
          {
            IDataTableRecord imprec = exchange.newRecord(transaction);

            // Sätze 1:1 schreiben
            Map oJcobData = oTbl.transferFields(SAP_JCOB_TASK, i);
            Iterator oIter = oJcobData.entrySet().iterator();
            while (oIter.hasNext())
            {
              Map.Entry oData = (Map.Entry) oIter.next();
              imprec.setValue(transaction, (String) oData.getKey(), (String) oData.getValue());
            } // while

            if (isTTSOrder(context, oTbl.getValue("AUFNR", i)))
            {
              imprec.setValue(transaction, Sapcallexchange.action, "import");
            }
            else
            {
              imprec.setValue(transaction, Sapcallexchange.action, "create");
            }
            imprec.setValue(transaction, Sapcallexchange.type, Sapcallexchange.type_ENUM._import);
            imprec.setValue(transaction, Sapcallexchange.exc_table, "task");

            transaction.commit();
          }
          finally
          {
            transaction.close();
          }
        }
      }
    }
    catch (Exception e)
    {
      throw new SapProcessingException(e);
    }

  }

  // ----------------------------------------------------------------------------
  /*
   * verbotener Konstruktor
   */
  private CImportCalls_Tasks()
  {
    // NIX
  }
}
