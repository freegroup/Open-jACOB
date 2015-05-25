-- Sollte unter Oracle 8i und größer funktionieren (funkt aber leider nicht für 8.0.5!):


SET SERVEROUTPUT ON SIZE 500000
DECLARE
	sql_stmt VARCHAR2(100) := 'alter index :1 rebuild noreverse'; 
	index_owner VARCHAR(32) := 'caretaker';
BEGIN
	DBMS_OUTPUT.PUT_LINE('Start');         
	FOR rec IN (select * from USER_INDEXES where TABLE_OWNER=upper(index_owner)) LOOP
		DBMS_OUTPUT.PUT_LINE('Refreshing index ' || rec.index_name);
		EXECUTE IMMEDIATE sql_stmt USING rec.index_name;
	END LOOP;
	DBMS_OUTPUT.PUT_LINE('End');         
END;
/



-- Sollte immer funktionieren:

spool rebuild_index.sql
select 'alter index '||index_name||' rebuild noreverse;' from user_indexes where TABLE_OWNER='CARETAKER';
spool off

@rebuild_index.sql;

