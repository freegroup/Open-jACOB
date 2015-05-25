CREATE OR REPLACE TRIGGER "AUDIT_CATEGORYPROCESS_UPD_TRG" BEFORE UPDATE
    OF "ROUTINGINFO"
    ON "CATEGORYPROCESS"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.ROUTINGINFO <> :old.ROUTINGINFO) or
   (:new.ROUTINGINFO is null and :old.ROUTINGINFO is not null) or
   (:new.ROUTINGINFO is not null and :old.ROUTINGINFO is null) then
   changeinfo := changeinfo || 'ROUTINGINFO:"' || :old.ROUTINGINFO || '"->"' || :new.ROUTINGINFO || '" ';
   hasChanged:=1;
end if;

primarykey := :new.CONTRACT_KEY ||
              ':' || :new.CATEGORY_KEY ||
              ':' || :new.PROCESS_KEY ||
              ':' || :new.WORKGROUP_KEY;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'CATEGORYPROCESS', primarykey, changeinfo);
end if;
END;
/
