<?xml version="1.0" encoding="ISO-8859-1"?>
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
				<fo:static-content  flow-name="xsl-region-before">
				<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid">
				<fo:table   table-layout="fixed">
				      <fo:table-column column-width="3cm"/>
				       <fo:table-column column-width="11cm"/>
				       <fo:table-body  font-family="sans-serif">
                                       <fo:table-row font-size="12pt">
					   <fo:table-cell>
					      <fo:block ><fo:external-graphic height="6mm" src="url('http://vutts282:8090/xsl_caretaker/DaimlerChrysler.gif')"/></fo:block>
					   </fo:table-cell>
					   <fo:table-cell>
					       <fo:block   vertical-align="baseline" text-align="end" >
					       <xsl:text> SMC-Meldung: </xsl:text><xsl:value-of select="./call/pkey" />
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
					<!--
					      Subject:
					-->
				        <fo:table table-layout="fixed" space-before.optimum="30pt">
				            <fo:table-column column-width="1cm"/>
				            <fo:table-column column-width="19cm"/>
				            <fo:table-body font-size="10pt" font-family="sans-serif">
				         
				                  <!-- 1 Zeile  -->

                                    <fo:table-row line-height="50pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell >
					                     <fo:block text-align="start" >
					                     <xsl:text>Ihre Meldung </xsl:text><xsl:value-of select="./call/pkey" /><xsl:text> wird voraussichtlich bis zum </xsl:text><xsl:value-of select="./call/date_sl_overdue" /><xsl:text> fertig gestellt.</xsl:text>
							     </fo:block>
					               </fo:table-cell>
                                    </fo:table-row>
				                  <!-- Standardtext  -->
				    <fo:table-row line-height="12pt" >
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell >
								<fo:block text-align="start" ><xsl:text>Sehr geehrte Kundin,</xsl:text></fo:block>
					                  </fo:table-cell>
                                    </fo:table-row>
                                    <fo:table-row line-height="12pt" >
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
								<fo:block text-align="start" ><xsl:text>Sehr geehrter Kunde,</xsl:text></fo:block>
					                  </fo:table-cell>
                                    </fo:table-row>
			            <fo:table-row line-height="12pt" >
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                  <fo:block text-align="start" >
					                     <xsl:text>Ihre Meldung </xsl:text>
					                     <xsl:value-of select="./call/pkey" />
					                     <xsl:text> wird voraussichtlich bis zum </xsl:text>
					                     <xsl:value-of select="./call/date_sl_overdue" />
					                  </fo:block >
					                  <fo:block text-align="start" >
					                     <xsl:text> fertig gestellt werden.</xsl:text>
					                  </fo:block >
                               			         <fo:block color="white">
							<xsl:text >  </xsl:text>		
							</fo:block>
 							<fo:block text-align="start" >
					                     <xsl:text> Grund:</xsl:text>
					                  </fo:block >
                               			         <fo:block color="white">
							<xsl:text >  </xsl:text>		
							</fo:block>
					                  <fo:block text-align="start" >
					                      <xsl:value-of select="./call/sl_overdue" />
					                  </fo:block >
					                  </fo:table-cell>
                                    </fo:table-row>
			            <fo:table-row line-height="50pt" >
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
								<fo:block text-align="start"  ><xsl:text>Für weitere Fragen stehen wir gerne unter unserer Service Desk Nummer Tel. 166</xsl:text></fo:block>
					                  </fo:table-cell>
                                    </fo:table-row>
			            <fo:table-row line-height="12pt" >
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
								<fo:block text-align="start" ><xsl:text>zur Verfügung.</xsl:text></fo:block>
					                  </fo:table-cell>
                                    </fo:table-row>
			            <fo:table-row line-height="50pt" >
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
								<fo:block text-align="start" ><xsl:text>Ihr FWT Team</xsl:text></fo:block>
					                  </fo:table-cell>
                                    </fo:table-row>
				            </fo:table-body>
				        </fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
