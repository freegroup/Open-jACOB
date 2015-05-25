/**
 * 
 */
package jacob.common.sap;

import jacob.exception.SapProcessingException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author Bernd.Clemenz
 *
 */
public final class CExecRFC_Example
{
  //----------------------------------------------------------------------------
  public static void createSSLETest(final ConnManager oCon, final IDataTableRecord call) throws SapProcessingException
  {
    Map oRFCInParam = new HashMap();

    try
    {
      String sprio = "";
      if (!call.getValue("priority").equals("1-Normal"))
      {
        sprio = "X";
      }

      oRFCInParam.put("ID_PRIORITY", sprio);
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
      StringBuffer sproblem = new StringBuffer();
      sproblem.append("Problem: ");
      sproblem.append(call.getSaveStringValue("problem") + " ");
      sproblem.append("Aktion: ");
      sproblem.append(call.getSaveStringValue("action") + " ");
      sproblem.append("Tätigkeit: ");
      if (call.hasLinkedRecord("process"))
      {
        sproblem.append(call.getLinkedRecord("process").getSaveStringValue("processname"));
      }
      oRFCInParam.put("ID_DESCRIPTION", sproblem.toString());
      // Objekt ermitteln
      String sobject = "";
      if (call.hasLinkedRecord("object"))
      {
        sobject = call.getLinkedRecord("object").getSaveStringValue("external_id");
      }
      oRFCInParam.put("ID_FUNCT_LOC", sobject);
      // AK Ermitteln: Feld hwg_name übergeben: Achtung Feld wird manuell
      // gepflegt
      String sak = "";
      if (call.hasLinkedRecord("callworkgroup"))
      {
        sak = call.getLinkedRecord("callworkgroup").getSaveStringValue("hwg_name");
      }

      oRFCInParam.put("ID_TTS_AK", sak);

      oRFCInParam.put("ID_CALLNUMBER", call.getSaveStringValue("pkey"));

      // Datum Meldungsbeginn ermitteln und in SAP Format konvertieren
      oRFCInParam.put("ID_CALL_DATE", DateFormater.GetStringDate(call.getDateValue("datereported")));
      oRFCInParam.put("ID_CALL_TIME", DateFormater.GetStringTime(call.getDateValue("datereported")));
      // Location Übergeben
      String slocation = "";
      String sWerk = "";
      String sWerkShort = "";
      System.out.println("vor Location");
      if (call.hasLinkedRecord("location"))
      {
        System.out.println("hat Location");
        slocation = call.getLinkedRecord("location").getSaveStringValue("description");
        // Werk übergeben
        if (call.getLinkedRecord("location").hasLinkedRecord("faplissite"))
        {
          System.out.println("hat Site");
          sWerk = call.getLinkedRecord("location").getLinkedRecord("faplissite").getSaveStringValue("name");
          sWerkShort = call.getLinkedRecord("location").getLinkedRecord("faplissite").getSaveStringValue("shortname");
          System.out.println("Werk" + sWerk + " Short " + sWerkShort);

        }
      }
      oRFCInParam.put("ID_AREA", slocation);

      // Werk übergeben
      oRFCInParam.put("ID_WERK", sWerkShort);

      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult = oFct.exec("Z_050_TTS_RFC_NOTIF_CREATE", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_FIELD, "ED_QMNUM") });

      if (!oResult.isSuccess())
      {
        // 
      }
      else if (oResult.isSimpleResult())
      {
        Map oData = oResult.getSimpleResult();
        Iterator oiter = oData.entrySet().iterator();

        while (oiter.hasNext())
        {
          Map.Entry oentry = (Map.Entry) oiter.next();

          System.out.println(oentry.getKey() + "  " + oentry.getValue());
        } // while
      }
    }
    //    catch (SapProcessingException ex)
    //    {
    //      throw ex;
    //    }
    catch (Exception ex)
    {
      throw new SapProcessingException(ex);
    }
  }

  //----------------------------------------------------------------------------
  /*
   * verbotener Konstruktor 
   */
  private CExecRFC_Example()
  {
    // NIX
  }
}
