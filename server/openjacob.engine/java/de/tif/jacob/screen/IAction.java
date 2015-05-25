/*
 * Created on 30.03.2008
 *
 */
package de.tif.jacob.screen;

import de.tif.jacob.screen.impl.IDProvider;

public abstract class IAction
{
  private final String id = "action_" + IDProvider.next();

  /**
   * returns the unique id of the action.
   * 
   * @return
   * @since 2.10
   */
  public String getId()
  {
    return id;
  }
 
  /**
   * 
   * @return the tooltip of the Action
   */
  public abstract String getTooltip(IClientContext context);
  
  /**
   * 
   * @return The label of the action
   */
  public abstract String getLabel(IClientContext context);
  }
