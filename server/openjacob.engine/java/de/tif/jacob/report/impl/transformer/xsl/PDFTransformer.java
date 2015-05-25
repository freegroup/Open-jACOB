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

package de.tif.jacob.report.impl.transformer.xsl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.report.impl.ILayoutReport;
import de.tif.jacob.report.impl.castor.CastorLayout;
import de.tif.jacob.report.impl.transformer.IReportDataIterator;
import de.tif.jacob.report.impl.transformer.IReportDataRecord;
import de.tif.jacob.report.impl.transformer.base.XMLTransformer;

/**
 * Classic plain text report rendering transformer.
 * 
 * @author Andreas Sonntag
 */
public final class PDFTransformer extends de.tif.jacob.report.impl.transformer.Transformer
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: PDFTransformer.java,v 1.1 2009/12/22 03:36:32 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  public static final String PDF_MIME_TYPE = "application/pdf";

  private static final class TruncationIterator implements IReportDataIterator
  {
    private final IReportDataIterator embedded;
    private final int maxRecords;
    private int count = 0;

    private TruncationIterator(IReportDataIterator embedded, int maxRecords)
    {
      this.embedded = embedded;
      this.maxRecords = maxRecords;
    }

    public IApplicationDefinition getApplication()
    {
      return this.embedded.getApplication();
    }

    public boolean hasNext()
    {
      return this.count < this.maxRecords && this.embedded.hasNext();
    }

    public IReportDataRecord next()
    {
      this.count++;
      return this.embedded.next();
    }

  }

  public void transform(OutputStream out, ILayoutReport report, CastorLayout layout, IReportDataIterator reportData, Locale locale) throws Exception
  {
    ByteArrayOutputStream xmlDataOutput = new ByteArrayOutputStream(100000);

    // IBIS: for out-of-memory reasons -> limit to 1000 records
    (new XMLTransformer()).transform(xmlDataOutput, report, layout, new TruncationIterator(reportData, 1000), locale);

    // configure fopFactory as desired
    FopFactory fopFactory = FopFactory.newInstance();

    FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
    // configure foUserAgent as desired

    // Setup output
    // out = new java.io.BufferedOutputStream(out);

    // Construct fop with desired output format
    Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

    // Setup XSLT
    TransformerFactory factory = TransformerFactory.newInstance();

    InputStream xsltfile = getStylesheet();
    try
    {
      Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

      // Set the value of a <param> in the stylesheet
      transformer.setParameter("versionParam", "2.0");

      // Setup input for XSLT transformation
      Source src = new StreamSource(new ByteArrayInputStream(xmlDataOutput.toByteArray()));

      // Resulting SAX events (the generated FO) must be piped through to FOP
      Result res = new SAXResult(fop.getDefaultHandler());

      // Start XSLT transformation and FOP processing
      transformer.transform(src, res);
    }
    finally
    {
      xsltfile.close();
    }
  }

  private final InputStream getStylesheet() throws Exception
  {
    String fileName = "fo.xsl";
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    if (null == inputStream)
    {
      throw new Exception("Could not open file '" + fileName + "' as stream");
    }
    return inputStream;
  }

}
