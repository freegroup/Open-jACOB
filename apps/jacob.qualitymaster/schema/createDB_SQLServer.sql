/* Starting CONFIGURE */




CREATE DATABASE qamaster

go




sp_dboption qamaster,'select into/bulkcopy', 'true'

go

sp_dboption qamaster,'trunc. log on chkpt.', 'true'

go

use qamaster

go

checkpoint

go

USE qamaster
go




/* Adding tables */




CREATE TABLE qw_keys (
	tablename VARCHAR(240) NOT NULL,
	keyvalue INT NOT NULL,
	CONSTRAINT pk0_qw_keys_tablename PRIMARY KEY (tablename))

go


REVOKE ALL ON qw_keys FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_keys TO PUBLIC

go



CREATE TABLE qw_fieldinfo (
	table_name VARCHAR(18) NOT NULL,
	column_name VARCHAR(18) NOT NULL,
	column_type INT NOT NULL,
	enum_value INT NULL,
	enum_label VARCHAR(128) NULL)

go


CREATE INDEX ik1_qw_fieldinfo_IKey ON qw_fieldinfo (table_name,column_name)

go


REVOKE ALL ON qw_fieldinfo FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_fieldinfo TO PUBLIC

go



CREATE TABLE qw_alert (
	qwkey INT NOT NULL,
	addressee VARCHAR(240) NULL,
	sender VARCHAR(240) NULL,
	tablename VARCHAR(20) NULL,
	tablekey INT NULL,
	message VARCHAR(240) NULL,
	severity INT NULL,
	alerttype VARCHAR(20) NULL,
	dateposted DATETIME NULL,
	CONSTRAINT pk2_qw_alert_qwkey PRIMARY KEY (qwkey))

go


CREATE INDEX ik3_qw_alert_addressee ON qw_alert (addressee)

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_alert','qwkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_alert',0)

go


REVOKE ALL ON qw_alert FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_alert TO PUBLIC

go



CREATE TABLE qw_item (
	pkey INT NOT NULL,
	batch_key INT NOT NULL,
	reports_key INT NULL,
	itempack_key INT NULL,
	itemid VARCHAR(40) NOT NULL,
	agentid VARCHAR(240) NULL,
	itemtype INT NOT NULL,
	itemtext TEXT NULL,
	email VARCHAR(240) NULL,
	subject VARCHAR(240) NULL,
	faxnumber VARCHAR(40) NULL,
	mr_ms VARCHAR(20) NULL,
	name VARCHAR(240) NULL,
	organization VARCHAR(240) NULL,
	zipcode VARCHAR(40) NULL,
	datecreated DATETIME NULL,
	itemsent INT DEFAULT 0 NULL CHECK (itemsent BETWEEN 0 AND 1),
	dateitemfirstsent DATETIME NULL,
	dateitemlastsent DATETIME NULL,
	sec1sent INT DEFAULT 0 NULL CHECK (sec1sent BETWEEN 0 AND 1),
	datesec1firstsent DATETIME NULL,
	datesec1lastsent DATETIME NULL,
	sec2sent INT DEFAULT 0 NULL CHECK (sec2sent BETWEEN 0 AND 1),
	datesec2firstsent DATETIME NULL,
	datesec2lastsent DATETIME NULL,
	CONSTRAINT pk4_qw_item_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','pkey',6,0,'')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,0,'Letter')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,1,'CC Letter')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,2,'BCC Letter')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,3,'Label')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,4,'Form')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,5,'Misc')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_item',0)

go


REVOKE ALL ON qw_item FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_item TO PUBLIC

go



