CREATE OR REPLACE TRIGGER "AUDIT_ACCOUNTINGCODE_DEL_TRG" BEFORE DELETE
    ON "ACCOUNTINGCODE"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'CONTRACT_KEY:"' || :old.CONTRACT_KEY || '" ' ||
              'ACCOUNTINGSTATUS:"' || :old.ACCOUNTINGSTATUS || '" ' ||
              'CENTER:"' || :old.CENTER || '" ' ||
              'DEPARTMENT:"' || :old.DEPARTMENT || '" ';

primarykey := :old.CODE;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'ACCOUNTINGCODE', primarykey, changeinfo);
END;
/
