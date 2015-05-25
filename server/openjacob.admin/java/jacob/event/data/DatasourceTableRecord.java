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
package jacob.event.data;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DatasourceTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: DatasourceTableRecord.java,v 1.2 2010/07/13 17:57:09 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // IBIS: Hack für LDAP Initialisierung
    //
    Context context = Context.getCurrent();
    if (context instanceof IClientContext)
    {
      IClientContext clientContext = (IClientContext) context;
      ISingleDataGuiElement datasourceConnectString = (ISingleDataGuiElement) clientContext.getGroup().findByName("datasourceConnectString");
      if (datasourceConnectString != null)
      {
        String connectString = datasourceConnectString.getValue();
        if (connectString != null && connectString.equalsIgnoreCase("ldap"))
        {
          tableRecord.setStringValue(transaction, Datasource.connectstring, "ldap:jacob@url:ldap://<HOST>;base:dc=democompany,dc=com;scope:<subtree|onelevel|object>;");
          tableRecord.setStringValue(transaction, Datasource.username, "cn=Manager,dc=democompany,dc=com");
          tableRecord.setStringValue(transaction, Datasource.rdbtype, Datasource.rdbType_ENUM._AutoDetect);
          tableRecord.setStringValue(transaction, Datasource.adjustment, Datasource.adjustment_ENUM._jACOB);
          tableRecord.setStringValue(transaction, Datasource.jdbcdriverclass, "com.sun.jndi.ldap.LdapCtxFactory");
          tableRecord.setStringValue(transaction, Datasource.location, Datasource.location_ENUM._jACOB);
        }
      }
    }
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (tableRecord.isNewOrUpdated())
    {
      if (Datasource.rdbType_ENUM._Lucene.equals(tableRecord.getStringValue(Datasource.rdbtype)))
      {
        tableRecord.setValue(transaction, Datasource.configurepool, Datasource.configurePool_ENUM._No);
        tableRecord.setValue(transaction, Datasource.maxconnectionwait, null);
        tableRecord.setValue(transaction, Datasource.maxactivecons, null);
        tableRecord.setValue(transaction, Datasource.maxidlecons, null);
        tableRecord.setValue(transaction, Datasource.validationquery, null);
      }
      else
      {
        // trim validation query
        String validationQuery = tableRecord.getStringValue(Datasource.validationquery);
        if (validationQuery != null)
          tableRecord.setStringValue(transaction, Datasource.validationquery, validationQuery.trim());

        int maxWait = tableRecord.getintValue(Datasource.maxconnectionwait);
        if (maxWait < 0)
          throw new UserException("Max. connection wait must be greater equal 0");

        int maxIdleCons = tableRecord.getintValue(Datasource.maxidlecons);
        if (maxIdleCons < 0)
          throw new UserException("Max. idle connections wait must be greater equal 0");

        int maxActiveCons = tableRecord.getintValue(Datasource.maxactivecons);
        if (maxActiveCons <= 0 && !tableRecord.hasNullValue(Datasource.maxactivecons))
          throw new UserException("Max. active connections wait must be greater 0");
      }
    }
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    if (tableRecord.isDeleted())
    {
      ClusterManager.getProvider().propagateDatasourceChange(tableRecord.getStringValue(Datasource.name));
    }
    else if (tableRecord.isUpdated())
    {
      // destroy already existing datasource to ensure that it is newly initialised
      ClusterManager.getProvider().propagateDatasourceChange(tableRecord.getOldStringValue(Datasource.name));
    }
   }

}
