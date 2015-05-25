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

import org.apache.commons.lang.exception.ExceptionUtils;

import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.i18n.CoreMessage;

/**
 * This exception indicates that a given data table record has already been
 * locked by another user.
 * 
 * @author Andreas Sonntag
 */
public class RecordLockedException extends RecordException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: RecordLockedException.java,v 1.4 2009/04/17 11:40:03 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.4 $";

  private final IDataRecordId recordId;
  private final String userId;
  private final String userName;

  /**
   * Constructs the <code>RecordLockedException</code>.
   * 
   * @param recordId
   *          the id of the locked record
   * @param userId
   *          the id of the user who has currently locked the record
   */
  public RecordLockedException(IDataRecordId recordId, String userId)
  {
    this(recordId, userId, null);
  }

  /**
   * Constructs the <code>RecordLockedException</code>.
   * 
   * @param recordId
   *          the id of the locked record
   * @param userId
   *          the id of the user who has currently locked the record
   * @param userName
   *          the name of the user who has currently locked the record or
   *          <code>null</code>
   * @since 2.8.5
   */
  public RecordLockedException(IDataRecordId recordId, String userId, String userName)
  {
    super(new CoreMessage(CoreMessage.RECORD_LOCKED, recordId, getUserString(userId, userName)),ExceptionUtils.getStackTrace(new Exception()));
    this.recordId = recordId;
    this.userId = userId;
    this.userName = userName;
  }

  private static String getUserString(String userId, String userName)
  {
    if (userName == null)
      return userId;
    return userName + " (" + userId + ")";
  }

  /**
   * Returns the id of the locked record.
   * 
   * @return id of the locked record
   * @since 2.6
   */
  public final IDataRecordId getRecordId()
  {
    return recordId;
  }

  /**
   * Returns the id of the user who has currently locked the record.
   * 
   * @return the user id
   * @since 2.8.5
   */
  public String getUserId()
  {
    return userId;
  }

  /**
   * Returns the name of the user who has currently locked the record.
   * 
   * @return the user name or <code>null</code> if not known
   * @since 2.8.5
   */
  public String getUserName()
  {
    return userName;
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
    return (new CoreMessage(CoreMessage.RECORD_LOCKED_SHORT, getUserString(this.userId, this.userName))).print(locale);
  }
}
