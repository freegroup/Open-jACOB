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

package de.tif.jacob.license.impl;

import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.license.License;
import de.tif.jacob.license.LicenseException;
import de.tif.jacob.license.LicenseManager;

/**
 * OpenjACOB license manager.
 * 
 * @author andreas
 */
public class OpenJacobLicenseManager extends LicenseManager
{
  static public final transient String RCS_ID = "$Id: OpenJacobLicenseManager.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public boolean hasValidLicense()
  {
    // always valid
    return true;
  }

  public synchronized License fetchLicense() throws LicenseException
  {
    return getLicense();
  }
  
  public synchronized License getLicense() throws LicenseException
  {
    return new License("OpenjACOB any Company", "support@tarragon-software.com", null, Integer.MAX_VALUE, false);
  }
  
  public synchronized void setLicense(String licenseKey) throws LicenseException
  {
  }
  
  public License validateLicense(String licenseKey) throws LicenseException
  {
    throw new UserRuntimeException("Openjacob license is always valid and cannot be changed");
  }
}
