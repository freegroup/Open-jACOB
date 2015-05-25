/*
 * Created on 12.02.2009
 * @since 2.8.3
 */
package de.tif.jacob.core.definition.guielements;

import java.util.List;

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

public class StackContainerDefinition extends ContainerDefinition
{
  static public final transient String RCS_ID = "$Id: StackContainerDefinition.java,v 1.3 2010/08/12 07:52:16 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  public StackContainerDefinition(String name, String description, String eventHandler, boolean visible, Dimension position, List panes, int borderWith,  String borderColor, String backgroundColor, CastorResizeMode castorResizeMode) throws Exception
  {
    super(name, description, eventHandler, visible, position, panes,borderWith, borderColor,backgroundColor, castorResizeMode);
  }
  
  public final IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
   return factory.createStackContainer(app, parent,this); 
  }
}
