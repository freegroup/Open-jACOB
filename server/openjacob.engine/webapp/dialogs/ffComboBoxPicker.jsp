<%@page import="de.tif.jacob.util.StringUtil"%>
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
             if(element instanceof FFComboBox)
             {
                 FFComboBox combo = (FFComboBox)element;
                 IDataBrowser browser = combo.search(context);
                 // Alle Columns von dem Browser holen
                 //
                 out.println("<table style=\"position:relative;z-index:1000\" width='100%' class='combobox_pane'  cellspacing=\"0\" cellpadding=\"0\">");
                 if(!combo.isRequired() || combo.getParent().getDataStatus()==IGuiElement.SEARCH)
                 {
                    out.print("<tr onclick='FireEvent("+guid+",\"deselect\")' >");
                    out.print("<td onmouseover=\"this.className='combobox_option_over'\" onmouseout=\"this.className='combobox_option'\"' class='combobox_option'>&nbsp;</td>");
                    out.print("</tr>\n");
                 }
                 for(int i=0; i<browser.recordCount();i++)
                 {
                    IDataBrowserRecord record = browser.getRecord(i);
                    String value =  StringUtil.htmlEncode(record.getStringValue(combo.getDisplayFieldName(), context.getLocale()));
                    out.print("<tr onclick='FireEventData("+guid+",\"select\",\""+i+"\")' >");
                    out.print("<td onmouseover=\"this.className='combobox_option_over'\" onmouseout=\"this.className='combobox_option'\"' class='combobox_option'>"+value+"</td>");
                    out.print("</tr>\n");
                 }
                 out.println("</table>\n");
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
