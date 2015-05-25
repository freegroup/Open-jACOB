/**
 * 
 */
package jacob.common.sap;

import jacob.exception.SapProcessingException;
import jacob.model.Calls;
import jacob.model.Sapcallexchange;
import jacob.model.Task;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;

/**
 * @author achim b�ken
 */
public class CExportTaskRFC
{

  /**
   * 
   */
  // Daten zusammenstellen
  private static Map getTaskData(final IDataTableRecord task) throws Exception
  {
    // TODO Unterschied zwischen new und Update einf�gen
    // damit die Entsprechenden felder gesetzt werden (allg Felder
    // Felder bei neu und Felder bei Update

    // 1. RFC: Z_050_TTS_RFC_SAP_MELD
    // Import-Parameter Beschreibung
    // ID_QMNUM Meldungsnummer St�rstelle
    // ID_CALLNUMBER Nummer eines Calls im TTS-System (Pkey)
    // ID_TTS_AUFNR Nummer eines Auftrags im TTS-System
    // ID_FUNCT_LOC Techn. Platz bzw. Equipment
    // ID_WERK Instandhaltungsplanungswerk
    // ID_WORK_CENTER Gewerk (Handwerkergruppe)
    // ID_CHANGE_MODE VerarbeitgsMod.: (A)=Create, (U)pdate, (D)elete,
    // (R)elease u. (C)ollect
    // ID_SYSTEM Ursprungssystem des Vorgangs
    // ID_QMNUM_MEL Meldungsnummer
    // ID_ZSTATUS Einzelstatus eines Objekts
    // ID_AUFNR Auftragsnummer
    // ID_QMTXT Kurztext
    // ID_KOSTL Kostenstelle
    // ID_AUSVN St�rungsbeginn (Datum)
    // ID_AUSBS St�rungsende (Datum)
    // ID_AUZTV St�rungsbeginn (Uhrzeit)
    // ID_AUZTB St�rungsende (Uhrzeit)
    // ID_CLOSEDATE Bezugsdatum
    // ID_CLOSETIME Bezugszeit
    // ID_GAUZT Gesamtaufwand
    // ID_GAUEH Einheit der Arbeit im ISO-Code
    // ID_LTEXT String mit 7200 L�nge

    Map oRFCInParam = new HashMap();

    oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
    String sobject = "";
    if (task.hasLinkedRecord("taskobject"))
    {
      sobject = task.getLinkedRecord("taskobject").getSaveStringValue("external_id");
    }
    else
    {
      CSAPHelperClass.printDebug("**** TASKUPDATE OBJECT NOT FOUND!");
    }

    oRFCInParam.put("ID_FUNCT_LOC", sobject);// TechPlatz
    String sWorkgroup = "";
    if (task.hasLinkedRecord("taskworkgroup"))
    {
      sWorkgroup = task.getLinkedRecord("taskworkgroup").getSaveStringValue("hwg_name");
    }
    else
    {
      CSAPHelperClass.printDebug("**** TASKUPDATE HWG NOT FOUND!");
    }
    oRFCInParam.put("ID_WORK_CENTER", sWorkgroup);// HWG

    oRFCInParam.put("ID_LTEXT", task.getValue(Task.description));// HWG

    // ID_QMTXT Kurztext
    oRFCInParam.put("ID_QMTXT", task.getValue(Task.summary));// HWG

    // ID_GAUZT Gesamtaufwand
    // ID_GAUEH Einheit der Arbeit im ISO-Code
    /*
     * // Alte Varaiante mit �bergabe in Stunden, Soll ge�ndertw erden in
     * Minuten BigDecimal totmin = new BigDecimal("0"); BigDecimal bNull = new
     * BigDecimal("0"); BigDecimal toth = new BigDecimal("0"); BigDecimal h =
     * new BigDecimal("60"); int iH = 60; if
     * (!"".equals(task.getSaveStringValue(Task.totaltimespent_h))) // { toth =
     * toth.add(new BigDecimal(task.getSaveStringValue(Task.totaltimespent_h))); }
     * if (!"".equals(task.getSaveStringValue(Task.totaltimespent_m))) // {
     * totmin = totmin.add(new
     * BigDecimal(task.getSaveStringValue(Task.totaltimespent_m))); totmin =
     * totmin.divide(h,4,0); } toth = toth.add(totmin);
     * 
     * if (!bNull.equals(toth)) { oRFCInParam.put("ID_GAUZT",
     * toth.toString());// Arbeitszeit oRFCInParam.put("ID_GAUEH", "H");//
     * Arbeitszeit Einheit (Hier // Stunden) }
     */

    // *Neue Variante in Minuten
    BigDecimal totmin = new BigDecimal("0");
    BigDecimal bNull = new BigDecimal("0");
    BigDecimal toth = new BigDecimal("0");
    BigDecimal h = new BigDecimal("60");
    if (!"".equals(task.getSaveStringValue(Task.totaltimespent_h))) //
    {
      toth = toth.add(new BigDecimal(task.getSaveStringValue(Task.totaltimespent_h)));
      // Umrechnung in Minuten
      toth = toth.multiply(h);
    }
    if (!"".equals(task.getSaveStringValue(Task.totaltimespent_m))) //
    {
      totmin = totmin.add(new BigDecimal(task.getSaveStringValue(Task.totaltimespent_m)));

    }

    toth = toth.add(totmin);

    if (!bNull.equals(toth))
    {
      oRFCInParam.put("ID_GAUZT", toth.toString());// Arbeitszeit
      oRFCInParam.put("ID_GAUEH", "MIN");// Arbeitszeit Einheit (Hier
      // Minuten)

    }

    // ID_ZSTATUS Einzelstatus eines Objekts
    Map mStat = new HashMap();
    // TODO Status Dokumentiert �ndern: Aufruf mit param r (�berlegen)
    mStat.put(Task.taskstatus_ENUM._Fertig_gemeldet, "FERT");
    // Interrimsl�sung: Wir setzten auch bei Dokumentiert Statzs FERT in SAP;
    // bis ein Benutzerstatus "erfunden" wird
    // mStat.put(Task.taskstatus_ENUM._Dokumentiert, "TABG");
    mStat.put(Task.taskstatus_ENUM._Dokumentiert, "FERT");
    // Hier noch pr�fen ob besser TABG
    mStat.put(Task.taskstatus_ENUM._Abgerechnet, "ABGS");
    String sTstatSAP = "";
    if (mStat.containsKey(task.getSaveStringValue(Task.taskstatus))) //
    {
      sTstatSAP = (String) mStat.get(task.getSaveStringValue(Task.taskstatus));
    }
    oRFCInParam.put("ID_ZSTATUS", sTstatSAP);// Status

    return oRFCInParam;
  }

