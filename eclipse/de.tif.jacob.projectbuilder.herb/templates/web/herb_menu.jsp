<%@ page session="false"%>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>

<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
 	<link type="text/css" rel="stylesheet" href="css/main.css" />
</head>

<%
 String name = request.getParameter("herb.name");
 name = name==null?"":name;

 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
 IDataTable productsTable = context.getDataTable("herb");
 productsTable.qbeSetValue("name",name);
 productsTable.search();
%>
  
<body style="border-right:1px solid #DDD;height:100%">

    <div id="list" style=" width:100%;height:100%;overflow: auto;"> 
	    <ul> 
	    <!-- =============================================================
	          render the search form 
	         ============================================================= -->
	    <li>
	        <form action="herb_menu.jsp" >
	        <table width=100%>
	         <tr>
	           <td width="60%" ><input style="width:70;" name="herb.name" value=""></td>
	           <td width="30%" ><input type="submit" value="search">
	         </tr>
	         </table>
	         </form>
	    </li>


	    <!-- =============================================================
	          Get and render the XML document with Apache/Jakarta XML-Taglib
	          to a well formed HTML. 
	         ============================================================= -->
	      <%for(int i=0; i<productsTable.recordCount();i++){ 
	        IDataTableRecord record= productsTable.getRecord(i);
	      %>
			  <li>
	      <a href="herb_content.jsp?pkey=<%=record.getSaveStringValue("pkey") %>" select="pkey" target="herb_content"><%=record.getSaveStringValue("name") %></a>
	      </li>
	      <%} %>
			</xtags:forEach>
			
	    </ul> 
    </div> 

</body>
</html>
