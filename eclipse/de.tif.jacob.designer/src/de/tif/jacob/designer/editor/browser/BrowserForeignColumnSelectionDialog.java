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
 * Created on 09.12.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.browser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserFieldForeignModel;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class BrowserForeignColumnSelectionDialog extends ElementListSelectionDialog
{
  
  /**
   * @param parent
   * @param renderer
   */
  public BrowserForeignColumnSelectionDialog(Shell parent,final BrowserModel browser)
  {
    super(parent, new LabelProvider()
    {
      List<TableAliasModel> relatedAliases;
      
      public Image getImage(Object element)
      {
        if(relatedAliases==null)
        {
          relatedAliases = new ArrayList<TableAliasModel>();
          
          List relations = browser.getJacobModel().getRelationModelsTo(browser.getTableAliasModel());
          Iterator iter = relations.iterator();
          while (iter.hasNext())
          {
            RelationModel relation = (RelationModel) iter.next();
            // Falls es eine Relation zu dieser Tabelle existiert, kann man den
            // Alias eventuell hinzufügen
//       immer erlauben      
            if(!relatedAliases.contains(relation.getFromTableAlias()))
            {
              relatedAliases.add(relation.getFromTableAlias());
            }
          }
        }
        if(relatedAliases.contains(element))
          return JacobDesigner.getImage("link.png");
        else
          return null;
      }

      public String getText(Object element)
      {
        return ((TableAliasModel) element).getName();
      }
    });

    setTitle("Browser foreign column");
    setMessage("Select a table alias to add them to the browser");
    setMultipleSelection(true);
    setElements(browser.getJacobModel().getTableAliasModels().toArray());
//    setElements(relatedAliases.toArray());
  }
}
