package jacob.common;

import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.History;
import jacob.model.History_entry;

import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.ITableField;
public class JournalManager
{
  private static final String AVOID_HISTORY = "AVOID_HISTORY";

  public static void disableHistoryForTransaction(IDataTransaction trans)
  {
    trans.setProperty(AVOID_HISTORY, Boolean.TRUE);
  }

  public static void append(IDataTableRecord record) throws Exception
  {
    Context c = Context.getCurrent();
    IDataTransaction trans = record.getCurrentTransaction();
    if (trans.getProperty(AVOID_HISTORY) != null)
      return;
    
    IDataTableRecord history = History.newRecord(record.getAccessor().newAccessor(), trans);
    if (record.isDeleted())
      history.setValue(trans, History.type, History.type_ENUM._delete);
    else if (record.isNew())
      history.setValue(trans, History.type, History.type_ENUM._new);
    else if (record.isUpdated())
      history.setValue(trans, History.type, History.type_ENUM._update);

   
    // Versuchen die History des Records mit abzuspeichern
    //
    try
    {
      history.setValue(trans, History.record_history, record.getValue("history"));
    }
    catch (Exception exc)
    {/* ignore */
    }
    
    // Den primary Key hinterlegen
    try
    {
      history.setValue(trans, History.record_serialized_pkey,  record.getTableAlias().getTableDefinition().getPrimaryKey().convertKeyValueToString(record.getPrimaryKeyValue()));
    }
    catch (Exception exc)
    {/* ignore */
    }
    
    String alias = record.getTableAlias().getName();
    history.setValue(trans, History.related_alias, alias);

    // Versuchen den PFad des geänderten Objektes zu ermitteln
    //
    if(alias.equals(Document.NAME) || alias.equals(Folder.NAME) && !record.isNew())
    {
      Context context = Context.getCurrent();
      IDataTableRecord boRecord = BoUtil.findByPkey(context, record.getStringValue("pkey"));
      String path = BoUtil.calculatePath(context, boRecord);
      history.setValue(trans, History.record_path, path);
    }
    
    if (!c.getUser().isSystem())
    {
      history.setValue(trans, History.user_key, c.getUser().getKey());
      history.setValue(trans, History.user_fullname, c.getUser().getFullName());
    }
    else
    {
      history.setValue(trans, History.user_fullname, "-System-");
    }
    
    if (record.isNewOrUpdated())
    {
      // OldValue / NewValue Protokollieren
      List<ITableField> tableFields = record.getTableAlias().getTableDefinition().getTableFields();
      for (ITableField tableField : tableFields)
      {
        if (record.hasChangedValue(tableField.getName()))
        {
          IDataTableRecord valueRecord = History_entry.newRecord(record.getAccessor().newAccessor(), trans);
          valueRecord.setValue(trans, History_entry.history_key, history.getValue(History.pkey));
          valueRecord.setValue(trans, History_entry.field, tableField.getName());
          valueRecord.setValue(trans, History_entry.field_label, tableField.getLabel());
          valueRecord.setStringValueWithTruncation(trans, History_entry.old_value, record.getOldStringValue(tableField.getName()));
          valueRecord.setStringValueWithTruncation(trans, History_entry.new_value, record.getStringValue(tableField.getName()));
        }
      }
    }
    
    // Wenn wir Glück haben hat der Alias/Tabelle ein "representative field"
    // gesetzt.
    //
    ITableField representativeField = record.getTableAlias().getTableDefinition().getRepresentativeField();
    if (representativeField != null)
    {
      history.setStringValueWithTruncation(trans, History.record_representative_field, record.getStringValue(representativeField.getName()));
    }
  }
}
