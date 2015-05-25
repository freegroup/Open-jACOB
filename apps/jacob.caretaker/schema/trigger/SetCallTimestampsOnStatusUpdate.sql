-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Setzen der Zeitstempel für fertig gemeldet und dokumentiert, falls diese von Mike's      #
-- #    Scripten nicht gesetzt werden, d.h. dieser Trigger kann entfallen sofern dieses Problem  #
-- #    in CustomerQ gelöst ist.                                                                 #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  26.11.2002  initiale Erstellung                                        Andreas Sonntag     #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER SetCallTimestampsOnStatUpdate before update of "CALLSTATUS" on "CALLS"
for each row
BEGIN

-- Callstatuswechsel auf fertig gemeldet bzw. dokumentiert
if (:new.callstatus=6 or :new.callstatus=8) and :old.callstatus<6 and :new.dateresolved is null then
   :new.dateresolved := sysdate;
end if;   
   
-- Callstatuswechsel auf dokumentiert
if :new.callstatus=8 and :old.callstatus<8 and :new.datedocumented is null then
   :new.datedocumented := sysdate;
end if;   
   
END;
/

