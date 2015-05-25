<%@ page session="false"%>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="java.util.*" %>

<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
 	<link type="text/css" rel="stylesheet" href="css/main.css" />
  <script type="text/javascript" src="common.js" ></script>

 	
</head>

<%

 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
 IDataTable processTable = context.getDataTable("process");
 processTable.search();
%>
  
<body class="treeContainer" onload='collapseAll(["ol"]);toggle(document.getElementById("firstNode"))' style="border-right:1px solid #DDD;height:100%">

	      <%
	      renderRoot(out , processTable);
	      %>

</body>
</html>

<%!
void renderRoot(Writer out, IDataTable processTable) throws Exception
{
  out.write("<ol id=\"root\">\n");
	for(int i=0; i<processTable.recordCount();i++)
	{
	   IDataTableRecord current= processTable.getRecord(i);
	   if(current.getValue("parent_process_key")==null)
	   {
				out.write("<li>");
				if(hasChildren(processTable, current))
		      out.write("<a id=\"firstNode\" href=\"#\" class=\"folder\" onclick=\"toggle(this)\"></a>");
		    else
		      out.write("<a id=\"firstNode\" href=\"pms_content.jsp?pkey="+current.getSaveStringValue("pkey")+"\" target=\"pms_content\" class=\"doc\" ></a>");
	      out.write("<a href=\"pms_content.jsp?pkey="+current.getSaveStringValue("pkey")+"\" target=\"pms_content\">"+current.getSaveStringValue("title")+"</a>");
        renderChildren(out, processTable, current);
        out.write("</li>");
        break;
	   }
	}
  out.write("</ol>\n");
}

void renderChildren(Writer out, IDataTable processTable, IDataTableRecord record) throws Exception
{
	List<IDataTableRecord> result = new ArrayList<IDataTableRecord>();
	for(int i=0; i<processTable.recordCount();i++)
	{
	   IDataTableRecord current= processTable.getRecord(i);
	   if(current.getSaveStringValue("parent_process_key").equals(record.getSaveStringValue("pkey")))
	   {
	   	result.add(current);
	   }
	}
	if(result.size()>0)
	{
	  out.write("<ol>\n");
	  for(IDataTableRecord child: result)
	  {
	    out.write("<li>");
				if(hasChildren(processTable, child))
		      out.write("<a href=\"#\" class=\"folder\" onclick=\"toggle(this)\"></a>");
		    else
		      out.write("<a  href=\"pms_content.jsp?pkey="+child.getSaveStringValue("pkey")+"\" target=\"pms_content\" class=\"doc\"></a>");
	    out.write("<a href=\"pms_content.jsp?pkey="+child.getSaveStringValue("pkey")+"\" target=\"pms_content\">"+child.getSaveStringValue("title")+"</a>");
	    renderChildren(out,processTable, child);
	    out.write("</li>");
	  }
	  out.write("</ol>\n");
	}
}
boolean hasChildren(IDataTable processTable, IDataTableRecord record) throws Exception
{
	List<IDataTableRecord> result = new ArrayList<IDataTableRecord>();
	for(int i=0; i<processTable.recordCount();i++)
	{
	   IDataTableRecord current= processTable.getRecord(i);
	   if(current.getSaveStringValue("parent_process_key").equals(record.getSaveStringValue("pkey")))
	   {
			return true;
	   }
	}
	return false;
}
%>