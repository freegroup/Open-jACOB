/*
 * Created on 09.10.2007
 *
 */
package de.tif.jacob.designer.model;

import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType;

public class UIVerticalButtonBarModel extends UIButtonBarModel
{

  public UIVerticalButtonBarModel()
  {
    super();
    this.getCastorContainer().setOrientation(FlowLayoutContainerOrientationType.VERTICAL);
  }

  public UIVerticalButtonBarModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
    this.getCastorContainer().setOrientation(FlowLayoutContainerOrientationType.VERTICAL);
  }
  
}
