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
package de.tif.jacob.designer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowJacobFormEditorAction;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.exception.InvalidFormNameException;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

public class UIJacobFormModel extends UIFormModel implements UIGroupContainer, IOpenable
{
	private final CastorJacobForm castor;
	private List<UIGroupModel> elements = null;
  private List hGuides   = null;
  private List vGuides   = null;
  
	public UIJacobFormModel(JacobModel jacob, String name)
	{
	  super(jacob);
	  this.castor = new CastorJacobForm();
	  this.castor.setName(name);
	  if(getJacobModel().useI18N())
	  	this.castor.setLabel("%"+suggestI18NKey());
	  else
	  	this.castor.setLabel(StringUtils.capitalise(name));
	}
	
	public UIJacobFormModel(JacobModel jacob, CastorJacobForm form)
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
  
  
  public void setLabel(String newLabel)
  {
    String save = getLabel();
    
    if(StringUtil.saveEquals(save,newLabel))
      return;
    
    castor.setLabel(newLabel);
    firePropertyChange(PROPERTY_LABEL_CHANGED, save, newLabel);
  }

  /**
   * 
   * @return List[UIFormGuideModel]
   */
  public List getHorizontalGuides()
  {
    if(hGuides==null)
    {
      hGuides = new ArrayList();
      for(int i=0;i<castor.getPropertyCount();i++)
      {
        CastorProperty property=castor.getProperty(i);
        if(property.getName().startsWith("HGUIDE_"))
        {
          UIFormGuideModel guid= new UIFormGuideModel(this,property.getName(), NumberUtils.stringToInt(property.getValue(),0), true);
          hGuides.add(guid);
        }
      }
    }
    return hGuides;
  }

  /**
   * 
   * @return List[UIFormGuideModel]
   */
  public List getVerticalGuides()
  {
    if(vGuides==null)
    {
      vGuides = new ArrayList();
      for(int i=0;i<castor.getPropertyCount();i++)
      {
        CastorProperty property=castor.getProperty(i);
        if(property.getName().startsWith("VGUIDE_"))
        {
          UIFormGuideModel guid= new UIFormGuideModel(this,property.getName(), NumberUtils.stringToInt(property.getValue(),0), false);
          vGuides.add(guid);
        }
      }
    }
    return vGuides;
  }

  public UIFormGuideModel getGuide(String id)
  {
    if(id==null)
      return null;
    
    for (Iterator iter = getVerticalGuides().iterator(); iter.hasNext();)
    {
      UIFormGuideModel guide = (UIFormGuideModel) iter.next();
      if(guide.getId().equals(id))
        return guide;
    }
    for (Iterator iter = getHorizontalGuides().iterator(); iter.hasNext();)
    {
      UIFormGuideModel guide = (UIFormGuideModel) iter.next();
      if(guide.getId().equals(id))
        return guide;
    }
    return null;
    
  }
  
	/**
	 * 
	 * @return List[UIGroupModel]
	 */
	public List<UIGroupModel> getElements()
	{
	  if(elements==null)
	  {
	  	elements = new ArrayList<UIGroupModel>();
	  	for(int i=0;i<castor.getGroupCount();i++)
	  	{
	  	  CastorGroup group=castor.getGroup(i);
	  	  UIGroupModel groupModel=new UIGroupModel(getJacobModel(),this, group);
	  	  elements.add(groupModel);
	  	}
	  }
		return elements;
	}

  
	/**
	 * 
	 * @return UIGroupElementModel
	 */
	public UIGroupElementModel getElement(String name)
	{
	  Iterator iter = getElements().iterator();
	  while(iter.hasNext())
  	{
  	  UIGroupModel groupModel=(UIGroupModel)iter.next();
  	  UIGroupElementModel obj=groupModel.getElement(name); 
  	  if(obj!=null)
  	    return obj;
  	}
		return null;
	}

  public boolean isUIElementNameFree(String name)
  {
    for (UIGroupModel group : getElements())
    {
      if(group.isUIElementNameFree(name)==false)
        return false;
    }
    return true;
  }

  public void addElement(UIFormGuideModel element)
  {
   if(element.isHorizontal())
     getHorizontalGuides().add(element);
   else
     getVerticalGuides().add(element);
   firePropertyChange(PROPERTY_GUIDE_ADDED,null, element);
  }
  
	public void addElement(UIGroupModel group)
	{
	  castor.addGroup(group.getCastor());
	  getElements().add(group);
	  group.setGroupContainerModel(this);
	  
		firePropertyChange(PROPERTY_ELEMENT_ADDED, null, group);
	}
	
