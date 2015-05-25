-- Starting CONFIGURE


-- Adding tables

CREATE TABLE qw_keys (
	tablename VARCHAR2(240) NOT NULL,
	keyvalue NUMBER(38,0) NOT NULL,
	CONSTRAINT pk0_qw_keys_tablename PRIMARY KEY (tablename))
;


REVOKE ALL ON qw_keys FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_keys TO PUBLIC
;



CREATE TABLE qw_fieldinfo (
	table_name VARCHAR2(18) NOT NULL,
	column_name VARCHAR2(18) NOT NULL,
	column_type NUMBER(2,0) NOT NULL,
	enum_value NUMBER(2,0) NULL,
	enum_label VARCHAR2(128) NULL)
;


CREATE INDEX ik1_qw_fieldinfo_IKey ON qw_fieldinfo (table_name,column_name)
;


REVOKE ALL ON qw_fieldinfo FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_fieldinfo TO PUBLIC
;



CREATE TABLE qw_text (
	qwkey NUMBER(38,0) NOT NULL,
	tablename VARCHAR2(30) NULL,
	fieldname VARCHAR2(30) NULL,
	textkey NUMBER(38,0) NULL,
	text LONG NULL,
	CONSTRAINT pk2_qw_text_qwkey PRIMARY KEY (qwkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_text','qwkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_text',0)
;


REVOKE ALL ON qw_text FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_text TO PUBLIC
;



CREATE TABLE qw_byte (
	qwkey NUMBER(38,0) NOT NULL,
	tablename VARCHAR2(30) NULL,
	fieldname VARCHAR2(30) NULL,
	textkey NUMBER(38,0) NULL,
	text LONG RAW NULL,
	CONSTRAINT pk3_qw_byte_qwkey PRIMARY KEY (qwkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_byte','qwkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_byte',0)
;


REVOKE ALL ON qw_byte FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_byte TO PUBLIC
;



CREATE TABLE qw_blobs (
	blobid VARCHAR2(240) NOT NULL,
	blobsegnum NUMBER(38,0) NOT NULL,
	blobsegsize NUMBER(38,0) NULL,
	blobseg LONG RAW NULL,
	CONSTRAINT pk4_qw_blobs_blobid PRIMARY KEY (blobid,blobsegnum))
;


REVOKE ALL ON qw_blobs FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_blobs TO PUBLIC
;



CREATE TABLE qw_alert (
	qwkey NUMBER(38,0) NOT NULL,
	addressee VARCHAR2(240) NULL,
	sender VARCHAR2(240) NULL,
	tablename VARCHAR2(20) NULL,
	tablekey NUMBER(38,0) NULL,
	message VARCHAR2(240) NULL,
	severity NUMBER(2,0) NULL,
	alerttype VARCHAR2(20) NULL,
	dateposted DATE NULL,
	CONSTRAINT pk5_qw_alert_qwkey PRIMARY KEY (qwkey))
;


CREATE INDEX ik6_qw_alert_addressee ON qw_alert (addressee)
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_alert','qwkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_alert',0)
;


REVOKE ALL ON qw_alert FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_alert TO PUBLIC
;



CREATE TABLE qw_item (
	pkey NUMBER(38,0) NOT NULL,
	batch_key NUMBER(38,0) NOT NULL,
	reports_key NUMBER(38,0) NULL,
	itempack_key NUMBER(38,0) NULL,
	itemid VARCHAR2(40) NOT NULL,
	agentid VARCHAR2(240) NULL,
	itemtype NUMBER(2,0) NOT NULL,
	itemtext NUMBER(38,0) NULL,
	email VARCHAR2(240) NULL,
	subject VARCHAR2(240) NULL,
	faxnumber VARCHAR2(40) NULL,
	mr_ms VARCHAR2(20) NULL,
	name VARCHAR2(240) NULL,
	organization VARCHAR2(240) NULL,
	zipcode VARCHAR2(40) NULL,
	datecreated DATE NULL,
	itemsent NUMBER(38,0) DEFAULT 0 NULL CHECK (itemsent BETWEEN 0 AND 1),
	dateitemfirstsent DATE NULL,
	dateitemlastsent DATE NULL,
	sec1sent NUMBER(38,0) DEFAULT 0 NULL CHECK (sec1sent BETWEEN 0 AND 1),
	datesec1firstsent DATE NULL,
	datesec1lastsent DATE NULL,
	sec2sent NUMBER(38,0) DEFAULT 0 NULL CHECK (sec2sent BETWEEN 0 AND 1),
	datesec2firstsent DATE NULL,
	datesec2lastsent DATE NULL,
	CONSTRAINT pk7_qw_item_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,0,'Letter')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,1,'CC Letter')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,2,'BCC Letter')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,3,'Label')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,4,'Form')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtype',14,5,'Misc')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_item','itemtext',10,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_item',0)
