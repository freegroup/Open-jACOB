<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
try
{
    String guid = request.getParameter("guid");
    if(!UserManagement.isLoggedInUser(request,response))
        return;

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        try
        {
            jacobSession.sendKeepAlive(guid);
            Object obj =jacobSession.getDialog(guid);
            if(obj instanceof UploadDialog)
            {
                UploadDialog dialog=(UploadDialog)obj;
                if(dialog.getExpectedFileSize()>0)
                {
                    int percent = (int)(dialog.getCurrentFileSize()*(100.0f/dialog.getExpectedFileSize()));
                    %>
                    getObj('progressImage').width="<%=350*Math.max(0.01,percent/100f)%>";
                    <%
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
