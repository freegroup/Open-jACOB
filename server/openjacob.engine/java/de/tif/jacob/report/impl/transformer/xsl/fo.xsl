<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!--
		The main template. Processing starts here
	-->
	<xsl:template match="/report">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simple" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="0.5cm" margin-left="1.5cm" margin-right="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="1cm" overflow="hidden"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="1cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple" initial-page-number="1" white-space-treatment="preserve" linefeed-treatment="preserve">
				<!--
					print the header of the page
				-->
				<xsl:if test="./page/header">
					<fo:static-content flow-name="xsl-region-before">
						<xsl:apply-templates select="./page/header"/>
					</fo:static-content>
				</xsl:if>
				<!--
					print the footer of the page
				-->
				<xsl:if test="./page/footer">
					<fo:static-content flow-name="xsl-region-after">
						<xsl:apply-templates select="./page/footer"/>
					</fo:static-content>
				</xsl:if>
				<!--
					print the body of the page
				-->
				<fo:flow flow-name="xsl-region-body" overflow="hidden">
					<xsl:apply-templates select="./prologue"/>
					<xsl:choose>
						<xsl:when test="./groups">
							<xsl:apply-templates select="./groups"/>
						</xsl:when>
						<xsl:when test="./records">
							<xsl:apply-templates select="./records"/>
						</xsl:when>
						<xsl:otherwise>
							<fo:table table-layout="fixed" width="100%">
								<!-- -->
								<xsl:for-each select="/report/labels/label">
									<fo:table-column>
										<xsl:attribute name="column-width">proportional-column-width(<xsl:value-of select="@width"/>)</xsl:attribute>
									</fo:table-column>
								</xsl:for-each>
								<!-- -->
								<fo:table-header font-size="8pt" font-family="Courier" font-weight="bold">
									<fo:table-row>
										<xsl:for-each select="/report/labels/label">
											<fo:table-cell padding="3pt" border="0.5pt solid black">
												<fo:block>
													<xsl:call-template name="CellValue"/>
												</fo:block>
											</fo:table-cell>
										</xsl:for-each>
									</fo:table-row>
								</fo:table-header>
								<fo:table-body font-size="8pt" font-family="Courier">
									<fo:table-row>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:apply-templates select="./epilogue"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="group">
		<xsl:choose>
			<xsl:when test="@ident">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column>
						<xsl:attribute name="column-width"><xsl:value-of select="9 * number(@ident)"/>pt</xsl:attribute>
					</fo:table-column>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell column-number="2">
								<xsl:call-template name="Group"/>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="Group"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="Group">
		<xsl:apply-templates select="header"/>
		<fo:table table-layout="fixed" width="100%">
			<fo:table-body font-size="9pt" font-family="Courier">
				<fo:table-row>
					<fo:table-cell>
						<xsl:apply-templates select="./groups"/>
						<xsl:apply-templates select="./records"/>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<xsl:apply-templates select="footer"/>
	</xsl:template>
	<xsl:template match="groups">
		<xsl:apply-templates select="./group"/>
	</xsl:template>
	<xsl:template match="records">
		<fo:table table-layout="fixed" width="100%">
			<!-- -->
			<xsl:for-each select="/report/labels/label">
				<fo:table-column>
					<xsl:attribute name="column-width">proportional-column-width(<xsl:value-of select="@width"/>)</xsl:attribute>
				</fo:table-column>
			</xsl:for-each>
			<!-- -->
			<fo:table-header font-size="8pt" font-family="Courier" font-weight="bold">
				<fo:table-row>
					<xsl:for-each select="/report/labels/label">
						<fo:table-cell padding="3pt" border="0.5pt solid black">
							<fo:block>
								<xsl:call-template name="CellValue"/>
							</fo:block>
						</fo:table-cell>
					</xsl:for-each>
				</fo:table-row>
			</fo:table-header>
			<!-- -->
			<fo:table-body font-size="8pt" font-family="Courier">
				<xsl:for-each select="record">
					<!-- record header-->
					<xsl:if test="./header">
						<fo:table-row>
							<fo:table-cell padding="3pt" border="0.5pt solid black">
								<xsl:attribute name="number-columns-spanned"><xsl:value-of select="count(value)"/></xsl:attribute>
								<xsl:apply-templates select="header"/>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
					<!-- -->
					<fo:table-row>
						<xsl:for-each select="value">
							<fo:table-cell padding="3pt" border="0.5pt solid black" overflow="hidden">
								<fo:block>
									<xsl:call-template name="CellValue"/>
								</fo:block>
							</fo:table-cell>
						</xsl:for-each>
					</fo:table-row>
					<!-- record footer-->
					<xsl:if test="./footer">
						<fo:table-row>
							<fo:table-cell padding="3pt" border="0.5pt solid black">
								<xsl:attribute name="number-columns-spanned"><xsl:value-of select="count(value)"/></xsl:attribute>
								<xsl:apply-templates select="footer"/>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
					<!-- -->
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<xsl:template match="prologue">
		<xsl:call-template name="Caption"/>
	</xsl:template>
	<xsl:template match="epilogue">
		<xsl:call-template name="Caption"/>
	</xsl:template>
	<xsl:template match="header">
		<xsl:call-template name="Caption"/>
	</xsl:template>
	<xsl:template match="footer">
		<xsl:call-template name="Caption"/>
	</xsl:template>
	<xsl:template name="CellValue">
		<!-- handle justification -->
		<xsl:choose>
			<xsl:when test="@justification='center'">
				<xsl:attribute name="text-align">center</xsl:attribute>
			</xsl:when>
			<xsl:when test="@justification='right'">
				<xsl:attribute name="text-align">right</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="text-align">left</xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
		<!-- handle value -->
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template name="Caption">
		<xsl:for-each select="block">
			<fo:block>
				<xsl:if test="@newPage='true'">
					<xsl:attribute name="break-before">page</xsl:attribute>
				</xsl:if>
				<fo:table table-layout="fixed" width="100%">
					<fo:table-body font-size="9pt" font-family="Courier">
						<fo:table-row>
							<xsl:apply-templates select="left"/>
							<xsl:apply-templates select="center"/>
							<xsl:apply-templates select="right"/>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="left">
		<fo:table-cell>
			<fo:block text-align="left">
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="center">
		<fo:table-cell>
			<fo:block text-align="center">
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="right">
		<fo:table-cell>
			<fo:block text-align="right">
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="text()">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="page-number">
		<fo:page-number/>
	</xsl:template>
</xsl:stylesheet>
