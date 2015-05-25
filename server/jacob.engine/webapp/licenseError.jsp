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
<%@ page import="de.tif.jacob.license.*" %>
<HTML>
<head>
  <style>
  .label
  {
  	font-family:sans-serif;
  	font-size:9pt;
  	text-align:left;
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
  input.hint
  {
		width:165px;
		border:1px solid #9FA09F;
		color:green;
		color:#89AEDD;
  }
  
  input.normal
  {
		width:165px;
		border:1px solid #9FA09F;
  }
  

  .trademark
  {
		position:absolute;
		bottom:0px;
		right:0px;
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
  </style>
  <script language="JavaScript">
	  function fitSize()
	  {
			if(document.body.offsetWidth<890)window.resizeBy(900-document.body.offsetWidth,0);
	  }
  </script></head>  
<BODY onload="fitSize()" onResize="fitSize()" scroll="no" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" style="background-color:#D0DAE0;">
		

<%
	String error     = request.getParameter("errorMessage");
	String action     = request.getParameter("action");
	String licenseKey = request.getParameter("licenseKey");

	String keyHint ="enter here your license key";
	if(error==null)
		error="";
		
	if(licenseKey!=null)
		licenseKey=licenseKey.trim();
%>
<div style="position:absolute;left:0px;top:30%;height:154px;width:100%;background:url(login/bar.png) left repeat-x;"  ></div>  
<div style="position:absolute;left:0px;top:30%;height:154px;width:100%;background:url(login/logo.png) left no-repeat;"></div>
<div style="position:absolute;left:0px;top:30%;height:154px;width:100%;background:url(login/right.png) right no-repeat;"></div>
<a target="_new" href="http://www.tarragon-software.de" class="trademark">JACOB is a product of Tarragon GmbH</a>
<div class="errorMessage"><%=error%></div>
<div style="position:absolute;left:45%;top:30%;height:114px;width:0px;margin-top:20px;border-left:1px solid white;border-right:1px solid #CDD0D0">
</div>
<%
	if ( action != null && action.equals("saveLicense"))
	{
		LicenseManager licManager = LicenseFactory.getLicenseManager();
		licManager.setLicense( licenseKey );
//		System.out.println(licManager.hasValidLicense());
%>

	<table style="position:absolute;left:50%;top:30%;height:140px;">  
		<tr><td valign="middle" align="top" class="label" >
Your license has been recorded.  <br>
If it is valid, you should be able to get to the login screen.  <br>
If not, you will be redirected here again until you enter a valid license.	</td></tr>
	<tr><td align="right">
	<button TYPE="button" onclick="location.href='login.jsp'" class="loginButton" >Login</button>
	</td></tr>
	</table>
<%
	}
	else
	{
		%>
<form action=licenseError.jsp method="post">
		<table style="position:absolute;left:50%;top:30%;height:154px;">  
		<tr><td valign="middle" align="top" >
			<table height="100%" ><tr><td  align="top" class="label">
		              This application requires a valid license in order to run.<br>Your license is either invalid or expired.
		        </td><td></td></tr>
						<tr><td width="100%">
		              <INPUT TYPE="text" class="hint" name="licenseKey" VALUE="<%=keyHint%>" onFocus="if(this.value=='<%=keyHint%>'){this.value='';this.className='normal'}" onBlur="if(this.value==''){this.value='<%=keyHint%>';this.className='hint';}" style="width:100%;" >
		        </td><td>
		              <INPUT TYPE="submit" name=submit VALUE="Save" class="loginButton" >
		        </td></tr>
		  </table>
		</td></tr>
		</table>
		<input type=hidden name=action value="saveLicense">
</form>
		<%
	}
%>

</BODY>
</HTML>