;


REVOKE ALL ON qw_item FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_item TO PUBLIC
;



CREATE TABLE qw_itempack (
	pkey NUMBER(38,0) NOT NULL,
	batch_key NUMBER(38,0) NOT NULL,
	templatepack_key NUMBER(38,0) NULL,
	agentid VARCHAR2(40) NULL,
	recipient VARCHAR2(40) NULL,
	datecreated DATE NULL,
	CONSTRAINT pk8_qw_itempack_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_itempack','pkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_itempack',0)
;


REVOKE ALL ON qw_itempack FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_itempack TO PUBLIC
;



CREATE TABLE qw_constraints (
	qwkey NUMBER(38,0) NOT NULL,
	name CHAR(30) NULL,
	username CHAR(30) NOT NULL,
	focusname CHAR(30) NOT NULL,
	control CHAR(240) NULL,
	fcontrol CHAR(240) NULL,
	doption CHAR(240) NULL,
	cgroup CHAR(240) NULL,
	CONSTRAINT pk9_qw_constraints_qwkey PRIMARY KEY (qwkey))
;


CREATE INDEX ik10_qw_constraints_IKey ON qw_constraints (focusname,username)
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraints','qwkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_constraints',0)
;


REVOKE ALL ON qw_constraints FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_constraints TO PUBLIC
;



CREATE TABLE qw_adl (
	pkey NUMBER(38,0) NOT NULL,
	adl LONG NULL,
	adl_version NUMBER(38,0) NULL,
	adl_timestamp DATE NULL,
	name VARCHAR2(20) NULL,
	CONSTRAINT pk11_qw_adl_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk12_qw_adl_UKey UNIQUE (adl_version,name))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_adl','pkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_adl',0)
;


REVOKE ALL ON qw_adl FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_adl TO PUBLIC
;



CREATE TABLE qw_response (
	pkey NUMBER(38,0) NOT NULL,
	question VARCHAR2(80) NULL,
	answer VARCHAR2(240) NULL,
	score NUMBER(38,0) NULL CHECK (score BETWEEN 0 AND 100),
	dateentered DATE NOT NULL,
	CONSTRAINT pk13_qw_response_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_response','pkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_response',0)
;


REVOKE ALL ON qw_response FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_response TO PUBLIC
;



CREATE TABLE qw_flow_page_xml (
	pkey NUMBER(38,0) NOT NULL,
	flow_key NUMBER(38,0) NULL,
	name VARCHAR2(80) NULL,
	version NUMBER(38,0) DEFAULT 1 NULL,
	text NUMBER(38,0) NULL,
	CONSTRAINT pk14_qw_flow_page_xml_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow_page_xml','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow_page_xml','text',10,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_flow_page_xml',0)
;


REVOKE ALL ON qw_flow_page_xml FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_flow_page_xml TO PUBLIC
;



CREATE TABLE qw_locks (
	tablename VARCHAR2(30) NOT NULL,
	keyvalue VARCHAR2(128) NOT NULL,
	username VARCHAR2(30) NULL,
	hostname VARCHAR2(128) NULL,
	pid NUMBER(38,0) NULL,
	locktime DATE NULL,
	CONSTRAINT pk15_qw_locks_PKey PRIMARY KEY (tablename,keyvalue))
;


REVOKE ALL ON qw_locks FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_locks TO PUBLIC
;



CREATE TABLE qw_forms (
	pkey NUMBER(38,0) NOT NULL,
	qw_appsforms NUMBER(38,0) NOT NULL,
	name VARCHAR2(20) NULL,
	text NUMBER(38,0) NULL,
	text_timestamp DATE NULL,
	CONSTRAINT pk16_qw_forms_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk17_qw_forms_UKey UNIQUE (qw_appsforms,name))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_forms','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_forms','text',10,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_forms',0)
;


REVOKE ALL ON qw_forms FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_forms TO PUBLIC
;



CREATE TABLE qw_reporttmplpk0 (
	report NUMBER(38,0) NOT NULL,
	templatepack NUMBER(38,0) NOT NULL,
	CONSTRAINT pk18_qw_reporttmplpk0_PKey PRIMARY KEY (report,templatepack))
;


REVOKE ALL ON qw_reporttmplpk0 FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_reporttmplpk0 TO PUBLIC
;



CREATE TABLE qw_schedule (
	qwkey NUMBER(38,0) NOT NULL,
	name VARCHAR2(80) NULL,
	description VARCHAR2(80) NULL,
	scheduleyear VARCHAR2(4) NULL,
	sunstart VARCHAR2(12) NULL,
	sunend VARCHAR2(12) NULL,
	monstart VARCHAR2(12) NULL,
	monend VARCHAR2(12) NULL,
	tuestart VARCHAR2(12) NULL,
	tueend VARCHAR2(12) NULL,
	wedstart VARCHAR2(12) NULL,
	wedend VARCHAR2(12) NULL,
	thustart VARCHAR2(12) NULL,
	thuend VARCHAR2(12) NULL,
	fristart VARCHAR2(12) NULL,
	friend VARCHAR2(12) NULL,
	satstart VARCHAR2(12) NULL,
	satend VARCHAR2(12) NULL,
	janholiday NUMBER(38,0) NULL,
	febholiday NUMBER(38,0) NULL,
	marholiday NUMBER(38,0) NULL,
	aprholiday NUMBER(38,0) NULL,
	mayholiday NUMBER(38,0) NULL,
	junholiday NUMBER(38,0) NULL,
	julholiday NUMBER(38,0) NULL,
	augholiday NUMBER(38,0) NULL,
	sepholiday NUMBER(38,0) NULL,
	octholiday NUMBER(38,0) NULL,
	novholiday NUMBER(38,0) NULL,
	decholiday NUMBER(38,0) NULL,
	CONSTRAINT pk19_qw_schedule_qwkey PRIMARY KEY (qwkey))
;


CREATE INDEX ik20_qw_schedule_IKey ON qw_schedule (name,scheduleyear)
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_schedule','qwkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_schedule',0)
;


REVOKE ALL ON qw_schedule FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_schedule TO PUBLIC
;



CREATE TABLE qw_constraint (
	qwkey NUMBER(38,0) NOT NULL,
	name CHAR(30) NOT NULL,
	username CHAR(30) NOT NULL,
	focusname CHAR(30) NOT NULL,
	formname VARCHAR2(30) NULL,
	groupname VARCHAR2(30) NULL,
	tablename VARCHAR2(30) NULL,
	value VARCHAR2(240) NULL,
	keyvalue VARCHAR2(30) NULL,
	controlname VARCHAR2(30) NULL,
	type NUMBER(2,0) NULL,
	fieldname VARCHAR2(30) NULL,
	targetname VARCHAR2(30) NULL,
	CONSTRAINT pk21_qw_constraint_qwkey PRIMARY KEY (qwkey))
;


CREATE INDEX ik22_qw_constraint_IKey ON qw_constraint (focusname,username)
;


CREATE INDEX ik23_qw_constraint_name ON qw_constraint (name)
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','qwkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,0,'CONTROL')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,1,'FOREIGN CONTROL')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,2,'GROUP')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,3,'DISJUNCTION')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_constraint','type',14,4,'IFB')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_constraint',0)
;


