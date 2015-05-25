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
package de.tif.jacob.designer.editor.jacobform.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.IFontProviderModel;
import de.tif.jacob.designer.model.UIFormElementModel;


public class ChangeFontWeightCommand extends Command
{
	final private IFontProviderModel element;
  final private String newWeight;
	final private String oldWeight;
	
	public ChangeFontWeightCommand(IFontProviderModel element, String bold)
	{
    this.newWeight = bold;
    this.oldWeight = element.getFontWeight();
		this.element = element;		
	}
	
	public void execute()
	{
		element.setFontWeight(newWeight);
	}
	
	public void redo()
	{
		element.setFontWeight(newWeight);
	}
	
	public void undo()
	{
		element.setFontWeight(oldWeight);
	}
}