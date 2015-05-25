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

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextInput;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 * 
 */
public class UIDBLongTextModel extends UIGroupElementModel implements UIInputElementModel, UIICaptionProviderModel, UIDBLocalInputFieldModel
{
	private static String[] textmodes = new String[]{TextInputModeType.APPEND.toString(),
																										TextInputModeType.FULLEDIT.toString(),
																										TextInputModeType.PREPEND.toString(),
																										TextInputModeType.READONLY.toString()
																								   };
  private UICaptionModel captionModel;

  public UIDBLongTextModel()
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
    dim.setHeight(50);
    caption.setDimension(dim);
    text.setCaption(caption);
    caption.setLabel("Caption");
    input.setLongTextInput(text);
    choice.setLocalInputField(input);
    getCastor().setCastorGuiElementChoice(choice);
    getCastor().setVisible(true);
  }

  /**
   * 
   */
  public UIDBLongTextModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
    captionModel = new UICaptionModel(jacob, container, this);
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

  public String getDefaultDbType()
  {
    return FieldModel.DBTYPE_LONGTEXT;
  }

  public String getDefaultNameSuffix()
  {
    return StringUtils.capitalise(getFieldModel().getName());
  }

  public String getDefaultCaption()
  {
    return getFieldModel().getLabel();
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
    String key = getFieldModel().getLabel();
    if (key.startsWith("%"))
      return key.substring(1);
    return (getFieldModel().getTableModel().getName() + getJacobModel().getSeparator() + getFieldModel().getName()).toUpperCase();
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
    // des ELements auf die neue Größe angepasst.
    //
    if (save.width == 0 && save.height == 0)
    {
      getCastorCaption().getDimension().setHeight(DEFAULT_CAPTION_HEIGHT);
      getCastorCaption().getDimension().setWidth(DEFAULT_CAPTION_WIDTH);
    }
    getCastorLongTextInput().getDimension().setHeight(DEFAULT_CAPTION_HEIGHT);
    getCastorLongTextInput().getDimension().setWidth(DEFAULT_CAPTION_HEIGHT);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }

  public Point getLocation()
  {
    return new Point(getCastorLongTextInput().getDimension().getX(), getCastorLongTextInput().getDimension().getY());
  }

  public Dimension getSize()
  {
    return new Dimension(getCastorLongTextInput().getDimension().getWidth(), getCastorLongTextInput().getDimension().getHeight());
  }
  
  protected boolean hasWordwrap()
  {
    return getCastorLongTextInput().getWordWrap();
  }

  protected void setWordwrap(boolean flag)
  {
    getCastorLongTextInput().setWordWrap(flag);
  }
  
  protected void setMode(TextInputModeType mode)
  {
    getCastorLongTextInput().setMode(mode);
  }
  
  protected TextInputModeType getMode()
  {
    return getCastorLongTextInput().getMode();
  }
  
  /**
   * true => Die ï¿½berschrift wird als eigenstï¿½ndiges Element im Formlayout
   * gezeichnet false => Das Model zeichnet die Caption selbst.
   */
  public boolean isCaptionExtern()
  {
    return true;
  }

  public UICaptionModel getCaptionModel()
  {
    if (captionModel == null)
      captionModel = new UICaptionModel(getJacobModel(), getGroupContainerModel(), this);
    return captionModel;
  }

  @Override
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 3];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length  ] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FIELD   , "Field", (String[]) getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length+1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_WORDWRAP, "Word wrap",new String[]{"true","false"}, PROPERTYGROUP_COMMON);
    if(getFieldModel().getReadonly())
      descriptors[superDescriptors.length+2] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_TEXTMODE, "Mode", PROPERTYGROUP_DB);
    else
      descriptors[superDescriptors.length+2] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_TEXTMODE, "Mode",textmodes, PROPERTYGROUP_DB);
    
    return descriptors;
  }

  @Override
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName == ID_PROPERTY_FIELD)
      setField(((Integer) val).intValue());
    else if(propName == ID_PROPERTY_WORDWRAP)
      setWordwrap(((Integer) val).intValue()==0);
    else if(propName == ID_PROPERTY_TEXTMODE)
    {
    	int index = ((Integer) val).intValue();
      setMode(TextInputModeType.valueOf(textmodes[index]));
    }
    else
      super.setPropertyValue(propName, val);
  }

  @Override
  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_FIELD)
      return new Integer(getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).indexOf(getFieldModel().getName()));
    if(propName == ID_PROPERTY_WORDWRAP)
      return new Integer(hasWordwrap()?0:1);
    if(propName == ID_PROPERTY_TEXTMODE)
    {
      if(getFieldModel().getReadonly())
        return TextInputModeType.READONLY.toString();
      else
        return new Integer(Arrays.binarySearch(textmodes,getMode().toString()));
    }
    return super.getPropertyValue(propName);
  }

  private LongTextInput getCastorLongTextInput()
  {
    return getCastor().getCastorGuiElementChoice().getLocalInputField().getLongTextInput();
  }

  public FieldModel getFieldModel()
  {
    if (getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField() == null)
    {
      String defaultField = getGroupModel().getTableAliasModel().getFieldNames(this).get(0).toString();
      getCastor().getCastorGuiElementChoice().getLocalInputField().setTableField(defaultField);
      getCastor().getCastorGuiElementChoice().getLocalInputField().getLongTextInput().setMode(TextInputModeType.FULLEDIT);
      getCastor().getCastorGuiElementChoice().getLocalInputField().getLongTextInput().setInForm(false);
      getCastor().getCastorGuiElementChoice().getLocalInputField().getLongTextInput().setHtmlInput(false);
    }
    return getGroupModel().getTableAliasModel().getFieldModel(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField());
  }

  public void setField(int index)
  {
    FieldHelper.setField(this, index);
    if(getFieldModel().getReadonly())
      setMode(TextInputModeType.READONLY);
  }

  public void setField(String fieldName)
  {
    FieldHelper.setField(this, fieldName);
    if(getFieldModel().getReadonly())
      setMode(TextInputModeType.READONLY);
  }

  @Override
	public void renameFieldReference(FieldModel field, String fromName, String toName)
	{
	  FieldHelper.renameFieldReference(this, field, fromName, toName);
	}


  @Override
  public void renameI18NKey(String fromName, String toName)
  {
    if (getCastorCaption() != null && getCastorCaption().getLabel().equals(fromName))
      setCaption(toName); // firePropertyChangeEvent(!!!) to update the GUI-Editor
  }
  
  @Override
	public String getError()
  {
    if (getFieldModel() == getFieldModel().getTableModel().NULL_FIELD)
      return "LongText input field [" + getName() + "] has not a valid reference to a column in the table [" + getFieldModel().getTableModel().getName() + "]";
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


  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getFieldModel())
      result.addReferences(this);
  }
}
