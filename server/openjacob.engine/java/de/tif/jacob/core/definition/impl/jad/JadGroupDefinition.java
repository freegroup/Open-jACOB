/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.core.definition.impl.jad;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearGroup;
import de.tif.jacob.core.definition.actiontypes.ActionTypeDeleteRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeGeneric;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNavigateToForm;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeRecordSelected;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearchUpdateRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeUpdateRecord;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.BarChartDefinition;
import de.tif.jacob.core.definition.guielements.BreadCrumbDefinition;
import de.tif.jacob.core.definition.guielements.ButtonDefinition;
import de.tif.jacob.core.definition.guielements.CalendarDefinition;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.CheckBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.ComboBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DateInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.DocumentInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DynamicImageDefinition;
import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.core.definition.guielements.ForeignInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.GUIElementDefinition;
import de.tif.jacob.core.definition.guielements.HeadingDefinition;
import de.tif.jacob.core.definition.guielements.ImageDefinition;
import de.tif.jacob.core.definition.guielements.InFormBrowserDefinition;
import de.tif.jacob.core.definition.guielements.InFormLongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LabelDefinition;
import de.tif.jacob.core.definition.guielements.LineChartDefinition;
import de.tif.jacob.core.definition.guielements.ListBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LongTextInputMode;
import de.tif.jacob.core.definition.guielements.OwnDrawElementDefinition;
import de.tif.jacob.core.definition.guielements.PasswordInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.PluginComponentDefinition;
import de.tif.jacob.core.definition.guielements.RadioButtonGroupInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.ResizeMode;
import de.tif.jacob.core.definition.guielements.StaticImageDefinition;
import de.tif.jacob.core.definition.guielements.StyledTextDefinition;
import de.tif.jacob.core.definition.guielements.TableListBoxDefinition;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TimeInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TimeLineDefinition;
import de.tif.jacob.core.definition.guielements.TimestampInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.Alignment.Horizontal;
import de.tif.jacob.core.definition.impl.AbstractGroupDefinition;
import de.tif.jacob.core.definition.impl.ContextMenuEntryDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.BarChart;
import de.tif.jacob.core.definition.impl.jad.castor.BreadCrumb;
import de.tif.jacob.core.definition.impl.jad.castor.Calendar;
import de.tif.jacob.core.definition.impl.jad.castor.CastorAction;
import de.tif.jacob.core.definition.impl.jad.castor.CastorButton;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFont;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.CheckBox;
import de.tif.jacob.core.definition.impl.jad.castor.ComboBox;
import de.tif.jacob.core.definition.impl.jad.castor.Container;
import de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry;
import de.tif.jacob.core.definition.impl.jad.castor.DateInput;
import de.tif.jacob.core.definition.impl.jad.castor.DocumentInput;
import de.tif.jacob.core.definition.impl.jad.castor.DynamicImage;
import de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer;
import de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField;
import de.tif.jacob.core.definition.impl.jad.castor.Heading;
import de.tif.jacob.core.definition.impl.jad.castor.Image;
import de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser;
import de.tif.jacob.core.definition.impl.jad.castor.Label;
import de.tif.jacob.core.definition.impl.jad.castor.LineChart;
import de.tif.jacob.core.definition.impl.jad.castor.ListBox;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextInput;
import de.tif.jacob.core.definition.impl.jad.castor.OwnDrawElement;
import de.tif.jacob.core.definition.impl.jad.castor.PasswordInput;
import de.tif.jacob.core.definition.impl.jad.castor.PluginComponent;
import de.tif.jacob.core.definition.impl.jad.castor.RadioButtonGroup;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;
import de.tif.jacob.core.definition.impl.jad.castor.StaticImage;
import de.tif.jacob.core.definition.impl.jad.castor.StyledText;
import de.tif.jacob.core.definition.impl.jad.castor.TableListBox;
import de.tif.jacob.core.definition.impl.jad.castor.TextInput;
import de.tif.jacob.core.definition.impl.jad.castor.TimeInput;
import de.tif.jacob.core.definition.impl.jad.castor.TimeLine;
import de.tif.jacob.core.definition.impl.jad.castor.TimestampInput;
import de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType;
import de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType;
import de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadGroupDefinition extends AbstractGroupDefinition
{
  static public transient final String RCS_ID = "$Id: JadGroupDefinition.java,v 1.47 2010/10/22 11:50:20 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.47 $";
  
  private final ResizeMode resizeMode ;
  
	/**
	 * @param name
	 * @param label
	 * @param tableAlias
	 * @param activeBrowserDefinition
	 * @param dimension
	 */
	public JadGroupDefinition(CastorGroup group, JadDefinition definition) throws Exception
	{
		super(group.getName(), group.getLabel(), group.getDescription(), group.getBorder(), group.getHideEmptyBrowser(), group.getEventHandler(), definition.getTableAlias(group.getAlias()), definition.getBrowserDefinition(group.getBrowser()),
        new Dimension(group.getDimension()), group.getBorderWidth(), group.getBorderColor(), group.getBackgroundColor());
    
		this.resizeMode = ResizeMode.fromJacob(group.getResizeMode());
		
    // handle properties
    if (group.getPropertyCount() > 0)
      putCastorProperties(group.getProperty());
    
		// fetch selection actions
		for (int i = 0; i < group.getSelectionActionEventHandlerCount(); i++)
		{
      SelectionActionEventHandler castorAction = group.getSelectionActionEventHandler(i);
      JadSelectionActionDefinition selectionActionDefinition = new JadSelectionActionDefinition(castorAction);
      if (castorAction.getPropertyCount() > 0)
        selectionActionDefinition.putCastorProperties(castorAction.getProperty());
      addSelectionAction(selectionActionDefinition);
		}

    // fetch context menu entries
    for (int i = 0; i < group.getContextMenuEntryCount(); i++)
    {
      ContextMenuEntry contextMenuEntry = group.getContextMenuEntry(i);
      ContextMenuEntryDefinition contextMenuEntryDefinition = createContextMenuEntry(contextMenuEntry, definition);
      if (contextMenuEntry.getPropertyCount() > 0)
        contextMenuEntryDefinition.putCastorProperties(contextMenuEntry.getProperty());
      addContextMenuEntry(contextMenuEntryDefinition);
    }

    // fetch gui elements
		for (int i = 0; i < group.getGuiElementCount(); i++)
		{
      CastorGuiElement castorGuiElement = group.getGuiElement(i);
      GUIElementDefinition guiElementDefinition = createGUIElement(castorGuiElement, definition);
      if (castorGuiElement.getPropertyCount() > 0)
      	  guiElementDefinition.putCastorProperties(castorGuiElement.getProperty());
			addGuiElement(guiElementDefinition);
		}
	}


	/**
	 * @since 2.10
	 */
  public ResizeMode getResizeMode()
  {
    return this.resizeMode;
  }

  private static ContextMenuEntryDefinition createContextMenuEntry(ContextMenuEntry entry, JadDefinition definition)
  {
    String name = entry.getName();
    String label = entry.getLabel();
    ActionType actionType = getActionType(entry.getAction(), definition);
    return new ContextMenuEntryDefinition(name, label, entry.getEventHandler(), actionType);
  }

  private static ActionType getActionType(CastorAction action, JadDefinition definition)
	{
		if (null != action.getGeneric())
			return new ActionTypeGeneric();

    else if (null != action.getNavigateToForm())
      return new ActionTypeNavigateToForm(action.getNavigateToForm().getFormName());

    else if (null != action.getNewRecord())
      return new ActionTypeNewRecord();

    else if (null != action.getSearch())
		{
			String relationSetName = action.getSearch().getRelationset();
			Filldirection filldirection = Filldirection.fromJad(action.getSearch().getFilldirection());
      boolean safeMode = action.getSearch().getSafeMode();
			return new ActionTypeSearch(definition.getRelationSet(relationSetName), filldirection, safeMode);
		}
    else if (null != action.getSearchUpdateRecord())
    {
      String relationSetName = action.getSearchUpdateRecord().getRelationset();
      Filldirection filldirection = Filldirection.fromJad(action.getSearchUpdateRecord().getFilldirection());
      boolean safeMode = action.getSearchUpdateRecord().getSafeMode();
      return new ActionTypeSearchUpdateRecord(definition.getRelationSet(relationSetName), filldirection, safeMode);
    }

		else if (null != action.getClearGroup())
			return new ActionTypeClearGroup();

		else if (null != action.getUpdateRecord())
		{
		  if(UpdateRecordExecuteScopeType.TAB_PANE== action.getUpdateRecord().getExecuteScope())
		    return new ActionTypeUpdateRecord(ActionType.SCOPE_TABPANE);
		  
		  return new ActionTypeUpdateRecord(ActionType.SCOPE_OUTERGROUP);
		}

		else if (null != action.getRecordSelected())
			return new ActionTypeRecordSelected();

		else if (null != action.getDeleteRecord())
			return new ActionTypeDeleteRecord(action.getDeleteRecord().getNeedUserConfirmation());

		else
			throw new RuntimeException("Unknown action");
	}

	private static Caption getCaption(CastorCaption caption, JadDefinition definition)
	{
		if (caption != null)
		{
      ActionType actionType = caption.getAction() == null ? null : getActionType(caption.getAction(), definition);
      String color = caption.getColor();
      if(caption.getFont()!=null)
      {
        CastorFont cfont=caption.getFont();
        
        return new Caption(caption,new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize()),caption.getEventHandler(), actionType, caption.getEllipsis(), color);
      }
      return new Caption( caption,null,caption.getEventHandler(), actionType,caption.getEllipsis(),color);
		}
		return null;
	}

  private ITableField getTableField(LocalInputField localInputField) throws NoSuchFieldException
  {
    if (null == localInputField.getTableField())
      return null;
    return getTableAlias().getTableDefinition().getTableField(localInputField.getTableField());
  }

  
  private GUIElementDefinition createGUIElement(CastorGuiElement element, JadDefinition definition) throws Exception
  {
    String name = element.getName();

    try
    {
      String description = element.getDescription();
      String inputHint = element.getInputHint();
      String eventHandler = element.getEventHandler();
      boolean visible = element.getVisible();
      int tabIndex = element.getTabIndex();
      int paneIndex = element.getPaneIndex();

      if (element.getCastorGuiElementChoice().getLocalInputField() != null)
      {
        LocalInputField localInputField = element.getCastorGuiElementChoice().getLocalInputField();
        ITableField localTableField = getTableField(localInputField);
        // there might be GUI elements which are not linked to any table field
        ITableAlias localTableAlias = localTableField == null ? null : getTableAlias();

        if (localInputField.getPasswordInput() != null)
        {
          PasswordInput object = localInputField.getPasswordInput();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }
          return new PasswordInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField,font);
        }
        else if (localInputField.getTextInput() != null)
        {
          TextInput object = localInputField.getTextInput();
          boolean multiline = object.getMultiline();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }
          Horizontal halign = object.getHalign()!=null?Alignment.fromJacob(object.getHalign()):null;

          return new TextInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, multiline, tabIndex, paneIndex, caption, localTableAlias,
              localTableField, font,halign);
        }
        else if (localInputField.getLongTextInput() != null)
        {
          LongTextInput object = localInputField.getLongTextInput();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);

          LongTextInputMode mode = LongTextInputMode.fromJacob(object.getMode());
          boolean wordwrap = object.getWordWrap();
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }

          if (object.getInForm())
          {
              return new InFormLongTextInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias,
                  localTableField, mode, wordwrap,font, object.getContentType().toString());
          }
          else
          {
              return new LongTextInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias,
                  localTableField, mode, wordwrap,font);
          }
        }
        else if (localInputField.getCheckBox() != null)
        {
          CheckBox object = localInputField.getCheckBox();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          return new CheckBoxInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias,
              localTableField);
        }
        else if (localInputField.getComboBox() != null)
        {
          ComboBox object = localInputField.getComboBox();

          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          String[] visibleValues = object.getValue();
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }
          return new ComboBoxInputFieldDefinition(name, description, inputHint, eventHandler, visibleValues,
                                                 object.getCallHookOnSelect(), 
                                                 object.getAllowUserDefinedValue(), 
                                                 object.getAllowNullSearch(), 
                                                 object.getAllowNotNullSearch(), 
                                                 position, visible, false, tabIndex, paneIndex, caption,
              localTableAlias, localTableField,font);
        }
        else if (localInputField.getRadioButtonGroup() != null)
        {
          RadioButtonGroup object = localInputField.getRadioButtonGroup();

          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          String[] visibleValues = object.getValue();
          return new RadioButtonGroupInputFieldDefinition(name, description, inputHint, eventHandler, visibleValues,object.getCallHookOnSelect(), position, visible, false, tabIndex, paneIndex, caption,
              localTableAlias, localTableField);
        }
        else if (localInputField.getListBox() != null)
        {
          ListBox object = localInputField.getListBox();

          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          String[] visibleValues = object.getValue();
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }
          return new ListBoxInputFieldDefinition(name, description, inputHint, eventHandler, visibleValues,object.getCallHookOnSelect(), object.getMultiselect(), position, visible, false, tabIndex, paneIndex, caption,
              localTableAlias, localTableField,font);
        }
        else if (localInputField.getDateInput() != null)
        {
          DateInput object = localInputField.getDateInput();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }
          return new DateInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField,font);
        }
        else if (localInputField.getImage() != null)
        {
          Image object = localInputField.getImage();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          return new ImageDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField);
        }
        else if (localInputField.getTimeInput() != null)
        {
          TimeInput object = localInputField.getTimeInput();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }
          return new TimeInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField,font);
        }
        else if (localInputField.getDocumentInput() != null)
        {
          DocumentInput object = localInputField.getDocumentInput();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }
          return new DocumentInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias,
              localTableField,font);
        }
        else if (localInputField.getTimestampInput() != null)
        {
          TimestampInput object = localInputField.getTimestampInput();
          Dimension position = new Dimension(object.getDimension());
          Caption caption = getCaption(object.getCaption(), definition);
          FontDefinition font=null;
          if(object.getFont()!=null)
          {
            CastorFont cfont=object.getFont();
            font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
          }
          return new TimestampInputFieldDefinition(name, description, inputHint, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias,
              localTableField,font);
        }
      }
      else if (element.getCastorGuiElementChoice().getButton() != null)
      {
        CastorButton button = element.getCastorGuiElementChoice().getButton();
        String label = button.getLabel();
        Boolean _default = button.hasDefault()?Boolean.valueOf(button.getDefault()):null;
        ActionType actionType = getActionType(button.getAction(), definition);
        Horizontal halign = button.getHalign()!=null?Alignment.fromJacob(button.getHalign()):null;
        Dimension position = new Dimension(button.getDimension());
        return new ButtonDefinition(name, description, eventHandler, label, visible, button.getEmphasize(), _default, tabIndex, paneIndex, actionType, //
            position, halign, element.getBorderWidth(), element.getBorderColor(), element.getBackgroundColor());
      }
      else if (element.getCastorGuiElementChoice().getContainer() != null)
      {
        Container container = element.getCastorGuiElementChoice().getContainer();
        Dimension position = new Dimension(container.getDimension());

        // fetch panes
        List panes = new ArrayList();
        for (int i = 0; i < container.getPaneCount(); i++)
        {
          CastorGroup castorGroup = container.getPane(i);
          JadGroupDefinition groupDef = new JadGroupDefinition(castorGroup, definition);
          panes.add(groupDef);
        }
        if(container.getLayout() == ContainerLayoutType.TAB_STRIP)
          return new JadTabContainerDefinition(element.getName(), element.getDescription(), element.getEventHandler(), element.getVisible(), position, panes, element.getBorderWidth(), element.getBorderColor(), element.getBackgroundColor(), container.getResizeMode());
        return new JadStackContainerDefinition(element.getName(), element.getDescription(), element.getEventHandler(), element.getVisible(), position, panes, element.getBorderWidth(), element.getBorderColor(), element.getBackgroundColor(), container.getResizeMode());
      }
      else if (element.getCastorGuiElementChoice().getFlowLayoutContainer() != null)
      {
        FlowLayoutContainer container = element.getCastorGuiElementChoice().getFlowLayoutContainer();
        Dimension position = new Dimension(container.getDimension());

        // fetch panes
        List children = new ArrayList();
        for (int i = 0; i < container.getGuiElementCount(); i++)
        {
          CastorGuiElement ui = container.getGuiElement(i);
          // Im Moment können nur Buttons im Container sein. Andere Elemente werden im Moment 
          // durch den Designer und Engine nicht unterstützt
          //
          CastorButton button = ui.getCastorGuiElementChoice().getButton();
          PluginComponent plugin = ui.getCastorGuiElementChoice().getPluginComponent();
          if (button == null && plugin == null)
            throw new Exception("FlowLayoutContainer can only handle ui elements of type button or plugin.");
          if (button != null)
          {
            String label = button.getLabel();
            Boolean _default = button.hasDefault() ? Boolean.valueOf(button.getDefault()) : null;
            ActionType actionType = getActionType(button.getAction(), definition);
            boolean emphasize = button.getEmphasize();
            Horizontal halign = button.getHalign() != null ? Alignment.fromJacob(button.getHalign()) : null;
            ButtonDefinition buttonDef = new ButtonDefinition(ui.getName(), ui.getDescription(), ui.getEventHandler(), label, ui.getVisible(), emphasize, _default, //
                ui.getTabIndex(), ui.getPaneIndex(), actionType, null, halign, element.getBorderWidth(), element.getBorderColor(), element.getBackgroundColor());
            buttonDef.putCastorProperties(ui.getProperty());
            children.add(buttonDef);
          }
          else
          {
            Dimension dimension = new Dimension(plugin.getDimension());
            String pluginClass = plugin.getPluginImplClass();
            String pluginId = plugin.getPluginId();
            String pluginVersion = plugin.getPluginVersion();
            boolean writeContainer = plugin.getWriteContainer();
            Properties properties = new Properties();
            for (int ii = 0; ii < plugin.getPropertyCount(); ii++)
            {
              CastorProperty cp = plugin.getProperty(ii);
              properties.put(cp.getName(), cp.getValue());
            }
            PluginComponentDefinition pluginDef = new PluginComponentDefinition(ui.getName(), ui.getEventHandler(), ui.getVisible(),ui.getTabIndex(), ui.getPaneIndex(), dimension, pluginId, pluginVersion, pluginClass, writeContainer, properties);
            children.add(pluginDef);
          }
         }
        return new JadFlowLayoutContainerDefinition(name, description, eventHandler, visible, position,container.getOrientation().getType()==FlowLayoutContainerOrientationType.HORIZONTAL_TYPE, children, element.getBorderWidth(), element.getBorderColor(), element.getBackgroundColor());
      }
      else if (element.getCastorGuiElementChoice().getTableListBox() != null)
      {
        TableListBox object = element.getCastorGuiElementChoice().getTableListBox();
        Dimension position = new Dimension(object.getDimension());
        Caption caption = getCaption(object.getCaption(), definition);
        
        
        IBrowserDefinition browserToUse = null;
        if(object.getBrowserToUse()!=null)
          browserToUse= definition.getBrowserDefinition(object.getBrowserToUse());
        ITableAlias alias = definition.getTableAlias(object.getTableAlias());
        ITableField field = alias.getTableDefinition().getTableField(object.getDisplayField());
        FontDefinition font=null;
        if(object.getFont()!=null)
        {
          CastorFont cfont=object.getFont();
          font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
        }
        return new TableListBoxDefinition(name, description, inputHint, eventHandler,  position,object.getDeleteModeUpdateNew(), object.getDeleteModeSelected(), visible,  tabIndex, paneIndex, caption,browserToUse, alias, field ,font, object.getResizeMode());
      }
      else if (element.getCastorGuiElementChoice().getLineChart() != null)
      {
        LineChart object = element.getCastorGuiElementChoice().getLineChart();
        Dimension position = new Dimension(object.getDimension());
        return new LineChartDefinition(name, description, eventHandler, visible, object.getTitle(), object.getBackground(), object.getLegendX(), object
            .getLegendY(), object.getGrid(), position);
      }
      else if (element.getCastorGuiElementChoice().getBarChart() != null)
      {
        BarChart object = element.getCastorGuiElementChoice().getBarChart();
        Dimension position = new Dimension(object.getDimension());
        return new BarChartDefinition(name, description, eventHandler, visible, object.getTitle(), object.getBackground(), object.getLegendX(), object
            .getLegendY(), object.getGrid(), position, element.getBorderWidth(), element.getBorderColor(), element.getBackgroundColor());
      }
      else if (element.getCastorGuiElementChoice().getOwnDrawElement() != null)
      {
        OwnDrawElement object = element.getCastorGuiElementChoice().getOwnDrawElement();
        Dimension position = new Dimension(object.getDimension());
        return new OwnDrawElementDefinition(name, description, eventHandler, visible, position);
      }
      else if (element.getCastorGuiElementChoice().getTimeLine() != null)
      {
        TimeLine object = element.getCastorGuiElementChoice().getTimeLine();
        Dimension position = new Dimension(object.getDimension());
        return new TimeLineDefinition(name, description, eventHandler, visible, position);
      }
      else if (element.getCastorGuiElementChoice().getCalendar() != null)
      {
        Calendar object = element.getCastorGuiElementChoice().getCalendar();
        Dimension position = new Dimension(object.getDimension());
        return new CalendarDefinition(name, description, eventHandler, visible, position);
      }
      else if (element.getCastorGuiElementChoice().getStaticImage() != null)
      {
        StaticImage object = element.getCastorGuiElementChoice().getStaticImage();
        Dimension position = new Dimension(object.getDimension());
        return new StaticImageDefinition(name, description, eventHandler, visible, position, object.getSrc(), object.getTooltip());
      }
      else if (element.getCastorGuiElementChoice().getDynamicImage() != null)
      {
        DynamicImage object = element.getCastorGuiElementChoice().getDynamicImage();
        Dimension position = new Dimension(object.getDimension());
        return new DynamicImageDefinition(name, description, eventHandler, visible, position, object.getTooltip());
      }
      else if (element.getCastorGuiElementChoice().getStyledText() != null)
      {
        StyledText object = element.getCastorGuiElementChoice().getStyledText();
        Dimension position = new Dimension(object.getDimension());
        return new StyledTextDefinition(name, description, eventHandler, visible,object.getPostProcessWithLetterEngine(), position, object.getI18nkey());
      }
      else if (element.getCastorGuiElementChoice().getBreadCrumb() != null)
      {
        BreadCrumb object = element.getCastorGuiElementChoice().getBreadCrumb();
        Caption caption = getCaption(object.getCaption(), definition);
        return new BreadCrumbDefinition(name, eventHandler, visible, tabIndex, paneIndex,object.getDelimiter(), caption, element.getBorderWidth(), element.getBorderColor(), element.getBackgroundColor());
      }
      else if (element.getCastorGuiElementChoice().getHeading() != null)
      {
        Heading object = element.getCastorGuiElementChoice().getHeading();
        Caption caption = getCaption(object.getCaption(), definition);
        return new HeadingDefinition(name, eventHandler, visible, tabIndex, paneIndex, caption);
      }
      else if (element.getCastorGuiElementChoice().getLabel() != null)
      {
        Label label = element.getCastorGuiElementChoice().getLabel();
        Caption caption = getCaption(label.getCaption(), definition);

        ITableField labelTableField = label.getTableField()==null?null:getTableAlias().getTableDefinition().getTableField(label.getTableField());;
        ITableAlias localTableAlias = labelTableField == null ? null : getTableAlias();
        return new LabelDefinition(name, eventHandler, visible, tabIndex, paneIndex, caption, localTableAlias, labelTableField, label.getNullDefaultValue(), element.getBorderWidth(), element.getBorderColor(), element.getBackgroundColor());
      }
      else if (element.getCastorGuiElementChoice().getPluginComponent() != null)
      {
        PluginComponent plugin = element.getCastorGuiElementChoice().getPluginComponent();

        Dimension dimension = new Dimension(plugin.getDimension());
        String pluginClass= plugin.getPluginImplClass();
        String pluginId= plugin.getPluginId();
        String pluginVersion= plugin.getPluginVersion();
        boolean writeContainer= plugin.getWriteContainer();
        Properties properties= new Properties();
        for(int i=0;i<plugin.getPropertyCount();i++)
        {
          CastorProperty cp = plugin.getProperty(i);
          properties.put(cp.getName(), cp.getValue());
        }
        return new PluginComponentDefinition(name, eventHandler, visible, tabIndex, paneIndex,dimension,pluginId, pluginVersion, pluginClass, writeContainer, properties);
      }
      else if (element.getCastorGuiElementChoice().getForeignInputField() != null)
      {
        ForeignInputField object = element.getCastorGuiElementChoice().getForeignInputField();
        Dimension dimension = new Dimension(object.getDimension());
        Caption caption = getCaption(object.getCaption(), definition);
        IBrowserDefinition browserToUse = definition.getBrowserDefinition(object.getBrowserToUse());
        IOneToManyRelation relationToUse = (IOneToManyRelation) definition.getRelation(object.getRelationToUse());
        IRelationSet relationSet = definition.getRelationSet(object.getRelationset());
        Filldirection fillDirection = Filldirection.fromJad(object.getFilldirection());
        ITableField foreignTableField = definition.getTableAlias(object.getForeignAlias()).getTableDefinition().getTableField(object.getForeignTableField());
        FontDefinition font=null;
        if(object.getFont()!=null)
        {
          CastorFont cfont=object.getFont();
          font = new FontDefinition(cfont.getFamily().toString(),cfont.getStyle().toString(), cfont.getWeight().toString(), cfont.getSize());
        }
        return new ForeignInputFieldDefinition(name, description, inputHint, eventHandler, dimension, visible, false,object.getAsComboBox(), tabIndex, paneIndex, caption, browserToUse, relationToUse,
            relationSet, fillDirection, foreignTableField,font);
      }
      else if (element.getCastorGuiElementChoice().getInFormBrowser() != null)
      {
        InFormBrowser object = element.getCastorGuiElementChoice().getInFormBrowser();
        Dimension dimension = new Dimension(object.getDimension());
        Caption caption = getCaption(object.getCaption(), definition);
        IBrowserDefinition browserToUse = definition.getBrowserDefinition(object.getBrowserToUse());
        boolean newMode = object.getNewMode();
        boolean updateMode = object.getUpdateMode();
        boolean deleteMode = object.getDeleteMode();
        boolean searchMode = object.getSearchMode();
        SelectionActionEventHandler[] selectionActionHandler = object.getSelectionActionEventHandler();        
        return new InFormBrowserDefinition(name, description, eventHandler, dimension, visible, tabIndex, paneIndex, caption, browserToUse, newMode,
            updateMode, deleteMode, searchMode,selectionActionHandler );
      }

      // should never occure
      throw new RuntimeException("Missing gui element in group: " + getName());
    }
    catch (Exception ex)
    {
      // create new exception to add info about element and group
      throw new Exception("Can not create GUI element '" + name + "' of group '" + getName() + "'", ex);
    }
  }
}