  public void removeElement(UIFormGuideModel element)
  {
    if(element.isHorizontal())
      getHorizontalGuides().remove(element);
    else
      getVerticalGuides().remove(element);
    this.setCastorProperty(element.getId(),null);
    firePropertyChange(PROPERTY_GUIDE_REMOVED,element,null);
  }
  
	public void removeElement(UIGroupModel group)
	{
	  for(int i=0;i<castor.getGroupCount();i++)
	  {
	    if(castor.getGroup(i)==group.getCastor())
	    {
	      castor.removeGroup(i);
	      elements.remove(group);
	  		firePropertyChange(PROPERTY_ELEMENT_REMOVED, group, null);
	      // Dem Browser und alle die diesen anzeigen mitteilen, das sich der (Error/Warning) Status 
	      // mï¿½glicherweise geï¿½ndert hat. Der Browser feuert ein Event und alle Listener aktualisieren sich dann
	      // /Es gibt eventuell keine Gruppe mehr die diesen Browser benï¿½tigt)
        group.getBrowserModel().firePropertyChange(PROPERTY_BROWSER_CHANGED, null,group.getBrowserModel());
	      break;
	    }
	  }
	}
	
	
	public void hasLinkedTo(UIDomainModel domain)
	{
		firePropertyChange(PROPERTY_FORM_LINKED, null, domain);
	}
	
  public void hasLinkedTo(UIFormGroupModel formGroup)
  {
    firePropertyChange(PROPERTY_FORM_LINKED, null, formGroup);
  }
  
  public void hasUnlinkedTo(UIDomainModel domain)
	{
		firePropertyChange(PROPERTY_FORM_UNLINKED,  domain, null);
	}
	

  public void hasUnlinkedTo(UIFormGroupModel formGroup)
  {
    firePropertyChange(PROPERTY_FORM_UNLINKED,  formGroup, null);
  }
  
  public String getLabel()
	{
	  return castor.getLabel();
	}
	
	/**
	 * 
	 * @return List[GroupModel]
	 */
	public List<UIGroupModel> getGroupModels()
	{
		return getElements();
	}
	
	/**
	 * 
	 * @return true if the form used in any Domain or false.
	 */
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
	
  protected void createMissingI18NKey()
  {
    String label = castor.getLabel();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);
    
