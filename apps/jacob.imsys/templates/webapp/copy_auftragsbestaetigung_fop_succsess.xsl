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
					<fo:region-end extent="3cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="3cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple" initial-page-number="1">
				<!--
					print the header of the page
				-->
				<fo:static-content  flow-name="xsl-region-before">
				<fo:block   border-bottom-color="black" border-bottom-width="thin" border-bottom-style="solid">
				<fo:table   table-layout="fixed">
					<fo:table-column column-width="15cm"/>
				    <fo:table-body  font-family="sans-serif">
                    <fo:table-row font-size="12pt">
					<fo:table-cell>
						<fo:block   vertical-align="baseline" text-align="start" >
					    	<xsl:text>Auftragsbestätigung mit Meldungsnummer </xsl:text>
					    	<xsl:value-of select="./call/pkey" />
					       	<xsl:text> vom </xsl:text>     
					       	<xsl:value-of select="./call/callstart"/>
					    </fo:block>
					</fo:table-cell>
                    </fo:table-row>
				    </fo:table-body>
				</fo:table>
				</fo:block>
				</fo:static-content>



				<!--
					print the content itself
				-->
				<fo:flow flow-name="xsl-region-body">
				<fo:block text-align="start" background-color="#00DD00">
					<xsl:text>erfolgreich versendet an </xsl:text>
					<xsl:value-of select="/doc/control/original_to/@url" />
				</fo:block>
				
				<!--  TO:  -->
				<fo:table table-layout="fixed" space-before.optimum="30pt">
				<fo:table-column column-width="6cm"/>
				<fo:table-column column-width="10cm"/>
				<fo:table-body font-size="10pt" font-family="sans-serif">
				
                <!-- E-MailDatum  -->
                <fo:table-row line-height="14pt">
                	<fo:table-cell>
					<fo:block  text-align="start" >
						<xsl:text>verschickt am: </xsl:text>
					</fo:block>
					</fo:table-cell>
					
					<fo:table-cell>
					<fo:block  text-align="start" >
						<xsl:value-of select="./general/date" />
						<xsl:text> / </xsl:text>
						<xsl:value-of select="./general/time" />
					</fo:block>
					</fo:table-cell>
					<fo:table-cell>

					</fo:table-cell>
				</fo:table-row>
                <!-- Endetermin  -->
                <fo:table-row line-height="14pt">
                	<fo:table-cell>
					<fo:block  text-align="start" >
						<xsl:text>mit Endetermin: </xsl:text>
					</fo:block>
					</fo:table-cell>
					
					<fo:table-cell>
					<fo:block  text-align="start" >
						<xsl:value-of select="./call/gepl_endetermin" />
					</fo:block>
					</fo:table-cell>
					<fo:table-cell>

					</fo:table-cell>
				</fo:table-row>				
	        	</fo:table-body>
	    	</fo:table>
		</fo:flow>
	  </fo:page-sequence>
    </fo:root>
  </xsl:template>
</xsl:stylesheet>
