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

package de.tif.jacob.report.birt.impl;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.BootstrapEntry;

/**
 * @author Andreas Herz
 *
 */
public class BirtEngineManager extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: BirtEngineManager.java,v 1.2 2009/07/21 13:18:44 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  private static ReportEngine engine;
  
  public static ReportEngine getEngine()
  {
    return engine;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#init()
   */
  public void init() throws Throwable
  {
    //Engine Configuration - set and get temp dir, BIRT home, Servlet context
    EngineConfig config = new EngineConfig();
    
    config.setEngineHome( Bootstrap.getBIRTPath() ); 
//    config.setEngineHome("/home/andherz/workspace/etr/webapp/WEB-INF/birt-runtime/ReportEngine" ); 
        
    //Create the report engine
    engine = new ReportEngine( config );
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    engine.destroy();
  }
  
  
}
