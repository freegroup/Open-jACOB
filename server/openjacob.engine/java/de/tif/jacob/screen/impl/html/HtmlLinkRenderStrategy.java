/*
 * Created on 27.09.2010
 *
 */
package de.tif.jacob.screen.impl.html;

import java.awt.Color;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILinkLabel;
import de.tif.jacob.screen.ILinkRenderStrategy;

public class HtmlLinkRenderStrategy implements ILinkRenderStrategy
{
  protected static String EVENT_CLICK = "link_click";
  
 
  public String buildLink(IClientContext context, IGuiElement target, String link, String label) throws Exception
  {
    HtmlLinkLabel linkLabel = new HtmlLinkLabel(label);
    this.decorateLink(context, target, link,linkLabel);
    return "<a style=\"text-decoration:none\" href=\"#\" onclick=\"FireEventData('" + Integer.toString(target.getId()) + "', '"+EVENT_CLICK+"','"+link+"');\">"+linkLabel.getHTML()+"</a>";
  }

  public void decorateLink(IClientContext context, IGuiElement target, String link, ILinkLabel label)
  {
    label.setUnderline(true);
    label.setColor(Color.BLUE);
  }
  
}
