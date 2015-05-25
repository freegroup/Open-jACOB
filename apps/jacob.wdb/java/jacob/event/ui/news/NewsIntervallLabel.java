/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 06 15:36:46 CEST 2010
 */
package jacob.event.ui.news;

import jacob.model.News;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ILabelEventHandler;
import de.tif.jacob.util.DatetimeUtil;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class NewsIntervallLabel extends ILabelEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: NewsIntervallLabel.java,v 1.1 2010-08-06 16:00:27 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  /**
   * Will be called, if the user selects a record or presses the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    IDataTableRecord record = context.getSelectedRecord();
    if(record !=null)
    {
      Date start = record.getDateValue(News.intervall_start);
      Date end   = record.getDateValue(News.intervall_end);
      Date now = new Date();
      boolean renew = record.getSaveStringValue(News.renew).equals(News.renew_ENUM._year);
      
      if(start==null && end ==null)
      {
        label.setLabel("kein Zeitlimit");
      }
      else if(start==null && end!=null)
      {
        label.setLabel("Bis zum "+DatetimeUtil.convertDateToString(end, context.getLocale()));
      }
      else if(start!=null && end==null)
      {
        label.setLabel("Ab dem "+DatetimeUtil.convertDateToString(start, context.getLocale()));
      }
      else if(start!=null && end!=null)
      {
        if(renew)
        {
          SimpleDateFormat format = new SimpleDateFormat("dd.MM");
          label.setLabel("Jedes Jahr vom "+format.format(start)+" bis "+format.format(end));
        }
        else
        {
          SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
          label.setLabel(format.format(start)+" bis "+format.format(end));
        }
      }
    }
    else
    {
      label.setLabel("-");
    }
    // your code here...
  }

}
