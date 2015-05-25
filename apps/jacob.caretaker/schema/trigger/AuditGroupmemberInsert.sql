CREATE OR REPLACE TRIGGER "AUDIT_GROUPMEMBER_INS_TRG" BEFORE INSERT
    ON "GROUPMEMBER"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'EMPLOYEEGROUP:"' || :new.EMPLOYEEGROUP || '" ' ||
              'WORKGROUPGROUP:"' || :new.WORKGROUPGROUP || '" ' ||
              'NOTIFYMETHOD:"' || :new.NOTIFYMETHOD || '" ';

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'GROUPMEMBER', primarykey, changeinfo);

END;
/
