<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <!-- 
       Replace new lines with <fo:block>  . This is required to display preformated text to the 
       pdf-Document.
  -->
  <xsl:template name="br-replace">
    <xsl:param name="text"/>
    <xsl:variable name="cr" select="'&#10;'"/>
    <xsl:choose>
      <!-- If the value of the $text parameter contains a carriage return... -->
      <xsl:when test="contains($text,$cr)">
        <fo:block><xsl:value-of select="substring-before($text,$cr)"/></fo:block>
          <xsl:call-template name="br-replace">
            <xsl:with-param name="text" select="substring-after($text,$cr)"/>
          </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
   </xsl:choose>
  </xsl:template>


	<!--
		The main template. Processing starts here
	-->
	<xsl:template match="/">
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
					       <xsl:text> Nachricht gesendet am: </xsl:text><xsl:value-of select="/doc/data/date" />
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
					</fo:block>
				</fo:static-content>

				<!--
					print the content itself
				-->

 				<fo:flow flow-name="xsl-region-body">
<fo:block text-align="center" background-color="#00DD00"><xsl:text>erfolgreich versendet an:</xsl:text><xsl:value-of select="/doc/control/original_to/@url" /></fo:block>
<fo:block   vertical-align="baseline" text-align="start"  space-before.optimum="5pt">
  <xsl:text> von: </xsl:text><xsl:value-of select="/doc/data/from" />
</fo:block>
<fo:block   vertical-align="baseline" text-align="start"  space-before.optimum="5pt">
  <xsl:text> CC: </xsl:text><xsl:value-of select="/doc/data/cc" />
</fo:block>
<fo:block   vertical-align="baseline" text-align="start" space-before.optimum="5pt">
  <xsl:text>Betreff: </xsl:text><xsl:value-of select="/doc/data/subject" />
</fo:block>
<fo:block text-align="start" space-before.optimum="20pt" ><xsl:text>Body:</xsl:text></fo:block>
				<fo:block  text-align="start" >
			         <xsl:call-template name="br-replace">
			           <xsl:with-param name="text">
						<xsl:value-of select="./doc/data/body" />
			           </xsl:with-param>
			         </xsl:call-template>
         </fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>

