CREATE OR REPLACE TRIGGER "AUDIT_GROUPMEMBER_UPD_TRG" BEFORE UPDATE
    OF "EMPLOYEEGROUP", "WORKGROUPGROUP", "NOTIFYMETHOD"
    ON "GROUPMEMBER"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.EMPLOYEEGROUP <> :old.EMPLOYEEGROUP) or
   (:new.EMPLOYEEGROUP is null and :old.EMPLOYEEGROUP is not null) or
   (:new.EMPLOYEEGROUP is not null and :old.EMPLOYEEGROUP is null) then
   changeinfo := changeinfo || 'EMPLOYEEGROUP:"' || :old.EMPLOYEEGROUP || '"->"' || :new.EMPLOYEEGROUP || '" ';
   hasChanged:=1;
end if;

if (:new.WORKGROUPGROUP <> :old.WORKGROUPGROUP) or
   (:new.WORKGROUPGROUP is null and :old.WORKGROUPGROUP is not null) or
   (:new.WORKGROUPGROUP is not null and :old.WORKGROUPGROUP is null) then
   changeinfo := changeinfo || 'WORKGROUPGROUP:"' || :old.WORKGROUPGROUP || '"->"' || :new.WORKGROUPGROUP || '" ';
   hasChanged:=1;
end if;

if (:new.NOTIFYMETHOD <> :old.NOTIFYMETHOD) or
   (:new.NOTIFYMETHOD is null and :old.NOTIFYMETHOD is not null) or
   (:new.NOTIFYMETHOD is not null and :old.NOTIFYMETHOD is null) then
   changeinfo := changeinfo || 'NOTIFYMETHOD:"' || :old.NOTIFYMETHOD || '"->"' || :new.NOTIFYMETHOD || '" ';
   hasChanged:=1;
end if;

primarykey := :new.pkey;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'GROUPMEMBER', primarykey, changeinfo);
end if;
END;
/
