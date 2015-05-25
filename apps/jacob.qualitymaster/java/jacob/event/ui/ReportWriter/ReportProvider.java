package jacob.event.ui.ReportWriter;


import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.report.ReportNotifyee;
import de.tif.jacob.report.impl.DatabaseReport;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.screen.IClientContext;

public class ReportProvider
{
  private static Set<String> supportedPreview;
  static
  {
    supportedPreview = new HashSet<String>();
    supportedPreview.add("text/plain");
    supportedPreview.add("text/formatted");
  }
  
  public static Report get(IClientContext context) throws Exception
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    
    if(currentRecord ==null)
      return null;
    Report report = (Report)context.getProperty(currentRecord);
    if(report==null)
      context.setPropertyForRequest(currentRecord, report=new DatabaseReport(currentRecord));
    
    return report;
  }
  
  public static ReportNotifyee getNotifyee(IClientContext context) throws Exception
  {
    Report report = get(context);
    if(report==null)
      return null;
    
    ReportNotifyee n = report.getReportNotifyee(context.getUser());
    if(n==null)
    {
      String email = context.getUser().getEMail();
      report.setReportNotifyee(n=new ReportNotifyee(context.getUser(),
                                                  email,
                                                  "email://",
                                                  "text/plain",
                                                  0,
                                                  0,
                                                  new int[0],
                                                  false));
    }
    return n;
  }
  
  public static String getPreview(IClientContext context) throws Exception
  {
    Report report = get(context);
    if(report !=null)
    {
      String header="";      
      String content="";
      String mimeType = context.getGroup().getInputFieldValue("reportPreviewFormatCombobox");
      
      if(supportedPreview.contains(mimeType))
      {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        report.render(out, mimeType);
        if(report.getLayout_01()!=null && "text/formatted".equals(mimeType))
          header="Preview generated with user defined report layout";
        else
          header="Preview generated with standard report text layout";
        
        content=out.toString();
      }
      else
      {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        report.render(out,"text/plain");
        
        content = out.toString();
        header = "No preview for Excel output available. Using standard text layout";
      }
      
      StringBuffer sb = new StringBuffer();
      sb.append("<div style='border-top:1px solid gray;border-bottom:1px solid gray;white-space:nowrap'>");
      sb.append(header);
      sb.append("</div><br><br><br><pre>");
      sb.append(content);
      sb.append("</pre>");
      return sb.toString();
    } 
    return "";
  }
}
