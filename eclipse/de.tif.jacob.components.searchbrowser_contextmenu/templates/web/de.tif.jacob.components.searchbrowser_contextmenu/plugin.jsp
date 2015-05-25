<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.i18n.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="java.lang.reflect.*" %>
<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
try
{
    String browserId = request.getParameter("browser");
    int row = Integer.parseInt(request.getParameter("row"));
    int column = Integer.parseInt(request.getParameter("column"));
    int guid = Integer.parseInt(request.getParameter("componentId"));
    int hook = Integer.parseInt(request.getParameter("hookId"));

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
                 List columns = browser.getColumns();
                 Iterator iter = columns.iterator();
                 int counter=0;
                 while(iter.hasNext())
                 {
                    IBrowserField field = (IBrowserField)iter.next();
                    if(browser.isColumnVisible(counter))
                    {
                       if(column==0)
                       {
                         // Das angeklickte Feld wurde gefunden. Zugehörigen Record holen und 
                         // den Hook aufrufen
                         IDataBrowserRecord record =browser.getDataRecord(row);
                         PluginComponent emitter=(PluginComponent)app.findChild(hook);
                         PluginComponentImpl impl = emitter.getImplementation();
                         Object handler = (Object) emitter.getEventHandler(context);

                         Method getMenuEntries = handler.getClass().getMethod("getMenuEntries",new Class[]{SessionContext.class,IBrowser.class, IDataBrowserRecord.class, IBrowserField.class});
                         List  entries = (List)getMenuEntries.invoke(handler,new Object[]{context,browser, record, field});
                         Iterator entryIter = entries.iterator();
                         out.println("[");
                         while(entryIter.hasNext())
                         {
                            Object entry = entryIter.next();
                            Class c = entry.getClass();
                            Field labelField= c.getField("label");
                            Field commandField= c.getField("command");
                            String label = (String) labelField.get(entry);
                            String command = (String) commandField.get(entry);
                            out.println("{command:'"+command+"', label:'"+label+"', row:"+row+", column:"+field.getFieldIndex()+"}");
                            if(entryIter.hasNext())
                              out.println(",");
                         }
                         out.println("]");
                       }
                       column--;
                    }
                    counter++;
                 }
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