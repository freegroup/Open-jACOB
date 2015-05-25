/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
/*
 * Created on 01.07.2005
 *
 */
package de.tif.jacob.designer.editor.domain;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import de.tif.jacob.designer.model.UIDomainModel;


/**
 * InnerClass that acts as a proxy for the ExampleTaskList 
 * providing content for the Table. It implements the ITaskListViewer 
 * interface since it must register changeListeners with the 
 * ExampleTaskList 
 */
class RoleContentProvider implements IStructuredContentProvider
{
  UIDomainModel domain;
  
  /**
   * @param page
   */
  RoleContentProvider(UIDomainModel domain)
  {
    this.domain = domain;
  }

  public void inputChanged(Viewer v, Object oldInput, Object newInput) 
  {
	}

	public void dispose() 
	{
	}

	// Return the tasks as an array of Objects
	public Object[] getElements(Object parent) 
	{
		return domain.getJacobModel().getUserRoleModels().toArray();
	}
}