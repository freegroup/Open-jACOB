package jacob.event.ui.{modulename};

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.report.ReportNotifyee;
import de.tif.jacob.report.impl.DatabaseReport;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IStyledText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class ReportPreviewGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: ReportPreviewGroup.java,v 1.2 2010/03/01 09:31:24 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
    IStyledText text = (IStyledText)group.findByName("reportPreviewStyledText");
    text.setLabel(ReportProvider.getPreview(context));
	}

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
	
}
