/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IHtmlGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;

public class IHtmlGroupEventHandler extends IGroupEventHandler
{

  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGroup group) throws Exception
  {
    // redirect to more confortable function signature
    // => No casting required for the application programmer
    //
    onGroupStatusChanged(context, state, (IHtmlGroup)group);
  }


  /**
   * Return the required Inlcude files for the HtmlGroup.
   * The file path must be relative to the web application directory
   * of the jACOB app.<br>
   * <br>
   * e.g. <b>myLib.js</b> will be expanded to <b>http://&lt;HOST&gt;/jacob/application/test/1.2/myLib.js</b>
   * @since 2.8.4
   */
  public String[] getRequiredIncludeFiles()
  {
    return new String[0];
  }
  
  public final void onHide(IClientContext context, IGroup group) throws Exception
  {
    // redirect to more confortable function signature
    // => No casting required for the application programmer
    //
    onHide(context, (IHtmlGroup)group);
  }

  public final void onShow(IClientContext context, IGroup group) throws Exception
  {
    // redirect to more confortable function signature
    // => No casting required for the application programmer
    //
    onShow(context, (IHtmlGroup)group);
  }


  public void onGroupStatusChanged(IClientContext context, GroupState state, IHtmlGroup group) throws Exception
  {
    // do nothing per default
  }

  public void onClick(IClientContext context, IHtmlGroup group, String link) throws Exception
  {
    // do nothing per default
  }
  
  public void onHide(IClientContext context, IHtmlGroup group) throws Exception
  {
    // do nothing per default
  }

  public void onShow(IClientContext context, IHtmlGroup group) throws Exception
  {
    // do nothing per default
  }



  public byte[] getImageData(IClientContext context, IHtmlGroup group, String imageId)
  {
    return null;
  }
  
}
