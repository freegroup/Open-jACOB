/*******************************************************************************
 *    This file is part of jACOB
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

package de.tif.qes.report.element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.report.impl.castor.Application;
import de.tif.jacob.report.impl.castor.CastorGroup;
import de.tif.jacob.report.impl.castor.CastorLayout;
import de.tif.jacob.report.impl.castor.CastorLayoutColumn;
import de.tif.jacob.report.impl.castor.CastorLayoutColumns;
import de.tif.jacob.report.impl.castor.CastorLayoutPart;
import de.tif.jacob.report.impl.castor.Column;
import de.tif.jacob.report.impl.castor.Input;
import de.tif.jacob.report.impl.castor.Layouts;
import de.tif.jacob.report.impl.castor.Output;
import de.tif.jacob.report.impl.castor.ReportDefinition;
import de.tif.jacob.screen.IClientContext;
import de.tif.qes.adl.element.ADLDefinition;


/**
 * @author Andreas Sonntag
 */
public final class QWRDefinition //extends AbstractDefinition
{
  static public final transient String RCS_ID = "$Id: QWRDefinition.java,v 1.4 2009-12-17 01:43:34 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  /**
   * Sort fields with smaller sort number (higher priority) before fields with
   * higher sort number (lower priority).
   * 
   * @see QWRFieldSortorder#getSortNumber()
   */
  private static final Comparator SORT_COMPARATOR = new Comparator()
  {
    public int compare(Object arg0, Object arg1)
    {
      QWRField f1 = (QWRField) arg0;
      QWRField f2 = (QWRField) arg1;

      int res = f1.getSortNumberForComparator() - f2.getSortNumberForComparator();
      if (res == 0)
        res = f1.hashCode() - f2.hashCode();
      return res;
    }
  };

  /**
   * Sort fields with smaller group number (higher priority) before fields with
   * higher group number (lower priority).
   * 
   * @see QWRFieldGrouping#getGroupedNbr()
   */
  private static final Comparator GROUP_COMPARATOR = new Comparator()
  {
    public int compare(Object arg0, Object arg1)
    {
      QWRField f1 = (QWRField) arg0;
      QWRField f2 = (QWRField) arg1;

      int res = f1.getGroupedNumberForComparator() - f2.getGroupedNumberForComparator();
      if (res == 0)
        res = f1.hashCode() - f2.hashCode();
      return res;
    }
  };
  
  private String name;
  private String description;
  private String domainName;
  private String relationsetName;
  private String anchorTableName;
	private Integer width, height, spacing;
	private QWROutputFormat outputFormat;
  private QWRCaption prologue, epilogue;
  private QWRCaption pageHeader, pageFooter;
  private QWRCaption recordHeader, recordFooter;
  
  private final Set fields = new TreeSet(SORT_COMPARATOR);
  private final Set groupFields = new TreeSet(GROUP_COMPARATOR);
  private final List layoutFields = new ArrayList();
  
  private final List constraints = new ArrayList();

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * @return the domainName
   */
  public String getDomainName()
  {
    return domainName;
  }

  /**
   * @param domainName the domainName to set
   */
  public void setDomainName(String domainName)
  {
    this.domainName = domainName;
  }

  /**
   * @return the relationsetName
   */
  public String getRelationsetName()
  {
    return relationsetName;
  }

  /**
   * @param relationsetName the relationsetName to set
   */
  public void setRelationsetName(String relationsetName)
  {
    if (ADLDefinition.CQDEFAULT.equals(relationsetName))
      relationsetName = IRelationSet.DEFAULT_NAME;
    else if (ADLDefinition.CQLOCAL.equals(relationsetName))
      relationsetName = IRelationSet.LOCAL_NAME;

    this.relationsetName = relationsetName;
  }

  /**
   * @return the anchorTableName
   */
  public String getAnchorTableName()
  {
    return anchorTableName;
  }

  /**
   * @param anchorTableName the anchorTableName to set
   */
  public void setAnchorTableName(String anchorTableName)
  {
    this.anchorTableName = anchorTableName;
  }

  /**
   * @return the width
   */
  public Integer getWidth()
  {
    return width;
  }

  /**
   * @param width the width to set
   */
  public void setWidth(Integer width)
  {
    this.width = width;
  }

