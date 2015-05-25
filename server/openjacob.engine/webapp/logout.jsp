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

<%@ page import="de.tif.jacob.screen.impl.tag.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.exception.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.Bootstrap" %>
<%@ page import="de.tif.jacob.core.definition.IApplicationDefinition" %>
<%
try
{
    response.setHeader("Cache-Control","no-cache");

    IApplicationDefinition forceApplication = null;
    String forceApplName = request.getParameter("forceApp");
    if (forceApplName != null)
        forceApplication = DeployMain.getActiveApplication(forceApplName);
    else
        forceApplication = DeployMain.getDefaultApplication();
    
    // Nachsehen ob nur eine einzige applikation installiert ist. Dann ist dies automatisch die
    // Default applikation. Selber Mechnischmus wird bei der login.jsp verwendet.
    if(forceApplication==null)
    {
        List entryList = new ArrayList();
   
        Iterator iter = DeployManager.getDeployEntries().iterator();
        while (iter.hasNext())
        {
           DeployEntry entry = (DeployEntry) iter.next();
           if (entry.getStatus().isProductive() && entry.isDaemon()==false && !entry.getName().equals("admin"))
           {
              entryList.add(entry);
           }
        }
        if (entryList.size() == 1)
           forceApplication = DeployMain.getActiveApplication(((DeployEntry)entryList.get(0)).getName());
    }
%>
<html >
<head>
    <META Http-Equiv="Cache-Control" Content="no-cache">
    <META Http-Equiv="Pragma" Content="no-cache">
    <META Http-Equiv="Expires" Content="0">
    <script language="JavaScript">
     if(top.frames.length > 0)
        top.location.href=self.location;
    </script>
<%
    if(forceApplication!=null)
    {
       // Nachsehen ob die "one and only" Applikation eine eigene Logoutseite hat.
       // Wenn ja, dann wird auf die Logoutseite der Applikation verzweigt.
       //
       String appLogout ="/application/"+forceApplication.getName()+"/"+forceApplication.getVersion().toShortString()+"/logout.jsp";
       String path = request.getSession().getServletContext().getRealPath(appLogout); 
       File file = new File(path);
       if (file.exists())
       {
         %><script>document.location.href=".<%=appLogout%>?forceApp=<%=forceApplName%>";</script><%
       }
       else
       {
         %><script>document.location.href="login.jsp?forceApp=<%=forceApplName%>";</script><%
       }
    }
    else
    {
      %><script>document.location.href="login.jsp";</script><%
    }
}
finally
{
    // clear the current ClientContext from the thread local variable!!!
    //
    Context.setCurrent(null);
}
%>
</head>
</html>