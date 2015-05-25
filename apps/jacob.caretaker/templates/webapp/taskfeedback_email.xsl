<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>Der SMC Auftrag </xsl:text><xsl:value-of select="./doc/data/task/pkey" /><xsl:text> wurde </xsl:text><xsl:value-of select="./doc/data/task/taskstatus" /><xsl:text>.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Auftragskoordinatorin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrter Auftragskoordinator,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>der SMC Auftrag </xsl:text><xsl:value-of select="./doc/data/task/pkey" /><xsl:text> wurde </xsl:text><xsl:value-of select="./doc/data/task/taskstatus" /><xsl:text>.</xsl:text>
 
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Mit freundlichen Grüßen,&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr FWT Team&#13;&#10;Tel:   166&#13;&#10;eMail: FWT-Servicecenter@daimlerchrysler.com</xsl:text>
</xsl:template>
</xsl:stylesheet>
