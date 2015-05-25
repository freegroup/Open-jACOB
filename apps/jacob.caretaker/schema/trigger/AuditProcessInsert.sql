CREATE OR REPLACE TRIGGER "AUDIT_PROCESS_INS_TRG" BEFORE INSERT
    ON "PROCESS"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'PROCESSNAME:"' || :new.PROCESSNAME || '" ' ||
              'DESCRIPTION:"' || :new.DESCRIPTION || '" ' ||
              'PROCESSSTATUS:"' || :new.PROCESSSTATUS || '" ' ||
              'PROCESS_KEY:"' || :new.PROCESS_KEY || '" ';

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'PROCESS', primarykey, changeinfo);

END;
/
