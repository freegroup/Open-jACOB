package de.tif.jacob.screen.impl;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.PropertyNotifyee;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.TerminatedSessionException;
import de.tif.jacob.core.exception.TimeoutApplicationException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.core.model.Activesession;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.license.License;
import de.tif.jacob.license.LicenseException;
import de.tif.jacob.license.LicenseFactory;
import de.tif.jacob.scheduler.SchedulerService;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientSession;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.impl.dialogs.HTTPGenericDialog;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;
import de.tif.jacob.security.UserManagement;
import de.tif.jacob.security.impl.AbstractUser;

/**
 *
 */
public abstract class HTTPClientSession extends IClientSession implements HttpSessionBindingListener, PropertyNotifyee, Serializable
{
  transient static public final String RCS_ID = "$Id: HTTPClientSession.java,v 1.19 2010/04/13 12:53:24 ibissw Exp $";
  transient static public final String RCS_REV = "$Revision: 1.19 $";
  
  transient static private final Log logger = LogFactory.getLog(HTTPClientSession.class);

  transient private int  secondsBeforeSessionDestroy  = Property.TIMEOUTINTERVAL_APPLICATION.getIntValue();//TIMEOUT_INTERVALL_SECONDS;
  transient private int  secondsBeforeAlertDestroy    = Property.TIMEOUTINTERVAL_DIALOG.getIntValue();//TIMEOUT_INTERVALL_SECONDS;
  
  transient static public  final String ALERT_KEY   = "alertWindow";

  transient static private final String NULL_ENTRY = new String("");
  transient private final Properties systemRuntimeProperties = new Properties();
  
  // managed resources welche von der session abgeräumt werden müssen
  //
  transient private Set     sessionManagedResources = new HashSet();

  // properties welche vom user gesetzt werden können und eine Gültigkeit
  // von der Sessionlaufzeit haben
  transient private Map     sessionProperties = new HashMap();
  
  /**
   * Map to cache mapping of http language (e.g. "en-US") to respective Locale.
   */
  transient static private final Map httpLanguage2Locale = new HashMap();

  // Hack: Grund liegt in der valueBound(...) Methode!!!
  transient private Throwable createException;
  
  transient private HttpSession httpSession;
  transient private String applicationRootWebPath;
  transient private final String host;
  transient private final String baseUrl;
  transient private final String sessionId;
  transient protected final IUser  user;
  transient protected final String clientHostName;
  transient protected final String clientHostAddr;

  transient private Map     id2Dialog               = new HashMap();
  transient private Map     id2Autosuggest          = new HashMap();
  transient private Map     autosuggest2Values      = new HashMap();
  transient private Map     browser2Application     = new HashMap();
  transient private final Set    invalidBrowserIds=new HashSet();

  transient private boolean alertVisible            = false;
  transient private boolean alertMustRefresh        = false;
  transient private Date    lastAccess              = new Date();    // Timestamp of last user interaction

  transient private StringBuffer asynchronHtml           = new StringBuffer("");

  public abstract IApplicationFactory getApplicationFactory();
  public abstract HTTPClientContext   createContext(IUser user, HTTPApplication app, String clientId) throws IOException;
  
  public HTTPClientSession(HttpServletRequest request, IUser user, IApplicationDefinition applicationDef)
  {
    super(applicationDef);
    
    this.sessionId = new java.rmi.server.UID().toString();
    this.user = user;

    // if user implementation is AbstractUser, check whether default locale from
    // browser has already been set.
    //
    if (user instanceof AbstractUser)
    {
      AbstractUser abstractUser = (AbstractUser)user;
      
      // To initialize user locale with browser locale, if method
      // IUser.getLocale() has not been overwritten
      if (!abstractUser.isLocaleSet())
      {
        Locale myLocale = getHttpLocale(request);
        if (myLocale != null)
          abstractUser.setLocale(myLocale);
      }
      
      // To initialize user timezoneOffset with browser timezoneOffset, if method
      // IUser.getTimezoneOffset() has not been overwritten
      if (!abstractUser.isTimezoneSet())
      {
        String timezoneOffset = request.getParameter("timezoneOffset");
        if(timezoneOffset!=null)
          abstractUser.setTimezoneOffset(NumberUtils.stringToInt(timezoneOffset));
      }
    }

    this.host = request.getHeader("Host");
    this.baseUrl = Bootstrap.getBaseUrl(request);
    
    this.clientHostName = request.getRemoteHost();
    this.clientHostAddr = request.getRemoteAddr();
  }

