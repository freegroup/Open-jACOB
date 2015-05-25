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

package de.tif.jacob.core.data.impl.ldap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

import com.sun.jndi.ldap.LdapCtxFactory;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.DataExecutionContext;
import de.tif.jacob.core.data.impl.DataRecord;
import de.tif.jacob.core.data.impl.DataRecordSet;
import de.tif.jacob.core.data.impl.DataSearchResult;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataTransaction;
import de.tif.jacob.core.data.impl.IDataSearchIterateCallback;
import de.tif.jacob.core.data.impl.IDataSearchResult;
import de.tif.jacob.core.data.impl.misc.InvalidFieldExpressionException;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEField;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.qbe.QBEUserConstraint;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordsAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.i18n.CoreMessage;

/**
 * LDAP data source implementation
 * 
 * @author Andreas Sonntag
 */
public final class LdapDataSource extends DataSource
{
  static public transient final String RCS_ID = "$Id: LdapDataSource.java,v 1.9 2010-07-13 17:57:40 sonntag Exp $";
  static public transient final String RCS_REV = "$Revision: 1.9 $";
  
  private static final String CONTEXTFACTORY = LdapCtxFactory.class.getName();
  
  private final String searchBase; //"dc=ibis-software,dc=de";
  private final String providerUrl; // = "ldap://localhost";
  private final String mgrDN; // = "cn=Manager,dc=ibis-software,dc=de";
  private final String mgrPw; // = "secret";
  private final int searchScope; 

  /**
   * Constructor 
   * @param name
   */
  public LdapDataSource(String name)
  {
    super(name);
    
    this.providerUrl = conf.getProperty("datasource." + name + ".providerUrl");
    this.searchBase = conf.getProperty("datasource." + name + ".searchBase");
    this.searchScope = parseScope(conf.getProperty("datasource." + name + ".searchScope"));
    this.mgrDN = conf.getProperty("datasource." + name + ".mgrDN");
    this.mgrPw = conf.getProperty("datasource." + name + ".mgrPw");
    
    if (null == this.providerUrl)
      throw new RuntimeException("No connect information found - provider url is missing!");
    
    if (null == this.searchBase)
      throw new RuntimeException("No connect information found - search base is missing!");
  }

  /**
   * @param record
   * @throws Exception
   */
  public LdapDataSource(IDataTableRecord record) throws Exception
  {
    super(record);
    
    String connectString;
    if (Datasource.location_ENUM._Webserver.equals(record.getStringValue(Datasource.location)))
    {
      String jndiName = record.getStringValue(Datasource.jndiname);
      
      if (null == jndiName)
        throw new RuntimeException("No connect information found - JNDI name is missing!");
      
      // try to resolve name space binding via JNDI
      //
      Object jndiObject;
      try
      {
        jndiObject = jndiLookup(jndiName);
      }
      catch (Exception ex)
      {
        throw new Exception("Accessing JNDI object '" + jndiName + "' failed", ex);
      }

      if (!(jndiObject instanceof String))
      {
        throw new Exception("JNDI object '" + jndiName + "' is not a String: " + (jndiObject == null ? "null" : jndiObject.getClass().getName()));
      }
      
      connectString = (String) jndiObject;
      
      this.mgrDN = extractFromConnectString(connectString, "user:");
      this.mgrPw  = extractFromConnectString(connectString, "password:");
    }
    else
    {
      connectString = record.getStringValue(Datasource.connectstring);
      
      if (null == connectString)
        throw new RuntimeException("No connect information found - connectString is missing!");
      
//      this.driverClassName = record.getStringValue("jdbcDriverClass");
      this.mgrDN = record.getStringValue(Datasource.username);
      this.mgrPw = record.getStringValue(Datasource.password);
    }
    
    this.providerUrl = extractFromConnectString(connectString, "url:");
    this.searchBase  = extractFromConnectString(connectString, "base:");
    this.searchScope  = parseScope(extractFromConnectString(connectString, "scope:"));
    
//    this.pooling = "Yes".equals(record.getValue("configurePool"));
//    this.validationQuery = record.getStringValue("validationQuery");
//    this.maxActiveCons = record.getintValue("maxActiveCons");
//    this.maxIdleCons = record.getintValue("maxIdleCons");
//    this.maxConnectionWait = record.getintValue("maxConnectionWait");
    
    if (null == this.providerUrl)
      throw new Exception("No connect information found - provider url is missing!");
    
    if (null == this.searchBase)
      throw new Exception("No connect information found - search base is missing!");
  }
  
  private static int parseScope(String scope)
  {
    if ("subtree".equals(scope))
      return SearchControls.SUBTREE_SCOPE;
    
    if ("onelevel".equals(scope))
      return SearchControls.ONELEVEL_SCOPE;
    
    if ("object".equals(scope))
      return SearchControls.OBJECT_SCOPE;
    
    throw new RuntimeException("Unknown scope: "+ scope);
  }
  
