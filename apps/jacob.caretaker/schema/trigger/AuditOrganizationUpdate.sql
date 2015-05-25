CREATE OR REPLACE TRIGGER "AUDIT_ORGANIZATION_UPD_TRG" BEFORE UPDATE
    OF "PORGORG", "CONTACTORG", "NAME", "ORGTYPE", "VIPLEVEL", "DEPARTMENT",
       "ACCOUNTINGCODE", "ORGSTATUS"
    ON "ORGANIZATION"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.PORGORG <> :old.PORGORG) or
   (:new.PORGORG is null and :old.PORGORG is not null) or
   (:new.PORGORG is not null and :old.PORGORG is null) then
   changeinfo := changeinfo || 'PORGORG:"' || :old.PORGORG || '"->"' || :new.PORGORG || '" ';
   hasChanged:=1;
end if;

if (:new.CONTACTORG <> :old.CONTACTORG) or
   (:new.CONTACTORG is null and :old.CONTACTORG is not null) or
   (:new.CONTACTORG is not null and :old.CONTACTORG is null) then
   changeinfo := changeinfo || 'CONTACTORG:"' || :old.CONTACTORG || '"->"' || :new.CONTACTORG || '" ';
   hasChanged:=1;
end if;

if (:new.NAME <> :old.NAME) then
   changeinfo := changeinfo || 'NAME:"' || :old.NAME || '"->"' || :new.NAME || '" ';
   hasChanged:=1;
end if;

if (:new.ORGTYPE <> :old.ORGTYPE) or
   (:new.ORGTYPE is null and :old.ORGTYPE is not null) or
   (:new.ORGTYPE is not null and :old.ORGTYPE is null) then
   changeinfo := changeinfo || 'ORGTYPE:"' || :old.ORGTYPE || '"->"' || :new.ORGTYPE || '" ';
   hasChanged:=1;
end if;

if (:new.VIPLEVEL <> :old.VIPLEVEL) or
   (:new.VIPLEVEL is null and :old.VIPLEVEL is not null) or
   (:new.VIPLEVEL is not null and :old.VIPLEVEL is null) then
   changeinfo := changeinfo || 'VIPLEVEL:"' || :old.VIPLEVEL || '"->"' || :new.VIPLEVEL || '" ';
   hasChanged:=1;
end if;

if (:new.DEPARTMENT <> :old.DEPARTMENT) or
   (:new.DEPARTMENT is null and :old.DEPARTMENT is not null) or
   (:new.DEPARTMENT is not null and :old.DEPARTMENT is null) then
   changeinfo := changeinfo || 'DEPARTMENT:"' || :old.DEPARTMENT || '"->"' || :new.DEPARTMENT || '" ';
   hasChanged:=1;
end if;

if (:new.ACCOUNTINGCODE <> :old.ACCOUNTINGCODE) or
   (:new.ACCOUNTINGCODE is null and :old.ACCOUNTINGCODE is not null) or
   (:new.ACCOUNTINGCODE is not null and :old.ACCOUNTINGCODE is null) then
   changeinfo := changeinfo || 'ACCOUNTINGCODE:"' || :old.ACCOUNTINGCODE || '"->"' || :new.ACCOUNTINGCODE || '" ';
   hasChanged:=1;
end if;

if (:new.ORGSTATUS <> :old.ORGSTATUS) or
   (:new.ORGSTATUS is null and :old.ORGSTATUS is not null) or
   (:new.ORGSTATUS is not null and :old.ORGSTATUS is null) then
   changeinfo := changeinfo || 'ORGSTATUS:"' || :old.ORGSTATUS || '"->"' || :new.ORGSTATUS || '" ';
   hasChanged:=1;
end if;

primarykey := :new.pkey;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'ORGANIZATION', primarykey, changeinfo);
end if;
END;
/
