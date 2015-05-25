package jacob.common.sap;

import jacob.common.AppLogger;
import jacob.exception.SapProcessingException;
import jacob.model.Accountingcode;
import jacob.model.Faplisbuilding;
import jacob.model.Faplisbuildingpart;
import jacob.model.Faplisfloor;
import jacob.model.Faplisplane;
import jacob.model.Faplissite;
import jacob.model.Faplissitepart;
import jacob.model.Object;
import jacob.model.Objectlocation;
import jacob.model.Sap_edvin_object;
import jacob.model.Sap_objimport;
import jacob.model.Sapadmin;
import jacob.model.Saplog;
import jacob.model.Sap_objimport.imp_status_ENUM;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.poi.util.SystemOutLogger;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;

public class CImportObjects
{
  // use this logger to write messages and NOT the System.println(..) ;-)
  private static final transient Log logger = AppLogger.getLogger();

  private static final Map SAP_JCOB_OBJ = new HashMap();
  static
  // Mapping für Objekt
  {
    SAP_JCOB_OBJ.put("EQUNR", Sap_objimport.eqnummer);
    SAP_JCOB_OBJ.put("ZZ_EQUI_EDVIN_ID", Sap_objimport.old_edvin_object);
    SAP_JCOB_OBJ.put("ZZ_ALT_EDVIN_ID", Sap_objimport.old_edvin_techplatz);
    SAP_JCOB_OBJ.put("SHTXT", Sap_objimport.eqname);
    SAP_JCOB_OBJ.put("EQTYP", Sap_objimport.eq_type);
    SAP_JCOB_OBJ.put("IWERK", Sap_objimport.instplanwerk);
    SAP_JCOB_OBJ.put("SWERK", Sap_objimport.standortwerk);
    SAP_JCOB_OBJ.put("STORT", Sap_objimport.standort_anlage);
    SAP_JCOB_OBJ.put("ANLNR", Sap_objimport.anlagen_haupt_nr);
    SAP_JCOB_OBJ.put("ANLUN", Sap_objimport.anlagen_unter_nr);
    SAP_JCOB_OBJ.put("KOSTL", Sap_objimport.kostenstelle);
    SAP_JCOB_OBJ.put("TPLNR", Sap_objimport.techplatz);
    SAP_JCOB_OBJ.put("STRNO", Sap_objimport.kz_techplatz);
    SAP_JCOB_OBJ.put("PLTXT", Sap_objimport.bez_techplatz);
    SAP_JCOB_OBJ.put("TIDNR", Sap_objimport.tech_id);
    SAP_JCOB_OBJ.put("HEQUI", Sap_objimport.eq_oberhalb);
    SAP_JCOB_OBJ.put("TPLMA", Sap_objimport.techpl_oberhalb);
    SAP_JCOB_OBJ.put("SERNR", Sap_objimport.serialnr);
    SAP_JCOB_OBJ.put("MAPAR", Sap_objimport.hersteller);
    SAP_JCOB_OBJ.put("INGRP", Sap_objimport.planergr_service);
    SAP_JCOB_OBJ.put("ELIEF", Sap_objimport.lieferant);
    SAP_JCOB_OBJ.put("INVNR", Sap_objimport.invnr);
    SAP_JCOB_OBJ.put("HERST", Sap_objimport.anl_hersteller);
    SAP_JCOB_OBJ.put("ARBPL", Sap_objimport.arbeitsplatz);
    SAP_JCOB_OBJ.put("GEWRK", Sap_objimport.verantw_arbeitspl);
    SAP_JCOB_OBJ.put("WERK", Sap_objimport.werk);
    SAP_JCOB_OBJ.put("TYPBZ", Sap_objimport.typenbez_herst);
    // SAP_JCOB_OBJ.put("STTXT", Sap_objimport.systemstatus);
    SAP_JCOB_OBJ.put("ILOAN", Sap_objimport.standort_techobj);
    SAP_JCOB_OBJ.put("USTXT", Sap_objimport.anwenderstatus);
    SAP_JCOB_OBJ.put("OBJNR", Sap_objimport.objektnummer);
    SAP_JCOB_OBJ.put("ZZ_OBJ_STANDORT", Sap_objimport.pmw50_standort);
    SAP_JCOB_OBJ.put("TECHNICAL_OBJECT", Sap_objimport.objektnr2);
    SAP_JCOB_OBJ.put("WARRANTY_CATEGORY", Sap_objimport.garantietyp);
    SAP_JCOB_OBJ.put("WARRANTY_TYPE", Sap_objimport.garantieart);
    SAP_JCOB_OBJ.put("WARRANTY_START", Sap_objimport.garantiebeginn);
    SAP_JCOB_OBJ.put("WARRANTY_END", Sap_objimport.garantieende);
    SAP_JCOB_OBJ.put("MASTER_WARRANTY", Sap_objimport.mustergarantienr);
    SAP_JCOB_OBJ.put("CHECK_RESULT", Sap_objimport.checkresult);
    SAP_JCOB_OBJ.put("CHECK_DATE", Sap_objimport.checkdate);
    SAP_JCOB_OBJ.put("ORT_KTEXT", Sap_objimport.ort_kurztext);
    SAP_JCOB_OBJ.put("OBJ_STATUS", Sap_objimport.systemstatus);
    // CHANGE_IND Art der Änderung (U, I, E, D) auf das Anwendungsobjekt
    SAP_JCOB_OBJ.put("CHANGE_IND", Sap_objimport.changestatus);
    // Objectlocation
    SAP_JCOB_OBJ.put("ZZ_EQUI_STANDORT", Sap_objimport.eq_werk);
    SAP_JCOB_OBJ.put("ZZ_EQUI_EDVIN_ID", Sap_objimport.eq_edvin_id);
    SAP_JCOB_OBJ.put("ZZ_EQ_WERKSTEIL", Sap_objimport.eq_werkteil);
    SAP_JCOB_OBJ.put("ZZ_EQ_GEBAEUDE", Sap_objimport.eq_gebaeude);
    SAP_JCOB_OBJ.put("ZZ_EQ_TEILGEB", Sap_objimport.eq_gebaeudeteil);
    SAP_JCOB_OBJ.put("ZZ_EQ_GESCHOSS", Sap_objimport.eq_geschoss);
    SAP_JCOB_OBJ.put("ZZ_EQ_EBENE", Sap_objimport.eq_ebene);
    SAP_JCOB_OBJ.put("ZZ_EQ_STD_TEXT", Sap_objimport.eq_locationdescr);
    SAP_JCOB_OBJ.put("ZZ_OBJ_STANDORT", Sap_objimport.tp_werk);
    SAP_JCOB_OBJ.put("ZZ_ALT_EDVIN_ID", Sap_objimport.tp_edvin_id);
    SAP_JCOB_OBJ.put("ZZ_WERKSTEIL", Sap_objimport.tp_werkteil);
    SAP_JCOB_OBJ.put("ZZ_GEBAEUDE", Sap_objimport.tp_gebaeude);
    SAP_JCOB_OBJ.put("ZZ_TEILGEBAEUDE", Sap_objimport.tp_gebaeudeteil);
    SAP_JCOB_OBJ.put("ZZ_GESCHOSS", Sap_objimport.tp_geschoss);
    SAP_JCOB_OBJ.put("ZZ_EBENE", Sap_objimport.tp_ebene);
    SAP_JCOB_OBJ.put("ZZ_STD_TEXT", Sap_objimport.tp_locationdescr);
    SAP_JCOB_OBJ.put("TPLNR_OLD", Sap_objimport.changed_extid);
  }

