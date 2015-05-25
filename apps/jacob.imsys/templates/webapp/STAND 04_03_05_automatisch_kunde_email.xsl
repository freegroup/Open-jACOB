<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>BS/IM Fertigmeldung: </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> vom: </xsl:text><xsl:value-of select="./doc/data/call/callende"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>sehr geehrter Kunde,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr Auftrag:</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>============</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:value-of select="./doc/data/call/action" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  
  <xsl:text>wurde in unserem System als erledigt erfasst.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sollte dies jedoch nicht der Fall sein, so wenden Sie sich bitte an das Infrastruktur-Service-Center unter der Rufnummer 161.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Wir möchten von Ihnen noch erfahren, wie zufrieden Sie mit unserer Leistung sind. Klicken Sie dazu einfach auf den Link: </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>http://53.186.5.110:8200/voting_imsys/display.jsp?id=</xsl:text><xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Wir danken Ihnen für Ihre Rückmeldung.&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Infrastrukturmanagement </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Tel: -161 (intern) 0711/17-21641 (extern) </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>eMail: pool-id.servicecenter_bs_im@daimlerchrysler.com </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
</xsl:template>
</xsl:stylesheet>