  /**
   * @return the height
   */
  public Integer getHeight()
  {
    return height;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(Integer height)
  {
    this.height = height;
  }

  /**
   * @return the spacing
   */
  public Integer getSpacing()
  {
    return spacing;
  }

  /**
   * @param spacing the spacing to set
   */
  public void setSpacing(Integer spacing)
  {
    this.spacing = spacing;
  }

  /**
   * @return the outputFormat
   */
  public QWROutputFormat getOutputFormat()
  {
    return outputFormat;
  }

  /**
   * @param outputFormat the outputFormat to set
   */
  public void setOutputFormat(QWROutputFormat outputFormat)
  {
    this.outputFormat = outputFormat;
  }

  /**
   * @return the prologue
   */
  public QWRCaption getPrologue()
  {
    return prologue;
  }

  /**
   * @param prologue the prologue to set
   */
  public void setPrologue(QWRCaption prologue)
  {
    if (this.prologue != null)
      throw new RuntimeException("Prologue has already been set");
    this.prologue = prologue;
  }

  /**
   * @return the epilogue
   */
  public QWRCaption getEpilogue()
  {
    return epilogue;
  }

  /**
   * @param epilogue the epilogue to set
   */
  public void setEpilogue(QWRCaption epilogue)
  {
    if (this.epilogue != null)
      throw new RuntimeException("Epilogue has already been set");
    this.epilogue = epilogue;
  }

  /**
   * @return the pageHeader
   */
  public QWRCaption getPageHeader()
  {
    return pageHeader;
  }

  /**
   * @param pageHeader the pageHeader to set
   */
  public void setPageHeader(QWRCaption pageHeader)
  {
    if (this.pageHeader != null)
      throw new RuntimeException("Page header has already been set");
    this.pageHeader = pageHeader;
  }

  /**
   * @return the pageFooter
   */
  public QWRCaption getPageFooter()
  {
    return pageFooter;
  }

  /**
   * @param pageFooter the pageFooter to set
   */
  public void setPageFooter(QWRCaption pageFooter)
  {
    if (this.pageFooter != null)
      throw new RuntimeException("Page footer has already been set");
    this.pageFooter = pageFooter;
  }

  /**
   * @return the recordHeader
   */
  public QWRCaption getRecordHeader()
  {
    return recordHeader;
  }

  /**
   * @param recordHeader the recordHeader to set
   */
  public void setRecordHeader(QWRCaption recordHeader)
  {
    if (this.recordHeader != null)
      throw new RuntimeException("Record header has already been set");
    this.recordHeader = recordHeader;
  }

  /**
   * @return the recordFooter
   */
  public QWRCaption getRecordFooter()
  {
    return recordFooter;
  }

  /**
   * @param recordFooter the recordFooter to set
   */
  public void setRecordFooter(QWRCaption recordFooter)
  {
    if (this.recordFooter != null)
      throw new RuntimeException("Record footer has already been set");
    this.recordFooter = recordFooter;
  }
  
  public void addField(QWRField field)
  {
    field.setIndex(this.fields.size());
    
    this.fields.add(field);
    if (field.isGrouped())
      this.groupFields.add(field);
    if (!field.isHidden())
      this.layoutFields.add(field);
  }
  
  public void addConstraint(QWRConstraint constraint)
  {
    if (constraint.isSkipped())
      return;
    
    this.constraints.add(constraint);
  }
  
  public ReportDefinition toCastor(IClientContext context, boolean _private) throws Exception
  {
    return toCastor(context.getApplicationDefinition().getName(), context.getApplicationDefinition().getVersion().toString(), //
      context.getUser().getLoginId(), _private);
  }
  
  public ReportDefinition toCastor(String applicationName, String applicationVersion, String ownerId, boolean _private) throws Exception
  {
    ReportDefinition castor = new ReportDefinition();
    castor.setName(this.name);
    castor.setDesciption(this.description);
    castor.setAnchorDomain(this.domainName);
    castor.setDefaultMimeType(this.outputFormat.getMimetype());
    
    Application application = new Application();
    castor.setApplication(application);
    application.setName(applicationName);
    application.setVersion(applicationVersion);
    
    castor.setOwner(ownerId);
    castor.setPrivate(_private);
    
    Input input = new Input();
    castor.setInput(input);
    input.setMainTableAlias(this.anchorTableName);
    input.setRelationSet(this.relationsetName);
    for (int i = 0; i < this.constraints.size(); i++)
    {
      QWRConstraint constraint = (QWRConstraint) this.constraints.get(i);
      input.addSearchCriteria(constraint.toCastor());
    }
    
    Output output = new Output();
    castor.setOutput(output);
    for (Iterator iter = this.fields.iterator(); iter.hasNext();)
    {
      QWRField field = (QWRField) iter.next();

      Column column = new Column();
      field.toCastor(column);
      output.addColumn(column);
    }
    
    Layouts layouts = new Layouts();
    castor.setLayouts(layouts);
    CastorLayout layout = new CastorLayout();
    layouts.addLayout(layout);
    
    layout.setMimeType(this.outputFormat.getMimetype());
    if (this.width != null)
      layout.setWidth(this.width.intValue());
    if (this.height != null)
      layout.setHeight(this.height.intValue());
    if (this.spacing != null)
      layout.setSpacing(this.spacing.intValue());
    if (this.prologue != null)
      layout.setPrologue(this.prologue.toCastor());
    if (this.pageHeader != null)
      layout.setPageHeader(this.pageHeader.toCastor());
    if (this.recordHeader != null)
      layout.setRecordHeader(this.recordHeader.toCastor());
    CastorLayoutPart part = new CastorLayoutPart();
    layout.setPart(part);
    {
      for (Iterator iter = this.groupFields.iterator(); iter.hasNext();)
      {
        QWRField field = (QWRField) iter.next();

        CastorGroup group = new CastorGroup();
        field.toCastor(group);
        part.setGroup(group);

        part = new CastorLayoutPart();
        group.setPart(part);
      }
    }
    {
      CastorLayoutColumns columns = new CastorLayoutColumns();
      part.setColumns(columns);
      for (Iterator iter = this.layoutFields.iterator(); iter.hasNext();)
      {
        QWRField field = (QWRField) iter.next();

        CastorLayoutColumn column = new CastorLayoutColumn();
        field.toCastor(column);
        columns.addColumn(column);
      }
    }
    if (this.recordFooter != null)
      layout.setRecordFooter(this.recordFooter.toCastor());
    if (this.pageFooter != null)
      layout.setPageFooter(this.pageFooter.toCastor());
    if (this.epilogue != null)
      layout.setEpilogue(this.epilogue.toCastor());
    
    return castor;
  }
  
}
