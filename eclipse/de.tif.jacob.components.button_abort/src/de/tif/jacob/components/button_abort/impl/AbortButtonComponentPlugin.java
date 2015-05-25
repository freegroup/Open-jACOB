/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.components.button_abort.impl;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.ImageDescriptor;

import de.tif.jacob.components.button_abort.AbortButton;
import de.tif.jacob.components.button_abort.PluginId;
import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.components.plugin.PluginComponentManager.Group;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.IButtonBarElementModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIButtonBarModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

public class AbortButtonComponentPlugin extends IComponentPlugin implements IButtonBarElementModel
{
  private final static Version version = new Version(2,10,0);

  public AbortButtonComponentPlugin()
  {
  }

  public String getName()
  {
    return "Abort";
  }

  public  Group getType()
  {
    return Group.button;
  }
  
  public ImageDescriptor getPaletteImage()
  {
    return Activator.getDefault().getImageDescriptor("toolbar_button.png");
  }

  public String getJavaImplClass()
  {
    return AbortButton.class.getName();
  }

  public Class getFigureClass()
  {
    return AbortButtonFigure.class;
  }

  @Override
  public String getPluginId()
  {
    return PluginId.ID;
  }

  @Override
  public Class getEventHandlerTemplateClass()
  {
    return IActionButtonEventHandler.class;
  }

  @Override
  public Version getRequiredJacobVersion()
  {
    return version;
  }

  @Override
  public String getPluginVersion()
  {
    return (String)Activator.getDefault().getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    // TODO Auto-generated method stub
    
  }

  public void createMissingI18NKey()
  {
    // nothing to do
    
  }

  public UIButtonBarModel getButtonBarModel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public String getDefaultName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public String getHookClassName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public void setJacobModel(JacobModel jacobModel)
  {
    // TODO Auto-generated method stub
    
  }

  public CastorGuiElement getCastor()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isI18NKeyInUse(String key)
  {
    return false;
  }

  public void renameEventHandler(String fromClass, String toClass)
  {
    // TODO Auto-generated method stub
    
  }

  public void renameFieldReference(FieldModel field, String fromName, String toName)
  {
  }

  public void renameI18NKey(String fromName, String toName)
  {
  }

  public void renameRelationReference(String from, String to)
  {
  }

  public void renameRelationsetReference(String from, String to)
  {
  }

  public void setButtonBarModel(UIButtonBarModel object)
  {
  }

  public void setGroup(UIGroupModel groupModel)
  {
  }

  public void setLocation(Point point)
  {
  }

  public void setName(String newName) throws Exception
  {
  }

  public void setSize(Dimension dimension)
  {
  }
  
}
