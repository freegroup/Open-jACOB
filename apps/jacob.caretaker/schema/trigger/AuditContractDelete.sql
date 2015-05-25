CREATE OR REPLACE TRIGGER "AUDIT_CONTRACT_DEL_TRG" BEFORE DELETE
    ON "CONTRACT"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'CONTRACTNUM:"' || :old.CONTRACTNUM || '" ' ||
              'DESCRIPTION:"' || :old.DESCRIPTION || '" ';

primarykey := :old.PKEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'CONTRACT', primarykey, changeinfo);
END;
/
