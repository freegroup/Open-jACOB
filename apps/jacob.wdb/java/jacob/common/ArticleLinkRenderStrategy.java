package jacob.common;

import jacob.entrypoint.gui.ShowArticle;

import java.awt.Color;
import java.util.Properties;

import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILinkLabel;
import de.tif.jacob.screen.impl.html.HtmlLinkRenderStrategy;

public class ArticleLinkRenderStrategy extends HtmlLinkRenderStrategy
{

  @Override
  public void decorateLink(IClientContext context, IGuiElement target, String link, ILinkLabel label)
  {
    if(link.indexOf("tp")!=-1)
    {
      label.setColor(Color.blue);
      label.setUnderline(true);
    }
    else
    {
      try
      {
        if(ArticleUtil.exists(context, link))
        {
          label.setColor(Color.blue);
          label.setUnderline(true);
        }
        else
        {
          label.setColor(Color.red);
          label.setStrike(true);
        }
      }
      catch (Exception e)
      {
        ExceptionHandler.handle(e);
      }
    }
  }
  
}
