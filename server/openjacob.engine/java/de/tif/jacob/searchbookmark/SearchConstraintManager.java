/*
 * Created on 17.11.2009
 *
 */
package de.tif.jacob.searchbookmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;

public class SearchConstraintManager
{
  private final static String GUID = "ddb85bab-fb3d-4ba7-ae90-7672f020c029";
  
  public static ISearchConstraint create(String name, String appName, String domainName, String formName, String version, IDataBrowserInternal dataBrowser)
  {
    return new SearchConstraint(name, appName, domainName, formName, version, dataBrowser);
  }
  
  /**
   * 
   * @param context
   * @return Collection of SearchConstraint
   * @throws Exception
   */
  public static Collection getSearchConstraints(IClientContext context) throws Exception
  {
    Map<String, SearchConstraint> constraints = (Map<String, SearchConstraint>)context.getProperty(GUID);
    
    if(constraints == null)
    {
       context.setPropertyForSession(GUID, constraints=new HashMap<String, SearchConstraint>());
       IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
       IDataTable table = accessor.getTable(de.tif.jacob.core.model.Searchconstraint.NAME);
       table.qbeClear();
       table.qbeSetKeyValue(de.tif.jacob.core.model.Searchconstraint.applicationname, context.getApplication().getName());
       table.qbeSetKeyValue(de.tif.jacob.core.model.Searchconstraint.ownerid, context.getUser().getLoginId());
       table.search(IRelationSet.LOCAL_NAME);
       for(int i=0;i<table.recordCount();i++)
       {
         IDataTableRecord record = table.getRecord(i);
         constraints.put(record.getStringValue(de.tif.jacob.core.model.Searchconstraint.pkey),new SearchConstraint(record));
       }
    }
    
    // Suchbedinungen/Bookmarks welche nicht ausgeführt werden können, z.B. Domain/Form ist nicht mehr
    // vorhanden, werden nicht durchgereicht. Führt zu Exception und Fehlermeldungen welche der Anwender eh nicht versteht
    //
    Collection<SearchConstraint> result = new ArrayList<SearchConstraint>();
    for (SearchConstraint searchConstraint : constraints.values())
    {
      String domainName = searchConstraint.getAnchorDomain();
      String formName = searchConstraint.getAnchorForm();
      String aliasName = searchConstraint.getAnchorTable();
      try
      {
        context.getDataTable(aliasName); // RuntimeException falls Tabelle nicht vorhanden
        IForm form = context.getDomain(domainName).getForm(formName);
        if(form!=null)
          result.add(searchConstraint);
      }
      catch(Exception e)
      {
        // ignore. Alias oder Domain nicht vorhanden
      }
    }
    
    return result;
  }
  
  /**
   * Returns the report definition with the hands over guid.
   * 
   * @return the report definition or <code>null</code> if the report does not
   *         exist anymore.
   */
  public static ISearchConstraint getSearchConstraint(IClientContext context, String pkey) throws Exception
  {
    Map constraints = (Map)context.getProperty(GUID);
    SearchConstraint constraint=null;
    
    if(constraints == null)
    {
       context.setPropertyForSession(GUID, constraints=new HashMap());
    }
    else
    {
      constraint= (SearchConstraint)constraints.get(pkey);
      if(constraint!=null)
        return constraint;
    }
    
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(de.tif.jacob.core.model.Searchconstraint.NAME);
    table.qbeClear();
    table.qbeSetKeyValue(de.tif.jacob.core.model.Searchconstraint.pkey, pkey);
    if (table.search() > 0)
    {
      constraints.put(pkey,constraint=new SearchConstraint(table.getRecord(0)));
    }
    return constraint;
  }


  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.report.IReport#delete()
   */
  public static void delete(IClientContext context, ISearchConstraint constraint) throws Exception
  {
    if (constraint.getGUID() == null)
    {
      // report not saved so far
      return;
    }

    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTransaction transaction = accessor.newTransaction();
    try
    {
      IDataTable table = accessor.getTable(de.tif.jacob.core.model.Searchconstraint.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(de.tif.jacob.core.model.Searchconstraint.pkey, constraint.getGUID());
      if (table.search() > 0)
      {
        table.getRecord(0).delete(transaction);
      }
      transaction.commit();
      Map constraints = (Map)context.getProperty(GUID);
      if(constraints == null)
        return;

      constraints.remove(constraint.getGUID());
    }
    finally
    {
      transaction.close();
    }
  }

  /*
   * Save the search constraint with the given name. This methods overrides existing SearchConstraints with
   * any warning.
   * 
   * (non-Javadoc)
   * 
   */
  public static void save(IClientContext context, ISearchConstraint constraint) throws Exception
  {
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(de.tif.jacob.core.model.Searchconstraint.NAME);
    IDataTransaction transaction = accessor.newTransaction();
    try
    {
      table.qbeClear();
      table.qbeSetKeyValue(de.tif.jacob.core.model.Searchconstraint.name, constraint.getName());
      table.qbeSetKeyValue(de.tif.jacob.core.model.Searchconstraint.applicationname, constraint.getApplication().getName());
      table.qbeSetKeyValue(de.tif.jacob.core.model.Searchconstraint.ownerid, constraint.getOwner());
      
      IDataTableRecord record;
      if (table.search() > 0)
       {
        record = table.getRecord(0);
      }
      else
      {
        record = table.newRecord(transaction);
        record.setValue(transaction, de.tif.jacob.core.model.Searchconstraint.name, constraint.getName());
        record.setValue(transaction, de.tif.jacob.core.model.Searchconstraint.applicationname, constraint.getApplicationName());
        record.setValue(transaction, de.tif.jacob.core.model.Searchconstraint.ownerid, constraint.getOwnerId());
        record.setValue(transaction, de.tif.jacob.core.model.Searchconstraint.applicationversion, constraint.getApplication().getVersion());

        constraint.setGUID(record.getStringValue(de.tif.jacob.core.model.Searchconstraint.pkey));
      }
      record.setValue(transaction, de.tif.jacob.core.model.Searchconstraint.definition, constraint.toXmlFormatted());
      
      transaction.commit();
      Map constraints = (Map)context.getProperty(GUID);
      if(constraints == null)
         context.setPropertyForSession(GUID, constraints=new HashMap());
      constraints.put(constraint.getGUID(),constraint);
    }
    finally
    {
      transaction.close();
    }
  }
}
