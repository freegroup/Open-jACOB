<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
	Author: Andreas Herz / Mike Doering 
	File:  NewIncidentMail.xsl
	Date: 08.03.2006
	Purpose: Stylesheet for 'New Incident Notification' for the QualityMaster
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" encoding="ISO-8859-1"/>
	<xsl:template match="/msg">
	<xsl:text>Meldung </xsl:text><xsl:value-of select="./incidentEntry/pkey"/><xsl:text disable-output-escaping="yes"> wurde angelegt&#10;</xsl:text>
	<h2>Sie sind als Verantwortlicher für Meldung <xsl:value-of select="./incidentEntry/pkey"/> eingetragen</h2>
	<hr/>
	<table border="0">
	<tr>
		<td>Betreff:</td>    <td><xsl:value-of select="./incidentEntry/subject"/></td>
	</tr>
	<tr>
		<td>Priorität:</td>    <td><xsl:value-of select="./incidentEntry/priority"/></td>
	</tr>
	<tr>
		<td>Ersteller:</td>    <td><xsl:value-of select="./incidentEntry/customer/fullname"/></td>
	</tr>
    <tr>
		<td>Firma:</td>    <td><xsl:value-of select="./incidentEntry/organization/name"/></td>
	</tr>
	<tr>
		<td>siehe unter:</td>    <td><a href="{./entrypoint/url}" ><xsl:value-of select="./entrypoint/name"/></a></td>
	</tr>
	<tr>
		<td>Beschreibung:</td>    <td><xsl:value-of select="./incidentEntry/description"/></td>
	</tr>
	</table>
	</xsl:template>
</xsl:stylesheet>
