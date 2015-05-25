package jacob.common;

import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Followup;
import jacob.model.Request;
import jacob.relationset.FollowupRelationset;
import jacob.relationset.RequestRelationset;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;

public class FormManager
{
    public final static String CALLHANDLING_DOMAIN = "<CallHandling>";
    public final static String CALLHANDLING_FORM = "CALLHANDLING";

    private final static String DETAIL_DOMAIN = "<emailDetailDomain>";
    private final static String DETAIL_FORM = "emailDetailForm";

    private final static String FOLLOWUP_DOMAIN = "followupDomain";
    private final static String FOLLOWUP_FORM = "followupForm";

    public static void showRequest(IClientContext context, IDataTableRecord requestRecord) throws Exception
    {
        context.setCurrentForm(CALLHANDLING_DOMAIN, CALLHANDLING_FORM);
        if (requestRecord != null)
        {
            context.getDataAccessor().propagateRecord(requestRecord, RequestRelationset.NAME, Filldirection.BOTH);
        }
        TabManagerRequest.setActive(context, Request.NAME);
    }

    public static void showFollowUp(IClientContext context, IDataTableRecord followupRecord) throws Exception
    {
        context.setCurrentForm(FOLLOWUP_DOMAIN, FOLLOWUP_FORM);

        context.getDataAccessor().clear();

        // Transform the "request_followup" record to "followup" Table Alias
        //
        if (followupRecord != null)
        {
            String pkey = followupRecord.getSaveStringValue(Followup.pkey);
            IDataTable followUpTable = context.getDataTable(Followup.NAME);
            followUpTable.qbeSetKeyValue(Followup.pkey, pkey);
            followUpTable.search();
            context.getDataAccessor().propagateRecord(followUpTable.getSelectedRecord(), FollowupRelationset.NAME, Filldirection.BOTH);
        }
    }

    public static void showEmailDetail(IClientContext context) throws Exception
    {
        context.setCurrentForm(DETAIL_DOMAIN, DETAIL_FORM);
    }

    public static void showEmailOutbound(IClientContext context) throws Exception
    {
        context.setCurrentForm("<outboundDomain>", "sendEmailForm");
    }

    public static IForm getCallhandling(IClientContext context)
    {
        return (IForm)context.getApplication().findByName(CALLHANDLING_FORM);
    }

    public static IForm getHistory(IClientContext context)
    {
        return (IForm)context.getApplication().findByName("historyForm");
    }
}
