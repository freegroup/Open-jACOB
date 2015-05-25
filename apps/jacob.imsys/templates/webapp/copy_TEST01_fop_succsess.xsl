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
						<fo:block ><fo:external-graphic height="6mm" src="url('http://53.186.5.110/yan/DaimlerChrysler.gif')"/></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block   vertical-align="baseline" text-align="end" >
					    	<xsl:text> Auftrag: </xsl:text><xsl:value-of select="./call/callno" />
					    	<xsl:text> vom: </xsl:text>     
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
						 <xsl:text >FWT-SMC Tel:166 eMail:FWT.Servicedesk@DaimlerChrysler.com</xsl:text>
				    <xsl:text>**************************************************************************</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>***  IM-Servicecenter                                                  ***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>***    Tel   : 0711/17-21777 (07 Uhr - 17 Uhr)                         ***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>***    FAX   : 0711/17-35199                                           ***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>***    E-Mail: Pool-ID.Servicecenter_BS_IM@DaimlerChrysler.Com         ***</xsl:text><xsl:text>&#13;&#10;</xsl:text>
                    <xsl:text>**************************************************************************</xsl:text>
                    <xsl:text>&#13;&#10;</xsl:text>
					</fo:block>
				</fo:static-content>

				<!--
					print the content itself
				-->
				<fo:flow flow-name="xsl-region-body">
				<fo:block text-align="center" background-color="#00DD00"><xsl:text>erfolgreich versendet an:</xsl:text><xsl:value-of select="/doc/control/original_to/@url" /></fo:block>				
				

				                  <!--  NAME  -->
                              <fo:table-row line-height="12pt">
					                  <fo:table-cell>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block text-align="start" ><xsl:text>Name:</xsl:text></fo:block>
					                  </fo:table-cell>
					                  <fo:table-cell>
					                     <fo:block  text-align="start" ><xsl:value-of select="./agent/fullname" /></fo:block>
					                  </fo:table-cell>
                              </fo:table-row>

			
			

				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
