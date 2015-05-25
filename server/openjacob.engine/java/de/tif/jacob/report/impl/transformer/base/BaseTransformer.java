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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.report.impl.ILayoutReport;
import de.tif.jacob.report.impl.castor.CastorCaption;
import de.tif.jacob.report.impl.castor.CastorGroup;
import de.tif.jacob.report.impl.castor.CastorLayout;
import de.tif.jacob.report.impl.castor.CastorLayoutColumn;
import de.tif.jacob.report.impl.castor.CastorLayoutColumns;
import de.tif.jacob.report.impl.castor.CastorLayoutPart;
import de.tif.jacob.report.impl.transformer.IReportDataIterator;
import de.tif.jacob.report.impl.transformer.IReportDataRecord;
import de.tif.jacob.report.impl.transformer.base.BaseTransformer.GroupPartFactory.GroupPart;
import de.tif.jacob.report.impl.transformer.base.BaseTransformer.RecordPartFactory.RecordPart;
import de.tif.jacob.report.impl.transformer.base.BaseTransformer.RootPartFactory.RootPart;
import de.tif.jacob.report.impl.transformer.base.rte.IRTEFunctionResolver;
import de.tif.jacob.report.impl.transformer.base.rte.IRTEPrinter;
import de.tif.jacob.report.impl.transformer.base.rte.RTEParser;
import de.tif.jacob.report.impl.transformer.base.util.AliasFieldNameSplitter;
import de.tif.jacob.util.DatetimeUtil;
import de.tif.jacob.util.ObjectUtil;

/**
 * Base transformer class.
 * 
 */
public abstract class BaseTransformer extends de.tif.jacob.report.impl.transformer.Transformer
{
  /**
   * Character encoding used for all base transformers.
   */
  public final static String ENCODING = "ISO-8859-1";

  public interface ITransformerPrinter
  {
    public void printPrologue(RootPart part) throws Exception;

    public void printEpilogue(RootPart part) throws Exception;

    public void printPrologue(GroupPart part) throws Exception;

    public void printEpilogue(GroupPart part) throws Exception;

    public void print(RecordPart part) throws Exception;
  }

  /**
   * Abstract part factory.
   * 
   * @author Andreas Sonntag
   */
  protected static abstract class PartFactory
  {
    protected ParentPartFactory parent;
    
    private PartFactory()
    {
    }
    
    public abstract RecordPartFactory getRecordPartFactory();
    
    protected abstract void init(CastorLayout layout);

    protected abstract Part add(IReportDataRecord record);

    protected abstract void createNewPart();
    
    /**
     * Gets the width of the parts created by this factory.
     * 
     * @return The character width or <code>-1</code> if not determined
     */
    public abstract int getWidth();
    
    /**
     * Gets the ident of the parts created by this factory.
     *  
     * @return the ident in characters 
     */
    public abstract int getIdent();
    
    /**
     * Gets the cumulated ident of the parts created by this factory.
     *  
     * @return the cumulated ident in characters 
     */
    public abstract int getCumulatedIdent();
  }

  /**
   * Abstract parent part factory.
   * 
   * @author Andreas Sonntag
   */
  protected static abstract class ParentPartFactory extends PartFactory
  {
    protected final PartFactory child;

    protected ParentPartFactory(PartFactory child)
    {
      this.child = child;
      child.parent = this;
    }
    
    public final RecordPartFactory getRecordPartFactory()
    {
      return child.getRecordPartFactory();
    }

    protected void init(CastorLayout layout)
    {
      this.child.init(layout);
    }
  }
  
  protected static RootPartFactory getRootPartFactory(ILayoutReport report, CastorLayout layout)
  {
    RootPartFactory factory = new RootPartFactory(report, layout);
    factory.init(layout);
    return factory;
  }

  /**
   * Root part factory.
   * 
   * @author Andreas Sonntag
   */
  protected static final class RootPartFactory extends ParentPartFactory
  {
    protected static final class RootPart extends ParentPart
    {
      protected final RootPartFactory factory;
      protected final IApplicationDefinition appl;

      private RootPart(RootPartFactory factory, IApplicationDefinition appl)
      {
        this.factory = factory;
        this.appl = appl;
      }

