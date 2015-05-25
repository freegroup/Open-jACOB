<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
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

    String applicationId = "@APPLICATION@";
    String versionId     = "@VERSION@";

    try
    {
            IApplicationDefinition defaultApplication =DeployMain.getApplication(applicationId,versionId);
            Context.setCurrent(new de.tif.jacob.core.JspContext(defaultApplication));
            IUserFactory userFactory = UserManagement.getUserFactory(defaultApplication);
            IUser user = userFactory.getAnonymous();
          
            if(user!=null)
            {
                // ClientSession erzeugen
                //
                de.tif.jacob.screen.impl.html.ClientSession jacobSession = new de.tif.jacob.screen.impl.html.ClientSession(request,user, defaultApplication);

                // und Application von dieser ClientSession geben lassen
                // ( bei einer CompositeClientSession ist es die default Application )
                //
                de.tif.jacob.screen.impl.html.Application app=(de.tif.jacob.screen.impl.html.Application)jacobSession.createApplication();

                // Jetzt wird in der Application etwas 'gearbeitet'. Dazu ben�tigt diese einen
                // g�ltigen Context um eventuell Datenbank Anfragen zu machen
                //
                Context.setCurrent( new de.tif.jacob.screen.impl.html.ClientContext(user,out, app, app.getHTTPApplicationId()));

                jacobSession.register(session);

                // Falls bei dem ganzen Prozess eine Exception aufgetaucht ist, wird diese jetzt geworfen.
                // Dummerweise k�nnen in "session.setAttribute(...)" Exceptions auftreten, welche nicht nach oben durchgereicht 
                // werden.
                if(jacobSession.getCreateException()!=null)
                    throw jacobSession.getCreateException();

                // umleiten auf die content.jsp oder den EntryPoint
                //
                String url = "../../../"+BrowserType.getType(request)+"_index.jsp?browser="+app.getHTTPApplicationId();
                out.write("<html><head></script><META HTTP-EQUIV=Refresh CONTENT=\"0; URL="+url+"\"></head></html>");
                return;
            }
     }
     catch(Throwable th)
     {
        th.printStackTrace();
     }
%>
<html><head><META HTTP-EQUIV="Refresh" CONTENT="0; URL=userlogin.jsp"></head><body></body></html>

<%
}
finally
{
    // clear the current ClientContext from the thread local variable!!!
    //
    Context.setCurrent(null);
}
%>
