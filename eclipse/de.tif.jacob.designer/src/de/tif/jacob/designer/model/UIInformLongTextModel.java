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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFont;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextInput;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontStyleType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontWeightType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.util.StringUtil;
/**
 * 
 */
public class UIInformLongTextModel extends UIGroupElementModel implements UIInputElementModel, UIICaptionProviderModel, IFontProviderModel
{
  private UICaptionModel captionModel;

  public UIInformLongTextModel()
  {
    super(null, null, null, new CastorGuiElement());
    CastorGuiElementChoice choice = new CastorGuiElementChoice();
    LocalInputField input = new LocalInputField();
    LongTextInput text = new LongTextInput();
    CastorCaption caption = new CastorCaption();
	  caption.setHalign(CastorHorizontalAlignment.valueOf(JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, CastorHorizontalAlignment.LEFT.toString())));
    text.setDimension(new CastorDimension());
    CastorDimension dim = new CastorDimension();
    dim.setWidth(200);
    dim.setHeight(20);
    caption.setDimension(dim);
    
    text.setCaption(caption);
    text.setInForm(true);
    text.setHtmlInput(false);
    caption.setLabel("Caption");
    input.setLongTextInput(text);
    choice.setLocalInputField(input);
    getCastor().setCastorGuiElementChoice(choice);
    getCastor().setVisible(true);
  }

  /**
   * 
   */
  public UIInformLongTextModel(JacobModel jacob,  UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
    captionModel = new UICaptionModel(jacob, container, this);
  }

  /**
   * true => Die ï¿½berschrift wird als eigenstï¿½ndiges Element im Formlayout
   * gezeichnet false => Das Model zeichnet die Caption selbst.
   */
  public boolean isCaptionExtern()
  {
    return true;
  }

  public void setCastorCaption(CastorCaption caption)
  {
    getCastorLongTextInput().setCaption(caption);
  }

  public CastorCaption getCastorCaption()
  {
    return getCastorLongTextInput().getCaption();
  }

  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorLongTextInput().getDimension();
  }
  
  public String suggestI18NKey()
  {
    String key = getGroupModel().getLabel();
    if (key.startsWith("%"))
      key = key.substring(1);
    return (key + getJacobModel().getSeparator() + getCaption()).toUpperCase();
  }

  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return false;
  }
  
  @Override
  public void renameI18NKey(String fromName, String toName)
  {
    if (getCastorCaption() != null && getCastorCaption().getLabel().equals(fromName))
      setCaption(toName); // firePropertyChangeEvent(!!!) to update the GUI-Editor
  }
  

  public int getFontSize()
  {
    if(getCastorLongTextInput().getFont()==null)
      return FontDefinition.DEFAULT_FONT_SIZE;
    return getCastorFont().getSize();
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
    if(getCastorLongTextInput().getFont()==null)
      return FontDefinition.DEFAULT_FONT_FAMILY;
    return getCastorFont().getFamily().toString();
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
    if(getCastorLongTextInput().getFont()==null)
      return FontDefinition.DEFAULT_FONT_WEIGHT;
    return getCastorFont().getWeight().toString();
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
    if(getCastorLongTextInput().getFont()==null)
      return FontDefinition.DEFAULT_FONT_STYLE;
    return getCastorFont().getStyle().toString();
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

  public String getAlign()
  {
    return ALIGN_LEFT;
  }
  
  public void setAlign(String align)
  {
  }


  private CastorFont getCastorFont()
  {
    if(getCastorLongTextInput().getFont()==null)
    {
      CastorFont font = new CastorFont();
      // derzeitige defaultgröße holen und eintragen
      // (die Anderen Werte werden durch Castro sinvoll aus dem jad.xsd geholt)
      font.setSize(getFontSize());
      
      // ...und erst jetzt den font einhängen
      getCastorLongTextInput().setFont(font);
    }
    return getCastorLongTextInput().getFont();
  }
    
  /*
   * Falls die Fontdefinitionen den defaultwerten entsprechen, dann wird der
   * CastorFont eintrag weg gelöscht.
   * 
   */
  private void checkFontDefault()
  {
    if(getCastorCaption()==null || getCastorCaption().getFont()==null)
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
  
  public String getDefaultNameSuffix()
  {
    return "InformLongtext";
  }

  public String getDefaultCaption()
  {
    return "Caption";
  }
  
  @Override
  public String getTemplateFileName()
  {
    return "IInformLongTextEventHandler.java";
  }
  
  /**
   *
   */
  public String getDefaultCaptionAlign()
  {
    return ALIGN_LEFT;
  }

  public int getDefaultWidth()
  {
    return ObjectModel.DEFAULT_ELEMENT_WIDTH;
  }
  
  @Override
  public Rectangle getDefaultCaptionConstraint(Rectangle parentConstraint)
  {
    Rectangle result = new Rectangle();
    result.x      = parentConstraint.x;
    result.y      = parentConstraint.y-DEFAULT_CAPTION_HEIGHT;
    result.width  = DEFAULT_CAPTION_WIDTH;
    result.height = DEFAULT_CAPTION_HEIGHT;
    
    return result;
  }
  
  @Override
  public int getDefaultHeight()
  {
    // eine Textbox geht per default über die Höhe von 3 Eingabeelemente
    //
    return DEFAULT_ELEMENT_HEIGHT*3+DEFAULT_ELEMENT_SPACING*2;
  }
  

  @Override
  public void setSize(Dimension size)
  {
    Dimension save = getSize();
    // Falls der ale Wert eine Dimension von [0,0] hat, wird die Caption
    // des ELements auf die neue Grï¿½ï¿½e angepasst.
    //
    if (save.width == 0 && save.height == 0)
    {
      getCastorCaption().getDimension().setHeight(DEFAULT_CAPTION_HEIGHT);
      getCastorCaption().getDimension().setWidth(DEFAULT_CAPTION_WIDTH);
    }
    getCastorLongTextInput().getDimension().setHeight(size.height);
    getCastorLongTextInput().getDimension().setWidth(size.width);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }

  
  @Override
  public Point getLocation()
  {
    return new Point(getCastorLongTextInput().getDimension().getX(), getCastorLongTextInput().getDimension().getY());
  }

  @Override
  public Dimension getSize()
  {
    return new Dimension(getCastorLongTextInput().getDimension().getWidth(), getCastorLongTextInput().getDimension().getHeight());
  }

  public UICaptionModel getCaptionModel()
  {
    if (captionModel == null)
      captionModel = new UICaptionModel(getJacobModel(), getGroupContainerModel(), this);
    return captionModel;
  }

  protected boolean hasWordwrap()
  {
    return getCastorLongTextInput().getWordWrap();
  }

  protected void setWordwrap(boolean flag)
  {
    getCastorLongTextInput().setWordWrap(flag);
  }
  

  @Override
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_WORDWRAP, "Word wrap",new String[]{"true","false"}, PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length+1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_EDITMODE, "Edit mode",EDIT_MODES.toArray(new String[0]), PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length+1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_CONTENTTYPE, "Content Type",CONTENT_TYPES.toArray(new String[0]), PROPERTYGROUP_COMMON);
    
    return descriptors;
  }

  @Override
  public void setPropertyValue(Object propName, Object val)
  {
    if(propName == ID_PROPERTY_WORDWRAP)
      setWordwrap(((Integer) val).intValue()==0);
    else if(propName == ID_PROPERTY_EDITMODE)
      setEditMode(EDIT_MODES.get(((Integer) val).intValue()));
    else if(propName == ID_PROPERTY_CONTENTTYPE)
      setContentType(CONTENT_TYPES.get(((Integer) val).intValue()));
    else
      super.setPropertyValue(propName, val);
  }


  
  @Override
  public Object getPropertyValue(Object propName)
  {
    if(propName == ID_PROPERTY_WORDWRAP)
      return new Integer(hasWordwrap()?0:1);
    if(propName == ID_PROPERTY_EDITMODE)
      return new Integer(EDIT_MODES.indexOf(getEditMode()));
    if(propName == ID_PROPERTY_CONTENTTYPE)
      return new Integer(CONTENT_TYPES.indexOf(getContentType()));
    return super.getPropertyValue(propName);
  }

  private void setEditMode(String string)
  {
    getCastorLongTextInput().setMode(TextInputModeType.valueOf(string));
  }
  
  private String getEditMode()
  {
    return getCastorLongTextInput().getMode().toString();
  }



  public void setContentType(String string)
  {
    String save = this.getContentType();
    
    if(StringUtil.saveEquals(string,save))
      return;
    
    getCastorLongTextInput().setContentType(LongTextInputContentTypeType.valueOf(string));
    
    firePropertyChange(PROPERTY_CONTENTYPE_CHANGED, save, string);
  }
  
  public String getContentType()
  {
    return getCastorLongTextInput().getContentType().toString();
  }


 
  private LongTextInput getCastorLongTextInput()
  {
    return getCastor().getCastorGuiElementChoice().getLocalInputField().getLongTextInput();
  }

  @Override
  public String getError()
  {
    return null;
  }

  @Override
  public String getWarning()
  {
    return null;
  }

  @Override
  public String getInfo()
  {
    return null;
  }

  @Override
  public boolean isInUse()
  {
    return true;
  }
}