  /**
   * Returns the jACOB web application base URL like <code>http://hostname:port/jacob/</code>
   * @return the jACOB web application base URL
   */
  public String getBaseUrl()
  {
    return this.baseUrl;
  }
  
  public static final char SESSION_APPLICATION_KEY_SEPARATOR = '$';
  
  protected static String getSessionIdFromApplicationId(String browserId)
  {
    if (browserId != null)
    {
      int idx = browserId.indexOf(SESSION_APPLICATION_KEY_SEPARATOR);
      if (idx > 0)
      {
        return browserId.substring(0, idx);
      }
    }
    throw new RuntimeException("Invalid application id: " + browserId);
  }
  
	/**
   * Retrieves the jACOB session object out of a http session object.
   * 
   * @param request the http request object
   * @return the client session or <code>null</code>, if the user is already logged out
   */
  public static HTTPClientSession get(HttpServletRequest request)
  {
    return get(request.getSession(), request.getParameter("browser"));
  }
  
  /**
   * Retrieves the jACOB session object out of a http session object.
   * 
   * @param session the http session
   * @param browserId the application id to use
   * @return the client session or <code>null</code>, if the user is already logged out
   */
  public static HTTPClientSession get(HttpSession session, String browserId)
  {
    if (browserId == null)
      return null;

    synchronized (session)
    {
      return (HTTPClientSession) session.getAttribute(getSessionIdFromApplicationId(browserId));
    }
  }
  
  private static final String CLIENT_SESSION_KEY = "jacob_sessions";
  
  /**
   * Tries to retrieve an already existing jACOB client session for the given application object and 
   * user.
   * 
   * @param session
   * @param appDef  the required application id
   * @param userId  the required userId 
   * @return
   */
  public static HTTPClientSession get(HttpSession session, IApplicationDefinition appDef, String userId) throws GeneralSecurityException
  {
    if (userId == null)
      return null;
    
    IUserFactory userFactory = UserManagement.getUserFactory(appDef);
    try
    {
      // find user because the userId might be treated case insensitive by the
      // user factory of the application
      return get(session, appDef, userFactory.findUser(userId));
    }
    catch (UserNotExistingException ex)
    {
      // ignore
      return null;
    }
  }
  
  public static HTTPClientSession get(HttpSession session, IApplicationDefinition appDef, IUser user)
  {
    if (user == null)
      return null;
    
    synchronized (session)
    {
      Map clientSessionMap = (Map)session.getAttribute(CLIENT_SESSION_KEY);
      if (clientSessionMap == null)
        return null;
      
      Iterator iter = clientSessionMap.values().iterator();
      while (iter.hasNext())
      {
        HTTPClientSession clientSession = (HTTPClientSession) iter.next();
        IApplicationDefinition other = clientSession.getApplicationDefinition();
        if (appDef.getName().equals(other.getName()) && appDef.getVersion().equals(other.getVersion()) && clientSession.getUser().getLoginId().equals(user.getLoginId()))
        {
          return clientSession;
        }
      }
    }
    
    // no active client session found for that application
    return null;
  }
  
  /**
   * Register this jACOB client session at the HTTP session.
   * 
   * @param session http session
   */
  public final void register(HttpSession session)
  {
    if (logger.isDebugEnabled())
      logger.debug("Registering jACOB session '" + getId() + "'");
    
    synchronized (session)
    {
      Map clientSessionMap = (Map) session.getAttribute(CLIENT_SESSION_KEY);
      if (clientSessionMap == null)
      {
        clientSessionMap = new HashMap();
        session.setAttribute(CLIENT_SESSION_KEY, clientSessionMap);
      }
      clientSessionMap.put(getId(), this);
      
      session.setAttribute(getId(), this);
    }
  }
  
