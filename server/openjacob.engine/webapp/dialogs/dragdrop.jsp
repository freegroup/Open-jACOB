<%@page import="de.tif.jacob.util.StringUtil"%>
<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.i18n.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.event.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
response.setContentType("text/x-javascript");

try
{
    int guid = Integer.parseInt(request.getParameter("guid"));
    String browserId = request.getParameter("browser");
    String callback = request.getParameter("callback");
    String dragIndex = request.getParameter("dragIndex");
    String dropIndex = request.getParameter("dropIndex");

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
                 Browser httpBrowser= (Browser)element;
                 IDataBrowser browser = httpBrowser.getData();
                 IDragDropListener listener = (IDragDropListener)httpBrowser.getEventHandler(context);
                 if("validateDrop".equals(callback))
                 {
                    boolean canDrop = true;
                    int drag = 0; try{drag=Integer.parseInt(dragIndex);}catch(Exception exc){};
                    int drop = -1;try{drop=Integer.parseInt(dropIndex);}catch(Exception exc){};
                    IDataBrowserRecord dragBrowserRecord = httpBrowser.getDataRecord(drag);
                    IDataBrowserRecord dropBrowserRecord = null;
                    if(drop>=0 && drop<browser.recordCount())
                    {
                       dropBrowserRecord = httpBrowser.getDataRecord(drop);
                       canDrop = drag!=drop;
                       if(canDrop)
                       {
                          IDataBrowserRecord parent = httpBrowser.getParent( dropBrowserRecord);
                          while(parent!=null)
                          {
                            if(parent.getPrimaryKeyValue().equals(dragBrowserRecord.getPrimaryKeyValue()))
                            {
                               canDrop = false;
                               break;
                            }
                            parent = httpBrowser.getParent(parent);
                          }
                       }
                       canDrop = canDrop && listener.validateDrop(context,httpBrowser,dragBrowserRecord,dropBrowserRecord);
                     }
                     else
                     {
                       canDrop =listener.validateDrop(context,httpBrowser,dragBrowserRecord,dropBrowserRecord);
                     }
                     out.println("updateDropTableRow("+canDrop+",'"+listener.getFeedback(context,httpBrowser,canDrop,dragBrowserRecord, dropBrowserRecord).getPath(true)+"');");
                 }
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
