<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>BS/IM Auftragsbest�tigung </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> vom </xsl:text><xsl:value-of select="./doc/data/call/callstart"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Kundin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>sehr geehrter Kunde,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr Auftrag:</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text></xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:value-of select="./doc/data/call/kundentext" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  
  <xsl:text>wurde in unserem System erfasst und hat die Auftragsnummer: </xsl:text>
  <xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Mit dem folgenden Link k�nnen Sie sich �ber den Status Ihres Auftrages informieren:</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>http://vudb0007.daimlerchrysler.com:8200/jacob/enter?entry=ShowMyCalls</xsl:text>
  <xsl:text>&#38;app=custinfo</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Mit freundlichen Gr��en</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Infrastrukturmanagement </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Telefon: -161 (intern) 0711/17-161 (extern)                </xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>          161 + 1 f�r B�rogestaltung, Umz�ge, Fl�che, Geb�ude          </xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>          161 + 2 f�r Conference Center                                </xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>          161 + 3 f�r Post, Adress-Service, Medienverleih, Hausdienste </xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>          161 + 4 f�r IT, B�ro- und Kommunikationstechnik              </xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Fax:     -53133</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>E-Mail: Pool-ID.161@DaimlerChrysler.Com </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
</xsl:template>
</xsl:stylesheet>
