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

import java.util.List;
import de.tif.jacob.core.definition.ActionType;

/**
 * @author Andreas Herz
 *
 */
public interface IToolbar extends IGuiElement
{
  static public final String RCS_ID = "$Id: IToolbar.java,v 1.2 2007/12/05 17:35:35 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.2 $";

  /**
   * 
   * @return List[IToolbarButton] the children
   */
  public List getButtons();
  
  /**
   * The buttons of the handsover action or <b>null</b> of the toolbar doesn't contain this type of button.<br>
   * It is not required, that the button is visible in the toolbar. It must only a member of the
   * toolbar.<br>
   * 
   * TODO Wie soll man besser einen Button identifiezieren?!
   *       Soll man wirklich die "Action"an den AppProgrammierer weiter reichen?
   * @param type
   * @return The buttons of the handsover action or null of the toolbar doesn't contain this type of button.
   */
  public IToolbarButton getButton(ActionType type);
  
  /**
   * Enable/disable the configure dialog of the toolbar. The configure dialog
   * can be activate with the right click on the toolbar.
   * 
   * @since 2.7.2
   * @param flag
   */
  public void setConfigureable(boolean flag);
}