REVOKE ALL ON qw_constraint FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_constraint TO PUBLIC
;



CREATE TABLE qw_batch (
	pkey NUMBER(38,0) NOT NULL,
	name VARCHAR2(40) NOT NULL,
	batchstatus NUMBER(2,0) NOT NULL,
	batchtype NUMBER(2,0) NOT NULL,
	datecreated DATE NULL,
	datemodified DATE NULL,
	datefrozen DATE NULL,
	datelastprocessed DATE NULL,
	dateprocessed DATE NULL,
	dateclosed DATE NULL,
	barcodes NUMBER(38,0) NULL CHECK (barcodes BETWEEN 0 AND 1),
	labelformat VARCHAR2(20) NULL,
	sortorder NUMBER(2,0) NOT NULL,
	sec1dest_key NUMBER(38,0) NULL,
	sec1copies NUMBER(38,0) DEFAULT 1 NULL,
	sec2dest_key NUMBER(38,0) NULL,
	sec2copies NUMBER(38,0) DEFAULT 1 NULL,
	ccdest_key NUMBER(38,0) NULL,
	cccopies NUMBER(38,0) DEFAULT 1 NULL,
	bccdest_key NUMBER(38,0) NULL,
	bcccopies NUMBER(38,0) DEFAULT 1 NULL,
	labeldest_key NUMBER(38,0) NULL,
	labelcopies NUMBER(38,0) DEFAULT 1 NULL,
	miscdest_key NUMBER(38,0) NULL,
	misccopies NUMBER(38,0) DEFAULT 1 NULL,
	CONSTRAINT pk24_qw_batch_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchstatus',14,0,'Open')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchstatus',14,1,'Frozen')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchstatus',14,2,'Processed')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchstatus',14,3,'Closed')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchtype',14,0,'Perpetual')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','batchtype',14,1,'Single-Use')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','sortorder',14,0,'None')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','sortorder',14,1,'Agent')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_batch','sortorder',14,2,'Zipcode')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_batch',0)
;


REVOKE ALL ON qw_batch FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_batch TO PUBLIC
;



