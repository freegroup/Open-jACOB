package de.tif.jacob.ruleengine;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;



public abstract class Decision 
{
  private Context context;
  IDataTableRecord affectedRecord;

  protected final void init(Context context, IDataTableRecord affectedRecord)
  {
    this.context        = context;
    this.affectedRecord = affectedRecord;
  }

  public Context getContext()
  {
    return context;
  }



  protected final IDataTableRecord getAffectedRecord(String aliasName) throws Exception
  {
    IDataTableRecord record = affectedRecord;
    if(record!=null && record.getTableAlias().getName().equals(aliasName))
    {
      // alles ok
    }
    else
    {
      IDataTable table = this.getContext().getDataAccessor().getTable(aliasName);
      record = table.getSelectedRecord();
    }
    return record;
  }

}