  // OBJ_ID
  // STAT_ID
  private static final Map SAP_JCOB_OBJSTAT = new HashMap();
  static
  // Mapping für Objekt status
  {
    SAP_JCOB_OBJSTAT.put("OBJ_ID", Sap_objimport.eqnummer);
    SAP_JCOB_OBJSTAT.put("STAT_ID", Sap_objimport.systemstatus);
  }
  private static final Map SAP_JCOB_STAT = new HashMap();
  static
  // Mapping für Status
  {
    SAP_JCOB_STAT.put("LÖVM", "gelöscht");
    SAP_JCOB_STAT.put("INAK", "außer Betrieb");
    SAP_JCOB_STAT.put("AKTIV", "in Betrieb");
  }
  private static final Map SAP_JCOB_OBJSITE = new HashMap();
  static
  // Mapping für Objekt status
  {
    SAP_JCOB_OBJSITE.put("50", "50");
    SAP_JCOB_OBJSITE.put("050", "50");
    SAP_JCOB_OBJSITE.put("59", "59");
    SAP_JCOB_OBJSITE.put("059", "59");
    SAP_JCOB_OBJSITE.put("066", "996");
    SAP_JCOB_OBJSITE.put("66", "996");
  }

  private static void setLocation(Context context, IDataTransaction transaction, IDataTableRecord location, IDataTableRecord objexrec, String objtype,
      String sstdWerk, String locrecstat) throws Exception
  {
    String x = location.getSaveStringValue("pkey");
    // abhänig von TP oder EQ Daten setzen
    String swerk;
    String swerkteil;
    String sgebaude;
    String sgebaudeteil;
    String sgeschoss;
    String sebene;
    String sdescr;
    String sedvin;

    if (objtype.equals(""))
    {
      throw new SapProcessingException("Objekttype konnte nicht festgestellt werden");
    }
    if (objtype.equals("tp")) // Techn. Platz
    {
      swerk = objexrec.getSaveStringValue(Sap_objimport.tp_werk);
      swerkteil = objexrec.getSaveStringValue(Sap_objimport.tp_werkteil);
      sgebaude = objexrec.getSaveStringValue(Sap_objimport.tp_gebaeude);
      sgebaudeteil = objexrec.getSaveStringValue(Sap_objimport.tp_gebaeudeteil);
      sgeschoss = objexrec.getSaveStringValue(Sap_objimport.tp_geschoss);
      sebene = objexrec.getSaveStringValue(Sap_objimport.tp_ebene);
      sdescr = objexrec.getSaveStringValue(Sap_objimport.tp_locationdescr);
      sedvin = objexrec.getSaveStringValue(Sap_objimport.tp_edvin_id);
    }
    else
    {
      swerk = objexrec.getSaveStringValue(Sap_objimport.eq_werk);
      swerkteil = objexrec.getSaveStringValue(Sap_objimport.eq_werkteil);
      sgebaude = objexrec.getSaveStringValue(Sap_objimport.eq_gebaeude);
      sgebaudeteil = objexrec.getSaveStringValue(Sap_objimport.eq_gebaeudeteil);
      sgeschoss = objexrec.getSaveStringValue(Sap_objimport.eq_geschoss);
      sebene = objexrec.getSaveStringValue(Sap_objimport.eq_ebene);
      sdescr = objexrec.getSaveStringValue(Sap_objimport.eq_locationdescr);
      sedvin = objexrec.getSaveStringValue(Sap_objimport.eq_edvin_id);
    }
    // bei geänderten Ortsinformationen soviele daten wie möglich retten

    if (locrecstat.equals("updLocRec"))
    {
      
      // Wenn Kein Werk übergeben wird, dann Abbrechen
      if (swerk.equals("") || swerk == null)
      {
        return;
      }
      else
      {
        if (SAP_JCOB_OBJSITE.containsKey(swerk))
        {
          // Gültiges Werk gefunden, swerk mit gültigem pkey belegen
          swerk = (String) SAP_JCOB_OBJSITE.get(swerk);

          if (!swerk.equals(location.getSaveStringValue(Objectlocation.site_key)))
          {
            location.setValue(transaction, Objectlocation.sitepart_key, null);
            location.setValue(transaction, Objectlocation.building_key, null);
            location.setValue(transaction, Objectlocation.buildingpart_key, null);
            location.setValue(transaction, Objectlocation.floor_key, null);
            location.setValue(transaction, Objectlocation.plane_key, null);
            // location.setBinaryValue(transaction, Objectlocation.description,
            // null);

          }
          location.setValue(transaction, Objectlocation.site_key, swerk);
        }
        else
        {
          return;
        }
      }

    }

    // Bei neuem Location-Record: Daten übertragen
    else if (locrecstat.equals("newLocRec"))
    {
      
      // Wenn Kein Werk übergeben wird, dann Standardwerk und Abbrechen
      if (swerk.equals("") || swerk == null)
      {
        location.setValue(transaction, Objectlocation.site_key, sstdWerk);
        if (sdescr.equals("") || sdescr == null)
        {
          location.setValue(transaction, Objectlocation.note, "Werk 50");
        }
        else
        {
          location.setValue(transaction, Objectlocation.note, sdescr);
        }

        return;
      }
      // Sonst Werk 50,59 oder 66 prüfen
      else
      {

        if (SAP_JCOB_OBJSITE.containsKey(swerk))
        {
          // Gültiges Werk gefunden, swerk mit gültigem pkey belegen
          swerk = (String) SAP_JCOB_OBJSITE.get(swerk);
          location.setValue(transaction, Objectlocation.site_key, swerk);
          if (sdescr.equals("") || sdescr == null)
          {
            location.setValue(transaction, Objectlocation.note, swerk);
          }
          else
          {
            location.setValue(transaction, Objectlocation.note, sdescr);
          }
        }

        // Sonst Standardwerk und Abbrechen
        else
        {
          location.setValue(transaction, Objectlocation.site_key, sstdWerk);
          if (sdescr.equals("") || sdescr == null)
          {
            location.setValue(transaction, Objectlocation.note, "Werk 50");
          }
          else
          {
            location.setValue(transaction, Objectlocation.note, sdescr);
          }
          return;
        }

      }
    }
    else
    {
      // Sollt nicht passieren
      return;
    }
    // Beschreibung
    if (!sdescr.equals("") && sdescr != null)
    {
      location.setValue(transaction, Objectlocation.note, sdescr);
    }

    // Werksteil
    if (swerkteil.equals("") || swerkteil == null)
    {
      return;
    }
    else
    {
      IDataTable faplissitepart = context.getDataTable(Faplissitepart.NAME);

      faplissitepart.qbeClear();
      faplissitepart.qbeSetValue(Faplissitepart.faplisstatus, Faplissitepart.faplisstatus_ENUM._gueltig);
      faplissitepart.qbeSetValue(Faplissitepart.name, swerkteil);
      faplissitepart.qbeSetKeyValue(Faplissitepart.site_key, swerk);
      faplissitepart.search();
      if (faplissitepart.recordCount() == 1)
      {
        swerkteil = faplissitepart.getRecord(0).getSaveStringValue(Faplissitepart.pkey);
        if (!swerkteil.equals(location.getSaveStringValue(Objectlocation.sitepart_key)))
        {
          location.setValue(transaction, Objectlocation.building_key, null);
          location.setValue(transaction, Objectlocation.buildingpart_key, null);
          location.setValue(transaction, Objectlocation.floor_key, null);
          location.setValue(transaction, Objectlocation.plane_key, null);
          // location.setBinaryValue(transaction, Objectlocation.description,
          // null);

        }
        location.setValue(transaction, Objectlocation.sitepart_key, faplissitepart.getRecord(0).getValue(Faplissitepart.pkey));

      }
      else
      {
        return;
      }
    }
    // Gebäude

    if (sgebaude.equals("") || sgebaude == null)
    {
      return;
    }
    else
    {
      IDataTable faplisbuilding = context.getDataTable(Faplisbuilding.NAME);

      faplisbuilding.qbeClear();
      faplisbuilding.qbeSetValue(Faplisbuilding.faplisstatus, Faplisbuilding.faplisstatus_ENUM._gueltig);
      faplisbuilding.qbeSetValue(Faplisbuilding.name, sgebaude);
      faplisbuilding.qbeSetKeyValue(Faplisbuilding.sitepart_key, swerkteil);
      faplisbuilding.search();
      if (faplisbuilding.recordCount() == 1)
      {
        sgebaude = faplisbuilding.getRecord(0).getSaveStringValue(Faplisbuilding.pkey);
        if (!sgebaude.equals(location.getSaveStringValue(Objectlocation.building_key)))
        {
          location.setValue(transaction, Objectlocation.buildingpart_key, null);
          location.setValue(transaction, Objectlocation.floor_key, null);
          location.setValue(transaction, Objectlocation.plane_key, null);
          // location.setBinaryValue(transaction, Objectlocation.description,
          // null);

        }
        location.setValue(transaction, Objectlocation.building_key, faplisbuilding.getRecord(0).getValue(Faplisbuilding.pkey));
      }
      else
      {
        return;
      }
    }
    // Gebäudeteil

    if (sgebaudeteil.equals("") || sgebaudeteil == null)
    {
      return;
    }
    else
    {
      IDataTable faplisbuildingpart = context.getDataTable(Faplisbuildingpart.NAME);

      faplisbuildingpart.qbeClear();
      faplisbuildingpart.qbeSetValue(Faplisbuildingpart.faplisstatus, Faplisbuildingpart.faplisstatus_ENUM._gueltig);
      faplisbuildingpart.qbeSetValue(Faplisbuildingpart.name, sgebaudeteil);
      faplisbuildingpart.qbeSetKeyValue(Faplisbuildingpart.building_key, sgebaude);
      faplisbuildingpart.search();
      if (faplisbuildingpart.recordCount() == 1)
      {
        sgebaudeteil = faplisbuildingpart.getRecord(0).getSaveStringValue(Faplisbuildingpart.pkey);
        if (!sgebaudeteil.equals(location.getSaveStringValue(Objectlocation.buildingpart_key)))
        {
          location.setValue(transaction, Objectlocation.floor_key, null);
          location.setValue(transaction, Objectlocation.plane_key, null);
          // location.setBinaryValue(transaction, Objectlocation.description,
          // null);

        }
        location.setValue(transaction, Objectlocation.buildingpart_key, faplisbuildingpart.getRecord(0).getValue(Faplisbuildingpart.pkey));
      }
      else
      {
        return;
      }
    }

    // Geschoss

    if (sgeschoss.equals("") || sgeschoss == null)
    {
      return;
    }
    else
    {
      IDataTable faplisfloor = context.getDataTable(Faplisfloor.NAME);

      faplisfloor.qbeClear();
      faplisfloor.qbeSetValue(Faplisfloor.faplisstatus, Faplisfloor.faplisstatus_ENUM._gueltig);
      faplisfloor.qbeSetValue(Faplisfloor.name, sgeschoss);
      faplisfloor.qbeSetKeyValue(Faplisfloor.buildingpart_key, sgebaudeteil);
      faplisfloor.search();
      if (faplisfloor.recordCount() == 1)
      {
        sgeschoss = faplisfloor.getRecord(0).getSaveStringValue(Faplisfloor.pkey);
        if (!sgeschoss.equals(location.getSaveStringValue(Objectlocation.floor_key)))
        {
          location.setValue(transaction, Objectlocation.plane_key, null);
          // location.setBinaryValue(transaction, Objectlocation.description,
          // null);

        }
        location.setValue(transaction, Objectlocation.floor_key, faplisfloor.getRecord(0).getValue(Faplisfloor.pkey));
      }
      else
      {
        return;
      }
    }
    // Ebene

    if (sebene.equals("") || sebene == null)
    {
      return;
    }
    else
    {
      IDataTable faplisplane = context.getDataTable(Faplisplane.NAME);

      faplisplane.qbeClear();
      faplisplane.qbeSetValue(Faplisplane.faplisstatus, Faplisplane.faplisstatus_ENUM._gueltig);
      faplisplane.qbeSetValue(Faplisplane.name, sebene);
      faplisplane.qbeSetKeyValue(Faplisplane.floor_key, sebene);
      faplisplane.search();
      if (faplisplane.recordCount() == 1)
      {
        sebene = faplisplane.getRecord(0).getSaveStringValue(Faplisplane.pkey);
        location.setValue(transaction, Objectlocation.plane_key, faplisplane.getRecord(0).getValue(Faplisplane.pkey));
      }
      else
      {
        return;
      }
    }

  }

