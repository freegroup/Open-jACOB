/*
 * Created on 07.07.2009
 *
 */
package de.tif.jacob.designer.editor.jacobform.editpart;

import org.eclipse.draw2d.IFigure;

import de.tif.jacob.designer.editor.jacobform.figures.IPropertyConsumerFigure;
import de.tif.jacob.designer.editor.jacobform.figures.IRefreshVisualAdapter;
import de.tif.jacob.designer.model.UIPluginComponentModel;

public class PluginElementEditPart extends GroupElementEditPart
{
  public PluginElementEditPart(Class figureClass)
  {
    super(figureClass);
  }

  @Override
  public void refreshVisuals()
  {
    IFigure figure = getFigure();
    UIPluginComponentModel model =(UIPluginComponentModel) getModel();
    if(figure instanceof IPropertyConsumerFigure)
      ((IPropertyConsumerFigure)figure).setProperties(model.getProperties());
    
    if(figure instanceof IRefreshVisualAdapter)
      ((IRefreshVisualAdapter)figure).refreshVisual(model);
    
    super.refreshVisuals();
  }
}
