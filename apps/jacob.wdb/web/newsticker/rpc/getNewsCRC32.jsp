<%@page contentType="text/plain" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.util.json.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.zip.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%
   IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
   Context context = new de.tif.jacob.core.JspContext(applicationDef);
   Context.setCurrent(context);

   IDataAccessor a = context.getDataAccessor().newAccessor();
   try
   {
      StringBuffer content = new StringBuffer("");
      IDataBrowser newsBrowser = a.getBrowser("active_newsBrowser");
      newsBrowser.search(IRelationSet.LOCAL_NAME);
      for(int i=0; i<newsBrowser.recordCount(); i++)
      {
        IDataBrowserRecord record = newsBrowser.getRecord(i);
        content.append(record.toString());
      }
      Checksum checksum = new CRC32();
      byte bytes[] = content.toString().getBytes();
      checksum.update(bytes,0,bytes.length);
      out.println(checksum.getValue());
  }
  catch(Exception exc)
  {
    exc.printStackTrace();
  }
  Context.setCurrent(null);
%>

