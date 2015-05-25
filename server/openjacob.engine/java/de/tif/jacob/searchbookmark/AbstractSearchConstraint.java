/*
 * Created on 17.11.2009
 *
 */
package de.tif.jacob.searchbookmark;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import de.tif.jacob.core.data.impl.IDataFieldConstraint;
import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.data.internal.IDataFieldConstraintPrivate;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.report.impl.castor.Application;
import de.tif.jacob.report.impl.castor.Input;
import de.tif.jacob.report.impl.castor.Output;
import de.tif.jacob.report.impl.castor.ReportDefinition;
import de.tif.jacob.report.impl.castor.SearchCriteria;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.UserManagement;
import de.tif.jacob.util.xml.XMLSAXCopyToHandler;
import de.tif.jacob.util.xml.XMLWriter;
public abstract class AbstractSearchConstraint implements ISearchConstraint
{
  protected final ReportDefinition searchConstraint;
  private IUser owner = null;
  
  static class SingleSearchConstraint implements IDataFieldConstraint
  {
    final String qbeValue;
    final ITableAlias table;
    final ITableField field;
    final boolean isKeyValue;

    protected SingleSearchConstraint(String qbeValue, ITableAlias table, ITableField field, boolean isKeyValue)
    {
      this.qbeValue = qbeValue;
      this.table = table;
      this.field = field;
      this.isKeyValue = isKeyValue;
    }

    public String getQbeValue()
    {
      return qbeValue;
    }

    public boolean isQbeKeyValue()
    {
      return isKeyValue;
    }

    public ITableAlias getTableAlias()
    {
      return table;
    }

    public ITableField getTableField()
    {
      return field;
    }
  }
  static class SingleSearchConstraintGui implements IDataFieldConstraintPrivate
  {
    final String qbeValue;
    final ITableAlias table;
    final ITableField field;
    final String guiSource;
    final boolean isKeyValue;

    protected SingleSearchConstraintGui(String guiSource, String qbeValue, ITableAlias table, ITableField field, boolean isKeyValue)
    {
      this.qbeValue = qbeValue;
      this.table = table;
      this.field = field;
      this.guiSource = guiSource;
      this.isKeyValue = isKeyValue;
    }

    public String getQbeValue()
    {
      return qbeValue;
    }

    public boolean isQbeKeyValue()
    {
      return isKeyValue;
    }

    public ITableAlias getTableAlias()
    {
      return table;
    }

    public ITableField getTableField()
    {
      return field;
    }

    public String guiElementName()
    {
      return guiSource;
    }
  }
  static class MultipleSearchContraint implements IDataFieldConstraints
  {
    List constraints = new ArrayList();

    private void add(IDataFieldConstraint constraint)
    {
      constraints.add(constraint);
    }

    public Iterator getConstraints()
    {
      return constraints.iterator();
    }
  }

  public AbstractSearchConstraint(ReportDefinition reportDef)
  {
    this.searchConstraint = reportDef;
  }

  public AbstractSearchConstraint(String name, String appName, String domainName, String formName, String version, String relationSet, String mainTableAlias)
  {
    searchConstraint = new ReportDefinition();
    Application app = new Application();
    Input input = new Input();
    app.setName(appName);
    app.setVersion(version);
    input.setRelationSet(relationSet);
    input.setMainTableAlias(mainTableAlias);
    searchConstraint.setAnchorDomain(domainName);
    searchConstraint.setAnchorForm(formName);
    searchConstraint.setOutput(new Output());
    searchConstraint.setInput(input);
    searchConstraint.setApplication(app);
    searchConstraint.setPrivate(false);
    searchConstraint.setName(name);
    searchConstraint.setGuid(null); // not saved so far
  }

  public final String getGUID()
  {
    return searchConstraint.getGuid();
  }

  public final void setGUID(String guid)
  {
    this.searchConstraint.setGuid(guid);
  }
  
  /*
   * 
   * @see
   * de.tif.jacob.report.IReport#getAnchorDomain(de.tif.jacob.screen.IClientContext
   * )
   */
  public String getAnchorDomain()
  {
    return searchConstraint.getAnchorDomain();
  }

  /*
   * 
   * @see
   * de.tif.jacob.report.IReport#getAnchorDomain(de.tif.jacob.screen.IClientContext
   * )
   */
  public String getAnchorForm()
  {
    return searchConstraint.getAnchorForm();
  }

  public String getAnchorTable()
  {
    return searchConstraint.getInput().getMainTableAlias();
  }

