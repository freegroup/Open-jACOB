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

package de.tif.jacob.balancer.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.balancer.Balancer;
import de.tif.jacob.balancer.IStrategy;
import de.tif.jacob.cluster.ClusterManager;

/**
 * Round robin balancer strategy.
 */
public class RoundRobinStrategy implements IStrategy
{
  static public final transient String RCS_ID = "$Id: RoundRobinStrategy.java,v 1.1 2007/01/19 09:50:50 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  static private final Log logger = LogFactory.getLog(Balancer.class);
  
  private int nextIndex = 0;

  public String getRedirectUrl(String srcUrl)
  {
    List members = ClusterManager.getProvider().getNodeUrls();
    // Wenn der Cluster nur ein Mitglied hat, dann braucht man auch kein Redirect machen
    //
    if(members.size()<=1)
      return null;
    
    if (nextIndex >= members.size())
      nextIndex = 0;

    String newUrl = (String) members.get(nextIndex);
    nextIndex += 1;
    
    // wenn der Redirect zum selben {Server:Port} geht, dann wird die original URL nicht
    // verändert
    //
    if(srcUrl.startsWith(newUrl))
      return null;
    
    
    if (logger.isDebugEnabled())
      logger.debug("Redirect request to:" + newUrl);
    return newUrl;
  }
}
