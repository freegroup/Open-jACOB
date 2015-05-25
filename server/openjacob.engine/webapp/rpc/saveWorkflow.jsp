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
<%@ page import="org.apache.commons.lang.StringUtils" %>

<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
response.setContentType("application/xml");
try
{
    String  browserId  = request.getParameter("browser");
    String  xml       = request.getParameter("content");
    String  guid       = request.getParameter("guid");
    HTTPClientSession jacobSession = HTTPClientSession.get(request);

    String mimeType = "text/xml";
    response.setContentType(mimeType);
    
    Application app = (Application)jacobSession.getApplication(browserId);
    WorkflowEditorDialog dialog = (WorkflowEditorDialog)jacobSession.getDialog(guid);

    dialog.setXml(xml);
    
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
