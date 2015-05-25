CREATE OR REPLACE TRIGGER "AUDIT_CATEGORY_DEL_TRG" BEFORE DELETE
    ON "CATEGORY"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
BEGIN

changeinfo := 'NAME:"' || :old.NAME || '" ' ||
              'CATEGORYSTATUS:"' || :old.CATEGORYSTATUS || '" ' ||
              'GDSALIAS:"' || :old.GDSALIAS || '" ' ||
              'EDVINALIAS:"' || :old.EDVINALIAS || '" ' ||
              'PARENTCATEGORY_KEY:"' || :old.PARENTCATEGORY_KEY || '" ' ||
              'SYNONYME:"' || :old.SYNONYME || '" ';

primarykey := :old.PKEY;

-- write audit record
my_next_key ('smcaudit', -1, auditkey );
INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
       VALUES (auditkey, sysdate, User, 'DELETE', 'CATEGORY', primarykey, changeinfo);
END;
/
