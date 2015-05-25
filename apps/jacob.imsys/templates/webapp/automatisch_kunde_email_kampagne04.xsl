<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
  <xsl:text>GET-Auftrag Fertigmeldung </xsl:text><xsl:value-of select="./doc/data/task/taskno" /><xsl:text> vom </xsl:text><xsl:value-of select="./doc/data/task/taskende"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Sehr geehrte Auftraggeberin,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>sehr geehrter Auftraggeber,</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Ihr Auftrag:</xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:text></xsl:text><xsl:text>&#13;&#10;</xsl:text>
  <xsl:value-of select="./doc/data/task/tasktext" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>zur Meldung: </xsl:text>
  <xsl:value-of select="./doc/data/call/pkey" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:value-of select="./doc/data/call/kundentext" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>wurde fertiggemeldet.</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Um mehr über die Qualität der Auftragsausführung von Ihnen zu erfahren, </xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>klicken Sie dazu bitte einfach auf den Link: </xsl:text>
  <xsl:text>http://53.186.5.110:8200/voting_imsys/vote.jsp?k=4&amp;taskid=</xsl:text><xsl:value-of select="./doc/data/task/taskno" />
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>Vielen Dank für Ihre Rückmeldung.&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
  </xsl:template>
</xsl:stylesheet>
