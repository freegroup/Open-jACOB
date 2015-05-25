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
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<% 
    ClientSession jacobSession =(ClientSession) HTTPClientSession.get(request);
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
    GridTableDialog dialog = (GridTableDialog)jacobSession.getDialog(guid);
    String themeId = jacobSession.getCurrentTheme();
    Theme theme = ThemeManager.getTheme(themeId);
%>
<HTML >
    <title><j:dialogTitle id="DIALOG_TITLE_GRIDTABLE"/></title>
    <HEAD>
        <META Http-Equiv="Cache-Control" Content="no-cache">
        <META Http-Equiv="Pragma" Content="no-cache">
        <META Http-Equiv="Expires" Content="0">
        
        <script>var userTheme = '<%=app.getTheme()%>';</script>
        <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
        <link   type="text/css" rel="stylesheet" href="../themes/common.css" />
        <link  type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />
    </HEAD>
    <script>
       // required for the FireEvent Method to autoclose the popup
       //
       var autoCloseWindow=true;
    </script>   
    <body class="dialog">
    <DIV STYLE='overflow:auto;width:100%;height:100%; padding:0px; margin: 0px'>
        <table  cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">
        <tr height=1 class=sbh>
        <%
            for(int i=0; i< dialog.getHeader().length;i++)
            {
                out.print("<td class=sbh valign=top height=1>"+dialog.getHeader()[i]+"</td>");
            }
            out.println("\n</tr>");
            for(int row=0; row< dialog.getData().length;row++)
            {
                  if((row%2)==0)
                    out.println("<tr  height=1 class=\"sbr_e\" onMouseOver=\"this.className='sbr_h'\" onMouseOut=\"this.className='sbr_e'\" onClick=\"FireEventData('"+guid+"','click','"+row+"')\" >");
                  else
                    out.println("<tr height=1  class=\"sbr_o\" onMouseOver=\"this.className='sbr_h'\" onMouseOut=\"this.className='sbr_o'\" onClick=\"FireEventData('"+guid+"','click','"+row+"')\" >");
                for(int column=0; column< dialog.getData()[row].length;column++)
                {
                    out.print("<td>"+dialog.getData()[row][column]+"</td>");
                }
                out.println("</tr>");
            }
            out.println("<tr>");
            for(int i=0; i< dialog.getHeader().length;i++)
            {
                out.print("<td valign=top >&nbsp</td>");
            }
            out.println("</tr>");
        %>
        
        </table>
    </div>  
    <body>
</html>
