<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ page session="false"%>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>
<%
 String pkey   = request.getParameter("pkey");
 String tag = request.getParameter("tag");
 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
 
 // search the dokument and mark them as deleteable
 IDataAccessor accessor = context.getDataAccessor();
 IDataTransaction trans = accessor.newTransaction();
 IDataTable documentTable = context.getDataTable("document");
 try
 {
   documentTable.qbeSetValue("pkey",pkey);
   if(documentTable.search()==1)
   {
     IDataTableRecord document = documentTable.getSelectedRecord();
     document.setValue(trans,"tag",tag);
     trans.commit();
   }
 }
 finally
 {
   trans.close();
 }
   
%>


