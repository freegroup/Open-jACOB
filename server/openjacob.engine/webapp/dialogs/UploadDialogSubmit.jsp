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
<%@ page import="java.io.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.util.upload.*" %>

<%
try
{
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }

    String guid       = request.getParameter("guid");
    String browserId  = request.getParameter("browser");
    Application   app        = (Application)jacobSession.getApplication(browserId);
    ClientContext context    = new ClientContext(jacobSession.getUser(),out, app, browserId);
    Context.setCurrent(context);

    UploadDialog dialog =(UploadDialog)jacobSession.getDialog(guid);

    // Create a new file upload handler
    FileUpload fu = new FileUpload(dialog,request);
    fu.upload();
    HashMap m = fu.getMimeParts();
    MimeBodyPart part = (MimeBodyPart)m.get("FN");
    if(part != null && part.isFile() && part.getBytes()!=null)
    {
        dialog.setDocument(part.getFileName(),part.getBytes());
        %>
        <HTML >
        <HEAD>
            <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
            <META Http-Equiv="Cache-Control" Content="no-cache">
            <META Http-Equiv="Pragma" Content="no-cache">
            <META Http-Equiv="Expires" Content="0">
        
        </HEAD>
        <body>
        <script>
            FireEvent('<%=guid%>','ok');
            window.close()
        </script>
        </body>
        <%
        return;
    }
%>
<HTML >
<HEAD>
    <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
</HEAD>
<body>
<script>
    FireEvent('<%=guid%>','cancel');
    window.close()
</script>
</body>
<%
}
catch(Throwable exc)
{
    exc.printStackTrace();
}
%>
