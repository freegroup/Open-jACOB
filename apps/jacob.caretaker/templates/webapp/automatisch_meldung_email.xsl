<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="text" encoding="iso-8859-1" />
<xsl:template match="/">
<xsl:text>Meldung: </xsl:text><xsl:value-of select="./doc/data/call/pkey" /><xsl:text> vom: </xsl:text><xsl:value-of select="./doc/data/call/datereported"/>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>An</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Abteilung:       </xsl:text><xsl:value-of select="./doc/data/wrkgporg/department" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Meisterei:       </xsl:text><xsl:value-of select="./doc/data/wrkgporg/name" /><xsl:text >&#13;&#10;</xsl:text>
                    <xsl:text>  Gruppe:          </xsl:text><xsl:value-of select="./doc/data/callworkgroup/name" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Fax:             </xsl:text><xsl:value-of select="./doc/data/callworkgroup/fax" /><xsl:text>&#13;&#10;</xsl:text>
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
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Melder     </xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Name:            </xsl:text><xsl:value-of select="./doc/data/customerint/firstnamecorr" /> <xsl:text> </xsl:text><xsl:value-of select="./doc/data/customerint/lastnamecorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Telefon:         </xsl:text><xsl:value-of select="./doc/data/customerint/phonecorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Fax:             </xsl:text><xsl:value-of select="./doc/data/customerint/faxcorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  eMail:           </xsl:text><xsl:value-of select="./doc/data/customerint/emailcorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Werk:            </xsl:text><xsl:value-of select="./doc/data/customerint/emplsite_keycorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Werksteil:       </xsl:text><xsl:value-of select="./doc/data/customerint/sitepartcorr" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Abteilung:       </xsl:text><xsl:value-of select="./doc/data/customerint/department" /><xsl:text>&#13;&#10;</xsl:text>
                    

                     <!--
                           CALL:
                     -->
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Meldung</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Betrf. Person:   </xsl:text><xsl:value-of select="./doc/data/call/affectedperson" /><xsl:text> </xsl:text><xsl:value-of select="./doc/data/call/relatedperson/@lastname" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Prioritaet:       </xsl:text><xsl:value-of select="./doc/data/call/priority" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Beschreibung:    </xsl:text><xsl:value-of select="./doc/data/call/problem" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Aktion:          </xsl:text><xsl:value-of select="./doc/data/call/action" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Doku./Loesung:    </xsl:text><xsl:value-of select="./doc/data/call/problemtext" /><xsl:text>&#13;&#10;</xsl:text>
                    
                    <!--
                          STÖRUNGSORT:
                    -->
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Stoerungsort</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Werk:            </xsl:text><xsl:value-of select="./doc/data/location/faplissite" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Werksteil:       </xsl:text><xsl:value-of select="./doc/data/location/faplissitepart" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Gebaeude:        </xsl:text><xsl:value-of select="./doc/data/location/faplisbuilding" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Teilgebaeude:    </xsl:text><xsl:value-of select="./doc/data/location/faplisbuildingpart" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Geschoss:        </xsl:text><xsl:value-of select="./doc/data/location/faplisfloor" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Raum:            </xsl:text><xsl:value-of select="./doc/data/location/faplisroom" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Achse:           </xsl:text><xsl:value-of select="./doc/data/location/faplisbaxis" /><xsl:text>/</xsl:text><xsl:value-of select="./doc/data/location/fapliszaxis" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Bemerkung:       </xsl:text><xsl:value-of select="./doc/data/location/note" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Himmelsrichtung: </xsl:text><xsl:value-of select="./doc/data/location/orientation" /><xsl:text>&#13;&#10;</xsl:text>
                   
                    <!--
                          OBJECT
                    -->
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Objekt</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Objekt ID:       </xsl:text><xsl:value-of select="./doc/data/object/external_id" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Gewerk:          </xsl:text><xsl:value-of select="./doc/data/category/longname" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Hersteller:      </xsl:text><xsl:value-of select="./doc/data/object/vendor" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Beschreibung:    </xsl:text><xsl:value-of select="./doc/data/object/name" /><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Kostenstelle:    </xsl:text><xsl:value-of select="./doc/data/object/objaccountingcode" /><xsl:text>&#13;&#10;</xsl:text>
                  
                    <!--
                          AGENT
                    -->
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>Agent</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>-----------------------------------------------------------------</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>  Name:            </xsl:text><xsl:value-of select="./doc/data/agent/fullname" /><xsl:text>&#13;&#10;</xsl:text>
                  
                  
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text >FWT-SMC&#13;&#10;Tel:   166&#13;&#10;eMail: FWT-Servicecenter@daimlerchrysler.com</xsl:text>
                </xsl:template>
</xsl:stylesheet>
