/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jun 10 17:34:07 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;

import jacob.common.htmleditor.AbstractHTMLEditorButton;

public class Storage_email_outboundStaticImage_AlignRight extends AbstractHTMLEditorButton
{
  @Override
  public String getStartTag()
  {
    return "<div style=\"text-align: right;\">";
  }

  @Override
  public String getEndTag()
  {
    return "</div>";
  }
}
