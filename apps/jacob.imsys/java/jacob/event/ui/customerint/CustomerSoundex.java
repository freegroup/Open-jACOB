package jacob.event.ui.customerint;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 02 17:45:46 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.Util;

import org.apache.commons.codec.language.Soundex;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ISearchActionEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * This is an event handler for a update button.
 * 
 * @author mike
 *
 */
public class CustomerSoundex extends ISearchActionEventHandler 
{
  static public final transient String RCS_ID = "$Id: CustomerSoundex.java,v 1.2 2005/06/06 12:53:05 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();



  

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
    {
      emitter.setEnable(context.getSelectedRecord()==null);
    }
     
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
        // wenn kein Constraint in Lastname dann nicht suchen!
        String lastname = context.getGroup().getInputFieldValue("customerLastname");
         if (StringUtil.toSaveString(lastname).length() <3 )
         {
             alert("Für die Suche ist ein Nachname mit mindestens 3 Buchstaben erforderlich");
             return false;
         }
         String search = Util.PrepareSoundex(lastname);
         Soundex soundex = new Soundex();
         
         IDataTable table = context.getDataTable();
         table.qbeClear();
         table.qbeSetValue("soundex","="+soundex.soundex(search));
             IDataBrowser browser = context.getDataBrowser();    // the current browser
             // do the search itself
             //
             browser.search("r_customer");

         // display the result set
             //
             context.getGUIBrowser().setData(context, browser);
             // clear qbe for further search
             table.qbeClear();
        return false;
    }
    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
     */
    public void onSuccess(IClientContext context, IGuiElement button) throws Exception
    {
       

    }
}