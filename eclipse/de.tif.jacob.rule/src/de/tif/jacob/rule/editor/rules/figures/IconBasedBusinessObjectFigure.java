package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.rule.Constants;
import de.tif.jacob.rule.RulePlugin;
import de.tif.jacob.rule.util.GradientLabel;

public class IconBasedBusinessObjectFigure  extends AbstractBusinessObjectFigure
{
  Insets INSETS = new Insets(0,8,0,0);

  final IFigure fig;
  public IconBasedBusinessObjectFigure(String labelString, String iconString)
	{
  	ToolbarLayout layout = new ToolbarLayout(false);
  	layout.setStretchMinorAxis(true);
    setLayoutManager(layout);
    
    fig = new GradientLabel(labelString);
    fig.setFont(Constants.FONT_NORMAL);
    
    Label   icon = new Label();
    icon.setBorder(new MarginBorder(INSETS));
		icon.setIcon(RulePlugin.getImage(iconString));

    add(fig);
    add(icon);
	}
  
  public Dimension getPreferredSize(int wHint, int hHint) 
  {
  	Dimension dim = super.getPreferredSize(wHint,hHint).getCopy();
  	dim.width = fig.getPreferredSize().width;
		dim.width = ((dim.width+Constants.RULE_GRID_WIDTH)/Constants.RULE_GRID_WIDTH)*Constants.RULE_GRID_WIDTH;
  	return dim;
  }

	public void setBounds(Rectangle rect) 
  {
		Dimension dim = getPreferredSize().getCopy();
		rect.setSize(dim);
		super.setBounds(rect);
	}
}
