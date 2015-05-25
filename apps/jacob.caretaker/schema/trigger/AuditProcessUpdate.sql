CREATE OR REPLACE TRIGGER "AUDIT_PROCESS_UPD_TRG" BEFORE UPDATE
    OF "PROCESSNAME", "DESCRIPTION", "PROCESSSTATUS", "PROCESS_KEY"
    ON "PROCESS"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.PROCESSNAME <> :old.PROCESSNAME) then
   changeinfo := changeinfo || 'PROCESSNAME:"' || :old.PROCESSNAME || '"->"' || :new.PROCESSNAME || '" ';
   hasChanged:=1;
end if;

if (:new.DESCRIPTION <> :old.DESCRIPTION) or
   (:new.DESCRIPTION is null and :old.DESCRIPTION is not null) or
   (:new.DESCRIPTION is not null and :old.DESCRIPTION is null) then
   changeinfo := changeinfo || 'DESCRIPTION:"' || :old.DESCRIPTION || '"->"' || :new.DESCRIPTION || '" ';
   hasChanged:=1;
end if;

if (:new.PROCESSSTATUS <> :old.PROCESSSTATUS) then
   changeinfo := changeinfo || 'PROCESSSTATUS:"' || :old.PROCESSSTATUS || '"->"' || :new.PROCESSSTATUS || '" ';
   hasChanged:=1;
end if;

if (:new.PROCESS_KEY <> :old.PROCESS_KEY) or
   (:new.PROCESS_KEY is null and :old.PROCESS_KEY is not null) or
   (:new.PROCESS_KEY is not null and :old.PROCESS_KEY is null) then
   changeinfo := changeinfo || 'PROCESS_KEY:"' || :old.PROCESS_KEY || '"->"' || :new.PROCESS_KEY || '" ';
   hasChanged:=1;
end if;

primarykey := :new.pkey;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'PROCESS', primarykey, changeinfo);
end if;
END;
/
