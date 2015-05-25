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
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.exception.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
response.setContentType("application/javascript");
try
{
    String browserId = request.getParameter("browser");

    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.println("isLoggedOutBySystem=true;");
        return;
    }

    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        Application app = (Application)jacobSession.getApplication(browserId);
        if(app!=null)
        {
           jacobSession.sendKeepAlive(browserId);
           String code = jacobSession.fetchAsynchronJavaScript();
           if(code!=null)
              out.println(code);
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

// Falls Fenster geöffnet wurden, muessen diese jetzt angezeigt werden.
// Wegen einem deadlock im IE kann dies nur asynchron nach dem 'flipPage' erfolgen.
// Bedingt durch diesen Mechanismus muss dies auch bei dem 'serverPush' umgesetzt werden. :-|
//
if(windowsToShow!=null)
{
  // array kopieren damit der serverPush die Fenster nicht nochmal anzeigt.
  // Dies kann bei einem "prompt(...)" passieren. Der IE oder FireFox stoppt bei der verarbeitung
  // eines alert/prompt. Wenn in dieser Zeit ein ServerPush kommt wird auf "windowsToShow" zugegriffen und dies
  // ist dann nicht zurück gesetzt => die Fenter werden doppelt angezeigt.
  //
  var tmp = windowsToShow;
  windowsToShow = new Array();
	for(a=0;a<tmp.length; a++)
	{
	  tmp[a].show();
	}
}