/*
 * Created on 15.07.2004
 * by mike
 *
 */
package jacob.common.gui.employee;

import jacob.common.Util;

import org.apache.commons.codec.language.Soundex;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * 
 * @author mike
 *
 */
/**
 * Ziel ist es auf Lastname eine soundex Suche zu machen
 * @author mike
 *
 */
public abstract class CustomerSoundex extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CustomerSoundex.java,v 1.8 2004/08/13 10:04:17 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";


  /**
   * Übergibt den Nachname aus der Gruppe  Melder
   * @param context
   * @return Stringvalue of ADtafield employee.lastnamecorr
   * @throws Exception
   */
  public abstract String getLastname(IClientContext context) throws Exception;

  /* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{

	    // wenn kein Constraint in Lastname dann nicht suchen!
	   String lastname = getLastname(context);
	    if (StringUtil.toSaveString(lastname).length() <3 )
	    {
	    	alert("Für die Suche ist ein Nachname mit mindestens 3 Buchstaben erforderlich");
	    	return ;
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
			
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
	  emitter.setEnable(context.getSelectedRecord()==null);
	}

}
