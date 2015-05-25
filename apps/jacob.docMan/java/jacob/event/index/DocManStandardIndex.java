package jacob.event.index;

import jacob.model.Document;
import jacob.model.Folder;

import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.impl.index.event.StandardLuceneEventHandler;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;

public class DocManStandardIndex extends StandardLuceneEventHandler
{
  private static final Set<String> INDEX_ALIAS = new HashSet<String>();
  
  private static final Set<String> FIELDS_TO_EXCLUDE = new HashSet<String>();
  
  private static final Set<String> FIELDS_TO_BOOST_2 = new HashSet<String>();
  
  private static final Set<String> FIELDS_TO_BOOST_5 = new HashSet<String>();
  
  static
  {
    INDEX_ALIAS.add(Document.NAME);
    INDEX_ALIAS.add(Folder.NAME);
    
    FIELDS_TO_EXCLUDE.add(Document.document_size_text);
    
    FIELDS_TO_BOOST_2.add(Document.name);
    FIELDS_TO_BOOST_2.add(Folder.name);
    
    // Synonyme werden 5-fach geboostet
    FIELDS_TO_BOOST_5.add(Document.synonyme);
    FIELDS_TO_BOOST_5.add(Folder.synonyme);
  }
  
  public boolean containsRecordsOfTableAlias(ITableAlias tableAlias)
  {
    return INDEX_ALIAS.contains(tableAlias.getName());
  }

  public boolean addToContentsField(Context context, ITableAlias alias, ITableField field) throws Exception
  {
    if (FIELDS_TO_EXCLUDE.contains(field.getName()))
      return false;
    
    return super.addToContentsField(context, alias, field);
  }

  public float getContentsFieldBoost(Context context, ITableAlias alias, ITableField field) throws Exception
  {
    if (FIELDS_TO_BOOST_2.contains(field.getName()))
      return 2.0f;
    
    if (FIELDS_TO_BOOST_5.contains(field.getName()))
      return 5.0f;
    
    // returns 1.0f
    return super.getContentsFieldBoost(context, alias, field);
  }
}
