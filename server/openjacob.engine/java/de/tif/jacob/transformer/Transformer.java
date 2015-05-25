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
package de.tif.jacob.transformer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;


/**
 * Util class to handle the transformation of documents corresponding
 * to the hands over mime type
 *
 */
public class Transformer
{
  static public final transient String RCS_ID = "$Id: Transformer.java,v 1.3 2009/07/21 13:26:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
//  static private Logger log = null;
  static private final transient Log logger = LogFactory.getLog(Transformer.class);
  
  private static final class MyErrorListener implements ErrorListener
  {
    private final URL stylesheet;

    private MyErrorListener(URL stylesheet)
    {
      this.stylesheet = stylesheet;
    }

    public void error(TransformerException exception) throws TransformerException
    {
      logger.error(getLocationString(exception) + exception.toString());
    }

    public void fatalError(TransformerException exception) throws TransformerException
    {
      logger.fatal(getLocationString(exception) + exception.toString());
      throw exception;
    }

    public void warning(TransformerException exception) throws TransformerException
    {
      logger.warn(getLocationString(exception) + exception.toString());
    }
    
    private String getLocationString(TransformerException exception)
    {
      StringBuffer sbuffer = new StringBuffer();
      
      sbuffer.append("Stylesheet '");
      sbuffer.append(this.stylesheet.toString());
      sbuffer.append("': ");
      
      SourceLocator locator = exception.getLocator();
      if (null != locator)
      {
        int line = locator.getLineNumber();
        int column = locator.getColumnNumber();

        if (0 != line)
        {
          sbuffer.append(" line ");
          sbuffer.append(line);
        }

        if (0 != column)
        {
          sbuffer.append(" column ");
          sbuffer.append(column);
        }
      }

      return sbuffer.toString();
    }
  }
    

    /**
     * Transform the input xmlData with the stylesheet to the hands over mimetype
     * and return the document as byte[].
     */
    public static byte[] render(String xmlData, URL stylesheet, String mimetype) throws Exception
    {
        InputStream xslt = stylesheet.openStream();
        try
        {
            System.setProperty("javax.xml.transformer.TransformerFactory","org.apache.xalan.processor.TransformerFactoryImpl");
            System.setProperty("jaxp.debug","false");
            String format=null;

            if(mimetype.equals("application/vnd.hp-PCL"))
               format = MimeConstants.MIME_PCL_ALT;
            else if(mimetype.equals("text/formated"))
               format = MimeConstants.MIME_PLAIN_TEXT;
            else if(mimetype.equals("application/postscript"))
              format = MimeConstants.MIME_POSTSCRIPT;
            else if(mimetype.equals("application/pdf"))
              format = MimeConstants.MIME_PDF;

            if(format !=null)
            {
               ByteArrayOutputStream out = new ByteArrayOutputStream();
  
               // configure fopFactory as desired
               FopFactory fopFactory = FopFactory.newInstance();
               FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

               // Construct fop with desired output format
               Fop fop = fopFactory.newFop(format, foUserAgent, out);

               // Resulting SAX events (the generated FO) must be piped through to FOP
               Result res = new SAXResult(fop.getDefaultHandler());

               // Setup input for XSLT transformation
               Source src = new StreamSource(new StringReader(xmlData));

               // Setup XSLT
               TransformerFactory factory = TransformerFactory.newInstance();
               factory.setErrorListener(new MyErrorListener(stylesheet));
               javax.xml.transform.Transformer transformer = factory.newTransformer(new StreamSource(xslt));
               transformer.setErrorListener(new MyErrorListener(stylesheet));

               // Start XSLT transformation and FOP processing
               transformer.transform(src, res);
               
               return out.toByteArray();
            }
            else if(mimetype.equals("text/plain") || mimetype.equals("text/html"))
            {
               ByteArrayOutputStream out = new ByteArrayOutputStream();
               TransformerFactory factory = TransformerFactory.newInstance();
               factory.setErrorListener(new MyErrorListener(stylesheet));
               javax.xml.transform.Transformer transformer = factory.newTransformer(new StreamSource(xslt));
               transformer.setErrorListener(new MyErrorListener(stylesheet));
               transformer.transform(new StreamSource(new StringReader(xmlData)), new StreamResult(out));
               return out.toByteArray();
            }
            else
            {
               throw new java.lang.UnsupportedOperationException("unsupported mimetype ["+mimetype+"] to render document");
            }
        }
        finally
        {
            try{xslt.close();}catch(Exception exc){};
        }
    }
}