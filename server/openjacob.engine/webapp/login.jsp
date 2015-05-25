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
<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%
try
{
    response.setHeader("Cache-Control","no-cache");

    // avoid login, if engine startup was not successful
    //
    if (!Bootstrap.isOk())
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=booterror.jsp\"></head></html>");
        return;
    }
    
    String submitTarget = null;
          
    String userId        = request.getParameter("user");
    String passwd        = request.getParameter("passwd");
    String applicationId = request.getParameter("app");
    String versionId     = request.getParameter("version");
    String fromUrl       = request.getParameter("fromUrl");
    String errorMessage  = request.getParameter("errorMessage");

    List entryList = new ArrayList();
    IApplicationDefinition forceApplication = null;
    IApplicationDefinition defaultApplication = null;
    try
    {
        // determine productive deploy applications
        {
            Iterator iter = DeployManager.getDeployEntries().iterator();
            while (iter.hasNext())
            {
                DeployEntry entry = (DeployEntry) iter.next();
                if (entry.getStatus().isProductive() && entry.isDaemon()==false && !entry.getName().equals("admin"))
                {
                    entryList.add(entry);
                }
            }
        }
        
        // check whether login should be foreced to an single application
        {
            String forceApplName = request.getParameter("forceApp");

            if("null".equals(forceApplName))
               forceApplName=null;
               
            if (forceApplName != null)
                forceApplication = DeployMain.getActiveApplication(forceApplName);
            if (forceApplication != null)
                submitTarget = forceApplName;
        }
        
        if (applicationId!=null)
        {
            if(versionId!=null && versionId.length()>0)
            {
                defaultApplication =DeployMain.getApplication(applicationId,versionId);
            }
            else
            {
                defaultApplication =DeployMain.getActiveApplication(applicationId);
                versionId = defaultApplication.getVersion().toString();
            }
            
            if (entryList.size() == 1)
                forceApplication = defaultApplication;
             
            IUserFactory userFactory = UserManagement.getUserFactory(defaultApplication);
            IUser user = null;
            // normal login with user password
            //
            if(userId != null && passwd != null)
            {
                user=UserManagement.getValid(applicationId,versionId,userId,passwd);
            }
            else if(userFactory instanceof HttpAuthentificator)
            {
                // login with request/response context. Usefull for 3rd party UserManagement.
                // Like SSO cookie redirect handling to another AppServer.
                user=((HttpAuthentificator)userFactory).get(request,response);
            }
    
            if(user!=null)
            {
                // ClientSession erzeugen
                //
                de.tif.jacob.screen.impl.html.ClientSession jacobSession;
                if(Property.RUNTIME_MERGE_APPLICATION.getBooleanValue())
                  jacobSession = new de.tif.jacob.screen.impl.html.CompositeClientSession(request,user,passwd, defaultApplication);
                else
                  jacobSession = new de.tif.jacob.screen.impl.html.ClientSession(request,user, defaultApplication);

                
                // und Application von dieser ClientSession geben lassen
                // ( bei einer CompositeClientSession ist es die default Application )
                //
                de.tif.jacob.screen.impl.html.Application app=(de.tif.jacob.screen.impl.html.Application)jacobSession.createApplication();
                
                if (submitTarget != null)
                  app.setDoForceApplicationOnLogin();

                // Jetzt wird in der Application etwas 'gearbeitet'. Dazu benötigt diese einen
                // gültigen Context um eventuell Datenbank Anfragen zu machen
                //                
                Context.setCurrent( new de.tif.jacob.screen.impl.html.ClientContext(user,out, app, app.getHTTPApplicationId()));
                
                jacobSession.register(session);
                
                String url;
                if(fromUrl!=null && fromUrl.length()>0)
                {
                    // Falls der Benutzer ursprünglich zu einem EntryPoint wollte, wird dieser jetzt dahin umgeleitet
                    //
                    url = new String(Base64.decode(fromUrl));
                    
                    // neue Application mit dem Entrypoint verknüpfen
                    url += "&browser=" + app.getHTTPApplicationId();
                }
                else
                {
                    url = BrowserType.getType(request)+"_index.jsp?browser="+app.getHTTPApplicationId();
                }
                    
                // Falls bei dem ganzen Prozess eine Exception aufgetaucht ist, wird diese jetzt geworfen.
                // Dummerweise können in "session.setAttribute(...)" Exceptions auftreten, welche nicht nach oben durchgereicht 
                // werden.                  
                if(jacobSession.getCreateException()!=null)
                    throw jacobSession.getCreateException();
                
                // umleiten auf die content.jsp oder den EntryPoint
                //                    
                out.write("<html><head></script><META HTTP-EQUIV=Refresh CONTENT=\"0; URL="+url+"\"></head></html>");
                return;
            }
        }
        else
        {
            UserManagement.logOutUser(request,response);
            
            if (forceApplication == null)
            {
                if (Property.RUNTIME_MERGE_APPLICATION.getBooleanValue())
                    forceApplication = DeployMain.getDefaultApplication();
                else if (entryList.size() == 1)
                    forceApplication = DeployMain.getActiveApplication(((DeployEntry) entryList.get(0)).getName());
            }
            
            if (forceApplication != null)
                defaultApplication = forceApplication;
            else
                defaultApplication = DeployMain.getDefaultApplication();
    
        }
     }
     catch(UserException ex)
     {
        errorMessage = ex.getLocalizedMessage(de.tif.jacob.screen.impl.html.ClientSession.getHttpLocale(request));
     }
     catch(UserRuntimeException ex)
     {
        errorMessage = ex.getLocalizedMessage(de.tif.jacob.screen.impl.html.ClientSession.getHttpLocale(request));
     }
     catch(Throwable th)
     {
        errorMessage = th.toString();
        th.printStackTrace();
     }
     
     String redirectUrl = de.tif.jacob.core.Redirect.getUrl(request);
     if (redirectUrl != null)
     {
         out.write("<html><head></script><META HTTP-EQUIV=Refresh CONTENT=\"0; URL="+redirectUrl+"\"></head></html>");
         return;
     }

     String appValue="";
     String app="";
     String version="";
     // 
     if (defaultApplication!=null)
     {
        app      = defaultApplication.getName();
        version  = defaultApplication.getVersion().toString();
        appValue = app+" "+version;
     }

