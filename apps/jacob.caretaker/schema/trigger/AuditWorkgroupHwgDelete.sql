CREATE OR REPLACE TRIGGER "AUDIT_WORKGROUPHWG_DEL_TRG" BEFORE DELETE
    ON "WORKGROUPHWG"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

primarykey := :old.WORKGROUP_KEY || ':' || :old.HWG_KEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'WORKGROUPHWG', primarykey, changeinfo);
END;
/
