/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.groupedlist;

import java.util.List;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ILabelProvider;
import de.tif.jacob.screen.IListBox;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionProvider;

public interface IGroupedListbox
{
  /**
   * Clear the groubed list box. This will remove all entries
   * from the list.
   * 
   * @param context
   */
  public void clear(IClientContext context) throws Exception;
  
  /**
   * Add a new sublist to the grouped listbox.
   * 
   * @param header
   * @param groupElements
   * @param allowSingleSelection true if you allow the user to select/deselect subelements of the group
   */
  public void addGroup(String groupHeader, List<Object> groupElements, boolean allowSingleSelection) throws Exception;
  
  /**
   * Set the labelprovider for the grouped listbox. This is optional.<br>
   * toString() will be called in the other case.
   * 
   * @param labelProvider
   */
  public void setLabelProvider(ILabelProvider labelProvider) throws Exception;
  
  /**
   * Reset the current user selection
   * 
   * @param context
   */
  public void resetSelection(IClientContext context) throws Exception;
  
  public void selectAll( boolean selectionFlag);

  /**
   * Select or deselect the given object
   * 
   * @param obj
   * @param selectionFlag
   */
  public void select(Object obj, boolean selectionFlag);
  
  /**
   * Select or deselect all given objects
   * 
   * @param objs
   * @param selectionFlag
   */
  public void select(List<Object> objs, boolean selectionFlag) throws Exception;
  
  /**
   * Returns the current user selection
   * 
   * @param context
   */
  public ISelection getSelection(IClientContext context) throws Exception;


  public boolean isEditable();
  
  public void setEditable(boolean editable);
  
  public void setVisible(boolean flag);
  
}

