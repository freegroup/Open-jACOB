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

package de.tif.jacob.util;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64 {
  static public final transient String RCS_ID = "$Id: Base64.java,v 1.1 2007/01/19 09:50:47 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  
  /**
   * Returns a base64-encoded string to represent the passed data array.
   *
   * @param data the array of bytes to encode
   * @return base64-coded string.
   */
  static public String encode(byte[] data)
  {
    return new BASE64Encoder().encode(data);
  //  return new String(encodeAsArray(data)); 
  }

  /**
   * Returns an array of bytes which were encoded in the passed string
   *
   * @param data the base64-encoded string
   * @return decoded data array
   */
  static public byte[] decode(String data) throws IOException
  {
    return new BASE64Decoder().decodeBuffer(data);
    /*
    char[] tdata = new char[data.length()];
    data.getChars(0,data.length(),tdata,0);
    return decode(tdata);
    */
  }


  /**
   * Returns an array of base64-encoded characters to represent the
   * passed data array.
   *
   * @param data the array of bytes to encode
   * @return base64-coded character array.
   */
  static public char[] encodeAsArray(byte[] data)
  {
    char[] out = new char[((data.length + 2) / 3) * 4];

    //
    // 3 bytes encode to 4 chars.  Output is always an even
    // multiple of 4 characters.
    //
    for (int i=0, index=0; i<data.length; i+=3, index+=4) {
      boolean quad = false;
      boolean trip = false;

      int val = (0xFF & (int) data[i]);
      val <<= 8;
      if ((i+1) < data.length) {
        val |= (0xFF & (int) data[i+1]);
        trip = true;
      }
      val <<= 8;
      if ((i+2) < data.length) {
        val |= (0xFF & (int) data[i+2]);
        quad = true;
      }
      out[index+3] = b64code[(quad? (val & 0x3F): 64)];
      val >>= 6;
      out[index+2] = b64code[(trip? (val & 0x3F): 64)];
      val >>= 6;
      out[index+1] = b64code[val & 0x3F];
      val >>= 6;
      out[index+0] = b64code[val & 0x3F];
    }
    return out;
  }

  /**
   * Returns an array of bytes which were encoded in the passed
   * character array.
   *
   * @param data the array of base64-encoded characters
   * @return decoded data array
   */
  static public byte[] decode(char[] data)
  {
    int len = ((data.length + 3) / 4) * 3;
    if (data.length>0 && data[data.length-1] == '=') --len;
    if (data.length>1 && data[data.length-2] == '=') --len;
    byte[] out = new byte[len];

    int shift = 0;   // # of excess bits stored in accum
    int accum = 0;   // excess bits
    int index = 0;

    for (int ix=0; ix<data.length; ix++)
    {
      int value = b64icode[ data[ix] & 0xFF ];   // ignore high byte of char
      if ( value >= 0 ) {                     // skip over non-code
        accum <<= 6;            // bits shift up by 6 each time thru
        shift += 6;             // loop, with new bits being put in
        accum |= value;         // at the bottom.
        if ( shift >= 8 ) {     // whenever there are 8 or more shifted in,
          shift -= 8;         // write them out (from the top, leaving any
          out[index++] =      // excess at the bottom for next iteration.
                  (byte) ((accum >> shift) & 0xff);
        }   }   }
    if (index != out.length)
      throw new Error("miscalculated data length!");

    return out;
  }

  //
  // code characters for values 0..63
  //
  private static char[] b64code =
                   "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

  //
  // lookup table for converting base64 characters to value in range 0..63
  //
  private static byte[] b64icode = new byte[256];
  static {
    for (int i=0; i<256; i++) b64icode[i] = -1;
    for (int i=0; i<b64code.length; i++) b64icode[b64code[i]] = (byte)i;
    b64icode['='] = -1;
  }

}
