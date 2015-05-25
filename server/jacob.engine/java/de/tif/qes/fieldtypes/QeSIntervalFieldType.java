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

package de.tif.qes.fieldtypes;

import java.util.Locale;
import java.util.StringTokenizer;

import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.definition.fieldtypes.IntegerFieldType;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.IntegerField;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QeSIntervalFieldType extends IntegerFieldType
{
  static public final transient String RCS_ID = "$Id: QeSIntervalFieldType.java,v 1.3 2010-07-13 17:57:40 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
	/**
	 * @param defaultValue
	 */
	public QeSIntervalFieldType()
	{
		super(null, null, null, false);
	}

  public QeSIntervalFieldType(IntegerField type)
  {
    super(type);
  }

  /**
   * @param value
   * @return
   */
  public String convertDataValueToString(Object value, Locale locale, int style)
	{
		StringBuffer result = new StringBuffer();
		int intValue = ((Integer) value).intValue();
		if (intValue < 0)
		{
		  result.append("-");
		  intValue = -intValue;
		}
		int hours = intValue / 3600;
		int mins = (intValue % 3600) / 60;
		int secs = intValue % 60;
		result.append(hours).append(":");
		if (mins >= 10)
			result.append(mins);
		else
			result.append("0").append(mins);
		result.append(":");
		if (secs >= 10)
			result.append(secs);
		else
			result.append("0").append(secs);
		return result.toString();
	}
  
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2)
  {
    // sort by order of enums!
    Integer val1 = (Integer) dataValue1;
    Integer val2 = (Integer) dataValue2;
    
    return val1.compareTo(val2);
  }
  
  public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale) throws InvalidExpressionException
	{
    if (object == null)
    {
      return null;
    }
    if (object instanceof Integer)
		{
			return object;
		}
		if (object instanceof String)
		{
			// parse hh:mm:ss string
			String str = ((String) object).trim();
			int factor = 1;
			
			// handle negative case
			if (str.startsWith("-"))
			{
			  factor = -1;
			  str = str.substring(1);
			}
			
			try
			{
				StringTokenizer st = new StringTokenizer(str, ":");
				if (!st.hasMoreTokens())
				{
					return null;
				}
				int intValue = Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					return new Integer(factor*intValue);
				}
				intValue *= 60;
				intValue += Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					return new Integer(factor*intValue);
				}
				intValue *= 60;
				intValue += Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					return new Integer(factor*intValue);
				}
			}
			catch (Exception ex)
			{
				// ignore
			}
			throw new InvalidExpressionException(str);
		}
		if (object instanceof Long)
		{
			return new Integer(((Long) object).intValue());
		}
		throw new IllegalArgumentException(object.getClass().toString());
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    Object value = constraint.getValue();
    if (value instanceof String)
    {
      return QBEExpression.parseInterval((String) value);
    }

    return super.createQBEExpression(dataSource, constraint);
  }

  public static void main(String[] args)
	{
		try
		{
      QeSIntervalFieldType test = new QeSIntervalFieldType();
      System.out.println(test.convertObjectToDataValue(null, "2:30:10", null, null));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
	public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    super.toJacob(tableField, options);
    if (!options.isSuppressCustomFieldTypes())
      tableField.setCustomFieldType(this.getClass().getName());
  }

}
