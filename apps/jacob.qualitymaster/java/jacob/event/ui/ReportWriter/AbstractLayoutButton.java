package jacob.event.ui.ReportWriter;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.report.impl.DatabaseReport;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.report.impl.castor.CastorLayout;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

public class AbstractLayoutButton extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: AbstractLayoutButton.java,v 1.1 2009-12-24 10:02:21 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // NOTE: The same GUID is in the index.jsp of the ReportWriter!!!!!
  static String REPORT_GUID = "150e1f3c-a7da-4874-ae5b-f75e69e3499a";
  static String FORM_GUID = "24396e1c-a46b-4d19-8500-d15855bae159";
  
  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    // Falls der Report im LayoutEditor gespeichert wurde haben wir jetzt
    // hier einen "veralteten" record => reload
    currentRecord = context.getDataTable().loadRecord(currentRecord.getPrimaryKeyValue());
    context.getDataTable().setSelectedRecord(currentRecord.getPrimaryKeyValue());
    
    DatabaseReport report = new DatabaseReport(currentRecord);
    CastorLayout layout = report.getLayout_01();
    if(layout==null)
    {
      layout=report.createLayout_01();
      report.getLayouts().addLayout(layout);
    }

    context.getForm().setVisible(false);
    context.getDomain().findByName("ReportWriter_layout").setVisible(true);

    context.setPropertyForWindow(REPORT_GUID, report);
    context.setPropertyForWindow(FORM_GUID, context.getForm().getName());

    context.setCurrentForm("ReportWriter_layout");
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    Report report = ReportProvider.get(context);
    element.setEnable(false);
    element.setLabel("Layout");
    if(report!=null)
    {
      if(report.getLayout_01()==null)
        element.setLabel("Create Layout");
      else
        element.setLabel("Edit Layout");
      
      // Ein Report welcher nicht dem eingeloggten Anwender gehrt ist nicht editierbar
      element.setEnable(context.getUser().getLoginId().equals(report.getOwnerId()));
    }

  }

}
