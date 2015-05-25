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

<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.exception.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
try
{
    String browserId = request.getParameter("browser");
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        Application app = (Application)jacobSession.getApplication(browserId);
        if(app!=null)
        {
           ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
           Context.setCurrent(context);
           out.println(StringUtil.toSaveString(context.getStatusMessage()));
        }
    }
}
finally
{
    // clear the current ClientContext from the thread local variable!!!
    //
    ClientContext.setCurrent(null);
}
%>
