/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 07 18:25:43 CET 2006
 */
package jacob.event.ui.configuration;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class ConfigurationGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: ConfigurationGroup.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	public void onHide(IClientContext context, IGroup group) throws Exception 
	{
//		System.out.println("hide:"+getClass().getName());
	}
	
	public void onShow(IClientContext context, IGroup group) throws Exception 
	{
//		System.out.println("Show:"+getClass().getName());
	}
}
