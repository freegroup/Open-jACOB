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
String pkey   = request.getParameter("pkey");

 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
 IDataTable productsTable = context.getDataTable("herb");
 productsTable.qbeSetValue("pkey",pkey);
 productsTable.search();
 IDataTableRecord herb = productsTable.getSelectedRecord();
%>

<body>
<table border=0 width="100%">
<tr>
    <td  colspan=2 valign="top">
			<div class="name"><%=herb.getSaveStringValue("name") %></div>
			<div class="description"><%=herb.getSaveStringValue("description") %></div>
    </td>
</tr>
<tr>
    <td valign="top">
      <table border=0  width="100%">
         <tr><!-- Row 1 -->
            <td >
						<img src="images/dot.png" class="bar"><span class="section_header">Cultivation:</span><br>
            <div class="section_text"><%=herb.getSaveStringValue("cultivation")%></div>
           </td>
         </tr>
         <tr><!-- Row 2 -->
            <td>
						<img src="images/dot.png" class="bar"><span class="section_header">Usage:</span><br>
	        	<div class="section_text"><%=herb.getSaveStringValue("application") %></div>
	        	</td>
         </tr>
         <tr><!-- Row 3 -->
           <td>
						<img src="images/dot.png" class="bar"><span class="section_header">Taste:</span><br>
	        	<div class="section_text"><%=herb.getSaveStringValue("taste") %></div>
  	      	</td>
         </tr>
         <tr><!-- Row 4 -->
            <td>
						<img src="images/dot.png" class="bar"><span class="section_header">Season:</span><br>
						<div class="section_text"><%=herb.getSaveStringValue("season") %></div>
	        	</td>
         </tr>
         <tr><!-- Row 6 -->
            <td>
						<img src="images/dot.png" class="bar"><span class="section_header">Store:</span><br>
						<div class="section_text"><%=herb.getSaveStringValue("store") %></div>
	        	</td>
         </tr>
         <tr><!-- Row 7 -->
            <td>
						<img src="images/dot.png" class="bar"><span class="section_header">Related:</span><br>
	        	<div class="section_text"><%=herb.getSaveStringValue("related") %></div>
						</td>
         </tr>
       </table>
    </td>
    <td valign="top">
    <% if(herb.getDocumentValue("image")!=null && herb.getDocumentValue("image").getContent()!=null){%>
	    <img src="db_image.jsp?pkey=<%=herb.getSaveStringValue("pkey") %>" width="400px"/>
    <%} %>
    </td>
</tr>
</table>
</body>
</html>
