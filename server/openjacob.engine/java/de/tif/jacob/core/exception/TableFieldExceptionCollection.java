/*
 * Created on 04.10.2007
 *
 */
package de.tif.jacob.core.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.i18n.Message;

/**
 * Internal collection exception.
 * 
 * @author Andreas Herz
 * @since 2.7.2
 */
public final class TableFieldExceptionCollection extends UserException implements ITableFieldsException
{
  private final Collection exceptions = new ArrayList();
  private final Collection fields = new HashSet();
  
  public TableFieldExceptionCollection(Message message)
  {
    super(message);
  }
  
  /**
   * @since 2.8.0
   * @param message
   */
  public TableFieldExceptionCollection(String message)
  {
    super(message);
  }

  public void add(ITableFieldException exc)
  {
    exceptions.add(exc);
    fields.add(exc.getTableField());
  }
  
  public Collection getExceptions()
  {
    return exceptions;
  }

  public Collection getTableFields()
  {
    return this.fields;
  }
  
  public String getDetails()
  {
    Context context = Context.getCurrent();
    StringBuffer detail = new StringBuffer("");
    Iterator iter = fields.iterator();
    while(iter.hasNext())
    {
       ITableField field = (ITableField)iter.next();
       String localizedLabel = I18N.localizeLabel(field.getLabel(), context, context.getApplicationLocale());
       if (localizedLabel == null || localizedLabel.length() == 0)
         localizedLabel = field.getLabel();
       detail.append(localizedLabel);
       detail.append(" (");
       detail.append(field.getTableDefinition().getName());
       detail.append(".");
       detail.append(field.getName());
       detail.append(")\n");
    }
    
    return detail.toString();
  }

  /**
   * Returns true if no Exception has been added to this collection.
   * 
   * @since 2.8.0
   * @return
   */
  public boolean isEmpty()
  {
    return exceptions.isEmpty();
  }
}


