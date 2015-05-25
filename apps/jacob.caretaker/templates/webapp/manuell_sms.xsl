<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" indent="yes"/>
	<xsl:template match="/">
           <xsl:value-of select="./doc/data/body" />
	</xsl:template>	
</xsl:stylesheet>