  /**
   * Remove the jACOB session used by the given request.
   * 
   * @param request
   */
  public static void remove(HttpServletRequest request)
  {
    String browserId = request.getParameter("browser");
    if (browserId == null)
    {
      return;
    }
    
    remove(HTTPClientSession.getSessionIdFromApplicationId(browserId), request.getSession());
  }
  
  private static void remove(String sessionId, HttpSession session)
  {
    if (logger.isDebugEnabled())
      logger.debug("Removing jACOB session '" + sessionId + "'");
    
    synchronized (session)
    {
      Map clientSessionMap = (Map) session.getAttribute(CLIENT_SESSION_KEY);
      if (clientSessionMap != null)
      {
        clientSessionMap.remove(sessionId);
      }
      
      if (session.getAttribute(sessionId) == null)
        logger.warn("jACOB session '" + sessionId + "' not anymore registered in HTTP session!");
      else
        session.removeAttribute(sessionId);
    }
  }
  
  public synchronized HTTPApplication createApplication() throws Exception
  {
    HTTPApplication app = (HTTPApplication) ApplicationProvider.create(user, getApplicationFactory(), getApplicationDefinition());
    browser2Application.put(app.getHTTPApplicationId(), app);
    app.setSession(this);
    HTTPClientContext context = createContext(user, app, app.getHTTPApplicationId());
    Context.setCurrent(context);
    
    // Es wurde nur ein neues Fenster erzeugt. Der logon Hook darf nicht nochmal aufgerufen
    // werden.
    if (browser2Application.size() > 1)
      app.setLoginHookCalled();
    
    return app;
  }
  
  public synchronized IApplication getApplication(String browserId) throws Exception
  {
    if (browserId == null)
      return null;

    // check if we get an timeout browserId. Redirect the user to the login screen
    //
    if(invalidBrowserIds.contains(browserId))
      throw new TimeoutApplicationException(); 
    
    // check if the admin has logout the user via administration
    // application
    //
    if (this.httpSession != null)
    {
      // This is a session in usage and must have an entry in the DB, i.e.
      // we do not do a database count here, because of performance and we
      // do not want an entry in "Show SQL" for each server request.
      // So we ask the ClusterManager  :-)
      //
      if (!ClusterManager.isActiveUserSession(this.sessionId))
      {
        // Die Session wurde vom Administrator verworfen, dann auch die Session schließen
        remove(this.sessionId, this.httpSession);
        
        throw new TerminatedSessionException();
      }
    }
    
    return (HTTPApplication) browser2Application.get(browserId);
  }

  /**
   * Diese Methode wird durch Application.close() aufgerufen, um eine Fenster
   * (Browser) von der Session abzumelden. Sofern es das letzte Fenster ist,
   * wird auch die Session geschlossen.
   * 
   * @param browserId
   * @throws Exception
   */
  public synchronized void unregisterClosedApplication(String browserId)
  {
    // Session schon geschlossen?
    if (this.httpSession == null)
      return;

    // schon abgemeldet?
    if (browser2Application.remove(browserId) == null)
      return;

    // sind wir die letzte Applikation der jACOB session?
    if (browser2Application.size() == 0)
    {
      // dann auch die Session schließen
      remove(this.sessionId, this.httpSession);
    }
  }

	/**
   * Tries to fetch locale information from http request.
   * 
   * @param request
   *          http request
   * @return Desired locale or <code>null</code>, if language info could not
   *         be fetched.
   */
  public static Locale getHttpLocale(HttpServletRequest request)
  {
    String language = request.getHeader("Accept-Language");
    if (null != language)
    {
      // fetch language info from http header
      //
      int pos = language.indexOf(",");
      if (pos != -1)
      {
        // TODO: How to support multiple language settings?
        language = language.substring(0, pos);
      }
      language.trim();
      
      // get locale from cache
      //
      synchronized (httpLanguage2Locale)
      {
        Locale locale = (Locale) httpLanguage2Locale.get(language);
        if (locale == null)
        {
          try
          {
            // The language can be en-us. Split it and create a valid language
            // object
            //
            String[] langParts = StringUtils.split(language, "-");
            locale = new Locale(langParts[0], langParts.length > 1 ? langParts[1] : "");
          }
          catch (Exception ex)
          {
            logger.warn("Fetching locale from HTTP request failed: " + ex.toString());
          }

          // put locale in cache
          httpLanguage2Locale.put(language, locale);
        }
        return locale;
      }
    }
    else
    {
      logger.debug("Fetching locale from HTTP request failed: Parameter 'Accept-Language' is missing");
    }
    return null;
  }

