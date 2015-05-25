/**
 * 
 */
package jacob.common.sap;

import java.util.Arrays;
import java.util.List;

import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author Achim B�ken
 * 
 */
public class CheckCallChange
{

  /**
   * 
   */
  private CheckCallChange()
  {
  }

  // �berpr�fung,ob sich die Werte ge�ndert haben.Wenn ja,
  // wird der �nderungs-RFC zur �bermittlung der Daten an SAP
  // aufgerufen, es werden alle relevaten Daten �bertragen
  public static boolean HasChanged(IDataTableRecord call) throws Exception
  {
    // AK
    if (call.hasChangedValue("workgroupcall"))
    {
      // Wenn Migrtion zuende, dann einfach
      // Funktionsaufruf SapActionUpdate(call) entfernen
      // und Return auf True setzen
      CSAPHelperClass.printDebug("AK Ge�ndert");
      // Ge�ndert, Pr�fung erfolgt jetzt w�hrend des Jobs
      // Wenn Migration zuende, dann dort ausbauen
      // SapActionUpdate(call);
      return true;
    }
    if (call.hasLinkedRecord("callworkgroup"))
    {
      IDataTableRecord ak = call.getLinkedRecord("callworkgroup");
      // ...und der AK ein SAP-AK ist
      if (ak.getintValue("sap_ak") == 0)
      {
        return false;
      }
    }

    // Problembeschreibung
    if (call.hasChangedValue("problem"))
    {
      return true;
    }
    // Aktion
    if (call.hasChangedValue("action"))
    {
      return true;
    }
    // Priorit�t
    if (call.hasChangedValue("priority"))
    {
      return true;
    }
    // Status
    if (call.hasChangedValue("callstatus"))
    {
      return true;
    }

    // Objekt
    if (call.hasChangedValue("object_key"))
    {
      return true;
    }

    return false;
  }

  public static boolean IsClosed(IDataTableRecord call) throws Exception
  {
    // Status

    if (call.hasChangedValue("callstatus"))
    {
      CSAPHelperClass.printDebug("Status has changed");
      List status = Arrays.asList(new String[]
        { "Verworfen", "Dokumentiert", "Geschlossen" });
      if (status.contains(call.getSaveStringValue("callstatus")))
      {
        CSAPHelperClass.printDebug("Status is closed");
        return true;
      }
      CSAPHelperClass.printDebug("Status not closed");
      return false;
    }

    return false;
  }

  // TODO: SAP SapActionUpdate() wird nie aufgerufen!
  private static void SapActionUpdate(IDataTableRecord call) throws Exception
  {
    // Fall 1: Call war nicht mit SAP synchronisiert, aber bei
    // AK wechsel k�nnte eine Synchronisation n�tig werden
    if (call.hasNullValue("sap_ssle_nr"))
    // Wenn call bisher kein Sap SSLE war
    {
      // Dann pr�fen wir ob der neue AK ein SAP AK ist
      IDataTableRecord ak = call.getLinkedRecord("callworkgroup");
      if (ak.getintValue("sap_ak") == 1)
      {
        // Es ist ein SAP AK, wir erstellen also einen SSLE
        CSAPHelperClass.printDebug("SAP-IsUpdate And SAP AK --> Create");
        CWriteSAPExchange.createExchangeCall(call);
      }
    }

    else if (call.hasNullValue("sap_sm_nr"))
    // Fall 2: Call war mit SAP synchronisiert ist aber noch keine SM,
    // aber bei AK wechsel k�nnte eine Synchronisation unn�tig werden
    {
      // Dann pr�fen wir ob der neue AK KEIN SAP-AK ist
      IDataTableRecord ak = call.getLinkedRecord("callworkgroup");
      if (ak.getintValue("sap_ak") != 1)
      {
        // Es ist KEIN SAP AK, wir l�schen also einen SSLE
        CSAPHelperClass.printDebug("SAP-IsUpdate And NO SAP-AK --> Delete");
        CWriteSAPExchange.deleteExchangeCall(call);
        // SSLE EINTRAG in Call l�schen

      }

    }
    else
    // Fall 3: Call ist bereits SM und mu� synchronisiert werden
    {

      CWriteSAPExchange.updateExchangeCall(call);
      CSAPHelperClass.printDebug("SAP-IsUpdate And SAP-AK --> Update");
    }
  }
}
