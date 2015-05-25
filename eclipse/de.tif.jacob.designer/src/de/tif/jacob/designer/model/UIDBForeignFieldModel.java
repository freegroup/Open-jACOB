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
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFont;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontStyleType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontWeightType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 * 
 */
public class UIDBForeignFieldModel extends UIGroupElementModel implements UIIBrowserProviderModel, UIICaptionProviderModel, UIInputElementModel, IFontProviderModel
{
  private final static String DEFAULT_CAPTION = "Caption";
  private UICaptionModel captionModel;

  public UIDBForeignFieldModel()
  {
    super(null, null, null, new CastorGuiElement());
    CastorGuiElementChoice choice = new CastorGuiElementChoice();
    ForeignInputField input = new ForeignInputField();
    CastorCaption caption = new CastorCaption();
	  caption.setHalign(CastorHorizontalAlignment.valueOf(JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, CastorHorizontalAlignment.LEFT.toString())));

	  input.setDimension(new CastorDimension());
    CastorDimension dim = new CastorDimension();
    dim.setWidth(200);
    dim.setHeight(50);
    caption.setDimension(dim);
    input.setCaption(caption);
    caption.setLabel(DEFAULT_CAPTION);
    choice.setForeignInputField(input);
    getCastor().setCastorGuiElementChoice(choice);
    getCastor().setVisible(true);
    getCastorForeignInputField().setFilldirection(CastorFilldirection.valueOf((String) FILLDIRECTIONS.get(0)));
    getCastorForeignInputField().setRelationset(RelationsetModel.RELATIONSET_LOCAL);
  }

  /**
   * 
   */
  public UIDBForeignFieldModel(JacobModel jacob,  UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
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
    getCastorForeignInputField().setCaption(caption);
  }

  public CastorCaption getCastorCaption()
  {
    return getCastorForeignInputField().getCaption();
  }
  
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorForeignInputField().getDimension();
  }

  public String suggestI18NKey()
  {
    return getForeignTableAlias().toUpperCase();
  }

  public String getDefaultNameSuffix()
  {
    return StringUtils.capitalise(getForeignTableAlias());
  }

  public String getDefaultCaption()
  {
    return StringUtils.capitalise(getForeignTableAlias());
  }

  public void setAsComboBox(boolean flag)
  {
    boolean save = getAsComboBox();
    if (flag==save)
      return;

    getCastorForeignInputField().setAsComboBox(flag);
  }

  public boolean getAsComboBox()
  {
    return getCastorForeignInputField().getAsComboBox();
  }
    
  /**
   * Per Default wird die Einstellung aus den Preferences genommen
   *
   */
  public String getDefaultCaptionAlign()
  {
    return JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, ALIGN_LEFT);
  }

  
  public void setGroup(UIGroupModel group)
  {
    super.setGroup(group);
    setDefaultBrowser();
  }

  /**
   * 
   */
  public void setSize(Dimension size)
  {
    Dimension save = getSize();
    if (save.equals(size))
      return;
    // Falls der ale Wert eine Dimension von [0,0] hat, wird die Caption
    // des Elements auf die neue Grï¿½ï¿½e angepasst.
    //
    if (save.width == 0 && save.height == 0)
    {
      getCastorCaption().getDimension().setHeight(size.height);
      getCastorCaption().getDimension().setWidth(DEFAULT_CAPTION_WIDTH);
    }
    getCastorForeignInputField().getDimension().setHeight(size.height);
    getCastorForeignInputField().getDimension().setWidth(size.width);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }

  public Point getLocation()
  {
    return new Point(getCastorForeignInputField().getDimension().getX(), getCastorForeignInputField().getDimension().getY());
  }

  public Dimension getSize()
  {
    return new Dimension(getCastorForeignInputField().getDimension().getWidth(), getCastorForeignInputField().getDimension().getHeight());
  }

  public UICaptionModel getCaptionModel()
  {
    if (captionModel == null)
      captionModel = new UICaptionModel(getJacobModel(), getGroupContainerModel(), this);
    return captionModel;
  }

  public void renameBrowserReference(String from, String to)
  {
    if (getCastorForeignInputField().getBrowserToUse().equals(from))
      getCastorForeignInputField().setBrowserToUse(to);
  }

  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 6];
    for (int i = 0; i < superDescriptors.length; i++)
    {
      descriptors[i] = superDescriptors[i];
    }
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_RELATIONSET, "Relationset", (String[]) getJacobModel().getRelationsetNames().toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FILLDIRECTION, "Fill direction", (String[]) FILLDIRECTIONS.toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 2] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FOREINGTABLE, "Foreign table alias", getValidFromTableAliasNames(), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 3] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FOREINGFIELD, "Foreign field to display", (String[]) getForeignTableAliasModel().getFieldNames().toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 4] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_BROWSER, "Search browser", (String[]) getForeignTableAliasModel().getPossibleBrowserNames().toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 5] = new CheckboxPropertyDescriptor(ID_PROPERTY_ASCOMBOBOX, "As ComboBox", PROPERTYGROUP_COMMON);
    return descriptors;
  }

  public void setPropertyValue(Object propName, Object val)
  {
    if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_RELATIONSET))
      setRelationset((String) (getJacobModel().getRelationsetNames().get(((Integer) val).intValue())));
    else if (propName == ID_PROPERTY_ASCOMBOBOX)
      setAsComboBox(((Boolean) val).booleanValue());
    else if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FILLDIRECTION))
      setFilldirection( FILLDIRECTIONS.get(((Integer) val).intValue()));
    else if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FOREINGTABLE))
      setForeignTableAlias(getValidFromTableAliasNames()[((Integer) val).intValue()]);
    else if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FOREINGFIELD))
      setDisplayField(getForeignTableAliasModel().getFieldNames().get(((Integer) val).intValue()));
    else if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_BROWSER))
      setSearchBrowser(getForeignTableAliasModel().getPossibleBrowserNames().get(((Integer) val).intValue()));
    else
      super.setPropertyValue(propName, val);
  }

  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_RELATIONSET)
      return new Integer(getJacobModel().getRelationsetNames().indexOf(getRelationset()));
    if (propName == ID_PROPERTY_FILLDIRECTION)
      return new Integer(FILLDIRECTIONS.indexOf(getFilldirection()));
    if (propName == ID_PROPERTY_FOREINGTABLE)
      return new Integer(StringUtil.indexOf(getValidFromTableAliasNames(), getForeignTableAlias()));
    if (propName == ID_PROPERTY_FOREINGFIELD)
      return new Integer(getForeignTableAliasModel().getFieldNames().indexOf(getDisplayField()));
    if (propName == ID_PROPERTY_BROWSER)
      return new Integer(getForeignTableAliasModel().getPossibleBrowserNames().indexOf(getSearchBrowser()));
    if (propName == ID_PROPERTY_ASCOMBOBOX)
      return new Boolean(getAsComboBox());
    
    return super.getPropertyValue(propName);
  }

  private void setFilldirection(String direction)
  {
    String save = getFilldirection();
    if (save.equals(direction))
      return;
    getCastorForeignInputField().setFilldirection(CastorFilldirection.valueOf(direction));
    firePropertyChange(PROPERTY_FILLDIRECTION_CHANGED, save, direction);
  }

  private String getFilldirection()
  {
    return getCastorForeignInputField().getFilldirection().toString();
  }

  private String getRelationset()
  {
    return getCastorForeignInputField().getRelationset();
  }

  public RelationModel getRelationToUse()
  {
    return getJacobModel().getRelationModel(getCastorForeignInputField().getRelationToUse());
  }

  private void setRelationset(String relation)
  {
    String save = getRelationset();
    if (save.equals(relation))
      return;
    getCastorForeignInputField().setRelationset(relation);
    firePropertyChange(PROPERTY_RELATIONSET_CHANGED, save, relation);
  }

  private String getSearchBrowser()
  {
    return getCastorForeignInputField().getBrowserToUse();
  }

  private void setSearchBrowser(String browser)
  {
    String save = getSearchBrowser();
    if (save != null && save.equals(browser))
      return;
    getCastorForeignInputField().setBrowserToUse(browser);
    firePropertyChange(PROPERTY_BROWSER_CHANGED, save, browser);
  }

  public BrowserModel getBrowserModel()
  {
    return getJacobModel().getBrowserModel(getSearchBrowser());
  }

  private String getForeignTableAlias()
  {
    return getCastorForeignInputField().getForeignAlias();
  }

  public TableAliasModel getForeignTableAliasModel()
  {
    return getJacobModel().getTableAliasModel(getCastorForeignInputField().getForeignAlias());
  }

  public void setForeignTableAlias(String tableAlias)
  {
    String save = getForeignTableAlias();
    if (save.equals(tableAlias))
      return;
    getCastorForeignInputField().setForeignAlias(tableAlias);
    // Diese information ist eigentlich unnï¿½tig, da diese sich errechnet
    //
    RelationModel relation = getJacobModel().getRelationModel(getForeignTableAliasModel(), getGroupModel().getTableAliasModel());
    getCastorForeignInputField().setRelationToUse(relation.getName());
    // Es wird ein default search browser fuer dieses foreign field
    // gesetzt
    setDefaultBrowser();
    
    // Die foreign Tabelle hat sich geï¿½ndert. Es wird jetzt das erste beste Feld
    // dieser Tabelle als Anzeige Feld genommen...oder das representivField der Tabelle
    //
    String saveField = getDisplayField();
    FieldModel representativeField = getForeignTableAliasModel().getTableModel().getRepresentativeFieldModel();
    String field = (String) getForeignTableAliasModel().getFieldNames().get(0);
    if (representativeField != null)
      field = representativeField.getName();
    getCastorForeignInputField().setForeignTableField(field);
    if (!saveField.equals(field))
      firePropertyChange(PROPERTY_DISPLAYFIELD_CHANGED, saveField, field);
    firePropertyChange(PROPERTY_FOREIGNTABLE_CHANGED, save, tableAlias);
  }

  private FieldModel getDisplayFieldModel()
  {
    return getForeignTableAliasModel().getFieldModel(getDisplayField());
  }

  private String getDisplayField()
  {
    return getCastorForeignInputField().getForeignTableField();
  }

  private void setDisplayField(String field)
  {
    String save = getDisplayField();
    if (save.equals(field))
      return;
    getCastorForeignInputField().setForeignTableField(field);
    firePropertyChange(PROPERTY_DISPLAYFIELD_CHANGED, save, field);
  }

  private ForeignInputField getCastorForeignInputField()
  {
    return getCastor().getCastorGuiElementChoice().getForeignInputField();
  }

  private String[] getValidFromTableAliasNames()
  {
    List x = getGroupModel().getTableAliasModel().getFromLinkedTableAliases();
    String[] result = new String[x.size()];
    for (int i = 0; i < x.size(); i++)
    {
      TableAliasModel tableAlias = (TableAliasModel) x.get(i);
      result[i] = tableAlias.getName();
    }
    return result;
  }

  /**
   * A table alias has been change the name, Fit the name in the castor
   * refrences too.
   * 
   * @param from
   * @param to
   */
  public void renameAliasReference(String from, String to)
  {
    if (getCastorForeignInputField().getForeignAlias().equals(from))
      getCastorForeignInputField().setForeignAlias(to);
  }

  public void renameRelationsetReference(String from, String to)
  {
    if (getCastorForeignInputField().getRelationset().equals(from))
      getCastorForeignInputField().setRelationset(to);
  }

  
  public void renameFieldReference(FieldModel field, String fromName, String toName) 
  {
    ForeignInputField foreign = getCastorForeignInputField();
    // Falls das Feogeing field sich auf die selben Tabelle bezieht, dann
    // muss auch das "FieldToDisplay" eventuell angepasst werden
    //
    if (getJacobModel().getTableAliasModel(foreign.getForeignAlias()).getTableModel() == field.getTableModel() && foreign.getForeignTableField().equals(fromName))
      foreign.setForeignTableField(toName);
	}

  public void renameRelationReference(String from, String to)
  {
    ForeignInputField field = getCastorForeignInputField();
    if (field.getRelationToUse().equals(from))
      field.setRelationToUse(to);
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
    
  public void setGroupContainerModel(UIGroupContainer form)
  {
    super.setGroupContainerModel(form);
    // falls die foreign table nicht gesetzt ist, wird diese jetzt
    // initial gesetzt
    //
    if (getForeignTableAlias() == null)
    {
      try
      {
        // man nehme die erste beste Tabelle....
        String tableAlias = getValidFromTableAliasNames()[0];
        getCastorForeignInputField().setForeignAlias(tableAlias);
        // ... und nehme das erst beste Feld als Anzeigeelement
        FieldModel representativeField = getForeignTableAliasModel().getTableModel().getRepresentativeFieldModel();
        String field = (String) getJacobModel().getTableAliasModel(tableAlias).getFieldNames().get(0);
        if (representativeField != null)
          field = representativeField.getName();
        getCastorForeignInputField().setForeignTableField(field);
        // Einen default ï¿½berschrift setzten
        //
        setCaption(StringUtils.capitalise(getForeignTableAlias()));
        // Diese information ist eigentlich unnï¿½tig, da diese sich errechnet
        //
        RelationModel relation = getJacobModel().getRelationModel(getForeignTableAliasModel(), getGroupModel().getTableAliasModel());
        getCastorForeignInputField().setRelationToUse(relation.getName());
        // Einen default Namen fï¿½r das Objekt setzten
        //
        String defaultName = getGroupModel().getTableAlias() + StringUtils.capitalise(tableAlias);
        String newName = defaultName;
        int counter = 1;
        while (form.isUIElementNameFree(newName)==false)
        {
          newName = defaultName + counter;
          counter++;
        }
        getCastor().setName(newName);
      }
      catch (Exception ex)
      {
        JacobDesigner.showException(ex);
      }
    }
  }

  private void setDefaultBrowser()
  {
    // Falls das ForeingField 'frisch' in eine Gruppe eingefï¿½gt wird, hat dieses
    // noch keinen Search browser zugeordnet. Dies kann erst passieren wenn die
    // Anchor Table definiert ist......und dies passiert hier.
    //
    getCastorForeignInputField().setBrowserToUse((String) getForeignTableAliasModel().getPossibleBrowserNames().get(0));
  }

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public int getFontSize()
  {
    if(getCastorForeignInputField().getFont()==null)
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
    if(getCastorForeignInputField().getFont()==null)
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
    if(getCastorForeignInputField().getFont()==null)
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
    if(getCastorForeignInputField().getFont()==null)
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
    if(getCastorForeignInputField().getFont()==null)
      return;
    
    CastorFont font = getCastorForeignInputField().getFont();
    if(font.getSize()!=FontDefinition.DEFAULT_FONT_SIZE)
      return;
    if(!font.getFamily().equals(FontDefinition.DEFAULT_FONT_FAMILY))
      return;
    if(!font.getStyle().equals(FontDefinition.DEFAULT_FONT_STYLE))
      return;
    if(!font.getWeight().equals(FontDefinition.DEFAULT_FONT_WEIGHT))
      return;
    getCastorForeignInputField().setFont(null);
    
  }

  private CastorFont getCastorFont()
  {
    if(getCastorForeignInputField().getFont()==null)
    {
      CastorFont font = new CastorFont();
      // derzeitige defaultgröße holen und eintragen
      // (die Anderen Werte werden durch Castro sinvoll aus dem jad.xsd geholt)
      font.setSize(getFontSize());
      
      // ...und erst jetzt den font einhängen
      getCastorForeignInputField().setFont(font);
    }
    return getCastorForeignInputField().getFont();
  }
    
  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "IForeignFieldEventHandler.java";
  }

  public String getError()
  {
    if (getDisplayFieldModel() == getDisplayFieldModel().getTableModel().NULL_FIELD)
      return "The field [" + getDisplayField() + "] doesn't exists in the table [" + getDisplayFieldModel().getTableModel().getName() + "]";
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
    if(model == getDisplayFieldModel() || model == getForeignTableAliasModel() || model == getBrowserModel())
      result.addReferences(this);
  }
}