  public static void cleanSAPObjects() throws Exception
  {
    Context context = Context.getCurrent();
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable objex = acc.getTable(Sap_objimport.NAME);
    objex.clear();
    objex.qbeClear();
    objex.qbeSetValue(Sap_objimport.imp_error, "0");
    objex.qbeSetValue(Sap_objimport.imp_status, Sap_objimport.imp_status_ENUM._Done);
    objex.setMaxRecords(100000);
    objex.search();
    IDataTransaction transaction = acc.newTransaction();
    CSAPHelperClass.printDebug(" **Deleting " + objex.recordCount() + "Objectimportrecords");
    try
    {
      for (int i = 0; i < objex.recordCount(); i++)
      {
        IDataTableRecord objimprec = objex.getRecord(i);
        objimprec.delete(transaction);
      }
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }

  }

  public static String getMaxRecs() throws Exception
  {
    Context context = Context.getCurrent();
    final IDataTable oSapAdm = context.getDataTable(Sapadmin.NAME);
    oSapAdm.qbeClear();
    oSapAdm.qbeSetValue(Sapadmin.active, "1");
    oSapAdm.search();
    String maxrec = "100";
    if (oSapAdm.recordCount() == 1)

    {
      if (!oSapAdm.getRecord(0).hasNullValue(Sapadmin.imp_max_recs))
      {
        maxrec = oSapAdm.getRecord(0).getSaveStringValue(Sapadmin.imp_max_recs);
      }

    }
    else
    {
      throw new SapProcessingException("SAP Administrationsdatensatz nicht vorhanden oder nicht eindeutig");
    }
    return maxrec;

  }

