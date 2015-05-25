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

/**
 * Interface to register deploy notifees.
 * 
 * @see DeployManager#registerNotifyee(DeployNotifyee)
 * 
 * @author Andreas Sonntag
 */
public interface DeployNotifyee
{
  public static final String RCS_ID = "$Id: DeployNotifyee.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
  public static final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Called if an application version is deployed the first time.
   * 
   * @param entry
   *          the deploy entry which has been deployed
   * @throws Exception
   *           on any problem
   */
  public void onDeploy(DeployEntry entry) throws Exception;

  /**
   * Called before an application version is redeployed, i.e. this is the case
   * if: <br>
   * <li>the status off an deploy entry is changed (see
   * {@link DeployEntry#getStatus()})
   * <li>a new patch off an application version has been uploaded
   * 
   * @param oldEntry
   *          the old entry before redeployment
   * @throws Exception
   *           on any problem
   */
  public void beforeRedeploy(DeployEntry oldEntry) throws Exception;

  /**
   * Called after an application version has been redeployed, i.e. this is the
   * case if: <br>
   * <li>the status off an deploy entry is changed (see
   * {@link DeployEntry#getStatus()})
   * <li>a new patch off an application version has been uploaded
   * 
   * @param newEntry
   *          the new entry after redeployment
   * @throws Exception
   *           on any problem
   */
  public void afterRedeploy(DeployEntry newEntry) throws Exception;

  /**
   * Called if an application version is undeployed, i.e. this is the case if:
   * <br>
   * <li>an application version is uninstalled
   * <li>the jACOB engine is shutdowned
   * 
   * @param entry
   *          the deploy entry which is going to be undeployed
   * @throws Exception
   *           on any problem
   */
  public void onUndeploy(DeployEntry entry) throws Exception;
}
