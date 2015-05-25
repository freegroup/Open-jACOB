/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Sat Oct 15 15:41:38 CEST 2005
 */
package jacob.event.ui.product;



/**
 * The event handler for the ShowFocus3Projects generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class ShowFocus3Projects extends ShowFocusProjects 
{
  /* (non-Javadoc)
   * @see jacob.event.ui.product.ShowFocusProjects#getFocus()
   */
  protected int getFocus()
  {
    return 3;
  }
}
