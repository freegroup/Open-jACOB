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
 * Created on 10.12.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.relationset;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import de.tif.jacob.designer.editor.relationset.editpart.RelationEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.StickyNoteEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.TableAliasEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.TableFieldEditPart;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.RelationsetStickyNoteModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class EditPartFactoryImpl implements EditPartFactory
{
	public EditPart createEditPart(EditPart context, Object model)
	{
		EditPart part = null;
		if (model instanceof RelationsetModel)
			part = new RelationsetEditPart();
		else if (model instanceof TableAliasModel)
			part = new TableAliasEditPart();
		else if (model instanceof RelationModel)
			part = new RelationEditPart();
		else if (model instanceof RelationsetStickyNoteModel)
			part = new StickyNoteEditPart();
		else if (model instanceof FieldModel)
			part = new TableFieldEditPart();
		else
		  return null;
		part.setModel(model);
		return part;
	}
}