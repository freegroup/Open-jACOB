/*
 * Created on 30.03.2008
 *
 */
package de.tif.jacob.screen;

/**
 * Interface common to all objects that provide a selection like Browsers, Trees or Listboxes.
 *
 */
public interface ISelectionProvider
{
  /**
   * Returns the current selection for this provider. <br>
   * This method never returns null.<br>
   * 
   * @return the current selection
   */
  ISelection getSelection();

  /**
   * Add an action to the element.
   * 
   * @param action
   * @since 2.7.4
   */
  public void addSelectionAction(ISelectionAction action);
  
  /**
   * Remove an action from the element.
   *  
   * @param action
   * @since 2.7.4
   */
  public void removeSelectionAction(ISelectionAction action);


  /**
   * Remove all selection actions from the element.
   *
   * @since 2.7.4
   */
  public void removeAllSelectionActions();
}
