<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>Terminstornierung f�r Ihren R�derwechsel - </xsl:text><xsl:value-of select="./doc/data/tc_order/pkw"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin, sehr geehrter Kunde,</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>hiermit best�tigen wir die Stornierung Ihres Termins an ihrem Dienstfahrzeug mit dem Kennzeichen </xsl:text>
  <xsl:value-of select="./doc/data/tc_order/pkw" />
  <xsl:text> am </xsl:text>
  <xsl:value-of select="./doc/data/tc_order/slotdate" />
  <xsl:text>, um </xsl:text>
  <xsl:value-of select="./doc/data/tc_order/slottime" />
  <xsl:text> Uhr.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Dieser Vorgang l�uft unter der Bearbeitungsnummer: </xsl:text>
  <xsl:value-of select="./doc/data/tc_order/call" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Bitte setzen Sie sich mit uns f�r eine erneute Terminabsprache in Verbindung.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Der R�derwechsel findet im Geb�ude der Firma Formel D in B�blingen - Herrenberger Stra�e 120 - statt.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>F�r Fragen steht Ihnen der Service Desk unter der Telefonnummer 166 gerne zur Verf�gung.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Mit freundlichen Gr�ssen / Kind regards</xsl:text>
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
