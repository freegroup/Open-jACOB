CREATE OR REPLACE TRIGGER "AUDIT_CATEGORYPROCESS_INS_TRG" BEFORE INSERT
    ON "CATEGORYPROCESS"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'ROUTINGINFO:"' || :new.ROUTINGINFO || '" ';

primarykey := :new.CONTRACT_KEY ||
              ':' || :new.CATEGORY_KEY ||
              ':' || :new.PROCESS_KEY ||
              ':' || :new.WORKGROUP_KEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'CATEGORYPROCESS', primarykey, changeinfo);

END;
/
