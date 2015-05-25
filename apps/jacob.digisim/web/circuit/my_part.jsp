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
    if(!UserManagement.isLoggedInUser(request,response))
    {
        return;
    }
    String browserId  = request.getParameter("browser");

    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    Application app = null;
    if(jacobSession!=null)
        app=(Application)jacobSession.getApplication(browserId);

    // no valid application in the session found...cleanup all data
    // and redirect to the login screen
    //
    if(jacobSession==null || app==null)
    {
        UserManagement.logOutUser(request,response);
        out.clearBuffer();
        return;
    }

   IUser user = jacobSession.getUser();
   synchronized(app)
   {
      Set<String> insertedElements = new TreeSet<String>();
      de.tif.jacob.core.JspContext context = new de.tif.jacob.core.JspContext(app.getApplicationDefinition(),user);
      Context.setCurrent(context);
		  IDataAccessor a = context.getDataAccessor().newAccessor();
		  IDataTable partTable = a.getTable("my_part");
		  partTable.search();
		  for(int i=0;i<partTable.recordCount();i++)
		  {
			    IDataTableRecord currentRecord = partTable.getRecord(i);
			    insertedElements.add(currentRecord.getSaveStringValue("name"));
		      out.println(currentRecord.getSaveStringValue("code"));
			}      
		  partTable = a.getTable("public_part");
		  partTable.search();
		  for(int i=0;i<partTable.recordCount();i++)
		  {
			    IDataTableRecord currentRecord = partTable.getRecord(i);
			    String name = currentRecord.getSaveStringValue("name");
			    if(!insertedElements.contains(name))
			    {
			      out.println(currentRecord.getSaveStringValue("code"));
			    }
			}      
  }
%>