	public final String getId()
	{
		return this.sessionId;
	}

	/**
	 * The hostname to which the client has been connected. This is required for the
	 * different session/auth handling.<br>
	 * <br>
	 * It is possible that the client has been connected to <code>host</code> or <code>host.domain</code>
	 * <code>ip-address</code>. This method returns the name of the host (my name itself).
	 * <br>
	 * 
	 * @return The name of the host
	 */
	public final String getHost()
	{
	  return host;
	}


	/*
	 * (non-Javadoc)
	 * @see de.tif.jacob.core.Session#getClientAddress()
	 */
  public String getClientAddress()
  {
    return this.clientHostAddr;
  }
  
  protected final HttpSession getHttpSession()
  {
    return httpSession;
  }
  
  public String getApplicationRootWebPath()
  {
    if (this.applicationRootWebPath == null)
      throw new IllegalStateException();
    return this.applicationRootWebPath;
  }
  
  /**
	 * @return Returns the lastAccess.
	 */
	public final Date getLastAccess() 
	{
		return lastAccess;
	}
	
	/**
	 * @param lastAccess The lastAccess to set.
	 */
	public final void setLastAccess(Date lastAccess) 
	{
		this.lastAccess = lastAccess;
	}
	
	/**
	 * Close all dead applications. A dead application is a client window which has 
	 * not send a keepAlive signal to the IApplication instance on the server. 
	 */
	public final synchronized void freeUnusedResources()
	{
		// close all dead IApplication instances
		//
	  Map newBrowser2Application=new HashMap();
	  // clone the keySet to avoid conccurent modification exceptions.
	  // This happens if app.close() will be called in this method an more than one application
	  // are stored in the session.
	  //
		Iterator iter = new HashSet(browser2Application.keySet()).iterator();
		while (iter.hasNext())
		{
		  String browserId = (String)iter.next();
		  HTTPApplication app = (HTTPApplication) browser2Application.get(browserId);
		  
		  // the application has to be closed, if refresh time has expired or application is not active 
		  // anymore
		  if (app.getSecondsBeforeDestroy() <= 0 || !DeployManager.isActiveApplication(app.getApplicationDefinition()))
		  {  
		    // save the invalid browserId. Invalid/timeout clients will be redirected to
		    // an timeout page.
		    //
		    invalidBrowserIds.add(browserId);
		    
		    // close and free all assigned resources of the application.
		    if(!app.isClosed())
		      app.close();
		  }
		  else
		  {
		    newBrowser2Application.put(browserId,app);
		  }
		}
		
		// We must close the session, if no active application exists anymore.
    // Note: We should not close the session during intermediate
    // logon process, i.e. session exists but applications are created
    // afterwards!
    boolean mustSessionClose = browser2Application.size() != 0 && newBrowser2Application.size() == 0;
    browser2Application = newBrowser2Application;

		// close all dead GenericDialogs instances
		//
		Map newid2Dialog=new HashMap();
		iter = id2Dialog.keySet().iterator();
		while (iter.hasNext())
		{
		  String dialogId = (String)iter.next();
		  HTTPGenericDialog dialog = (HTTPGenericDialog) id2Dialog.get(dialogId);
		  if(dialog.getSecondsBeforeTimeout()>0)
		  {
		    newid2Dialog.put(dialogId,dialog);
		  }
		}
		id2Dialog = newid2Dialog;
		
		// destroy the session itself if we didn't receive a keep alive
		//
		if (httpSession != null && (mustSessionClose || secondsBeforeSessionDestroy <= 0))
		{
		  try
      {
        httpSession.removeAttribute(this.sessionId);
      }
      catch (IllegalStateException e)
      {
        // Falls der Server im hypernate Modus war, kann es passieren, dass die Session
        // invalid ist. Im Normalfall wird die session vorher vom jACOB invalidiert.
        // Ignore!
      }
		  // The httSession will be removed from the application server. 
		  // We remove only the reference to the session.
		  //
		  httpSession=null;
		}
	}
	

