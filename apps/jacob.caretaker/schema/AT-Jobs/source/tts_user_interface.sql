/********************************************************************************
* TTS_USER_ADMIN (PACKAGE INTERFACE)
*
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
*
* changelog
*    20020830  gz   neue Funktion "grant_cq_user_roles"
*    20021112  as   neue Funktionen "grant_cq_agent_role" und "grant_cq_ak_role"
*                   anstatt Funktion "grant_cq_user_roles"
*    20021203  as   neue Funktionen check_existing_ttsuser, revoke_cq_role, etc.
*
******************************************************************************/
CREATE OR REPLACE PACKAGE tts_user_admin
IS
	FUNCTION create_user (username IN VARCHAR2) RETURN NUMBER;
	FUNCTION grant_connect (username IN VARCHAR2) RETURN NUMBER;
	FUNCTION grant_cq_role (username IN VARCHAR2, cqrole IN VARCHAR2) RETURN NUMBER;
	FUNCTION revoke_cq_role (username IN VARCHAR2, cqrole IN VARCHAR2) RETURN NUMBER;
	FUNCTION delete_user (username IN VARCHAR2) RETURN NUMBER;

	FUNCTION create_available_ttsuser RETURN NUMBER;
	FUNCTION delete_unavailable_ttsuser RETURN NUMBER;
END tts_user_admin;
/
