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

<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>

<%
    String browserId = request.getParameter("browser");
    String control = request.getParameter("control");
    String event = request.getParameter("event");
    String applicationTitle = "";
    String applicationName = "";
    String versionId        = "";
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession != null)
    {
        Application app= (Application) jacobSession.getApplication(browserId);
        if (app != null)
        {
           IApplicationDefinition appDef = app.getApplicationDefinition();
           applicationName = appDef.getName();
           applicationTitle = appDef.getTitle();
           versionId = appDef.getVersion().toString();
        }
    }

%>

<html>
    <link type="text/css" rel="stylesheet" href="themes/common.css">
    <link type="image/x-icon" rel="shortcut icon" href="<j:favIcon applName="<%=applicationName%>" applVersion="<%=versionId%>"/>">
<title><j:windowTitlePrefix applName="<%=applicationName%>"/><%=applicationTitle%> <%=versionId%></title>
<script>
    window.name="jacob_twinframe";
</script>
<frameset noresize id="jacob_twinframe" rows="0,100%,*" border="0" >
    <frame noresize marginheight="0" marginwidth="0"  frameborder="0" scrolling="no" id="teaser" name="teaser"    src="teaser.jsp?browser=<%=browserId%>"  class="teaser" />
<% if (control!=null && event !=null) {%>
    <frame noresize marginheight=0 marginwidth=0  frameborder="0" scrolling="no" id=jacob_content1 name=jacob_content1  src="ns_content.jsp?browser=<%=browserId%>&control=<%=control%>&event=<%=event%>"/>
<%} else { %>
    <frame noresize marginheight=0 marginwidth=0  frameborder="0" scrolling="no" id=jacob_content1 name=jacob_content1  src="ns_content.jsp?browser=<%=browserId%>"/>
<%}%>
    <frame noresize marginheight=0 marginwidth=0  frameborder="0" scrolling="no" id=jacob_content2 name=jacob_content2  src="blank.html"/>
</frameset>
</html>


