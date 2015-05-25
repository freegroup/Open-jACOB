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

/**
 * Abstract class for manipulating license data
 *
 * @author Andreas Herz
 */
public abstract class LicenseManager
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: LicenseManager.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
  
  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Checks if a valid unexpired license exists.
   * 
   * @return <code>true</code> if a valid license exists, <code>false</code>
   *         if the license is invalid or expired or no license exists at all.
   */
  public abstract boolean hasValidLicense();

  /**
   * Return the license, which might be cached for performance reasons.
   * 
   * @return the license object or <code>null</code> if no license exists so far
   * @throws LicenseException if the license is invalid
   */
  public abstract License getLicense() throws LicenseException;

  /**
   * Fetch the license from persistent store.
   * 
   * @return the license object or <code>null</code> if no license exists so far
   * @throws LicenseException if the license is invalid
   */
  public abstract License fetchLicense() throws LicenseException;

  /**
   * Sets the license object by means of a given license key.
   * 
   * @param licenseKey the encrypted license key returned from the Registration Server
   * @throws LicenseException
   */
  public abstract void setLicense(String licenseKey) throws LicenseException;
  
  /**
   * Validates the given license key.
   * @param licenseKey the encrypted license key returned from the Registration Server
   * @return the license object
   * @throws LicenseException
   */
  public abstract License validateLicense(String licenseKey) throws LicenseException;
}
