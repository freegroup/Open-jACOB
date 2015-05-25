<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>BS_IM Fertigmeldung: </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> vom: </xsl:text><xsl:value-of select="./doc/data/call/datecallconnected"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrter Kunde,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr Dienstleister BS/IM hat Ihren Auftrag mit der oben genannten ID-Nummer erledigt und fertig gemeldet.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Für weitere Fragen stehen wir gerne unter unserer Service-Nummer Tel. 161 zur Verfügung.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Über ein kurzes Feedback von Ihnen hinsichtlich unserer Leistungen aus obigem Auftrag, würden wir uns freuen.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Wir führen Sie hierzu mit der nachfolgenden Adresse und einem Aufwand von ca. zwei Minuten zu unseren drei Bewertungskriterien.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>http://53.11.51.27:8200/voting_testimsys/vote.jsp?id=</xsl:text><xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Vielen Dank.&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr BS/IM Team&#13;&#10;Tel:   161&#13;&#10;eMail: pool-id@DaimlerChrysler.com</xsl:text>
</xsl:template>
</xsl:stylesheet>
