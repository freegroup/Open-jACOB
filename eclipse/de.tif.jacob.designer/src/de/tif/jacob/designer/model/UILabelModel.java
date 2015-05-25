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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFont;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.Label;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontStyleType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontWeightType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ColorPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.IntegerPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.util.ColorUtil;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UILabelModel extends UIGroupElementModel implements IFontProviderModel
{
	public UILabelModel()
	{
	  super(null, null, null, new CastorGuiElement());
	  
	  Label label = new Label();
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  CastorCaption caption = new CastorCaption();
	  
	  caption.setLabel("<unset>");
	  caption.setDimension(new CastorDimension());
	  caption.setHalign(CastorHorizontalAlignment.valueOf(JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, CastorHorizontalAlignment.LEFT.toString())));
	  
	  label.setCaption(caption);
	  choice.setLabel(label);
	  
	  getCastor().setVisible(true);
	  getCastor().setCastorGuiElementChoice(choice);
	}
	
	/**
   * 
   */
  protected UILabelModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
  }

  public String getDefaultNameSuffix()
  {
    return "Label";
  }

  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorCaption().getDimension();
  }
	
	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		getCastorCaption().getDimension().setHeight(size.height);
		getCastorCaption().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

	public Point getLocation()
	{
	  return new Point(getCastorCaption().getDimension().getX(), getCastorCaption().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCastorCaption().getDimension().getWidth(), getCastorCaption().getDimension().getHeight());
	}


	public IPropertyDescriptor[] getPropertyDescriptors()
	{
	  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
	  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 1] = new CheckboxPropertyDescriptor(ID_PROPERTY_ELLIPSIS, "Use Ellipsis", PROPERTYGROUP_COMMON);
		
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		if (propName ==ID_PROPERTY_LABEL)
			setLabel((String) val);
    else if (propName == ID_PROPERTY_ELLIPSIS)
      setEllipsis(((Boolean) val).booleanValue());
		else
			super.setPropertyValue(propName, val);
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName ==ID_PROPERTY_LABEL)
			return getLabel();
    if (propName == ID_PROPERTY_ELLIPSIS)
      return new Boolean(getEllipsis());

		return super.getPropertyValue(propName);
	}

  public void setEllipsis(boolean flag)
  {
    boolean save = getEllipsis();
    if (flag==save)
      return;

    getCastorCaption().setEllipsis(flag);
  }

  public boolean getEllipsis()
  {
    return getCastorCaption().getEllipsis();
  }
  
	
	public void setLabel(String l)
	{
		String save = getLabel();
		if(save.equals(l))
		  return;
	  getCastorCaption().setLabel(l);
		firePropertyChange(PROPERTY_LABEL_CHANGED, save, l);
		getGroupModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED,null,this);
		getGroupModel().getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED,null,this);
	}
	
  public int getFontSize()
  {
    if(getCastorCaption().getFont()==null)
      return FontDefinition.DEFAULT_FONT_SIZE;
    return getCastorCaption().getFont().getSize();
  }
  
  public void setFontSize(int size)
  {
    int save = getFontSize();
    if(save == size)
      return;
    
    getCastorFont().setSize(size);
    firePropertyChange(PROPERTY_FONT_CHANGED,new Integer(save), new Integer(size));
    checkFontDefault();
  }
  
  public String getFontFamily()
  {
    if(getCastorCaption().getFont()==null)
      return FontDefinition.DEFAULT_FONT_FAMILY;
    return getCastorCaption().getFont().getFamily().toString();
  }
  
  public void setFontFamily(String family)
  {
    String save = getFontFamily();
    if(save.equals(family))
      return;
    
    getCastorFont().setFamily(CastorFontFamilyType.valueOf(family));
    firePropertyChange(PROPERTY_FONT_CHANGED,save, family);
    checkFontDefault();
  }

  public String getFontWeight()
  {
    if(getCastorCaption().getFont()==null)
      return FontDefinition.DEFAULT_FONT_WEIGHT;
    return getCastorCaption().getFont().getWeight().toString();
  }
  
  public void setFontWeight(String weight)
  {
    String save = getFontWeight();
    if(save.equals(weight))
      return;
    
    getCastorFont().setWeight(CastorFontWeightType.valueOf(weight));
    firePropertyChange(PROPERTY_FONT_CHANGED,save, weight);
    checkFontDefault();
  }

  public String getFontStyle()
  {
    if(getCastorCaption().getFont()==null)
      return FontDefinition.DEFAULT_FONT_STYLE;
    return getCastorCaption().getFont().getStyle().toString();
  }
  
  public void setFontStyle(String style)
  {
    String save = getFontStyle();
    if(save.equals(style))
      return;
    
    getCastorFont().setStyle(CastorFontStyleType.valueOf(style));
    firePropertyChange(PROPERTY_FONT_CHANGED,save, style);
    checkFontDefault();
  }


  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public Color getColor()
  {
    if(getCastorCaption().getColor()==null)
      return ColorConstants.black;
    return ColorUtil.toColor(getCastorCaption().getColor());
  }
  
  public void setColor(Color color)
  {
    Color save = getColor();
    String saveCSS = getCastorCaption().getColor();
    String css  = ColorUtil.toCSS(color);
    
    if(StringUtil.saveEquals(saveCSS,css))
      return;
    
    
    if(Constants.DEFAULT_COLOR.equals(css))
      getCastorCaption().setColor(null);
    else
      getCastorCaption().setColor(css);
    
    firePropertyChange(PROPERTY_COLOR_CHANGED, save, color);
  }
  
    
  private CastorFont getCastorFont()
  {
    if(getCastorCaption().getFont()==null)
    {
      CastorFont font = new CastorFont();
      // derzeitige defaultgröße holen und eintragen
      // (die Anderen Werte werden durch Castro sinvoll aus dem jad.xsd geholt)
      font.setSize(getFontSize());
      
      // ...und erst jetzt den font einhängen
      getCastorCaption().setFont(font);
    }
    return getCastorCaption().getFont();
  }
  
  public void renameI18NKey(String fromName, String toName)
  {
    if (getLabel() != null && getLabel().equals(fromName))
      setLabel(toName);
  }

  public void createMissingI18NKey()
  {
    String label = getLabel();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);
  }
  
  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(key,getLabel());
  }

  public String getLabel()
	{
	  return getCastorCaption().getLabel();
	}
	
  public String getAlign()
  {
    if(getCastorCaption().getHalign()==null)
      return ALIGN_RIGHT;
    return getCastorCaption().getHalign().toString();
  }
  
  public void setAlign(String align)
  {
    String save = getAlign();
    if(save.equals(align))
      return;
    
    getCastorCaption().setHalign(CastorHorizontalAlignment.valueOf(align));
    firePropertyChange(PROPERTY_ALIGN_CHANGED, save, align);
  }

  protected CastorCaption getCastorCaption()
  {
    return getCastor().getCastorGuiElementChoice().getLabel().getCaption();
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
  
  public String getTemplateFileName()
  {
    return "ILabelEventHandler.java";
  }

  protected void setCastorEventHandler(String event)
  {
    getCastorCaption().setEventHandler(event);
  }  

  protected String getCastorEventHandler()
  {
    return getCastorCaption().getEventHandler();
  }
  
  /*
   * Falls die Fontdefinitionen den defaultwerten entsprechen, dann wird der
   * CastorFont eintrag weg gelöscht.
   * 
   */
  private void checkFontDefault()
  {
    if(getCastorCaption().getFont()==null)
      return;
    
    CastorFont font = getCastorCaption().getFont();
    if(font.getSize()!=FontDefinition.DEFAULT_FONT_SIZE)
      return;
    if(!font.getFamily().equals(FontDefinition.DEFAULT_FONT_FAMILY))
      return;
    if(!font.getStyle().equals(FontDefinition.DEFAULT_FONT_STYLE))
      return;
    if(!font.getWeight().equals(FontDefinition.DEFAULT_FONT_WEIGHT))
      return;
    getCastorCaption().setFont(null);
    
  }
}
