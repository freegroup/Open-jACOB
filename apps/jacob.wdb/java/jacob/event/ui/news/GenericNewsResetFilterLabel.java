/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 30 13:55:27 CEST 2010
 */
package jacob.event.ui.news;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ILabelEventHandler;
import de.tif.jacob.screen.event.IOnClickEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class GenericNewsResetFilterLabel extends ILabelEventHandler  implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: GenericNewsResetFilterLabel.java,v 1.1 2010-08-03 13:54:19 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    IDataBrowser browser = context.getDataBrowser();
    
    context.getDataAccessor().qbeClearAll();
    browser.search(IRelationSet.DEFAULT_NAME, Filldirection.BOTH);
    if(browser.recordCount()>0)
    {
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
    }
    context.getGroup().findByName("newsResetFilterLabel1").setVisible(false);
    context.getGroup().findByName("newsResetFilterLabel2").setVisible(false);
    context.getGroup().findByName("newsResetFilterLabel3").setVisible(false);
    context.getGroup().findByName("newsResetFilterLabel4").setVisible(false);
    
    context.showTransparentMessage("Filter / Suchkriterien zurückgesetzt");
  }

  
}
