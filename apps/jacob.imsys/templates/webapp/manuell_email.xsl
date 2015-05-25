<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />

	<xsl:template match="/">
           <xsl:value-of select="./doc/data/subject" />
<xsl:text>
</xsl:text>
           <xsl:value-of select="./doc/data/body" /><xsl:text></xsl:text>
	</xsl:template>	
</xsl:stylesheet>

