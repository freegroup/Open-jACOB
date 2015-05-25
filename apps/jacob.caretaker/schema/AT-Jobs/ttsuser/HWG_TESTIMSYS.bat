@echo OFF
echo /*************************************************
echo * HWG IMSYS!
echo *
echo * Handwerker  werden allen AK und Owner zugeordnet
echo *************************************************/
echo

sqlplus testimsys/testimsys@smcdbs @D:\AT-Jobs\ttsuser\hwg.sql
pause
IF NOT ERRORLEVEL 0 GOTO Fehler
GOTO Ende


:Fehler
echo Es ist ein Fehler aufgetreten!
GOTO Ende


:Ende