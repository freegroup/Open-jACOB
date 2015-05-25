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

package de.tif.jacob.screen.impl.html;

import java.awt.Color;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;

/**
 * Abstrakte Klasse welche ein neuenes Plugin implementieren muss.<br>
 * Dies ist somit das Interface was dem Plugin-Programmierer zur verfügung steht.
 * 
 * Eine Ableitung diese Klasse (das Plugin) wird in der Klasse PluginComponent
 * gespeichert. Diese Instance wird durch den applikation Klassloader geladen und
 * verworfen sobald eine neue Applikationversion deployet wird.
 * 
 * @author Andreas Herz
 */
public abstract class PluginComponentImpl implements IGuiElement
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: PluginComponentImpl.java,v 1.8 2010/10/22 11:50:08 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.8 $";

  protected PluginComponent wrapper;
  
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    return false;
  }

  public void calculateHTML(ClientContext context, Writer w) throws Exception
  {
  }

  public void addDataFields(Vector fields)
  {
  }

  /**
   * Plugin wird aufgefordert HTML Fragmenten auszuliefern.
   *  
   * @param context
   * @param w
   * @param partId
   */
  public void writeHTMLPart(ClientContext context, Writer w, String partId)
  {
  }
  
  public boolean processParameter(int guid, String value)
  {
    if(guid==wrapper.getId())
      return true;
   
    return false;
  }

  public void resetCache()
  {
    wrapper.resetCache();
  }
  
  /**
   * Return the required Inlcude files for the plugin object.
   * The file path must be relative to the web application directory
   * of the jACOB app.<br>
   * <br>
   * e.g. <b>myLib.js</b> will be expanded to <b>http://&lt;HOST&gt;/jacob/application/test/1.2/&lt;PLUGIN_ID&gt;/myLib.js</b>
   * @return
   */
  public String[] getRequiredIncludeFiles()
  {
    return new String[0];
  }
  
  public void clear(IClientContext context) throws Exception
  {
  }

  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
  }

  public IGuiElement findByName(String name)
  {
    return wrapper.findByName(name);
  }

  public String getProperty(String name)
  {
    return wrapper.definition.getProperty(name);
  }
  
  public IForm getForm()
  {
    return wrapper.getForm();
  }

  public IApplicationDefinition getApplicationDefinition()
  {
    return wrapper.getApplicationDefinition();
  }

  public List getChildren()
  {
    return wrapper.getChildren();
  }

  public GroupState getDataStatus()
  {
    return wrapper.getDataStatus();
  }

  public IGroup getGroup()
  {
    return wrapper.getGroup();
  }

  public String getGroupTableAlias()
  {
    return wrapper.getGroupTableAlias();
  }

  public String getI18NLabel(Locale locale)
  {
    return wrapper.getI18NLabel(locale);
  }

  public int getId()
  {
    return wrapper.getId();
  }

  public String getLabel()
  {
    return wrapper.getLabel();
  }

  public String getName()
  {
    return wrapper.getName();
  }

  public IGroup getOuterGroup()
  {
    return wrapper.getOuterGroup();
  }

  public IGuiElement getParent()
  {
    return wrapper.getParent();
  }

  public String getPathName()
  {
    return wrapper.getPathName();
  }

  public boolean hasChild(IGuiElement element)
  {
    return wrapper.hasChild(element);
  }

  public boolean isEnabled()
  {
    return wrapper.isEnabled();
  }

  public boolean isVisible()
  {
    return  wrapper.isVisible();
  }

  public void requestFocus()
  {
    wrapper.requestFocus();
  }

  public void resetErrorDecoration(IClientContext context)
  {
    wrapper.resetErrorDecoration(context);
  }

  public void setBackgroundColor(Color color)
  {
    wrapper.setBackgroundColor(color);
  }

  public void setColor(Color color)
  {
    wrapper.setColor(color);
  }

  public void setEnable(boolean isEnable)
  {
    wrapper.setEnable(isEnable);
  }

  public void setErrorDecoration(IClientContext context, String message)
  {
    wrapper.setErrorDecoration(context, message);
  }

  public void setLabel(String label)
  {
    wrapper.setLabel(label);
  }

  public void setVisible(boolean visible)
  {
    wrapper.setVisible(visible);
  }

  public abstract Properties getProperties();
  public abstract void setProperties(Properties properties);

}
