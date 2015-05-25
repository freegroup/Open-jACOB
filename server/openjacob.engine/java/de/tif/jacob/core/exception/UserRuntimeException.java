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
 * Created on 12.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.exception;

import java.util.Locale;

import de.tif.jacob.i18n.Message;

/**
 * UserRuntimeException is the base class of all runtime exceptions which in
 * general result due to inproper user operation.
 * <p>
 * Exceptions of type UserRuntimeException should be handled by gui clients by means of
 * generating a nice (i.e. a localized, user understandable) warning.
 * 
 * @see UserException
 * 
 * @author Andreas Sonntag
 */
public class UserRuntimeException extends RuntimeException
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: UserRuntimeException.java,v 1.2 2008/03/19 14:03:43 ibissw Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.2 $";

  private final Message message;
  private final UserException wrapped;
  private final String details;
  
  /**
   * Constructs a new user runtime exception with the specified detail message.
   * <p>
   * Note: This constructor should only be used, if your application is not
   * designed for multi language support! To internationalize the exception use
   * {@link #UserRuntimeException(Message)}.
   * 
   * @param message
   *          the detail message
   */
  public UserRuntimeException(String message)
  {
  	super(message);
  	this.message = null;
  	this.wrapped = null;
    this.details = null;
  }

  /**
   * Constructs a new user runtime exception with the specified message and additional exception details.
   * <p>
   * Note: This constructor should only be used, if your application is not
   * designed for multi language support! To internationalize the exception use
   * {@link #UserRuntimeException(Message, String)}.
   * 
   * @param message
   *          the message
   * @param details
   *          the exception details or <code>null</code>
   * @since 2.7.2          
   */
  public UserRuntimeException(String message, String details)
  {
    super(message);
    this.message = null;
    this.wrapped = null;
    this.details = details;
  }

  /**
   * Constructs a new user runtime exception with the specified internationalized detail
   * message.
   * 
   * @param message
   *          the internationalized detail message
   */
  public UserRuntimeException(Message message)
  {
    super();
    this.message = message;
  	this.wrapped = null;
    this.details = null;
  }

  /**
   * Constructs a new user runtime exception with the specified internationalized
   * message and additional exception details.
   * 
   * @param message
   *          the internationalized message
   * @param details
   *          the exception details or <code>null</code>
   * @since 2.7.2
   */
  public UserRuntimeException(Message message, String details)
  {
    super();
    this.message = message;
    this.wrapped = null;
    this.details = details;
  }

  /**
   * Constructs a new user exception with the specified internationalized detail
   * message and an additional exception cause.
   * 
   * @param message
   *          the internationalized detail message
   * @param cause
   *          the original cause
   */
  public UserRuntimeException(Message message, Throwable cause)
  {
    super(cause);
    this.message = message;
  	this.wrapped = null;
    this.details = null;
  }
  
  /**
   * This constructor is for internal use only.
   * 
   * @param cause
   *          the original cause
   */
  public UserRuntimeException(UserException cause)
  {
    super(cause);
    this.message = null;
  	this.wrapped = cause;
    this.details = cause.getDetails();
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
    {
      if (this.wrapped != null)
        return this.wrapped.getLocalizedMessage(locale);
      return getMessage();
    }
    
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
    {
      if (this.wrapped != null)
        return this.wrapped.getMessage();
      return super.getMessage();
    }
    
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
