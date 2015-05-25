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
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.util.StringUtil"%>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.util.stream.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="de.tif.jacob.util.file.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.i18n.I18N"%>
<% 
try
{
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }

    String browserId  = request.getParameter("browser");
    String guid       = request.getParameter("guid");

    Application   app        = (Application)jacobSession.getApplication(browserId);
    ClientContext context    = new ClientContext(jacobSession.getUser(),out, app, browserId);
    Context.setCurrent(context);

    String themeId = jacobSession.getCurrentTheme();
    Theme theme = ThemeManager.getTheme(themeId);
    UploadDialog dialog =(UploadDialog)jacobSession.getDialog(guid);

    String title = dialog.getTitle()!=null?dialog.getTitle():I18N.getLocalized("DIALOG_TITLE_UPLOAD",context);
    String description = StringUtil.toSaveString(dialog.getDescription());
    %>
<html>
        <head>
            <title><%=title%></title>
            <META Http-Equiv="Cache-Control" Content="no-cache">
            <META Http-Equiv="Pragma" Content="no-cache">
            <META Http-Equiv="Expires" Content="0">

            <script>var userTheme = '<%=app.getTheme()%>';</script>
            <!--REGEXP_START_REMOVE-->
            <script type="text/javascript" src="../javascript/prototype.js"></script>
            <script type="text/javascript" src="../javascript/moo.dom.js"></script>
            <script type="text/javascript" src="../javascript/moo.fx.js"></script>
            <!--REGEXP_END_REMOVE-->
            <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
            <script id="serverPush"        type="text/javascript" src="../javascript/blank.js" ></script>

            <link   type="text/css" rel="stylesheet" href="../themes/common.css" />
            <link  type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />

            <SCRIPT type="text/javascript" >
                var currentTimer = window.setInterval("loadRemotePage('serverPush.jsp?guid=<%=guid%>&browser=<%=browserId%>')",<%=de.tif.jacob.core.Property.KEEPALIVEINTERVAL_DIALOG.getIntValue()*1000%>);
            </SCRIPT>

            <script   type="text/javascript">
            function prepareUpload()
            {
                new LayerObject("fileSelect").hide();
                new LayerObject("progressBarPane").show();
                clearInterval(currentTimer);
                currentTimer = window.setInterval("loadRemotePage('serverPush.jsp?guid=<%=guid%>&browser=<%=browserId%>')",500);
                getObj("submitButton").setAttribute('disabled','true');
            }

            </script>
        </head>
    <body class=dialog scroll=no >
<%-- muss in der URL stehen, da der multipart parser die Variablen sonst 'verschlampt(?)'
            <input type=hidden name=browser value="<%=browserId%>">
            <input type=hidden name=guid    value="<%=guid%>">
--%> 
    <form   onSubmit="prepareUpload();" ENCTYPE="multipart/form-data" METHOD="POST"  ACTION="UploadDialogSubmit.jsp?browser=<%=browserId%>&guid=<%=guid%>">
    <table  cellspacing="0" cellpadding="0" width="100%" height=100% border="0" >
    <tr><td  class="dialog_infotext"><%=description%></td></tr>
    <tr height=100% >
        <td>
            <div id="progressBarPane" STYLE="padding:10px;display:none">
                <j:i18n id="LABEL_UPLOAD_UPLOAD_IN_PROGRESS"/><br>
                <div style="border:1px solid black;width:350px" ><img id="progressImage" src="../themes/progressbar.png" width="1" height="20"/></div>
            </div>
            <div id="fileSelect" STYLE="padding:10px;">
                <span class='caption_normal' >&nbsp;<j:i18n id="LABEL_UPLOAD_FILE"/></span>
                <input style="width:70%" class="text_normal" SIZE="30" TYPE="FILE" NAME="FN"/>
            </div>
        </td>
    </tr>
    <tr>
        <td  width=100% align="right" valign="bottom">
            <div class="dialog_buttonbar">
                <input id=submitButton class="button_emphasize_normal" TYPE="SUBMIT" VALUE="<j:i18n id="BUTTON_COMMON_UPLOAD"/>">
                <input type=button  value="<j:i18n id="BUTTON_COMMON_CLOSE"/>"  onClick="FireEvent(window,'close');" class="button_normal" >
            </div>
        </td>
    </tr>
    </table>
    </form>
    </body>
<%-- Falls das Fenster zuvor im Hintergrund war, muss dieses jetzt nach vorne gebracht werden --%>
<script   type="text/javascript">window.focus();</script>
    </html>
<%
}
catch(Throwable th)
{
    th.printStackTrace();
}
%>