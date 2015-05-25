CREATE OR REPLACE PACKAGE BODY tts_user_admin
IS
/******************************************************************************
* TTS_USER_ADMIN (PACKAGE BODY)
* *****************************************************************************
* Dieses Package stellt Funktionen zur Verfuegung, die anwendungsseitig
* erstellte (FWT-SMC) Benutzer / Logins als echte Datenbankuser anlegt bzw.
* diese wieder loescht.
*
* 2001, gregor.zurowski@daimlerchrysler.com
* im Auftrag der Firma Tessmar GmbH
* fuer die DaimlerChrysler AG
*
* 2002, osa.sonntag@daimlerchrysler.com
* im Auftrag der Firma OneStepAhead AG
* fuer die DaimlerChrysler AG
*
* changelog
*    20020829  gz   Anderung des Schemanamens von qfwt0110 in caretaker
*    20020830  gz   neue Funktion "grant_cq_user_roles" und Programmanpassungen
*    20021112  as   neue Funktionen "grant_cq_agent_role" und "grant_cq_ak_role"
*                   anstatt Funktion "grant_cq_user_roles"
*
*******************************************************************************






 ******************************************************************************
 * CREATE_USER
 *-----------------------------------------------------------------------------
 *
 * PARAMETER:
 * username IN VARCHAR2		- Datenbanklogin
 *
 * FUNCTION:
 * Erstellt einen neune Datenbanklogin / Benutzer in der aktuellen Datenbank
 *
 * RETURN:
 * 1 bei Erfolg
 * -1 bei Fehler
 *****************************************************************************/
FUNCTION create_user (username IN VARCHAR2) RETURN NUMBER IS
cur_id		INTEGER;
ignore		INTEGER;
BEGIN
	cur_id := DBMS_SQL.OPEN_CURSOR;
	DBMS_SQL.PARSE(cur_id, 'CREATE USER "' || UPPER(username) || '" IDENTIFIED BY ' || username, DBMS_SQL.NATIVE);
	ignore := DBMS_SQL.EXECUTE(cur_id);

	DBMS_SQL.CLOSE_CURSOR(cur_id);

	RETURN(1);

	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Fehler in [CREATE_USER]! Grund: ' || SQLERRM);
			RETURN(-1);
END create_user;


/******************************************************************************
 * GRANT_CONNECT
 *-----------------------------------------------------------------------------
 *
 * PARAMETER:
 * username IN VARCHAR2		- Datenbanklogin
 *
 * FUNCTION:
 * Gibt einem Benutzer "Connect"-Privilegien
 *
 * RETURN:
 * 1 bei Erfolg
 * -1 bei Fehler
 *****************************************************************************/
FUNCTION grant_connect (username IN VARCHAR2) RETURN NUMBER IS
cur_id		INTEGER;
ignore		INTEGER;
BEGIN
	cur_id := DBMS_SQL.OPEN_CURSOR;
	DBMS_SQL.PARSE(cur_id, 'GRANT CONNECT TO "' || UPPER(username) || '"', DBMS_SQL.NATIVE);
	ignore := DBMS_SQL.EXECUTE(cur_id);

	DBMS_SQL.CLOSE_CURSOR(cur_id);

	RETURN(1);

	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Fehler in [GRANT_CONNECT]! Grund: ' || SQLERRM);
			RETURN(-1);
END grant_connect;


/******************************************************************************
 * GRANT_CQ_ROLE
 *-----------------------------------------------------------------------------
 *
 * PARAMETER:
 * username IN VARCHAR2		- Datenbanklogin
 * cqrole IN VARCHAR2		- CQ Rolle 
 *
 * FUNCTION:
 * Vergibt an einen Benutzer eine CQ Rolle
 *
 * RETURN:
 * 1 bei Erfolg
 * -1 bei Fehler
 *****************************************************************************/
FUNCTION grant_cq_role (username IN VARCHAR2, cqrole IN VARCHAR2) RETURN NUMBER IS
cur_id		INTEGER;
ignore		INTEGER;
BEGIN
	DBMS_OUTPUT.PUT_LINE('Rolle <' || cqrole || '> wird an <' || username || '> vergeben');
	
	cur_id := DBMS_SQL.OPEN_CURSOR;
	DBMS_SQL.PARSE(cur_id, 'GRANT ' || cqrole || ' TO "' || UPPER(username) || '"', DBMS_SQL.NATIVE);
	ignore := DBMS_SQL.EXECUTE(cur_id);

	DBMS_SQL.CLOSE_CURSOR(cur_id);

	RETURN(1);

	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Fehler in [GRANT_CQ_ROLE]! Grund: ' || SQLERRM);
			RETURN(-1);
END grant_cq_role;


/******************************************************************************
 * REVOKE_CQ_ROLE
 *-----------------------------------------------------------------------------
 *
 * PARAMETER:
 * username IN VARCHAR2		- Datenbanklogin
 * cqrole IN VARCHAR2		- CQ Rolle 
 *
 * FUNCTION:
 * Entzieht einem Benutzer eine CQ Rolle
 *
 * RETURN:
 * 1 bei Erfolg
 * -1 bei Fehler
 *****************************************************************************/
