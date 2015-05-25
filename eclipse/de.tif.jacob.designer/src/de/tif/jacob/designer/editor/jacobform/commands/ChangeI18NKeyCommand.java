/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
/*
 * Created on Oct 20, 2004
 *
 */
package de.tif.jacob.designer.editor.jacobform.commands;

import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.UICaptionModel;

/**
 * Command to change the name field
 * 
 * @author Phil Zoio
 */
public class ChangeI18NKeyCommand extends Command
{
	private I18NResourceModel model;
	private String key;
	private String oldValue;
	private String newValue;
	private boolean keyCreated = false;
	
  /**
   * @param model
   * @param key
   * @param oldValue
   * @param newValue
   */
  public ChangeI18NKeyCommand(I18NResourceModel model, String key, String oldValue, String newValue)
  {
    super();
    this.model = model;
    this.key = key;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
	  if(!model.getJacobModel().hasI18NKey(key))
	  {
	    model.getJacobModel().addI18N(key,newValue);
	    keyCreated=true;
	  }
	  else
	  {
	    model.setValue(key,newValue);
	  }
	}

	/**
	 * @return whether we can apply changes
	 */
	public boolean canExecute()
	{
			return true;
	}


	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()
	{
	  if(keyCreated==true)
	    model.getJacobModel().removeI18N(key);
	  else
	    model.setValue(key,oldValue);
	}

}