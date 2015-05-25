/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.screen.impl.html;

import java.awt.Rectangle;
import java.util.Collections;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IContextMenuEntry;

public abstract class ContextMenuEntry  extends ActionEmitter implements IContextMenuEntry
{
  protected ContextMenuEntry(IApplication app, String name, String label, boolean isVisible, Rectangle boundingRect, ActionType action)
  {
    super(app, name, label, isVisible, boundingRect, action, Collections.EMPTY_MAP);
  }
  
	/* 
	 * @see de.tif.jacob.screen.GUIElement#renderHTML(java.io.Writer)
	 */
	public  abstract void calculateHTML(ClientContext c) throws Exception;
}

