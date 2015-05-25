rem -----------------------------------------------------------------------
rem Filename:   lock_usr.sql
rem Purpose:    Release DBMS_LOCK locks on database
rem Date:       12-Apr-1999
rem Author:     Frank Naude, Oracle FAQ
rem -----------------------------------------------------------------------

set serveroutput on size 50000

col name format a30
set veri off feed off pagesize 50000
cle scr

prompt Please enter the user's login id:
select *
from   sys.dbms_lock_allocated
where  upper(name) like upper('%&userid.%');

prompt Please enter lockid to release:
declare
        rc integer;
begin
        rc := dbms_lock.release('&lockid');
        if rc = 0 then
                dbms_output.put_line('Success.');
        elsif rc = 3 then
                dbms_output.put_line('Parameter Error.');
        elsif rc = 4 then
                dbms_output.put_line('Do not own lock specified.');
        elsif rc = 5 then
                dbms_output.put_line('Illegal lock handled.');
        else
                dbms_output.put_line('Unknown error.');
        end if;
end;
/
