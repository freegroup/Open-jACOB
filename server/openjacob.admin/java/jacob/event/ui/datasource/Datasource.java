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

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;

/**
 *
 * @author andreas
 */
public final class Datasource extends IGroupEventHandler
{
  static public final transient String RCS_ID = "$Id: Datasource.java,v 1.3 2010/07/13 17:57:09 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
  {
    if (status == IGuiElement.NEW || status == IGuiElement.UPDATE)
    {
      IDataTableRecord record = context.getSelectedRecord();

      boolean jacobConnectionHandling = de.tif.jacob.core.model.Datasource.location_ENUM._jACOB.equals(record.getStringValue(de.tif.jacob.core.model.Datasource.location));
      boolean webserverHandling = de.tif.jacob.core.model.Datasource.location_ENUM._Webserver.equals(record.getStringValue(de.tif.jacob.core.model.Datasource.location));
      boolean configurePool = de.tif.jacob.core.model.Datasource.configurePool_ENUM._Yes.equals(record.getStringValue(de.tif.jacob.core.model.Datasource.configurepool));
      boolean lucene = de.tif.jacob.core.model.Datasource.rdbType_ENUM._Lucene.equals(record.getStringValue(de.tif.jacob.core.model.Datasource.rdbtype));

      boolean bool = jacobConnectionHandling && configurePool;
      DatasourceConfigurePool.setPoolFieldsStatus(context, bool, bool);
      DatasourceConfigurePool.setConnectionFieldsStatus(context, jacobConnectionHandling, jacobConnectionHandling, lucene);
      DatasourceLocation.setJndiFieldsStatus(context, webserverHandling, webserverHandling);
    }
    else
    {
      DatasourceConfigurePool.setPoolFieldsStatus(context, true, status != IGuiElement.SELECTED);
      DatasourceConfigurePool.setConnectionFieldsStatus(context, true, status != IGuiElement.SELECTED, false);
      DatasourceLocation.setJndiFieldsStatus(context, true, status != IGuiElement.SELECTED);
    }
  }
}
