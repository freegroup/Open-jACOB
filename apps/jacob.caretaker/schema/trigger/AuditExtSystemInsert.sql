CREATE OR REPLACE TRIGGER "AUDIT_EXT_SYSTEM_INS_TRG" BEFORE INSERT
    ON "EXT_SYSTEM"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'NAME:"' || :new.NAME || '" ' ||
              'CONNECT_INFO:"' || :new.CONNECT_INFO || '" ' ||
              'CONNECT_USER:"' || :new.CONNECT_USER || '" ' ||
              'CONNECT_PWD:"' || :new.CONNECT_PWD || '" ' ||
              'SYSTEMTYPE:"' || :new.SYSTEMTYPE || '" ' ||
              'SYSTEMSTATUS:"' || :new.SYSTEMSTATUS || '" ' ||
              'FLUSHCATEGORIES":"' || :new.FLUSHCATEGORIES || '" ' ||
              'FETCHHWGDATA:"' || :new.FETCHHWGDATA || '" ';

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'EXT_SYSTEM', primarykey, changeinfo);

END;
/
