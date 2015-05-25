<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

		<xsl:template match="/data">
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
			
				<fo:flow flow-name="xsl-region-body">
				
 					<fo:block text-align="left"  font-size="13pt" space-before.optimum="15pt">
			             <xsl:text>Product Id: </xsl:text><xsl:value-of select="./product/pkey" />
            	    </fo:block>
 					<fo:block text-align="left"  font-size="13pt" space-before.optimum="15pt">
			             <xsl:text>Name: </xsl:text><xsl:value-of select="./product/name" />
            	    </fo:block>
 					<fo:block text-align="left"  font-size="13pt" space-before.optimum="15pt">
			             <xsl:text>Description: </xsl:text><xsl:value-of select="./product/description" />
            	    </fo:block>

				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
