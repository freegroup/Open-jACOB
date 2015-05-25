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

<div style="position:absolute;left:50%;top:65px;font-size:15pt">
      <%-- 
           KEIN I18N an dieser Stelle. Es ist ja nicht gesagt, dass bei einem Fehler I18N funktioniert
           (z.B. bei einem fehlerhaften/unvollständigen Deployment)
      --%>
      First of all you must accept our license agreement<br>
</div>
<div style="top:170px;left:0px;margin-right:100px;width:100%;position:absolute;">
<div style="padding:10px;border-bottom:1px solid gray;border-top:1px solid gray;font-size:12pt;">License Information:</div>
<pre style="margin-top:10px;margin-left:10px;font-size:10pt;font-family:sans-serif;">
- HIER KOMMT DAS COPYRIGHT REIN -
</pre>
<form method="post" action="license.jsp" style="width:100%;background-color:#EFEFEE;padding:10px;border-top:1px solid gray;border-bottom:1px solid gray;">
<input type="checkbox" name="accepted" >accept&nbsp;
<input type="submit" value="Send"  class="loginButton" >
</form>
</div>


</BODY>
</HTML>
