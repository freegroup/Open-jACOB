/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 10 08:54:26 CET 2009
 */
package jacob.event.ui;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.*;

/**
 *
 * @author andherz
 */
public class {Modulename}Domain extends IDomainEventHandler
{
	static public final transient String RCS_ID = "$Id: {Modulename}Domain.java,v 1.1 2009/12/14 23:18:53 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param group The hidden group
   */
  public void onShow(IClientContext context, IDomain domain) throws Exception
  {
    context.setCurrentForm("{modulename}_configure");
  }
}
