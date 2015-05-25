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

package jacob.event.ui.datasource;

import jacob.model.Datasource;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 * @author andreas
 */
public final class DatasourceRdbType extends IComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: DatasourceRdbType.java,v 1.5 2010/07/13 17:57:09 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.5 $";
	
	public static IComboBox get(IClientContext context)
  {
    return (IComboBox) context.getForm().findByName("datasourceRdbType");
  }

  public void onSelect(IClientContext context, IComboBox comboBox) throws Exception
  {
    if (context.getGroup().getDataStatus() == IGuiElement.NEW || context.getGroup().getDataStatus() == IGuiElement.UPDATE)
    {
      ISingleDataGuiElement datasourceJdbcDriverClass = (ISingleDataGuiElement) context.getGroup().findByName("datasourceJdbcDriverClass");
      if (datasourceJdbcDriverClass.isEnabled()) // &&
                                                 // StringUtils.isEmpty(datasourceJdbcDriverClass.getValue()))
      {
        if (Datasource.rdbType_ENUM._Oracle.equals(comboBox.getValue()) || Datasource.rdbType_ENUM._Oracle9.equals(comboBox.getValue()))
          datasourceJdbcDriverClass.setValue("oracle.jdbc.OracleDriver");
        else if (Datasource.rdbType_ENUM._MSSQL.equals(comboBox.getValue()))
          // datasourceJdbcDriverClass.setValue("com.microsoft.jdbc.sqlserver.SQLServerDriver"); alter JDBC Treiber für SQL Server 2000
          datasourceJdbcDriverClass.setValue("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        else if (Datasource.rdbType_ENUM._MySQL.equals(comboBox.getValue()))
          datasourceJdbcDriverClass.setValue("com.mysql.jdbc.Driver");
        else if (Datasource.rdbType_ENUM._HSQL.equals(comboBox.getValue()))
          datasourceJdbcDriverClass.setValue("org.hsqldb.jdbcDriver");
        else if (Datasource.rdbType_ENUM._Lucene.equals(comboBox.getValue()))
          datasourceJdbcDriverClass.setValue("");
      }

      ISingleDataGuiElement datasourceUserName = (ISingleDataGuiElement) context.getGroup().findByName("datasourceUserName");
      ISingleDataGuiElement datasourcePassword = (ISingleDataGuiElement) context.getGroup().findByName("datasourcePassword");
      ISingleDataGuiElement datasourceConnectString = (ISingleDataGuiElement) context.getGroup().findByName("datasourceConnectString");
      if (datasourceConnectString.isEnabled()) // &&
                                               // StringUtils.isEmpty(datasourceConnectString.getValue()))
      {
        if (Datasource.rdbType_ENUM._Oracle.equals(comboBox.getValue()) || Datasource.rdbType_ENUM._Oracle9.equals(comboBox.getValue()))
          datasourceConnectString.setValue("jdbc:oracle:thin:@<SERVER>:1521:<SID>");
        else if (Datasource.rdbType_ENUM._MSSQL.equals(comboBox.getValue()))
          // datasourceConnectString.setValue("jdbc:microsoft:sqlserver://<SERVER>:1433;DatabaseName=<DBNAME>;"); alter Connectstring für SQL Server 2000
          datasourceConnectString.setValue("jdbc:sqlserver://<SERVER>:1433;databaseName=<DBNAME>;");
        else if (Datasource.rdbType_ENUM._MySQL.equals(comboBox.getValue()))
          datasourceConnectString.setValue("jdbc:mysql://<SERVER>:3306/<DBNAME>");
        else if (Datasource.rdbType_ENUM._HSQL.equals(comboBox.getValue()))
        {
          datasourceConnectString.setValue("jdbc:hsqldb:file:<FILENAME>");
          
          // initialise with default user and password
          datasourceUserName.setValue("sa");
          datasourcePassword.setValue("");
        }
        else if (Datasource.rdbType_ENUM._Lucene.equals(comboBox.getValue()))
        {
          datasourceConnectString.setValue("index:lucene:fsDirectory:<DIRECTORYNAME>");
          
          datasourceUserName.setValue("");
          datasourcePassword.setValue("");
        }
      }
      
      DatasourceLocation.updateDatasourceFieldsStatus(context);
      DatasourceConfigurePool.initValidationQuery(context);
    }
  }
}
