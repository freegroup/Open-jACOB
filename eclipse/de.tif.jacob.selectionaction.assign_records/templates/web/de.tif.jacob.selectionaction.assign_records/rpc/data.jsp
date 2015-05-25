<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.core.definition.fieldtypes.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="java.util.*" %>

<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

{ "aaData": [

<%
    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head><body></body></html>");
        return;
    }
    String browserId  = request.getParameter("browser");
    String dataBrowser= request.getParameter("dataBrowser");
    String qbe= request.getParameter("sSearch");
    String recordCountString = request.getParameter("iDisplayLength");
    int maxRecordCount = 100;
    try
   {
     maxRecordCount = Integer.parseInt(recordCountString);
   }
   catch(Exception e)
   {
   }

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
  
        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataBrowser browser = acc.getBrowser(dataBrowser);
        IDataTable table = acc.getTable(browser.getTableAlias().getName());
        if(qbe!=null && qbe.length()>0)
        {
          if(qbe.endsWith("|"))
            qbe = qbe.substring(0,qbe.length()-1);

          if(qbe.startsWith("|"))
            qbe = qbe.substring(1);

          if(qbe.length()>0)
          {
            List tableFields = table.getTableAlias().getTableDefinition().getTableFields();
            for (Iterator iterator = tableFields.iterator(); iterator.hasNext();)
            {
              ITableField field = (ITableField) iterator.next();
              if(field.getType() instanceof TextFieldType && !(field.getType() instanceof LongTextFieldType))
              {
                table.qbeSetValue(field,"|"+qbe);
              }
            }
          }
        }
        browser.setMaxRecords(maxRecordCount);
        browser.search(IRelationSet.LOCAL_NAME);
        
        List fields = browser.getBrowserDefinition().getBrowserFields();
        for(int recordCount=0; recordCount<browser.recordCount();recordCount++)
        {
          if(recordCount==0)
          	out.print("[");
          else
          	out.print(",[");
          IDataBrowserRecord record = browser.getRecord(recordCount);
          for(int i=0; i<fields.size();i++)
          {
            String value = record.getSaveStringValue(i);
            if(i==0)
              out.print("\""+value+"\"");
            else
              out.print(", \""+value+"\"");
          }
          out.println("]");
        }
   }
%>

] }