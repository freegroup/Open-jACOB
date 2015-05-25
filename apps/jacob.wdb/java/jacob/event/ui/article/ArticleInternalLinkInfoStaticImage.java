/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 28 12:44:36 CEST 2010
 */
package jacob.event.ui.article;


import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class ArticleInternalLinkInfoStaticImage extends IStaticImageEventHandler implements IOnClickEventHandler
{

  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    IUrlDialog dialog = context.createUrlDialog(context.getApplication(), "help/article.html");
    dialog.enableNavigation(false);
    dialog.enableScrollbar(true);
    dialog.show(650, 400);
  }

}

