package jacob.common;

import jacob.event.ui.Application;
import jacob.model.Contact;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;

public class EmailAddress
{
  public static void add(IClientContext context, String emailAddress) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTable contactTable = accessor.getTable(Contact.NAME);
    contactTable.qbeClear();
    contactTable.qbeSetValue(Contact.privat_email,"|"+emailAddress);
    contactTable.qbeSetValue(Contact.work_email,"|"+emailAddress);
    if(contactTable.search()==0)
    {
      System.out.println("ContactRecords:"+contactTable.recordCount());
      IDataTransaction trans = contactTable.startNewTransaction();
      try
      {
       IDataTableRecord contactRec =contactTable.newRecord(trans);
       contactRec.setValue(trans,Contact.privat_email,emailAddress);
       contactRec.setValue(trans,Contact.type,Contact.type_ENUM._privat);
       contactRec.setValue(trans,Contact.mandator_id,context.getUser().getMandatorId());
       trans.commit();
  		 context.getUser().setProperty(Application.INITIAL_PRIVATE,Boolean.TRUE);
      }
      finally
      {
        trans.close();
      }
    }
  }
}
