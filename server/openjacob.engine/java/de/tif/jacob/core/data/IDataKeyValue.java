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

package de.tif.jacob.core.data;

import java.io.Serializable;

import de.tif.jacob.core.definition.IKey;

/**
 * Data key value instances could be obtained by means of
 * <li>{@link IDataRecord#getPrimaryKeyValue()}
 * <li>{@link IDataTableRecord#getKeyValue(IKey)}
 * <li>{@link IDataTableRecord#getKeyValue(String)}.
 * 
 * @author Andreas
 */
public final class IDataKeyValue implements Serializable
{
	/**
   * 
   */
  private static final long serialVersionUID = 37836313781937677L;

  /**
	 * The revision control system id.
	 */
	static public transient final String RCS_ID = "$Id: IDataKeyValue.java,v 1.2 2010/06/29 12:39:04 ibissw Exp $";
	
	/**
	 * The revision control system id in short form.
	 */
	static public transient final String RCS_REV = "$Revision: 1.2 $";

	private final Object fieldValues;

	/** Cache the hash code for the key value */
	private transient int hash = 0;

	public IDataKeyValue(Object[] fieldValues)
	{
	  // IBIS : make protected!
		if (null == fieldValues)
			throw new NullPointerException();
		this.fieldValues = fieldValues;
	}

	public IDataKeyValue(Object fieldValue)
	{
	  // IBIS : make protected!
	  if (null == fieldValue)
			throw new NullPointerException();
		this.fieldValues = fieldValue;
	}

	/**
   * Returns the number of field values this key value exists of.
   * 
   * @return the number of field values. For non composite keys the number
   *         returned will be <code>1</code>.
   */
	public int numberOfFieldValues()
	{
		if (this.fieldValues instanceof Object[])
		{
			return ((Object[]) this.fieldValues).length;
		}
		return 1;
	}

	/**
   * Returns the specified field value given by its index.
   * 
   * @param fieldIndex
   *          the field index
   * @return the requested field value.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #numberOfFieldValues()}</code>
   *           is not fulfilled
   */
	public Object getFieldValue(int fieldIndex) throws IndexOutOfBoundsException
	{
		if (this.fieldValues instanceof Object[])
		{
			return ((Object[]) this.fieldValues)[fieldIndex];
		}
		if (fieldIndex != 0)
			throw new IndexOutOfBoundsException("" + fieldIndex);
		return this.fieldValues;
	}

	/**
   * Indicates whether some other object is "equal to" this one.
   * <p>
   * Different instances of this class are equal, if the underlying field values
   * are equal as well.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
	public boolean equals(Object anObject)
	{
		if (this == anObject)
		{
			return true;
		}
		if (anObject instanceof IDataKeyValue)
		{
			IDataKeyValue another = (IDataKeyValue) anObject;

			if (this.fieldValues instanceof Object[])
			{
				if (another.fieldValues instanceof Object[])
				{
					Object[] values = (Object[]) this.fieldValues;
					Object[] othervalues = (Object[]) another.fieldValues;
					if (values.length == othervalues.length)
					{
						for (int i = 0; i < values.length; i++)
						{
							if (!values[i].equals(othervalues[i]))
							{
								return false;
							}
						}
						return true;
					}
				}
			}
			else
			{
				return this.fieldValues.equals(another.fieldValues);
			}
		}
		return false;
	}

	/**
   * Returns a hash code for this key value.
   * <p>
   * For different instances of this class the hash codes are equal, if the hash
   * codes of the underlying field values are equal as well.
   * 
   * @see java.lang.Object#hashCode()
   */
	public int hashCode()
	{
		if (this.fieldValues instanceof Object[])
		{
			int h = hash;
			if (h == 0)
			{
				Object[] values = (Object[]) this.fieldValues;
				for (int i = 0; i < values.length; i++)
				{
					h = 3 * h + values[i].hashCode();
				}
				hash = h;
			}
			return h;
		}
		return this.fieldValues.hashCode();
	}
	
	/**
	 * Returns the key value as string. In case of composite keys, a string in
	 * the following form k1|k2|k3 will be returned.
	 * 
	 * @return the key value as string
	 */
	public String toString()
	{
		if (this.fieldValues instanceof Object[])
		{
			StringBuffer buffer = new StringBuffer();
			Object[] values = (Object[]) this.fieldValues;
			for (int i = 0; i < values.length; i++)
			{
				if (i != 0)
					buffer.append('|');
				buffer.append(values[i].toString());
			}

			return buffer.toString();
		}
		return this.fieldValues.toString();
	}
}
