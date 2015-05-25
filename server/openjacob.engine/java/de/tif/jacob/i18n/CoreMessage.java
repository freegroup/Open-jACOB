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

import java.util.Locale;
import java.util.MissingResourceException;

import de.tif.jacob.core.Context;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class CoreMessage extends Message
{
	static public transient final String RCS_ID = "$Id: CoreMessage.java,v 1.7 2009/12/07 03:36:17 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.7 $";
	
	// the messages the core knows
  public static final String RECORD_LOCKED               = "RECORD_LOCKED";
  public static final String RECORD_LOCKED_SHORT         = "RECORD_LOCKED_SHORT";
  public static final String NO_RECORDS_SELECTED         = "NO_RECORDS_SELECTED";
	public static final String REQUIRED_FIELD_MISSING      = "REQUIRED_FIELD_MISSING";
	public static final String UNCOMMITTED_LINK_RECORD     = "MSG_UNCOMMITTED_LINK_RECORD";
	public static final String INVALID_EXPRESSION          = "INVALID_EXPRESSION";
	public static final String VALUE_OUT_OF_RANGE_EXPRESSION = "VALUE_OUT_OF_RANGE_EXPRESSION";
	public static final String VALUE_TOO_SMALL_EXPRESSION  = "VALUE_TOO_SMALL_EXPRESSION";
	public static final String VALUE_TOO_BIG_EXPRESSION    = "VALUE_TOO_BIG_EXPRESSION";
	public static final String INVALID_LONGTEXT_EXPRESSION = "INVALID_LONGTEXT_EXPRESSION";
	public static final String INVALID_BINARY_EXPRESSION   = "INVALID_BINARY_EXPRESSION";
  public static final String RECORD_NOT_FOUND            = "RECORD_NOT_FOUND";
  public static final String RECORD_NOT_FOUND_SHORT      = "RECORD_NOT_FOUND_SHORT";
	public static final String UNCONSTRAINED_SEARCH        = "UNCONSTRAINED_SEARCH";
	public static final String TABLE_ALREADY_EXISTS        = "TABLE_ALREADY_EXISTS";
  public static final String UNIQUE_VIOLATION            = "UNIQUE_VIOLATION";
  public static final String UNIQUE_FIELD_VIOLATION      = "UNIQUE_FIELD_VIOLATION";
  public static final String UNIQUE_FIELDS_VIOLATION     = "UNIQUE_FIELDS_VIOLATION";
  public static final String FOREIGN_CONSTRAINT_VIOLATION= "FOREIGN_CONSTRAINT_VIOLATION";
  public static final String INVALID_NEW_PASSWORD        = "INVALID_NEW_PASSWORD";
  public static final String INVALID_USERID_OR_PASSWORD  = "INVALID_USERID_OR_PASSWORD";
  public static final String USER_NOT_EXISTING           = "USER_NOT_EXISTING";
  public static final String REQUEST_CANCELED_BY_USER    = "REQUEST_CANCELED_BY_USER";
  public static final String UNDEFINED_DATASOURCE        = "UNDEFINED_DATASOURCE";
  public static final String UNAVAILABLE_DATASOURCE_TYPE = "MSG_UNAVAILABLE_DATASOURCE_TYPE";
  public static final String UNAVAILABLE_TRANSFORMER_TYPE = "MSG_UNAVAILABLE_TRANSFORMER_TYPE";
  public static final String INSUFFICIENT_ACCESS_RIGHTS  = "INSUFFICIENT_ACCESS_RIGHTS";
  public static final String NO_DOMAIN_ACCESS            = "NO_DOMAIN_ACCESS";
  public static final String MISSING_APPLICATION_VERSION = "MSG_MISSING_APPLICATION_VERSION";
  public static final String NO_COLUMNS_IN_REPORT        = "MSG_REPORTING_FIELDS_REQUIRED";
  public static final String FUNCTION_NOT_AVAILABLE_IN_DEMO_VERSION = "MSG_FUNCTION_NOT_AVAILABLE_IN_DEMO_VERSION";
  public static final String MULTI_UPDATE_START_FAILED   = "MULTI_UPDATE_START_FAILED";
  public static final String NO_RECORDS_FOR_MULTIPLEUPDATE   = "NO_RECORDS_FOR_MULTIPLEUPDATE";
  public static final String NOT_ALL_RECORDS_AVAILABLE_FOR_MULTIPLEUPDATE   = "NOT_ALL_RECORDS_AVAILABLE_FOR_MULTIPLEUPDATE";
  
	/**
	 * @param messageId
	 */
	public CoreMessage(String messageId)
	{
		super(messageId);
	}

	/**
	 * @param messageId
	 * @param arg
	 */
	public CoreMessage(String messageId, Object arg)
	{
		super(messageId, arg);
	}

	/**
	 * @param messageId
	 * @param args
	 */
	public CoreMessage(String messageId, Object[] args)
	{
		super(messageId, args);
	}

	/**
	 * @param messageId
	 * @param arg1
	 * @param arg2
	 */
	public CoreMessage(String messageId, Object arg1, Object arg2)
	{
		super(messageId, arg1, arg2);
	}

	/**
	 * @param messageId
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public CoreMessage(String messageId, Object arg1, Object arg2, Object arg3)
	{
		super(messageId, arg1, arg2, arg3);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.message.Message#getResourceBundle(java.util.Locale)
	 */
	public ResourceBundle getResourceBundle(Locale locale) throws MissingResourceException
  {
    return I18N.getCoreResourceBundle(Context.getCurrent(), locale);
  }
}
