CREATE OR REPLACE TRIGGER "AUDIT_CATEGORY_UPD_TRG" BEFORE UPDATE
    OF "NAME", "CATEGORYSTATUS", "GDSALIAS", "EDVINALIAS", "PARENTCATEGORY_KEY", "SYNONYME"
    ON "CATEGORY"
    FOR EACH ROW
declare
   changeinfo         varchar(4000);
   primarykey         varchar(128);
   auditkey           number;
   hasChanged         number:=0;
BEGIN

if (:new.NAME <> :old.NAME) or
   (:new.NAME is null and :old.NAME is not null) or
   (:new.NAME is not null and :old.NAME is null) then
   changeinfo := changeinfo || 'NAME:"' || :old.NAME || '"->"' || :new.NAME || '" ';
   hasChanged:=1;
end if;

if (:new.CATEGORYSTATUS <> :old.CATEGORYSTATUS) or
   (:new.CATEGORYSTATUS is null and :old.CATEGORYSTATUS is not null) or
   (:new.CATEGORYSTATUS is not null and :old.CATEGORYSTATUS is null) then
   changeinfo := changeinfo || 'CATEGORYSTATUS:"' || :old.CATEGORYSTATUS || '"->"' || :new.CATEGORYSTATUS || '" ';
   hasChanged:=1;
end if;

if (:new.GDSALIAS <> :old.GDSALIAS) or
   (:new.GDSALIAS is null and :old.GDSALIAS is not null) or
   (:new.GDSALIAS is not null and :old.GDSALIAS is null) then
   changeinfo := changeinfo || 'GDSALIAS:"' || :old.GDSALIAS || '"->"' || :new.GDSALIAS || '" ';
   hasChanged:=1;
end if;

if (:new.EDVINALIAS <> :old.EDVINALIAS) or
   (:new.EDVINALIAS is null and :old.EDVINALIAS is not null) or
   (:new.EDVINALIAS is not null and :old.EDVINALIAS is null) then
   changeinfo := changeinfo || 'EDVINALIAS:"' || :old.EDVINALIAS || '"->"' || :new.EDVINALIAS || '" ';
   hasChanged:=1;
end if;

if (:new.PARENTCATEGORY_KEY <> :old.PARENTCATEGORY_KEY) or
   (:new.PARENTCATEGORY_KEY is null and :old.PARENTCATEGORY_KEY is not null) or
   (:new.PARENTCATEGORY_KEY is not null and :old.PARENTCATEGORY_KEY is null) then
   changeinfo := changeinfo || 'PARENTCATEGORY_KEY:"' || :old.PARENTCATEGORY_KEY || '"->"' || :new.PARENTCATEGORY_KEY || '" ';
   hasChanged:=1;
end if;

if (:new.SYNONYME <> :old.SYNONYME) or
   (:new.SYNONYME is null and :old.SYNONYME is not null) or
   (:new.SYNONYME is not null and :old.SYNONYME is null) then
   changeinfo := changeinfo || 'SYNONYME:"' || :old.SYNONYME || '"->"' || :new.SYNONYME || '" ';
   hasChanged:=1;
end if;

primarykey := :new.pkey;

-- and write audit record if any relevant changes have been done
if hasChanged > 0 then
  my_next_key ('smcaudit', -1, auditkey );
  INSERT INTO smcaudit (pkey, timestamp, userid, action, tablename, foreignkey, changeinfo)
         VALUES (auditkey, sysdate, User, 'UPDATE', 'CATEGORY', primarykey, changeinfo);
end if;
END;
/
