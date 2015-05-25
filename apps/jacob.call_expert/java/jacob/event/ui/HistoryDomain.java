/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Fri Nov 28 11:34:27 CET 2008
 */
package jacob.event.ui;

import jacob.common.AppLogger;
import jacob.model.Event;
import jacob.relationset.RequestRelationset;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.event.IDomainEventHandler;

/**
 *
 * @author andherz
 */
public class HistoryDomain extends IDomainEventHandler
{
    static public final transient String RCS_ID = "$Id: HistoryDomain.java,v 1.4 2009/11/23 11:33:44 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

    /**
     * Use this logger to write messages and NOT the
     * <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    class DomainNavigationPanel implements INavigationPanel
    {

        public INavigationEntry[] getNavigationEntries(IClientContext context, IDomain domain)
        {
            // die letzten Events aus der Datenbank lesen
            //
            try
            {
                IDataAccessor acc = context.getDataAccessor().newAccessor();
                IDataBrowser eventBrowser = acc.getBrowser("_eventHistoryBrowser");
                IDataTable eventTable = acc.getTable(Event.NAME);
                eventTable.qbeSetValue(Event.agent_key, context.getUser().getKey());
                eventBrowser.setMaxRecords(5);
                eventBrowser.search(IRelationSet.LOCAL_NAME);
                int recordCount = eventBrowser.recordCount();
                INavigationEntry[] entries = new INavigationEntry[recordCount];
                for(int i=0;i<recordCount;i++)
                {
                    IDataBrowserRecord eventRecord = eventBrowser.getRecord(i);
                    String label = "Event:"+eventRecord.getSaveStringValue("browserPkey");
                    String pkey  = eventRecord.getSaveStringValue("browserPkey");
                    entries[i] = new INavigationEntry(domain,"unused",label,"showx",pkey);
                }
                return entries;
            }
            catch (Exception e)
            {
            }
            return new INavigationEntry[0];
        }
    }

    public INavigationPanel getNavigationPanel(IClientContext context, IDomain domain) throws Exception
    {
        return new DomainNavigationPanel();
    }


    public void onNavigation(IClientContext context, IDomain domain, String navigationId, String navigationData) throws Exception
    {
        context.setCurrentForm("historyDomain", "historyForm");
        IDataTable eventTable = context.getDataTable(Event.NAME);
        eventTable.qbeClear();
        eventTable.qbeSetKeyValue(Event.pkey, navigationData);
        eventTable.search();
        if(eventTable.getSelectedRecord()!=null)
            context.getDataAccessor().propagateRecord(eventTable.getSelectedRecord(),RequestRelationset.NAME, Filldirection.BOTH);
    }


    /**
     * This event method will be called, if the user switches to another form or
     * domain.
     *
     * @param context
     *          The current client context
     * @param group
     *          The hidden group
     */
    public void onShow(IClientContext context, IDomain domain) throws Exception
    {
        // do nothing by default
    }
}
