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

package de.tif.jacob.screen.impl;

import java.util.Iterator;
import java.util.TimeZone;
import org.apache.commons.collections.iterators.SingletonIterator;
import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.security.IUser;


/**
 *
 */
public abstract class HTTPClientContext extends IClientContext
{
  transient private Boolean debug;
  
  public abstract void addOnLoadJavascript(String code);
  
  public HTTPClientContext(IApplication application, IUser user)
  {
    super(application, user);
  }
  
  public final TimeZone getTimeZone()
  {
    return TimeZone.getDefault();
  }

  /**
   * @return Returns the debug.
   */
  public final boolean isDebug()
  {
    // lazy evaluation of Property.GUI_DEBUG to avoid endless recursion during
    // initialization
    if (debug == null)
      debug = Boolean.valueOf(Property.GUI_DEBUG.getBooleanValue());
    return debug.booleanValue();
  }
  

  /**
   * Return the current data accessor for the logged in user
   * 
   * @return
   */
  public final IDataAccessor getDataAccessor()
  {
    // Die Application hat eine allgemeine Sicht auf die Daten.
    // ALLE Domainen haben einen IDataAccessor!!
    //
    DataScope dataScope;
    HTTPForm form = (HTTPForm) getForm();
    HTTPDomain domain = (HTTPDomain) getDomain();
    if (form != null)
      dataScope = form.getDataScope();
    else if (domain != null)
      dataScope = domain.getDataScope();
    else
      dataScope = ((HTTPApplication) getApplication()).getDataScope();

    // Alle Formen in einer Domain haben die gleiche Datensicht
    //
    if (dataScope == DataScope.DOMAIN)
      return domain.getDataAccessor();

    // Jede Form hat Ihre eigene Datensicht
    //
    if (dataScope == DataScope.FORM)
      return form.getDataAccessor(); 
    
    if (dataScope == DataScope.APPLICATION)
      return ((HTTPApplication) getApplication()).getDataAccessor();

    throw new IllegalArgumentException("jACOB-Property [" + Property.DATA_ACCESSOR_SCOPE + "] has illegal value [" + Property.DATA_ACCESSOR_SCOPE.getValue(this.getApplicationDefinition()) + "]. Valid values are [application,domain,form]");
  }

  
  /**
   * Return the selected record for the group in which the user action 
   * has been performed
   * 
   * @return the selected datarecord or null
   */
  public final IDataTableRecord getSelectedRecord()
  {
    IDataTable table = getDataAccessor().getTable(getGroup().getGroupTableAlias());
    return table.getSelectedRecord();
  }
  
  /**
   * Reset the GUI and the data elements for the current group.
   * 
   */
  public final void clearGroup() throws Exception
  {
    IGroup group = getGroup();
    if (group != null)
    {  
      String tableAliasName= group.getGroupTableAlias();
      
      // Es müssen ALLE (auch nicht sichtbaren) Gruppen, welche
      // mit dem Tabellenalias verbunden sind gelöscht werden.
      Iterator formIter;
      DataField[] fields;
      if (((GuiElement) group).getDataScope() == DataScope.FORM)
      {
       formIter = new SingletonIterator(getForm());
       fields =((HTTPForm)getForm()).getDataFields();
      }
      else
      {
       formIter = getDomain().getChildren().iterator();
       fields =((HTTPDomain)getDomain()).getDataFields();
      }
      
      while(formIter.hasNext())
      {
        Object obj=formIter.next();
        if(obj instanceof HTTPForm)
        {
          HTTPForm form=(HTTPForm)obj;
          Iterator groupIter = form.getChildren().iterator();
          while(groupIter.hasNext())
          {
            obj = groupIter.next();
            if(obj instanceof HTTPGroup)
            {
              HTTPGroup grp=(HTTPGroup)obj;
              if(grp.getGroupTableAlias().equals(tableAliasName))
                grp.clear(this,false);
            }
          }
        }
      }
      
      // Alle ForeingFields welche mit dem Tabellenalias verbunden sind müssen
      // gelöscht werden.
      //
      for(int i=0;i<fields.length;i++)
      {
        if((fields[i].getParent() instanceof HTTPForeignField))
        {
          if( (fields[i].getTableAlias().getName().equals(tableAliasName)))
	        {  
	          fields[i].getParent().clear(this);
	        }
        }
      }
      
    }
  }
  /** 
   * Register the Resource in the ClientSession scope. If the session will be destroyed, the 
   * resource will be destroyed too.<br>
   * <br>
   * 
   * Call Context.getCurrent().unregister(..) if you have destroyed the resource
   * manually. If you don't call it, the resource will be released twice. 
   * 
   * @param resource The resource to manage.
   */
  public final void registerForSession(ManagedResource resource)
  {
    ((HTTPClientSession)getApplication().getSession()).register(resource);
  }

  /** 
   * Register the Resource in the Window scope. If the client window will be destroyed, 
   * the resource will be destroyed too.<br>
   * <br>
   * 
   * Call Context.getCurrent().unregister(..) if you have destroyed the resource
   * manually. If you don't call it, the resource will be released twice. 
   * 
   * @param resource The resource to manage.
   */
  public final void registerForWindow(ManagedResource resource)
  {
    ((HTTPApplication)getApplication()).register(resource);
  }

  
  /**
   * Remove the managed resource for the life cycle management. Now it's your
   * turn to free the resource.
   * 
   * @param resource The resource which should remove from life cycle management.
   */
  public final void unregister(ManagedResource resource)
  {
    super.unregister(resource);
    ((HTTPClientSession)getApplication().getSession()).unregister(resource);
    ((HTTPApplication)getApplication()).unregister(resource);
  }
  
  /** 
   * Set the property in the ClientSession scope. If the session will be destroyed, the 
   * properties will be removed too.<br>
   * <br>
   */
  public void setPropertyForWindow(Object key, Object value)
  {
    ((HTTPApplication)getApplication()).setPropertyForWindow(key, value);
  }
  
  public void setPropertyForSession(Object key, Object value)
  {
    ((HTTPClientSession)getApplication().getSession()).setPropertyForSession(key, value);
  }
  
  public Object getProperty(Object key)
  {
    // versuche das property vom parent zu holen (request lifecycle)
    Object value = super.getProperty(key);
    
    if(value==null)
      value = ((HTTPApplication)getApplication()).getPropertyForWindow(key);
    
    if(value==null)
      value = ((HTTPClientSession)getApplication().getSession()).getPropertyForSession(key);

    return value;
  }
  
  public final boolean isInReportMode()
  {
    return ((HTTPApplication)getApplication()).isInReportMode(this);
  }
  
  public final void setDomain(IDomain domain)
  {
    this.domain=domain;
  }

  public final void setForm(HTTPForm form)
  {
    this.form = form;
  }

  public final void setGroup(HTTPGroup group)
  {
    this.group = group;
  }
  
  public void setDomain(HTTPDomain domain)
  {
    this.domain = domain;
  }
  }
