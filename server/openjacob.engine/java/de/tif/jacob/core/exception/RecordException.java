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

package de.tif.jacob.core.exception;

import java.util.Locale;

import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.Message;

/**
 * Abstract record exception.
 * 
 * @author Andreas Sonntag
 * @since 2.8.5
 */
public abstract class RecordException extends UserException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: RecordException.java,v 1.3 2009/04/17 11:40:03 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.3 $";

  protected RecordException(Message message)
  {
    super(message);
  }

  public RecordException(Message message, String details)
  {
    super(message, details);
  }

  /**
   * Returns the id of the record which has caused this exception.
   * 
   * @return id of the record
   */
  public abstract IDataRecordId getRecordId();
  
  /**
   * Returns a localized short message for the given locale, i.e. the message
   * which does not contain record id info.
   * 
   * @param locale
   *          the locale
   * @return localized short message
   */
  public abstract String getLocalizedShortMessage(Locale locale);
}
