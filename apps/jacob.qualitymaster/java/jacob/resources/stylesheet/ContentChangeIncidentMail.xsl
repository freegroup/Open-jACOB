<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" encoding="ISO-8859-1"/>
	<xsl:template match="/msg">
	<xsl:text>Ihre Meldung </xsl:text><xsl:value-of select="./incident/pkey"/><xsl:text disable-output-escaping="yes"> wurde inhaltlich bearbeitet&#10;</xsl:text>
	<h2>Die Meldung <xsl:value-of select="./incident/pkey"/> wurde inhaltlich bearbeitet</h2>
	<hr/>
    Sie sind als Ersteller der Meldung eingetragen.
    <br/>
    <br/>
	<table border="0">
	<tr>
		<td><font style="font-weight:bold;">Nr.:</font></td>
		<td><a href="{./entrypoint/url}"><font style="font-weight:bold;color:#154288"><xsl:value-of select="./incident/pkey"/></font></a></td>
	</tr>
	<tr>
		<td><font style="font-weight:bold;">Betreff:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./incident/subject"/></font></td>
	</tr>
	<tr>
		<td><font style="font-weight:bold;">Status:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./incident/state"/></font></td>
	</tr>
	<tr>
		<td><font style="font-weight:bold;">Priorität:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./incident/priority"/></font></td>
	</tr>
	<tr>
		<td><font style="font-weight:bold;">Ersteller:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./incident/customer/fullname"/></font></td>
	</tr>
    <tr>
		<td><font style="font-weight:bold;">Firma:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./incident/organization/name"/></font></td>
	</tr>
    <tr>
		<td><font style="font-weight:bold;">Kategorie:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./incident/incidentCategory/name"/></font></td>
	</tr>
    <tr>
		<td><font style="font-weight:bold;">Meilenstein:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./incident/milestone/name"/></font></td>
	</tr>
	<tr>
		<td valign="top"><font style="font-weight:bold;">Beschreibung:</font></td>
		<td><font style="color:#154288;"><xsl:call-template name="replace"><xsl:with-param name="string" select="./incident/description"/></xsl:call-template></font></td>
	</tr>
	<tr>
		<td valign="top"><font style="font-weight:bold;">Kundeninfo:</font></td>
		<td><font style="color:#154288;"><xsl:call-template name="replace"><xsl:with-param name="string" select="./incident/cust_solution_info"/></xsl:call-template></font></td>
	</tr>
	</table>
	</xsl:template>
	
	<!-- Replace LF by HTML BR -->
	<xsl:template name="replace">
		<xsl:param name="string"/>
		<xsl:choose>
			<xsl:when test="contains($string,'&#10;')">
				<xsl:value-of select="substring-before($string,'&#10;')"/>
				<br/>
				<xsl:call-template name="replace">
					<xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$string"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>