      public RootPart getRootPart()
      {
        return this;
      }

      public void print(ITransformerPrinter printer, Locale locale) throws Exception
      {
        printer.printPrologue(this);
        super.print(printer, locale);
        printer.printEpilogue(this);
      }

      protected PartFactory getFactory()
      {
        return this.factory;
      }
    }

    protected final ILayoutReport report;
    protected final CastorLayout layout;
    protected final CastorLayoutColumns columns;
    private RootPart part;

    private RootPartFactory(ILayoutReport report, CastorLayout layout)
    {
      super(create(layout.getPart()));
      this.report = report;
      this.layout = layout;
      this.columns = getColumns(layout.getPart());
    }
    
    private static CastorLayoutColumns getColumns(CastorLayoutPart part)
    {
      CastorLayoutColumns columns = part.getColumns();
      if (columns != null)
        return columns;
      return getColumns(part.getGroup().getPart());
    }

    private static PartFactory create(CastorLayoutPart part)
    {
      CastorGroup group = part.getGroup();
      if (group != null)
      {
        return new GroupPartFactory(create(group.getPart()), part);
      }

      return new RecordPartFactory(part);
    }

    protected Part add(IReportDataRecord record)
    {
      Part newChildPart = this.child.add(record);
      if (newChildPart != null)
        this.part.add(newChildPart);

      return null;
    }

    protected void createNewPart()
    {
      throw new IllegalStateException();
    }

    public Part createRootPart(IReportDataIterator reportData)
    {
      this.part = new RootPart(this, reportData.getApplication());
      while (reportData.hasNext())
      {
        add(reportData.next());
      }
      return this.part;
    }

    public int getWidth()
    {
      if (this.layout.hasWidth())
        return this.layout.getWidth();
      return -1;
    }

    public int getIdent()
    {
      return 0;
    }

    public int getCumulatedIdent()
    {
      return 0;
    }
  }

  /**
   * Record part factory.
   * 
   * @author Andreas Sonntag
   */
  protected static final class RecordPartFactory extends PartFactory
  {
    protected static final class RecordPart extends Part
    {
      protected final RecordPartFactory factory;
      protected final List records = new ArrayList();

      private RecordPart(RecordPartFactory factory)
      {
        this.factory = factory;
      }

      protected PartFactory getFactory()
      {
        return this.factory;
      }

      protected void add(IReportDataRecord record)
      {
        this.records.add(record);
      }

      public void processAllRecords(IReportDataRecordProcessor processor, Locale locale) throws Exception
      {
        for (int i = 0; i < this.records.size(); i++)
          processor.process((IReportDataRecord) this.records.get(i), locale);
      }

      public boolean processOneRecord(IReportDataRecordProcessor processor, Locale locale) throws Exception
      {
        if (this.records.size() > 0)
        {
          processor.process((IReportDataRecord) this.records.get(0), locale);
          return true;
        }
        return false;
      }

      public void print(ITransformerPrinter printer, Locale locale) throws Exception
      {
        printer.print(this);
      }
    }

    protected final CastorLayoutColumns castor;
    protected CastorCaption recordHeader;
    protected CastorCaption recordFooter;
    private final CastorLayoutPart castorLayoutPart;
    private boolean[] isGroupColumn;
    private RecordPart part;

    private RecordPartFactory(CastorLayoutPart castorLayoutPart)
    {
      this.castor = castorLayoutPart.getColumns();
      this.castorLayoutPart = castorLayoutPart;
    }

    public RecordPartFactory getRecordPartFactory()
    {
      return this;
    }

    protected void init(CastorLayout layout)
    {
      this.recordHeader=layout.getRecordHeader();
      this.recordFooter=layout.getRecordFooter();
      this.isGroupColumn = new boolean[this.castor.getColumnCount()];
      for (int i = 0; i < this.isGroupColumn.length; i++)
      {
        this.isGroupColumn[i] = isGroupColumn(layout.getPart(), this.castor.getColumn(i));
      }
    }
    
    private static boolean isGroupColumn(CastorLayoutPart part, CastorLayoutColumn column)
    {
      CastorGroup group = part.getGroup();
      if (group != null)
      {
        if (group.getTableAlias().equals(column.getTableAlias()) && group.getField().equals(column.getField()))
          return true;
        return isGroupColumn(group.getPart(), column);
      }
      return false;
    }
    
