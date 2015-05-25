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
    String xml        = org.apache.commons.io.IOUtils.toString(request.getInputStream());
    ClientSession     jacobSession = (ClientSession)session.getAttribute(HTTPConstants.SESSION_KEY);
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
   if(jacobSession.getUser().hasRole("guest"))
    return;

   synchronized(app)
   {
      ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
      Context.setCurrent(context);

      IDataAccessor a = context.getDataAccessor().newAccessor();//Table("rule");
      IDataTable ruleTable = a.getTable("rule");
      ruleTable.qbeSetValue("pkey",pkey);
      if(ruleTable.search()==1)
      {
        IDataTableRecord currentRecord = ruleTable.getSelectedRecord();
        IDataTransaction transaction = a.newTransaction();
	DataDocumentValue doc = currentRecord.getDocumentValue("rule");
	currentRecord.setDocumentValue(transaction, "rule",DataDocumentValue.create(doc.getName(), xml.getBytes()));
        transaction.commit();  
      }
    System.out.println(xml+"\n\n\n\n\n\n");
  }
%>
