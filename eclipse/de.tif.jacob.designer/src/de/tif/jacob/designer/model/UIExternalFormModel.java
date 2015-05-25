/*
 * Created on 14.06.2007
 *
 */
package de.tif.jacob.designer.model;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorExternalFormTargetType;
import de.tif.jacob.designer.actions.ShowExternalFormEditorAction;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.exception.InvalidFormNameException;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

public class UIExternalFormModel extends UIFormModel implements IOpenable
{
  private final CastorExternalForm castor;
  
  public UIExternalFormModel(JacobModel jacob, String name)
  {
    super(jacob);
    this.castor = new CastorExternalForm();
    this.castor.setName(name);
    if(getJacobModel().useI18N())
      this.castor.setLabel("%"+suggestI18NKey());
    else
      this.castor.setLabel(StringUtils.capitalise(name));
  }
  
  public UIExternalFormModel(JacobModel jacob, CastorExternalForm form)
  {
    super(jacob);
    this.castor=form;
  }

  
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
      IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
      IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
      for (int i = 0; i < superDescriptors.length; i++)
        descriptors[i] = superDescriptors[i];
      descriptors[superDescriptors.length] = new CheckboxPropertyDescriptor(ID_PROPERTY_VISIBLE, "Visible", PROPERTYGROUP_COMMON);
    
    return descriptors;
  }
  
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_VISIBLE))
      setVisible(((Boolean) val).booleanValue());
    else
      super.setPropertyValue(propName, val);
  }
  

  public Object getPropertyValue(Object propName)
  {
    if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_VISIBLE))
      return new Boolean(getVisible());
    return super.getPropertyValue(propName);
  }
  
  private boolean getVisible()
  {
    return getCastor().getVisible();
  }

  private void setVisible(boolean booleanValue)
  {
    getCastor().setVisible(booleanValue);
  }

  
  public String getLabel()
  {
    return castor.getLabel();
  }
  

  public void setLabel(String newLabel)
  {
    String save = getLabel();
    
    if(StringUtil.saveEquals(save,newLabel))
      return;
    
    castor.setLabel(newLabel);
    firePropertyChange(PROPERTY_LABEL_CHANGED, save, newLabel);
  }
  

  public void setName(String name) throws Exception
  {
    // Den Namen einer form zu ändern ist ein wenig 'tricky'. Alle Referenzen in den Domï¿½nen ( dies sind nur
    // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
    String save = getName();

    // unnöigerweise braucht man den Namen nicht setzten
    //
    if(StringUtil.saveEquals(save,name))
      return;
    
    // prüfen ob der Name bereits vergeben ist. Der Formname muss eindeutig sein
    //
    if(getJacobModel().getFormModel(name)!=null)
      throw new InvalidFormNameException("Form name is already in use. Please use another name.");
    
    // prüfen ob der Name bereits vergeben ist. Der Formname muss eindeutig sein
    //
    if(name.length()==0 || !StringUtils.containsOnly(name,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
      throw new InvalidFormNameException("Invalid Form name. Valid characters are [a-zA-Z_]");
    
    getJacobModel().renameFormReference(save,name);
    castor.setName(name);
    
    // Es müssen alle Referenzen auf diese Form angepasst werden
    //
    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }

  @Override
  public String getName()
  {
    return castor.getName();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  @Override
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  @Override
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  @Override
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  @Override
  void removeCastorProperty(CastorProperty property)
  {
    for(int i=0;i<castor.getPropertyCount();i++)
    {
      if(castor.getProperty(i)==property)
      {
        castor.removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property ["+property.getName()+"] is not part of "+getClass().getName());
  }

  @Override
  public String getError()
  {
    return null;
  }

  @Override
  public String getInfo()
  {
    return null;
  }

  @Override
  public String getWarning()
  {
    return null;
  }

  /**
   * 
   * @return true if the form used in any Domain or false.
   */
  @Override
  public boolean isInUse()
  {
    for(UIDomainModel domain:getJacobModel().getDomainModels())
    {
      // Die Form kann direkt in der Domain sein
      //
      if(domain.getFormModel(getName())==this)
        return true;
      
      // Oder innerhalb einer Gruppe
      //
      for(UIFormGroupModel group:domain.getFormGroupModels())
      {
        if(group.getFormModel(getName())==this)
          return true;
      }
    }
    return false;
  }
  
  /**
   * 
   * @param key the key to check WITH the % at first character
   * @return
   */
  protected boolean isI18NKeyInUse(String key)
  {
    if(getLabel()!=null && getLabel().equals(key))
      return true;
    
    return false;
  }  

  
  public CastorExternalForm getCastor()
  {
    return castor;
  }


  @Override
  public void hasLinkedTo(UIDomainModel domain)
  {
    firePropertyChange(PROPERTY_FORM_LINKED, null, domain);
  }
  
  @Override
  public void hasLinkedTo(UIFormGroupModel formGroup)
  {
    firePropertyChange(PROPERTY_FORM_LINKED, null, formGroup);
  }
  
  @Override
  public void hasUnlinkedTo(UIDomainModel domain)
  {
    firePropertyChange(PROPERTY_FORM_UNLINKED,  domain, null);
  }
  

  @Override
  public void hasUnlinkedTo(UIFormGroupModel formGroup)
  {
    firePropertyChange(PROPERTY_FORM_UNLINKED,  formGroup, null);
  }

  public void setTarget(String target)
  {
    // Den Namen einer form zu ändern ist ein wenig 'tricky'. Alle Referenzen in den Domï¿½nen ( dies sind nur
    // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
    String save = getTarget();

    // unnötigerweise braucht man die URL nicht setzten
    //
    if(StringUtil.saveEquals(save,target))
      return;
    
    castor.setTarget(CastorExternalFormTargetType.valueOf(target));
    
    // Es müssen alle Referenzen auf diese Form angepasst werden
    //
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, save, target);
  }


  public void setURL(String url)
  {
    // Den Namen einer form zu ändern ist ein wenig 'tricky'. Alle Referenzen in den Domï¿½nen ( dies sind nur
    // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
    String save = getURL();

    // unnötigerweise braucht man die URL nicht setzten
    //
    if(StringUtil.saveEquals(save,url))
      return;
    
    castor.setUrl(url);
    
    // Es müssen alle Referenzen auf diese Form angepasst werden
    //
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, save, url);
  }

  public String getURL()
  {
    return StringUtil.toSaveString(getCastor().getUrl());
  }
  
  public String getTarget()
  {
    return getCastor().getTarget()==null?"": getCastor().getTarget().toString();
  }

  protected void createMissingI18NKey()
  {
    String label = castor.getLabel();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);
  }

  protected void renameI18NKey( String fromName, String toName)
  {
    if(castor.getLabel()!=null && castor.getLabel().equals(fromName))
      castor.setLabel(toName);
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }

  public void openEditor()
  {
    new ShowExternalFormEditorAction()
    {
      @Override
      public UIExternalFormModel getFormModel()
      {
        return UIExternalFormModel.this;
      }
    }.run(null);
  }

  public void renameEventHandler(String fromClass, String toClass)
  {
  }  
}
