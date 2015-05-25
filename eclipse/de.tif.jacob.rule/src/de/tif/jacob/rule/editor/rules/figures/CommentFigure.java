/*
 * Created on 04.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.rule.Constants;

public class CommentFigure extends Label
{
  Insets INSETS = new Insets(3,8,3,8);

  public CommentFigure()
  {
    setForegroundColor(Constants.COLOR_FONT);
    setFont(Constants.FONT_NORMAL);
    setOpaque(true);
    setBackgroundColor(Constants.COLOR_WORKAREA);
    setBorder(new LineBorder(Constants.COLOR_BORDER_BRIGHT){
    
      public Insets getInsets(IFigure figure)
      {
        return INSETS;
      }
    
    });
    setTextAlignment(PositionConstants.CENTER);
  }

}
