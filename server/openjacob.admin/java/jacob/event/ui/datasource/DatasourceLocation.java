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
public final class DatasourceLocation extends IComboBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: DatasourceLocation.java,v 1.2 2010/07/13 17:57:09 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public static IComboBox get(IClientContext context)
  {
    return (IComboBox) context.getForm().findByName("datasourceLocation");
  }

  public void onSelect(IClientContext context, IComboBox comboBox) throws Exception
  {
    if (context.getGroup().getDataStatus() == IGuiElement.NEW || context.getGroup().getDataStatus() == IGuiElement.UPDATE)
    {
      updateDatasourceFieldsStatus(context);
    }
  }

  public static void updateDatasourceFieldsStatus(IClientContext context) throws Exception
  {
    IComboBox locationCombobox = get(context);
    
    boolean iswebserver = Datasource.location_ENUM._Webserver.equals(locationCombobox.getValue());
    setJndiFieldsStatus(context, iswebserver, iswebserver);

    boolean jacobConnectionHandling = Datasource.location_ENUM._jACOB.equals(locationCombobox.getValue());
    boolean lucene = de.tif.jacob.core.model.Datasource.rdbType_ENUM._Lucene.equals(DatasourceRdbType.get(context).getValue());
    boolean configurePool = jacobConnectionHandling && !lucene
        && Datasource.configurePool_ENUM._Yes.equals(((ISingleDataGuiElement) context.getGroup().findByName("datasourceConfigurePool")).getValue());
    DatasourceConfigurePool.setPoolFieldsStatus(context, configurePool, configurePool);
    DatasourceConfigurePool.setConnectionFieldsStatus(context, jacobConnectionHandling, jacobConnectionHandling, lucene);
  }

  public static void setJndiFieldsStatus(IClientContext context, boolean enable, boolean editable) throws Exception
  {
    ISingleDataGuiElement datasourceJndiName = (ISingleDataGuiElement) context.getGroup().findByName("datasourceJndiName");
    datasourceJndiName.setEnable(enable);
    datasourceJndiName.setRequired(editable);
  }
}
