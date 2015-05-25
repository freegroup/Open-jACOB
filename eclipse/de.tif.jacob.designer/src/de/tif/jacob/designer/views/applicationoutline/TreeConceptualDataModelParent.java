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
package de.tif.jacob.designer.views.applicationoutline;

import org.eclipse.jface.viewers.TreeViewer;
import de.tif.jacob.designer.model.JacobModel;

public class TreeConceptualDataModelParent extends TreeParent
{
  public TreeConceptualDataModelParent(TreeViewer viewer, TreeParent parent, JacobModel jacob) 
  {
    super(viewer,parent, jacob, jacob,  "Conceptual Data Model");
  }
  
  public void addChildren()
  {
		TreeParent pdmNode = new TreePhysicalDataModelParent(viewer,this, jacobModel);
		addChild(pdmNode);
		
    TreeParent aliasesNode = new TreeTableAliasParent(viewer,this, jacobModel);
		addChild(aliasesNode);

//		TreeParent relationsNode = new TreeRelationParent(viewer,this, jacobModel);
//		addChild(relationsNode);

		TreeParent relationsetsNode = new TreeRelationsetParent(viewer,this, jacobModel);
		addChild(relationsetsNode);

		TreeBrowserParent commonBrowserNode = new TreeBrowserParent(viewer,this, jacobModel);
    addChild(commonBrowserNode);
  }
}