/*
 * Created on 08.05.2008
 *
 */
package de.tif.jacob.screen.impl;

import java.util.Iterator;
import java.util.List;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISelection;

/**
 * Extended ISelection with persistend delete functionality.
 * Internal use only
 *
 */
public abstract class ExtSelection implements ISelection
{
  /**
   * Delete the handsover object in the database
   * 
   * @param trans
   * @param obj
   */
  public abstract void delete(IDataTransaction trans, Object obj) throws Exception;
  
  /**
   * Remove the object from the visual representation
   * @param obj
   * @throws Exception
   */
  public abstract void removeFromView(IClientContext context, Object obj) throws Exception;
  

  
  /**
   * {@inheritDoc}
   */
  public void remove(List objs)
  {
    Iterator iter = objs.iterator();
    while(iter.hasNext())
      remove(iter.next());
  }


  /**
   * {@inheritDoc}
   */
  public void clear()
  {
    this.remove(this.toList());
  }
}
