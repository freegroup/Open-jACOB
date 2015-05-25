package jacob.event.ui.milestone;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Aug 31 20:50:14 CEST 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.data.xml.Serializer;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the CreateReportButton-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class CreateReportButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CreateReportButton.java,v 1.4 2009-05-06 22:00:27 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
   {
		IDataTableRecord currentRecord = context.getSelectedRecord();
		// create new accessor to avoid any backlash to calling context
		DataAccessor accessor = (DataAccessor)context.getDataAccessor().newAccessor();

		accessor.propagateRecord(currentRecord, Filldirection.BOTH);
		StringBuffer data = new StringBuffer();
		IRelationSet relSet = context.getApplicationDefinition().getDefaultRelationSet();
		
		Serializer.appendXml(data,currentRecord,relSet,Filldirection.BOTH);
		
		DataDocumentValue report = LetterFactory.transform(context,currentRecord, currentRecord.getLinkedRecord("report_template").getDocumentValue("document"));
		context.createDocumentDialog(report).show();
  }

   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
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
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
    boolean enable = status == IGuiElement.SELECTED && context.getSelectedRecord().hasLinkedRecord("report_template");
	  button.setVisible(enable);
	}
}

