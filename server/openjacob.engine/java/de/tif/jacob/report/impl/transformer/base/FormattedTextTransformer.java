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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
 * Plain text transformer.
 * 
 */
public final class FormattedTextTransformer extends BaseTransformer
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: FormattedTextTransformer.java,v 1.7 2009/12/22 15:34:18 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.7 $";
  
  public static final String TEXT_FORMATTED_MIME_TYPE = "text/formatted";

  public void transform(OutputStream out, ILayoutReport report, CastorLayout layout, IReportDataIterator reportData, Locale locale) throws Exception
  {
    RootPartFactory factory = getRootPartFactory(report, layout);
    Part rootPart = factory.createRootPart(reportData);
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, ENCODING));
    rootPart.print(new TransformerPrinter(pw, locale), locale);
    pw.flush();
    pw.close();
  }
  
  private static final class TransformerPrinter extends BaseTransformerPrinter
  {
    private final PrintWriter pw;

    private TransformerPrinter(PrintWriter pw, Locale locale)
    {
      super(locale);

      this.pw = pw;
    }
    
    public void printPrologue(RootPart part) throws Exception
    {
      printCaption(part, part.factory.layout.getPrologue());

      // TODO: page header nur ausgeben, wenn kein prologue vorhanden?
      if (part.factory.layout.getPrologue() == null)
        printCaption(part, part.factory.layout.getPageHeader());

      // column labels
      //
      {
        printSpaces(part.getFactory().getRecordPartFactory().getCumulatedIdent());

        for (int j = 0; j < part.factory.columns.getColumnCount(); j++)
        {
          CastorLayoutColumn layoutColumn = part.factory.columns.getColumn(j);

          String label = StringUtil.toSaveString(layoutColumn.getLabel());

          printSpaces(layoutColumn.hasIdent() ? layoutColumn.getIdent() : (j == 0 ? 0 : 1));

          int columnWidth = layoutColumn.hasWidth() ? layoutColumn.getWidth() : getAutoLayoutColumnWidth(part, layoutColumn);
          if (label.length() > columnWidth)
          {
            pw.print(label.substring(0, columnWidth));
          }
          else
          {
            pw.print(label);
            printSpaces(columnWidth - label.length());
          }
        }
        pw.println();
      }

      // column labels dashes
      //
      {
        printSpaces(part.getFactory().getRecordPartFactory().getCumulatedIdent());

        for (int j = 0; j < part.factory.columns.getColumnCount(); j++)
        {
          CastorLayoutColumn layoutColumn = part.factory.columns.getColumn(j);

          printSpaces(layoutColumn.hasIdent() ? layoutColumn.getIdent() : (j == 0 ? 0 : 1));

          int columnWidth = layoutColumn.hasWidth() ? layoutColumn.getWidth() : getAutoLayoutColumnWidth(part, layoutColumn);
          printCharacters(columnWidth, '-');
        }
        pw.println();
      }
    }

    public void printEpilogue(RootPart part) throws Exception
    {
      // TODO: page footer nur ausgeben, wenn kein epilogue vorhanden?
      if (part.factory.layout.getEpilogue() == null)
        printCaption(part, part.factory.layout.getPageFooter());

      printCaption(part, part.factory.layout.getEpilogue());
    }

    public void printPrologue(GroupPart part) throws Exception
    {
      printCaption(part, part.factory.castor.getHeader());
    }

    public void printEpilogue(GroupPart part) throws Exception
    {
      printCaption(part, part.factory.castor.getFooter());
    }

    public void print(RecordPart part) throws Exception
    {
      for (int i = 0; i < part.records.size(); i++)
      {
        IReportDataRecord record = (IReportDataRecord) part.records.get(i);

        printCaption(part, part.factory.recordHeader);

        printSpaces(part.getFactory().getCumulatedIdent());

        for (int j = 0; j < part.factory.castor.getColumnCount(); j++)
        {
          CastorLayoutColumn layoutColumn = part.factory.castor.getColumn(j);

          printSpaces(layoutColumn.hasIdent() ? layoutColumn.getIdent() : (j == 0 ? 0 : 1));

          int columnWidth = layoutColumn.hasWidth() ? layoutColumn.getWidth() : getAutoLayoutColumnWidth(part, layoutColumn);
          if (i > 0 && part.factory.isGroupColumn(j))
          {
            // print value only for the first row for group columns
            printSpaces(columnWidth);
            continue;
          }

          String value = truncateValue(getSaveStringValue(record, layoutColumn), layoutColumn, columnWidth);
          if (value.length() == columnWidth)
          {
            pw.print(value);
          }
          else
          {
            CastorLayoutColumnJustificationType justification = layoutColumn.getJustification();
            if (justification == null)
              justification = CastorLayoutColumnJustificationType.LEFT;
            if (justification == CastorLayoutColumnJustificationType.LEFT)
            {
              pw.print(value);
              printSpaces(columnWidth - value.length());
            }
            else if (justification == CastorLayoutColumnJustificationType.RIGHT)
            {
              printSpaces(columnWidth - value.length());
              pw.print(value);
            }
            else if (justification == CastorLayoutColumnJustificationType.CENTER)
            {
              int spaces = columnWidth - value.length();
              int spacesRight = spaces / 2;
              printSpaces(spaces - spacesRight);
              pw.print(value);
              printSpaces(spacesRight);
            }
          }
        }
        pw.println();

        printCaption(part, part.factory.recordFooter);
      }
    }

    private void printSpaces(int count) throws Exception
    {
      printCharacters(count, ' ');
    }

    private void printCharacters(int count, char character) throws Exception
    {
      for (int i = 0; i < count; i++)
      {
        pw.print(character);
      }
    }
    
    /**
     * 
     * 
     * @author Andreas Sonntag
     */
    private final class MyRTEPrinter extends RTEPrinter
    {
      private abstract class PrintCommand
      {
        protected abstract void print() throws Exception;
      }

      private final class LinePrintCommand extends PrintCommand
      {
        private StringBuffer leftText = null;
        private StringBuffer centerText = null;
        private StringBuffer rightText = null;
        
        private void appendLeft(String text)
        {
          if (this.leftText == null)
            this.leftText = new StringBuffer();
          this.leftText.append(text);
        }

        private void appendCenter(String text)
        {
          if (this.centerText == null)
            this.centerText = new StringBuffer();
          this.centerText.append(text);
        }

        private void appendRight(String text)
        {
          if (this.rightText == null)
            this.rightText = new StringBuffer();
          this.rightText.append(text);
        }

        protected void print() throws Exception
        {
          printLine( //
            this.leftText == null ? "" : this.leftText.toString(), //
            this.centerText == null ? "" : this.centerText.toString(), //
            this.rightText == null ? "" : this.rightText.toString());
        }
      }

      private final List printCommands = new ArrayList();
      private LinePrintCommand currentLinePrintCommand = null;
      private final Part part;
      
      private MyRTEPrinter(Part part)
      {
        this.part = part;
      }
      
      protected void appendLeft(String text)
      {
        if (this.currentLinePrintCommand == null)
          printCommands.add(this.currentLinePrintCommand = new LinePrintCommand());
        
        this.currentLinePrintCommand.appendLeft(text);
      }

      protected void appendCenter(String text)
      {
        if (this.currentLinePrintCommand == null)
          printCommands.add(this.currentLinePrintCommand = new LinePrintCommand());
        
        this.currentLinePrintCommand.appendCenter(text);
      }

      protected void appendRight(String text)
      {
        if (this.currentLinePrintCommand == null)
          printCommands.add(this.currentLinePrintCommand = new LinePrintCommand());
        
        this.currentLinePrintCommand.appendRight(text);
      }

      public void printLinefeed()
      {
        super.printLinefeed();
        printCommands.add(this.currentLinePrintCommand = new LinePrintCommand());
      }

      public void printPagebreak()
      {
        // make 2 line feeds instead
        printLinefeed();
        printLinefeed();
      }

      public void printPageNumber()
      {
        print("#");
      }
      
      private void printLine(String leftText, String centerText, String rightText) throws Exception
      {
        printSpaces(part.getFactory().getCumulatedIdent());

        int width = part.getFactory().getWidth();
        if (width > 0)
        {
          pw.print(leftText);
          int pos = leftText.length();

          if (centerText.length() + rightText.length() > 0)
          {
            int centerPos = width / 2 - centerText.length() / 2;
            int spaces = Math.max(1, centerPos - pos);
            printSpaces(spaces);
            pw.print(centerText);
            pos += spaces + centerText.length();
          }

          if (rightText.length() > 0)
          {
            int rightPos = width - rightText.length();
            int spaces = Math.max(1, rightPos - pos);
            printSpaces(spaces);
            pw.print(rightText);
          }
        }
        else
        {
          pw.print(leftText);

          if (centerText.length() + rightText.length() > 0)
          {
            printSpaces(3);
            pw.print(centerText);
          }

          if (rightText.length() > 0)
          {
            printSpaces(3);
            pw.print(rightText);
          }
        }
        pw.println();
      }
      
      private void print() throws Exception
      {
        for (int i = 0; i < this.printCommands.size(); i++)
          ((PrintCommand) this.printCommands.get(i)).print();
      }
    }
    
    private void printCaption(Part part, CastorCaption caption) throws Exception
    {
      if (caption != null)
      {
        MyRTEPrinter rtePrinter = new MyRTEPrinter(part);
        part.printText(rtePrinter, caption.getText(), getLocale());
        rtePrinter.print();
      }
    }
  }
}
