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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.TableListBox;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;
public class UIDBTableListBoxModel extends UIGroupElementModel implements UIICaptionProviderModel
{
  public final static String DELETE_DISABLED = "no delete";
  public final static String DELETE_UPDATE = "update";
  public final static String DELETE_SELECT_UPDATE = "select & update";
  private static final List DELETE_MODES;
  static
  {
    DELETE_MODES = new ArrayList();
    DELETE_MODES.add(DELETE_DISABLED);
    DELETE_MODES.add(DELETE_UPDATE);
    DELETE_MODES.add(DELETE_SELECT_UPDATE);
  }
  private UICaptionModel captionModel;

  public UIDBTableListBoxModel()
  {
    super(null, null, null, new CastorGuiElement());
    CastorGuiElementChoice choice = new CastorGuiElementChoice();
    TableListBox listbox = new TableListBox();
    CastorCaption caption = new CastorCaption();
    caption.setHalign(CastorHorizontalAlignment.valueOf(JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, CastorHorizontalAlignment.LEFT.toString())));
    listbox.setDimension(new CastorDimension());
    CastorDimension dim = new CastorDimension();
    dim.setWidth(200);
    dim.setHeight(50);
    dim.setX(0);
    dim.setY(0);
    caption.setDimension(dim);
    listbox.setCaption(caption);
    caption.setLabel("Caption");
    choice.setTableListBox(listbox);
    getCastor().setCastorGuiElementChoice(choice);
    getCastor().setVisible(true);
  }

  /**
   * @param jacob
   * @param container
   * @param group
   * @param gui
   */
  public UIDBTableListBoxModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement gui)
  {
    super(jacob, container, group, gui);
    // TODO Auto-generated constructor stub
  }

  public CastorCaption getCastorCaption()
  {
    return getCastorTableListBox().getCaption();
  }

  public void setCastorCaption(CastorCaption caption)
  {
    getCastorTableListBox().setCaption(caption);
  }

  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorTableListBox().getDimension();
  }

  /**
   * 
   */
  public UIDBTableListBoxModel(JacobModel jacob, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, group.getGroupContainerModel(), group, guiElement);
  }

  public String getDefaultNameSuffix()
  {
    return "Listbox";
  }

  public String getDefaultCaption()
  {
    return StringUtils.capitalise(getTableAliasModel().getName());
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
    result.x = parentConstraint.x;
    result.y = parentConstraint.y - DEFAULT_CAPTION_HEIGHT;
    result.width = DEFAULT_CAPTION_WIDTH;
    result.height = DEFAULT_CAPTION_HEIGHT;
    return result;
  }

  @Override
  public int getDefaultHeight()
  {
    // eine Textbox geht per default über die Höhe von 3 Eingabeelemente
    //
    return DEFAULT_ELEMENT_HEIGHT * 3 + DEFAULT_ELEMENT_SPACING * 2;
  }

  public String suggestI18NKey()
  {
    return "LISTBOX" + getJacobModel().getSeparator() + getTableAliasModel().getName().toUpperCase();
  }

  /**
   * @param key
   *          the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(key, getCaption());
  }

  public void setSize(Dimension size)
  {
    Dimension save = getSize();
    if (save.equals(size))
      return;
    getCastorTableListBox().getDimension().setHeight(size.height);
    getCastorTableListBox().getDimension().setWidth(size.width);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }

  public Point getLocation()
  {
    return new Point(getCastorTableListBox().getDimension().getX(), getCastorTableListBox().getDimension().getY());
  }

  public Dimension getSize()
  {
    return new Dimension(getCastorTableListBox().getDimension().getWidth(), getCastorTableListBox().getDimension().getHeight());
  }

  /**
   * true => Die Überschrift wird als eigenständiges Element im Formlayout
   * gezeichnet false => Das Model zeichnet die Caption selbst.
   */
  public boolean isCaptionExtern()
  {
    return true;
  }

  public UICaptionModel getCaptionModel()
  {
    if (captionModel == null)
    {
      CastorCaption caption = getCastorTableListBox().getCaption();
      if (caption == null)
      {
        caption = new CastorCaption();
        CastorDimension dim = new CastorDimension();
        dim.setWidth(200);
        dim.setHeight(50);
        dim.setX(0);
        dim.setY(0);
        caption.setDimension(dim);
        getCastorTableListBox().setCaption(caption);
      }
      captionModel = new UICaptionModel(getJacobModel(), getGroupContainerModel(), this);
    }
    return captionModel;
  }

  public TableAliasModel getTableAliasModel()
  {
    return getJacobModel().getTableAliasModel(getCastorTableListBox().getTableAlias());
  }

  public void setTableAliasModel(TableAliasModel tableAlias)
  {
    String save = getCastorTableListBox().getTableAlias();
    if (tableAlias == null)
      return;
    if (tableAlias.getName().equals(save))
      return;
    getCastorTableListBox().setTableAlias(tableAlias.getName());
    // DisplayColumn muss auch angepasst werden, wenn das Feld nicht in dem
    // neuen TableAlias
    // vorhanden ist oder wenn zuvor keine TableAlias definiert war
    //
    int index = tableAlias.getFieldNames().indexOf(getField());
    if (index == -1 || save == null)
    {
      String newField = tableAlias.getTableModel().getRepresentativeField();
      if (newField == null || newField.length() == 0)
        newField = (String) tableAlias.getFieldNames().get(0);
      getCastorTableListBox().setDisplayField(newField);
    }
    firePropertyChange(PROPERTY_TABLEALIAS_CHANGED, save, tableAlias.getName());
  }

  private String getSearchBrowser()
  {
    return StringUtil.toSaveString(getCastorTableListBox().getBrowserToUse());
  }

  private void setSearchBrowser(String browser)
  {
    String save = getSearchBrowser();
    if (StringUtils.equals(save, browser))
      return;
    if (browser != null && browser.length() == 0)
      browser = null;
    getCastorTableListBox().setBrowserToUse(browser);
    firePropertyChange(PROPERTY_BROWSER_CHANGED, save, browser);
  }

  public String getField()
  {
    return getCastorTableListBox().getDisplayField();
  }

  public void setField(String field)
  {
    String save = getField();
    if (field.equals(save))
      return;
    getCastorTableListBox().setDisplayField(field);
    firePropertyChange(PROPERTY_FIELD_CHANGED, save, field);
  }

  private String getResizeMode()
  {
    if (getCastorTableListBox().getResizeMode() == null)
      return RESIZE_MODE_NONE;
    return getCastorTableListBox().getResizeMode().toString();
  }

  private void setResizeMode(String mode)
  {
    if (StringUtil.saveEquals(RESIZE_MODE_NONE, mode))
      getCastorTableListBox().setResizeMode(null);
    else
      getCastorTableListBox().setResizeMode(CastorResizeMode.valueOf(mode));
  }

  public void setDeleteMode(String mode)
  {
    if (DELETE_DISABLED.equals(mode))
    {
      getCastorTableListBox().setDeleteModeSelected(false);
      getCastorTableListBox().setDeleteModeUpdateNew(false);
    }
    else if (DELETE_SELECT_UPDATE.equals(mode))
    {
      getCastorTableListBox().setDeleteModeSelected(true);
      getCastorTableListBox().setDeleteModeUpdateNew(true);
    }
    else if (DELETE_UPDATE.equals(mode))
    {
      getCastorTableListBox().setDeleteModeSelected(false);
      getCastorTableListBox().setDeleteModeUpdateNew(true);
    }
  }

  public String getDeleteMode()
  {
    boolean selected = getCastorTableListBox().getDeleteModeSelected();
    boolean update = getCastorTableListBox().getDeleteModeUpdateNew();
    if (selected == false && update == false)
      return DELETE_DISABLED;
    else if (selected == true && update == true)
      return DELETE_SELECT_UPDATE;
    else if (selected == false && update == true)
      return DELETE_UPDATE;
    return DELETE_DISABLED;
  }

  public void renameI18NKey(String fromName, String toName)
  {
    if (getCaption() != null && getCaption().equals(fromName))
      setCaption(toName);
  }

  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 5];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_TABLEALIAS, "Table Alias", (String[]) getJacobModel().getTableAliasNames().toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FIELD, "Display field", (String[]) getTableAliasModel().getFieldNames().toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 2] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_DELETE_MODE, "Delete Mode", (String[]) DELETE_MODES.toArray(new String[0]), PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 3] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_BROWSER, "Sorting Browser", (String[]) getTableAliasModel().getPossibleBrowserNames().toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 4] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_RESIZE_MODE, "Component Resize Mode", (String[]) RESIZE_MODES.toArray(new String[0]), PROPERTYGROUP_DIMENSION);
    return descriptors;
  }

  public void setPropertyValue(Object propName, Object val)
  {
    try
    {
      if (propName == ID_PROPERTY_TABLEALIAS)
      {
        int index = ((Integer) val).intValue();
        String name = (String) getJacobModel().getTableAliasNames().get(index);
        setTableAliasModel(getJacobModel().getTableAliasModel(name));
      }
      else if (propName == ID_PROPERTY_FIELD)
      {
        int index = ((Integer) val).intValue();
        String name = (String) getTableAliasModel().getFieldNames().get(index);
        setField(name);
      }
      else if (propName == ID_PROPERTY_DELETE_MODE)
      {
        int index = ((Integer) val).intValue();
        String name = (String) DELETE_MODES.get(index);
        setDeleteMode(name);
      }
      else if (propName == ID_PROPERTY_RESIZE_MODE)
      {
        int index = ((Integer) val).intValue();
        String name = (String) RESIZE_MODES.get(index);
        setResizeMode(name);
      }
      else if (propName == ID_PROPERTY_BROWSER)
      {
        setSearchBrowser(getTableAliasModel().getPossibleBrowserNames().get(((Integer) val).intValue()));
      }
      else
        super.setPropertyValue(propName, val);
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
  }

  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_FIELD)
    {
      List fields = getTableAliasModel().getFieldNames();
      return new Integer(fields.indexOf(getField()));
    }
    if (propName == ID_PROPERTY_TABLEALIAS)
    {
      List aliases = getJacobModel().getTableAliasNames();
      return new Integer(aliases.indexOf(getTableAliasModel().getName()));
    }
    if (propName == ID_PROPERTY_BROWSER)
      return new Integer(getTableAliasModel().getPossibleBrowserNames().indexOf(getSearchBrowser()));
    if (propName == ID_PROPERTY_DELETE_MODE)
      return new Integer(DELETE_MODES.indexOf(getDeleteMode()));
    if (propName == ID_PROPERTY_RESIZE_MODE)
      return new Integer(RESIZE_MODES.indexOf(getResizeMode()));
    return super.getPropertyValue(propName);
  }

  private TableListBox getCastorTableListBox()
  {
    return getCastor().getCastorGuiElementChoice().getTableListBox();
  }

  public void renameFieldReference(FieldModel field, String fromName, String toName)
  {
    if (field.getTableModel() == getTableAliasModel().getTableModel() && getField() != null && getField().equals(fromName))
      setField(toName);
  }

  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "ITableListBoxEventHandler.java";
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

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if (model == getTableAliasModel() || model == getTableAliasModel().getTableModel().getFieldModel(getField()))
      result.addReferences(this);
  }
}
