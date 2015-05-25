/*
 * Created on 30.03.2008
 *
 */
package de.tif.jacob.screen.impl.html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.IDataBrowserSortStrategy;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.ExtSelection;

public class TableListBoxSelection extends ExtSelection
{
  List records = new ArrayList();
  Object primarySelection=null;
  
  private final TableListBox parent;
  
  /**
   * Empty selection
   *
   */
  public TableListBoxSelection(TableListBox parent)
  {
    this.parent = parent;
  }

  /**
   * Selection with only one element
   * 
   * @param singleRecord
   */
  public TableListBoxSelection(TableListBox parent, IDataBrowserRecord primaryRecord)
  {
    this.parent = parent;
    primarySelection = primaryRecord;
  }

  /**
   * Multiple selection
   * 
   * @param firstSelectedRecord
   * @param allRecords
   */
  public TableListBoxSelection(TableListBox parent, IDataBrowserRecord primaryRecord, List allRecords)
  {
    this.parent = parent;
    records.add(primaryRecord);
    records.addAll(allRecords);
    primarySelection = primaryRecord;
  }

  
  public void delete(IDataTransaction trans, Object obj) throws Exception
  {
    IDataBrowserRecord record = (IDataBrowserRecord)obj;
    // remove the record fromt the database 
    record.getTableRecord().delete(trans);
  }
  
  public void add(IDataBrowserRecord record)
  {
    if(!records.contains(record))
      records.add(record);
  }
  
  public Object getPrimarySelection()
  {
    return this.primarySelection;
  }

  public Iterator iterator()
  {
    return records.iterator();
  }

  public int size()
  {
    return records.size();
  }

  public Object[] toArray()
  {
    return records.toArray();
  }

  public List toList()
  {
    return records;
  }


  public boolean isEmpty()
  {
    return records.isEmpty();
  }

  
  public void remove(Object obj)
  {
    IDataBrowserSortStrategy sort =parent.getDataInternal().getGuiSortStrategy();
    
    // Object in dem Browser finden und entfernen
    //
    int count = sort.recordCount();
    for(int i=0; i<count;i++)
    {
      IDataBrowserRecord record = sort.getRecord(i);
      if(record==obj)
      {
        this.parent.uncheckRow(i);
        return;
      }
    }
  }

  
  public void add(Object obj)
  {
    if(!(obj instanceof IDataRecord))
      return; // silently. Dummerweise kann man keine UserException werfen. Gibt das Interface icht her.

    IDataBrowserSortStrategy sort =parent.getDataInternal().getGuiSortStrategy();
    
    // Object in dem Browser finden und entfernen
    //
    int count = sort.recordCount();
    for(int i=0; i<count;i++)
    {
      IDataBrowserRecord record = sort.getRecord(i);
      if(record==obj || record.getPrimaryKeyValue().equals(((IDataRecord)obj).getPrimaryKeyValue()))
      {
        this.parent.checkRow(i);
        return;
      }
    }
  }

  /**
   * {@inheritDoc} 
   */
  public boolean contains(Object obj)
  {
    if(!(obj instanceof IDataRecord))
      return false;
    
    IDataBrowserInternal data =parent.getDataInternal();
    
    int count = data.recordCount();
    for(int i=0; i<count;i++)
    {
      IDataBrowserRecord record = data.getRecord(i);
      if(record==obj || record.getPrimaryKeyValue().equals(((IDataRecord)obj).getPrimaryKeyValue()))
        return true;
    }
    return false;
  }

  public void removeFromView(IClientContext context, Object obj) throws Exception
  {
    parent.remove(context,(IDataBrowserRecord)obj);
  }

  public void resetErrorDecoration(IClientContext context, Object obj)
  {
  }

  public void setErrorDecoration(IClientContext context, Object obj, String message)
  {
  }

  public void resetEmphasize(IClientContext context, Object obj)
  {
  }

  public void setEmphasize(IClientContext context, Object obj, boolean flag)
  {
  }
}
