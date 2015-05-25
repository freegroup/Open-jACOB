<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSPY v5 rel. 4 U (http://www.xmlspy.com) by Stephan Thuemmler (Freizeit und Reisen) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!--
		The main template. Processing starts here
	-->
	<xsl:template match="/doc/data">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simple" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="2cm" margin-left="2.5cm" margin-right="2.5cm">
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
				<fo:static-content flow-name="xsl-region-before">
					<fo:block border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid">
						<fo:table table-layout="fixed">
							<fo:table-column column-width="3cm"/>
							<fo:table-column column-width="11cm"/>
							<fo:table-body font-family="sans-serif">
								<fo:table-row font-size="12pt">
									<fo:table-cell>
										<fo:block>
											<fo:external-graphic height="10mm" src="url('http://53.186.5.110/yan/DaimlerChrysler.gif')"/>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block vertical-align="baseline" text-align="end">
											<xsl:text> Auftrag: </xsl:text>
											<xsl:value-of select="./call/pkey"/>
											<xsl:text> vom:</xsl:text>
											<xsl:value-of select="./call/datereported"/>
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
				<fo:static-content background-color="black" flow-name="xsl-region-after">
					<fo:block text-align="left" font-size="20pt" font-family="sans-serif" line-height="20pt" background-color="255">
						<!--Kommentar-->Hier kommt noch weiterer Text rein ...Hier kommt noch weiterer Text rein ...Hier kommt noch weiterer Text rein ...Hier kommt noch weiterer Text rein ...Hier kommt noch weiterer Text rein ...<xsl:text>IM-Servicecenter IM-Servicecenter 
IM-Servicecenter IM-Servicecenter IM-Servicecenter IM-Servicecenter IM-Servicecenter</xsl:text>
					</fo:block>
				</fo:static-content>
				<!--
					print the content itself
				-->
				<fo:flow flow-name="xsl-region-body">
					<!--
					      TO:
					-->
					<fo:table table-layout="fixed" space-before.optimum="30pt">
						<fo:table-column column-width="1cm"/>
						<fo:table-column column-width="3cm"/>
						<fo:table-column column-width="10cm"/>
						<fo:table-body font-size="10pt" font-family="sans-serif">
							<!-- HEADER  -->
							<fo:table-row font-size="12pt" line-height="14pt">
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>An</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
							</fo:table-row>
							<!-- ABTEILUNG  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Abteilung:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./wrkgporg/department"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- MEISTEREI  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Meisterei:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./wrkgporg/name"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- GRUPPE  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Gruppe:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./callworkgroup/name"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- FAX  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Fax:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./callworkgroup/fax"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- TELEFONISCH INFORMIERT?  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Tel. informiert:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:choose>
											<xsl:when test="./call/forwardbyphone='0'">
												<xsl:value-of select="'nein'"/>
											</xsl:when>
											<xsl:when test="./call/forwardbyphone='1'">
												<xsl:value-of select="'ja'"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="'nein'"/>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<!--
					      CALLER:
					-->
					<fo:table table-layout="fixed" space-before.optimum="30pt">
						<fo:table-column column-width="1cm"/>
						<fo:table-column column-width="3cm"/>
						<fo:table-column column-width="10cm"/>
						<fo:table-body font-size="10pt" font-family="sans-serif">
							<!-- HEADER  -->
							<fo:table-row font-size="12pt" line-height="14pt">
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Melder</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
							</fo:table-row>
							<!-- NAME  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Name:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./customerint/firstnamecorr"/>
										<xsl:text> </xsl:text>
										<xsl:value-of select="./customerint/lastnamecorr"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- TELEFON  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Telefon:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./customerint/phonecorr"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- FAX  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Fax:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./customerint/faxcorr"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- EMAIL  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>eMail:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./customerint/emailcorr"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- WERK  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Werk:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./customerint/sitecorr"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- WERKSTEIL  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Werksteil:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./customerint/sitepartcorr"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- ABTEILUNG  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Abteilung:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./customerint/department"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<!--
                           CALL:
                     -->
					<fo:table table-layout="fixed" space-before.optimum="30pt">
						<fo:table-column column-width="1cm"/>
						<fo:table-column column-width="3cm"/>
						<fo:table-column column-width="10cm"/>
						<fo:table-body font-size="10pt" font-family="sans-serif">
							<!-- HEADER  -->
							<fo:table-row font-size="12pt" line-height="14pt">
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Auftrag</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
							</fo:table-row>
							<!-- Betrf. Person  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Betrf. Person:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./call/affectedperson"/>
										<xsl:text> </xsl:text>
										<xsl:value-of select="./call/relatedperson/@lastname"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- PRIORITÄT  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Priorität:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./call/priority"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- BESCHREIBUNG  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Beschreibung:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./call/problem"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- SOLUTION  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Doku./Lösung:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./call/problemtext"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<!--
					      STÖRUNGSORT:
					-->
					<fo:table table-layout="fixed" space-before.optimum="30pt">
						<fo:table-column column-width="1cm"/>
						<fo:table-column column-width="3cm"/>
						<fo:table-column column-width="10cm"/>
						<fo:table-body font-size="10pt" font-family="sans-serif">
							<!-- WERK  -->
							<fo:table-row font-size="12pt" line-height="14pt">
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Auftragsort</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
							</fo:table-row>
							<!--  SITE  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Werk:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/faplissite"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- SITEPART  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Werksteil:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/faplissitepart"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- BUILDING  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Gebäude:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/faplisbuilding"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- BUILDINGPARTTION  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Doku./Lösung:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/faplisbuildingpart"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- FLOOR  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Geschoß:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/faplisfloor"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- ROOM  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Raum:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/faplisroom"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- AXIS  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Achse:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/faplisbaxis"/>
										<xsl:text>/</xsl:text>
										<xsl:value-of select="./location/fapliszaxis"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- REMARK  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Bemerkung:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/note"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- ORIENTATION  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Himmelsrichtung:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./location/orientation"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<!--
                        OBJECT
                  -->
					<fo:table table-layout="fixed" space-before.optimum="30pt">
						<fo:table-column column-width="1cm"/>
						<fo:table-column column-width="3cm"/>
						<fo:table-column column-width="10cm"/>
						<fo:table-body font-size="10pt" font-family="sans-serif">
							<fo:table-row font-size="12pt" line-height="14pt">
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Objekt</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
							</fo:table-row>
							<!--  OBJECT ID  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Objekt ID:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./object/external_id"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!--  CATEGORY  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Gewerk:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./category/longname"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- FANUFACTURA  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Hersteller:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./object/vendor"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- DESCRIPTION  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Beschreibung:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./object/name"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<!-- ACCOUNTING CODE  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Kostenstelle:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./object/objaccountingcode"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<!--
                        AGENT
                  -->
					<fo:table table-layout="fixed" space-before.optimum="30pt">
						<fo:table-column column-width="1cm"/>
						<fo:table-column column-width="3cm"/>
						<fo:table-column column-width="10cm"/>
						<fo:table-body font-size="10pt" font-family="sans-serif">
							<fo:table-row font-size="12pt" line-height="14pt">
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Agent</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
					                  </fo:table-cell>
							</fo:table-row>
							<!--  NAME  -->
							<fo:table-row line-height="12pt">
								<fo:table-cell>
					                  </fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:text>Name:</xsl:text>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="start">
										<xsl:value-of select="./agent/fullname"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
