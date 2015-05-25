package jacob.event.ui.{modulename};


import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.report.ReportNotifyee;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author 
 */
 public class ReportConfigureGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: ReportConfigureGroup.java,v 1.2 2010/03/01 09:31:24 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
    Report report = ReportProvider.get(context);
    ReportNotifyee n = ReportProvider.getNotifyee(context);
    if(n==null)
      return;

    IMutableComboBox comboBox = (IMutableComboBox)context.getGroup().findByName("reportFormatCombobox");
    comboBox.removeOptions();
    comboBox.addOption("application/excel");
    comboBox.addOption("text/plain");
    if(report.getLayout_01()!=null)
      comboBox.addOption("text/formatted");
    
    group.setInputFieldValue("reportEmailText",n.getAddress());
    group.setInputFieldValue("reportFormatCombobox",n.getMimeType());
    group.setInputFieldValue("reportHourCombobox",""+n.getHour());
    group.setInputFieldValue("reportMinuteCombobox",""+n.getMinute());
    group.setInputFieldValue("reportMondayCheckbox",getCheckForDay(n,java.util.Calendar.MONDAY));
    group.setInputFieldValue("reportTuesdayCheckbox",getCheckForDay(n,java.util.Calendar.TUESDAY));
    group.setInputFieldValue("reportWednesdayCheckbox",getCheckForDay(n,java.util.Calendar.WEDNESDAY));
    group.setInputFieldValue("reportThursdayCheckbox",getCheckForDay(n,java.util.Calendar.THURSDAY));
    group.setInputFieldValue("reportFridayCheckbox",getCheckForDay(n,java.util.Calendar.FRIDAY));
    group.setInputFieldValue("reportSaturdayCheckbox",getCheckForDay(n,java.util.Calendar.SATURDAY));
    group.setInputFieldValue("reportSundayCheckbox",getCheckForDay(n,java.util.Calendar.SUNDAY));
	}

  /**
   * Will be called, if there is a state change from visible=>hidden.
   * 
   * This happens, if the user switches the Domain or Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onHide(IClientContext context, IGroup group) throws Exception 
  {
    // insert your code here
  }
  
  /**
   * Will be called, if there is a state change from hidden=>visible.
   * 
   * This happens, if the user switches to a Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onShow(IClientContext context, IGroup group) throws Exception 
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    if(currentRecord==null)
      return;
    
    // Falls der Report im LayoutEditor gespeichert wurde haben wir jetzt
    // hier einen "veralteten" record => reload
    group.clear(context,false);
    currentRecord = context.getDataTable().loadRecord(currentRecord.getPrimaryKeyValue());
    context.getDataTable().setSelectedRecord(currentRecord.getPrimaryKeyValue());
  }
  
  private String getCheckForDay(ReportNotifyee rn, int day)
  {
    for(int i=0;i<rn.getDays().length;i++)
    {
      if(rn.getDays()[i]==day)
        return "1";
    }
    return "0";
  }
}

