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
package de.tif.jacob.designer.editor.table;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class TableFieldSelectionDialog extends ElementListSelectionDialog
{
  private final List fields;

  /**
   * @param parent
   * @param renderer
   */
  public TableFieldSelectionDialog(Shell parent, TableModel model)
  {
    super(parent, new LabelProvider()
    {
      public Image getImage(Object element)
      {
        return ((FieldModel) element).getImage();
      }

      public String getText(Object element)
      {
        return ((FieldModel) element).getName();
      }
    });
    setTitle("Table field selection");
    setMessage("Select a table field");
    this.fields = new ArrayList(model.getFieldModels());
  }

  public void filter(String fieldName)
  {
    for (int i = 0; i < this.fields.size(); i++)
    {
      if (((FieldModel) this.fields.get(i)).getName().equals(fieldName))
      {
        this.fields.remove(i);
        return;
      }
    }
  }

  public void filter(String[] fieldNames)
  {
    for (int i = 0; i < fieldNames.length; i++)
    {
      filter(fieldNames[i]);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.window.Window#create()
   */
  public void create()
  {
    // set fields to select
    Object[] elements = new Object[fields.size()];
    for (int i = 0; i < fields.size(); i++)
    {
      elements[i] = fields.get(i);
    }
    setElements(elements);

    super.create();
  }
}
