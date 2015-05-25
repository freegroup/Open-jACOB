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
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.Session;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.data.impl.IDataFieldConstraint;
import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataFieldConstraintPrivate;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.impl.AbstractApplicationDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.license.LicenseException;
import de.tif.jacob.license.LicenseFilter;
import de.tif.jacob.report.IReport;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IToolbar;
import de.tif.jacob.screen.dialogs.IExcelDialog;
import de.tif.jacob.screen.event.IApplicationEventHandler;
import de.tif.jacob.screen.impl.ActionTypeHandler;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.screen.impl.HTTPApplication;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPForm;
import de.tif.jacob.screen.impl.HTTPGroup;
import de.tif.jacob.screen.impl.dialogs.HTTPGenericDialog;
import de.tif.jacob.screen.impl.html.dialogs.ExcelDialog;
import de.tif.jacob.searchbookmark.ISearchConstraint;
import de.tif.jacob.searchbookmark.SearchConstraintManager;
import de.tif.jacob.util.ObjectUtil;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.search.spotlight.Spotlight;

/**
 * 
 * @author Andreas Herz
 */
public class Application extends GuiHtmlElement implements HTTPApplication
{
  transient static public final String RCS_ID = "$Id: Application.java,v 1.46 2010/10/24 10:30:58 freegroup Exp $";
  transient static public final String RCS_REV = "$Revision: 1.46 $";

  transient static private final Log logger = LogFactory.getLog(Application.class);

  private final static String CANCEL_REQUEST_GUID="1C7F339C-8B3D-11DE-B812-DFF155D89593";
  private final static String CAN_ABORT_GUID="B2E2B816-8B3F-11DE-8779-451556D89593";

  public final static String EVENT_SEARCHBACKFILL = "searchcriteria"; 
  public final static String EVENT_SEARCHDELETE = "deletecriteria"; 
  public final static String EVENT_SPOTLIGHT = "spotlight"; 
   
  transient final IApplicationDefinition definition;
  transient final String httpApplicationId;

  transient Toolbar toolbar = null;

  transient boolean closed = false;
  transient boolean toolbarVisible = true;
  transient boolean toolbarConfigureable = true;
  transient boolean searchBrowserVisible = true;
  transient boolean navigationVisible = true;
  transient boolean showSQL = false;
  transient boolean trace = false;
  transient ClientSession session = null;

  transient public int dividerPos=200;
  
  private ReportBrowser reportBrowser;

  transient private int secondsBeforeDestroy = Property.TIMEOUTINTERVAL_APPLICATION.getIntValue();//TIMEOUT_INTERVALL_SECONDS;

  transient private ForeignFieldBrowser foreignFieldBrowser = null;

  transient private Set applicationManagedResources = new HashSet();
  
  // all data fields in the form.
  // ONLY VALID/REQUIRED if the scope of the DataAccess=="application"
  private DataField[] applicationDataFields = null;
  
  // properties for the application programmer. This has the scope/lifetime of a browser window
  //
  private final Map properties = new HashMap();
  

  // All active foci for this application. All objects in this HashMap
  // are copies of an object in the 'foci' HashMap.
  //
  transient Domain activeDomain = null;

  protected IDataAccessor dataAccessor = null;

  public String doForward=null;
  private boolean isLoginHookCalled=false;
  private boolean isCreatedHookCalled=false;
  private boolean initDone = false;
  private Boolean hasSpotlightSupport = null;
  
  /**
   * If <code>true</code> force login screen to single application when closed.
   */
  private boolean doForceApplicationOnLogin = false;
  
  protected Application(IApplicationDefinition definition, String httpApplicationId) throws Exception 
  {
    super(null, definition.getName(), definition.getTitle(), true, null, definition.getProperties());

    this.definition = definition;
    this.httpApplicationId = httpApplicationId;
  }

  
  public final String getHTTPApplicationId()
  {
    return this.httpApplicationId;
  }

  /**
   * The method collect all DataFields of it childs and return them.
   * 
   * @return All DataFields in this domain.
   * 
   */
  public DataField[] getDataFields()
  {
    if(applicationDataFields==null)
    {
      Vector tmp = new Vector();
      addDataFields(tmp);
      applicationDataFields = new DataField[tmp.size()];
      tmp.copyInto(applicationDataFields);
    }
    
    return applicationDataFields;
  }
  
