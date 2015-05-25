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

package de.tif.jacob.screen.impl;

import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IAction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;

public abstract class AbstractAction extends IAction
{
  private String i18nTooltip=null;
  private String i18nLabel=null;
  
  /** 
   * Falls die Action ein Dokument zurückliefert, sollte hier
   * "false" eingetragen werden.......siehe am besten unter BrowserActionExcel.java!!
   * @return
   */
  public boolean reloadPage()
  {
    return true;
  }
  
  /**
   * The name of the Icon to display. The icon will be searched in the <b>{currentTheme}/icon</b> directory.<br>
   * In opsosite to  {@link #getIcon(IClientContext)}, the visual representation <b>is related</b> to the 
   * current selected theme.
   * <br>
   * <br>
   * Your action must at least override one of these methods: {@link #getIcon(IClientContext)} or {@link #getIcon()} 
   *  
   * @return The name of the icon to display.
   * @see #getIcon(IClientContext)
    */
  public  String getIcon()
  {
    return null;
  }
  
  /**
   * The icon to display. The icon will be searched in the <b>&lt;jACOB-WEB&gt;/icons/.</b> directory.<br>
   * In oposite to {@link #getIcon()}, the visual representation of the icon <b>is not related</b> to the current user theme.
   * <br>
   * <br>
   * Your action must at least override one of these methods: {@link #getIcon(IClientContext)} or {@link #getIcon()} 
   * 
   * @param context
   * @return
   */
  public Icon getIcon(IClientContext context)
  {
    return Icon.NONE;
  }
  
  
  public final String getI18NLabel(IClientContext context)
  {
    if(i18nLabel==null)
      return i18nLabel = I18N.getCoreLocalized(getLabelId(), context, context.getLocale());
    else
      return i18nLabel;
  }

  /**
   * The tooltip text if the user hoovers over the browser action icon.
   * 
   * @return The tooltip message to display if the user hoover over the browser action icon.
   */
  public final String getTooltip(IClientContext context)
  {
    if(i18nTooltip==null)
      return i18nTooltip = I18N.getCoreLocalized(getTooltipId(), context, context.getLocale());
    else
      return i18nTooltip;
  }
 
  public String getLabel(IClientContext context)
  {
    return getTooltip(context);
  }

  /**
   * Dummerweise werden hier ID zurückgegeben. Somit kann man "AbstractAction" nicht dem Anwendungsentwickler
   * veröffentlichen. Design Fehler. 
   * 
   * @return The key in the i18n translation file for the ToolTip in the UI.
   */
  public abstract String getTooltipId();
  
  /**
   * Dummerweise werden hier ID zurückgegeben. Somit kann man "AbstractAction" nicht dem Anwendungsentwickler
   * veröffentlichen. Design Fehler. 
   * 
   * @return The key in the i18n translation file for the label in the UI.
   */
  public abstract String getLabelId();
  
  /**
   * The action method. This will be called if the use clicks on the browser action icon.
   * 
   * @param context The current context if the application
   * @param browser The browser assigned to the action
   * @param value The value of the action. In the case of the action 'click' is this the selected row
   * @throws Exception w
   */
  public abstract void execute(IClientContext context, IGuiElement element, String value) throws Exception;
  
}
