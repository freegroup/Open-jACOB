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
package de.tif.jacob.designer.editor.relationset.figures;

import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.BentCornerFigure;

public class StickyNoteFigure extends de.tif.jacob.designer.editor.util.StickyNoteFigure
{
  /**
   * Creates a new StickyNoteFigure with a default MarginBorder size of
   * DEFAULT_CORNER_SIZE - 3 and a FlowPage containing a TextFlow with the style
   * WORD_WRAP_SOFT.
   */
  public StickyNoteFigure() 
  {
    super(BentCornerFigure.DEFAULT_CORNER_SIZE - 3);
  }
}
