/*
 * Created on 10.08.2010
 *
 */
package de.tif.jacob.screen;

/**
 * @since 2.10
 */
public abstract class ITabPaneAction extends IAction
{
  /**
   * Return the representation icon of the action.
   * 
   * @param context
   * @return
   * @throws Exception
   */
  public abstract Icon getIcon(IClientContext context) throws Exception;

  /**
   * The hook method will be called, if the user has been click on the action.
   * 
   * @param context
   *          the current client context
   * @param pane
   *          the related tab pane
   */
  public abstract void execute(IClientContext context, ITabContainer container, ITabPane pane) throws Exception;

  /**
   */
  public String getLabel(IClientContext context)
  {
    return "";
  }

  /**
   */
  public String getTooltip(IClientContext context)
  {
    return "";
  }
}
