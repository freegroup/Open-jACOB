CREATE OR REPLACE  PROCEDURE "MY_NEXT_KEY"       
(	a_input_table		VARCHAR2, 
	a_increment		INTEGER, 
	a_returnkey	    OUT INTEGER 
) AS 

PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN

q_next_key(a_input_table,a_increment,a_returnkey);
COMMIT;

END my_next_key;
/