CREATE TABLE qw_doc (
	qwkey NUMBER(38,0) NOT NULL,
	eventkey NUMBER(38,0) NULL,
	ref VARCHAR2(240) NULL,
	filename VARCHAR2(240) NULL,
	type NUMBER(38,0) NULL,
	sequence NUMBER(38,0) NULL,
	doc NUMBER(38,0) NULL,
	CONSTRAINT pk25_qw_doc_qwkey PRIMARY KEY (qwkey))
;


CREATE INDEX ik26_qw_doc_eventkey ON qw_doc (eventkey)
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_doc','qwkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_doc','doc',11,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_doc',0)
;


REVOKE ALL ON qw_doc FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_doc TO PUBLIC
;



CREATE TABLE qw_scripts (
	qwkey NUMBER(38,0) NOT NULL,
	name VARCHAR2(40) NULL,
	app VARCHAR2(20) NULL,
	version VARCHAR2(20) NULL,
	type NUMBER(38,0) NULL,
	template VARCHAR2(240) NULL,
	text NUMBER(38,0) NULL,
	compiled NUMBER(38,0) NULL,
	time DATE NULL,
	compiletime DATE NULL,
	binary NUMBER(38,0) NULL,
	CONSTRAINT pk27_qw_scripts_qwkey PRIMARY KEY (qwkey),
	CONSTRAINT uk28_qw_scripts_UKey UNIQUE (version,name))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_scripts','qwkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_scripts','text',10,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_scripts','binary',11,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_scripts',0)
;


REVOKE ALL ON qw_scripts FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_scripts TO PUBLIC
;



