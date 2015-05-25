/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 06 17:45:32 CEST 2010
 */
package jacob.event.ui.news;

import java.awt.Color;

import jacob.model.News;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class NewsStatus extends ILabelEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: NewsStatus.java,v 1.1 2010-08-06 16:00:27 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  /**
   * Will be called, if the user selects a record or presses the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
   if(state==IGroup.SELECTED)
   {
     if(context.getSelectedRecord().getSaveStringValue(News.status).equals(News.status_ENUM._active))
       label.setColor(Color.green.darker().darker());
     else
       label.setColor(Color.red);
   }
  }

}
