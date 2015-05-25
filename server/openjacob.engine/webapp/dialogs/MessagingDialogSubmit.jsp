<%--
     This file is part of Open-jACOB
     Copyright (C) 2005-2006 Tarragon GmbH
  
     This program is free software; you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation; version 2 of the License.
  
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
  
     You should have received a copy of the GNU General Public License     
     along with this program; if not, write to the Free Software
     Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
     USA
--%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="j" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="java.util.*" %>

<% 
try
{
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String browserId    = request.getParameter("browser");
    String message      = request.getParameter("message");
    String messageType  = request.getParameter("messageType");
    
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
        return;

    Application   app          = (Application)jacobSession.getApplication(browserId);
    IUser         user         = jacobSession.getUser();
    if(jacobSession!=null)
    {
        ClientContext context = new ClientContext(jacobSession.getUser(),app,browserId);
        Context.setCurrent(context);
        for(int i=0;i<100;i++)
        {
            String recipien     = request.getParameter("recipien_"+i);
            if(recipien==null)
                break;
            try
            {
                if(Property.YAN_PROTOCOL_EMAIL.getName().equals(messageType))
                    de.tif.jacob.messaging.Message.sendEMail(recipien,message);
                else if(Property.YAN_PROTOCOL_SMS.getName().equals(messageType))
                    de.tif.jacob.messaging.Message.sendSMS(recipien,message);
                else if(Property.YAN_PROTOCOL_FAX.getName().equals(messageType))
                    de.tif.jacob.messaging.Message.sendFAX(recipien,message);
                else if(Property.YAN_PROTOCOL_ALERT.getName().equals(messageType))
                    de.tif.jacob.messaging.Message.sendAlert(recipien,message);
            }
            catch(Exception exc)
            {
                exc.printStackTrace();
            }
       }
    }
    String themeId = jacobSession.getCurrentTheme();
    Theme theme = ThemeManager.getTheme(themeId);
%>
<HTML>
<HEAD>
     <title><j:dialogTitle id="DIALOG_TITLE_SEND_MESSAGE"/></title>
    <META Http-Equiv="Cache-Control" Content="no-cache">
    <META Http-Equiv="Pragma" Content="no-cache">
    <META Http-Equiv="Expires" Content="0">
    
    <script>var userTheme = '<%=app.getTheme()%>';</script>
    <script id="common_js"  type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
    <link   type="text/css" rel="stylesheet" href="../themes/common.css" />
    <link  type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />

</HEAD>
<body class="dialog" >
    
    <table  cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">
    <tr id="fields" name="fields" ></tr>
    <tr>
        <td  height="100%"  align=center valign="top">
            <div width="100%" height="100%"  class="dialog_content">
            <j:i18n id="MSG_SUCCESSFULLY_SEND"/>
            </div>
        </td>
    </tr>
    <tr>
        <td  align="right" valign="bottom">
            <div class="dialog_buttonbar">
                <input type=button  value="<j:i18n id="BUTTON_COMMON_CLOSE"/>"  onClick="FireEvent(window,'close');" class="button_normal" >
            </div>
        </td>
    </tr>
    </table>
<body>
</html>
<%
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>