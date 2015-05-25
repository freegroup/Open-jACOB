/*
 * Created on 30.03.2008
 *
 */
package de.tif.jacob.screen;

public abstract class ISelectionAction extends IAction
{
  /**
   * Returns the label of the action.<br>
   * This method will be ignored if you have added the action in the
   * jACOB-Designer and not via API call.<br>
   * 
   * @param context
   *          The current context of the application
   * @return
   */
  public String getLabel(IClientContext context)
  {
    return null;
  }

  /**
   * @since 2.10
   */
  public String getTooltip(IClientContext context)
  {
    return "";
  }

  /**
   * Return an Icon representation of the Action. This will be used if you
   * visualize the selection actions as flat button bar.<br>
   * 
   * @param context
   * @return the Icon tho show or null.
   * @since 2.9.3
   */
  public Icon getIcon(IClientContext context)
  {
    return null;
  }

  /**
   * The action method. This will be called if the use clicks on the browser
   * action icon.
   * 
   * @param context
   *          The current context of the application
   * @param emitter
   *          The UI element which request the execution of this action
   * @param selection
   *          The selection
   * @throws Exception
   *           w
   */
  public abstract void execute(IClientContext context, IGuiElement emitter, ISelection selection) throws Exception;

  /**
   * The action can be disabled by return <code>false</code>
   * 
   * @param context
   * @param host
   *          The corresponding UI Element which provides the selection. Can be
   *          a browser, table, grid or any kind of container
   * @return
   */
  public boolean isEnabled(IClientContext context, IGuiElement host)
  {
    return true;
  }

  /**
   * The action can be hide by return <code>false</code>
   * 
   * @param context
   * @param host
   *          The corresponding UI Element which provides the selection. Can be
   *          a browser, table, grid or any kind of container
   * @since 3.0
   * @return
   */
  public boolean isVisible(IClientContext context, IGuiElement host)
  {
    return true;
  }
}
