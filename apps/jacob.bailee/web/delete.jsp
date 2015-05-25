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
 String doit   = request.getParameter("doit");
 String reason = request.getParameter("reason");
 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
  <link type="text/css" rel="stylesheet" href="css/main.css" />
  <title>Mark for Delete</title>
</head>

<body>
<form method="POST" action="delete.jsp" style="width:100%;height:100%">

<%
if(doit==null || reason==null)
{
	%>
	<table style="width:100%;height:100%" cellspacing="0" cellpadding="0" >
	<tr>
	  <td colspan="3" valign="top">
	  <table style="width:100%;height:100%" cellspacing="0" cellpadding="0" >
	  <tr>
	    <td colspan="2">
	    <input type="hidden" name="doit"  value="true">
	    <input type="hidden" name="pkey"  value="<%=pkey%>">
	    </td>
	  </tr>
	  <tr class="hint">
	  <td width="1%" style="padding:10px;" valign="top">
	  Note:
	  </td>
	  <td style="padding:10px;" valign="top">
	  You can mark the document as "deleteable". The document will be delete in the next 2 weeks. In this
	  period any user can be contrary to your action and can remove the delete flag. 
	  </td>
	  </tr>
	  <tr>
	  <td colspan="2" class="label">
	  Reason:
	  </td>
	  </tr>
	  <tr height="100%" style="white-space:nowrap">
	    <td  colspan="2"  style="padding:10px;"><textarea name="reason" style="width:100%;height:100%"></textarea></td>
	  </tr>
	  </table>
	</td>
	</tr>
	<tr height="1" class="buttonbar">
	  <td width="100%">
	  </td>
	  <td style="padding:10px;">
	    <button class="button" onclick="window.close()">Abort</button>
	  </td>
	  <td style="padding:10px;">
	    <input class="button" type="submit" value="Commit">
	  </td>
	</tr>
	</table>
	</form>
	<%
}
else	
{
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
			document.setValue(trans,"request_for_delete_date","now");
			document.setValue(trans,"request_for_delete_comment",reason);
			trans.commit();
		}
	}
	finally
	{
		trans.close();
	}
	
	%>
	<table style="width:100%;height:100%" cellspacing="0" cellpadding="0" >
	<tr>
	  <td colspan="2" valign="top">
	  <table style="width:100%;height:100%" cellspacing="0" cellpadding="0" >
	  <tr class="hint">
	  <td width="1%" style="padding:10px;" valign="top">
	  Note:
	  </td>
	  <td style="padding:10px;" valign="top">
	  <big><i>The document will be deleted in the next 2 weeks.</i></big> In this
	  period any user can be contrary to your action and can remove the delete flag. 
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
	<%
}	
%>
</body>
</html>