    public boolean isGroupColumn(int columnIndex)
    {
      return this.isGroupColumn[columnIndex];
    }

    protected Part add(IReportDataRecord record)
    {
      Part result = null;
      if (this.part == null)
      {
        this.part = new RecordPart(this);
        result = this.part;
      }
      this.part.add(record);
      return result;
    }

    protected void createNewPart()
    {
      this.part = null;
    }

    public int getIdent()
    {
      if (this.castorLayoutPart.hasIdent())
        return this.castorLayoutPart.getIdent();
      return 0;
    }

    public int getCumulatedIdent()
    {
      if (this.castorLayoutPart.hasIdent())
        return this.castorLayoutPart.getIdent() + this.parent.getCumulatedIdent();
      return this.parent.getCumulatedIdent();
    }

    public int getWidth()
    {
      int parentWidth = this.parent.getWidth();
      if (parentWidth < 0)
        return parentWidth;
      return parentWidth - this.castorLayoutPart.getIdent();
    }
  }

  /**
   * Group part factory.
   * 
   * @author Andreas Sonntag
   */
  protected static final class GroupPartFactory extends ParentPartFactory
  {
    protected static final class GroupPart extends ParentPart
    {
      protected final GroupPartFactory factory;
      protected final Object groupValue;

      private GroupPart(GroupPartFactory factory, Object groupValue)
      {
        this.factory = factory;
        this.groupValue = groupValue;
      }

      protected PartFactory getFactory()
      {
        return this.factory;
      }

      public void print(ITransformerPrinter printer, Locale locale) throws Exception
      {
        printer.printPrologue(this);
        super.print(printer, locale);
        printer.printEpilogue(this);
      }
    }

    protected final CastorGroup castor;
    private final CastorLayoutPart castorLayoutPart;
    private GroupPart part;

    private GroupPartFactory(PartFactory child, CastorLayoutPart castorLayoutPart)
    {
      super(child);
      this.castor = castorLayoutPart.getGroup();
      this.castorLayoutPart = castorLayoutPart;
    }

    protected Part add(IReportDataRecord record)
    {
      Part result = null;
      Object groupValue = record.getValue(this.castor.getTableAlias(), this.castor.getField());
      if (this.part == null || !ObjectUtil.equals(this.part.groupValue, groupValue))
      {
        this.child.createNewPart();
        this.part = new GroupPart(this, groupValue);
        result = this.part;
      }

      Part newChildPart = this.child.add(record);
      if (newChildPart != null)
        this.part.add(newChildPart);

      return result;
    }

    protected void createNewPart()
    {
      this.part = null;
    }

    public int getIdent()
    {
      return this.castorLayoutPart.getIdent();
    }

    public int getCumulatedIdent()
    {
      return this.castorLayoutPart.getIdent() + this.parent.getCumulatedIdent();
    }

    public int getWidth()
    {
      int parentWidth = this.parent.getWidth();
      if (parentWidth < 0)
        return parentWidth;
      return parentWidth - this.castorLayoutPart.getIdent();
    }
  }

  /**
   * Abstract part.
   * 
   * @author Andreas Sonntag
   */
  protected static abstract class Part
  {
    private final class Resolver implements IRTEFunctionResolver
    {
      private final Locale locale;
      private final String reportName;
      private final String dateString;
      private final String timeString;
      
      private Resolver(String reportName, Locale locale)
      {
        this.locale = locale;
        this.reportName = reportName;
        long millis = System.currentTimeMillis();
        this.dateString = DatetimeUtil.convertDateToString(new Date(millis), locale);
        this.timeString = DatetimeUtil.convertTimeToString(new Time(millis), locale);
      }
      
      public String count() throws Exception
      {
        RecordCountProcessor processor = new RecordCountProcessor();
        processAllRecords(processor, this.locale);
        return Integer.toString(processor.getCount());
      }

