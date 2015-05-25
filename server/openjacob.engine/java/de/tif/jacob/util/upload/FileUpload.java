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
package de.tif.jacob.util.upload;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.screen.impl.HTTPUploadDialog;



public class FileUpload
{
  static private final Log logger = LogFactory.getLog(FileUpload.class);

  protected String err = "";
  protected HttpServletRequest request;
  protected HashMap name2BodyParts;

  final HTTPUploadDialog dialog;
  
  public FileUpload(HTTPUploadDialog dialog, HttpServletRequest request)
  {
    this.request = request;
    this.name2BodyParts = new HashMap();
    this.dialog=dialog;
    this.dialog.setExpectedFileSize(request.getContentLength());
  }

  // FREEGROUP: Bessere Möglichkeit den Stream zu behandeln finden.
  //            Die letzten 2 Zeichen im Stream dürfen nicht beachtet werden. 
  //            Wenn mann verhindern kann, dass diese überhaupt in den Stream kommen, dann
  //            spart man sich einen ganzen Kopiervorgang (je nach datei sind dass 50MB und mehr)
  protected byte[] setByteArray(ByteArrayOutputStream baos) 
  {
    byte[] temp;
    byte[] ret = null;
    if(baos != null) 
    {
      temp = baos.toByteArray();
      // \r\n am Ende des streams wird nicht berücksichtigt -> -2
      ret = new byte[temp.length - 2];
      System.arraycopy(temp,0,ret,0,temp.length - 2);
      temp = null;
    }
    return ret;
  }

  public String getErrString() 
  {
    return err;
  }

  public HashMap getMimeParts() 
  {
    return name2BodyParts;
  }

  public boolean upload()  
  {
    String contentType = request.getContentType();
    String boundary = "";
    final int BOUNDARY_WORD_SIZE = "boundary=".length();

    if(contentType == null || !contentType.startsWith("multipart/form-data")) {
      err = "Ilegal ENCTYPE : must be multipart/form-data\n";
      err += "ENCTYPE set = " + contentType;
    }
    else 
    {
      boundary = contentType.substring(contentType.indexOf("boundary=") + BOUNDARY_WORD_SIZE);
      boundary = "--" + boundary; 
      parseBody(boundary);
    }
    return (err.equals(""));
  }

  protected void setupHashMap(MimeBodyPart m) 
  {
    if(m != null) 
    {
      String nome = m.getName();
      Object c;
      if((c=name2BodyParts.get(nome)) == null) 
      { 
        name2BodyParts.put(nome,m);
      }
      else 
      {
        if(c instanceof Vector) 
        {
          Vector temp = (Vector)c;
          temp.add(m);
          name2BodyParts.put(nome,temp);
        }
        else 
        {
          Vector temp = new Vector();
          temp.add(c); 
          temp.add(m);
          name2BodyParts.put(nome,temp);
        }
      }
    }
  }

  
  public void parseBody(String boundary)
  {
    int bytesRead = 0;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    try
    {
      ServletInputStream sis = request.getInputStream();
      byte[] b = new byte[1024];
      int x = 0;
      int state = 0;
      String name = null, fileName = null, contentType = null;

      while ((x = sis.readLine(b, 0, 1024)) > -1)
      {
        bytesRead += x;
        bytesRead = writeStats(bytesRead);

        //buffer.write(b,0,x);
        String s = new String(b, 0, x);
        if (s.startsWith(boundary))
        {
          state = 0;
          if (name != null)
          {
            byte[] data = setByteArray(buffer);
            MimeBodyPart part = new MimeBodyPart(name, data, fileName, contentType);
            setupHashMap(part);
            buffer = new ByteArrayOutputStream();
            name = null;
            contentType = null;
            fileName = null;
            data = null;
            part = null;
          }
        }
        else if (s.startsWith("Content-Disposition") && state == 0)
        {
          state = 1;
          if (s.indexOf("filename=") == -1)
          {
            name = s.substring(s.indexOf("name=") + "name=".length(),  s.length() - 2);  //- 2 do CR/LF
          }
          else
          {
            name = s.substring(s.indexOf("name=") + "name=".length(),s.lastIndexOf(";"));
            fileName =s.substring(s.indexOf("filename=") + "filename=".length(), s.length() - 2);//-2 CR/LF "C:\boot.ini"

            if (fileName.equals("\"\""))
            {
              fileName = null;
            }
            else
            {
              String userAgent = request.getHeader("User-Agent");
              String userSeparator = "\\"; // default
              if (userAgent.indexOf("Windows") != -1)
              {
                userSeparator = "\\";
              }
              if (userAgent.indexOf("Linux") != -1)
              {
                userSeparator = "/";
              }
              fileName =fileName.substring(fileName.lastIndexOf(userSeparator) + 1,fileName.length() - 1);
              if (fileName.startsWith("\""))
              {
                fileName = fileName.substring(1);
              }
            }
          }
          name = name.substring(1, name.length() - 1);
        }
        else if (s.startsWith("Content-Type") && state == 1)
        {
          state = 2;
          contentType = s.substring(s.indexOf(":") + 2, s.length() - 2);
        }
        else if (s.equals("\r\n") && state != 3)
        {
          state = 3;
        }
        else
        {
          buffer.write(b, 0, x);
        }
      }
      sis.close();
      buffer.close();

    }
    catch (IOException e)
    {
      err = e.toString();
      logger.error("Parsing body failed", e);
    }
  }

  public int writeStats(int bytesRead) throws IOException
  {
    if(dialog!=null)
      dialog.setCurrentFileSize(bytesRead);
    return bytesRead;
  }
}