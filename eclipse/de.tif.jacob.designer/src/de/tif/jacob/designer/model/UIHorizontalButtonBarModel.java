/*
 * Created on 09.10.2007
 *
 */
package de.tif.jacob.designer.model;

import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType;

public class UIHorizontalButtonBarModel extends UIButtonBarModel
{

  public UIHorizontalButtonBarModel()
  {
    super();
    this.getCastorContainer().setOrientation(FlowLayoutContainerOrientationType.HORIZONTAL);
  }

  public UIHorizontalButtonBarModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
    this.getCastorContainer().setOrientation(FlowLayoutContainerOrientationType.HORIZONTAL);
  }
  
}
