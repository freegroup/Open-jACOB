CREATE TABLE jacob_document ( columnname VARCHAR(30) NULL, docsize NUMBER(38) NOT NULL, docname VARCHAR(255) NOT NULL, tablename VARCHAR(30) NULL, doccontent LONG RAW NULL, id CHAR(36) NOT NULL, CONSTRAINT PK_JACOB_DOCUMENT PRIMARY KEY (id));
CREATE INDEX IDX_JACOB_DOCUMENTNAME ON jacob_document (docname);
REVOKE ALL ON jacob_document FROM PUBLIC;
GRANT SELECT, UPDATE, INSERT, DELETE ON jacob_document TO PUBLIC;
CREATE TABLE jacob_ids ( nextid NUMBER(38) NOT NULL, tablename VARCHAR(250) NOT NULL, CONSTRAINT PK_JACOB_IDS PRIMARY KEY (tablename));
REVOKE ALL ON jacob_ids FROM PUBLIC;
GRANT SELECT, UPDATE, INSERT, DELETE ON jacob_ids TO PUBLIC;
CREATE TABLE jacob_text ( columnname VARCHAR(30) NULL, text LONG NULL, tablename VARCHAR(30) NULL, id CHAR(36) NOT NULL, CONSTRAINT PK_JACOB_TEXT PRIMARY KEY (id));
REVOKE ALL ON jacob_text FROM PUBLIC;
GRANT SELECT, UPDATE, INSERT, DELETE ON jacob_text TO PUBLIC;
CREATE TABLE jacob_locks ( created DATE NOT NULL, userid VARCHAR(50) NOT NULL, keyvalue VARCHAR(255) NOT NULL, username VARCHAR(255) NULL, tablename VARCHAR(30) NOT NULL, nodename VARCHAR(255) NOT NULL, CONSTRAINT PK_JACOB_LOCKS PRIMARY KEY (tablename,keyvalue));
CREATE INDEX IDX_JACOB_LOCKS_NODENAME ON jacob_locks (nodename);
REVOKE ALL ON jacob_locks FROM PUBLIC;
GRANT SELECT, UPDATE, INSERT, DELETE ON jacob_locks TO PUBLIC;
CREATE TABLE jacob_binary ( columnname VARCHAR(30) NULL, tablename VARCHAR(30) NULL, bdata LONG RAW NULL, id CHAR(36) NOT NULL, CONSTRAINT PK_JACOB_BINARY PRIMARY KEY (id));
REVOKE ALL ON jacob_binary FROM PUBLIC;
GRANT SELECT, UPDATE, INSERT, DELETE ON jacob_binary TO PUBLIC;
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
/
