/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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

package de.tif.jacob.report.impl.transformer;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.Service;
import de.tif.jacob.core.exception.UnavailableTransformerException;
import de.tif.jacob.report.impl.ILayoutReport;
import de.tif.jacob.report.impl.castor.CastorLayout;

/**
 * Base class of new report transformer.
 * 
 * @since 2.9
 * @author Andreas Sonntag
 */
public abstract class Transformer
{
  static public transient final String RCS_ID = "$Id: Transformer.java,v 1.2 2009/12/08 00:21:15 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";

  private final static transient Log logger = LogFactory.getLog(Transformer.class);

  private final static Class TRANSFORMER_ARGS[] = {};

  private static final Map mimetypeTotransformerMap = new HashMap();

  /**
   * Gets the transformer instance specified by mime type.
   * 
   * @param mimeType
   *          the mime type
   * @return the requested transformer instance
   * @throws UnavailableTransformerException
   *           the requested transformer is not known
   */
  public static Transformer get(String mimeType) throws UnavailableTransformerException
  {
    synchronized (mimetypeTotransformerMap)
    {
      Transformer transformer = (Transformer) mimetypeTotransformerMap.get(mimeType);
      if (null == transformer)
      {
        try
        {
          transformer = instantiate(mimeType);
        }
        catch (UnavailableTransformerException ex)
        {
          // just rethrow
          throw ex;
        }
        catch (InvocationTargetException ex)
        {
          throw new RuntimeException("Getting transformer for mime type '" + mimeType + "' failed!", ex.getCause());
        }
        catch (Exception ex)
        {
          throw new RuntimeException("Getting transformer for mime type '" + mimeType + "' failed!", ex);
        }
        mimetypeTotransformerMap.put(mimeType, transformer);
      }
      return transformer;
    }
  }

  private static Transformer instantiate(String mimeType) throws UnavailableTransformerException, Exception
  {
    logger.info("Loading transformer for mime type '" + mimeType + "' ..");

    Class clazz = getDatasourceClass(mimeType);
    logger.info("Instantiating transformer class " + clazz.getName() + " ..");
    Constructor constructor = clazz.getConstructor(TRANSFORMER_ARGS);
    return (Transformer) constructor.newInstance(new Object[] {});
  }

  private static Class getDatasourceClass(String mimeType) throws UnavailableTransformerException, Exception
  {
    try
    {
      Iterator iter = Service.providers(TransformerProvider.class);
      while (iter.hasNext())
      {
        TransformerProvider provider = (TransformerProvider) iter.next();
        Class clazz = provider.getTransformerByMimetype(mimeType);
        if (clazz != null)
          return clazz;
      }

      if (logger.isWarnEnabled())
        logger.warn("No provider for mime type '" + mimeType + "' found");
    }
    catch (NoClassDefFoundError e)
    {
      // ignore
    }

    throw new UnavailableTransformerException(mimeType);
  }

  protected Transformer()
  {
  }
  
  public abstract void transform(OutputStream out, ILayoutReport report, CastorLayout layout, IReportDataIterator reportData, Locale locale) throws Exception;
}
