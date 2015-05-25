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
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="java.util.*" 
         import="de.tif.jacob.core.*" 
         import="de.tif.jacob.screen.impl.*"
         import="de.tif.jacob.screen.impl.html.*" 
         import="de.tif.jacob.security.*"
         import="de.tif.jacob.report.*" %><%

    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }

    String      thisApp      =  request.getContextPath();
    String      thisPage     =  request.getContextPath()+request.getServletPath();
    String      thisPath     =  thisPage.substring(0,thisPage.lastIndexOf("/")+1);

    String      browserId    = request.getParameter("browser");
    String      action       = request.getParameter("action");

    Application   app          = (Application)jacobSession.getApplication(browserId);
    IUser         user         = jacobSession.getUser();
    // no valid IApplication in the session found...cleanup all data
    // and redirect to the login screen
    //
    
    if(app==null)
    {
        UserManagement.logOutUser(request,response);
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }

    ClientContext context = new ClientContext(jacobSession.getUser(),app,browserId);
    Context.setCurrent(context);
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
    <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
    <link   type="text/css"        rel="stylesheet" href="../themes/common.css" />
    <link  type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />

    <SCRIPT type="text/javascript" >
        var singleLineMessage=true;
    
        function sendMessages()
        {
            var userList=getObj('recipiens');
            for(var i=0; i<userList.options.length;i++)
            {
                var input = document.createElement("input");
                input.type  = "hidden";
                input.name  = "recipien_"+i;
                input.value = userList.options[i].value;
                getObj("fields").appendChild(input);

            }
            getObj("submitForm").submit();
        }

        function searchUser()
        {
            var width=300;
            var height=300;
            var _url="MessagingDialogFunctions/searchUserDialog.jsp?browser=<%=browserId%>&browserType=<j:browserType/>&messageType="+getObj('messageType').value+"&search="+getObj('search').value;
            var winleft = (screen.width - width) / 2;
            var winUp   = (screen.height - height) / 2;
            window.open(_url, '_new', 'left='+winleft+', top='+winUp+', toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised, width='+width+',height='+height);
        }
    
        function addRecipien(label, value)
        {
            var option = document.createElement("OPTION");
            getObj('recipiens').options.add(option);
            option.text = label;
            option.value = value;
            getObj('search').value='';
            getObj('send').disabled=false;
        }
    
        function removeSelectedRecipien()
        {
            var userList=getObj('recipiens');
            for(var i=0; i<userList.options.length;i++)
            {
                if (userList.options[i].selected == true)
                {
                    userList.options[i] = null;
                    break;
                }
            }
            if(getObj('recipiens').options.length==0)
                getObj('send').disabled=true;
        }
    
        function onMessageTypeChanged()
        {
            var messageType=getObj('messageType').value;
            if(messageType=='<%=Property.YAN_PROTOCOL_ALERT.getName()%>')
            {
                getObj('broadcastNamed').disabled=false;
                getObj('broadcastAll').disabled=false;
                singleLineMessage=true;
            }
            else
            {
                getObj('broadcastAll').checked=false;
                getObj('broadcastNamed').checked=true;
                getObj('broadcastNamed').disabled=true;
                getObj('broadcastAll').disabled=true;

                getObj('recipiens').disabled=false;
                getObj('recipiens').readonly=false;
                getObj('recipiens').className='text_normal';
    
                getObj('search').disabled=false;
                getObj('search').readonly=false;
                getObj('search').className='text_normal editable_inputfield';

                getObj('searchbutton').disabled=false;
                getObj('searchbutton').className='button_normal';
                singleLineMessage=false;
            }
            while(getObj('recipiens').options.length>0)
                getObj('recipiens').options.remove(0);
        }
    
        function disableRecipien()
        {
            if(getObj('broadcastAll').checked==true)
            {
                getObj('search').disabled=true;
                getObj('search').readonly=true;
                getObj('search').className='text_disabled';

                getObj('searchbutton').disabled=true;
                getObj('searchbutton').className='button_disabled';

                getObj('recipiens').disabled=true;
                getObj('recipiens').readonly=true;
                getObj('recipiens').className='text_disabled';
                addRecipien('<j:i18n id="LABEL_MESSAGING_RECIPIEN_ALL"/>','All');
            }
            else
            {
                getObj('search').disabled=false;
                getObj('search').readonly=false;
                getObj('search').className='text_normal editable_inputfield';

                getObj('searchbutton').disabled=false;
                getObj('searchbutton').className='button_normal';

                getObj('recipiens').disabled=false;
                getObj('recipiens').readonly=false;
                getObj('recipiens').className='text_normal';
    
                while(getObj('recipiens').options.length>0)
                {
                    getObj('recipiens').options.remove(0);
                }
            }
        }
    

        resizeTo(650,550);
    </SCRIPT>
    <%-- ----------------------------------------------------------- --%>

