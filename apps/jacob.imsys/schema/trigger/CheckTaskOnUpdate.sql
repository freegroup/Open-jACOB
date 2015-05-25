-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Do some checks to avoid inconsistencies.                                                 #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  12.12.2002  initiale Erstellung                                        Andreas Sonntag     #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER CheckTaskOnUpdate BEFORE UPDATE OF
    "CALLTASK"
    ON "TASK" 
    FOR EACH ROW
BEGIN

   -- do not allow to move a task from one call to another call
   if (:new.calltask <> :old.calltask) then
	RAISE_APPLICATION_ERROR(-20001, 'Umhängen von Aufträgen ist nicht erlaubt!' );
   end if;

END;
/

