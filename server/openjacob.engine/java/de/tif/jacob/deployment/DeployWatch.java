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

import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.SecondsIterator;

/**
 * This task checks for any updates concerning deployed application verions and
 * processes these updates. <br>
 * Note: This class will be extended by an appropriate class in the admin
 * applications and will, therefore, be automatically loaded on deployment of
 * the admin application.
 * 
 * @author Andreas Sonntag
 */
public abstract class DeployWatch extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: DeployWatch.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#iterator()
   */
  public ScheduleIterator iterator()
  {
    return new SecondsIterator(60);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTaskSystem#run(de.tif.jacob.scheduler.TaskContextSystem)
   */
  public void run(TaskContextSystem context) throws Exception
  {
    DeployManager.checkInstalledApplicationUpdates();
    
    // IBIS: Hier fehlt noch die Behandling zum Aufräumen von gelöschten Applicationsversionen
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#runPerInstance()
   */
  public boolean runPerInstance()
  {
    // task must run on each instance
    return true;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#hibernatedOnSchedule()
   */
  public boolean hibernatedOnSchedule()
  {
    // IBIS: Im Single-Node-Betrieb könnte man true zurückliefern.
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#isVisible()
   */
  public boolean isVisible()
  {
    // IBIS: Im Single-Node-Betrieb könnte man false zurückliefern.
    return true;
  }
}
