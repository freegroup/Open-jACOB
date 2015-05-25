/*
 * Created on 23.02.2005
 *
 */
package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import de.tif.jacob.rule.Constants;

/**
 *
 */
public class TransitionFigure extends PolylineConnection
{

  public TransitionFigure()
  {
    PolygonDecoration deco= new PolygonDecoration();
    deco.setScale(7,4);
    setTargetDecoration(deco); // arrow at target
    setConnectionRouter(new ManhattanConnectionRouter());
//    setConnectionRouter(new FanRouter());
    setLineWidth(2);
    setForegroundColor(Constants.COLOR_RULE_TRANSISTION);
    
  }
}
