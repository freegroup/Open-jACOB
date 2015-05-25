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
/*
 * Created on 14.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.PropertyNotifyee;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class I18N implements PropertyNotifyee
{
	static public transient final String RCS_ID = "$Id: I18N.java,v 1.6 2010/06/29 12:39:05 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.6 $";
	
	static protected final transient Log logger = LogFactory.getLog(I18N.class);
	
  /**
   * There should be no application with such a name :-)
   */
  private static final String NONE_APPLICATION_NAME = "%%NONE%%";
  
	private static final String APPLICATION_LABEL_RESOURCE_BASE_NAME = "jacob.resources.i18n.applicationLabel";
	
	private static final String APPLICATION_MESSAGE_RESOURCE_BASE_NAME = "jacob.resources.i18n.applicationMessage";
	
	private static final String CORE_RESOURCE_BASE_NAME = "jacobMessages";
	
	/**
	 * Map(IApplicationDefinition -> Map(Locale.toString() -> ResourceBundle))
	 */
	private static final WeakHashMap applicationLabelResourceBundles = new WeakHashMap();
	
	/**
	 * Map(IApplicationDefinition -> Map(Locale.toString() -> ResourceBundle))
	 */
	private static final WeakHashMap applicationMessageResourceBundles = new WeakHashMap();
	
	/**
	 * Map(applicationName -> Map(Locale.toString() -> ResourceBundle))
	 * 
	 * Note: We must also consider the application because we might have different
	 *       default language settings for different applications!
	 */
	private static final Map coreResourceBundles = new HashMap();
	
	private static final Set supportedCalendarLanguages = new HashSet();
	
	static
	{
	  // Add here all languages to support webapp/javascript/lang/calendar-XX.js
	  supportedCalendarLanguages.add("de");
	  supportedCalendarLanguages.add("en");
	  supportedCalendarLanguages.add("fr");
	  supportedCalendarLanguages.add("nl");
	  supportedCalendarLanguages.add("sp");
	  supportedCalendarLanguages.add("es");
	  supportedCalendarLanguages.add("it");
	  
	  // create the one and only instance as property listener
	  Property.registerNotifyee(new I18N());
	}
	
	/**
	 * We need just one instance to register as property listener
	 */
	private I18N()
	{
	  // nothing more to do
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.PropertyNotifyee#onChange(de.tif.jacob.core.Property)
   */
  public void onChange(Property property) throws Exception
  {
    if (property == Property.COUNTRY_DEFAULT || property == Property.LANGUAGE_DEFAULT)
    {
      // Reset maps because default locale might have changed!
      //
      
      synchronized (coreResourceBundles)
      {
        coreResourceBundles.clear();
      }

      synchronized (applicationLabelResourceBundles)
      {
        applicationLabelResourceBundles.clear();
      }

      synchronized (applicationMessageResourceBundles)
      {
        applicationMessageResourceBundles.clear();
      }
    }
  }
  
  
	protected static ResourceBundle getCoreResourceBundle(Context context, Locale locale) throws MissingResourceException
  {
    IApplicationDefinition applDef = context.hasApplicationDefinition() ? context.getApplicationDefinition() : null;
    return getCoreResourceBundle(applDef, locale);
  }
	
  
	protected static ResourceBundle getApplicationMessageResourceBundle(Context context, Locale locale) throws MissingResourceException
  {
    return getApplicationResourceBundle(context.getApplicationDefinition(), locale, applicationMessageResourceBundles, APPLICATION_MESSAGE_RESOURCE_BASE_NAME);
  }
	
	private static ResourceBundle getCoreResourceBundle(IApplicationDefinition applDef, Locale locale) throws MissingResourceException
  {
    String applicationName = applDef != null ? applDef.getName() : NONE_APPLICATION_NAME;
    synchronized (coreResourceBundles)
    {
      HashMap locale2bundle = (HashMap) coreResourceBundles.get(applicationName);

      if (locale2bundle == null)
        coreResourceBundles.put(applicationName, locale2bundle = new HashMap());

      ResourceBundle rb = (ResourceBundle) locale2bundle.get(locale.toString());
      if (rb != null)
      {
        return rb;
      }

      rb = ResourceBundle.getBundle(CORE_RESOURCE_BASE_NAME, locale, I18NClassLoader.INSTANCE);
      locale2bundle.put(locale.toString(), rb);
      return rb;
    }
  }
	
	private static ResourceBundle getApplicationLabelResourceBundle(IApplicationDefinition applDef, Locale locale) throws MissingResourceException
  {
    return getApplicationResourceBundle(applDef, locale, applicationLabelResourceBundles, APPLICATION_LABEL_RESOURCE_BASE_NAME);
  }

	private static ResourceBundle getApplicationResourceBundle(IApplicationDefinition applDef, Locale locale, Map applicationResourceBundles, String baseName) throws MissingResourceException
  {
    synchronized (applicationResourceBundles)
    {
      HashMap locale2bundle = (HashMap) applicationResourceBundles.get(applDef);

      if (locale2bundle == null)
        applicationResourceBundles.put(applDef, locale2bundle = new HashMap());

      ResourceBundle rb = (ResourceBundle) locale2bundle.get(locale.toString());
      if (rb != null)
      {
//        applicationResourceBundles.remove(null);// IMPORTANT: process the internal clean up for WeakReferenceQueue
        return rb;
      }

      Object obj = ClassProvider.getInstance(applDef, "jacob.resources.ResourceProvider");
      if (null == obj)
      {
        throw new RuntimeException("No resource provider found for application [" + applDef + "]");
      }

      rb = ResourceBundle.getBundle(baseName, locale, obj.getClass().getClassLoader());
      locale2bundle.put(locale.toString(), rb);
      return rb;
    }
  }

  /**
   * 
   * @param label a label starting with <b>%</b>
   * @param app the application definition for the resource lookup
   * @return
   */
  public static String localizeLabel(String label, IClientContext context)
  {
    return localizeLabel(label, context, context.getLocale());
  }

  /**
   * 
   * @param label a label starting with <b>%</b>
   * @param context the current working context
   * @param locale the required language
   * @return
   */
  public static String localizeLabel(String label, Context context, Locale locale)
  {
    IApplicationDefinition applDef = context.hasApplicationDefinition() ? context.getApplicationDefinition() : null;
    return localizeLabel(label,applDef, locale);
  }
  
  /**
   * 
   * @param label a label starting with <b>%</b>
   * @param app the application definition for the resource lookup
   * @param locale the required language
   * @return
   */
  public static String localizeLabel(String label, IApplicationDefinition app, Locale locale)
    {

      if (label.startsWith("%") && label.length()>1)
    {
      String i18nLabelKey = label.substring(1);
      try
      {
        if (app!=null)
        {
          try
          {
            return getApplicationLabelResourceBundle(app, locale).getString(i18nLabelKey);
          }
          catch (MissingResourceException ex)
          {
            // ignore
          }
        }
        
        // Fallback. Try to find the resource in the core resource bundles
        //
        try
        {
          return getCoreResourceBundle(app, locale).getString(i18nLabelKey);
        }
        catch (MissingResourceException ex2)
        {
          logger.warn("Could not localize label '" + label + "'");
        }
      }
      catch (Exception ex)
      {
        logger.warn("Could not localize label '" + label + "'", ex);
      }
    }
    return label;
  }
  
  public static String getLocalized(String key, Context context, Locale locale)
  {
    try
    {
      if (context.hasApplicationDefinition())
      {
        try
        {
          return getApplicationLabelResourceBundle(context.getApplicationDefinition(), locale).getString(key);
        }
        catch (MissingResourceException ex)
        {
          // ignore
        }
      }
      
      // Fallback. Try to find the resource in the core resource bundles
      //
      try
      {
        return getCoreResourceBundle(context, locale).getString(key);
      }
      catch (MissingResourceException ex2)
      {
        logger.warn("Could not localize key '" + key + "'");
      }
    }
    catch (Exception ex)
    {
      logger.warn("Could not localize key '" + key + "'", ex);
    }

    // return with '%' to signal missing resource
    return "%" + key;
  }
  
  
  public static String getLocalized(String key, IClientContext context)
  {
    return getLocalized(key, context, context.getLocale());
  }
  
  public static String getCoreLocalized(String key, IApplicationDefinition applDef, Locale locale)
  {
    return getCoreLocalized(key, applDef, locale, null);
  }
  
  public static String getCoreLocalized(String key, IApplicationDefinition applDef, Locale locale, Object[] msgArgs)
  {
    try
    {
      String localized = getCoreResourceBundle(applDef, locale).getString(key);
      if (msgArgs == null)
        return localized;
      return MessageFormat.format(localized, msgArgs);
    }
    catch (MissingResourceException ex)
    {
      logger.warn("Could not localize core key '" + key + "'");
    }
    catch (Exception ex)
    {
      logger.warn("Could not localize core key '" + key + "'", ex);
    }
    
    // return with '%' to signal missing resource
    return "%" + key;
  }

  /**
   * Return the i18n value of the key with the locale of the hands over context.
   * 
   * @param key
   * @param context
   * @return
   */
  public static String getCoreLocalized(String key, Context context)
  {
    return getCoreLocalized(key,context,context.getLocale());
  }

  public static String getCoreLocalized(String key, Context context, Locale locale)
  {
    try
    {
      return getCoreResourceBundle(context, locale).getString(key);
    }
    catch (MissingResourceException ex)
    {
      logger.warn("Could not localize core key '" + key + "'");
    }
    catch (Exception ex)
    {
      logger.warn("Could not localize core key '" + key + "'", ex);
    }
    
    // return with '%' to signal missing resource
    return "%" + key;
  }
  
  public static String getHttpCalendarLanguage(Locale locale)
  {
    if (supportedCalendarLanguages.contains(locale.getLanguage()))
        return locale.getLanguage();
    
    // english should be of course the default!
    return "en";
  }
  
  public static boolean isHttpCalendar24Hour(Locale locale)
  {
    // Note: In case the possible formats are extended -> the QBE parser has to
    // be extended as well
    if ("en".equals(locale.getLanguage()))
    {
      if ("US".equals(locale.getCountry()))
        return false;
    }
    return true;
  }

  public static String getHttpCalendarTimeFormat(Locale locale)
  {
    if ("en".equals(locale.getLanguage()))
    {
      if ("US".equals(locale.getCountry()))
        return "%I:%M %p";
    }
    return "%H:%M";
  }

  public static String getHttpCalendarDatetimeFormats(Locale locale)
  {
    // Note: In case the possible formats are extended -> the QBE parser has to
    // be extended as well
    if ("en".equals(locale.getLanguage()))
    {
      if ("US".equals(locale.getCountry()))
        return "%m/%d/%Y %I:%M %p";
    }
    return "%d.%m.%Y %H:%M";
  }
  
  public static String getHttpCalendarDateFormat(Locale locale)
  {
    // Note: In case the possible formats are extended -> the QBE parser has to
    // be extended as well
    if ("en".equals(locale.getLanguage()))
    {
      if ("US".equals(locale.getCountry()))
        return "%m/%d/%Y";
    }    
    return "%d.%m.%Y";
  }
  
  public static String toFullDatetimeString(Locale locale, Date datetime)
  {
    DateFormat format;
    if (Locale.GERMAN.getLanguage() == locale.getLanguage())
    {
      // default pattern would be : "dd.MM.yyyy H.mm' Uhr 'z"  (we miss the seconds!)
      format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss' Uhr 'z");
    }
    else
    {
      format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.FULL, locale);
    }
    return format.format(datetime);
  }
  
  private static boolean isCommaSeparated(Locale locale)
  {
    return locale != null && !"en".equals(locale.getLanguage());
  }
  
  public static String toString(BigDecimal value, Locale locale, int style)
  {
    if (style == IDataRecord.LONG_STYLE)
    {
      NumberFormat format = NumberFormat.getInstance(locale);
      format.setGroupingUsed(true);
      return format.format(value);
    }
    
    String result = value.toString();
    if (style != IDataRecord.RAW_STYLE && isCommaSeparated(locale))
    {
      result = StringUtil.replace(result, ".", ",");
    }
    return result;
  }
  
  public static BigDecimal parseDecimal(String expression, Locale locale) throws InvalidExpressionException
  {
    String str = expression.trim();
    if (isCommaSeparated(locale))
    {
      // if comma separated, there should be no dots
      if (str.indexOf('.') != -1)
        throw new InvalidExpressionException(expression);

      str = StringUtil.replace(str, ",", ".");
    }
    try
    {
      return new BigDecimal(str);
    }
    catch (NumberFormatException ex)
    {
      throw new InvalidExpressionException(expression);
    }
  }
  
  public static String toString(Float value, Locale locale, int style)
  {
    String result = value.toString();
    if (style != IDataRecord.RAW_STYLE && isCommaSeparated(locale))
    {
      result = StringUtil.replace(result, ".", ",");
    }
    return result;
  }
  
  public static Float parseFloat(String expression, Locale locale) throws InvalidExpressionException
  {
    String str = expression.trim();
    if (isCommaSeparated(locale))
    {
      // if comma separated, there should be no dots
      if (str.indexOf('.') != -1)
        throw new InvalidExpressionException(expression);

      str = StringUtil.replace(str, ",", ".");
    }
    try
    {
      return Float.valueOf(str);
    }
    catch (NumberFormatException ex)
    {
      throw new InvalidExpressionException(expression);
    }
  }
  
  public static String toString(Double value, Locale locale, int style)
  {
    String result = value.toString();
    if (style != IDataRecord.RAW_STYLE && isCommaSeparated(locale))
    {
      result = StringUtil.replace(result, ".", ",");
    }
    return result;
  }
  
  public static Double parseDouble(String expression, Locale locale) throws InvalidExpressionException
  {
    String str = expression.trim();
    if (isCommaSeparated(locale))
    {
      // if comma separated, there should be no dots
      if (str.indexOf('.') != -1)
        throw new InvalidExpressionException(expression);
        
      str = StringUtil.replace(str, ",", ".");
    }
    try
    {
      return Double.valueOf(str);
    }
    catch (NumberFormatException ex)
    {
      throw new InvalidExpressionException(expression);
    }
  }
  
  private static void testFullDatetimeFormat(Locale locale, Date datetime)
  {
    SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.FULL, locale);
    System.out.println("Locale: " + locale);
    System.out.println("\tpattern: " + format.toPattern());
    System.out.println("\tdefault : " + format.format(datetime));
    System.out.println("\tmodified: " + toFullDatetimeString(locale, datetime));
    SimpleDateFormat shortFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
    System.out.println("\tspattern: " + shortFormat.toPattern());
    System.out.println("\tsdefault: " + shortFormat.format(datetime));
  }
	
  private static void testNumberFormat(Locale locale)
  {
    DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(locale);
    System.out.println("Locale: " + locale);
    System.out.println("\tpattern: " + format.toLocalizedPattern());
    System.out.println("\tbigdec : " + format.format(new BigDecimal("123456789.3478")));
    System.out.println("\tfloat  : " + format.format(new Float(1234.5678E-5)));
    System.out.println("\tdouble  : " + format.format(new Double(1234.5678E+3)));
  }
	
	public static void main(String[] args)
  {
    try
    {
      Date datetime = new Date();
      testFullDatetimeFormat(new Locale("de", "DE"), datetime);
      testFullDatetimeFormat(Locale.GERMAN, datetime);
      testFullDatetimeFormat(Locale.GERMANY, datetime);
      testFullDatetimeFormat(new Locale("nl", ""), datetime);
      testFullDatetimeFormat(Locale.FRANCE, datetime);
      testFullDatetimeFormat(Locale.FRENCH, datetime);
      testFullDatetimeFormat(Locale.ENGLISH, datetime);
      testFullDatetimeFormat(Locale.UK, datetime);
      testFullDatetimeFormat(Locale.US, datetime);
      
      System.out.println("#############################################");
      
      for (int i=0; i <= 24; i++)
      {
        testFullDatetimeFormat(Locale.US, datetime);
        datetime = new Date(datetime.getTime()+1000*3600);
      }
      
      System.out.println("#############################################");
      
      testNumberFormat(Locale.GERMAN);
      testNumberFormat(Locale.GERMANY);
      testNumberFormat(new Locale("nl", ""));
      testNumberFormat(Locale.FRANCE);
      testNumberFormat(Locale.FRENCH);
      testNumberFormat(Locale.ENGLISH);
      testNumberFormat(Locale.UK);
      testNumberFormat(Locale.US);
      
      System.out.println(new Time(System.currentTimeMillis()).toString());
      System.out.println((new Float(1234.5678E-5)).toString());
      System.out.println((new Double(1234.5678E+3)).toString());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
	
	/**
	 * @author Andreas Sonntag
	 *
	 * To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Generation - Code and Comments
	 */
	public static class I18NClassLoader extends ClassLoader
	{
		public static final I18NClassLoader INSTANCE = new I18NClassLoader();
		
		private I18NClassLoader()
		{
			// we do not want any parent class loader
			super(null);
		}


		public URL getResource(String name)
		{
			return findResource(name);
		}


		public InputStream getResourceAsStream(String name)
		{
		  // This is needed to generate localized messages from applications outside
      // of jACOB runtime environment (i.e. build process), which use
      // jacobBase.jar stand-alone!!!
      //
		  boolean loadResourcesFromStandAlone = true;
		  try
		  {
		    // Class Bootstrap extends HttpServlet, but if servlet.jar is not present
		    // the following call would throw a NoClassDefFoundError!
		    //
		    loadResourcesFromStandAlone = Bootstrap.getApplicationRootPath() == null;
		  }
		  catch (NoClassDefFoundError error)
		  {
		    // ignore
		  }
		  if (loadResourcesFromStandAlone)
		    return getClass().getResourceAsStream(name);
		    
		  // Load resource from runtime environment
		  //
		  String descriptionBasePath = Bootstrap.getApplicationRootPath()+"WEB-INF"+File.separator+"language";
			
			File resourceFile = new File(descriptionBasePath, name);

      if (!resourceFile.exists())
        return null;
        
			try
			{
				return new FileInputStream(resourceFile);
			}
			catch (FileNotFoundException ex)
			{
				logger.error("Accessing resource failed", ex);
				return null;
			}
		}


		protected Class loadClass(String name, boolean resolve)
		throws ClassNotFoundException
		{
			// just throw exception
			throw new ClassNotFoundException(name);
		}


		protected Enumeration findResources(String name)
		throws IOException
		{
			return null;
		}


		/**
		 *  Finds the resource with the given name.
		 *
		 * @param  name the resource name
		 * @return  a URL for reading the resource, or <code>null</code> if the
		 *      resource could not be found
		 */
		protected URL findResource(String name)
		{
			return null;
		}

	}
}