CREATE TABLE qw_reports (
	pkey NUMBER(38,0) NOT NULL,
	reportname VARCHAR2(40) NULL,
	description VARCHAR2(240) NULL,
	focus VARCHAR2(40) NULL,
	ispublic NUMBER(38,0) NULL,
	anchortable VARCHAR2(18) NULL,
	relationset VARCHAR2(40) NULL,
	reportowner VARCHAR2(20) NULL,
	datemodified DATE NULL,
	reportspec NUMBER(38,0) NULL,
	reporttype NUMBER(38,0) DEFAULT 0 NULL,
	outputformat NUMBER(38,0) DEFAULT 0 NULL,
	hasbackground NUMBER(38,0) NULL CHECK (hasbackground BETWEEN 0 AND 1),
	background NUMBER(38,0) NULL,
	CONSTRAINT pk29_qw_reports_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_reports','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_reports','reportspec',10,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_reports','background',11,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_reports',0)
;


REVOKE ALL ON qw_reports FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_reports TO PUBLIC
;



CREATE TABLE qw_templatepack (
	pkey NUMBER(38,0) NOT NULL,
	packname VARCHAR2(40) NOT NULL,
	description VARCHAR2(240) NULL,
	focus VARCHAR2(40) NULL,
	ispublic NUMBER(38,0) NULL,
	anchortable VARCHAR2(18) NULL,
	relationset VARCHAR2(40) NULL,
	packowner VARCHAR2(20) NULL,
	destination VARCHAR2(40) NULL,
	addressee VARCHAR2(18) NULL,
	ccaliaslist VARCHAR2(80) NULL,
	bccaliaslist VARCHAR2(80) NULL,
	datemodified DATE NULL,
	CONSTRAINT pk30_qw_templatepack_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_templatepack','pkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_templatepack',0)
;


REVOKE ALL ON qw_templatepack FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_templatepack TO PUBLIC
;



CREATE TABLE qw_flow (
	pkey NUMBER(38,0) NOT NULL,
	flowset_key NUMBER(38,0) NULL,
	name VARCHAR2(80) NOT NULL,
	version NUMBER(38,0) DEFAULT 1 NULL,
	type NUMBER(38,0) DEFAULT 0 NULL,
	text NUMBER(38,0) NULL,
	CONSTRAINT pk31_qw_flow_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow','text',10,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_flow',0)
;


REVOKE ALL ON qw_flow FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_flow TO PUBLIC
;



CREATE TABLE qw_flow_page_dhtml (
	pkey NUMBER(38,0) NOT NULL,
	flow_key NUMBER(38,0) NULL,
	name VARCHAR2(80) NULL,
	version NUMBER(38,0) DEFAULT 1 NULL,
	text NUMBER(38,0) NULL,
	CONSTRAINT pk32_qw_flow_page_dhtml_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow_page_dhtml','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flow_page_dhtml','text',10,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_flow_page_dhtml',0)
;


REVOKE ALL ON qw_flow_page_dhtml FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_flow_page_dhtml TO PUBLIC
;



CREATE TABLE qw_apps (
	pkey NUMBER(38,0) NOT NULL,
	qw_adlapps NUMBER(38,0) NOT NULL,
	name VARCHAR2(20) NULL,
	title VARCHAR2(20) NULL,
	help16 NUMBER(38,0) NULL,
	help16_timestamp DATE NULL,
	help32 NUMBER(38,0) NULL,
	help32_timestamp DATE NULL,
	help32_cnt NUMBER(38,0) NULL,
	CONSTRAINT pk33_qw_apps_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk34_qw_apps_UKey UNIQUE (qw_adlapps,name))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_apps','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_apps','help16',11,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_apps','help32',11,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_apps','help32_cnt',11,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_apps',0)
;


REVOKE ALL ON qw_apps FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_apps TO PUBLIC
;



CREATE TABLE qw_rptsched (
	pkey NUMBER(38,0) NOT NULL,
	scheduledrptkey NUMBER(38,0) NOT NULL,
	rptrepeat NUMBER(38,0) DEFAULT 0 NULL,
	rptwhen DATE NULL,
	rptemail NUMBER(38,0) DEFAULT 0 NULL,
	rptemailto VARCHAR2(240) NULL,
	rptemailcc VARCHAR2(240) NULL,
	rptemailsubj VARCHAR2(160) NULL,
	rptfax NUMBER(38,0) DEFAULT 0 NULL,
	rptfaxto VARCHAR2(240) NULL,
	rptfaxno VARCHAR2(20) NULL,
	rptfaxsubj VARCHAR2(160) NULL,
	rptprint NUMBER(38,0) DEFAULT 0 NULL,
	rptcommand VARCHAR2(40) NULL,
	rptswitches VARCHAR2(40) NULL,
	rpttitle VARCHAR2(160) NULL,
	rptfile NUMBER(38,0) DEFAULT 0 NULL,
	rptfilename VARCHAR2(160) NULL,
	rptappend NUMBER(38,0) DEFAULT 0 NULL,
	CONSTRAINT pk35_qw_rptsched_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_rptsched','pkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_rptsched',0)
;


REVOKE ALL ON qw_rptsched FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_rptsched TO PUBLIC
;



CREATE TABLE qw_flowset (
	pkey NUMBER(38,0) NOT NULL,
	name VARCHAR2(80) NOT NULL,
	version NUMBER(38,0) DEFAULT 1 NULL,
	CONSTRAINT pk36_qw_flowset_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_flowset','pkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_flowset',0)
;


REVOKE ALL ON qw_flowset FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_flowset TO PUBLIC
;



CREATE TABLE qw_consolelayout (
	pkey NUMBER(38,0) NOT NULL,
	text NUMBER(38,0) NULL,
	description VARCHAR2(240) NULL,
	name VARCHAR2(40) NULL,
	datemodified DATE NULL,
	qw_apps_key NUMBER(38,0) NULL,
	CONSTRAINT pk37_qw_consolelayout_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk38_qw_consolelayout_UKey UNIQUE (qw_apps_key,name))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_consolelayout','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_consolelayout','text',10,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_consolelayout',0)
;


REVOKE ALL ON qw_consolelayout FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_consolelayout TO PUBLIC
;



CREATE TABLE qw_events (
	qwkey NUMBER(38,0) NOT NULL,
	qw_when DATE NULL,
	action NUMBER(2,0) DEFAULT 0 NULL,
	workgroupkey NUMBER(38,0) NULL,
	ownerkey NUMBER(38,0) NULL,
	escalationkey NUMBER(38,0) NULL,
	state NUMBER(2,0) DEFAULT 0 NULL,
	tablename VARCHAR2(20) NULL,
	tablekey NUMBER(38,0) NULL,
	severity NUMBER(2,0) NULL,
	message VARCHAR2(240) NULL,
	addressee VARCHAR2(240) NULL,
	sender VARCHAR2(240) NULL,
	address2 VARCHAR2(240) NULL,
	escstatus NUMBER(38,0) NULL,
	tier NUMBER(3,0) NULL,
	repeatinterval NUMBER(6,0) NULL,
	agent NUMBER(2,0) DEFAULT 0 NULL,
	datemodified DATE NULL,
	rptschedkey NUMBER(38,0) NULL,
	workgroupkeyname VARCHAR2(40) NULL,
	ownerkeyname VARCHAR2(40) NULL,
	CONSTRAINT pk39_qw_events_qwkey PRIMARY KEY (qwkey))
;


CREATE INDEX ik40_qw_events_IKey1 ON qw_events (action,state)
;


CREATE INDEX ik41_qw_events_IKey2 ON qw_events (tablename,tablekey)
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_events','qwkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_events',0)
;


REVOKE ALL ON qw_events FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_events TO PUBLIC
;



CREATE TABLE qw_routinginfo (
	pkey NUMBER(38,0) NOT NULL,
	batch_key NUMBER(38,0) NOT NULL,
	reports_key NUMBER(38,0) NOT NULL,
	destination_key NUMBER(38,0) NULL,
	copies NUMBER(38,0) DEFAULT 1 NULL,
	CONSTRAINT pk42_qw_routinginfo_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_routinginfo','pkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_routinginfo',0)
;


REVOKE ALL ON qw_routinginfo FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_routinginfo TO PUBLIC
;



CREATE TABLE qw_destination (
	pkey NUMBER(38,0) NOT NULL,
	name VARCHAR2(40) NOT NULL,
	method NUMBER(2,0) NOT NULL,
	datecreated DATE NULL,
	datemodified DATE NULL,
	printername VARCHAR2(120) NULL,
	printersource VARCHAR2(24) DEFAULT 'Automatically Select' NULL,
	printersourcebin NUMBER(38,0) DEFAULT 0 NULL,
	topmargin VARCHAR2(5) NULL,
	leftmargin VARCHAR2(5) NULL,
	bottommargin VARCHAR2(5) NULL,
	rightmargin VARCHAR2(5) NULL,
	marginunits NUMBER(2,0) NULL,
	filename VARCHAR2(255) NULL,
	CONSTRAINT pk43_qw_destination_pkey PRIMARY KEY (pkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','method',14,0,'Print')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','method',14,1,'Email')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','method',14,2,'Fax')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','method',14,3,'File')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','marginunits',14,0,'Inches')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_destination','marginunits',14,1,'Cms')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_destination',0)
;


REVOKE ALL ON qw_destination FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_destination TO PUBLIC
;



CREATE TABLE qw_dbinfo (
	qwkey NUMBER(38,0) NOT NULL,
	type NUMBER(38,0) NULL,
	value NUMBER(38,0) NULL,
	dbcomment VARCHAR2(240) NULL,
	CONSTRAINT pk44_qw_dbinfo_qwkey PRIMARY KEY (qwkey))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_dbinfo','qwkey',6,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_dbinfo',0)
;


REVOKE ALL ON qw_dbinfo FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_dbinfo TO PUBLIC
;



CREATE TABLE qw_eduviewerlayout (
	pkey NUMBER(38,0) NOT NULL,
	text NUMBER(38,0) NULL,
	description VARCHAR2(240) NULL,
	name VARCHAR2(40) NULL,
	datemodified DATE NULL,
	qw_apps_key NUMBER(38,0) NULL,
	CONSTRAINT pk45_qw_eduviewerlayout_pkey PRIMARY KEY (pkey),
	CONSTRAINT uk46_qw_eduviewerlayout_UKey UNIQUE (qw_apps_key,name))
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_eduviewerlayout','pkey',6,0,'')
;


INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label)
	VALUES ('qw_eduviewerlayout','text',10,0,'')
