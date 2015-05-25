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

var userList=getObj('searchResult');

while(getObj('searchResult').options.length>0)
{
    userList.options.remove(0);
}
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="java.util.*" %>
<% 
try
{
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

	String guid = request.getParameter("guid");
	String searchFullName = request.getParameter("searchFullName");
	String messageType    = request.getParameter("messageType");
    if(!UserManagement.isLoggedInUser(request,response))
        return;

    ClientSession jacobSession = (ClientSession)HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        IUserFactory userFactory =UserManagement.getUserFactory(jacobSession.getApplicationDefinition());
        
        try
        {
            List users= userFactory.findByFullName(searchFullName);
            for(int i=0;i<users.size();i++)
            {
                IUser user = (IUser)users.get(i);
                String value=null;
                if(Property.YAN_PROTOCOL_EMAIL.getName().equals(messageType) && user.getEMail()!=null)
                    value=user.getEMail();
                else if(Property.YAN_PROTOCOL_SMS.getName().equals(messageType) && user.getCellPhone()!=null)
                    value=user.getCellPhone();
                else if(Property.YAN_PROTOCOL_FAX.getName().equals(messageType) && user.getFax()!=null)
                    value=user.getFax();
                else if(Property.YAN_PROTOCOL_ALERT.getName().equals(messageType))
                    value=user.getLoginId();
                if(value!=null)
                {
                    %>
                        var option = document.createElement("OPTION");
                        userList.options.add(option);
                        option.innerText = "<%=user.getFullName()%> <<%=value%>>";
                        option.value = "<%=value%>";
                    <%
                }
            }
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
   	}
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
getObj('accept').disabled=userList.options.length==0;
