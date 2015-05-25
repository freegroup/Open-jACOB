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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.impl.AbstractDomainDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IDomainEventHandler;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationPanel;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPForm;
import de.tif.jacob.screen.impl.HTTPNavigationEntryProvider;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Domain extends GuiHtmlElement implements HTTPDomain 
{
  static public final transient String RCS_ID = "$Id: Domain.java,v 1.29 2011/01/03 17:11:25 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.29 $";

  private final IDomainDefinition definition;
  private HTTPForm currentForm = null; // the current visible form
  private INavigationEntry[] entries=null;
  private int   currentEntryIndex=-1;
  
  private DataField[] domainDataFields = null; // all data fields in the forms
  private IDataAccessor dataAccessor = null;
  private int scrollPos=0;
  public  String browserId;
  
  // Eine Domain hat die Möglichkeit FormGroups als Gruppierung für Formen zu verwenden
  // (optional)
  public List formGroups = new ArrayList();
  
  // Falls eine Domain das Flag canCollapse" hat kann der Benutzer in manchen Themes
  // die Domain "zusammenklappen". Die formen sind dann darin nicht sichtbar.
  // Falls der Benutzer dies getan hat wird dies in diesem Flag widergespiegelt.
  // (null = User hat ix getan).
  private Boolean isOpen; 
  
  /**
   * Initial constructor with the castor definition
   * 
   * @param parent
   * @param childs
   * @param name
   * @param name
   */
  protected Domain(IApplication app, IDomainDefinition domain)
  {
    super(app, domain.getName(), domain.getTitle(), true, null, domain.getProperties());

    definition = domain;
    this.setVisible(definition.isVisible());
  }
  

  /**
   * Undocumented feature for invisible domains to use its forms as dialogs.<br>
   * See task 72: Unterstützung von unsichtbaren Domains (undocumented feature)
   */
//  public boolean isVisible()
//  {
//    return this.definition.isVisible();
//  }

  public boolean getCanCollapse()
  {
    return this.definition.canCollapse();
  }
  
  public void addChild(IGuiElement child)
  {
    super.addChild(child);
    if((child instanceof HTTPForm) && currentForm==null)
      currentForm = (HTTPForm)child;
  }

  public void addFormGroup(IFormGroup formGroup)
  {
    super.addChild(formGroup);
    this.formGroups.add(formGroup);
    ((FormGroup)formGroup).setParent(this);
  }
  
  public List getFormGroups()
  {
    return this.formGroups; 
  }
  
  
  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    boolean result = false;
    ((ClientContext)context).setDomain(this);
    
    // Falls es sich um ein NavigationEntry handelt, dann muss das Event an
    // den Emitter dispatched werden
    //
    if(entries==null)
    	getNavigationEntries(context);
    for(int i=0;i<entries.length;i++)
    {
    	if(entries[i].hashCode()==guid)
    	{
    		guid = entries[i].emitter;
    		currentEntryIndex = i;
    		break;
    	}
    }
    
		// send the event to the child elements, and this elements sends the event 
    // to here children.
		//
    if(guid==this.getId())
    {
      if(event.equals("show"))
      {
        ((ClientContext)context).setForm(currentForm);
        context.getApplication().setActiveDomain(context,this);
      }
      else // it must be an user defined navigation event
      {
        GuiEventHandler obj = getEventHandler(context);
        if(obj instanceof IDomainEventHandler)
        {
          IDomainEventHandler handler = (IDomainEventHandler)obj;
          handler.onNavigation(context,this,event,value);
        }
      }
      result= true;
    }
    else if(super.processEvent(context, guid,event,value))
    {
      // Da hat sich ein Kind angesprochen gefühlt (z.B eine Form mit dem Event 'show') und ich bin 
      // nicht die active Domain.
      // Dies kann passieren wenn die Domain das Flag canCollapse==false gesetzt hat.
      // Dann sind immer alle Formen der Domain sichbar und anklickbar obwohl die Domain nicht 
      // gerade aktive ist!!!!! Der Event kann dann nur von so einer sichtbaren Form kommen.
      // ....naja oder in dem Event wurde die aktive Domain verändert....via Hook
      //
      if(((Application)context.getApplication()).getActiveDomain()!=this)
      {
        GuiElement element = this.findChild(guid);
        if(element instanceof HTTPForm)
        {
          this.currentForm = (HTTPForm)element;
          ((ClientContext)context).setForm(currentForm);
          context.getApplication().setActiveDomain(context,this);
        }
      }
      
      result= true;
    }
    
    // maybe the event comes from the screen divider
    //
    return result;
  }

  /**
   * The default implementation transfer the data from the event to the GUI guid.
   * (this is not usefull for check boxes!)
   * 
   */
  public boolean processParameter( int guid, String data) throws IOException, NoSuchFieldException
  {
    if(guid==this.getId())
    {  
      int newScrollPos =Integer.parseInt(data);
      if(newScrollPos!=scrollPos)
      {  
       // resetCache();
        scrollPos=newScrollPos;
      }
      
      return true;
    }

    if(((GuiHtmlElement)currentForm).processParameter( guid, data))
      return true;
    
    return false;
  }
  

  public void setCurrentFormWithoutEvent(HTTPForm currentForm)
  {
    this.currentForm = currentForm;
  }
  
  /**
   * 
   * @return
   */
  public IForm getCurrentForm(de.tif.jacob.screen.IClientContext context)
  {
    return currentForm;
  }

  public void setCurrentForm(de.tif.jacob.screen.IClientContext context, IForm form) throws Exception
  {
    if (form == null)
      throw new NullPointerException("setCurrentForm(....,IForm null) is not allowed. You must hand over a valid form.");

    if (currentForm == form)
      return;
    
    Application app = (Application)context.getApplication();
    
    // Nur onHide() aufrufen wenn die neue Domain auch die gerade angezeigte ist.
    // Ansonsten würd für die Form 2 mal onHide aufgerufen, da ja bei dem Wechsel der Domain
    // schon form.onHide aufgerufen wurde.
    //
    if (currentForm != null && app.getActiveDomain()==this && app.isInitDone())
    {
      ((ClientContext)context).setForm(currentForm);
      currentForm.onHide(context);
    }
    
    currentForm = (HTTPForm)form;
    ((ClientContext)context).setForm(currentForm);

    // Falls eventuell programatisch die Form gewechselt wird, dann muss 
    // der korrekte Index des NavigationEntry bestimmt werden.
    //
    if(entries==null)
      getNavigationEntries(context);
    for(int i=0;i<entries.length;i++)
    {
      if(entries[i].emitter==form.getId())
      {
        currentEntryIndex = i;
        break;
      }
    }
    
    // Falls die Domain nicht die gerade Aktive Domain ist, dann wird diese Aktiv geschaltet
    // Dies ruft dann an der Domain onShow auf und dies ruft dan auch von der Form.onShow.
    // Ansonsten wird nur Form.onShow aufgerufen (aber auch nur wenn die Applikation vollständig initalisiert wurde)
    //
    if(app.getActiveDomain()!=this)
      app.setActiveDomain(context, this);
    else if(app.isInitDone())
      currentForm.onShow(context);
  }


  public void onShow(IClientContext context) throws Exception
  {
    GuiEventHandler handler = getEventHandler(context);
    if(handler instanceof IDomainEventHandler)
      ((IDomainEventHandler)handler).onShow(context,this);
    
    if(currentForm!=null)
      currentForm.onShow(context);
  }
  
  
  public void onHide(IClientContext context) throws Exception
  {
    GuiEventHandler handler = getEventHandler(context);
    if(handler instanceof IDomainEventHandler)
      ((IDomainEventHandler)handler).onHide(context,this);
    
    if(currentForm!=null)
      currentForm.onHide(context);
  }

  /**
   * Clear/Reset the GUI Element to the default behaviour.
   * 
   * The default implementation calls all child to clear it.
   */
  public void clear(IClientContext context) throws Exception
  {
    // Falls in eine Script/Hook context.clearForm(..) aufgerufen wird, darf sich die Gruppe/Form/Focus
    // NICHT verändern. Derzeitige Domain merken und am ende der Funktion wieder restaurieren
    //
    IDomain current=context.getDomain();
    
    ((ClientContext)context).setDomain(this);
    getDataAccessor().clear();
    super.clear(context);
    
    ((ClientContext)context).setDomain(current);
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
   * Write the HTML stream of the domain and the current visible form.
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    // display the form selection area if an domain is selected
    //
    context.setDomain(this);
    
    // Es müssen alle Formen berechnet werden (auch unsichtbare) da diese Werte
    // halten können, obwohl diese veraltet sind. Das calculateHTML schaut in die
    // gelinkten TableAliase und resetet eventuell die entsprechenden DataFields!!!!
    // SEHR wichtig!!!
    //
    // Anpassung: Ist nur wichtig wenn die Formen sich einen Datenaccessor teilen.
    //            Wenn jede Form Ihren eigenen DatenAccessor hat macht dies kein Sinn
    //
    if (getDataScope() == DataScope.FORM)
    {
      ((GuiHtmlElement) this.currentForm).calculateHTML(context);
    }
    else
    {
      Iterator iter = getChildren().iterator();
      while(iter.hasNext())
      {
        GuiHtmlElement obj=(GuiHtmlElement)iter.next();
        obj.calculateHTML(context);
      }
    }
    
    if(getCache()==null)
    {  
    	Writer w = newCache();
      w.write("<script type=\"text/javascript\">\nfunction markScrollPosDomain(divElement){\n");
      w.write("getObj('"+getEtrHashCode()+"').value=divElement.scrollTop;\n");
      w.write("getObj('domainScrollPos').value=divElement.scrollTop;\n}\n");
      w.write("</script>\n");

    }
  }
  
  /**
   * 
   */
  public void calculateIncludes(ClientContext context)
  {
    if(this.currentForm instanceof Form)
      ((Form)this.currentForm).calculateIncludes(context);
  }
  
  /** 
   * Writes the HTML content to the stream
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
  	context.setDomain(this);
    if(currentForm!=null)
      ((GuiHtmlElement)currentForm).writeHTML(context, w);
    writeCache(w);
    w.write("<input type=\"hidden\" id=\""+getEtrHashCode()+"\" name=\""+getEtrHashCode()+"\" value=\""+scrollPos+"\">\n");
    w.write("<input type=\"hidden\" id=\"domainScrollPos\" name=\"domainScrollPos\" value=\""+scrollPos+"\">\n");
  }
  
  public void renderSearchBrowserHTML(de.tif.jacob.screen.impl.html.ClientContext context) throws Exception
  {
    if(!context.getApplication().isSearchBrowserVisible(context))
      return;
    
    context.setDomain(this);
    if(currentForm instanceof Form)
      ((Form)currentForm).renderSearchBrowserHTML(context);
  }
  
  public void renderSearchBrowserTabHTML(de.tif.jacob.screen.impl.html.ClientContext context) throws Exception
  {
    context.setDomain(this);
    if(currentForm instanceof Form)
      ((Form)currentForm).renderSearchBrowserTabHTML(context);
  }

  
  public void resetCache() 
  {
  	entries=null; // clean the Navigation entries too
		super.resetCache();
	}

	public INavigationEntry[] getNavigationEntries(IClientContext context) throws Exception
  {
  	IDomainEventHandler handler = (IDomainEventHandler)getEventHandler(context);
  	INavigationPanel panel = (handler!=null)?handler.getNavigationPanel(context,this):null;

    // Falls die Domain ein Eventhandler hat und dieser ein Navigation Panel, dann
    // werden die Navigationseinträge von diesem erfragt.
    //
  	if(panel!=null)
  	{
  		entries = panel.getNavigationEntries(context, this);
  		if(entries!=null)
  		{
  			currentEntryIndex = Math.max(0,Math.min(currentEntryIndex,entries.length-1));
  			return entries;
  		}
  	}
  	
    // Ansonsten werden einfach die normalen Formen eingehängt
    //
  	Vector entryVector = new Vector();//new INavigationEntry[getChildren().size()+ getFormGroups().size()];
  	for(int i =0;i<getChildren().size();i++)
  	{
      HTTPNavigationEntryProvider form = (HTTPNavigationEntryProvider)getChildren().get(i);
      if((form instanceof HTTPForm) && (form.getFormGroup()==null))
      {
        if(form == getCurrentForm(context) && currentEntryIndex==-1)
          currentEntryIndex=entryVector.size();
        entryVector.add(form.getNavigationEntry(context));
      }
  	}
    for(int i =0;i<getFormGroups().size();i++)
    {
      FormGroup group = (FormGroup)getFormGroups().get(i);
      entryVector.add(group.getNavigationEntry(context));
      for(int x =0;x<group.getChildren().size();x++)
      {
        HTTPNavigationEntryProvider form = (HTTPNavigationEntryProvider)group.getChildren().get(x);
        if(form == getCurrentForm(context) && currentEntryIndex==-1)
          currentEntryIndex=entryVector.size();
        entryVector.add(form.getNavigationEntry(context));
      }
    }
  	entries = (INavigationEntry[])entryVector.toArray(new INavigationEntry[entryVector.size()] );
    
		currentEntryIndex = Math.min(currentEntryIndex,entries.length-1);
		return entries;
	}

  
	public boolean isCurrentNavigationEntry(INavigationEntry entry) 
	{
  	if(entries[currentEntryIndex]==entry)
  		return true;
    return false;
	}

  public boolean setCurrentNavigationEntry(INavigationEntry entry)
  {
    for (int i = 0; i < this.entries.length; i++)
    {
      if (this.entries[i] == entry)
      {
        this.currentEntryIndex = i;
        resetCache();
        return true;
      }
    }
    return false;
  }

  /**
   * The method collect all DataFields of it childs and return them.
   * 
   * @return All DataFields in this domain.
   * 
   */
  public DataField[] getDataFields()
  {
  	if(domainDataFields==null)
    {
      Vector tmp = new Vector();
      addDataFields(tmp);
      domainDataFields = new DataField[tmp.size()];
      tmp.copyInto(domainDataFields);
    }
    
    return domainDataFields;
  }


  /**
   * Add all data fields of this form and the childs to the Vector.
   * 
   */
  protected void addDataFields(Vector fields)
  {
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      GuiHtmlElement element =(GuiHtmlElement)iter.next();
      element.addDataFields(fields);
    }
  }
  
  public IForm getForm(String name)
  {
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      GuiHtmlElement element =(GuiHtmlElement)iter.next();
      if(element instanceof IForm && element.getName().equals(name))
        return (IForm)element;
    }
    return null;
  }
  
  /*
   *  
   * @see de.tif.jacob.screen.IDomain#setInputFieldValue(java.lang.String, java.lang.String, java.lang.String)
   */
  public void setInputFieldValue(String formName, String fieldName, String value) throws Exception
  {
    IGuiElement form=findByName(formName);
    if(!(form instanceof IForm))
      throw new UserException("Form with the name ["+formName+"] does not exists.");
    
    ((IForm)form).setInputFieldValue(fieldName,value);
  }


  /*
   *  
   * @see de.tif.jacob.screen.IDomain#getInputFieldValue(java.lang.String, java.lang.String)
   */
  public String getInputFieldValue(String formName, String fieldName) throws Exception
  {
    IGuiElement form=findByName(formName);
    if(!(form instanceof IForm))
      throw new UserException("Form with the name ["+formName+"] does not exists.");
    
    return ((IForm)form).getInputFieldValue(fieldName);
  }

  /**
   * Returns the data domain (with cache...) of the current user guiBrowser window.
   * 
   * @return
   */
  public IDataAccessor getDataAccessor()
  {
    if(dataAccessor==null/* && parent instanceof HTTPApplication*/)
      dataAccessor = new DataAccessor(getApplicationDefinition());
    return dataAccessor;
  }
  
  public DataScope getDataScope()
  {
    DataScope dataScope = this.definition.getDataScope();
    if (dataScope == null)
    {
      return super.getDataScope();
    }
    return dataScope;
  }

  public String getEventHandlerReference()
  {
    return ((AbstractDomainDefinition)definition).getEventHandler();
  }

  /**
   * Wird von manchen Themes gerufen (Ajax) um eine Domain zusammen zu klappen.
   * Die Domain wird Clientseitig via moo.fx zusammen geklappt und der Status via 
   * Ajax der Domain mitgeteilt. 
   * Bei dem rausschreiben beachtet das Theme dann den Status.
   */
  public void collapse()
  {
    this.isOpen = Boolean.FALSE;
    ClientSession session = (ClientSession)((SessionContext)Context.getCurrent()).getSession();
    session.setRuntimeProperty(this.getPathName()+"."+this.getName(),this.isOpen.booleanValue());
  }

  /**
   * Wird von manchen Themes gerufen (Ajax) um eine Domain zusammen zu klappen.
   * Die Domain wird Clientseitig via moo.fx zusammen geklappt und der Status via 
   * Ajax der Domain mitgeteilt. 
   * Bei dem rausschreiben beachtet das Theme dann den Status.
   *
   */
  public void expand()
  {
    this.isOpen = Boolean.TRUE;
    ClientSession session = (ClientSession)((SessionContext)Context.getCurrent()).getSession();
    session.setRuntimeProperty(this.getPathName()+"."+this.getName(),this.isOpen.booleanValue());
  }
  
  
  public Boolean isOpen()
  {
    if(isOpen==null)
    {
      ClientSession session = (ClientSession)((SessionContext)Context.getCurrent()).getSession();
      String flag = session.getRuntimeProperty(this.getPathName()+"."+this.getName());
      if(flag!=null)
        this.isOpen = new Boolean(flag);
      else
        this.isOpen = Boolean.FALSE;
    }
    return isOpen;
  }

}
