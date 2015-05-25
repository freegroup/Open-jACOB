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

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFont;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.ComboBox;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontStyleType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontWeightType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIComboboxModel extends  UIGroupElementModel implements UIInputElementModel, UIICaptionProviderModel, IFontProviderModel
{
	private UICaptionModel captionModel ;

	public UIComboboxModel()
	{
	  super(null, null,null, new CastorGuiElement());
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  LocalInputField        input  = new LocalInputField();
	  ComboBox               box    = new ComboBox();
	  CastorCaption          caption= new CastorCaption();
	  caption.setHalign(CastorHorizontalAlignment.valueOf(JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, CastorHorizontalAlignment.LEFT.toString())));
	  
	  box.setDimension(new CastorDimension());
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(200);
	  dim.setHeight(50);
	  caption.setDimension(dim);
	  box.setCaption(caption);
	  caption.setLabel("Caption");
	  input.setComboBox(box);
	  choice.setLocalInputField(input);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
	}
	
	/**
   * 
   */
  protected UIComboboxModel(JacobModel jacob,  UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
	  captionModel = new UICaptionModel(jacob,container, this);
  }

  public void setCastorCaption(CastorCaption caption)
  {
    getCastorComboBox().setCaption(caption);
  }
  
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorComboBox().getDimension();
  }
  
  public CastorCaption getCastorCaption()
  {
    return getCastorComboBox().getCaption();
  }


  public String getDefaultNameSuffix()
  {
    return "Combobox";
  }
  
  public String getDefaultCaption()
  {
    return "Caption";
  }
  
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length ] = new CheckboxPropertyDescriptor(ID_PROPERTY_SEARCH_NULL, "Allow NULL Search", PROPERTYGROUP_FEATURES);
    descriptors[superDescriptors.length + 1] = new CheckboxPropertyDescriptor(ID_PROPERTY_SEARCH_NOT_NULL, "Allow !NULL Search", PROPERTYGROUP_FEATURES);

    return descriptors;
  }

  /**
   * 
   */
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName == ID_PROPERTY_SEARCH_NOT_NULL)
      setAllowNotNullSearch(((Boolean) val).booleanValue());
    else if (propName == ID_PROPERTY_SEARCH_NULL)
      setAllowNullSearch(((Boolean) val).booleanValue());
    else
      super.setPropertyValue(propName, val);
  }

  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_SEARCH_NOT_NULL)
      return new Boolean(getAllowNotNullSearch());
    if (propName == ID_PROPERTY_SEARCH_NULL)
      return new Boolean(getAllowNullSearch());

    return super.getPropertyValue(propName);
  }
  
  public void setAllowNullSearch(boolean flag)
  {
    boolean save = getAllowNullSearch();
    if (flag==save)
      return;

    getCastorComboBox().setAllowNullSearch(flag);
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, null , this);
  }

  private boolean getAllowNullSearch()
  {
    return getCastorComboBox().getAllowNullSearch();
  }

  public void setAllowNotNullSearch(boolean flag)
  {
    boolean save = getAllowNotNullSearch();
    if (flag==save)
      return;

    getCastorComboBox().setAllowNotNullSearch(flag);
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, null , this);
  }

  private boolean getAllowNotNullSearch()
  {
    return getCastorComboBox().getAllowNotNullSearch();
  }
  
  /**
   * Per Default wird die Einstellung aus den Preferences genommen
   *
   */
  public String getDefaultCaptionAlign()
  {
    return JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, ALIGN_LEFT);
  }

  public String suggestI18NKey()
  {
    String key = getGroupModel().getLabel();
    if (key.startsWith("%"))
      key = key.substring(1);
    return (key + getJacobModel().getSeparator() + getCaption()).toUpperCase();
  }


  @Override
  public void renameI18NKey(String fromName, String toName)
  {
    if (getCastorCaption() != null && getCastorCaption().getLabel().equals(fromName))
      setCaption(toName); // firePropertyChangeEvent(!!!) to update the GUI-Editor
  }

  
  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(key,getCaption());
  }
  
	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		// Falls der ale Wert eine Dimension von [0,0] hat, wird die Caption
		// des ELements auf die neue Grï¿½ï¿½e angepasst.
		//
		if(save.width==0 && save.height==0)
		{
		  getCastorCaption().getDimension().setHeight(size.height);
		  getCastorCaption().getDimension().setWidth(DEFAULT_CAPTION_WIDTH);
		}
		getCastorComboBox().getDimension().setHeight(size.height);
		getCastorComboBox().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

	public Point getLocation()
	{
	  return new Point(getCastorComboBox().getDimension().getX(), getCastorComboBox().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCastorComboBox().getDimension().getWidth(), getCastorComboBox().getDimension().getHeight());
	}

	public UICaptionModel getCaptionModel()
	{
	  if(captionModel==null)
		  captionModel = new UICaptionModel(getJacobModel(),getGroupContainerModel(),this);
	  return captionModel;
	}
	
	protected ComboBox getCastorComboBox()
	{
	  return getCastor().getCastorGuiElementChoice().getLocalInputField().getComboBox();
	}
	
	/**
	 * true  => Die Üœberschrift wird als eigenständiges Element im Formlayout gezeichnet
	 * false => Das Model zeichnet die Caption selbst.
	 */
  public boolean   isCaptionExtern()
  {
    return true;
  }

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public int getFontSize()
  {
    if(getCastorComboBox().getFont()==null)
      return FontDefinition.DEFAULT_FONT_SIZE;
    return getCastorFont().getSize();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#setFontSize(int)
   */
  public void setFontSize(int size)
  {
    int save = getFontSize();
    if(save == size)
      return;
    
    getCastorFont().setSize(size);
    firePropertyChange(PROPERTY_FONT_CHANGED,new Integer(save), new Integer(size));
    checkFontDefault();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontFamily()
   */
  public String getFontFamily()
  {
    if(getCastorComboBox().getFont()==null)
      return FontDefinition.DEFAULT_FONT_FAMILY;
    return getCastorFont().getFamily().toString();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#setFontFamily(java.lang.String)
   */
  public void setFontFamily(String family)
  {
    String save = getFontFamily();
    if(save.equals(family))
      return;
    
    getCastorFont().setFamily(CastorFontFamilyType.valueOf(family));
    firePropertyChange(PROPERTY_FONT_CHANGED,save, family);
    checkFontDefault();
  }

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontWeight()
   */
  public String getFontWeight()
  {
    if(getCastorComboBox().getFont()==null)
      return FontDefinition.DEFAULT_FONT_WEIGHT;
    return getCastorFont().getWeight().toString();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#setFontWeight(java.lang.String)
   */
  public void setFontWeight(String weight)
  {
    String save = getFontWeight();
    if(save.equals(weight))
      return;
    
    getCastorFont().setWeight(CastorFontWeightType.valueOf(weight));
    firePropertyChange(PROPERTY_FONT_CHANGED,save, weight);
    checkFontDefault();
  }

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontStyle()
   */
  public String getFontStyle()
  {
    if(getCastorComboBox().getFont()==null)
      return FontDefinition.DEFAULT_FONT_STYLE;
    return getCastorFont().getStyle().toString();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#setFontStyle(java.lang.String)
   */
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
   * @see de.tif.jacob.designer.model.IFontProviderModel#getAlign()
   */
  public String getAlign()
  {
      return ALIGN_LEFT;
//    return getCastorCaption().getHalign().toString();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#setAlign(java.lang.String)
   */
  public void setAlign(String align)
  {
//    String save = getAlign();
//    if(save.equals(align))
//      return;
//    
//    getCastorCaption().setHalign(CastorHorizontalAlignment.valueOf(align));
//    firePropertyChange(PROPERTY_ALIGN_CHANGED, save, align);
  }
  
  /*
   * Falls die Fontdefinitionen den defaultwerten entsprechen, dann wird der
   * CastorFont eintrag weg gelöscht.
   * 
   */
  private void checkFontDefault()
  {
    if(getCastorComboBox().getFont()==null)
      return;
    
    CastorFont font = getCastorComboBox().getFont();
    if(font.getSize()!=FontDefinition.DEFAULT_FONT_SIZE)
      return;
    if(!font.getFamily().equals(FontDefinition.DEFAULT_FONT_FAMILY))
      return;
    if(!font.getStyle().equals(FontDefinition.DEFAULT_FONT_STYLE))
      return;
    if(!font.getWeight().equals(FontDefinition.DEFAULT_FONT_WEIGHT))
      return;
    getCastorComboBox().setFont(null);
    
  }

  private CastorFont getCastorFont()
  {
    if(getCastorComboBox().getFont()==null)
    {
      CastorFont font = new CastorFont();
      // derzeitige defaultgröße holen und eintragen
      // (die Anderen Werte werden durch Castro sinvoll aus dem jad.xsd geholt)
      font.setSize(getFontSize());
      
      // ...und erst jetzt den font einhängen
      getCastorComboBox().setFont(font);
    }
    return getCastorComboBox().getFont();
  }
  
  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "IMutableComboBoxEventHandler.java";
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
}
