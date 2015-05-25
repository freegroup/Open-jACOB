<%@ page import="java.io.*" %>
<%@ page import="de.tif.jacob.core.Bootstrap" %>
<%
  String accepted= request.getParameter("accepted");
	if(accepted!=null)
  	Bootstrap.setLicenseAccepted();
	
	if(Bootstrap.hasLicenseAccepted())
	{
    out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../login.jsp\"></head><body></body></html>");
		return;
	}
%>   

<HTML>
<head>
  <style>
  .label
  {
  	font-family:sans-serif;
  	font-size:9pt;
  	text-align:left;
  }

  .trademark
  {
		position:absolute;
		top:140px;
		right:30px;
		font-family:sans-serif;
		font-weight:normal;
		font-size:8pt;
		color:#298EBD;
	}
  .loginButton
  {
	  width:82px;
	  height:24px;
	  border:0px;
	  color:white;
	  font-family:sans-serif;
  	font-size:9pt;
	  background:url(button.png);
  }
    </style>
  <script language="JavaScript">
	  function fitSize()
	  {
			if(document.body.offsetWidth<890)window.resizeBy(900-document.body.offsetWidth,0);
	  }
  </script></head>  
<BODY onload="fitSize()" onResize="fitSize()" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" style="background-color:#D0DAE0;">
		
<div style="position:absolute;left:0px;top:10px;height:154px;width:100%;background:url(bar.png) left repeat-x;"  ></div>  
<div style="position:absolute;left:0px;top:10px;height:154px;width:100%;background:url(logo.png) left no-repeat;"></div>
<div style="position:absolute;left:0px;top:10px;height:154px;width:100%;background:url(right.png) right no-repeat;"></div>
<%-- 
     KEIN I18N an dieser Stelle. Es ist ja nicht gesagt, dass bei einem Fehler I18N funktioniert
     (z.B. bei einem fehlerhaften/unvollständigen Deployment)
--%>
<a target="_new" href="http://www.tarragon-software.de" class="trademark">jACOB is a product of Tarragon GmbH</a>
<div style="position:absolute;left:45%;top:10px;height:114px;width:0px;margin-top:20px;border-left:1px solid white;border-right:1px solid #CDD0D0">
</div>

<div style="position:absolute;left:50%;top:65px;font-size:10pt;color:#4a0a0a;">
      <%-- 
           KEIN I18N an dieser Stelle. Es ist ja nicht gesagt, dass bei einem Fehler I18N funktioniert
           (z.B. bei einem fehlerhaften/unvollständigen Deployment)
      --%>

</div>
<div style="top:170px;left:0px;margin-right:100px;width:100%;position:absolute;">
<div style="padding:10px;border-bottom:1px solid gray;border-top:1px solid gray;">
<b>This program is distributed under GPL -</b> <b style="color:red">Please accept the license to use this software.</b>
</div>
<div style="padding:10px;">
     Open-jACOB<br>
     Copyright (C) 2005-2006 Tarragon GmbH<br>
  
     This program is free software; you can redistribute it and/or modify<br>
     it under the terms of the GNU General Public License as published by<br>
     the Free Software Foundation; version 2 of the License.<br>
  
     This program is distributed in the hope that it will be useful,<br>
     but WITHOUT ANY WARRANTY; without even the implied warranty of<br>
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br>
     GNU General Public License for more details.<br>
  
     You should have received a copy of the GNU General Public License     <br>
     along with this program; if not, write to the Free Software<br>
     Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  <br>
     USA<br>
<br>
</div>
<form method="post" action="license.jsp" style="width:100%;background-color:#EFEFEE;padding:10px;border-top:1px solid gray;border-bottom:1px solid gray;">
<input type="checkbox" name="accepted" >Accept&nbsp;
<input type="submit" value="Ok"  class="loginButton" >
</form>
</div>


</BODY>
</HTML>
