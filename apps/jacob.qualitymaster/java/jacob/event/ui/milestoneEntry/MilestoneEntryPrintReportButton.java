/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed May 06 14:50:36 CEST 2009
 */
package jacob.event.ui.milestoneEntry;

import jacob.model.Milestone;
import jacob.model.MilestoneEntry;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
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
 * The event handler for the MilestoneEntryPrintReportButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class MilestoneEntryPrintReportButton extends IButtonEventHandler 
{
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
      // Den milestoneEntry Record nach milestone "konvertieren", da die Reports alle
      // auf den Alias 2milestone" basieren.
      IDataTable milestoneTable = context.getDataAccessor().newAccessor().getTable(Milestone.NAME);
      milestoneTable.qbeSetKeyValue(Milestone.pkey, currentRecord.getValue(MilestoneEntry.pkey));
      milestoneTable.search(IRelationSet.LOCAL_NAME);
      currentRecord = milestoneTable.getSelectedRecord();
      
      
      // create new accessor to avoid any backlash to calling context
      DataAccessor accessor = (DataAccessor)context.getDataAccessor().newAccessor();

      accessor.propagateRecord(currentRecord, Filldirection.BOTH);
      StringBuffer data = new StringBuffer();
      IRelationSet relSet = context.getApplicationDefinition().getDefaultRelationSet();
      
      Serializer.appendXml(data,currentRecord,relSet,Filldirection.BOTH);
      
      DataDocumentValue report = LetterFactory.transform(context,currentRecord, currentRecord.getLinkedRecord("report_template").getDocumentValue("document"));
      context.createDocumentDialog(report).show();
//      BirtReport birtReport = new BirtReport(new FileInputStream(new File("/home/andherz/workspace_birt/qualitymaster/milestone.rptdesign")));
//      IDataTableRecord invoice = context.getSelectedRecord();
//      birtReport.setParameterValue("milestonePkey", currentRecord.getSaveStringValue(Milestone.pkey));
//      DataDocumentValue doc  = birtReport.createPDFDocument("Milestone.pdf");
//      context.createDocumentDialog(doc).show();
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
