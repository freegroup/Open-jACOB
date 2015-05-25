<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%
    String browserId  = request.getParameter("browser");

    de.tif.jacob.screen.impl.html.ClientSession jacobSession = (de.tif.jacob.screen.impl.html.ClientSession)de.tif.jacob.screen.impl.HTTPClientSession.get(request);
		de.tif.jacob.screen.impl.html.Application app = null;
		if(jacobSession!=null)
		    app=(de.tif.jacob.screen.impl.html.Application)jacobSession.getApplication(browserId);
		
		// no valid application in the session found...cleanup all data
		// and redirect to the login screen
		//
		if(jacobSession==null || app==null)
		{
		    de.tif.jacob.security.UserManagement.logOutUser(request,response);
		    out.clearBuffer();
		    return;
		}
		
		de.tif.jacob.security.IUser user = jacobSession.getUser();
    de.tif.jacob.core.Context context = new de.tif.jacob.core.JspContext(app.getApplicationDefinition(),user);
    de.tif.jacob.core.Context.setCurrent(context);

    IDataAccessor a = context.getDataAccessor().newAccessor();
    IDataTable partTable = a.getTable("public_part");
    partTable.search();
    for(int i=0;i<partTable.recordCount();i++)
    {
      IDataTableRecord currentRecord = partTable.getRecord(i);
      out.println(currentRecord.getSaveStringValue("code"));
    }
%>
