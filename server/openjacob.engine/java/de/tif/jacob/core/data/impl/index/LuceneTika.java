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

package de.tif.jacob.core.data.impl.index;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import de.tif.jacob.core.data.DataDocumentValue;

/**
 * Helper class to parse documents by means of TIKA and add content to Lucene.
 * 
 * @author Andreas Sonntag
 * @since 2.10
 */
public final class LuceneTika
{
  static public transient final String RCS_ID = "$Id: LuceneTika.java,v 1.5 2010/09/25 20:58:03 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";

  private static final transient Log logger = Lucene.logger;

  private static final Parser PARSER = new AutoDetectParser();

  /**
   * Adds a jACOB document value to Lucene by means of extracting the contents
   * by TIKA.
   * 
   * @param luceneDocument
   *          the Lucene document
   * @param luceneFieldName
   *          the Lucene field name to add content to
   * @param docvalue
   *          the document to parse and add to the given Lucene document
   * @param boost
   *          the boost factor to use (default should be <code>1.0f</code>)
   * @return the detected mime type or <code>null</code>, if not detected
   * @throws TikaException
   *           in case the given document can not be handled by TIKA
   * @throws SAXException
   *           in case the given document can not be parsed by TIKA
   */
  public static String addToLuceneDocument(Document luceneDocument, String luceneFieldName, DataDocumentValue docvalue, float boost) throws TikaException, SAXException
  {
    // construct meta data
    //
    Metadata metadata = new Metadata();
    String name = docvalue.getName();
    metadata.set(Metadata.RESOURCE_NAME_KEY, name);
    byte[] content = docvalue.getContent();
    metadata.set(Metadata.CONTENT_LENGTH, Integer.toString(content.length));

    // add file name to index
    {
      Field docfield = new Field(luceneFieldName, name, Field.Store.NO, Field.Index.ANALYZED);
      docfield.setBoost(boost);
      luceneDocument.add(docfield);
    }

    // add content to index
    //
    boolean added = false;
    try
    {
      // if (content.length < 4096)
      {
        ContentHandler handler = new BodyContentHandler();
        try
        {
          PARSER.parse(new ByteArrayInputStream(content), handler, metadata);
        }
        catch (NoClassDefFoundError err)
        {
          // nobody knows which third party libraries Tika needs ;-)
          if (logger.isWarnEnabled())
            logger.warn("Document '" + name + "' NOT added to index: " + err.toString());
          return null;
        }
        String bodyContent = handler.toString();
        if (bodyContent != null && bodyContent.length() > 0)
        {
          Field docfield = new Field(luceneFieldName, bodyContent, Field.Store.NO, Field.Index.ANALYZED);
          docfield.setBoost(boost);
          luceneDocument.add(docfield);
          added = true;
        }
      }
      // else
      // {
      // try
      // {
      // Reader reader = new ParsingReader(PARSER, new
      // ByteArrayInputStream(content), metadata);
      // try
      // {
      // luceneDocument.add(new Field(luceneFieldName, reader));
      // }
      // finally
      // {
      // reader.close();
      // }
      // }
      // catch (IOException ex)
      // {
      // Throwable cause = ex.getCause();
      // if (cause != null)
      // {
      // if (cause instanceof TikaException)
      // throw (TikaException) cause;
      // if (cause instanceof SAXException)
      // throw (SAXException) cause;
      // }
      // throw ex;
      // }
      // }
    }
    catch (IOException ex)
    {
      // should never occur
      throw new RuntimeException(ex);
    }

    String mimetype = metadata.get(Metadata.CONTENT_TYPE);
    String encoding = metadata.get(Metadata.CONTENT_ENCODING);

    if (logger.isInfoEnabled())
    {
      StringBuffer info = new StringBuffer();
      info.append("Document '").append(name).append("'");
      if (!added)
        info.append(" NOT");
      info.append(" added to index");
      if (mimetype != null || encoding != null)
      {
        info.append(" (");
        if (mimetype != null)
          info.append("detected type '").append(mimetype).append("'");
        if (encoding != null)
        {
          if (mimetype != null)
            info.append(", ");
          info.append("detected encoding '").append(encoding).append("'");
        }
        info.append(")");
        logger.info(info.toString());
      }
    }

    return MediaType.OCTET_STREAM.equals(mimetype) ? null : mimetype;
  }
  