;


INSERT INTO qw_keys (tablename,keyvalue)
	VALUES ('qw_eduviewerlayout',0)
;


REVOKE ALL ON qw_eduviewerlayout FROM PUBLIC
;


GRANT SELECT,UPDATE,INSERT,DELETE ON qw_eduviewerlayout TO PUBLIC
;



-- Creating referential constraints








































-- Creating stored procedures




-- q_create_lock(Table,Key,User,Host,PID,TimeLock)			
--	==> CurrUser,CurrHost,CurrPID					
-- Create an application-level lock on a record in a table for a	
-- particular user, running on a host and identified by a process id.	
-- Return a string containing the user, host, pid on success or		
-- return the current user, host, pid holding the lock if we fail.	
-- Note that if the current user has the lock already the lock update	
-- will fail but the return string will contain the right user/host/pid	
-- so that the caller will treat it as success.				
-- This code has to ensure that only one user gets a lock even when	
-- multiple users are running this procedure concurrently. There are	
-- probably several ways to do this. The method used here is:		
-- 	Maintain a UNIQUE index on qw_locks for tablename,keyvalue and	
--	use this to ensure only one lock record can be created. Use an	
--	UPDATE cursor to find and reclaim an existing lock record so	
--	that this can only be done by one user. Look for an existing	
--	record only if the INSERT raises an exception.			
CREATE OR REPLACE PROCEDURE q_create_lock				
(									
        a_table            VARCHAR2,					
        a_key              VARCHAR2,					
        a_user             VARCHAR2,					
        a_host             VARCHAR2,					
        a_pid              INTEGER,					
        a_time_lock        INTEGER,					
	a_try_insert       INTEGER,					
	a_lock	       OUT VARCHAR2					
) AS									
	r_user		VARCHAR2(30);					
	r_host		VARCHAR2(128);					
	r_pid		INTEGER;					
	status		INTEGER;					
	v_i		INTEGER;					
