/*
   This file is part of Open-jACOB
   Copyright (C) 2005-2010 Andreas Herz | FreeGroup

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; version 2 of the License.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License     
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
   USA

*/
package de.tif.jacob.designer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IObjectActionDelegate;

/**
 * 
 */
public abstract class AbstractNotifyAction implements IObjectActionDelegate 
{
  
  /**
   * 
   * 
   * @param action 
   * @param succeed 
   */
  protected void setSucceed(IAction action, boolean succeed)
  {
    if(action instanceof Action)
      ((Action)action).notifyResult(succeed);
  }
}
