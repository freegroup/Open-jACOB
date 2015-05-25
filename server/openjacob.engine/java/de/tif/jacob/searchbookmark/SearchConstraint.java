/*
 * Created on 17.11.2009
 *
 */
package de.tif.jacob.searchbookmark;
import java.io.StringReader;
import java.util.Iterator;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.IDataFieldConstraint;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataFieldConstraintPrivate;
import de.tif.jacob.report.impl.castor.ReportDefinition;
public class SearchConstraint extends AbstractSearchConstraint
{
  /**
   * @param tableRecord
   * @throws Exception
   */
  public SearchConstraint(IDataTableRecord tableRecord) throws Exception
  {
    super((ReportDefinition) ReportDefinition.unmarshalReportDefinition(new StringReader(tableRecord.getStringValue(de.tif.jacob.core.model.Searchconstraint.definition))));
  }

  public SearchConstraint(String name, String appName, String domainName, String formName, String version, IDataBrowserInternal dataBrowser)
  {
    super(name, appName, domainName, formName, version, dataBrowser.getRelationSet().getName(), dataBrowser.getTableAlias().getName());
    
    if (dataBrowser != null && dataBrowser.getLastSearchConstraints() != null)
    {
      Iterator constraints = dataBrowser.getLastSearchConstraints().getConstraints();
      while (constraints.hasNext())
      {
        IDataFieldConstraintPrivate constraint = (IDataFieldConstraintPrivate) constraints.next();
        String guiSource = constraint.guiElementName();
        String table = constraint.getTableAlias().getName();
        String field = constraint.getTableField().getName();
        String value = constraint.getQbeValue();
        boolean isKeyValue = constraint.isQbeKeyValue();
        addConstraint(guiSource, table, field, value, isKeyValue);
      }
    }
    // Die Constraints die per Script gesetzt worden sind müssen jetzt wieder
    // gesetzt werden
    //
    Iterator constraints = dataBrowser.getLastSearchConstraints().getConstraints();
    while (constraints.hasNext())
    {
      Object obj = constraints.next();
      if (obj instanceof IDataFieldConstraintPrivate)
      {
        IDataFieldConstraintPrivate constraint = (IDataFieldConstraintPrivate) obj;
        if (constraint.guiElementName() != null && constraint.guiElementName().length() > 0)
        {
          // ignore. This is not a script constraint.
        }
        else
        {
          String gui = constraint.guiElementName();
          String table = constraint.getTableAlias().getName();
          String field = constraint.getTableField().getName();
          String value = constraint.getQbeValue();
          boolean isKeyValue = constraint.isQbeKeyValue();
          addConstraint(gui, table, field, value, isKeyValue);
        }
      }
      else if (obj instanceof IDataFieldConstraint)
      {
        IDataFieldConstraint constraint = (IDataFieldConstraint) obj;
        String table = constraint.getTableAlias().getName();
        String field = constraint.getTableField().getName();
        String value = constraint.getQbeValue();
        boolean isKeyValue = constraint.isQbeKeyValue();
        addConstraint(table, field, value, isKeyValue);
      }
    }
  }
}
