package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.LineBorder;

import de.tif.jacob.rule.Constants;

/**
 *
 */
public class EnumDecisionFigure extends DecisionFigure
{
  public EnumDecisionFigure()
  {
    setLayoutManager(new FreeformLayout());
    setBackgroundColor(Constants.COLOR_DECISION);
    setBorder(new LineBorder(Constants.COLOR_BORDER_BRIGHT));
  }
}
