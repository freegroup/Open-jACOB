/*
 * Created on 15.07.2004
 * by mike
 *
 */
package jacob.event.screen.f_call_entry.orgcustomerInt;

import de.tif.jacob.screen.IClientContext;

/**
 * 
 * @author mike
 *
 */
public final class CustomerSoundex extends jacob.common.gui.employee.CustomerSoundex
{

	/* (non-Javadoc)
	 * @see jacob.common.gui.employee.CustomerSoundex#getLastname(de.tif.jacob.screen.IClientContext)
	 */
	public String getLastname(IClientContext context) throws Exception
	{
		return context.getGroup().getInputFieldValue("customerLastname");
	}
}
