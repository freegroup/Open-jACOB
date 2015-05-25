<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.core.definition.fieldtypes.*" %>
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
        out.write("session timed out");
        return;
    }
    String browserId  = request.getParameter("browser");
    String fieldName  = request.getParameter("field");

    String aliasName  = fieldName.split("[.]")[0];
    String columnName = fieldName.split("[.]")[1];

    System.out.println("Alias:"+aliasName);
    System.out.println("Column:"+columnName);

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
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
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);

   synchronized(app)
   {
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
        ITableAlias alias = app.getApplicationDefinition().getTableAlias(aliasName);
        ITableField field = alias.getTableDefinition().getTableField(columnName);
        FieldType type = field.getType();
        List values = new ArrayList();
        if(type instanceof EnumerationFieldType)
        {
            EnumerationFieldType enumType = (EnumerationFieldType)type;

            for(int i=0;i<enumType.enumeratedValueCount();i++)
            {
                values.add(enumType.getEnumeratedValue(i));
            }
        }
        out.print("[");
        Iterator iter = values.iterator();
        if(iter.hasNext())
          out.print("\""+iter.next()+"\"");
        while(iter.hasNext())
        {
          String value = (String)iter.next();
          out.print(",\""+value+"\"");
        }
        out.println("]");
   }
%>