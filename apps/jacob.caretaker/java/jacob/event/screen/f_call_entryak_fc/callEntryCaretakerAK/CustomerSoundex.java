/*
 * Created on 15.07.2004
 * by mike
 *
 */
package jacob.event.screen.f_call_entryak_fc.callEntryCaretakerAK;

import de.tif.jacob.screen.IClientContext;

/**
 * 
 * @author mike
 *
 */
public final class CustomerSoundex extends jacob.common.gui.employee.CustomerSoundex
{
  static public final transient String RCS_ID = "$Id: CustomerSoundex.java,v 1.3 2004/08/04 17:36:05 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
	/* (non-Javadoc)
	 * @see jacob.common.gui.employee.CustomerSoundex#getLastname(de.tif.jacob.screen.IClientContext)
	 */
	public String getLastname(IClientContext context) throws Exception
	{
		return context.getGroup().getInputFieldValue("customerintLastname");
	}
}
