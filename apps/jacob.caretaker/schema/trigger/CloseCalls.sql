-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Alle dokumentierten Meldungen schließen, bei welchen der letzte Dokumentationsvorgang    #
-- #    mindestens durationInDays zurückliegt. Der letzte Dokumentationsvorgang kann sowohl an   #
-- #    der Meldung oder einer der Aufträge einer Meldungen vorgenommen worden sein.             #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  07.04.2003  initiale Erstellung                                        Andreas Sonntag     #
-- #  30.04.2003  Änderung für IMSYS: Nur Aufträge mit endstatus>0           Andreas Sonntag     #
-- #              berücksichtigen.                                                               #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE  PROCEDURE "CLOSE_CALLS"       
(	a_returnkey	    OUT INTEGER 
) AS 

   durationInDays NUMBER;
   closeFlag NUMBER;
   now DATE;
   cnt NUMBER;
   duration NUMBER;
BEGIN

   now:=SYSDATE;
   cnt:=0;
   
   -- 
   SELECT callcloseduration INTO duration FROM appprofile WHERE rownum<=1 ORDER BY pkey;
   durationInDays:=duration/(24 * 3600);
   
   FOR callrec IN (SELECT * FROM calls WHERE callstatus=8 and (now - datedocumented)>=durationInDays) LOOP
      closeFlag:=1;
      
      FOR taskrec IN (SELECT t.taskstatus, t.datedocumented FROM task t, ext_system e WHERE t.calltask=callrec.pkey and t.ext_system_key=e.pkey and e.endstatus>0) LOOP
        if taskrec.taskstatus<6 then
           closeFlag:=0;
        else
           if (now - taskrec.datedocumented)<durationInDays then
              closeFlag:=0;     
           end if;   
        end if;   
      END LOOP;
      
      if closeFlag=1 then
         cnt:=cnt+1;
         UPDATE calls SET callstatus=9, dateclosed=sysdate, datemodified=sysdate, modifiedby='eiman' WHERE pkey=callrec.pkey;
      END IF;   
   END LOOP;

-- COMMIT;

   a_returnkey:=cnt;

END close_calls;
/

