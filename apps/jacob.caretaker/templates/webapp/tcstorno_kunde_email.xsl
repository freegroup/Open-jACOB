<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>Terminstornierung für Ihren Räderwechsel - </xsl:text><xsl:value-of select="./doc/data/tc_order/pkw"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin, sehr geehrter Kunde,</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>hiermit bestätigen wir die Stornierung Ihres Termins an ihrem Dienstfahrzeug mit dem Kennzeichen </xsl:text>
  <xsl:value-of select="./doc/data/tc_order/pkw" />
  <xsl:text> am </xsl:text>
  <xsl:value-of select="./doc/data/tc_order/slotdate" />
  <xsl:text>, um </xsl:text>
  <xsl:value-of select="./doc/data/tc_order/slottime" />
  <xsl:text> Uhr.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Dieser Vorgang läuft unter der Bearbeitungsnummer: </xsl:text>
  <xsl:value-of select="./doc/data/tc_order/call" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Bitte setzen Sie sich mit uns für eine erneute Terminabsprache in Verbindung.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Der Räderwechsel findet im Gebäude der Firma Formel D in Böblingen - Herrenberger Straße 120 - statt.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Für Fragen steht Ihnen der Service Desk unter der Telefonnummer 166 gerne zur Verfügung.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Mit freundlichen Grüssen / Kind regards</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>FWT - Servicecenter</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>im Auftrag der DaimlerChrysler AG</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>050/C174</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>71059 Sindelfingen</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Phone   +49-(0)7031-90-166</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Fax     +49-(0)7031-90-61011</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>mailto:fwt-servicecenter@daimlerchrysler.com</xsl:text><xsl:text>&#13;&#10;</xsl:text>
</xsl:template>
</xsl:stylesheet>
