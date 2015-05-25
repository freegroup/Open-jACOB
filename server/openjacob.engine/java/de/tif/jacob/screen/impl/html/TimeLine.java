/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.screen.impl.html;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang.time.DateUtils;

import de.tif.jacob.core.definition.guielements.TimeLineDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITimeLine;
import de.tif.jacob.screen.event.ITimeLineEventHandler;
import de.tif.jacob.screen.event.TimeLineSlot;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TimeLine extends GuiHtmlElement implements ITimeLine
{
  static public final transient String RCS_ID = "$Id: TimeLine.java,v 1.9 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

  
  protected long currentScrollPosX = 0;
  protected long currentScrollPosY = 0;
  protected TimeLineSlot[] currentTimeSlots = new TimeLineSlot[0];
  
  private final TimeLineDefinition definition;
  
  protected TimeLine(IApplication app, TimeLineDefinition definition)
  {
    super(app, definition.getName(),definition.getName(), definition.isVisible(), definition.getRectangle(), definition.getProperties());
    this.definition = definition;
  }

  /**
   * The default implementation transfer the data from the event to the GUI guid.
   * (this is not usefull for check boxes!)
   * 
   */
  public boolean processParameter( int guid, String data) throws IOException, NoSuchFieldException
  {
    if(guid==this.getId())
    {
      String[] values = data.split(":");
      currentScrollPosX = Long.parseLong(values[0]);
      currentScrollPosY = Long.parseLong(values[1]);
      return true;
    }
    // the super-implementation trys to find the corresponding child
    // and sends the event to them
    return super.processParameter( guid, data); 
  }

  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    // This is not an event for this guiBrowser
    // and a generic guiBrowser has no childs -> return with no action
    //
    if(guid!=this.getId())
      return false;

    ITimeLineEventHandler obj = (ITimeLineEventHandler)getEventHandler(context);
    if(obj!=null)
      obj.onClick(context,this, this.currentTimeSlots[Integer.parseInt(value)]);
    return true;
  }

  /**
   * return the HTML representation of this object
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    if(getCache()==null)
    {
      Writer w = newCache();

      w.write("\n\n<div id='"+this.getId()+"' class=\"timeline\" style=\"");
      getCSSStyle(context, w,boundingRect);
      w.write("\">\n");
      
      // Tabelle mit den slots rausschreiben
      //
      
      // 1. calculate the first and laste date
      SortedSet dates  = new TreeSet();
      for (int i = 0; i < currentTimeSlots.length; i++)
      {
        dates.add(currentTimeSlots[i].getStartDate());
        dates.add(currentTimeSlots[i].getEndDate());
      }
      Date startDate = new Date();//(Date)dates.first();
      Date endDate =  new Date(startDate.getTime()+1000*60*60*24*8);//+8 Tage (Date)dates.last();
      if(dates.size()>0)
      {
        startDate = (Date)dates.first();
        endDate =   (Date)dates.last();
      }
      // Find the first Monday before the the "firstDate"
      Iterator iter = DateUtils.iterator(endDate, DateUtils.RANGE_WEEK_MONDAY);
      Calendar cal = null;
      while (iter.hasNext())
        cal= (Calendar) iter.next();
      Date monday = ((Calendar)DateUtils.iterator(startDate, DateUtils.RANGE_WEEK_MONDAY).next()).getTime();
      Date friday = cal.getTime();
      
      // Anzahl Tage muss immer ein Vielfaches von 7 sein
      // n*(Montag..Sonntag)
      //
      long days = (long)(1+((double)friday.getTime()-monday.getTime())/((double)1000*60*60*24));

      // Columns = Anzahl der Tage+1
      long columns = days+1;
      long rows    = currentTimeSlots.length;
      
      w.write("\n\n<table cellpadding=\"0\" cellspacing=\"0\">\n");
      w.write("<thead>\n");
      // Monate rausschreiben
      //
      w.write("<tr>");
      // erste Column für das Label
      w.write("<th class='timeline_header_month'>&nbsp;</th>");
      DateFormat monthFormat= new SimpleDateFormat("MMMMMMMMMMMM yy",context.getLocale());
      Calendar calendar = GregorianCalendar.getInstance(context.getLocale());
      calendar.setTime(monday);
      int colspan=1;
      for(int i=0;i< columns;i++)
      {
        // Tag soweit erhöhen bis wir in dem nächsten Monat sind
        //
        Date current= calendar.getTime();        
        calendar.add(Calendar.DATE,1);
        Date next = calendar.getTime();
        while(current.getMonth()==next.getMonth() && i<(columns-1))
        {
          colspan++;
          i++;
          calendar.add(Calendar.DATE,1);
          next = calendar.getTime();
        }
        w.write("<th colspan=\""+colspan+"\" class='timeline_header_month'>");
        w.write(monthFormat.format(current));
        w.write("</th>");
        colspan=1;
      }
      w.write("</tr>\n");
      
      // Tage rausschreiben
      //
      w.write("<tr>");
      // erste Column für das Label
      w.write("<th class='timeline_header_day'>&nbsp;</th>");
      DateFormat format=new SimpleDateFormat("dd",context.getLocale());
      calendar.setTime(monday);
      for(int i=0;i< columns;i++)
      {
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
          w.write("<th class='timeline_header_day_saturday'>");
        else if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
          w.write("<th class='timeline_header_day_sunday'>");
        else
          w.write("<th class='timeline_header_day_weekday'>");
        w.write(format.format(calendar.getTime()));
        w.write("</th>");
        calendar.add(Calendar.DATE,1);
      }
      w.write("</tr>\n");
      w.write("</thead>\n");
      w.write("<tr>");
      for (int j = 0; j < (columns+1); j++)
      {
        w.write("<td >");
        w.write("&nbsp;");
        w.write("</td>");
      }
      w.write("</tr>\n");

      // Über alle Slots
      String hashCode = Integer.toString(getId());
      DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT, context.getLocale());
      for (int i = 0; i < rows; i++)
      {
        TimeLineSlot slot = currentTimeSlots[i];
        Date start = slot.getStartDate();
        Date end = slot.getEndDate();
        long slotDays = (long)(1+((double)end.getTime()-start.getTime())/((double)1000*60*60*24));
        Color color = slot.getColor();
        w.write("<tr class=\"timeline_row\">");
        if(color!=null)
          w.write("<td title=\""+slot.getTooltip()+"\" style=\"background-color:"+toCSSString(color)+";white-space:nowrap;\">");
        else
          w.write("<td title=\""+slot.getTooltip()+"\" style=\"white-space:nowrap;\">");
        w.write(StringUtil.htmlEncode(slot.getLabel(),"<br>"));
        w.write("</td>");
        // über alle Tage
        calendar.setTime(monday);
        boolean spanning=false;
        for (int j = 0; j < (columns-slotDays+1); j++)
        {
          if(spanning==false && DateUtils.isSameDay(calendar.getTime(),start))
          {
            w.write("<td colspan=\""+(1+slotDays)+"\" class='timeline_cell_spanning'");
            w.write(" style=\"background:transparent url(./themes/gantt_"+slot.getColorName()+".png) repeat-x top left\"");
            w.write(" title=\"");
            w.write(dateTimeFormat.format(start));
            w.write(" - ");
            w.write(dateTimeFormat.format(end));
            w.write("\"");
            w.write(" onClick=\"FireEventData('");
            w.write(hashCode);
            w.write("','click','");
            w.write(Integer.toString(i));
            w.write("')\">");
            w.write("</td>");
            spanning=true;
          }

          if(spanning==false)
          {
            w.write("<td>&nbsp;</td>");
          }
          // spanning wird zurück gesetzt wenn wir das endedatum erreicht haben
          if(spanning==true)
           spanning = DateUtils.isSameDay(calendar.getTime(),end);
          calendar.add(Calendar.DATE,1);
        }
        w.write("</tr>\n");
      }
      w.write("</table>\n");
      w.write("</div>\n");
    }
  }


  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    context.addOnLoadJavascript("var timeline_"+this.getId()+" = new jACOBTimeline('"+this.getId()+"','"+getEtrHashCode()+"');");
    // Die Scrollposition einer TimeLine kann sich unabhängig von seinem Inhalt ändern.
    // Man kann somit den String für das setzten der HiddenVarialbe nicht in den Cache des
    // BrowserContent schreiben. Dieser wird ja nur berechnet wenn sich der Inhalt der Timeline
    // wirklich ändert.
    //
    w.write("<input type=\"hidden\" name=\""+getEtrHashCode()+"\"  id=\""+getEtrHashCode()+"\" value=\""+currentScrollPosX+":"+currentScrollPosY+"\">\n");
    writeCache(w);
  }
  
  /**
   * a TimeLine has no searchable data fields......
   */
  protected void addDataFields(Vector fields)
  {
  }
  

	public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }

  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    super.onGroupDataStatusChanged(context, newGroupDataStatus);

    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the textfield
    // or it can calculate the new value if the element a non DB bounded element.
    //
    Object obj = getEventHandler(context);
    if(obj!=null)
    {
      if(obj instanceof ITimeLineEventHandler)
        ((ITimeLineEventHandler)obj).onGroupStatusChanged(context,newGroupDataStatus, this);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+ITimeLineEventHandler.class.getName()+"]");
    }
  }

  public TimeLineSlot[] getSlots(IClientContext context)
  {
    return this.currentTimeSlots;
  }

  public void setSlots(IClientContext context, TimeLineSlot[] slots)
  {
    this.currentTimeSlots = slots;
    if(this.currentTimeSlots == null)
      this.currentTimeSlots = new TimeLineSlot[0];
    resetCache();
  }
}
