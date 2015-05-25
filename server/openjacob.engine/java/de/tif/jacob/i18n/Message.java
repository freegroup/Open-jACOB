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
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package de.tif.jacob.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public abstract class Message
{
	static public transient final String RCS_ID = "$Id: Message.java,v 1.1 2007/01/19 09:50:46 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.1 $";

	static protected final transient Log logger = LogFactory.getLog(Message.class);

	protected static final Object[] NO_ARGS = new Object[0];

	protected final String messageId;
	protected final Object[] args;

	public abstract ResourceBundle getResourceBundle(Locale locale) throws MissingResourceException;

	protected Message(String messageId)
	{
		if (null == messageId)
			throw new NullPointerException("messageId is null");
		
		this.messageId = messageId;
		this.args = NO_ARGS;
	}

	protected Message(String messageId, Object arg)
	{
		if (null == messageId)
			throw new NullPointerException("messageId is null");
		if (null == arg)
			throw new NullPointerException("arg is null");
		
		this.messageId = messageId;
		this.args = new Object[] {arg};
	}

	protected Message(String messageId, Object arg1, Object arg2)
	{
		if (null == messageId)
			throw new NullPointerException("messageId is null");
		if (null == arg1)
			throw new NullPointerException("arg1 is null");
		if (null == arg2)
			throw new NullPointerException("arg2 is null");
		
		this.messageId = messageId;
		this.args = new Object[] {arg1, arg2};
	}

	protected Message(String messageId, Object arg1, Object arg2, Object arg3)
	{
		if (null == messageId)
			throw new NullPointerException("messageId is null");
		if (null == arg1)
			throw new NullPointerException("arg1 is null");
		if (null == arg2)
			throw new NullPointerException("arg2 is null");
		if (null == arg3)
			throw new NullPointerException("arg3 is null");
		
		this.messageId = messageId;
		this.args = new Object[] {arg1, arg2, arg3};
	}

	protected Message(String messageId, Object[] args)
	{
		if (null == messageId)
			throw new NullPointerException("messageId is null");		
		if (null == args)
			throw new NullPointerException("args is null");
		
		this.messageId = messageId;
		this.args = args;
	}

	private String get(Locale locale) throws MissingResourceException
	{
		return getResourceBundle(locale).getString(this.messageId);
	}
	
	public final String print()
	{
	  return print(Context.getCurrent().getLocale());
	}
	
	public final String print(Locale locale) throws MissingResourceException
	{
	  if (this.args.length == 0)
	    return get(locale);
		return MessageFormat.format(get(locale), this.args);
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClass().getName()).append("[");
		buffer.append(this.messageId);
		for (int i = 0; i < args.length; i++)
		{
			buffer.append(",");
			buffer.append(args[i]);
		}
		buffer.append("]");
		return buffer.toString();
	}
}
