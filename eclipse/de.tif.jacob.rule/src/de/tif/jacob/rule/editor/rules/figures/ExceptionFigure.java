package de.tif.jacob.rule.editor.rules.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.rule.Constants;

public class ExceptionFigure  extends IconBasedBusinessObjectFigure
{
	public ExceptionFigure()
	{
	  super("Abort the Rule", "message_exception.png");
	}

}
