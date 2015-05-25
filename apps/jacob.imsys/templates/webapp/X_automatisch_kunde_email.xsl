<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>BS/IM Fertigmeldung: </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> vom: </xsl:text><xsl:value-of select="./doc/data/call/callende"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrter Kunde,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr Dienstleister BS/IM hat Ihren Auftrag mit der oben genannten ID-Nummer erledigt und fertig gemeldet.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Auftragstext:</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>=============</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:value-of select="./doc/data/call/action" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>F�r weitere Fragen stehen wir gerne unter unserer Service-Nummer Tel. 161 zur Verf�gung.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>�ber ein kurzes Feedback von Ihnen hinsichtlich unserer Leistungen aus obigem Auftrag, w�rden wir uns freuen.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Wir f�hren Sie hierzu mit der nachfolgenden Adresse und einem Aufwand von ca. zwei Minuten zu unseren drei Bewertungskriterien.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>http://53.186.5.110:8200/voting_imsys/display.jsp?id=</xsl:text><xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Vielen Dank.&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr BS/IM Team&#13;&#10;Tel: intern -161&#13;&#10;eMail: pool-id.servicecenter_bs_im@daimlerchrysler.com</xsl:text>
</xsl:template>
</xsl:stylesheet>