	/**
	 * Returns all living applications instances in this Session. For each client browser 
	 * one IApplication created.
	 * 
	 * @return Collection[IApplication]
	 */
	public final synchronized Collection getApplications()
	{
	  return new ArrayList(browser2Application.values());
	}

	/**
	 * Returns all living dialog instances in this Session. 
	 * 
	 * @return Collection[GenericDialog]
	 */
	public final synchronized Collection getDialogs()
	{
	  return new ArrayList(id2Dialog.values());
	}

	/**
	 * Refresh the keep alive for the hands over browserId.<br>
	 * The keep alive <b>must</b> be called in fixed time intervalls.
	 * The data structur for the browser window (user client) will be removed if 
	 * the signal are absence. The correponding user window is now invalid.
	 *  
	 * @param browserId the unique id of the browser (client) window or a dialog.
	 */
	public synchronized void sendKeepAlive(String guid)
	{
	  // we use the keep alive to refresh the session itself
	  //
	  secondsBeforeSessionDestroy =Property.TIMEOUTINTERVAL_APPLICATION.getIntValue();
	  
	  if(ALERT_KEY.equals(guid) && alertVisible==true)
	  {  
	    secondsBeforeAlertDestroy =Property.TIMEOUTINTERVAL_DIALOG.getIntValue();
	    logger.debug("ALERT keep alive........................");
	    return;
	  }
	  
	  HTTPApplication app = (HTTPApplication) browser2Application.get(guid);
	  
	  // Refresh the timeout timer for the application.
	  // The countdown of the counter will be handled in an background ClientSession scheduler job.
	  // The job removes the application instance if secondsBeforeTimeout==0.
	  //
	  if(app!=null)
	  {  
	    app.setSecondsBeforeDestroy(Property.TIMEOUTINTERVAL_APPLICATION.getIntValue());//Application.TIMEOUT_INTERVALL_SECONDS;
	    return;
	  }
	  
	  // Refresh the timeout timer for the application.
	  // The countdown of the counter will be handled in an background ClientSession scheduler job.
	  // The job removes the application instance if secondsBeforeTimeout==0.
	  //
	  HTTPGenericDialog dialog = (HTTPGenericDialog) id2Dialog.get(guid);
	  if(dialog!=null)
	  {  
	    dialog.setSecondsBeforeTimeout(Property.TIMEOUTINTERVAL_DIALOG.getIntValue());
	    return;
	  }
	  
	  // FREEGROUP: Tracelevel sollte auf info gesetzt werden, sobald diese Nachricht nur einmal
	  // (pro Session/Fenster) kommt und entsprechend behandelt wird)
	  logger.debug("unmatched keep alive ["+guid+"] for dialog/application ");
	}

	public final synchronized void register(ManagedResource resource)
	{
	  sessionManagedResources.add(resource);
	}
	
	public final synchronized void unregister(ManagedResource resource)
	{
	  sessionManagedResources.remove(resource);
	}

	/**
	 * Properties welche für die Sessionlaufzeit gehalten werden. Diese Properties
	 * werden an dem Context.setPlropertiesForSession(...) gesetzt und durch den Context
	 * an die HTTPClientSession weiter gereicht.
	 * 
	 * @param resource
	 */
	public final synchronized void setPropertyForSession(Object key, Object value)
	{
	  if(value==null)
	    sessionProperties.remove(key);
	  else
	    sessionProperties.put(key, value);
	}
	
	public final synchronized Object getPropertyForSession(Object key)
	{
	  return sessionProperties.get(key);
	}

