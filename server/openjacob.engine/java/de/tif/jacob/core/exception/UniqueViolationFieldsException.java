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
 * Created on 15.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.exception;

import java.util.Collection;
import java.util.List;

import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.i18n.CoreMessage;

/**
 * This exception indicates that a unique key violation has been occurred. This
 * is the case, if a data table record is going to be modified in such a way,
 * that a unique constraint is not fulfilled anymore.
 * 
 * @author Andreas Sonntag
 * @since 2.7.2
 */
public final class UniqueViolationFieldsException extends UniqueViolationException implements ITableFieldsException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: UniqueViolationFieldsException.java,v 1.2 2008/07/09 14:27:35 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.2 $";

  private final Collection tableFields;

  /**
   * Constructs the <code>UniqueViolationFieldsException</code>.
   * 
   * @param uniqueKey
   *          the unique key which is violated
   */
  public UniqueViolationFieldsException(IKey uniqueKey)
  {
    this(uniqueKey, null);
  }

  /**
   * Constructs the <code>UniqueViolationFieldsException</code>.
   * 
   * @param uniqueKey
   *          the unique key which is violated
   * @param details
   *          additional details or <code>null</code>
   * @since 2.7.4
   */
  public UniqueViolationFieldsException(IKey uniqueKey, String details)
  {
    super(new CoreMessage(CoreMessage.UNIQUE_FIELDS_VIOLATION, determineLabels(uniqueKey)), details);
    this.tableFields = uniqueKey.getTableFields();
  }

  private static String determineLabels(IKey uniqueKey)
  {
    StringBuffer buffer = new StringBuffer();
    List fields = uniqueKey.getTableFields();
    for (int i = 0; i < fields.size(); i++)
    {
      buffer.append(AbstractTableFieldException.determineLabel((ITableField) fields.get(i)));
      if (i + 1 < fields.size())
        buffer.append("', '");
    }
    return buffer.toString();
  }

  public Collection getTableFields()
  {
    return this.tableFields;
  }
}
