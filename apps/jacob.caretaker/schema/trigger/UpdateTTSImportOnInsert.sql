-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Zeitstempel automatisch setzen.                                                          #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  22.12.2004  initiale Erstellung                                        Mike Doering        #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER UpdateTTSImportOnInsert BEFORE INSERT
    ON "TTS_IMPORT" 
    FOR EACH ROW

BEGIN
   :new.timestamp := sysdate;
  
END;
/
