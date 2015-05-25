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

package de.tif.jacob.core.definition.guielements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.definition.IToolbarDefinition;
import de.tif.jacob.core.definition.actiontypes.ActionTypeAbout;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearApplication;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearDomain;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearForm;
import de.tif.jacob.core.definition.actiontypes.ActionTypeCreateReport;
import de.tif.jacob.core.definition.actiontypes.ActionTypeExit;
import de.tif.jacob.core.definition.actiontypes.ActionTypeMessaging;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewWindow;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSetUserPassword;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowAlert;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowReports;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowSQL;
import de.tif.jacob.core.definition.actiontypes.ActionTypeThemeSelect;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;

/**
 * @author Andreas Sonntag
 *
 */
public class ToolbarDefinition implements IToolbarDefinition
{
  static public final transient String RCS_ID = "$Id: ToolbarDefinition.java,v 1.5 2009/11/19 17:56:10 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  private final List buttons;
  private final List unmodifiableButtons;
  
  /**
	 * @param name
	 * @param caption
	 * @param top
	 * @param left
	 * @param width
	 * @param height
	 */
	public ToolbarDefinition()
	{
		this.buttons = new ArrayList();
		this.unmodifiableButtons = Collections.unmodifiableList(this.buttons);

    addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_EXIT.getName(),         "%BUTTON_TOOLBAR_CLOSE", true, new ActionTypeExit()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_CLEARALL.getName(),     "%BUTTON_TOOLBAR_CLEAR_ALL", true, new ActionTypeClearApplication()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_CLEARFOCUS.getName(),   "%BUTTON_TOOLBAR_CLEAR_DOMAIN", true, new ActionTypeClearDomain()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_CLEARFORM.getName(),    "%BUTTON_TOOLBAR_CLEAR_FORM", true, new ActionTypeClearForm()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_CREATEREPORT.getName(), "%BUTTON_TOOLBAR_CREATE_REPORT", true, new ActionTypeCreateReport()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_SHOWREPORTS.getName(),  "%BUTTON_TOOLBAR_SHOW_REPORTS", true, new ActionTypeShowReports()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_THEMESELECT.getName(),  "%BUTTON_TOOLBAR_THEME_SELECT", true, new ActionTypeThemeSelect()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_SHOWSQL.getName(),      "%BUTTON_TOOLBAR_SHOW_SQL", true, new ActionTypeShowSQL()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_ALERT.getName(),        "%BUTTON_TOOLBAR_ALERT", true, new ActionTypeShowAlert()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_MESSAGING.getName(),    "%BUTTON_TOOLBAR_MESSAGING", true, new ActionTypeMessaging()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_USERPASSWORD.getName(), "%BUTTON_TOOLBAR_SET_USER_PASSWORD", true, new ActionTypeSetUserPassword()));
		addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_NEWWINDOW.getName(),    "%BUTTON_TOOLBAR_NEW_WINDOW", true, new ActionTypeNewWindow()));
    addButton(new ToolbarButtonDefinition(Property.UI_TOOLBAR_ABOUT.getName(),        "%BUTTON_TOOLBAR_ABOUT", true, new ActionTypeAbout()));
	}

  protected void addButton(ToolbarButtonDefinition button)
  {
    this.buttons.add(button);
  }

  
  // IBIS: Frage:was muss hier getan werden?
  protected final void toJacob(CastorGuiElement jacobGuiElement)
  {
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IToolbarDefinition#getName()
	 */
	public String getName()
	{
		return "toolbar";
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IToolbarDefinition#getToolbarButtons()
	 */
	public List getToolbarButtons()
	{
		return this.unmodifiableButtons;
	}

}
