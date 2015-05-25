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

    int group = Integer.parseInt(request.getParameter("group"));
    int ident = Integer.parseInt(request.getParameter("ident"));

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
        while(group>0)
        {
           part = part.getGroup().getPart();
           group--;
        }        
        part.setIdent(ident);
      }
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
