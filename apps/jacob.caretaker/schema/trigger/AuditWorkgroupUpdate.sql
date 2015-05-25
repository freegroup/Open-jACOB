CREATE OR REPLACE TRIGGER "AUDIT_WORKGROUP_UPD_TRG" BEFORE UPDATE
    OF "NAME", "DESCRIPTION", "NOTIFICATIONADDR", "NOTIFYMETHOD", "PHONE", "FAX",
       "WRKGRPTYPE", "ACCOUNTINGCODE", "HWG_NAME", "ORGEXTERNAL_KEY", "MIGRATION", "GROUPCONFERENCECAL"
    ON "WORKGROUP"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.NAME <> :old.NAME) then
   changeinfo := changeinfo || 'NAME:"' || :old.NAME || '"->"' || :new.NAME || '" ';
   hasChanged:=1;
end if;

if (:new.DESCRIPTION <> :old.DESCRIPTION) or
   (:new.DESCRIPTION is null and :old.DESCRIPTION is not null) or
   (:new.DESCRIPTION is not null and :old.DESCRIPTION is null) then
   changeinfo := changeinfo || 'DESCRIPTION:"' || :old.DESCRIPTION || '"->"' || :new.DESCRIPTION || '" ';
   hasChanged:=1;
end if;

if (:new.NOTIFICATIONADDR <> :old.NOTIFICATIONADDR) or
   (:new.NOTIFICATIONADDR is null and :old.NOTIFICATIONADDR is not null) or
   (:new.NOTIFICATIONADDR is not null and :old.NOTIFICATIONADDR is null) then
   changeinfo := changeinfo || 'NOTIFICATIONADDR:"' || :old.NOTIFICATIONADDR || '"->"' || :new.NOTIFICATIONADDR || '" ';
   hasChanged:=1;
end if;

if (:new.NOTIFYMETHOD <> :old.NOTIFYMETHOD) or
   (:new.NOTIFYMETHOD is null and :old.NOTIFYMETHOD is not null) or
   (:new.NOTIFYMETHOD is not null and :old.NOTIFYMETHOD is null) then
   changeinfo := changeinfo || 'NOTIFYMETHOD:"' || :old.NOTIFYMETHOD || '"->"' || :new.NOTIFYMETHOD || '" ';
   hasChanged:=1;
end if;

if (:new.PHONE <> :old.PHONE) or
   (:new.PHONE is null and :old.PHONE is not null) or
   (:new.PHONE is not null and :old.PHONE is null) then
   changeinfo := changeinfo || 'PHONE:"' || :old.PHONE || '"->"' || :new.PHONE || '" ';
   hasChanged:=1;
end if;

if (:new.FAX <> :old.FAX) or
   (:new.FAX is null and :old.FAX is not null) or
   (:new.FAX is not null and :old.FAX is null) then
   changeinfo := changeinfo || 'FAX:"' || :old.FAX || '"->"' || :new.FAX || '" ';
   hasChanged:=1;
end if;

if (:new.WRKGRPTYPE <> :old.WRKGRPTYPE) or
   (:new.WRKGRPTYPE is null and :old.WRKGRPTYPE is not null) or
   (:new.WRKGRPTYPE is not null and :old.WRKGRPTYPE is null) then
   changeinfo := changeinfo || 'WRKGRPTYPE:"' || :old.WRKGRPTYPE || '"->"' || :new.WRKGRPTYPE || '" ';
   hasChanged:=1;
end if;

if (:new.ACCOUNTINGCODE <> :old.ACCOUNTINGCODE) or
   (:new.ACCOUNTINGCODE is null and :old.ACCOUNTINGCODE is not null) or
   (:new.ACCOUNTINGCODE is not null and :old.ACCOUNTINGCODE is null) then
   changeinfo := changeinfo || 'ACCOUNTINGCODE:"' || :old.ACCOUNTINGCODE || '"->"' || :new.ACCOUNTINGCODE || '" ';
   hasChanged:=1;
end if;

if (:new.HWG_NAME <> :old.HWG_NAME) or
   (:new.HWG_NAME is null and :old.HWG_NAME is not null) or
   (:new.HWG_NAME is not null and :old.HWG_NAME is null) then
   changeinfo := changeinfo || 'HWG_NAME:"' || :old.HWG_NAME || '"->"' || :new.HWG_NAME || '" ';
   hasChanged:=1;
end if;

if (:new.ORGEXTERNAL_KEY <> :old.ORGEXTERNAL_KEY) or
   (:new.ORGEXTERNAL_KEY is null and :old.ORGEXTERNAL_KEY is not null) or
   (:new.ORGEXTERNAL_KEY is not null and :old.ORGEXTERNAL_KEY is null) then
   changeinfo := changeinfo || 'ORGEXTERNAL_KEY:"' || :old.ORGEXTERNAL_KEY || '"->"' || :new.ORGEXTERNAL_KEY || '" ';
   hasChanged:=1;
end if;

if (:new.MIGRATION <> :old.MIGRATION) or
   (:new.MIGRATION is null and :old.MIGRATION is not null) or
   (:new.MIGRATION is not null and :old.MIGRATION is null) then
   changeinfo := changeinfo || 'MIGRATION:"' || :old.MIGRATION || '"->"' || :new.MIGRATION || '" ';
   hasChanged:=1;
end if;

if (:new.GROUPCONFERENCECAL <> :old.GROUPCONFERENCECAL) or
   (:new.GROUPCONFERENCECAL is null and :old.GROUPCONFERENCECAL is not null) or
   (:new.GROUPCONFERENCECAL is not null and :old.GROUPCONFERENCECAL is null) then
   changeinfo := changeinfo || 'GROUPCONFERENCECAL:"' || :old.GROUPCONFERENCECAL || '"->"' || :new.GROUPCONFERENCECAL || '" ';
   hasChanged:=1;
end if;

primarykey := :new.pkey;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'WORKGROUP', primarykey, changeinfo);
end if;
END;
/
