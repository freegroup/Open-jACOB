CREATE OR REPLACE TRIGGER "AUDIT_WORKGROUP_DEL_TRG" BEFORE DELETE
    ON "WORKGROUP"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'NAME:"' || :old.NAME || '" ' ||
              'DESCRIPTION:"' || :old.DESCRIPTION || '" ' ||
              'NOTIFICATIONADDR:"' || :old.NOTIFICATIONADDR || '" ' ||
              'NOTIFYMETHOD:"' || :old.NOTIFYMETHOD || '" ' ||
              'PHONE:"' || :old.PHONE || '" ' ||
              'FAX:"' || :old.FAX || '" ' ||
              'WRKGRPTYPE:"' || :old.WRKGRPTYPE || '" ' ||
              'ACCOUNTINGCODE:"' || :old.ACCOUNTINGCODE || '" ' ||
              'HWG_NAME:"' || :old.HWG_NAME || '" ' ||
              'ORGEXTERNAL_KEY:"' || :old.ORGEXTERNAL_KEY || '" ' ||
              'MIGRATION:"' || :old.MIGRATION || '" ' ||
              'GROUPCONFERENCECAL:"' || :old.GROUPCONFERENCECAL || '" ';

primarykey := :old.PKEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'WORKGROUP', primarykey, changeinfo);
END;
/
