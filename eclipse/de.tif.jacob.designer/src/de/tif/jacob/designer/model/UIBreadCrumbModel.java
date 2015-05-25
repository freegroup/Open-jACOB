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

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.BreadCrumb;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.Heading;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.IntegerPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIBreadCrumbModel extends UILabelModel
{
	public UIBreadCrumbModel()
	{
    BreadCrumb breadcrumb = new BreadCrumb();
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  CastorCaption caption = new CastorCaption();
	  
	  caption.setLabel("breadcrumb/path/to/something");
	  caption.setDimension(new CastorDimension());
	  caption.setHalign(CastorHorizontalAlignment.LEFT);
	  
    breadcrumb.setCaption(caption);
	  choice.setBreadCrumb(breadcrumb);
	  
	  getCastor().setVisible(true);
	  getCastor().setCastorGuiElementChoice(choice);
	  }
	
	/**
   * 
   */
  protected UIBreadCrumbModel(JacobModel jacob,UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob,container, group, guiElement);
  }


  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(ID_PROPERTY_DELIMITER, "Delimiter", PROPERTYGROUP_COMMON);
    
    return descriptors;
  }
  
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName ==ID_PROPERTY_DELIMITER)
      setDelimiter((String) val);
    else
      super.setPropertyValue(propName, val);
  }
  
  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_DELIMITER)
      return getDelimiter();

    return super.getPropertyValue(propName);
  }
  
  public void setDelimiter(String l)
  {
    String save = getDelimiter();
    if(save.equals(l))
      return;
    getCastorBreadCrumb().setDelimiter(l);
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, save, l);
    
    // bestehendes Label an den neuen Delimiter anpassen
    String label = getLabel();
    label = StringUtil.replace(label,save,l);
    setLabel(label);
  }
  
  public String getDelimiter()
  {
    return getCastorBreadCrumb().getDelimiter();
  }

  public String getDefaultNameSuffix()
  {
    return "BreadCrumb";
  }
   
  public String getTemplateFileName()
  {
    return "IBreadCrumbEventHandler.java";
  }

  protected void setCastorEventHandler(String event)
  {
    getCastor().setEventHandler(event);
  }  

  protected String getCastorEventHandler()
  {
    return getCastor().getEventHandler();
  }
  
  protected BreadCrumb getCastorBreadCrumb()
  {
    return getCastor().getCastorGuiElementChoice().getBreadCrumb();
  }
  
  protected CastorCaption getCastorCaption()
  {
    return getCastorBreadCrumb().getCaption();
  }
 }
