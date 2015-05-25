<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
					<!-- Betreff-Zeile E-Mail -->
					<xsl:text>BS-IM-Meldung: </xsl:text>
					<xsl:value-of select="./doc/data/call/pkey" />
					<xsl:text>&#13;&#10;</xsl:text>
								
					<xsl:text>**************************************************************************</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*                                                                        *</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*                  Info zu IMSYS-Meldung/Auftrag                         *</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*                                                                        *</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>**************************************************************************</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
		        	<xsl:text>Meldungsnummer: </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> &#13;&#10;</xsl:text>
			    	<xsl:text>Auftragsnummer: </xsl:text><xsl:value-of select="./doc/data/task/taskno" /><xsl:text> &#13;&#10;</xsl:text>
			    	<xsl:text>Melder        : </xsl:text><xsl:value-of select="./doc/data/customerint/fullname" /><xsl:text> &#13;&#10;</xsl:text>
			    	<xsl:text>Absendedatum  : </xsl:text><xsl:value-of select="./doc/data/general/date" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Absendezeit   : </xsl:text><xsl:value-of select="./doc/data/general/time" /><xsl:text>&#13;&#10;</xsl:text>
				    <xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>==> </xsl:text>
					<xsl:value-of select="./doc/data/general/Info_Text" />
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Absender der Info:</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>------------------</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Name   : </xsl:text><xsl:value-of select="./doc/data/general/Auftraggeber_Name" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>FAX    : </xsl:text><xsl:value-of select="./doc/data/general/Auftraggeber_FAX" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Telefon: </xsl:text><xsl:value-of select="./doc/data/general/Auftraggeber_Telefon" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>E-Mail : </xsl:text><xsl:value-of select="./doc/data/general/Auftraggeber_Email" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					
                    <xsl:text>&#13;&#10;</xsl:text>
                </xsl:template>
</xsl:stylesheet>
