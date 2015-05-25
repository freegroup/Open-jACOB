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

import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.TableListBox;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TableListBoxDefinition extends InputFieldDefinition
{
	static public final transient String RCS_ID = "$Id: TableListBoxDefinition.java,v 1.6 2010/08/12 07:52:16 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.6 $";

  private final ITableAlias alias;
  private final ITableField field;
  IBrowserDefinition browserToUse;
  // Records can be remove in the update/new mode of the group
  private final boolean canDeleteUpdateNew;
  
  // Records can be removed at any time
  private final boolean canDeleteSelected;
  
  private final ResizeMode resizeMode;

  
	/**
	 * @param name
	 * @param eventHandler
	 * @param values
	 * @param position
	 * @param visible
	 * @param readonly
	 * @param tabIndex
	 * @param caption
	 * @param localTableAlias
	 * @param localTableField
	 */
	public TableListBoxDefinition(String name, String description,String inputHint, String eventHandler,
      Dimension position,boolean deleteUpdateNew, boolean deleteSelected, boolean visible, int tabIndex, int paneIndex,
      Caption caption, IBrowserDefinition browserToUse,
      ITableAlias localTableAlias, ITableField displayColmun, FontDefinition font, CastorResizeMode castorResizeMode)
	{
    super(name, description,inputHint, eventHandler, position, visible, true, tabIndex, paneIndex, caption, font, null);
    alias = localTableAlias;
    field = displayColmun;
    this.canDeleteSelected  = deleteSelected;
    this.canDeleteUpdateNew = deleteUpdateNew;
    this.browserToUse = browserToUse;
    this.resizeMode = ResizeMode.fromJacob(castorResizeMode);
	}

	public ResizeMode getResizeMode()
	{
	  return this.resizeMode;
	}
	
	public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent)
	{
		return factory.createTableListBox(app, parent, this);
	}


  protected void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    TableListBox jacobTableListBox = new TableListBox();
    if (null != getCaption())
      jacobTableListBox.setCaption(getCaption().toJacob());
    jacobTableListBox.setDimension(getDimension().toJacob());
    jacobTableListBox.setDisplayField(this.field.getName());
    jacobTableListBox.setTableAlias(this.alias.getName());
    if(this.browserToUse!=null)
      jacobTableListBox.setBrowserToUse(this.browserToUse.getName());
    jacobGuiElement.getCastorGuiElementChoice().setTableListBox(jacobTableListBox);
  }


  public ITableAlias getTableAlias()
  {
    return alias;
  }


  public ITableField getDisplayField()
  {
    return field;
  }

  /**
   * 
   * @return Returns the browserToUse or null they use only the displayField as sort criteria
   */
  public IBrowserDefinition getBrowserToUse()
  {
    return browserToUse;
  }
  
  
  /**
   * The user can delete a record if the group is in the "SELECTED" Mode
   * @return
   */
  public boolean getCanDeleteSelected()
  {
    return canDeleteSelected;
  }


  /**
   * The user can delete a record if the goup is in the "UPDATE" or "NEW" mode
   * @return
   */
  public boolean getCanDeleteUpdateNew()
  {
    return canDeleteUpdateNew;
  }

}
