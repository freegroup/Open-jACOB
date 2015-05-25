-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #    Anpassen von 'Location.description' beim EINFÜGEN in die Tabelle Location.               #
-- #    'Description wird aus den Feld 'shortname' von den Tabellen faplissite,                  #
-- #    faplissitepart, faplisbuilding, faplisbuildingpart, faplisfloor, faplisplane,            #
-- #    faplisroom, faplisaxis.                                                                  #
-- #                                                                                             #
-- #                                                                                             #
-- #  History:                                                                                   #
-- #  Datum       Grund                                                      Wer                 #
-- #  17.09.2002  initiale Erstellung                                        Andreas Herz        #
-- #                                                                                             #
-- ###############################################################################################
CREATE OR REPLACE TRIGGER UpdateLocationDescOnInsert before insert on Location
for each row
declare
   name_site          varchar(200);
   name_sitepart      varchar(200);
   name_building      varchar(200);
   name_buildingpart  varchar(200);
   name_floor         varchar(200);
   name_plane         varchar(200);
   name_room          varchar(200);
   name_axis          varchar(200);
   fapliskey_existing number:=0;
BEGIN

if :new.site_key IS NOT NULL then
   select RTRIM(shortname) into  name_site         from faplissite         where pkey= :new.site_key;
   fapliskey_existing:=1;
end if;   
if :new.sitepart_key IS NOT NULL then
   select RTRIM(shortname) into  name_sitepart     from faplissitepart     where pkey= :new.sitepart_key;
   fapliskey_existing:=1;
end if;   
if :new.building_key IS NOT NULL then
   select RTRIM(shortname) into  name_building     from faplisbuilding     where pkey= :new.building_key;
   fapliskey_existing:=1;
end if;   
if :new.buildingpart_key IS NOT NULL then
   select RTRIM(shortname) into  name_buildingpart from faplisbuildingpart where pkey= :new.buildingpart_key;
   fapliskey_existing:=1;
end if;   
if :new.floor_key IS NOT NULL then
   select RTRIM(shortname) into  name_floor        from faplisfloor        where pkey= :new.floor_key;
   fapliskey_existing:=1;
end if;   
if :new.plane_key IS NOT NULL then
   select RTRIM(shortname) into  name_plane        from faplisplane        where pkey= :new.plane_key;
   fapliskey_existing:=1;
end if;   
if :new.room_key IS NOT NULL then
   select RTRIM(shortname) into  name_room         from faplisroom         where pkey= :new.room_key;
   fapliskey_existing:=1;
end if;   
if :new.note IS NOT NULL then
   name_axis:= RTRIM(:new.note);
   fapliskey_existing:=1;
end if;  
   
if fapliskey_existing > 0 then
   :new.description := SUBSTR(name_site || '/' || name_sitepart || '/' || name_building || '/' || name_buildingpart || '/' || name_floor || '/' || name_plane || '/' || name_room || '/' || name_axis ,1,120);
else
   :new.description := '///////';
end if;   
   
--   DBMS_OUTPUT.PUT_LINE('new description for location object [' || :new.pkey || '] is [' || :new.description || ']'); 
END;
/
