/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Apr 02 14:49:46 CEST 2009
 */
package jacob.event.ui;

import jacob.browser._followupBrowser;
import jacob.common.AppLogger;
import jacob.model.Followup;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @author achim
 */
public class FollowupDomain extends IDomainEventHandler
{
    static public final transient String RCS_ID = "$Id: FollowupDomain.java,v 1.9 2009/11/23 11:33:44 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.9 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    class DomainNavigationPanel implements INavigationPanel
    {

        public INavigationEntry[] getNavigationEntries(IClientContext context, IDomain domain)
        {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

            // die letzten Events aus der Datenbank lesen
            //
            try
            {
                IDataAccessor acc = context.getDataAccessor().newAccessor();
                IDataBrowser fuBrowser = acc.getBrowser(_followupBrowser.NAME);
                IDataTable fuTable = acc.getTable(Followup.NAME);
                fuTable.qbeSetValue(Followup.agent_followup_key, context.getUser().getKey());
                fuTable.qbeSetValue(Followup.status, Followup.status_ENUM._in_Progress);
                fuTable.qbeSetValue(Followup.status, Followup.status_ENUM._open);
                fuBrowser.setMaxRecords(5);
                fuBrowser.search(IRelationSet.LOCAL_NAME);
                int recordCount = fuBrowser.recordCount();
                INavigationEntry[] entries = new INavigationEntry[recordCount];
                for(int i=0;i<recordCount;i++)
                {
                    IDataBrowserRecord eventRecord = fuBrowser.getRecord(i);
                    Date dueDate = eventRecord.getDateValue(_followupBrowser.browserDuedate);
                    String status = eventRecord.getSaveStringValue(_followupBrowser.browserFu_status);
                    String label = format.format(dueDate);
                    if(status.equals(Followup.status_ENUM._in_Progress))
                        label = label +" *";

                    String pkey  = eventRecord.getSaveStringValue(_followupBrowser.browserPkey);
                    entries[i] = new INavigationEntry(domain,"unused",label,"showx",pkey);
                }
                long totalCount = fuTable.count();
                if(totalCount != recordCount)
                    domain.setLabel("FollowUp ("+recordCount+"/"+totalCount+")");
                else
                    domain.setLabel("FollowUp");
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
        // BUG: Es muss die Form ein hin und her geschaltet werden damit man den richtigen DatenAccessor hat
        //      Sch"�$%
        //
        context.setCurrentForm("followupDomain", "followupForm");
        IDataTable followupTable = context.getDataTable(Followup.NAME);
        if (followupTable.getSelectedRecord()!=null&&followupTable.getSelectedRecord().getCurrentTransaction()!=null)
        {
            followupTable.getSelectedRecord().getCurrentTransaction().close();
        }
        followupTable.qbeClear();
        followupTable.qbeSetKeyValue(Followup.pkey, navigationData);
        followupTable.search();
        if(followupTable.getSelectedRecord()!=null)
            context.getDataAccessor().propagateRecord(followupTable.getSelectedRecord(),Filldirection.BOTH);
    }


    @Override
    public void onHide(IClientContext context, IDomain domain) throws Exception
    {
        context.getApplication().setToolbarVisible(true);
    }


    @Override
    public void onShow(IClientContext context, IDomain domain) throws Exception
    {
        context.getApplication().setToolbarVisible(false);
    }
}
