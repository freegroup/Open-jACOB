
USE qamaster
go



GRANT EXECUTE ON qw_get_key_fields TO PUBLIC

go
/* Dropping referential constraints */



ALTER TABLE request DROP CONSTRAINT fk26_request_buildapprove_key

go


ALTER TABLE request DROP CONSTRAINT fk28_request_buildreport_key

go


ALTER TABLE request DROP CONSTRAINT fk27_request_builddone_key

go



/* Altering meta tables */




/* Adding tables */




CREATE TABLE attachment (
	pkey INT NOT NULL,
	datecreated DATETIME NULL,
	docdescr VARCHAR(255) NULL,
	document IMAGE NULL,
	filename VARCHAR(255) NULL,
	request_key INT NULL,
	CONSTRAINT pk0_attachment_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('attachment','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('attachment',0)

go


REVOKE ALL ON attachment FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON attachment TO PUBLIC

go



CREATE TABLE milestone (
	pkey INT NOT NULL,
	history TEXT NULL,
	description VARCHAR(255) NOT NULL,
	descriptiontext TEXT NULL,
	actualdone DATETIME NULL,
	plandone DATETIME NULL,
	milestonestatus INT DEFAULT 2 NOT NULL,
	milestonebuild_key INT NULL,
	CONSTRAINT pk1_milestone_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('milestone','pkey',6,0,'')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('milestone','milestonestatus',14,0,'Red')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('milestone','milestonestatus',14,1,'Yellow')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('milestone','milestonestatus',14,2,'Green')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('milestone','milestonestatus',14,3,'Done')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('milestone',0)

go


REVOKE ALL ON milestone FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON milestone TO PUBLIC

go



CREATE TABLE organization (
	pkey INT NOT NULL,
	name VARCHAR(120) NOT NULL,
	CONSTRAINT pk2_organization_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('organization','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('organization',0)

go


REVOKE ALL ON organization FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON organization TO PUBLIC

go



/* Dropping tables */




DROP TABLE dtproperties

go


DELETE FROM qw_fieldinfo WHERE table_name='dtproperties'

go


DELETE FROM qw_keys where tablename = 'dtproperties'

go



/* Altering tables */




ALTER TABLE employee
	 ADD password VARCHAR(20) NULL,organization_key INT NULL

go



ALTER TABLE build DROP CONSTRAINT pk23_build_pkey

go


sp_rename build,TMP_QUINTUS_TMP

go


DELETE FROM qw_fieldinfo WHERE table_name='build'

go


CREATE TABLE build (
	pkey INT NOT NULL,
	build VARCHAR(32) NOT NULL,
	notes TEXT NULL,
	category_key INT NULL,
	CONSTRAINT pk3_build_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('build','pkey',6,0,'')

go


REVOKE ALL ON build FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON build TO PUBLIC

go





INSERT INTO build (pkey,build,notes) SELECT pkey,build,notes FROM TMP_QUINTUS_TMP WHERE (pkey > -1)

go


DROP TABLE TMP_QUINTUS_TMP

go



ALTER TABLE request
	 ADD history TEXT NULL,organization_key INT NULL,milestone_key INT NULL

go




DELETE FROM qw_fieldinfo WHERE table_name='request' AND column_name='requeststatus'

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','requeststatus',14,0,'New')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','requeststatus',14,1,'Proved')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','requeststatus',14,2,'In progress')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','requeststatus',14,3,'QA')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','requeststatus',14,4,'Done')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','requeststatus',14,5,'QA Customer')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','requeststatus',14,6,'Obsolete')

go



/* Creating referential constraints */

ALTER TABLE employee ADD 
	CONSTRAINT fk0_employee_organization_key FOREIGN KEY (organization_key) REFERENCES organization (pkey)

go



ALTER TABLE build ADD 
	CONSTRAINT fk1_build_category_key FOREIGN KEY (category_key) REFERENCES category (pkey)

go



ALTER TABLE attachment ADD 
	CONSTRAINT fk2_attachment_request_key FOREIGN KEY (request_key) REFERENCES request (pkey)

go



ALTER TABLE milestone ADD 
	CONSTRAINT fk3_milestone_milestonebuild_k FOREIGN KEY (milestonebuild_key) REFERENCES build (pkey)

go


ALTER TABLE request ADD 
	CONSTRAINT fk4_request_buildreport_key FOREIGN KEY (buildreport_key) REFERENCES build (pkey)

go


ALTER TABLE request ADD 
	CONSTRAINT fk5_request_builddone_key FOREIGN KEY (builddone_key) REFERENCES build (pkey)

go


ALTER TABLE request ADD 
	CONSTRAINT fk6_request_milestone_key FOREIGN KEY (milestone_key) REFERENCES milestone (pkey)

go


ALTER TABLE request ADD 
	CONSTRAINT fk7_request_organization_key FOREIGN KEY (organization_key) REFERENCES organization (pkey)

go


ALTER TABLE request ADD 
	CONSTRAINT fk8_request_buildapprove_key FOREIGN KEY (buildapprove_key) REFERENCES build (pkey)

go




