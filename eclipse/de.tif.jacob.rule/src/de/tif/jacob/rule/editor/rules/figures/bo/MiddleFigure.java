/*
 * Created on 22.02.2005
 *
 */
package de.tif.jacob.rule.editor.rules.figures.bo;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import de.tif.jacob.rule.Constants;


/**
 *
 */
public class MiddleFigure extends Figure
{
	Insets INSETS = new Insets(2,8,2,8);
  
  IFigure left;
  IFigure center;
  IFigure right;
  
  Label   function;
  RectangleFigure out;
  
  public MiddleFigure(String name)
  {
    function = new Label("");
    function.setForegroundColor(Constants.COLOR_FONT);
    function.setBackgroundColor(Constants.COLOR_BUSINESS_IN);
    function.setOpaque(true);
    function.setFont(Constants.FONT_NORMAL);
    function.setBorder(new LineBorder(1){
		
			public Insets getInsets(IFigure figure) {
				return INSETS;
			}
		});
    
    left = new Figure();
    left.setOpaque(false);
    left.setLayoutManager(new BorderLayout());
    left.add(function,BorderLayout.CENTER);
    
    center = new RectangleFigure();
    center.setOpaque(false);
    
    out = new RectangleFigure();
    out.setBackgroundColor(Constants.COLOR_DECISION_TRUE);
    out.setPreferredSize(new Dimension(10,10));
    out.setMaximumSize(new Dimension(10,10));
    out.setForegroundColor(Constants.COLOR_FONT);
    out.setBackgroundColor(Constants.COLOR_BUSINESS_OUT);

    right = new Figure();
    right.setOpaque(false);
    right.setLayoutManager(new BorderLayout());
    right.add(out,BorderLayout.CENTER);


    BorderLayout layout = new BorderLayout();
    setLayoutManager(layout);
    add(left,BorderLayout.LEFT);
    add(center,BorderLayout.CENTER);
    add(right,BorderLayout.RIGHT);
  }
  
  public void setText(String text)
  {
  	function.setText(text);
  }
  
  public IFigure getInFigure()
  {
  	return function;
  }
  
  public IFigure getOutFigure()
  {
  	return out;
  }
}
