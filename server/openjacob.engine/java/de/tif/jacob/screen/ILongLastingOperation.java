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
/*
 * Created on 15.10.2004
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.screen;

/**
 * @author Andreas
 * 
 * Interface for cancelable long lasting operations.
 */
public interface ILongLastingOperation
{
  /**
	 * Any long lasting operation must implement this cancel method, which should abort the operation
	 * as quick as possible.
	 * 
	 * @return <code>true</code> if the operation has been successfully canceled or <code>false</code>
	 *         if cancelling is not possible at the current moment for any reason
	 * @throws Exception
	 *           any other problem then trying to cancel
	 */
  public boolean tryToCancel() throws Exception;
}