  // Aufruf RFC
  public static void createTask(final ConnManager oCon, final IDataTableRecord task) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    try
    {
      oRFCInParam = getTaskData(task);
      oRFCInParam.put("ID_CHANGE_MODE", "A");
      String sSSLEnum = task.getLinkedRecord("call").getSaveStringValue("sap_ssle_nr");
      oRFCInParam.put("ID_QMNUM", sSSLEnum);// SSLE
      String sSMnum = task.getLinkedRecord("call").getSaveStringValue("sap_sm_nr");
      oRFCInParam.put("ID_QMNUM_MEL", sSMnum);// SM
      String sCallnum = task.getSaveStringValue("calltask");
      oRFCInParam.put("ID_CALLNUMBER", sCallnum);// TTSCALL
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter f�r createTask ");
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult;

      oResult = oFct.exec("Z_050_TTS_RFC_SAP_MELD", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_AUFNR"), new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM"), /* St�rmeldung!!!! */
        new CSapReturn(CSapReturn.TYPE_FIELD, "ED_CALLNUMBER"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });

      if (!oResult.isSuccess())
      {
        String sMES = oResult.getLogMessage();

        // Es kann Sein,dass die S�rmeldung erfolgreich angelegt wurde,
        // der Auftrag aber nicht.
        // Deswegen pr�fen wir, ob es einen R�ckgabewert f�r die
        // St�rmeldung gibt und schreiben ihn
        // in den Call zur�ck.

        // SAP St�rmeldung Nr in Meldung zur�ckschreiben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          // Debug: Outputparameter ausgeben
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success OutputParameter f�r createTask ");
          String sCall = (String) oData.get("ED_CALLNUMBER");
          String sSM = (String) oData.get("ED_QMNUM");
          if (sCall.length() == 0 || sSM.length() == 0)
          {
            // Callnummer oder SM Nummer nicht vorhanden, wir schreiben Error
            throw new SapProcessingException(sMES);
          }

          // IDataAccessor retAcc = task.getAccessor().newAccessor();
          // IDataTable call = retAcc.getTable("call");
          //
          // // int myInt=Integer.parseInt(sTest);
          //
          // call.qbeSetKeyValue("pkey", sCall);
          // call.search();
          // if (call.recordCount() == 1)
          // {
          //
          // IDataTableRecord callrec = call.getRecord(0);
          // IDataTransaction retTrans = retAcc.newTransaction();
          // try
          // {
          // callrec.setValue(retTrans, "sap_sm_nr", oData.get("ED_QMNUM"));
          // retTrans.commit();
          // }
          // finally
          // {
          // retTrans.close();
          // }
          // }

          SQLDataSource caretaker = (SQLDataSource) DataSource.get("caretakerDataSource");
          Connection con = caretaker.getConnection();
          try
          {

            Statement statemet = con.createStatement();
            try
            {

              String sSql = "UPDATE caretaker.calls SET sap_sm_nr='" + oData.get("ED_QMNUM") + "' WHERE pkey=" + sCall;
              statemet.executeUpdate(sSql);
              con.commit();
            }
            finally
            {
              statemet.close();
            }
          }

          finally
          {
            con.close();
          }
        }
        throw new SapProcessingException(sMES);
      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success, OutputParameter f�r createTask ");
        // SAP St�rmeldung Nr in Meldung zur�ckschreiben
        IDataAccessor retAcc = task.getAccessor().newAccessor();
        IDataTable call = retAcc.getTable("call");
        String sCall = (String) oData.get("ED_CALLNUMBER");
        // int myInt=Integer.parseInt(sTest);
        call.qbeSetKeyValue("pkey", sCall);
        call.search();
        if (call.recordCount() == 1)
        {
          // IDataTableRecord callrec = call.getRecord(0);
          // IDataTransaction retTrans = retAcc.newTransaction();
          // try
          // {
          // callrec.setValue(retTrans, "sap_sm_nr", oData.get("ED_QMNUM"));
          // task.setValue(retTrans, "sap_auftrag", oData.get("ED_AUFNR"));
          // retTrans.commit();
          // }
          // finally
          // {
          // retTrans.close();
          // }
          // Call zur�ckschreiben
          SQLDataSource caretaker = (SQLDataSource) DataSource.get("caretakerDataSource");
          Connection con = caretaker.getConnection();
          try
          {

            Statement statemet = con.createStatement();
            try
            {

              String sSql = "UPDATE caretaker.calls SET sap_sm_nr='" + oData.get("ED_QMNUM") + "' WHERE pkey=" + sCall;
              statemet.executeUpdate(sSql);
              con.commit();
            }
            finally
            {
              statemet.close();
            }
          }

          finally
          {
            con.close();
          }
          Connection con1 = caretaker.getConnection();
          try
          {

            Statement statemet1 = con.createStatement();
            try
            {

              String sSql = "UPDATE caretaker.task SET sap_auftrag='" + oData.get("ED_AUFNR") + "' WHERE pkey=" + task.getSaveStringValue(Task.pkey);
              statemet1.executeUpdate(sSql);
              con1.commit();
            }
            finally
            {
              statemet1.close();
            }
          }

          finally
          {
            con1.close();
          }
        }
        else
        {
          throw new SapProcessingException("Meldung '" + sCall + "' konnte nicht zugeordnet werden");
        }
      }
    }
    catch (SapProcessingException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new SapProcessingException(ex);
    }
  }

  public static void closeTask(final ConnManager oCon, final IDataTableRecord task) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();

    try
    {
      oRFCInParam.put("ID_CHANGE_MODE", "R");
      oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
      String sTasknun = task.getSaveStringValue(Task.sap_auftrag);
      // SAP Schnittstelle ist so gebaut, dass gleichzeitig auch die in
      // der St�rmeldung geschlossen werden kann
      // Dazu mu� als Parameter auch die SM nr �bergeben werden
      // Wir machen das hier aber nicht, sondern schlie�en nur Auftrag
      oRFCInParam.put("ID_AUFNR", sTasknun);// TTSCALL
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult;
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter f�r closeTask ");
      oResult = oFct.exec("Z_050_TTS_RFC_SAP_MELD", oRFCInParam, new CSapReturn[]
        // { new CSapReturn(
          // CSapReturn.TYPE_FIELD, "ED_AUFNR") });//***
          { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_AUFNR"), new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM"), /* St�rmeldung!!!! */
          new CSapReturn(CSapReturn.TYPE_FIELD, "ED_CALLNUMBER"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });

      if (!oResult.isSuccess())
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success, OutputParameter f�r closeTask ");
        }
        String sMES = oResult.getLogMessage();
        throw new SapProcessingException(sMES);
      }
      else if (oResult.isSimpleResult())
      {
        CSAPHelperClass.printDebug("Succsess");
        Map oData = oResult.getSimpleResult();
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success, OutputParameter f�r closeTask ");
        // String sED = (String) oData.get("ED_AUFNR");
      }
    }
    catch (SapProcessingException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new SapProcessingException(ex);
    }
  }

  public static void updateTask(final ConnManager oCon, final IDataTableRecord task) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();

    try
    {
      oRFCInParam = getTaskData(task);
      oRFCInParam.put("ID_CHANGE_MODE", "U");
      String sTasknun = task.getSaveStringValue(Task.sap_auftrag);
      // SAP Schnittstelle ist so gebaut, dass gleichzeitig auch daten in
      // der St�rmeldung ge�ndert werden k�nnen
      // Dazu mu� als Parameter auch die SM nr �bergeben werden
      // Wir machen das hier aber nicht, sondern �ndern nur Auftrag
      oRFCInParam.put("ID_AUFNR", sTasknun);// TTSCALL
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter f�r updateTask ");

      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult;

      oResult = oFct.exec("Z_050_TTS_RFC_SAP_MELD", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_AUFNR"), new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM"), /* St�rmeldung!!!! */
        new CSapReturn(CSapReturn.TYPE_FIELD, "ED_CALLNUMBER"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });

      if (!oResult.isSuccess())
      {
        String sMES = oResult.getLogMessage();
        oResult.getLogMessage();

        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success, OutputParameter f�r updateTask ");
        }
        throw new SapProcessingException(sMES);
      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success, OutputParameter f�r updateTask ");
//        // SAP St�rmeldung Nr in Meldung zur�ckschreiben
//        IDataAccessor retAcc = task.getAccessor().newAccessor();
//        IDataTable call = retAcc.getTable("call");
//        String sTest = (String) oData.get("ED_CALLNUMBER");
//        call.qbeSetKeyValue("pkey", sTest);
//        call.search();
//        if (call.recordCount() == 1)
//        {
//
//          IDataTableRecord callrec = call.getRecord(0);
//          IDataTransaction retTrans = retAcc.newTransaction();
//          try
//          {
//            callrec.setValue(retTrans, "sap_sm_nr", oData.get("ED_QMNUM"));
//            retTrans.commit();
//          }
//          finally
//          {
//            retTrans.close();
//          }
//        }
//        else
//        {
//          throw new SapProcessingException("Meldung '" + sTest + "' konnte nicht zugeordnet werden");
//        }
      }
    }
    catch (SapProcessingException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new SapProcessingException(ex);
    }
  }

  private CExportTaskRFC()
  {
  }

}
