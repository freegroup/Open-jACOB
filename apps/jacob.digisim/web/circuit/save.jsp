<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%
    if(!UserManagement.isLoggedInUser(request,response))
    {
        return;
    }
    String browserId  = request.getParameter("browser");
    String pkey       = request.getParameter("pkey");
    String name       = request.getParameter("name");
    String xml        = org.apache.commons.io.IOUtils.toString(request.getInputStream());
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

    de.tif.jacob.security.IUser user = jacobSession.getUser();
   synchronized(app)
   {
      de.tif.jacob.core.JspContext context = new de.tif.jacob.core.JspContext(app.getApplicationDefinition(),user);
      Context.setCurrent(context);

      IDataAccessor a = context.getDataAccessor().newAccessor();
      IDataTransaction transaction = a.newTransaction();
      IDataTable ruleTable = a.getTable("circuit");
      // Save an existing circuit
      //
      if(pkey!=null && pkey.length()>0)
      {
				ruleTable.qbeSetValue("pkey",pkey);
				if(ruleTable.search()==1)
				{
          IDataTableRecord currentRecord = ruleTable.getSelectedRecord();
          currentRecord.setValue(transaction, "xml", xml);
				}
      }
      // create a new circuit in the database
      //
      else
      {
           IDataTableRecord currentRecord = ruleTable.newRecord(transaction);
           currentRecord.setValue(transaction, "xml", xml);
           currentRecord.setValue(transaction, "title", name);
           out.println("pkey="+currentRecord.getSaveStringValue("pkey")+";");
      }
      transaction.commit();
  }
%>
