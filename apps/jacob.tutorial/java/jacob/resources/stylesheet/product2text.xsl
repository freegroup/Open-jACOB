<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" encoding="UTF-8"/>
	<xsl:template match="/data">
   		<xsl:text>Product Id:</xsl:text><xsl:value-of select="./product/pkey" /><xsl:text>&#13;&#10;</xsl:text>
   		<xsl:text>Name:</xsl:text><xsl:value-of select="./product/name" /><xsl:text>&#13;&#10;</xsl:text>
   		<xsl:text>Description:</xsl:text><xsl:value-of select="./product/description" /><xsl:text>&#13;&#10;</xsl:text>
	</xsl:template>
</xsl:stylesheet>
