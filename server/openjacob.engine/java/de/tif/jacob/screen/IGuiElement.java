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

package de.tif.jacob.screen;

import java.awt.Color;
import java.util.List;
import java.util.Locale;

import de.tif.jacob.core.definition.IApplicationDefinition;


/**
 * Base interface which is implemented by all jACOB GUI elements.
 * 
 * @author Andreas Herz
 */
public interface IGuiElement
{
	/**
	 * The internal revision control system id.
	 */
  static public final String RCS_ID = "$Id: IGuiElement.java,v 1.12 2010/10/13 14:21:23 freegroup Exp $";
  
	/**
	 * The internal revision control system id in short form.
	 */
  static public final String RCS_REV = "$Revision: 1.12 $";

  /**
   * Class to reflect the (data) status of a GUI group.
   * 
   * @see IGuiElement#getDataStatus()
   * 
   * @author Andreas Herz
   */
  static class GroupState
  {
    private final String label;

    private GroupState(String label)
    {
      this.label = label;
    }

    /**
     * Returns the string representation of this group state.
     * 
     * @return the string representation
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return label;
    }
  }; 
  
  /**
   * Internal dummy group state.
   */
  public final static GroupState UNDEFINED = new GroupState("undefined");
  
  /**
   * The group state for the update mode, i.e. the data record displayed within the group is going to be updated by an user. 
   * 
   * @see IClientContext#getSelectedRecord()
   * @see de.tif.jacob.core.data.IDataTableRecord#isUpdated()
   */
  public final static GroupState UPDATE = new GroupState("update");
  
  /**
   * The group state for the new mode, i.e. the data record displayed within the group is a new record.
   * 
   * @see IClientContext#getSelectedRecord()
   * @see de.tif.jacob.core.data.IDataTableRecord#isNew() 
   */
  public final static GroupState NEW = new GroupState("new");
  
  /**
   * The group state for the search mode, i.e. no underlying data record exists.
   * Nevertheless, data within the group's input field is used as QBE search
   * constraints.
   */
  public final static GroupState SEARCH = new GroupState("search");
  
  /**
   * The group state for the selected mode, i.e. a data record has been selected
   * for being displayed within the group.
   * 
   * @see IClientContext#getSelectedRecord()
   */
  public final static GroupState SELECTED = new GroupState("selected");
 

  /**
   * Checks whether this GUI element is visible.
   * 
   * @return <code>true</code> if visible, otherwise <code>false</code>
   */
  public boolean isVisible();
  
  /**
   * Sets the visibility of this GUI element.
   * 
   * @param visible
   *          <code>true</code> to make the GUI element visible,
   *          <code>false</code> to make the GUI element invisible
   */
  public void setVisible(boolean visible);
  
	/**
   * Returns the name of this GUI element.
   * <p>
   * Note: GUI element names must not necessarily be unique within an
   * application.
   * 
   * @return the name of the GUI element
   */
  public String getName();

  /**
   * Returns the label of this GUI element. If the label starts with a '%', it
   * is treated as a label key to the application's internationalization
   * resource bundle.
   * 
   * @return the label of the GUI element or <code>null</code>, if the GUI
   *         element has no label
   * 
   * @see #getI18NLabel(Locale)
   */
  public String getLabel();

  /**
   * Returns the internationalized label of this GUI element.
   * 
   * @param locale
   *          the current client context
   * @return the internationalized label or <code>null</code>, if the GUI
   *         element has no label
   */
  public String getI18NLabel(Locale locale);
  
  /**
   * Sets the label of this GUI element.
   * 
   * @param label
   *          the new label
   */
  public void setLabel(String label);
  
  /**
   * Sets the background color of the GUI element. <br>
   * Use <b>null </b> to reset to the default value. <br>
   * 
   * @param color
   *          The new background color of the element or <code>null</code>
   */
  public void setBackgroundColor(Color color);
  
  /**
   * Sets the foreground color of the GUI element. <br>
   * Use <b>null </b> to reset to the default value. <br>
   * 
   * @param color
   *          The new color of the element or <code>null</code>
   */
  public void setColor(Color color);
  
