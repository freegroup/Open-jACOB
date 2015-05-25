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

package de.tif.qes.report;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.qes.report.element.QWRDefinition;

/**
 * @author Andreas
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QWRReportDefinitionLoader
{
  static public final transient String RCS_ID = "$Id: QWRReportDefinitionLoader.java,v 1.2 2009-12-07 03:36:09 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  static private final transient Log logger = LogFactory.getLog(QWRReportDefinitionLoader.class);

  public QWRDefinition load(Reader reader) throws Exception
  {
    try
    {
      QWRParser parser = new QWRParser(new QWRScanner(reader));
      QWRDefinition qwrDefinition = (QWRDefinition) parser.parse().value;
      return qwrDefinition;
    }
    finally
    {
      reader.close();
    }
  }

  public static Reader getQWRReader(File file) throws IOException
  {
    System.out.println("Reading file: " + file.getCanonicalPath() + " ..");
    System.out.flush();
    
    URL url = file.toURL();
    if (logger.isInfoEnabled())
      logger.info("Reading QWR (ISO-8859-1): " + url);
    return new InputStreamReader(url.openStream(), "ISO-8859-1");
  }

  public static Reader getQWRReader(byte[] data) throws IOException
  {
    return new InputStreamReader(new ByteArrayInputStream(data), "ISO-8859-1");
  }

  public static void main(String[] args)
  {
    try
    {
      if (args.length > 0)
      {
        QWRReportDefinitionLoader qwrLoader = new QWRReportDefinitionLoader();
        for (int i = 0; i < args.length; i++)
        {
          String qwrFileName = args[i];

          File file = new File(qwrFileName);
          if (file.isDirectory())
          {
            System.out.println("Processing directory: " + file.getCanonicalPath() + " ..");
            System.out.flush();
            
            File[] files = file.listFiles();
            for (int j=0; j<files.length;j++)
            {
              File child = files[j];
              if (child.getName().endsWith(".qwr"))
              {
                QWRDefinition qwrDefinition = qwrLoader.load(getQWRReader(child));
                qwrDefinition.toCastor("asmlq", "0.3", "andreas", true);
              }
            }
          }
          else
          {
            QWRDefinition qwrDefinition = qwrLoader.load(getQWRReader(file));
            qwrDefinition.toCastor("asmlq", "0.3", "andreas", true);
          }
        }

        System.out.println("Successfully finished!");
        System.exit(0);
      }
      else
      {
        System.err.println("Usage: QWRReportDefinitionLoader.main qwrfile");
        System.exit(1);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.err.println("Converting failed: " + ex.toString());
      System.exit(1);
    }
  }
}