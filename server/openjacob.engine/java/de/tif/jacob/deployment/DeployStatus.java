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

package de.tif.jacob.deployment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andreas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class DeployStatus
{
  public static final String RCS_ID = "$Id: DeployStatus.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
  public static final String RCS_REV = "$Revision: 1.1 $";

  // must be declared before following static DeployStatus instances
  private static final Map statusMap = new HashMap();
  
  public static final DeployStatus INACTIVE = new DeployStatus("inactive", false, false);
  public static final DeployStatus TEST = new DeployStatus("test", true, false);
  public static final DeployStatus PRODUCTIVE = new DeployStatus("productive", true, true);
  
  private final String status;
  private final boolean active;
  private final boolean productive;
  
  public static DeployStatus parseStatus(String s)
  {
    DeployStatus status = (DeployStatus) statusMap.get(s);
    if (status == null)
      throw new RuntimeException("Unknown deploy status: " + s);
    return status;
  }

  private DeployStatus(String status, boolean active, boolean productive)
  {
    this.status = status;
    this.active = active;
    this.productive = productive;
    
    statusMap.put(status, this);
  }
  
  public boolean isActive()
  {
    return this.active;
  }
  
  public boolean isProductive()
  {
    return this.productive;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return this.status;
  }
}
