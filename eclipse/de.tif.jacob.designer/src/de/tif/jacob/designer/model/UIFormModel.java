/*
 * Created on 14.06.2007
 *
 */
package de.tif.jacob.designer.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.util.StringUtil;

public abstract class UIFormModel extends ObjectWithPropertyModel
{
  
  public UIFormModel()
  {
    super();
  }

  public UIFormModel(JacobModel jacobModel)
  {
    super(jacobModel);
  }

  public abstract void hasUnlinkedTo(UIDomainModel model);

  public abstract void hasLinkedTo(UIDomainModel model);

  public abstract void hasLinkedTo(UIFormGroupModel model);

  public abstract void hasUnlinkedTo(UIFormGroupModel model);

  public abstract String getName();

  public abstract void setName(String name) throws Exception;
  
  public abstract String getLabel();
  
  public abstract void setLabel(String name) throws Exception;

  public final String suggestI18NKey()
  {
    return "FORM" + getJacobModel().getSeparator() + getName().toUpperCase();
  }

  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,  "Name", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length+1] = new TextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
    
    return descriptors;
  }
  
  public void setPropertyValue(Object propName, Object val)
  {
    try 
    {
      if (propName == ID_PROPERTY_NAME)
        setName((String) val);
      else if (propName ==ID_PROPERTY_LABEL)
        setLabel((String) val);
      else
        super.setPropertyValue(propName, val);
    } 
    catch (Exception e) 
    {
      JacobDesigner.showException(e);
    }
  }
  
  public Object getPropertyValue(Object propName)
  {
    if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
      return getName();
    if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_LABEL))
      return getLabel();
    return super.getPropertyValue(propName);
  }
  


  public String getExtendedDescriptionLabel()
  {
    return this.getName();
  }
  
  
  @Override
  public ObjectModel getParent()
  {
    return getJacobModel().getApplicationModel();
  }
}
