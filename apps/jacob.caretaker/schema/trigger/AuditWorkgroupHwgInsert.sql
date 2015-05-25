CREATE OR REPLACE TRIGGER "AUDIT_WORKGROUPHWG_INS_TRG" BEFORE INSERT
    ON "WORKGROUPHWG"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

primarykey := :new.WORKGROUP_KEY || ':' || :new.HWG_KEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'WORKGROUPHWG', primarykey, changeinfo);

END;
/
