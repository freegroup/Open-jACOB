CREATE OR REPLACE TRIGGER "AUDIT_WORKGROUP_INS_TRG" BEFORE INSERT
    ON "WORKGROUP"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'NAME:"' || :new.NAME || '" ' ||
              'DESCRIPTION:"' || :new.DESCRIPTION || '" ' ||
              'NOTIFICATIONADDR:"' || :new.NOTIFICATIONADDR || '" ' ||
              'NOTIFYMETHOD:"' || :new.NOTIFYMETHOD || '" ' ||
              'PHONE:"' || :new.PHONE || '" ' ||
              'FAX:"' || :new.FAX || '" ' ||
              'WRKGRPTYPE:"' || :new.WRKGRPTYPE || '" ' ||
              'ACCOUNTINGCODE:"' || :new.ACCOUNTINGCODE || '" ' ||
              'HWG_NAME:"' || :new.HWG_NAME || '" ' ||
              'ORGEXTERNAL_KEY:"' || :new.ORGEXTERNAL_KEY || '" ' ||
              'MIGRATION:"' || :new.MIGRATION || '" ' ||
              'GROUPCONFERENCECAL:"' || :new.GROUPCONFERENCECAL || '" ';

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'WORKGROUP', primarykey, changeinfo);

END;
/