%>
<html >
<head>
    <META Http-Equiv="Cache-Control" Content="no-cache">
    <META Http-Equiv="Pragma" Content="no-cache">
    <META Http-Equiv="Expires" Content="0">
<%
  if (forceApplication == null)
  {  
%>
    <link type="image/x-icon" rel="shortcut icon" href="<j:favIcon/>">
<% 
  }
  else
  { 
%>  
    <link type="image/x-icon" rel="shortcut icon" href="<j:favIcon applName="<%=forceApplication.getName()%>" applVersion="<%=forceApplication.getVersion().toString()%>"/>">
<% 
  }
%>  
    <script type="text/javascript" src="javascript/<j:browserType/>_common.js"></script>
    <!--REGEXP_START_REMOVE-->
    <script type="text/javascript" src="javascript/debug.js" ></script>
    <!--REGEXP_END_REMOVE-->
    <script language="JavaScript">
  <!--
   if(top.frames.length > 0)
    top.location.href=self.location;
  //-->
  function fitSize()
  {
                if(document.body.offsetWidth<800)window.resizeBy(850-document.body.offsetWidth,0);
  }
  </script>
  <style>
  .label
  {
        font-family:sans-serif;
        font-size:9pt;
        width:80px;
        text-align:right;
  }
  .loginButton
  {
          width:82px;
          height:24px;
          border:0px;
          color:white;
          font-family:sans-serif;
        font-size:9pt;
          background:url(login/button.png);
  }
  input
  {
                width:165px;
                border:1px solid #9FA09F;
  }
  select
  {
                width:165px;
                border:1px solid #9FA09F;
  }
  .applicationName
  {
                position:absolute;
                left:0px;
                top:100px;
                height:54px;
                width:342px;
                text-align:right;
                font-family:sans-serif;
                font-weight:bold;
                font-size:10pt;
                color:#298EBD;
  }
  .trademark
  {
                position:absolute;
                bottom:5px;
                right:5px;
                font-family:sans-serif;
                font-weight:normal;
                font-size:8pt;
                color:#298EBD;
        }
        .errorMessage
        {
                position:absolute;
                top:30%;
                left:50%;
                margin-top:230px;
                margin-left:90px;
                font-family:sans-serif;
                font-size:9pt;
                width:165px;
        }
        
        .infoFrame
        {
                position:absolute;
                top:30%;
                left:50%;
                margin-top:270px;
                margin-left:90px;
                width:265px;
        }
        
  </style>
<%
  if (forceApplication == null)
  {  
%>
  <title><j:windowTitlePrefix/>Login</title>
<% 
  }
  else
  { 
%>  
  <title><j:windowTitlePrefix applName="<%=forceApplication.getName()%>"/>Login - <%=forceApplication.getTitle()%></title>
<% 
  }
%>  
</head>

<BODY onload="document.body.style.visibility='inherit';fitSize()" onResize="fitSize()" scroll="no" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" style="visibility:hidden;background-color:#D0DAE0;">

<div style="position:absolute;left:0px;top:30%;height:154px;width:100%;background:url(login/bar.png) left repeat-x;"  ></div>  
<div style="position:absolute;left:0px;top:30%;height:154px;width:100%;background:url(login/right.png) right no-repeat;"></div>
<a href="http://www.tarragon-software.de" class="trademark"><j:i18n id="LINK_LOGIN_TRADEMARK"/></a>
<%
  if (errorMessage != null)
  {  
%>
<div class="errorMessage"><%=StringUtil.htmlEncode(errorMessage)%></div>
<% 
  }
