CREATE OR REPLACE TRIGGER "AUDIT_CONTRACT_UPD_TRG" BEFORE UPDATE
    OF "CONTRACTNUM", "DESCRIPTION"
    ON "CONTRACT"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.CONTRACTNUM <> :old.CONTRACTNUM) then
   changeinfo := changeinfo || 'CONTRACTNUM:"' || :old.CONTRACTNUM || '"->"' || :new.CONTRACTNUM || '" ';
   hasChanged:=1;
end if;

if (:new.DESCRIPTION <> :old.DESCRIPTION) or
   (:new.DESCRIPTION is null and :old.DESCRIPTION is not null) or
   (:new.DESCRIPTION is not null and :old.DESCRIPTION is null) then
   changeinfo := changeinfo || 'DESCRIPTION:"' || :old.DESCRIPTION || '"->"' || :new.DESCRIPTION || '" ';
   hasChanged:=1;
end if;

primarykey := :new.pkey;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'CONTRACT', primarykey, changeinfo);
end if;
END;
/
