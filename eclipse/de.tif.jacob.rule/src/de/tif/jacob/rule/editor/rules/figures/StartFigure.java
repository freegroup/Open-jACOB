/*
 * Created on 22.02.2005
 *
 */
package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import de.tif.jacob.rule.RulePlugin;

/**
 *
 */
public class StartFigure extends Figure
{
  private static Color background = new Color(null,128,255,128);
  
  private final IFigure circle;
  
  public StartFigure()
  {
    setOpaque(false);
    
  	circle = new ImageFigure(RulePlugin.getImage("start_bo.png"));
  	circle.setBounds(new Rectangle(0,0,50,50));
  	
  	ToolbarLayout layout = new ToolbarLayout(false);
//		layout.setStretchMinorAxis(true);
  	layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		
		setLayoutManager(layout);
		
		add(circle);
  }
  

	public IFigure getCircle() {
		return circle;
	}
}
