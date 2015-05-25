CREATE OR REPLACE PROCEDURE jacob_next_id
(	in_tablename IN VARCHAR2, 
	in_increment IN INTEGER, 
	out_nextid	    OUT INTEGER 
) 
AS

CURSOR idcursor IS SELECT nextid FROM jacob_ids WHERE tablename = in_tablename FOR UPDATE;
BEGIN 
	OPEN idcursor; 
	LOOP 
		out_nextid := -1; 
		FETCH idcursor INTO out_nextid; 
		
		IF ( idcursor%NOTFOUND ) THEN 
			-- Note: There is a minimal chance that the following statement could fail!
			--       This is when two clients are trying to insert simultaneously
			INSERT INTO jacob_ids (tablename, nextid) VALUES (in_tablename, 1 + in_increment);
			out_nextid := 1;
		ELSE
			UPDATE jacob_ids SET nextid = out_nextid + in_increment WHERE CURRENT OF idcursor; 
		END IF; 
		COMMIT; 
		EXIT; 
	END LOOP; 
	CLOSE idcursor; 
END jacob_next_id;
