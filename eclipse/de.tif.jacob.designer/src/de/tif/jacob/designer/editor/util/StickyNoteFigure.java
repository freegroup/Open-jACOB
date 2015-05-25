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
package de.tif.jacob.designer.editor.util;

import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.BentCornerFigure;

/**
 * A Figure with a bent corner and an embedded TextFlow within a FlowPage that
 * contains text.
 */
public class StickyNoteFigure extends BentCornerFigure
{

  /** The inner TextFlow * */
  private TextFlow textFlow;

  /**
   * Creates a new StickyNoteFigure with a default MarginBorder size of
   * DEFAULT_CORNER_SIZE - 3 and a FlowPage containing a TextFlow with the style
   * WORD_WRAP_SOFT.
   */
  public StickyNoteFigure() 
  {
    this(BentCornerFigure.DEFAULT_CORNER_SIZE - 3);
    
  }

  /**
   * Creates a new StickyNoteFigure with a MarginBorder that is the given size
   * and a FlowPage containing a TextFlow with the style WORD_WRAP_SOFT.
   * 
   * @param borderSize
   *          the size of the MarginBorder
   */
  public StickyNoteFigure(int borderSize) 
  {
    setBorder(new MarginBorder(borderSize));
    FlowPage flowPage = new FlowPage();

    textFlow = new TextFlow();

    textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
    textFlow.setOpaque(false);
    flowPage.add(textFlow);

    setLayoutManager(new StackLayout());
    setBackgroundColor(Constants.COLOR_STICKYNOTE);
    add(flowPage);
  }

  /**
   * Returns the text inside the TextFlow.
   * 
   * @return the text flow inside the text.
   */
  public String getText()
  {
    return textFlow.getText();
  }

  /**
   * Sets the text of the TextFlow to the given value.
   * 
   * @param newText
   *          the new text value.
   */
  public void setText(String newText)
  {
    textFlow.setText(newText);
  }

}
