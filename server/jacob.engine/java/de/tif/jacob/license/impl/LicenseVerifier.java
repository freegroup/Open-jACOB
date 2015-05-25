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

import java.security.PublicKey;

import de.tif.jacob.license.License;
import de.tif.jacob.license.LicenseException;

/**
 * Verify a License Key using the public key to decrypt it
 * 
 * @author Andreas Sonntag
 */
public class LicenseVerifier
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: LicenseVerifier.java,v 1.1 2006-12-21 11:25:22 sonntag Exp $";
  
  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  public LicenseVerifier()
  {
  }

  /**
   * Decrypt the encoded license key and return the corresponding License Object
   * 
   * @param licenseKey
   *          the license key as generated by the Registration Server
   * @param publicKeyFile
   *          the name of the file containing the public key
   * @return the License Object corresponding to the supplied licenseKey
   * @throws LicenseException
   */
  public static License validateLicense(String licenseKey, String publicKeyFile) throws LicenseException
  {
    try
    {
      EncryptionUtil encrytor = new EncryptionUtil();

      // the license key consists of the base64 encoded license info and
      // the signature generated by the private key, separated by a #
      String tokens[] = licenseKey.split("#");
      if (tokens.length != 2)
        throw new LicenseException("LicenseKey is invalid");
      String licenseInfo = Base64Coder.decode(tokens[0]);
      String signature = tokens[1];

      // verify that the license info has not be manipulated
      if (!encrytor.verify(licenseInfo, signature, publicKeyFile))
        throw new LicenseException("License key verification failed.");

      return new License(licenseInfo, licenseKey);
    }
    catch (LicenseException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new LicenseException("LicenseKey is invalid: " + e.getMessage());
    }
  }

  /**
   * Read a public Key from a file
   * 
   * @param URI
   *          the name of the file containing the public key
   * @return the public key
   * @throws LicenseException
   */
  public static PublicKey readPublicKey(String URI) throws LicenseException
  {
    try
    {
      EncryptionUtil keyUtil = new EncryptionUtil();
      PublicKey publicKey = keyUtil.readPublicKey(URI);
      return publicKey;
    }
    catch (Exception e)
    {
      throw new LicenseException("Unable to read Key File:" + e.getMessage());
    }
  }
}