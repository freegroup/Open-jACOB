ALTER TABLE appprofile
	 ADD (interfacechanel NUMBER(2,0) DEFAULT 0 NULL)
;

ALTER TABLE edvintask
	 ADD (errortext NUMBER(38,0) NULL)
;


ALTER TABLE task
	 ADD (issynchronized NUMBER(2,0) DEFAULT 0 NULL)
;



ALTER TABLE tts_import
	 ADD (timestamp DATE NULL,origin VARCHAR2(40) DEFAULT 'unbekannt' NULL)
;



INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('appprofile','interfacechanel',14,0,'Email')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('appprofile','interfacechanel',14,1,'Signal')
;



INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('task','issynchronized',14,0,'Nein')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('task','issynchronized',14,1,'Ja')
;


ALTER TABLE process
	 ADD (checkwarrenty NUMBER(38,0) DEFAULT 0 NULL CHECK (checkwarrenty BETWEEN 0 AND 1))
;

ALTER TABLE calls
	 ADD (warrentystatus NUMBER(2,0) NULL)
;
	
INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('calls','warrentystatus',14,0,'überprüfen')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('calls','warrentystatus',14,1,'verfolgen')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('calls','warrentystatus',14,2,'nicht verfolgen')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('calls','warrentystatus',14,3,'wird verfolgt')
;



ALTER TABLE appprofile
	 ADD (warrentyend NUMBER(38,0) DEFAULT 42 NULL,wprocess_key NUMBER(38,0) NULL,warrentyagent_key NUMBER(38,0) NULL,wmanager_key NUMBER(38,0) NULL)
;



ALTER TABLE appprofile ADD (
	CONSTRAINT fk20_appprofile_warrentyagent_ FOREIGN KEY (warrentyagent_key) REFERENCES employee (pkey))
;


ALTER TABLE appprofile ADD (
	CONSTRAINT fk21_appprofile_wmanager_key FOREIGN KEY (wmanager_key) REFERENCES workgroup (pkey))
;


ALTER TABLE appprofile ADD (
	CONSTRAINT fk22_appprofile_wprocess_key FOREIGN KEY (wprocess_key) REFERENCES process (pkey))
;

CREATE INDEX ik11_object_object_above_idx ON object (object_above,ext_system_key)
;

CREATE OR REPLACE TRIGGER UpdateTTSImportOnInsert BEFORE INSERT
    ON "TTS_IMPORT" 
    FOR EACH ROW

BEGIN
   :new.timestamp := sysdate;
  
END;
/

-- reconfigure nach Datenbankänderung MBTEC

CREATE ROLE CQ_MBTECHAGENT
/



ALTER TABLE employee
	 ADD (mbtech_role NUMBER(38,0) DEFAULT 0 NULL CHECK (mbtech_role BETWEEN 0 AND 1))
;




ALTER TABLE calls
	 ADD (ordernumber VARCHAR2(20) NULL)
;


REVOKE ALL ON calls FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON calls TO PUBLIC
;


ALTER TABLE object
	 ADD (cmbtnumber VARCHAR2(20) NULL,hwtype NUMBER(2,0) NULL,standard NUMBER(2,0) NULL,hwswinfo NUMBER(38,0) NULL,objectowner_key NUMBER(38,0) NULL)
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('object','hwtype',14,0,'Laptop')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('object','hwtype',14,1,'Desktop')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('object','hwtype',14,2,'Drucker')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('object','hwtype',14,3,'Sonstiges')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('object','standard',14,0,'Nein')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('object','standard',14,1,'Ja')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('object','hwswinfo',10,0,'')
;



ALTER TABLE appprofile
	 ADD (mbtechgroup_key NUMBER(38,0) NULL,mbtechsystem_key NUMBER(38,0) NULL)
;



-- Creating referential constraints

ALTER TABLE object ADD (
	CONSTRAINT fk15_object_objectowner_key FOREIGN KEY (objectowner_key) REFERENCES employee (pkey))
;


ALTER TABLE appprofile ADD (
	CONSTRAINT fk17_appprofile_mbtechsystem_k FOREIGN KEY (mbtechsystem_key) REFERENCES ext_system (pkey))
;


ALTER TABLE appprofile ADD (
	CONSTRAINT fk18_appprofile_mbtechgroup_ke FOREIGN KEY (mbtechgroup_key) REFERENCES workgroup (pkey))
;

