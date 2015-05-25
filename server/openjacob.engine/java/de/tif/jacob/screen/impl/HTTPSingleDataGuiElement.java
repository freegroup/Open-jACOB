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

import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ISingleDataGuiElement;

/**
 *
 */
public interface HTTPSingleDataGuiElement extends ISingleDataGuiElement
{
  	public DataField getDataField();
    /**
     * @return Returns the isEditable.
     */
    public boolean isEditable();

    /**
     * @param isEditable The isEditable to set.
     */
    public void setEditable(boolean isEditable);
    
    public IApplication getApplication();
    
    public int getDisplayRecordIndex();
    
    /**
     * Returns the parent group of this UI element. This can be a TabPane if
     * the element inside a TabPane group.
     * 
     * @see #getOuterGroup
     * @since 2.8.1
     * @return the parent group of this UI element
     */
    public IGroup getGroup();

    /**
     * Returns the outer HTTPGroup element of this UI element. This is always the
     * top most group. It is never an TabPane-Group if the UI element is a child 
     * of a TabPane. 
     * 
     * @see #getGroup
     * @since 2.8.1
     * @return the top most group (parent) of this UI element 
     */
    public IGroup getOuterGroup();
}
