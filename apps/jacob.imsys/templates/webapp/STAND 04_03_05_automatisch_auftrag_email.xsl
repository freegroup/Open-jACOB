<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
					<!-- Betreff-Zeile E-Mail -->
					<xsl:text>BS-IM-Auftrag: </xsl:text>
					<xsl:value-of select="./doc/data/task/taskno" />
					<xsl:text>   </xsl:text>
					<xsl:value-of select="./doc/data/general/ortsangabe" />
					<xsl:text>&#13;&#10;</xsl:text>
									
					<xsl:text>**************************************************************************</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*                                                                        *</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*                  BS-Infrastrukturmanagement Auftrag                    *</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*                                                                        *</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>**************************************************************************</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
			    	
			    	<xsl:text>AUFTRAGSNUMMER: </xsl:text><xsl:value-of select="./doc/data/task/taskno" /><xsl:text>       (Bitte immer angeben)&#13;&#10;</xsl:text>
			    	<xsl:text>Absendedatum  : </xsl:text><xsl:value-of select="./doc/data/general/date" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Absendezeit   : </xsl:text><xsl:value-of select="./doc/data/general/time" /><xsl:text>&#13;&#10;</xsl:text>
				    <xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Auftraggeber</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Name   : </xsl:text><xsl:value-of select="./doc/data/general/Auftraggeber_Name" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>FAX    : </xsl:text><xsl:value-of select="./doc/data/general/Auftraggeber_FAX" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Telefon: </xsl:text><xsl:value-of select="./doc/data/general/Auftraggeber_Telefon" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>E-Mail : </xsl:text><xsl:value-of select="./doc/data/general/Auftraggeber_Email" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					
					<xsl:text>An</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:value-of select="./doc/data/taskworkgroup/name" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
			
					<xsl:text>Endkunde</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Name     : </xsl:text><xsl:value-of select="./doc/data/customerint/fullname" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>Abteilung: </xsl:text><xsl:value-of select="./doc/data/customerint/department" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					
					<xsl:text>Auftragsbeschreibung: </xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>===================== </xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:value-of select="./doc/data/task/summary" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:value-of select="./doc/data/task/description" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*** Zu beginnen ab  :</xsl:text><xsl:value-of select="./doc/data/task/estimatedstart" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*** Zu erledigen bis:</xsl:text><xsl:value-of select="./doc/data/task/estimateddone" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>

					<xsl:text>*** Ortsangabe:</xsl:text><xsl:value-of select="./doc/data/general/ortsangabe" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<!--<xsl:text>*** Ca. Fläche:</xsl:text><xsl:value-of select="./doc/data/general/Flaeche" /><xsl:text>&#13;&#10;</xsl:text> -->
					<xsl:text>***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>*** Zu belastende Kostenstelle:</xsl:text><xsl:value-of select="./doc/data/general/Kostenstelle" /><xsl:text>&#13;&#10;</xsl:text>					
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>

					<xsl:text>---  Bei Terminverschiebungen bitte ich um sofortige Benachrichtigung. ---</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>---                                                                    ---</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>---  Bitte melden Sie den erledigten Auftrag unter Angabe der          ---</xsl:text><xsl:text>&#13;&#10;</xsl:text>					
					<xsl:text>---  Auftragsnummer unserem Servicecenter.                             ---</xsl:text><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>

					<xsl:text>**************************************************************************</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>***  IM-Servicecenter                                                  ***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>***    Tel   : 0711/17-21777 intern: -161 (07 Uhr - 18 Uhr)            ***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>***    FAX   : 0711/17-53133                                           ***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>***    E-Mail: Pool-ID.Servicecenter_BS_IM@DaimlerChrysler.Com         ***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>**************************************************************************</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                </xsl:template>
</xsl:stylesheet>
