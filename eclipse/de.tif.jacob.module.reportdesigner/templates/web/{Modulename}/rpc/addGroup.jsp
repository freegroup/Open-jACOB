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
    String tableAlias = request.getParameter("tableAlias");
    String field = request.getParameter("field");

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

        Set grouped_by_fields = new HashSet();
        IReport.Column[] columns = report.getColumns();

        CastorLayoutPart part = layout.getPart();

        CastorGroup group = part.getGroup();
        while(group!=null)
        {
           if(group.getPart()==null)
              break;
          String group_key = group.getTableAlias()+"."+group.getField();
          grouped_by_fields.add(group_key);
          group = group.getPart().getGroup();
        }        

        IReport.Column c = report.getColumns()[0];

        for(int i=0;i<columns.length;i++)
        {
          IReport.Column test_c = columns[i];
          String key = test_c.table+"."+test_c.field;
          if(!grouped_by_fields.contains(key))
          {
              c= test_c;
              break;
          }
        }

        CastorLayoutPart newPart = new CastorLayoutPart();
        CastorGroup newGroup = new CastorGroup();
 
        newGroup.setTableAlias(c.table);
        newGroup.setField(c.field);

        newPart.setGroup(newGroup);

        newGroup.setPart(part); 

        layout.setPart(newPart);
        out.println(c.table+"."+c.field);
      }
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
