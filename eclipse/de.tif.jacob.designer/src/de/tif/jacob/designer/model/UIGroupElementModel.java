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

import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.IntegerPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;


public abstract class UIGroupElementModel extends UIFormElementModel
{
  final static String DEFAULT_NAME= "defaultName";
  
  private transient UIFormGuideModel leftGuide;
  private transient UIFormGuideModel topGuide;
  private transient UIFormGuideModel rightGuide;
  private transient UIFormGuideModel bottomGuide;
  
  private CastorGuiElement  castor = null;
  private UIGroupModel      group  = null;;
	
  public abstract String getDefaultNameSuffix();
  
  protected UIGroupElementModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group,  CastorGuiElement gui)
  {
  	super(jacob, container);
    this.setCastor(gui);
    this.group      = group;
    if(getCastor()!=null && getCastor().getName()==null)
      getCastor().setName(DEFAULT_NAME);
    
    if(container!=null)
    {
      UIJacobFormModel form= (UIJacobFormModel)jacob.getFormModel(container.getName());
      if(form==container)
      {
        leftGuide   = form.getGuide(getCastorStringProperty("LEFT_GUIDE"));
        topGuide    = form.getGuide(getCastorStringProperty("TOP_GUIDE"));
        rightGuide  = form.getGuide(getCastorStringProperty("RIGHT_GUIDE"));
        bottomGuide = form.getGuide(getCastorStringProperty("BOTTOM_GUIDE"));
        
        if(leftGuide!=null)
          leftGuide.getMap().put(this,new Integer(-1));
        if(topGuide!=null)
          topGuide.getMap().put(this,new Integer(-1));
        if(rightGuide!=null)
          rightGuide.getMap().put(this,new Integer(1));
        if(bottomGuide!=null)
          bottomGuide.getMap().put(this,new Integer(1));
      }
    }
  }

  /**
   * Per Default sind alle Captions an der Linken Seite des Elementes
   * 
   * @param parentContraint
   * @return
   */
  public Rectangle getDefaultCaptionConstraint(Rectangle parentContraint)
  {
    Rectangle result = new Rectangle();
    result.x = parentContraint.x-(DEFAULT_CAPTION_WIDTH+DEFAULT_CAPTION_SPACING);
    result.y = parentContraint.y;
    result.width = DEFAULT_CAPTION_WIDTH;
    result.height =DEFAULT_CAPTION_HEIGHT;
    
    return result;
  }
  
  public final String getDefaultName()
  {
   return getGroupModel().getTableAlias()+getDefaultNameSuffix(); 
  }
  
  public int getDefaultWidth()
  {
    return ObjectModel.DEFAULT_ELEMENT_WIDTH;
  }
  
  public int getDefaultHeight()
  {
    return ObjectModel.DEFAULT_ELEMENT_HEIGHT;
  }

  public String getGroupTableAlias()
  {
    return getGroupModel().getTableAlias();
  }
  
  public String getHookClassName()
  {
    return getCastorEventHandler();
  }

  public void setHookClassName(String className) throws Exception
  {
    if(getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
      setCastorEventHandler(className);
    else
      throw new Exception("Java Hooks by Name not sopported any more. Change the configuration in your application.jad");
  }

  
  public final void resetHookClassName() throws Exception
  {
    if(getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = getCastorEventHandler();
      setCastorEventHandler(null);
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED,save,null);
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
  }
  
	public void  generateHookClassName() throws Exception
	{
    if(getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = getCastorEventHandler();
      setCastorEventHandler(getGroupModel().getChildrenDefaultJavaPackage()+"."+StringUtils.capitalise(getName()));
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED,save,getCastorEventHandler());
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
	}

	protected String getCastorEventHandler()
  {
	  if(getCastor()==null)
	    return null;
	  
    return getCastor().getEventHandler();
  }
  
  protected void setCastorEventHandler(String event)
  {
    if(getCastor()==null)
      throw new RuntimeException("Object does'nt support event handling");
    
    getCastor().setEventHandler(event);
  }

  public void setGroup(UIGroupModel group)
  {
    if(this.group==group)
      return;
    
    this.group = group;
    setJacobModel(group.getJacobModel());
    setGroupContainerModel(group.getGroupContainerModel());
  } 
  
  public final UIGroupModel getGroupModel()
  {
    return group;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return getCastor().getProperty(index);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    return getCastor().getPropertyCount();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
    getCastor().addProperty(property);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void removeCastorProperty(CastorProperty property)
  {
    for(int i=0;i<getCastor().getPropertyCount();i++)
    {
      if(getCastor().getProperty(i)==property)
      {
        getCastor().removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property ["+property.getName()+"] is not part of "+getClass().getName());
  }
  
  public final boolean isVisible()
  {
  	return getCastor().getVisible();
  }
  
	public final void setCaption(String caption)
	{
	  if(this instanceof UIICaptionProviderModel)
	  {
	    UIICaptionProviderModel provider = (UIICaptionProviderModel)this;
      caption = StringUtil.toSaveString(caption);
		  if(caption.startsWith("%"))
		    caption = caption.toUpperCase();
		  
			String save = getCaption();

			if(caption.equals(save))
			  return;
      
      // Es war zuvor kein castor Element vorhanden. Es muss jetzt eins angelegt werden
      //
			if(StringUtil.toSaveString(save).length()==0 && caption.length()>0)
			{
        CastorCaption castor = new CastorCaption();
        
        CastorDimension dim = new CastorDimension();
        Rectangle rect = getDefaultCaptionConstraint(getConstraint());
        dim.setX(rect.x);
        dim.setY(rect.y);
        dim.setWidth(rect.width);
        dim.setHeight(rect.height);
        castor.setDimension(dim);
        provider.setCastorCaption(castor);
        provider.getCaptionModel().setAlign(provider.getDefaultCaptionAlign());
        getGroupModel().addElement( provider.getCaptionModel());
			}
      // Element wird auf NULL gesetzt => es wird gelöscht
			else if(caption.length()==0)
			{
        getGroupModel().removeElement( provider.getCaptionModel());
			}
      
      // neues Label wird gesetzt....falls vorhanden
      //
      if(provider.getCastorCaption()!=null && caption.length()>0)
      {
        provider.getCastorCaption().setLabel(caption);
        provider.getCaptionModel().firePropertyChange(PROPERTY_LABEL_CHANGED, save, caption);
      }
      
			firePropertyChange(PROPERTY_LABEL_CHANGED, save, caption);
			getGroupModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED,null,this);
	  }
	  else
		  throw new RuntimeException("Object of class ["+this.getClass().getName()+"] is not an instance of "+UIICaptionProviderModel.class.getName()+"]");
	}
	
 
  public final String getCaption()
	{
	  if(this instanceof UIICaptionProviderModel)
	  {
	    UIICaptionProviderModel provider = (UIICaptionProviderModel)this;
	    CastorCaption caption = provider.getCastorCaption();
	    if(caption==null)
	    {
        return null;
	    }
			return caption.getLabel();
	  }
	  throw new IllegalStateException("Object is not an instance of UIICaptionProviderModel");
	}
		
	public final void setName(String name) throws Exception
	{
		String save = getName();
		
		if(save!=null && save.equals(name))
			return;
		
		if(getGroupContainerModel().isUIElementNameFree(name)==false)
		  return;
		
    // erst jetzt darf der Name in Castor eingetragen werden
    // da getFormModel()... den alten Namen benötigt
    //
    setCastorName(name);
    
		firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
	}
	
  public String getCastorName()
  {
	  // eine caption hat z.b. kein castor element
	  if(getCastor()==null)
	    return null;
	  
    return getCastor().getName();
  }
  
  public void setCastorName(String name)
  {
	  // eine caption hat z.b. kein castor element
	  if(getCastor()==null)
	    return;
	  
    getCastor().setName(name);
  }
  	
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
	  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
	  
    // Elemente welche kein Castor Element haben, können kein TabIndex und/oder Namen haben.
    //
	  if(getCastor()!=null)
	  {
	    IPropertyDescriptor[] descriptors=null;
	    if(this instanceof UIICaptionProviderModel)
			{
			  descriptors = new IPropertyDescriptor[superDescriptors.length + 3];
				for (int i = 0; i < superDescriptors.length; i++)
					descriptors[i] = superDescriptors[i];
				descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,   "Name", PROPERTYGROUP_COMMON);
				descriptors[superDescriptors.length+1] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_TABINDEX, "Tab-Index", PROPERTYGROUP_COMMON);
				descriptors[superDescriptors.length+2] = new TextPropertyGroupingDescriptor(ID_PROPERTY_CAPTION,"Caption", PROPERTYGROUP_COMMON);
			}
			else 
			{
			  descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
				for (int i = 0; i < superDescriptors.length; i++)
					descriptors[i] = superDescriptors[i];
				descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,   "Name", PROPERTYGROUP_COMMON);
				descriptors[superDescriptors.length+1] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_TABINDEX, "Tab-Index", PROPERTYGROUP_COMMON);
			}
			superDescriptors= descriptors;
	  }
    
    if(this instanceof IFontProviderModel)
    {
      IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 5];
      for (int i = 0; i < superDescriptors.length; i++)
        descriptors[i] = superDescriptors[i];
      descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_ALIGN, "Align" ,(String[]) ALIGNS.toArray(new String[0]), PROPERTYGROUP_COMMON);
      descriptors[superDescriptors.length+1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FONTFAMILY, "family", (String[]) FONT_FAMILIES.toArray(new String[0]), PROPERTYGROUP_FONT);
      descriptors[superDescriptors.length+2] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FONTWEIGHT, "weight", (String[]) FONT_WEIGHTS.toArray(new String[0]), PROPERTYGROUP_FONT);
      descriptors[superDescriptors.length+3] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FONTSTYLE,  "style", (String[]) FONT_STYLES.toArray(new String[0]), PROPERTYGROUP_FONT);
      descriptors[superDescriptors.length+4] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_FONTSIZE,    "size", PROPERTYGROUP_FONT);
      superDescriptors= descriptors;
    }
	  return superDescriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		try 
		{
			if (propName==ID_PROPERTY_CAPTION)
				setCaption((String) val);
			if (propName==ID_PROPERTY_NAME)
				setName((String) val);
			if (propName==ID_PROPERTY_TABINDEX)
				setTabIndex(Integer.parseInt(val.toString()));
			else if(this instanceof IFontProviderModel)
      {
        IFontProviderModel provider = (IFontProviderModel)this;
        if (propName == ID_PROPERTY_ALIGN)
          provider.setAlign((String) ALIGNS.get(((Integer) val).intValue()));
        else if (propName ==ID_PROPERTY_FONTFAMILY)
          provider.setFontFamily((String) FONT_FAMILIES.get(((Integer) val).intValue()));
        else if (propName ==ID_PROPERTY_FONTSTYLE)
          provider.setFontStyle((String) FONT_STYLES.get(((Integer) val).intValue()));
        else if (propName ==ID_PROPERTY_FONTWEIGHT)
          provider.setFontWeight((String) FONT_WEIGHTS.get(((Integer) val).intValue()));
        else if (((String) propName).equals(ID_PROPERTY_FONTSIZE))
          provider.setFontSize(Integer.parseInt(val.toString()));
      }
  		super.setPropertyValue(propName, val);
		} 
		catch (Exception e) 
		{
			JacobDesigner.showException(e);
		}
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_CAPTION))
			return StringUtil.toSaveString(getCaption());
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
			return getName();
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_TABINDEX))
		  return new Integer(getTabIndex()).toString();
    if( this instanceof IFontProviderModel)
    {
      IFontProviderModel provider = (IFontProviderModel)this;
      if (propName == ID_PROPERTY_ALIGN)
        return new Integer(ALIGNS.indexOf(provider.getAlign()));
      if (propName  == ID_PROPERTY_FONTFAMILY)
        return new Integer(FONT_FAMILIES.indexOf(provider.getFontFamily()));
      if (propName  == ID_PROPERTY_FONTWEIGHT)
        return new Integer(FONT_WEIGHTS.indexOf(provider.getFontWeight()));
      if (propName  == ID_PROPERTY_FONTSTYLE)
        return new Integer(FONT_STYLES.indexOf(provider.getFontStyle()));
      if (propName ==ID_PROPERTY_FONTSIZE)
        return new Integer(provider.getFontSize()).toString();
    }
		return super.getPropertyValue(propName);
	}
	
	protected int getTabIndex()
	{
	  // eine caption hat z.b. kein castor element
	  if(getCastor()==null)
	    return 0;
	  
	  return getCastor().getTabIndex();
	}

	protected void setTabIndex(int index)
	{
	  // eine caption hat z.b. kein castor element
	  if(getCastor()==null)
	    return;

	  int save = getTabIndex();
	  if(save==index)
	    return;
	  
	  getCastor().setTabIndex(index);
		firePropertyChange(PROPERTY_TABINDEX_CHANGED, new Integer(save), new Integer(index));
	}
	

  /**
   * @param castor The castor to set.
   */
  void setCastor(CastorGuiElement castor)
  {
    this.castor = castor;
  }

  /**
   * @return Returns the castor.
   */
  public CastorGuiElement getCastor()
  {
    return castor;
  }

  public void renameAliasReference(String from, String to)
  {
  	// do nothing per default
  }
  
  public void renameBrowserReference(String from, String to)
  {
  	// do nothing per default
  }

  public void renameEventHandler(String fromClass, String toClass)
  {
    // Nicht StringUtils.saveEquals(..) verwenden. Da sind null und ""
    // gleich!!
    // Desweiteren darf dieser Teil nicht ausgefuehrt werden wenn zuvor kein
    // Eventhandler definert war. 
    // Wenn zuvor kein Eventhandler definiert war, dann kann man
    // diesen auch nicht umbenennen.
    //
    if (getCastorEventHandler() != null && getCastorEventHandler().equals(fromClass))
    {
      setCastorEventHandler(toClass);
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, fromClass, toClass);
    }
  }
  
  public void renameFieldReference(FieldModel field, String fromName, String toName)
  {
  }
  
  public void createMissingI18NKey()
  {
  }
  
  public abstract void renameI18NKey(String fromName, String toName);
  
  public abstract boolean isI18NKeyInUse(String key);

  public void renameRelationReference(String from, String to)
  {
  }
  
  public void renameRelationsetReference(String from, String to)
  {
  }

  public UIFormGuideModel getBottomGuide()
  {
    return bottomGuide;
  }

  public void setBottomGuide(UIFormGuideModel guide)
  {
    // no changes => return
    if(this.bottomGuide==guide)
      return;
    
    // store the guide persistend in the *.jad
    if(guide!=null)
      this.setCastorProperty("BOTTOM_GUIDE",guide.getId());
    else
      this.setCastorProperty("BOTTOM_GUIDE",null);
    
    // detach from the old guide
    if(this.bottomGuide!=null)
      this.bottomGuide.detachPart(this);
    
    this.bottomGuide = guide;
  }

  public UIFormGuideModel getLeftGuide()
  {
    return leftGuide;
  }

  public void setLeftGuide(UIFormGuideModel guide)
  {
    // no changes => return
    if(this.leftGuide==guide)
      return;
    
    // store the guide persistend in the *.jad
    if(guide!=null)
      this.setCastorProperty("LEFT_GUIDE",guide.getId());
    else
      this.setCastorProperty("LEFT_GUIDE",null);

    // detach from the old guide
    if(this.leftGuide!=null)
      this.leftGuide.detachPart(this);
    
    this.leftGuide = guide;
  }

  public UIFormGuideModel getRightGuide()
  {
    return rightGuide;
  }

  public void setRightGuide(UIFormGuideModel guide)
  {
    // no changes => return
    if(this.rightGuide==guide)
      return;

    // store the guide persistend in the *.jad
    if(guide!=null)
      this.setCastorProperty("RIGHT_GUIDE",guide.getId());
    else
      this.setCastorProperty("RIGHT_GUIDE",null);

    // detach from the old guide
    if(this.rightGuide!=null)
      this.rightGuide.detachPart(this);
    
    this.rightGuide = guide;
  }

  public UIFormGuideModel getTopGuide()
  {
    return topGuide;
  }

  public void setTopGuide(UIFormGuideModel guide)
  {
    // no changes => return
    if(this.topGuide==guide)
      return;
    
    // store the guide persistend in the *.jad
    if(guide!=null)
      this.setCastorProperty("TOP_GUIDE",guide.getId());
    else
      this.setCastorProperty("TOP_GUIDE",null);

    // detach from the old guide
    if(this.topGuide!=null)
      this.topGuide.detachPart(this);

    this.topGuide = guide;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
  
  @Override
  public ObjectModel getParent()
  {
    return getGroupModel();
  }
}