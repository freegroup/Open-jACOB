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

    String userId        = request.getParameter("user");
    String passwd        = request.getParameter("passwd");
    String applicationId = "digisim";
    String versionId     = "0.1";

    try
    {
            IApplicationDefinition defaultApplication =DeployMain.getApplication(applicationId,versionId);

            IUserFactory userFactory = UserManagement.getUserFactory(defaultApplication);
            IUser user = null;
            // normal login with user password
            //
            if(userId != null && passwd != null)
            {
                user=UserManagement.getValid(applicationId,versionId,userId,passwd);
            }

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
                String url = "edit.jsp?browser="+app.getHTTPApplicationId();
                out.write("<html><head></script><META HTTP-EQUIV=Refresh CONTENT=\"0; URL="+url+"\"></head></html>");
                return;
            }
     }
     catch(Throwable th)
     {
        th.printStackTrace();
     }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Digital Circuit Simulator</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="./login/login.css" />
<script type="text/javascript">
function focusit() 
{
   document.getElementById('log').focus();
}
window.onload = focusit;
</script>

</head>
<body>
<center>
<div class="header">Digital Circuit Simulator</div>
<div class="login_panel">

<h1><a href="http://www.openjacob.org/" title="Powered by Open-jACOB">
</a></h1>

<form name="loginform" id="loginform" action="login.jsp" method="post">
<label>Benutzername:<br/></label><input type="text" name="user" id="log" value="" size="20" tabindex="1" />
<br>
<br>
<label>Passwort:<br/></label><input type="password" name="passwd" id="pwd" value="" size="20" tabindex="2" />
<p>
<p class="submit"><input type="submit" name="submit" id="submit" value="Anmelden &raquo;" tabindex="4" /></p>
</form>
<label>Use <b>guest / guest</b> for demo account</label>
<div class="footer">&nbsp;</div>
</div>
</center>
</body>
</html>

<%
}
finally
{
    // clear the current ClientContext from the thread local variable!!!
    //
    Context.setCurrent(null);
}
%>
