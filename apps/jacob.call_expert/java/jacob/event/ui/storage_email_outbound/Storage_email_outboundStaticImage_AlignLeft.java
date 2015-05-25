/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jun 10 17:33:43 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;
import jacob.common.htmleditor.AbstractHTMLEditorButton;

public class Storage_email_outboundStaticImage_AlignLeft extends AbstractHTMLEditorButton
{
  @Override
  public String getStartTag()
  {
    return "<div style=\"text-align: left;\">";
  }

  @Override
  public String getEndTag()
  {
    return "</div>";
  }
}
