<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>
{"images":[
<%
    response.reset();

    String browserId  = request.getParameter("browser");
    de.tif.jacob.screen.impl.html.ClientSession     jacobSession = (de.tif.jacob.screen.impl.html.ClientSession)de.tif.jacob.screen.impl.HTTPClientSession.get(request);
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

   synchronized(app)
   {
      de.tif.jacob.screen.impl.html.ClientContext context = new de.tif.jacob.screen.impl.html.ClientContext(jacobSession.getUser(),out, app, browserId);
      de.tif.jacob.screen.impl.html.ClientContext.setCurrent(context);
  
      de.tif.jacob.core.data.IDataAccessor a = context.getDataAccessor().newAccessor();
	  de.tif.jacob.core.data.IDataTable pptTable = a.getTable("ppt");
	  pptTable.search();
      for(int i=0; i<pptTable.recordCount(); i++)
      {
         IDataTableRecord pptRecord = pptTable.getRecord(i);
         String name = pptRecord.getSaveStringValue("name");
         String pkey = pptRecord.getSaveStringValue("pkey");
         long changedate = pptRecord.getDateValue("changedate").getTime();
         if((i+1)<pptTable.recordCount())
           out.println("{\"name\":\""+name+"\",\"pkey\":"+pkey+",\"lastmod\":"+changedate+",\"url\":\"ppt_image.jsp?browser="+browserId+"&pkey="+pkey+"\"},");
         else
           out.println("{\"name\":\""+name+"\",\"pkey\":"+pkey+",\"lastmod\":"+changedate+",\"url\":\"ppt_image.jsp?browser="+browserId+"&pkey="+pkey+"\"}");
      }
   }
%>
]}