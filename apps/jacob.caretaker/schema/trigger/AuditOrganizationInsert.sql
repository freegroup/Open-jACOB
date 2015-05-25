CREATE OR REPLACE TRIGGER "AUDIT_ORGANIZATION_INS_TRG" BEFORE INSERT
    ON "ORGANIZATION"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'PORGORG:"' || :new.PORGORG || '" ' ||
              'CONTACTORG:"' || :new.CONTACTORG || '" ' ||
              'NAME:"' || :new.NAME || '" ' ||
              'ORGTYPE:"' || :new.ORGTYPE || '" ' ||
              'VIPLEVEL:"' || :new.VIPLEVEL || '" ' ||
              'DEPARTMENT:"' || :new.DEPARTMENT || '" ' ||
              'ACCOUNTINGCODE:"' || :new.ACCOUNTINGCODE || '" ' ||
              'ORGSTATUS:"' || :new.ORGSTATUS || '" ';

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'ORGANIZATION', primarykey, changeinfo);

END;
/
