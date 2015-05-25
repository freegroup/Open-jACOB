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
package de.tif.jacob.report.birt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderContext;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import de.tif.jacob.core.data.DataDocumentValue;
/**
 * Class to create PDF or HTML reports out of BIRT report definitions.
 * <p>
 * Note: Data sources within BIRT report definitions, which match to jACOB SQL
 * data sources, are adapted (i.e. changed) in such a way that the already given
 * jACOB data source configuration is used. By this way BIRT reports become
 * "environment/database" independant.
 * 
 * @since 2.6
 * @author Andreas Sonntag
 */
public class BirtReport
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: BirtReport.java,v 1.4 2008/11/29 18:41:00 freegroup Exp $";
  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.4 $";
  private final InputStream input;
  private final Map parameters;

  /**
   * Creates a BIRT report.
   * 
   * @param rptDefinition
   *          the BIRT report definition as data document value
   * @throws Exception
   *           on any problem
   */
  public BirtReport(DataDocumentValue rptDefinition) throws Exception
  {
    this(rptDefinition.getContent());
  }

  /**
   * Creates a BIRT report.
   * 
   * @param rptDefinition
   *          the BIRT report definition as byte array
   * @throws Exception
   *           on any problem
   */
  public BirtReport(byte[] rptDefinition) throws Exception
  {
    this(new ByteArrayInputStream(rptDefinition));
  }

  /**
   * Creates a BIRT report.<br>
   * BirtReport doesn't close the InputStream. This is the part of the caller.
   * 
   * @param rptDefinitionStream
   *          the BIRT report definition as input stream
   * @throws Exception
   *           on any problem
   */
  public BirtReport(InputStream rptDefinitionStream) throws Exception
  {
    this.input = rptDefinitionStream;
    this.parameters = new HashMap();
  }

  /**
   * Sets a BIRT report parameter to a given value.
   * 
   * @param name
   *          the name of the BIRT report parameter
   * @param value
   *          the parameter value
   */
  public void setParameterValue(String name, Object value)
  {
    this.parameters.put(name, value);
  }

  /**
   * Resets all BIRT report parameter values.
   * <p>
   * This method could be used to create multiple report output documents with
   * different parameter settings without creating multiple instances of this
   * class, which would lead to parse the BIRT report definition multiple times.
   */
  public void resetParameterValues()
  {
    this.parameters.clear();
  }

  /**
   * Creates a PDF document from the BIRT report.
   * 
   * @param documentName
   *          the name of the document
   * @return the PDF data document
   * @throws Exception
   *           on any problem
   */
  public DataDocumentValue createPDFDocument(String documentName) throws Exception
  {
    return DataDocumentValue.create(documentName, createPDF());
  }

  /**
   * Creates a <b>Excel 2003 XML</b> document from the BIRT report.
   * 
   * @param documentName
   *          the name of the document
   * @return the PDF data document
   * @throws Exception
   *           on any problem
   * @since 2.7.4
   */
  public DataDocumentValue createExcelDocument(String documentName) throws Exception
  {
    return DataDocumentValue.create(documentName, createExcel());
  }

  /**
   * Creates a HTML document from the BIRT report.
   * 
   * @param documentName
   *          the name of the document
   * @return the HTML data document
   * @throws Exception
   *           on any problem
   */
  public DataDocumentValue createHTMLDocument(String documentName) throws Exception
  {
    return DataDocumentValue.create(documentName, createHTML());
  }

  /**
   * Creates a PDF document as byte array from the BIRT report.
   * 
   * @return the byte array containing the PDF document
   * @throws Exception
   *           on any problem
   */
  public byte[] createPDF() throws Exception
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
    try
    {
      createPDF(out);
      return out.toByteArray();
    }
    finally
    {
      out.close();
    }
  }

  /**
   * Creates a <b>Excel 2003 XML</b> document as byte array from the BIRT
   * report.
   * 
   * @return the byte array containing the Excel document
   * @throws Exception
   *           on any problem
   * @since 2.7.4
   */
  public byte[] createExcel() throws Exception
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
    try
    {
      createExcel(out);
      return out.toByteArray();
    }
    finally
    {
      out.close();
    }
  }

  /**
   * Creates a HTML document as byte array from the BIRT report.
   * 
   * @return the byte array containing the HTML document
   * @throws Exception
   *           on any problem
   */
  public byte[] createHTML() throws Exception
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
    try
    {
      createHTML(out);
      return out.toByteArray();
    }
    finally
    {
      out.close();
    }
  }

  /**
   * Creates a PDF document from the BIRT report and writes it to the given
   * output stream.
   * 
   * @param out
   *          the output stream
   * @throws Exception
   *           on any problem
   */
  public void createPDF(OutputStream out) throws Exception
  {
    IRunAndRenderTask task = ReportManager.createRunAndRenderTask(input);
    PDFRenderContext renderContext = new PDFRenderContext();
    task.getAppContext().put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, renderContext);
    setParameters(task);
    RenderOptionBase options = new RenderOptionBase();
    options.setOutputStream(out);
    options.setOutputFormat(RenderOptionBase.OUTPUT_FORMAT_PDF);
    task.setRenderOption(options);
    task.run();
  }

  /**
   * Creates a <b>Excel 2003 XML</b> document from the BIRT report and writes
   * it to the given output stream.
   * 
   * @param out
   *          the output stream
   * @throws Exception
   *           on any problem
   * @since 2.7.4
   */
  public void createExcel(OutputStream out) throws Exception
  {
    IRunAndRenderTask task = ReportManager.createRunAndRenderTask(input);
    setParameters(task);
    IRenderOption options = new RenderOption();
    options.setOutputFormat("xls");
    options.setOutputStream(out);
    task.setRenderOption(options);
    task.run();
  }

  /**
   * Creates a HTML document from the BIRT report and writes it to the given
   * output stream.
   * 
   * @param out
   *          the output stream
   * @throws Exception
   *           on any problem
   */
  public void createHTML(OutputStream out) throws Exception
  {
    IRunAndRenderTask task = ReportManager.createRunAndRenderTask(input);
    HTMLRenderContext renderContext = new HTMLRenderContext();
    task.getAppContext().put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
    setParameters(task);
    RenderOptionBase options = new RenderOptionBase();
    options.setOutputStream(out);
    options.setOutputFormat(RenderOptionBase.OUTPUT_FORMAT_HTML);
    task.setRenderOption(options);
    task.run();
  }

  /**
   * Set all BIRT report parameters.
   */
  private void setParameters(IRunAndRenderTask task)
  {
    // set all BIRT report parameters
    //
    Iterator iter = this.parameters.entrySet().iterator();
    while (iter.hasNext())
    {
      Map.Entry entry = (Map.Entry) iter.next();
      task.setParameterValue((String) entry.getKey(), entry.getValue());
    }
  }
}
