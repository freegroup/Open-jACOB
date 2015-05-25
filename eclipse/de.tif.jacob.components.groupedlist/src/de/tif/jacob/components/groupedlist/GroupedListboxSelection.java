/*
 * Created on 20.11.2009
 *
 */
package de.tif.jacob.components.groupedlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISelection;

public class GroupedListboxSelection implements ISelection
{
  GroupedListbox listbox;
  List<Object> selection = new ArrayList<Object>();
  
  public GroupedListboxSelection(GroupedListbox groupedList)
  {
    this.listbox = groupedList;
  }
  
  public void add(Object obj)
  {
    this.listbox.select(obj,true);
    this.selection.add(obj);
  }

  public void clear()
  {
    this.listbox.clear((IClientContext)Context.getCurrent());
    this.selection.clear();
  }

  public boolean contains(Object obj)
  {
    return this.listbox.isSelectet(obj);
  }

  public Object getPrimarySelection()
  {
    return null;
  }

  public boolean isEmpty()
  {
    return selection.isEmpty();
  }

  public Iterator iterator()
  {
    // avoid concurrent modification exception
    return new ArrayList(selection).iterator();
  }

  public void remove(List objs)
  {
    this.selection.remove(objs);
    this.listbox.select(objs, false);
  }

  public void remove(Object obj)
  {
    this.selection.remove(obj);
    this.listbox.select(obj, false);
  }

  public void resetEmphasize(IClientContext context, Object obj)
  {
    this.listbox.setEmphasize(obj,false);
    
  }

  public void resetErrorDecoration(IClientContext context, Object obj)
  {
    this.listbox.setErrorDecoration(obj,null);
  }

  public void setEmphasize(IClientContext context, Object obj, boolean flag)
  {
    this.listbox.setEmphasize(obj,true);
  }

  public void setErrorDecoration(IClientContext context, Object obj, String message)
  {
    this.listbox.setErrorDecoration(obj,message);
  }

  public int size()
  {
    return this.selection.size();
  }

  public Object[] toArray()
  {
    return this.selection.toArray();
  }

  public List toList()
  {
    return new ArrayList(this.selection);
  }
}
