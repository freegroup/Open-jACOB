/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Mar 24 11:58:46 CET 2006
 */
package jacob.event.ui.myaccess;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;

import jacob.common.Access;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author mike
 */
 public class MyaccessGroup extends IGroupListenerEventHandler 
 {
	static public final transient String RCS_ID = "$Id: MyaccessGroup.java,v 1.2 2006-05-12 10:04:53 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
	 * @param emitter The corresponding GUI element of this event handler
	 * 
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
    // erstmal alle deaktivieren
    for (int i = 0; i < 20; i++)
    {
      IGuiElement button = context.getGroup().findByName("AppName" + Integer.toString(i));
      button.setVisible(false);
    }
    
    TreeMap application = (TreeMap) context.getUser().getProperty("applications");
    
    //wenn nur eine Applikation direkt einloggen
    if(application.size()==1)
    {
      Access.login((String)application.get(application.firstKey()));
      return;
    }
    int counter =0;
    Set keySet = application.keySet();
    for (Iterator iter = keySet.iterator(); iter.hasNext();)
    {
      IGuiElement button = context.getGroup().findByName("AppName" + Integer.toString(counter));
      String name = (String)(iter.next());
      button.setLabel(name);
      button.setVisible(true);
      counter=counter+1;
    }
    
	}
}
