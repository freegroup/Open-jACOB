/*
 * Created on 30.07.2004
 * by mike
 *
 */
package jacob.common.gui.task;

import de.tif.jacob.screen.IClientContext;
import jacob.common.gui.GenericPrint;

/**
 * 
 * @author mike
 *
 */
public class TaskPrint extends GenericPrint
{
	 static public final transient String RCS_ID = "$Id: TaskPrint.java,v 1.1 2004/07/30 17:31:54 mike Exp $";
	 static public final transient String RCS_REV = "$Revision: 1.1 $";

	/* (non-Javadoc)
	 * @see jacob.common.gui.GenericPrint#getConstraint(de.tif.jacob.screen.IClientContext)
	 */
	public String getConstraint(IClientContext context) throws Exception
	{
		// F�r Meldungen ist nur die Dokumentvorlage mit Use_im "Meldung|�berall" g�ltig
		return "Auftrag|�berall";
	}
}