  public static void writeSAPObjects() throws Exception
  {
    String locrecstat = "";
    CSAPHelperClass.printDebug("StartWriteObjects" + (new Date()));
    Context context = Context.getCurrent();
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataAccessor accChExtId = context.getDataAccessor().newAccessor();
    // Import braucht ext.system
    final IDataTable oSapAdm = context.getDataTable(Sapadmin.NAME);
    oSapAdm.qbeClear();
    oSapAdm.qbeSetValue(Sapadmin.active, "1");
    oSapAdm.search();
    String extSystemObj = "";
    String sstdWerk = "";
    if (oSapAdm.recordCount() == 1)

    {
      extSystemObj = oSapAdm.getRecord(0).getSaveStringValue(Sapadmin.ext_system_key);
      sstdWerk = oSapAdm.getRecord(0).getSaveStringValue(Sapadmin.sap_faplissite_key);
    }
    String imp_kind = "";
    IDataTable objex = acc.getTable(Sap_objimport.NAME);
    IDataTable object = acc.getTable(Object.NAME);
    IDataTable objectChExtId = accChExtId.getTable(Object.NAME);
    IDataTable log = acc.getTable(Saplog.NAME);
    objex.setMaxRecords(objex.UNLIMITED_RECORDS);
    objex.clear();
    objex.qbeClear();
    objex.qbeSetValue(Sap_objimport.imp_error, "0");
    objex.qbeSetValue(Sap_objimport.imp_status, imp_status_ENUM._ToDo);
    objex.search();
    CSAPHelperClass.printDebug("Objekte zu verarbeiten: " + objex.recordCount());
    for (int i = 0; i < objex.recordCount(); i++)
    {

      IDataTableRecord objexrec = objex.getRecord(i);
      // Prüfen ob Status oder import
      String imptype = "all";

      IDataTransaction transaction = acc.newTransaction();
      IDataTransaction transaction2 = acc.newTransaction();
      
      

      CSAPHelperClass.printDebug("  *Importing Object-Record  " + (i + 1) + "*");
      boolean commitReached = false;
      String eqnummer = "";
      String eqname = "";
      try
      {

        object.qbeClear();

        // Feststellen ob equipment oder technischer platz

        // Wenn kein Equipment
        String objtype = "";
        if (objexrec.hasNullValue(Sap_objimport.eqnummer))
        {
          if (objexrec.hasNullValue(Sap_objimport.kz_techplatz))
          {
            throw new SapProcessingException("Weder Equipmentnummer noch Technischer Platz wurden übergeben");
          }
          else
          {
            objtype = "tp";
            eqnummer = objexrec.getSaveStringValue(Sap_objimport.kz_techplatz);
            if ("".equals(objexrec.getSaveStringValue(Sap_objimport.bez_techplatz)) || objexrec.getSaveStringValue(Sap_objimport.bez_techplatz) == null)
            {
              eqname = eqnummer;
            }
            else
            {
              eqname = objexrec.getSaveStringValue(Sap_objimport.bez_techplatz);
            }

          }
        }
        // Also Equipment
        else
        {
          objtype = "eq";
          eqnummer = objexrec.getSaveStringValue(Sap_objimport.eqnummer);
          if ("".equals(objexrec.getSaveStringValue(Sap_objimport.eqname)) || objexrec.getSaveStringValue(Sap_objimport.eqname) == null)
          {
            eqname = eqnummer;
          }
          else
          {
            eqname = objexrec.getSaveStringValue(Sap_objimport.eqname);
          }

        }
        
//      Abhandlung Änderung ExternalID (8.7.08)
        
        if (!objexrec.hasNullValue(Sap_objimport.changed_extid))
        {
          
          objectChExtId.qbeClear();
          objectChExtId.qbeSetKeyValue(Object.external_id, objexrec.getSaveStringValue(Sap_objimport.changed_extid));
          objectChExtId.search();
          if (objectChExtId.recordCount()==1)
          {
            IDataTableRecord objectChExtIdRec = objectChExtId.getRecord(0);
            IDataTransaction transactionChangeExtId = acc.newTransaction();
            IDataTransaction transaction3 = acc.newTransaction();
            try 
            {
              objectChExtIdRec.setValue(transactionChangeExtId, Object.external_id, eqnummer);
              transactionChangeExtId.commit();
              // Log erzeugen
              IDataTableRecord logrec = log.newRecord(transaction3);
              

              logrec.setValue(transaction3, Saplog.message, "ExternalID wurde geändert von: "+objexrec.getSaveStringValue(Sap_objimport.changed_extid)+ " nach: "  + eqnummer);
              logrec.setValue(transaction3, Saplog.modus, "Import");
              logrec.setValue(transaction3, Saplog.object, eqnummer);

              transaction3.commit();
            } 
            catch (Exception e) 
            {
              //TODO Handle Exeption
              System.out.println("**********Fehler beim umbenennen des Objektes: " +objexrec.getSaveStringValue(Sap_objimport.changed_extid));
              System.out.println(e.getMessage());
            }
            finally
            {
              transactionChangeExtId.close();
              transaction3.close();
            }
          }
          else 
          { // Alter Datensatz wurde nicht gefunden
            IDataTransaction transaction3 = acc.newTransaction();
            try 
            {
              IDataTableRecord logrec = log.newRecord(transaction3);
              

              logrec.setValue(transaction3, Saplog.message, "ExternalID "+objexrec.getSaveStringValue(Sap_objimport.changed_extid)+ 
                  "nicht gefunden - Änderung nicht möglich, "  + eqnummer + " wird neu angelegt");
              logrec.setValue(transaction3, Saplog.modus, "Import");
              logrec.setValue(transaction3, Saplog.object, eqnummer);

              transaction3.commit();
            } 
            catch (Exception e) 
            {
              System.out.println(e.getMessage());
              // TODO: handle exception
            }
            finally
            {
              transaction3.close();
            }
          }
          
        } // Ende Abhandlung Änderung ExternalID
        
        object.qbeSetKeyValue(Object.external_id, eqnummer);
        object.qbeSetKeyValue(Object.ext_system_key, extSystemObj);
        object.search();
        IDataTableRecord objectrec;
        IDataTable newLocation;
        IDataTableRecord newLocRec = null;
        IDataTableRecord objectrecLocaton;
        if (object.recordCount() == 0 && objexrec.getSaveStringValue(Sap_objimport.systemstatus).equals("LÖVM"))
        {
          objexrec.setValue(transaction2, Sap_objimport.imp_status, Sap_objimport.imp_status_ENUM._Done);
          // Log erzeugen
          IDataTableRecord logrec = log.newRecord(transaction2);
          String sImpStatus = "Löschen";

          logrec.setValue(transaction2, Saplog.message, "Object-Datensatz wurde importiert. - " + sImpStatus);
          logrec.setValue(transaction2, Saplog.modus, "Import");
          logrec.setValue(transaction2, Saplog.object, eqnummer);
          transaction2.commit();
          continue;
        }

        if (object.recordCount() == 1)
        {
          CSAPHelperClass.printDebug("wir haben ein Objekt und ändern");
          objectrecLocaton = object.getRecord(0);

          // Wenn wir keinen locationrecord haben dann legen
          // wir einen an
          if (!objectrecLocaton.hasLinkedRecord(Objectlocation.NAME))
          {
            newLocation = acc.getTable(Objectlocation.NAME);
            newLocRec = newLocation.newRecord(transaction);
            locrecstat = "newLocRec";
          }
          else
          {
            newLocRec = objectrecLocaton.getLinkedRecord(Objectlocation.NAME);
            locrecstat = "updLocRec";
          }
          objectrec = object.getRecord(0);
          imp_kind = "change";
        }

        else
        {

          // Wenn ein neuer Satz erstellt wird, dan brauchen wir noch einen
          // locationsatz, der vorher gespeichert werden muß
          newLocation = acc.getTable(Objectlocation.NAME);
          newLocRec = newLocation.newRecord(transaction);
          locrecstat = "newLocRec";
          // jetzt erst legen wir den objektsatz an
          objectrec = object.newRecord(transaction);
          imp_kind = "new";
        }

        // Object anlegen oder ändern

        if (imp_kind.equals("new"))
        {
          CSAPHelperClass.printDebug("wir haben ein neues Objekt ");
          // Bei neu
          objectrec.setValue(transaction, Object.external_id, eqnummer);
          objectrec.setValue(transaction, Object.ext_system_key, extSystemObj);
        }
        // Nach Änderung in SAP: Status bei neu oder Änderung überehmen:
        // TODO s.o. if (imptype.equals("status")) ... entfernen

        if (!SAP_JCOB_STAT.containsKey(objexrec.getSaveStringValue(Sap_objimport.systemstatus)))
        {
          throw new SapProcessingException("Statusänderung nicht möglich, Status: " + objexrec.getSaveStringValue(Sap_objimport.systemstatus) + " NICHT gültig");
        }
        else
        {
          objectrec.setValue(transaction, Object.objstatus, SAP_JCOB_STAT.get(objexrec.getSaveStringValue(Sap_objimport.systemstatus)));
        }

        // Defensives Vorgehen:
        // Wenn ein Feld leer angeliefert wird, überschreiben wir nicht

        // Beim Ändern und bei neu
        // Noch verbessern??
        // Texte ...
        if (!"".equals(eqname))
        {
          objectrec.setValue(transaction, Object.name, eqname);
        }

        // Garantie
        if (!objexrec.hasNullValue(Sap_objimport.garantiebeginn) && objexrec.getStringValue(Sap_objimport.garantiebeginn).equals("000000"))
        {
          Date warrantyStart = DateFormater.SAP2Java(Sap_objimport.garantiebeginn);
          objectrec.setValue(transaction, Object.warranty_begin, warrantyStart);
        }
        if (!objexrec.hasNullValue(Sap_objimport.garantieende) && objexrec.getStringValue(Sap_objimport.garantieende).equals("000000"))
        {
          Date warrantyStart = DateFormater.SAP2Java(Sap_objimport.garantieende);
          objectrec.setValue(transaction, Object.warranty_end, warrantyStart);
        }

        // Hersteller
        if (!objexrec.hasNullValue(Sap_objimport.anl_hersteller))
        {
          objectrec.setValue(transaction, Object.vendor, objexrec.getStringValue(Sap_objimport.anl_hersteller));
        }

        // Inventar

        if (!objexrec.hasNullValue(Sap_objimport.invnr))
        {
          objectrec.setValue(transaction, Object.assettag, objexrec.getStringValue(Sap_objimport.invnr));
        }

        // Überg. Object
        if (!objexrec.hasNullValue(Sap_objimport.techpl_oberhalb))
        {
          objectrec.setValue(transaction, Object.object_above, objexrec.getStringValue(Sap_objimport.techpl_oberhalb));
        }

        // Linked records

        // Werk : Sap Standortwerk
        setLocation(context, transaction, newLocRec, objexrec, objtype, sstdWerk, locrecstat);
        objectrec.setLinkedRecord(transaction, newLocRec);

        // Alte Edvin ID
        String sedvin = "";
        if (objtype.equals("tp"))
        {
          sedvin = objexrec.getSaveStringValue(Sap_objimport.tp_edvin_id);
        }
        else if (objtype.equals("eq"))
        {
          sedvin = objexrec.getSaveStringValue(Sap_objimport.eq_edvin_id);
        }
        if (!sedvin.equals("") && sedvin != null)
        {
          IDataTable edvin = context.getDataTable(Sap_edvin_object.NAME);
          edvin.qbeClear();
          edvin.qbeSetKeyValue(Sap_edvin_object.external_id, sedvin);
          edvin.search();
          if (edvin.recordCount() == 1)
          {
            objectrec.setValue(transaction, "sap_edvin_object_key", edvin.getRecord(0).getValue(Sap_edvin_object.pkey));
          }
        }
        
        // Kostenstelle
        if (!objexrec.hasNullValue(Sap_objimport.kostenstelle))
        {
          // Ja!, Kostenstelle im TTS suchen
          IDataTable costcenter = acc.getTable(Accountingcode.NAME);
          costcenter.qbeClear();
          costcenter.qbeSetKeyValue(Accountingcode.code, objexrec.getSaveStringValue(Sap_objimport.kostenstelle));
          costcenter.search();

          // gibt es SAP Kostenstelle im TTS?
          if (costcenter.recordCount() == 1)
          {
            // Ja!, Kostenstelle setzen
            objectrec.setValue(transaction, Object.accountingcode_key, objexrec.getSaveStringValue(Sap_objimport.kostenstelle));
          }
        }

        transaction.commit();

        commitReached = true;
        // / Quittieren
        objexrec.setValue(transaction2, Sap_objimport.imp_status, Sap_objimport.imp_status_ENUM._Done);
        // Log erzeugen
        IDataTableRecord logrec = log.newRecord(transaction2);
        String sImpStatus = " Type NEU oder ÄNDERUNG";

        logrec.setValue(transaction2, Saplog.message, "Object-Datensatz wurde importiert. - " + sImpStatus);
        logrec.setValue(transaction2, Saplog.modus, "Import");
        logrec.setValue(transaction2, Saplog.object, eqnummer);

        transaction2.commit();

      }

      catch (Exception e)
      {
        if (commitReached)
        {

          // hier ist was beim Schreiben des Logrecords schief gegangen -> also
          // nicht noch mal probieren
          throw e;
        }

        // ein Fehler in der Abarbeitung und nicht beim Schreiben des
        // Log-Records

        // Log-Meldung mit Stacktrace ins catalina.out schreiben
        logger.error("sapObjectImport() failed", e);
        try
        {
          objexrec.setIntValue(transaction2, Sap_objimport.imp_error, 1);
          objexrec.setStringValueWithTruncation(transaction2, Sap_objimport.message, e.getMessage());
          objexrec.setValue(transaction2, Sap_objimport.imp_status, Sap_objimport.imp_status_ENUM._ToDo);
          IDataTableRecord logrec = log.newRecord(transaction2);
          String sImpStatus = " Type NEU oder ÄNDERUNG";
          logrec.setStringValueWithTruncation(transaction2, Saplog.message, e.getMessage() + " - kein Import" + sImpStatus);
          logrec.setValue(transaction2, Saplog.modus, "Import");
          logrec.setValue(transaction2, Saplog.object, eqnummer);
          logrec.setStringValueWithTruncation(transaction2, Saplog.detail, e.getMessage());
          logrec.setIntValue(transaction2, Saplog.errorstatus, 1);
          transaction2.commit();
        }
        catch (Exception e1)
        {
          throw e1;
        }
        finally
        {
          transaction2.close();
        }
      }
      finally
      {
        transaction.close();

      }
    }
    System.out.println("objectwrite endet at " + new Date());
  }

