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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.definition.IFormDefinition;
import de.tif.jacob.core.definition.impl.AbstractGuiElement;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IFormEventHandler;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;
import de.tif.jacob.screen.impl.BrowserActionShow;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.HTTPForm;
import de.tif.jacob.screen.impl.HTTPFormChild;
import de.tif.jacob.screen.impl.IDProvider;
import de.tif.jacob.util.FastStringWriter;



/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Form extends GuiHtmlElement implements HTTPForm
{
  static public final transient String RCS_ID = "$Id: Form.java,v 1.30 2010/11/18 11:26:04 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.30 $";

  HashMap       searchBrowsers       = new HashMap();
  List          searchBrowserTabKeys  = new ArrayList();
  SearchBrowser currentSearchBrowser = null;
  IGuiElement   currentElementWithInputFocus = null;
  int           currentElementCaretPosition  = 0;
  private boolean hideTabStrip = false;
  
  // all data fields in the form.
  // ONLY VALID/REQUIRED if the scope of the DataAccess=="form"
  private DataField[] formDataFields = null;
  
  IFormDefinition definition = null;
  private IDataAccessor dataAccessor = null;
  private IFormGroup formGroup;

  final int scrollPosXId = IDProvider.next(); 
  final int scrollPosYId = IDProvider.next(); 
  Integer scrollPosX = new Integer(0);
  Integer scrollPosY = new Integer(0);
  
  /**
   * Constructor for the castor definition
   * 
   * @param parent
   * @param form
   */
  protected Form(IApplication app,  IFormDefinition definition)
  {
    super(app,  definition.getName(), definition.getLabel(), definition.isVisible(), null, definition.getProperties());
    
    this.definition = definition;
    hideTabStrip = definition.hideSearchBrowserTabStrip();
  }

  protected Form(IApplication app, String name, String label)
  {
    super(app, name, label, true, null, Collections.EMPTY_MAP);
  }


  /**
   * The Form has only Groups as child. It's ok to throw a ClassCastException in the other case
   *  
   */
  public void addChild(IGuiElement child)
  {
    // call the super implementation to do the normal stuff
    //
    super.addChild(child);

    // add the SearchBrowser of the group to the top in the screen (Form).
    //
    HTTPFormChild provider = (HTTPFormChild)child;
    Iterator browserIter = provider.getBrowsers().iterator();
    while(browserIter.hasNext())
    {
      IBrowser browser = (IBrowser)browserIter.next();
      searchBrowsers.put(new Integer(browser.getId()),browser);
    }
    
    // die Reihenfolge der Tabs werden durch die Y-Koordinate der Gruppe bestimmt.
    // Der Tab der obersten Gruppe ist ganz Links. 
    //
    List zOrderGroups = new ArrayList(getChildren());
    Collections.sort(zOrderGroups,new Comparator()
    {
      public int compare(Object arg0, Object arg1)
      {
        return ((Group)arg0).getBoundingRect().y-((Group)arg1).getBoundingRect().y;
      }
    });
    searchBrowserTabKeys = new ArrayList();
    Iterator iter = zOrderGroups.iterator();
    while (iter.hasNext())
    {
      Group obj = (Group)iter.next();
      searchBrowserTabKeys.add(new Integer(obj.getBrowser().getId()));
    }
    currentSearchBrowser = (SearchBrowser)searchBrowsers.get(searchBrowserTabKeys.get(0));
  }


  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    ((ClientContext)context).setForm(this);
    // this event has 'this' as target
    //
    if(guid==this.getId())
    {
      // handle the 'show' event of an form
      //
      if(event.equals("show"))
      {
        HTTPForm oldForm = (HTTPForm)context.getDomain().getCurrentForm(context);
        if(oldForm!=this)
        {
          context.getDomain().setCurrentForm(context,this);
        }
      }
      return true;
    }
    // a searchBrowser has send an event
    //
    else if(searchBrowsers.containsKey(new Integer(guid)))
    {
      SearchBrowser browser = (SearchBrowser)searchBrowsers.get(new Integer(guid));
      // prepare the group for the search browser. The SearchBrowser need the correct group
      // for the event handling
      //
      ((ClientContext)context).setGroup((Group)browser.getRelatedGroup());

      // send the event to the SearchBrowser
      //
      if(browser.processEvent(context, guid, event,value)==true)
        return true;
    }
    // send the event to the child elements
    //
    else
    {
      Iterator iter = getChildren().iterator();
      while(iter.hasNext())
      {
        HTTPFormChild f = (HTTPFormChild)iter.next();
        if(f.processEvent(context,guid,event,value))
          return true;
      }
    }
    return false;
  }

  
  /**
   * The default implementation transfer the data from the event to the GUI guid.
   * (this is not usefull for check boxes!)
   * 
   */
  public boolean processParameter( int guid, String data) throws IOException, NoSuchFieldException
  {
    if(scrollPosXId==guid)
    {  
      Integer newScrollPos =Integer.valueOf(data);
      if(!scrollPosX.equals(newScrollPos))
      {  
        resetCache();
        scrollPosX=newScrollPos;
      }
      return true;
    }
    else if(scrollPosYId==guid)
    {  
      Integer newScrollPos =Integer.valueOf(data);
      if(!scrollPosY.equals(newScrollPos))
      {  
        resetCache();
        scrollPosY=newScrollPos;
      }
      return true;
    }
    
    
    if(super.processParameter( guid, data)==true)
      return true;
    
    if(currentSearchBrowser!=null && currentSearchBrowser.processParameter(guid,data))
      return true;
    
    return false;
  }

  /**
   * Clear/Reset the GUI Element to the default behaviour.
   * 
   * The default implementation calls all child to clear it.
   */
  public void clear(IClientContext context) throws Exception
  {
    // WICHTIG:
    // Falls in eine Script/Hook context.clearDomain(..) aufgerufen wird, darf sich die Gruppe/Form/Focus
    // NICHT veraendern. Derzeitige Form merken und am Ende der Funktion wieder restaurieren
    //
    HTTPForm current=(HTTPForm)context.getForm();
    
    ((ClientContext)context).setForm(this);
    
    if(dataAccessor!=null)
      dataAccessor.clear();
    
    super.clear(context);
    resetTabOrder(); 
   
    ((ClientContext)context).setForm(current);
  }

  public boolean isDirty(IClientContext context) throws Exception
  {
    return this.hasChildInDataStatus(context, IGuiElement.UPDATE) || this.hasChildInDataStatus(context, IGuiElement.NEW);
  }

  /**
   * Returns the data domain (with cache...) of the current user guiBrowser window.
   * FOR INTERNAL USE ONLY
   * @return
   */
  public IDataAccessor getDataAccessor()
  {
    if(dataAccessor==null/* && parent instanceof HTTPApplication*/)
      dataAccessor = new DataAccessor(getApplicationDefinition());
    return dataAccessor;
  }
  
  /**
   * Find the group with the element with the lowest TabIndex and set the focus to this element
   * 
   */
  public void resetTabOrder()
  {
    SingleDataGUIElement focusElement=null;
    for (int i=0; i<getChildren().size();i++)
    {
      Object obj = getChildren().get(i);
      if(obj instanceof HTTPFormChild)
      {
        HTTPFormChild input = (HTTPFormChild)obj;
        SingleDataGUIElement groupInput=(SingleDataGUIElement)input.getFirstElementInTabOrder();
        if(focusElement==null)
          focusElement=groupInput;
        else if(groupInput!=null && focusElement.getTabIndex()>input.getFirstElementInTabOrder().getTabIndex())
          focusElement=groupInput;
      }
    }
    setFocus(focusElement);
  }
  
  
  
 
  public void onShow(IClientContext context) throws Exception
  {
    GuiEventHandler handler = getEventHandler(context);
    if(handler instanceof IFormEventHandler)
      ((IFormEventHandler)handler).onShow(context,this);
    
    for (int i=0; i<getChildren().size();i++)
    {
      Object obj = getChildren().get(i);
      if(obj instanceof HTTPFormChild)
        ((HTTPFormChild)obj).onShow(context);
    }
  }
  
  
  public void onHide(IClientContext context) throws Exception
  {
    GuiEventHandler handler = getEventHandler(context);
    if(handler instanceof IFormEventHandler)
      ((IFormEventHandler)handler).onHide(context,this);
    
    for (int i=0; i<getChildren().size();i++)
    {
      Object obj = getChildren().get(i);
      if(obj instanceof HTTPFormChild)
        ((HTTPFormChild)obj).onHide(context);
    }
  }

  /**
   * Return HTML representation of this object
   * 
   */
  public void calculateHTML(de.tif.jacob.screen.impl.html.ClientContext context) throws Exception
  {
    context.setForm(this);
    
    if(getCache()==null)
    {  
      Writer w = newCache();
      
      w.write("<script type=\"text/javascript\">\nfunction markScrollPosForm(divElement){\n");
      w.write("getObj('"+ID_PREFIX+scrollPosYId+"').value=divElement.scrollTop;\n");
      w.write("}</script>\n");
      w.write("<input type=\"hidden\" id=\""+ID_PREFIX+scrollPosYId+"\" name=\""+ID_PREFIX+scrollPosYId+"\" value=\""+scrollPosY.toString()+"\">\n");
      w.write("<input type=\"hidden\" id=\"formScrollPos\" name=\"formScrollPos\" value=\""+scrollPosY.toString()+"\">\n");
    }
    super.calculateHTML(context);
  }
  
  /** 
   * ATTENTION: This implementation differs from the super implementation. See the sequenze of the calls
   *            FIRST write me than the children!!!!
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    context.setForm(this);
    super.writeHTML(context, w);
    writeCache(w);
    if (getCurrentElementWithInputFocus() != null)
      context.addOnLoadJavascript("setFocus('"+((GuiHtmlElement)getCurrentElementWithInputFocus()).getEtrHashCode()+"'," +getCurrentElementCaretPosition() + ");");
  }

  /**
   * Return HTML representation of this object
   * 
   */
  public void renderSearchBrowserHTML(de.tif.jacob.screen.impl.html.ClientContext context) throws Exception
  {
    if(currentSearchBrowser.isVisible()==false)
    {
      // try to find a alternative browser to show. 
      // Reason: It's very ugly if the tab-strip is visible with different browser tabs and no
      // content will be shown.
      Iterator iter = this.searchBrowsers.values().iterator();
      while (iter.hasNext())
      {
        SearchBrowser browser = (SearchBrowser) iter.next();
        if(browser.isVisible())
        {
          currentSearchBrowser = browser;
          break;
        }
      }
    }
    
    // unable to find any visible browser...shit happens.
    if(currentSearchBrowser.isVisible()==false)
      return;
    
    ((GuiHtmlElement)currentSearchBrowser).calculateHTML(context);
    ((GuiHtmlElement)currentSearchBrowser).writeHTML(context, context.out);
  }

  /**
   * Return HTML representation of this object
   * 
   */
  public void renderSearchBrowserTabHTML(de.tif.jacob.screen.impl.html.ClientContext context) throws Exception
  {
    if(this.hideTabStrip==true)
      return;
    
    int counter =0;
    Iterator iter = searchBrowserTabKeys.iterator();
    FastStringWriter sw = new FastStringWriter();
    while(iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement)searchBrowsers.get(iter.next());
      if(element.isVisible())
      {
        counter++;
        if(currentSearchBrowser!=null && currentSearchBrowser==element)
          sw.write("<a href='#' class=\"downtabSelected\" ><div><em>"+element.getI18NLabel(context.getLocale())+"</em></div></a><div class=\"downtabFiller\">&nbsp;</div>");      
        else
          sw.write("<a href='javascript:FireEvent(\""+element.getId()+"\",\""+BrowserActionShow.ACTION_ID+"\");' class=\"downtab\"><div><em>"+element.getI18NLabel(context.getLocale())+"</em></div></a><div class=\"downtabFiller\">&nbsp;</div>");
      }
    }

    // Wenn nur ein Browser sichtbar ist wird kein Tab rausgeschrieben.
    //
    if(counter>1)
      sw.writeTo(context.out);
  }

  
  public void hideTabStrip(boolean flag)
  {
    this.hideTabStrip = flag;
  }

  
  public boolean hasVisibleSearchBrowser()
  {
    Iterator iter = searchBrowserTabKeys.iterator();
    while(iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement)searchBrowsers.get(iter.next());
      if(element.isVisible())
        return true;
    }
    return false;
  }
  
  /*
   *  
   * @see de.tif.jacob.screen.IDomain#getGuiRepresentations(java.lang.String, java.lang.String)
   */
  public List getGuiRepresentations(String tableAlias, String fieldName)
  {
    List result =new ArrayList();
    
    for(int i=0; i<getDataFields().length;i++)
    {
      DataField field=getDataFields()[i];
      if(field.getTableAlias().getName().equals(tableAlias) && field.getField().getName().equals(fieldName))
        result.add(field.getParent());
    }
    return result;
  }
  
  /**
   * The main Domain calles this method to retrieve all relevant DataFields. The domain
   * doesn't call this method twice.
   * 
   * This is only called onece per Domain instance.
   */
  protected void addDataFields(Vector fields)
  {
    for(int i =0;i<getChildren().size();i++)
    {
      HTTPFormChild group = (HTTPFormChild)getChildren().get(i);
      group.addDataFields(fields);
    }
  }
  
  /**
   * The method collect all DataFields of it childs and return them.
   * 
   * @return All DataFields in this domain.
   * 
   */
  public DataField[] getDataFields()
  {
    if(formDataFields==null)
    {
      Vector tmp = new Vector();
      addDataFields(tmp);
      formDataFields = new DataField[tmp.size()];
      tmp.copyInto(formDataFields);
    }
    
    return formDataFields;
  }
  
 
  /*
   *  
   * @see de.tif.jacob.screen.IForm#setInputFieldValue(java.lang.String, java.lang.String)
   */
  public void  setInputFieldValue(String fieldName, String value) throws Exception
  {
    GuiHtmlElement element=(GuiHtmlElement)findByName(fieldName);
    if(element==null || !(element instanceof ISingleDataGuiElement))
      throw new NoSuchFieldException("Unable to find GUI input field ["+fieldName+"] in form ["+getPathName()+"]");
    ISingleDataGuiElement input=(ISingleDataGuiElement)element;
    input.setValue(value);
  }
  

  /*
   *  
   * @see de.tif.jacob.screen.IForm#getInputFieldValue(java.lang.String)
   */
  public String getInputFieldValue(String fieldName) throws Exception
  {
    GuiHtmlElement element=(GuiHtmlElement)findByName(fieldName);
    if(element==null || !(element instanceof ISingleDataGuiElement))
      throw new NoSuchFieldException("Unable to find GUI input field ["+fieldName+"] in form ["+getPathName()+"]");
    ISingleDataGuiElement input=(ISingleDataGuiElement)element;
    return input.getValue();
  }

  /**
   * Jedes GUI Element kann so seine zugeh�rige Gruppe ermitteln.
   */
  public IForm getForm()
  {
    return this;
  }
  /**
   * set the handover search guiBrowser as the active/visible guiBrowser
   * 
   * @param guiBrowser
   */
  public void setCurrentBrowser(IBrowser browser)
  {
    currentSearchBrowser =(SearchBrowser) browser; 
    resetCache();
  }

  public IBrowser getCurrentBrowser()
  {
    return currentSearchBrowser; 
  }
  
  /**
   * @return Returns the currentElementWithInputFocus.
   */
  public IGuiElement getCurrentElementWithInputFocus()
  {
    return currentElementWithInputFocus;
  }

  /**
   * @param currentElementWithInputFocus The currentElementWithInputFocus to set.
   */
  public void setFocus(IGuiElement element)
  {
    setFocus(element,0);
  }

  /**
   * @param currentElementWithInputFocus The currentElementWithInputFocus to set.
   */
  public void setFocus(IGuiElement element, int cursorPosition)
  {
    this.currentElementWithInputFocus = element;
    this.currentElementCaretPosition  = cursorPosition; 
  }

  public void setFocus(IInFormLongText element, int caretPosition)
  {
    this.setFocus((IGuiElement)element, caretPosition);
  }

  public void setFocus(IText element, int caretPosition)
  {
    this.setFocus((IGuiElement)element, caretPosition);
  }

  /**
   * @return Returns the currentElementCaretPosition.
   */
  public synchronized int getCurrentElementCaretPosition()
  {
    return currentElementCaretPosition;
  }

  public String getEventHandlerReference()
  {
    return ((AbstractGuiElement)definition).getEventHandler();
  }

  public void setFormGroup(IFormGroup group)
  {
    this.formGroup = group;
    
  }
  
  public IFormGroup getFormGroup()
  {
    return this.formGroup;
  }
  
  public INavigationEntry getNavigationEntry(IClientContext context)
  {
    return new INavigationEntry(this,getEtrHashCode(), getI18NLabel(context.getLocale()),"show","unused");
  }
  
}
