@echo OFF
echo /*************************************************
echo * TTS_USER_ADMIN - ...create and destroy!
echo *
echo * DB-User werden gesucht und angelegt bzw.
echo * geloescht.
echo *************************************************/
echo

sqlplus testtaker/testtaker@smcdb09 @D:\AT-Jobs\ttsuser\start.sql
pause
IF NOT ERRORLEVEL 0 GOTO Fehler
GOTO Ende


:Fehler
echo Es ist ein Fehler aufgetreten!
GOTO Ende


:Ende