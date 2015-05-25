-- ###############################################################################################
-- #  Aufgabe:                                                                                   #
-- #   Feld phonecti mit dem Wert aus phonecorr befuellen und zwar um Sonderzeichen bereinigt    #
-- #  Datum       Grund                                                      Wer                 #
-- #  01.03.2003  initiale Erstellung                                        MIke Doering        #
-- #                                                                                             #
-- ###############################################################################################

CREATE OR REPLACE TRIGGER UpdateEmployeeOnInsertOrUpdate
BEFORE  INSERT  OR UPDATE  OF  PHONECORR ON EMPLOYEE FOR EACH ROW
DECLARE
   nCounter      integer;
   nPos          integer;
   nLength 	     integer;
   sString       varchar(30);
   sNewString    varchar(30);
   sChar         varchar(1);
Begin
if (:new.PHONECORR is NULL) then
   :new.PHONECORR := NULL;
end if;
if (:new.PHONECORR is NOT NULL)  then
   nPos := 1;
   sString := '';
   sNewString := '';
   sChar := '';
   sString := :new.PHONECORR;
   nLength := LENGTH(sString);
   WHILE  nPos <= nLength
   LOOP
   	sChar := SUBSTR(sString,nPos,1);
   	IF INSTR( '0123456789',sChar,1,1) > 0 THEN
   		sNewString := sNewString || sChar;
   	END IF;
   	IF INSTR( ',;',sChar,1,1) > 0 THEN
   		sNewString := sNewString || '#';
   	END IF;
    nPos := nPos +1;
   END LOOP;
    :new.PHONECTI := sNewString || '#';

end if;

End;
/
