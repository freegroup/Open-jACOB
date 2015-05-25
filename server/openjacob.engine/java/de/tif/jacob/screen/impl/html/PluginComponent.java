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

import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.definition.guielements.PluginComponentDefinition;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IPluginComponent;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PluginComponent extends GuiHtmlElement implements IPluginComponent
{
  static public final transient String RCS_ID = "$Id: PluginComponent.java,v 1.9 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

  public final PluginComponentDefinition definition;
  PluginComponentImpl implementation;
  
  protected PluginComponent(IApplication app, PluginComponentDefinition def)
  {
    super(app, def.getName(),"", def.isVisible(), def.getRectangle(), def.getProperties());
    this.definition = def;
    this.implementation = (PluginComponentImpl)ClassProvider.createInstance(app.getApplicationDefinition(), this.definition.getPluginImplClass());
    
    if(this.implementation==null)
      throw new RuntimeException("Unable to load class ["+this.definition.getPluginImplClass()+"] from jacapp");
    this.implementation.wrapper = this;
  }

  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    checkClassLoader((ClientContext)context);    
    return this.implementation.processEvent(context, guid, event, value);
  }

  /**
   * return the HTML representation of this object
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    if(getCache()==null)
    {
      checkClassLoader((ClientContext)context);    
      Writer w = newCache();
      w.write("\t<div class=\"plugin_container\" style=\"overflow:hidden;");
      getCSSStyle(context, w,boundingRect);
      w.write("\">");
      this.implementation.calculateHTML(context, w);
      // custom stuff from the implementation
      w.write("</div>\n");
    }
  }


  public PluginComponentImpl getImplementation()
  {
    // bevor die Implementierung rausgegeben werden kann muß erstmal geprüft werdne ob ein HotDeployment
    // durchgeführt wurde. Man bekommt sonst eine ClassCastException
    checkClassLoader((ClientContext)Context.getCurrent());
    
    return this.implementation;
  }
  
  public void calculateIncludes(ClientContext context)
  {
    String[] files = this.implementation.getRequiredIncludeFiles();
    for(int i=0;i<files.length;i++)
    {
      context.addAdditionalIncludes(this.definition.getPluginId(), files[i]);
    }
  }
  
  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    writeCache(w);
  }
  
  /**
   * Wird von einer Pluginimplementation gerufen wenn diese nur Teile, meist nach
   * einer aktualisieren/event, von der Componente benötigt. ISt z.B. beim aufklappen
   * von Listen oder Bäumen der Fall. Durch diese Methode kann dann gezielt die aktualiserten
   * Teile abgefragt werden.
   * 
   * @param context
   * @param w
   * @param partId
   */
  public void writeHTMLPart(ClientContext context, Writer w, String partId)
  {
    this.implementation.writeHTMLPart(context, w, partId);
  }
  
  /**
   * a catiopn has no searchable data fields......
   */
  protected void addDataFields(Vector fields)
  {
    checkClassLoader((ClientContext)Context.getCurrent());    
    this.implementation.addDataFields(fields);
  }
  

	public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }

  public boolean processParameter(int guid, String value) throws IOException, NoSuchFieldException
  {
    checkClassLoader((ClientContext)Context.getCurrent());    
    if(this.implementation.processParameter(guid, value)==true)
      return true;
    return super.processParameter(guid, value);
  }

  public void clear(IClientContext context) throws Exception
  {
    super.clear(context);
    checkClassLoader((ClientContext)context);    
    this.implementation.clear(context);
  }


  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    super.onGroupDataStatusChanged(context, newGroupDataStatus);
    checkClassLoader((ClientContext)context);    
    this.implementation.onGroupDataStatusChanged(context, newGroupDataStatus);
  }
  
  private void checkClassLoader(ClientContext context)
  {
    // Falls der Classloader des Hook!=Implementierung ist wurde ein Hootdeployment durchgeführt.
    // Folge: Die Implementierung muss verworfen und mit dem Classloader des Hook neu geladen werden
    //        da es sonst zu einer Classcast-Exception kommt. 
    // Nachteil: Daten (Membervariablen) der Implementierung gehen verloren. Diese werden dann manuell durch properties 
    //           rüber gerettet. Durch die Unterschiedlichen Klassloader kann die Implementierung dies nicht 
    //           selber tun. Dies muß eine übergeordnete Instanz managen.
    Object hook = ClassProvider.createInstance(context.getApplicationDefinition(), this.definition.getPluginImplClass());
    if(hook!=null && hook.getClass().getClassLoader()!= this.implementation.getClass().getClassLoader())
    {
      PluginComponentImpl oldImpl = this.implementation;
      this.implementation = (PluginComponentImpl)hook;
      
      if(this.implementation==null)
        throw new RuntimeException("Unable to load class ["+this.definition.getPluginImplClass()+"] from jacapp");
      this.implementation.wrapper = this;
      
      // rüberretten der alten Properties von der alten Instance in die Instance mit dem neuen Classloader
      //
     this.implementation.setProperties( oldImpl.getProperties());
     
     resetCache();
    }
  }
}
