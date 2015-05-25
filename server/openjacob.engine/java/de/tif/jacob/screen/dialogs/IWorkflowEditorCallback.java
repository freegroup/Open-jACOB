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

package de.tif.jacob.screen.dialogs;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;

/**
 * 
* @author Andreas Herz
 */
public abstract class IWorkflowEditorCallback
{
  static public final String RCS_ID = "$Id: IWorkflowEditorCallback.java,v 1.3 2011/01/11 10:56:52 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.3 $";

  /**
   */
  public abstract void onSave(IClientContext context, String xmlWorkflowDefinition) throws Exception;

  /**
   */
  public void onCancel(IClientContext context) throws Exception
  {
    // do nothing per default
  }

  /**
   * Indicator if a workflow supports user interaction like AskDialog or normal message Dialogs.
   * A workflow which runs in a TableHook should return <code>false</code>. A TableHook can be
   * run in a context without a logged in user (scheduler). In this case the workflow runs in an error.
   *  
   * @return
   */
  public boolean supportsUserInteraction()
  {
    return true;
  }
  
  public List<ISingleDataGuiElement> getRelevantGuiElements(IClientContext context)
  {
    return Collections.EMPTY_LIST;
  }
  
  public  List<ITableField> getRelevantTableFields(IClientContext context)
  {
    return Collections.EMPTY_LIST;
  }

  /**
   * Predefined Variables for the workflow.
   * 
   * @param context
   * @return
   */
  public   List<String> getPredefindVariables(IClientContext context)
  {
     return Collections.EMPTY_LIST;
  }
}
