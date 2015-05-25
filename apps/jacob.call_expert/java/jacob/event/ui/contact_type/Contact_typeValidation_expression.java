/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Mar 03 14:20:29 CET 2009
 */
package jacob.event.ui.contact_type;

import jacob.common.AppLogger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;

import com.hecc.jacob.validation.contacts.ContactValidator;

import de.tif.jacob.core.Context;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.event.ITextFieldEventHandler;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 * 
 * @author andherz
 */
 public class Contact_typeValidation_expression extends ITextFieldEventHandler implements IAutosuggestProvider
 {
	static public final transient String RCS_ID = "$Id: Contact_typeValidation_expression.java,v 1.1 2009/03/04 08:59:38 A.Herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
	{
		// insert your code here e.g.
    //
    // text.setEditable(state.equals(IGuiElement.NEW));
	}


  public AutosuggestItem[] suggest(Context arg0, String arg1, int arg2) throws Exception
  {
    List<AutosuggestItem> result = new ArrayList<AutosuggestItem>();

    // use ServiceLoader if you are running in java 1.6
    
    ClassLoader ldr = this.getClass().getClassLoader();
    Enumeration<URL> urls = ldr.getResources("META-INF/service/" + ContactValidator.class.getName());
    while(urls.hasMoreElements())
    {
      URL url = urls.nextElement();
      InputStream is = url.openStream();
      try 
      {
        BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while (true) 
        {
          String line = r.readLine();
          if (line == null)
            break;
          int comment = line.indexOf('#');
          if (comment >= 0)
            line = line.substring(0, comment);
          String name = line.trim();
          if (name.length() == 0)
            continue;
          result.add(new AutosuggestItem(name,name));
        }
      }
      finally 
      {
        is.close();
      }
    }

    return result.toArray(new AutosuggestItem[result.size()]); 
  }


  public void suggestSelected(IClientContext arg0, AutosuggestItem arg1) throws Exception
  {
    // TODO Auto-generated method stub
    
  }
  
  /**
   * Eventhandler for hot keys like ENTER.
   * You must implement the interface "HotkeyEventHandler" if you want receive this events.
   * 
  public void keyPressed(IClientContext context, KeyEvent e)
  {
    System.out.println("pressed");
  }
  
  public int getKeyMask(IClientContext context)
  {
    return KeyEvent.VK_ENTER;
  }
  */
  
}