      public String count(String field) throws Exception
      {
        AliasFieldNameSplitter splitter = new AliasFieldNameSplitter(field);
        RecordFieldCountProcessor processor = new RecordFieldCountProcessor(splitter.aliasName, splitter.fieldName);
        processAllRecords(processor, this.locale);
        return Integer.toString(processor.getCount());
      }

      public String value(String field) throws Exception
      {
        AliasFieldNameSplitter splitter = new AliasFieldNameSplitter(field);
        RecordFieldValueProcessor processor = new RecordFieldValueProcessor(splitter.aliasName, splitter.fieldName);
        processOneRecord(processor, this.locale);
        return processor.getValue();
      }

      public String average(String field) throws Exception
      {
        AliasFieldNameSplitter splitter = new AliasFieldNameSplitter(field);
        RecordFieldCalculationProcessor processor = new RecordFieldCalculationProcessor(splitter.aliasName, splitter.fieldName);
        processAllRecords(processor, this.locale);
        return processor.getAverage();
      }

      public String sum(String field) throws Exception
      {
        AliasFieldNameSplitter splitter = new AliasFieldNameSplitter(field);
        RecordFieldCalculationProcessor processor = new RecordFieldCalculationProcessor(splitter.aliasName, splitter.fieldName);
        processAllRecords(processor, this.locale);
        return processor.getSum();
      }

      public String max(String field) throws Exception
      {
        AliasFieldNameSplitter splitter = new AliasFieldNameSplitter(field);
        RecordFieldCalculationProcessor processor = new RecordFieldCalculationProcessor(splitter.aliasName, splitter.fieldName);
        processAllRecords(processor, this.locale);
        return processor.getMax();
      }

      public String min(String field) throws Exception
      {
        AliasFieldNameSplitter splitter = new AliasFieldNameSplitter(field);
        RecordFieldCalculationProcessor processor = new RecordFieldCalculationProcessor(splitter.aliasName, splitter.fieldName);
        processAllRecords(processor, this.locale);
        return processor.getMin();
      }

      public String tab()
      {
        // use default tab size of 3
        return tab("3");
      }

      public String tab(String num)
      {
        try
        {
          int n = Integer.parseInt(num);
          // avoid that someone causes a OutOfMemoryException just because of a
          // very big number
          if (n >= 1 && n < 100)
          {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < n; i++)
              buffer.append(' ');
            return buffer.toString();
          }
        }
        catch (NumberFormatException ex)
        {
        }
        
        // error case
        return "~tab("+num+")";
      }

      public String name()
      {
        return this.reportName;
      }

      public String date()
      {
        return this.dateString;
      }

      public String time()
      {
        return this.timeString;
      }

      public String ignore()
      {
        return "";
      }

      public String tilde()
      {
        return "~";
      }
    }
    
    private ParentPart parent;
    
    private Part()
    {
    }
    
    public RootPart getRootPart()
    {
      return this.parent.getRootPart();
    }

    public abstract void print(ITransformerPrinter printer, Locale locale) throws Exception;
    
    public abstract void processAllRecords(IReportDataRecordProcessor processor, Locale locale) throws Exception;
    
    public abstract boolean processOneRecord(IReportDataRecordProcessor processor, Locale locale) throws Exception;
    
    protected abstract PartFactory getFactory();
    
