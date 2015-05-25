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

<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<% 
try
{
    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=login.jsp\"></head></html>");
        return;
    }

    String browserId = request.getParameter("browser");
    HTTPClientSession jacobSession = HTTPClientSession.get(request);

    // no valid application in the session found...cleanup all data
    // and redirect to the login screen
    //
    Application app =(Application) jacobSession.getApplication(browserId);
    if(app==null)
    {
        UserManagement.logOutUser(request,response);
		    return;
   	}
%> 	
<html>
    <head>
        <META Http-Equiv="Cache-Control" Content="no-cache">
        <META Http-Equiv="Pragma" Content="no-cache">
        <META Http-Equiv="Expires" Content="0"> 

        <script type="text/javascript" src="javascript/<j:browserType/>_common.js" ></script>
        <!--REGEXP_START_REMOVE-->
        <script type="text/javascript" src="javascript/debug.js"></script>
        <script type="text/javascript" src="javascript/popupWindow.js" ></script>
        <script type='text/javascript' src='javascript/jACOBTable.js'></script>
        <!--REGEXP_END_REMOVE-->

       <link  type="text/css"  rel="stylesheet" href="themes/common.css" />
       <link  type="text/css"  rel="stylesheet" href="themes/<%=app.getTheme()%>/custom.css" />
       <title>jACOB</title>
    </head>
<script>
   // required for the FireEvent Method to autoclose the popup
   //
   var autoCloseWindow=true;
   var userTheme = '<%=app.getTheme()%>';
</script>	

<body  style="margin:0px;" onresize="resize()" onkeypress="return handleKey(event);" id="body">
	<%
   	String foreignBrowser = request.getParameter("foreignBrowser");
	ClientContext context = new ClientContext(jacobSession.getUser(),out, app,browserId);
	Context.setCurrent(context);
	ForeignFieldSelectDialog.renderBrowser(context,foreignBrowser);
	%>
</body>
<script>
var browserId = '<%=browserId%>';
var j         = new jACOBTable("<%=foreignBrowser%>", false);
resize();
calculateCurrentHighlightedRow('<%=foreignBrowser%>');

function resize()
{
	if(j===undefined || j===null)
		return;
  var size = new PageSize();
  j.root.style.width = size.width+"px";
  j.root.style.height= size.height+"px";
  j.resize();
}

function handleKey(event)
{
	switch(event.keyCode)
	{
		case 40:
			onCursorDownKey('<%=foreignBrowser%>');
		  break;
		case 38:
			onCursorUpKey('<%=foreignBrowser%>');
		  break;
		case 13:
			onEnter('<%=foreignBrowser%>');
		  break;
	}
	return false;
}
   	    
</script>
</html>
<%
}
finally
{
	// clear the current ClientContext from the thread local variable!!!
	//
	ClientContext.setCurrent(null);
}
%>