</HEAD>
<body class="dialog" >
    
    <form name="submitForm" id="submitForm" action="MessagingDialogSubmit.jsp" method="post">
        <input type=hidden name=browser value="<%=browserId%>">

    <table cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">
    <tr id="fields" name="fields" ></tr>
    <tr>
        <td height="100%" align=center valign="top">
            <div width="100%" height="100%"  class="dialog_content">
                <table width="95%" height="100%" border="0">
                    <tr height=20>
                        <td width=100% colspan=2 ></td>
                    </tr>
                    <tr height=1 >
                        <td nowrap align=right ><span style="width:80px" class="caption_normal_update" ><j:i18n id="LABEL_MESSAGING_MESSAGETYPE"/></span></td>
                        <td width=100% >
                            <select onChange="onMessageTypeChanged();" name="messageType" id="messageType" class="text_normal editable_inputfield" style="width:100%" >
                               <%if(Property.YAN_PROTOCOL_SMS.getBooleanValue()){%>   <option value="<%=Property.YAN_PROTOCOL_SMS.getName()%>">SMS</option> <%}%>
                               <%if(Property.YAN_PROTOCOL_EMAIL.getBooleanValue()){%> <option value="<%=Property.YAN_PROTOCOL_EMAIL.getName()%>">eMail</option> <%}%>
                               <%if(Property.YAN_PROTOCOL_FAX.getBooleanValue()){%>   <option value="<%=Property.YAN_PROTOCOL_FAX.getName()%>">Fax</option> <%}%>
                               <%if(Property.YAN_PROTOCOL_ALERT.getBooleanValue()){%> <option selected value="<%=Property.YAN_PROTOCOL_ALERT.getName()%>">Alert</option> <%}%>
                           </select>
                        </td>
                    </tr>
                    <tr height=20>
                        <td width=100% colspan=2 ></td>
                    </tr>
                    <tr height=1>
                        <td width=1% valign=top align=right><span  class=caption_normal_update ><j:i18n id="LABEL_MESSAGING_RECIPIEN"/></span></td>
                        <td width=100% >
                            <input         id="broadcastAll"   name="broadcast" value=true  type=radio onClick="disableRecipien();"><span  class="caption_normal_update" ><j:i18n id="LABEL_MESSAGING_RECIPIEN_ALL"/></span><br>
                            <input checked id="broadcastNamed" name="broadcast" value=false type=radio onClick="disableRecipien();"><span  class="caption_normal_update" ><j:i18n id="LABEL_MESSAGING_RECIPIEN_BYNAME"/></span>
                        </td>
                    </tr>
                    <tr height=1>
                        <td/>
                        <td nowrap width=100% >
                            <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                                <td width=100%>
                                    <input onkeypress="if(event.keyCode==13){searchUser(); return false; };" class="text_normal editable_inputfield" type="text" id="search" name="search" style="width:100%;height:100%" />
                                </td>
                                <td width="5px">&nbsp;</td>
                                <td>
                                    <input type="button" value="<j:i18n id="BUTTON_COMMON_SEARCH"/>" id="searchbutton" onClick="searchUser();" class="button_normal" >
                                </td>
                            </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td/>
                        <td width=100% >
                            <select onkeyup="if(event.keyCode==46){removeSelectedRecipien();};" size="8" name="recipiens" id="recipiens" class="text_normal" style="width:100%" >
                            </select>
                        </td>
                    </tr>
                    <tr height=20>
                        <td width=100% colspan=2 ></td>
                    </tr>
                    <tr height=100%>
                        <td width=1% valign=top  align=right><span  class=caption_normal_update ><j:i18n id="LABEL_MESSAGING_MESSAGE"/></span></td>
                        <td width=100% ><textarea onkeypress="if(singleLineMessage==true && event.keyCode==13)return false;" id="message" name="message" class="longtext_normal editable_inputfield" style="width:100%;height:100%" ></textarea></td>
                    </tr>
                    <tr height=20>
                        <td width=100% colspan=2 ></td>
                    </tr>
                    <tr >
                        <td width=1% ></td>
                        <td width=100% ></td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td  align="right" valign="bottom">
            <div class="dialog_buttonbar">
                <input type="button" disabled name="send" id="send" value="<j:i18n id="BUTTON_COMMON_SEND"/>"  onClick="sendMessages();" class="button_emphasize_normal" >
                <input type="button" value="<j:i18n id="BUTTON_COMMON_CLOSE"/>"  onClick="FireEvent(window,'close');" class="button_normal" >
            </div>
        </td>
    </tr>
    </table>
    </form>
<body>
</html>