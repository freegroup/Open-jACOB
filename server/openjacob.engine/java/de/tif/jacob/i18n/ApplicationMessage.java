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

package de.tif.jacob.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;

import de.tif.jacob.core.Context;

/**
 * 
 * @author Andreas Sonntag
 */
public final class ApplicationMessage extends Message
{
  /**
   * @param messageId
   */
  public ApplicationMessage(String messageId)
  {
    super(messageId);
  }
  
  /**
   * @param messageId
   * @param arg
   */
  public ApplicationMessage(String messageId, Object arg)
  {
    super(messageId, arg);
  }
  
  /**
   * @param messageId
   * @param arg1
   * @param arg2
   */
  public ApplicationMessage(String messageId, Object arg1, Object arg2)
  {
    super(messageId, arg1, arg2);
  }
  
  /**
   * @param messageId
   * @param arg1
   * @param arg2
   * @param arg3
   */
  public ApplicationMessage(String messageId, Object arg1, Object arg2, Object arg3)
  {
    super(messageId, arg1, arg2, arg3);
  }
  
  /**
   * @param messageId
   * @param args
   */
  public ApplicationMessage(String messageId, Object[] args)
  {
    super(messageId, args);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.i18n.Message#getResourceBundle(java.util.Locale)
   */
  public ResourceBundle getResourceBundle(Locale locale) throws MissingResourceException
  {
    return I18N.getApplicationMessageResourceBundle(Context.getCurrent(), locale);
  }
  
  private static String getLocalized(String messageId, Object[] args, Context context, Locale locale)
  {
    try
    {
      String message = I18N.getApplicationMessageResourceBundle(context, locale).getString(messageId);
  	  if (args.length == 0)
  	    return message;
  		return MessageFormat.format(message, args);
    }
    catch (MissingResourceException ex)
    {
      logger.warn("Could not localize message id '" + messageId + "'");
    }

    // return with '%' to signal missing resource
    return "%" + messageId;
  }
  
  public static String getLocalized(String messageId)
  {
    return getLocalized(messageId, NO_ARGS);
  }
  
  public static String getLocalized(String messageId, Object arg1)
  {
    return getLocalized(messageId, new Object[] {arg1});
  }
  
  public static String getLocalized(String messageId, Object arg1, Object arg2)
  {
    return getLocalized(messageId, new Object[] {arg1, arg2});
  }
  
  public static String getLocalized(String messageId, Object arg1, Object arg2, Object arg3)
  {
    return getLocalized(messageId, new Object[] {arg1, arg2, arg3});
  }
  
  public static String getLocalized(String messageId, Object[] args)
  {
    Context context = Context.getCurrent();
    return getLocalized(messageId, args, context, context.getLocale());
  }
  
  public static String getLocalized(String messageId, Locale locale)
  {
    return getLocalized(messageId, NO_ARGS, locale);
  }
  
  public static String getLocalized(String messageId, Object arg1, Locale locale)
  {
    return getLocalized(messageId, new Object[] {arg1}, locale);
  }
  
  public static String getLocalized(String messageId, Object arg1, Object arg2, Locale locale)
  {
    return getLocalized(messageId, new Object[] {arg1, arg2}, locale);
  }
  
  public static String getLocalized(String messageId, Object arg1, Object arg2, Object arg3, Locale locale)
  {
    return getLocalized(messageId, new Object[] {arg1, arg2, arg3}, locale);
  }
  
  public static String getLocalized(String messageId, Object[] args, Locale locale)
  {
    return getLocalized(messageId, args, Context.getCurrent(), locale);
  }
}
