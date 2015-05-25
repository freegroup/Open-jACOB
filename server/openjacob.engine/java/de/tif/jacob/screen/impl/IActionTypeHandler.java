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

import de.tif.jacob.core.definition.actiontypes.ActionTypeAbout;
import de.tif.jacob.core.definition.actiontypes.ActionTypeChangeUpdateRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearApplication;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearDomain;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearForm;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearGroup;
import de.tif.jacob.core.definition.actiontypes.ActionTypeCreateReport;
import de.tif.jacob.core.definition.actiontypes.ActionTypeDeleteRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeExit;
import de.tif.jacob.core.definition.actiontypes.ActionTypeGeneric;
import de.tif.jacob.core.definition.actiontypes.ActionTypeMessaging;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNavigateToForm;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewWindow;
import de.tif.jacob.core.definition.actiontypes.ActionTypeRecordSelected;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearchUpdateRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSetUserPassword;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowAlert;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowReports;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowSQL;
import de.tif.jacob.core.definition.actiontypes.ActionTypeThemeSelect;
import de.tif.jacob.core.definition.actiontypes.ActionTypeUpdateRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * @author Andreas Herz
 *
 */
public interface IActionTypeHandler
{
  static public final String RCS_ID = "$Id: IActionTypeHandler.java,v 1.7 2009/11/19 17:56:10 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.7 $";

  public void execute(ActionTypeNewRecord type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeNewWindow type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeClearApplication type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeClearDomain type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeClearForm type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeClearGroup type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeDeleteRecord type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeGeneric type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeRecordSelected type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeSearch type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeSearchUpdateRecord type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeUpdateRecord type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeExit type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeThemeSelect type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeShowSQL type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeShowAlert type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeMessaging type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeCreateReport type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeShowReports type, IClientContext context, IActionEmitter emitter) throws Exception;
	public void execute(ActionTypeNavigateToForm type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeSetUserPassword type, IClientContext context, IActionEmitter emitter) throws Exception;
  public void execute(ActionTypeAbout type, IClientContext context, IActionEmitter emitter) throws Exception;
	
	public void onOuterGroupStatusChanged(ActionTypeNewRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
	public void onOuterGroupStatusChanged(ActionTypeNewWindow type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
	public void onOuterGroupStatusChanged(ActionTypeChangeUpdateRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeClearApplication type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeClearDomain type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeClearForm type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeClearGroup type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeDeleteRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeGeneric type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeRecordSelected type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeSearch type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeSearchUpdateRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeUpdateRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeExit type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeShowSQL type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeShowAlert type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeMessaging type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeThemeSelect type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeCreateReport type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeShowReports type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
	public void onOuterGroupStatusChanged(ActionTypeNavigateToForm type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeSetUserPassword type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  public void onOuterGroupStatusChanged(ActionTypeAbout about, IClientContext context, IGuiElement emitter, GroupState status) throws Exception;
  
  public void onGroupStatusChanged(ActionTypeUpdateRecord actionTypeUpdateRecord, IClientContext context, IGuiElement target, GroupState status) throws Exception;
}
