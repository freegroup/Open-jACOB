/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Oct 25 17:10:51 CEST 2007
 */
package jacob.common.gui.sap_objimport;

import jacob.common.AppLogger;
import jacob.common.sap.CSAPHelperClass;
import jacob.exception.SapProcessingException;
import jacob.model.Object;
import jacob.model.Sap_first_objectimp;
import jacob.model.Sap_objimport;
import jacob.model.Sapadmin;
import jacob.model.Saplog;
import jacob.model.Sap_objimport.imp_status_ENUM;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the SelectNewEdvin generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class SelectNewEdvin extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: SelectNewEdvin.java,v 1.1 2007/12/18 14:17:16 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * The user has clicked on the corresponding button.<br>
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {

    // Rest ersetzen durch
    // CSelectNewEdvin.selectNewEdvinRecs(context);

    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataAccessor accurimp = context.getDataAccessor().newAccessor();
    final IDataTable oSapAdm = context.getDataTable(Sapadmin.NAME);
    IDataTable urimp = accurimp.getTable(Sap_first_objectimp.NAME);
    // IDataTransaction transur = urimp.startNewTransaction();
    oSapAdm.qbeClear();
    oSapAdm.qbeSetValue(Sapadmin.active, "1");
    oSapAdm.search();
    String extSystemObj = "";
    if (oSapAdm.recordCount() == 1)

    {
      extSystemObj = oSapAdm.getRecord(0).getSaveStringValue(Sapadmin.ext_system_key);
    }

    IDataTable objex = acc.getTable(Sap_objimport.NAME);
    IDataTable object = acc.getTable(Object.NAME);
    objex.clear();
    objex.qbeClear();
    objex.qbeSetValue(Sap_objimport.imp_error, "0");
    objex.qbeSetValue(Sap_objimport.imp_status, imp_status_ENUM._ToDo);
    // Mußte leider entfernt werden, da es um zwei Felder geht, und die QBE in
    // 2.5 keine Veroderung kann
    // s.u. If Abfrage
    // objex.qbeSetValue(Sap_objimport.old_edvin_object, "!null");

    // Für Testzwecke
    // objex.qbeSetValue(Sap_objimport.pkey, "<3640");
    objex.setMaxRecords(objex.UNLIMITED_RECORDS);
    objex.search();
    CSAPHelperClass.printDebug("Objekte " + objex.recordCount() + "");
    String eqnummer = "";
    String commitdup = "";
    String sOldEdvin = "";

    for (int i = 0; i < objex.recordCount(); i++)
    {

      IDataTableRecord objexrec = objex.getRecord(i);
      // Überprüfen, ob Edvin-Relevant
      if (objexrec.hasNullValue(Sap_objimport.old_edvin_object) && objexrec.hasNullValue(Sap_objimport.old_edvin_techplatz))
      {
        // nicht relevant
      }
      else
      {
        object.qbeClear();
        // Feststellen ob equipment oder technischer platz

        // Wenn kein Equipment
        if (objexrec.hasNullValue(Sap_objimport.eqnummer))
        {
          if (objexrec.hasNullValue(Sap_objimport.kz_techplatz))
          {
            throw new SapProcessingException("Weder Equipmentnummer noch Technischer Platz wurden übergeben");
          }
          else
          {
            eqnummer = objexrec.getSaveStringValue(Sap_objimport.kz_techplatz);
            sOldEdvin = objexrec.getSaveStringValue(Sap_objimport.old_edvin_techplatz);
          }
        }
        // Also Equipment
        else
        {
          eqnummer = objexrec.getSaveStringValue(Sap_objimport.eqnummer);
          sOldEdvin = objexrec.getSaveStringValue(Sap_objimport.old_edvin_object);
        }
        object.qbeSetKeyValue(Object.external_id, eqnummer);
        object.qbeSetKeyValue(Object.ext_system_key, extSystemObj);
        object.search();
        // Wenn keine SAP ID in TTS vorhaden, dann machen wir den "Ur-Import"
        // (wenn wir das Edvinobjekt finden)
        if (object.recordCount() == 0)
        {

          object.qbeClear();
          object.qbeSetKeyValue(Object.external_id, sOldEdvin);
          object.search();
          if (object.recordCount() == 1)
          {
            // Alles wird gut
            // Neuen Satz in Urimport schreiben
            IDataTransaction transur = urimp.startNewTransaction();
            try
            {
              IDataTableRecord objrec = object.getRecord(0);
              CSAPHelperClass.printDebug(objrec.getSaveStringValue(Object.external_id) + "Edvin Object gefunden, kopieren!");
              IDataTableRecord urimprec = urimp.newRecord(transur);
              urimprec.setValue(transur, Sap_first_objectimp.edvin_object, sOldEdvin);
              urimprec.setValue(transur, Sap_first_objectimp.sap_object, eqnummer);
              transur.commit();
            }

            catch (Exception e)
            {
              IDataTable log = acc.getTable(Saplog.NAME);
              IDataTransaction translog = log.startNewTransaction();
              IDataTableRecord logrec = log.newRecord(translog);
              try
              {
                logrec.setValue(translog, Saplog.message, "Urimportssatz wurde nicht geschrieben ");
                logrec.setValue(translog, Saplog.modus, "Import");
                logrec.setValue(translog, Saplog.object, eqnummer);
                logrec.setIntValue(translog, Saplog.errorstatus, 1);
                logrec.setValue(translog, Saplog.detail, e.getMessage());
                translog.commit();
              }
              finally
              {
                translog.close();
              }
            }
            finally
            {
              transur.close();
            }

          }
          else if (object.recordCount() == 0)
          {
            // Schade, nichts zum Kopieren vorhanden__> Error setzen
            CSAPHelperClass.printDebug(sOldEdvin + " Nicht gefunden, schade ");
          }
          else
          {
            IDataTransaction transur = urimp.startNewTransaction();
            IDataTransaction transdub = acc.newTransaction();
            // Leider viele gefunden, also abarbeiten
            try
            {
              IDataTableRecord urimprec = urimp.newRecord(transur);
              urimprec.setValue(transur, Sap_first_objectimp.edvin_object, sOldEdvin);
              urimprec.setValue(transur, Sap_first_objectimp.sap_object, eqnummer);
              CSAPHelperClass.printDebug(eqnummer + " viele gefunden nämlich " + object.recordCount());

              for (int j = 0; j < object.recordCount() - 1; j++)
              {
                IDataTableRecord objdublette = object.getRecord(j);
                objdublette.setValue(transdub, Object.sap_old_edvin_status, objdublette.getValue(Object.objstatus));
                objdublette.setValue(transdub, Object.objstatus, Object.objstatus_ENUM._geloescht);

              }
              transdub.commit();
              commitdup = "OK";
              transur.commit();
            }

            catch (Exception ex)
            {
              String mess = "";
              if (commitdup.equals("OK"))
              {
                mess = "Urimportssatz wurde nicht geschrieben ";
              }
              else
              {
                mess = "Dubletten wurden nicht gelöscht ";
              }
              IDataTable log = acc.getTable(Saplog.NAME);
              IDataTransaction translog = log.startNewTransaction();
              IDataTableRecord logrec = log.newRecord(translog);
              try
              {
                logrec.setValue(translog, Saplog.message, mess);
                logrec.setValue(translog, Saplog.modus, "Import");
                logrec.setValue(translog, Saplog.object, eqnummer);
                logrec.setIntValue(translog, Saplog.errorstatus, 1);
                logrec.setValue(translog, Saplog.detail, ex.getMessage());
                translog.commit();
              }
              finally
              {
                translog.close();
              }
            }
            finally
            {
              transdub.close();
              transur.close();
            }

          }

        }
        else
        {
          CSAPHelperClass.printDebug(eqnummer + " Ipro gefunden");
        }

      }
    }
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // You can enable/disable the button in relation to your conditions.
    //
    // button.setEnable(true/false);
  }
}
