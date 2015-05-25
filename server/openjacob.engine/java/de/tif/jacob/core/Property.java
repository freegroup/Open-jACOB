/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Tarragon GmbH
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
package de.tif.jacob.core;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.model.Japroperty;
import de.tif.jacob.core.model.Jproperty;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class Property
{
	static public transient final String RCS_ID = "$Id: Property.java,v 1.15 2010/01/20 02:04:26 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.15 $";
	
	static final transient Log logger = LogFactory.getLog(PropertyManagement.class);
	
	/**
	 * Property scope interface.
	 * 
	 * @author Andreas Sonntag
	 * @since 2.9
	 */
	private interface Scope
	{
	  boolean includesUserLevel();
	}
	
  /**
   * Property scope which covers all three property levels, i.e. engine, application and user level.
   */
  private static final Scope GLOBAL = new Scope()
  {
    public boolean includesUserLevel()
    {
      return true;
    }
  };
  
  /**
   * Property scope which covers engine and application level, but not the user level.
   */
  private static final Scope ENGINE = new Scope()
  {
    public boolean includesUserLevel()
    {
      return false;
    }
  };
  
  /**
   * Property scope which covers engine level.
   */
  private static final Scope ENGINE_PLUS_APPLICATION = new Scope()
  {
    public boolean includesUserLevel()
    {
      return false;
    }
  };
  
  // Map{String->Property}
  private static final Map properties = new HashMap();
  
  // Set{PropertyNotifyee}
  private static final Set propertyNotifyees = new HashSet();
  
  public static final Property BROWSER_COMMON_MAX_RECORDS = new Property("browser.common.max.records", "100", GLOBAL, "Common maximum of records displayed in browsers");
  public static final Property BROWSER_SYSTEM_MAX_RECORDS = new Property("browser.system.max.records", "1000", GLOBAL, "System maximum of records displayed in browsers");
  public static final Property REPORT_MAX_RECORDS         = new Property("report.max.records", "10000", GLOBAL, "Maximum number of records retrieved for reports");
  
  /**
   * Gibt an ob eine Installation / Applikation sich im DemoMode befindet. In Abhängigkeit von diesem
   * Schalter kann eine Applikation automatische Reset der Datenbank innerhalb einer gewissen Zeitspanne 
   * durchführen....oder eine Anwendung neu deployen.
   */
  public static final Property DEMO_MODE                     = new Property("runtime.demo", "false", ENGINE_PLUS_APPLICATION, "Flag to define whenever a system/application is running in a demo mode.");
  public static final Property RUNTIME_MERGE_APPLICATION     = new Property("runtime.merge.application", "false", ENGINE_PLUS_APPLICATION, "Flag to define if the runtime should merge all active applications to a single one, [true, false]");
  public static final Property APPLICATION_DEFAULT           = new Property("application.default", null, ENGINE, "Default application which should be preselected on login");
  /**
   * @deprecated use the jACOB Designer to define the scope of the DataAccessor instead
   */
  public static final Property DATA_ACCESSOR_SCOPE           = new Property("dataaccessor.scope", "domain", ENGINE_PLUS_APPLICATION, "The scope of a DataAccessor. Valid values [application, domain, form].");
  
  public static final Property USER_THEME_DEFAULT            = new Property("user.theme.default", "default", GLOBAL, "Default user theme to be used");
  public static final Property LANGUAGE_DEFAULT              = new Property("language.default", null, GLOBAL, "Default language to be used, e.g. en, de or fr");
  public static final Property COUNTRY_DEFAULT               = new Property("country.default", null, GLOBAL, "Default country to be used, e.g. US, GB, DE or FR");
  
  public static final Property CHECKINTERVAL_APPLICATION     = new Property("interval.check.application", "60", ENGINE_PLUS_APPLICATION, "Time interval in seconds to check application (browser) expiration of the validity.");
  public static final Property CHECKINTERVAL_DIALOG          = new Property("interval.check.dialog", "60", ENGINE_PLUS_APPLICATION, "Time interval in seconds to check dialog windows expiration of the validity.");
  public static final Property CHECKINTERVAL_ALERT           = new Property("interval.check.alert", "60", ENGINE_PLUS_APPLICATION, "Time interval in seconds to check alert messages.");
  
  public static final Property KEEPALIVEINTERVAL_APPLICATION = new Property("interval.keepalive.application", "10", ENGINE_PLUS_APPLICATION, "Time in seconds for the keep alive interval of the application window.");
  public static final Property KEEPALIVEINTERVAL_DIALOG      = new Property("interval.keepalive.dialog", "10", ENGINE_PLUS_APPLICATION, "Time in seconds for the keep alive interval of a dialog window.");
  
  public static final Property TIMEOUTINTERVAL_APPLICATION   = new Property("interval.timeout.application", "200", ENGINE_PLUS_APPLICATION, "The timeout interval in seconds for the application invalidation. The application must send a keep alive in this timeperiod");
  public static final Property TIMEOUTINTERVAL_DIALOG        = new Property("interval.timeout.dialog", "200", ENGINE_PLUS_APPLICATION, "The timeout interval in seconds for the application invalidation. The application must send a keep alive in this timeperiod");
  
  public static final Property GUI_DEBUG                     = new Property("gui.debug", "false", GLOBAL, "Enable/disable [true/false] tooltips for input elements. This is useful during the application development.");
  public static final Property WINDOW_TITLE_PREFIX           = new Property("gui.window.prefix", "jACOB - ", GLOBAL, "The prefix in the window title of each dialog or main window.", false);
  public static final Property FFBROWSER_INLINE              = new Property("gui.ffbrowser.inline", "false", GLOBAL, "Set to true if you want an inline foreignField browser. Set to [false] if you want a a popup window.");
  public static final Property WINDOW_NAVIGATION             = new Property("gui.navigation", "outlook", GLOBAL, "The type of the main navigation/menu.");
  public static final Property DIVIDER_POS                   = new Property("gui.divider.pos", "200", GLOBAL, "Position of the diver between the search browser and content.");
  /**
   * Switch to enable/disable web browser auto completion for text fields.
   * 
   * @since 2.8.11
   */
  public static final Property TEXT_AUTOCOMPLETE             = new Property("gui.text.autocomplete", "on", GLOBAL, "Perform web browser auto complete on text fields [on/off].");
  

  public static final Property SQL_HISTORY_THRESHOLD         = new Property("sql.history.threshold", "1000", ENGINE_PLUS_APPLICATION, "Max execution time (in milli seconds) to write an SQL statement to the performance history. If the execution duration longer than the name threshold the SQL command will be stored in the performance DB.");

  public static final Property YAN_PROTOCOL_SMS              = new Property("sms://"       , "true", ENGINE_PLUS_APPLICATION, "Set to [true] if the installation has a messaging channel for SMS. Set to [false] if the YAN doesn't support the SMS channel.");
  public static final Property YAN_PROTOCOL_HTMLEMAIL        = new Property("htmlemail://" , "true", ENGINE_PLUS_APPLICATION, "Set to [true] if the installation has a messaging channel for eMAIl. Set to [false] if the YAN doesn't support the eMAIL channel.");
  public static final Property YAN_PROTOCOL_EMAIL            = new Property("email://"     , "true", ENGINE_PLUS_APPLICATION, "Set to [true] if the installation has a messaging channel for eMAIl. Set to [false] if the YAN doesn't support the eMAIL channel.");
  public static final Property YAN_PROTOCOL_FAX              = new Property("rightfax://"  , "true", ENGINE_PLUS_APPLICATION, "Set to [true] if the installation has a messaging channel for FAX. Set to [false] if the YAN doesn't support the FAX channel.");
  public static final Property YAN_PROTOCOL_ALERT            = new Property("alert://"     , "true", ENGINE_PLUS_APPLICATION, "Set to [true] if the installation has a messaging channel for ALERT. Set to [false] if the YAN doesn't support the ALERT channel.");

  public static final Property TOP_CPU_THRESHOLD             = new Property("admin.top.threshold", "25", ENGINE, "Threshold value in percentage. CPU usage over this threshold will be reported in the jACOB performance database.");
  public static final Property TOP_INTERVAL                  = new Property("admin.top.interval", "120", ENGINE, "Interval in seconds. jACOB inspects the CPU usage in this interval.");
  public static final Property MEMORY_WATCH_THRESHOLD        = new Property("admin.memory.watch.threshold", "40", ENGINE, "Threshold value in percentage. Free total memory percentages below this threshold will be reported in the jACOB performance database.");
  public static final Property MEMORY_WATCH_INTERVAL         = new Property("admin.memory.watch.interval", "300", ENGINE, "Interval in seconds. jACOB inspects the memory usage in this interval.");
  public static final Property MEMORY_WATCH_GC               = new Property("admin.memory.watch.gc", "off", ENGINE, "Perform a full garbage collection when inspecting memory usage [on/off].");

  public static final String   UI_TOOLBARBOTTON_PREFIX       = "ui.toolbar.";
  public static final Property UI_TOOLBAR_EXIT               = new Property(UI_TOOLBARBOTTON_PREFIX+"exit"           , "true", GLOBAL, "Visibility of the toolbar button [exit]");
  public static final Property UI_TOOLBAR_CLEARALL           = new Property(UI_TOOLBARBOTTON_PREFIX+"clearAll"       , "true", GLOBAL, "Visibility of the toolbar button [clear All]");
  public static final Property UI_TOOLBAR_CLEARFOCUS         = new Property(UI_TOOLBARBOTTON_PREFIX+"clearFocus"     , "true", GLOBAL, "Visibility of the toolbar button [Clear Focus]");
  public static final Property UI_TOOLBAR_CLEARFORM          = new Property(UI_TOOLBARBOTTON_PREFIX+"clearForm"      , "true", GLOBAL, "Visibility of the toolbar button [Clear Form]");
  public static final Property UI_TOOLBAR_CREATEREPORT       = new Property(UI_TOOLBARBOTTON_PREFIX+"createReport"   , "true", GLOBAL, "Visibility of the toolbar button [Create Report]");
  public static final Property UI_TOOLBAR_SHOWREPORTS        = new Property(UI_TOOLBARBOTTON_PREFIX+"showReports"    , "true", GLOBAL, "Visibility of the toolbar button [Show Report]");
  public static final Property UI_TOOLBAR_THEMESELECT        = new Property(UI_TOOLBARBOTTON_PREFIX+"themeSelect"    , "true", GLOBAL, "Visibility of the toolbar button [Themes]");
  public static final Property UI_TOOLBAR_SHOWSQL            = new Property(UI_TOOLBARBOTTON_PREFIX+"showSql"        , "true", GLOBAL, "Visibility of the toolbar button [Show SQL]");
  public static final Property UI_TOOLBAR_ALERT              = new Property(UI_TOOLBARBOTTON_PREFIX+"alert"          , "true", GLOBAL, "Visibility of the toolbar button [Alert]");
  public static final Property UI_TOOLBAR_MESSAGING          = new Property(UI_TOOLBARBOTTON_PREFIX+"messaging"      , "true", GLOBAL, "Visibility of the toolbar button [Messaging]");
  public static final Property UI_TOOLBAR_USERPASSWORD       = new Property(UI_TOOLBARBOTTON_PREFIX+"userPassword"   , "true", GLOBAL, "Visibility of the toolbar button [Password]");
  public static final Property UI_TOOLBAR_NEWWINDOW          = new Property(UI_TOOLBARBOTTON_PREFIX+"newWindow"      , "true", GLOBAL, "Visibility of the toolbar button [New Window]");
  public static final Property UI_TOOLBAR_ABOUT              = new Property(UI_TOOLBARBOTTON_PREFIX+"about"          , "true", GLOBAL, "Visibility of the toolbar button [About]");
  
  public static final String   UI_SIDEBAR_PREFIX             = "ui.sidebar.";
  public static final Property UI_SIDEBAR_MANAGE_CRITERIA    = new Property(UI_SIDEBAR_PREFIX+"manage_criteria", "true", GLOBAL, "Visibility of the sidebar [Manage Search Criteria]");

  public static final Property SHORTCUT_FOREIGNFIELD         = new Property("shortcut.foreignfield", "113", GLOBAL, "The shortcut for fast search on the foreign Field. 113=F2, 117=F6...");

  public static final Property BALANCER_SWITCH               = new Property("cluster.balancer", "off", ENGINE, "Set to [on] to enable load balancer. Set to [off] to switch off.");
  public static final Property BALANCER_STRATEGY             = new Property("cluster.balancer.strategy", null, ENGINE, "The strategy for the load balancer.");

  /**
   * Type mapping version to use for WSDL on SOAP entrypoints.
   * 
   * @since 2.8.11
   */
  public static final Property SOAP_TYPE_MAPPING_VERSION     = new Property("soap.type.mapping.version", "1.3", ENGINE_PLUS_APPLICATION, "Type mapping version to use for WSDL on SOAP entrypoints [1.0,1.1,1.2,1.3].");

  /**
   * Property for the base url of the jacob web context.
   * <p>
   * Note: By default there is no need to explicitly set this property value,
   * since the base URL is determined automatically. Nevertheless, in certain
   * situations this property must be set, e.g. jACOB runs within a Tomcat
   * instance behind an Apache Webserver.
   * 
   * @since 2.8.6
   */
  public static final Property BASE_URL                      = new Property("url.base", null, ENGINE, "The jacob base URL, e.g. \"http://hostname:port/jacob/\". Keep [null] to determine automatically.");

  private final String name;
  private final boolean adjustValue;
	private final String defaultValue;
  private final String initialDescription;
  private final Scope scope;
	private String value;
	
	// Map[String->String] application specific settings
	private Map applicationValues;
  
  private Property(String name, String defaultValue, Scope scope, String initialDescription, boolean adjustValue)
  {
    this.name = name;
    this.adjustValue = adjustValue;
    this.defaultValue = adjustValue(defaultValue);
    this.value = adjustValue(defaultValue);
    this.initialDescription = initialDescription;
    this.scope = scope;
    
    register(this);
  }
  
  private Property(String name, String defaultValue, Scope scope, String initialDescription)
  {
    this(name, defaultValue, scope, initialDescription, true);
  }
  
	private String adjustValue(String value)
  {
    if (this.adjustValue && value != null)
    {
      value = value.trim();
      if (value.length() == 0)
        return null;
    }
    return value;
  }
	
	public long getLongValue()
	{
		return Long.parseLong(getValue());
	}
	
	public int getIntValue()
	{
		return Integer.parseInt(getValue());
	}
	
	public BigDecimal getDecimalValue()
	{
		return new BigDecimal(getValue());
	}
	
	public boolean getBooleanValue()
	{
		return Boolean.valueOf(getValue()).booleanValue();
	}
	
	/**
   * Enforces that property values are refetched from datasource.
   */
	public void refresh()
  {
	  if (logger.isInfoEnabled())
      logger.info("refreshing property '" + this.name + "'");
	  
    synchronized (this)
    {
      try
      {
        IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();

        // read jacob property
        IDataTable jpropertyTable = accessor.getTable(Jproperty.NAME);
        jpropertyTable.qbeSetKeyValue(Jproperty.name, this.name);
        jpropertyTable.search();
        if (jpropertyTable.recordCount() == 1)
        {
          IDataTableRecord record = jpropertyTable.getRecord(0);
          setValueInternal(record.getStringValue(Jproperty.value));
        }

        // clear application values first
        if (this.applicationValues != null)
          this.applicationValues.clear();
        
        // read jacob application properties
        IDataTable japropertyTable = accessor.getTable(Japroperty.NAME);
        japropertyTable.qbeSetKeyValue(Japroperty.name, this.name);
        japropertyTable.search();
        for (int i = 0; i < japropertyTable.recordCount(); i++)
        {
          IDataTableRecord record = japropertyTable.getRecord(i);
          setValueInternal(record.getStringValue(Japroperty.applicationname), record.getStringValue(Japroperty.value));
        }
      }
      catch (Exception ex)
      {
        throw new RuntimeException("Could not refresh property '" + this.name + "'", ex);
      }
    }

    // always notify listeners, because the caller of this method has to ensure
    // that really a change exists
    notifyListeners();
  }
	
	/**
   * Returns the property value.
   * 
   * @return the property value or <code>null</code> if no value exists
   */
	public String getValue()
  {
    return getValue(Context.getCurrent());
  }
	
	public synchronized String getValue(Context context)
  {
    if (context.hasApplicationDefinition())
    {
      if (this.scope.includesUserLevel() && context instanceof HTTPClientContext)
      {
        Session session = ((HTTPClientContext) context).getSession();
        if (session instanceof HTTPClientSession)
        {
          HTTPClientSession httpClientSession = (HTTPClientSession) session;
          return httpClientSession.getRuntimeProperty(this.name);
        }
      }
      return getValue(context.getApplicationDefinition());
    }
    return this.value;
  }
	
  public synchronized String getValue(IApplicationDefinition applicationDefinition)
  {
    return getValue(applicationDefinition.getName());
  }
  
  /**
   * @param applicationName
   * @return
   * @since 2.8.5
   */
  public synchronized String getValue(String applicationName)
  {
    if (null != this.applicationValues && applicationName != null)
    {
      if (this.applicationValues.containsKey(applicationName))
      {
        return (String) this.applicationValues.get(applicationName);
      }
    }
    return this.value;
  }
  
  /**
   * For internal use only!
   * 
   * @param applicationName
   * @param value
   */
  public void setValue(String applicationName, String value)
  {
    // this method should not be synchronized to avoid deadlocks
    if (setValueInternal(applicationName, value))
    {
      notifyListeners();
    }
  }
  
  private synchronized boolean setValueInternal(String applicationName, String value)
  {
    if (null == this.applicationValues)
    {
      this.applicationValues = new HashMap();
    }
    
    value = adjustValue(value);
    if (null == value)
      return null != this.applicationValues.remove(applicationName);
    
    Object oldValue = this.applicationValues.put(applicationName, value);
    return !value.equals(oldValue);
  }
  
  /**
   * For internal use only!
   * 
   * @param value
   */
  public void setValue(String value)
  {
    // this method should not be synchronized to avoid deadlocks
    if (setValueInternal(value))
    {
      notifyListeners();
    }
  }
  
  private synchronized boolean setValueInternal(String value)
  {
    String oldValue = this.value;
    this.value = adjustValue(value);
    return !StringUtil.saveEquals(this.value, oldValue);
  }
  
  private void notifyListeners()
  {
    synchronized (propertyNotifyees)
    {
      // call all listeners, that the application has been changed or a new
      // application has been deployed.
      //
      Iterator iter = propertyNotifyees.iterator();
      while (iter.hasNext())
      {
        PropertyNotifyee notifyee = (PropertyNotifyee) iter.next();
        try
        {
          notifyee.onChange(this);
        }
        catch (Exception ex)
        {
          ExceptionHandler.handle(ex);
        }
      }
    }
  }
  
  /*
  protected synchronized void reset()
  {
  	this.value = this.defaultValue;
  	this.applicationValues = null;
  }
  */
  
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return Returns the defaultValue.
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}

  private static void register(Property property)
  {
    synchronized (properties)
    {
      properties.put(property.getName(), property);
    }
  }

  /**
   * For internal use only
   * 
   * @param name
   * @return
   */
  public static Property getProperty(String name)
  {
    synchronized (properties)
    {
      return (Property) properties.get(name);
    }
  }

  /**
   * Adds an listener to the property management. If a property will be changed,
   * the listener will be called.
   * 
   * @param notifyee
   *          the listener to register
   */
  public static void registerNotifyee(PropertyNotifyee notifyee)
  {
    boolean success;
    synchronized (propertyNotifyees)
    {
      success = propertyNotifyees.add(notifyee);
    }
    if (!success)
      logger.warn("Notifyee '" + notifyee + "' already registered");
  }
  
  /**
   * Removes an listener from the property management.
   * 
   * @param notifyee
   *          the listener to unregister
   * @since 2.9
   */
  public static void unregisterNotifyee(PropertyNotifyee notifyee)
  {
    boolean success;
    synchronized (propertyNotifyees)
    {
      success = propertyNotifyees.remove(notifyee);
    }
    if (!success)
      logger.warn("Notifyee '" + notifyee + "' not registered");
  }
  
  /**
   * For internal use only
   * 
   * @param name
   * @return
   */
  public static String getPropertyValue(IApplicationDefinition applicationDefinition, String name)
  {
    Property property = getProperty(name);
    return property == null ? null : property.getValue(applicationDefinition);
  }

  protected static Set getPropertyNames()
  {
    synchronized (properties)
    {
      return new HashSet(properties.keySet());
    }
  }

	/**
	 * @return Returns the initialDescription.
	 */
	public String getInitialDescription()
	{
		return initialDescription;
	}

}
