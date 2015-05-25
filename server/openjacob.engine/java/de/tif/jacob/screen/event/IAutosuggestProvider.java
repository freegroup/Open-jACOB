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
package de.tif.jacob.screen.event;

import de.tif.jacob.core.Context;
import de.tif.jacob.screen.IClientContext;

public interface IAutosuggestProvider 
{
	public final class AutosuggestItem
	{
		final String label;
		final Object userObj;
		public AutosuggestItem(String label, Object userObj)
		{
			this.label = label;
			this.userObj = userObj;
		}
		public String getLabel() 
		{
			return label;
		}
		
		public Object getUserObj() 
		{
			return userObj;
		}
		
	}

  /**
   * Will be called with the current user input.<br>
   * It's your turn to offer the user the 'best' values for the current user input fragment.
   * (autocomplete)<br>
   * 
   * @param context
   * @param userInputFragment
   * @param caretPosition TODO
   * @return auto suggest items
   * @throws Exception
   */
	public AutosuggestItem[] suggest(Context context, String userInputFragment, int caretPosition) throws Exception;
  
  /**
   * Open issue: this will not be called in a case of a FormDialog!
   * 
   * @param context
   * @param selectedEntry
   * @throws Exception
   */
	public void suggestSelected(IClientContext context, AutosuggestItem selectedEntry ) throws Exception;
}
