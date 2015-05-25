package jacob.event.ui.person;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Mar 22 10:02:07 CET 2005
 *
 */
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ISearchActionEventHandler;
import jacob.common.AppLogger;
import jacob.scheduler.system.PersonSearchFactory;

import org.apache.commons.logging.Log;

/**
 * This is an event handler for a update button.
 * 
 * @author andherz
 *
 */
public class PersonButtonFuzzySearch extends ISearchActionEventHandler 
{
  static public final transient String RCS_ID = "$Id: PersonButtonFuzzySearch.java,v 1.1 2005/03/22 17:27:23 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();


  /**
   * This event handler will be called if the corresponding button has been pressed.
   * You can prevent the execution of the SEARCH action if you return [false].<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action else return [true]
   */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		return true;
	}

  /**
   * This event method will be called if the search action has been successfully done.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	}

  /**
   * The event handle if the status of the group has been changed.
   * This is a good place to enable/disable the button on relation to the
   * group state.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param button  The corresbonding button to this event handler
   * 
   */
	public void onGroupStatusChanged(IClientContext context, GroupState status,	IGuiElement button) throws Exception 
	{
	  button.setLabel("Fuzzy Search");
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.ISearchActionEventHandler#modifyQbe(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public void modifyQbe(IClientContext context, IActionEmitter emitter)throws Exception 
	{
		IDataTable table= context.getDataTable("person");
		String value = context.getGroup().getInputFieldValue("personLastname");
		System.out.println("Searching:"+value);
		List result = PersonSearchFactory.search(PersonSearchFactory.FUZZY, value,50);
		Iterator iter = result.iterator();
		while (iter.hasNext()) 
		{
			String element = (String) iter.next();
			System.out.println("\t-> matched: "+element);
			table.qbeSetValue("lastname",element);
		}
	}
}
