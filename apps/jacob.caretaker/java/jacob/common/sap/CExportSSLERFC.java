/**
 * 
 */
package jacob.common.sap;

import jacob.exception.SapProcessingException;
import jacob.model.Call;
import jacob.model.Calls;
import jacob.model.Sapcallexchange;


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
import de.tif.jacob.core.exception.RecordLockedException;

/**
 * Erzeugung oder Änderung eines Störstellenlisteneintrags (SSLE)
 * 
 * @author Achim Böken
 * 
 * 
 * 
 */
public final class CExportSSLERFC
{

  /**
   * Hilfsmethode zur Zusammenstellung der gemeinsamen Daten für die Erzeugung
   * oder Änderung einer Störstellenliste
   * 
   * @param call,
   *          IDataTableRecord, der zu erstellende oder zu ändernde Datensatz
   * @exception Exception
   */
  private static Map getCallData(final IDataTableRecord call, String sType) throws Exception
  {
    // MIT LANGTEXT Prüfen
    // Map<String,String> oRFCInParam = new HashMap<String,String>();
    Map oRFCInParam = new HashMap();
    // Map oRFCInParam = new HashMap();

    // Hier werden dieParameterlisten für SSLE und SM
    // zusammengestellt

    // 1. Parameter nur für SSLE
    if ("SSLE".equals(sType))
    {

      // Priorität setzen
      String sprio = "";
      if (!call.getValue("priority").equals("1-Normal"))
      {
        sprio = "X";
      }
      oRFCInParam.put("ID_PRIORITY", sprio);

      // Kundendaten
      String scustint = "";
      String scusttel = "";
      String scustdepartment = "";
      if (call.hasLinkedRecord("customerint"))
      {
        IDataTableRecord customerint = call.getLinkedRecord("customerint");
        scustint = customerint.getSaveStringValue("fullname");
        scusttel = customerint.getSaveStringValue("phonecorr");
        scustdepartment = customerint.getSaveStringValue("department");
      }
      oRFCInParam.put("ID_MELDENDER", scustint);
      oRFCInParam.put("ID_TELNR", scusttel);
      oRFCInParam.put("ID_DEPARTMENT", scustdepartment);
      // Texte
      StringBuffer sproblem = new StringBuffer();
      // sproblem.append("Problem: ");
      sproblem.append(call.getSaveStringValue("problem") + " ");
      sproblem.append("Aktion: ");
      sproblem.append(call.getSaveStringValue("action") + " ");
      sproblem.append("Tätigkeit: ");
      if (call.hasLinkedRecord("process"))
      {
        sproblem.append(call.getLinkedRecord("process").getSaveStringValue("processname"));
      }
      oRFCInParam.put("ID_DESCRIPTION", sproblem.toString());
      // AK Ermitteln: Feld hwg_name übergeben: Achtung Feld wird
      // manuell
      // gepflegt
      String sak = "";
      if (call.hasLinkedRecord("callworkgroup"))
      {
        sak = call.getLinkedRecord("callworkgroup").getSaveStringValue("hwg_name");
      }

      oRFCInParam.put("ID_TTS_AK", sak);
      // Location und Werk Übergeben
      String slocation = "";
      // String sWerk = "";
      String sWerkShort = "";
      if (call.hasLinkedRecord("location"))
      {
        slocation = call.getLinkedRecord("location").getSaveStringValue("description");
        // Werk übergeben
        if (call.getLinkedRecord("location").hasLinkedRecord("faplissite"))
        {
          sWerkShort = call.getLinkedRecord("location").getLinkedRecord("faplissite").getSaveStringValue("shortname");
        }
      }
      // Location
      oRFCInParam.put("ID_AREA", slocation);
      // Werk
      oRFCInParam.put("ID_WERK", sWerkShort);
      // Datum Meldungsbeginn ermitteln und in SAP Format konvertieren
      oRFCInParam.put("ID_CALL_DATE", DateFormater.GetStringDate(call.getDateValue("datereported")));
      oRFCInParam.put("ID_CALL_TIME", DateFormater.GetStringTime(call.getDateValue("datereported")));
      // ServiceLevel
      if (call.hasNullValue(Call.sl))
      {
        oRFCInParam.put("ID_TTS_SL", "");
      }
      else
      {
        Integer sl = call.getIntegerValue(Call.sl);
        oRFCInParam.put("ID_TTS_SL", sl.toString());
      }

    }
    // Ende SSLE
    // 2. Parameter nur für SM
    if ("SM".equals(sType))
    {

      // SAP SM Nr
      String sSMMeldung = "";
      sSMMeldung = call.getSaveStringValue("sap_sm_nr");
      oRFCInParam.put("ID_QMNUM_MEL", sSMMeldung);
      // Texte
      StringBuffer sproblem = new StringBuffer();
      // sproblem.append("Problem: ");
      sproblem.append(call.getSaveStringValue("problem") + " ");
      sproblem.append("Aktion: ");
      sproblem.append(call.getSaveStringValue("action") + " ");
      sproblem.append("Tätigkeit: ");
      if (call.hasLinkedRecord("process"))
      {
        sproblem.append(call.getLinkedRecord("process").getSaveStringValue("processname"));
      }
      oRFCInParam.put("ID_QMTXT", sproblem.toString());
      // Kostenstelle
      String sCostcenter = "";
      sCostcenter = call.getSaveStringValue("accountingcode_key");
      oRFCInParam.put("ID_KOSTL", sCostcenter);
      // Störungsende
      if (null != call.getDateValue("dateresolved"))
      {
        oRFCInParam.put("ID_AUSBS", DateFormater.GetStringDate(call.getDateValue("dateresolved")));
        oRFCInParam.put("ID_AUZTB", DateFormater.GetStringTime(call.getDateValue("dateresolved")));

      }

      oRFCInParam.put("ID_LTEXT", call.getValue("problemtext"));

      // Location und Werk Übergeben

      String sWerkShort = "";
      if (call.hasLinkedRecord("location"))
      {

        // Werk übergeben
        if (call.getLinkedRecord("location").hasLinkedRecord("faplissite"))
        {
          sWerkShort = call.getLinkedRecord("location").getLinkedRecord("faplissite").getSaveStringValue("shortname");
        }
      }

      oRFCInParam.put("ID_WERK", sWerkShort);
    }

    // Ende SM

    // Gemeinsame Parameter
    oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
    // Objekt ermitteln
    String sobject = "";
    if (call.hasLinkedRecord("object"))
    {
      sobject = call.getLinkedRecord("object").getSaveStringValue("external_id");
    }
    oRFCInParam.put("ID_FUNCT_LOC", sobject);

    oRFCInParam.put("ID_CALLNUMBER", call.getSaveStringValue("pkey"));

    return oRFCInParam;

  }

