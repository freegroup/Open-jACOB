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

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.designer.actions.ShowJacobFormEditorAction;
import de.tif.jacob.designer.editor.jacobform.misc.IntegerPropertyGroupingDescriptor;


public abstract class UIFormElementModel extends ObjectWithPropertyModel implements IOpenable
{
  private UIGroupContainer  container;

	public abstract Point     getLocation();
	public abstract Dimension getSize();

	public abstract void      setSize(Dimension size);
  public abstract String    getCastorName();
  public abstract CastorDimension getCastorDimension();
  public abstract void      setCastorName(String name);
	public abstract void      setName(String name) throws Exception;
	
  protected UIFormElementModel(JacobModel jacob, UIGroupContainer container)
  {
    super(jacob);
  	this.container=container;
  }
  
  public final String getName()
	{
		return getCastorName();
	}
	
	public void setConstraint(Rectangle rect)
	{
		Rectangle save = new Rectangle(getLocation(), getSize());
		if(save.equals(rect))
		  return;
		setSize(rect.getSize());
		setLocation(rect.getLocation());
	}

  public final void setLocation(Point location)
  {
    // Nicht jedes element hat eine Dimension z.B. Kontextmenu oder
    // Tab's...
    // Diese ermitteln Ihre Größe dann zur Laufzeit.
    //
    if(getCastorDimension()==null)
      return;
    
    Point save = getLocation();
    getCastorDimension().setX(location.x);
    getCastorDimension().setY(location.y);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, location);
  }

  public Rectangle getConstraint()
	{
		if(getLocation()==null)
			return null;
		
		return new Rectangle(getLocation(), getSize());
	}
	
  public boolean isVisible()
  {
  	return true;
  }
  
	/**
	 * internal usage
	 * @param form
	 */
	public void setGroupContainerModel(UIGroupContainer container)
	{
			this.container=container;
	}
	
	public UIGroupContainer getGroupContainerModel()
	{
		return this.container;
	}

  public UIJacobFormModel getJacobFormModel()
  {
    UIGroupContainer container = getGroupContainerModel();
    while(container instanceof UITabPanesModel)
      container = ((UITabPanesModel)container).getGroupContainerModel();
    
    return (UIJacobFormModel)container;
  }
  
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 4];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		
		descriptors[superDescriptors.length]     = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_LOCATION_X, "X", PROPERTYGROUP_LOCATION);
		descriptors[superDescriptors.length + 1] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_LOCATION_Y, "Y", PROPERTYGROUP_LOCATION);
		descriptors[superDescriptors.length + 2] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_HEIGHT    , "Height", PROPERTYGROUP_DIMENSION);
		descriptors[superDescriptors.length + 3] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_WIDTH     , "Width" , PROPERTYGROUP_DIMENSION);
		
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
  {
    if (propName == ID_PROPERTY_LOCATION_X)
      setLocation(new Point(Integer.parseInt(val.toString()), getLocation().y));
    else if (propName == ID_PROPERTY_LOCATION_Y)
      setLocation(new Point(getLocation().x, Integer.parseInt(val.toString())));
    else if (propName == ID_PROPERTY_WIDTH)
      setSize(new Dimension(Integer.parseInt(val.toString()), getSize().height));
    else if (propName == ID_PROPERTY_HEIGHT)
      setSize(new Dimension(getSize().width, Integer.parseInt(val.toString())));
    else
      super.setPropertyValue(propName, val);
  }
	
	public Object getPropertyValue(Object propName)
	{
		if (propName==ID_PROPERTY_LOCATION_X)
			return new Integer(getLocation().x).toString();
		else if (propName==ID_PROPERTY_LOCATION_Y)
			return new Integer(getLocation().y).toString();
		else if ( propName==ID_PROPERTY_WIDTH)
			return new Integer(getSize().width).toString();
		else if ( propName==ID_PROPERTY_HEIGHT)
			return new Integer(getSize().height).toString();
      
		return super.getPropertyValue(propName);
	}
	
  public String getHookClassName()
  {
    return null;//getEventHandlerName(getDomainModel(),getFormModel(), this);
  }
  
	public static String getEventHandlerName(UIDomainModel domain, UIGroupContainer container, String elementName)
	{
    String eventClassName = "jacob.event.screen";
    if (domain != null)
    {
      if (container != null)
      {
        eventClassName = eventClassName + "." + domain.getName();
        if (elementName != null)
        {
          eventClassName = eventClassName + "." + container.getName();
          eventClassName = eventClassName + "." + StringUtils.capitalise(elementName);
        }
        else
        {
          eventClassName = eventClassName + "." + StringUtils.capitalise(container.getName());
        }
      }
      else
      {
       
        eventClassName = eventClassName + "." + StringUtils.capitalise(domain.getName());
      }
    }
    // e.g. a toolbar button. They have no form, group or domain.
    else if (elementName != null)
    {
      eventClassName = eventClassName + "." + StringUtils.capitalise(elementName);
    }
    return eventClassName;
	}


  public void openEditor()
  {
    new ShowJacobFormEditorAction()
    {
      @Override
      public UIJacobFormModel getFormModel()
      {
        return UIFormElementModel.this.getJacobFormModel();
      }
    }.run(null);
  } 
}


