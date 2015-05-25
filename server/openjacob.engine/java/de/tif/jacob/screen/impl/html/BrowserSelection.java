/*
 * Created on 30.03.2008
 *
 */
package de.tif.jacob.screen.impl.html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.IDataBrowserSortStrategy;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.ExtSelection;
import de.tif.jacob.screen.impl.HTTPBrowser.Marker;
import de.tif.jacob.util.StringUtil;

public final class BrowserSelection extends ExtSelection
{
  private final List records = new ArrayList();
  private final Object primarySelection;
  
  private final Browser parent;

  /**
   * Empty selection
   *
   */
  public BrowserSelection(Browser parent)
  {
    this.parent = parent;
    this.primarySelection = null;
  }

  /**
   * Selection with only one element
   * 
   * @param singleRecord
   */
  public BrowserSelection(Browser parent, IDataBrowserRecord primaryRecord)
  {
    this.parent = parent;
    primarySelection = primaryRecord;
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
    // avoid concurrent modification exception
    return new ArrayList(records).iterator();
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
    // avoid concurrent modification exception
    return new ArrayList(records);
  }

  public boolean isEmpty()
  {
    return records.isEmpty();
  }

  
  public void delete(IDataTransaction trans, Object obj) throws Exception
  {
    IDataBrowserRecord record = (IDataBrowserRecord)obj;
    // remove the record from the database 
    record.getTableRecord().delete(trans);
  }

  public void remove(Object obj)
  {
    IDataBrowserSortStrategy sort =parent.getDataInternal().getGuiSortStrategy();
    
    // Object im Browser finden und Selektion zur�cknehmen
    //
    int count = sort.recordCount();
    for(int i=0; i<count;i++)
    {
      IDataBrowserRecord record = sort.getRecord(i);
      if(record==obj)
      {
        this.parent.uncheckRow(i);
        records.remove(record);
        return;
      }
    }
  }

  
  /**
   * Add the the handsover object from the selection.
   */
  public void add(Object obj)
  {
    if(!(obj instanceof IDataRecord))
      return; // silently. Dummerweise kann man keine UserException werfen. Gibt das Interface icht her.

    IDataBrowserSortStrategy sort =parent.getDataInternal().getGuiSortStrategy();
    
    // Object im Browser finden und Selektion setzen
    //
    int count = sort.recordCount();
    for(int i=0; i<count;i++)
    {
      IDataBrowserRecord record = sort.getRecord(i);
      if(record==obj || record.getPrimaryKeyValue().equals(((IDataRecord)obj).getPrimaryKeyValue()))
      {
        this.parent.checkRow(i);
        records.add(record);
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
    
    int count = records.size();
    for(int i=0; i<count;i++)
    {
      IDataRecord record = (IDataRecord)records.get(i);
      if(record==obj || record.getPrimaryKeyValue().equals(((IDataRecord)obj).getPrimaryKeyValue()))
        return true;
    }
    return false;
  }

  public void removeFromView(IClientContext context, Object obj) throws Exception
  {
    this.parent.remove(context, (IDataBrowserRecord)obj);
  }

  public void resetErrorDecoration(IClientContext context, Object obj)
  {
    parent.resetErrorDecoration(context,(IDataBrowserRecord)obj);
  }

  public void setErrorDecoration(IClientContext context, Object obj, String message)
  {
    parent.setErrorDecoration(context,(IDataBrowserRecord)obj,StringUtil.toSaveString(message));
  }

  public void resetEmphasize(IClientContext context, Object obj)
  {
    Map markerMap = parent.getRecordMarkerMap();
    Marker marker =(Marker)markerMap.get(obj);
    if(marker!=null)
    {
      marker.emphasize=false;
      if(marker.allFalse())
        markerMap.remove(marker);
    }
  }

  public void setEmphasize(IClientContext context, Object obj, boolean flag)
  {
    parent.setEmphasize(context,(IDataBrowserRecord)obj,flag);
  }

}



