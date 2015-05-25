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

    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=login.jsp\"></head></html>");
        return;
    }
   		try
   		{
	String      browserId  = request.getParameter("browser");
  HTTPClientSession jacobSession = HTTPClientSession.get(request);
	Application app        = (Application)jacobSession.getApplication(browserId);

//	String control   = request.getParameter("control");
//	String event     = request.getParameter("event");
	   		
   synchronized(app)
   {
	    ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
	    Context.setCurrent(context);
			%>
			<html>
			<head>
			<META Http-Equiv="Cache-Control" Content="no-cache">
			<META Http-Equiv="Pragma" Content="no-cache">
			<META Http-Equiv="Expires" Content="0"> 
			
			<script>var userTheme = '<%=app.getTheme()%>';</script>

    		<script type="text/javascript" src="javascript/calendar.js"></script>
    		<script type="text/javascript" src="javascript/calendar-setup.js"></script>
    		<script type="text/javascript" src="javascript/lang/calendar-de.js"></script>
    		<script type="text/javascript" src="javascript/<j:browserType/>_common.js" ></script>
    		<script type="text/javascript" src="javascript/debug.js" ></script>
    		<script type="text/javascript" src="javascript/popupWindow.js" ></script>

	    <link   id="custom_css"        type="text/css"        rel="stylesheet" href="printerfriendly.css" />
		
		<body  onload="ContextMenu.intializeContextMenu();" onload="ContextMenu.intializeContextMenu()" class="contentBody">
		<DIV ID="calendarPopupDiv" STYLE="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></DIV>
		
		<table cellspacing="0" cellpadding="0" height=100% border=0 width=100%>
		<tr><td valign=top height=1>
			<table style="visibility:hidden;" cellspacing="0" cellpadding="0" border=0  width=100% >
			<%if(app.isSearchBrowserVisible(context)) {%>
				<tr>
				    <td colspan=2 class="formTabPane" id="SearchBrowserPlaceholder" valign=top >
				    </td>
				</tr>
				<tr height=1 >
				    <td colspan=2 class="searchTabFooter" id="SearchBrowserTabPanePlaceHolder">
				    </td>
				</tr>
			<%}%>
			</table>
	    </td></tr>
	    <tr><td>
	     	<div id=formDiv name=formDiv >
				<input type="hidden" id="formScrollPos"   name="formScrollPos"  value="">
				<input type="hidden" id="browser"            name="browser"           value="">
				<input type="hidden" id="control"            name="control"           value="">
				<input type="hidden" id="event"              name="event"             value="">
				<input type="hidden" id="eventValue"         name="eventValue"        value="">
				<input type="submit" id="submitButton" style="display:none;" name="submit"  >
				<%			
		   		// render the HTML to the stream
		   		//
//				app.calculateHTML(context);
				app.writeHTML(context, out);
				%>
			</div>
	        </td></tr>
	      </table>
<%
	} // end synchronized(app)
}
catch( Exception exc)
{
	out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=login.jsp?errorMessage="+exc.getMessage()+"\"></head></html>");
	return;
}
finally
{
	// clear the current ClientContext from the thread local variable!!!
	//
	Context.setCurrent(null);
}
%>
<script>window.print();</script>
</body>
</html>
