package jacob.common;

import jacob.model.Account;
import jacob.model.Public_part;
import jacob.model.Room;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

public class PartHelper 
{
	public static void setMandatorId(IDataTransaction trans, IDataTableRecord part) throws Exception
	{
		String userKey = Context.getCurrent().getUser().getKey();
		
		// Falls der Benutzer Mitglied eines Raumes ist, dann wird diese Id genommen
		//
		IDataTable accountTable = part.getAccessor().newAccessor().getTable(Account.NAME);
		accountTable.qbeSetKeyValue(Account.pkey, userKey);
		accountTable.search();
	
		IDataTableRecord record = accountTable.getSelectedRecord().getLinkedRecord(Room.NAME);
		if(record==null)
			part.setStringValue(trans, Public_part.mandator_id, "/digisim/"+Constants.DEFAULT_ROOM_NAME+"/"+userKey);
		else
			part.setStringValue(trans, Public_part.mandator_id, "/digisim/"+record.getSaveStringValue(Room.name)+"/"+userKey);
	}
}
