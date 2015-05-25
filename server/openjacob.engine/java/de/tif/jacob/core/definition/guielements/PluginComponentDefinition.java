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

package de.tif.jacob.core.definition.guielements;

import java.util.Properties;

import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.PluginComponent;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas Herz
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PluginComponentDefinition extends GUIElementDefinition
{
  static public final transient String RCS_ID = "$Id: PluginComponentDefinition.java,v 1.4 2009/07/27 15:06:11 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";


//  private final boolean writeContainer;
//  private final Properties properties;
  private final String pluginClass;
  private final String pluginId;
  private final String pluginVersion;
  
  /**
	 * @param name
	 * @param eventHandler
	 * @param visible
	 * @param tabIndex
   * @param dimension 
	 * @param javaClass
	 */
	public PluginComponentDefinition(String name, String eventHandler, boolean visible, int tabIndex, int paneIndex, Dimension dimension,String pluginId, String pluginVersion, String pluginClass, boolean writeContainer, Properties properties)
  {
    super(name, null, eventHandler, dimension, true, tabIndex, paneIndex,null,-1,null,null);
    
//    this.writeContainer = writeContainer;
//    this.properties = properties;
    this.pluginClass = pluginClass;
    this.pluginId = pluginId;
    this.pluginVersion = pluginVersion;
  }


  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
    return factory.createPluginComponent(app, parent,this); 
  }
  
  protected final void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    PluginComponent jacobPlugin = new PluginComponent();
    
    jacobGuiElement.getCastorGuiElementChoice().setPluginComponent(jacobPlugin);
  }

  public String getPluginImplClass()
  {
    return this.pluginClass;
  }
  
  public String getPluginId()
  {
    return this.pluginId;
  }
  
  public String getPluginVersion()
  {
    return this.pluginVersion;
  }
}
