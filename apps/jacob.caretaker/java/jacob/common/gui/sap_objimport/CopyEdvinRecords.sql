CREATE OR REPLACE PROCEDURE sap_object_import_cp
(
	edvin_ext_system_key IN INTEGER,
	sap_ext_system_key IN INTEGER,
	sap_object_count OUT INTEGER
)
AS

sap_object_pkey INTEGER;
sap_object_location_pkey INTEGER;
hwswinfo_pkey INTEGER;
currentdate DATE;

BEGIN
	--  einige Variablen initialisieren 
	sap_object_count:=0;
	currentdate:=SYSDATE;
	
	-- alle EDVIN-Objekte finden, welche deren externe Objekt-ID in der Austauschtabelle zu finden sind 
	FOR edvinobj_rec IN (SELECT edvin_object.*, 
	                            sap_first.pkey AS sap_first_pkey,
	                            sap_first.SAP_OBJECT AS sap_object_external_id,
	                            edvin_location.site_key AS l_site_key,
	                            edvin_location.sitepart_key AS l_sitepart_key,
	                            edvin_location.building_key AS l_building_key,
	                            edvin_location.buildingpart_key AS l_buildingpart_key,
	                            edvin_location.room_key AS l_room_key,
	                            edvin_location.floor_key AS l_floor_key,
	                            edvin_location.buildpartobj_key AS l_buildpartobj_key,
	                            edvin_location.gdsbaxis_key AS l_gdsbaxis_key,
	                            edvin_location.gdszaxis_key AS l_gdszaxis_key,
	                            edvin_location.baxis_key AS l_baxis_key,
	                            edvin_location.zaxis_key AS l_zaxis_key,
	                            edvin_location.description AS l_description,
	                            edvin_location.note AS l_note,
	                            edvin_location.orientation AS l_orientation,
	                            qw_hwswinfo.text AS t_hwswinfo
	                     FROM sap_first_objectimp sap_first, 
	                          object edvin_object, 
	                          location edvin_location,
	                          qw_text qw_hwswinfo
	                     WHERE sap_first.EDVIN_OBJECT=edvin_object.EXTERNAL_ID
	                           AND edvin_object.location_key=edvin_location.pkey(+)
						  AND edvin_object.hwswinfo=qw_hwswinfo.qwkey(+))
	LOOP
		-- SAP-Objekt bereits vorhanden?
		sap_object_pkey:=null;
		SELECT max(pkey) INTO sap_object_pkey FROM (SELECT pkey FROM object WHERE EXT_SYSTEM_KEY=sap_ext_system_key AND EXTERNAL_ID=edvinobj_rec.sap_object_external_id UNION select 0 as pkey FROM DUAL);
		IF ( sap_object_pkey<>0 ) THEN
			-- Write error reason
			UPDATE sap_first_objectimp SET error='SAP-Objekt (PKEY=' || sap_object_pkey || ') bereits vorhanden' WHERE pkey=edvinobj_rec.sap_first_pkey;
		ELSE
			-- EDVIN-Objekt bereits gelöscht (Status 6) ?
			IF ( edvinobj_rec.objstatus = 6 ) THEN
				-- Write error reason
				UPDATE sap_first_objectimp SET error='EDVIN-Objekt als gelöscht markiert' WHERE pkey=edvinobj_rec.sap_first_pkey;
			ELSE
				-- Location-Datensatz kopieren, wenn noetig
				sap_object_location_pkey:=null;
				IF ( edvinobj_rec.location_key IS NOT NULL ) THEN 
				   Q_NEXT_KEY('location', 1, sap_object_location_pkey);
		   
				   INSERT INTO location (
						pkey,
						site_key,
						sitepart_key,
						building_key,
						buildingpart_key,
						room_key,
						floor_key,
						buildpartobj_key,
						gdsbaxis_key,
						gdszaxis_key,
						baxis_key,
						zaxis_key,
						description,
						note,
						orientation
					) VALUES (
						sap_object_location_pkey,
						edvinobj_rec.l_site_key,
						edvinobj_rec.l_sitepart_key,
						edvinobj_rec.l_building_key,
						edvinobj_rec.l_buildingpart_key,
						edvinobj_rec.l_room_key,
						edvinobj_rec.l_floor_key,
						edvinobj_rec.l_buildpartobj_key,
						edvinobj_rec.l_gdsbaxis_key,
						edvinobj_rec.l_gdszaxis_key,
						edvinobj_rec.l_baxis_key,
						edvinobj_rec.l_zaxis_key,
						edvinobj_rec.l_description,
						edvinobj_rec.l_note,
						edvinobj_rec.l_orientation
					);
				END IF; 
		
				-- schon mal eine neue Objekt-ID holen
				Q_NEXT_KEY('object', 1, sap_object_pkey);
		
				-- hwswinfo-Langtext kopieren, wenn noetig
				hwswinfo_pkey:=null;
				IF ( edvinobj_rec.hwswinfo IS NOT NULL ) THEN 
				   Q_NEXT_KEY('qw_text', 1, hwswinfo_pkey);
		   
				   INSERT INTO qw_text (
						qwkey,
						tablename,
						fieldname,
						textkey,
						text
					) VALUES (
						hwswinfo_pkey,
						'object',
						'hwswinfo',
						sap_object_pkey,
						edvinobj_rec.t_hwswinfo
					);
				END IF; 
		
				-- neuen SAP-Objekt Datensatz erzeugen
				INSERT INTO object (
						pkey,
						external_id,
						name,
						objstatus,
						assettag,
						ext_system_key,
						location_key,
						objectcategory_key,
						accountingcode_key,
						vendor,
						change_user,
						datemodified,
						datecreated,
						priority,
						warranty_begin,
						warranty_end,
						warranty_extension,
						warranty_controled,
						supplierid,
						object_above,
						specification,
						cmbtnumber,
						hwtype,
						standard,
						hwswinfo,
						objectowner_key,
						sap_old_edvin_status,
						sap_edvin_object_key
					) VALUES (
						sap_object_pkey,
						edvinobj_rec.sap_object_external_id,
						edvinobj_rec.name,
						edvinobj_rec.objstatus,
						edvinobj_rec.assettag,
						sap_ext_system_key,
						sap_object_location_pkey,
						edvinobj_rec.objectcategory_key,
						edvinobj_rec.accountingcode_key,
						edvinobj_rec.vendor,
						'sapimport',
						currentdate,
						currentdate,
						edvinobj_rec.priority,
						edvinobj_rec.warranty_begin,
						edvinobj_rec.warranty_end,
						edvinobj_rec.warranty_extension,
						edvinobj_rec.warranty_controled,
						edvinobj_rec.supplierid,
						-- ggfs. anpassen?
						edvinobj_rec.object_above,
						edvinobj_rec.specification,
						edvinobj_rec.cmbtnumber,
						edvinobj_rec.hwtype,
						edvinobj_rec.standard,
						edvinobj_rec.hwswinfo,
						edvinobj_rec.objectowner_key,
						0,
						edvinobj_rec.pkey
					);
			
				-- und die zusaetzlichen Objectidentifikationen nicht vergessen
				INSERT INTO obj_ident (object_key, ident_name, ident_value) SELECT sap_object_pkey, oi.ident_name, oi.ident_value FROM obj_ident oi WHERE oi.object_key=edvinobj_rec.pkey;
			
				-- Alten EDVIN-Objektdatensatz auf gelöscht setzen, aber Status merken
				-- UPDATE object SET objstatus=6, sap_old_edvin_status=objstatus WHERE pkey=edvinobj_rec.pkey;
				-- wegen Migration lt. Sitzung vom 28.11.07 (Pross, Kasper, Schneider) wird Edvin Objekt nicht mehr gelöscht
				
				   UPDATE object SET sap_old_edvin_status=objstatus WHERE pkey=edvinobj_rec.pkey;
			
				-- Zaehler erhoehen
				sap_object_count:=sap_object_count+1;
			END IF;
		END IF;
	END LOOP;
	
	-- Alle Datensätze markieren zu welchen es kein EDVIN-Objekt gibt
	UPDATE sap_first_objectimp SET error='Kein EDVIN-Objekt vorhanden' WHERE EDVIN_OBJECT NOT IN (SELECT EXTERNAL_ID FROM object);
	
	-- do not commit since this will be done outside, i.e. by the caller
	-- COMMIT;
END sap_object_import_cp;

