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

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.NumberUtils;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.definition.guielements.ContainerDefinition;
import de.tif.jacob.core.definition.guielements.StackContainerDefinition;
import de.tif.jacob.core.definition.guielements.TabContainerDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IStackContainer;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITabPaneAction;
import de.tif.jacob.screen.event.IContainerEventHandler;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.util.FastStringWriter;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TabContainer extends GuiHtmlElement implements ITabContainer, IStackContainer
{
  static public final transient String RCS_ID = "$Id: TabContainer.java,v 1.27 2010/10/25 10:24:53 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.27 $";

  ContainerDefinition definition = null;
  
  GuiHtmlElement currentVisibleGroup= null;
  private int currentPaneIndex = -1;
  String stripCSS = null;
  private boolean tabStripIsHidden=false;
  
  protected TabContainer(IApplication app, TabContainerDefinition container)
  {
    super(app, container.getName(), null, container.isVisible(), container.getRectangle(), container.getProperties());
    definition = container;
    StringBuffer tmp=new StringBuffer(1024);
    tmp.append("position:absolute;top:");
    tmp.append(Long.toString(boundingRect.y));
    tmp.append("px;left:");
    tmp.append(Long.toString(boundingRect.x));
    tmp.append("px;");

    tmp.append("width:");
    tmp.append(Long.toString(boundingRect.width));
    tmp.append("px;");
    tmp.append("height:20px;");
    stripCSS = tmp.toString();

    boundingRect.height = boundingRect.height-20;
    boundingRect.y      = boundingRect.y+20;
    
    this.backgroundColor = container.getBackgroundColor();
    this.borderColor = container.getBorderColor();
    this.borderWidth = container.getBorderWith();
  }
  
  protected TabContainer(IApplication app, StackContainerDefinition container)
  {
    super(app, container.getName(), null, container.isVisible(), container.getRectangle(), container.getProperties());
    definition = container;
    stripCSS="";
    
    this.backgroundColor = container.getBackgroundColor();
    this.borderColor = container.getBorderColor();
    this.borderWidth = container.getBorderWith();
    
    hideTabStrip(true);
  }

  public void addChild(IGuiElement child)
  {
    super.addChild(child);
    ((GuiElement)child).addPropertyChangeListener(this);
  }


  public int getTabIndex()
  {
    return definition.getTabIndex();
  }
  
  
  protected void addDataFields(Vector fields)
  {
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement)iter.next();
      element.addDataFields(fields);
    }
  }
 
  public List getPanes()
  {
    List result = new ArrayList();
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement)iter.next();
      if(element instanceof ITabPane)
        result.add(element);
    }
    return result;
  }
 
  public void hideTabStrip(boolean flag)
  {
    this.tabStripIsHidden = flag;
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement)iter.next();
      if(element instanceof ITabPane)
        ((ITabPane)element).setBorder(!flag);
    }
  }

  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    // the TabContainer itself is the target of the event
    //
    if(guid==this.getId())
    {
      // the one and only event ist the tab switch (at the moment)
      //
      setActivePane(context, NumberUtils.stringToInt(value,0));
      return true;
    }

    return super.processEvent(context, guid,event, value);
  }

  /**
   * @deprecated use the method with the context as additional param
   */
  public void setActivePane(int index)
  {
    try
    {
      this.setActivePane((IClientContext)Context.getCurrent(),index);
    }
    catch(Exception e)
    {
      ExceptionHandler.handle(e);
    }
  }
  
  public void setActivePane(IClientContext context, int index) throws Exception
  {
    currentVisibleGroup = (GuiHtmlElement)getChildren().get(index);
    this.currentPaneIndex = index;
    
    // eine aktive gruppe/pane muss auch sichtbar sein
    currentVisibleGroup.setVisible(true);
    
    resetCache();
  }

  /**
   * @since 2.8.0
   * @param context
   * @param pane
   * @throws Exception
   */
  public void setActivePane(IClientContext context, ITabPane pane) throws Exception
  {
    int index = getChildren().indexOf(pane);
    if(index>=0)
    {
     this.currentPaneIndex = index;
     currentVisibleGroup = (GuiHtmlElement)pane;
     
     // eine aktive gruppe/pane muss auch sichtbar sein
     currentVisibleGroup.setVisible(true);
     
     resetCache();
    }
    else
    {
      throw new Exception("TabPane '" + pane.getPathName() + "' is not child of the container [invalid call of setActivePane]");
    }
  }

  /**
   * @since 2.8.0
   * @param name
   * @return
   */
  public ITabPane getPane(String name)
  {
    Iterator iter = getChildren().iterator();
    while (iter.hasNext())
    {
      IGuiElement pane = (IGuiElement) iter.next();
      if(pane.getName().equals(name))
        return (ITabPane)pane;
    }
    return null;
  }

  /**
   * @since 2.8.0
   */
  public ITabPane getPane(int index)
  {
    return (ITabPane)getChildren().get(index);
  }
  
  public ITabPane getActivePane()
  {
    return (ITabPane)currentVisibleGroup;
  }

  public int getActivePaneIndex()
  {
    return this.currentPaneIndex;
  }

  /**
   * Fehler der Kinder an den Container weiter leiten.
   * 
   * @since 2.10
   */
  public boolean hasError()
  {
    Iterator iter = getPanes().iterator();
    while (iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement) iter.next();
      if (element.hasError())
        return true;
    }
    return false;
  }


  /**
   * Return HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    // Jetzt wird erstmal geprï¿½ft welche Gruppe (Tab) angezeigt werden soll
    // Falls alle visible==true haben werden diese erstmal auf false gesetzt
    // und der Erste ist dann visible==true
    // 
    if(currentVisibleGroup==null)
    {
      currentVisibleGroup=(GuiHtmlElement)getChildren().get(0);
      currentPaneIndex = 0;
    }
    
    // wichtig: erst die Kinder berechnen und dann den TabContainer.
    // Grund: Die Darstellung des TabStrip ist Abhängig von dem Ergebniss der
    //        Berechnungen der Kinder. (Stichwort: ErrorDecoration).
    super.calculateHTML(context);
    
    if(getCache()==null)
    {
      Writer cache = newCache();
      FastStringWriter w = new FastStringWriter();
      
      w.write("<ul style=\"");
      w.write(stripCSS);
      w.write("\" class=\"tabStrip\">");
      Iterator iter = getChildren().iterator();
      int index=0;
      int visibleCounter = 0;
      while(iter.hasNext())
      {
        Group pane = (Group)iter.next();
        // don't write any Tab if the element are not visible
        if(pane.isVisible())
        {
          visibleCounter++;
          // need additional "div" inside the "li" tag to work easy with background images in the CSS
          //
          if(pane==currentVisibleGroup)
          {
            w.write("<li class=\"tab_selected\" >");
          }
          else
          {
            // the TAB isn't clickable if enable==false
            if(pane.isEnabled())
            {
              w.write("<li class=\"tab\"  onClick=\"FireEventData('");
              w.write(Integer.toString(getId()));
              w.write("','click','"+Integer.toString(index)+"')\">" );
            }
            else
            {
              w.write("<li class=\"tab_disabled\" >" );
            }
          }
          if(pane.hasError())
            w.write("<div class=\"error\">");
          else
            w.write("<div>");
            
          w.write(StringUtil.htmlEncode(I18N.localizeLabel(pane.getLabel(), context)));
          
          Iterator actionIter =pane.getActions().iterator();
          boolean firstAction=true;
          while(actionIter.hasNext())
          {
            ITabPaneAction action =(ITabPaneAction) actionIter.next();
            if(firstAction)
            {
              w.write("<img class=\"tab_action_first\" ");
            }
            else
            {
              w.write("<img class=\"tab_action\" ");
            }
            w.write(" title=\"");
            w.write(action.getTooltip(context));
            w.write("\" onClick=\"FireEventData('");
            w.write(Integer.toString(pane.getId()));
            w.write("','"+Group.TAB_ACTION_EVENT+"','");
            w.write(action.getId());
            w.write("')\" src=\"");
            w.write(action.getIcon(context).getPath(true));
            w.write("\">");
            firstAction=false;
          }
          w.write("</div>");
        }
        index++;
      }
      w.write("</ul>\n");
      
      // Falls mind. ein TAB sichtbar ist wird die TabPane rausgeschrieben
      // ansonsten ist diese versteckt.
      //
      if(visibleCounter>0 && tabStripIsHidden==false)
        w.writeTo(cache);
      
      // write the decoration
      // Wird immer für SpotLight benötigt
//      if(this.borderColor!=null || this.backgroundColor !=null )
      {
        w = new FastStringWriter();
        w.write("<div style=\"");
        getCSSStyle(context, w,boundingRect);
        w.write("\" id=\"");
        w.write(getEtrHashCode());
        w.write("\"></div>");
        w.writeTo(cache);
      }
    }
  }
  
  
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    writeCache(w);
    if(currentVisibleGroup!=null)
      currentVisibleGroup.writeHTML(context,w);
  }


  /* 
   * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context,GroupState groupStatus)  throws Exception
  {
  // Nicht den Status der übergeordneten Grupp an die KinderGruppen (TabPanes) weiter
  // leiten. Diese berechnen Ihren Status selber anhand der Datenschicht.
  // Hinweis: Falls benötigt muss das Event onOuterGroupStatusChanged genutzt werden.
//    super.onGroupDataStatusChanged(context, groupStatus);
    
    // Den Status nur an die untergeordnete Pane weiter leiten wenn diese den selben Alias hat.
    // Der DataStatus muss so früh wie möglich an alle Elemente propagiert werden damit diese
    // sich korrekt darstellen können. 
    // Es sind zwar kinder eines TabPane => onOuterGroupStatus ABER es ist der gleiche Alias
    // und somit ändert sich auch der Status des Pane
    List panes = getPanes();
    for (int i=0; i<panes.size();i++)
    {
      Group pane = (Group)panes.get(i);
      if(pane.getPaneTableAlias().equals(this.getGroupTableAlias()))
        pane.onGroupDataStatusChanged(context, groupStatus);
    }

    IContainerEventHandler obj = (IContainerEventHandler)getEventHandler(context);
    if(obj!=null)
      obj.onGroupStatusChanged(context,groupStatus, this);
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }


  public void propertyChange(PropertyChangeEvent arg0)
  {
    // Falls sich von einem Kind ein Property geändert hat, dann muss
    // von dem TabStrip der Cache gelöscht werden.
    // Grund: Der TabStrip hat je nach FehlerDecoration seiner Kinder einen anderen Style
    //
    resetCache();
    
    // und natürlich muss der übergeordnet TabContainer darüber informiert werden
    // Grund: Es sollen z.B. ErrorDecoration auch recursive nach oben propagiert werden
    ((GuiElement)this.getParent()).firePropertyChange();
  }
  

  /**
   * 
   */
  public void setInputFieldValue(String fieldName, String value) throws Exception
  {
    GuiHtmlElement element = (GuiHtmlElement) findByName(fieldName);
    if (element == null || !(element instanceof ISingleDataGuiElement))
      throw new NoSuchFieldException("Unable to find GUI input field [" + fieldName + "] in group [" + getPathName() + "]");
    ISingleDataGuiElement input = (ISingleDataGuiElement) element;
    input.setValue(value);
  }


  public void setInputFieldValue(String fieldName, java.util.Date value) throws Exception
  {
    String strValue = I18N.toFullDatetimeString(Context.getCurrent().getLocale(), value);
    setInputFieldValue(fieldName, strValue);
  }

  public void setInputFieldValue(String fieldName, BigDecimal value) throws Exception
  {
    String strValue = I18N.toString(value, Context.getCurrent().getLocale(), IDataRecord.DEFAULT_STYLE);
    setInputFieldValue(fieldName, strValue);
  }

  public void setInputFieldValue(String fieldName, Double value) throws Exception
  {
    String strValue = I18N.toString(value, Context.getCurrent().getLocale(), IDataRecord.DEFAULT_STYLE);
    setInputFieldValue(fieldName, strValue);
  }

  public void setInputFieldValue(String fieldName, Float value) throws Exception
  {
    String strValue = I18N.toString(value, Context.getCurrent().getLocale(), IDataRecord.DEFAULT_STYLE);
    setInputFieldValue(fieldName, strValue);
  }

  /**
   * 
   */
  public String getInputFieldValue(String fieldName) throws Exception
  {
    GuiHtmlElement element = (GuiHtmlElement) findByName(fieldName);
    if (element == null || !(element instanceof ISingleDataGuiElement))
      throw new NoSuchFieldException("Unable to find GUI input field [" + fieldName + "] in group [" + getPathName() + "]");
    ISingleDataGuiElement input = (ISingleDataGuiElement) element;
    return input.getValue();
  }
}