%>  
<iframe class="infoFrame" src="./applicationInfo.html" scrolling="auto" frameborder="0" ></iframe>
<div style="position:absolute;left:45%;top:30%;height:114px;width:0px;margin-top:20px;border-left:1px solid white;border-right:1px solid #CDD0D0"></div>
<FORM id="submitForm" autocomplete="off" name="submitForm" ACTION="<%=submitTarget != null ? submitTarget : "login.jsp"%>" method="post" >
    <input type="hidden" id="fromUrl"        name="fromUrl"        value="<%=fromUrl==null?"":fromUrl%>" >
    <input type="hidden" id="timezoneOffset" name="timezoneOffset" value="0" >
<table style="position:absolute;left:50%;top:30%;height:154px;">  
<tr><td valign="middle" >
    <table >
        <tr>
              <td class="label"><j:i18n id="LABEL_LOGIN_USERID"/></td>
              <td width="1"><INPUT autocomplete="off" TYPE=text id=user NAME=user VALUE='<%=StringUtil.toSaveString(userId )%>' SIZE="14"></td>
              <td></td>
        </tr>
        <%
        try
        {
           if(forceApplication!=null)
           {
              // Nachsehen ob die "one and only" Applikation eine eigene Loginseite hat.
              // Wenn ja, dann wird auf die Loginseite der Applikation verzweigt.
              //
              String appLogin ="/application/"+forceApplication.getName()+"/"+forceApplication.getVersion().toShortString()+"/login.jsp";
              String path = request.getSession().getServletContext().getRealPath(appLogin); 
              File file = new File(path);
              if (file.exists())
              {
                %><script>document.location.href=".<%=appLogin%>";</script><%
              }
        %>
        <tr>
             <td class="label"><j:i18n id="LABEL_LOGIN_PASSWORD"/></td>
             <td><INPUT autocomplete="off" TYPE="password" id="passwd" NAME="passwd" value="" SIZE="14" ></td>
             <td><INPUT TYPE="submit" name="submit" VALUE="login" class="loginButton" ></td>
        </tr>
        <input type="hidden" id="app"     name="app"     value="<%=app%>" >
        <input type="hidden" id="version" name="version" value="<%=version%>" >
        <%
        }
        else 
        {
           out.println("<script>");
           out.println("var versions = new Array();");
           for (int i=0; i<entryList.size(); i++)
           {
               DeployEntry entry = (DeployEntry) entryList.get(i);
               
               out.println("versions["+i+"]='"+entry.getVersion()+"'");
               
               // keine DefaultApplication gefunden. Jetzt wrid die erst beste genommen
               //
               if(appValue.length()==0)
                  appValue=entry.getName()+" "+entry.getVersion();
           }
           out.println("</script>");
           {
             %>
             <tr>
                 <td class="label"><j:i18n id="LABEL_LOGIN_PASSWORD"/></td>
                 <td><INPUT autocomplete="off" TYPE="password" id="passwd" NAME="passwd" value="" SIZE="14" ></td>
                 <td></td>
             </tr>
             <tr><td align="right" class="label"><j:i18n id="LABEL_LOGIN_APPLICATION"/></td><td>
                  <select name="app" onchange="getObj('version').value=(versions[this.selectedIndex]);">
                                          <%
                  for (int i=0; i<entryList.size(); i++)
                  {
                      DeployEntry entry = (DeployEntry) entryList.get(i);
                      String name = entry.getName()+" "+entry.getVersion();
                      if(appValue.equals(name))
                          out.println("<option selected value=\""+entry.getName()+"\">"+name+"</option>");
                      else
                          out.println("<option value=\""+entry.getName()+"\">"+name+"</option>");
                  }
                                          %>
                                          </select>
                                          </td>
                  <td><INPUT TYPE="submit" name="submit" VALUE="login" class="loginButton" ></td>
              </tr>
                   <input type="hidden" id="version" name="version" value="<%=version%>" >
              <%
            }
       }
    }
    catch(Throwable th)
    {
        th.printStackTrace();
    }
    %>
    </table>
</td></tr></table>
</FORM>
<div style="position:absolute;left:0px;top:30%;height:154px;width:50%;background:url(login/logo.png) left no-repeat;">
<%
  if (forceApplication != null)
  { 
%>  
  <div class="applicationName" style="position:absolute;right:0px;top:120px"><%=forceApplication.getTitle()%></div>
<% 
  }
%>  
</div>
<script> 
    if(getObj('user').value =="")
        setFocus('user');
    else
        setFocus('passwd');
</script>

<script>
    addKeyHandler(document.body);
    document.body.addKeyPress(13,function(){getObj('submit').click();});
    getObj('timezoneOffset').value=new Date().getTimezoneOffset();
</script>

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
