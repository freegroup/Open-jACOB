package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import de.tif.jacob.rule.editor.rules.dialogs.commonBo.SelectBusinessMethodDialog;
import de.tif.jacob.rule.editor.rules.figures.BusinessObjectFigure;
import de.tif.jacob.rule.editor.rules.model.BusinessObjectModel;


public class BusinessObjectModelEditPart extends RuleModelEditPart
{
  public BusinessObjectModelEditPart()
  {
  }
  
  protected BusinessObjectModel getBusinessObjectModel()
  {
    return (BusinessObjectModel)getModel();
  }
  
  /**
   * Return a IFigure depending on the instance of the current model element.
   * This allows this EditPart to be used for both sublasses of Shape.
   */
  public IFigure createFigureForModel()
  {
      return new BusinessObjectFigure(((BusinessObjectModel)getModel()).getId());
  }


	public void performRequest(Request request)
	{
	  if(request.getType()== RequestConstants.REQ_OPEN)
    {
      org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();   
      new SelectBusinessMethodDialog(display.getActiveShell(),(BusinessObjectModel)getModel()).open();
    }
	  else
	  {
		  super.performRequest(request);
	  }
	}
  
	 protected void refreshVisuals()
	 {
		 BusinessObjectFigure figure = (BusinessObjectFigure)getFigure();
		 figure.setBusinessObjectName( getBusinessObjectModel().getDisplayBusinessObjectClass());
		 figure.setBusinessFunctionName( getBusinessObjectModel().getDisplayBusinessObjectMethod());
		 super.refreshVisuals();
	 }
}

