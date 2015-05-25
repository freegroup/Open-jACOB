/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
package de.tif.jacob.core.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * This class represents the abstract base class for all data table record event
 * handlers. A data table record event handler has to be implemented to "hook"
 * application-specific business logic to table aliases. Therefore, the
 * following hook methods could be implemented: <br>
 * <li>{@link #afterNewAction(IDataTableRecord, IDataTransaction)}
 * <li>{@link #afterDeleteAction(IDataTableRecord, IDataTransaction)}
 * <li>{@link #beforeCommitAction(IDataTableRecord, IDataTransaction)}
 * <li>{@link #afterCommitAction(IDataTableRecord)}
 * <p>
 * The jACOB application server then executes those hook methods on data level
 * whenever data records of the related table alias are modified.
 * 
 * @author Andreas Sonntag
 */
public abstract class DataTableRecordEventHandler extends DataEventHandler
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: DataTableRecordEventHandler.java,v 1.13 2010/04/13 08:29:26 freegroup Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.13 $";

  private static final Log logger = LogFactory.getLog(DataTableRecordEventHandler.class);

  private static final DefaultEventHandler DEFAULT = new DefaultEventHandler();

  private static final ReadonlyEventHandler READONLY = new ReadonlyEventHandler();

	/**
   * This hook method will be called immediately after a new record instance has
   * been created and initialized. This hook method is the right place to carry
   * out further application specific initializations before the record will be
   * displayed on the user screen.
   * <p>
   * Attention: The record has not been committed to the data source!
   * 
   * @param tableRecord
   *          the table record
   * @param transaction
   *          the transaction in which context this method is called
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * 
   * @see IDataTable#newRecord(IDataTransaction)
   * @see IDataAccessor#cloneRecord(IDataTransaction, IDataTableRecord)
   * @see IDataAccessor#cloneRecord(IDataTransaction, IDataTableRecord,
   *      ITableAlias)
   * @see IDataAccessor#cloneRecord(IDataTransaction, IDataTableRecord, String)
   */
  public abstract void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception;

	/**
   * This hook method will be called immediately after a delete operation has
   * been invoked on the given record instance. This hook method is the right
   * place to carry out further application specific clear up actions.
   * <p>
   * Attention: The record deletion has not been pushed to the data source!
   * 
   * @param tableRecord
   *          the table record
   * @param transaction
   *          the transaction in which context this method is called
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * 
   * @see IDataTableRecord#delete(IDataTransaction)
   * @see IDataTable#searchAndDelete(IDataTransaction)
   * @see IDataTable#fastDelete(IDataTransaction)
   */
  public abstract void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception;

	/**
   * This hook method will be called immediately before a record modification
   * will be pushed to the data source. A record modification might be either an
   * insert, update or delete action!
   * <p>
   * This hook method is the right place to carry out further application
   * specific actions within the same transactional context and is especially
   * useful to implement extended data consistency checks which are not handled
   * by the underlying database. This is done by means of throwing an exception
   * (in most cases this will be a
   * {@link de.tif.jacob.core.exception.UserException}), if such a consistency
   * check fails.
   * <p>
   * Attention: This hook method does <b>not</b> have a <b>recursive</b>
   * behaviour, i.e. neither
   * {@link #beforeCommitAction(IDataTableRecord, IDataTransaction)} nor
   * {@link #afterCommitAction(IDataTableRecord)} hook methods will be called
   * for data records being additionally created or manipulated within this method.
   * Nevertheless, this is the case for
   * {@link #afterNewAction(IDataTableRecord, IDataTransaction)} and
   * {@link #afterDeleteAction(IDataTableRecord, IDataTransaction)}!
   * 
   * @param tableRecord
   *          the table record
   * @param transaction
   *          the transaction in which context this method is called
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * @see IDataTransaction#commit()
   */
	public abstract void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception;

	/**
   * This hook method will be called immediately after a record modification has
   * been pushed to the data source. A record modification might be either an
   * insert, update or delete action!
   * <p>
   * This hook method could be used to carry out further application actions
   * within a separate transactional context.
   * 
   * @param tableRecord
   *          the table record
   * @throws Exception
   *           on any error, exception will <b>NOT </b> be handed over to the
   *           caller but a log entry will be written.
   * @see IDataTransaction#commit()
   */
  public abstract void afterCommitAction(IDataTableRecord tableRecord) throws Exception;

  /**
   * This hook method will be called immediately before execution of an search
   * operation. The main use of this hook is to set additional QBE constraints
   * dynamically.
   * 
   * @param table
   *          the data table on which a search operation is performed
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * @since 2.7.2
   */
  public void beforeSearchAction(IDataTable table) throws Exception
  {
    // do nothing by default
  }

  /**
   * This hook method will be called immediately before execution of an search
   * operation. The main use of this hook is to set additional QBE constraints
   * dynamically.
   * 
   * @param browser
   *          the data browser on which a search operation is performed
   * @param table
   *          the appropriate data table 
   * @param relationSet
   *          the relation set used to check for QBE constraints
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * @since 2.8.6
   */
  public void beforeSearchAction(IDataBrowser browser, IDataTable table, IRelationSet relationSet) throws Exception
  {
    // for downward compatibility, but might be overwritten
    beforeSearchAction(table);
  }

  /**
   * This hook method enables table record filtering.
   * <p>
   * This hook method can be used, if {@link #beforeSearchAction(IDataTable)}
   * can not be used to deliver the desired record result set, i.e. in case
   * there is a complex business logic for filtering which cannot be handled
   * with QBE constraints. Nevertheless, this usually has a significantly higher
   * overhead since records have to be transfer from datasource first. If
   * possible, setting QBE constraints by means of
   * {@link #beforeSearchAction(IDataTable)} is a good strategie to "pre-filter"
   * record result set on datasource.
   * <p>
   * Note: {@link #isFilterSearchAction()} should return <code>true</code>,
   * if filtering is enabled depending on the given business logic.
   * 
   * @param tableRecord
   *          the table record to check
   * @return <code>true</code> if the given record passes filtering, otherwise
   *         <code>false</code>, if the record should be skipped
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * @see #isFilterSearchAction()
   * @since 2.7.2
   */
  public boolean filterSearchAction(IDataTableRecord tableRecord) throws Exception
  {
    return true;
  }

  /**
   * This hook method enables browser record filtering.
   * <p>
   * This hook method can be used, if {@link #beforeSearchAction(IDataTable)}
   * can not be used to deliver the desired record result set, i.e. in case
   * there is a complex business logic for filtering which cannot be handled
   * with QBE constraints. Nevertheless, this usually has a significantly higher
   * overhead since records have to be transfer from datasource first. If
   * possible, setting QBE constraints by means of
   * {@link #beforeSearchAction(IDataTable)} is a good strategie to "pre-filter"
   * record result set on datasource.
   * <p>
   * Note: {@link #isFilterSearchAction()} should return <code>true</code>,
   * if filtering is enabled depending on the given business logic.
   * 
   * @param browserRecord
   *          the browser record to check
   * @return <code>true</code> if the given record passes filtering, otherwise
   *         <code>false</code>, if the record should be skipped
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * @see #isFilterSearchAction()
   * @since 2.7.2
   */
  public boolean filterSearchAction(IDataBrowserRecord browserRecord) throws Exception
  {
    return true;
  }

  /**
   * This hook method enables browser record modification.
   * <p>
   * This hook method can be used to modify browser record field values. This is
   * especially useful to implement calculated fields. Another use case is to
   * hide field values (e.g. employee salary) depending on the access rights of
   * the current user.
   * <p>
   * Note:
   * {@link IBrowserEventHandler#filterCell(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, int, int, IDataBrowserRecord, String)}
   * should not be used instead, if the modified field values are regarded for
   * sorting.
   * 
   * @param browserRecord
   *          the browser record to modify
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * @see IDataBrowserModifiableRecord#setValue(int, Object)
   * @see IDataBrowserModifiableRecord#setValue(String, Object)
   * @since 2.7.4
   */
  public void afterSearchAction(IDataBrowserModifiableRecord browserRecord) throws Exception
  {
    // do nothing by default
  }

  /**
   * This hook method indicates whether record filtering is enabled by means of
   * {@link #filterSearchAction(IDataTableRecord)} or
   * {@link #filterSearchAction(IDataBrowserRecord)}.
   * 
   * @return <code>true</code> if record filtering is activated, otherwise
   *         <code>false</code>
   * @see #filterSearchAction(IDataTableRecord)
   * @see #filterSearchAction(IDataBrowserRecord)
   * @since 2.7.2
   */
  public boolean isFilterSearchAction()
  {
    return false;
  }

  /**
   * This method adds the default header "###### USERID: TIMESTAMP" (e.g.
   * "###### admin: Feb 8, 2008 1:58:52 PM GMT+01:00") to text parts which are
   * appended or prepended to a long text field.
   * 
   * @param tableRecord
   *          the table record
   * @param transaction
   *          the transaction in which context this method is called
   * @param longtextFieldName
   *          the name of the long text field
   * @param text
   *          the text part to append or prepend to the long text field
   * @param appendNotPrepend
   *          <code>true</code> if the text part is appended or
   *          <code>false</code> if the text part is prepended
   * @return the text part with the added default header.
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * @see #getLongTextWithHeader(IDataTableRecord, IDataTransaction, String,
   *      String, boolean)
   * @since 2.9
   */
  public static String getLongTextWithDefaultHeader(IDataTableRecord tableRecord, IDataTransaction transaction, String longtextFieldName, String text, boolean appendNotPrepend) throws Exception
  {
    StringBuffer buffer = new StringBuffer(text.length() + 80);
    if (appendNotPrepend)
    {
      // IBIS: avoid '\n' for first append
      buffer.append("\n");
    }
    buffer.append("###### ").append(transaction.getUser().getLoginId());
    
    // create locale depending formatter
    // Note: Use full format for printing timezone as well
    //
    buffer.append(": ").append(I18N.toFullDatetimeString(transaction.getApplicationLocale(), transaction.getTimestamp()));
    
    // Den Alias auf den der Hook gerufen wurde mit ausgeben. Es ist wichtig zu sehen ob z.B. am Alias "Employee" oder "User" etwas geändert wurde.
    //
    buffer.append(" (");
    buffer.append(tableRecord.getTableAlias().getName());
    buffer.append(")");
    
    if (text.charAt(0) != '\n')
      buffer.append("\n");
    
    buffer.append(text);
    
    if (text.charAt(text.length() - 1) != '\n')
      buffer.append("\n");
    
    if (!appendNotPrepend)
    {
      buffer.append("\n");
    }
    
    return buffer.toString();
  }
  
  /**
   * This hook method adds a header to text parts which are appended or
   * prepended to a long text field.
   * <p>
   * This hook method can be overwritten, if the default header "###### USERID:
   * TIMESTAMP" (e.g. "###### admin: Feb 8, 2008 1:58:52 PM GMT+01:00") is not
   * desired (see
   * {@link #getLongTextWithDefaultHeader(IDataTableRecord, IDataTransaction, String, String, boolean)}
   * ).
   * <p>
   * This method will be called for history fields as well as for long text
   * fields with enabled header (see
   * {@link LongTextFieldType#isChangeHeaderEnabled()}).
   * <p>
   * Note: Simply return <code>text</code>, if no header should be added for any
   * circumstances.
   * 
   * @param tableRecord
   *          the table record
   * @param transaction
   *          the transaction in which context this method is called
   * @param longtextFieldName
   *          the name of the long text field
   * @param text
   *          the text part to append or prepend to the long text field
   * @param appendNotPrepend
   *          <code>true</code> if the text part is appended or
   *          <code>false</code> if the text part is prepended
   * @return the text part with the added header or <code>null</code> to use the
   *         default header as mentioned above.
   * @throws Exception
   *           on any error, exception will be handed over to the caller
   * @see IDataTableRecord#appendLongTextValue(IDataTransaction, String, String)
   * @see IDataTableRecord#prependLongTextValue(IDataTransaction, String,
   *      String)
   * @since 2.7.2
   */
  public String getLongTextWithHeader(IDataTableRecord tableRecord, IDataTransaction transaction, String longtextFieldName, String text, boolean appendNotPrepend) throws Exception
  {
    return null;
  }

	/**
   * Internal method for fetching an event handler by means of the given data
   * table.
   * 
   * @param table
   *          the data table to fetch the event handler for
   * @return the resulting event handler, i.e. if no explicit table record event
   *         handler exists, a default handler would be returned.
   * @throws Exception
   *           a severe problem has occurred
   */
	public static final DataTableRecordEventHandler get(IDataTable table) throws Exception
	{
    DataTableRecordEventHandler handler;
    if (DeployManager.isReadonlyDataHookAccess())
    {
      handler = READONLY;
    }
    else
    {
      handler = (DataTableRecordEventHandler) getEventHandler(table.getAccessor().getApplication(),table, "TableRecord");
      if (null == handler)
      {
        handler = DEFAULT;
      }
      
      // Falls ein generischer EventHandler hinterlegt ist, dann wird dieser ebenfalls gerufen
      // sobald der HauptEventHandler gerufen wrude. Mann hat so die Möglichkeit, dass man einmal
      // generischen Code hinterlegen kann. Hilfreiche für Logging, Tracing, Wertprüfung,...
      //
      DataTableRecordEventHandler genericHandler = (DataTableRecordEventHandler) getEventHandler(table.getAccessor().getApplication(), null, "TableRecord");
      if (null != genericHandler)
      {
        handler = new TwinEventHandler(handler,genericHandler);
      }
    }
    if (logger.isTraceEnabled())
    {
      logger.trace("Got handler class " + handler.getClass().toString());
    }
    return handler;
	}


  /**
   * Readonly table record event handler.
   * 
   * @author Andreas Sonntag
   */
  private static final class ReadonlyEventHandler extends DataTableRecordEventHandler
  {
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction)
    {
      throw new IllegalStateException();
    }

    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction)
    {
      throw new IllegalStateException();
    }

    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction)
    {
      throw new IllegalStateException();
    }

    public void afterCommitAction(IDataTableRecord tableRecord)
    {
      throw new IllegalStateException();
    }
  }

  /**
   * Record EventHandler for sequenz calling of EventHandler
   * 
   * @author Andreas Herz
   */
  private static final class TwinEventHandler extends DataTableRecordEventHandler
  {
    private final DataTableRecordEventHandler first;
    private final DataTableRecordEventHandler second;
    
    private TwinEventHandler(DataTableRecordEventHandler first, DataTableRecordEventHandler second)
    {
      this.first = first;
      this.second = second;
    }
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
      this.first.afterNewAction(tableRecord, transaction);
      this.second.afterNewAction(tableRecord, transaction);
    }

    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
      this.first.afterDeleteAction(tableRecord, transaction);
      this.second.afterDeleteAction(tableRecord, transaction);
    }

    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
      this.first.beforeCommitAction(tableRecord, transaction);
      this.second.beforeCommitAction(tableRecord, transaction);
    }

    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
      this.first.afterCommitAction(tableRecord);
      this.second.afterCommitAction(tableRecord);
    }
    
    public void afterSearchAction(IDataBrowserModifiableRecord browserRecord) throws Exception
    {
      this.first.afterSearchAction(browserRecord);
      this.second.afterSearchAction(browserRecord);
    }
    
    public void beforeSearchAction(IDataBrowser browser, IDataTable table, IRelationSet relationSet) throws Exception
    {
      this.first.beforeSearchAction(browser, table, relationSet);
      this.second.beforeSearchAction(browser, table, relationSet);
    }
    
    public void beforeSearchAction(IDataTable table) throws Exception
    {
      this.first.beforeSearchAction(table);
      this.second.beforeSearchAction(table);
    }
    
    public boolean filterSearchAction(IDataBrowserRecord browserRecord) throws Exception
    {
      return this.first.filterSearchAction(browserRecord) &&
             this.second.filterSearchAction(browserRecord);
    }
    
    public boolean filterSearchAction(IDataTableRecord tableRecord) throws Exception
    {
      return this.first.filterSearchAction(tableRecord) &&
             this.second.filterSearchAction(tableRecord);
    }
    
    public String getLongTextWithHeader(IDataTableRecord tableRecord, IDataTransaction transaction, String longtextFieldName, String text, boolean appendNotPrepend) throws Exception
    {
      String res = this.first.getLongTextWithHeader(tableRecord, transaction, longtextFieldName, text, appendNotPrepend);
      if (res == null)
        res = this.second.getLongTextWithHeader(tableRecord, transaction, longtextFieldName, text, appendNotPrepend);
      return res;
    }
    
    public boolean isFilterSearchAction()
    {
      return this.first.isFilterSearchAction() ||
             this.second.isFilterSearchAction();
    }
    
  }

  /**
   * Default table record event handler which does nothing than logging.
   * 
   * @author Andreas Sonntag
   */
  private static final class DefaultEventHandler extends DataTableRecordEventHandler
  {
    private static final Log logger = LogFactory.getLog(DefaultEventHandler.class);

    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction)
    {
      if (logger.isTraceEnabled())
      {
        logger.trace("No after new action to execute for " + tableRecord.getTable());
      }
    }

    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction)
    {
      if (logger.isTraceEnabled())
      {
        logger.trace("No after delete action to execute for " + tableRecord.getTable());
      }
    }

    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction)
    {
      if (logger.isTraceEnabled())
      {
        logger.trace("No before commit action to execute for " + tableRecord.getTable());
      }
    }

    public void afterCommitAction(IDataTableRecord tableRecord)
    {
      if (logger.isTraceEnabled())
      {
        logger.trace("No after commit action to execute for " + tableRecord.getTable());
      }
    }
  }
}
