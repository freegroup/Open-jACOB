/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import de.tif.jacob.rule.Constants;
import de.tif.jacob.rule.util.BentCornerFigure;

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
    setFont(Constants.FONT_NORMAL);
    setBackgroundColor(Constants.COLOR_RULE_ANNOTATION);
//    setBackgroundColor(Constants.COLOR_RULE_ANNOTATION);
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