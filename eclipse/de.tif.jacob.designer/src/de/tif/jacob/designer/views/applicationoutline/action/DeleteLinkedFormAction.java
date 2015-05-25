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
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.designer.views.applicationoutline.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import de.tif.jacob.designer.views.applicationoutline.TreeDomainObject;
import de.tif.jacob.designer.views.applicationoutline.TreeLinkedFormObject;

/**
 *
 */
public final class DeleteLinkedFormAction extends Action
{
  Tree tree;
  
  public DeleteLinkedFormAction(Tree tree)
  {
    super("Delete");
    this.tree = tree;
  }
  
  /**
   * 
   * @param obj
   */
  public void run()
  {
    TreeItem[] items = tree.getSelection();
    if(items.length>0)
    {
      for(int i=0;i<items.length;i++)
      {
        // Falls es sich um eine Linked form hadelt, kann diese ohne nachfragen gelöscht werden.
        // Es wird die Referenz gelöscht und nicht die eigentliche Form
        //
	      if(items[i].getData() instanceof TreeLinkedFormObject)
	      {
	        TreeLinkedFormObject linkedForm = (TreeLinkedFormObject)items[i].getData();
	        ((TreeDomainObject)linkedForm.getParent()).getDomainModel().removeElement(linkedForm.getFormModel());
	      }
      }
    }
  }
}
