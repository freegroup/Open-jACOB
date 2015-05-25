CREATE OR REPLACE TRIGGER "AUDIT_ACCOUNTINGCODE_INS_TRG" BEFORE INSERT
    ON "ACCOUNTINGCODE"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'CONTRACT_KEY:"' || :new.CONTRACT_KEY || '" ' ||
              'ACCOUNTINGSTATUS:"' || :new.ACCOUNTINGSTATUS || '" ' ||
              'CENTER:"' || :new.CENTER || '" ' ||
              'DEPARTMENT:"' || :new.DEPARTMENT || '" ';

primarykey := :new.CODE;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'ACCOUNTINGCODE', primarykey, changeinfo);

END;
/
