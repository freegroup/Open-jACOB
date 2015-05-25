

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('employeesite',1);


ALTER TABLE object MODIFY(ext_system_key NOT NULL);

ALTER TABLE ext_system MODIFY(objectrequired NOT NULL);

ALTER TABLE ext_system MODIFY(allowcancel NOT NULL);

ALTER TABLE ext_system MODIFY(initialstatus NOT NULL);

ALTER TABLE ext_system MODIFY(fetchhwgdata NOT NULL);

ALTER TABLE ext_system MODIFY(endstatus NOT NULL);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('hwgtasktype',0);

ALTER TABLE yan_task MODIFY(xmltext NOT NULL);

ALTER TABLE organization MODIFY(orgstatus NOT NULL);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('tasktype',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('applicationper',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('addfieldsdetail',0);

ALTER TABLE callaction MODIFY(migration NOT NULL);


ALTER TABLE task MODIFY(timedocumentation DEFAULT NULL);

ALTER TABLE task MODIFY(totaltimespent DEFAULT NULL);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('contractsl',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('freepage',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('education_employee',0);

DROP INDEX IK0_CALLEVENT_CALLCALLEVENT;

ALTER TABLE gdserrorcode MODIFY(unitdescription NOT NULL);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('contractcoverage0',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('accountingcode',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('obj_ident',0);

ALTER TABLE workgroup MODIFY(migration NOT NULL);

ALTER TABLE workgroup MODIFY(wrkgrptype NOT NULL);

ALTER TABLE workgroup MODIFY(notifyowngroup NOT NULL);

ALTER TABLE workgroup MODIFY(groupconferencecal NUMBER(2) NOT NULL);

ALTER TABLE workgroup MODIFY(groupstatus NOT NULL);

ALTER TABLE workgroup MODIFY(autodocumented NOT NULL);

ALTER TABLE appprofile MODIFY(warrentyend NOT NULL);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('fwt_sequencer',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('patchaddresses',0);

ALTER TABLE xml_template MODIFY(xmltext NOT NULL);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('categoryprocess',0);

DELETE FROM qw_fieldinfo WHERE table_name='calltemplate';

CREATE TABLE calltemplate (
  pkey NUMBER(38) NOT NULL,
  action VARCHAR(240) NULL,
  name VARCHAR(80) NOT NULL,
  nextcreatedate DATE NULL,
  autoclosed NUMBER(2) DEFAULT 1 NULL,
  callbackmethod NUMBER(2) DEFAULT 0 NULL,
  priority NUMBER(2) DEFAULT 0 NULL,
  problem VARCHAR(240) NOT NULL,
  problemtext NUMBER(38) NULL,
  customerint_key NUMBER(38) NULL,
  callworkgroup_key NUMBER(38) NOT NULL,
  accountcode_key VARCHAR(8) NULL,
  object_key NUMBER(38) NULL,
  location_key NUMBER(38) NOT NULL,
  process_key NUMBER(38) NOT NULL,
  category_key NUMBER(38) NOT NULL,
  repeatintervalunit NUMBER(2) DEFAULT 0 NOT NULL,
  repeatinterval NUMBER(38) DEFAULT 1 NOT NULL,
  error NUMBER(38) NULL,
  CONSTRAINT PK1_CALLTEMPLATE_PRIMARYKEY PRIMARY KEY (pkey));

DELETE FROM qw_fieldinfo WHERE table_name='calltemplate' AND column_name='autoclosed';

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','autoclosed',14,0,'Nein');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','autoclosed',14,1,'Ja');

DELETE FROM qw_fieldinfo WHERE table_name='calltemplate' AND column_name='callbackmethod';

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','callbackmethod',14,0,'Keine');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','callbackmethod',14,1,'SMS');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','callbackmethod',14,2,'Telefon');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','callbackmethod',14,3,'Email');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','callbackmethod',14,4,'FAX');

DELETE FROM qw_fieldinfo WHERE table_name='calltemplate' AND column_name='priority';

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','priority',14,0,'1-Normal');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','priority',14,1,'2-Kritisch');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','priority',14,2,'3-Produktion');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','priority',14,3,'4-Notfall');

DELETE FROM qw_fieldinfo WHERE table_name='calltemplate' AND column_name='repeatintervalunit';

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','repeatintervalunit',14,0,'täglich');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','repeatintervalunit',14,1,'wöchentlich');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','repeatintervalunit',14,2,'monatlich');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','repeatintervalunit',14,3,'jährlich');

