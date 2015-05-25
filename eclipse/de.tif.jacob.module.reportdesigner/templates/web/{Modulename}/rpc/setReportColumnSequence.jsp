<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.report.*" %>
<%@ page import="de.tif.jacob.report.impl.*" %>
<%@ page import="de.tif.jacob.report.impl.castor.*" %>
<%@ page import="de.tif.jacob.report.impl.castor.types.*" %>
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
    String reportColumns[] = request.getParameterValues("columns");

    if(!UserManagement.isLoggedInUser(request,response))
        return;

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        de.tif.jacob.screen.impl.html.Application app =(de.tif.jacob.screen.impl.html.Application)jacobSession.getApplication(browserId);
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
        Report report = (Report)context.getProperty(REPORT_GUID);
        CastorLayout layout = report.getLayout_01();

        CastorLayoutPart part = layout.getPart();
        while(true)
        {
          if(part.getColumns()!=null)
             break;
           part = part.getGroup().getPart();
        }   
        CastorLayoutColumns layoutColumns = part.getColumns();
        CastorLayoutColumn[] columns = layoutColumns.getColumn();

        // die Anzahl der übergebenen Columns stimmt nicht mit den enthaltenen Columns übeine
        // Fehler.
        if(columns.lenght != reportColumns.lenght)
            return;

        layoutColumns.removeAllColumn();
    
        for(int f=0; f<reportColumns.length;f++)
        {
          String column = reportColumns[f];
          for(int i=0;i<columns.length;i++)
          {
            CastorLayoutColumn c = columns[i];
            String key = c.getTableAlias()+"."+c.getField();
            if(key.equals(column))
            {
              layoutColumns.addColumn(c);
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
