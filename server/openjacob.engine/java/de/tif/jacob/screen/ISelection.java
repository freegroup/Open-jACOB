/*
 * Created on 30.03.2008
 *
 */
package de.tif.jacob.screen;

import java.util.Iterator;
import java.util.List;

/**
 * Interface for a selection.
 * 
 * @since 2.7.4
 */
public interface ISelection
{
  /**
   * Returns whether this selection is empty. 
   * 
   * @return  true if this selection is empty, and false otherwise
   */
  public boolean isEmpty();
  
  /**
   * Returns the primary element in this selection, or null  if the selection is empty.<br> 
   * This is basicly the backfilled object in the UI.<br>
   * 
   * @return an element, or null if none
   */
  public Object getPrimarySelection(); 
  
  /**
   * Returns an iterator over the elements of this selection. 
   * 
   * @return an iterator over the selected elements
   */
  public Iterator iterator();
  
  /**
   * Returns the number of elements selected in this selection. 
   * 
   * @return the number of elements selected.
   */
  public int size();
  
  /**
   * Returns the elements in this selection as an array.
   * 
   * @return the selected elements as an array
   */
  public Object[] toArray();
  
  /**
   * Returns the elements in this selection as a List.
   *  
   * @return the selected elements as a list
   */
  public List toList();
  
  /**
   * Remove the handsover object from the selection.
   */
  public void remove(Object obj);
  
  /**
   * Remove the handsover objects from the selection.<br>
   * The objects will not removed from the container itself.<br>
   * 
   * @since 2.8.4
   * @param objs the objects to remove from the selection.
   */
  public void remove(List objs);

  /**
   * Return true if the object part of the selection.
   * 
   * @since 2.8.4
   */
  public boolean contains(Object obj);

  /**
   * Clear the selection. All selections will be released.
   * @since 2.8.4
   */
  public void clear();
  
  /**
   * Add the the handsover object from the selection.
   */
  public void add(Object obj);
  
  /**
   * Mark the hands over element with an error decoration.
   * The error decoration will be remove if the underlying element has
   * been changed.
   * 
   * @param context
   *          the current client context
   * @param message
   *          the message to add to the error decoration
   *          
   * @see de.tif.jacob.screen.IGuiElement#setErrorDecoration
   */
  public void setErrorDecoration(IClientContext context, Object obj, String message);

  /**
   * Remove an error decoration from the hands over element (if any).
   * 
   * @param context
   *          the current client context
   *          
   * @see de.tif.jacob.screen.IGuiElement#resetErrorDecoration
   */
  public void resetErrorDecoration(IClientContext context, Object obj);


  
  /**
   * Emphasize the hands over element.
   * 
   * @param context
   *          the current client context
   * @param obj
   *          the related object
   * @param flag
   *          the emphasize flag
   */
  public void setEmphasize(IClientContext context, Object obj, boolean flag);

  /**
   * Remove the emphasize attribute from the hands over element (if any).
   * 
   * @param context
   *          the current client context
   * @param obj
   *          the related object
   */
  public void resetEmphasize(IClientContext context, Object obj);
}
