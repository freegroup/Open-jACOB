/*
 * Created on 21.06.2004
 * by mike
 *
 */
package jacob.common.gui.call;

import jacob.common.data.AuftragsKoordinator;
import jacob.common.data.Routing;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * 
 * @author mike
 * 
 */
public class CallRouting extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CallRouting.java,v 1.19 2008/04/29 16:50:29 sonntag Exp $";

    static public final transient String RCS_REV = "$Revision: 1.19 $";

    public static final String DURCHGESTELLT = "Durchgestellt";

    public static final String VERWORFEN = "Verworfen";

    public static final String AK_ZUGEWIESEN = "AK zugewiesen";

    public static final String FEHLGEROUTET = "Fehlgeroutet";

    // Set welches alle Status vor "Angenommen" enthält
    private static final Set allStatusBeforeAngenommen = new HashSet();
    static
    {
        // -------------------------------------------------------------------------------
        // Statusmaps initialisieren
        // -------------------------------------------------------------------------------
        allStatusBeforeAngenommen.add(DURCHGESTELLT);
        allStatusBeforeAngenommen.add(VERWORFEN);
        allStatusBeforeAngenommen.add(AK_ZUGEWIESEN);
        allStatusBeforeAngenommen.add(FEHLGEROUTET);
    }

    class GridCallback implements IGridTableDialogCallback
    {
        /*
         * (non-Javadoc)
         * 
         * @see de.tif.jacob.screen.dialogs.IGridTableDialogCallback#onSelect(de.tif.jacob.screen.IClientContext,
         *      int, java.util.Properties)
         */
        /*
         * @see de.tif.jacob.screen.dialogs.IGridTableDialogCallback#onSelect(de.tif.jacob.screen.IClientContext,
         *      int, java.util.Properties)
         */
        public void onSelect(IClientContext context, int rowIndex, Properties columns) throws Exception
        {
            fillForm(context, columns.getProperty("ID"), columns.getProperty("Routinginfo"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
     *      de.tif.jacob.screen.IGuiElement)
     */
    public void onAction(IClientContext context, IGuiElement button) throws Exception
    {
        IDataTable categoryTable = context.getDataTable("category");
        if (categoryTable.recordCount() != 1)
        {
            
            alert("Es ist kein Gewerk ausgewählt");
            return;
        }
        
        IDataTable processTable = context.getDataTable("process");
        if (processTable.recordCount() != 1)
        {
            alert("Es ist keine Tätigkeit ausgewählt");
            return;
        }

        IDataTable locationTable = context.getDataTable("location");
        if (locationTable.recordCount() != 1)
        {
            alert("Es muss ein Störungsort angegeben werden");
            return;
        }
        else if (!locationTable.getRecord(0).isNormal())
        {
            alert("Der Störungsort muss gespeichert werden");
            return;
        }
        if (!locationTable.getRecord(0).hasLinkedRecord("faplissite"))
        {
            alert("Der Störungsort muss mindestens eine Werksangabe haben");
            return;
        }
        String categoryKey = categoryTable.getRecord(0).getStringValue("pkey");
        String processKey = processTable.getRecord(0).getStringValue("pkey");
        String contractKey = Routing.getContractKey(locationTable.getRecord(0));

        List aks = Routing.getRoutingAK(context.getDataAccessor(), categoryKey, processKey, contractKey);

        if (aks.size() == 1)
        {
            AuftragsKoordinator ak = (AuftragsKoordinator) aks.get(0);
            fillForm(context, ak.getPkey(), ak.getRoutingInfo());
        }
        else if (aks.size() > 1)
        {
            IGridTableDialog dialog = context.createGridTableDialog(button, new GridCallback());

            // create the header for the selection grid dialog
            //
            String[] header = new String[]
            { "ID", "AK", "Beschreibung", "Routinginfo" };
            dialog.setHeader(header);

            String[][] daten = new String[aks.size()][4];
            for (int j = 0; j < aks.size(); j++)
            {
                AuftragsKoordinator ak = (AuftragsKoordinator) aks.get(j);
                daten[j] = new String[]
                { ak.getPkey(), ak.getName(), ak.getDescription(), ak.getRoutingInfo() };
            }
            dialog.setData(daten);
            dialog.show(1000, 300);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
     *      int, de.tif.jacob.screen.IGuiElement)
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
    {
        if (status == IGuiElement.NEW || status == IGuiElement.UPDATE)
        {
            IDataTableRecord call = context.getSelectedRecord();
            // nur bei nicht angenommene Meldungen ist ein Rouing erlaubt
            if (allStatusBeforeAngenommen.contains(call.getStringValue("callstatus")))
            {
                emitter.setEnable(true);
            }
            else
            {
                emitter.setEnable(false);
            }
        }
        else
        {
            emitter.setEnable(!(status == IGuiElement.SELECTED));
        }
    }

    /**
     * @param context
     * @param pkey
     * @param routinginfo
     * @throws Exception
     */
    private void fillForm(IClientContext context, String pkey, String routinginfo) throws Exception
    {
        // setze den AK-Datensatz
        //
        IDataTable callworkgroupTable = context.getDataTable("callworkgroup");
        callworkgroupTable.clear();
        callworkgroupTable.qbeClear();
        callworkgroupTable.qbeSetValue("pkey", pkey);
        callworkgroupTable.search();

        // Setze die Routinginfo manuell
        //
        ISingleDataGuiElement callRoutinginfo = (ISingleDataGuiElement) context.getGroup().findByName("callRoutinginfo");
        // wenn Routinginfo leer dann Eintag anpassen
        if (callRoutinginfo != null)
        {
            if ("".equals(routinginfo))
            {
                callRoutinginfo.setValue(" keine Routinginfo vorhanden");
            }
            else
            {
                callRoutinginfo.setValue(routinginfo);
            }
        }
    }
}
