CREATE OR REPLACE TRIGGER "AUDIT_ORGANIZATION_DEL_TRG" BEFORE DELETE
    ON "ORGANIZATION"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'PORGORG:"' || :old.PORGORG || '" ' ||
              'CONTACTORG:"' || :old.CONTACTORG || '" ' ||
              'NAME:"' || :old.NAME || '" ' ||
              'ORGTYPE:"' || :old.ORGTYPE || '" ' ||
              'VIPLEVEL:"' || :old.VIPLEVEL || '" ' ||
              'DEPARTMENT:"' || :old.DEPARTMENT || '" ' ||
              'ACCOUNTINGCODE:"' || :old.ACCOUNTINGCODE || '" ' ||
              'ORGSTATUS:"' || :old.ORGSTATUS || '" ';

primarykey := :old.PKEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'ORGANIZATION', primarykey, changeinfo);
END;
/
