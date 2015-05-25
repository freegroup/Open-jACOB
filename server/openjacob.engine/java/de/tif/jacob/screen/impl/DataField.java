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

package de.tif.jacob.screen.impl;

import org.apache.slide.webdav.util.URLUtil;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.webdav.impl.WebDAV;
import de.tif.jacob.webdav.impl.WebdavDocument;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataField
{
  static public final transient String RCS_ID = "$Id: DataField.java,v 1.5 2009/03/13 15:59:16 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  private static class Type{};
  public final static Type TYPE_NORMAL  = new Type();           // normal data field
  public final static Type TYPE_FOREIGN = new Type();           // foreign field data element
  public final static Type TYPE_INLINE  = new Type();           // foreign field data element
  
  private Type fieldType = TYPE_NORMAL;
  private final ITableAlias tableAlias;
  private final ITableField field;
  private Object value;
  // wird für multiple Update benötigt
  // Hinweis dafür, dass die beteiligten Records unterschiedliche Werte haben.
  private final HTTPSingleDataGuiElement parent;
  private final boolean isRequired;
  
  private MyWebdavDocument webdavDocument=null;

  /**
   * Class to get and set document data from the <code>DataDocumentValue</code>
   * associated to this document field.
   * 
   * @author Andreas Sonntag
   */
  private class MyWebdavDocument extends WebdavDocument
  {
    private final Object application;
    
    private MyWebdavDocument(IApplication application)
    {
      this.application = application;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.webdav.impl.WebdavDocument#getLength()
     */
    public int getContentLength()
    {
      // requests from the jACOB GUI must be synchronized with WebDAV requests
      // via client applications!
      // 
      synchronized (application)
      {
        Object value = getValue();
        if (value instanceof DataDocumentValue)
        {
          DataDocumentValue document = (DataDocumentValue) value;
          return document.getContent().length;
        }
      }
      return 0;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.webdav.impl.WebdavDocument#getContent()
     */
    public byte[] getContent()
    {
      // requests from the jACOB GUI must be synchronized with WebDAV requests
      // via client applications!
      // 
      synchronized (application)
      {
        Object value = getValue();
        if (value instanceof DataDocumentValue)
        {
          DataDocumentValue document = (DataDocumentValue) value;
          return document.getContent();
        }
      }
      return null;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.webdav.impl.WebdavDocument#setContent(byte[])
     */
    public boolean setContent(byte[] content)
    {
      // requests from the jACOB GUI must be synchronized with WebDAV requests
      // via client applications!
      // 
      synchronized (application)
      {
        if (value instanceof DataDocumentValue)
        {
          DataDocumentValue document = (DataDocumentValue) value;
          value =DataDocumentValue.create(document.getName(), content);
          return true;
        }
      }
      return false;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.webdav.impl.WebdavDocument#getName()
     */
    public String getName()
    {
      // requests from the jACOB GUI must be synchronized with WebDAV requests
      // via client applications!
      // 
      synchronized (application)
      {
        Object value = getValue();
        if (value instanceof DataDocumentValue)
        {
          DataDocumentValue document = (DataDocumentValue) value;
          return document.getName();
        }
      }
      return null;
    }
  }

	/**
	 * @param tableAlias
	 * @param column
	 * @param value
	 */
	public DataField(HTTPSingleDataGuiElement parent,  ITableAlias tableAlias, ITableField column, String value)
	{
		super();

		if((tableAlias==null && column!=null ) || (tableAlias!=null && column==null))
		{
		  throw new RuntimeException("Table alias and field dosn't match [table="+tableAlias+"][field="+column+"]");
		}
    this.parent          = parent;
		this.tableAlias      = tableAlias;
		this.field           = column;
    this.value           = value;
    this.isRequired      = column != null && column.isRequired();
	}
  
  
  /**
   * The corresponding table (alias name) of this field
   * 
   * @return The table name (alias)
   */
  public ITableAlias getTableAlias()
  {
    return tableAlias; 
  }
  
  /**
   * The field name of the data element
   * 
   * @return The name of the field
   */
  public ITableField getField()
  {
    return field; 
  }
  
  public Object getValue()
  {
    return value; 
  }
  
  /**
   * required for the QBE. Don't know to handle 'Image' at this time 
   * @return
   */
  public String getQBEValue()
  {
    if(value instanceof String)
      return (String)value;
    return "";
  }
  
  public void setValue(Object value)
  {
    if(this.value==value)
      return;
    
    // value has changed -> drop webdav document proxy
    // if existing..
    //
    if (this.webdavDocument != null)
    {
      this.webdavDocument.close();
      this.webdavDocument = null;
    }
    
    // create a new webdav reference if required/possible
    //
    if(value instanceof DataDocumentValue)
      this.webdavDocument = new MyWebdavDocument(parent.getApplication());
    this.value = value;
  }
  
  // should be move to DataDocumentValue?!
  public String getWebdavURL(HTTPClientSession session)
  {
    return session.getBaseUrl() + "webdav" + WebDAV.FILES_URI + "/" + URLUtil.URLEncode(webdavDocument.getFullName(), "ISO-8859-1");
  }
  
  // should be move to DataDocumentValue?!
  public String getDownloadURL(HTTPClientSession session)
  {
    return session.getBaseUrl()+ "document?documentId="+webdavDocument.getId();
  }

  /**
	 * @return Returns the fieldType.
	 */
	public Type getFieldType()
	{
		return fieldType;
	}

	/**
	 * @param fieldType The fieldType to set.
	 */
	public void setFieldType(Type fieldType)
	{
		this.fieldType = fieldType;
	}

  public ISingleDataGuiElement getParent()
  {
   return parent; 
  }
  
  public boolean isRequired()
  {
   return isRequired; 
  }
  

  /**
   * Indicator required for MultipleUpdate. The related Records did have different values if this method returns
   * true.
   * 
   * @return
   */
  public boolean isDiverse()
  {
    return this.value==IDataMultiUpdateTableRecord.DIVERSE;
  }


		/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("DataField[");
		buffer.append("tableAlias = ").append(tableAlias);
		buffer.append(", column = ").append(field);
//		buffer.append(", value = ").append(value);
//    buffer.append(", parent = ").append(parent); don't add them -> recursion
    buffer.append(", parent = ").append(parent.getPathName());
    buffer.append(", isRequired = ").append(isRequired);
		buffer.append("]");
		return buffer.toString();
	}
}
