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

package de.tif.jacob.report.impl.transformer.base;

import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import de.tif.jacob.report.impl.ILayoutReport;
import de.tif.jacob.report.impl.castor.CastorCaption;
import de.tif.jacob.report.impl.castor.CastorLayout;
import de.tif.jacob.report.impl.castor.CastorLayoutColumn;
import de.tif.jacob.report.impl.castor.types.CastorLayoutColumnJustificationType;
import de.tif.jacob.report.impl.transformer.IReportDataIterator;
import de.tif.jacob.report.impl.transformer.IReportDataRecord;
import de.tif.jacob.report.impl.transformer.base.BaseTransformer.GroupPartFactory.GroupPart;
import de.tif.jacob.report.impl.transformer.base.BaseTransformer.RecordPartFactory.RecordPart;
import de.tif.jacob.report.impl.transformer.base.BaseTransformer.RootPartFactory.RootPart;
import de.tif.jacob.report.impl.transformer.base.rte.RTEPrinter;
import de.tif.jacob.util.StringUtil;

/**
 * Beispieloutput mit einem angegebenen RelationSet
 * 
 * <pre>
 * &lt;faplisbuildingpart&gt;
 *   &lt;pkey&gt;4&lt;/pkey&gt;
 *   &lt;name&gt;L28c&lt;/name&gt;
 *   &lt;faplisstatus&gt;valid&lt;/faplisstatus&gt;
 *   &lt;building_key&gt;3&lt;/building_key&gt;
 *   &lt;shortname&gt;L28c&lt;/shortname&gt;
 *   &lt;faplisbuilding&gt;
 *     &lt;pkey&gt;3&lt;/pkey&gt;
 *     &lt;name&gt;L28 Louis London&lt;/name&gt;
 *     &lt;faplisstatus&gt;valid&lt;/faplisstatus&gt;
 *     &lt;sitepart_key&gt;2&lt;/sitepart_key&gt;
 *     &lt;shortname&gt;L28&lt;/shortname&gt;
 *     &lt;faplissitepart&gt;
 *       &lt;pkey&gt;2&lt;/pkey&gt;
 *       &lt;name&gt;LKW Planung&lt;/name&gt;
 *       &lt;faplisstatus&gt;valid&lt;/faplisstatus&gt;
 *       &lt;site_key&gt;2&lt;/site_key&gt;
 *       &lt;shortname&gt;LKW&lt;/shortname&gt;
 *       &lt;faplissite&gt;
 *         &lt;pkey&gt;2&lt;/pkey&gt;
 *         &lt;name&gt;059&lt;/name&gt;
 *         &lt;faplisstatus&gt;valid&lt;/faplisstatus&gt;
 *         &lt;shortname&gt;059&lt;/shortname&gt;
 *       &lt;/faplissite&gt;
 *     &lt;/faplissitepart&gt;
 *   &lt;/faplisbuilding&gt;
 * &lt;/faplisbuildingpart&gt;
 * </pre>
 * 
 * It uses JAXP 1.1 so it will work under JDK 1.4 or JDK 1.2/1.3 with XALAN2
 * library (or any XML library JAXP 1.1 compliant). It's also memory-friendly
 * because it doesn't need DOM. Think about Servlet Engines (Tomcat, ..) and
 * Application servers (Websphere, Weblogic ...). Most of them already include
 * an XML parser/processor and try to add a new one could generate seal errors.
 * 
 * To avoid this problems we use JAXP 1.1 + SAX to implement a portable XML
 * generation.
 * 
 */
