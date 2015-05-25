/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jun 10 16:12:50 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;


import jacob.common.htmleditor.AbstractHTMLEditorButton;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 */
public class Storage_email_outboundStaticImage_AlignCenter extends AbstractHTMLEditorButton
{
  @Override
  public String getStartTag()
  {
    return "<div style=\"text-align: center;\">";
  }
  
  @Override
  public String getEndTag()
  {
    return "</div>";
  }
}

