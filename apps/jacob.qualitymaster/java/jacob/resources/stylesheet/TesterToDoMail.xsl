<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
	Author: Andreas Herz
	File:  RequestMail.xsl
	Date: 18.05.2004
	Purpose: Stylesheet for 'New Request Notification' for the QualityMaster
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" encoding="ISO-8859-1"/>
	<xsl:template match="/msg">
	<xsl:text>Aufgabe </xsl:text><xsl:value-of select="./request/pkey"/><xsl:text disable-output-escaping="yes"> kann getestet werden&#10;</xsl:text>
	<h2>Aufgabe <xsl:value-of select="./request/pkey"/> wurde auf [QA] geschaltet.</h2>
	<hr/>
    Sie k�nnen mit dem Testen der unten aufgef�hrten Aufgabe beginnen.<br/>
    Bitte vergessen Sie nicht den
    den Status der Aufgabe nach erfolgreichen/erfolglosen Test anzupassen (done, in process,...)
    <br/>
    <br/>
	<table border="0">
	<tr>
		<td>Betreff:</td>    <td><xsl:value-of select="./request/subject"/></td>
	</tr>
	<tr>
		<td>Priorit�t:</td>    <td><xsl:value-of select="./request/priority"/></td>
	</tr>
	<tr>
		<td>Ersteller:</td>    <td><xsl:value-of select="./request/creater/fullname"/></td>
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