  /*
   * @see de.tif.jacob.screen.GUIElement#addDataFields(java.Util.Vector)
   */
  protected void addDataFields(Vector fields)
  {
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      Object element =iter.next();
      if(element instanceof GuiHtmlElement)
        ((GuiHtmlElement)element).addDataFields(fields);
    }
  }

  public void setToolbar(IToolbar toolbar)
  {
    this.toolbar = (Toolbar) toolbar;
    this.toolbar.setParent(this);
  }

  public IToolbar getToolbar()
  {
    return this.toolbar;
  }

  
  public void doForward(IExcelDialog dialog)
  {
    doForward = ((ExcelDialog)dialog).generateUrl();
  }

  public int getDividerPos()
  {
    return dividerPos;
  }
  
  /**
   * Return HTML representation of this object
   *  
   */
  public void renderToolbar(ClientContext context) throws Exception
  {
    if (isVisible() == false || toolbarVisible == false)
      return;

    toolbar.calculateHTML(context);
    toolbar.writeHTML(context, context.out);
  }

  /**
   * Render the SearchBrowser element if it visible
   * 
   * @param context
   * @throws IOException
   * @throws NoSuchFieldException
   */
  public void renderSearchBrowserHTML(ClientContext context) throws IOException, NoSuchFieldException
  {
    if (isVisible() == false)
      return;

    try
    {
      if (reportBrowser != null)
      {
        reportBrowser.calculateHTML(context);
        reportBrowser.writeHTML(context, context.out);
      }
      else if (activeDomain != null && searchBrowserVisible==true)
      {
        activeDomain.renderSearchBrowserHTML(context);
      }
    }
    catch (Throwable th)
    {
      ExceptionHandler.handle(context, th);
    }
  }

  public void renderSearchBrowserTabHTML(ClientContext context) throws IOException, NoSuchFieldException
  {
    if (isVisible() == false)
      return;

    try
    {
      // Falls die Application sich im 'Report mode' befinden, wir anstelle der
      // Tab-Leiste eine Steuerleiste für den Report eingetragen.
      //
      if (reportBrowser != null)
      {
        reportBrowser.writeTabHTML(context);
      }
      else if (activeDomain != null && searchBrowserVisible==true)
      {
        activeDomain.renderSearchBrowserTabHTML(context);
      }
    }
    catch (Throwable th)
    {
      ExceptionHandler.handle(context, th);
    }
  }

  public void renderSearchBrowserActionHTML(ClientContext context) throws IOException, NoSuchFieldException
  {
    if (isVisible() == false)
      return;

    try
    {
      // Falls die Application sich im 'Report mode' befinden, wird anstelle der
      // Tab-Leiste eine Steuerleiste für den Report eingetragen.
      //
      if (reportBrowser != null)
      {
        // do nothing
      }
      else if (activeDomain != null && searchBrowserVisible==true)
      {
        if(activeDomain.getCurrentForm(context) instanceof Form)
        {
          Form form=(Form)activeDomain.getCurrentForm(context);
         ((SearchBrowser) form.getCurrentBrowser()).writeActionHTML(context);
        }
      }
    }
    catch (Throwable th)
    {
      ExceptionHandler.handle(context, th);
    }
  }
  
  /*
   * @see de.tif.jacob.screen.GUIElement#renderHTML(java.io.Writer) @author
   *      Andreas Herz
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    try
    {
      // Any event has terminate this application. Send the request to the login
      // screen.
      if (closed == true)
        return;

      if (getCache() == null)
      {
        Writer w = newCache();
        w.write("<script type=\"text/javascript\">var browserId=\'");
        w.write(context.clientBrowser);
        w.write("\';</script>\n");
      }
      activeDomain.calculateHTML(context);
      if (foreignFieldBrowser != null)
        ForeignFieldSelectDialog.show( context, foreignFieldBrowser);
    }
    catch (Throwable th)
    {
      ExceptionHandler.handle(context, th);
    }
  }
  
  /**
   * 
   */
  public void calculateIncludes(ClientContext context)
  {
    if(this.activeDomain !=null)
      this.activeDomain.calculateIncludes(context);
  }
  
  public void setDoForceApplicationOnLogin()
  {
    this.doForceApplicationOnLogin = true;
  }

  /**
   * Writes the HTML content to the stream
   * 
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w)
  {
    try
    {
      // Any event has terminate this application. Send the request to the login
      // screen.
      if (closed == true)
      {
        if (this.doForceApplicationOnLogin)
        {
          w.write("<script type=\"text/javascript\">location.href='logout.jsp?forceApp=");
          w.write(this.definition.getName());
          w.write("';</script>\n");
        }
        else
          w.write("<script type=\"text/javascript\">location.href='logout.jsp';</script>\n");
        return;
      }
      writeCache(w);
      activeDomain.writeHTML(context, w);
    }
    catch (Throwable th)
    {
      ExceptionHandler.handle(th);
    }
  }

  /**
   * 
   * @return true if the application has been show a domain once before.
   */
  public boolean hasActiveDomain()
  {
    return activeDomain != null;
  }

  /**
   * 
   * @return true if the application has been show a domain once before.
   */
  public HTTPDomain getActiveDomain()
  {
    return activeDomain;
  }

  /**
   * Returns the theme for the current user
   * @deprecated use ClientSession.getTheme() instead
   */
  public String getTheme()
  {
    return session.getCurrentTheme();
  }

  /**
   * Returns the theme for the current user
   */
  public void setTheme(String theme)
  {
    // invalidate to reflect new icons (which might be cached), etc.
    invalidate();
    
    session.setCurrentTheme(theme);
  }


  public void setSession(HTTPClientSession session) throws Exception
  {
    this.session = (ClientSession)session;
  }
  
  /**
   *  
   */
  public final void init(HTTPClientContext context) throws Exception
  {
    Domain defaultDomain = null;
    HTTPForm defaultForm   = null;
    Domain userDomain    = null;
    HTTPForm userForm      = null;
    
    Domain currentDomain    = null;
    HTTPForm currentForm      = null;

    // DefaultEinstellung für Domain und Form ermitteln
    // (wenn sich der Benutzer noch nie eingeloggt hat).
    if (getChildren().size() > 0)
    {
      defaultDomain = (Domain) getChildren().get(0);
      defaultForm   = (HTTPForm)defaultDomain.getCurrentForm(context);
    }
    
    // Versuchen die letzte Domain und Form des Benutzers zu ermitteln.
    // Dies wird bei dem Logout des Users in der DB gespeichert.
    //
    String domainId = session.getRuntimeProperty("ui.last.domain");
    String formId = session.getRuntimeProperty("ui.last.form");
    dividerPos = Integer.parseInt(session.getRuntimeProperty(Property.DIVIDER_POS.getName())); 
    Object obj = findByName(domainId);
    if (obj instanceof Domain)
    {
      userDomain = (Domain) obj;
      obj = userDomain.findByName(formId);
      if (obj instanceof Form)
        userForm = (HTTPForm) obj;
      else
        userForm = (HTTPForm)userDomain.getCurrentForm(context);
    }
    
    // Es kann passieren, dass de Anwender durch einen Entrypoint die Anwendung betritt.
    // In diesem Fall wurde die Domain/Form von dem Entrypoint bereits vorgegeben. Dies
    // darf icht durch Benutzereinstellungen überschrieben werden.
    //
    if(this.activeDomain==null)
    {
      // Es wird am Context die konrkete Form und Domain gesetzt, damit in dem 
      // onLogin und onCreate Hook der Anwendungsprogrammierer sich auf 
      // gültige Werte beziehen kann.
      // Es werden dabei noch keine Events an den Hook aufgerufen (onShow/onHide)
      //
      if(userForm!=null)
      {
        context.setDomain(currentDomain=userDomain);
        context.setForm(currentForm=userForm);
      }
      else
      {
        context.setDomain(currentDomain=defaultDomain);
        context.setForm(currentForm=defaultForm);
      }
      
      // Auch in der Application und Domain die ermittelten Domain/Form
      // setzten, damit in dem onLogin / onCreate ein möglicher Wechsel der
      // Domain korrekt funktioniert.
      // 
      this.activeDomain = currentDomain;
      this.activeDomain.setCurrentFormWithoutEvent(currentForm);
    }
    // Die erste Gruppe in der Form als "currentGroup" setzen. So hat die Anwendung einen
    //
    if(!this.activeDomain.getCurrentForm(context).getChildren().isEmpty())
      context.setGroup((HTTPGroup)this.activeDomain.getCurrentForm(context).getChildren().get(0));

    IApplicationEventHandler handler = getEventHandler();
    if (handler != null)
    {
      // Falls es die erste Applicaton ist, dann wird der login Hook
      // aufgerufen
      if (hasLoginHookCalled()==false)
      {
        handler.onLogin(context, this);
        setLoginHookCalled();
      }

      // ein neues Fenster wurde erzeugt.
      //
      if (hasCreatedHookCalled()==false)
      {
        handler.onCreate(context, this);
        setCreatedHookCalled();
      }
    }

    
    // Es wurden die Applikation, Domain und Form korrekt gesetzt.
    // ( Eventuell wurde dies von dem onLogin oder onCreate Hook geändert)
    // Jetzt kann die onShow-Methode aufgerufen werden.
    // Domain oder Form kann in dem Hook wieder geändert werden. Ab jetzt ist aber die
    // Applikation initalisiert und es dürfen onShow/onHide Event ausgelöst werden
    //
    this.initDone=true;
    this.activeDomain.onShow(context);
  }

  /**
   * Close (exit) this application. This method can be called from the Toolbar,
   * contextmenu, timer.... All associated resource will be released.
   *  
   */
  public final void close()
  {
    if (true == closed)
    {
      logger.warn("Don't call Application.close() twice.", new RuntimeException());
      return;
    }

    if (logger.isDebugEnabled())
      logger.debug("closing application:" + this.getName());

    closed = true;
    
    Context context = Context.getCurrent();
    // The user has logged out by him self (no timeout,....)
    // In this case it is possible to store the GUI state in the database
    //
    if (context instanceof ClientContext)
    {
      ClientContext cc = (ClientContext) context;
      
      try
      {
        this.activeDomain.onHide(cc);
      }
      catch (Exception e)
      {
        // don't call handleSmart(...). This method try to show a dialog to the
        // user.
        //
        ExceptionHandler.handle(e);
      }
      
      IDomain domain = cc.getDomain();
      IForm form = cc.getForm();
      // FREEGROUP: form ist manchmal null! warum?
      //            Habe dies mehrmals in QM und caretaker logs gesehen!
      if (domain != null && form != null)
      {
        session.setRuntimeProperty("ui.last.domain", domain.getName());
        session.setRuntimeProperty("ui.last.form", form.getName());
        session.setRuntimeProperty(Property.DIVIDER_POS.getName(), dividerPos);
      }
      try
      {
        IApplicationEventHandler handler = getEventHandler();
        if (handler != null)
          handler.onLogout(cc, this);
      }
      catch (Exception e)
      {
        // don't call handleSmart(...). This method try to show a dialog to the user.
        //
        ExceptionHandler.handle(e);
      }
    }

    // Iterate over all resources and release them
    //
    synchronized (applicationManagedResources)
    {
      Iterator iter = applicationManagedResources.iterator();
      while (iter.hasNext())
      {
        ManagedResource res = (ManagedResource) iter.next();
        res.release();
      }
    }
    
    // Unregister application at jACOB session
    if (this.session != null)
      this.session.unregisterClosedApplication(this.httpApplicationId);
  }

  /**
   * Send the event from the client guiBrowser to the GUIElements
   * 
   *  
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    try
    {
      // update the session statistik
      //
      session.setLastAccess(new Date());

      if(guid == getId())
      {
        if(EVENT_SEARCHBACKFILL.equals(event))
        {
          ISearchConstraint constraint = SearchConstraintManager.getSearchConstraint(context,value);
          this.backfillSearchConstraint(context,constraint);
          // zu dem searchconstraint den zugehörigen browser/gruppe finden
          Iterator iter = context.getForm().getChildren().iterator();
          while(iter.hasNext())
          {
            IGuiElement child = (IGuiElement)iter.next();
            IGroup group  =child.getOuterGroup();
            if(group.getGroupTableAlias().equals(constraint.getAnchorTable()))
            {
              group.getBrowser().getData().setMaxRecords(Property.BROWSER_COMMON_MAX_RECORDS.getIntValue());
              group.getBrowser().getData().search(constraint.getRelationset(context));
              break;
            }
          }
        }
        else if(EVENT_SEARCHDELETE.equals(event))
        {
          ISearchConstraint constraint = SearchConstraintManager.getSearchConstraint(context,value);
          if(constraint!=null)
            SearchConstraintManager.delete(context,constraint);
        }
        else if (EVENT_SPOTLIGHT.equals(event))
        {
          IGuiElement element = findChild(Integer.parseInt(value));
          IGuiElement current = element;
          IDomain domain = current instanceof IDomain ? (IDomain) current : null;
          IForm form = null;
          while (domain == null)
          {
            if (current instanceof IDomain)
            {
              domain = (IDomain) current;
              ((Domain) domain).expand();
            }
            else if (current instanceof ITabPane)
              ((ITabPane) current).setActive(context);
            else if (current instanceof IForm)
              form = (IForm) current;
            current = current.getParent();
          }
          if (form != null && domain != null)
          {
            context.setCurrentForm(domain.getName(), form.getName());
            ((GuiElement)element).marchingAnts(context);
            context.clearForm();
          }
        }                
        return true;
      }
      // check if the event is related to a dialog
      //
      HTTPGenericDialog dialog = session.getDialog("" + guid);
      if (dialog != null)
      {
        if(dialog.getAutoclose()==true)
          session.removeDialog(dialog);
        if (logger.isDebugEnabled())
          logger.debug("Processing eventhandling for dialog type:" + dialog.getClass().getName());
        return dialog.processEvent(context, guid, event, value);
      }

      // Check if the guiBrowser of the foreign field has send the event
      //
      if (foreignFieldBrowser != null && foreignFieldBrowser.processEvent(context, guid, event, value))
      {
        foreignFieldBrowser = null;
        return true;
      }
      foreignFieldBrowser = null;

      // 3.) Check if this a simple refresh event. No object as emitter
      //
      if (guid == 0)
      {
        return true;
      }
      
      // 4.) Check if the event comes from the ToolBar
      //
      else if (toolbar.processEvent(context, guid, event, value))
      {
        return true;
      }
      
      // 5.) If we are in the report mode and the reportBrwoser send this event
      //
      else if (reportBrowser != null && reportBrowser.processEvent(context, guid, event, value))
      {
        return true;
      }
      
      // ...else send the event to the common children
      //
      else
      {
        boolean result = super.processEvent(context, guid, event, value);

        if (!result)
          // this can happens if a dialog (upload/report) has reached a timeout.
          logger.warn("Unable to find event source for guid[" + guid + "] event[" + event + "] value[" + value + "]");

        return result;
      }
    }
    catch (Throwable e)
    {
      ExceptionHandler.handleSmart(context, e);
    }
    return false;
  }

  /**
   * Send the parameters from the request to the GUIELemetns in the current
   * active (visible) form.
   * 
   * @param request
   * @author Andreas Herz
   */
  public void processParameter(IClientContext context, HttpServletRequest request) throws Exception
  {
    if(LicenseFilter.isFilterApplied(request)==false)
      throw new LicenseException("Request processed without any License management.");

    try
    {
      // Das init der Application so spät wie möglich aufrufen.
      // D.h. das Init muss in dem ersten Request Cyklus aufgerufen werden
      // welcher von dem xy_content.jsp aus geht.
      // (Es gibt noch einen Request Zyklus zuvor welcher von der login.jsp ausgeht. 
      //  dies ist zu früh)
      //
      if (initDone==false)
        init((HTTPClientContext)context);
      
    
      if (activeDomain != null)
      {
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements())
        {
          String param = (String) e.nextElement();
          if (param.startsWith(ID_PREFIX))
          {
            // only the current visible guiBrowser will receive the
            // processParameter event
            //
            activeDomain.processParameter(Integer.parseInt(param.substring(ID_PREFIX.length())), request.getParameter(param));
          }
          // The user can switch the input focus of an gui element. Trace them
          // and restore the foucs after
          // request cycle
          //
          else if (param.equals("focusElement"))
          {
            Object obj = activeDomain.getCurrentForm(context).findByName(request.getParameter(param));
            if (obj instanceof SingleDataGUIElement)
            {
              SingleDataGUIElement input = (SingleDataGUIElement) obj;
              activeDomain.getCurrentForm(context).setFocus(input);
            }
          }
          else if (param.equals("dividerPos") && isSearchBrowserVisible(context))
          {
            dividerPos = Integer.parseInt(request.getParameter(param));
          }
          else if (param.equals("notepadTextarea"))
          {
            // get them from the cache or db
            String save= session.getRuntimeProperty("notepadTextarea");
            String value = request.getParameter("notepadTextarea");
            if(!StringUtil.saveEquals(save,value))
              session.setRuntimeProperty("notepadTextarea",value);
          }
          else if (reportBrowser != null && param.startsWith(ReportBrowser.VAR_SUFFIX))
          {
            int guid = Integer.parseInt(param.substring(ReportBrowser.VAR_SUFFIX.length()));
            String reportName = request.getParameter(param);
            boolean isPrivate = request.getParameter(ReportBrowser.VAR_PRIVATE) != null;
            reportBrowser.processParameter(guid, reportName, isPrivate);
          }
        }
      }
    }
    catch (Throwable e)
    {
      ExceptionHandler.handle(e);
    }
  }

  
  public String getEventHandlerReference()
  {
    return ((AbstractApplicationDefinition)definition).getEventHandler();
  }
  
  /**
   * Register a Resource for the current application window. The application
   * calls the releaseResource method of the resource if they close.
   * 
   * @param resource
   *          The resource which should be released if the application session
   *          will be droppend.
   *  
   */
  public void register(ManagedResource resource)
  {
    synchronized (applicationManagedResources)
    {
      applicationManagedResources.add(resource);
    }
  }

  /**
   * Unregister a resource from the application scope.
   * 
   * @param resource
   *          The resource to remove from the application scope.
   */
  public void unregister(ManagedResource resource)
  {
    synchronized (applicationManagedResources)
    {
      applicationManagedResources.remove(resource);
    }
  }

  /**
   * 
   * @param context
   * @param domain
   * @author Andreas Herz
   */
  public void setActiveDomain(de.tif.jacob.screen.IClientContext context, IDomain domain) throws Exception
  {
    if(domain==null)
      throw new NullPointerException("setActiveDomain(....,IDomain null) is not allowed. You must hand over a valid domain.");
  
    if(activeDomain==domain)
      return;
    
    if(activeDomain!=null && initDone)
    {
      // Domain wird gewechselt. Dies passiert z.B. durch ein Klick in die Navigation oder via Script.
      // Im Moment ist am Context die Domain/Form gesetzt welche den Event ausgelöst hat.
      // Dies hilft allerdings bei dem onHide nicht, da dort die alte Domain/Form gesetzt
      // sein muss damit die Eventhandler richtig arbeiten können.
      //
      ((ClientContext)context).setForm((HTTPForm)(activeDomain).getCurrentForm(context));
      ((ClientContext)context).setDomain(activeDomain);
      ((HTTPDomain)activeDomain).onHide(context);
    }

    activeDomain = (Domain) domain;
    
    // Ab jetzt wird die neue Domain und die darin aktive Form für alle gültig.
    //
    ((ClientContext) context).setDomain(domain);
    ((ClientContext) context).setForm((HTTPForm)domain.getCurrentForm(context));

    if(initDone)
      ((HTTPDomain)activeDomain).onShow(context);
  }

  /**
   * @return Returns the applicationDefinition.
   */
  public IApplicationDefinition getApplicationDefinition()
  {
    return definition;
  }


  public boolean isDirty(IClientContext context) throws Exception
  {
    return this.hasChildInDataStatus(context, IGuiElement.UPDATE) || this.hasChildInDataStatus(context, IGuiElement.NEW);
  }

  /**
   * Returns <code>true</code> if the application has been closed. <br>
   * All client actions to a closed application will be ignored and has no
   * affects.
   * 
   * @return Returns <code>true</code> if the application has been closed.
   */
  public final boolean isClosed()
  {
    return closed;
  }

  public boolean hasLoginHookCalled()
  {
    return isLoginHookCalled;
  }

  public void setLoginHookCalled()
  {
    isLoginHookCalled=true;
  }
  
  public boolean hasCreatedHookCalled()
  {
    return isCreatedHookCalled;
  }

  public void setCreatedHookCalled()
  {
    isCreatedHookCalled=true;
  }
  
  /*
   * @see de.tif.jacob.screen.IApplication#isToolbarVisible() 
   */
  public boolean isToolbarVisible()
  {
    return toolbarVisible;
  }

  /*
   * @see de.tif.jacob.screen.IApplication#setToolbarVisible(boolean) @
   */
  public void setToolbarVisible(boolean flag)
  {
    toolbarVisible = flag;
  }


  /**
   * @return Returns the session.
   */
  public Session getSession()
  {
    return session;
  }


  /**
   * @param searchBrowserVisible
   *          The searchBrowserVisible to set.
   */
  public void setNavigationVisible(boolean flag)
  {
    this.navigationVisible = flag;
  }

  public boolean isNavigationVisible()
  {
    return navigationVisible;
  }

  /**
   * Spotlightsupport ist vorhanden wenn mind. ein GUI Element ein Spotlight Suchbegriff enthält.
   * 
   * @return
   */
  public boolean hasSpotlightSupport()
  {
    if(hasSpotlightSupport==null)
      hasSpotlightSupport =  Spotlight.checkSpotlightSupport(this)?Boolean.TRUE:Boolean.FALSE;
    return hasSpotlightSupport.booleanValue();
  }

  /**
   * @param searchBrowserVisible
   *          The searchBrowserVisible to set.
   */
  public void setSearchBrowserVisible(boolean searchBrowserVisible)
  {
    this.searchBrowserVisible = searchBrowserVisible;
  }

  /**
   * @return Returns the searchBrowserVisible.
   */
  public boolean isSearchBrowserVisible(IClientContext context)
  {
    if (activeDomain != null && activeDomain.getCurrentForm(context) != null && activeDomain.getCurrentForm(context).getCurrentBrowser() == null)
      return false;

    // Falls die aktuelle Form keinen sichtbaren SearchBrowser hat muss dies hier berücksichtigt werden.
    //
    if(((HTTPForm)activeDomain.getCurrentForm(context)).hasVisibleSearchBrowser()==false)
      return false;
    
    return searchBrowserVisible;
  }

  /**
   * @return
   */
  public String getVersion()
  {
    return definition.getVersion().toString();
  }

  /**
   * @return Returns the trace.
   */
  protected boolean isTrace()
  {
    return trace;
  }

  /**
   * @param trace
   *          The trace to set.
   */
  protected void setTrace(boolean trace)
  {
    this.trace = trace;
  }

  /**
   * @return Returns the trace.
   */
  protected boolean isShowSQL()
  {
    return showSQL;
  }

  /**
   * @param trace
   *          The trace to set.
   */
  protected void setShowSQL(boolean showSQL)
  {
    this.showSQL = showSQL;
  }

  /**
   * 
   * @param secondsBeforeTimeout
   *          The secondsBeforeTimeout to set.
   */
  public synchronized final void decrementSecondsBeforeDestroy(int amount)
  {
    this.secondsBeforeDestroy -= amount;
  }

  /**
   * @param foreignFieldBrowser
   *          The foreignFieldBrowser to set.
   */
  protected void setForeignFieldBrowser(ForeignFieldBrowser foreignFieldBrowser)
  {
    this.foreignFieldBrowser = foreignFieldBrowser;
  }

  public boolean isInReportMode(IClientContext context)
  {
    return reportBrowser != null;
  }

  public boolean isLookupEventHandlerByReference()
  {
    return ((AbstractApplicationDefinition)definition).lookupEventHandlerByReference();
  }
  
  public IDataBrowser getReportDataBrowser(IClientContext context)
  {
    return reportBrowser.getData();
  }

  public void setReportDataBrowser(IClientContext context, IDataBrowser browser)
  {
    reportBrowser.setData(context, browser);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.screen.impl.GuiElement#invalidate()
   */
  public void invalidate()
  {
    super.invalidate();
    
    if (toolbar != null)
      toolbar.invalidate();
  }

  /**
   * Used by the ReportDialog.
   * @param context
   * @param report
   *          [optional] template/start definition of the report
   */
  public void startReportMode(IClientContext context, IReport report)
  {
    try
    {
      IRelationSet relationset = report.getRelationset(context);
      String anchorTable       = report.getAnchorTable();
      String reportName = report.getName();
      boolean isPrivate = report.isPrivate();

      // ReportBrowser aufbauen 
      //
      reportBrowser = new ReportBrowser(this, context, anchorTable, relationset, report.getConstraints());

      IReport.Column[] columns = report.getColumns();
      for (int i = 0; i < columns.length; i++)
      {
        IReport.Column column = columns[i];
        reportBrowser.addColumn(column.table, column.field, column.label, column.order);
      }
      reportBrowser.setReportName(reportName);
      reportBrowser.setPrivate(isPrivate);

      backfillSearchConstraint(context, report);
    }
    catch (Exception e)
    {
      reportBrowser = null;
      alert(context, "MSG_UNABLE_TO_CREATE_REPORT");
    }
  }
  

  public void startReportMode(IClientContext context)
  {
    try
    {
      IRelationSet relationset = context.getForm().getCurrentBrowser().getData().getRelationSet();
      String reportDefaultName = "report name";

      String anchorTable = ((SearchBrowser) ((Form) context.getForm()).getCurrentBrowser()).getGroupTableAlias();
      
      IDataBrowserInternal dataBrowser = (IDataBrowserInternal) context.getDataBrowser();
      IDataFieldConstraints startConstraints= dataBrowser.getLastSearchConstraints();

      reportBrowser = new ReportBrowser(this, context, anchorTable, relationset, startConstraints);
      reportBrowser.setReportName(reportDefaultName);
      
      // force a redraw of all gui elements
      //
      invalidate();
    }
    catch (Exception e)
    {
      reportBrowser = null;
      alert(context, "MSG_UNABLE_TO_CREATE_REPORT");
    }
  }

  
  public void stopReportMode(IClientContext context)
  {
    reportBrowser = null;

    // force a redraw of all gui elements
    //
    invalidate();
  }

  /**
   * Used by the ReportDialog.
   * @param context
   * @param report
   *          [optional] template/start definition of the report
   */
  public void backfillSearchConstraint(IClientContext context, ISearchConstraint constraint) throws Exception
  {
      // Die Form in der gesucht worden ist wieder herstellen
      //
      context.setCurrentForm(constraint.getAnchorDomain(), constraint.getAnchorForm());

      // die GUI muss jetzt den SELECTED mode verlassen da sonst keine Iniiale Suche gefahren
      // werdne kann. Es ist ja ein PKEY zurückgefüllt welcher somit ein Constraint auf genau
      // einen Rekord hat. Desweiteren wird beim EDIT_REPORT verhindert, dass die Constraints
      // in den Report zurückgefüllt wird. Der Report hat nach dem EDIT keine Cosntraints mehr falls
      // ein Record selectiert war.
      // see BrowserActionSaveReport
      // @deprecated?
      context.getGroup().clear(context,false);
      
      // Alle GUI Felder löschen
      //
      DataScope dataScope = ActionTypeHandler.calculateDataScope(context, this);
      if (dataScope == DataScope.APPLICATION)
        context.clearApplication();
      else if (dataScope == DataScope.DOMAIN)
        context.clearDomain();
      else if (dataScope == DataScope.FORM)
        context.clearForm();
      else
        // default behaviour in the case of corrupt Property configuration
        context.clearApplication();
 
      // Constraints des Reports in der QBE eintragen und eine Suche fahren
      //
      // TODO: Hier muß eigentlich die im Report gespeicherte locale gesetzt werden! 
      ((DataAccessor) context.getDataAccessor()).applySearchConstraints(constraint.getConstraints(), context.getLocale());
      

      ((Application) context.getApplication()).calculateHTML((ClientContext) context);

      // 5.) GUI Felder mit den letzen Suchbedingungen füllen
      //
      Iterator iter = constraint.getConstraints().getConstraints();
      while (iter.hasNext())
      {
        IDataFieldConstraint c = (IDataFieldConstraint) iter.next();
        if( !c.isQbeKeyValue())
        {
          String table = c.getTableAlias().getName();
          String field = c.getTableField().getName();
  
          String guiField = null;
  
          // Das constraints beinhaltet eventuell die Information von welchem
          // GUIElement dies ursprünglich kommt.
          //
          if (c instanceof IDataFieldConstraintPrivate)
          {
            guiField = ((IDataFieldConstraintPrivate)c).guiElementName();
  
            // Wert nur in das GUI Feld eintragen, von dem es auch kommt.
            //
            if (guiField != null)
            {
              List elements = context.getDomain().getGuiRepresentations(table, field);
              Iterator elementIter = elements.iterator();
              while (elementIter.hasNext())
              {
                ISingleDataGuiElement element = (ISingleDataGuiElement) elementIter.next();
                if (ObjectUtil.equalsIgnoreNull(element.getPathName(), guiField))
                {
                  element.setValue(c.getQbeValue());
                  break;
                }
              }
            }
          }
        }
      }
      // force a redraw of all gui elements
      //
      invalidate();
      ((Application) context.getApplication()).calculateHTML((ClientContext) context);
  }
  
  /**
   * Sends an ABORT message to the application/window. <br>
   * The current running context can ignore this event if 
   * the didn't provide a "abort" mechanism.<br>
   *  
   * @since 2.8.7
   */
  public void requestAbort()
  {
    if(canAbort())
      this.setPropertyForWindow(CANCEL_REQUEST_GUID, Boolean.TRUE);
  }
  
  /**
   * Return true if the current running context should abort and rollback
   * all running operations. 
   *  
   * @since 2.8.7
   */
  public boolean shouldAbort()
  {
    Object flag = this.getPropertyForWindow(CANCEL_REQUEST_GUID);
    return flag == Boolean.TRUE;
  }

  /**
   * Set the flag that all running context related to the window 
   * can be aborted.
   * 
   * @param flag
   * 
   * @since 2.8.7
   */
  public void canAbort(boolean flag)
  {
    if(flag)
      this.setPropertyForWindow(CAN_ABORT_GUID, Boolean.TRUE);
    else
      this.setPropertyForWindow(CAN_ABORT_GUID, null);
  }

  /**
   * Check whenever the context provides the "abort" feature.
   *  
   * @since 2.8.7
   */
  public boolean canAbort()
  {
    Object flag = this.getPropertyForWindow(CAN_ABORT_GUID);
    return flag == Boolean.TRUE;
  }
  
  public void addReportColumn(String alias, String column, String label) throws IllegalStateException, NoSuchFieldException
  {
    if (reportBrowser == null)
      throw new IllegalStateException("Engine ist not in report mode");

    reportBrowser.addColumn(alias, column, label);
  }

  public int getSecondsBeforeDestroy()
  {
    return secondsBeforeDestroy;
  }
  
  public void setSecondsBeforeDestroy(int sec)
  {
    secondsBeforeDestroy = sec;
  }
  
  public IApplicationEventHandler getEventHandler() throws Exception
  {
    return Command.getEventHandler(this);
  }
  
  /**
   * Wird nur aufgerufen wenn sich alle Domains einen IDataAccessor teilen
   * FREEGROUP: temporär eingestellt in der AdminKonsole property konfiguration
   * 
   * @return
   */
  public IDataAccessor getDataAccessor()
  {
    if(dataAccessor==null)
      dataAccessor = new DataAccessor(getApplicationDefinition());
    return dataAccessor;
  }

  public DataScope getDataScope()
  {
    DataScope dataScope = this.definition.getDataScope();
    if (dataScope == null)
    {
      return DataScope.parseDataScope(Property.DATA_ACCESSOR_SCOPE.getValue(this.definition));
    }
    return dataScope;
  }

  // getter setter methods for the application programmer
  //
  public final Object getPropertyForWindow(Object key)
  {
    return properties.get(key);
  }
  
  public final void setPropertyForWindow(Object key, Object value)
  {
    if(value==null)
      properties.remove(key);
    else
      properties.put(key, value);
  }

  public boolean isInitDone()
  {
    return initDone;
  }
}
