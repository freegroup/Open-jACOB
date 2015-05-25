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

package de.tif.jacob.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class which extends from this class and has an entry in common.properties will be loaded at the bootstrap of the system For further information see the common.properties file
 * 
 * @author Andreas Herz
 *  
 */
public abstract class BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: BootstrapEntry.java,v 1.1 2007/01/19 16:38:08 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  protected static final Log logger = LogFactory.getLog(BootstrapEntry.class);

  final void boot() throws Throwable
  {
    logger.info("+-------------------------------------------------");
    logger.info("| starting subsystem [" + getClass().getName() + "]");
    logger.info("+-------------------------------------------------");
    init();
  }

  /**
   * Initializes the subsystem behind this bootstrap entry.
   * 
   * @throws Throwable
   *           On any severe problem
   */
  public abstract void init() throws Throwable;
  
  /**
   * Checks whether minor initialization problems exist, i.e. problems which
   * should not lead to abortion of jACOB startup procedure.
   * 
   * @return <code>true</code>, if the bootstrap entry has been initialized
   *         without any problems. <code>false</code>, if the bootstrap entry
   *         has been initialized with minor problems.
   * @see #init()
   */
  public boolean hasWarnings()
  {
    return false;
  }

  final void shutdown()
  {
    logger.info("+-------------------------------------------------");
    logger.info("| stopping subsystem [" + getClass().getName() + "]");
    logger.info("+-------------------------------------------------");
    try
    {
      destroy();
    }
    catch (Throwable th)
    {
      logger.error("Unable to stop subsystem [" + getClass().getName() + "]", th);
    }
  }

  /**
	 * will be called at the shutdown
	 *  
	 */
  public abstract void destroy() throws Throwable;
}
