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
<%@ page import="java.util.*" 
         import="de.tif.jacob.core.*" 
         import="de.tif.jacob.screen.impl.*" 
         import="de.tif.jacob.screen.impl.html.*" 
         import="de.tif.jacob.security.*" 
         import="de.tif.jacob.report.*" %><%

    String      thisApp      =  request.getContextPath();
    String      thisPage     =  request.getContextPath()+request.getServletPath();
    String      thisPath     =  thisPage.substring(0,thisPage.lastIndexOf("/")+1);

	String      browserId    = request.getParameter("browser");
	String      browserType  = request.getParameter("browserType");
	String      messageType  = request.getParameter("messageType");
	String      search       = request.getParameter("search");

    if(search==null)
        search="";
        
    ClientSession jacobSession = (ClientSession)HTTPClientSession.get(request);
	Application   app          = (Application)jacobSession.getApplication(browserId);

%>
<HTML>
<HEAD>
     <title><j:dialogTitle id="DIALOG_TITLE_SEARCH_USER"/></title>
	<META Http-Equiv="Cache-Control" Content="no-cache">
	<META Http-Equiv="Pragma" Content="no-cache">
	<META Http-Equiv="Expires" Content="0"> 
	
	<script>var userTheme = '<%=app.getTheme()%>';</script>
	<script type="text/javascript" src="../../javascript/<j:browserType/>_common.js" ></script>
  <link   type="text/css"        rel="stylesheet" href="../../themes/<%=app.getTheme()%>/custom.css" />
	<%-- for the serverPush!! don't remove this --%>
	
	<%-- ----------------------------------------------------------------
	  Server -> client notification
	  --------------------------------------------------------------- --%>
<%    if ("ie".equals(browserType)) 
	  { %>
	<script id="searchUserScript" type="text/javascript" src="" ></script>
<%    } %>
	<SCRIPT type="text/javascript" >
	    var searchUserTimer = null;
		function searchUserStart()
		{
		    if(searchUserTimer!=null)
		    {
		        window.clearTimeout(searchUserTimer);
		    }
		    searchUserTimer=window.setTimeout("searchUser()",500);
		} 
		
		function searchUser()	
		{
		   searchUserTimer=null;
		   searchUserIntern();
		}
		
		function searchUserIntern()	
		{
<%        if ("ie".equals(browserType))
          {
%>
			document.getElementById('searchUserScript').src="ie_searchByFullname.jsp?browser=<%=browserId%>&searchFullName="+getObj('searchFullName').value+"&messageType=<%=messageType%>";
<%        }
          else
          {
%>
		    var url = "ns_searchByFullname.jsp";
		    var _searchFullName = getObj('searchFullName').value;
		    var myAjax = new Ajax.Request(url,
		    {
		      parameters: 
		      {
		        browser: "<%=browserId%>",
		        searchFullName: _searchFullName,
		        messageType: "<%=messageType%>"
		      },
		      asynchronous: false,
		      onSuccess: function(transport)
		      {
		        var obj = getObj('searchResult');
		        obj.innerHTML=transport.responseText;
		        getObj('accept').disabled=obj.options.length==0;
		      }
		   });
<%        }
%>
		}
		
		
		function addRecipien()
		{
            var userList=getObj('searchResult');
            for(var i=0; i<userList.options.length;i++)
            {
                if(userList.options[i].selected)
                {
                    opener.addRecipien(userList.options[i].text,userList.options[i].value);
                }
            }
		}
	</SCRIPT>
	<%-- ----------------------------------------------------------- --%>

</HEAD>
<body class="dialog" >
    
	<table cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">
	<tr>
	    <td height="100%" valign="top">
	        <div width="100%" height="100%" class="dialog_content">
	            <table width="95%" height="100%" border="0">
                        <tr height=20>
                            <td width=100% colspan=2 ></td>
                        </tr>
                	<tr> 
                	    <td width=1><span  class="caption_normal_update" style="white-space:nowrap;" ><j:i18n id="LABEL_MESSAGING_FULLNAME_SEARCH"/></span></td>
                	    <td width=100%><input class="text_normal editable_inputfield" style="width:100%;" onkeypress="searchUserStart();" id="searchFullName" name="searchFullName" value="<%=search%>"><td>
                	</tr>
                	<tr height=100%>
                	    <td width=1></td>
                	    <td width=100%>
                	    <select multiple ondblclick="addRecipien();self.close();" id="searchResult" name="searchResult" size="10" style="height=100%;width:100%">
                	    </select>
                	    </td>
                	<tr>
	            </table>
	        </div>
	    </td>
	</tr>
	<tr>
	    <td  align="right" valign="bottom">
	        <div class="dialog_buttonbar">
	            <input type="button" id="accept" name="accept" disabled value="<j:i18n id="BUTTON_COMMON_ACCEPT"/>"  onClick="addRecipien();opener.setCaretToEnd(opener.getObj('search'));self.close();" class="button_emphasize_normal" >
	            <input type="button" value="<j:i18n id="BUTTON_COMMON_CLOSE"/>" onClick="opener.setCaretToEnd(opener.getObj('search'));self.close();" class="button_normal" >
	        </div>
	    </td>
	</tr>
	</table>
<body>
</html>

<script>
<% if (search.length() > 0)
   {
%>
    searchUser();
<%
   }
%>
// An das Ende des Eingabefeldes springen
//    
setCaretToEnd(getObj('searchFullName'));
</script>