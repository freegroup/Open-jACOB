@ ECHO OFF
rem
rem This batch file runs the Jiffie JUnit tests. Please change the value of
rem the jiffie.junit.data dir property to the correct location for your
rem installation of jiffie before running the batch file
rem


@echo. >jacobtest.log

:START
@echo. >>jacobtest.log
@echo. >>jacobtest.log
@echo --------------------------------------------------------------------------- >>jacobtest.log
@echo Starting new test run >>jacobtest.log
@DATE /T >>jacobtest.log
@TIME /T >>jacobtest.log
@echo. >>jacobtest.log
java  -cp ./classes/;./lib/jiffie.jar;./lib/jacob.jar;./lib/junit.jar test.AllTests  >>jacobtest.log 2>>jacoberr.log
@echo --------------------------------------------------------------------------- >>jacobtest.log

@REM restart the whole thing
@goto START
