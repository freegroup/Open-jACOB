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
try
{
    if(!UserManagement.isLoggedInUser(request,response))
    {
        return;
    }
    String browserId  = request.getParameter("browser");
    String pkey       = request.getParameter("pkey");
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

   synchronized(app)
   {
      ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
      Context.setCurrent(context);

      IDataAccessor a = context.getDataAccessor().newAccessor();
      IDataTransaction transaction = a.newTransaction();

      try
      {
        IDataTable linkTable = a.getTable("ppt_to_composition");
        // alte Einträge löschen
        //
        linkTable.qbeSetValue("composition_key",pkey);
        linkTable.searchAndDelete(transaction);
  
        // neue Einträge anlegen
        //
        for(int i=0; true;i++)
        {
          String pptId = request.getParameter("ppt_"+i);
          if(pptId==null)
            break;
          IDataTableRecord linkRecord = linkTable.newRecord(transaction);
          linkRecord.setValue(transaction, "ppt_key",pptId);
          linkRecord.setValue(transaction, "composition_key",pkey);
          linkRecord.setValue(transaction, "index",i);
        }
        transaction.commit();
      }
      finally
      {
        transaction.close();
      }
  }
}
catch(Exception exc)
{
 exc.printStackTrace();
}
%>
