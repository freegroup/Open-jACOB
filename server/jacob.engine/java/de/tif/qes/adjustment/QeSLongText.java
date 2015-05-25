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

package de.tif.qes.adjustment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataLongText;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QeSLongText extends DataLongText
{
	static public final transient String RCS_ID = "$Id: QeSLongText.java,v 1.1 2006-12-21 11:31:25 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	static private final transient Log logger = LogFactory.getLog(QeSLongText.class);

	private String dataSourceName;
	private long key;

	private transient String prependText = null;
	private transient String appendText = null;
	private transient boolean appendPrependMode;

	private QeSLongText(QeSLongText other)
	{
		super(other);
		this.dataSourceName = other.dataSourceName;
		this.key = other.key;
		this.prependText = other.prependText;
		this.appendText = other.appendText;
		this.appendPrependMode = other.appendPrependMode;
	}

	protected QeSLongText(String dataSourceName, long key)
	{
		super();
		this.dataSourceName = dataSourceName;
		this.key = key;
		this.appendPrependMode = true;
	}

	protected QeSLongText(String dataSourceName, long key, String longText)
	{
		super(longText);
		this.dataSourceName = dataSourceName;
		this.key = key;
		this.appendPrependMode = false;
	}

  protected QeSLongText(String dataSourceName)
  {
    super((String) null);
    this.dataSourceName = dataSourceName;
    this.key = -1;
    this.appendPrependMode = false;
  }

  protected QeSLongText(String dataSourceName, String longText)
  {
    super(longText);
    this.dataSourceName = dataSourceName;
    this.key = -1;
    this.appendPrependMode = false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object anObject)
  {
    if (this == anObject)
    {
      return true;
    }
    if (anObject instanceof QeSLongText)
    {
      QeSLongText other = (QeSLongText) anObject;

      if (this.key == other.key)
      {
        if (StringUtil.saveEquals(this.prependText, other.prependText) && 
            StringUtil.saveEquals(this.appendText, other.appendText))
        {
          return super.equals(anObject);
        }
      }
    }
    return false;
  }
  
  protected String fetchLongText() throws Exception
	{
		// hack: it is always assumed that a long text field comes together with an
		// SQL datasource
		SQLDataSource dataSource = (SQLDataSource) DataSource.get(this.dataSourceName);

		try
		{
			Connection connection = dataSource.getConnection();
			try
			{
				PreparedStatement statement = connection.prepareStatement("SELECT text FROM qw_text WHERE qwkey=?");
				try
				{
					statement.setLong(1, this.key);
					ResultSet rs = statement.executeQuery();
					try
					{
						if (rs.next())
						{
							return rs.getString(1);
						}
						else
						{
							// TODO: create warning
							return null;
						}
					}
					finally
					{
						rs.close();
					}
				}
				finally
				{
					statement.close();
				}
			}
			finally
			{
				connection.close();
			}
		}
		catch (SQLException ex)
		{
			logger.error("Fetching long text failed!", ex);
			throw new RuntimeException(ex.toString());
		}
	}

	public static void main(String[] args)
	{
		//		try
		//		{
		//      System.setProperty("jacob.configfile", System.getProperty("user.dir") +
		// File.separator + "WEB-INF" + File.separator + "conf" + File.separator +
		// "jacob.properties");
		//      new Database().init();
		//      
		//      QeSLongText qesLongText = new QeSLongText("caretaker", 2558);
		//			String reference = qesLongText.toReference();
		//			System.out.println("Reference: " + reference);
		//			DataLongText longText = DataLongText.createByReference(reference);
		//
		//			System.out.println("FINISHED: " + longText.getValue());
		//		}
		//		catch (Throwable ex)
		//		{
		//			ex.printStackTrace();
		//		}
	}

	/**
	 * toString methode: creates a String representation of the object
	 * 
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("QeSLongText[");
		buffer.append("dataSourceName = ").append(dataSourceName);
		buffer.append(", key = ").append(key);
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * @return Returns the key.
	 */
	protected long getKey()
	{
		return key;
	}

	/**
	 * @return Returns the isNew.
	 */
	protected boolean isNew()
	{
		return this.key == -1;
	}

	/**
	 * @return Returns the dataSourceName.
	 */
	protected String getDataSourceName()
	{
		return dataSourceName;
	}

	/**
	 * @param key
	 *          The key to set.
	 */
	protected void setKey(long key)
	{
		this.key = key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.DataLongText#toReference(de.tif.jacob.core.data.DataTableRecord,
	 *      java.lang.String)
	 */
	protected String toReference(IDataTableRecord record, String fieldName)
	{
		// ignore arguments
		return this.toReferenceInternal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.DataLongText#append(java.lang.String)
	 */
	protected DataLongText append(String text)
	{
		if (this.appendPrependMode)
		{
			QeSLongText newLongText = new QeSLongText(this);
			newLongText.appendText = this.appendText == null ? text : this.appendText + text;
			return newLongText;
		}
		else
		{
			String oldValue = getValue();
			return new QeSLongText(this.dataSourceName, this.key, oldValue == null ? text : oldValue + text);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.DataLongText#prepend(java.lang.String)
	 */
	protected DataLongText prepend(String text)
	{
		if (this.appendPrependMode)
		{
			QeSLongText newLongText = new QeSLongText(this);
			newLongText.prependText = this.prependText == null ? text : text + this.prependText;
			return newLongText;
		}
		else
		{
			String oldValue = getValue();
			return new QeSLongText(this.dataSourceName, this.key, oldValue == null ? text : text + oldValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.DataLongText#getValue()
	 */
	public String getValue()
	{
		String value = super.getValue();
		if (!this.appendPrependMode || (this.appendText == null && this.prependText == null))
			return value;

		// calculate buffer size
		int size = 0;
		if (value != null)
			size += value.length();
		if (this.prependText != null)
			size += this.prependText.length();
		if (this.appendText != null)
			size += this.appendText.length();

		// build result string
		StringBuffer buffer = new StringBuffer(size);
		if (this.prependText != null)
			buffer.append(this.prependText);
		if (value != null)
			buffer.append(value);
		if (this.appendText != null)
			buffer.append(this.appendText);
		return buffer.toString();
	}

	/**
	 * @return Returns the appendPrependMode.
	 */
	protected final boolean isAppendPrependMode()
	{
		return appendPrependMode;
	}

	/**
	 * @return Returns the appendText.
	 */
	protected final String getAppendText()
	{
		return appendText == null ? "" : appendText;
	}

	/**
	 * @return Returns the prependText.
	 */
	protected final String getPrependText()
	{
		return prependText == null ? "" : prependText;
	}

}
