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
public final class DatasourceConfigurePool extends IComboBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: DatasourceConfigurePool.java,v 1.3 2010/07/13 17:57:09 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IComboBoxEventHandler#onSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IComboBox)
   */
  public void onSelect(IClientContext context, IComboBox comboBox) throws Exception
  {
    if (context.getGroup().getDataStatus() == IGuiElement.NEW || context.getGroup().getDataStatus() == IGuiElement.UPDATE)
    {
      boolean configurePool = "Yes".equals(comboBox.getValue());

      setPoolFieldsStatus(context, configurePool, configurePool);
      if (configurePool)
      {
        initValidationQuery(context);

        // initialise other pool fields
        //
        ISingleDataGuiElement datasourceMaxActiveCons = (ISingleDataGuiElement) context.getGroup().findByName("datasourceMaxActiveCons");
        datasourceMaxActiveCons.setValue("8");

        ISingleDataGuiElement datasourceMaxIdleCons = (ISingleDataGuiElement) context.getGroup().findByName("datasourceMaxIdleCons");
        datasourceMaxIdleCons.setValue("3");

        ISingleDataGuiElement datasourceMaxConnectionWait = (ISingleDataGuiElement) context.getGroup().findByName("datasourceMaxConnectionWait");
        datasourceMaxConnectionWait.setValue("5000");
      }
    }
  }

  public static void initValidationQuery(IClientContext context) throws Exception
  {
    ISingleDataGuiElement datasourceConfigurePool = (ISingleDataGuiElement) context.getGroup().findByName("datasourceConfigurePool");
    ISingleDataGuiElement datasourceValidationQuery = (ISingleDataGuiElement) context.getGroup().findByName("datasourceValidationQuery");
    if (datasourceConfigurePool.isEnabled() && "Yes".equals(datasourceConfigurePool.getValue()))
    {
      String rdbType = ((ISingleDataGuiElement) context.getGroup().findByName("datasourceRdbType")).getValue();
      if (Datasource.rdbType_ENUM._Oracle.equals(rdbType) || Datasource.rdbType_ENUM._Oracle9.equals(rdbType))
        datasourceValidationQuery.setValue("select sysdate from dual");
      else if (Datasource.rdbType_ENUM._MSSQL.equals(rdbType))
        datasourceValidationQuery.setValue("select count(*) from dbo.sysusers");
      else if (Datasource.rdbType_ENUM._MySQL.equals(rdbType))
        datasourceValidationQuery.setValue("show variables like 'have_%'");
      else if (Datasource.rdbType_ENUM._HSQL.equals(rdbType))
        datasourceValidationQuery.setValue("");
    }
  }

  public static void setConnectionFieldsStatus(IClientContext context, boolean enable, boolean editable, boolean lucene) throws Exception
  {
    ISingleDataGuiElement datasourceConnectString = (ISingleDataGuiElement) context.getGroup().findByName("datasourceConnectString");
    datasourceConnectString.setEnable(enable);
    datasourceConnectString.setRequired(editable);
    
    if (lucene)
      editable = enable = false;

    ISingleDataGuiElement datasourceUserName = (ISingleDataGuiElement) context.getGroup().findByName("datasourceUserName");
    datasourceUserName.setEnable(enable);
    datasourceUserName.setRequired(editable);

    ISingleDataGuiElement datasourcePassword = (ISingleDataGuiElement) context.getGroup().findByName("datasourcePassword");
    datasourcePassword.setEnable(enable);
    // password should not be required

    ISingleDataGuiElement datasourceJdbcDriverClass = (ISingleDataGuiElement) context.getGroup().findByName("datasourceJdbcDriverClass");
    datasourceJdbcDriverClass.setEnable(enable);
    datasourceJdbcDriverClass.setRequired(editable);

    ISingleDataGuiElement datasourceConfigurePool = (ISingleDataGuiElement) context.getGroup().findByName("datasourceConfigurePool");
    datasourceConfigurePool.setEnable(enable);
    datasourceConfigurePool.setRequired(editable);
  }

  public static void setPoolFieldsStatus(IClientContext context, boolean enable, boolean editable) throws Exception
  {
    ISingleDataGuiElement datasourceMaxActiveCons = (ISingleDataGuiElement) context.getGroup().findByName("datasourceMaxActiveCons");
    datasourceMaxActiveCons.setEnable(enable);
    datasourceMaxActiveCons.setRequired(editable);

    ISingleDataGuiElement datasourceMaxIdleCons = (ISingleDataGuiElement) context.getGroup().findByName("datasourceMaxIdleCons");
    datasourceMaxIdleCons.setEnable(enable);
    datasourceMaxIdleCons.setRequired(editable);

    ISingleDataGuiElement datasourceValidationQuery = (ISingleDataGuiElement) context.getGroup().findByName("datasourceValidationQuery");
    datasourceValidationQuery.setEnable(enable);
    // validation query should not be required

    ISingleDataGuiElement datasourceMaxConnectionWait = (ISingleDataGuiElement) context.getGroup().findByName("datasourceMaxConnectionWait");
    datasourceMaxConnectionWait.setEnable(enable);
    datasourceMaxConnectionWait.setRequired(editable);
  }
}
