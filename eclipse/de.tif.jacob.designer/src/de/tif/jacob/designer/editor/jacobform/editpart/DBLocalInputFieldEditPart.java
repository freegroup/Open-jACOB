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
 * Created on 31.12.2004
 *
 */
package de.tif.jacob.designer.editor.jacobform.editpart;

import de.tif.jacob.designer.actions.ShowTableFieldEditorAction;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;
import de.tif.jacob.designer.model.UIDBLocalInputFieldModel;

/**
 *
 */
public class DBLocalInputFieldEditPart extends GroupDBElementEditPart implements TreeSelectionObjectModelProvider
{
  public DBLocalInputFieldEditPart(Class figureClass)
  {
    super(figureClass);
  }
  
  public FieldModel getFieldModel()
  {
    return getUIDBLocalInputFieldModel().getFieldModel();
  }
  
  /**
   * Liefert das Objekt Model zurück welches im ApplicationOutline selektiert werden
   * soll, wenn dieses Element selektiert wird (sync-Mode)
   * 
   * Wenn ein eingabefeld selektiert wird, dann wird das entsprechende Feld in der Tabelle im
   * ApplikationOutline angezeigt..
   */
  public ObjectModel getTreeObjectModel()
  {
    return getFieldModel();
  }

  public UIDBLocalInputFieldModel getUIDBLocalInputFieldModel()
  {
    return (UIDBLocalInputFieldModel)getModel();
  }
  
  public void refreshVisuals()
	{
		super.refreshVisuals();
		getObjectFigure().setText(getFormElementModel().getName()+"<"+getFieldModel().getType()+">");
	}

  public void openDBEditor()
  {
    // reuse the object selection contribution to use a common method to oopen
    // the table editor.
    //
    final FieldModel model = getFieldModel(); 
    new ShowTableFieldEditorAction()
    {
      public FieldModel getFieldModel() {return model; }
    }.run(null);
  }
}
