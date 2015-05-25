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
 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
	// search the dokument and remove the delete flag.
	IDataAccessor accessor = context.getDataAccessor();
	IDataTransaction trans = accessor.newTransaction();
	IDataTable documentTable = context.getDataTable("document");
	try
	{
		documentTable.qbeSetValue("pkey",pkey);
		if(documentTable.search()==1)
		{
			IDataTableRecord document = documentTable.getSelectedRecord();
			document.setValue(trans,"request_for_delete_date",null);
			document.setValue(trans,"request_for_delete_comment",null);
			trans.commit();
		}
	}
	finally
	{
		trans.close();
	}
%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
  <link type="text/css" rel="stylesheet" href="css/main.css" />
  <title>Remove Delete Flag</title>
</head>

<body>
	<table style="width:100%;height:100%" cellspacing="0" cellpadding="0" >
	<tr>
	  <td colspan="2" valign="top">
	  <table style="width:100%;height:100%" cellspacing="0" cellpadding="0" >
	  <tr class="hint">
	  <td width="1%" style="padding:10px;" valign="top">
	  Note:
	  </td>
	  <td style="padding:10px;" valign="top">
	  The delete flag has been remove from this document. 
	  </td>
	  </tr>
	  </table>
	</td>
	</tr>
	<tr height="1" class="buttonbar">
	  <td width="100%">
	  </td>
	  <td style="padding:10px;">
	    <button class="button" onclick="window.close()">Close</button>
	  </td>
	</tr>
	</table>
</body>
</html>