CREATE TABLE qw_itempack (
	pkey INT NOT NULL,
	batch_key INT NOT NULL,
	templatepack_key INT NULL,
	agentid VARCHAR(40) NULL,
	recipient VARCHAR(40) NULL,
	datecreated DATETIME NULL,
	CONSTRAINT pk5_qw_itempack_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_itempack','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_itempack',0)

go


REVOKE ALL ON qw_itempack FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_itempack TO PUBLIC

go



CREATE TABLE qw_constraints (
	qwkey INT NOT NULL,
	name CHAR(30) NULL,
	username CHAR(30) NOT NULL,
	focusname CHAR(30) NOT NULL,
	control CHAR(240) NULL,
	fcontrol CHAR(240) NULL,
	doption CHAR(240) NULL,
	cgroup CHAR(240) NULL,
	CONSTRAINT pk6_qw_constraints_qwkey PRIMARY KEY (qwkey))

go


CREATE INDEX ik7_qw_constraints_IKey ON qw_constraints (focusname,username)

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraints','qwkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_constraints',0)

go


REVOKE ALL ON qw_constraints FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_constraints TO PUBLIC

go



CREATE TABLE qw_adl (
	pkey INT NOT NULL,
	adl TEXT NULL,
	adl_version INT NULL,
	adl_timestamp DATETIME NULL,
	name VARCHAR(20) NULL,
	CONSTRAINT pk8_qw_adl_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk9_qw_adl_UKey UNIQUE (adl_version,name))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_adl','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_adl',0)

go


REVOKE ALL ON qw_adl FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_adl TO PUBLIC

go



CREATE TABLE qw_flow_page_xml (
	pkey INT NOT NULL,
	flow_key INT NULL,
	name VARCHAR(80) NULL,
	version INT DEFAULT 1 NULL,
	text TEXT NULL,
	CONSTRAINT pk10_qw_flow_page_xml_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow_page_xml','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_flow_page_xml',0)

go


REVOKE ALL ON qw_flow_page_xml FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_flow_page_xml TO PUBLIC

go



CREATE TABLE qw_response (
	pkey INT NOT NULL,
	question VARCHAR(80) NULL,
	answer VARCHAR(240) NULL,
	score INT NULL CHECK (score BETWEEN 0 AND 100),
	dateentered DATETIME NOT NULL,
	CONSTRAINT pk11_qw_response_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_response','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_response',0)

go


REVOKE ALL ON qw_response FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_response TO PUBLIC

go



CREATE TABLE employee (
	pkey INT NOT NULL,
	fullname VARCHAR(64) NULL,
	loginname VARCHAR(40) NULL,
	emailcorr VARCHAR(40) NULL,
	phone VARCHAR(30) NULL,
	mobile VARCHAR(30) NULL,
	CONSTRAINT pk12_employee_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('employee','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('employee',0)

go


REVOKE ALL ON employee FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON employee TO PUBLIC

go



CREATE TABLE qw_locks (
	tablename VARCHAR(30) NOT NULL,
	keyvalue VARCHAR(128) NOT NULL,
	username VARCHAR(30) NULL,
	hostname VARCHAR(128) NULL,
	pid INT NULL,
	locktime DATETIME NULL,
	CONSTRAINT pk13_qw_locks_PKey PRIMARY KEY (tablename,keyvalue))

go


REVOKE ALL ON qw_locks FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_locks TO PUBLIC

go



CREATE TABLE qw_forms (
	pkey INT NOT NULL,
	qw_appsforms INT NOT NULL,
	name VARCHAR(20) NULL,
	text TEXT NULL,
	text_timestamp DATETIME NULL,
	CONSTRAINT pk14_qw_forms_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk15_qw_forms_UKey UNIQUE (qw_appsforms,name))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_forms','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_forms',0)

go


REVOKE ALL ON qw_forms FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_forms TO PUBLIC

go



CREATE TABLE qw_reporttmplpk0 (
	report INT NOT NULL,
	templatepack INT NOT NULL,
	CONSTRAINT pk16_qw_reporttmplpk0_PKey PRIMARY KEY (report,templatepack))

go


REVOKE ALL ON qw_reporttmplpk0 FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_reporttmplpk0 TO PUBLIC

go



CREATE TABLE qw_schedule (
	qwkey INT NOT NULL,
	name VARCHAR(80) NULL,
	description VARCHAR(80) NULL,
	scheduleyear VARCHAR(4) NULL,
	sunstart VARCHAR(12) NULL,
	sunend VARCHAR(12) NULL,
	monstart VARCHAR(12) NULL,
	monend VARCHAR(12) NULL,
	tuestart VARCHAR(12) NULL,
	tueend VARCHAR(12) NULL,
	wedstart VARCHAR(12) NULL,
	wedend VARCHAR(12) NULL,
	thustart VARCHAR(12) NULL,
	thuend VARCHAR(12) NULL,
	fristart VARCHAR(12) NULL,
	friend VARCHAR(12) NULL,
	satstart VARCHAR(12) NULL,
	satend VARCHAR(12) NULL,
	janholiday INT NULL,
	febholiday INT NULL,
	marholiday INT NULL,
	aprholiday INT NULL,
	mayholiday INT NULL,
	junholiday INT NULL,
	julholiday INT NULL,
	augholiday INT NULL,
	sepholiday INT NULL,
	octholiday INT NULL,
	novholiday INT NULL,
	decholiday INT NULL,
	CONSTRAINT pk17_qw_schedule_qwkey PRIMARY KEY (qwkey))

go


CREATE INDEX ik18_qw_schedule_IKey ON qw_schedule (name,scheduleyear)

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_schedule','qwkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_schedule',0)

go


REVOKE ALL ON qw_schedule FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_schedule TO PUBLIC

go



CREATE TABLE qw_constraint (
	qwkey INT NOT NULL,
	name CHAR(30) NOT NULL,
	username CHAR(30) NOT NULL,
	focusname CHAR(30) NOT NULL,
	formname VARCHAR(30) NULL,
	groupname VARCHAR(30) NULL,
	tablename VARCHAR(30) NULL,
	value VARCHAR(240) NULL,
	keyvalue VARCHAR(30) NULL,
	controlname VARCHAR(30) NULL,
	type INT NULL,
	fieldname VARCHAR(30) NULL,
	targetname VARCHAR(30) NULL,
	CONSTRAINT pk19_qw_constraint_qwkey PRIMARY KEY (qwkey))

go


CREATE INDEX ik20_qw_constraint_IKey ON qw_constraint (focusname,username)

go


CREATE INDEX ik21_qw_constraint_name ON qw_constraint (name)

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','qwkey',6,0,'')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,0,'CONTROL')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,1,'FOREIGN CONTROL')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,2,'GROUP')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,3,'DISJUNCTION')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,4,'IFB')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_constraint',0)

go


REVOKE ALL ON qw_constraint FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_constraint TO PUBLIC

go



CREATE TABLE build (
	pkey INT NOT NULL,
	version VARCHAR(32) NOT NULL,
	build VARCHAR(32) NOT NULL,
	name VARCHAR(32) NOT NULL,
	notes TEXT NULL,
	CONSTRAINT pk22_build_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('build','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('build',0)

go


REVOKE ALL ON build FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON build TO PUBLIC

go



CREATE TABLE timespent (
	pkey INT NOT NULL,
	category INT NULL,
	timespent INT NOT NULL,
	summary VARCHAR(256) NULL,
	employee_key INT NULL,
	request_key INT NULL,
	datereported DATETIME NULL,
	CONSTRAINT pk23_timespent_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('timespent','pkey',6,0,'')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('timespent','category',14,0,'Coding')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('timespent','category',14,1,'Testing')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('timespent','category',14,2,'Documention')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('timespent','category',14,3,'Marketing')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('timespent','category',14,4,'other')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('timespent',0)

go


REVOKE ALL ON timespent FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON timespent TO PUBLIC

go



CREATE TABLE qw_batch (
	pkey INT NOT NULL,
	name VARCHAR(40) NOT NULL,
	batchstatus INT NOT NULL,
	batchtype INT NOT NULL,
	datecreated DATETIME NULL,
	datemodified DATETIME NULL,
	datefrozen DATETIME NULL,
	datelastprocessed DATETIME NULL,
	dateprocessed DATETIME NULL,
	dateclosed DATETIME NULL,
	barcodes INT NULL CHECK (barcodes BETWEEN 0 AND 1),
	labelformat VARCHAR(20) NULL,
	sortorder INT NOT NULL,
	sec1dest_key INT NULL,
	sec1copies INT DEFAULT 1 NULL,
	sec2dest_key INT NULL,
	sec2copies INT DEFAULT 1 NULL,
	ccdest_key INT NULL,
	cccopies INT DEFAULT 1 NULL,
	bccdest_key INT NULL,
	bcccopies INT DEFAULT 1 NULL,
	labeldest_key INT NULL,
	labelcopies INT DEFAULT 1 NULL,
	miscdest_key INT NULL,
	misccopies INT DEFAULT 1 NULL,
	CONSTRAINT pk24_qw_batch_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','pkey',6,0,'')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchstatus',14,0,'Open')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchstatus',14,1,'Frozen')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchstatus',14,2,'Processed')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchstatus',14,3,'Closed')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchtype',14,0,'Perpetual')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchtype',14,1,'Single-Use')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','sortorder',14,0,'None')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','sortorder',14,1,'Agent')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','sortorder',14,2,'Zipcode')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_batch',0)

go


REVOKE ALL ON qw_batch FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_batch TO PUBLIC

go



CREATE TABLE qw_doc (
	qwkey INT NOT NULL,
	eventkey INT NULL,
	ref VARCHAR(240) NULL,
	filename VARCHAR(240) NULL,
	type INT NULL,
	sequence INT NULL,
	doc IMAGE NULL,
	CONSTRAINT pk25_qw_doc_qwkey PRIMARY KEY (qwkey))

go


CREATE INDEX ik26_qw_doc_eventkey ON qw_doc (eventkey)

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_doc','qwkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_doc',0)

go


REVOKE ALL ON qw_doc FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_doc TO PUBLIC

go



CREATE TABLE qw_scripts (
	qwkey INT NOT NULL,
	name VARCHAR(40) NULL,
	app VARCHAR(20) NULL,
	version VARCHAR(20) NULL,
	type INT NULL,
	template VARCHAR(240) NULL,
	text TEXT NULL,
	compiled INT NULL,
	time DATETIME NULL,
	compiletime DATETIME NULL,
	binary IMAGE NULL,
	CONSTRAINT pk27_qw_scripts_qwkey PRIMARY KEY (qwkey),
	CONSTRAINT uk28_qw_scripts_UKey UNIQUE (version,name))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_scripts','qwkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_scripts',0)

go


REVOKE ALL ON qw_scripts FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_scripts TO PUBLIC

go



CREATE TABLE qw_reports (
	pkey INT NOT NULL,
	reportname VARCHAR(40) NULL,
	description VARCHAR(240) NULL,
	focus VARCHAR(40) NULL,
	ispublic INT NULL,
	anchortable VARCHAR(18) NULL,
	relationset VARCHAR(40) NULL,
	reportowner VARCHAR(20) NULL,
	datemodified DATETIME NULL,
	reportspec TEXT NULL,
	reporttype INT DEFAULT 0 NULL,
	outputformat INT DEFAULT 0 NULL,
	hasbackground INT NULL CHECK (hasbackground BETWEEN 0 AND 1),
	background IMAGE NULL,
	CONSTRAINT pk29_qw_reports_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_reports','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_reports',0)

go


REVOKE ALL ON qw_reports FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_reports TO PUBLIC

go



CREATE TABLE qw_flow (
	pkey INT NOT NULL,
	flowset_key INT NULL,
	name VARCHAR(80) NOT NULL,
	version INT DEFAULT 1 NULL,
	type INT DEFAULT 0 NULL,
	text TEXT NULL,
	CONSTRAINT pk30_qw_flow_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_flow',0)

go


REVOKE ALL ON qw_flow FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_flow TO PUBLIC

go



CREATE TABLE qw_flow_page_dhtml (
	pkey INT NOT NULL,
	flow_key INT NULL,
	name VARCHAR(80) NULL,
	version INT DEFAULT 1 NULL,
	text TEXT NULL,
	CONSTRAINT pk31_qw_flow_page_dhtml_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow_page_dhtml','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_flow_page_dhtml',0)

go


REVOKE ALL ON qw_flow_page_dhtml FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_flow_page_dhtml TO PUBLIC

go



CREATE TABLE qw_templatepack (
	pkey INT NOT NULL,
	packname VARCHAR(40) NOT NULL,
	description VARCHAR(240) NULL,
	focus VARCHAR(40) NULL,
	ispublic INT NULL,
	anchortable VARCHAR(18) NULL,
	relationset VARCHAR(40) NULL,
	packowner VARCHAR(20) NULL,
	destination VARCHAR(40) NULL,
	addressee VARCHAR(18) NULL,
	ccaliaslist VARCHAR(80) NULL,
	bccaliaslist VARCHAR(80) NULL,
	datemodified DATETIME NULL,
	CONSTRAINT pk32_qw_templatepack_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_templatepack','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_templatepack',0)

go


REVOKE ALL ON qw_templatepack FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_templatepack TO PUBLIC

go



CREATE TABLE qw_apps (
	pkey INT NOT NULL,
	qw_adlapps INT NOT NULL,
	name VARCHAR(20) NULL,
	title VARCHAR(20) NULL,
	help16 IMAGE NULL,
	help16_timestamp DATETIME NULL,
	help32 IMAGE NULL,
	help32_timestamp DATETIME NULL,
	help32_cnt IMAGE NULL,
	CONSTRAINT pk33_qw_apps_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk34_qw_apps_UKey UNIQUE (qw_adlapps,name))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_apps','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_apps',0)

go


REVOKE ALL ON qw_apps FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_apps TO PUBLIC

go



CREATE TABLE qw_rptsched (
	pkey INT NOT NULL,
	scheduledrptkey INT NOT NULL,
	rptrepeat INT DEFAULT 0 NULL,
	rptwhen DATETIME NULL,
	rptemail INT DEFAULT 0 NULL,
	rptemailto VARCHAR(240) NULL,
	rptemailcc VARCHAR(240) NULL,
	rptemailsubj VARCHAR(160) NULL,
	rptfax INT DEFAULT 0 NULL,
	rptfaxto VARCHAR(240) NULL,
	rptfaxno VARCHAR(20) NULL,
	rptfaxsubj VARCHAR(160) NULL,
	rptprint INT DEFAULT 0 NULL,
	rptcommand VARCHAR(40) NULL,
	rptswitches VARCHAR(40) NULL,
	rpttitle VARCHAR(160) NULL,
	rptfile INT DEFAULT 0 NULL,
	rptfilename VARCHAR(160) NULL,
	rptappend INT DEFAULT 0 NULL,
	CONSTRAINT pk35_qw_rptsched_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_rptsched','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_rptsched',0)

go


REVOKE ALL ON qw_rptsched FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_rptsched TO PUBLIC

go



CREATE TABLE qw_flowset (
	pkey INT NOT NULL,
	name VARCHAR(80) NOT NULL,
	version INT DEFAULT 1 NULL,
	CONSTRAINT pk36_qw_flowset_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flowset','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_flowset',0)

go


REVOKE ALL ON qw_flowset FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_flowset TO PUBLIC

go



CREATE TABLE qw_consolelayout (
	pkey INT NOT NULL,
	text TEXT NULL,
	description VARCHAR(240) NULL,
	name VARCHAR(40) NULL,
	datemodified DATETIME NULL,
	qw_apps_key INT NULL,
	CONSTRAINT pk37_qw_consolelayout_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk38_qw_consolelayout_UKey UNIQUE (qw_apps_key,name))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_consolelayout','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_consolelayout',0)

go


REVOKE ALL ON qw_consolelayout FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_consolelayout TO PUBLIC

go



CREATE TABLE qw_events (
	qwkey INT NOT NULL,
	qw_when DATETIME NULL,
	action INT DEFAULT 0 NULL,
	workgroupkey INT NULL,
	ownerkey INT NULL,
	escalationkey INT NULL,
	state INT DEFAULT 0 NULL,
	tablename VARCHAR(20) NULL,
	tablekey INT NULL,
	severity INT NULL,
	message VARCHAR(240) NULL,
	addressee VARCHAR(240) NULL,
	sender VARCHAR(240) NULL,
	address2 VARCHAR(240) NULL,
	escstatus INT NULL,
	tier INT NULL,
	repeatinterval INT NULL,
	agent INT DEFAULT 0 NULL,
	datemodified DATETIME NULL,
	rptschedkey INT NULL,
	workgroupkeyname VARCHAR(40) NULL,
	ownerkeyname VARCHAR(40) NULL,
	CONSTRAINT pk39_qw_events_qwkey PRIMARY KEY (qwkey))

go


CREATE INDEX ik40_qw_events_IKey1 ON qw_events (action,state)

go


CREATE INDEX ik41_qw_events_IKey2 ON qw_events (tablename,tablekey)

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_events','qwkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_events',0)

go


REVOKE ALL ON qw_events FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_events TO PUBLIC

go



CREATE TABLE qw_routinginfo (
	pkey INT NOT NULL,
	batch_key INT NOT NULL,
	reports_key INT NOT NULL,
	destination_key INT NULL,
	copies INT DEFAULT 1 NULL,
	CONSTRAINT pk42_qw_routinginfo_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_routinginfo','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_routinginfo',0)

go


REVOKE ALL ON qw_routinginfo FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_routinginfo TO PUBLIC

go



CREATE TABLE qw_destination (
	pkey INT NOT NULL,
	name VARCHAR(40) NOT NULL,
	method INT NOT NULL,
	datecreated DATETIME NULL,
	datemodified DATETIME NULL,
	printername VARCHAR(120) NULL,
	printersource VARCHAR(24) DEFAULT 'Automatically Select' NULL,
	printersourcebin INT DEFAULT 0 NULL,
	topmargin VARCHAR(5) NULL,
	leftmargin VARCHAR(5) NULL,
	bottommargin VARCHAR(5) NULL,
	rightmargin VARCHAR(5) NULL,
	marginunits INT NULL,
	filename VARCHAR(255) NULL,
	CONSTRAINT pk43_qw_destination_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','pkey',6,0,'')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','method',14,0,'Print')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','method',14,1,'Email')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','method',14,2,'Fax')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','method',14,3,'File')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','marginunits',14,0,'Inches')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','marginunits',14,1,'Cms')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_destination',0)

go


REVOKE ALL ON qw_destination FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_destination TO PUBLIC

go



CREATE TABLE qw_dbinfo (
	qwkey INT NOT NULL,
	type INT NULL,
	value INT NULL,
	dbcomment VARCHAR(240) NULL,
	CONSTRAINT pk44_qw_dbinfo_qwkey PRIMARY KEY (qwkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_dbinfo','qwkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_dbinfo',0)

go


REVOKE ALL ON qw_dbinfo FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_dbinfo TO PUBLIC

go



CREATE TABLE request (
	pkey INT NOT NULL,
	planstart DATETIME NULL,
	plandone DATETIME NULL,
	actualstart DATETIME NULL,
	actualdone DATETIME NULL,
	description VARCHAR(255) NOT NULL,
	descriptiontext TEXT NULL,
	plantime INT NULL,
	actualtime INT NULL,
	requeststatus INT DEFAULT 0 NOT NULL,
	priority INT DEFAULT 0 NOT NULL,
	reporter_key INT NULL,
	buildreport_key INT NULL,
	approver_key INT NULL,
	buildapprove_key INT NULL,
	builddone_key INT NULL,
	tester_key INT NULL,
	datereported DATETIME NULL,
	owner_key INT NULL,
	CONSTRAINT pk45_request_pkey PRIMARY KEY (pkey))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','pkey',6,0,'')

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
	VALUES ('request','priority',14,0,'Low')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','priority',14,1,'Medium')

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('request','priority',14,2,'High')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('request',0)

go


REVOKE ALL ON request FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON request TO PUBLIC

go



CREATE TABLE qw_eduviewerlayout (
	pkey INT NOT NULL,
	text TEXT NULL,
	description VARCHAR(240) NULL,
	name VARCHAR(40) NULL,
	datemodified DATETIME NULL,
	qw_apps_key INT NULL,
	CONSTRAINT pk46_qw_eduviewerlayout_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk47_qw_eduviewerlayout_UKey UNIQUE (qw_apps_key,name))

go


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_eduviewerlayout','pkey',6,0,'')

go


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_eduviewerlayout',0)

go


REVOKE ALL ON qw_eduviewerlayout FROM PUBLIC

go


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_eduviewerlayout TO PUBLIC

go



/* Creating referential constraints */








































ALTER TABLE request ADD 
	CONSTRAINT fk23_request_approver_key FOREIGN KEY (approver_key) REFERENCES employee (pkey),
	CONSTRAINT fk24_request_buildapprove_key FOREIGN KEY (buildapprove_key) REFERENCES build (pkey),
	CONSTRAINT fk25_request_builddone_key FOREIGN KEY (builddone_key) REFERENCES build (pkey),
	CONSTRAINT fk26_request_buildreport_key FOREIGN KEY (buildreport_key) REFERENCES build (pkey),
	CONSTRAINT fk27_request_owner_key FOREIGN KEY (owner_key) REFERENCES employee (pkey),
	CONSTRAINT fk28_request_reporter_key FOREIGN KEY (reporter_key) REFERENCES employee (pkey),
	CONSTRAINT fk29_request_tester_key FOREIGN KEY (tester_key) REFERENCES employee (pkey)

go



ALTER TABLE timespent ADD 
	CONSTRAINT fk30_timespent_employee_key FOREIGN KEY (employee_key) REFERENCES employee (pkey),
	CONSTRAINT fk31_timespent_request_key FOREIGN KEY (request_key) REFERENCES request (pkey)

go






/* Creating stored procedures */




CREATE PROCEDURE q_create_lock						
(									
	@a_table        varchar(30),					
	@a_key          varchar(128),					
	@a_user         varchar(30),					
	@a_host         varchar(128),					
	@a_pid          int,						
	@a_time_lock    int,        					
	@a_try_insert   int,						
	@ret_string     varchar(255) OUTPUT				
) AS									
declare @r_user		varchar(30),					
	@r_host		varchar(128),					
	@r_pid		int,						
	@status		int,						
	@v_i		int						
									
/* initialize variables */						
select @v_i		= 0						
									
									
while (@v_i < @a_try_insert)						
begin									
select @v_i = @v_i + 1			/* loop control */		
select @status = 0							
									
/*	-- try and slam the record in	*/				
insert into  qw_locks							
values (@a_table, @a_key, @a_user, @a_host, @a_pid, getdate())		
									
if @@error != 0								
begin									
	select @status = 1						
end									
									
if @status = 0								
begin									
	select @r_user = @a_user					
	select @r_host = @a_host					
	select @r_pid  = @a_pid						
end									
else									
begin									
	/* -- a lock exists, try and grab it if stale	*/		
	update qw_locks							
	set username  = @a_user,					
	    hostname  = @a_host,					
	    pid       = @a_pid,						
	    locktime  = getdate()					
	where tablename = @a_table					
	and   keyvalue  = @a_key					
	and   dateadd(second, @a_time_lock, locktime) < getdate()	
									
	if @@rowcount = 0						
	begin								
		/* -- someone else has the lock */			
		select	@r_user = username,				
			@r_host = hostname,				
			@r_pid  = pid					
		from	qw_locks					
		where	tablename = @a_table				
		and	keyvalue  = @a_key				
									
		if @@rowcount = 0					
		begin							
			select @r_host = null				
			select @r_user = null				
			select @r_pid  = null				
			select @status = 2				
		end							
	end								
	else								
	begin								
		select @r_user = @a_user				
		select @r_host = @a_host				
		select @r_pid  = @a_pid					
	end								
end									
									
/* we continue looping only if slam and select failed (probably		
   the record was there when we started, but was removed before select	
*/									
if @status != 2								
	break								
end /* while */								
select @ret_string = 'Return / /' + @r_user + '/' + @r_host + '/'
		+ CONVERT(varchar,@r_pid) + '/'			
if @@error != 0								
begin									
	return (1)							
end									
else									
begin									
	return (0)							
end									


go


GRANT EXECUTE ON q_create_lock TO PUBLIC

go



CREATE PROCEDURE qw_create_lock					
(									
	@a_table        varchar(30),					
	@a_key          int,						
	@a_user         varchar(30),					
	@a_host         varchar(128),					
	@a_pid          int,						
	@a_time_lock    int,        					
	@a_try_insert   int,						
	@ret_string     varchar(255) OUTPUT				
) AS									
declare @a_keystr	varchar(128)					
									
	select @a_keystr = convert(varchar, @a_key)			
	execute q_create_lock @a_table, @a_keystr, @a_user, @a_host,	
		@a_pid, @a_time_lock, @a_try_insert, @ret_string output 
	return (@@error)						


go


GRANT EXECUTE ON qw_create_lock TO PUBLIC

go



/*
-- q_drop_lock
--
-- Drop	a particular application-level lock. This takes the same arguments
-- as q_create_lock.
-- We return the holder of the lock to show success/failure.
-- Thus, there are two possible failures:
-- (i) someone else had the lock, and
-- (ii) there was no lock record.
*/
CREATE PROCEDURE q_drop_lock
(
        @a_table            varchar(30),
        @a_key              varchar(128),
        @a_user             varchar(30),
        @a_host             varchar(128),
        @a_pid              int,
        @ret_string         varchar(255) OUTPUT
) AS
    declare @r_user  varchar(30),
            @r_host  varchar(128),
            @r_pid   int

	/* -- lets just try to delete the lock */
	delete
	from  qw_locks
	where tablename = @a_table
	and   keyvalue  = @a_key
	and   username  = @a_user
	and   hostname  = @a_host
	and   pid       = @a_pid

	if @@rowcount > 0
	begin
		select @r_host = @a_host
		select @r_user = @a_user
		select @r_pid  = @a_pid
	end
	else
	begin
		/* update failed, check who has the lock */
		select	@r_user = username,
			@r_host = hostname,
			@r_pid  = pid
		from	qw_locks
		where	tablename = @a_table
		and	keyvalue  = @a_key

		if @@rowcount = 0
		begin
			select @r_host = null
			select @r_user = null
			select @r_pid  = null
		end
	end

	select  @ret_string = 'Return / /' + @r_user + '/' + @r_host + '/'
			+ convert(varchar,@r_pid) + '/'
if @@error != 0
begin
	return (1)
end
else
begin
	return (0)
end


go


GRANT EXECUTE ON q_drop_lock TO PUBLIC

go



CREATE PROCEDURE qw_drop_lock
(
        @a_table	varchar(30),
        @a_key		int,
        @a_user		varchar(30),
        @a_host		varchar(128),
        @a_pid		int,
        @ret_string	varchar(255) OUTPUT
) AS
declare @a_keystr	varchar(128)
	select @a_keystr = convert(varchar, @a_key)
	execute q_drop_lock @a_table, @a_keystr, @a_user,
		@a_host, @a_pid, @ret_string output
	return (@@error)


go


GRANT EXECUTE ON qw_drop_lock TO PUBLIC

go



/*
-- q_refresh_locks(User,Host,PID) ==> Ok
-- Refresh all the locks for a particular User/Host/PID 
-- DROP PROCEDURE q_refresh_locks;
*/
CREATE PROCEDURE q_refresh_locks 
(
        @a_user		varchar(30),
        @a_host		varchar(128),
        @a_pid		int,
	@ret_int	int OUTPUT
) AS 

BEGIN

	UPDATE	qw_locks
	SET	locktime  = GETDATE()
	WHERE	username  = @a_user
	  AND	hostname  = @a_host
	  AND	pid       = @a_pid

END 


if @@error != 0
begin
	select @ret_int = 0
	return (1)
end
else
begin
	select @ret_int = 1
	return (0)
end


go


GRANT EXECUTE ON q_refresh_locks TO PUBLIC

go



CREATE PROCEDURE qw_refresh_locks 
(
        @a_user		varchar(30),
        @a_host		varchar(128),
        @a_pid		int,
	@ret_string	varchar(255) OUTPUT
) AS 
declare	@ret_int	int
	execute q_refresh_locks @a_user, @a_host, @a_pid, @ret_int output
	if @ret_int = 1
	begin
		select @ret_string = 'Ok'
	end
	return (@@error)


go


GRANT EXECUTE ON qw_refresh_locks TO PUBLIC

go



/*
-- q_next_key(Table) ==> NextKeyValue

-- Implementation of the Quintus key allocation algorithm
-- using an efficient stored procedure that performs the
-- whole operation in one client EXECUTE PROCEDURE
-- SQL statement.

*/

CREATE PROCEDURE q_next_key
(
	@table		varchar(20),
	@incr		int,
	@ret_int 	int OUTPUT
) AS

BEGIN 

	BEGIN TRANSACTION

	  UPDATE  qw_keys
	  SET     qw_keys.keyvalue = qw_keys.keyvalue + @incr
	  WHERE   qw_keys.tablename = @table

	  SELECT  @ret_int = qw_keys.keyvalue - @incr + 1
	  FROM    qw_keys
	  WHERE   qw_keys.tablename = @table


/*	-- Lets hope we didn't find more than one!	*/

	COMMIT TRANSACTION
END
if @@error != 0
begin
	return (1)
end
else
begin
	return (0)
end


go


GRANT EXECUTE ON q_next_key TO PUBLIC

go



CREATE PROCEDURE qw_next_key 
( 
	@table		varchar(20),
	@incr		int,
	@ret_key 	int OUTPUT
) AS 
	execute q_next_key @table, @incr, @ret_key output
	return (@@error)


go


GRANT EXECUTE ON qw_next_key TO PUBLIC

go



/*									
-- q_clean_locks(LockLife) ==> 'Ok'					
--									
-- Clean up the lock table deleting all dead locks. It would be nice if	
-- the event server or someone called this occasionally. Note that this	
-- is just cleanliness - the locking logic does NOT require this.	
-- Redundant lock records cause no harm and will be reclaimed if	
-- someone else wants							
-- a lock on that record.						
*/									
CREATE PROCEDURE q_clean_locks						
(									
	@a_time_lock	int,						
        @ret_int    	int OUTPUT					
) AS									
	delete								
	from  qw_locks							
	where dateadd(second, @a_time_lock, locktime) < getdate()	
									
if @@error != 0
begin
	select @ret_int = 0
	return (1)
end
else
begin
	select @ret_int = 1
	return (0)
end


go


GRANT EXECUTE ON q_clean_locks TO PUBLIC

go



CREATE PROCEDURE qw_clean_locks 
(	@a_time_lock	int,
	@ret_string	varchar(255) OUTPUT
) AS
declare	@ret_int	int
	execute q_clean_locks @a_time_lock, @ret_int output
	if @ret_int = 1
	begin
		select @ret_string = 'Ok'
	end
	return (@@error)


go


GRANT EXECUTE ON qw_clean_locks TO PUBLIC

go



create procedure qw_get_key_fields (
@objid 		int,                    /* the table id */
@keyname	varchar(30),	        /* the name of the key */
@ret_string     varchar(255) OUTPUT
)
as

declare @indid int                      /* the index id of an index */
declare @keys varchar(255)              /* string to build up index key in */
declare @msg varchar(90)
declare @len1 int, @len2 int
declare @objname varchar(30)


/* Check to see the the table exists */
if not exists (select id from sysobjects where id = @objid)
/* Table doesn't exist so return.*/
begin
        select @msg = 'Table does not exist'
        print @msg
        return (1)
end

select @objname = (select name from sysobjects where id = @objid)

/* See if the object has any indexes.*/
select @indid = min(indid)
        from sysindexes
                where id = @objid
                        and indid > 0
                        and indid < 255
/* If no indexes, return. */
if @indid is NULL
begin
        select @msg = 'No indexes by this name exist'
        print @msg
        return (1)
end

	select @indid = (select indid from sysindexes where name = @keyname
			 and indid > 0 and indid < 255)

if @indid is NULL
begin
        select @msg = 'Index does not exist'
        print @msg
        return (1)
end

        /* First we'll figure out what the keys are. */
        declare @i int
        declare @thiskey varchar(30)

        select @keys = '', @i = 1

        set nocount on

        while @i <= 16
        begin
                select @thiskey = index_col(@objname, @indid, @i)

if @thiskey is NULL
                begin
                        goto keysdone
                end

                if @i > 1
                begin
                        select @keys = @keys + ','
                end

                select @keys = @keys + index_col(@objname, @indid, @i)

                /* Increment @i so it will check for the next key. */
                select @i = @i + 1

        end

        /* When we get here we now have all the keys.*/
        keysdone:
		if @keys = ''
		begin
			select @msg = 'No Index by name exists'
			return (1)
		end
		print @keys
                set nocount off

select @ret_string = @keys

return (0)


go


GRANT EXECUTE ON qw_get_key_fields TO PUBLIC

go



/* Updating database version number */


INSERT INTO qw_dbinfo(qwkey, type, value, dbcomment)
	VALUES(3,1,550,'Quintus internal database version number')

go

INSERT INTO qw_dbinfo(qwkey, type, value, dbcomment)
	VALUES(4,6,1,'Database current status')

go



/* CONFIGURE completed */

