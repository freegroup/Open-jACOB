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
 * Created on 22.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.exception;

import java.util.Locale;

import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.impl.DataRecordId;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.i18n.CoreMessage;

/**
 * This exception indicates that a given data table record could not be
 * retrieved from data source for any reason.
 * 
 * @author Andreas Sonntag
 */
public class RecordNotFoundException extends RecordException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: RecordNotFoundException.java,v 1.3 2009/03/27 00:45:06 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.3 $";

  private final IDataRecordId recordId;

  /**
   * Constructs the <code>RecordNotFoundException</code>.
   * 
   * @param recordId
   *          the id of the record which has not been found
   */
  public RecordNotFoundException(IDataRecordId recordId)
  {
    super(new CoreMessage(CoreMessage.RECORD_NOT_FOUND, recordId));
    this.recordId = recordId;
  }

  /**
   * Constructs the <code>RecordNotFoundException</code>.
   * 
   * @param tableDefinition the table definition
   * @param primaryKeyValue the record primary key value
   */
  public RecordNotFoundException(ITableDefinition tableDefinition, IDataKeyValue primaryKeyValue)
  {
    this(new DataRecordId(tableDefinition, primaryKeyValue));
  }

  /**
   * Returns the id of the record which has not been found.
   * 
   * @return id of the record which has not been found
   */
  public final IDataRecordId getRecordId()
  {
    return recordId;
  }
  
  /**
   * Returns a localized short message for the given locale, i.e. the message
   * which does not contain record id info.
   * 
   * @param locale
   *          the locale
   * @return localized short message
   * @since 2.8.5
   */
  public String getLocalizedShortMessage(Locale locale)
  {
    return (new CoreMessage(CoreMessage.RECORD_NOT_FOUND_SHORT)).print(locale);
  }
}
