<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
	Author: Andreas Herz
	File:  RequestMail.xsl
	Date: 18.05.2004
	Purpose: Stylesheet for 'Request has been done' for the QualityMaster
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" encoding="ISO-8859-1"/>
	<xsl:template match="/msg">
	<xsl:text>Aufgabe </xsl:text><xsl:value-of select="./request/pkey"/><xsl:text disable-output-escaping="yes"> wurde auf 'Done' gesetzt&#10;</xsl:text>
	<h2>Die von ihnen erstellte Aufgabe <xsl:value-of select="./request/pkey"/> wurde heute auf den Status 'Done' gesetzt</h2>
	<hr/>
	<table border="0">
	<tr>
		<td>Betreff:</td>    <td><xsl:value-of select="./request/subject"/></td>
	</tr>
	<tr>
		<td>Priorität:</td>    <td><xsl:value-of select="./request/priority"/></td>
	</tr>
	<tr>
		<td>Ersteller:</td>    <td><xsl:value-of select="./request/creater/fullname"/></td>
	</tr>
	<tr>
		<td>Eigner:</td>    <td><xsl:value-of select="./request/owner/fullname"/></td>
	</tr>
	<tr>
		<td>siehe unter:</td>    <td><a href="{./entrypoint/url}" ><xsl:value-of select="./entrypoint/name"/></a></td>
	</tr>
	<tr>
		<td>Beschreibung:</td>    <td><xsl:value-of select="./request/description"/></td>
	</tr>
	</table>
	</xsl:template>
</xsl:stylesheet>
