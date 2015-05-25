<%@ taglib uri="http://jakarta.apache.org/taglibs/xtags-1.0" prefix="xtags" %>

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
</head>
<%
 String pkey = request.getParameter("pkey");
 String url  = "http://localhost:8080/jacob/cmdenter?entry=GenericSearch&app=qualitymaster&user=achim&pwd=ch3ch2oh&anchorTableAlias=request&request.pkey="+pkey;
%>
<body>
<button onClick="window.location.href='index.jsp'">Back</button>&nbsp;
<button onClick="window.location.href='http://localhost:8080/jacob/cmdenter?entry=SetStatusToQACustomer&app=qualitymaster&user=achim&pwd=ch3ch2oh&pkey=<%=pkey%>'">Set Done</button>

<xtags:parse url="<%= url %>"/>

<table border=0 width="100%">
<tr>
	<td><b>Id</b></td>
	<td></td>
</tr>
<tr>
    <td style="background:lightgrey" colspan=2>
	<small><xtags:valueOf select="//request/pkey"/></small>
    </td>
<tr>
	<td><b>Status</b></td>
	<td></td>
</tr>
<tr>
    <td style="background:lightgrey" colspan=2>
	<small><xtags:valueOf select="//request/requeststatus"/></small>
    </td>
<tr>
<tr>
	<td><b>Beschreibung</b></td>
	<td></td>
</tr>
<tr>
    <td style="background:lightgrey" colspan=2>
	<small><xtags:valueOf select="//request/description"/></small>
    </td>
<tr>
<tr>
	<td><b>Langbeschreibung</b>g</td>
	<td></td>
</tr>
<tr>
    <td style="background:lightgrey" colspan=2>
	<small><xtags:valueOf select="//request/descriptiontext"/></small>
    </td>
<tr>
</table>
</body>
</html>
