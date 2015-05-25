/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Feb 05 21:26:09 CET 2006
 */
package jacob.event.ui.email;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import jacob.common.AbstractDeleteIMAP;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 * The event handler for a delete button.<br>
 * 
 * @author andherz
 *
 */
public class DeleteWithAttachmentContextMenu extends AbstractDeleteIMAP 
{
	static public final transient String RCS_ID = "$Id: DeleteWithAttachmentContextMenu.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
}
