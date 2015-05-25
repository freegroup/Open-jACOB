/*
 * Created on 22.02.2005
 *
 */
package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import de.tif.jacob.rule.Constants;

/**
 *
 */
public class OnTrueFigure extends BooleanFigure
{
  public OnTrueFigure()
  {
    setBackgroundColor(Constants.COLOR_DECISION_TRUE);
    setForegroundColor(Constants.COLOR_DECISION_TRUE_BORDER);
  }
  
}


