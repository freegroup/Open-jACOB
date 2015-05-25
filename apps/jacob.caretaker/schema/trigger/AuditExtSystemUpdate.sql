CREATE OR REPLACE TRIGGER "AUDIT_EXT_SYSTEM_UPD_TRG" BEFORE UPDATE
    OF "NAME", "CONNECT_INFO", "CONNECT_USER", "CONNECT_PWD", "SYSTEMTYPE", "SYSTEMSTATUS", "FLUSHCATEGORIES", "FETCHHWGDATA"
    ON "EXT_SYSTEM"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.NAME <> :old.NAME) or
   (:new.NAME is null and :old.NAME is not null) or
   (:new.NAME is not null and :old.NAME is null) then
   changeinfo := changeinfo || 'NAME:"' || :old.NAME || '"->"' || :new.NAME || '" ';
   hasChanged:=1;
end if;

if (:new.CONNECT_INFO <> :old.CONNECT_INFO) or
   (:new.CONNECT_INFO is null and :old.CONNECT_INFO is not null) or
   (:new.CONNECT_INFO is not null and :old.CONNECT_INFO is null) then
   changeinfo := changeinfo || 'CONNECT_INFO:"' || :old.CONNECT_INFO || '"->"' || :new.CONNECT_INFO || '" ';
   hasChanged:=1;
end if;

if (:new.CONNECT_USER <> :old.CONNECT_USER) or
   (:new.CONNECT_USER is null and :old.CONNECT_USER is not null) or
   (:new.CONNECT_USER is not null and :old.CONNECT_USER is null) then
   changeinfo := changeinfo || 'CONNECT_USER:"' || :old.CONNECT_USER || '"->"' || :new.CONNECT_USER || '" ';
   hasChanged:=1;
end if;

if (:new.CONNECT_PWD <> :old.CONNECT_PWD) or
   (:new.CONNECT_PWD is null and :old.CONNECT_PWD is not null) or
   (:new.CONNECT_PWD is not null and :old.CONNECT_PWD is null) then
   changeinfo := changeinfo || 'CONNECT_PWD:"' || :old.CONNECT_PWD || '"->"' || :new.CONNECT_PWD || '" ';
   hasChanged:=1;
end if;

if (:new.SYSTEMTYPE <> :old.SYSTEMTYPE) or
   (:new.SYSTEMTYPE is null and :old.SYSTEMTYPE is not null) or
   (:new.SYSTEMTYPE is not null and :old.SYSTEMTYPE is null) then
   changeinfo := changeinfo || 'SYSTEMTYPE:"' || :old.SYSTEMTYPE || '"->"' || :new.SYSTEMTYPE || '" ';
   hasChanged:=1;
end if;

if (:new.SYSTEMSTATUS <> :old.SYSTEMSTATUS) or
   (:new.SYSTEMSTATUS is null and :old.SYSTEMSTATUS is not null) or
   (:new.SYSTEMSTATUS is not null and :old.SYSTEMSTATUS is null) then
   changeinfo := changeinfo || 'SYSTEMSTATUS:"' || :old.SYSTEMSTATUS || '"->"' || :new.SYSTEMSTATUS || '" ';
   hasChanged:=1;
end if;

if (:new.FLUSHCATEGORIES <> :old.FLUSHCATEGORIES) or
   (:new.FLUSHCATEGORIES is null and :old.FLUSHCATEGORIES is not null) or
   (:new.FLUSHCATEGORIES is not null and :old.FLUSHCATEGORIES is null) then
   changeinfo := changeinfo || 'FLUSHCATEGORIES:"' || :old.FLUSHCATEGORIES || '"->"' || :new.FLUSHCATEGORIES || '" ';
   hasChanged:=1;
end if;

if (:new.FETCHHWGDATA <> :old.FETCHHWGDATA) or
   (:new.FETCHHWGDATA is null and :old.FETCHHWGDATA is not null) or
   (:new.FETCHHWGDATA is not null and :old.FETCHHWGDATA is null) then
   changeinfo := changeinfo || 'FETCHHWGDATA:"' || :old.FETCHHWGDATA || '"->"' || :new.FETCHHWGDATA || '" ';
   hasChanged:=1;
end if;

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'EXT_SYSTEM', primarykey, changeinfo);
end if;
END;
/
