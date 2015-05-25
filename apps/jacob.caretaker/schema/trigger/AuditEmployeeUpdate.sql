CREATE OR REPLACE TRIGGER "AUDIT_EMPLOYEE_UPD_TRG" BEFORE UPDATE
    OF "LOGINNAME", "AVAILABILITY", "SUPPORTROLE", "PM_ROLE", "AK_ROLE", "WARTE_ROLE", "SDADMIN_ROLE", "ADMIN_ROLE"
    ON "EMPLOYEE"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.LOGINNAME <> :old.LOGINNAME) or
   (:new.LOGINNAME is null and :old.LOGINNAME is not null) or
   (:new.LOGINNAME is not null and :old.LOGINNAME is null) then
   changeinfo := changeinfo || 'LOGINNAME:"' || :old.LOGINNAME || '"->"' || :new.LOGINNAME || '" ';
   hasChanged:=1;
end if;

if (:new.AVAILABILITY <> :old.AVAILABILITY) or
   (:new.AVAILABILITY is null and :old.AVAILABILITY is not null) or
   (:new.AVAILABILITY is not null and :old.AVAILABILITY is null) then
   changeinfo := changeinfo || 'AVAILABILITY:"' || :old.AVAILABILITY || '"->"' || :new.AVAILABILITY || '" ';
   hasChanged:=1;
end if;

if (:new.SUPPORTROLE <> :old.SUPPORTROLE) or
   (:new.SUPPORTROLE is null and :old.SUPPORTROLE is not null) or
   (:new.SUPPORTROLE is not null and :old.SUPPORTROLE is null) then
   changeinfo := changeinfo || 'SUPPORTROLE:"' || :old.SUPPORTROLE || '"->"' || :new.SUPPORTROLE || '" ';
   hasChanged:=1;
end if;

if (:new.PM_ROLE <> :old.PM_ROLE) or
   (:new.PM_ROLE is null and :old.PM_ROLE is not null) or
   (:new.PM_ROLE is not null and :old.PM_ROLE is null) then
   changeinfo := changeinfo || 'PM_ROLE:"' || :old.PM_ROLE || '"->"' || :new.PM_ROLE || '" ';
   hasChanged:=1;
end if;

if (:new.AK_ROLE <> :old.AK_ROLE) or
   (:new.AK_ROLE is null and :old.AK_ROLE is not null) or
   (:new.AK_ROLE is not null and :old.AK_ROLE is null) then
   changeinfo := changeinfo || 'AK_ROLE:"' || :old.AK_ROLE || '"->"' || :new.AK_ROLE || '" ';
   hasChanged:=1;
end if;

if (:new.WARTE_ROLE <> :old.WARTE_ROLE) or
   (:new.WARTE_ROLE is null and :old.WARTE_ROLE is not null) or
   (:new.WARTE_ROLE is not null and :old.WARTE_ROLE is null) then
   changeinfo := changeinfo || 'WARTE_ROLE:"' || :old.WARTE_ROLE || '"->"' || :new.WARTE_ROLE || '" ';
   hasChanged:=1;
end if;

if (:new.SDADMIN_ROLE <> :old.SDADMIN_ROLE) or
   (:new.SDADMIN_ROLE is null and :old.SDADMIN_ROLE is not null) or
   (:new.SDADMIN_ROLE is not null and :old.SDADMIN_ROLE is null) then
   changeinfo := changeinfo || 'SDADMIN_ROLE:"' || :old.SDADMIN_ROLE || '"->"' || :new.SDADMIN_ROLE || '" ';
   hasChanged:=1;
end if;

if (:new.ADMIN_ROLE <> :old.ADMIN_ROLE) or
   (:new.ADMIN_ROLE is null and :old.ADMIN_ROLE is not null) or
   (:new.ADMIN_ROLE is not null and :old.ADMIN_ROLE is null) then
   changeinfo := changeinfo || 'ADMIN_ROLE:"' || :old.ADMIN_ROLE || '"->"' || :new.ADMIN_ROLE || '" ';
   hasChanged:=1;
end if;

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'EMPLOYEE', primarykey, changeinfo);
end if;
END;
/