begin									
    v_i    := 0;							
    << outer >>								
    FOR v_i in 1..a_try_insert LOOP					
	begin								
		status := 0;						
		-- try and slam the record in				
		INSERT							
		INTO qw_locks						
		VALUES (a_table,a_key,a_user,a_host,a_pid,sysdate);	
		exception when DUP_VAL_ON_INDEX then status := 1;	
	end;								
	if (status = 0) then						
		COMMIT;							
		r_user := a_user;					
		r_host := a_host;					
		r_pid  := a_pid;					
	else								
		-- a lock exists, try and grab it			
		begin							
			UPDATE qw_locks					
			SET	username  = a_user,			
				hostname  = a_host,			
				pid       = a_pid,			
				locktime  = sysdate			
			WHERE	tablename = a_table			
			AND	keyvalue  = a_key			
			AND	(locktime + (a_time_lock/86400) < sysdate);
			COMMIT;						
			if (SQL%NOTFOUND) then				
				-- someone else has the lock		
				SELECT	username, hostname, pid		
				INTO	r_user,   r_host,   r_pid 	
				FROM	qw_locks			
				WHERE	tablename = a_table		
				AND	keyvalue  = a_key;		
									
				if (SQL%NOTFOUND) then			
					-- unexpected ...		
					r_user := ' ';			
					r_host := ' ';			
					r_pid  := 0;			
					status := 2;			
				end if;					
			else						
				-- no failure no exception, so stale	
				r_user := a_user;			
				r_host := a_host;			
				r_pid := a_pid;				
			end if;						
			exception when NO_DATA_FOUND then		
				-- unexpected ...			
				r_user := ' ';				
				r_host := ' ';				
				r_pid  := 0;				
				status := 2;				
		end;							
	end if;								
	-- we continue looping only if slam and select failed (probably	
	-- the record was there when we started, but was removed before	
	-- select							
	if (status != 2) then	-- ie slam failed, select failed	
		EXIT outer;	-- try again until limit		
	end if;								
    END LOOP outer;							
    a_lock := 'Return /'|| ' '|| '/' || r_user || '/' ||		
			r_host || '/' || r_pid || '/';			
END q_create_lock;

/


GRANT EXECUTE ON q_create_lock TO PUBLIC
;



CREATE OR REPLACE FUNCTION qw_create_lock				
(									
        a_table            VARCHAR2,					
        a_key              INTEGER,					
        a_user             VARCHAR2,					
        a_host             VARCHAR2,					
        a_pid              INTEGER,					
        a_time_lock        INTEGER,					
	a_try_insert       INTEGER					
) RETURN VARCHAR2 IS							
	ret_string	   VARCHAR2(255);				
	dot_pos		   INTEGER;					
	len		   INTEGER;					
	a_table2	   VARCHAR2(255);				
BEGIN									
	len := LENGTH(a_table);						
	dot_pos := INSTR(a_table, '.');					
	a_table2 := SUBSTR(a_table, dot_pos+1, len-dot_pos);		
	q_create_lock(a_table2, a_key, a_user, a_host,			
		      a_pid, a_time_lock, a_try_insert, ret_string);	
	RETURN(ret_string);						
END qw_create_lock;

/


GRANT EXECUTE ON qw_create_lock TO PUBLIC
;



-- q_drop_lock								
--									
-- Drop	a particular application-level lock. This takes the same	
-- arguments as q_create_lock.						
--									
-- We return the holder of the lock to show success/failure		
-- Thus, there are two possible failures:				
-- (i) someone else had the lock, and				 	
-- (ii) there was no lock record.					
									
									
CREATE OR REPLACE PROCEDURE q_drop_lock 				
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
									
	-- lets just try to delete the lock				
	DELETE FROM qw_locks						
	WHERE tablename	= a_table					
	AND   keyvalue	= a_key						
	AND   username	= a_user					
	AND   hostname	= a_host					
	AND   pid	= a_pid;					
									
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


GRANT EXECUTE ON q_drop_lock TO PUBLIC
;



CREATE OR REPLACE FUNCTION qw_drop_lock				
(									
        a_table            VARCHAR2,					
        a_key              INTEGER,					
        a_user             VARCHAR2,					
        a_host             VARCHAR2,					
        a_pid              INTEGER					
) RETURN VARCHAR2 IS							
	ret_string	   VARCHAR2(255);				
	dot_pos		   INTEGER;					
	len		   INTEGER;					
	a_table2	   VARCHAR2(255);				
BEGIN									
	len := LENGTH(a_table);						
	dot_pos := INSTR(a_table, '.');					
	a_table2 := SUBSTR(a_table, dot_pos+1, len-dot_pos);		
	q_drop_lock(a_table2, a_key, a_user, a_host, a_pid, ret_string);
	RETURN(ret_string);						
END qw_drop_lock;

/


GRANT EXECUTE ON qw_drop_lock TO PUBLIC
;



-- q_refresh_locks(User,Host,PID) 					
-- Refresh all the locks for a particular User/Host/PID 		
									
CREATE OR REPLACE PROCEDURE q_refresh_locks 				
(									
        a_user       VARCHAR2,						
        a_host       VARCHAR2,						
        a_pid        INTEGER,						
	a_status OUT INTEGER						
) AS									
BEGIN									
	a_status := 0;							
	UPDATE	qw_locks						
	SET	locktime  = sysdate					
	WHERE	username  = a_user					
	  AND	hostname  = a_host					
	  AND	pid       = a_pid;					
									
	COMMIT;								
	a_status := 1;							
END q_refresh_locks;

/


GRANT EXECUTE ON q_refresh_locks TO PUBLIC
;



CREATE OR REPLACE FUNCTION qw_refresh_locks 				
(									
        a_user       VARCHAR2,						
        a_host       VARCHAR2,						
        a_pid        INTEGER						
) RETURN VARCHAR2 IS							
	a_status     INTEGER;						
BEGIN									
	q_refresh_locks(a_user, a_host, a_pid, a_status);		
        RETURN('Ok');							
END qw_refresh_locks;

/


GRANT EXECUTE ON qw_refresh_locks TO PUBLIC
;



-- q_next_key(Table) ==> NextKeyValue 
CREATE OR REPLACE PROCEDURE q_next_key 
(	a_input_table		VARCHAR2, 
	a_increment		INTEGER, 
	a_returnkey	    OUT INTEGER 
) AS 
  -- Implementation of the Quintus key allocation algorithm 
  -- using an efficient stored procedure that performs the 
  -- whole operation in one client EXECUTE PROCEDURE 
  -- SQL statement, and which uses an update cursor internally 
  -- for maximum concurrency 
 
 
    tablekey INTEGER; 
    returnkey  INTEGER; 
    increment INTEGER; 
    docommit INTEGER; 
 
    CURSOR cursor1 IS 
    SELECT  keyvalue 
    FROM    qw_keys 
    WHERE   tablename = a_input_table 
    FOR     UPDATE; 
 
BEGIN 
	-- if called from a trigger docommit should be false(0) 
	-- if called from the dco library docommit should be true(1) 
	-- docommit means a commit will occur in the loop 
	-- increment is used because you cannot change the value 
	-- of argument to a procedure. 
 
	IF ( a_increment = -1 ) THEN 
		increment := 1; 
		docommit := 0; 
	ELSE 
		increment := a_increment; 
		docommit := 1; 
	END IF; 
 
	OPEN cursor1; 
	LOOP 
		a_returnkey := -1; 
		FETCH cursor1 INTO  tablekey; 
		EXIT WHEN cursor1%NOTFOUND; 
		-- There should only be one 
		a_returnkey := tablekey + 1; 
		UPDATE qw_keys SET keyvalue = tablekey + increment 
			WHERE CURRENT OF cursor1; 
		IF ( docommit = 1) THEN 
			COMMIT; 
		END IF; 
		EXIT; 
	END LOOP; 
	CLOSE cursor1; 
 
END q_next_key;

/


GRANT EXECUTE ON q_next_key TO PUBLIC
;



CREATE OR REPLACE FUNCTION qw_next_key					
(	a_input_table		VARCHAR2,				
	a_increment		INTEGER					
) RETURN INTEGER IS							
	returnkey	INTEGER;					
BEGIN									
	q_next_key(a_input_table, a_increment, returnkey); 		
	RETURN(returnkey);						
END qw_next_key;

/


GRANT EXECUTE ON qw_next_key TO PUBLIC
;



-- q_clean_locks(LockLife)						
--									
-- Clean up the lock table deleting all dead locks. It would be nice if	
-- the event server or someone called this occasionally. Note that this	
-- is just cleanliness - the locking logic does NOT require this.	
-- Redundant lock records cause no harm and will be reclaimed if	
-- someone else wants a lock on that record.				
									
CREATE OR REPLACE PROCEDURE q_clean_locks				
(									
	a_time_lock	INTEGER,					
	a_status    OUT INTEGER						
) AS									
BEGIN									
	a_status := 0;							
	DELETE								
	FROM	qw_locks						
	WHERE	locktime + (a_time_lock/86400) < sysdate;		
									
	COMMIT;								
	a_status := 1;							
END q_clean_locks;

/


GRANT EXECUTE ON q_clean_locks TO PUBLIC
;



CREATE OR REPLACE FUNCTION qw_clean_locks (a_time_lock INTEGER)	
RETURN VARCHAR2 IS							
	a_status INTEGER;						
BEGIN									
	q_clean_locks(a_time_lock, a_status);				
        RETURN('Ok');							
END qw_clean_locks;

/


GRANT EXECUTE ON qw_clean_locks TO PUBLIC
;



-- Updating database version number


INSERT INTO qw_dbinfo(qwkey, type, value, dbcomment)
	VALUES(3,1,550,'Quintus internal database version number')
;

INSERT INTO qw_dbinfo(qwkey, type, value, dbcomment)
	VALUES(4,6,1,'Database current status')
;



-- CONFIGURE completed