    Iterator iter = getGroupModels().iterator();
    while (iter.hasNext())
    {
      UIGroupModel obj = (UIGroupModel) iter.next();
      obj.createMissingI18NKey();
    }
  }  

  protected void renameFieldReference(FieldModel field, String fromName, String toName)
  {
    Iterator iter = getGroupModels().iterator();
    while (iter.hasNext())
    {
      UIGroupModel obj = (UIGroupModel) iter.next();
      obj.renameFieldReference(field, fromName,toName);
    }
  }  
 
  /**
   * @param from the orignal key WITH the % at first character
   * @param to the new key WITH the % at first character
   */
  protected void renameI18NKey( String fromName, String toName)
  {
    if(castor.getLabel()!=null && castor.getLabel().equals(fromName))
      castor.setLabel(toName);
    
    for(UIGroupModel group: getGroupModels())
    {
      group.renameI18NKey(fromName,toName);
    }
  }  

  protected void resetI18N()
  {
    String oldLabel = getLabel();
    castor.setLabel("%"+suggestI18NKey());
    // Es wird wenn möglich das alte Label wiederverwendet...
    //
    if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
      getJacobModel().addI18N(castor.getLabel().substring(1),oldLabel,false);
    else
      getJacobModel().addI18N(castor.getLabel().substring(1),getName(),false);
    
    for (UIGroupModel group : getGroupModels())
      group.resetI18N();
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
    
    for (UIGroupModel group : getGroupModels())
    {
      if(group.isI18NKeyInUse(key))
        return true;
    }
    return false;
  }  
  
  // Den Namen einer Relation zu ändern ist ein wenig 'tricky'. Alle Referenzen in den ForeignFields ( dies sind nur
  // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
  //
  protected void renameRelationReference(String from, String to)
  {
    Iterator iter = getGroupModels().iterator();
    while (iter.hasNext())
    {
      UIGroupModel obj = (UIGroupModel) iter.next();
      obj.renameRelationReference(from,to);
    }
  }  
	
  protected void renameAliasReference(String from, String to)
  {
    for (UIGroupModel group : getGroupModels())
    {
      group.renameAliasReference(from,to);
    }
  }  
	
  protected void renameRelationsetReference(String from, String to)
  {
    for (UIGroupModel group : getGroupModels())
    {
      group.renameRelationsetReference(from,to);
    }
  }  
	
  protected void renameBrowserReference(String from, String to)
  {
    for (UIGroupModel group : getGroupModels())
    {
      group.renameBrowserReference(from,to);
    }
  }  
	
  protected void removeEnumReference(FieldModel field, String value)
  {
    for (UIGroupModel group : getGroupModels())
    {
      group.removeEnumReference(field, value);
    }
  }  
	
  protected void renameEnumReference(FieldModel field, String fromValue, String toValue)
  {
    for (UIGroupModel group : getGroupModels())
    {
      group.renameEnumReference(field, fromValue,toValue);
    }
  }  
  
	
  public void renameEventHandler(String fromClass, String toClass)
  {
    // Nicht StringUtils.saveEquals(..) verwenden. Da sind null und "" gleich!!
    // Desweiteren darf dieser Teil nicht ausgefï¿½hrt werden wenn zuvor kein Eventhandler
    // definert war. Wenn zuvor kein Eventhandler definiert war, dann kann man Ihn auch
    // nicht umbenennen.
    //
    if(castor.getEventHandler()!=null && castor.getEventHandler().equals(fromClass))
    {
      castor.setEventHandler(toClass);
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, fromClass, toClass);
    }
    
    for (UIGroupModel group : getGroupModels())
    {
      group.renameEventHandler(fromClass,toClass);
    }
  }  

  /**
	 * Return all Domains which contains the form.
	 * 
	 * @return List[DomainModel]
	 */
	public List<UIDomainModel> getLinkedDomainModels()
	{
		List<UIDomainModel> result = new ArrayList<UIDomainModel>();
		for(UIDomainModel domain:getJacobModel().getApplicationModel().getDomainModels())
		{
			if(domain.contains(this))
				result.add(domain);
		}
		return result;
	}
	
	public void setName(String name) throws Exception
	{
	  // Den Namen einer form zu ändern ist ein wenig 'tricky'. Alle Referenzen in den Domï¿½nen ( dies sind nur
	  // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
		String save = getName();

		// unnï¿½tigerweise braucht man den Namen nicht setzten
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
		
		// Falls sich der Name eines Elementes geändert hat, müssen alle Eventhandler auf den neuen
		// Namen angepasst werden
		//
    IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
    Iterator iter = getLinkedDomainModels().iterator();
    
    while(iter.hasNext())
    {
    	UIDomainModel domain = (UIDomainModel)iter.next();
    	String fromClass = "jacob.event.screen."+domain.getName()+"."+save;
    	String toClass   = "jacob.event.screen."+domain.getName()+"."+name;
  		ClassFinder.renamePackage(fromClass,toClass ,myJavaProject);
    }
	  
	  // Es müssen alle Referenzen auf diese Form angepasst werden
	  //
		firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
	}

	
	public String getName()
	{
	  return castor.getName();
	}
	
  protected CastorJacobForm getCastor()
  {
    return castor;
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

  public String getTemplateFileName()
  {
    return "IFormEventHandler.java";
  }
  

  public String getHookClassName()
  {
    if (getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
      return castor.getEventHandler();
    return "jacob.common.gui." + StringUtils.capitalise(getName());
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
  
  public String getError()
  {
    if(getGroupModels().size()==0)
      return "Form <"+getName()+"> must contain at least one group.";
    
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupModel obj = (UIGroupModel) iter.next();
      String error = obj.getError();
      if(error!=null)
        return error;
    }
    return null;
  }
  
  public String getWarning()
  {
    if(!isInUse())
      return "Form <"+getName()+"> is not used in any Domain.";
    
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupModel obj = (UIGroupModel) iter.next();
      String warning = obj.getWarning();
      if(warning!=null)
        return warning;
    }
    return null;
  }
  
  public String getInfo()
  {
    return null;
  }
  
  public String getImageBaseName()
  {
    if(getJacobModel().getTestRelationset()!=null)
      return super.getImageBaseName()+"_test";
    
    return super.getImageBaseName();
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    for (UIGroupModel group : getGroupModels())
    {
      group.addReferrerObject(result,model);
    }
  }
  
  public void openEditor()
  {
    new ShowJacobFormEditorAction()
    {
      @Override
      public UIJacobFormModel getFormModel()
      {
        return UIJacobFormModel.this;
      }
    }.run(null);
  } 
}