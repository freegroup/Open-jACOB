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

package de.tif.jacob.core.exception;

import java.util.Locale;

import de.tif.jacob.i18n.Message;

/**
 * UserException is the base class of all (non runtime) exceptions which in
 * general result due to inproper user operation.
 * <p>
 * Exceptions of type UserException should be handled by gui clients by means of
 * generating a nice (i.e. a localized, user understandable) warning.
 * 
 * @see UserRuntimeException
 * 
 * @author Andreas Sonntag
 * @author Andreas Herz
 */
public class UserException extends Exception
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: UserException.java,v 1.5 2009/03/23 00:09:21 ibissw Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.5 $";

  private final Message message;
  private final String details;

  /**
   * Constructs a new user exception with the specified detail message.
   * <p>
   * Note: This constructor should only be used, if your application is not
   * designed for multi language support! To internationalize the exception use
   * {@link #UserException(Message)}.
   * 
   * @param message
   *          the detail message
   */
  public UserException(String message)
  {
    super(message);
    this.message = null;
    this.details = null;
  }

  /**
   * Constructs a new user exception with the specified detail message.
   * <p>
   * Note: This constructor should only be used, if your application is not
   * designed for multi language support! To internationalize the exception use
   * {@link #UserException(Message)}.
   * 
   * @since 2.8.4
   * @param message
   *          the detail message
   * @param cause 
   *          the cause of the exception
   */
  public UserException(String message, Throwable cause)
  {
    super(message, cause);
    this.message = null;
    this.details = null;
  }
  
  /**
   * Constructs a new user exception with the specified message and additional exception details.
   * <p>
   * Note: This constructor should only be used, if your application is not
   * designed for multi language support! To internationalize the exception use
   * {@link #UserException(Message, String)}.
   * 
   * @param message
   *          the message
   * @param details
   *          the exception details or <code>null</code>
   * @since 2.7.2          
   */
  public UserException(String message, String details)
  {
    super(message);
    this.message = null;
    this.details = details;
  }

  /**
   * Constructs a new user exception with the specified message and additional exception details.
   * <p>
   * Note: This constructor should only be used, if your application is not
   * designed for multi language support! To internationalize the exception use
   * {@link #UserException(Message, String)}.
   * 
   * @param message
   *          the message
   * @param details
   *          the exception details or <code>null</code>
   * @param cause
   *          the cause of the exception
   * @since 2.8.4    
   */
  public UserException(String message, String details, Throwable cause)
  {
    super(message,cause);
    this.message = null;
    this.details = details;
  }

  /**
   * Constructs a new user exception with the specified internationalized detail
   * message.<br>
   * This constructor is mainly for internal use. Application programmer should
   * use the generated <b>jacob.resources.I18N</b> class for localisation.<br>
   * 
   * @param message
   *          the internationalized detail message
   */
  public UserException(Message message)
  {
    super();
    this.message = message;
    this.details = null;
  }

  /**
   * Constructs a new user exception with the specified internationalized
   * message and additional exception details.<br>
   * This constructor is mainly for internal use. Application programmer should
   * use the generated <b>jacob.resources.I18N</b> class for localisation.<br>
   * 
   * @param message
   *          the internationalized message
   * @param details
   *          the exception details or <code>null</code>
   * @since 2.7.2
   */
  public UserException(Message message, String details)
  {
    super();
    this.message = message;
    this.details = details;
  }

  /**
   * Constructs a new user exception with the specified internationalized detail
   * message and an additional exception cause.<br>
   * This constructor is mainly for internal use. Application programmer should
   * use the generated <b>jacob.resources.I18N</b> class for localisation.<br>
   * 
   * @param message
   *          the internationalized detail message
   * @param cause
   *          the original cause
   */
  public UserException(Message message, Throwable cause)
  {
    super(cause);
    this.message = message;
    this.details = null;
  }

  /**
   * Constructs a new user exception with the specified internationalized detail
   * message and an additional exception cause.<br>
   * This constructor is mainly for internal use. Application programmer should
   * use the generated <b>jacob.resources.I18N</b> class for localisation.<br>
   * 
   * @param message
   *          the internationalized detail message
   * @param details
   *          the exception details or <code>null</code>
   * @param cause
   *          the original cause
   * @since 2.8.5
   */
  public UserException(Message message, String details, Throwable cause)
  {
    super(cause);
    this.message = message;
    this.details = details;
  }

  /**
   * Returns a localized detail message for the given locale.
   * 
   * @param locale
   *          the locale
   * @return localized detail message
   */
  public String getLocalizedMessage(Locale locale)
  {
    if (this.message == null)
      return getMessage();

    // Tomcat ruft in manchen Teilen "exc.getLocalizedMessage(null)" auf.
    // Es kracht dann im I18N-Teil vom jACOB wenn kein Locale vorhanden
    //
    if(locale==null)
      locale = Locale.getDefault();
    
    return this.message.print(locale);
  }
  
  /**
   * Returns the detail message. If possible, the message is localized.
   * 
   * @return detail message
   * 
   * @see java.lang.Throwable#getMessage()
   */
  public String getMessage()
  {
    if (this.message == null)
      return super.getMessage();
    
    return this.message.print();
  }
  
  /**
   * Returns the exception details if any.
   * 
   * @return the exception details or <code>null</code>
   * @since 2.7.2
   */
  public String getDetails()
  {
    return this.details;
  }
}
