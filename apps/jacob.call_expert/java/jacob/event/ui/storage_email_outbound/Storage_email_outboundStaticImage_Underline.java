/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jun 10 14:26:51 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;
import jacob.common.htmleditor.AbstractHTMLEditorButton;


public class Storage_email_outboundStaticImage_Underline extends AbstractHTMLEditorButton
{
  @Override
  public String getStartTag()
  {
    return "<u>";
  }

  @Override
  public String getEndTag()
  {
    return "</u>";
  }
}
