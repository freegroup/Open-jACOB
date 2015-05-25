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

package de.tif.qes.adf;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.qes.QesLayoutAdjustment;
import de.tif.qes.adf.castor.Position;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class ADFCaption extends Caption
{

	/**
	 * @param caption
	 */
	protected ADFCaption(de.tif.qes.adf.castor.Caption caption, ActionType actionType, Alignment.Horizontal halign, Alignment.Vertical valign, QesLayoutAdjustment layout)
	{
		super(caption.getPosition() == null ? null : new ADFDimension(layout.adjustCaption(caption.getPosition())), caption.getValue(),null, actionType, halign, valign,false, null);
	}
	
	protected ADFCaption(de.tif.qes.adf.castor.Caption caption, Position position, Alignment.Horizontal halign, Alignment.Vertical valign, QesLayoutAdjustment layout)
	{
		super(position == null ? null : new ADFDimension(layout.adjustCaption(position)), caption.getValue(),null, null, halign, valign,false, null);
	}
}
