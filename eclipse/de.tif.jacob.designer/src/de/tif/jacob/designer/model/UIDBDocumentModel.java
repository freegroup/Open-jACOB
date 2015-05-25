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
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFont;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.DocumentInput;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontStyleType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontWeightType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIDBDocumentModel extends UIGroupElementModel implements UIDBLocalInputFieldModel, UIInputElementModel, UIICaptionProviderModel, IFontProviderModel
{
	private UICaptionModel captionModel ;

	public UIDBDocumentModel()
	{
	  super(null, null, null,new CastorGuiElement());
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  LocalInputField        input  = new LocalInputField();
	  DocumentInput          doc    = new DocumentInput();
	  CastorCaption          caption= new CastorCaption();
	  caption.setHalign(CastorHorizontalAlignment.valueOf(JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, ALIGN_LEFT)));
	  
	  doc.setDimension(new CastorDimension());
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(200);
	  dim.setHeight(50);
	  caption.setDimension(dim);
	  doc.setCaption(caption);
	  caption.setLabel("Caption");
	  input.setDocumentInput(doc);
	  choice.setLocalInputField(input);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
	}
	
	/**
   * 
   */
  public UIDBDocumentModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container,group, guiElement);
    captionModel = new UICaptionModel(jacob,container, this);
  }
	
	/**
	 * true  => Die ï¿½berschrift wird als eigenstï¿½ndiges Element im Formlayout gezeichnet
	 * false => Das Model zeichnet die Caption selbst.
	 */
  public boolean   isCaptionExtern()
  {
    return true;
  }

  public void setCastorCaption(CastorCaption caption)
  {
    getCastorDocument().setCaption(caption);
  }
  
  public CastorCaption getCastorCaption()
  {
    return getCastorDocument().getCaption();
  }

  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorDocument().getDimension();
  }
  
  public String getDefaultDbType()
  {
    return FieldModel.DBTYPE_DOCUMENT;
  }
  
  public String suggestI18NKey()
  {
    String key = getFieldModel().getLabel();
    if(key.startsWith("%"))
      return key.substring(1);
    
    return (getFieldModel().getTableModel().getName()+getJacobModel().getSeparator()+getFieldModel().getName()).toUpperCase();
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
  
  @Override
  public void renameI18NKey(String fromName, String toName)
  {
    if (getCastorCaption() != null && getCastorCaption().getLabel().equals(fromName))
      setCaption(toName); // firePropertyChangeEvent(!!!) to update the GUI-Editor
  }
  

  public FieldModel getFieldModel()
	{
	  if(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField()==null)
	  {
	    String defaultField = getGroupModel().getTableAliasModel().getFieldNames(this).get(0).toString();
	    getCastor().getCastorGuiElementChoice().getLocalInputField().setTableField(defaultField);
	  }
	  return getGroupModel().getTableAliasModel().getFieldModel(getCastor().getCastorGuiElementChoice().getLocalInputField().getTableField());
	}
	
	public void setField(int index)
	{
	  FieldHelper.setField(this, index);
	}

	public void setField(String fieldName)
	{
	  FieldHelper.setField(this, fieldName);
	}
	

  public String getDefaultNameSuffix()
  {
    return "Document";
  }
    
  
  public String getDefaultCaption()
  {
    return getFieldModel().getLabel();
  }
  
  public String getDefaultCaptionAlign()
  {
    return JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, ALIGN_LEFT);
  }
  
	public void renameFieldReference(FieldModel field, String fromName, String toName)
	{
	  FieldHelper.renameFieldReference(this, field, fromName, toName);
	}

	public String getTemplateFileName()
  {
    return "IDocumentFieldEventHandler.java";
  }
  
	
	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		// Falls der ale Wert eine Dimension von [0,0] hat, wird die Caption
		// des Elements auf die neue Grï¿½ï¿½e angepasst.
		//
		if(save.width==0 && save.height==0)
		{
		  getCastorCaption().getDimension().setHeight(size.height);
		  getCastorCaption().getDimension().setWidth(DEFAULT_CAPTION_WIDTH);
		}
		getCastorDocument().getDimension().setHeight(size.height);
		getCastorDocument().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

	public Point getLocation()
	{
	  return new Point(getCastorDocument().getDimension().getX(), getCastorDocument().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCastorDocument().getDimension().getWidth(), getCastorDocument().getDimension().getHeight());
	}
	
	public UICaptionModel getCaptionModel()
	{
	  if(captionModel==null)
		  captionModel = new UICaptionModel(getJacobModel(), getGroupContainerModel(),this);
	  return captionModel;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
	  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
	  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FIELD, "Field",(String[])getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).toArray(new String[0]), PROPERTYGROUP_DB);
		
		return descriptors;
	}

	public void setPropertyValue(Object propName, Object val)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FIELD))
		  setField(((Integer)val).intValue());
		else
			super.setPropertyValue(propName, val);
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FIELD))
		  return new Integer(getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).indexOf(getFieldModel().getName()));

		return super.getPropertyValue(propName);
	}
	
	
	private DocumentInput getCastorDocument()
	{
	  return getCastor().getCastorGuiElementChoice().getLocalInputField().getDocumentInput();
	}
	
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public int getFontSize()
  {
    if(getCastorDocument().getFont()==null)
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
    if(getCastorDocument().getFont()==null)
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
    if(getCastorDocument().getFont()==null)
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
    if(getCastorDocument().getFont()==null)
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
    if(getCastorDocument().getFont()==null)
      return;
    
    CastorFont font = getCastorDocument().getFont();
    if(font.getSize()!=FontDefinition.DEFAULT_FONT_SIZE)
      return;
    if(!font.getFamily().equals(FontDefinition.DEFAULT_FONT_FAMILY))
      return;
    if(!font.getStyle().equals(FontDefinition.DEFAULT_FONT_STYLE))
      return;
    if(!font.getWeight().equals(FontDefinition.DEFAULT_FONT_WEIGHT))
      return;
    getCastorDocument().setFont(null);
    
  }

  private CastorFont getCastorFont()
  {
    if(getCastorDocument().getFont()==null)
    {
      CastorFont font = new CastorFont();
      // derzeitige defaultgröße holen und eintragen
      // (die Anderen Werte werden durch Castro sinvoll aus dem jad.xsd geholt)
      font.setSize(getFontSize());
      
      // ...und erst jetzt den font einhängen
      getCastorDocument().setFont(font);
    }
    return getCastorDocument().getFont();
  }
  
  public String getError()
  {
    if(getFieldModel() == getFieldModel().getTableModel().NULL_FIELD)
      return "Document field ["+getName()+"] has not a valid reference to a column in the table ["+getFieldModel().getTableModel().getName()+"]";
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
  
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getFieldModel())
      result.addReferences(this);
  }
}