INSERT INTO qw_fieldinfo (table_name,column_name,column_type,enum_value,enum_label) VALUES ('calltemplate','repeatintervalunit',14,4,'keine');

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('calltemplate',0);

REVOKE ALL ON calltemplate FROM PUBLIC;

GRANT SELECT, UPDATE, INSERT, DELETE ON calltemplate TO PUBLIC;

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('wagedetail',0);


INSERT INTO qw_keys (tablename,keyvalue) VALUES ('workgrouphwg',0);

ALTER TABLE groupmember MODIFY(accessallowed NOT NULL);

ALTER TABLE category MODIFY(locationrequired NOT NULL);


DROP INDEX IDX_OBJECTCALL;


ALTER TABLE calls MODIFY(sumcoordtime DEFAULT NULL);

ALTER TABLE calls MODIFY(personal_vote NOT NULL);

ALTER TABLE calls MODIFY(sumtaskdoc DEFAULT NULL);

ALTER TABLE calls MODIFY(totaltaskdoc DEFAULT NULL);

ALTER TABLE calls MODIFY(totalcalltime DEFAULT NULL);

ALTER TABLE calls MODIFY(sd_time DEFAULT NULL);

ALTER TABLE calls MODIFY(callbackmethod DEFAULT NULL NOT NULL);

ALTER TABLE calls MODIFY(sumtasktimespent DEFAULT NULL);

ALTER TABLE calls MODIFY(totaltasktimespent DEFAULT NULL);

CREATE INDEX IK6_CALLS_EXTERNALORDERID_IDX ON calls (externalorderid);

ALTER TABLE employee ADD(prj_role NUMBER(38) DEFAULT 0 NULL,
  pqp_role NUMBER(38) DEFAULT 0 NULL);

ALTER TABLE employee MODIFY(sdadmin_role NULL);

ALTER TABLE employee MODIFY(warte_role NULL);

ALTER TABLE employee MODIFY(supportrole NULL);

ALTER TABLE employee MODIFY(admin_role NULL);

ALTER TABLE employee MODIFY(availability NULL);

ALTER TABLE employee MODIFY(pm_role NULL);

ALTER TABLE employee MODIFY(ak_role NULL);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('errorcodetaskdata',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('accountinggroup',0);

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('assistent_question',0);

DELETE FROM qw_fieldinfo WHERE table_name='services';

CREATE TABLE services (
  pkey NUMBER(38) NOT NULL,
  name VARCHAR(80) NULL,
  description VARCHAR(500) NULL,
  service_key NUMBER(38) NULL,
  CONSTRAINT PK1_SERVICES_PRIMARYKEY PRIMARY KEY (pkey));

INSERT INTO qw_keys (tablename,keyvalue) VALUES ('services',0);

REVOKE ALL ON services FROM PUBLIC;

GRANT SELECT, UPDATE, INSERT, DELETE ON services TO PUBLIC;



ALTER TABLE categoryprocess ADD CONSTRAINT FK4_CATEGORYPROCESS_SERVICES_F FOREIGN KEY (service_key) REFERENCES services (pkey);

ALTER TABLE calltemplate ADD CONSTRAINT FK8_CALLTEMPLATE_OBJECT_FKEY FOREIGN KEY (object_key) REFERENCES object (pkey);

ALTER TABLE calltemplate ADD CONSTRAINT FK4_CALLTEMPLATE_CALLWORKGROUP FOREIGN KEY (callworkgroup_key) REFERENCES workgroup (pkey);

ALTER TABLE calltemplate ADD CONSTRAINT FK7_CALLTEMPLATE_PROCESS_FKEY FOREIGN KEY (process_key) REFERENCES process (pkey);

ALTER TABLE calltemplate ADD CONSTRAINT FK6_CALLTEMPLATE_ACCOUNTCODE_F FOREIGN KEY (accountcode_key) REFERENCES accountingcode (code);

ALTER TABLE calltemplate ADD CONSTRAINT FK2_CALLTEMPLATE_CUSTOMERINT_F FOREIGN KEY (customerint_key) REFERENCES employee (pkey);

ALTER TABLE calltemplate ADD CONSTRAINT FK5_CALLTEMPLATE_LOCATION_FKEY FOREIGN KEY (location_key) REFERENCES location (pkey);

ALTER TABLE calltemplate ADD CONSTRAINT FK3_CALLTEMPLATE_CATEGORY_FKEY FOREIGN KEY (category_key) REFERENCES category (pkey);

ALTER TABLE services ADD CONSTRAINT FK2_SERVICES_PARENTSERVICE_FKE FOREIGN KEY (service_key) REFERENCES services (pkey);

