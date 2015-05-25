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
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.messaging.alert.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="de.tif.jacob.scheduler.*" %>
<%@ page import="de.tif.jacob.screen.dialogs.form.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<% 
try
{
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }

    TaskContextUser context = new TaskContextUser(jacobSession);
    Context.setCurrent(context);
    boolean sended = "out".equals(StringUtil.toSaveString(request.getParameter("direction")));
    String receivedString="";
    String sendedChecked="";
    String suspendChecked=AlertManager.doSuspend(context)?"checked":"";
    String browserId = request.getParameter("browser");
    
    if(sended)
        sendedChecked="checked";
    else
        receivedString="checked";
    String themeId = jacobSession.getCurrentTheme();
    Theme theme = ThemeManager.getTheme(themeId);
%>
<HTML>
    <HEAD>
        <title><j:dialogTitle id="DIALOG_TITLE_ALERT"/></title>
        <META Http-Equiv="Cache-Control" Content="no-cache">
        <META Http-Equiv="Pragma" Content="no-cache">
        <META Http-Equiv="Expires" Content="0">
    
        <script>var userTheme = '<%=jacobSession.getCurrentTheme()%>';</script>
    
        <!--REGEXP_START_REMOVE-->
        <script type="text/javascript" src="../javascript/debug.js" ></script>
        <script type="text/javascript" src="../javascript/popupWindow.js" ></script>
        <!--REGEXP_END_REMOVE-->
        <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
        <link   type="text/css"        rel="stylesheet" href="../themes/common.css" />
        <link  type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />
        
        <%-- 
          Client / Server Notification
        --%>
       <script id="serverPush"        type="text/javascript" src="" ></script>
       <SCRIPT type="text/javascript" >
        window.setInterval("keepAlive()",<%=de.tif.jacob.core.Property.KEEPALIVEINTERVAL_DIALOG.getIntValue()*1000%>);
       </SCRIPT>

        <script>
        var doClose = 1;
        function keepAlive()
        {
            loadRemotePage('serverPush.jsp?browser=<%=browserId%>&guid=<%=jacobSession.ALERT_KEY%>');
            loadRemotePage('AlertDialogFunctions/checkRefresh.jsp?browser=<%=browserId%>');
        }
        
        function doSuspend(value)
        {
            loadRemotePage("AlertDialogFunctions/suspend.jsp?browser=<%=browserId%>&suspend="+value);
        }
        
        function onClose()
        {
            if (doClose<1)
               return;
               
            loadRemotePage('AlertDialogFunctions/onClose.jsp?browser=<%=browserId%>');
            var gap=500;
            var then,now; then=new Date().getTime();
            now=then;
            while((now-then)<gap)
            {now=new Date().getTime();}
        }
        </script>
    </HEAD>
    <body class="dialog" onUnload="onClose();return false;" >
    <table  style="table-layout:fixed;width:100%;height:100%" cellspacing="0" cellpadding="0" border="0">
    <tr height="310">
        <td valign="top">
            <span  class="caption_normal_search" style="font-size:19pt;font-weight:bold;"><j:i18n id="LABEL_ALERT_MESSAGES"/></span><br>
            <DIV STYLE='overflow:no;width:100%;height=100%; padding:0px; margin: 0px'>
            
            <form id="alertForm" name="alertForm" action="AlertDialog.jsp" method="post">
                <input type="hidden" name="browser" value="<%=browserId%>">
                <input type="radio" <%=sendedChecked%>  name="direction" onClick="doClose=0; getObj('alertForm').submit();" value="out"><span  class="caption_normal_search"><j:i18n id="LABEL_ALERT_SENDED"/></span>
                <input type="radio" <%=receivedString%> name="direction" onClick="doClose=0; getObj('alertForm').submit();" value="in"><span  class="caption_normal_search"><j:i18n id="LABEL_ALERT_RECEIVED"/></span>
                <table cellspacing="0" cellpadding="0" width="100%" border="0">
                    <tr>
                        <td class="sbh" width="30"  nowrap >&nbsp;</td>
                        <td class="sbh" width="70"  nowrap ><j:i18n id="LABEL_ALERT_TYPE"/></td>
                        <td class="sbh" width="140" nowrap ><j:i18n id="LABEL_ALERT_DATE"/></td>
                        <td class="sbh" width="90"  nowrap ><j:i18n id="LABEL_ALERT_FROM"/></td>
                        <td class="sbh" width="100%"         ><j:i18n id="LABEL_ALERT_MESSAGETEXT"/></td>
                    </tr>
                    <tr>
                    <td colspan=5>
                    <%
                    
                    Iterator iter=null;
                    if(sended)
                        iter=AlertManager.getSendedAlertItems(context).iterator();
                    else
                        iter=AlertManager.getReceivedAlertItems(context).iterator();
                    
                    if(iter.hasNext())
                    {
                        %>
                        <div style="overflow:auto;width:648;height:200;">
                        <table cellspacing="0" cellpadding="0" width="100%" border="0"  >
                        <%
                        boolean even=true;
                        while(iter.hasNext())
                        {
                            even=!even;
                            String css =even?"sbr_e":"sbr_o";
                            IAlertItem item=(IAlertItem)iter.next();
                            if(request.getParameter("alertItem_"+item.getKey())!=null)
                            {
                                item.delete(context);
                                continue;
                            }
                        
                            out.println("<tr class=\""+css+"\" ><td class=\""+css+"\" nowrap width=30px >");
                            if(item.isDeleteable())
                                out.println("<input type=checkbox name=alertItem_"+item.getKey()+" >");
                            out.println("</td><td class=\""+css+"\" nowrap width=70px ><img title="+item.getSeverity()+" src=\"../themes/"+jacobSession.getCurrentTheme()+"/images/severity_"+item.getSeverity()+".png\"</td>");
                            out.println("<td class=\""+css+"\" nowrap width=140px >");
                            out.println(DatetimeUtil.convertTimestampToString(new java.sql.Timestamp(item.getDate().getTime()), context.getUser().getLocale()));
                            out.println("</td>");
                            out.println("<td class=\""+css+"\" nowrap width=90px >");
                            out.println(item.getSender());
                            out.println("</td>");
                            out.println("<td class=\""+css+"\" width=100% >");
                            URL url=item.getDisplayUrl(context);
                            if(url!=null)
                            {
                                out.println("<a  class=\""+css+"\" target=\"_new\" href=\""+url.toString()+"\">"+item.getMessage()+"</a>");
                            }
                            else
                            {
                                out.println(item.getMessage());
                            }
                            out.println("</td></tr>");
                        }
                        %>
                        </table>
                        </div>
                        </td>
                        </tr>
                        <tr><td></td><td></td><td ></td><td></td></tr>
                        <tr>
                        <td colspan="5" style="margin-top:0px;padding-top:0px;">
                          <img src="images/01.gif">
                          <button class="button_normal" style="margin-top:0px;padding-top:0px;" onClick="doClose=0; getObj('alertForm').submit();"><j:i18n id="BUTTON_COMMON_DELETE"/></button>
                        </td></tr>
                        <%
                    }
                    %>
                </table>
            </form>
            </div>
        </td>
    </tr>
    <tr>
        <td  width="100%" valign="bottom">
    
            <div class="dialog_buttonbar">
                <table width="100%" border="0">
                <tr>
                    <td>
                        <input type="checkbox" onClick='doSuspend(this.checked)' name="doSuspend" <%=suspendChecked%> ><span  class="caption_normal_search"><j:i18n id="LABEL_ALERT_SUSPEND_DIALOG"/></span>
                    </td>
                    <td align="right">
                        <button class="button_normal"  onClick="doClose=0; getObj('alertForm').submit();"><j:i18n id="BUTTON_COMMON_RELOAD"/></button>&nbsp;
                        <button class="button_normal"  onClick="window.close()"><j:i18n id="BUTTON_COMMON_CLOSE"/></button>
                    </td>
                </tr>
                </table>
            </div>
        </td>
    </tr>
    </table>
    </body>
    
    
    <script>window.focus();</script>
    <script>
        addKeyHandler(document.body);
        <%-- **************************************************************************************
          CTRL+A Key handler
          Select all alert items in the list
        **************************************************************************************--%
        document.body.addKeyDown(65,function()
        {
           var items=document.getElementsByTagName("input");
           for(var i=0;i< items.length;i++)
           {
                if(items[i].name.indexOf('alertItem_')==0)
                {
                    items[i].checked=true;
                }
           }
            event.cancelBubble = true;
            event.returnValue = false;
        });
    
        <%-- **************************************************************************************
          CTRL+I Key handler
          Invert the selection
        ************************************************************************************** --%>
        document.body.addKeyDown(73, function()
        {
           var items=document.getElementsByTagName("input");
           for(var i=0;i< items.length;i++)
           {
                if(items[i].name.indexOf('alertItem_')==0)
                {
                    items[i].checked=!items[i].checked;
                }
           }
            event.cancelBubble = true;
            event.returnValue = false;
        });
    </script>

</html>

<%
}
catch(Throwable th)
{
 th.printStackTrace();
}
%>
