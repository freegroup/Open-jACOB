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
package de.tif.jacob.core.config;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.collections.iterators.EnumerationIterator;


/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractConfig implements IConfig
{

	/**
	 * @return
	 */
	protected abstract ResourceBundle getResourceBundle();

  /* (non-Javadoc)
   * @see de.tif.jacob.core.config.IConfig#getResource(java.lang.String)
   */
  public java.net.URL getResource(String name)
	{
		return getClass().getResource(name);
	}
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.config.IConfig#getProperty(java.lang.String)
	 */
	public final String getProperty(String key) throws MissingResourceException
	{
		return getResourceBundle().getString(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.config.IConfig#getKeys()
	 */
	public final Iterator getKeys()
	{
		return new EnumerationIterator(getResourceBundle().getKeys());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.config.IConfig#getKeysStartingWith(java.lang.String)
	 */
	public Iterator getKeysStartingWith(String keyPrefix)
	{
		return new StringPrefixIterator(keyPrefix, getResourceBundle().getKeys());
	}

	private static class StringPrefixIterator implements Iterator
	{
		private final Enumeration enumeration;
		private final String keyPrefix;
		private Object next;

		StringPrefixIterator(String keyPrefix, Enumeration enumeration)
		{
			this.enumeration = enumeration;
			this.keyPrefix = keyPrefix;
			iterate();
		}

		private void iterate()
		{
			while (this.enumeration.hasMoreElements())
			{
				this.next = this.enumeration.nextElement();
				if (next instanceof String && ((String) next).startsWith(this.keyPrefix))
					return;
			}
			this.next = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{
			return this.next != null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		public Object next()
		{
			if (this.next == null)
				return null;

			Object next = this.next;
			iterate();
			return next;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

}
