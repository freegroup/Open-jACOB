CREATE OR REPLACE TRIGGER "AUDIT_PROCESS_DEL_TRG" BEFORE DELETE
    ON "PROCESS"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'PROCESSNAME:"' || :old.PROCESSNAME || '" ' ||
              'DESCRIPTION:"' || :old.DESCRIPTION || '" ' ||
              'PROCESSSTATUS:"' || :old.PROCESSSTATUS || '" ' ||
              'PROCESS_KEY:"' || :old.PROCESS_KEY || '" ';

primarykey := :old.PKEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'PROCESS', primarykey, changeinfo);
END;
/
