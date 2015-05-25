package de.tif.jacob.rule.editor.rules.figures.decision;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.rule.Constants;
import de.tif.jacob.rule.editor.rules.figures.OnTrueFigure;


public class EnumValueFigure extends Figure 
{
	public EnumValueFigure(String s ) 
	{
    ToolbarLayout layout = new ToolbarLayout(true);
    layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
    Label label = new Label(s);
    label.setFont(Constants.FONT_SMALL);
//    label.setBorder(new LineBorder(1));
		add(label);
    add(new OnTrueFigure());
	}
  
  public void setBounds(Rectangle rect) 
  {
    rect.setSize(getPreferredSize());
    super.setBounds(rect);
  }
}
