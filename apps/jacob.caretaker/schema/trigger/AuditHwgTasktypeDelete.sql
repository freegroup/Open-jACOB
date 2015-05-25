CREATE OR REPLACE TRIGGER "AUDIT_HWGTASKTYPE_DEL_TRG" BEFORE DELETE
    ON "HWGTASKTYPE"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

primarykey := :old.EXT_SYSTEM_KEY || ':' || :old.TASKTYPE_KEY || ':' || :old.TASKWORKGROUP_KEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'HWGTASKTYPE', primarykey, changeinfo);
END;
/
