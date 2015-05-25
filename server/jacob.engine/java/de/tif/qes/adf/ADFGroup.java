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

package de.tif.qes.adf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.actiontypes.ActionTypeChangeUpdateRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearGroup;
import de.tif.jacob.core.definition.actiontypes.ActionTypeDeleteRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeGeneric;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNavigateToForm;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeRecordSelected;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.core.definition.actiontypes.ActionTypeUpdateRecord;
import de.tif.jacob.core.definition.fieldtypes.DateFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimeFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimestampFieldType;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.CheckBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.ComboBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DateInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.GUIElementDefinition;
import de.tif.jacob.core.definition.guielements.InFormLongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LabelDefinition;
import de.tif.jacob.core.definition.guielements.LongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LongTextInputMode;
import de.tif.jacob.core.definition.guielements.ResizeMode;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TimeInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TimestampInputFieldDefinition;
import de.tif.jacob.core.definition.impl.AbstractGroupDefinition;
import de.tif.jacob.core.definition.impl.ContextMenuEntryDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.qes.IQeScriptContainer;
import de.tif.qes.QesLayoutAdjustment;
import de.tif.qes.adf.castor.ADL;
import de.tif.qes.adf.castor.Button;
import de.tif.qes.adf.castor.CheckBox;
import de.tif.qes.adf.castor.Combo;
import de.tif.qes.adf.castor.DateTime;
import de.tif.qes.adf.castor.Foreign;
import de.tif.qes.adf.castor.Group;
import de.tif.qes.adf.castor.IFB;
import de.tif.qes.adf.castor.Label;
import de.tif.qes.adf.castor.LongText;
import de.tif.qes.adf.castor.Masked;
import de.tif.qes.adf.castor.MenuItem;
import de.tif.qes.adf.castor.ObjectsItem;
import de.tif.qes.adf.castor.Text;
import de.tif.qes.adl.element.ADLDefinition;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class ADFGroup extends AbstractGroupDefinition implements IQeScriptContainer
{
	private final List browsers;
  private final Set scripts;

	/**
	 *  
	 */
	protected ADFGroup(Group group, ADLDefinition definition, ADFForm parent, QesLayoutAdjustment layout) throws Exception
	{
	  // use default event handler class name
    super(group.getName(), group.getCaption().getValue(), null, true,false, null, definition.getTableAlias(group.getAlias()), definition.getBrowserDefinition(group.getActiveBrowser()), new ADFDimension(layout.adjustGroup(group.getPosition())),-1,null,null);

    // fetch context menu entries
    for (int i = 0; i < group.getMenus().getMenuItemCount(); i++)
    {
      ContextMenuEntryDefinition entry = createContextMenuEntry(group.getMenus().getMenuItem(i), definition);
      addContextMenuEntry(entry);
    }

		// fetch browsers
		List browsers = new ArrayList();
		for (int i = 0; i < group.getBrowsers().getBrowserCount(); i++)
		{
			IBrowserDefinition browser = definition.getBrowserDefinition(group.getBrowsers().getBrowser(i).getName());
			browsers.add(browser);
		}
		this.browsers = Collections.unmodifiableList(browsers);

		// fetch gui elements
		for (int i = 0; i < group.getObjects().getObjectsItemCount(); i++)
		{
			GUIElementDefinition element = createGUIElement(group.getObjects().getObjectsItem(i), definition, parent, layout);
			if (null != element)
				addGuiElement(element);
		}
    
    // fetch scripts
    this.scripts = ADFScript.fetchScripts(group.getScripts());
  }
	

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractGroupDefinition#ignoreErrors()
   */
  protected boolean ignoreInvalidEnums()
  {
    // QDesigner does not properly performer enum checking -> ignore, i.e. release a warning only
    return true;
  }
  
	private static ITableField getTableField(ADL adl, ADLDefinition adlDefinition) throws NoSuchFieldException
	{
		if (null == adl || null == adl.getTable())
			return null;
		return adlDefinition.getTableAlias(adl.getTable()).getTableDefinition().getTableField(adl.getField());
	}

	private static ITableAlias getTableAlias(ADL adl, ADLDefinition adlDefinition)
	{
		if (null == adl || null == adl.getTable())
			return null;
		return adlDefinition.getTableAlias(adl.getTable());
	}

	private static boolean getBoolean(String booleanString)
	{
		return booleanString != null && booleanString.equalsIgnoreCase("true");
	}

	private static Filldirection getFillDirection(String filldirectionString)
	{
		if ("Backward".equalsIgnoreCase(filldirectionString))
			return Filldirection.BACKWARD;
		if ("Both".equalsIgnoreCase(filldirectionString))
			return Filldirection.BOTH;
		if ("Forward".equalsIgnoreCase(filldirectionString))
			return Filldirection.FORWARD;

		return Filldirection.BACKWARD;
	}

  private static LongTextInputMode getLongTextMode(String longTextMode)
  {
    if ("LT_EDIT".equals(longTextMode))
    {
      return LongTextInputMode.FULLEDIT;
    }
    else if ("LT_READONLY".equals(longTextMode))
    {
      return LongTextInputMode.READONLY;
    }
    else if ("LT_APPEND".equals(longTextMode))
    {
      return LongTextInputMode.APPEND;
    }
    else if ("LT_PREPEND".equals(longTextMode))
    {
      return LongTextInputMode.PREPEND;
    }
    throw new RuntimeException("Unknown long text mode: "+longTextMode);
    
  }
  
  private static ActionType getActionType(String actionEmitterName, String clazz, ADL adl, ADLDefinition adlDefinition)
  {
		if (clazz == null)
			return new ActionTypeGeneric();

		if (clazz.equals("QW_NONE"))
			return new ActionTypeGeneric();
		else if (clazz.equals("QW_LOCAL"))
		  // IBIS: �berpr�fen: neuer Actiontype bei OSMQ!
			return new ActionTypeGeneric();
		else if (clazz.equals("QW_NEW"))
			return new ActionTypeNewRecord();
		else if (clazz.equals("QW_SEARCH"))
		{
			String relationSetName = null;
			String filldirectionString = null;
			if (null != adl)
			{
				relationSetName = adl.getRelationSet();
				filldirectionString = adl.getFillDirection();
			}
			
			// Generate warnings if relationset and/or filldirection are missing
			if (relationSetName == null)
			{
				System.err.println("### Warning: Search-"+actionEmitterName+" has no relationset defined (using default relationset)!");
			}
			if (filldirectionString == null)
			{
				System.err.println("### Warning: Search-"+actionEmitterName+" has no filldirection defined (using BACKWARD)!");
			}
			
      // IBIS: Namenskonvention erstellen, um Mode zu setzen
      boolean safeMode = false;
      return new ActionTypeSearch(adlDefinition.getRelationSet(relationSetName), getFillDirection(filldirectionString), safeMode);
		}
		else if (clazz.equals("QW_CLEAR"))
			return new ActionTypeClearGroup();
		else if (clazz.equals("QW_UPDATE"))
			return new ActionTypeUpdateRecord(ActionType.SCOPE_OUTERGROUP);
		else if (clazz.equals("QW_CHANGEUPDATE"))
			return new ActionTypeChangeUpdateRecord(ActionType.SCOPE_OUTERGROUP);
		else if (clazz.equals("QW_SELECTED"))
			return new ActionTypeRecordSelected();
		else if (clazz.equals("QW_DELETE"))
			return new ActionTypeDeleteRecord(true);
		else
			throw new RuntimeException("Unknown action clazz: " + clazz);
	}

	private static ContextMenuEntryDefinition createContextMenuEntry(MenuItem item, ADLDefinition adlDefinition)
	{
		String name = item.getName();
		String label = item.getCaption().getValue();
		ActionType actionType = getActionType("Contextmenu '" + name+"'", item.getClazz(), item.getADL(), adlDefinition);
    ADFScript script = null;
    if (item.getScriptName() != null && item.getFile() != null)
    {
      script = new ADFScript(item.getFile(), item.getScriptName(), "Click");
    }
		return new ADFContextMenuEntryDefinition(name, label, actionType, script);
	}
  
  private static Caption getCaption(de.tif.qes.adf.castor.Caption caption, QesLayoutAdjustment layout)
  {
    return getCaption(caption, null, layout);
  }
  
  private static Caption getCaption(de.tif.qes.adf.castor.Caption caption, ActionType actionType, QesLayoutAdjustment layout)
  {
		if (caption != null)
		{
			if (caption.getValue() != null)
			{
				return new ADFCaption(caption, actionType, Alignment.RIGHT, Alignment.TOP, layout);
			}
		}
		return null;
	}
  
	private GUIElementDefinition createGUIElement(ObjectsItem item, ADLDefinition adlDefinition, ADFForm parent, QesLayoutAdjustment layout) throws Exception
  {
    String name = "unknown";
    
    // use default eventhandler
    String eventHandler = null;
    
    String description = null;
    
    final int paneIndex = 0;
    
    try
    {
      if (item.getText() != null)
      {
        Text object = item.getText();
        name = object.getName();
        Dimension position = new ADFDimension(layout.adjustText(object.getPosition()));
        boolean visible = !getBoolean(object.getInvisible());
        if (layout.skipInvisible() && !visible)
          return null;
        boolean multiline = "QWOD_MULTILINE".equals(object.getDisplayType());
        int tabIndex = object.getTabIndex();
        Caption caption = getCaption(object.getCaption(), layout);
        ITableAlias localTableAlias = getTableAlias(object.getADL(), adlDefinition);
        ITableField localTableField = getTableField(object.getADL(), adlDefinition);
        return new TextInputFieldDefinition(name, description, null, eventHandler, position, visible, false, multiline, tabIndex, paneIndex, caption, localTableAlias, localTableField, null,null);
      }
      else if (item.getButton() != null)
      {
        Button object = item.getButton();
        name = object.getName();
        String label = object.getCaption().getValue();
        boolean visible = !getBoolean(object.getInvisible());
        if (layout.skipInvisible() && !visible)
          return null;
        int tabIndex = object.getTabIndex();
        ActionType actionType = getActionType("Button '" + name + "'", object.getClazz(), object.getADL(), adlDefinition);
        Dimension position = new ADFDimension(layout.adjustButton(object.getPosition()));
        return new ADFButtonDefinition(name, label, visible, tabIndex, actionType, position, object.getEvents());
      }
      else if (item.getLabel() != null)
      {
        Label object = item.getLabel();
        name = object.getName();
        boolean visible = !getBoolean(object.getInvisible());
        if (layout.skipInvisible() && !visible)
          return null;
        int tabIndex = object.getTabIndex();
        if (object.getCaption().getValue() == null)
        {
          // QDesigner: A caption is not a required field for labels
  				System.err.println("### Warning: Caption is missing for label '" + name + "' of group '" + getName() + "'!");
          object.getCaption().setValue("<unset>");
        }
        Caption caption = new ADFCaption(object.getCaption(), object.getPosition(), Alignment.RIGHT, Alignment.TOP, layout);
        return new LabelDefinition(name, eventHandler, visible, tabIndex, paneIndex, caption, null,null,null,-1,null,null);
      }
      else if (item.getLongText() != null)
      {
        LongText object = item.getLongText();
        name = object.getName();
        Dimension position = new ADFDimension(layout.adjustLongText(object.getPosition()));
        boolean visible = !getBoolean(object.getInvisible());
        if (layout.skipInvisible() && !visible)
          return null;
        int tabIndex = object.getTabIndex();
        Caption caption = getCaption(object.getCaption(), layout);
        ITableAlias localTableAlias = getTableAlias(object.getADL(), adlDefinition);
        ITableField localTableField = getTableField(object.getADL(), adlDefinition);
        LongTextInputMode mode = getLongTextMode(object.getLongTextMode());

        // Hack: Falls jemand das Feld gr��er als einen 'normalen' Button gezogen hat, wird das
        // Element als 'InForm' Elemetn dargestellt.
        if (position.getWidth() > 80)
          return new InFormLongTextInputFieldDefinition(name, description, null, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField, mode, false,null,null);

        return new LongTextInputFieldDefinition(name, description, null, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField, mode, false,null);
      }
      else if (item.getCheckBox() != null)
      {
        CheckBox object = item.getCheckBox();
        name = object.getName();
        Dimension position = new ADFDimension(layout.adjustCheckBox(object.getPosition()));
        boolean visible = !getBoolean(object.getInvisible());
        if (layout.skipInvisible() && !visible)
          return null;
        int tabIndex = object.getTabIndex();
        Caption caption = getCaption(object.getCaption(), layout);
        ITableAlias localTableAlias = getTableAlias(object.getADL(), adlDefinition);
        ITableField localTableField = getTableField(object.getADL(), adlDefinition);
        return new CheckBoxInputFieldDefinition(name, description, null, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField);
      }
      else if (item.getCombo() != null)
      {
        Combo object = item.getCombo();
        name = object.getName();
        Dimension position = new ADFDimension(layout.adjustComboBox(object.getPosition()));
        boolean visible = !getBoolean(object.getInvisible());
        if (layout.skipInvisible() && !visible)
          return null;
        int tabIndex = object.getTabIndex();
        Caption caption = getCaption(object.getCaption(), layout);
        ITableAlias localTableAlias = getTableAlias(object.getADL(), adlDefinition);
        ITableField localTableField = getTableField(object.getADL(), adlDefinition);
        String[] values;
        if (object.getItems() == null)
        {
  				System.err.println("### Error: Combobox '" + name + "' of group '" + getName() + "' has no items (enum values) defined!");
          values = new String[0];
        }
        else
        {
          values = new String[object.getItems().getItemCount()];
          for (int index = 0; index < object.getItems().getItemCount(); index++)
            values[index] = object.getItems().getItem(index).getValue();
        }
        return new ComboBoxInputFieldDefinition(name, description, null, eventHandler, values, true, false, true,true, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField, null);
      }
      else if (item.getForeign() != null)
      {
        Foreign object = item.getForeign();
        name = object.getName();
        Dimension dimension = new ADFDimension(layout.adjustForeign(object.getPosition()));
        boolean visible = !getBoolean(object.getInvisible());
        int tabIndex = object.getTabIndex();

        // create NavigateToForm action, if necessary
        ActionType actionType = null;
        String targetForm = object.getTargetForm();
        if (targetForm != null && !parent.getName().equals(targetForm))
        {
          actionType = new ActionTypeNavigateToForm(targetForm);
        }

        Caption caption = getCaption(object.getCaption(), actionType, layout);
        IBrowserDefinition browserToUse = adlDefinition.getBrowserDefinition(object.getADL().getBrowser());
        IOneToManyRelation relationToUse = (IOneToManyRelation) adlDefinition.getRelation(object.getADL().getRelation());
        IRelationSet relationSet = adlDefinition.getRelationSet(object.getADL().getRelationSet());
        Filldirection fillDirection = getFillDirection(object.getADL().getFillDirection());
        ITableField foreignTableField = getTableField(object.getADL(), adlDefinition);
        return new ADFForeignInputFieldDefinition(name, dimension, visible, tabIndex, caption, browserToUse, relationToUse, relationSet, fillDirection, foreignTableField, object.getEvents());
      }
      else if (item.getIFB() != null)
      {
        IFB object = item.getIFB();
        name = object.getName();
        Dimension dimension = new ADFDimension(layout.adjustIFB(object.getPosition()));
        boolean visible = !getBoolean(object.getInvisible());
        int tabIndex = object.getTabIndex();
        Caption caption = getCaption(object.getCaption(), layout);
        IBrowserDefinition browserToUse = adlDefinition.getBrowserDefinition(object.getADL().getBrowser());
        IRelation relationToUse = adlDefinition.getRelation(object.getADL().getRelation());
        boolean newMode = object.getIfbModes() != null ? getBoolean(object.getIfbModes().getNew()) : false;
        boolean updateMode = object.getIfbModes() != null ? getBoolean(object.getIfbModes().getUpdate()) : false;
        boolean deleteMode = object.getIfbModes() != null ? getBoolean(object.getIfbModes().getDelete()) : false;
        boolean searchMode = true;
        return new ADFInFormBrowserDefinition(name, dimension, visible, tabIndex, caption, browserToUse, relationToUse, newMode, updateMode, deleteMode, searchMode, object.getEvents());
      }
      else if (item.getDateTime() != null)
      {
        DateTime object = item.getDateTime();
        name = object.getName();
        Dimension position = new ADFDimension(layout.adjustDateTime(object.getPosition()));
        boolean visible = !getBoolean(object.getInvisible());
        if (layout.skipInvisible() && !visible)
          return null;
        int tabIndex = object.getTabIndex();
        Caption caption = getCaption(object.getCaption(), layout);
        ITableAlias localTableAlias = getTableAlias(object.getADL(), adlDefinition);
        ITableField localTableField = getTableField(object.getADL(), adlDefinition);
        if (localTableField.getType() instanceof TimestampFieldType)
          return new TimestampInputFieldDefinition(name, description, null, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField,null);
        else if (localTableField.getType() instanceof DateFieldType)
          return new DateInputFieldDefinition(name, description, null, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField,null);
        else if (localTableField.getType() instanceof TimeFieldType)
          return new TimeInputFieldDefinition(name, description, null, eventHandler, position, visible, false, tabIndex, paneIndex, caption, localTableAlias, localTableField,null);
      }
      else if (item.getMasked() != null)
      {
        // IBIS: Maske f�r TextInputFieldDefinition fehlt
        Masked object = item.getMasked();
        name = object.getName();
        Dimension position = new ADFDimension(layout.adjustText(object.getPosition()));
        boolean visible = !getBoolean(object.getInvisible());
        if (layout.skipInvisible() && !visible)
          return null;
        int tabIndex = object.getTabIndex();
        Caption caption = getCaption(object.getCaption(), layout);
        ITableAlias localTableAlias = getTableAlias(object.getADL(), adlDefinition);
        ITableField localTableField = getTableField(object.getADL(), adlDefinition);
        return new TextInputFieldDefinition(name, description, null, eventHandler, position, visible, false, false, tabIndex, paneIndex, caption, localTableAlias, localTableField,null, null);
      }
      else if (item.getMtoN() != null)
      {
        // IBIS: MtoN missing!
        return null;
      }
      else if (item.getQControl() != null)
      {
        // IBIS: QControl missing!
        return null;
      }
    }
    catch (Exception ex)
    {
      throw new Exception("Error creating GUI element '" + name + "' of group '" + getName() + "'", ex);
    }

    throw new RuntimeException("Unknown or unsupported object item");
  }

  /**
   * @return Returns the scripts.
   */
  public Set getScripts()
  {
    return scripts;
  }
  


  /**
   * @since 2.10.0
   */
	public ResizeMode getResizeMode()
  {
    return ResizeMode.NONE;
  }


  /* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractGroupDefinition#toJacob(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup, de.tif.jacob.core.definition.impl.ConvertToJacobOptions)
	 */
	public void toJacob(CastorGroup jacobGroup, ConvertToJacobOptions options)
	{
		super.toJacob(jacobGroup, options);
    
    ADFScript.putScriptsToProperties(getScripts(), options, this);
    jacobGroup.setProperty(getCastorProperties());
  }

}
