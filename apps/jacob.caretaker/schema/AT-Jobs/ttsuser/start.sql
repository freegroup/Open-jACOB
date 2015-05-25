/*
 **********************************************************
 * TTSUSER_ADMIN.... create and destroy!
 *---------------------------------------------------------
 *
 * Nach 'loginnamen' in Tabelle 'employee' suchen und
 * Datenbankuser erstellen bzw. loeschen.
 *
 * 2001, gregor.zurowski@lunetta.de
 * im Autrag der Firma Tessmar GmbH
 * fuer die DaimlerChrysler AG
 *********************************************************
*/

SPOOL D:\AT-Jobs\ttsuser\SPOOL.TXT
SET SERVEROUTPUT ON SIZE 100000
WHENEVER SQLERROR EXIT SQL.SQLCODE

DECLARE
	dummy	INTEGER;
BEGIN
	DBMS_OUTPUT.PUT_LINE('zu erstellende Datenbankbenutzer werden verarbeitet...');
	
	dummy := tts_user_admin.create_available_ttsuser;
	
	IF dummy = -1 THEN
		DBMS_OUTPUT.PUT_LINE('Fehler beim Anlegen von Datenbankbenutzern!');
		RAISE_APPLICATION_ERROR(-20001, 'Fehler beim Ausfuehren von [CREATE_AVAILABLE_TTSUSER]' );
		GOTO last_exit;
	END IF;
	
	
	DBMS_OUTPUT.PUT_LINE('zu loeschende Datenbankbenutzer werden verarbeitet...');
	
	dummy := tts_user_admin.delete_unavailable_ttsuser;
	
	IF dummy = -1 THEN
		DBMS_OUTPUT.PUT_LINE('Fehler beim Loeschen von Datenbankbenutzern!');
		RAISE_APPLICATION_ERROR(-20001, 'Fehler beim Ausfuehren von [DELETE_UNAVAILABLE_TTSUSER]' );
		GOTO last_exit;
	END IF;

	--------------------------------------
	-- CQ_AGENT Berechtigung überprüfen --
	--------------------------------------
	FOR childrec IN (select upper(loginname) as ttslogin from employee where
	    supportrole=1 and availability=1 and 
	    upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_AGENT') and 
	    UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
	  dummy := tts_user_admin.grant_cq_role(childrec.ttslogin, 'CQ_AGENT');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_AGENT-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
	  END IF;
	END LOOP;

	FOR childrec IN (select upper(loginname) as ttslogin from employee where 
	    (supportrole<>1 or supportrole is null) and availability=1 and 
	    upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_AGENT')) LOOP
	  dummy := tts_user_admin.revoke_cq_role(childrec.ttslogin, 'CQ_AGENT');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_AGENT-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
	  END IF;
	END LOOP;
	
	--------------------------------------
	-- CQ_AK Berechtigung überprüfen -----
	--------------------------------------
	FOR childrec IN (select upper(loginname) as ttslogin from employee where
	    ak_role=1 and availability=1 and 
	    upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_AK') and 
	    UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
	  dummy := tts_user_admin.grant_cq_role(childrec.ttslogin, 'CQ_AK');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_AK-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
	  END IF;
	END LOOP;

	FOR childrec IN (select upper(loginname) as ttslogin from employee where 
	    (ak_role<>1 or ak_role is null) and availability=1 and 
	    upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_AK')) LOOP
	  dummy := tts_user_admin.revoke_cq_role(childrec.ttslogin, 'CQ_AK');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_AK-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
	  END IF;
	END LOOP;
	
	--------------------------------------
	-- CQ_PM Berechtigung überprüfen -----
	--------------------------------------
	FOR childrec IN (select upper(loginname) as ttslogin from employee where
	    pm_role=1 and availability=1 and 
	    upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_PM') and 
	    UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
	  dummy := tts_user_admin.grant_cq_role(childrec.ttslogin, 'CQ_PM');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_PM-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
	  END IF;
	END LOOP;

	FOR childrec IN (select upper(loginname) as ttslogin from employee where 
	    (pm_role<>1 or pm_role is null) and availability=1 and 
	    upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_PM')) LOOP
	  dummy := tts_user_admin.revoke_cq_role(childrec.ttslogin, 'CQ_PM');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_PM-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
	  END IF;
	END LOOP;
	
	--------------------------------------
	-- CQ_WARTE Berechtigung überprüfen --
	--------------------------------------
	FOR childrec IN (select upper(loginname) as ttslogin from employee where
	    warte_role=1 and availability=1 and 
	    upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_WARTE') and 
	    UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
	  dummy := tts_user_admin.grant_cq_role(childrec.ttslogin, 'CQ_WARTE');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_WARTE-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
	  END IF;
	END LOOP;

	FOR childrec IN (select upper(loginname) as ttslogin from employee where 
	    (warte_role<>1 or warte_role is null) and availability=1 and 
	    upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_WARTE')) LOOP
	  dummy := tts_user_admin.revoke_cq_role(childrec.ttslogin, 'CQ_WARTE');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_WARTE-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
	  END IF;
	END LOOP;
	
	----------------------------------------
	-- CQ_SDADMIN Berechtigung überprüfen --
	----------------------------------------
	FOR childrec IN (select upper(loginname) as ttslogin from employee where
	    sdadmin_role=1 and availability=1 and 
	    upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_SDADMIN') and 
	    UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
	  dummy := tts_user_admin.grant_cq_role(childrec.ttslogin, 'CQ_SDADMIN');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_SDADMIN-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
	  END IF;
	END LOOP;

	FOR childrec IN (select upper(loginname) as ttslogin from employee where 
	    (sdadmin_role<>1 or sdadmin_role is null) and availability=1 and 
	    upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_SDADMIN')) LOOP
	  dummy := tts_user_admin.revoke_cq_role(childrec.ttslogin, 'CQ_SDADMIN');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_SDADMIN-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
	  END IF;
	END LOOP;
	
	--------------------------------------
	-- CQ_ADMIN Berechtigung überprüfen --
	--------------------------------------
	FOR childrec IN (select upper(loginname) as ttslogin from employee where
	    admin_role=1 and availability=1 and 
	    upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_ADMIN') and 
	    UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
	  dummy := tts_user_admin.grant_cq_role(childrec.ttslogin, 'CQ_ADMIN');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-ADMIN-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
	  END IF;
	END LOOP;

	FOR childrec IN (select upper(loginname) as ttslogin from employee where 
	    (admin_role<>1 or admin_role is null) and availability=1 and 
	    upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_ADMIN')) LOOP
	  dummy := tts_user_admin.revoke_cq_role(childrec.ttslogin, 'CQ_ADMIN');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-ADMIN-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
	  END IF;
	END LOOP;
	--------------------------------------
	-- CQ_SUPERAK Berechtigung überprüfen --
	--------------------------------------
	FOR childrec IN (select upper(loginname) as ttslogin from employee where
	    superak_role=1 and availability=1 and 
	    upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_SUPERAK') and 
	    UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
	  dummy := tts_user_admin.grant_cq_role(childrec.ttslogin, 'CQ_SUPERAK');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-SUPERAK-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
	  END IF;
	END LOOP;

	FOR childrec IN (select upper(loginname) as ttslogin from employee where 
	    (superak_role<>1 or superak_role is null) and availability=1 and 
	    upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_SUPERAK')) LOOP
	  dummy := tts_user_admin.revoke_cq_role(childrec.ttslogin, 'CQ_SUPERAK');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ-SUPERAK-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
	  END IF;
	END LOOP;
	--------------------------------------
	-- CQ_MBTECHAGENT Berechtigung überprüfen --
	--------------------------------------
	FOR childrec IN (select upper(loginname) as ttslogin from employee where
	    mbtech_role=1 and availability=1 and 
	    upper(loginname) not in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_MBTECHAGENT') and 
	    UPPER(loginname) IN (SELECT UPPER(username) FROM all_users)) LOOP
	  dummy := tts_user_admin.grant_cq_role(childrec.ttslogin, 'CQ_MBTECHAGENT');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_MBTECHAGENT-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht gegeben werden !');
	  END IF;
	END LOOP;

	FOR childrec IN (select upper(loginname) as ttslogin from employee where 
	    (mbtech_role<>1 or mbtech_role is null) and availability=1 and 
	    upper(loginname) in (select GRANTEE from sys.dba_role_privs where GRANTED_ROLE='CQ_MBTECHAGENT')) LOOP
	  dummy := tts_user_admin.revoke_cq_role(childrec.ttslogin, 'CQ_MBTECHAGENT');
	  IF dummy <> 1 THEN
	    DBMS_OUTPUT.PUT_LINE('*** VORSICHT *** CQ_MBTECHAGENT-Berechtigung konnte dem Datenbankbenutzer <' || childrec.ttslogin || '> nicht entzogen werden !');
	  END IF;
	END LOOP;
		
	-- Last Exit Brooklyn
	<< last_exit >>
	NULL;
END;
/
SPOOL OFF;
EXIT