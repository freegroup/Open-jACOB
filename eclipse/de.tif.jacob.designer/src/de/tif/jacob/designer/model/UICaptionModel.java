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

import org.apache.commons.lang.StringUtils;
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
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontStyleType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontWeightType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ColorPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.util.ColorUtil;
import de.tif.jacob.util.StringUtil;

/**
 * 
 */
public class UICaptionModel extends UIGroupElementModel implements UIICaptionProviderModel, IFontProviderModel 
{
  final UIICaptionProviderModel parent;
  
  
  /**
   * 
   */
  protected UICaptionModel(JacobModel jacob,  UIGroupContainer container, UIICaptionProviderModel parent)
  {
    super(jacob, container, parent.getGroupModel(), null);
    this.parent = parent;
    parent.getCaption();// important! first init
  }

  public String getDefaultNameSuffix()
  {
    return "Caption";
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
    return parent.suggestI18NKey();
  }

  public void createMissingI18NKey()
  {
    String  label = getCaption();
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
    return StringUtil.saveEquals(key,getCaption());
  }
  
  /*
   * Entweder wird das Castor Element dieser Caption genommen, oder wenn dies ausgehängt wurde
   * (z.B. man hat in der Capton ein leer String eingetragen) dann wird auf das temporär verfügbare
   * Castor Element des Vaters zugegriffen.
   * return
   * @see de.tif.jacob.designer.model.UIICaptionProviderModel#getCastorCaption()
   */
  public CastorCaption getCastorCaption()
  {
    return parent.getCastorCaption();
  }

  public void setCastorCaption(CastorCaption caption)
  {
    parent.setCastorCaption(caption);
  }

  public String getCastorName()
  {
    return getCaptionProvider().getName()+"Caption";
  }
  
  public void  generateHookClassName() throws Exception
  {
     String save = getCastorEventHandler();
     setCastorEventHandler("jacob.event.ui."+getGroupTableAlias()+"."+StringUtils.capitalise(getName()));
     firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED,save,getCastorEventHandler());
  }

  protected void setCastorEventHandler(String event)
  {
    getCastorCaption().setEventHandler(event);
  }  

  protected String getCastorEventHandler()
  {
    return getCastorCaption().getEventHandler();
  }
    
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorCaption().getDimension();
  }

  public UICaptionModel getCaptionModel()
  {
    return this;
  }

  public String getDefaultCaption()
  {
    return "Caption";
  }

  public boolean isCaptionExtern()
  {
    return false;
  }


  public void setSize(Dimension size)
  {
    Dimension save = getSize();
    getCastorCaption().getDimension().setHeight(size.height);
    getCastorCaption().getDimension().setWidth(size.width);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public int getFontSize()
  {
    if(getCastorCaption().getFont()==null)
      return FontDefinition.DEFAULT_FONT_SIZE;
    return getCastorCaption().getFont().getSize();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public Color getColor()
  {
    if(getCastorCaption()==null || getCastorCaption().getColor()==null)
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
    if(getCastorCaption()==null || getCastorCaption().getFont()==null)
      return FontDefinition.DEFAULT_FONT_FAMILY;
    return getCastorCaption().getFont().getFamily().toString();
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
    if(getCastorCaption()==null || getCastorCaption().getFont()==null)
      return FontDefinition.DEFAULT_FONT_WEIGHT;
    return getCastorCaption().getFont().getWeight().toString();
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
    if(getCastorCaption()==null || getCastorCaption().getFont()==null)
      return FontDefinition.DEFAULT_FONT_STYLE;
    return getCastorCaption().getFont().getStyle().toString();
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

  /**
   * Returns a new Point object of the caption position
   */
  public Point getLocation()
  {
    return new Point(getCastorCaption().getDimension().getX(), getCastorCaption().getDimension().getY());
  }

  public Dimension getSize()
  {
    return new Dimension(getCastorCaption().getDimension().getWidth(), getCastorCaption().getDimension().getHeight());
  }

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getAlign()
   */
  public String getAlign()
  {
    if(getCastorCaption().getHalign()==null)
      return ALIGN_RIGHT;
    return getCastorCaption().getHalign().toString();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#setAlign(java.lang.String)
   */
  public void setAlign(String align)
  {
    String save = getAlign();
    if(save.equals(align))
      return;
    
    getCastorCaption().setHalign(CastorHorizontalAlignment.valueOf(align));
    firePropertyChange(PROPERTY_ALIGN_CHANGED, save, align);
  }
  

  public String getTemplateFileName()
  {
    return "ICaptionEventHandler.java";
  }



  public String getError()
  {
    return null;
  }

  
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
	  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
	  
	  IPropertyDescriptor[]  descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		
		descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(ID_PROPERTY_CAPTION,   "Caption", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 1] = new CheckboxPropertyDescriptor(ID_PROPERTY_ELLIPSIS, "Use Ellipsis", PROPERTYGROUP_COMMON);
		return descriptors;
  }
  
	public void setPropertyValue(Object propName, Object val)
	{
		if (propName == ID_PROPERTY_CAPTION)
			setCaption((String) val);
    else if (propName == ID_PROPERTY_ELLIPSIS)
      setEllipsis(((Boolean) val).booleanValue());
		else
			super.setPropertyValue(propName, val);
	}
	
	public Object getPropertyValue(Object propName)
	{
    if (propName == ID_PROPERTY_CAPTION)
      return getCaption();
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
	
  public void renameI18NKey(String fromName, String toName)
  {
    try
    {
      if (getCastorCaption() != null && getCastorCaption().equals(fromName))
        setCaption(toName);
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
  }

  public String getWarning()
  {
    String caption = getCaption();
    if (caption != null && caption.startsWith("%") && getJacobModel().hasI18NKey(caption.substring(1)) == false)
      return "No localization entry for [" + caption.substring(1) + "] found";
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

  public final UIICaptionProviderModel getCaptionProvider()
  {
    return parent;
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
  
  // ------------------
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void removeCastorProperty(CastorProperty property)
  {
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