  /**
   * Returns the parent of this GUI element.
   * 
   * @return The parent or <code>null</code> if no parent exists, which should
   *         be the case for top level GUI elements only.
   */
  public IGuiElement getParent();
  
	/**
   * Returns all child GUI elements of this GUI element.
   * 
   * @return <code>List</code> of {@link IGuiElement}. An empty list will be
   *         returned, if no child GUI elements exist.
   */
  public List getChildren();
	
  /**
   * Traverse the GUI element tree and looks for a GUI element with the given
   * name.
   * <p>
   * 
   * Note: This method looks up for a matching child GUI element first. If this
   * is not successful, this method will be invoked on each child GUI element
   * until success. 
   * 
   * @param name
   *          the name of the GUI element
   * @return the desired GUI element or <code>null</code> if no GUI element of
   *         the given name exists.
   */
  public IGuiElement findByName(String name);

  /**
   * Checks whether this GUI element is enabled.
   * 
   * @return <code>true</code> if enabled, <code>false</code> if disabled.
	 */
	public boolean isEnabled();
	
  /**
   * Enables or disables this GUI element.
   * 
   * @param isEnable
   *          <code>true</code> to enable,
   *          <code>false</code> to disable
   */
	public void setEnable(boolean isEnable);

  /**
   * Returns the data status of this GUI element. The data status is equivalent
   * to the status of the group this GUI element belongs to.
   * 
   * @return the data status or {@link #UNDEFINED} if not applicable.
   */
  public GroupState getDataStatus();
  
  /**
   * Returns the definition of the application this GUI element belongs to.
   *  
   * @return the application definition.
   */
  public IApplicationDefinition getApplicationDefinition();
  
  /**
   * Returns the name of the group table alias. This can be the alias of 
   * a TabPane (if the UI is a child of a pane) or the real outer group alias.
   * 
   * @return the group table alias name or <code>null</code>, if this GUI
   *         element does not belong to a group.
   */
  public String getGroupTableAlias();
  
  /**
   * Returns the parent IGroup element of this UI element. This can be an ITabPane if
   * the element inside a ITabPane group.
   * 
   * @see #getOuterGroup
   * @since 2.8.2
   * @return the parent group of this UI element
   */
  public IGroup getGroup();

  /**
   * Returns the outer IGroup element of this UI element. This is always the
   * top most group. It is never an ITabPane-Group if the UI element is a child 
   * of a ITabPane. 
   * 
   * @see #getGroup
   * @since 2.8.2
   * @return the top most group (parent) of this UI element 
   */
  public IGroup getOuterGroup();


  /**
   * Return the parent Form of this UI Element
   * @since 2.8.5
   */
  public IForm getForm();
  
  /**
   * Checks whether the given GUI element is contained within this GUI element,
   * i.e. the given GUI element is either a (direct) child of this GUI element
   * or a child of a child of this GUI element and so further.
   * 
   * @param element
   *          the GUI element to check for containment
   * @return <code>true</code>, if the given GUI element is contained within
   *         this GUI element, otherwise <code>true</code>.
   */
  public boolean hasChild(IGuiElement element);
  
  /**
   * Returns the full path name of this GUI element.<p>
   * Example:<br>
   * <code>jcrm.f_incident.incidenForm.incidentGroup.myButton</code>
   * 
   * @return the full path name
   */
  public String getPathName();
  
  /**
   * The session unique id of this object
   * 
   * @return the sesssion unique id of this element.
   */
  public int getId();


  /**
   * Requests that this Component get the input focus. This component must be displayable, 
   * visible, and focusable for the request to be granted. Every effort will be 
   * made to honor the request; however, in some cases it may be impossible to 
   * do so. If this request is denied because this Component's top-level Form 
   * is not the current visislbe Form, the request will be remembered and will be 
   * granted when the Form is later displayed.
   * 
   */
  public void requestFocus();

  /**
   * Marks this GUI element with an error decoration.
   * 
   * @param context
   *          the current client context
   * @param message
   *          the message to add to the error decoration
   * @since 2.7.2
   */
  public void setErrorDecoration(IClientContext context, String message);

  /**
   * Remove an error decoration from this GUI element (if any).
   * 
   * @param context
   *          the current client context
   * @since 2.7.2
   */
  public void resetErrorDecoration(IClientContext context);
}
