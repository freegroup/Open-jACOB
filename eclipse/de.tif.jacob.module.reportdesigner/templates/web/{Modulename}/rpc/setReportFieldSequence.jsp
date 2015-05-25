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
    String reportFields[] = request.getParameterValues("fields");

    if(!UserManagement.isLoggedInUser(request,response))
        return;

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        Application app =(Application)jacobSession.getApplication(browserId);
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
        Report report = (Report)context.getProperty(REPORT_GUID);
       
        IReport.Column[] columns = report.getColumns();

        // die Anzahl der übergebenen Columns stimmt nicht mit den enthaltenen Columns übeine
        // Fehler.
        if(columns.lenght != reportFields.lenght)
            return;

        report.removeAllColumns(); 

        for(int f=0; f<reportFields.length;f++)
        {
          String field = reportFields[f];
          for(int i=0;i<columns.length;i++)
          {
            IReport.Column c = columns[i];
            String key = c.table+"."+c.field;
            if(key.equals(field))
            {
              report.addColumn(c);
              break;
            }
          }
        }
    }
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