FUNCTION revoke_cq_role (username IN VARCHAR2, cqrole IN VARCHAR2) RETURN NUMBER IS
cur_id		INTEGER;
ignore		INTEGER;
BEGIN
	DBMS_OUTPUT.PUT_LINE('Rolle <' || cqrole || '> wird <' || username || '> entzogen');
	
	cur_id := DBMS_SQL.OPEN_CURSOR;
	DBMS_SQL.PARSE(cur_id, 'REVOKE ' || cqrole || ' FROM "' || UPPER(username) || '"', DBMS_SQL.NATIVE);
	ignore := DBMS_SQL.EXECUTE(cur_id);

	DBMS_SQL.CLOSE_CURSOR(cur_id);

	RETURN(1);

	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Fehler in [REVOKE_CQ_ROLE]! Grund: ' || SQLERRM);
			RETURN(-1);
END revoke_cq_role;


/******************************************************************************
 * FUNCTION delete_user
 *-----------------------------------------------------------------------------
 *
 * PARAMETER:
 * username IN VARCHAR2		- Datenbanklogin
 *
 * FUNCTION:
 * Loescht einen Benutzer in aktueller Datenbank
 *
 * RETURN:
 * 1 bei Erfolg
 * -1 bei Fehler
 *****************************************************************************/
FUNCTION delete_user (username IN VARCHAR2) RETURN NUMBER IS
cur_id		INTEGER;
ignore		INTEGER;

BEGIN
	cur_id := DBMS_SQL.OPEN_CURSOR;
	DBMS_SQL.PARSE(cur_id, 'DROP USER "' || UPPER(username) || '"', DBMS_SQL.NATIVE);
	ignore := DBMS_SQL.EXECUTE(cur_id);

	DBMS_SQL.CLOSE_CURSOR(cur_id);

	RETURN 1;

	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Fehler in [DELETE_USER]! Grund: ' || SQLERRM);
			RETURN(-1);
END delete_user;


/******************************************************************************
 * FUNCTION create_available_ttsuser
 *-----------------------------------------------------------------------------
 *
 * PARAMETER:
 * keine
 *
 * FUNCTION:
 * Durchsucht die Tabelle 'employee' nach Datensaetzen, die einen 'loginnamen'
 * haben und dessen 'availability'-Feld mit 1 gefuellt ist.
 * Fallst es fuer dieses gelesene Login noch keinen Datenbankbenutzer gibt,
 * wird dieser erstellt.
 *
 * RETURN:
 * Anzahl der erstellten Datenbankbenutzer oder
 * -1 bei Fehler
 *
 * changelog
 *    20020830  gz   Anpassung an neue CQ-Benutzerrollen
 *
 *****************************************************************************/
FUNCTION create_available_ttsuser RETURN NUMBER IS

CURSOR available_user_cur IS
	SELECT UPPER(loginname), supportrole, ak_role, admin_role, sdadmin_role, pm_role, warte_role FROM employee
		WHERE availability=1 AND UPPER(loginname) NOT IN (SELECT UPPER(username) FROM all_users);

cloginname 	employee.loginname%TYPE;

cagentrole employee.supportrole%TYPE;
cakrole employee.ak_role%TYPE;
cadminrole employee.admin_role%TYPE;
csdadminrole employee.sdadmin_role%TYPE;
cpmrole employee.pm_role%TYPE;
cwarterole employee.warte_role%TYPE;

counter		INTEGER := 0;
dummy		INTEGER;

BEGIN
	IF NOT available_user_cur%ISOPEN THEN
		OPEN available_user_cur;
	END IF;

	FETCH available_user_cur INTO cloginname, cagentrole, cakrole, cadminrole, csdadminrole, cpmrole, cwarterole;

	WHILE available_user_cur%FOUND
	LOOP
		DBMS_OUTPUT.PUT_LINE('Datenbanklogin <' || cloginname || '> wird angelegt');

		---------------------------------
		-- Datenbankbenutzer erstellen --
		---------------------------------
		dummy := create_user(cloginname);

		IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** Datenbankbenutzer <' || cloginname || '> konnte nicht erstellt werden !');
		END IF;

		----------------------------------------------------------
		-- Connect-Berechtigungen an Datenbankbenutzer vergeben --
		----------------------------------------------------------
		dummy := grant_connect(cloginname);

		IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** Connect-Berechtigung konnte dem Datenbankbenutzer <' || cloginname || '> nicht gegeben werden !');
		END IF;

		-----------------------------------------------------
		-- CQ-Benutzerrollen an Datenbankbenutzer vergeben --
		-----------------------------------------------------
		IF cagentrole=1 THEN
		  dummy := grant_cq_role(cloginname, 'CQ_AGENT');
		  IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-Agent-Berechtigung konnte dem Datenbankbenutzer <' || cloginname || '> nicht gegeben werden !');
		  END IF;
		END IF;

		IF cakrole=1 THEN
		  dummy := grant_cq_role(cloginname, 'CQ_AK');
		  IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-AK-Berechtigung konnte dem Datenbankbenutzer <' || cloginname || '> nicht gegeben werden !');
		  END IF;
		END IF;

		IF cadminrole=1 THEN
		  dummy := grant_cq_role(cloginname, 'CQ_ADMIN');
		  IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-ADMIN-Berechtigung konnte dem Datenbankbenutzer <' || cloginname || '> nicht gegeben werden !');
		  END IF;
		END IF;

		IF csdadminrole=1 THEN
		  dummy := grant_cq_role(cloginname, 'CQ_SDADMIN');
		  IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-SDADMIN-Berechtigung konnte dem Datenbankbenutzer <' || cloginname || '> nicht gegeben werden !');
		  END IF;
		END IF;

		IF cpmrole=1 THEN
		  dummy := grant_cq_role(cloginname, 'CQ_PM');
		  IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-PM-Berechtigung konnte dem Datenbankbenutzer <' || cloginname || '> nicht gegeben werden !');
		  END IF;
		END IF;

		IF cwarterole=1 THEN
		  dummy := grant_cq_role(cloginname, 'CQ_WARTE');
		  IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-WARTE-Berechtigung konnte dem Datenbankbenutzer <' || cloginname || '> nicht gegeben werden !');
		  END IF;
		END IF;

		-----------------------------------------------------

		counter := counter + 1;

		FETCH available_user_cur INTO cloginname, cagentrole, cakrole, cadminrole, csdadminrole, cpmrole, cwarterole;
	END LOOP;

	RETURN counter;

	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Fehler in [CREATE_AVAILABLE_TTSUSER]! Grund: ' || SQLERRM);
			RETURN(-1);

