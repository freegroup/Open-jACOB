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

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="j" %><%@ page import="java.util.*"
    import="de.tif.jacob.license.*"
    import="de.tif.jacob.core.*"
    import="de.tif.jacob.core.exception.*"
    import="de.tif.jacob.screen.impl.theme.*"
    import="de.tif.jacob.screen.impl.*"
    import="de.tif.jacob.screen.impl.html.*"
    import="de.tif.jacob.security.*" 
    import="de.tif.jacob.util.StringUtil"
%><%
    String browserId    = request.getParameter("browser");
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    Application app = null;
    if(jacobSession!=null)
        app=(Application)jacobSession.getApplication(browserId);

    // no valid application in the session found...cleanup all data
    // and redirect to the login screen
    //
    if(app==null)
    {
        out.clearBuffer();
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=login.jsp\"></head></html>");
        return;
    }
    Theme theme = ThemeManager.getTheme(app.getTheme());
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    %>
    <HTML>
    <head>
    <META Http-Equiv="Cache-Control" Content="no-cache">
    <META Http-Equiv="Pragma" Content="no-cache">
    <META Http-Equiv="Expires" Content="0">

    <link  type="text/css"  rel="stylesheet" href="themes/common.css" />
    <link  type="text/css"  rel="stylesheet" href="<%=theme.getCSSRelativeURL()%>" />

    </head>
    
<body class="teaser" id="teaser" >
<a class="home_link" href="http://www.tarragon-software.de" target="_new" >Home</a>
<div class="teaser_applicationtitle" id="teaser_applicationtitle"><%=StringUtil.htmlEncode(app.getI18NLabel(jacobSession.getUser().getLocale()))%></div>
<div class="teaser_applicationname" id="teaser_applicationname"><%=app.getName()%></div>
<div class="teaser_applicationversion" id="teaser_applicationversion"><%=app.getVersion().toString()%></div>
<div class="teaser_username" id="teaser_username"><%=jacobSession.getUser().getFullName()%></div>
<a class="teaser_logout" id="teaser_logout" href="logout.jsp?browser=<%=browserId%>"><j:i18n id="BUTTON_TOOLBAR_CLOSE"/></a>
<div class="teaser_container01" id="teaser_container01">&nbsp;</div>
<div class="teaser_container02" id="teaser_container02">&nbsp;</div>
<div class="teaser_container03" id="teaser_container03">&nbsp;</div>
</body>
</html>


