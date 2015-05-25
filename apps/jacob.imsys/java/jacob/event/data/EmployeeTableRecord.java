/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.common.Util;

import org.apache.commons.codec.language.Soundex;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author mike
 */
public class EmployeeTableRecord extends DataTableRecordEventHandler
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord rec, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord rec, IDataTransaction transaction) throws Exception
	{
        {
              // record wird gel�scht. Verhindern, dass Werte an einem gel�schten Record gesetzt werden
              //
              if(rec.isDeleted())
                return;
              
                // �berpr�fen, ob f�r neue bzw. alte modifizierte Datens�tze der TTS Login
                // gesetzt bzw. ver�ndert
                // wurde und dieser noch nicht vergeben wurde.
                if (rec.hasChangedValue("loginname"))
                {
                    String loginname = rec.getStringValue("loginname");
                    if (loginname != null && rec.getTable().exists("loginname", loginname))
                    {
                        throw new UserException("TTS Login existiert bereits: " + loginname);
                    }
                    rec.setValue(transaction,"loginname",loginname.toUpperCase());
                }

                // �berpr�fen, ob f�r neue bzw. alte modifizierte Datens�tze die
                // Personalnummer gesetzt bzw. ver�ndert
                // wurde und dieser noch nicht vergeben wurde.
                if (rec.hasChangedValue("employeeid"))
                {
                    Object employeeid = rec.getValue("employeeid");
                    if (employeeid != null && rec.getTable().exists("employeeid", employeeid))
                    {
                        throw new UserException("Pers.Nr. existiert bereits: " + employeeid);
                    }
                }

                if (rec.hasChangedValue("employeestatus"))
                {
                    if (rec.isNew())
                    {
                        rec.setValue(transaction, "employeestatus", "von Hand");
                    }
                    else
                    {
                        Object oldValue = rec.getOldValue("employeestatus");
                        Object newValue = rec.getValue("employeestatus");
                        if ("DBCS".equals(oldValue))
                        {
                            rec.setValue(transaction, "employeestatus", "DBCS");
                            throw new UserException("Mitarbeiter Status DBCS darf nicht ge�ndert werden");
                        }
                        if ("DBCS Gel�scht".equals(oldValue) && !"DBCS".equals(newValue))
                        {
                            rec.setValue(transaction, "employeestatus", "DBCS");
                            throw new UserException("Mitarbeiter Status 'DBCS gel�scht' darf nur nach 'DBCS' ge�ndert werden");
                        }
                    }
                }

                if (rec.hasChangedValue("admin_role") || rec.hasChangedValue("supportrole") || rec.hasChangedValue("sdadmin_role") )
                {
                    if (!transaction.getUser().hasRole("CQ_ADMIN"))
                    {
                        if (!(rec.isNew() && rec.getintValue("admin_role") == 0 && rec.getintValue("supportrole") == 0 && rec.getintValue("sdadmin_role") == 0 ))
                        {
                            throw new UserException("Nur Administratoren' d�rfen die administrative Rollen �ndern");
                        }
                    }
                }
                
                if ("Email".equals(rec.getValue("communicatepref")) && null == rec.getValue("emailcorr"))
                {
                    rec.setValue(transaction, "communicatepref", "keine");
                }
                if ("FAX".equals(rec.getValue("communicatepref")) && null == rec.getValue("faxcorr"))
                {
                    rec.setValue(transaction, "communicatepref", "keine");
                }
                if ("SMS".equals(rec.getValue("communicatepref")) && null == rec.getValue("phonecorr"))
                {
                    rec.setValue(transaction, "communicatepref", "keine");
                }

                String sFirstname = rec.getSaveStringValue("firstnamecorr");
                String sLastname =  rec.getSaveStringValue("lastnamecorr");

                // We construct fullname only for new records
                if (rec.isNew())
                {
                    rec.setValue(transaction, "employeestatus","von Hand");
                }

                //Set the fullname from first and last names
                if (rec.hasChangedValue("firstnamecorr") || rec.hasChangedValue("lastnamecorr"))
                {
                    rec.setValue(transaction,"fullname", sFirstname + " " + sLastname);
                }
                
                // Force the first and last name to uppercase so that searching
                // will use any indexes.  
                rec.setValue(transaction, "firstnamecorr", sFirstname.toUpperCase());
                rec.setValue(transaction, "lastnamecorr", sLastname.toUpperCase());
            
                // create soundex entry
                Soundex code = new Soundex();
                rec.setValue(transaction,"soundex",code.soundex(Util.PrepareSoundex(sLastname)));
                 
                if (rec.isNew())
                {
                    rec.setValue(transaction, "firstname", sFirstname.toUpperCase());
                    rec.setValue(transaction, "lastname", sLastname.toUpperCase());
                }
                // festhalten, wer den Datensatz zuletzt ge�ndert hat
                rec.setValue(transaction, "changedby", Util.getUserLoginID(Context.getCurrent()));
            }
	}
}
