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
    String errorMessage  = request.getParameter("errorMessage");
    String applicationId = "@APPLICATION@";
    String versionId     = "@VERSION@";

    try
    {
            IApplicationDefinition defaultApplication =DeployMain.getApplication(applicationId,versionId);
            Context.setCurrent(new de.tif.jacob.core.JspContext(defaultApplication));
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

                // Jetzt wird in der Application etwas 'gearbeitet'. Dazu benï¿½tigt diese einen
                // gï¿½ltigen Context um eventuell Datenbank Anfragen zu machen
                //
                Context.setCurrent( new de.tif.jacob.screen.impl.html.ClientContext(user,out, app, app.getHTTPApplicationId()));

                jacobSession.register(session);

                // Falls bei dem ganzen Prozess eine Exception aufgetaucht ist, wird diese jetzt geworfen.
                // Dummerweise kï¿½nnen in "session.setAttribute(...)" Exceptions auftreten, welche nicht nach oben durchgereicht 
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
     catch(UserException ex)
     {
        errorMessage = ex.getLocalizedMessage(Locale.GERMAN);
     }
     catch(UserRuntimeException ex)
     {
        errorMessage = ex.getLocalizedMessage(Locale.GERMAN);
     }
     catch(Throwable th)
     {
        errorMessage = th.toString();
        th.printStackTrace();
     }
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="pragma" content="no-cache">
<link rel="shortcut icon" type="image/x-icon" href="favicon.ico">

<title>DocPool <%=versionId%> - Anmeldung</title>

<script language="JavaScript">

  if (top.document.location != document.location)
  {
    // use whole page if this happens in a frame
    top.document.location = document.location;
  }
  
function hasPopupblocker() 
{
  var objChild;
  var reWork = new RegExp('object','gi');

  try
  {
     objChild = window.open('','child','width=50,height=50,status=no,resizable=yes'); 
     objChild.close();
  }
  catch(e) { }

  if(!reWork.test(String(objChild)))
     return true;
  return false;
}

function showPopupBlocker()
{
   document.getElementById('blocker_info').style.display='block';
   document.getElementById('splash').style.display='none';
   
   document.getElementById('input_userid').disabled='true';
   document.getElementById('input_password').disabled='true';
   document.getElementById('input_submit').disabled='true';
   
   document.getElementById('input_submit').style.color='gray';
   document.getElementById('label_userid').style.color='gray';
   document.getElementById('label_password').style.color='gray';
}

</script>


<style>
body
{
font-family:Verdana,Helvetica,sans-serif;
}

.label
{
  font-size:10px;
  font-weight:bold;
  height:15px;
  margin-top:10px;
}

button
{
}

#login_pane
{
  position:absolute;
  top:100px;
  left:150px;
  width:242px;
}

#login_form
{
  border-top:1px dotted #f0f0f0;
  border-bottom:1px dotted #f0f0f0;
  height:160px;
  padding-top:10px;
}



#page_title
{
  position:absolute;
  left:150px;
  top:30px;
  font-weight:bold;
  font-size:18px;
  color:white;
}

input
{
  margin:0px;
  padding:0px;
  
}

#blocker_info
{
  position:absolute;
  left:500px;
  top:100px;
  height:400px;
  width:450px;
  font-weight:bold;
  color:white;
  display:none;
}


#copyright
{
  text-align:right;
  left:150px;
  bottom:10px;
  position:fixed;
  width:883px;
  height:23px;
  position:absolute;
  font-size:8pt;
  color:white;
  padding-top:4px;
}

</style>


<!--[if gte IE 5.5]>
<![if lt IE 7]>
<style type="text/css">
div#copyright {
  /* IE5.5+/Win - this is more specific than the IE 5.0 version */
  right: auto; bottom: auto;
  top: expression( ( -10 - copyright.offsetHeight + ( document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight ) + ( ignoreMe = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop ) ) + 'px' );
}
</style>
<![endif]>
<![endif]-->


</head>

<body class="top" onload="document.getElementById('input_userid').focus();if(hasPopupblocker())showPopupBlocker();">

<span class="errorMessage"><%=StringUtil.toSaveString(errorMessage)%></span>
<div id="login_pane">   
   <div class="content_top"> </div>
   <div class="content_content">
      <form id="login_form" name="loginForm" method="post" action="userlogin.jsp">
   
        <div   id="label_userid" class="label">Benutzer</div>
        <input id="input_userid" type="text" name="user" value="" tabindex="1">
        <div   id="label_password" class="label">Kennwort</div>
        <input id="input_password" type="password" name="passwd" value="" tabindex="2"><br>
   
         <button  id="input_submit" type="submit" tabindex="7">
          <div class="submit">
              <span>Anmelden</span>
          </div>
         </button>
       </form>
       <br>
       <a href="login.jsp">Gastzugang</a>
   </div>
   <div class="content_top"> </div>
   <div class="content_bottom"> </div>
</div>

<span id="page_title">Willkommen beim DocPool&nbsp;<%=versionId%></span><br>

<div  id="blocker_info" >
    <br>Sie haben einen Poupblocker installiert<br>
    Schalten Sie diesen aus.<br>
    <br>
    Näheren Hinweis unter:&nbsp;<a href="http://www.microsoft.com/windowsxp/using/web/sp2_popupblocker.mspx">Microsoft Hilfe zu Pop-up Blocker</a>
</div>

<div id="copyright" >DocPool ist eine Entwicklung der Tarragon-Software GmbH;&nbsp;&nbsp;&nbsp;</div>

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
