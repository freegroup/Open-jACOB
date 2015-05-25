/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Feb 02 15:46:39 CET 2006
 */
package jacob.event.data;

import jacob.model.Rcs_branch;
import jacob.model.Rcs_project;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 * 
 * @author mike
 */
public class Rcs_branchTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: Rcs_branchTableRecord.java,v 1.2 2006/02/24 02:16:15 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public static final String HEAD = "HEAD";
  
  /**
   * Use a very high (hopefully never reached) version number for the head entry
   * to ensure that this will be on the top, if sorted by version number.
   */
  public static final BigDecimal HEAD_VERSIONNBR = new BigDecimal("999.999999"); 

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
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  private static String checkVersion(IDataTableRecord currentRecord) throws Exception
  {
    String version = currentRecord.getStringValue(Rcs_branch.version);
    if (!currentRecord.hasChangedValue(Rcs_branch.version) && version != null)
    {
      return version;
    }
    Pattern pattern = Pattern.compile("((BRANCH){0,1}(\\d+)(\\.\\d+){0,2})|" + HEAD);
    Matcher matcher = pattern.matcher(version);
    if (matcher.matches())
      return version;

    throw new UserException("Invalid branch version: " + version);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // Be in mind: It is not possible to modify the 'tableRecord', if we want
    // delete it
    //
    if (tableRecord.isDeleted())
      return;

    actualizeRecord(tableRecord, transaction);
  }

  protected static void actualizeRecord(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (!tableRecord.hasLinkedRecord(Rcs_project.NAME))
      throw new UserException("You have to link a project first");

    String projectName = tableRecord.getLinkedRecord(Rcs_project.NAME).getSaveStringValue(Rcs_project.rcs_name);
    String version = checkVersion(tableRecord);
//    if (!HEAD.equals(version))
//    {
//      if (Rcs_branch.status_ENUM._Release.equals(tableRecord.getValue(Rcs_branch.status)))
//        version = "R" + version;
//    }
    tableRecord.setValue(transaction, Rcs_branch.name, projectName + ":" + version);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }
}