  /*
	 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 * In valueBound(...) eine Exception zu werfen bringt nichts! Diese wird vom AppServer nur geschluckt/geloggt.
	 * Das Teil steckt danach trotzdem in der Session!!!!!
	 * 
	 * Hack: In der login.jsp prüfen ob ein Fehler aufgetreten ist.
	 */
	public final synchronized void valueBound(HttpSessionBindingEvent event)
	{
    if (logger.isDebugEnabled())
      logger.debug("Bounding jACOB session '" + sessionId + "'");
    
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
	  IDataTransaction transaction = accessor.newTransaction();
	  try
		{
	    httpSession =event.getSession();
      
      // calculate application web path
      IApplicationDefinition appDef = getApplicationDefinition();
      applicationRootWebPath = httpSession.getServletContext().getRealPath("/application/"+appDef.getName()+"/"+appDef.getVersion().toShortString());
      
			// Register the ClientSession in the internal database
	    //
			IDataTable table = accessor.getTable(Activesession.NAME);
	    
			// Allow login always for admin application even if no license existing or maximum number
			// of concurrent users exceeded.
			//
			if (!DeployManager.ADMIN_APPLICATION_NAME.equals(getApplicationDefinition().getName()))
      {
        License license = LicenseFactory.getLicenseManager().getLicense();
        if (license == null)
          throw new LicenseException(new CoreMessage("NO_LICENSEKEY_FOUND").print(getUser().getLocale()));

        if (ClusterManager.getUserSessionCount() >= license.getUserCount())
          throw new LicenseException(new CoreMessage("NO_LICENSE_FOR_MORE_USERS").print(getUser().getLocale()));
      }
			
			IDataTableRecord record = table.newRecord(transaction);
			record.setStringValue(transaction, Activesession.applicationname, getApplicationDefinition().getName());
			record.setStringValue(transaction, Activesession.applicationversion, getApplicationDefinition().getVersion().toString());
			record.setStringValue(transaction, Activesession.userid, this.user.getLoginId());
			record.setStringValue(transaction, Activesession.sessionid, getId());
			record.setStringValue(transaction, Activesession.clienthostname, this.clientHostName);
			record.setStringValue(transaction, Activesession.clienthostaddress, this.clientHostAddr);
			record.setStringValue(transaction, Activesession.serverurl, getBaseUrl());

			// create all required scheduler jobs for this session
			//
			SchedulerService.createJobs(this);
			
	    // register session to refresh runtime property cache in case property
	    // values are changed (e.g. from the jACOB admin console)
	    //
	    Property.registerNotifyee(this);
	    
			// on success commit the new session in the registry.
			//
			transaction.commit();
	  }
		catch (LicenseException e)
		{
			createException = e;
		}
		catch (Exception e)
		{
			logger.error("User registration failed", e);
			createException= new RuntimeException(new CoreMessage("UNABLE_TO_REGISTER_USERSESSION").print(getUser().getLocale()), e);
		}
		finally
		{
		  transaction.close();
		}		
	}

  /**
   * @param alertVisible The alertVisible to set.
   */
  public synchronized final void setAlertVisible(boolean alertVisible)
  {
    secondsBeforeAlertDestroy=Property.TIMEOUTINTERVAL_DIALOG.getIntValue();//TIMEOUT_INTERVALL_SECONDS;
    this.alertVisible = alertVisible;
  }
  
  /**
   * @param secondsBeforeTimeout The secondsBeforeTimeout to set.
   */
  public synchronized final void decrementSecondsBeforeDestroy(int amount)
  {
    this.secondsBeforeSessionDestroy-=amount;
    if(logger.isDebugEnabled())logger.debug("ClientSession time out:"+secondsBeforeSessionDestroy);
  }
  
  
  /**
   * @param secondsBeforeTimeout The secondsBeforeTimeout to set.
   */
  public synchronized final void decrementAlertBeforeDestroy(int amount)
  {
    this.secondsBeforeAlertDestroy-=amount;
    if(secondsBeforeAlertDestroy<=0)
      setAlertVisible(false);
    if(logger.isDebugEnabled())logger.debug("Alert time out:"+secondsBeforeAlertDestroy+" alert ist visible:"+isAlertVisible());
  }

  /**
   * @return Returns the alertVisible.
   */
  public synchronized final boolean isAlertVisible()
  {
    return alertVisible;
  }

