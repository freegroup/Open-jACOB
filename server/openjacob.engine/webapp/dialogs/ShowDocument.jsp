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
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.core.data.impl.DataBinary" %>
<%@ page import="sun.net.www.MimeTable" %>
<% 
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }
    response.reset();

    String filename  = request.getParameter("filename");
    String uri       = request.getParameter("uri");
    String browserId = request.getParameter("browser");
    String submitted = request.getParameter("submitted");
    
    Application   app        = (Application)jacobSession.getApplication(browserId);
    ClientContext context    = new ClientContext(jacobSession.getUser(),out, app, browserId);
    Context.setCurrent(context);

    if(submitted!=null)
    {    
        byte[] document = DataBinary.createByReference(uri).getValue();
    
    
        filename= filename.replace('\\','/');
        filename= filename.replace('*','_');
        filename= filename.replace(':','_');
        filename= filename.replace('@','_');
        filename= filename.replace(' ','_');
        int index = filename.lastIndexOf("/");
        if(index>0)
            filename = filename.substring(index+1);
    
        // prepare writing the file as response
        String mimeType = "application/octet-stream";
        if(MimeTable.getDefaultTable().findByFileName(filename)!=null)
            mimeType = MimeTable.getDefaultTable().findByFileName(filename).getType();
        
        
        // If the InternetExplorer the default browser for xml documents, then the IE trys to render
        // the document with the xsl import declaration.
        // The browser must show the plain xml file. No rendering
        //
        if(mimeType.equals("application/xml"))
            mimeType="text/plain";
        
        response.setContentType("application/octet-stream ");
        response.setHeader("Content-Disposition", "attachment; filename="+filename);
        response.setContentLength(document.length);
        ServletOutputStream stream = response.getOutputStream();
        stream.write(document);
        stream.flush();
        return;
    }
    String themeId = jacobSession.getCurrentTheme();
    Theme theme = ThemeManager.getTheme(themeId);
%>
<html>
    <head>
        <META Http-Equiv="Cache-Control" Content="no-cache">
        <META Http-Equiv="Pragma" Content="no-cache">
        <META Http-Equiv="Expires" Content="0">
        <link type="text/css"  rel="stylesheet" href="../themes/common.css" />
        <link type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />
        <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
    </head>

    <body class=dialog scroll=no >

    <form  method="post" action="ShowDocument.jsp" >
    <table  cellspacing="0" cellpadding="0" width="100%" height=100% border="0">
    <tr height=50 ><td></td></tr>
    <tr height=100% >
        <td valign=top style="padding:20">
            <input type=hidden name=filename value="<%=filename%>">
            <input type=hidden name=uri value="<%=uri%>">
            <input type=hidden name=browser value="<%=browserId%>">
            <input type=hidden name=submitted value="<%=true%>">
            <h3><j:i18n id="MSG_DOWNLOAD_DOCUMENT_01"/></h3>
            <j:i18n id="MSG_DOWNLOAD_DOCUMENT_02"/>
        </td>
    </tr>
    <tr>
        <td  width=100% align="right" valign="bottom">
            <div class="dialog_buttonbar">
                <input  name=submitForm id=submitForm  class="button_emphasize_normal" type=submit value='<j:i18n id="BUTTON_COMMON_SHOW"/>' />
                <button class="button_normal" onClick="window.close()"><j:i18n id="BUTTON_COMMON_CLOSE"/></button>
            </div>
        </td>
    </tr>
    </table>
    </form>
    </body>
</html>
<script>
window.resizeTo(450,350);
//getObj('submitForm').click();
</script>
                    