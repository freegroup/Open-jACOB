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

import java.awt.Color;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;
import de.tif.jacob.screen.impl.html.PluginComponent;
import de.tif.jacob.util.FastStringWriter;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public abstract class GuiElement implements IGuiElement  , PropertyChangeListener
{
  private GroupState dataStatus = UNDEFINED;
  protected GuiElement parent = null;
  
  private FastStringWriter cache = null;
  private int cacheBufferSize = 128;
  private String eventClassName     = null;
  private String eventGUID          = null;
  private List   childList= Collections.EMPTY_LIST;
  
  protected char[] cssStyleCache = null;

  static public final transient String ID_PREFIX ="j_";
  
  public Rectangle boundingRect;
  private boolean      visible    = true;

  protected FastStringWriter lastErrorMessage =null;
  
  protected final int id;

  // Eine MutableGroup kann im onGroupStatusChanged Elemente in der Gruppe einfï¿½gen
  // Diesen Elementen wird dann ein Eventhandler ï¿½bergeben. Dieser wird sich dann
  // hier gemerkt. 
  // Im gegensatz zu den GuiElementen welche im JAD aufgefï¿½hrt sind. Dort wird der 
  // der Klassenname des Eventhandler eingetragen 
  // Im Moment NUR relevant fï¿½r Elemente welche mit OwnDraGroup eingefï¿½gt werden.
  //
  private GuiEventHandler eventHandlerSetByOwnDrawGroup=null;
  
  // required for Group, FlowLayoutContainer.
  // This elements must be notified if any child has beend changed any
  // property
  //
  private PropertyChangeSupport   listeners;

  /**
   * Tri state: null -> default handling, i.e. disabled when selected otherwise enabled
   *            true -> application programmer has explicitly enabled by event handler
   *            false -> application programmer has explicitly disabled by event handler
   */
  protected Boolean isUserEnabled = null;
  
  private String       label      = "";
  private String       i18nLabel  = null;
  
  protected final String  name;
  
  protected Color   backgroundColor = null; // null => use default from CSS
  protected Color   borderColor = null; // null => use default from CSS
  protected Color   foregroundColor  = null; // null => use default from CSS
  protected int     borderWidth = -1; // -1 => use default from CSS
  protected final Map definitionProperties;
  
  // TODO: implement font handling for the other elements
  //         (At the Moment only for caption.)
  protected FontDefinition fontDef  = null; // null => use default from CSS 
  
  protected final IApplication application;
  
  public abstract String  getEventHandlerReference();

  public GuiElement(IApplication app, String name, String label, boolean isVisible, Rectangle boundingRect, Map definitionProperties)
  {
    super();
    this.visible = isVisible; 
    this.name  = name;
    this.label = label;
    this.boundingRect = boundingRect;
    this.application = app;
    this.id = IDProvider.next();
    listeners = new PropertyChangeSupport(this);
    this.definitionProperties = definitionProperties;
  }

  public IApplication getApplication()
  {
    return application;
  }
  
 
  public void setParent(GuiElement newParent)
  {
    parent = newParent; 
  }

  /**
   * 
   * @return
   * @author Andreas Herz
   */
  public boolean isVisible()
  {
    return visible;
  }
  
  /**
   * 
   * @param flag
   * @author Andreas Herz
   */
  public void setVisible(boolean flag)
  {
    // Ein set visible sollte nicht den Cache lï¿½schen.
    // Wenn man die TabPane wechselt wird setVisible aufgerufen. Dies zerstï¿½rt dann immer
    // das HTML für die Tab welche ausgeblendet wird und für die Tab welche eingeblendet wird.
    // => Performance
    // 
    // Man braucht den Cache auch nicht löschen, da das HTML bei visible==false gar nicht raus gerendert wird.
    //
   // resetCache();
    visible = flag;
  }
  

  public void resetErrorDecoration(IClientContext context)
  {
    lastErrorMessage = null;
    ((GuiElement)this.getParent()).firePropertyChange();
  }

  /**
   * Return true if the last "active" server request produce an error
   * which are related to this UI-Element. e.g RequireFieldException during a save operation.
   * 
   * @return  <b>true</b> if the last "active" server request produce an error
   */
  public boolean hasError()
  {
    return lastErrorMessage!=null;
  }
  
  /**
   * Checks whether an application programmer has disabled the element by scripting.
   * 
   * @return
   */
  protected final boolean isNotUserDisabled()
  {
    if (isUserEnabled == null)
      return true;
    
    return isUserEnabled.booleanValue();
  }
  
  public boolean isEnabled()
  {
    return isNotUserDisabled();
  }
  
  public final void setEnable(boolean flag)
  {
    // reset cache for child (e.g. captions) elements as well 
    invalidate();
    isUserEnabled = Boolean.valueOf(flag);
  }
  
  protected void setFont(FontDefinition font)
  {
    this.fontDef = font;
  }
  
  public void setLabel(String l)
  {
    resetCache();
    label = l;
    i18nLabel=null;
  }
  
  public String getLabel()
  {
  	return label;
  }

  
  public String getI18NLabel(Locale locale)
  {
    if (i18nLabel == null)
    {
      if (this.label == null)
        return null;
      
      i18nLabel = I18N.localizeLabel(this.label, this.getApplicationDefinition(), locale);
    }
    return i18nLabel;
  }

  
  public final String getName()
  {
   return name; 
  }


  /**
   * Traverse the GUI-Element tree and try's to find the hands over element in the
   * object tree. This method search recursive in all children.
   * 
   * @param elementId
   * @return The GUI-Eleemtn with the required name.
   */
  public GuiElement findChild(int guid)
  {
    // if this object the required object....
    //
    if(guid==this.getId())
      return this;
    
    // ...or any of its childs....
    //
    for (int i=0; i<childList.size();i++)
    {
      GuiElement element = (GuiElement)childList.get(i);
      if(element.getId()==guid)
        return element;
    }
    
    // ...or does any child has the required object as child?
    //
    for (int i=0; i<childList.size();i++)
    {
      GuiElement element = (GuiElement)childList.get(i);
      GuiElement founded = element.findChild(guid);
      if(founded!=null)
        return founded;
    }
    
    // The object is not in this subtree
    //
    return null;
  }
 
  /* (non-Javadoc)
   * @see de.tif.jacob.screen.IGuiElement#findByName(java.lang.String)
   */
  public final IGuiElement findByName(String name)
  {
    if(name==null)
      return null;

    // if this object the required object....
    //
    /* sollte nur in die child objekte schauen. Nicht auf sich selbst
    if(n.equals(this.name))
      return this;
    */
    // ...or any of its children....
    //
    for (int i=0; i<childList.size();i++)
    {
      GuiElement element = (GuiElement)childList.get(i);
      if(element.getName().equals(name))
      {
        if(element instanceof PluginComponent)
          return ((PluginComponent)element).getImplementation();
        return element;
      }
    }

    // ...or does any children has the required object as child?
    //
    for (int i=0; i<childList.size();i++)
    {
      IGuiElement element = (GuiElement)childList.get(i);
      IGuiElement founded = element.findByName(name);
      if(founded!=null)
        return founded;
    }
    
    // The object is not in this subtree
    //
    return null;
  }


  
  /**
   * Traverse the GUI-Element tree and try's to find the hands over element in the
   * object tree. This methos search recursive in all childs.
   * 
   * @param elementId
   * @return The GUI-Eleemtn with the required name.
   */
  public final boolean hasChild(IGuiElement element)
  {
  	  return findChild(element.getId())!=null;
  }
  
  /**
   * Clear/Reset the GUI Element to the default behaviour.
   * 
   * The default implementation calls all child to clear it.
   */
  public void clear(IClientContext context) throws Exception
  {
    resetCache();
    for (int i=0; i<childList.size();i++)
    {
      ((GuiElement)childList.get(i)).clear(context);
    }
  }
  
  /**
   * Will  be called if the outer group status has been changed. The outer group is always the top level
   * group. The event is never triggerd by a TabPane.
   * 
   */
  public void onOuterGroupDataStatusChanged(de.tif.jacob.screen.IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    for (int i=0; i<childList.size();i++)
    {
      ((GuiElement)childList.get(i)).onOuterGroupDataStatusChanged(context, newGroupDataStatus);
    }
    Object eventObj =getEventHandler(context);
    if(eventObj instanceof IGroupMemberEventHandler)
      ((IGroupMemberEventHandler)eventObj).onOuterGroupStatusChanged(context, newGroupDataStatus, this);
  }

  /**
   * Will be called if the group status has been changed. A group can be either a TabPane or a real top level
   * group.
   * 
   */
  public void onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    resetCache();
    for (int i=0; i<childList.size();i++)
    {
      ((GuiElement)childList.get(i)).onGroupDataStatusChanged(context, newGroupDataStatus);
    }
    dataStatus= newGroupDataStatus;
  }

  
  /**
   * @param dataStatus The dataStatus to set.
   */
  public void setDataStatus(IClientContext context, GroupState dataStatus) throws Exception
  {
    if(dataStatus!=this.dataStatus)
      resetCache();
    this.dataStatus = dataStatus;
  }

  /**
   * force the redraw of all elements
   *
   */
  public void invalidate()
  {
    resetCache();
    List children = getChildren();
    for (int i = 0; i < children.size(); i++)
    {
      ((GuiElement) children.get(i)).invalidate();
    }
  }

  /**
	 * @return Returns the cachedHTML.
	 */
  protected final FastStringWriter getCache()
  {
  	return cache;
  }

  public void resetCache()
  {
    cache = null;
    this.lastErrorMessage = null;
    firePropertyChange();
  }

	public final Writer newCache(int initialSize)
	{
	  this.cache = new FastStringWriter(initialSize);
	  return this.cache;
	}
	
	public final Writer newCache()
	{
	  this.cache = new FastStringWriter(this.cacheBufferSize);
	  return this.cache;
	}
	
	public final void writeCache(Writer w) throws IOException
	{
	  if (null != this.cache)
	  {  
	    if (cacheBufferSize < cache.length())
	     {
	      cacheBufferSize = cache.length();
	    }
	    this.cache.writeTo(w);
	  }
	}

  public List getChildren()
  {
  	return childList;
  }
	
  public void addChild(IGuiElement child)
  {
    resetCache();
    ((GuiElement)child).setParent(this);
    if (childList == Collections.EMPTY_LIST)
    	childList = new ArrayList();
    childList.add(child);
  }
  
  public void removeAllChildren()
  {
    childList = Collections.EMPTY_LIST;
    resetCache();
  }
  
  /**
   * 
   * @param elementId
   * @return
   */
  protected final GuiElement getChild(int guid)
  {
    if(guid==this.getId())
      return this;
    
    for (int i=0; i<childList.size();i++)
    {
      GuiElement element = (GuiElement)childList.get(i);
      if(element.getId()==guid)
        return element;
    }
    
    return null;
  }

  /**
   * Check if an Element is in the hands over GroupState.<br>
   * This is usefull to handle the CLearFocus function proper. The clearFocus must show an
   * 'are you shure' dialog if a group is in the NEW or UPDATE mode.
   * 
   */
  public boolean hasChildInDataStatus(IClientContext context, IGuiElement.GroupState state) throws Exception
  {
    if(dataStatus==state)
      return true;
    
    for (int i=0; i<childList.size();i++)
    {
      if(((GuiElement)childList.get(i)).hasChildInDataStatus(context, state))
        return true;
    }
    return false;
  }

  /**
   * Return the eventhandler for the GUI Element
   * 
   * @return The Eventhandler for this GUIElement
   */
  public GuiEventHandler getEventHandler(IClientContext context)
  {
    return getEventHandler((HTTPApplication)context.getApplication(),context.getDomain(), context.getForm(),this);
  }

  private static final String BY_NAME_ROOT_CLASS_NAME = "jacob.event.screen";
  
  /**
   * For domains and forms the designer generates a single eventhandler class under
   * the following path!!!
   */
  private static final String BY_NAME_ROOT_CLASS_NAME2 = "jacob.common.gui";
  
  /**
   * Match the gui element (Form,Domain,...) to the corresponding event handler class name
   * 
   * @param app
   * @param domain
   * @param form
   * @param guid
   * @return
   */
  protected final GuiEventHandler getEventHandler(HTTPApplication app, IDomain domain, IForm form, IGuiElement element)
  {
    // Falls der Eventhandler von aussen gesetzt wrude
    //
    if (eventHandlerSetByOwnDrawGroup != null)
      return eventHandlerSetByOwnDrawGroup;

    if (eventClassName == null)
    {
      if (app.isLookupEventHandlerByReference())
      {
        eventClassName = StringUtil.toSaveString(getEventHandlerReference());
      }
      else
      {
        if (element instanceof IDomain || element instanceof IForm)
        {
          eventClassName = BY_NAME_ROOT_CLASS_NAME2 + "." + StringUtils.capitalize(element.getName());
        }
        else
        {
          IGuiElement parent = element.getParent();
          if (parent == null)
          {
            // e.g. a toolbar button. They have no form, group or domain.
            eventClassName = BY_NAME_ROOT_CLASS_NAME + "." + StringUtils.capitalize(element.getName());
          }
          else
          {
            if (element instanceof IGroup && parent instanceof IForm)
            {
              // to be on the safe side due to bug (Aufgabe 297)
              form = (IForm) element.getParent();
              domain = (IDomain) form.getParent();
            }

            eventClassName = BY_NAME_ROOT_CLASS_NAME + "." + domain.getName() + "." + form.getName() + "." + StringUtils.capitalize(element.getName());
          }
        }
      }
    }
    return (GuiEventHandler) ClassProvider.getInstance(app.getApplicationDefinition(), eventClassName);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.screen.IGuiElement#getPathName()
   */
  public final String getPathName()
  {
    if (parent != null)
    {
      StringBuffer buffer = new StringBuffer(256);
      appendParentToString(buffer);
      return buffer.toString();
    }
    return name;
  }
  
  private final void appendParentToString(StringBuffer buffer)
  {
    if (parent != null)
    {
      parent.appendParentToString(buffer);
      buffer.append(".");
    }
    buffer.append(name);
  }

  public GroupState getDataStatus()
  {
   return dataStatus; 
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.screen.IGuiElement#getGroupTableAlias()
   */
  public String getGroupTableAlias()
  {
    if (parent == null)
      return null;
    return parent.getGroupTableAlias();
  }
  
  /**
   * Returns the parent group of this UI element. This can be a TabPane if
   * the element inside a TabPane group.
   * 
   * @see #getOuterGroup
   * @return the parent group of this UI element
   */
  public IGroup getGroup()
  {
    if (parent == null)
      return null;
    return parent.getGroup();
  }

  /**
   * Returns the outer HTTPGroup element of this UI element. This is always the
   * top most group. It is never an TabPane-Group if the UI element is a child 
   * of a TabPane. 
   * 
   * @see #getGroup
   * @return the top most group (parent) of this UI element 
   */
  public IGroup getOuterGroup()
  {
    if (parent == null)
      return null;
    return parent.getOuterGroup();
  }

  public IForm getForm()
  {
    if (parent == null)
      return null;
    return parent.getForm();
  }
  
  public DataScope getDataScope()
  {
    if (parent == null)
      return null;
    return parent.getDataScope();
  }
  
  
  public void requestFocus()
  {
    getForm().setFocus(this);
  }

  /**
   * @return Returns the parent of this GUI Element.
   */
  public final IGuiElement getParent()
  {
    return parent;
  }
  
  public final void alert(IClientContext context, String messageId)
  {
    context.createMessageDialog(new CoreMessage(messageId)).show();
  }
  
	/**
	 * @return Returns the eventGUID.
	 */
	public final String getEtrHashCode()
	{
		if (null == eventGUID)
			eventGUID = getEtrHashCode(this.id);
		return eventGUID;
	}
  
  /**
   * Dieser Wert wird in HTML Form Elementen als NAME verwendet.
   * 
   * @return Returns the eventGUID.
   */
  public static final String getEtrHashCode(int id)
  {
      StringBuffer buffer = new StringBuffer(20);
      buffer.append(ID_PREFIX);
      buffer.append(id);
      return buffer.toString();
  }

  /**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("GUIElement[");
		buffer.append(", isVisible = ").append(visible);
		buffer.append(", isUserEnabled = ").append(isUserEnabled);
		buffer.append(", label = ").append(label);
    buffer.append(", name = ").append(name);
    buffer.append(", id = ").append(""+getId());
    buffer.append(", class = ").append(getClass().getName());
//		buffer.append(", children = ").append(children); // recursion!
		buffer.append("]");
		return buffer.toString();
	}
  
  /**
   * @return Returns the applicationDefinition.
   */
  public IApplicationDefinition getApplicationDefinition()
  {
    return parent.getApplicationDefinition();
  }
  
  public final static String toCSSString(Color c)
  {
    String colorR = "0" + Integer.toHexString(c.getRed());
    colorR = colorR.substring(colorR.length() - 2);
    String colorG = "0" + Integer.toHexString(c.getGreen());
    colorG = colorG.substring(colorG.length() - 2);
    String colorB = "0" + Integer.toHexString(c.getBlue());
    colorB = colorB.substring(colorB.length() - 2);
    return "#" + colorR + colorG + colorB;
  }

  public final static String toCSSString(Rectangle boundingRect) throws IOException
  {
    StringBuffer tmp = new StringBuffer();
    if(boundingRect==null)
    {
      tmp.append("height:20px;");
    }
    else
    {  
      if(boundingRect.y!=-1)
      {
        // "position:absolute" macht nur sinn wenn auch eine Position in dem Element vorhanden ist.
        tmp.append("position:absolute;");
        tmp.append("top:");
        tmp.append(Long.toString(boundingRect.y));
        tmp.append("px;");
      }
      if(boundingRect.x!=-1)
      {
        tmp.append("left:");
        tmp.append(Long.toString(boundingRect.x));
        tmp.append("px;");
      }
      if(boundingRect.width!=-1)
      {
        tmp.append("width:");
        tmp.append(Long.toString(boundingRect.width));
        tmp.append("px;");
      }
      if(boundingRect.height!=-1)
      {
        tmp.append("height:");
        tmp.append(Long.toString(boundingRect.height));
        tmp.append("px;");
      }
    }
    return tmp.toString();
  }
  
  protected final boolean isAbsolute()
  {
    return boundingRect != null && boundingRect.y != -1;
  }

  /*
   * Notify the element that the engine switchs from update/new/edit mode selected mode.
   * So - the UI Element can do any additional work. (eg. Se InformBrowsers)
   */
  public void onEndEditMode(IClientContext context) throws Exception
  {
    List children = getChildren();
    for (int i = 0; i < children.size(); i++)
    {
      ((GuiElement) children.get(i)).onEndEditMode(context);
    }
  }
  
  public final int getId()
  {
    return this.id;
  }

  public void setEventHandlerSetByMutableGroup(GuiEventHandler eventHandlerSetByOwnDrawGroup)
  {
    this.eventHandlerSetByOwnDrawGroup = eventHandlerSetByOwnDrawGroup;
  }

  
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    listeners.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    listeners.removePropertyChangeListener(listener);
  }

  public void firePropertyChange()
  {
    listeners.firePropertyChange("PROPERTY", "", "any");
  }


  public void propertyChange(PropertyChangeEvent arg0)
  {
  }
  
  /**
   * Returns the readOnly element property which is stored in the UI element definition in the application.jad. 
   * 
   * @param id
   * @return
   * @since 2.10
   */
  public  final String getProperty(String id)
  {
    if(this.definitionProperties==null)
      return null;
    
    return (String)this.definitionProperties.get(id);
  }

  /**
   *  Highlight the element with marching ant selection box.
   *  
   * @param context
   * @since 2.10
   */
  public abstract void marchingAnts(IClientContext context);
}
