<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.report.*" %>
<%@ page import="de.tif.jacob.report.impl.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%@ include file="../guid.inc" %> 
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
try
{
    String browserId = request.getParameter("browser");
    String column = request.getParameter("column");

    if(!UserManagement.isLoggedInUser(request,response))
        return;

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        Application app =(Application)jacobSession.getApplication(browserId);
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
        IReport report = (IReport)context.getProperty(REPORT_GUID);
        IReport.Column[] columns = report.getColumns();
        for(int i=0;i<columns.length;i++)
        {
          IReport.Column c = columns[i];
          String key = c.table+"."+c.field;
          if(key.equals(column))
          {
            if(c.order==SortOrder.NONE)
            {
               c.setSortOrder(SortOrder.ASCENDING);
            }
            else if(c.order==SortOrder.ASCENDING)
            {
               c.setSortOrder(SortOrder.DESCENDING);
            }
            else
            {
               c.setSortOrder(SortOrder.NONE);
            }
            out.println(c.order);
            break;
          }
          
        }
    }
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
