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

import java.awt.Rectangle;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.lang.time.DateUtils;
import de.tif.jacob.core.definition.guielements.CalendarDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICalendarAppointment;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.CalendarAppointment;
import de.tif.jacob.screen.event.ICalendarEventHandler;
import de.tif.jacob.screen.impl.CalendarAction;
import de.tif.jacob.screen.impl.CalendarActionAppointmentClick;
import de.tif.jacob.screen.impl.CalendarActionCurrentMonth;
import de.tif.jacob.screen.impl.CalendarActionNextMonth;
import de.tif.jacob.screen.impl.CalendarActionPreviousMonth;
import de.tif.jacob.screen.impl.HTTPCalendar;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Calendar extends GuiHtmlElement implements HTTPCalendar
{

  static public final transient String RCS_ID = "$Id: Calendar.java,v 1.8 2011/07/01 21:16:26 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";

  
  private final CalendarDefinition definition;
  private ICalendarAppointment[] currentAppointments;

  private final Rectangle headerBound;
  private final List headerActions = new ArrayList();
  private final Map actionMap = new HashMap();
  private final CalendarAction appointmentClick = new CalendarActionAppointmentClick();

  private java.util.Date  displayMonth ;
  
  protected Calendar(IApplication app, CalendarDefinition def)
  {
    super(app, def.getName(),def.getName(), def.isVisible(), def.getRectangle(), def.getProperties());
    this.definition = def;
    
    displayMonth = new java.util.Date();
    
    headerBound = new Rectangle(boundingRect.x,boundingRect.y,boundingRect.width,20);
    boundingRect.height -= headerBound.height;
    boundingRect.y      += headerBound.height;
    
    CalendarAction previousMonth = new CalendarActionPreviousMonth();
    actionMap.put(previousMonth.getId(),previousMonth);
    headerActions.add(previousMonth);

    CalendarAction currentMonth = new CalendarActionCurrentMonth();
    actionMap.put(currentMonth.getId(),currentMonth);
    headerActions.add(currentMonth);

    CalendarAction nextMonth = new CalendarActionNextMonth();
    actionMap.put(nextMonth.getId(),nextMonth);
    headerActions.add(nextMonth);

    actionMap.put(appointmentClick.getId(),appointmentClick);
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

    CalendarAction action = (CalendarAction)actionMap.get(event);
    if(action!=null)
      action.execute(context,this,value);
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

      MultiMap day2appointment = new MultiHashMap();
      Map      appointment2FireEvent = new HashMap();
      
      // write the div for the days
      //
      java.util.Calendar cal = java.util.GregorianCalendar.getInstance(context.getLocale());
      cal.setTime(this.displayMonth);
      // den Calendar auf den Anfang des Monats setzen
      // 
      cal.set(java.util.Calendar.DAY_OF_MONTH,1);
      
      // Jetzt den Anfang der Woche holen
      // 
      Iterator calIter = DateUtils.iterator(cal,DateUtils.RANGE_WEEK_MONDAY);
      if(context.getLocale().getLanguage().equals(Locale.ENGLISH))
        calIter = DateUtils.iterator(cal,DateUtils.RANGE_WEEK_SUNDAY);
      java.util.Calendar firstDisplayDayInCalendar = (java.util.Calendar)calIter.next();
      firstDisplayDayInCalendar = DateUtils.truncate(firstDisplayDayInCalendar, java.util.Calendar.DATE);
      
      
      // call the eventhandler if any exists
      // The event handler can override the status (enable/disable) of the textfield
      // or it can calculate the new value if the element a non DB bounded element.
      //
      if(currentAppointments==null)
      {
        java.util.Calendar eventCal = java.util.GregorianCalendar.getInstance(context.getLocale());
        eventCal.setTime(firstDisplayDayInCalendar.getTime());
        Date start = DateUtils.truncate(eventCal.getTime(), java.util.Calendar.DATE);
        eventCal.add(java.util.Calendar.DATE,7*5);
        Date end   = DateUtils.truncate(eventCal.getTime(),java.util.Calendar.DATE);
        ICalendarEventHandler obj = (ICalendarEventHandler)getEventHandler(context);
        if(obj!=null)
          currentAppointments = obj.getAppointments(context, this, start, end);
        if(currentAppointments==null)
          currentAppointments=new CalendarAppointment[0];
        // add the appointsments to the multimap for fast access
        //
        for (int i = 0; i < currentAppointments.length; i++)
        {
          // Das Datum auf den Anfang vom Tag abrunden und in die MultiMap Tag=>Appointment stecken
          appointment2FireEvent.put(currentAppointments[i],"FireEventData('"+this.getId()+"','"+appointmentClick.getId()+"','"+i+"')");
          day2appointment.put(DateUtils.truncate(currentAppointments[i].getDate(),java.util.Calendar.DATE),currentAppointments[i]);
        }
      }


      // The Navigation Header
      //
      DateFormat headerFormat = new SimpleDateFormat("MMMMMMMMM yyyy",context.getLocale()); 
      w.write("\n\n<div class=\"fullcalendar_header\" style=\"");
      w.write(toCSSString(headerBound));
      w.write("\">&nbsp;");
      w.write(headerFormat.format(cal.getTime()));
      // write the navigation actions
      //
      w.write("<div style=\"position:absolute;right:0px;top:0px\">");
      Iterator iter = headerActions.iterator();
      while (iter.hasNext())
      {
        CalendarAction action = (CalendarAction) iter.next();
        w.write("<img onClick=\"FireEvent('");
        w.write(Integer.toString(getId()));
        w.write("','");
        w.write(action.getId());
        w.write("');\" src=\"");
        if(action.getIcon(context)!=null)
          w.write(action.getIcon(context).getPath(true));
        else
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL(action.getIcon()));
        w.write("\">");
      }
      w.write("</div>");
      
      w.write("</div>");
      
      // The Calendar itself
      //
      w.write("\n\n<div id='"+this.getId()+"' class=\"fullcalendar\" style=\"");
      getCSSStyle(context, w,boundingRect);
      w.write("\">\n");

      
      double cellWidth = this.getBoundingRect().width/7;  
      double cellHeight = this.getBoundingRect().height/5;
      DateFormat dateFormat = new SimpleDateFormat("d",context.getLocale()); 
      DateFormat dateNameFormat = new SimpleDateFormat("d EEEEEEEEEEE",context.getLocale()); 
      java.util.Date now = new java.util.Date();
      for(int row=0; row<5;row++)
      {
        for(int column=0;column<7;column++)
        {
          Rectangle rect = new Rectangle((int)(column*cellWidth),(int)(row*cellHeight),(int) cellWidth, (int)cellHeight );
          if(DateUtils.isSameDay(now,firstDisplayDayInCalendar.getTime()))
          {
            w.write("<div class=\"fullcalendar_day_today\" style=\"");
            w.write(toCSSString(rect));
            w.write("\"><div class=\"fullcalendar_day_today_title\">");
          }
          else
          {
            w.write("<div class=\"fullcalendar_day\" style=\"");
            w.write(toCSSString(rect));
            w.write("\"><div class=\"fullcalendar_day_title\">");
          }
          if(row==0)
            w.write(dateNameFormat.format(firstDisplayDayInCalendar.getTime()));
          else
            w.write(dateFormat.format(firstDisplayDayInCalendar.getTime()));
          w.write("</div>");
          Collection appointments = (Collection) day2appointment.get(firstDisplayDayInCalendar.getTime());
          w.write("<div class=\"fullcalendar_day_appointment_container\" >\n");
          if(appointments!=null)
          {
            Iterator appIter = appointments.iterator();
            while (appIter.hasNext())
            {
              ICalendarAppointment appointment = (ICalendarAppointment) appIter.next();
              if(appointment.getBackgroundColor()==null)
                w.write("<div style=\"padding-left:18px;background:"+toCSSString(appointment.getBackgroundColor())+" url(");
              else
                w.write("<div style=\"padding-left:18px;background:transparent url(");
              w.write(appointment.getIcon().getPath(true));
              w.write(") no-repeat;\" class=\"fullcalendar_day_appointment\" onclick=\"");
              w.write((String)appointment2FireEvent.get(appointment));
              w.write("\" >\n");
              w.write(appointment.getLabel());
              w.write("</div>\n");
            }
          }
          w.write("</div>\n"); //_end appointment_container
          w.write("</div>\n"); //_end day_container
          firstDisplayDayInCalendar.add(java.util.Calendar.DATE,1);
        }
      }
      w.write("</div>\n");
    }
  }


  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    context.addOnLoadJavascript("var calendar_"+this.getId()+" = new jACOBCalendar('"+this.getId()+"');");
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
//    Object obj = getEventHandler(context);
//    if(obj!=null)
//    {
//      if(obj instanceof ITimeLineEventHandler)
//        ((ITimeLineEventHandler)obj).onGroupStatusChanged(context,newGroupDataStatus, this);
//      else
//        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+ITimeLineEventHandler.class.getName()+"]");
//    }
  }

  public void refresh()
  {
    this.currentAppointments=null;
    resetCache();
  }
  
  public void setDisplayMonth(java.util.Date date)
  {
    this.displayMonth = date;
    refresh();
  }

  public java.util.Date getDisplayMonth()
  {
    return this.displayMonth;
  }
  
  public ICalendarAppointment[] getCurrentAppointments()
  {
    return this.currentAppointments;
  }
}
