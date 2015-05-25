-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Flag transmitted für noch nicht abgerechnete Aufträge setzen, sobald sich relevante      #
-- #    Attribute des Auftrages ändern.                                                          #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  28.08.2002  initiale Erstellung                                        Andreas Sonntag     #
-- #  10.02.2003  Anpassungen bzgl. neuer Taskstatus                         Andreas Sonntag     #
-- #  07.04.2003  Auf Objekt im Task reagieren                               Andreas Sonntag     #
-- #  28.05.2003  Erweiterung bzgl. "5 Felder"                               Andreas Sonntag     #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER "SMC_TASK_TRG" BEFORE UPDATE OF 
    "SUMMARY", "TASKDONE", "TASKSTART", 
    "TASKSTATUS", "TASKTYPE_KEY", "TIMEDOCUMENTATION", "TIMEDOC_H",
    "TIMEDOC_M","TOTALTIMESPENT","TOTALTIMESPENT_H",
    "TOTALTIMESPENT_M", "PM_ORDER", "NO_RESOLVER", "OBJECT_KEY",
    "DISRUPTION_START", "DISRUPTION_H", "DISRUPTION_M",
    "PRODUCTIONLOSS_H", "PRODUCTIONLOSS_M",
    "EDVIN_BEH_CODE", "EDVIN_BGR_CODE", "EDVIN_BTL_CODE",
    "EDVIN_SHA_CODE", "EDVIN_MAH_CODE", "WORKGROUPTASK"
    ON "TASK" 
    FOR EACH ROW BEGIN
    
  -- Flag nicht setzen, sofern EIMAN die Änderungen durchführt (Grund: Vermeidung von ungewünschten Pingpong-Effekt)
  IF lower(:new.change_user) = 'eiman' THEN
    :new.change_user := 'edvin';
  ELSE
    -- Flag nicht setzen, sofern Task als abgerechnet markiert wird
    IF :new.taskstatus < 7 THEN
      :new.transmitted := 0;
    END IF;
  END IF;
END;
/

