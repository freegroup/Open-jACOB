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

package de.tif.jacob.screen.impl;

import java.util.List;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;

/**
 *
 */
public interface HTTPDomain extends IDomain
{
  public boolean hasChildInDataStatus(IClientContext context, IGuiElement.GroupState state) throws Exception;
  public DataField[] getDataFields();
  

  public INavigationEntry[] getNavigationEntries(IClientContext context) throws Exception;
  public boolean            isCurrentNavigationEntry(INavigationEntry entry);
  public boolean            setCurrentNavigationEntry(INavigationEntry entry);

  public void onHide(IClientContext context) throws Exception;
  public void onShow(IClientContext context) throws Exception;
  
  /**
   * Returns the data accessor for this domain.
   * 
   * @return the data accessor.
   */
  public IDataAccessor getDataAccessor();
  
  public void addFormGroup(IFormGroup formGroup);
  public List getFormGroups();
  
  /**
   * Return [true] if the navigation entry of the domain can collapse / expand. Default is [true]
   * @return
   */
  public boolean getCanCollapse();
  
  public DataScope getDataScope();
}
