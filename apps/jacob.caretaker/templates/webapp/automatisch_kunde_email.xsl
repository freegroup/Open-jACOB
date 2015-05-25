<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>Fertigmeldung vom: </xsl:text><xsl:value-of select="./doc/data/call/datereported"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrter Kunde,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr Dienstleister FWT hat Ihren Auftrag mit der </xsl:text>
  <xsl:text>ID-Nr.: </xsl:text><xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Beschreibung: "</xsl:text><xsl:value-of select="./doc/data/call/problem" /><xsl:text>"&#13;&#10;&#13;&#10;</xsl:text>
  <xsl:text>erledigt und fertig gemeldet.&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Für weitere Fragen stehen wir gerne unter unserer Service Desk Nummer Tel. 166 zur Verfügung.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Über ein kurzes Feedback von Ihnen, hinsichtlich unserer Leistungen aus obigem Auftrag, würden wir uns freuen.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Wir führen Sie hierzu mit der nachfolgenden Adresse und einem Aufwand von ca. zwei Minuten zu unseren fünf Bewertungskriterien.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>http://vutts282.sifi.daimlerchrysler.com:8090/voting_caretaker/vote.jsp?id=</xsl:text><xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Vielen Dank.&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr FWT Team&#13;&#10;Tel:   166&#13;&#10;eMail: FWT-Servicecenter@daimlerchrysler.com</xsl:text>
</xsl:template>
</xsl:stylesheet>
