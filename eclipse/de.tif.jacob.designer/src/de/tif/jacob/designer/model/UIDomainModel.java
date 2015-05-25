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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomain;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorExternalFormTargetType;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.exception.InvalidDomainNameException;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIDomainModel extends ObjectWithPropertyModel implements UIIFormContainer
{
	CastorDomain castor;
  List<UIFormModel>      forms  = new ArrayList<UIFormModel>();
  List<UIFormGroupModel> groups = new ArrayList<UIFormGroupModel>();
  
	public UIDomainModel(JacobModel jacob, String name)
	{
	  super(jacob);
	  this.castor = new CastorDomain();
	  this.castor.setVisible(true);
	  this.castor.setName(name);
	  if(getJacobModel().useI18N())
	  	this.castor.setTitle("%DOMAIN"+getJacobModel().getSeparator()+ name.toUpperCase());
	  else
	  	this.castor.setTitle(StringUtils.capitalise(name));
	}
	
	/**
   */
  protected UIDomainModel(JacobModel jacob, CastorDomain domain)
  {
    super(jacob);
    this.castor = domain;
    
    for(int i=0;i<domain.getFormCount();i++)
    {
      UIFormModel form =jacob.getFormModel(domain.getForm(i));
      if(form==null)
        System.out.println("ERROR:"+domain.getForm(i));
      else
        forms.add(form);
    }

    for(int i=0;i<domain.getFormGroupCount();i++)
    {
      UIFormGroupModel group =new UIFormGroupModel(jacob,this, domain.getFormGroup(i));
      groups.add(group);
    }
  }

  
  public String getExtendedDescriptionLabel()
  {
    return this.getName();
  }
  
  
  public boolean hasRole(UserRoleModel role)
  {
    for(int i=0;i<castor.getRole().length;i++)
    {
      if(castor.getRole(i).equals(role.getName()))
        return true;
    }
    return false;
  }
  
  public void addElement(UserRoleModel role)
  {
    if(!hasRole(role))
    {
      castor.addRole(role.getName());
  		firePropertyChange(PROPERTY_USERROLE_ASSIGNED, null, role);
    }
  }
  
  public void removeElement(UserRoleModel role)
  {
    for(int i=0;i<castor.getRole().length;i++)
    {
      if(castor.getRole(i).equals(role.getName()))
      {
        castor.removeRole(i);
    		firePropertyChange(PROPERTY_USERROLE_UNASSIGNED, role,null);
    		break;
      }
    }
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
   * @param newLabel
   */
  public boolean getCanCollapse()
  {
    return castor.getCanCollapse();
  }

  public String getDataScope()
  {
    if(castor.getDataScope()==null)
      return "";
    return  castor.getDataScope().toString();
  }
  
  public void setDataScope(String scope)
  {
    String save = getDataScope();

    // unnötigerweise braucht man die URL nicht setzten
    //
    if(StringUtil.saveEquals(save,scope))
      return;
    
    castor.setDataScope(CastorDomainDataScopeType.valueOf(scope));
    
    // Es müssen alle Referenzen auf diese Form angepasst werden
    //
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, save, scope);
  }
  
  /**
   * 
   * @param newLabel
   */
  public void setCanCollapse(boolean canCollapse)
  {
    castor.setCanCollapse(canCollapse);
  }
  
  /**
	 * 
	 * @param newName
	 */
	public void setName(String name) throws Exception
	{
      // Den Namen einer form zu ï¿½ndern ist ein wenig 'tricky'. Alle Referenzen in den Domï¿½nen ( dies sind nur
      // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
      String save = getName();

      // unnï¿½tigerweise braucht man den Namen nicht setzten
      //
      if(save.equals(name))
      	return;
      
      // prï¿½fen ob der Name bereits vergeben ist. Der Formname muss eindeutig sein
      //
      if(getJacobModel().getDomainModel(name)!=null)
      	throw new InvalidDomainNameException("Domain name is already in use. Please use another name.");
      
      
      getJacobModel().renameDomainReference(save, name);

      castor.setName(name);
      
      // Falls sich der Name eines Elementes geï¿½ndert hat, mï¿½ssen alle Eventhandler auf den neuen
      // Namen angepasst werden
      //
      IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
      String fromClass = "jacob.event.screen."+save;
      String toClass   = "jacob.event.screen."+name;
      ClassFinder.renamePackage(fromClass,toClass ,myJavaProject);

      
      // Es mï¿½ssen alle Referenzen auf diese Form angepasst werden
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

  public List<UIFormGroupModel> getFormGroupModels()
  {
    return groups;
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
  public void removeElement(UIFormGroupModel group)
  {
    int index=-1;
    if((index=groups.indexOf(group))!=-1)
    {
      castor.removeFormGroup(index);
      groups.remove(index);
      // Allen Formen in der Gruppe mitteilen, dass diese eventuell nicht mehr 
      // gelinkt sind (benötigt werden).
      //
      for(UIFormModel form :group.getFormModels())
      {
        form.hasUnlinkedTo(this);
      }
      // Alle UI Elementen mitteilen, dass die domain ein Kind weniger hat
      // (ApplicationTreeView)
      //
      firePropertyChange(PROPERTY_ELEMENT_REMOVED, group, null);
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
   */
  public void addElement(UIFormGroupModel newGroup)
  {
    if(!groups.contains(newGroup))
    {
      castor.addFormGroup(newGroup.getCastor());
      groups.add(newGroup);
    
      firePropertyChange(PROPERTY_ELEMENT_ADDED, null,newGroup);
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
    // Alle formen welche direkt an der Domain hängen müssen umbenannt werden
    //
    for(int i=0;i<castor.getFormCount();i++)
    {
      if(castor.getForm(i).equals(from))
        castor.setForm(i,to);
    }
    
    // ...und alle welche innerhalb einer Gruppe sind
    //
    for(UIFormGroupModel group:getFormGroupModels())
    {
      group.renameFormReference(from,to);
    }
    
  }
  
  protected void renameUserRole(String from , String to)
  {
    for(int i=0;i<castor.getRoleCount();i++)
    {
      if(castor.getRole(i).equals(from))
        castor.setRole(i,to);
    }
  }
   
  /**
   * 
   * @param fromName from key WITH '%' as first sign
   * @param toName to key WITH '%' as first sign
   */
  protected void renameI18NKey( String fromName, String toName)
  {
    // Der key von der Domain muss angepasst werden
    //
    if(castor.getTitle()!=null && castor.getTitle().equals(fromName))
      castor.setTitle(toName);
    
    // und eventuell aller Gruppen in dieser Domain
    //
    for(UIFormGroupModel group:getFormGroupModels())
    {
      group.renameI18NKey(fromName,toName);
    }
  }  

  /**
   * 
   * @param key the key to check WITH the % at first character
   * @return
   */
  protected boolean isI18NKeyInUse(String key)
  {
    if( StringUtil.saveEquals(getLabel(), key))
        return true;
    
    for(UIFormGroupModel group:getFormGroupModels())
    {
      if(group.isI18NKeyInUse(key))
        return true;
    }
    return false;
  }
   
  /**
   * 
   */
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 4];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,  "Name", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length+1] = new TextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length+2] = new CheckboxPropertyDescriptor(ID_PROPERTY_COLLAPSE, "Can Collapse", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length+3] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_DATASCOPE, "Data Scope" ,(String[]) DATA_SCOPES.toArray(new String[0]), PROPERTYGROUP_COMMON);
		
		return descriptors;
	}
	
	/**
	 * 
	 */
	public void setPropertyValue(Object propName, Object val)
	{
		try 
		{
			if (propName.equals(ID_PROPERTY_NAME))
				setName((String) val);
      else if (propName == ID_PROPERTY_DATASCOPE)
        setDataScope((String) DATA_SCOPES.get(((Integer) val).intValue()));
			else if (propName.equals(ID_PROPERTY_LABEL))
				setLabel((String) val);
      else if (propName.equals(ID_PROPERTY_COLLAPSE))
        setCanCollapse(((Boolean) val).booleanValue());
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
		if (propName.equals(ID_PROPERTY_NAME))
			return getName();
    if (propName == ID_PROPERTY_DATASCOPE)
      return new Integer(DATA_SCOPES.indexOf(getDataScope()));
    if (propName.equals(ID_PROPERTY_LABEL))
      return getLabel();
    if (propName.equals(ID_PROPERTY_COLLAPSE))
      return new Boolean(getCanCollapse());
		return super.getPropertyValue(propName);
	}
	
  /**
   * @return Returns the jacobModel.
   */
  public ApplicationModel getApplicationModel()
  {
    return getJacobModel().getApplicationModel();
  }
  
  public String getImageBaseName()
  {
    String imageBaseName= super.getImageBaseName();
    if(castor.getRoleCount()>0)
      imageBaseName = imageBaseName+"_locked";
    return imageBaseName;
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
  
  protected CastorDomain getCastor()
  {
    return castor;
  }

  public String getTemplateFileName()
  {
    return "IDomainEventHandler.java";
  }

  public void generateHookClassName() throws Exception
  {
    if (getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = castor.getEventHandler();
      castor.setEventHandler("jacob.event.ui." + StringUtils.capitalise(getName()));
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, save, castor.getEventHandler());
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
  }
  

  public String getHookClassName()
  {
    if (getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
      return castor.getEventHandler();
    return "jacob.common.gui." + StringUtils.capitalise(getName());
  }
  
  /**
   * 
   */
  public void resetI18N()
  {
    String oldLabel = getLabel();
    
    castor.setTitle("%DOMAIN"+getJacobModel().getSeparator()+getName().toUpperCase());

    // Es wird wenn möglich das alte Label wiederverwendet...
    //
    if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
      getJacobModel().addI18N(castor.getTitle().substring(1),oldLabel,false);
    else
      getJacobModel().addI18N(castor.getTitle().substring(1),getName(),false);

    for(UIFormGroupModel group:getFormGroupModels())
    {
      group.resetI18N();
    }
  }

  /**
   * 
   */
  public void createMissingI18NKey()
  {
    String label = castor.getTitle();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);
    
    for(UIFormGroupModel group:getFormGroupModels())
    {
      group.createMissingI18NKey();
    }
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getApplicationModel())
      result.addReferences(this);
    
    for(UIFormGroupModel group:getFormGroupModels())
    {
      group.addReferrerObject(result, model);
    }
  }

  @Override
  public ObjectModel getParent()
  {
    return getApplicationModel();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
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

  public void renameEventHandler(String fromClass, String toClass)
  {
    // Nicht StringUtils.saveEquals(..) verwenden. Da sind null und "" gleich!!
    // Desweiteren darf dieser Teil nicht ausgefï¿½hrt werden wenn zuvor kein Eventhandler
    // definert war. Wenn zuvor kein Eventhandler definiert war, dann kann man Ihn auch
    // nicht umbenennen.
    //
    if(this.castor.getEventHandler()!=null && this.castor.getEventHandler().equals(fromClass))
    {
      this.castor.setEventHandler(toClass);
      this.firePropertyChange(ObjectModel.PROPERTY_EVENTHANDLER_CHANGED, fromClass, toClass);
    }
  }  

 }
