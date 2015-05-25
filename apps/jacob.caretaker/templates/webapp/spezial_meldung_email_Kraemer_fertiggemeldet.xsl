<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
<xsl:text>Meldung: </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> Objekt: </xsl:text><xsl:value-of select="./doc/data/object/name"/>

                     <!--
                           CALL:
                     -->
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Meldung</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Meldungsnummer:   </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> </xsl:text><xsl:value-of select="./doc/data/call/relatedperson/@lastname" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Meldungsbeginn:   </xsl:text><xsl:value-of select="./doc/data/call/datereported" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Fertig gemeldet:  </xsl:text><xsl:value-of select="./doc/data/call/dateresolved" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Beschreibung:     </xsl:text><xsl:value-of select="./doc/data/call/problem" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Aktion:           </xsl:text><xsl:value-of select="./doc/data/call/action" /><xsl:text>&#13;&#10;</xsl:text>
                    <!--
                          OBJECT
                    -->
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Objekt</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Objekt ID:       </xsl:text><xsl:value-of select="./doc/data/object/external_id" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Beschreibung:    </xsl:text><xsl:value-of select="./doc/data/object/name" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text >FWT-SMC&#13;&#10;Tel:   166&#13;&#10;eMail: FWT-Servicecenter@daimlerchrysler.com</xsl:text>
                </xsl:template>
</xsl:stylesheet>
