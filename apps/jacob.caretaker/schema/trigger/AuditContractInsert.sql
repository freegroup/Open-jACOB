CREATE OR REPLACE TRIGGER "AUDIT_CONTRACT_INS_TRG" BEFORE INSERT
    ON "CONTRACT"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'CONTRACTNUM:"' || :new.CONTRACTNUM || '" ' ||
              'DESCRIPTION:"' || :new.DESCRIPTION || '" ';

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'CONTRACT', primarykey, changeinfo);

END;
/
