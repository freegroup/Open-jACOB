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
       return;

    String browserId  = request.getParameter("browser");
    String name       = request.getParameter("name");

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
    jacobSession.sendKeepAlive(browserId);

    IUser user = jacobSession.getUser();
   synchronized(app)
   {
      de.tif.jacob.core.JspContext context = new de.tif.jacob.core.JspContext(app.getApplicationDefinition(),user);
      Context.setCurrent(context);

      IDataAccessor a = context.getDataAccessor().newAccessor();
      // Try to find the documention.
      // FIRST: The current user is the owner of the object.
      //        We must load the docuemtnation from the current object version of the user
      //
      IDataTable myPartTable = a.getTable("my_part");
      myPartTable.qbeSetValue("name",name);
      if(myPartTable.search()==1)
      {
         IDataTableRecord currentRecord = myPartTable.getSelectedRecord();
         out.println(currentRecord.getSaveStringValue("comment"));
      }
      else
      {
        a = context.getDataAccessor().newAccessor();
        IDataTable partTable = a.getTable("part");
        partTable.qbeSetValue("name",name);
        partTable.qbeSetValue("state","released");
        if(partTable.search()==1)
        {
           IDataTableRecord currentRecord = partTable.getSelectedRecord();
           out.println(currentRecord.getSaveStringValue("comment"));
        }
      }
   }
%>
