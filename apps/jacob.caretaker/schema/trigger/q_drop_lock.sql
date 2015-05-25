-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    In der Verganegenheit kam es immer wieder vor, dass qw_locks nicht richtig               #
-- #    geloescht wurden und die Arbeit behinderten. Jetzt werden Locks, die aelter als 20       #
-- #    Minuten sind, geloescht.                                                                 #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  07.04.2003  Zur Beseitigung von Deadlocks                              Mike Doering        #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE  PROCEDURE "q_drop_lock"
(									
        a_table            VARCHAR2,					
        a_key              VARCHAR2,					
        a_user             VARCHAR2,					
        a_host             VARCHAR2,					
        a_pid              INTEGER,					
	a_lock	       OUT VARCHAR2					
) AS									
	ret_string	VARCHAR2(255);					
	r_user		VARCHAR2(30);					
	r_host		VARCHAR2(128);					
	r_pid		INTEGER;					
									
begin									
	-- initialise variables						
	r_user := a_user;						
	r_host := a_host;						
	r_pid  := a_pid;						

--  delete old locks (older than 20 minutes Tarragon)
  delete from qw_locks where locktime < (sysdate -1/72);
 
    --lets just try to delete the lock				
	DELETE FROM qw_locks						
	WHERE tablename	= a_table					
	AND   keyvalue	= a_key						
	AND   username	= a_user					
	AND   hostname	= a_host					
	AND   pid	= a_pid;					
    --delete from qw_locks where locktime < (sysdate -1/144); 
									
	COMMIT;								
	if (SQL%NOTFOUND) then						
		-- we could not delete the lock, lets check who owns	
		begin							
			SELECT username, hostname, pid			
			INTO   r_user,   r_host,   r_pid		
			FROM   qw_locks					
			WHERE  tablename = a_table			
			AND    keyvalue  = a_key;			
									
			if (SQL%NOTFOUND) then				
				-- no lock on this record		
				r_user := ' ';				
				r_host := ' ';				
				r_pid  := 0;				
			end if;						
			exception when NO_DATA_FOUND then		
				-- no lock on this record		
				r_user := ' ';				
				r_user := ' ';				
				r_pid  := 0;				
		end;							
	end if;								
	a_lock := 'Return /'|| ' '|| '/' || r_user || '/' ||		
			r_host || '/' || r_pid || '/';			
END q_drop_lock;
/