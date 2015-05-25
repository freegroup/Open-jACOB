/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Aug 04 16:59:40 CEST 2010
 */
package jacob.event.data;

import jacob.browser.GlobalcontentBrowser;
import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Folder;

import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.event.IDataBrowserModifiableRecord;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * 
 * @author andreas
 */
public class GlobalcontentTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: GlobalcontentTableRecord.java,v 1.1 2010-09-17 08:42:22 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static final String CACHE_DATAACCESSOR = GlobalcontentTableRecord.class.getName() + ":CACHE_DATAACCESSOR";

  private static String calculatePath(IDataTableRecord boRecord) throws RecordNotFoundException
  {
    try
    {
      IDataKeyValue parentBoKey = boRecord.getKeyValue("parent_bo_FKey");
      if (parentBoKey == null)
        return "/" + boRecord.getSaveStringValue(Bo.name);

      // Note: Use IDataTable#getRecord to get already prefetched records from
      // data accessor cache
      return calculatePath(boRecord.getTable().getRecord(parentBoKey)) + "/" + boRecord.getSaveStringValue(Bo.name);
    }
    catch (NoSuchFieldException ex)
    {
      // should never occur
      throw new RuntimeException(ex);
    }
  }

  private static IDataKeyValue getBoPrimaryKeyValue(IDataBrowserRecord browserRecord) throws Exception
  {
    String alias = browserRecord.getStringValue(GlobalcontentBrowser.browserTablealias);
    if (Document.NAME.equals(alias) || Folder.NAME.equals(alias))
    {
      IApplicationDefinition applDef = browserRecord.getAccessor().getApplication();
      return applDef.getTableAlias(alias).getTableDefinition().getPrimaryKey().convertStringToKeyValue(browserRecord.getStringValue(GlobalcontentBrowser.primarykey));
    }
    return null;
  }

  /**
   * Helper class to preload bo records
   * 
   * @author Andreas Sonntag
   */
  private final static class Preloader
  {
    private final IDataAccessor cacheAccessor;
    private final Set<IDataKeyValue> alreadyPreloadedBoPrimaryKeyValues = new HashSet<IDataKeyValue>();

    private Preloader(IDataAccessor cacheAccessor)
    {
      this.cacheAccessor = cacheAccessor;
    }

    /**
     * Preload bo records in chunks of max. 100!
     * 
     * @param cacheAccessor
     * @param preloadBoPrimaryKeyValues
     * @throws Exception
     */
    private void preloadBoRecordsAndParents(Set<IDataKeyValue> boPrimaryKeyValues) throws Exception
    {
      if (boPrimaryKeyValues.size() > 0)
      {
        Set<IDataKeyValue> nextBoPrimaryKeyValues = new HashSet<IDataKeyValue>();

        int n = 0;
        IDataTable boTable = cacheAccessor.getTable(Bo.NAME);
        boTable.qbeClear();
        final IKey boPrimaryKey = boTable.getTableAlias().getTableDefinition().getKey("primaryKey");
        for (IDataKeyValue boPrimaryKeyValue : boPrimaryKeyValues)
        {
          if (this.alreadyPreloadedBoPrimaryKeyValues.add(boPrimaryKeyValue))
          {
            boTable.qbeSetKeyValue(boPrimaryKey, boPrimaryKeyValue);
            if (++n == 100)
            {
              search(boTable, nextBoPrimaryKeyValues);
              boTable.qbeClear();
              n = 0;
            }
          }
        }
        if (n > 0)
          search(boTable, nextBoPrimaryKeyValues);
        
        // and get recursively the parents
        preloadBoRecordsAndParents(nextBoPrimaryKeyValues);
      }
    }

    private void search(IDataTable boTable, Set<IDataKeyValue> nextBoPrimaryKeyValues) throws Exception
    {
      boTable.search();
      for (int i = 0; i < boTable.recordCount(); i++)
      {
        IDataKeyValue parentBoKey = boTable.getRecord(i).getKeyValue("parent_bo_FKey");
        if (parentBoKey != null)
          nextBoPrimaryKeyValues.add(parentBoKey);
      }
    }
  }

  public void afterSearchAction(IDataBrowserModifiableRecord browserRecord) throws Exception
  {
    // Preload BO record instances to accelerate path calculation by means of
    // much fewer database queries
    //
    Context context = Context.getCurrent();
    IDataAccessor cacheAccessor = (IDataAccessor) context.getProperty(CACHE_DATAACCESSOR);
    if (cacheAccessor == null)
    {
      context.setPropertyForRequest(CACHE_DATAACCESSOR, cacheAccessor = context.getDataAccessor().newAccessor());

      // collect primary keys of bos
      IDataBrowser browser = browserRecord.getBrowser();
      Set<IDataKeyValue> boPrimaryKeyValues = new HashSet<IDataKeyValue>(browser.recordCount());
      for (int i = 0; i < browser.recordCount(); i++)
      {
        IDataKeyValue boPrimaryKeyValue = getBoPrimaryKeyValue(browser.getRecord(i));
        if (boPrimaryKeyValue != null)
          boPrimaryKeyValues.add(boPrimaryKeyValue);
      }
      
      // and recursively prefetch them
      new Preloader(cacheAccessor).preloadBoRecordsAndParents(boPrimaryKeyValues);
    }

    // Calculate path for documents and folders
    // Note: Here no database accesses should happen anymore
    //
    IDataKeyValue boPrimaryKeyValue = getBoPrimaryKeyValue(browserRecord);
    if (boPrimaryKeyValue != null)
    {
      try
      {
        browserRecord.setValue(GlobalcontentBrowser.subjectlong, calculatePath(cacheAccessor.getTable(Bo.NAME).getRecord(boPrimaryKeyValue)));
      }
      catch (RecordNotFoundException ex)
      {
        browserRecord.setValue(GlobalcontentBrowser.subjectlong, "<unknown path>");
      }
    }

    super.afterSearchAction(browserRecord);
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // nothing to do here
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // nothing to do here
  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // nothing to do here
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    // nothing to do here
  }
}