  // ----------------------------------------------------------------------------
  /**
   * Änderung eines Störstellenlisteneintrags
   * 
   * @param oCon,
   *          ConnManager, JCo Connection
   * @param call,
   *          IDataTableRecord, der zu erstellende oder zu ändernde Datensatz
   * @exception SapProcessingException
   */
  public static void updateSSLE(final ConnManager oCon, final IDataTableRecord call) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    try
    {
      // //#/#/ Allgemeine Datensammlung
      oRFCInParam = getCallData(call, "SSLE");
      // //#/#///////////////////////////////////////////

      // RFC Call in Update Mode setzen
      oRFCInParam.put("ID_CHANGE_MODE", "U");

      // SAP_NR setzen
      if ("".equals(call.getSaveStringValue("sap_ssle_nr")))
      {
        throw new SapProcessingException("Keine SAP Nr. vorhanden, Update nicht möglich!");
      }
      String sSAPNr = call.getSaveStringValue("sap_ssle_nr");
      oRFCInParam.put("ID_QMNUM", sSAPNr);
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für updateSSLE ");
      // RFC Aufruf
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult = oFct.exec("Z_050_TTS_RFC_NOTIF_UPDATE", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });

      if (!oResult.isSuccess())
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success OutputParameter für updateSSLE ");
        }
        String sMES = oResult.getLogMessage();
        throw new SapProcessingException(sMES);

      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        // String sED = (String) oData.get("ED_QMNUM");
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success OutputParameter für updateSSLE ");
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

  // ----------------------------------------------------------------------------
  /**
   * Erzeugung eines Störstellenlisteneintrags
   * 
   * @param oCon,
   *          ConnManager, JCo Connection
   * @param call,
   *          IDataTableRecord, der zu erstellende oder zu ändernde Datensatz
   * @exception SapProcessingException
   */
  public static void createSSLE(final ConnManager oCon, final IDataTableRecord call, final IDataTableRecord callexrec, IDataTransaction transaction)
      throws  SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    String sED = "";
    try
    {
      // //#/#/ Allgemeine Datensammlung
      oRFCInParam = getCallData(call, "SSLE");
      // //#/#///////////////////////////////////////////

      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult;
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für createSSLE ");
      oResult = oFct.exec("Z_050_TTS_RFC_NOTIF_CREATE", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });

      if (!oResult.isSuccess())
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success OutputParameter für createSSLE ");
        }
        String sMES = oResult.getLogMessage();
        throw new SapProcessingException(sMES);
      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success OutputParameter für createSSLE ");
        sED = (String) oData.get("ED_QMNUM");
        // SAP Nr zurückschreiben, da die bestehenden Locks ignoriert werden sollen,
        // Hier "händisch" an der Connection
        SQLDataSource caretaker = (SQLDataSource) DataSource.get("caretakerDataSource");
        Connection con = caretaker.getConnection();
        try
        {
          callexrec.setValue(transaction, Sapcallexchange.sapssleid, sED);
          Statement statemet = con.createStatement(); 
          try
          {
            String sPkey = call.getSaveStringValue(Calls.pkey);
            String sSql = "UPDATE caretaker.calls SET sap_ssle_nr='" + sED + "' WHERE pkey=" + sPkey;
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

  public static void closeSSLE(final ConnManager oCon, final IDataTableRecord call) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    try
    {
      oRFCInParam.put("ID_QMNUM", call.getSaveStringValue("sap_ssle_nr"));// SSLE
      // Auf Close (release) setzen
      oRFCInParam.put("ID_CHANGE_MODE", "R");// Close Paramaeter
      oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult;
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für closeSSLE ");
      oResult = oFct.exec("Z_050_TTS_RFC_NOTIF_UPDATE", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });

      if (!oResult.isSuccess())
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success OutputParameter für closeSSLE ");
        }
        String sMES = oResult.getLogMessage();
        throw new SapProcessingException(sMES);
      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        // String sED = (String) oData.get("ED_QMNUM");
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success OutputParameter für closeSSLE ");
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

  public static void closeSM(final ConnManager oCon, final IDataTableRecord call) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    try
    {
      oRFCInParam.put("ID_QMNUM_MEL", call.getSaveStringValue(Calls.sap_sm_nr));// SSLE
      // Nummer
      oRFCInParam.put("ID_CHANGE_MODE", "R");// Close Paramaeter
      oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult;
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für closeSM ");
      oResult = oFct.exec("Z_050_TTS_RFC_SAP_MELD", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });

      if (!oResult.isSuccess())
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success OutputParameter für closeSM ");
        }

        String sMES = oResult.getLogMessage();
        throw new SapProcessingException(sMES);
      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        // String sED = (String) oData.get("ED_QMNUM");
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success OutputParameter für closeSM ");
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

  public static void deleteSSLE(final ConnManager oCon, final IDataTableRecord call) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    try
    {
      oRFCInParam.put("ID_QMNUM", call.getSaveStringValue(Calls.sap_ssle_nr));// SSLE
      oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
      // Nummer

      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult;
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für deleteSSLE ");
      oResult = oFct.exec("Z_050_TTS_RFC_NOTIF_STORNO", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });

      if (!oResult.isSuccess())
      {
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success OutputParameter für deleteSSLE ");
        }

        String sMES = oResult.getLogMessage();
        throw new SapProcessingException(sMES);
      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        // String sED = (String) oData.get("MESSAGE");
        // SAP Nr zurückschreiben
        IDataAccessor retAcc = call.getAccessor().newAccessor();
        IDataTransaction retTrans = retAcc.newTransaction();
        try
        {
          call.setValue(retTrans, "sap_ssle_nr", "");
          retTrans.commit();
        }
        finally
        {
          retTrans.close();
        }
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success OutputParameter für deleteSSLE ");

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

  // ----------------------------------------------------------------------------
  /**
   * Änderung einer Störmeldung
   * 
   * @param oCon,
   *          ConnManager, JCo Connection
   * @param call,
   *          IDataTableRecord, der zu erstellende oder zu ändernde Datensatz
   * @exception SapProcessingException
   */
  public static void updateSM(final ConnManager oCon, final IDataTableRecord call) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();
    try
    {
      // Daten sammeln
      oRFCInParam = getCallData(call, "SM");
      String sSAPNr = call.getSaveStringValue(Call.sap_ssle_nr);
      oRFCInParam.put("ID_QMNUM", sSAPNr);
      // RFC Call in Update Mode setzen
      oRFCInParam.put("ID_CHANGE_MODE", "U");

      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für updateSM ");
      // RFC Aufruf
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult = oFct.exec("Z_050_TTS_RFC_SAP_MELD", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });
      // Result auswerten
      if (!oResult.isSuccess())
      {
        String sMES = oResult.getLogMessage();
        // Debug: Outputparameter ausgeben
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   *No Success OutputParameter für updateSM ");
        }
        throw new SapProcessingException(sMES);

      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        // String sED = (String) oData.get("ED_QMNUM");
        // Debug: Outputparameter ausgeben
        CSAPHelperClass.WriteSAPOutputParam(oData, "   *Success OutputParameter für updateSM ");
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

  // ----------------------------------------------------------------------------
  /*
   * verbotener Konstruktor
   */
  private CExportSSLERFC()
  {
    // NIX
  }
}
