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
package de.tif.jacob.license;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * License Information. Basic information (company Name, email Address, and
 * expiration date) and a license key is encapsulated within.
 * 
 * @author Andreas Sonntag
 */
public final class License
{
  static public final transient String RCS_ID = "$Id: License.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public static final String SEPARATOR = "#";

  private static final String INDEFINITE = "INDEFINITE";

  private final long licenseNbr;
  private final String companyName;
  private final String email;
  private final Date expirationDate;
  private final int userCount;
  private final String licenseKey;
  private final boolean demo;
  private final String description;

  /**
   * constructor to create a license from the specified license key
   * 
   * @param licenseInfo
   *          the decoded license info
   * @param licenseKey
   *          the complete license key
   * @throws LicenseException
   */
  public License(String licenseInfo, String licenseKey) throws LicenseException
  {
    this.licenseKey = licenseKey;

    String tokens[] = licenseInfo.split(SEPARATOR);
    int i = 0;

    // enhanced license info?
    if (tokens.length == 7)
    {
      // YES
      this.licenseNbr = Long.parseLong(tokens[i++]);
      this.description = tokens[i++];
    }
    else
    {
      // NO
      if (tokens.length != 5)
        throw new LicenseException("LicenseKey is invalid");

      this.licenseNbr = 0;
      this.description = null;
    }

    this.companyName = tokens[i++];
    this.email = tokens[i++];
    this.expirationDate = parseExpirationDate(tokens[i++]);
    try
    {
      this.userCount = Integer.parseInt(tokens[i++]);
      this.demo = new Boolean(tokens[i++]).booleanValue();
    }
    catch (NumberFormatException e)
    {
      throw new LicenseException("LicenseKey is invalid");
    }
  }

  /**
   * Constructor to create a license from simple license data.
   * 
   * @param companyName
   *          the company name
   * @param email
   *          the email of the company contact
   * @param expirationDate
   *          the expiration date
   * @param userCount
   *          the user count
   * @param demo
   *          the demo flag
   */
  public License(String companyName, String email, Date expirationDate, int userCount, boolean demo)
  {
    if (companyName == null)
      throw new NullPointerException("companyName is null");
    if (email == null)
      throw new NullPointerException("email is null");
    if (userCount <= 0)
      throw new RuntimeException("invalid userCount: " + userCount);

    this.licenseNbr = 0;
    this.companyName = companyName;
    this.email = email;
    this.expirationDate = expirationDate;
    this.userCount = userCount;
    this.demo = demo;
    this.description = null;
    this.licenseKey = null;
  }

  /**
   * Constructor to create a license from enhanced license data.
   * 
   * @param licenseNbr
   *          the license number
   * @param companyName
   *          the company name
   * @param email
   *          the email of the company contact
   * @param expirationDate
   *          the expiration date or <code>null</code> for indefinite keys
   * @param userCount
   *          the user count
   * @param demo
   *          the demo flag
   * @param additionalDescription
   *          additional description
   */
  public License(long licenseNbr, String companyName, String email, Date expirationDate, int userCount, boolean demo, String additionalDescription)
  {
    if (companyName == null)
      throw new NullPointerException("companyName is null");
    if (email == null)
      throw new NullPointerException("email is null");
    if (userCount <= 0)
      throw new RuntimeException("invalid userCount: " + userCount);
    if (licenseNbr <= 0)
      throw new RuntimeException("invalid licenseNbr: " + licenseNbr);

    this.licenseNbr = licenseNbr;
    this.companyName = companyName;
    this.email = email;
    this.expirationDate = expirationDate;
    this.userCount = userCount;
    this.demo = demo;
    this.description = additionalDescription;
    this.licenseKey = null;
  }

  public boolean isDemo()
  {
    return demo;
  }

  public String getCompanyName()
  {
    return companyName;
  }

  /**
   * Returns the number of the license.
   * 
   * @return the license number
   */
  public long getLicenseNbr()
  {
    if (isEnhanced())
      return this.licenseNbr;

    // construct hash code as pseudo license number
    if (this.licenseKey == null)
    {
      // should never occur
      throw new IllegalStateException();
    }
    return licenseKey.hashCode();
  }

  private boolean isEnhanced()
  {
    return this.licenseNbr > 0;
  }

  /**
   * Returns the license info to sign.
   * 
   * @return the license info
   */
  public String getLicenseInfo()
  {
    StringBuffer buffer = new StringBuffer(256);

    if (isEnhanced())
    {
      buffer.append(this.licenseNbr).append(SEPARATOR);
      buffer.append(this.description).append(SEPARATOR);
    }
    buffer.append(this.companyName).append(SEPARATOR);
    buffer.append(this.email).append(SEPARATOR);
    buffer.append(getExpirationDateString()).append(SEPARATOR);
    buffer.append(this.userCount).append(SEPARATOR);
    buffer.append(this.demo).append(SEPARATOR);

    return buffer.toString();
  }

  public String getEmailAddress()
  {
    return this.email;
  }

  /**
   * Returns the license expiration date.
   * 
   * @return the license expiration date or <code>null</code> for indefinite
   *         expiration.
   */
  public Date getExpirationDate()
  {
    return this.expirationDate;
  }

  public String getExpirationDateString()
  {
    if (this.expirationDate == null)
      return INDEFINITE;

    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
    return df.format(expirationDate);
  }

  /**
   * Parses the expiration string in US format and converts it to a date.
   * 
   * @param expirationDateStr
   *          the string to parse
   * @return the expiration date
   * @throws LicenseException
   *           on any parse error
   */
  private static Date parseExpirationDate(String expirationDateStr) throws LicenseException
  {
    if (INDEFINITE.equals(expirationDateStr))
      return null;

    try
    {
      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
      return df.parse(expirationDateStr);
    }
    catch (Exception e)
    {
      throw new LicenseException(e.getMessage());
    }
  }

  /**
   * Determines if the license is expired.
   * 
   * @return <code>true</code> if license is expired, otherwise <code>false</code>
   */
  public boolean isExpired()
  {
    // indefinite license?
    if (this.expirationDate == null)
      return false;

    return System.currentTimeMillis() > expirationDate.getTime();
  }

  /**
   * Returns the number of concurrent user sessions.
   * 
   * @return number of concurrent user sessions
   */
  public int getUserCount()
  {
    return userCount;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("jACOB License");
    if (isEnhanced())
    {
      buffer.append("\n\tNo.: ").append(this.licenseNbr);
      buffer.append("\n\tDescription: ").append(this.description);
    }
    buffer.append("\n\tCompany: ").append(getCompanyName());
    buffer.append("\n\teMail: ").append(getEmailAddress());
    buffer.append("\n\texpires: ").append(getExpirationDateString());
    buffer.append("\n\tconcurrent users: ").append(getUserCount());
    buffer.append("\n\tdemo: ").append(isDemo());
    return buffer.toString();
  }
}
