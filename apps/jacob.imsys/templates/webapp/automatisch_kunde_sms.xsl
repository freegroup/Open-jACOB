<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" indent="yes"/>
	<xsl:template match="/">
                        <xsl:text>Fertigmeldung: </xsl:text>
                        <xsl:value-of select="./doc/data/call/pkey"     /><xsl:text> </xsl:text>
                        <xsl:value-of select="./doc/data/call/problem"  /><xsl:text> </xsl:text>
                        <xsl:text>ist fertiggestellt. FWT-SMC Tel.166</xsl:text>

	</xsl:template>
</xsl:stylesheet>

