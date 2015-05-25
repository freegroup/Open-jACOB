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

package de.tif.jacob.core.definition.impl;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IGUIElementDefinition;
import de.tif.jacob.core.definition.IJacobGroupDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
import de.tif.jacob.core.definition.guielements.ComboBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.GUIElementDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractGroupDefinition extends AbstractGuiElement implements IJacobGroupDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractGroupDefinition.java,v 1.7 2009/07/27 15:06:11 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.7 $";
  
  private final String label;
  private final Dimension dimension;
  private final boolean hasBorder;
  private final int borderWidth;
  private Color borderColor;
  private Color backgroundColor;
  
  private final List guiElements;
  private final List contextMenuEntries;
  private final List selectionActions;
  private final List unmodifiableGuiElements;
  private final List unmodifiableContextMenuEntries;
  private final List unmodifiableSelectionActions;
  
  private final ITableAlias tableAlias;
  private final IBrowserDefinition activeBrowserDefinition;
  private final boolean hideEmptyBrowser;
  
  

  /**
	 * @param name
	 * @param label
	 * @param description
	 * @param hasBorder
	 * @param eventHandler
	 * @param tableAlias
	 * @param activeBrowserDefinition
	 * @param dimension
	 */
	public AbstractGroupDefinition(String name, String label, String description, boolean hasBorder, boolean hideEmptyBrowser, String eventHandler, ITableAlias tableAlias,
      IBrowserDefinition activeBrowserDefinition, Dimension dimension, int borderWidth,  String borderColor, String backgroundColor)
	{
		super(name, description, eventHandler);
    this.label = label;
    this.tableAlias = tableAlias;
    this.activeBrowserDefinition = activeBrowserDefinition; 
    this.dimension = dimension;
    this.guiElements = new ArrayList();
    this.unmodifiableGuiElements = Collections.unmodifiableList(this.guiElements);
    this.contextMenuEntries = new ArrayList();
    this.unmodifiableContextMenuEntries = Collections.unmodifiableList(this.contextMenuEntries);
    this.hasBorder = hasBorder;
    this.hideEmptyBrowser=hideEmptyBrowser;
    this.selectionActions = new ArrayList();
    this.unmodifiableSelectionActions = Collections.unmodifiableList(this.selectionActions);
    this.borderWidth = borderWidth;
    try
    {
      this.backgroundColor = new javax.swing.text.html.StyleSheet().stringToColor(backgroundColor);
    }
    catch(Exception exc)
    {
      this.backgroundColor = null;
    }
    
    try
    {
      this.borderColor = new javax.swing.text.html.StyleSheet().stringToColor(borderColor);
    }
    catch(Exception exc)
    {
      this.borderColor = null;
    }
  }
  
  protected final void addGuiElement(GUIElementDefinition guielement)
  {
    this.guiElements.add(guielement);
  }
  
  protected final void addContextMenuEntry(ContextMenuEntryDefinition entry)
  {
    this.contextMenuEntries.add(entry);
  }

  protected final void addSelectionAction(AbstractSelectionActionDefinition action)
  {
    this.selectionActions.add(action);
  }


  public List getSelectionActions()
  {
    return this.unmodifiableSelectionActions;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGroupDefinition#getLabel()
   */
  public final String getLabel()
  {
    return this.label;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGroupDefinition#getTableAlias()
   */
  public final ITableAlias getTableAlias()
  {
    return this.tableAlias;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGroupDefinition#getActiveBrowserDefinition()
   */
  public final IBrowserDefinition getActiveBrowserDefinition()
  {
    return this.activeBrowserDefinition;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGroupDefinition#getContextMenuEntries()
   */
  public final List getContextMenuEntries()
  {
    return this.unmodifiableContextMenuEntries;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGroupDefinition#getGUIElementDefinitions()
   */
  public final List getGUIElementDefinitions()
  {
    return this.unmodifiableGuiElements;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGUIElementDefinition#getDimension()
   */
  public final Dimension getDimension()
  {
    return this.dimension;
  }

  public final boolean hasBorder()
  {
    return hasBorder;
  }

  public boolean hideEmptyBrowser()
  {
    return hideEmptyBrowser;
  }

  
  public Color getBackgroundColor()
  {
    return backgroundColor;
  }

  public Color getBorderColor()
  {
    return borderColor;
  }

  public int getBorderWidth()
  {
    return borderWidth;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGUIElementDefinition#getRectangle()
   */
  public final Rectangle getRectangle()
  {
    return new Rectangle(this.dimension.getLeft(), this.dimension.getTop(), this.dimension.getWidth(), this.dimension.getHeight());
  }
  
  public void toJacob(CastorGroup jacobGroup, ConvertToJacobOptions options)
  {
    jacobGroup.setName(getName());
    jacobGroup.setLabel(getLabel());
    jacobGroup.setDescription(getDescription());
    jacobGroup.setEventHandler(getEventHandler());
    jacobGroup.setAlias(getTableAlias().getName());
    jacobGroup.setBrowser(getActiveBrowserDefinition().getName());    
    jacobGroup.setDimension(dimension.toJacob());
    jacobGroup.setBorder(hasBorder());
    jacobGroup.setHideEmptyBrowser(hideEmptyBrowser());
    
    // fetch gui elements
    for (int i=0; i < this.guiElements.size(); i++)
    {
      GUIElementDefinition element = (GUIElementDefinition) this.guiElements.get(i);
      jacobGroup.addGuiElement(element.toJacob(options));
    }
    
    // fetch context menu
    for (int i=0; i < this.contextMenuEntries.size(); i++)
    {
      ContextMenuEntry jacobContextMenuEntry = new ContextMenuEntry();
      ContextMenuEntryDefinition menuEntry = (ContextMenuEntryDefinition) this.contextMenuEntries.get(i);
      menuEntry.toJacob(jacobContextMenuEntry, options);
      jacobGroup.addContextMenuEntry(jacobContextMenuEntry);
    }
    
    // fetch selection actions
    for (int i=0; i < this.selectionActions.size(); i++)
    {
      AbstractSelectionActionDefinition element = (AbstractSelectionActionDefinition) this.selectionActions.get(i);
      jacobGroup.addSelectionActionEventHandler(element.toJacob());
    }
    
    // handle properties
    jacobGroup.setProperty(getCastorProperties());
  }
  
  protected boolean ignoreInvalidEnums()
  {
    return false;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    // perform plausibility checks
    //
    List guiElements = getGUIElementDefinitions();
    for (int i = 0; i < guiElements.size(); i++)
    {
      IGUIElementDefinition guiElement = (IGUIElementDefinition) guiElements.get(i);

      // check combobox concerning illegal enumeration values.
      // Do not trust QDesigner and jACOB designer :-)
      //
      if (guiElement instanceof ComboBoxInputFieldDefinition)
      {
        ComboBoxInputFieldDefinition combobox = (ComboBoxInputFieldDefinition) guiElement;
        ITableField tableField = combobox.getLocalTableField();
        if (tableField != null && tableField.getType() instanceof EnumerationFieldType)
        {
          EnumerationFieldType enumerationFieldType = (EnumerationFieldType) tableField.getType();
          for (int j = 0; j < combobox.getEnumCount(); j++)
          {
            String value = combobox.getEnumEntry(j);
            if (!enumerationFieldType.containsEnumeratedValue(value))
            {
              String errorMessage = "Combobox '" + combobox.getName() + "' contains illegal enum value '" + value + "' which is not a member of table field '"
                  + tableField + "'";
              if (ignoreInvalidEnums())
              {
                System.err.println("### Error: " + errorMessage);
              }
              else
              {
                throw new Exception(errorMessage);
              }
            }
          }
        }
      }
    }
  }
}
