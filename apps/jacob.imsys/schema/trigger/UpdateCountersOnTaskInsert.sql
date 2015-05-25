-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Counter im Call beim Anlegen eines neuen Auftrages anpassen.                             #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  15.10.2002  initiale Erstellung                                        Andreas Sonntag     #
-- #  28.08.2003  Erweiterung für Auftragsnummernerstellung                  Andreas Sonntag     #
-- #  06.12.2005  Erweiterung für Auftragsnummernformatierung                Mike Doering        #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER UpdateCountersOnTaskInsert BEFORE INSERT
    ON "TASK" 
    FOR EACH ROW
declare
   val2          integer;
BEGIN

   SELECT count(pkey) into val2 from task where calltask = :new.calltask;
   :new.taskno := :new.calltask || '/' || LPAD((val2+1),3,'0');
     if (:new.TASKSTATUS>=5 and :old.TASKSTATUS<=4) Or (:new.TASKSTATUS=3  and :old.TASKSTATUS<>3) then
        UPDATE calls SET
      	closedtaskcount=closedtaskcount+1
   	WHERE pkey=:new.calltask;
     else
   	UPDATE calls SET
     	opentaskcount=opentaskcount+1
   	WHERE pkey=:new.calltask;
   end if;

END;
/
