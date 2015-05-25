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
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
try
{
    int guid = Integer.parseInt(request.getParameter("guid"));
    String browserId = request.getParameter("browser");
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
                 // Alle Columns von dem Browser holen
                 //
                 List columns = browser.getColumns();
                 Iterator iter = columns.iterator();
                 out.println("<table cellspacing=\"0\" cellpadding=\"0\" class=\"Multiselection_BodyTable\">");
                 int counter=0;
                 int visibleCounter=0;
                 while(iter.hasNext())
                 {
                    IBrowserField field = (IBrowserField)iter.next();
                    // Nur wenn die Spalte sichtbar und konfigurierbar ist wird ein eintrag in dem Menu gemacht
                    //
                    if(browser.isColumnVisible(counter)==false && browser.isColumnConfigureable(counter)==false)
                    {
                       // show nothing
                    }
                    else
                    {
                       if(browser.isColumnConfigureable(counter))
                       {
                          if(browser.isColumnVisible(counter))
                          {
                             out.print("<tr onclick=\"jACOBTable_removeColumn('"+guid+"','"+BrowserAction.ACTION_COLUMN_PICKER.getId()+"','"+counter+"',"+visibleCounter+")\" >");
                             out.print("<td style=\"background-image: url(./themes/"+app.getTheme()+"/images/checked.png);\" class=\"Multiselection_ItemIcon\"/></td>");
                             out.print("<td class=\"Multiselection_ItemTitle\" style=\"white-space: nowrap;\">"+I18N.localizeLabel(field.getLabel(),context)+"</td>");
                             out.print("</tr>\n");
                             visibleCounter++;
                          }
                          else
                          {
                             out.print("<tr onclick=\"FireEventData('"+guid+"','"+BrowserAction.ACTION_COLUMN_PICKER.getId()+"','"+counter+"')\" >");
                             out.print("<td style=\"background-image: url(./themes/"+app.getTheme()+"/images/unchecked.png);\" class=\"Multiselection_ItemIcon\"/></td>");
                             out.print("<td class=\"Multiselection_ItemTitle\" style=\"white-space: nowrap;\">"+I18N.localizeLabel(field.getLabel(),context)+"</td>");
                             out.print("</tr>\n");
                          }
                       }
                       else
                       {
                          out.print("<tr>");
                          out.print("<td style=\"background-image: url(./themes/"+app.getTheme()+"/images/checked_disabled.png);\" class=\"Multiselection_ItemIcon\"/></td>");
                          out.print("<td class=\"Multiselection_ItemTitle\" style=\"white-space: nowrap;\">"+I18N.localizeLabel(field.getLabel(),context)+"</td>");
                          out.print("</tr>\n");
                          visibleCounter++;
                       }
                    }
                    counter++;
                 }
                 out.println("</table>\n");
             }
             else
             {
               // not found
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
