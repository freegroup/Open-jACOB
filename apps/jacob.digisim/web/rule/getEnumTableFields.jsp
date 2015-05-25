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
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);

   synchronized(app)
   {
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
        Iterator aliasIter = app.getApplicationDefinition().getTableAliases().iterator();
        List values = new ArrayList();
        while(aliasIter.hasNext()) 
        {
            ITableAlias alias = (ITableAlias)aliasIter.next();
            Iterator fieldIter = alias.getTableDefinition().getTableFields().iterator();
            while (fieldIter.hasNext()) 
            {
                ITableField field = (ITableField) fieldIter.next();
                FieldType type = field.getType();
                if(type instanceof EnumerationFieldType)
                {
                    EnumerationFieldType enumType = (EnumerationFieldType)type;
                    values.add(alias.getName()+"."+field.getName());
/*
                    for(int i=0;i<enumType.enumeratedValueCount();i++)
                    {
                        out.println("\t\t"+enumType.getEnumeratedValue(i));
                    }
*/
                }
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