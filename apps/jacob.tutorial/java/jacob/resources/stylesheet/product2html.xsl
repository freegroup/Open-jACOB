<?xml version="1.0" encoding="utf-8"?>
<!--
	Author: 
	File: 
	Date: 
	Purpose: 
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" encoding="UTF-8"/>
	<xsl:template match="/data">
	<html>
		<body>
		    <h1>Product</h1>
		    <table>
		    	<tr>
		    		<td>Product Id</td><td><xsl:value-of select="./product/pkey" /></td>
		    	</tr>
		    	<tr>
		    		<td>Name</td><td><xsl:value-of select="./product/name" /></td>
		    	</tr>
		    	<tr>
		    		<td>Description</td><td><xsl:value-of select="./product/description" /></td>
		    	</tr>
			</table>
		</body>
	</html>
	</xsl:template>
</xsl:stylesheet>