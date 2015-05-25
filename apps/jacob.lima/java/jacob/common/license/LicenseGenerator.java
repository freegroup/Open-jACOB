/*
 * $Id: LicenseGenerator.java,v 1.3 2006/03/07 19:20:35 sonntag Exp $
 *
 *  Copyright (c) 2003 SourceTap - www.sourcetap.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a
 *  copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included
 *  in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 *  OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 *  OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 *  THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package jacob.common.license;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import de.tif.jacob.license.Base64Coder;
import de.tif.jacob.license.EncryptionUtil;
import de.tif.jacob.license.LicenseException;

/**
 * Generate an encrypted license
 * 
 * @author Steve Fowler
 * @version $Revision: 1.3 $
 */
public class LicenseGenerator
{
  private LicenseGenerator()
  {
  }

  /**
   * return an encrypted license by signing the license with the private key
   * file and encoding the result in Base64
   * 
   * @param license
   *          the unencrypted license key
   * @param privateKeyFile
   *          the name of the file containing the private key
   * @return a Base64 representation of the encrypted license
   * @throws LicenseException
   */
  public static String generateLicense(String license, byte[] privateKey) throws LicenseException
  {
    try
    {
      String signature = signMessage(license, privateKey);
      String licenseEncoded = Base64Coder.encode(license) + "#" + signature;

      return licenseEncoded;
    }
    catch (Exception e)
    {
      throw new LicenseException(e.getMessage());
    }
  }

  public static String signMessage(String message, byte[] privateKey) throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
      InvalidKeyException, SignatureException, InvalidKeySpecException
  {
    if (message == null)
      throw new SignatureException("No Message to sign");

    PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKey);
    KeyFactory keyFactory = KeyFactory.getInstance("DSA");
    PrivateKey key = keyFactory.generatePrivate(privKeySpec);

    EncryptionUtil signer = new EncryptionUtil();
    String signature = signer.sign(message, key);
    return signature;
  }
}