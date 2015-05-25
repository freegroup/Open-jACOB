-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Dieser Trigger stellt sicher das die redundaten Aufwandsdaten konsistent bleiben.        #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  14.01.2003  initiale Erstellung                                        Andreas Sonntag     #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER UpdateEffortOnCallUpdate BEFORE UPDATE OF
    "COORDINATIONTIME",
    "COORDINATIONTIME_H",
    "COORDINATIONTIME_M"
    ON "CALLS" 
    FOR EACH ROW
BEGIN

   -- make documentation time consistent
   if (:new.COORDINATIONTIME <> :old.COORDINATIONTIME) or
      (:new.COORDINATIONTIME is not null and :old.COORDINATIONTIME is null) then
      :new.COORDINATIONTIME_H := trunc((:new.COORDINATIONTIME / 3600),0);
      :new.COORDINATIONTIME_M := to_char(trunc(((:new.COORDINATIONTIME - (:new.COORDINATIONTIME_H * 3600)) / 60),0),'FM09');
   else if (:new.COORDINATIONTIME is null and :old.COORDINATIONTIME is not null) then
      :new.COORDINATIONTIME_H := null;
      :new.COORDINATIONTIME_M := null;
   else
      if (:new.COORDINATIONTIME_H is null and :new.COORDINATIONTIME_M is null) then
         :new.COORDINATIONTIME := null;
      else
         if (:new.COORDINATIONTIME_H is null) then
            :new.COORDINATIONTIME_H := '0';
         end if;
         if (:new.COORDINATIONTIME_M is null) then
            :new.COORDINATIONTIME_M := '0';
         end if;
         :new.COORDINATIONTIME := 3600 * :new.COORDINATIONTIME_H + 60 * :new.COORDINATIONTIME_M;
         :new.COORDINATIONTIME_M := to_char(1*:new.COORDINATIONTIME_M,'FM09');
      end if;
   end if;
   end if;

END;
/

