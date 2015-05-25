package jacob.event.ui.{modulename};

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.report.ReportNotifyee;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.security.IUser;

public class AbstractUpdateButton extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: AbstractUpdateButton.java,v 1.1 2009/12/14 23:18:52 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This event handler will be called, if the corresponding button has been pressed.
   * You can prevent the execution of the UPDATE action if you return <code>false</code>.<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();

    if (context.getGroup().getDataStatus()== IGuiElement.SELECTED)
    {
      // The record will be toggled from IGuiElement.SELECTED => IGuiElement.UPDATE
      // (return false to prevent this)
    }
    else
    {
      // The record will be toggled from IGuiElement.UPDATE => IGuiElement.SELECTED
      Report report = ReportProvider.get(context);
      ReportNotifyee n = ReportProvider.getNotifyee(context);
      String mimeType = context.getGroup().getInputFieldValue("reportFormatCombobox");
      String to = context.getGroup().getInputFieldValue("reportEmailText");
      int hour = 0;
      int minutes=0;
      try {hour = Integer.parseInt( context.getGroup().getInputFieldValue("reportHourCombobox"));}catch(Exception exc){};
      try {minutes = Integer.parseInt( context.getGroup().getInputFieldValue("reportMinuteCombobox"));}catch(Exception exc){};
      
      List<Integer> days = new ArrayList<Integer>();
      
      if(((ICheckBox)context.getGroup().findByName("reportMondayCheckbox")).isChecked()) days.add(java.util.Calendar.MONDAY);
      if(((ICheckBox)context.getGroup().findByName("reportTuesdayCheckbox")).isChecked()) days.add(java.util.Calendar.TUESDAY);
      if(((ICheckBox)context.getGroup().findByName("reportWednesdayCheckbox")).isChecked()) days.add(java.util.Calendar.WEDNESDAY);
      if(((ICheckBox)context.getGroup().findByName("reportThursdayCheckbox")).isChecked()) days.add(java.util.Calendar.THURSDAY);
      if(((ICheckBox)context.getGroup().findByName("reportFridayCheckbox")).isChecked()) days.add(java.util.Calendar.FRIDAY);
      if(((ICheckBox)context.getGroup().findByName("reportSaturdayCheckbox")).isChecked()) days.add(java.util.Calendar.SATURDAY);
      if(((ICheckBox)context.getGroup().findByName("reportSundayCheckbox")).isChecked()) days.add(java.util.Calendar.SUNDAY);

      IUser user= context.getUser();
      if(days.size()==0)
      {
        report.deleteReportNotifyee(user);
      }
      else
      {
        int[] selectedDays = new int[days.size()];
        for(int i=0;i<days.size();i++) selectedDays[i]=days.get(i).intValue();
        
        ReportNotifyee noti = new ReportNotifyee(user,to , n.getProtocol(), mimeType, minutes, hour, selectedDays, n.isOmitEmpty());
        report.setReportNotifyee(noti);
      }
      
      currentRecord.setValue(currentRecord.getCurrentTransaction(), jacob.model.Report.definition,report.toXmlFormatted());
    }
    return true;
  }

  /**
   * This event method will be called, if the UPDATE action has been successfully executed.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button) 
  {
  }
}