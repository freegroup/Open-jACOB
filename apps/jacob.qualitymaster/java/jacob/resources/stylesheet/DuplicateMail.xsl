<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" encoding="ISO-8859-1"/>
	<xsl:template match="/msg">
	<xsl:text>Aufgabe </xsl:text><xsl:value-of select="./request/pkey"/><xsl:text disable-output-escaping="yes"> wurde auf 'Duplicate' gesetzt&#10;</xsl:text>
	<h2>Die von ihnen erstellte Aufgabe <xsl:value-of select="./request/pkey"/> wurde heute auf den Status 'Duplicate' gesetzt</h2>
	<hr/>
	<table border="0">
	<tr>
		<td><font style="font-weight:bold;">Nr.:</font></td>
		<td><a href="{./entrypoint/url}"><font style="font-weight:bold;color:#154288"><xsl:value-of select="./request/pkey"/></font></a></td>
	</tr>
	<tr>
		<td><font style="font-weight:bold;">Betreff:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./request/subject"/></font></td>
	</tr>
	<tr>
		<td><font style="font-weight:bold;">Priorität:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./request/priority"/></font></td>
	</tr>
	<tr>
		<td><font style="font-weight:bold;">Ersteller:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./request/creater/fullname"/></font></td>
	</tr>
	<tr>
		<td><font style="font-weight:bold;">Eigner:</font></td>
		<td><font style="color:#154288;"><xsl:value-of select="./request/owner/fullname"/></font></td>
	</tr>
	<tr>
		<td valign="top"><font style="font-weight:bold;">Beschreibung:</font></td>
		<td><font style="color:#154288;"><xsl:call-template name="replace"><xsl:with-param name="string" select="./request/description"/></xsl:call-template></font></td>
	</tr>
	<tr>
		<td valign="top"><font style="font-weight:bold;">Kundeninfo:</font></td>
		<td><font style="color:#154288;"><xsl:call-template name="replace"><xsl:with-param name="string" select="./request/cust_solution_info"/></xsl:call-template></font></td>
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
