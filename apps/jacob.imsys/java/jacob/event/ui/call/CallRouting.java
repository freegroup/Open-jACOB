package jacob.event.ui.call;

import jacob.common.data.AuftragsKoordinator;
import jacob.common.data.DataUtils;
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
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * 
 * @author mike
 * 
 */
public class CallRouting extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CallRouting.java,v 1.3 2005/06/30 17:13:51 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.3 $";

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

    class OkCancelDialogCallback implements IOkCancelDialogCallback
    {
        private IGuiElement button;

        private String categoryKey;

        private String processKey;

        private String contractKey;

        OkCancelDialogCallback(String categoryKey, String processKey, String contractKey, IGuiElement emmitter)
        {
            this.button = emmitter;
            this.categoryKey = categoryKey;
            this.contractKey = contractKey;
            this.processKey = contractKey;
        }

        public void onOk(IClientContext context) throws Exception
        {
            List aks = Routing.getRoutingAK(context.getDataAccessor(), categoryKey, processKey, contractKey);

            setAK(context, aks, button);
        }

        public void onCancel(IClientContext context) throws Exception
        {
        }
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

    /**
     * 
     * @param context
     * @param aks
     * @param button
     * @throws Exception
     */
    public void setAK(IClientContext context, List aks, IGuiElement button) throws Exception
    {
        if (aks.size() == 0)
        {
            alert("Kein Auftragskoordinator gefunden.\nEs besteht kein Vertragsvereinbarung für diese Tätigkeit und Gewerk");
            return;
        }

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
            dialog.show(300, 100);
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

        String categoryKey = categoryTable.getRecord(0).getStringValue("pkey");
        String processKey = processTable.getRecord(0).getStringValue("pkey");
        String contractKey = Routing.getContractKey(context);

        List aks = Routing.getRoutingAK(context.getDataAccessor(), categoryKey, processKey, contractKey);


        if (aks.size() == 0)
        {   //Wenn mit diesem Vertrag nichts gefunden wird, dann mit Standardvertrag versuchen?
            String defaultContract = DataUtils.getAppprofileValue(context, "contract_key");
            if (!(defaultContract.equals(contractKey)))
            {
                IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Die Tätigkeit wird nicht vom Vertrag der gewählten Kostenstelle gedeckt. \n"
                        + "Soll mit dem Standardvertrag gesucht werden?", new OkCancelDialogCallback(categoryKey, processKey, defaultContract, button));

                okCancelDialog.show();
                return;
            }
        }
 
        setAK(context, aks, button);
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
