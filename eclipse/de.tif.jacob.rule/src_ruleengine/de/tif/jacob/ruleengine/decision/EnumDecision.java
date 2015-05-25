/*
 * Created on 12.12.2005
 *
 */
package de.tif.jacob.ruleengine.decision;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.ruleengine.Decision;

public class EnumDecision extends Decision
{
  public String getValue(String tableAlias, String field) throws Exception
  {
    IDataTableRecord record = this.getAffectedRecord(tableAlias);
    if(record==null)
      throw new UserException("No selected record for ["+tableAlias+"] found");
    
    return record.getStringValue(field);
  }
}
