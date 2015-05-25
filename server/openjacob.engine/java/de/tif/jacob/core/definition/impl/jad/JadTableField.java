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

package de.tif.jacob.core.definition.impl.jad;

import java.lang.reflect.Constructor;

import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.fieldtypes.BinaryFieldType;
import de.tif.jacob.core.definition.fieldtypes.BooleanFieldType;
import de.tif.jacob.core.definition.fieldtypes.DateFieldType;
import de.tif.jacob.core.definition.fieldtypes.DecimalFieldType;
import de.tif.jacob.core.definition.fieldtypes.DocumentFieldType;
import de.tif.jacob.core.definition.fieldtypes.DoubleFieldType;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
import de.tif.jacob.core.definition.fieldtypes.FloatFieldType;
import de.tif.jacob.core.definition.fieldtypes.IntegerFieldType;
import de.tif.jacob.core.definition.fieldtypes.LongFieldType;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimeFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimestampFieldType;
import de.tif.jacob.core.definition.impl.AbstractTableField;
import de.tif.jacob.core.definition.impl.jad.castor.BinaryField;
import de.tif.jacob.core.definition.impl.jad.castor.BooleanField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.DateField;
import de.tif.jacob.core.definition.impl.jad.castor.DecimalField;
import de.tif.jacob.core.definition.impl.jad.castor.DocumentField;
import de.tif.jacob.core.definition.impl.jad.castor.DoubleField;
import de.tif.jacob.core.definition.impl.jad.castor.EnumerationField;
import de.tif.jacob.core.definition.impl.jad.castor.FloatField;
import de.tif.jacob.core.definition.impl.jad.castor.IntegerField;
import de.tif.jacob.core.definition.impl.jad.castor.LongField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextField;
import de.tif.jacob.core.definition.impl.jad.castor.TextField;
import de.tif.jacob.core.definition.impl.jad.castor.TimeField;
import de.tif.jacob.core.definition.impl.jad.castor.TimestampField;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadTableField extends AbstractTableField
{
  static public transient final String RCS_ID = "$Id: JadTableField.java,v 1.1 2007/01/19 09:50:38 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  private final static Class TEXT_ARGS[] = { TextField.class };
  private final static Class INTEGER_ARGS[] = { IntegerField.class };
  private final static Class BOOLEAN_ARGS[] = { BooleanField.class };
	private final static Class LONG_ARGS[] = { LongField.class };
	private final static Class FLOAT_ARGS[] = { FloatField.class };
	private final static Class DOUBLE_ARGS[] = { DoubleField.class };
	private final static Class DECIMAL_ARGS[] = { DecimalField.class };
	private final static Class DATE_ARGS[] = { DateField.class };
	private final static Class TIME_ARGS[] = { TimeField.class };
	private final static Class TIMESTAMP_ARGS[] = { TimestampField.class };
	private final static Class LONGTEXT_ARGS[] = { LongTextField.class };
	private final static Class BINARY_ARGS[] = { BinaryField.class };
	private final static Class DOCUMENT_ARGS[] = { DocumentField.class };
	private final static Class ENUMERATION_ARGS[] = { EnumerationField.class };

	/**
	 *  
	 */
	public JadTableField(CastorTableField tablefield, int index) throws Exception
	{
		super(tablefield.getName(), tablefield.getDbName(), tablefield.getLabel(), tablefield.getDescription(), getType(tablefield), tablefield.getRequired(), tablefield.getReadonly(), tablefield.getHistory());
		setFieldIndex(index);
    
    // handle properties
    if (tablefield.getPropertyCount() > 0)
      putCastorProperties(tablefield.getProperty());
  }

	private static FieldType getType(CastorTableField tablefield) throws Exception
	{
		if (null != tablefield.getCastorTableFieldChoice().getText())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, TEXT_ARGS, tablefield.getCastorTableFieldChoice().getText());
			}
			return new TextFieldType(tablefield.getCastorTableFieldChoice().getText());
		}

    if (null != tablefield.getCastorTableFieldChoice().getInteger())
    {
      if (tablefield.getCustomFieldType() != null)
      {
        return loadFieldType(tablefield, INTEGER_ARGS, tablefield.getCastorTableFieldChoice().getInteger());
      }
      return new IntegerFieldType(tablefield.getCastorTableFieldChoice().getInteger());
    }

    if (null != tablefield.getCastorTableFieldChoice().getBoolean())
    {
      if (tablefield.getCustomFieldType() != null)
      {
        return loadFieldType(tablefield, BOOLEAN_ARGS, tablefield.getCastorTableFieldChoice().getBoolean());
      }
      return new BooleanFieldType(tablefield.getCastorTableFieldChoice().getBoolean());
    }

		if (null != tablefield.getCastorTableFieldChoice().getLong())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, LONG_ARGS, tablefield.getCastorTableFieldChoice().getLong());
			}
			return new LongFieldType(tablefield.getCastorTableFieldChoice().getLong());
		}

		if (null != tablefield.getCastorTableFieldChoice().getFloat())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, FLOAT_ARGS, tablefield.getCastorTableFieldChoice().getFloat());
			}
			return new FloatFieldType(tablefield.getCastorTableFieldChoice().getFloat());
		}

		if (null != tablefield.getCastorTableFieldChoice().getDouble())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, DOUBLE_ARGS, tablefield.getCastorTableFieldChoice().getDouble());
			}
			return new DoubleFieldType(tablefield.getCastorTableFieldChoice().getDouble());
		}

		if (null != tablefield.getCastorTableFieldChoice().getDecimal())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, DECIMAL_ARGS, tablefield.getCastorTableFieldChoice().getDecimal());
			}
			return new DecimalFieldType(tablefield.getCastorTableFieldChoice().getDecimal());
		}

		if (null != tablefield.getCastorTableFieldChoice().getDate())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, DATE_ARGS, tablefield.getCastorTableFieldChoice().getDate());
			}
			return new DateFieldType(tablefield.getCastorTableFieldChoice().getDate());
		}

		if (null != tablefield.getCastorTableFieldChoice().getTime())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, TIME_ARGS, tablefield.getCastorTableFieldChoice().getTime());
			}
			return new TimeFieldType(tablefield.getCastorTableFieldChoice().getTime());
		}

		if (null != tablefield.getCastorTableFieldChoice().getTimestamp())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, TIMESTAMP_ARGS, tablefield.getCastorTableFieldChoice().getTimestamp());
			}
			return new TimestampFieldType(tablefield.getCastorTableFieldChoice().getTimestamp());
		}

		if (null != tablefield.getCastorTableFieldChoice().getLongText())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, LONGTEXT_ARGS, tablefield.getCastorTableFieldChoice().getLongText());
			}
			return new LongTextFieldType(tablefield.getCastorTableFieldChoice().getLongText());
		}

		if (null != tablefield.getCastorTableFieldChoice().getBinary())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, BINARY_ARGS, tablefield.getCastorTableFieldChoice().getBinary());
			}
			return new BinaryFieldType(tablefield.getCastorTableFieldChoice().getBinary());
		}

		if (null != tablefield.getCastorTableFieldChoice().getDocument())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, DOCUMENT_ARGS, tablefield.getCastorTableFieldChoice().getDocument());
			}
			return new DocumentFieldType(tablefield.getCastorTableFieldChoice().getDocument());
		}

		if (null != tablefield.getCastorTableFieldChoice().getEnumeration())
		{
			if (tablefield.getCustomFieldType() != null)
			{
				return loadFieldType(tablefield, ENUMERATION_ARGS, tablefield.getCastorTableFieldChoice().getEnumeration());
			}
			return new EnumerationFieldType(tablefield.getCastorTableFieldChoice().getEnumeration());
		}

		// should never occure
		throw new RuntimeException("Invalid XML: Missing type for table field " + tablefield.getName());
	}

	private static FieldType loadFieldType(CastorTableField tablefield, Class[] parameterTypes, Object parameter) throws Exception
	{
		Class clazz = Class.forName(tablefield.getCustomFieldType());
		Constructor constructor = clazz.getConstructor(parameterTypes);
		return (FieldType) constructor.newInstance(new Object[] { parameter });
	}

}