  private static String truncate(String str)
  {
    final int TRUNC_LENGTH = 80;
    if (str.length() <= TRUNC_LENGTH)
      return str;
    return str.substring(0, TRUNC_LENGTH) + "..";
  }

  private static final String ENCODING_TO_USE = "UTF-8";
  
  /**
   * Adds a String value to Lucene by means of extracting/parsing the contents
   * by TIKA.
   * 
   * @param luceneDocument
   *          the Lucene document
   * @param luceneFieldName
   *          the Lucene field name to add content to
   * @param data
   *          the data to parse by means of the given content type before adding
   *          to the Lucene document
   * @param contentType
   *          the content type to parse (e.g. "text/html")
   * @param boost
   *          the boost factor to use (default should be <code>1.0f</code>)
   * @return <code>true</code> if the data has been successfully parsed and
   *         added to the index
   * @throws TikaException
   *           in case the given document can not be handled by TIKA
   * @throws SAXException
   *           in case the given document can not be parsed by TIKA
   */
  public static boolean addToLuceneDocument(Document luceneDocument, String luceneFieldName, String data, String contentType, float boost) throws TikaException, SAXException
  {
    // construct meta data
    //
    Metadata metadata = new Metadata();
    metadata.set(Metadata.CONTENT_LENGTH, Integer.toString(data.length()));
    metadata.set(Metadata.CONTENT_ENCODING, ENCODING_TO_USE);
    if (contentType != null)
      metadata.set(Metadata.CONTENT_TYPE, contentType);

    // add content to index
    //
    boolean added = false;
    try
    {
      // if (content.length < 4096)
      {
        ContentHandler handler = new BodyContentHandler();
        try
        {
          PARSER.parse(new ByteArrayInputStream(data.getBytes(ENCODING_TO_USE)), handler, metadata);
        }
        catch (NoClassDefFoundError err)
        {
          // nobody knows which third party libraries Tika needs ;-)
          if (logger.isWarnEnabled())
            logger.warn("Data '" + truncate(data) + "' of type '" + contentType + "' NOT added to index: " + err.toString());
          return false;
        }
        String bodyContent = handler.toString();
        if (bodyContent != null && bodyContent.length() > 0)
        {
          Field docfield = new Field(luceneFieldName, bodyContent, Field.Store.NO, Field.Index.ANALYZED);
          docfield.setBoost(boost);
          luceneDocument.add(docfield);
          added = true;
        }
      }
    }
    catch (IOException ex)
    {
      // should never occur
      throw new RuntimeException(ex);
    }

    String mimetype = metadata.get(Metadata.CONTENT_TYPE);

    return added && contentType.equals(mimetype);
  }

  public static void main(String[] args)
  {
    try
    {
      for (int i = 0; i < args.length; i++)
      {
        if (i != 0)
          System.out.println("#################################");
        System.out.println("Parsing: " + args[i] + " ..");
        InputStream stream = new FileInputStream(args[i]);
        try
        {
          Metadata metadata = new Metadata();
          metadata.set(Metadata.CONTENT_ENCODING, ENCODING_TO_USE);
          metadata.set(Metadata.CONTENT_TYPE, "text/html");
          ContentHandler handler = new BodyContentHandler();
          PARSER.parse(stream, handler, metadata);

          System.out.println(metadata.toString());
          System.out.println();
          System.out.println(handler.toString());
        }
        finally
        {
          stream.close();
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public static void main0(String[] args)
  {
    try
    {
      for (int i = 0; i < args.length; i++)
      {
        if (i != 0)
          System.out.println("#################################");
        System.out.println("Parsing: " + args[i] + " ..");
        InputStream stream = new FileInputStream(args[i]);
        try
        {
          Metadata metadata = new Metadata();
          ContentHandler handler = new BodyContentHandler();
          PARSER.parse(stream, handler, metadata);

          System.out.println(metadata.toString());
          System.out.println();
          System.out.println(handler.toString());
        }
        finally
        {
          stream.close();
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
