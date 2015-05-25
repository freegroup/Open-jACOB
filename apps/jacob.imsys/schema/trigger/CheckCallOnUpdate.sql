-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Do some checks to avoid inconsistencies.                                                 #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  12.12.2002  initiale Erstellung                                        Andreas Sonntag     #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER CheckCallOnUpdate BEFORE UPDATE OF
    "MASTERCALL_KEY"
    ON "CALLS" 
    FOR EACH ROW
BEGIN

   -- do not allow to move a subcall from one call to another call
   if (:new.mastercall_key <> :old.mastercall_key) or
      (:new.mastercall_key is null and :old.mastercall_key is not null) or
      (:new.mastercall_key is not null and :old.mastercall_key is null) then
	RAISE_APPLICATION_ERROR(-20001, 'Umhängen von Untermeldungen ist nicht erlaubt!' );
   end if;

END;
/

