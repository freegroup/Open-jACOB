<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.i18n.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<% 
response.reset();
response.setContentType("application/json");           
response.setHeader("Cache-Control", "no-cache");
try
{
    int guid = Integer.parseInt(request.getParameter("guid"));
    String browserId = request.getParameter("browser");
    String row = request.getParameter("row"); // optional
    boolean check = new Boolean(request.getParameter("check")).booleanValue();
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
             IGuiElement element=app.findChild(guid);
             if(element instanceof Browser)
             {
                 Browser browser = (Browser)element;
                 if(row!=null)
                 {
                   if(check)
                      browser.checkRow(Integer.parseInt(row));
                   else
                      browser.uncheckRow(Integer.parseInt(row));
                 }
                 else
                 {
                   int recordCount = browser.getData().recordCount();
                   if(check)
                      for(int i=0;i<recordCount;i++)browser.checkRow(i);
                   else
                      for(int i=0;i<recordCount;i++)browser.uncheckRow(i);
                 }
                 
                 // überprüfen welche Action jetzt noch activ sind
                 //
                 StringBuffer json = new StringBuffer("");
                 json.append("[\n");
                 Collection<ISelectionAction> actions = browser.getSelectionActions();
                 boolean first = true;
                 for(ISelectionAction action:actions)
                 {
                    boolean visible = action.isVisible(context, browser);
                    if(!first)
                      json.append(",");
                    json.append("{\n");
                    json.append("\t\"id\":\""+action.getId()+"\",\n");
                    json.append("\t\"visible\":"+visible+"\n");
                    json.append("}\n");
                    first=false;
                 }
                 json.append("]");

                 out.println(json.toString());
             }
             else
             {
//               System.out.println("not found....");
             }
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