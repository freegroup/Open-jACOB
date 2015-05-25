CREATE OR REPLACE TRIGGER "AUDIT_EXT_SYSTEM_DEL_TRG" BEFORE DELETE
    ON "EXT_SYSTEM"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'NAME:"' || :old.NAME || '" ' ||
              'CONNECT_INFO:"' || :old.CONNECT_INFO || '" ' ||
              'CONNECT_USER:"' || :old.CONNECT_USER || '" ' ||
              'CONNECT_PWD:"' || :old.CONNECT_PWD || '" ' ||
              'SYSTEMTYPE:"' || :old.SYSTEMTYPE || '" ' ||
              'SYSTEMSTATUS:"' || :old.SYSTEMSTATUS || '" ' ||
              'FLUSHCATEGORIES":"' || :old.FLUSHCATEGORIES || '" ' ||
              'FETCHHWGDATA:"' || :old.FETCHHWGDATA || '" ';

primarykey := :old.PKEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'EXT_SYSTEM', primarykey, changeinfo);
END;
/
