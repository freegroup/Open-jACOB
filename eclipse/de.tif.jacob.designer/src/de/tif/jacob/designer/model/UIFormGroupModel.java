/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
/*
 * Created on Oct 19, 2004
 *
 */
package de.tif.jacob.designer.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIFormGroupModel extends ObjectModel implements UIIFormContainer
{
	CastorFormGroup castor;
  private final UIDomainModel domain;
  
  List<UIFormModel> forms = new ArrayList<UIFormModel>();
  
	public UIFormGroupModel(JacobModel jacob,UIDomainModel domain, String name)
	{
	  super(jacob);
    this.domain = domain;
    
	  this.castor = new CastorFormGroup();
	  this.castor.setName(name);
	  if(getJacobModel().useI18N())
	  	this.castor.setTitle("%FORMGROUP"+getJacobModel().getSeparator()+ name.toUpperCase());
	  else
	  	this.castor.setTitle(StringUtils.capitalise(name));
	}
	
	/**
   */
  protected UIFormGroupModel(JacobModel jacob, UIDomainModel domain, CastorFormGroup formGroup)
  {
    super(jacob);
    this.domain = domain;

    this.castor = formGroup;
    
    for(int i=0;i<formGroup.getFormCount();i++)
    {
      UIFormModel form =jacob.getFormModel(formGroup.getForm(i));
      if(form==null)
        System.out.println("ERROR:"+formGroup.getForm(i));
      else
        forms.add(form);
    }
  }


  public String getExtendedDescriptionLabel()
  {
    return this.getName();
  }
  
  /**
   * 
   * @return
   */
  public String getLabel()
  {
    return castor.getTitle();
  }
  
  /**
   * 
   * @param newLabel
   */
	public void setLabel(String newLabel)
	{
		String save = getLabel();
		if(save.equals(newLabel))
		  return;
		
		castor.setTitle(newLabel);
		firePropertyChange(PROPERTY_LABEL_CHANGED, save, newLabel);
	}
	
	/**
	 * 
	 * @param newName
	 */
	public void setName(String name) throws Exception
	{
      String save = getName();

      // unnötigerweise braucht man den Namen nicht setzten
      //
      if(save.equals(name))
      	return;
      
      castor.setName(name);
      
      // Es müssen alle Referenzen auf diese FormGroup angepasst werden
      //
      firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
	}

	/**
	 * 
	 * @return
	 */
	public String getName()
  {
  	return castor.getName();
  }
  
  /**
   * 
   * @return List[FormModel]
   */
  public List<UIFormModel> getFormModels()
  {
    return forms;
  }

  /**
   * 
   * @param name
   */
  public UIFormModel getFormModel(String name)
  {
    for(int i=0;i<castor.getFormCount();i++)
    {
      if(castor.getForm(i).equals(name))
        return getJacobModel().getFormModel(name);
    }
    return null;
  }

  /**
   * 
   * @param form
   */
  public void removeElement(UIFormModel form)
  {
    int index=-1;
    if((index=forms.indexOf(form))!=-1)
    {
      castor.removeForm(index);
      forms.remove(index);
      form.hasUnlinkedTo(this);
      firePropertyChange(PROPERTY_ELEMENT_REMOVED, form, null);
    }
  }
  
  /**
   * 
   * @param form
   */
  public void addElement(UIFormModel existingForm, UIFormModel newForm)
  {
    int index = forms.indexOf(existingForm);
    castor.addForm(index + 1, newForm.getName());
    forms.add(index + 1, newForm);
    newForm.hasLinkedTo(this);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null,newForm);
  }

  /**
   * 
   * @param form
   */
  public void addElement(UIFormModel newForm)
  {
    if(!forms.contains(newForm))
    {
      castor.addForm(newForm.getName());
      forms.add(newForm);
      newForm.hasLinkedTo(this);
    
      firePropertyChange(PROPERTY_ELEMENT_ADDED, null,newForm);
    }
  }

  /**
   * 
   * @param form
   * @return true if the hands over form a member of this domain.
   */
  public boolean contains(UIFormModel form)
  {
  	return forms.contains(form);
  }
  
  protected void renameFormReference(String from , String to)
  {
    for(int i=0;i<castor.getFormCount();i++)
    {
      if(castor.getForm(i).equals(from))
        castor.setForm(i,to);
    }
  }
  
  /**
   * 
   * @param fromName from key WITH '%' as first sign
   * @param toName to key WITH '%' as first sign
   */
  protected void renameI18NKey( String fromName, String toName)
  {
    if(castor.getTitle()!=null && castor.getTitle().equals(fromName))
      castor.setTitle(toName);
  }

  /**
   * 
   * @param key the key to check WITH the % at first character
   * @return
   */
  protected boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(getLabel(), key);
  }
   
  /**
   * 
   */
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
	
	/**
	 * 
	 */
	public void setPropertyValue(Object propName, Object val)
	{
		try 
		{
			if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
				setName((String) val);
			else if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_LABEL))
				setLabel((String) val);
			else
				super.setPropertyValue(propName, val);
		} 
		catch (Exception e) 
		{
			JacobDesigner.showException(e);
		}
	}
	
	/**
	 * 
	 */
	public Object getPropertyValue(Object propName)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
			return getName();
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_LABEL))
			return getLabel();
		return super.getPropertyValue(propName);
	}
	
  /**
   * @return Returns the jacobModel.
   */
  public ApplicationModel getApplicationModel()
  {
    return getJacobModel().getApplicationModel();
  }
  

  public String getError()
  {
    return null;
  }
  
  public String getWarning()
  {
    return null;
  }
  
  public String getInfo()
  {
    return null;
  }
  
  public boolean isInUse()
  {
    return true;
  }
  
  protected CastorFormGroup getCastor()
  {
    return castor;
  }

  /**
   * 
   */
  public void resetI18N()
  {
    String oldLabel = getLabel();
    
    castor.setTitle("%FORMGROUP"+getJacobModel().getSeparator()+getName().toUpperCase());

    // Es wird wenn möglich das alte Label wiederverwendet...
    //
    if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
      getJacobModel().addI18N(castor.getTitle().substring(1),oldLabel,false);
    else
      getJacobModel().addI18N(castor.getTitle().substring(1),getName(),false);
  }

  /**
   * 
   */
  public void createMissingI18NKey()
  {
    String label = castor.getTitle();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);
  }

  public UIDomainModel getDomainModel()
  {
    return domain;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
  
  @Override
  public ObjectModel getParent()
  {
    return domain;
  }
}