  /*
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public synchronized final void valueUnbound(HttpSessionBindingEvent event)
	{
    if (logger.isDebugEnabled())
      logger.debug("Unbounding jACOB session '" + sessionId + "'");
    
	  // remove the reference from the session
	  //
	  httpSession=null;
	  
	  /////////////////////////////////////////////////////////
	  // close all assigned IApplication instances
	  //
	  /////////////////////////////////////////////////////////
	  try
    {
      Iterator iter = browser2Application.keySet().iterator();
      while (iter.hasNext())
      {
        HTTPApplication app = (HTTPApplication) browser2Application.get(iter.next());
      	if(!app.isClosed())
      	  app.close();
      }
    }
    catch (Throwable th)
    {
      ExceptionHandler.handle(th);
      // don't throw an exception. Try to remove all resource! 
    }

		/////////////////////////////////////////////////////////
		// terminate all created scheduler jobs
		// Note: Do this before unregistering session from
		//       internal database, because tasks might
		//       access the active session record and throw
		//       an exception, if already deleted.
		// 
		/////////////////////////////////////////////////////////
		try
    {
      SchedulerService.removeJobs(this);
    }
    catch (Throwable th)
    {
      ExceptionHandler.handle(th);
      // don't throw an exception. Try to remove all resource! 
    }

    /////////////////////////////////////////////////////////
		// unregister the session in the internal database
		//
    /////////////////////////////////////////////////////////
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTransaction transaction = accessor.newTransaction();
		try
		{
			// unregister for HSQL
      String sessionId = getId();
			IDataTable table = accessor.getTable(Activesession.NAME);
      table.qbeSetKeyValue(Activesession.sessionid, sessionId);
			/*int recnum =*/ table.searchAndDelete(transaction);
			transaction.commit();
      // passiert wenn ein Admin eine Session verwirft und danach der User einen Request ausführt
//      if (recnum != 1)
//      {
//        // Nur zur Überprüfung, ob hier der Sessioncounter richtig funktioniert
//        // Siehe Aufgabe 196
//        logger.warn("Removing session '" + sessionId + "' resulted in " + recnum + " entries deleted!");
//      }
		}
		catch (Throwable th)
		{
      // only report exception if jACOB has not been stopped so far
      if (!Bootstrap.isDestroyed())
        ExceptionHandler.handle(th);
			// don't throw an exception. Try to remove all resource! 
		}
		finally
		{
		  transaction.close();
		}

    /////////////////////////////////////////////////////////
    // release all registered resources
    // 
    /////////////////////////////////////////////////////////
    try
    {
      Iterator iter =sessionManagedResources.iterator();
      while(iter.hasNext())
      {
        ManagedResource resource=(ManagedResource)iter.next();
        if(logger.isDebugEnabled())logger.debug("SESSION Release ManagedResource"+resource.getClass().getName());
        resource.release();
      }
      sessionManagedResources.clear();
    }
    catch (Throwable th)
    {
      ExceptionHandler.handle(th);
      // don't throw an exception. Try to remove all resource! 
    }
    
    /////////////////////////////////////////////////////////
    // unregister property notification
    // 
    /////////////////////////////////////////////////////////
    Property.unregisterNotifyee(this);
	}
  
  public final Throwable getCreateException()
  {
    return createException;
  }
  
  /* 
   * @see de.tif.jacob.screen.Session#getUser()
   */
  public final IUser getUser()
  {
    return user;
  }
  
  public final synchronized void addDialog(HTTPGenericDialog dialog)
  {
    id2Dialog.put(""+dialog.getId(),dialog);
    if(logger.isDebugEnabled())
      logger.debug("Application session has reference to ["+id2Dialog.size()+"] dialog objects.");
  }
 
  
  public final synchronized void removeDialog(HTTPGenericDialog dialog)
  {
    id2Dialog.remove(""+dialog.getId());
    if(logger.isDebugEnabled())
      logger.debug("Application session has reference to ["+id2Dialog.size()+"] dialog objects.");
  }

  
  public final synchronized HTTPGenericDialog getDialog(String hashCode )
  {
    return (HTTPGenericDialog)id2Dialog.get(hashCode);
  }
  
  public final synchronized void addAutosuggestProvider(GuiElement element, IAutosuggestProvider provider)
  {
  	id2Autosuggest.put(element.getEtrHashCode(),provider);
    if(logger.isDebugEnabled())
      logger.debug("Application session has reference to ["+id2Autosuggest.size()+"] AutosuggestProvider objects.");
  }
 
  public final synchronized void removeAutosuggestProvider(GuiElement element)
  {
    id2Autosuggest.remove(element.getEtrHashCode());
    if(logger.isDebugEnabled())
      logger.debug("Application session has reference to ["+id2Autosuggest.size()+"] autosuggest objects.");
  }
  
  public final synchronized void addAutosuggestValues(IAutosuggestProvider element, IAutosuggestProvider.AutosuggestItem[] values)
  {
  	autosuggest2Values.put(""+element.hashCode(),values);
    if(logger.isDebugEnabled())
      logger.debug("Application session has reference to ["+autosuggest2Values.size()+"] AutosuggestItem objects.");
  }
 
  public final synchronized void removeAutosuggestValues(IAutosuggestProvider element)
  {
  	autosuggest2Values.remove(""+element.hashCode());
    if(logger.isDebugEnabled())
      logger.debug("Application session has reference to ["+autosuggest2Values.size()+"] AutosuggestItem objects.");
  }
  
  public final synchronized IAutosuggestProvider.AutosuggestItem[] getAutosuggestValues(IAutosuggestProvider element )
  {
    return (IAutosuggestProvider.AutosuggestItem[])autosuggest2Values.get(""+element.hashCode());
  }
  
  public final synchronized IAutosuggestProvider getAutosuggestProvider(String etrHashCode )
  {
    return (IAutosuggestProvider)id2Autosuggest.get(etrHashCode);
  }
  

  /**
	 * 
	 * @param message
	 * @author Andreas Herz
	 */
	public final synchronized void addAsynchronHtml(String code)
	{
	  asynchronHtml.append(code); 
	}
	
	/**
	 */
	public final synchronized String fetchAsynchronHtml()
	{
	  String m = asynchronHtml.toString();
	  asynchronHtml = new StringBuffer(512);
    asynchronHtml.append("");
	  return m;
	}
	
	/**
	 * jACOB runtime properties for the user. The values will be cached for the current session.
	 * 
	 * @param key
	 * @return
	 */
  public String getRuntimeProperty(String key)
  {
    String value = systemRuntimeProperties.getProperty(key);
    // key wurde noch nie angefragt. Versuchen diesen von der Datenbank zu holen
    //
    if(value==null)
    {
      // the value has not been read before. Try to load them from the DB.
      value=RuntimeProperty.getValue(getApplicationDefinition(), this.getUser(), key);
      if(value==null)
        systemRuntimeProperties.put(key,NULL_ENTRY);
      else
        systemRuntimeProperties.put(key,value);
    }
    else if (value==NULL_ENTRY)
      value=null;
    
    return value;
  }
  
  public void  setRuntimeProperty(String key, int value)
  {
    setRuntimeProperty(key,Integer.toString(value));
  }
  
  public void  setRuntimeProperty(String key, boolean value)
  {
    setRuntimeProperty(key,Boolean.toString(value));
  }
  
  /**
   * jACOB runtime properties for the user. The values will be chached for the current session.
   * 
   * @param key
   * @param value
   */
  public void  setRuntimeProperty(String key, String value)
  {
    RuntimeProperty.setValue(getApplicationDefinition(), this.getUser(), key,value);
    if(value==null)
      value = NULL_ENTRY;
    systemRuntimeProperties.put(key,value);
  }
  
  public void onChange(Property property) throws Exception
  {
    // reset cache to enforce new read from persistent storage
    systemRuntimeProperties.remove(property.getName());
  }
  
  public synchronized boolean isAlertMustRefresh()
  {
    return alertMustRefresh;
  }
  
  public synchronized void setAlertMustRefresh(boolean alertMustRefresh)
  {
    this.alertMustRefresh = alertMustRefresh;
  } 
}
