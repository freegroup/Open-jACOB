package de.tif.jacob.ruleengine.decision;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.ruleengine.Decision;
import de.tif.jacob.util.ObjectUtil;

public class FieldDecision extends Decision 
{
  /**
   * Return true if the table field of the hands over table.field has been changed.
   * 
   * @param value1 must match the pattern <code>tablealias.field</code>
   * @return true if the table field has been changed
   * @throws Exception
   */
	public boolean fieldHasChanged(String value1) throws Exception
	{
     String[] fields = value1.split("[.]");
     if(fields.length!=2)
       throw new UserException("The parameter ["+value1+"] doesn't match the pattern [alias.field].");
     
     IDataTableRecord record = this.getAffectedRecord(fields[0]);
     
     if(record==null)
       return false;
     
     return record.hasChangedValue(fields[1]);
	}

  public boolean fieldIsSet(String value1) throws Exception
  {
     String[] fields = value1.split("[.]");
     if(fields.length!=2)
       throw new UserException("The parameter ["+value1+"] doesn't match the pattern [alias.field].");
     
     IDataTableRecord record = this.getAffectedRecord(fields[0]);

     if(record==null)
       return false;
     
     
     return record.getSaveStringValue(fields[1]).length()>0;
  }
}
