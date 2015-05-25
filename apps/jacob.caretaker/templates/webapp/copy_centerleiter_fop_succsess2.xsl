<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<!--
		The main template. Processing starts here
	-->
	<xsl:template match="/doc/data">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simple" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="2cm" margin-left="1.5cm" margin-right="2.5cm">
					<fo:region-body margin-top="1cm"/>
					<fo:region-end extent="1cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="1cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple" initial-page-number="1">
			
				<!--
					print the header of the page
				-->
				<fo:static-content  flow-name="xsl-region-before">
				<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid">
				<fo:table   table-layout="fixed">
				      <fo:table-column column-width="6cm"/>
				       <fo:table-column column-width="11cm"/>
				       <fo:table-body  font-family="sans-serif">
                                       <fo:table-row font-size="12pt">
					   <fo:table-cell>
					      <fo:block ><fo:external-graphic height="20mm" src="url('http://vutts282:8090/xsl_caretaker/DaimlerChrysler.gif')"/></fo:block>
					   </fo:table-cell>
					   <fo:table-cell>
					       <fo:block   vertical-align="baseline" text-align="end" >
					       <xsl:text>FWT: Information Produktions-/Anlagenstörung</xsl:text>
					       </fo:block>
					   </fo:table-cell>
                                       </fo:table-row>
				            </fo:table-body>
				       </fo:table>
				</fo:block>
				</fo:static-content>

				<!--
					print the footer of the page
				-->
				<fo:static-content background-color="black"  flow-name="xsl-region-after">
					<fo:block text-align="center" font-size="10pt" font-family="sans-serif" line-height="10pt">
						 <xsl:text >FWT-SMC Tel:166 eMail:FWT-Servicecenter@daimlerchrysler.com</xsl:text>
					</fo:block>
				</fo:static-content>

				<!--
					print the content itself
				-->
				<fo:flow flow-name="xsl-region-body">
				
				<fo:block text-align="center"  font-size="13pt" space-before.optimum="15pt"><xsl:text>SMC-Meldung Nr.: </xsl:text><xsl:value-of select="./call/pkey" /><xsl:text> vom: </xsl:text><xsl:value-of select="./call/datereported"/></fo:block>
					<!--
					      TO:
					-->				
<fo:block text-align="center" background-color="#00DD00">
   <xsl:text>Am </xsl:text>
   <xsl:value-of select="/doc/data/general/date" />
   <xsl:text> </xsl:text>
   <xsl:value-of select="/doc/data/general/time" />
   <xsl:text> versendet an:</xsl:text><xsl:value-of select="/doc/control/original_to/@url" />
</fo:block>
					<fo:block text-align="start" font-size="15pt"  space-before.optimum="15pt"><xsl:text>Betroffene Meisterei</xsl:text></fo:block>
<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid" ></fo:block>
				        <fo:table table-layout="fixed" space-before.optimum="10pt">
				            <fo:table-column column-width="0.5cm"/>
				            <fo:table-column column-width="3.5cm"/>
				            <fo:table-column column-width="10cm"/>
				            <fo:table-body font-size="10pt" font-family="sans-serif">
				                  <!-- ABTEILUNG  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Abteilung:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./wrkgporg/department" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>

				                  <!-- MEISTEREI  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Meisterei:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./wrkgporg/name" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>

				                  <!-- GRUPPE  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Gruppe:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./callworkgroup/name" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>

				               <!-- FAX  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Fax:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./callworkgroup/fax" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>
                              
				              <!-- TELEFON?  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Tel.:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./callworkgroup/phone" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>
				            </fo:table-body>
				        </fo:table>                
<!--
                        OBJECT
                  -->
					<fo:block text-align="start" font-size="15pt"  space-before.optimum="15pt"><xsl:text>Störungsort</xsl:text></fo:block>
<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid" ></fo:block>
				        <fo:table   table-layout="fixed" space-before.optimum="10pt">
				            <fo:table-column column-width="0.5cm"/>
				            <fo:table-column column-width="3.5cm"/>
				            <fo:table-column column-width="10cm"/>
				            <fo:table-body font-size="10pt" font-family="sans-serif">
                              <fo:table-row font-size="12pt" line-height="14pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                  </fo:table-cell>
                              </fo:table-row>

				                  <!--  PRODUCTION_AREA  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Produktionsnbereich</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./general/productionarea" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>

				                  <!-- BUILDING  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Gebäude:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./location/faplisbuilding" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>
				                  <!--  CATEGORY  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Gewerk:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./category/longname" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>
				                  <!-- DESCRIPTION  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Anlage:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./object/name" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>
                                     </fo:table-body>
				        </fo:table>

                                <!--
                           ALERT:
                     -->
					<fo:block text-align="start" font-size="15pt"  space-before.optimum="30pt"><xsl:text>Störungsszeitraum</xsl:text></fo:block>
<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid" ></fo:block>
				        <fo:table  table-layout="fixed" space-before.optimum="10pt">
				            <fo:table-column column-width="0.5cm"/>
				            <fo:table-column column-width="3.5cm"/>
				            <fo:table-column column-width="10cm"/>
				            <fo:table-body font-size="10pt" font-family="sans-serif">
				                  <!-- HEADER  -->
                              <fo:table-row font-size="13pt" line-height="14pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                  </fo:table-cell>
                              </fo:table-row>

				                  <!-- PRODUKTIONSAUSFALL  -->
                              <fo:table-row line-height="25pt"   >
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text >Produktionsausfall:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./general/productionlost" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>
				                  <!-- Störmeldeeingang  -->

                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Störmeldeeingang:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./call/datereported" /><xsl:text> </xsl:text><xsl:value-of select="./call/relatedperson/@lastname" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>

				                  <!-- PRIORITÄT  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Störungsbeginn:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./general/troublestart" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>

				                  <!-- BESCHREIBUNG  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Störungsende:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./call/dateresolved" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>


				                  <!-- ANLAGENSTILLSTAND  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Anlagenstillstand:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./general/produktionlostduration" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>
                                    </fo:table-body>
				        </fo:table>

                                <!--
                           ALERT:
                     -->
					<fo:block text-align="start" font-size="15pt"  space-before.optimum="30pt"><xsl:text>Hintergründe und Maßnahmen</xsl:text></fo:block>
<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid" ></fo:block>

				        <fo:table  table-layout="fixed" space-before.optimum="10pt">
				            <fo:table-column column-width="0.5cm"/>
				            <fo:table-column column-width="3.5cm"/>
				            <fo:table-column column-width="10cm"/>
				            <fo:table-body font-size="10pt" font-family="sans-serif">
				                  <!-- HEADER  -->
                              <fo:table-row font-size="13pt" line-height="14pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                  </fo:table-cell>
                              </fo:table-row>


				                  <!-- ART OF TROUBLE  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Störungsart:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./call/problem" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>


				                  <!-- REASON OF TROUBLE  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Störungsursache:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./general/troublereason" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>


				                  <!-- CREATED ACTIOONS  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Maßnahmen:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./call/action" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>


				                  <!-- REMARKS  -->
                              <fo:table-row line-height="13pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Bemerkung:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./general/remarks" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>


				            </fo:table-body>
				        </fo:table>
		<fo:block text-align="start" font-size="12pt"  space-before.optimum="60pt"><xsl:text>eigene Bemerkungen:</xsl:text></fo:block>
<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid" space-before.optimum="35pt"></fo:block>
<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid" space-before.optimum="20pt"></fo:block>
<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid" space-before.optimum="20pt"></fo:block>

				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
