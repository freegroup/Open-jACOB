-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Flag transmitted für alle dokumentierten Aufträge setzen, sobald die zugehörige Meldung  #
-- #    geschlossen wird.                                                                        #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  17.09.2002  initiale Erstellung                                        Andreas Sonntag     #
-- #  10.02.2003  Anpassungen bzgl. neuer Taskstatus                         Andreas Sonntag     #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER TransmitTasksOnCallCloseUpdate before update of "CALLSTATUS" on "CALLS"
for each row
BEGIN

-- Callstatuswechsel von dokumentiert auf geschlossen
if :new.callstatus=9 and :old.callstatus<>9 then
   update task set transmitted=0 where transmitted=1 and taskstatus=6 and calltask=:new.pkey;
end if;   
   
END;
/

