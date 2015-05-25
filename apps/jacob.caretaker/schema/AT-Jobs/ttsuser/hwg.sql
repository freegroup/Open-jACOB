SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 50000
delete from workgrouphwg;
DECLARE
   nrec NUMBER; 
   cnt NUMBER;
BEGIN
   DBMS_OUTPUT.PUT_LINE('Updating workgrouphwg...');
   cnt:=0;
   FOR hwgrec IN (SELECT * FROM workgroup where wrkgrptype=1) LOOP

      FOR akrec IN (SELECT * FROM workgroup WHERE wrkgrptype = 0 or wrkgrptype = 3) LOOP
        select count(*) into nrec from workgrouphwg wh where wh.workgroup_key = akrec.pkey and wh.hwg_key =hwgrec.pkey ; 
        if  nrec= 1 then
             DBMS_OUTPUT.PUT_LINE(' AK/OWNER= ' || akrec.pkey || ' and hwg= ' || hwgrec.pkey || 'exists' );
        else
             DBMS_OUTPUT.PUT_LINE(' AK/OWNER= ' || akrec.pkey || ' and hwg= ' || hwgrec.pkey || 'does not exist' );
             insert into workgrouphwg (workgroup_key, hwg_key) values ( akrec.pkey , hwgrec.pkey) ;
             cnt:=cnt+1;
        end if;         
      END LOOP;
   END LOOP;

   IF cnt>0 THEN
      DBMS_OUTPUT.PUT_LINE(cnt || ' new records made');         
   END IF;   

   DBMS_OUTPUT.PUT_LINE('Done!');         
END;
/
commit;
