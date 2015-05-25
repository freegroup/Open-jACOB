REM enable the extensions for the xalan parser
REM 
set CLASSPATH=C:\java\docbook-xsl-1.65.1\extensions\xalan2.jar

java org.apache.xalan.xslt.Process -IN bookSet.xml -XSL C:/java/docbook-xsl-1.65.1/fo/docbook.xsl -PARAM use.extensions 1 -OUT output.fo
C:\java\fop-0.20.5\fop.bat -fo output.fo -pdf jACOB-Programming.pdf
start output.pdf