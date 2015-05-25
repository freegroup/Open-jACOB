CREATE OR REPLACE TRIGGER "AUDIT_CATEGORY_INS_TRG" BEFORE INSERT
    ON "CATEGORY"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'NAME:"' || :new.NAME || '" ' ||
              'CATEGORYSTATUS:"' || :new.CATEGORYSTATUS || '" ' ||
              'GDSALIAS:"' || :new.GDSALIAS || '" ' ||
              'EDVINALIAS:"' || :new.EDVINALIAS || '" ' ||
              'PARENTCATEGORY_KEY:"' || :new.PARENTCATEGORY_KEY || '" ' ||
              'SYNONYME:"' || :new.SYNONYME || '" ';

primarykey := :new.PKEY;

-- and write audit record if any relevant changes have been done
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'INSERT', 'CATEGORY', primarykey, changeinfo);

END;
/