    public final void printText(IRTEPrinter printer, String text, Locale locale) throws Exception
    {
      RTEParser.printTextExpression(printer, new Resolver(getRootPart().factory.report.getName(), locale), text);
    }
  }

  /**
   * Abstract parent part.
   * 
   * @author Andreas Sonntag
   */
  protected static abstract class ParentPart extends Part
  {
    private final List children = new ArrayList();

    private ParentPart()
    {
    }

    protected void add(Part child)
    {
      child.parent = this;
      this.children.add(child);
    }

    public final void processAllRecords(IReportDataRecordProcessor processor, Locale locale) throws Exception
    {
      for (int i = 0; i < this.children.size(); i++)
      {
        Part child = (Part) this.children.get(i);
        child.processAllRecords(processor, locale);
      }
    }

    public final boolean processOneRecord(IReportDataRecordProcessor processor, Locale locale) throws Exception
    {
      for (int i = 0; i < this.children.size(); i++)
      {
        Part child = (Part) this.children.get(i);
        if (child.processOneRecord(processor, locale))
          return true;
      }
      return false;
    }

    public void print(ITransformerPrinter printer, Locale locale) throws Exception
    {
      for (int i = 0; i < this.children.size(); i++)
      {
        Part child = (Part) this.children.get(i);
        child.print(printer, locale);
      }
    }
  }
  
  /**
   * Abstract record processor.
   * 
   * @author Andreas Sonntag
   */
  private static abstract class RecordProcessor implements IReportDataRecordProcessor
  {
  }
  
  private static final class RecordCountProcessor extends RecordProcessor
  {
    private int count = 0;

    private RecordCountProcessor()
    {
    }

    public void process(IReportDataRecord record, Locale locale)
    {
      this.count++;
    }
    
    public int getCount()
    {
      return this.count;
    }
  }
  
  private static final class RecordFieldCountProcessor extends RecordProcessor
  {
    private final String aliasName, fieldName;
    private int count = 0;

    private RecordFieldCountProcessor(String aliasName, String fieldName)
    {
      this.aliasName = aliasName;
      this.fieldName = fieldName;
    }

    public void process(IReportDataRecord record, Locale locale)
    {
      if (record.getValue(aliasName, fieldName) != null)
        this.count++;
    }
    
    public int getCount()
    {
      return this.count;
    }
  }
  
  private static final class RecordFieldCalculationProcessor extends RecordProcessor
  {
    private final String aliasName, fieldName;
    private Number min = null;
    private double minDouble = Double.MAX_VALUE;
    private Number max = null;
    private double maxDouble = Double.MIN_VALUE;
    private double sum = 0d;
    private BigDecimal sumDecimal = null;
    private int count = 0;

    private RecordFieldCalculationProcessor(String aliasName, String fieldName)
    {
      this.aliasName = aliasName;
      this.fieldName = fieldName;
    }

    public void process(IReportDataRecord record, Locale locale)
    {
      Object value = record.getValue(aliasName, fieldName);
      if (value instanceof Number)
      {
        Number number = (Number) value;
        double doubleNumber = number.doubleValue();

        if (doubleNumber <= this.minDouble)
        {
          this.minDouble = doubleNumber;
          this.min = number;
        }

        if (doubleNumber >= this.maxDouble)
        {
          this.maxDouble = doubleNumber;
          this.max = number;
        }
        
        if (number instanceof BigDecimal)
        {
          BigDecimal decimal = (BigDecimal) number;
          
          if (this.sumDecimal == null)
            this.sumDecimal = decimal;
          else
            this.sumDecimal = this.sumDecimal.add(decimal);
        }

        this.sum += doubleNumber;
        this.count++;
      }
    }
    
    public String getSum()
    {
      if (this.count == 0)
        return NULL;
      if (this.sumDecimal != null)
        return this.sumDecimal.toString();
      if (this.min instanceof Float || this.min instanceof Double)
        return Double.toString(this.sum);
      return Long.toString(new Double(this.sum).longValue());
    }

    public String getAverage()
    {
      if (this.count == 0)
        return NULL;
      if (this.sumDecimal != null)
        return this.sumDecimal.divide(new BigDecimal(Integer.toString(this.count)), this.sumDecimal.scale(), BigDecimal.ROUND_HALF_UP).toString();
      if (this.min instanceof Float || this.min instanceof Double)
        return Double.toString(this.sum / this.count);
      return Long.toString(Math.round(this.sum / this.count));
    }
    
    public String getMin()
    {
      return this.min == null ? NULL : this.min.toString();
    }
    
    public String getMax()
    {
      return this.max == null ? NULL : this.max.toString();
    }
  }
  
  private static final String NULL = "<null>";
  
  private static final class RecordFieldValueProcessor extends RecordProcessor
  {
    private final String aliasName, fieldName;
    private String value = "<no value>";

    private RecordFieldValueProcessor(String aliasName, String fieldName)
    {
      this.aliasName = aliasName;
      this.fieldName = fieldName;
    }

    public void process(IReportDataRecord record, Locale locale)
    {
      this.value = record.getStringValue(aliasName, fieldName, locale);
    }
    
    public String getValue()
    {
      if (this.value == null)
        return NULL;
      return this.value;
    }
  }
}
