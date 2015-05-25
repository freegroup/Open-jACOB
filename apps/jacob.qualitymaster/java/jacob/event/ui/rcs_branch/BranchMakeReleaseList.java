/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 25 12:48:53 CEST 2006
 */
package jacob.event.ui.rcs_branch;

import jacob.model.Rcs_branch;
import jacob.model.Rcs_project;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.report.birt.BirtReport;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the BranchMakeReleaseList record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class BranchMakeReleaseList extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: BranchMakeReleaseList.java,v 1.2 2006/08/29 15:34:14 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

//  public void onAction(IClientContext context, IGuiElement button) throws Exception
//  {
//    IDataTableRecord releaseRecord = context.getSelectedRecord();
//
//    InputStream in = BranchMakeReleaseList.class.getResourceAsStream("release.rptdesign");
//    try
//    {
//      IRunAndRenderTask task = ReportManager.createRunAndRenderTask(in);
//
//      PDFRenderContext renderContext = new PDFRenderContext();
//      HashMap contextMap = new HashMap();
//      contextMap.put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, renderContext);
//      task.setAppContext(contextMap);
//
//      String releaseVersion = releaseRecord.getStringValue(Rcs_branch.version);
//      task.setParameterValue("releaseNbr", releaseVersion);
//      task.setParameterValue("projectName", releaseRecord.getLinkedRecord(Rcs_project.NAME).getStringValue(Rcs_project.display_name));
//      task.setParameterValue("rcsProjectKey", releaseRecord.getStringValue(Rcs_branch.rcs_project_key));
//      task.setParameterValue("rcsVersionNbr", releaseRecord.getStringValue(Rcs_branch.versionnbr));
//
//      ByteArrayOutputStream out = new ByteArrayOutputStream();
//      try
//      {
//        RenderOptionBase options = new RenderOptionBase();
//        options.setOutputStream(out);
//        options.setOutputFormat(RenderOptionBase.OUTPUT_FORMAT_PDF);
//        task.setRenderOption(options);
//
//        task.run();
//
//        context.createDocumentDialog(null, "release" + StringUtils.replace(releaseVersion, ".", "_") + ".pdf", out.toByteArray()).show();
//      }
//      finally
//      {
//        out.close();
//      }
//    }
//    finally
//    {
//      in.close();
//    }
//  }
   
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord releaseRecord = context.getSelectedRecord();

    BirtReport birtReport = new BirtReport(BranchMakeReleaseList.class.getResourceAsStream("release.rptdesign"));

    String releaseVersion = releaseRecord.getStringValue(Rcs_branch.version);
    birtReport.setParameterValue("releaseNbr", releaseVersion);
    birtReport.setParameterValue("projectName", releaseRecord.getLinkedRecord(Rcs_project.NAME).getStringValue(Rcs_project.display_name));
    birtReport.setParameterValue("rcsProjectKey", releaseRecord.getStringValue(Rcs_branch.rcs_project_key));
    birtReport.setParameterValue("rcsVersionNbr", releaseRecord.getStringValue(Rcs_branch.versionnbr));

    String documentName = "release" + StringUtils.replace(releaseVersion, ".", "_") + ".pdf";
    context.createDocumentDialog(birtReport.createPDFDocument(documentName)).show();
  }
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
    if (status == IGuiElement.SELECTED)
    {
      IDataTableRecord record = context.getSelectedRecord();
      button.setEnable(Rcs_branch.status_ENUM._Release.equals(record.getValue(Rcs_branch.status)));
    }
    else
      button.setEnable(false);
	}
}

