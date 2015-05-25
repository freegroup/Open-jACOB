
<%@page import="de.tif.jacob.util.clazz.ClassUtil"%><%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="de.tif.jacob.screen.impl.dialogs.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
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

{"tables":
   [
<%
    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("session timed out");
        return;
    }
    Map<String,String> typeMapping  = new HashMap<String,String>();
    String browserId  = request.getParameter("browser");
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

   ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
   Context.setCurrent(context);

   synchronized(app)
   {
       Iterator aliasIter = app.getApplicationDefinition().getTableAliases().iterator();
       boolean firstAlias = true;
       while(aliasIter.hasNext()) 
       {
           ITableAlias alias = (ITableAlias)aliasIter.next();

           if(firstAlias)
              out.print("{");
           else
              out.print(",{");
           out.print("\"name\":\""+alias.getName()+"\",");
           out.print("\"label\":\""+I18N(app,context,alias.getName().toUpperCase())+"\",");
           out.print("\"columns\":[");
           boolean firstField= true;
           Iterator fieldIter = alias.getTableDefinition().getTableFields().iterator();
           while (fieldIter.hasNext()) 
           {
              if(firstField)
                 out.print("{");
              else
                 out.print(",{");
              ITableField field = (ITableField) fieldIter.next();

              out.print("\"name\":\""+field.getName()+"\",");
              out.print("\"type\":\""+ClassUtil.getShortClassName(field.getType().getClass())+"\",");
              if(field.getType() instanceof TextFieldType)
              {
            	  TextFieldType type = (TextFieldType)field.getType();
                  out.print("\"length\":\""+type.getMaxLength()+"\",");
              }
              if(field.getLabel().startsWith("%"))
	              out.print("\"label\":\""+I18N(app,context,field.getLabel().substring(1))+"\"");
              else
    	          out.print("\"label\":\""+I18N(app,context,field.getLabel())+"\"");

              firstField=false;
              out.println("}");
           }
           out.print("],");
           
           out.print("\"unique\":[");
           boolean firstUnique= true;
           Iterator keyIter = alias.getTableDefinition().getKeys();
           while (keyIter.hasNext()) 
           {
              IKey key = (IKey) keyIter.next();
              if(!key.getType().isUnique())
            	  continue;
              if(firstUnique)
                 out.print("{");
              else
                 out.print(",{");

              out.print("\"name\":\""+key.getName()+"\",");
              out.print("\"columns\":[");
              boolean firstColumn= true;
              Iterator columnIter = key.getTableFields().iterator();
              while (columnIter.hasNext()) 
              {
                 if(firstColumn)
                    out.print("{");
                 else
                    out.print(",{");
                 ITableField field = (ITableField) columnIter.next();

                 out.print("\"name\":\""+field.getName()+"\"");

                 firstColumn=false;
                 out.println("}");
              }
              out.print("]");

              firstUnique=false;
              out.println("}");
           }
           out.print("]");

           firstAlias = false;
           out.println("}");
       }
   }
%>
   ]
}


<%!
String I18N(IApplication app, Context context, String key)
{
// The hard way! The one and only way in the moment. We must change the jACOB application classloader
//
try
{
    Object i18n  = ClassProvider.createInstance(app.getApplicationDefinition(),"jacob.resources.JspI18N");
    Method method = i18n.getClass().getMethod("get",new Class[]{Context.class, String.class});
    return (String)method.invoke(i18n,new Object[]{context, key});
}
catch(Exception exc)
{
// I18N Not part of the localisation file  exc.printStackTrace();
}
return key;
}
%>