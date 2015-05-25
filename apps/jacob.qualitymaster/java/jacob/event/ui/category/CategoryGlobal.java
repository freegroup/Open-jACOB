/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 17 11:26:47 CET 2006
 */
package jacob.event.ui.category;

import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;

/**
 * 
 * @author andreas
 */
public class CategoryGlobal extends ICheckBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: CategoryGlobal.java,v 1.1 2006/02/24 02:16:16 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This event handler method will be called, if the user sets a mark at a
   * checkbox.
   * 
   * @param checkBox
   *          The checkbox itself
   * @param context
   *          The current context of the application
   * 
   */
  public void onCheck(IClientContext context, ICheckBox checkBox) throws Exception
  {
    IGroup group = context.getGroup();
    if (group.getDataStatus() == IGuiElement.NEW || group.getDataStatus() == IGuiElement.UPDATE)
    {
      IGuiElement categoryOrgcategoryIFBrowser =group.findByName("categoryOrgcategoryIFBrowser");
      
      // TODO: Hack, remove if deployed on Engine with fixed problem
      if (categoryOrgcategoryIFBrowser == null)
        categoryOrgcategoryIFBrowser =group.findByName("orgcategoryIFBrowser");
      
      categoryOrgcategoryIFBrowser.setEnable(false);
    }
  }

  /**
   * This event handler method will be called, if the user unchecks a checkbox.
   * 
   * @param checkBox
   *          The checkbox itself
   * @param context
   *          The current context of the application
   */
  public void onUncheck(IClientContext context, ICheckBox checkBox) throws Exception
  {
    IGroup group = context.getGroup();
    if (group.getDataStatus() == IGuiElement.NEW || group.getDataStatus() == IGuiElement.UPDATE)
    {
      IGuiElement categoryOrgcategoryIFBrowser =group.findByName("categoryOrgcategoryIFBrowser");
      
      // TODO: Hack, remove if deployed on Engine with fixed problem
      if (categoryOrgcategoryIFBrowser == null)
        categoryOrgcategoryIFBrowser =group.findByName("orgcategoryIFBrowser");
      
      categoryOrgcategoryIFBrowser.setEnable(true);
    }
  }
}
