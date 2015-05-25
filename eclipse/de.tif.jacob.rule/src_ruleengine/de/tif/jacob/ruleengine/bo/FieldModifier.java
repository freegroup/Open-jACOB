/*
 * Created on 06.12.2005
 *
 */
package de.tif.jacob.ruleengine.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.messaging.Message;
import de.tif.jacob.ruleengine.BusinessObject;
import de.tif.jacob.ruleengine.RuleEngine;


public class FieldModifier extends BusinessObject
{
  // use the common logger for the rule engine!!! (Engine.class)
  static private final Log logger = LogFactory.getLog(RuleEngine.class);

  public void setFieldValue(String alias_dot_field, String newValue) throws Exception
  {
    String[] fields = alias_dot_field.split("[.]");
    if(fields.length!=2)
      throw new UserException("The parameter ["+alias_dot_field+"] doesn't match the pattern [alias.field].");
    
    IDataTableRecord record = this.getAffectedRecord(fields[0]);
    
    if(record.getCurrentTransaction()==null)
      throw new UserException("Selected record for ["+fields[0]+"] is not in update mode.");
    
    IDataTransaction trans = record.getCurrentTransaction();
    record.setValue(trans,fields[1],newValue);
  }
  
  public void setLinkedRecord(String localName, String foreignName,String foreignPkey ) throws Exception
  {
    IDataTableRecord localTable = getAffectedRecord(localName);
    if(localTable.getCurrentTransaction()==null)
      throw new UserException("Selected record for ["+localName+"] is not in update mode.");

    IDataAccessor acc = getContext().getDataAccessor();//;.newAccessor();
    IDataTable foreignTable = acc.getTable(foreignName);
    foreignTable.qbeClear();
    foreignTable.qbeSetKeyValue("pkey", foreignPkey);
    foreignTable.search(IRelationSet.LOCAL_NAME);
    if (localTable != null)
    {
      IDataTransaction trans = localTable.getCurrentTransaction();
      localTable.setLinkedRecord(trans, foreignTable.getSelectedRecord());
    }
    
  }
}
