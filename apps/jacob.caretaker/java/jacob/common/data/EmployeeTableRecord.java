/*
 * Created on 13.07.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jacob.common.data;

import jacob.common.Util;

import org.apache.commons.codec.language.Soundex;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class EmployeeTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: EmployeeTableRecord.java,v 1.9 2007/03/07 22:50:26 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.9 $";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
	 *      de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
	 *      de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
	 *      de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord rec, IDataTransaction transaction) throws Exception
	{
	  // record wird gelöscht. Verhindern, dass Werte an einem gelöschten Record gesetzt werden
	  //
	  if(rec.isDeleted())
	    return;
	  
		// Überprüfen, ob für neue bzw. alte modifizierte Datensätze der TTS Login
		// gesetzt bzw. verändert
		// wurde und dieser noch nicht vergeben wurde.
		if (rec.hasChangedValue("loginname"))
		{
			Object loginname = rec.getValue("loginname");
			if (loginname != null && rec.getTable().exists("loginname", loginname))
			{
				throw new UserException("TTS Login existiert bereits: " + loginname);
			}
		}

		// Überprüfen, ob für neue bzw. alte modifizierte Datensätze die
		// Personalnummer gesetzt bzw. verändert
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
					throw new UserException("Mitarbeiter Status 'DBCS' darf nicht geändert werden");
				}
				if ("DBCS Gelöscht".equals(oldValue) && !"DBCS".equals(newValue))
				{
					rec.setValue(transaction, "employeestatus", "DBCS");
					throw new UserException("Mitarbeiter Status 'DBCS gelöscht' darf nur nach 'DBCS' geändert werden");
				}
			}
		}

		if (rec.hasChangedValue("admin_role") || rec.hasChangedValue("supportrole") || //
        rec.hasChangedValue("sdadmin_role") || rec.hasChangedValue("bp_role") || rec.hasChangedValue("tcadmin_role"))
		{
			if (!transaction.getUser().hasRole("CQ_ADMIN"))
			{
				if (!(rec.isNew() && rec.getintValue("admin_role") == 0 && rec.getintValue("supportrole") == 0 && //
            rec.getintValue("sdadmin_role") == 0 && rec.getintValue("bp_role") == 0 && rec.getintValue("tcadmin_role") == 0))
				{
					throw new UserException("Nur Administratoren dürfen die administrativen Rollen ändern");
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
    
    // festhalten, wer den Datensatz zuletzt geändert hat
    rec.setValue(transaction, "changedby", Util.getUserLoginID(Context.getCurrent()));
	}
}