public final class XMLTransformer extends BaseTransformer
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: XMLTransformer.java,v 1.7 2009/12/22 15:34:21 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.7 $";

  private static final String REPORT_ELEMENT = "report";
  private static final String PROLOGUE_ELEMENT = "prologue";
  private static final String EPILOGUE_ELEMENT = "epilogue";
  private static final String PAGE_ELEMENT = "page";
  private static final String LABELS_ELEMENT = "labels";
  private static final String LABEL_ELEMENT = "label";
  private static final String GROUP_ELEMENT = "group";
  private static final String GROUPS_ELEMENT = "groups";
  private static final String HEADER_ELEMENT = "header";
  private static final String FOOTER_ELEMENT = "footer";
  private static final String BLOCK_ELEMENT = "block";
  private static final String LEFT_ELEMENT = "left";
  private static final String CENTER_ELEMENT = "center";
  private static final String RIGHT_ELEMENT = "right";
  private static final String RECORD_ELEMENT = "record";
  private static final String RECORDS_ELEMENT = "records";
  private static final String VALUE_ELEMENT = "value";
  private static final String PAGE_NUMBER_ELEMENT = "page-number";

  private static final String APPLICATION_ATTRIBUTE = "application";
  private static final String APPLICATION_VERSION_ATTRIBUTE = "applicationVersion";
  private static final String APPLICATION_TITLE_ATTRIBUTE = "applicationTitle";
  private static final String IDENT_ATTRIBUTE = "ident";
  private static final String WIDTH_ATTRIBUTE = "width";
  private static final String JUSTIFICATION_ATTRIBUTE = "justification";
  private static final String NEW_PAGE_ATTRIBUTE = "newPage";

  private static final String CDATA = "CDATA";

  public void transform(OutputStream out, ILayoutReport report, CastorLayout layout, IReportDataIterator reportData, Locale locale) throws Exception
  {
    toXml(new StreamResult(out), report, layout, reportData, locale);
  }

  public static void appendXml(Writer writer, ILayoutReport report, CastorLayout layout, IReportDataIterator reportData, Locale locale) throws Exception
  {
    toXml(new StreamResult(writer), report, layout, reportData, locale);
  }

  private static void toXml(StreamResult streamResult, ILayoutReport report, CastorLayout layout, IReportDataIterator reportData, Locale locale) throws Exception
  {
    // init XML
    SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

    TransformerHandler hd = tf.newTransformerHandler();
    Transformer serializer = hd.getTransformer();
    serializer.setOutputProperty(OutputKeys.ENCODING, ENCODING);
    serializer.setOutputProperty(OutputKeys.INDENT, "no");

    serializer.setOutputProperty(OutputKeys.METHOD, "xml");
    serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

    hd.setResult(streamResult);
    hd.startDocument();

    RootPartFactory factory = getRootPartFactory(report, layout);
    Part rootPart = factory.createRootPart(reportData);
    rootPart.print(new TransformerPrinter(hd, locale), locale);

    hd.endDocument();
  }

  private static final class TransformerPrinter extends BaseTransformerPrinter
  {
    private static final Attributes NO_ATTRIBUTES = new Attributes()
    {
      public int getIndex(String uri, String localName)
      {
        return -1;
      }

      public int getIndex(String name)
      {
        return -1;
      }

      public int getLength()
      {
        return 0;
      }

      public String getLocalName(int index)
      {
        return null;
      }

      public String getQName(int index)
      {
        return null;
      }

      public String getType(int index)
      {
        return null;
      }

      public String getType(String uri, String localName)
      {
        return null;
      }

      public String getType(String name)
      {
        return null;
      }

      public String getURI(int index)
      {
        return null;
      }

      public String getValue(int index)
      {
        return null;
      }

      public String getValue(String uri, String localName)
      {
        return null;
      }

      public String getValue(String name)
      {
        return null;
      }
    };

    private final TransformerHandler hd;

    private TransformerPrinter(TransformerHandler hd, Locale locale)
    {
      super(locale);

      this.hd = hd;
    }

    public void printPrologue(RootPart part) throws Exception
    {
      {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "", APPLICATION_ATTRIBUTE, CDATA, part.appl.getName());
        attrs.addAttribute("", "", APPLICATION_VERSION_ATTRIBUTE, CDATA, part.appl.getVersion().toShortString());
        if (part.appl.getTitle() != null)
          attrs.addAttribute("", "", APPLICATION_TITLE_ATTRIBUTE, CDATA, part.appl.getTitle());
        hd.startElement("", "", REPORT_ELEMENT, attrs);
      }

      {
        hd.startElement("", "", PAGE_ELEMENT, NO_ATTRIBUTES);
        printCaption(hd, part, HEADER_ELEMENT, part.factory.layout.getPageHeader());
        printCaption(hd, part, FOOTER_ELEMENT, part.factory.layout.getPageFooter());
        hd.endElement("", "", PAGE_ELEMENT);
      }

      printCaption(hd, part, PROLOGUE_ELEMENT, part.factory.layout.getPrologue());

      // column labels
      //
      {
        hd.startElement("", "", LABELS_ELEMENT, NO_ATTRIBUTES);
        for (int j = 0; j < part.factory.columns.getColumnCount(); j++)
        {
          CastorLayoutColumn layoutColumn = part.factory.columns.getColumn(j);

          AttributesImpl attrs = new AttributesImpl();

          int columnWidth = layoutColumn.hasWidth() ? layoutColumn.getWidth() : getAutoLayoutColumnWidth(part, layoutColumn);
          attrs.addAttribute("", "", WIDTH_ATTRIBUTE, CDATA, Integer.toString(columnWidth));

          CastorLayoutColumnJustificationType justification = layoutColumn.getJustification();
          if (justification != null && justification != CastorLayoutColumnJustificationType.LEFT)
            attrs.addAttribute("", "", JUSTIFICATION_ATTRIBUTE, CDATA, justification.toString());

          int ident = part.getFactory().getIdent();
          if (ident != 0)
            attrs.addAttribute("", "", IDENT_ATTRIBUTE, CDATA, Integer.toString(ident));

          String label = StringUtil.toSaveString(layoutColumn.getLabel());
          if (label.length() > columnWidth)
            label = label.substring(0, columnWidth);
          hd.startElement("", "", LABEL_ELEMENT, attrs);
          hd.characters(label.toCharArray(), 0, label.length());
          hd.endElement("", "", LABEL_ELEMENT);
        }
        hd.endElement("", "", LABELS_ELEMENT);
      }

      if (part.factory.child instanceof GroupPartFactory)
        hd.startElement("", "", GROUPS_ELEMENT, NO_ATTRIBUTES);
    }

    public void printEpilogue(RootPart part) throws Exception
    {
      if (part.factory.child instanceof GroupPartFactory)
        hd.endElement("", "", GROUPS_ELEMENT);

      printCaption(hd, part, EPILOGUE_ELEMENT, part.factory.layout.getEpilogue());
      hd.endElement("", "", REPORT_ELEMENT);
    }

    public void printPrologue(GroupPart part) throws Exception
    {
      AttributesImpl attrs = new AttributesImpl();

      int ident = part.getFactory().getIdent();
      if (ident != 0)
        attrs.addAttribute("", "", IDENT_ATTRIBUTE, CDATA, Integer.toString(ident));
      
      hd.startElement("", "", GROUP_ELEMENT, attrs);
      printCaption(hd, part, HEADER_ELEMENT, part.factory.castor.getHeader());

      if (part.factory.child instanceof GroupPartFactory)
        hd.startElement("", "", GROUPS_ELEMENT, NO_ATTRIBUTES);
    }

    public void printEpilogue(GroupPart part) throws Exception
    {
      if (part.factory.child instanceof GroupPartFactory)
        hd.endElement("", "", GROUPS_ELEMENT);

      printCaption(hd, part, FOOTER_ELEMENT, part.factory.castor.getFooter());
      hd.endElement("", "", GROUP_ELEMENT);
    }

    public void print(RecordPart part) throws Exception
    {
      {
        AttributesImpl attrs = new AttributesImpl();

        int ident = part.getFactory().getIdent();
        if (ident != 0)
          attrs.addAttribute("", "", IDENT_ATTRIBUTE, CDATA, Integer.toString(ident));

        hd.startElement("", "", RECORDS_ELEMENT, attrs);
      }
      
      for (int i = 0; i < part.records.size(); i++)
      {
        IReportDataRecord record = (IReportDataRecord) part.records.get(i);

        hd.startElement("", "", RECORD_ELEMENT, NO_ATTRIBUTES);

        printCaption(hd, part, HEADER_ELEMENT, part.getRootPart().factory.layout.getRecordHeader());
        printCaption(hd, part, FOOTER_ELEMENT, part.getRootPart().factory.layout.getRecordFooter());

        for (int j = 0; j < part.factory.castor.getColumnCount(); j++)
        {
          CastorLayoutColumn layoutColumn = part.factory.castor.getColumn(j);

          AttributesImpl attrs = new AttributesImpl();

          // handle justification; left is default -> omit attribute
          CastorLayoutColumnJustificationType justification = layoutColumn.getJustification();
          if (justification != null && justification != CastorLayoutColumnJustificationType.LEFT)
            attrs.addAttribute("", "", JUSTIFICATION_ATTRIBUTE, CDATA, justification.toString());

          hd.startElement("", "", VALUE_ELEMENT, attrs);
          if (i > 0 && part.factory.isGroupColumn(j))
          {
            // print value only for the first row for group columns
          }
          else
          {
            int columnWidth = layoutColumn.hasWidth() ? layoutColumn.getWidth() : getAutoLayoutColumnWidth(part, layoutColumn);
            String value = truncateValue(getSaveStringValue(record, layoutColumn), layoutColumn, columnWidth);
            if (value.length() > 0)
              hd.characters(value.toCharArray(), 0, value.length());
          }
          hd.endElement("", "", VALUE_ELEMENT);
        }
        hd.endElement("", "", RECORD_ELEMENT);
      }
      hd.endElement("", "", RECORDS_ELEMENT);
    }

    private static final String PAGE_NUMBER_FLAG = new String("<page_nbr>");

    /**
     * Report text expression printer implementation
     * 
     * @author Andreas Sonntag
     */
    private final class MyRTEPrinter extends RTEPrinter
    {
      /**
       * Abstract print command
       * 
       * @author Andreas Sonntag
       */
      private abstract class PrintCommand
      {
        protected abstract void print() throws Exception;
      }

      /**
       * Print command to print a xsl-fo page number.
       * 
       * @author Andreas Sonntag
       */
      private final class PageNumberPrintCommand extends PrintCommand
      {
        protected void print() throws Exception
        {
          hd.startElement("", "", PAGE_NUMBER_ELEMENT, NO_ATTRIBUTES);
          hd.endElement("", "", PAGE_NUMBER_ELEMENT);
        }
      }

      /**
       * Print command to just print text.
       * 
       * @author Andreas Sonntag
       */
      private final class TextPrintCommand extends PrintCommand
      {
        private StringBuffer textBuffer = new StringBuffer();

        protected void print() throws Exception
        {
          String text = this.textBuffer.toString();
          hd.characters(text.toCharArray(), 0, text.length());
        }

        private void append(String text)
        {
          this.textBuffer.append(text);
        }
      }

      /**
       * Print command to print a part/column of block (header, footer, etc.)
       * 
       * @author Andreas Sonntag
       */
      private final class BlockColumnPrintCommand extends PrintCommand
      {
        private final List printCommands = new ArrayList();
        private TextPrintCommand currentTextPrintCommand = null;
        private boolean doPrint = false;

        private void append(String text, boolean doPrint)
        {
          if (doPrint)
            this.doPrint = true;
          
          if (text == PAGE_NUMBER_FLAG)
          {
            printCommands.add(new PageNumberPrintCommand());
            this.currentTextPrintCommand = null;
          }
          else
          {
            if (this.currentTextPrintCommand == null)
              printCommands.add(this.currentTextPrintCommand = new TextPrintCommand());

            this.currentTextPrintCommand.append(text);
          }
        }
        
        private boolean doPrint()
        {
          return this.doPrint;
        }

        protected void print() throws Exception
        {
          for (int i = 0; i < this.printCommands.size(); i++)
            ((PrintCommand) this.printCommands.get(i)).print();
        }
      }

      /**
       * Print command to print a text block (header, footer, etc.)
       * 
       * @author Andreas Sonntag
       */
      private final class BlockPrintCommand extends PrintCommand
      {
        private final boolean newpage;
        private BlockColumnPrintCommand leftText = null;
        private BlockColumnPrintCommand centerText = null;
        private BlockColumnPrintCommand rightText = null;

        private BlockPrintCommand()
        {
          this(false);
        }

        private BlockPrintCommand(boolean newpage)
        {
          this.newpage = newpage;
        }

        private void appendLeft(String text, boolean doPrint)
        {
          if (this.leftText == null)
            this.leftText = new BlockColumnPrintCommand();
          this.leftText.append(text, doPrint);
        }

        private void appendCenter(String text, boolean doPrint)
        {
          if (this.centerText == null)
            this.centerText = new BlockColumnPrintCommand();
          this.centerText.append(text, doPrint);
        }

        private void appendRight(String text, boolean doPrint)
        {
          if (this.rightText == null)
            this.rightText = new BlockColumnPrintCommand();
          this.rightText.append(text, doPrint);
        }

        protected void print() throws Exception
        {
          AttributesImpl attrs = new AttributesImpl();
          if (this.newpage)
            attrs.addAttribute("", "", NEW_PAGE_ATTRIBUTE, CDATA, Boolean.TRUE.toString());
          hd.startElement("", "", BLOCK_ELEMENT, attrs);

          if (this.leftText != null && this.leftText.doPrint())
          {
            hd.startElement("", "", LEFT_ELEMENT, NO_ATTRIBUTES);
            this.leftText.print();
            hd.endElement("", "", LEFT_ELEMENT);
          }

          if (this.centerText != null && this.centerText.doPrint())
          {
            hd.startElement("", "", CENTER_ELEMENT, NO_ATTRIBUTES);
            this.centerText.print();
            hd.endElement("", "", CENTER_ELEMENT);
          }

          if (this.rightText != null && this.rightText.doPrint())
          {
            hd.startElement("", "", RIGHT_ELEMENT, NO_ATTRIBUTES);
            this.rightText.print();
            hd.endElement("", "", RIGHT_ELEMENT);
          }

          hd.endElement("", "", BLOCK_ELEMENT);
        }
      }

      private final List printCommands = new ArrayList();
      private BlockPrintCommand currentBlockPrintCommand = null;
      private final Part part;

      private MyRTEPrinter(Part part)
      {
        this.part = part;
      }

      protected void appendLeft(String text)
      {
        if (this.currentBlockPrintCommand == null)
          printCommands.add(this.currentBlockPrintCommand = new BlockPrintCommand());

        this.currentBlockPrintCommand.appendLeft(text, true);
      }

      protected void appendCenter(String text)
      {
        if (this.currentBlockPrintCommand == null)
          printCommands.add(this.currentBlockPrintCommand = new BlockPrintCommand());

        this.currentBlockPrintCommand.appendCenter(text, true);
      }

      protected void appendRight(String text)
      {
        if (this.currentBlockPrintCommand == null)
          printCommands.add(this.currentBlockPrintCommand = new BlockPrintCommand());

        this.currentBlockPrintCommand.appendRight(text, true);
      }

      public void printLinefeed()
      {
        super.printLinefeed();
        if (this.currentBlockPrintCommand == null)
          printCommands.add(this.currentBlockPrintCommand = new BlockPrintCommand());

        this.currentBlockPrintCommand.appendLeft("\n", isLeftAligned());
        this.currentBlockPrintCommand.appendCenter("\n", isCentered());
        this.currentBlockPrintCommand.appendRight("\n", isRightAligned());
      }

      public void printPagebreak()
      {
        // start a new block
        printCommands.add(this.currentBlockPrintCommand = new BlockPrintCommand(true));
      }

      public void printPageNumber()
      {
        print(PAGE_NUMBER_FLAG);
      }

      private void print() throws Exception
      {
        for (int i = 0; i < this.printCommands.size(); i++)
          ((PrintCommand) this.printCommands.get(i)).print();
      }
    }

    private AttributesImpl printCaption(TransformerHandler hd, Part part, String element, CastorCaption caption) throws Exception
    {
      if (caption != null && !StringUtil.emptyOrNull(caption.getText()))
      {
        AttributesImpl atts = new AttributesImpl();
        hd.startElement("", "", element, atts);

        MyRTEPrinter rtePrinter = new MyRTEPrinter(part);
        part.printText(rtePrinter, caption.getText(), getLocale());
        rtePrinter.print();

        hd.endElement("", "", element);
        return atts;
      }
      return null;
    }
  }
}
