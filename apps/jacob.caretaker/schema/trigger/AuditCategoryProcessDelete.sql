CREATE OR REPLACE TRIGGER "AUDIT_CATEGORYPROCESS_DEL_TRG" BEFORE DELETE
    ON "CATEGORYPROCESS"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'ROUTINGINFO:"' || :old.ROUTINGINFO || '" ';

primarykey := :old.CONTRACT_KEY ||
              ':' || :old.CATEGORY_KEY ||
              ':' || :old.PROCESS_KEY ||
              ':' || :old.WORKGROUP_KEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'CATEGORYPROCESS', primarykey, changeinfo);
END;
/
