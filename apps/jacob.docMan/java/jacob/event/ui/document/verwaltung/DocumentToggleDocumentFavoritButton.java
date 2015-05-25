/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 01 16:55:16 CEST 2010
 */
package jacob.event.ui.document.verwaltung;

import jacob.browser.FavoriteBrowser;
import jacob.common.AppLogger;
import jacob.model.Bo;
import jacob.model.Favorite;
import jacob.relationset.BoRelationset;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the DocumentToggleDocumentFavoritButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class DocumentToggleDocumentFavoritButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: DocumentToggleDocumentFavoritButton.java,v 1.1 2010-09-17 08:42:23 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
		IDataTableRecord currentRecord = context.getSelectedRecord();
    int favoriteIndex = currentRecord.getintValue(Favorite.favorite_index);
    int index =0;
    if(favoriteIndex==0)
    {
      index =1;
      IDataBrowser browser = context.getDataAccessor().newAccessor().getBrowser(FavoriteBrowser.NAME);
      browser.setMaxRecords(1);
      browser.search(IRelationSet.LOCAL_NAME);
      
      if(browser.recordCount()>0)
        index = browser.getRecord(0).getintValue(FavoriteBrowser.browserFavorite_index)+1;
    }
    
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      currentRecord.setValue(trans, Bo.favorite_index, index);
      trans.commit();
      context.getDataAccessor().propagateRecord(context.getSelectedRecord(), BoRelationset.NAME, Filldirection.BOTH);
    }
    finally
    {
      trans.close();
    }
    IButton button = (IButton)emitter;
    if(index==0)
      button.setLabel("Zu Favoriten hinzufŸügen");
    else
      button.setLabel("Von Favoriten entfernen");
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
	{
    IDataTableRecord currentRecord = context.getSelectedRecord();
    if(currentRecord!=null)
    {
      IButton button = (IButton)emitter;
      int favoriteIndex = currentRecord.getintValue(Favorite.favorite_index);
      if(favoriteIndex==0)
        button.setLabel("Zu Favoriten hinzufügen");
      else
        button.setLabel("Von Favoriten entfernen");
    }
	}
}
