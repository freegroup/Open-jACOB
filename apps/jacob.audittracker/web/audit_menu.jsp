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
 
 IDataTable objectgroupTable = context.getDataTable("objectgroup");
 objectgroupTable.search();
 
 IDataTable objectTable = context.getDataTable("object");
 objectTable.search();
%>
  
<body class="treeContainer" onload='collapseAll(["ol"]);toggle(document.getElementById("firstNode"))' style="border-right:1px solid #DDD;height:100%">

	      <%
	      renderRoot(out , objectTable, objectgroupTable);
	      %>

</body>
</html>

<%!
void renderRoot(Writer out, IDataTable objectTable, IDataTable objectgroupTable) throws Exception
{
  out.write("<ol id=\"root\">\n");
	for(int i=0; i<objectgroupTable.recordCount();i++)
	{
	   IDataTableRecord current= objectgroupTable.getRecord(i);
	   if(current.getValue("parentobjectgroup_key")==null)
	   {
		out.write("<li>");
		if(hasChildren(objectTable, objectgroupTable, current))
		{
		  out.write("<a id=\"firstNode\" href=\"#\" class=\"folder\" onclick=\"toggle(this)\"></a>");
          out.write("<a  class=\"node_group\" href=\"#\" onclick=\"toggle(this.previousSibling)\">"+current.getSaveStringValue("name")+"</a>");
  	   	}
		else
		{
		  out.write("<a id=\"firstNode\" href=\"#\" class=\"doc\" ></a>");
		  out.write("<a  class=\"node_group\" href=\"#\" >"+current.getSaveStringValue("name")+"</a>");
		}
        renderChildren(out,objectTable, objectgroupTable, current);
        out.write("</li>\n");
        break;
	   }
	}
	
  out.write("</ol>\n");
}

void renderChildren(Writer out, IDataTable objectTable, IDataTable objectgroupTable, IDataTableRecord record) throws Exception
{
	List<IDataTableRecord> result = new ArrayList<IDataTableRecord>();
	for(int i=0; i<objectgroupTable.recordCount();i++)
	{
	   IDataTableRecord current= objectgroupTable.getRecord(i);
	   if(current.getSaveStringValue("parentobjectgroup_key").equals(record.getSaveStringValue("pkey")))
	   {
	   	result.add(current);
	   }
	}
	if(hasChildren(objectTable, objectgroupTable, record))
	{
	  out.write("<ol>\n");
	  for(IDataTableRecord child: result)
	  {
	    out.write("<li>");
		if(hasChildren(objectTable, objectgroupTable, child))
		{
		  out.write("<a href=\"#\" class=\"folder\" onclick=\"toggle(this)\"></a>");
          out.write("<a class=\"node_group\"  onclick=\"toggle(this.previousSibling)\" href=\"#\" >"+child.getSaveStringValue("name")+"</a>");
		}
		else
		{
		  out.write("<div class=\"empty_folder\"></div>");
          out.write("<div class=\"empty_group\" >"+child.getSaveStringValue("name")+"</div>");
		}
	    renderChildren(out,objectTable, objectgroupTable, child);
	    out.write("</li>\n");
	  }
      // Alle Objekte/Produkte welche in dieser Gruppe sind rausrendern
      //
      for(int i=0; i<objectTable.recordCount();i++)
      {
         IDataTableRecord current= objectTable.getRecord(i);
         System.out.println(current.getSaveStringValue("objectgroup_key")+"=="+record.getSaveStringValue("pkey"));
         if(current.getSaveStringValue("objectgroup_key").equals(record.getSaveStringValue("pkey")))
         {
      		out.write("<li>");
            out.write("<a href=\"#\" class=\"doc\" ></a>");
      	    out.write("<a class=\"node_object\"  href=\"audit_content.jsp?pkey="+current.getSaveStringValue("pkey")+"\" target=\"audit_content\">"+current.getSaveStringValue("name")+"</a>");
            out.write("</li>\n");
         }
      }
	  out.write("</ol>\n");
	}
}

/**
 * Ein Knoten hat Kinder wenn es eine Untergruppe gibt oder ein Objekt/Produkt an diesem
 * hängt
 */
boolean hasChildren(IDataTable objectTable, IDataTable objectgroupTable, IDataTableRecord record) throws Exception
{
	for(int i=0; i<objectgroupTable.recordCount();i++)
	{
	   IDataTableRecord current= objectgroupTable.getRecord(i);
	   if(current.getSaveStringValue("parentobjectgroup_key").equals(record.getSaveStringValue("pkey")))
	   {
			return true;
	   }
	}

	for(int i=0; i<objectTable.recordCount();i++)
	{
	   IDataTableRecord current= objectTable.getRecord(i);
	   if(current.getSaveStringValue("objectgroup_key").equals(record.getSaveStringValue("pkey")))
	   {
			return true;
	   }
	}
	return false;
}
%>