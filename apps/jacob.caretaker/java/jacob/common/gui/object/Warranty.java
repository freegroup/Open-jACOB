/*
 * Created on 08.12.2004
 * by mike
 *
 */
package jacob.common.gui.object;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;

/**
 * Hillfklasse zur gew�hrleistungs�berpr�fung
 * 
 * @author mike
 * 
 */
public class Warranty
{
    /**
     * Callback Klasse f�r den Nachfragedialog. Eingegebenen Werte in das
     * Dokumenteintragen und einen Eintrag in den yan_task machen.
     * 
     */
    static class AskCallback implements IFormDialogCallback
    {
        final Map name2Key;

        AskCallback(Map name2Key)
        {
            this.name2Key = name2Key;
        }

        public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
        {
          String warrentyStatus ="nicht verfolgen";  
          IDataTableRecord callRecord = context.getSelectedRecord();
          IDataTable objectTable = context.getDataTable("object");
          
          if (buttonId.equals("takeover"))
            {
                String userSelection = (String) formValues.get("object");
                String objectKey = (String) name2Key.get(userSelection);
                objectTable.qbeClear();
                objectTable.qbeSetKeyValue("pkey", objectKey);
                objectTable.search();
                warrentyStatus ="�berpr�fen";
            }
        
            if (objectTable.recordCount() == 1)
            {
                IDataTransaction trans = callRecord.getCurrentTransaction();
                if (trans == null)
                    trans = context.getDataAccessor().newTransaction();
                try
                {
                    callRecord.setValue(trans, "warrentystatus", warrentyStatus);
                    callRecord.setLinkedRecord(trans, objectTable.getRecord(0));
                    trans.commit();
                }
                finally
                {
                    trans.close();
                }
            }
        }
    }

    /**
     * �berpr�ft ob das Objekt direkt Gew�hrleistung hat
     * 
     * @param context
     * @param object
     * @return
     * @throws Exception
     */
    private static boolean hasWarranty(IClientContext context, IDataTableRecord object) throws Exception
    {
        Date today = new Date();
        Date warrantyEnd = object.getDateValue("warranty_end");
        Date warrantyExtension = object.getDateValue("warranty_extension");
        if (warrantyEnd != null)
        {
            if (today.before(warrantyEnd))
            {
                return true;
            }
        }
        if (warrantyExtension != null)
        {
            if (today.before(warrantyExtension))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * schau ob untergeordnete Objekte Gew�hrleistung haben.
     * 
     * @param context
     * @param object
     * @throws Exception
     */
    private static boolean checkSubObjectWarranty(IClientContext context, IDataTableRecord object) throws Exception
    {
        Date today = new Date();
        IDataAccessor newAccessor = context.getDataAccessor().newAccessor();
        IDataTable objectTable = newAccessor.getTable("object");
        IDataBrowser objectBrowser = newAccessor.getBrowser("objectBrowser");
        newAccessor.qbeClearAll();

        objectTable.qbeSetKeyValue("object_above", object.getValue("external_id"));
        objectTable.qbeSetKeyValue("ext_system_key", object.getValue("ext_system_key"));
        // Achtung schauen ob Gew�hrleistung oder Gew�hrleistungsverl�ngerung
        // besteht
        objectBrowser.searchWhere("r_object", Filldirection.BACKWARD, "(object.warranty_end > sysdate or object.warranty_extension> sysdate)");
        if (objectBrowser.recordCount() > 0)
        {
            // es existieren Untergewerke mit Gew�hrleistung Anzeigen und
            // Nachfragen
            Map name2Key = new HashMap();
            String[] names = new String[objectBrowser.recordCount()];
            for (int i = 0; i < objectBrowser.recordCount(); i++)
            {
                IDataTableRecord currentRecord = objectBrowser.getRecord(i).getTableRecord();
                names[i] = currentRecord.getSaveStringValue("external_id") + " | " + currentRecord.getSaveStringValue("name");

                // store the correponding Object Record in a map
                name2Key.put(names[i], currentRecord.getStringValue("pkey"));
            }

            // Create a FormDialog with a list of all available doc templates.
            //
            FormLayout layout = new FormLayout("10dlu, p,10dlu", // columns
                    "5dlu,p,5dlu,p,5dlu,p,10dlu,250dlu,10dlu"); // rows
            CellConstraints cc = new CellConstraints();
            IFormDialog dialog = context.createFormDialog("Gew�hrleistung", layout, new AskCallback(name2Key));
            dialog.addHeader("�berpr�fung untergeordneter Objekte", cc.xy(1, 1));
            dialog.addLabel("Die Gew�hrleistungszeit von untergeordneten Objekten des zugeh�rigen Objektes ist noch nicht abgelaufen.", cc.xy(1, 3));
            dialog.addLabel("Bitte �berpr�fen Sie m�gliche Gew�hrleistungsanspr�che folgender Objekte ", cc.xy(1, 5));
            dialog.addListBox("object", names, 0, cc.xy(1, 7));
            dialog.addSubmitButton("takeover", "�bernehmen");
            dialog.addSubmitButton("standby", "Nicht �bernehmen");
            // Show the dialog with a prefered size. The dialog trys to resize
            // to the
            // optimum size!
            dialog.show(600, 500);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * �berpr�ft ob die Meldung Gew�hrleistungsanspruch hat
     * 
     * @param context
     * @param callRecord
     * @throws Exception
     */
    public static boolean checkWarranty(IClientContext context, IDataTableRecord callRecord) throws Exception
    {
        if (!(callRecord.hasLinkedRecord("object") && callRecord.getValue("warrentystatus") == null && callRecord.hasLinkedRecord("process")))
        {
            // nichts zu tun, also raus
            return true;
        }
        if (!callRecord.getLinkedRecord("process").getSaveStringValue("checkwarrenty").equals("1"))
        {
            // T�tigkeit erfordert keine �berpr�fung
            return true;
        }
        if (hasWarranty(context, callRecord.getLinkedRecord("object")))
        {
            IDataTransaction trans = callRecord.getCurrentTransaction();
            boolean hasTransaction = true;
            if (trans == null)
            {
                trans = context.getDataAccessor().newTransaction();
                hasTransaction = false;
            }
            if (!hasTransaction)
            {

                try
                {
                    callRecord.setValue(trans, "warrentystatus", "�berpr�fen");
                    trans.commit();
                }
                finally
                {
                    trans.close();
                }
            }
            else
            {
                callRecord.setValue(trans, "warrentystatus", "�berpr�fen"); 
            }
            IMessageDialog dialog = context
                    .createMessageDialog("Die Gew�hrleistungszeit des zugeh�rigen Objektes ist noch nicht abgelaufen.\\n Bitte �berpr�fen Sie m�gliche Gew�hrleistungsanspr�che!");
            dialog.show();
            return true;
        }
        else
           return checkSubObjectWarranty(context, callRecord.getLinkedRecord("object"));
        

    }
}
