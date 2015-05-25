CREATE OR REPLACE TRIGGER "AUDIT_ACCOUNTINGCODE_UPD_TRG" BEFORE UPDATE
    OF "CONTRACT_KEY", "ACCOUNTINGSTATUS", "CENTER", "DEPARTMENT"
    ON "ACCOUNTINGCODE"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.CONTRACT_KEY <> :old.CONTRACT_KEY) or
   (:new.CONTRACT_KEY is null and :old.CONTRACT_KEY is not null) or
   (:new.CONTRACT_KEY is not null and :old.CONTRACT_KEY is null) then
   changeinfo := changeinfo || 'CONTRACT_KEY:"' || :old.CONTRACT_KEY || '"->"' || :new.CONTRACT_KEY || '" ';
   hasChanged:=1;
end if;

if (:new.ACCOUNTINGSTATUS <> :old.ACCOUNTINGSTATUS) or
   (:new.ACCOUNTINGSTATUS is null and :old.ACCOUNTINGSTATUS is not null) or
   (:new.ACCOUNTINGSTATUS is not null and :old.ACCOUNTINGSTATUS is null) then
   changeinfo := changeinfo || 'ACCOUNTINGSTATUS:"' || :old.ACCOUNTINGSTATUS || '"->"' || :new.ACCOUNTINGSTATUS || '" ';
   hasChanged:=1;
end if;

if (:new.CENTER <> :old.CENTER) or
   (:new.CENTER is null and :old.CENTER is not null) or
   (:new.CENTER is not null and :old.CENTER is null) then
   changeinfo := changeinfo || 'CENTER:"' || :old.CENTER || '"->"' || :new.CENTER || '" ';
   hasChanged:=1;
end if;

if (:new.DEPARTMENT <> :old.DEPARTMENT) or
   (:new.DEPARTMENT is null and :old.DEPARTMENT is not null) or
   (:new.DEPARTMENT is not null and :old.DEPARTMENT is null) then
   changeinfo := changeinfo || 'DEPARTMENT:"' || :old.DEPARTMENT || '"->"' || :new.DEPARTMENT || '" ';
   hasChanged:=1;
end if;

primarykey := :new.CODE;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'ACCOUNTINGCODE', primarykey, changeinfo);
end if;
END;
/
