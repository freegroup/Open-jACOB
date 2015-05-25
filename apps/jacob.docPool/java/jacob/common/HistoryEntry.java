package jacob.common;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;

public class HistoryEntry
{
  public String getSerializedPkey()
  {
    return serializedPkey;
  }

  private String domain;
  private String form;
  private String tableAlias;
  private String serializedPkey;
  private String label;
  
  public HistoryEntry(IClientContext context, IGroup displayGroup) throws Exception
  {
    domain = displayGroup.getForm().getParent().getName();
    form = displayGroup.getForm().getName();
    tableAlias = displayGroup.getGroupTableAlias();
    IDataTableRecord record = context.getDataTable().getSelectedRecord();
    if(record!=null)
    {
      serializedPkey = record.getTableAlias().getTableDefinition().getPrimaryKey().convertKeyValueToString(record.getPrimaryKeyValue());
      if(record.getTableAlias().getTableDefinition().getRepresentativeField()!=null)
        label = record.getSaveStringValue(record.getTableAlias().getTableDefinition().getRepresentativeField().getName());
      else
        label = record.getTable().getName()+":"+record.getPrimaryKeyValue().toString();
    }
  }
  
  public void show(IClientContext context) throws Exception
  {
    context.setCurrentForm(domain, form);
    IDataAccessor acc = context.getDataAccessor();
    
    IDataTable table = acc.getTable(tableAlias);
    IDataKeyValue pkeyvalue = table.getTableAlias().getTableDefinition().getPrimaryKey().convertStringToKeyValue(serializedPkey);
    IDataTableRecord record = table.loadRecord(pkeyvalue);
    context.getGUIBrowser().add(context, record, true);
  }

  public String getLabel()
  {
    return label;
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((domain == null) ? 0 : domain.hashCode());
    result = prime * result + ((form == null) ? 0 : form.hashCode());
    result = prime * result + ((serializedPkey == null) ? 0 : serializedPkey.hashCode());
    result = prime * result + ((tableAlias == null) ? 0 : tableAlias.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HistoryEntry other = (HistoryEntry) obj;
    if (domain == null)
    {
      if (other.domain != null)
        return false;
    }
    else if (!domain.equals(other.domain))
      return false;
    if (form == null)
    {
      if (other.form != null)
        return false;
    }
    else if (!form.equals(other.form))
      return false;
    if (serializedPkey == null)
    {
      if (other.serializedPkey != null)
        return false;
    }
    else if (!serializedPkey.equals(other.serializedPkey))
      return false;
    if (tableAlias == null)
    {
      if (other.tableAlias != null)
        return false;
    }
    else if (!tableAlias.equals(other.tableAlias))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "HistoryEntry [domain=" + domain + ", form=" + form + ", serializedPkey=" + serializedPkey + ", tableAlias=" + tableAlias + "]";
  }
  
  
}
