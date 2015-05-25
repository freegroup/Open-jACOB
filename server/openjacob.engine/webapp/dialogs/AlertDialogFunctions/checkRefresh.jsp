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

<%@ page import="de.tif.jacob.messaging.alert.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.scheduler.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="java.util.*" %>
<% 

response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

try
{
    HTTPClientSession jacobSession = (HTTPClientSession)HTTPClientSession.get(request);
    if (jacobSession!=null)
    {
        TaskContextUser context = new TaskContextUser(jacobSession);
        Context.setCurrent(context);
        if (jacobSession.isAlertMustRefresh())
        {
            jacobSession.setAlertMustRefresh(false);
            %>
            window.focus();
            window.doClose=0;
            window.location.reload();
            <%
   	    }
   	}
}
catch(Exception exc)
{
    de.tif.jacob.core.exception.ExceptionHandler.handle(exc) ;
}
%>