  /*
   * 
   * @see de.tif.jacob.report.IReport#getApplication()
   */
  public final IApplicationDefinition getApplication()
  {
    IApplicationDefinition reportApplication;
    try
    {
      reportApplication = DeployMain.getActiveApplication(searchConstraint.getApplication().getName(), searchConstraint.getApplication().getVersion());
    }
    catch (Exception exc)
    {
      reportApplication = DeployMain.getActiveApplication(searchConstraint.getApplication().getName());
    }
    return reportApplication;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.report.IReport#getApplicationName()
   */
  public final String getApplicationName()
  {
    return searchConstraint.getApplication().getName();
  }

  /*
   * 
   * @see de.tif.jacob.report.IReport#getName()
   */
  public String getName()
  {
    return searchConstraint.getName();
  }

  /*
   * 
   * @see de.tif.jacob.report.IReport#setName(java.lang.String)
   */
  public void setName(String name)
  {
    searchConstraint.setName(name);
  }

  /*
   * 
   * @see de.tif.jacob.report.IReport#setDescription(java.lang.String)
   */
  public void setDescription(String description)
  {
    searchConstraint.setDesciption(description);
  }

  /*
   * 
   * @see de.tif.jacob.report.IReport#setOwner(de.tif.jacob.security.IUser)
   */
  public void setOwner(IUser user)
  {
    searchConstraint.setOwner(user.getLoginId());
    owner = user;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.report.IReport#getOwnerId()
   */
  public String getOwnerId()
  {
    return searchConstraint.getOwner();
  }

  public IUser getOwner() throws Exception
  {
    if (owner == null)
    {
      IApplicationDefinition def = getApplication();
      owner = UserManagement.getUser(def.getName(), def.getVersion().toString(), searchConstraint.getOwner());
    }
    return owner;
  }

  /**
   * 
   * @param tableAlias
   * @param fieldName
   * @param value
   * 
   */
  public void addConstraint(String tableAlias, String fieldName, String constraint, boolean isKeyValue)
  {
    addConstraint(null, tableAlias, fieldName, constraint, isKeyValue);
  }

  /**
   * 
   * @param tableAlias
   * @param fieldName
   * @param value
   * 
   */
  public void addConstraint(String guiSource, String tableAlias, String fieldName, String constraint, boolean isKeyValue)
  {
    SearchCriteria criteria = new SearchCriteria();
    criteria.setTableAlias(tableAlias);
    criteria.setField(fieldName);
    criteria.setValue(constraint);
    criteria.setGuiElement(guiSource);
    criteria.setIsKeyValue(isKeyValue);
    searchConstraint.getInput().addSearchCriteria(criteria);
  }

  /*
   * @see de.tif.jacob.report.IReport#getConstraints()
   */
  public IDataFieldConstraints getConstraints()
  {
    MultipleSearchContraint result = new MultipleSearchContraint();
    IApplicationDefinition def = getApplication();
    try
    {
      // enter search criteria for the browser
      //
      for (int i = 0; i < searchConstraint.getInput().getSearchCriteriaCount(); i++)
      {
        SearchCriteria criteria = searchConstraint.getInput().getSearchCriteria(i);
        ITableAlias table = def.getTableAlias(criteria.getTableAlias());
        ITableField field = table.getTableDefinition().getTableField(criteria.getField());
        if (criteria.getGuiElement() != null)
          // Falls die Suchbedingung von einem GUI Feld kommt, kann dies
          // gegenzeichnet werden
          // (Wird bei einem backfill in die GUI benötigt.)
          result.add(new SingleSearchConstraintGui(criteria.getGuiElement(), criteria.getValue(), table, field, criteria.getIsKeyValue()));
        else
          // Es ist unbekannt woher die Suchbedingung kommt
          result.add(new SingleSearchConstraint(criteria.getValue(), table, field, criteria.getIsKeyValue()));
      }
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(e);
    }
    return result;
  }

  public void setAnchorTable(String tableAlias)
  {
    searchConstraint.getInput().setMainTableAlias(tableAlias);
  }

  public void setRelationset(IRelationSet relationset)
  {
    searchConstraint.getInput().setRelationSet(relationset.getName());
  }

  public IRelationSet getRelationset(IClientContext context)
  {
    if(searchConstraint.getInput().getRelationSet()==null)
      return context.getApplicationDefinition().getRelationSet(IRelationSet.DEFAULT_NAME);
    
    return context.getApplicationDefinition().getRelationSet(searchConstraint.getInput().getRelationSet());
  }

  /**
   * returns the XML representation of the report
   * 
   */
  public String toXml() throws Exception
  {
    StringWriter sw = new StringWriter();
    XMLWriter xw = new XMLWriter(sw);
    XMLSAXCopyToHandler dh = new XMLSAXCopyToHandler(xw);
    Marshaller.marshal(searchConstraint, dh);
    return sw.toString();
  }

  public String toXmlFormatted() throws Exception
  {
    Document doc = XMLUtils.newDocument();
    Marshaller.marshal(searchConstraint, doc);
    StringWriter sw = new StringWriter();
    org.apache.xml.serialize.OutputFormat outFormat = new org.apache.xml.serialize.OutputFormat();
    outFormat.setIndenting(true);
    outFormat.setIndent(2);
    outFormat.setLineWidth(200);
    // IBIS: remove hardcoded encoding
    outFormat.setEncoding("ISO-8859-1");
    org.apache.xml.serialize.XMLSerializer xmlser = new org.apache.xml.serialize.XMLSerializer(sw, outFormat);
    xmlser.serialize(doc); // replace your_document with reference to document
                           // you want to serialize
    return sw.toString();
  }
}
