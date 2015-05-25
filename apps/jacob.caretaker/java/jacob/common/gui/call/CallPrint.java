/*
 * Created on 30.07.2004
 * by mike
 *
 */
package jacob.common.gui.call;

import de.tif.jacob.screen.IClientContext;
import jacob.common.gui.GenericPrint;

/**
 * 
 * @author mike
 *
 */
public class CallPrint extends GenericPrint
{
	 static public final transient String RCS_ID = "$Id: CallPrint.java,v 1.1 2004/07/30 13:09:40 mike Exp $";
	 static public final transient String RCS_REV = "$Revision: 1.1 $";

	/* (non-Javadoc)
	 * @see jacob.common.gui.GenericPrint#getConstraint(de.tif.jacob.screen.IClientContext)
	 */
	public String getConstraint(IClientContext context) throws Exception
	{
		// Für Meldungen ist nur die Dokumentvorlage mit Use_im "Meldung|überall" gültig
		return "Meldung|überall";
	}
}
