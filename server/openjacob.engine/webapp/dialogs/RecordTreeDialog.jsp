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
<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="de.tif.jacob.screen.impl.dialogs.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<% 
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }

    String        guid       = request.getParameter("guid");
    String        browserId  = request.getParameter("browser");
    Application app        = (Application)jacobSession.getApplication(browserId);
    ClientContext context = new ClientContext(jacobSession.getUser(),app,browserId);
    Context.setCurrent(context);
    RecordTreeDialog dialog = (RecordTreeDialog)jacobSession.getDialog(guid);
    StringBuffer sb = new StringBuffer();
    renderNode(browserId, guid, sb,dialog.getRootNode());
    String themeId = jacobSession.getCurrentTheme();
    Theme theme = ThemeManager.getTheme(themeId);
%>
<HTML>
    <title><j:dialogTitle id="DIALOG_TITLE_GRIDTABLE"/></title>
    <HEAD>
        <script>
         window.setInterval("loadRemotePage('serverPush.jsp?browser=<%=browserId%>&guid=<%=guid%>')",<%=de.tif.jacob.core.Property.KEEPALIVEINTERVAL_DIALOG.getIntValue()*1000%>);
         
         var autoCloseWindow = <%=dialog.getAutoclose()%>;
         var openerUrl=opener.location.href;
         var appWindow = null;
         var newWindowName = opener.parent.parent.name;
         if(newWindowName==null || newWindowName=="")
          newWindowName="newAppWindow";
         function FireEventInAppWindow(guid, event, data)
         {
          if(autoCloseWindow==false)
          {
            var newUrl=openerUrl.split('/ie_content')[0]+"/ie_index.jsp?browser="+new Date().getTime();
            appWindow = window.open(newUrl,newWindowName);
            window.setTimeout("doIt('"+guid+"','"+event+"','"+data+"')", 500);
          }
          else
          {
            FireEventData(guid,event,data);
          }
         }
         
         var counter = 0;
         function doIt(guid,event,data)
         {
          
            if(appWindow.contentFrame==null)
            {
              if(counter==5000)
                alert("timeout");
              else
                window.setTimeout("doIt('"+guid+"','"+event+"','"+data+"')", 50);
              counter++;                
              return;
            }
            counter=0;
            appWindow.contentFrame.opener=null;
            appWindow.contentFrame.FireEventData(guid,event,data);
         }
         
        </script>
        <META Http-Equiv="Cache-Control" Content="no-cache">
        <META Http-Equiv="Pragma" Content="no-cache">
        <META Http-Equiv="Expires" Content="0">
        
        <script>var userTheme = '<%=app.getTheme()%>';</script>
        <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
        <script type="text/javascript" src="../javascript/debug.js" ></script>
        <script type="text/javascript" src="../javascript/tree.js" ></script>
        <link   type="text/css" rel="stylesheet" href="../themes/common.css" />
        <link  type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />
        <script id="serverPush"   type="text/javascript" src="javascript/blank.js" ></script>
    </HEAD>
    <body class="dialog">
    <DIV STYLE='overflow:auto; width:100%;height=100%; padding:0px; margin: 0px'>
    <ul style="white-space:nowrap;"  class="mktree" id="recordtree" name="recordtree">
        <%=sb.toString()%>
    </ul>
    </div>
    <body>
</html>

<%!
void renderNode(String browserId, String guid, StringBuffer sb, TreeNode node)
{
    sb.append("<li style=\"white-space:nowrap;\" class=\"liOpen\" >");
    if(node.getImage()!=null)
        sb.append("<img border=0 src=\"../image?browser="+browserId+"&image="+node.getImage()+"\"/>");
    sb.append("<span onclick=\"window.event.cancelBubble=true;FireEventInAppWindow('"+guid+"','click','"+node.getCallbackId()+"');\" style=\"white-space:nowrap;\">");
    sb.append(com.lowagie.text.html.HtmlEncoder.encode( node.getLabel()));
    sb.append("</span>");
    Iterator iter = node.getChildren().iterator();
    if(iter.hasNext())
    {
        sb.append("<ul style=\"white-space:nowrap;\" >\n");
        while(iter.hasNext())
        {
          TreeNode child = (TreeNode)iter.next();
          renderNode(browserId, guid, sb,child);
        }
        sb.append("</ul>\n");
    }
}
%>
