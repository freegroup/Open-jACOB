CREATE OR REPLACE TRIGGER "AUDIT_GROUPMEMBER_DEL_TRG" BEFORE DELETE
    ON "GROUPMEMBER"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'EMPLOYEEGROUP:"' || :old.EMPLOYEEGROUP || '" ' ||
              'WORKGROUPGROUP:"' || :old.WORKGROUPGROUP || '" ' ||
              'NOTIFYMETHOD:"' || :old.NOTIFYMETHOD || '" ';

primarykey := :old.PKEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'GROUPMEMBER', primarykey, changeinfo);
END;
/
