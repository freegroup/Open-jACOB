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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.components.plugin.PluginComponentManager;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.ComboBox;
import de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;
import de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType;
import de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowJacobFormEditorAction;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.util.ColorUtil;
import de.tif.jacob.designer.exception.InvalidNameException;
import de.tif.jacob.designer.exception.InvalidTableAliasException;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public class UIGroupModel extends UIFormElementModel implements UIIBrowserProviderModel, Cloneable, IOpenable, ISelectionActionProvider
{
  CastorGroup castor;
  ArrayList<UIGroupElementModel> elements = null;
  List<ContextMenuEntryModel> contextMenuEntries;
  List<SelectionActionModel> selectionActionModels= new ArrayList<SelectionActionModel>();

  public UIGroupModel()
  {
    super(null, null);
    castor = new CastorGroup();
    castor.setDimension(new CastorDimension());
    setSize(new Dimension(50, 50));
    setLocation(new Point(100, 100));
  }

  public UIGroupModel(JacobModel jacob, UIGroupContainer container, CastorGroup group)
  {
    super(jacob, container);
    this.castor = group;
    for(int i=0;i<castor.getSelectionActionEventHandlerCount();i++)
    {
      selectionActionModels.add( SelectionActionModel.createModelObject(this,castor.getSelectionActionEventHandler(i)));
    }
  }

  public String getGroupTableAlias()
  {
    return getTableAlias();
  }

  public Point getLocation()
  {
  	// eine Gruppe hat keine dimensions oder Position wenn diese in einem TabContainer
  	// eingebetet ist.
  	//
  	if(castor.getDimension()==null)
  		return null;
  	
    return new Point(castor.getDimension().getX(), castor.getDimension().getY());
  }

  public Dimension getSize()
  {
  	// eine Gruppe hat keine dimensions oder Position wenn diese in einem TabContainer
  	// eingebetet ist.
  	//
  	if(castor.getDimension()==null)
  		return null;

  	return new Dimension(castor.getDimension().getWidth(), castor.getDimension().getHeight());
  }

  public boolean getHasBorder()
  {
    return castor.getBorder();
  }

  public int getBorderWidth()
  {
    return castor.getBorderWidth();
  }
  

  public void setBorderWith(int width)
  {
    width = Math.max(-1,width);
    
    int save = getBorderWidth();
    if(save == width)
      return;
    
    castor.setBorderWidth(width);
    firePropertyChange(PROPERTY_BORDER_CHANGED, save, width);
  }


  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public Color getBorderColor()
  {
    if(castor.getBorderColor()==null)
      return ColorUtil.toColor(Constants.DEFAULT_COLOR);
    return ColorUtil.toColor(castor.getBorderColor());
  }
  
  public void setBorderColor(Color color)
  {
    Color save = getBorderColor();
    String saveCSS = castor.getBorderColor();
    String css  = ColorUtil.toCSS(color);
    
    if(StringUtil.saveEquals(saveCSS,css))
      return;
    
    
    if(Constants.DEFAULT_COLOR.equals(css))
      castor.setBorderColor(null);
    else
      castor.setBorderColor(css);
    
    firePropertyChange(PROPERTY_COLOR_CHANGED, save, color);
  }
  
  
  /**
   * 
   * @return List[TableAliasModel]
   */
  public Set<BrowserModel> getPossibleEmbeddableInformBrowsers()
  {
    TableAliasModel thisAlias = this.getTableAliasModel();
    Set<BrowserModel> result = new HashSet<BrowserModel>();
    List<TableAliasModel> possibleAliases = new ArrayList<TableAliasModel>();
    
    for(RelationModel relation: getJacobModel().getRelationModels())
    {
      if(relation.getFromTableAlias()==thisAlias)
      {
        TableAliasModel toAlias = relation.getToTableAlias();
        // Falls es sich um eine M-N Tabelle handelt, dann wird die darin verbundene Tabelle ebenfalls
        // geholt.
        if(toAlias.getTableModel().getMtoNTable()==true)
        {
          for(RelationModel mnRelation: getJacobModel().getRelationModels())
          {
            if(mnRelation.getToTableAlias()==toAlias)
            {
              TableAliasModel mnFromTable = mnRelation.getFromTableAlias();
              possibleAliases.add(mnFromTable);
            }
          }
        }
        else
        {
          possibleAliases.add(relation.getToTableAlias());
        }
      }
    }
    for(TableAliasModel alias:possibleAliases)
    {
      for(String browserName :alias.getPossibleBrowserNames())
      {
        result.add(this.getJacobModel().getBrowserModel(browserName));
      }
    }

    return result;
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public Color getBackgroundColor()
  {
    if(castor.getBackgroundColor()==null)
      return ColorUtil.toColor(Constants.DEFAULT_COLOR);
    return ColorUtil.toColor(castor.getBackgroundColor());
  }
  
  public void setBackgroundColor(Color color)
  {
    Color save = getBackgroundColor();
    String saveCSS = castor.getBackgroundColor();
    String css  = ColorUtil.toCSS(color);
    
    if(StringUtil.saveEquals(saveCSS,css))
      return;
    
    
    if(Constants.DEFAULT_COLOR.equals(css))
      castor.setBackgroundColor(null);
    else
      castor.setBackgroundColor(css);
    
    firePropertyChange(PROPERTY_COLOR_CHANGED, save, color);
  }
  
  public void setSize(Dimension size)
  {
    Dimension save = getSize();
    if (save.equals(size))
      return;
    castor.getDimension().setHeight(size.height);
    castor.getDimension().setWidth(size.width);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }


  public BrowserModel getBrowserModel()
  {
    return getJacobModel().getBrowserModel(castor.getBrowser());
  }

  /**
   * 
   * @param browser
   */
  public void setBrowserModel(BrowserModel browser)
  {
    setBrowser(browser.getName());
  }

  public void setBrowser(String browser)
  {
    String save = castor.getBrowser();
    BrowserModel saveBrowser = getBrowserModel();
    
    // no changes
    //
    if (StringUtil.saveEquals(save, browser))
      return;
    
    BrowserModel newBrowser = getJacobModel().getBrowserModel(browser);
    if (newBrowser == null)
      throw new RuntimeException("Browser [" + browser + "] doesn't exists.");
    
    castor.setBrowser(browser);
    firePropertyChange(PROPERTY_BROWSER_CHANGED, save, browser);
    
    // Dem Browser und alle die diesen anzeigen mitteilen, das sich der (Error/Warning) Status 
    // mï¿½glicherweise geï¿½ndert hat. Der Browser feuert ein Event und alle Listener aktualisieren sich dann
    //
    newBrowser.firePropertyChange(PROPERTY_BROWSER_CHANGED, null, newBrowser);
    if(saveBrowser!=null)
      saveBrowser.firePropertyChange(PROPERTY_BROWSER_CHANGED, null, saveBrowser);
  }

  public final void resetHookClassName() throws Exception
  {
    if (getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = castor.getEventHandler();
      castor.setEventHandler(null);
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, save, null);
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
  }

  public void generateHookClassName() throws Exception
  {
    if (getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = castor.getEventHandler();
      castor.setEventHandler(getChildrenDefaultJavaPackage() + "." + StringUtils.capitalise(getName()));
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, save, castor.getEventHandler());
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
  }

  /**
   * 
   * @return List[ContextMenuEntryModel]
   */
  public List<ContextMenuEntryModel> getContextMenuEntries()
  {
    if (contextMenuEntries == null)
    {
      contextMenuEntries = new ArrayList<ContextMenuEntryModel>();
      for (int i = 0; i < castor.getContextMenuEntryCount(); i++)
      {
        ContextMenuEntry element = castor.getContextMenuEntry(i);
        contextMenuEntries.add(new ContextMenuEntryModel(getJacobModel(), this, element));
      }
    }
    return contextMenuEntries;
  }

  /**
   * 
   * @return List[ObjectModel]
   */
  public List<UIGroupElementModel> getElements()
  {
    if (elements == null)
    {
      elements = new ArrayList<UIGroupElementModel>();
      for (int i = 0; i < castor.getGuiElementCount(); i++)
      {
        CastorGuiElement element = castor.getGuiElement(i);
        // TEXTINPUT Element
        //
        if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getTextInput() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBTextModel model = new UIDBTextModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UITextModel model = new UITextModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // TableListBox Element
        //
        if (element.getCastorGuiElementChoice().getTableListBox() != null)
        {
          UIDBTableListBoxModel model = new UIDBTableListBoxModel(getJacobModel(), getGroupContainerModel(), this,  element);
          elements.add(model);
          if(model.getCaption()!=null)
            elements.add(model.getCaptionModel());
        }
        // PASSWORDINPUT Element
        //
        if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getPasswordInput() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBPasswordModel model = new UIDBPasswordModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UIPasswordModel model = new UIPasswordModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // LONGTEXTINPUT Element
        //
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getLongTextInput() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            if (element.getCastorGuiElementChoice().getLocalInputField().getLongTextInput().getInForm())
            {
              UIDBInformLongTextModel model = new UIDBInformLongTextModel(getJacobModel(), getGroupContainerModel(), this, element);
              elements.add(model);
              if(model.getCaption()!=null)
                elements.add(model.getCaptionModel());
            }
            else
            {
              UIDBLongTextModel model = new UIDBLongTextModel(getJacobModel(), getGroupContainerModel(), this, element);
              elements.add(model);
              if(model.getCaption()!=null)
                elements.add(model.getCaptionModel());
            }
          }
          // 'normal'
          else
          {
            UIInformLongTextModel model = new UIInformLongTextModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // CHECKBOX Element
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getCheckBox() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBCheckboxModel model = new UIDBCheckboxModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UICheckboxModel model = new UICheckboxModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // COMBOBOX Element
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getComboBox() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIComboboxModel model=null;
            if(fieldModel.getType() == FieldModel.DBTYPE_ENUM)
              model = new UIDBEnumComboboxModel(getJacobModel(), getGroupContainerModel(), this, element);
            else
              model = new UIDBTextfieldComboboxModel(getJacobModel(), getGroupContainerModel(), this, element);
            
            elements.add(model);

            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UIComboboxModel model = new UIComboboxModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // RADIOBUTTONGROUP Element
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getRadioButtonGroup() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBRadioButtonGroupModel model = new UIDBRadioButtonGroupModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UIRadioButtonGroupModel model = new UIRadioButtonGroupModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // LISTBOX
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getListBox() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBListboxModel model = new UIDBListboxModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UIListboxModel model = new UIListboxModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // DATE Element
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getDateInput() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBDateModel model = new UIDBDateModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UIDateModel model = new UIDateModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // TIME Element
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getTimeInput() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBTimeModel model = new UIDBTimeModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UITimeModel model = new UITimeModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // Image Element
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getImage() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBImageModel model = new UIDBImageModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UIImageModel model = new UIImageModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // StaticImage Element
        else if (element.getCastorGuiElementChoice().getStaticImage() != null)
        {
            UIStaticImageModel model = new UIStaticImageModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
        }
        // StyledText Element
        else if (element.getCastorGuiElementChoice().getStyledText() != null)
        {
            UIStyledTextModel model = new UIStyledTextModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
        }
        // TimeLine Element
        else if (element.getCastorGuiElementChoice().getTimeLine() != null)
        {
            UITimeLineModel model = new UITimeLineModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
        }
        // Calendar Element
        else if (element.getCastorGuiElementChoice().getCalendar() != null)
        {
            UICalendarModel model = new UICalendarModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
        }
        // TIMESTAMP Element
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getTimestampInput() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLocalInputField().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBDateTimeModel model = new UIDBDateTimeModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
          // 'normal'
          else
          {
            UIDateTimeModel model = new UIDateTimeModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
            if(model.getCaption()!=null)
              elements.add(model.getCaptionModel());
          }
        }
        // Document Element
        else if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getDocumentInput() != null)
        {
          UIDBDocumentModel model = new UIDBDocumentModel(getJacobModel(), getGroupContainerModel(), this, element);
          elements.add(model);
          if(model.getCaption()!=null)
            elements.add(model.getCaptionModel());
        }
        else if (element.getCastorGuiElementChoice().getContainer() != null)
        {
          if(element.getCastorGuiElementChoice().getContainer().getLayout()==ContainerLayoutType.TAB_STRIP)
          {
            UITabContainerModel containerModel = new UITabContainerModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(containerModel);
          }
          else
          {
            UIStackContainerModel containerModel = new UIStackContainerModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(containerModel);
          }
        }
        else if (element.getCastorGuiElementChoice().getLabel() != null && element.getCastorGuiElementChoice().getLabel().getCaption() != null)
        {
          FieldModel fieldModel = getTableAliasModel().getFieldModel(element.getCastorGuiElementChoice().getLabel().getTableField());
          // DB-bounded
          if (fieldModel != null)
          {
            UIDBLabelModel model = new UIDBLabelModel(getJacobModel(), getGroupContainerModel(), this, element);
            elements.add(model);
          }
          // 'normal'
          else
          {
            UILabelModel captionModel = new UILabelModel(getJacobModel(),getGroupContainerModel(), this, element);
            elements.add(captionModel);
          }
        }
        else if (element.getCastorGuiElementChoice().getFlowLayoutContainer() != null )
        {
          if(element.getCastorGuiElementChoice().getFlowLayoutContainer().getOrientation().getType() == FlowLayoutContainerOrientationType.HORIZONTAL_TYPE)
          {
            UIButtonBarModel container = new UIHorizontalButtonBarModel(getJacobModel(),getGroupContainerModel(), this, element);
            elements.add(container);
          }
          else
          {
            UIButtonBarModel container = new UIVerticalButtonBarModel(getJacobModel(),getGroupContainerModel(), this, element);
            elements.add(container);
          }
        }
        else if (element.getCastorGuiElementChoice().getHeading() != null && element.getCastorGuiElementChoice().getHeading().getCaption() != null)
        {
          UIHeadingModel headingModel = new UIHeadingModel(getJacobModel(),getGroupContainerModel(), this, element);
          elements.add(headingModel);
        }
        else if (element.getCastorGuiElementChoice().getBreadCrumb() != null && element.getCastorGuiElementChoice().getBreadCrumb().getCaption() != null)
        {
          UIBreadCrumbModel breadCrumbModel = new UIBreadCrumbModel(getJacobModel(),getGroupContainerModel(), this, element);
          elements.add(breadCrumbModel);
        }
        else if (element.getCastorGuiElementChoice().getButton() != null)
        {
          UIButtonModel buttonModel = new UIButtonModel(getJacobModel(), getGroupContainerModel(), this, element);
          elements.add(buttonModel);
        }
        else if (element.getCastorGuiElementChoice().getLineChart() != null)
        {
          UILineChartModel model = new UILineChartModel(getJacobModel(), getGroupContainerModel(), this, element);
          elements.add(model);
        }
        else if (element.getCastorGuiElementChoice().getBarChart() != null)
        {
          UIGroupElementModel model = new UIBarChartModel(getJacobModel(), getGroupContainerModel(), this, element);
          elements.add(model);
        }
        else if (element.getCastorGuiElementChoice().getOwnDrawElement() != null)
        {
          UICanvasModel model = new UICanvasModel(getJacobModel(), getGroupContainerModel(), this, element);
          elements.add(model);
        }
        else if (element.getCastorGuiElementChoice().getPluginComponent() != null)
        {
          UIPluginComponentModel model = new UIPluginComponentModel(getJacobModel(), getGroupContainerModel(), this, element);
          elements.add(model);
        }
        else if (element.getCastorGuiElementChoice().getInFormBrowser() != null)
        {
          UIDBInformBrowserModel browserModel = new UIDBInformBrowserModel(getJacobModel(), this, element);
          elements.add(browserModel);
        }
        else if (element.getCastorGuiElementChoice().getForeignInputField() != null)
        {
          UIDBForeignFieldModel foreignModel = new UIDBForeignFieldModel(getJacobModel(), getGroupContainerModel(), this, element);
          elements.add(foreignModel);
          if(foreignModel.getCaption()!=null)
            elements.add(foreignModel.getCaptionModel());
        }
      }
    }
    return elements;
  }

  /**
   * 
   * @param name
   * @return
   */
  public UIGroupElementModel getElement(String name)
  {
    if (name == null)
      throw new NullPointerException("you must hands over a valid element name. <null> is not alowed.");
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      Object obj = iter.next();
      UIGroupElementModel ui = (UIGroupElementModel) obj;
      if (name.equals(ui.getName()))
        return ui;
    }
    return null;
  }

  /**
   * 
   * @param name
   * @return
   */
  public boolean isUIElementNameFree(String name)
  {
    if (name == null)
      throw new NullPointerException("you must hands over a valid element name. <null> is not alowed.");
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      Object obj = iter.next();
      UIGroupElementModel ui = (UIGroupElementModel) obj;
      if (name.equals(ui.getName()))
        return false;
      if(ui instanceof UIButtonBarModel)
      {
        UIButtonBarModel bar = (UIButtonBarModel)ui;
        if(bar.getElement(name)!=null)
          return false;
      }
    }
    return true;
  }

  /**
   * @param name
   */
  public void setName(String name) throws Exception
  {
    String save = getName();
    if (StringUtil.saveEquals(save, name))
      return;
    if (getGroupContainerModel().isUIElementNameFree(name)==false)
      throw new InvalidNameException("UI element with the name [" + name + "] already exists in this form. Please use another name.");
    castor.setName(name);
    // Falls sich der Name der Gruppe geï¿½ndert hat, mï¿½ssen alle Eventhandler auf
    // den neuen
    // Namen angepasst werden
    //
    if (save != null && save.length() > 0)
    {
      IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
      UIGroupContainer container = getGroupContainerModel();
      Iterator iter = getGroupContainerModel().getLinkedDomainModels().iterator();
      while (iter.hasNext())
      {
        UIDomainModel domain = (UIDomainModel) iter.next();
        String fromClass = getEventHandlerName(domain, container, save);
        String toClass = getEventHandlerName(domain, container, name);
        ClassFinder.renameClass(fromClass, toClass, myJavaProject);
      }
    }
    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }

  public String getCastorName()
  {
    return castor.getName();
  }

  public void setCastorName(String name)
  {
    castor.setName(name);
  }

  public void setLabel(String label)
  {
    String save = getLabel();
    if (StringUtil.saveEquals(save, label))
      return;
    
    castor.setLabel(label);
    firePropertyChange(PROPERTY_LABEL_CHANGED, save, label);
  }

  public void setBorder(boolean border)
  {
    boolean save = getHasBorder();
    if (border==save)
      return;

    castor.setBorder(border);
    firePropertyChange(PROPERTY_BORDER_CHANGED, new Boolean(save), new Boolean(border));
  }
 
  public void setHideEmptyBrowser(boolean hideBrowser)
  {
    boolean save = getHideEmptyBrowser();
    if (hideBrowser==save)
      return;

    castor.setHideEmptyBrowser(hideBrowser);
    firePropertyChange(PROPERTY_HIDE_BROWSER_CHANGED, new Boolean(save), new Boolean(hideBrowser));
  }

  private boolean getHideEmptyBrowser()
  {
    return castor.getHideEmptyBrowser();
  }

  public String getExtendedDescriptionLabel()
  {
    return this.getName();
  }
  
  public String getLabel()
  {
    return castor.getLabel();
  }

  public void setTableAlias(String tableAlias, boolean renameChildren) throws Exception
  {
    String save = getTableAlias();
    if (save != null && save.equals(tableAlias))
      return;
    if (getJacobModel().getTableAliasModel(tableAlias) == null)
      throw new InvalidTableAliasException("Table alias [" + tableAlias + "] doesn't exists.");
    castor.setAlias(tableAlias);
    firePropertyChange(PROPERTY_LABEL_CHANGED, save, tableAlias);
    // set a default browser for the new group
    //
    setBrowserModel((BrowserModel) getJacobModel().getBrowserModels(getJacobModel().getTableAliasModel(tableAlias)).get(0));
    if (getName() == null || getName().length() == 0)
      setName(tableAlias);
    
    // Falls die Gruppe eventuell schon Kinder hat, dann müssen diesen
    // im Namen angepasst werden
    if(renameChildren)
    {
      renameChildIfRequired(this,save, tableAlias);
      
      List<UIGroupElementModel> children= getElements();
      for (UIGroupElementModel child : children)
      {
        renameChildIfRequired(child,save, tableAlias);
        if(child instanceof UIButtonBarModel)
        {
          UIButtonBarModel bar = (UIButtonBarModel)child;
          List<IButtonBarElementModel> barChildren =bar.getElements();
          for (IButtonBarElementModel barChild : barChildren)
          {
            renameChildIfRequired(barChild, save, tableAlias);
          }
        }
        else if(child instanceof UIGroupContainer)
        {
          UIGroupContainer container = (UIGroupContainer)child;
          List<UIGroupModel> groupChildren =container.getElements();
          for (UIGroupModel groupChild : groupChildren)
          {
            if(groupChild.getTableAlias().equals(save))
              groupChild.setTableAlias(tableAlias,true);
          }
        }
        else if(child instanceof UITabContainerModel)
        {
          UITabContainerModel container = (UITabContainerModel)child;
          List<UIGroupModel> groupChildren =container.getPanes();
          for (UIGroupModel groupChild : groupChildren)
          {
            if(groupChild.getTableAlias().equals(save))
              groupChild.setTableAlias(tableAlias,true);
          }
          
        }
     }
    }
  }


  private void renameChildIfRequired(IButtonBarElementModel child, String firstPartOld, String firtPartNew) throws Exception
  {
    if(StringUtils.isBlank(firstPartOld))
      return;
    
    String oldName = child.getName();
    if(oldName.startsWith(firstPartOld))
    {
      String newName = firtPartNew+oldName.substring(firstPartOld.length());
      child.setName(newName);
    }
  }
  
  private void renameChildIfRequired(UIFormElementModel child, String firstPartOld, String firtPartNew) throws Exception
  {
    if(StringUtils.isBlank(firstPartOld))
      return;
    
    String oldName = child.getName();
    if(oldName.startsWith(firstPartOld))
    {
      String newName = firtPartNew+oldName.substring(firstPartOld.length());
      child.setName(newName);
    }
  }
  
  public String getTableAlias()
  {
    return castor.getAlias();
  }

  public TableAliasModel getTableAliasModel()
  {
    return getJacobModel().getTableAliasModel(castor.getAlias());
  }

  @Override
  public CastorDimension getCastorDimension()
  {
    return castor.getDimension();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void removeCastorProperty(CastorProperty property)
  {
    for (int i = 0; i < castor.getPropertyCount(); i++)
    {
      if (castor.getProperty(i) == property)
      {
        castor.removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property [" + property.getName() + "] is not part of " + getClass().getName());
  }

  protected CastorGroup getCastor()
  {
    return castor;
  }

  public void addElement(UIDBLocalInputFieldModel element)
  {
    addElement((UIGroupElementModel) element);
  }

  /**
   * Hinzufügen einer neuen Caption. 
   * Das Castor Element einer Caption wird vom Vaterelement gehalten. Es ist somit nicht
   * notwendig hier das Castorelement gesondert zu behandeln.
   * 
   * @param element
   */
  public void addElement(UICaptionModel element)
  {
    elements.add(element);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, element);
    getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED, null, this);
  }
  
  public void addElement(UIGroupElementModel element)
  {
    this.addElement(element,false);
  }
  
  /**
   * 
   * @param element
   */
  public void addElement(UIGroupElementModel element, boolean asFirstElement)
  {
    try
    {
      Version version = element.getRequiredJacobVersion();
      Version currentJacobBaseVersion = JacobDesigner.getUsedJacobBaseVersion();
      if(!currentJacobBaseVersion.isGreaterOrEqual(version))
      {
        String type = ClassUtil.getShortClassName(element.getClass());
        if(element instanceof UIPluginComponentModel)
          type = ClassUtil.getShortClassName(((UIPluginComponentModel)element).getJavaImplClass());
        MessageDialog.openError(null,"Wrong jACOB Version","The project did have a wrong version of the jacobBase.jar for ["+type+"] integration.\n\n"+
                                                           "Required Version:"+version.toString()+"\n"+
                                                           "Project Version:"+currentJacobBaseVersion.toString()+"\n\n"+
                                                           "Replace the [./build/jacobBase.jar] and [./build/OpenjACOB.war] with the latest jACOB version.");
        return;
      }
    }
    catch(Exception exc)
    {
      JacobDesigner.showException(exc);
    }
    
    // init the lazy data structur if required
    if(elements==null)
      getElements();
    
    if(asFirstElement)
    {
      castor.addGuiElement(0,element.getCastor());
      elements.add(0, element);
    }
    else
    {
      castor.addGuiElement(element.getCastor());
      elements.add(element);
    }
    element.setJacobModel(getJacobModel());
    element.setGroup(this);

    if (element instanceof UIICaptionProviderModel && ((UIICaptionProviderModel)element).isCaptionExtern())
    {
      UICaptionModel captionModel = ((UIICaptionProviderModel) element).getCaptionModel();
      elements.add(captionModel);
    }
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, element);
    getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED, null, this);
    
    if(element instanceof UIPluginComponentModel)
    {
      UIPluginComponentModel pluginModel = (UIPluginComponentModel)element;
      String clazz = pluginModel.getJavaImplClass();
      IComponentPlugin plugin =PluginComponentManager.getComponentPlugin(clazz);
      try
      {
        plugin.checkResources();
      }
      catch(Exception exc)
      {
        JacobDesigner.showException(exc);
      }
    }
  }

  public void addElement(ContextMenuEntryModel element)
  {
    contextMenuEntries.add(element);
    castor.addContextMenuEntry(element.getMenuCastor());
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, element);
  }


  public void addElement(SelectionActionModel action)
  {
    if(JacobDesigner.ensureVersion(ClassUtil.getShortClassName(action.getCastor().getClassName()),action.getRequiredJacobVersion())==false)
      return;

    selectionActionModels.add(action);
    castor.addSelectionActionEventHandler(action.getCastor());
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, action);
  }
  
  public void removeElement(UIDBLocalInputFieldModel element)
  {
    removeElement((UIGroupElementModel) element);
  }

  public void removeElement(ContextMenuEntryModel element)
  {
    int index = contextMenuEntries.indexOf(element);
    if (index != -1)
    {
      contextMenuEntries.remove(element);
      castor.removeContextMenuEntry(index);
      firePropertyChange(PROPERTY_ELEMENT_REMOVED, element, null);
      getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED, null, this);
    }
  }


  public void removeElement(SelectionActionModel action)
  {
    int index = selectionActionModels.indexOf(action);
    if (index != -1)
    {
      selectionActionModels.remove(action);
      castor.removeSelectionActionEventHandler(index);
      firePropertyChange(PROPERTY_ELEMENT_REMOVED, action, null);
      getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED, null, this);
    }
  }
  
  /**
   * 
   * @param element
   */
  public void removeElement(UIGroupElementModel element)
  {
    if(element instanceof UICaptionModel)
    {
      // Element entfernen
      //
      if(elements.remove(element))
      {
        // Dem zugehörigen Vater mitteilen, dass ein Kindelement entfern wurde
        //
        ((UICaptionModel)element).getCaptionProvider().setCastorCaption(null);
        
        firePropertyChange(PROPERTY_ELEMENT_REMOVED, element, null);
        getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED, null, this);
      }
    }
    else
    {
      for (int i = 0; i < castor.getGuiElementCount(); i++)
      {
        if (castor.getGuiElement(i) == element.getCastor())
        {
          castor.removeGuiElement(i);
          break;
        }
      }
      elements.remove(element);
      
      // remove the caption of the input element too
      //
      if (element instanceof UIICaptionProviderModel)
        elements.remove(((UIICaptionProviderModel) element).getCaptionModel());
      
      
      
      firePropertyChange(PROPERTY_ELEMENT_REMOVED, element, null);
      getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED, null, this);
    }
  }

  /**
   * 
   */
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 6];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME, "Name", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 1] = new TextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 2] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_TABLEALIAS, "Table alias", PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 3] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_BROWSER, "Browser", (String[]) getJacobModel().getBrowserNames(getTableAliasModel()).toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length + 4] = new CheckboxPropertyDescriptor(ID_PROPERTY_BORDER, "Border", PROPERTYGROUP_COMMON);
    descriptors[superDescriptors.length + 5] = new CheckboxPropertyDescriptor(ID_PROPERTY_HIDE_EMPTY_BROWSER, "Hide Empty Browser", PROPERTYGROUP_COMMON);

    return descriptors;
  }

  /**
   * 
   */
  public void setPropertyValue(Object propName, Object val)
  {
    try
    {
      if (propName == ID_PROPERTY_NAME)
        setName((String) val);
      else if (propName == ID_PROPERTY_LABEL)
        setLabel((String) val);
      else if (propName == ID_PROPERTY_BORDER)
        setBorder(((Boolean) val).booleanValue());
      else if (propName == ID_PROPERTY_HIDE_EMPTY_BROWSER)
        setHideEmptyBrowser(((Boolean) val).booleanValue());
//      else if (propName == ID_PROPERTY_TABLEALIAS)
//        setTableAlias((String) getJacobModel().getCorrespondingTableAliasNames(getTableAliasModel()).get(((Integer) val).intValue()));
      else if (propName == ID_PROPERTY_BROWSER)
        setBrowser((String) getJacobModel().getBrowserNames(getTableAliasModel()).get(((Integer) val).intValue()));
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
    if (propName == ID_PROPERTY_NAME)
      return StringUtil.toSaveString(getName());
    if (propName == ID_PROPERTY_LABEL)
      return StringUtil.toSaveString(getLabel());
    if (propName == ID_PROPERTY_TABLEALIAS)
      return getTableAlias();
    if (propName == ID_PROPERTY_BORDER)
      return new Boolean(getHasBorder());
    if (propName == ID_PROPERTY_HIDE_EMPTY_BROWSER)
      return new Boolean(getHideEmptyBrowser());
    if (propName == ID_PROPERTY_BROWSER)
    {
      List browsers = getJacobModel().getBrowserNames(getTableAliasModel());
      return new Integer(browsers.indexOf(getBrowserModel().getName()));
    }
    return super.getPropertyValue(propName);
  }

  // Den Namen einer Relation zu ï¿½ndern ist ein wenig 'tricky'. Alle Referenzen
  // in den ForeignFields ( dies sind nur
  // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
  //
  protected void renameRelationReference(String from, String to)
  {
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
    	UIGroupElementModel obj = (UIGroupElementModel)iter.next();
      obj.renameRelationReference(from, to);
    }
  }

  protected void renameAliasReference(String from, String to)
  {
    // rename the anchor table of the group
    //
    if (castor.getAlias().equals(from))
      castor.setAlias(to);
    // rename the anchor table of the foreign fields
    //
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
    	UIGroupElementModel obj = (UIGroupElementModel)iter.next();
      obj.renameAliasReference(from, to);
    }
  }

  protected void renameRelationsetReference(String from, String to)
  {
    // rename the relationset of foreign fields
    //
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj = (UIGroupElementModel)iter.next();
      obj.renameRelationsetReference(from, to);
    }
    // relationset im contextmenu umbenennen
    //
    iter = getContextMenuEntries().iterator();
    while (iter.hasNext())
    {
    	UIGroupElementModel model = (UIGroupElementModel) iter.next();
      model.renameRelationsetReference(from, to);
    }
  }

  public void renameEventHandler(String fromClass, String toClass)
  {
    // Nicht StringUtils.saveEquals(..) verwenden. Da sind null und "" gleich!!
    // Desweiteren darf dieser Teil nicht ausgefÃ¼hrt werden wenn zuvor kein
    // Eventhandler
    // definert war. Wenn zuvor kein Eventhandler definiert war, dann kann man
    // diesen auch nicht umbenennen.
    //
    if (castor.getEventHandler() != null && castor.getEventHandler().equals(fromClass))
    {
      castor.setEventHandler(toClass);
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, fromClass, toClass);
    }
    
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj = (UIGroupElementModel) iter.next();
      obj.renameEventHandler(fromClass, toClass);
    }
    
    // eventhandler im contextmenu umbenennen
    //
    iter = getContextMenuEntries().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj = (UIGroupElementModel) iter.next();
      // Nicht StringUtils.saveEquals(..) verwenden. Da sind null und ""
      // gleich!!
      // Desweiteren darf dieser Teil nicht ausgefï¿½hrt werden wenn zuvor kein
      // Eventhandler definiert war. Wenn zuvor kein Eventhandler definiert war, 
      // dann kann man diesen auch nicht umbenennen.
      //
      if (obj.getCastorEventHandler() != null && obj.getCastorEventHandler().equals(fromClass))
      {
        obj.setCastorEventHandler(toClass);
        firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, fromClass, toClass);
      }
    }
  }

  public void renameBrowserReference(String from, String to)
  {
    // rename the browser of the group
    //
    if (castor.getBrowser().equals(from))
      castor.setBrowser(to);
    // rename the anchor table of the foreign fields
    //
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj = (UIGroupElementModel)iter.next();
      obj.renameBrowserReference(from, to);
    }
  }

  /*
   * remove an entry in the enum and all the GUI references.
   * 
   */
  protected void removeEnumReference(FieldModel field, String value)
  {
    renameEnumReference(field, value, null);
  }

  /*
   * 
   */
  protected void renameEnumReference(FieldModel field, String fromValue, String toValue)
  {
    if (field.getTableModel() != getTableAliasModel().getTableModel())
      return;
    for (int i = 0; i < castor.getGuiElementCount(); i++)
    {
      CastorGuiElement element = castor.getGuiElement(i);
      if (element.getCastorGuiElementChoice().getLocalInputField() != null && element.getCastorGuiElementChoice().getLocalInputField().getComboBox() != null)
      {
        LocalInputField local = element.getCastorGuiElementChoice().getLocalInputField();
        ComboBox combo = local.getComboBox();
        if (field.getName().equals(local.getTableField()) && combo != null)
        {
          // enum UI Element gefunden welches sich auf das DB-Enum bezieht. Wert
          // jetzt umbenennen
          //
          for (int index = 0; index < combo.getValueCount(); index++)
          {
            if (combo.getValue(index).equals(fromValue))
            {
              System.out.println(fromValue + "->" + toValue);
              if (toValue == null)
                combo.removeValue(index);
              else
                combo.setValue(index, toValue);
              break;
            }
          }
        }
      }
    }
  }

  /*
   * 
   */
  protected void renameFieldReference(FieldModel field, String fromName, String toName)
  {
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj = (UIGroupElementModel)iter.next();
      obj.renameFieldReference(field, fromName, toName);
    }
  }

  /*
   * 
   */
  protected void createMissingI18NKey()
  {
    String label = castor.getLabel();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);

    Iterator iter = getContextMenuEntries().iterator();
    while(iter.hasNext())
    {
      ContextMenuEntryModel menuEntry = (ContextMenuEntryModel)iter.next();
      label = menuEntry.getLabel();
      if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
        getJacobModel().addI18N(label.substring(1),"",false);
    }

    iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel element = (UIGroupElementModel) iter.next();
      element.createMissingI18NKey();
    }
  }
  
  /**
   * 
   * @param from the orignal key WITH the % at first character
   * @param to the new key WITH the % at first character
   */
  protected void renameI18NKey(String fromName, String toName)
  {
    if (castor.getLabel() != null && castor.getLabel().equals(fromName))
      setLabel(toName); // firePropertyChangeEvent(!!!) to update the GUI-Editor
    
    for (UIGroupElementModel element : getElements())
    {
      element.renameI18NKey(fromName, toName);
    }

    for (ContextMenuEntryModel menuEntry : getContextMenuEntries())
    {
      if(menuEntry.getAction()==ObjectModel.ACTION_GENERIC || menuEntry.getAction()==ObjectModel.ACTION_SELECTED)
      {
        menuEntry.renameI18NKey(fromName, toName);
      }
    }
  }

  protected void resetI18N()
  {
  	String oldLabel = getLabel();
    castor.setLabel("%GROUP"+getJacobModel().getSeparator()+getName().toUpperCase());

    if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
      getJacobModel().addI18N(getLabel().substring(1),oldLabel,false);
    else
      getJacobModel().addI18N(getLabel().substring(1),getName(),false);

    
    // Kontemenüs anpassen
    //
    for (ContextMenuEntryModel menuEntry : getContextMenuEntries())
    {
      if(menuEntry.getAction()==ObjectModel.ACTION_GENERIC || menuEntry.getAction()==ObjectModel.ACTION_SELECTED)
      {
        oldLabel = menuEntry.getLabel();
        menuEntry.setLabel("%CONTEXTMENU"+getJacobModel().getSeparator()+menuEntry.getName().toUpperCase());
        if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
          getJacobModel().addI18N(menuEntry.getLabel().substring(1),oldLabel,false);
        else
          getJacobModel().addI18N(menuEntry.getLabel().substring(1),getName(),false);
      }
    }

    
    for (UIGroupElementModel element : getElements())
    {
      if (element instanceof UILabelModel)
      {
        UILabelModel label = (UILabelModel) element;
        if (label.getLabel() != null)
        {
          oldLabel = label.getLabel();
          label.setLabel("%LABEL"+getJacobModel().getSeparator()+label.getName().toUpperCase());
          if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
            getJacobModel().addI18N(label.getLabel().substring(1),oldLabel,false);
          else
            getJacobModel().addI18N(label.getLabel().substring(1),label.getName(),false);
        }
      }
      else if (element instanceof UICaptionModel)
      {
        try
        {
          UICaptionModel caption = (UICaptionModel) element;
          oldLabel = caption.getCaption();
          caption.setCaption("%"+caption.suggestI18NKey());
          if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
            getJacobModel().addI18N(caption.getCaption().substring(1),oldLabel,false);
          else
            getJacobModel().addI18N(caption.getCaption().substring(1),caption.getName(),false);
        }
        catch (Exception e)
        {
          JacobDesigner.showException(e);
        }
      }
      else if(element instanceof UIDBInformBrowserModel)
      {
        UIDBInformBrowserModel browser = (UIDBInformBrowserModel)element;
        oldLabel = browser.getCaption();
        browser.setCaption("%"+browser.suggestI18NKey());
        if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
          getJacobModel().addI18N(browser.getCaption().substring(1),oldLabel,false);
        else
          getJacobModel().addI18N(browser.getCaption().substring(1),StringUtils.capitalise(browser.getTableAliasModel().getName()),false);
      }
      else if (element instanceof UIButtonModel)
      {
        UIButtonModel button = (UIButtonModel) element;
        if(button.getAction()==ObjectModel.ACTION_GENERIC || button.getAction()==ObjectModel.ACTION_SELECTED)
        {
          oldLabel = button.getLabel();
          button.setLabel("%BUTTON"+getJacobModel().getSeparator()+button.getName().toUpperCase());
          if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
            getJacobModel().addI18N(button.getLabel().substring(1),oldLabel,false);
          else
            getJacobModel().addI18N(button.getLabel().substring(1),StringUtils.capitalise(button.getName()),false);
        }
      }
    }
    
  }

  protected boolean isI18NKeyInUse(String key)
  {
    if (castor.getLabel() != null && castor.getLabel().equals(key))
      return true;
                  
    Iterator iter = getContextMenuEntries().iterator();
    while(iter.hasNext())
    {
      ContextMenuEntryModel menuEntry = (ContextMenuEntryModel)iter.next();
      if (menuEntry.getLabel() != null && menuEntry.getLabel().equals(key))
        return true;
    }

    for (UIGroupElementModel element:getElements())
    {
      if(element.isI18NKeyInUse(key))
        return true;
    }
    return false;
  }

  public void upElement(ContextMenuEntryModel model)
  {
    int index = contextMenuEntries.indexOf(model);
    contextMenuEntries.remove(model);
    ContextMenuEntry castorField = castor.removeContextMenuEntry(index);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
    contextMenuEntries.add(index - 1, model);
    castor.addContextMenuEntry(index - 1, castorField);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, model);
  }

  public void downElement(ContextMenuEntryModel model)
  {
    int index = contextMenuEntries.indexOf(model);
    contextMenuEntries.remove(model);
    ContextMenuEntry castorField = castor.removeContextMenuEntry(index);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
    contextMenuEntries.add(index + 1, model);
    castor.addContextMenuEntry(index + 1, castorField);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, model);
  }

  public void upElement(SelectionActionModel model)
  {
    int index = selectionActionModels.indexOf(model);
    selectionActionModels.remove(model);
    SelectionActionEventHandler castorField = castor.removeSelectionActionEventHandler(index);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
    selectionActionModels.add(index - 1, model);
    castor.addSelectionActionEventHandler(index - 1, castorField);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, model);
  }

  public void downElement(SelectionActionModel model)
  {
    int index = selectionActionModels.indexOf(model);
    selectionActionModels.remove(model);
    SelectionActionEventHandler castorField = castor.removeSelectionActionEventHandler(index);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
    selectionActionModels.add(index + 1, model);
    castor.addSelectionActionEventHandler(index + 1, castorField);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, model);
  }

  public String getChildrenDefaultJavaPackage()
  {
    String defaultPackage = getCastorStringProperty(ID_PROPERTY_DEFAULTPACKAGE);

    if(StringUtils.isNotEmpty(defaultPackage))
      return defaultPackage;
    
    return "jacob.event.ui." + getGroupTableAlias().toLowerCase();
  }
  
  public void setChildrenDefaultJavaPackage(String defaultPackage)
  {
    if(StringUtils.isEmpty(defaultPackage))
      setCastorProperty(ID_PROPERTY_DEFAULTPACKAGE,null);
    else
      setCastorProperty(ID_PROPERTY_DEFAULTPACKAGE,defaultPackage);
  }
  
  public String getHookClassName()
  {
     return castor.getEventHandler();
  }

  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "IGroupEventHandler.java";
  }

  public String getError()
  {
    Iterator<UIGroupElementModel> iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj = iter.next();
      if (obj instanceof UIDBLocalInputFieldModel)
      {
        UIDBLocalInputFieldModel field = (UIDBLocalInputFieldModel) obj;
        if (field.getFieldModel() == field.getFieldModel().getTableModel().NULL_FIELD)
          return "UI element [" + field.getName() + "] has not a valid reference to column in the table [" + field.getFieldModel().getTableModel().getName() + "]";
      }
      String error = obj.getError();
      if (error != null)
        return error;
    }
    return null;
  }

  @Override
  public String getWarning()
  {
    String caption = getLabel();
    if (caption != null && caption.startsWith("%") && getJacobModel().hasI18NKey(caption.substring(1)) == false)
      return "No localization entry for [" + caption.substring(1) + "] found";
    Iterator<UIGroupElementModel> iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj = iter.next();
      String warning = obj.getWarning();
      if (warning != null)
        return warning;
    }
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

  public String suggestI18NKey()
  {
    return "GROUP" + getJacobModel().getSeparator() + getTableAlias().toUpperCase();
  }
  
  public void reorderStandardButtons()
  {
    // Falls der Benutzer sein eigenes Template definiert hat, dann
    // wird dieses verwendet.
    //
    if(JacobDesigner.getPluginProperty(JacobDesigner.TEMPLATE_USAGE, false))
    {
      // Ausrichtungskanten im Plugin preference store speichern
      //
      boolean alignLeft = JacobDesigner.getPluginProperty(JacobDesigner.TEMPLATE_BUTTON_LEFT, false);
      boolean alignTop  = JacobDesigner.getPluginProperty(JacobDesigner.TEMPLATE_BUTTON_TOP,  false);

      
      // Jetzt muss für die Standarbuttons bestimmt werden wie diese relativ zu der
      // ermittelten Ausrichtungskanten liegen.
      // (z.B. ButtonNew liegt immer [20,50] relative zu der linken oberen Kante) 
      //
      Iterator iter = getStandardButtons().iterator();
      while (iter.hasNext())
      {
        UIButtonModel button = (UIButtonModel) iter.next();
        String action  = button.getAction();
        if(JacobDesigner.hasPluginProperty("BUTTON_"+action+"_Y"))
        {
          Point loc = new Point();
          if(alignLeft)
            loc.x = JacobDesigner.getPluginProperty("BUTTON_"+action+"_X",10);
          else
            loc.x = getSize().width-JacobDesigner.getPluginProperty("BUTTON_"+action+"_X",10);

          if(alignTop)
            loc.y = JacobDesigner.getPluginProperty("BUTTON_"+action+"_Y",10);
          else
            loc.y = getSize().height-JacobDesigner.getPluginProperty("BUTTON_"+action+"_Y",10);
          button.setLocation(loc);
        }
      }
    }
  }
  

  public void addStandardButtons(Properties properties)
  {
    try
    {
        boolean alignLeft = Boolean.parseBoolean( properties.getProperty(JacobDesigner.TEMPLATE_BUTTON_LEFT, "false"));
        boolean alignTop  = Boolean.parseBoolean(properties.getProperty(JacobDesigner.TEMPLATE_BUTTON_TOP,  "false"));

        
        // Jetzt muss für die Standarbuttons bestimmt werden wie diese relativ zu der
        // ermittelten Ausrichtungskanten liegen.
        // (z.B. ButtonNew liegt immer [20,50] relative zu der linken oberen Kante) 
        //
        Iterator iter = AbstractActionEmitterModel.FAVOUR_ACTIONS.iterator();
        while (iter.hasNext())
        {
          String action = (String) iter.next();
          if(properties.containsKey("BUTTON_"+action+"_Y"))
          {
            Point loc = new Point();
            if(alignLeft)
              loc.x = NumberUtils.stringToInt(properties.getProperty("BUTTON_"+action+"_X"),10);
            else
              loc.x = getSize().width-NumberUtils.stringToInt(properties.getProperty("BUTTON_"+action+"_X"),10);

            if(alignTop)
              loc.y = NumberUtils.stringToInt( properties.getProperty("BUTTON_"+action+"_Y"),10);
            else
              loc.y = getSize().height-NumberUtils.stringToInt( properties.getProperty("BUTTON_"+action+"_Y"),10);
            
            UIButtonModel button = new UIButtonModel();
            button.setSize(new Dimension(70,20));
            button.setAction(action);
            button.setLocation(loc);
            addElement(button);
            button.setName(button.getDefaultName());
          }
        }
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public Rectangle getButtonBoundingRect()
  {
    // Standartbuttons der Gruppe zusammensuchen
    // (search, clear, new, update, delete)
    //
    List buttons = new ArrayList();
    Iterator iter = getElements().iterator();
    
    while (iter.hasNext())
    {
      ObjectModel obj = (ObjectModel) iter.next();
      if(obj instanceof UIButtonModel)
      {
        UIButtonModel button = (UIButtonModel)obj;
        if(AbstractActionEmitterModel.FAVOUR_ACTIONS.contains(button.getAction()))
        {
          buttons.add(button);
        }
      }
    }
    if(buttons.size()==0)
      return null;
    
    Iterator iterator = buttons.iterator();
    
    UIButtonModel button = (UIButtonModel) iterator.next();
    Rectangle boundingRect = button.getConstraint().getCopy();
    
    while(iterator.hasNext())
    {
      button = (UIButtonModel) iterator.next();
      boundingRect = boundingRect.getUnion(button.getConstraint());
    }
    return boundingRect;
  }

  public List getStandardButtons()
  {
    List buttons = new ArrayList();
    Iterator iter = getElements().iterator();
    
    while (iter.hasNext())
    {
      ObjectModel obj = (ObjectModel) iter.next();
      if(obj instanceof UIButtonModel)
      {
        UIButtonModel button = (UIButtonModel)obj;
        if(AbstractActionEmitterModel.FAVOUR_ACTIONS.contains(button.getAction()))
        {
          buttons.add(button);
        }
      }
    }
    return buttons;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getTableAliasModel())
      result.addReferences(this);
    
    if(model == getBrowserModel())
      result.addReferences(this);

    for (UIGroupElementModel element : getElements())
    {
      element.addReferrerObject(result,model);
    }

    for (ContextMenuEntryModel menuEntry : getContextMenuEntries())
    {
       menuEntry.addReferrerObject(result,model);
    }
  }

  public void openEditor()
  {
    new ShowJacobFormEditorAction()
    {
      @Override
      public UIJacobFormModel getFormModel()
      {
        return getJacobFormModel();
      }
    }.run(null);
  } 
  
  @Override
  public ObjectModel getParent()
  {
    return getJacobFormModel();
  }


  public List<SelectionActionModel> getActions()
  {
    return selectionActionModels;
  }

 }
