package jacob.common;

import jacob.entrypoint.gui.ShowArticle;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.event.ILinkEventListener;

public class ArticleLinkEventListener implements ILinkEventListener
{
    public void onClick(IClientContext context, IGuiElement element, String link) throws Exception
    {
      if(link.indexOf("tp")!=-1)
      {
        IUrlDialog dialog = context.createUrlDialog(StringUtils.trim(link));
        dialog.enableNavigation(true);
        dialog.enableScrollbar(true);
        dialog.show(750, 500);
      }
      else
      {
        Properties props = new Properties();
        props.put("pkey", link);
        EntryPointUrl.popup(context, new ShowArticle(), props, 850,550);
      }
    }
}
