<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.i18n.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.core.definition.impl.jad.*" %>
<%@ page import="de.tif.jacob.core.definition.impl.jad.castor.*" %>
<%@ page import="de.tif.jacob.core.definition.guielements.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
try
{
    String browserId = request.getParameter("browser");
    int guid = Integer.parseInt(request.getParameter("guid"));
    String event = request.getParameter("event");
    String data = request.getParameter("data");

    if(!UserManagement.isLoggedInUser(request,response))
        return;

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        Application app =(Application)jacobSession.getApplication(browserId);
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
        try
        {
           app.processEvent(context, guid,event,data);
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
