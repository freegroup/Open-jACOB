<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output method="text" encoding="iso-8859-1" />
	<xsl:template match="/">
					<!-- Betreffzeile in der Mail -->
					<xsl:text>IM-Auftrag: </xsl:text>
					<xsl:value-of select="./doc/data/task/pkey" />
					<xsl:text> von: </xsl:text>
					<xsl:value-of select="./doc/data/callworkgroup/name" />
					<xsl:text> </xsl:text>
					<xsl:value-of select="./doc/data/task/daterequested"/>
					
					<!--                         Start Body                            -->
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
				
					<xsl:text>An</xsl:text><xsl:text>&#13;&#10;</xsl:text>
		            <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  HWG:             </xsl:text><xsl:value-of select="./doc/data/taskworkgroup/name" /><xsl:text >&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
 
 
                     <!--
                           Task:
                     -->

                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Auftrag</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Beschreibung:    </xsl:text><xsl:value-of select="./doc/data/task/summary" />
  					<xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>                   </xsl:text><xsl:value-of select="./doc/data/task/action" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Prioritaet:      </xsl:text><xsl:value-of select="./doc/data/task/priority" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>  Bemerkung :      </xsl:text><xsl:value-of select="./doc/data/general/frage_01" /><xsl:text>&#13;&#10;</xsl:text>                    
                    <!--
                          STÖRUNGSORT:
                    -->
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Ortsangabe</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Werk:            </xsl:text><xsl:value-of select="./doc/data/location/faplissite" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Werksteil:       </xsl:text><xsl:value-of select="./doc/data/location/faplissitepart" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Gebaeude:        </xsl:text><xsl:value-of select="./doc/data/location/faplisbuilding" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Geschoss:        </xsl:text><xsl:value-of select="./doc/data/location/faplisfloor" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Raum:            </xsl:text><xsl:value-of select="./doc/data/location/faplisroom" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Bemerkung:       </xsl:text><xsl:value-of select="./doc/data/location/note" /><xsl:text>&#13;&#10;</xsl:text>

                   
                    <!--
                          CALLER:
                    -->
       				<xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Auftraggeber</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Name:            </xsl:text><xsl:value-of select="./doc/data/customerint/firstnamecorr" /> <xsl:text> </xsl:text><xsl:value-of select="./doc/data/customerint/lastnamecorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Telefon:         </xsl:text><xsl:value-of select="./doc/data/customerint/phonecorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Fax:             </xsl:text><xsl:value-of select="./doc/data/customerint/faxcorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  eMail:           </xsl:text><xsl:value-of select="./doc/data/customerint/emailcorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Werk:            </xsl:text><xsl:value-of select="./doc/data/customerint/sitecorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Werksteil:       </xsl:text><xsl:value-of select="./doc/data/customerint/sitepartcorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Abteilung:       </xsl:text><xsl:value-of select="./doc/data/customerint/department" /><xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
					<xsl:text>&#13;&#10;</xsl:text>
 				
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    
                    <xsl:text> Bitte fuehren Sie den obigen Auftrag aus bis zum: </xsl:text>
                    <xsl:value-of select="./doc/data/task/estimateddone" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text> Sollten Sie diesen Termin nicht einhalten können, </xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text> informieren Sie bitte unser Servicecenter. </xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text> Bitte informieren Sie uns, wenn der Auftrag erledigt ist. </xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text> Vielen Dank fuer Ihre Unterstuetzung. </xsl:text>
 					<xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>                                        
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text >IM-Servicecenter Tel: 21777 eMail: Pool-ID.Servicecenter_BS_IM@daimlerchrysler.com</xsl:text>
	</xsl:template>
</xsl:stylesheet>
