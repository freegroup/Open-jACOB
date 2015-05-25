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

import java.io.*;
import java.security.*;
import java.security.spec.*;


/**
 * Provides utility functions to handle public/private key encryption.
 *
 * @author Andreas
 */
public class EncryptionUtil
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: EncryptionUtil.java,v 1.1 2006-12-21 11:25:22 sonntag Exp $";
  
  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

	private PublicKey publicKey = null;
	private PrivateKey privateKey = null;
	
	public EncryptionUtil()
	{
	}

	/**
	* Generate a public/private key pair 
	*/
	public void generateKeys() 
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
		keyGen.initialize(1024, new SecureRandom());
		KeyPair pair = keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	/**
	* Generate a public/private key pair, and write the keys to the specified files
	* @param publicURI   name of file to store the public key in
	* @param privateURI  name of file to store the private key in
	*/
	public void generateKeys( String publicURI, String privateURI )
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException
	{
		generateKeys();
		writeKeys(publicURI, privateURI);
	}

	public PublicKey getPublic()
	{
		return publicKey;
	}
	
	public PrivateKey getPrivate()
	{
		return privateKey;
	}
	
	public void writeKeys(String publicURI, String privateURI) throws IOException, FileNotFoundException
	{
		writePublicKey(publicURI);
		writePrivateKey(privateURI);
	}
	
	public void writePublicKey(String URI) throws IOException, FileNotFoundException
	{
		byte[] enckey = publicKey.getEncoded();
		FileOutputStream keyfos = new FileOutputStream(URI);
		keyfos.write(enckey);
		keyfos.close();
	}
	
	public void writePrivateKey(String URI) throws IOException, FileNotFoundException
	{
		byte[] enckey = privateKey.getEncoded();
		FileOutputStream keyfos = new FileOutputStream(URI);
		keyfos.write(enckey);
		keyfos.close();
	}
	
	/**
	* read public/private keys from specified files
	* @param publicURI   name of public key file
	* @param privateURI  name of private key file
	*/
	public void readKeys(String publicURI, String privateURI)
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException
	{
		readPublicKey(publicURI);
		readPrivateKey(privateURI);
	}

	/**
	* read public key from specified file
	* @param publicURI   name of public key file
	* @return PublicKey  public key
	*/
  public PublicKey readPublicKey(String URI) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException
  {
    FileInputStream keyfis = new FileInputStream(URI);
    byte[] encKey = new byte[keyfis.available()];
    keyfis.read(encKey);
    keyfis.close();
    publicKey = makePublicKey(encKey);
    return publicKey;
  }

  public static PublicKey makePublicKey(byte[] encKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException
  {
    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
    KeyFactory keyFactory = KeyFactory.getInstance("DSA");
    return keyFactory.generatePublic(pubKeySpec);
  }

	/**
	* read private key from specified file
	* @param privateURI   name of private key file
	* @return PrivateKey  private key
	*/
	public PrivateKey readPrivateKey(String URI) 
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException
	{
		FileInputStream keyfis = new FileInputStream(URI);
		byte[] encKey = new byte[keyfis.available()];
		keyfis.read(encKey);
		keyfis.close();
		privateKey = makePrivateKey(encKey);
		return privateKey;
	}
	
  public static PrivateKey makePrivateKey(byte[] encKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException
  {
    PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);
    KeyFactory keyFactory = KeyFactory.getInstance("DSA");
    return keyFactory.generatePrivate(privKeySpec);
  }

	/**
	* sign a message using the private key
	* @param message   the message to be signed
	* @return String  the signed message encoded in Base64
	*/
	public String sign(String message) 
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
		InvalidKeySpecException, InvalidKeyException, SignatureException
	{
		return sign(message, privateKey);
	}

	/**
	* sign a message using the private key
	* @param message   the message to be signed
	* @param message   the name of the file containing the private key
	* @return String  the signed message encoded in Base64
	*/
	public String sign(String message, String privateKeyURI) 
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
		InvalidKeySpecException, InvalidKeyException, SignatureException, IOException
	{
		PrivateKey pk = readPrivateKey(privateKeyURI);
		return sign(message, pk);
	}

	/**
	* sign a message using the private key
	* @param message   the message to be signed
	* @param message   the private key
	* @return String   the signed message encoded in Base64
	*/
	public String sign(String message, PrivateKey privateKey) 
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
		InvalidKeySpecException, InvalidKeyException, SignatureException
	{
		Signature dsa = Signature.getInstance("SHA/DSA");
		dsa.initSign(privateKey);
		dsa.update(message.getBytes());
		byte m1[] = dsa.sign();
		
		String signature = new String(Base64Coder.encode(m1));
		
		return signature;
	}
	
	/**
	* verify that the message was signed by the private key by using the public key
	* @param message     the message to be verified
	* @param signature   the signature generated by the private key and encoded in Base64
	* @param publicKeyURI   the name of the file containing the public key
	* @return boolean   true if the message was signed by the private key
	*/
	public boolean verify(String message, String signature, String publicKeyURI)
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
		InvalidKeySpecException, InvalidKeyException, SignatureException
	{
		
		PublicKey pk = readPublicKey(publicKeyURI);
		return verify(message, signature, pk);
	}
	
	/**
	* verify that the message was signed by the private key by using the public key
	* @param message     the message to be verified
	* @param signature   the signature generated by the private key and encoded in Base64
	* @return boolean   true if the message was signed by the private key
	*/
	public boolean verify(String message, String signature)
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
		InvalidKeySpecException, InvalidKeyException, SignatureException
	{
		if ( publicKey == null )
			throw new InvalidKeyException("Public Key not provided.");
		return verify( message, signature, publicKey);
	}
	
	/**
	* verify that the message was signed by the private key by using the public key
	* @param message     the message to be verified
	* @param signature   the signature generated by the private key and encoded in Base64
	* @param publicKey   the public key
	* @return boolean   true if the message was signed by the private key
	*/
	public boolean verify(String message, String signature, PublicKey publicKey)
		throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
		InvalidKeySpecException, InvalidKeyException, SignatureException
	{
		Signature dsa = Signature.getInstance("SHA/DSA");
		dsa.initVerify(publicKey);
		dsa.update(message.getBytes());
		
		byte sigDec[] = Base64Coder.decode(signature.toCharArray());
		return dsa.verify(sigDec);
	}

}