  public static void getSAPObjects(final ConnManager oCon, final String sfromDate, final String sfromTime, final String sObject) throws SapProcessingException
  {
    Context context = Context.getCurrent();
    // Debug Modus abfragen und ggf. setzen
    try
    {
      if (CSAPHelperClass.checkDebug()) //
      {
        context.setPropertyForSession("SAPdebug", "1");
      }
      else
      {
        context.setPropertyForSession("SAPdebug", "0");
      }
    }
    catch (Exception e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    // Import braucht ext.system
    final IDataTable oSapAdm = context.getDataTable(Sapadmin.NAME);
    oSapAdm.qbeClear();
    Map oRFCInParam = new HashMap();
    IDataAccessor acc = context.getDataAccessor();
    IDataTable exchange = acc.getTable(Sap_objimport.NAME);
    String sMoreRecs = "X";
    String sED = "0";
    // Das arbeitet nicht richtig :Mohamend fragen
    while (sMoreRecs.equals("X"))
    {

      try
      {
        oRFCInParam.put("ID_DATE_OF_CHANGE", sfromDate);
        oRFCInParam.put("ID_TIME_OF_CHANGE", sfromTime);
        oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
        oRFCInParam.put("ID_MAX_REC_PER_CALL", getMaxRecs());
        if (!sObject.equals(""))
        {
          oRFCInParam.put("ID_OBJECTID", sObject);
        }
        // oRFCInParam.put("ID_OBJ_MODE", "OBJ");
        // Input: MaxRecords
        // "
        // Debug: Inputparameter ausgeben
        //CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für getSAPObjects ");
        

        // RFC Aufruf
        CFunction oFct = new CFunction(oCon.getPoolName());
        CResult oResult = oFct.exec("Z_054B_RFC_READ_CD_PM_TECH_OBJ", oRFCInParam, new CSapReturn[]
          { new CSapReturn(CSapReturn.TYPE_TABLE, "ET_TECH_OBJ_CHANGES"), new CSapReturn(CSapReturn.TYPE_FIELD, "ED_REC_SENT_OBJ"),
              new CSapReturn(CSapReturn.TYPE_FIELD, "ED_MAX_RECS_REACHED"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });
        // TODO Mohamed muss Error-Rückgabe ändern: Kein Error, nur weil noch
        // Sätze vorhanden
        if (!oResult.isSuccess() && !"Maximale Anzahl der zu übertragenen Objekte ist erreicht worden.".equals(oResult.getLogMessage()))
        {
          // Debug: Outputparameter ausgeben
          if (oResult.isSimpleResult())
          {
            Map oData = oResult.getSimpleResult();
            CSAPHelperClass.WriteSAPOutputParam(oData, "   * No Success: OutputParameter für getSAPObjects ");
            sMoreRecs = "";
            CSAPHelperClass.printDebug("Bei X hinter Doppelpunkt sind noch nicht alle Sätze abgeholt:" + sMoreRecs);
          }
          else
          {
            throw new SapProcessingException("***SAP-SCHNITTSTELLE: Fehler beim Import (Objekte), Rückgabe Parameter unlesbar");
          }

          // Hier soll sysout sein s.u.
          System.out.println("***SAP-SCHNITTSTELLE: Fehler beim Import (Objekte)");

        }
        else
        {
          CTable oTbl = oResult.getPrimaryTableResult();
          CSAPHelperClass.printDebug(" **SAPExchange:" + oTbl.getNumRows() + " Oject-Records to import **");
          Map oDatares = oResult.getSimpleResult();

          sED = (String) oDatares.get("ED_REC_SENT_OBJ");
          if (null == sED)
          {
            sMoreRecs = "";
          }
          else
          {
            int iED = Integer.parseInt(sED);
            sMoreRecs = (String) oDatares.get("ED_MAX_RECS_REACHED");
            if (iED == 0)
            {
              sMoreRecs = "";
            }
          }
          CSAPHelperClass.printDebug("Bei X hinter Doppelpunkt sind noch nicht alle Sätze abgeholt:" + sMoreRecs);
          CSAPHelperClass.printDebug(" **SAPExchange:" + sED + " Oject-Records sent from SAP **");

          IDataTransaction transaction = acc.newTransaction();
          try
          {
            for (int i = 0; i < oTbl.getNumRows(); ++i)
            {

              IDataTableRecord imprec = exchange.newRecord(transaction);

              // Sätze 1:1 schreiben

              Map oJcobData = oTbl.transferFields(SAP_JCOB_OBJ, i);
              Iterator oIter = oJcobData.entrySet().iterator();
              while (oIter.hasNext())
              {
                Map.Entry oData = (Map.Entry) oIter.next();
                imprec.setValue(transaction, (String) oData.getKey(), (String) oData.getValue());
              } // while

              imprec.setValue(transaction, Sap_objimport.imp_status, Sap_objimport.imp_status_ENUM._ToDo);
              imprec.setValue(transaction, Sap_objimport.imp_type, Sap_objimport.imp_type_ENUM._all);

            }
            transaction.commit();
          }
          finally
          {
            transaction.close();
          }
        }
      }
      catch (Exception e)
      {
        throw new SapProcessingException(e);
      }
    }// Endwhile
  }

  public static void getSAPObjStatus(final ConnManager oCon, final String sFromDate, final String sfromTime) throws SapProcessingException
  {
    // Debug Modus abfragen und ggf. setzen
    try
    {
      if (CSAPHelperClass.checkDebug()) //
      {
        Context.getCurrent().setPropertyForSession("SAPdebug", "1");
      }
      else
      {
        Context.getCurrent().setPropertyForSession("SAPdebug", "0");
      }
    }
    catch (Exception e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Map oRFCInParam = new HashMap();
    Context context = Context.getCurrent();
    IDataAccessor acc = context.getDataAccessor();
    IDataTable exchange = acc.getTable(Sap_objimport.NAME);
    String sMoreRecs = "X";
    String sED = "0";
    // Das arbeitet nicht richtig :Mohamend fragen

    try
    {
      oRFCInParam.put("ID_DATE_OF_CHANGE", sFromDate);
      oRFCInParam.put("ID_TIME_OF_CHANGE", sfromTime);
      oRFCInParam.put("ID_SYSTEM", "JACOB-TTS");
      // Herr Mohamed hält es für sinvoll alle Statusänderungen in einmal
      // abzuholen
      // oRFCInParam.put("ID_MAX_REC_PER_CALL", getMaxRecs());
      // oRFCInParam.put("ID_OBJ_MODE", "STA");
      // Debug: Inputparameter ausgeben
      CSAPHelperClass.WriteSAPInputParam(oRFCInParam, "   * InputParameter für getSAPObjects ");
      // RFC Aufruf
      CFunction oFct = new CFunction(oCon.getPoolName());
      CResult oResult = oFct.exec("Z_054B_RFC_READ_CD_PM_TECH_OBJ", oRFCInParam, new CSapReturn[]
        { new CSapReturn(CSapReturn.TYPE_TABLE, "ET_TECH_OBJ_STATUS"), new CSapReturn(CSapReturn.TYPE_STRUCTRE, "ES_RETURN") });
      if (!oResult.isSuccess())
      {
        // Debug: Outputparameter ausgeben
        CSAPHelperClass.printDebug(oResult.getLogMessage());
        if (oResult.isSimpleResult())
        {
          Map oData = oResult.getSimpleResult();
          CSAPHelperClass.WriteSAPOutputParam(oData, "   * No Success: OutputParameter für getSAPObjstatus ");
          sMoreRecs = "";
        }
        // Hier soll sysout sein s.u.
        System.out.println("***SAP-SCHNITTSTELLE: Fehler beim Import (Objekte/Status)");
        // TODO S.o.
      }
      else
      {
        CTable oTbl = oResult.getPrimaryTableResult();
        CSAPHelperClass.printDebug(" **SAPExchange:" + oTbl.getNumRows() + " Oject/Status-Records to import **");
        Map oDatares = oResult.getSimpleResult();

        sED = (String) oDatares.get("ED_REC_SENT_STAT");
        if (null == sED)
        {
          sMoreRecs = "";
        }
        else
        {
          int iED = Integer.parseInt(sED);
          sMoreRecs = (String) oDatares.get("ED_MAX_RECS_REACHED");
          if (iED == 0)
          {
            sMoreRecs = "";
          }
        }
        CSAPHelperClass.printDebug("Bei X hinter Doppelpunkt sind noch nicht alle Sätze abgeholt:" + sMoreRecs);
        CSAPHelperClass.printDebug(" **SAPExchange:" + sED + " Oject-Statschanges sent from SAP **");

        for (int i = 0; i < oTbl.getNumRows(); ++i)
        {
          IDataTransaction transaction = acc.newTransaction();
          try
          {
            IDataTableRecord imprec = exchange.newRecord(transaction);

            // Sätze 1:1 schreiben

            Map oJcobData = oTbl.transferFields(SAP_JCOB_OBJSTAT, i);
            Iterator oIter = oJcobData.entrySet().iterator();
            while (oIter.hasNext())
            {
              Map.Entry oData = (Map.Entry) oIter.next();
              imprec.setValue(transaction, (String) oData.getKey(), (String) oData.getValue());
            } // while

            imprec.setValue(transaction, Sap_objimport.imp_status, Sap_objimport.imp_status_ENUM._ToDo);
            imprec.setValue(transaction, Sap_objimport.imp_type, Sap_objimport.imp_type_ENUM._status);

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
  private CImportObjects()
  {
    // Leer
  }
}
