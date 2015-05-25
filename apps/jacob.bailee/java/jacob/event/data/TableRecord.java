/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 06 23:29:17 CET 2010
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.RuleCache;
import jacob.common.RuleDocument;

import java.io.ByteArrayInputStream;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.ruleengine.RuleEngine;

/**
 *
 * @author andherz
 */
public class TableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: TableRecord.java,v 1.1 2010/01/11 08:50:43 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if(!RuleCache.has(Context.getCurrent(), tableRecord.getTableAlias().getName(), "afterNewAction"))
      return ;
    RuleDocument rule = RuleCache.get(Context.getCurrent(), tableRecord.getTableAlias().getName(), "afterNewAction");
    RuleEngine.execute(new ByteArrayInputStream(rule.xml),tableRecord);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if(!RuleCache.has(Context.getCurrent(), tableRecord.getTableAlias().getName(), "afterDeleteAction"))
      return ;
    RuleDocument rule = RuleCache.get(Context.getCurrent(), tableRecord.getTableAlias().getName(), "afterDeleteAction");
    RuleEngine.execute(new ByteArrayInputStream(rule.xml),tableRecord);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
    //
    if(tableRecord.isDeleted())
      return;

    // enter your code here
    if(!RuleCache.has(Context.getCurrent(), tableRecord.getTableAlias().getName(), "beforeCommitAction"))
      return ;
    RuleDocument rule = RuleCache.get(Context.getCurrent(), tableRecord.getTableAlias().getName(), "beforeCommitAction");
    RuleEngine.execute(new ByteArrayInputStream(rule.xml),tableRecord);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    if(!RuleCache.has(Context.getCurrent(), tableRecord.getTableAlias().getName(), "afterCommitAction"))
      return ;
    RuleDocument rule = RuleCache.get(Context.getCurrent(), tableRecord.getTableAlias().getName(), "afterCommitAction");
    RuleEngine.execute(new ByteArrayInputStream(rule.xml),tableRecord);
  }
}
