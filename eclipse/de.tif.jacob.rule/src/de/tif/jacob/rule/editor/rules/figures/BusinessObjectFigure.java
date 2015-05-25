/*
 * Created on 22.02.2005
 *
 */
package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import de.tif.jacob.rule.Constants;
import de.tif.jacob.rule.editor.rules.figures.bo.MiddleFigure;
import de.tif.jacob.rule.util.GradientLabel;

/**
 *
 */
public class BusinessObjectFigure extends AbstractBusinessObjectFigure
{
  MiddleFigure middle;
  Label        title;
  
  public BusinessObjectFigure(String name)
  {
    ToolbarLayout layout = new ToolbarLayout();
    setLayoutManager(layout);	
    setOpaque(false);
    
    middle = new MiddleFigure(name);
    middle.setOpaque(false);
    
    title  = new GradientLabel("Dies ist Text");
    title.setFont(Constants.FONT_NORMAL);
    title.setLabelAlignment(PositionConstants.LEFT);
    
    add(title);
    add(middle);
  }
  
  public void setBusinessObjectName(String name)
  {
  	title.setText(name);
  }
  
  public void setBusinessFunctionName(String name)
  {
  	middle.setText(name);
  }

	public IFigure getInFigure() 
	{
		return middle.getInFigure();
	}

	public IFigure getOutFigure() 
	{
		return middle.getOutFigure();
	}
}
