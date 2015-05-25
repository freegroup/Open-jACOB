<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>BS/IM Fertigmeldung </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> vom </xsl:text><xsl:value-of select="./doc/data/call/callende"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>sehr geehrter Kunde,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr Auftrag:</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text></xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:value-of select="./doc/data/call/kundentext" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  
  <xsl:text>wurde in unserem System als erledigt erfasst.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Um unseren Service zu verbessern, wollen wir von Ihnen erfahren, wie zufrieden Sie mit unserer Leistung sind. </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Klicken Sie dazu einfach auf den Link: </xsl:text>
  <xsl:text>http://53.186.5.110:8200/voting_imsys/display.jsp?k=5&id=</xsl:text><xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Wir danken Ihnen für Ihre Rückmeldung.&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Mit freundlichen Grüßen</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Infrastrukturmanagement </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Telefon: -161 (intern) 0711/17-21641 (extern) </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Fax:     -53133</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>E-Mail: Pool-ID.161@DaimlerChrysler.Com </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
</xsl:template>
</xsl:stylesheet>
