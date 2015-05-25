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

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorButton;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ButtonDefinition extends GUIElementDefinition
{
  static public final transient String RCS_ID = "$Id: ButtonDefinition.java,v 1.7 2009/12/06 13:15:47 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  private final ActionType actionType;
  private final String label;
  private final boolean emphasize;
  private final Boolean _default;
  private final Alignment.Horizontal halign;

	/**
	 * @param name
	 * @param description
	 * @param eventHandler
	 * @param label
	 * @param visible
	 * @param emphasize
	 * @param _default
	 * @param tabIndex
	 * @param paneIndex
	 * @param actionType
	 * @param position
	 * @param halign
	 * @param borderWith
	 * @param borderColor
	 * @param backgroundColor
	 */
	public ButtonDefinition(String name, String description, String eventHandler, String label, boolean visible, boolean emphasize, Boolean _default, int tabIndex, int paneIndex, ActionType actionType, Dimension position,
      Alignment.Horizontal halign, int borderWith, String borderColor, String backgroundColor)
	{
		super(name, description, eventHandler, position, visible, tabIndex, paneIndex, null, borderWith, borderColor, backgroundColor);
		
    this.actionType = actionType;
    this.label = label;
    this.emphasize = emphasize;
    this._default = _default;
    this.halign = halign;
	}

	/**
	 * @return Returns the actionType.
	 */
	public final ActionType getActionType()
	{
		return actionType;
	}

	/**
	 * @return Returns the label.
	 */
	public final String getLabel()
	{
		return label;
	}

  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
   return factory.createButton(app, parent, this); 
  }
  
  protected void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    CastorButton jacobButton = new CastorButton();
    jacobButton.setLabel(getLabel());
    jacobButton.setEmphasize(this.emphasize);
    if (this._default != null)
      jacobButton.setDefault(this._default.booleanValue());
    jacobButton.setDimension(getDimension().toJacob());
    if (this.halign != null)
      jacobButton.setHalign(halign.toJacob());
    jacobButton.setAction(this.actionType.toJacob());
    jacobGuiElement.getCastorGuiElementChoice().setButton(jacobButton);
  }

  public boolean isEmphasize()
  {
    return emphasize;
  }

  /**
   * Determines whether the default flag has been set for this button.
   * 
   * @return <code>true</code> default flag has been set, i.e. calling
   *         {@link #hasDefault()} is applicable.<code>false</code> default flag
   *         is determined dynamically by means of application state, i.e.
   *         calling {@link #hasDefault()} is not applicable
   * @see #isDefault()
   * @since 2.9
   */
  public boolean hasDefault()
  {
    return this._default != null;
  }

  /**
   * Determines whether this button is always the default button or is never the
   * default button.
   * 
   * @return <code>true</code> button is always the default button,
   *         <code>false</code> button is never the default button.
   * @throws IllegalStateException
   *           default flag has not been set during application design and is
   *           application state dependent.
   * @see #hasDefault()
   * @since 2.9
   */
  public boolean isDefault() throws IllegalStateException
  {
    if (this._default == null)
      new IllegalStateException("Default flag has not been specified for this button");
    return _default.booleanValue();
  }

  /**
   * @return Returns the halign.
   */
  public Alignment.Horizontal getHorizontalAlign()
  {
    return halign;
  }
  
}
