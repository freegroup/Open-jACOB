CREATE OR REPLACE PROCEDURE replace_workgroup
(
	old_workgroup IN INTEGER,
	new_name IN VARCHAR,
	new_workgroup OUT INTEGER
)
AS

CURSOR akcursor IS SELECT * FROM workgroup WHERE pkey = old_workgroup;

wrkgrp_rec akcursor%ROWTYPE;

newid INTEGER;

newhwg_name workgroup.hwg_name%TYPE;
oldhwg_name workgroup.hwg_name%TYPE;

BEGIN
	new_workgroup := -1;
	OPEN akcursor;
	
	FETCH akcursor INTO wrkgrp_rec;
	IF ( akcursor%NOTFOUND ) THEN 
		raise_application_error(-20100, 'Workgroup not found');
	END IF; 

	IF ( wrkgrp_rec.wrkgrptype <> 0 AND wrkgrp_rec.wrkgrptype <> 1) THEN 
		raise_application_error(-20100, 'Workgroup is not an AK or an HWG');
	END IF; 
	
	IF ( wrkgrp_rec.groupstatus = 0 ) THEN 
		raise_application_error(-20100, 'Workgroup is not valid');
	END IF; 
	
	-- get new workgroup id
	Q_NEXT_KEY('workgroup', 1, new_workgroup);
	
	-- this is an ak?
	IF ( wrkgrp_rec.wrkgrptype = 0) THEN 
		-- ak: keep hwg_name and set hwg_name of new ak to null
		oldhwg_name := wrkgrp_rec.hwg_name;
		newhwg_name := null;
	ELSE
		-- hwg: inherit hwg_name of old hwg and set hwg_name of old to null
		-- note: hwg_name is used by EIMAN to identify a hwg in TTS
		oldhwg_name := null;
		newhwg_name := wrkgrp_rec.hwg_name;
	END IF;
	
	-- set status of old workgroup to invalid
	-- and set hwg_name as well (note: there is a unique index on hwg_name!)
	UPDATE workgroup SET groupstatus = 0, hwg_name = oldhwg_name WHERE pkey = wrkgrp_rec.pkey;
	
	-- create new workgroup
	INSERT INTO workgroup (
			pkey,
			name,
			description,
			notificationaddr,
			notifymethod,
			phone,
			fax,
			wrkgrptype,
			organization_key,
			accountingcode,
			hwg_title,
			hwg_shift,
			hwg_manager,
			hwg_costingtype,
			hwg_rate,
			hwg_noemployees,
			hwg_weekdays,
			hwg_actualcost,
			hwg_timelimit,
			hwg_hours,
			ak_site,
			hwg_name,
			orgexternal_key,
			migration,
			email,
			groupconferencecal,
			autodocumented,
			groupstatus,
			notifyowngroup,
			activitytype,
			autoclosed
		) VALUES (
			new_workgroup,
			new_name,
			wrkgrp_rec.description,
			wrkgrp_rec.notificationaddr,
			wrkgrp_rec.notifymethod,
			wrkgrp_rec.phone,
			wrkgrp_rec.fax,
			wrkgrp_rec.wrkgrptype,
			wrkgrp_rec.organization_key,
			wrkgrp_rec.accountingcode,
			wrkgrp_rec.hwg_title,
			wrkgrp_rec.hwg_shift,
			wrkgrp_rec.hwg_manager,
			wrkgrp_rec.hwg_costingtype,
			wrkgrp_rec.hwg_rate,
			wrkgrp_rec.hwg_noemployees,
			wrkgrp_rec.hwg_weekdays,
			wrkgrp_rec.hwg_actualcost,
			wrkgrp_rec.hwg_timelimit,
			wrkgrp_rec.hwg_hours,
			wrkgrp_rec.ak_site,
			newhwg_name,
			wrkgrp_rec.orgexternal_key,
			wrkgrp_rec.migration,
			wrkgrp_rec.email,
			wrkgrp_rec.groupconferencecal,
			wrkgrp_rec.autodocumented, 
			1,
			wrkgrp_rec.notifyowngroup,
			wrkgrp_rec.activitytype,
			wrkgrp_rec.autoclosed
		);
		
	-- copy group members
	FOR grpmbr_rec IN (SELECT * FROM groupmember WHERE workgroupgroup = wrkgrp_rec.pkey)
	LOOP
		Q_NEXT_KEY('groupmember', 1, newid);
		INSERT INTO groupmember (
				pkey,
				employeegroup,
				workgroupgroup,
				notifymethod,
				tier,
				accessallowed,
				xsl_stylesheet
			) VALUES (
				newid,
				grpmbr_rec.employeegroup,
				new_workgroup,
				grpmbr_rec.notifymethod,
				grpmbr_rec.tier,
				grpmbr_rec.accessallowed,
				grpmbr_rec.xsl_stylesheet
			);
	END LOOP;
		
	-- handle N:M association between aks and hwgs
	-- NOTE: We do not copy association entries, since this is more easy to operate with.
	--       Nevertheless we loss relations between old (invalid) aks and hwgs!!!
	UPDATE workgrouphwg SET workgroup_key = new_workgroup WHERE workgroup_key = wrkgrp_rec.pkey;
	UPDATE workgrouphwg SET hwg_key = new_workgroup WHERE hwg_key = wrkgrp_rec.pkey;
	
	-- change workgroup references of master data
	UPDATE categoryprocess SET workgroup_key = new_workgroup WHERE workgroup_key = wrkgrp_rec.pkey;
	
	UPDATE callescalation SET escgroupescalation = new_workgroup WHERE escgroupescalation = wrkgrp_rec.pkey;
	UPDATE callescalation SET groupescalation = new_workgroup WHERE groupescalation = wrkgrp_rec.pkey;

	UPDATE callaction SET groupaction = new_workgroup WHERE groupaction = wrkgrp_rec.pkey;

	UPDATE calltemplate SET callworkgroup_key = new_workgroup WHERE callworkgroup_key = wrkgrp_rec.pkey;

	UPDATE appprofile SET callworkgroup_key = new_workgroup WHERE callworkgroup_key = wrkgrp_rec.pkey;
	UPDATE appprofile SET problemmanager_key = new_workgroup WHERE problemmanager_key = wrkgrp_rec.pkey;
	UPDATE appprofile SET mbtechgroup_key = new_workgroup WHERE mbtechgroup_key = wrkgrp_rec.pkey;
	UPDATE appprofile SET wmanager_key = new_workgroup WHERE wmanager_key = wrkgrp_rec.pkey;

	UPDATE hwgtasktype SET taskworkgroup_key = new_workgroup WHERE taskworkgroup_key = wrkgrp_rec.pkey;

	-- change workgroup references of transaction data
	IF ( wrkgrp_rec.wrkgrptype = 0) THEN 
		-- ak: all unclosed (Geschlossen) calls should point to the new ak.
		--     This is because for those tasks could be still generated
		UPDATE calls SET workgroupcall = new_workgroup WHERE workgroupcall = wrkgrp_rec.pkey AND callstatus<9;
	ELSE
		-- hwg: all unclosed (Abgerechnet or Geschlossen) tasks should point to the new hwg
		--      This is because for those data could be still transfered to/from EDVIN via EIMAN
		UPDATE task SET workgrouptask = new_workgroup WHERE workgrouptask = wrkgrp_rec.pkey AND taskstatus<7;
	END IF;


	CLOSE akcursor;
	
	-- and commit the staff
	COMMIT;
END replace_workgroup;

