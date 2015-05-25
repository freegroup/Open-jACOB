/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 23:43:52 CEST 2010
 */
package jacob.event.ui.search;

import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class GlobalcontentGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: GlobalcontentGroup.java,v 1.1 2010-08-17 22:02:39 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
  @Override

  public Class getSearchBrowserEventHandlerClass()
  {
    return SearchBrowserEventHandler.class;
  }



}
