<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>Ihre Meldung </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> wird voraussichtlich bis zum </xsl:text><xsl:value-of select="./doc/data/call/date_sl_overdue" /><xsl:text> fertig gestellt.</xsl:text>
</xsl:template>
</xsl:stylesheet>
