CREATE OR REPLACE TRIGGER "AUDIT_HWGTASKTYPE_INS_TRG" BEFORE INSERT
    ON "HWGTASKTYPE"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

primarykey := :new.EXT_SYSTEM_KEY || ':' || :new.TASKTYPE_KEY || ':' || :new.TASKWORKGROUP_KEY;

-- and write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'HWGTASKTYPE', primarykey, changeinfo);

END;
/
