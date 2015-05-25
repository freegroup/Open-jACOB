<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="java.util.*" %>

<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%
    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head><body></body></html>");
        return;
    }
    String browserId  = request.getParameter("browser");
    String linkTable  = request.getParameter("linkTable");
    String browserTable  = request.getParameter("browserTable");
    String pkeys[] = request.getParameterValues("pkeys[]");
    int guid = Integer.parseInt(request.getParameter("guid"));

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    Application app = null;
    if(jacobSession!=null)
        app=(Application)jacobSession.getApplication(browserId);

    // no valid application in the session found...cleanup all data
    // and redirect to the login screen
    //
    if(jacobSession==null || app==null)
    {
        UserManagement.logOutUser(request,response);
        out.clearBuffer();
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);

   synchronized(app)
   {
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
        InFormBrowser browser = (InFormBrowser)((Application)context.getApplication()).findChild(guid);

        context.setDomain((IDomain)browser.getForm().getParent());
        context.setForm((HTTPForm)browser.getForm());
        context.setGroup((HTTPGroup)browser.getGroup());

        IDataTableRecord groupRecord = context.getSelectedRecord();
        if(linkTable!=null)
        {
           IDataAccessor acc = context.getDataAccessor();
           IDataTransaction trans = groupRecord.getCurrentTransaction();
           IDataTable browserDataTable = acc.newAccessor().getTable(browserTable);
           IDataTable linkDataTable = acc.newAccessor().getTable(linkTable);

           // Alle Records suchen welche den pkey enthalten
           for(int i=0; i< pkeys.length; i++)
           {
              browserDataTable.qbeSetValue("pkey",pkeys[i]);
           }
           browserDataTable.search();
           for(int i=0; i< browserDataTable.recordCount();i++)
           {
              IDataTableRecord browserRecord = browserDataTable.getRecord(i);
              if(browser.getRecordIndex(context, browserRecord)<0)
              {
                 IDataTableRecord linkRecord = linkDataTable.newRecord(trans);
                 linkRecord.setLinkedRecord(trans, groupRecord);
                 linkRecord.setLinkedRecord(trans, browserRecord);
                 browser.append(context, browserRecord, false);
              }
           }
        }
        else
        {
           IDataAccessor acc = context.getDataAccessor();
           IDataTransaction trans = groupRecord.getCurrentTransaction();
           IDataTable browserDataTable = acc.newAccessor().getTable(browserTable);

           // Alle Records suchen welche den pkey enthalten
           for(int i=0; i< pkeys.length; i++)
           {
              browserDataTable.qbeSetValue("pkey",pkeys[i]);
           }
           browserDataTable.search();
           for(int i=0; i< browserDataTable.recordCount();i++)
           {
              IDataTableRecord browserRecord = browserDataTable.getRecord(i);
              if(browser.getRecordIndex(context, browserRecord)<0)
              {
                 browserRecord.setLinkedRecord(trans, groupRecord);
                 browser.append(context, browserRecord, false);
              }
           }
        }
   }
%>
