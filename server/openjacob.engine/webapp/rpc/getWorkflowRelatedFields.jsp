<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.i18n.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.core.definition.guielements.*" %>
<%@ page import="de.tif.jacob.core.definition.impl.jad.castor.*" %>
<%@ page import="de.tif.jacob.core.definition.impl.jad.castor.types.*" %>
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
response.setContentType("application/json");
try
{
    String browserId = request.getParameter("browser");
    String guid = request.getParameter("guid");

    if(!UserManagement.isLoggedInUser(request,response))
        return;

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        Application app =(Application)jacobSession.getApplication(browserId);
        DeployEntry entry = DeployManager.getDeployEntry(app.getApplicationDefinition());
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        WorkflowEditorDialog dialog = (WorkflowEditorDialog)jacobSession.getDialog(guid);

        Context.setCurrent(context);
        out.println("[");
        try
        {
           List<ISingleDataGuiElement> children = dialog.getRelevantGuiElements(context);
    
           boolean first = true;
           for(ISingleDataGuiElement child : children)
           {
             if(first)
               out.println("{name:\""+child.getName()+"\"}");
             else
               out.println(",{name:\""+child.getName()+"\"}");
        
             first = false;
          }
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
        out.println("]");
    }
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
