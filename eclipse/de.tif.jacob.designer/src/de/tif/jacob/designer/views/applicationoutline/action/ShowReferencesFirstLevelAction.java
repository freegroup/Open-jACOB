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
 * Created on 10.01.2005
 *
 */
package de.tif.jacob.designer.views.applicationoutline.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.ui.IActionDelegate;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIMutableFormModel;
import de.tif.jacob.designer.views.applicationoutline.TreeFormObject;
import de.tif.jacob.designer.views.applicationoutline.TreeJacobFormObject;
import de.tif.jacob.designer.views.applicationoutline.TreeObject;
import de.tif.jacob.designer.views.search.ReferenceSearchQueryFirstLevel;

/**
 *
 */
public final class ShowReferencesFirstLevelAction extends de.tif.jacob.designer.actions.ShowReferencesAction
{
  ObjectModel model;

  
	@Override
  public ISearchQuery createQuery()
  {
    return new ReferenceSearchQueryFirstLevel(model);
  }


  /**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
      model =((TreeObject)((IStructuredSelection)selection).getFirstElement()).getModel();
    else 
      model=null;
	}

}
