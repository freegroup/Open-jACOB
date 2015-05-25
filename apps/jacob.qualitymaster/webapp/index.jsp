<%@ taglib uri="http://jakarta.apache.org/taglibs/xtags-1.0" prefix="xtags" %>

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
</head>

<%
 String url="http://localhost:8080/jacob/cmdenter?entry=GenericSearch&app=qualitymaster&user=andherz&pwd=anshez&anchorTableAlias=request";
%>

<body>
	    <!-- =============================================================
	          Get and render the XML document with Apache/Jakarta XML-Taglib
	          to a well formed HTML. 
	         ============================================================= -->
		  <xtags:parse url="<%= url %>"/>
			<xtags:forEach select="//request">
			  <xtags:valueOf select="pkey"/>
	      <xtags:element name="a">
	          <xtags:attribute name="href">content.jsp?pkey=<xtags:valueOf select="pkey"/></xtags:attribute>
	          <small><xtags:valueOf select="description"/></small>
	      </xtags:element>
	      <br>
			</xtags:forEach>
			
</body>
</html>
