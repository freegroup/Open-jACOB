<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="yes"/>

<xsl:template match="/">
<xsl:text>Meldung: </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> vom: </xsl:text><xsl:value-of select="./doc/data/call/datereported"/>
                    <br/>
                    <xsl:text>An</xsl:text><br/>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><br/>
                    <xsl:text>  Abteilung:       </xsl:text><xsl:value-of select="./doc/data/wrkgporg/department" /><br/>
                    <xsl:text>  Meisterei:       </xsl:text><xsl:value-of select="./doc/data/wrkgporg/name" /><xsl:text >&#13;&#10;</xsl:text>
                    <xsl:text>  Gruppe:          </xsl:text><xsl:value-of select="./doc/data/callworkgroup/name" /><br/>
                    <xsl:text>  Fax:             </xsl:text><xsl:value-of select="./doc/data/callworkgroup/fax" /><br/>
                    <xsl:text>  Tel. informiert: </xsl:text>
                              <xsl:choose>
                                       <xsl:when test="./doc/data/call/forwardbyphone='0'"><xsl:value-of select="'nein'" /></xsl:when>
                                       <xsl:when test="./doc/data/call/forwardbyphone='1'"><xsl:value-of select="'ja'" /></xsl:when>
                                       <xsl:otherwise>   <xsl:value-of select="'nein'" /></xsl:otherwise>
                              </xsl:choose>
                              <xsl:text disable-output-escaping="yes">&#13;&#10;</xsl:text>
                    <!--
                          CALLER:
                    -->
                    <br/>
                    <br/>
                    <xsl:text>Melder     </xsl:text><br/>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><br/>
                    <xsl:text>  Name:            </xsl:text><xsl:value-of select="./doc/data/customerint/firstnamecorr" /> <xsl:text> </xsl:text><xsl:value-of select="./doc/data/customerint/lastnamecorr" /><br/>
                    <xsl:text>  Telefon:         </xsl:text><xsl:value-of select="./doc/data/customerint/phonecorr" /><br/>
                    <xsl:text>  Fax:             </xsl:text><xsl:value-of select="./doc/data/customerint/faxcorr" /><br/>
                    <xsl:text>  eMail:           </xsl:text><xsl:value-of select="./doc/data/customerint/emailcorr" /><br/>
                    <xsl:text>  Werk:            </xsl:text><xsl:value-of select="./doc/data/customerint/sitecorr" /><br/>
                    <xsl:text>  Werksteil:       </xsl:text><xsl:value-of select="./doc/data/customerint/sitepartcorr" /><br/>
                    <xsl:text>  Abteilung:       </xsl:text><xsl:value-of select="./doc/data/customerint/department" /><br/>
                    

                     <!--
                           CALL:
                     -->
                    <br/>
                    <br/>
                    <xsl:text>Meldung</xsl:text><br/>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><br/>
                    <xsl:text>  Betrf. Person:   </xsl:text><xsl:value-of select="./doc/data/call/affectedperson" /><xsl:text> </xsl:text><xsl:value-of select="./doc/data/call/relatedperson/@lastname" /><br/>
                    <xsl:text>  Priorität:       </xsl:text><xsl:value-of select="./doc/data/call/priority" /><br/>
                    <xsl:text>  Beschreibung:    </xsl:text><xsl:value-of select="./doc/data/call/problem" /><br/>
                    <xsl:text>  Doku./Lösung:    </xsl:text><xsl:value-of select="./doc/data/call/problemtext" /><br/>
                    
                    <!--
                          STÖRUNGSORT:
                    -->
                    <br/>
                    <br/>
                    <xsl:text>Störungsort</xsl:text><br/>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><br/>
                    <xsl:text>  Werk:            </xsl:text><xsl:value-of select="./doc/data/location/faplissite" /><br/>
                    <xsl:text>  Werksteil:       </xsl:text><xsl:value-of select="./doc/data/location/faplissitepart" /><br/>
                    <xsl:text>  Gebäude:         </xsl:text><xsl:value-of select="./doc/data/location/faplisbuilding" /><br/>
                    <xsl:text>  Doku./Lösung:    </xsl:text><xsl:value-of select="./doc/data/location/faplisbuildingpart" /><br/>
                    <xsl:text>  Geschoß:         </xsl:text><xsl:value-of select="./doc/data/location/faplisfloor" /><br/>
                    <xsl:text>  Raum:            </xsl:text><xsl:value-of select="./doc/data/location/faplisroom" /><br/>
                    <xsl:text>  Achse:           </xsl:text><xsl:value-of select="./doc/data/location/faplisbaxis" /><xsl:text>/</xsl:text><xsl:value-of select="./doc/data/location/fapliszaxis" /><br/>
                    <xsl:text>  Bemerkung:       </xsl:text><xsl:value-of select="./doc/data/location/note" /><br/>
                    <xsl:text>  Himmelsrichtung: </xsl:text><xsl:value-of select="./doc/data/location/orientation" /><br/>
                   
                    <!--
                          OBJECT
                    -->
                    <br/>
                    <br/>
                    <xsl:text>Objekt</xsl:text><br/>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><br/>
                    <xsl:text>  Objekt ID:       </xsl:text><xsl:value-of select="./doc/data/object/external_id" /><br/>
                    <xsl:text>  Gewerk:          </xsl:text><xsl:value-of select="./doc/data/category/longname" /><br/>
                    <xsl:text>  Hersteller:      </xsl:text><xsl:value-of select="./doc/data/object/vendor" /><br/>
                    <xsl:text>  Beschreibung:    </xsl:text><xsl:value-of select="./doc/data/object/name" /><br/>
                    <xsl:text>  Kostenstelle:    </xsl:text><xsl:value-of select="./doc/data/object/objaccountingcode" /><br/>
                  
                    <!--
                          AGENT
                    -->
                    <br/>
                    <br/>
                    <xsl:text>Agent</xsl:text><br/>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><br/>
                    <xsl:text>  Name:            </xsl:text><xsl:value-of select="./doc/data/agent/fullname" /><br/>
                  
                  
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <xsl:text >FWT-SMC&#13;&#10;Tel:   166&#13;&#10;eMail: FWT-Servicecenter@daimlerchrysler.com</xsl:text>
                </xsl:template>
</xsl:stylesheet>