END create_available_ttsuser;


/*****************************************************************************
 * FUNCTION delete_unavailable_ttsuser
 *-----------------------------------------------------------------------------
 *
 * PARAMETER:
 * keine
 *
 * FUNCTION:
 * Durchsucht die Tabelle 'employee' nach Datensaetzen, die einen 'loginname'
 * haben und dessen 'availability'-Feld mit 0 gefuellt ist.
 * Falls es fuer dieses Login einen Datenbankbenutzer gibt, wird dieser
 * geloescht.
 *
 * RETURN:
 * Anzahl der geloeschten Datenbankbenutzer oder
 * -1 bei Fehler
 *****************************************************************************/
FUNCTION delete_unavailable_ttsuser RETURN NUMBER IS
CURSOR unavailable_user_cur IS
	SELECT UPPER(loginname) AS loginname FROM employee
		WHERE availability=0 AND UPPER(loginname) IN (SELECT UPPER(username) FROM all_users);

cloginname	employee.loginname%TYPE;
counter		INTEGER := 0;
dummy		INTEGER;

BEGIN
	IF NOT unavailable_user_cur%ISOPEN THEN
		OPEN unavailable_user_cur;
	END IF;

	FETCH unavailable_user_cur INTO cloginname;

	WHILE unavailable_user_cur%FOUND
	LOOP
		DBMS_OUTPUT.PUT_LINE('Datenbanklogin <' || cloginname || '> wird geloescht!');

		dummy := delete_user(cloginname);

		IF dummy <> 1 THEN
			DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** Datenbankbenutzer <' || cloginname || '> konnte nicht geloescht werden!');
		END IF;

		counter := counter + 1;

		FETCH unavailable_user_cur INTO cloginname;
	END LOOP;

	RETURN counter;

	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Fehler in Funktion [DELETE_UNAVAILABLE_TTSUSER]! Grund: ' || SQLERRM);
			RETURN(-1);

END delete_unavailable_ttsuser;


/******************************************************************************
 * FUNCTION check_existing_ttsuser
 *-----------------------------------------------------------------------------
 *
 * PARAMETER:
 * keine
 *
 * FUNCTION:
 * Überprüft ob die Datenbankberechtigungen (Rollen) von existierenden 
 * Datenbankbenutzern angepasst werden müssen.
 *
 * RETURN:
 * 1 bei Erfolg
 * -1 bei Fehler
 *
 *****************************************************************************/
FUNCTION check_existing_ttsuser RETURN NUMBER IS

dummy		INTEGER;

BEGIN
	--------------------------------------
	-- CQ_ADMIN Berechtigung überprüfen --
	--------------------------------------
--	FOR childrec IN (select upper(loginname) as ttslogin from employee where admin_role=1 and availability=1 and upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_ADMIN') and UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
--	  dummy := grant_cq_role(childrec.ttslogin, 'CQ_ADMIN');
--	  IF dummy <> 1 THEN
--	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-ADMIN-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
--	  END IF;
--	END LOOP;

--	FOR childrec IN (select upper(loginname) as ttslogin from employee where (admin_role<>1 or admin_role is null) and availability=1 and upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_ADMIN')) LOOP
--	  dummy := revoke_cq_role(childrec.ttslogin, 'CQ_ADMIN');
--	  IF dummy <> 1 THEN
--	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-ADMIN-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
--	  END IF;
--	END LOOP;

	RETURN 1;

	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Fehler in [CHECK_EXISTING_TTSUSER]! Grund: ' || SQLERRM);
			RETURN(-1);

END check_existing_ttsuser;


END tts_user_admin;
/