  private static String extractFromConnectString(String connectString, String parameter)
  {
    int pos = connectString.indexOf(parameter);
    if (-1 != pos)
    {
      int startPos = pos + parameter.length();
      int endPos = connectString.indexOf(";", startPos);
      if (-1 == endPos)
        return connectString.substring(startPos);
      return connectString.substring(startPos, endPos);
    }

    return null;
  }

  public static String convertToLdap(String input)
  {
    if (input == null || input.length() == 0)
      return null;
    
    StringBuffer buffer = new StringBuffer(input.length()+16);
    for (int i=0; i< input.length(); i++)
    {
      char c = input.charAt(i);
      switch (c)
      {
        case '(':
          buffer.append("\\28");
          continue;
          
        case ')':
          buffer.append("\\29");
          continue;
          
        case '\\':
          buffer.append("\\5c");
          continue;
          
        case '/':
          buffer.append("\\2f");
          continue;
          
        case '*':
          buffer.append("\\2a");
          continue;
      }
      
      buffer.append(c);
    }
    return buffer.toString();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#test()
   */
  public String test() throws Exception
  {
	  StringBuffer buffer = new StringBuffer();
	  
	  // simply try to get a context
    DirContext context = getContext();
	  try
	  {
	    buffer.append("Environment:\r\n");
	    Hashtable table = context.getEnvironment();
	    Enumeration enumeration = table.keys();
	    while (enumeration.hasMoreElements())
	    {
	      Object key = enumeration.nextElement();
	      Object value = table.get(key);
		    buffer.append("\t").append(key).append(": ").append(value).append("\r\n");
	    }
	    
	    // IBIS: LDAP: What else to test?
	  }
	  finally
	  {
	    context.close();
	  }
	  
	  return buffer.toString();
  }

  private String buildSearchFilter(QBESpecification spec) throws InvalidExpressionException
  {
    boolean constraintExists = false;
    LdapSearchFilterBuilder filterBuilder = new LdapSearchFilterBuilder();
    filterBuilder.append("(&");

    // -------------------------------------
    // process user where clauses
    // -------------------------------------
    //
  	List userConstraints = spec.getUserConstraints();
  	for (int i=0; i < userConstraints.size(); i++)
  	{
      constraintExists = true;
  	  QBEUserConstraint userConstraint = (QBEUserConstraint) userConstraints.get(i);
  	  filterBuilder.append("(").append(userConstraint.toString()).append(")");
  	}

    // -------------------------------------
    // process relation constraints (joins)
    // -------------------------------------
    // IBIS : LDAP: How to handle joins?

    // -------------------------------------
    // process field constraints
    // -------------------------------------
    Iterator iter = spec.getFieldConstraints();
    while (iter.hasNext())
    {
      QBEFieldConstraint constraint = (QBEFieldConstraint) iter.next();

      ITableAlias alias = constraint.getTableAlias();
      ITableField field = constraint.getTableField();
      
      filterBuilder.setTableField(alias, field);

      try
      {
        QBEExpression expr = field.getType().createQBEExpression(this, constraint);
        if (expr == null)
          continue;

        constraintExists = true;

        // IBIS : LDAP: How to handle constraint.isOptional()?

        // IBIS : LDAP: aliasOr Handling missing
        
        expr.makeConstraint(filterBuilder, false);
      }
      catch (InvalidExpressionException ex)
      {
        // exchange exception to add field info
        throw new InvalidFieldExpressionException(field, ex);
      }
      finally
      {
        filterBuilder.resetTableField();
      }
    }
    
    filterBuilder.append(")");

    // -------------------------------------
    // process inverse relations
    // -------------------------------------
    // IBIS : LDAP: How to handle inverse relations?

    // -------------------------------------
    // process orderby clause
    // -------------------------------------
    // IBIS : LDAP: How to handle order by?

    // we do not allow unconstrained LDAP queries
    //
    if (!constraintExists)
      throw new UserRuntimeException(new CoreMessage(CoreMessage.UNCONSTRAINED_SEARCH));
    
    return filterBuilder.toString();
  }
  
	private DirContext getContext() throws NamingException
  {
    Hashtable environment = new Hashtable();
    environment.put(Context.INITIAL_CONTEXT_FACTORY, CONTEXTFACTORY);
    environment.put(Context.PROVIDER_URL, providerUrl);
    
    /*
     * specify authentication information
     */
    environment.put(Context.SECURITY_AUTHENTICATION, "simple");
    environment.put(Context.SECURITY_PRINCIPAL, mgrDN);
    environment.put(Context.SECURITY_CREDENTIALS, mgrPw);
    //          if (USE_SSL) environment.put(Context.SECURITY_PROTOCOL, "ssl");
    
    DirContext dircontext = new InitialDirContext(environment);
    return new WrapperDirContext(this, dircontext);
  }
  
  private DataSearchResult executeQuery(DataRecordSet recordSet, QBESpecification spec, String searchFilter, IDataSearchIterateCallback callback, DataTableRecordEventHandler eventHandler) throws Exception
  {
    DirContext context = getContext();
    try
    {
      // Collect attribute names
      //
      List resultFields = spec.getFieldsToQuery();
      List resultFieldNames = new ArrayList(resultFields.size());

      for (int i = 0; i < resultFields.size(); i++)
      {
        QBEField field = (QBEField) resultFields.get(i);

        // consider empty fields
        if (field.isKeepEmpty())
          continue;

        resultFieldNames.add(field.getTableField().getDBName());
      }
      
      String[] attributeNames = new String[resultFieldNames.size()];
      attributeNames = (String[]) resultFieldNames.toArray(attributeNames);
        
      // Release search request
      //
      DataSearchResult result = new DataSearchResult(callback == null);

      int maxRecords;
      if (callback != null)
      {
        maxRecords = Integer.MAX_VALUE;
      }
      else
      {
        maxRecords = recordSet.getMaxRecords();
        if (maxRecords == DataRecordSet.DEFAULT_MAX_RECORDS)
          maxRecords = Property.BROWSER_SYSTEM_MAX_RECORDS.getIntValue();
        if (maxRecords == DataRecordSet.UNLIMITED_RECORDS)
          maxRecords = Integer.MAX_VALUE;
      }
      
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(this.searchScope);
      constraints.setReturningAttributes(attributeNames);
      
      // do not set count limit if filtering is activated!
      if (maxRecords != Integer.MAX_VALUE && !(eventHandler != null && eventHandler.isFilterSearchAction()))
        // set limit to one record more to avoid an javax.naming.SizeLimitExceededException then checking results.hasMore()
        constraints.setCountLimit(maxRecords + 1);

      NamingEnumeration results = context.search(searchBase, searchFilter, constraints);

      // Process search result
      //
      int[] primaryKeyIndices = spec.getPrimaryKeyIndices();
      boolean hasMore = results.hasMore();
      while (hasMore && result.getRecordCount() < maxRecords)
      {
        javax.naming.directory.SearchResult si = (javax.naming.directory.SearchResult) results.next();
        Attributes attrs = si.getAttributes();

        Object[] values = new Object[resultFields.size()];
        //int j = 1;
        for (int i = 0; i < values.length; i++)
        {
          QBEField field = (QBEField) resultFields.get(i);
          if (field.isKeepEmpty())
          {
            values[i] = null;
          }
          else
          {
            //                  values[i] =
            // field.getTableField().getType().convertSQLValueToDataValue(this,
            // rs, j++);
            
            
            boolean multiValue = true;
            String attrName = field.getTableField().getDBName();
//            boolean multiValue = false;
//            if (attrName.endsWith(".."))
//            {
//              multiValue = true;
//              attrName = attrName.substring(0, attrName.length() - 2);
//            }
            Attribute attr = attrs.get(attrName);
            if (attr != null && attr.size() > 0)
            {
              if (multiValue && attr.size() > 1)
              {
                // handle multi value attributes
                //
                StringBuffer buffer = new StringBuffer(64);
                for (int n = 0; n < attr.size(); n++)
                {
                  if (n != 0)
                    buffer.append("|");
                  buffer.append(attr.get(n));
                }
                values[i] = buffer.toString();
              }
              else
              {
                values[i] = attr.get(0);
              }
            }
          }
        }

        DataRecord record = instantiateRecord(recordSet, eventHandler, primaryKeyIndices, values, result.getRecordCount());

        // record filtered or not?
        if (record != null)
        {
          if (callback != null && !callback.onNextRecord(record))
            maxRecords = 0;
          else
            result.add(record);
        }

        // check for more records
        hasMore = results.hasMore();
      }
      result.setHasMore(hasMore);
      return result;
    }
    finally
    {
      context.close();
    }
  }

  protected IDataSearchResult search(DataRecordSet recordSet, QBESpecification spec, IDataSearchIterateCallback callback, DataTableRecordEventHandler eventHandler) throws InvalidExpressionException
  {
		String searchFilter = buildSearchFilter(spec);
		
		try
		{
			return executeQuery(recordSet, spec, searchFilter, callback, eventHandler);
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			// convert exception
			throw new RuntimeException(e);
		}
  }

  protected long count(DataRecordSet recordSet, QBESpecification spec) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }

  protected DataExecutionContext newExecutionContext(DataTransaction transaction) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  protected void executeInternal(DataExecutionContext context, TAInsertRecordAction action) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  protected void executeInternal(DataExecutionContext context, TAUpdateRecordAction action) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  protected void executeInternal(DataExecutionContext context, TADeleteRecordAction action) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  protected void executeInternal(DataExecutionContext context, TADeleteRecordsAction action) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  public long newJacobIds(ITableDefinition table, ITableField field, int increment) throws Exception
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean setNextJacobId(ITableDefinition table, ITableField field, long nextId) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  public boolean supportsAutoKeyGeneration()
  {
    return false;
  }

  public boolean supportsSorting()
  {
    return true;
  }
}
