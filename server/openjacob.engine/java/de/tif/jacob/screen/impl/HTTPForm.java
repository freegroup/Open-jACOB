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

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;

/**
 *
 */
public interface HTTPForm extends IForm, HTTPNavigationEntryProvider
{
  public void onHide(IClientContext context) throws Exception;
  public void onShow(IClientContext context) throws Exception;

  /**
   * Returns the data accessor for this form.
   * 
   * @return the data accessor.
   */
  public IDataAccessor getDataAccessor();

  // the call is only valid if the DataAccessor ExecuteScope equals "form"
  public DataField[] getDataFields();
  public boolean hasVisibleSearchBrowser();
  
  public DataScope getDataScope();
  /**
   * @since 2.8.0
   * @param child
   */
  public void addChild(IGuiElement child);
  
}
