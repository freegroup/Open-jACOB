-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Counter in Call anpassen sofern sich relevante Daten in einem Auftrag ändern.            #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  15.10.2002  initiale Erstellung                                        Andreas Sonntag     #
-- #  14.01.2003  redundaten Aufwandsdaten konsistent halten                 Andreas Sonntag     #
-- #  10.02.2003  Feld amount berücksichtigt                                 Andreas Sonntag     #
-- #  10.02.2003  Anpassungen bzgl. neuer Taskstatus                         Andreas Sonntag     #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER UpdateCountersOnTaskUpdate BEFORE UPDATE OF 
    "TASKSTATUS", 
    "TIMEDOCUMENTATION",
    "TOTALTIMESPENT",
    "TOTALTIMESPENT_M",
    "TOTALTIMESPENT_H",
    "TIMEDOC_M",
    "TIMEDOC_H",
    "AMOUNT"
    ON "TASK" 
    FOR EACH ROW
DECLARE

   amountdiff NUMBER:=0;
   timedocdiff NUMBER:=0;
   timespentdiff NUMBER:=0;
   opentaskcntdiff NUMBER:=0;
   closedtaskcntdiff NUMBER:=0;

BEGIN

   -- make time spent consistent
   if (:new.TOTALTIMESPENT <> :old.TOTALTIMESPENT) or
      (:new.TOTALTIMESPENT is not null and :old.TOTALTIMESPENT is null) then
      :new.TOTALTIMESPENT_H := trunc((:new.TOTALTIMESPENT / 3600),0);
      :new.TOTALTIMESPENT_M := to_char(trunc(((:new.TOTALTIMESPENT - (:new.TOTALTIMESPENT_H * 3600)) / 60),0),'FM09');
   else if (:new.TOTALTIMESPENT is null and :old.TOTALTIMESPENT is not null) then
      :new.TOTALTIMESPENT_H := null;
      :new.TOTALTIMESPENT_M := null;
   else
      if (:new.TOTALTIMESPENT_H is null and :new.TOTALTIMESPENT_M is null) then
         :new.TOTALTIMESPENT := null;
      else
         if (:new.TOTALTIMESPENT_H is null) then
            :new.TOTALTIMESPENT_H := '0';
         end if;
         if (:new.TOTALTIMESPENT_M is null) then
            :new.TOTALTIMESPENT_M := '0';
         end if;
         :new.TOTALTIMESPENT := 3600 * :new.TOTALTIMESPENT_H + 60 * :new.TOTALTIMESPENT_M;
         :new.TOTALTIMESPENT_M := to_char(1*:new.TOTALTIMESPENT_M,'FM09');
      end if;
   end if;
   end if;

   -- check for changes of time spent
   if (:new.TOTALTIMESPENT <> :old.TOTALTIMESPENT) then
      timespentdiff := :new.TOTALTIMESPENT - :old.TOTALTIMESPENT;
   end if;
   if (:new.TOTALTIMESPENT is null and :old.TOTALTIMESPENT is not null) then
      timespentdiff := - :old.TOTALTIMESPENT;
   end if;
   if (:new.TOTALTIMESPENT is not null and :old.TOTALTIMESPENT is null) then
      timespentdiff := :new.TOTALTIMESPENT;
   end if;


   -- make documentation time consistent
   if (:new.TIMEDOCUMENTATION <> :old.TIMEDOCUMENTATION) or
      (:new.TIMEDOCUMENTATION is not null and :old.TIMEDOCUMENTATION is null) then
      :new.timedoc_h := trunc((:new.timedocumentation / 3600),0);
      :new.timedoc_m := to_char(trunc(((:new.timedocumentation - (:new.timedoc_h * 3600)) / 60),0),'FM09');
   else if (:new.TIMEDOCUMENTATION is null and :old.TIMEDOCUMENTATION is not null) then
      :new.timedoc_h := null;
      :new.timedoc_m := null;
   else
      if (:new.TIMEDOC_H is null and :new.TIMEDOC_M is null) then
         :new.TIMEDOCUMENTATION := null;
      else
         if (:new.TIMEDOC_H is null) then
            :new.TIMEDOC_H := '0';
         end if;
         if (:new.TIMEDOC_M is null) then
            :new.TIMEDOC_M := '0';
         end if;
         :new.TIMEDOCUMENTATION := 3600 * :new.TIMEDOC_H + 60 * :new.TIMEDOC_M;
         :new.TIMEDOC_M := to_char(1*:new.TIMEDOC_M,'FM09');
      end if;
   end if;
   end if;

   -- check for changes of time documented
   if (:new.TIMEDOCUMENTATION <> :old.TIMEDOCUMENTATION) then
      timedocdiff := :new.TIMEDOCUMENTATION - :old.TIMEDOCUMENTATION;
   end if;
   if (:new.TIMEDOCUMENTATION is null and :old.TIMEDOCUMENTATION is not null) then
      timedocdiff := - :old.TIMEDOCUMENTATION;
   end if;
   if (:new.TIMEDOCUMENTATION is not null and :old.TIMEDOCUMENTATION is null) then
      timedocdiff := :new.TIMEDOCUMENTATION;
   end if;


   -- check for changes of amount
   if (:new.AMOUNT <> :old.AMOUNT) then
      amountdiff := :new.AMOUNT - :old.AMOUNT;
   end if;
   if (:new.AMOUNT is null and :old.AMOUNT is not null) then
      amountdiff := - :old.AMOUNT;
   end if;
   if (:new.AMOUNT is not null and :old.AMOUNT is null) then
      amountdiff := :new.AMOUNT;
   end if;


   -- Taskstatuswechsel von "in Arbeit" (oder kleiner) auf "fertig gemeldet" (oder höher)
   if (:new.TASKSTATUS>=5 and :old.TASKSTATUS<=4) Or (:new.TASKSTATUS=3  and :old.TASKSTATUS<>3) then
      opentaskcntdiff := -1;
      closedtaskcntdiff := 1;
   end if;


   -- and change counters in respective call
   if (timedocdiff<>0 or timespentdiff<>0 or opentaskcntdiff<>0 or closedtaskcntdiff<>0 or amountdiff<>0) then
      UPDATE calls SET
         totaltaskamount=totaltaskamount+amountdiff,
         closedtaskcount=closedtaskcount+closedtaskcntdiff,
         opentaskcount=opentaskcount+opentaskcntdiff,
         totaltasktimespent=totaltasktimespent+timespentdiff,
         totaltaskdoc=totaltaskdoc+timedocdiff
      WHERE pkey=:new.calltask;
   end if;

END;
/

