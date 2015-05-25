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
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.ObjectModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class BrowserColumnSelectionDialog extends ElementListSelectionDialog
{
  /**
   * @param parent
   * @param renderer
   */
  public BrowserColumnSelectionDialog(Shell parent, BrowserModel browser)
  {
    super(parent, new LabelProvider()
    {
      public Image getImage(Object element)
      {
        return JacobDesigner.getImage(((ObjectModel) element).getImageName());
      }

      public String getText(Object element)
      {
        return ((FieldModel) element).getName();
      }
    });
    setTitle("Browser column");
    setMessage("Select a table field to add them to the UI browser");
    setMultipleSelection(true);

    List elements = new ArrayList();
    Iterator iter = browser.getTableAliasModel().getTableModel().getFieldModels().iterator();
    while(iter.hasNext())
    {
      FieldModel field = (FieldModel)iter.next();
      if(field.getType()!= FieldModel.DBTYPE_LONGTEXT && field.getType()!= FieldModel.DBTYPE_BINARY)
        elements.add(field);
    }
    setElements(elements.toArray());
  }
}
