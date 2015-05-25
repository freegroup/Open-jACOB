<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>Ihre Meldung </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> wird voraussichtlich bis zum </xsl:text><xsl:value-of select="./doc/data/call/date_sl_overdue" /><xsl:text> fertig gestellt.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrter Kunde,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihre Meldung </xsl:text><xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text> mit der Beschreibung &#13;&#10; "</xsl:text><xsl:value-of select="./doc/data/call/problem" />
  <xsl:text>" &#13;&#10;wird voraussichtlich bis zum </xsl:text><xsl:value-of select="./doc/data/call/date_sl_overdue" /><xsl:text> fertig gestellt.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Grund:</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:value-of select="./doc/data/call/sl_overdue" /><xsl:text>.</xsl:text>

  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Wir bitten um Ihr Verständnis.&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr FWT Team&#13;&#10;Tel:   166&#13;&#10;eMail: FWT-Servicecenter@daimlerchrysler.com</xsl:text>
</xsl:template>
</xsl:stylesheet>
