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
package de.tif.jacob.cluster.impl;

import java.io.Serializable;

/**
 * Interface to implement for group message listeners.
 * 
 * @see de.tif.jacob.cluster.Group#register(String, IGroupMessageListener, boolean)
 * 
 * @author Andreas Sonntag
 */
public interface IGroupMessageListener
{
  /**
   * Method called by group, if a new message of a given type is received.
   * 
   * @param msg
   *          the message content or <code>null</code>, if no content
   *          exists.
   * @throws Exception
   *           an exception could be thrown, if message could not be processed
   *           for any reason.
   */
  public void receive(Serializable msg) throws Exception;
}
