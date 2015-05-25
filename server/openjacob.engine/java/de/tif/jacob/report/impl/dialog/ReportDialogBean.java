/*
 * Created on 09.03.2010
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.report.impl.dialog;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.report.IReport;
import de.tif.jacob.report.ReportManager;
import de.tif.jacob.report.ReportNotifyee;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.scheduler.iterators.CronEntry;
import de.tif.jacob.scheduler.iterators.CronIterator;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.security.IUser;
import de.tif.jacob.util.DatetimeUtil;
import de.tif.jacob.util.StringUtil;

/**
 * Bean for report dialog handling.
 * 
 * @author Andreas Sonntag
 */
public class ReportDialogBean
{
  static public final transient String RCS_ID = "$Id: ReportDialogBean.java,v 1.3 2010/05/24 23:00:15 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  public static abstract class ScheduleMode
  {
    private final String name;

    private ScheduleMode(String name)
    {
      this.name = name;
      ScheduleModes.put(name, this);
    }

    public String toString()
    {
      return this.name;
    }
  }

  public static final Map ScheduleModes = new HashMap();
  public static final ScheduleMode NONE = new ScheduleMode("none")
  {
  };
  public static final ScheduleMode STANDARD = new ScheduleMode("standard")
  {
  };
  public static final ScheduleMode EXPERT = new ScheduleMode("expert")
  {
  };

  private static final int PREVIEW_SIZE = 4;
  private static final String BEAN_PROPERTY = ReportDialogBean.class.getName() + "#Bean";
  private static final String ERROR_PROPERTY = ReportDialogBean.class.getName() + "#Error";

  public static ReportDialogBean getBean(IClientContext context, IReport report) throws Exception
  {
    return new ReportDialogBean(context, report);
  }
  
  public static void reset(IClientContext context)
  {
    // HACK: Do reset possibliy cached report definition which might already been changed by means of Edit (new search constrains)
    // See below : FREEGROUP: setPropertyForRequest does not work here
    context.setPropertyForWindow(BEAN_PROPERTY, null);
  }

  public static ReportDialogBean getBean(IClientContext context, String reportId) throws Exception
  {
    ReportDialogBean bean = (ReportDialogBean) context.getProperty(BEAN_PROPERTY);
    if (bean == null || !bean.getReport().getGUID().equals(reportId))
    {
      IReport report = ReportManager.getReport(reportId);
      if (report != null)
      {
        // FREEGROUP: setPropertyForRequest does not work here
        context.setPropertyForWindow(BEAN_PROPERTY, bean = new ReportDialogBean(context, report));
      }
    }
    return bean;
  }
  
  public static String getError(IClientContext context)
  {
    return (String) context.getProperty(ERROR_PROPERTY);
  }

  private final IReport report;
  private IDataBrowser previewReportData;
  private boolean hasSave;

  private ScheduleMode scheduleMode;

  private String address;
  private String protocol;
  private String mimeType;
  private boolean omitEmpty;

  private int minute;
  private int hour;

  private boolean everyMinute = true;
  private boolean everyHour = true;
  private boolean everyDayOfMonth = true;
  private boolean everyMonth = true;
  private boolean everyDayOfWeek = true;
  private final boolean[] minuteFlags = new boolean[60];
  private final boolean[] hourFlags = new boolean[24];
  private final boolean[] dayOfMonthFlags = new boolean[31];
  private final boolean[] monthFlags = new boolean[12];
  private final boolean[] dayOfWeekFlags = new boolean[7];

  private ReportDialogBean(IClientContext context, IReport report)
  {
    this.report = report;

    ReportNotifyee rn = report.getReportNotifyee(context.getUser());
    if (rn == null)
    {
      this.scheduleMode = NONE;
    }
    else
    {
      this.address = rn.getAddress();
      this.protocol = rn.getProtocol();
      this.mimeType = rn.getMimeType();
      this.omitEmpty = rn.isOmitEmpty();
      
      this.minute = rn.getMinute();
      this.hour = rn.getHour();
      
      // Note: in STANDARD mode the minute must be a multiply of 10!
      this.scheduleMode = rn.getDays() != null && (this.minute % 10 == 0) ? STANDARD : EXPERT;
      
      CronEntry entry = rn.getCronEntry();
      
      this.everyMinute = entry.includesAnyMinute();
      this.everyHour = entry.includesAnyHour();
      this.everyDayOfMonth = entry.includesAnyDayOfMonth();
      this.everyMonth = entry.includesAnyMonth();
      this.everyDayOfWeek = entry.includesAnyDayOfWeek();
      
      if (!this.everyMinute)
        for (int i = 0; i < this.minuteFlags.length; i++)
          this.minuteFlags[i] = entry.includesMinute(i);
      
      if (!this.everyHour)
        for (int i = 0; i < this.hourFlags.length; i++)
          this.hourFlags[i] = entry.includesHour(i);
      
      if (!this.everyDayOfMonth)
        for (int i = 0; i < this.dayOfMonthFlags.length; i++)
          this.dayOfMonthFlags[i] = entry.includesDayOfMonth(i + 1);
      
      if (!this.everyMonth)
        for (int i = 0; i < this.monthFlags.length; i++)
          this.monthFlags[i] = entry.includesMonth(i);
      
      // Remember: dayOfWeekFlags is used for STANDARD mode as well
      if (!this.everyDayOfWeek || this.scheduleMode == STANDARD)
        for (int i = 0; i < this.dayOfWeekFlags.length; i++)
          this.dayOfWeekFlags[i] = entry.includesDayOfWeek(i + 1);
    }
  }

  public IReport getReport()
  {
    return this.report;
  }

  public IDataBrowser getPreviewReportData() throws Exception
  {
    if (this.previewReportData == null)
      this.previewReportData = ((Report) this.report).fetchData(PREVIEW_SIZE);
    return this.previewReportData;
  }

  public ScheduleMode getScheduleMode()
  {
    return this.scheduleMode;
  }

  public boolean isEveryMinute()
  {
    return this.everyMinute;
  }

  public boolean hasMinute(int minute)
  {
    if (this.scheduleMode == STANDARD)
      return minute == this.minute;
    return this.minuteFlags[minute];
  }

  public boolean isEveryHour()
  {
    return this.everyHour;
  }

  public boolean hasHour(int hour)
  {
    if (this.scheduleMode == STANDARD)
      return hour == this.hour;
    return this.hourFlags[hour];
  }

  public boolean isEveryDayOfMonth()
  {
    return this.everyDayOfMonth;
  }

  public boolean hasDayOfMonth(int dayOfMonth)
  {
    return this.dayOfMonthFlags[dayOfMonth - 1];
  }

  public boolean isEveryMonth()
  {
    return this.everyMonth;
  }

  public boolean hasMonth(int month)
  {
    return this.monthFlags[month];
  }

  public boolean isEveryDayOfWeek()
  {
    return this.everyDayOfWeek;
  }

  public boolean hasDayOfWeek(int dayOfWeek)
  {
    return this.dayOfWeekFlags[dayOfWeek - 1];
  }

  public boolean hasSave()
  {
    return this.hasSave;
  }

  public void update(IClientContext context, HttpServletRequest request) throws Exception
  {
    // refresh to remember bean
    context.setPropertyForWindow(BEAN_PROPERTY, this);

    ScheduleMode oldScheduleMode = this.scheduleMode;
    this.scheduleMode = (ScheduleMode) ScheduleModes.get(request.getParameter("mode"));
    if (this.scheduleMode == null)
      this.scheduleMode = NONE;

    if (this.scheduleMode == NONE)
    {
      // nothing more to do
    }
    else
    {
      this.address = StringUtil.toSaveString(request.getParameter("address")).trim();
      if (this.address.length() == 0)
      {
        this.address = StringUtil.toSaveString(context.getUser().getEMail());
      }
      this.protocol = request.getParameter("protocol");
      this.mimeType = request.getParameter("mimetype");
      this.omitEmpty = request.getParameter("omitEmpty") != null;

      if (this.scheduleMode == STANDARD)
      {
        if (oldScheduleMode == NONE)
        {
          this.minute = 0;
          this.hour = 0;

          this.dayOfWeekFlags[Calendar.MONDAY - 1] = true;
          this.dayOfWeekFlags[Calendar.TUESDAY - 1] = true;
          this.dayOfWeekFlags[Calendar.WEDNESDAY - 1] = true;
          this.dayOfWeekFlags[Calendar.THURSDAY - 1] = true;
          this.dayOfWeekFlags[Calendar.FRIDAY - 1] = true;
          this.dayOfWeekFlags[Calendar.SATURDAY - 1] = true;
          this.dayOfWeekFlags[Calendar.SUNDAY - 1] = true;
        }
        else if (oldScheduleMode == EXPERT)
        {
          // round to 10 because combobox has just 0, 10, 20, 30, 40, 50
          this.minute = 10 * (singleValue(this.minuteFlags, 0) / 10);
          this.hour = singleValue(this.hourFlags, 0);

          if (this.everyDayOfWeek)
          {
            this.dayOfWeekFlags[Calendar.MONDAY - 1] = true;
            this.dayOfWeekFlags[Calendar.TUESDAY - 1] = true;
            this.dayOfWeekFlags[Calendar.WEDNESDAY - 1] = true;
            this.dayOfWeekFlags[Calendar.THURSDAY - 1] = true;
            this.dayOfWeekFlags[Calendar.FRIDAY - 1] = true;
            this.dayOfWeekFlags[Calendar.SATURDAY - 1] = true;
            this.dayOfWeekFlags[Calendar.SUNDAY - 1] = true;
          }
        }
        else
        {
          this.minute = parseInt(request.getParameter("minute"), 0);
          this.hour = parseInt(request.getParameter("hour"), 0);

          this.dayOfWeekFlags[Calendar.MONDAY - 1] = request.getParameter("monday") != null;
          this.dayOfWeekFlags[Calendar.TUESDAY - 1] = request.getParameter("tuesday") != null;
          this.dayOfWeekFlags[Calendar.WEDNESDAY - 1] = request.getParameter("wednesday") != null;
          this.dayOfWeekFlags[Calendar.THURSDAY - 1] = request.getParameter("thursday") != null;
          this.dayOfWeekFlags[Calendar.FRIDAY - 1] = request.getParameter("friday") != null;
          this.dayOfWeekFlags[Calendar.SATURDAY - 1] = request.getParameter("saturday") != null;
          this.dayOfWeekFlags[Calendar.SUNDAY - 1] = request.getParameter("sunday") != null;
        }
      }
      else
      {
        // this.scheduleMode == EXPERT
        
        if (oldScheduleMode == NONE)
        {
          this.everyMinute = false;
          reset(this.minuteFlags);
          this.minuteFlags[0] = true;

          this.everyHour = false;
          reset(this.hourFlags);
          this.hourFlags[0] = true;

          this.everyDayOfMonth = true;
          reset(this.dayOfMonthFlags);

          this.everyMonth = true;
          reset(this.monthFlags);

          this.everyDayOfWeek = true;
          reset(this.dayOfWeekFlags);
        }
        else if (oldScheduleMode == STANDARD)
        {
          this.everyMinute = false;
          reset(this.minuteFlags);
          this.minuteFlags[this.minute] = true;

          this.everyHour = false;
          reset(this.hourFlags);
          this.hourFlags[this.hour] = true;

          this.everyDayOfMonth = true;
          reset(this.dayOfMonthFlags);

          this.everyMonth = true;
          reset(this.monthFlags);

          this.everyDayOfWeek = false;
        }
        else
        {
          this.everyMinute = !"0".equals(StringUtil.toSaveString(request.getParameter("every_minute")));
          fill(this.minuteFlags, request.getParameterValues("minutes"), 0);

          this.everyHour = !"0".equals(StringUtil.toSaveString(request.getParameter("every_hour")));
          fill(this.hourFlags, request.getParameterValues("hours"), 0);

          this.everyDayOfMonth = !"0".equals(StringUtil.toSaveString(request.getParameter("every_dayofmonth")));
          fill(this.dayOfMonthFlags, request.getParameterValues("daysofmonth"), -1);

          this.everyMonth = !"0".equals(StringUtil.toSaveString(request.getParameter("every_month")));
          fill(this.monthFlags, request.getParameterValues("months"), 0);

          this.everyDayOfWeek = !"0".equals(StringUtil.toSaveString(request.getParameter("every_dayofweek")));
          fill(this.dayOfWeekFlags, request.getParameterValues("daysofweek"), -1);
        }
      }
    }

    // assume there is always a change which should be saved
    this.hasSave = true;
  }

  private static int parseInt(String intStr, int defaultVal)
  {
    if (intStr != null)
    {
      try
      {
        return Integer.parseInt(intStr);
      }
      catch (NumberFormatException ex)
      {
        // ignore
      }
    }
    return defaultVal;
  }

  private static void reset(boolean[] array)
  {
    for (int i = 0; i < array.length; i++)
      array[i] = false;
  }

  private static void fill(boolean[] array, String[] intValues, int delta)
  {
    reset(array);

    if (intValues != null)
      for (int i = 0; i < intValues.length; i++)
        array[Integer.parseInt(intValues[i]) + delta] = true;
  }

  private static int singleValue(boolean[] array, int defaultVal)
  {
    int val = -1;
    for (int i = 0; i < array.length; i++)
      if (array[i])
        if (val >= 0)
          return defaultVal;
        else
          val = i;
    return val >= 0 ? val : defaultVal;
  }

  public void save(IClientContext context, HttpServletRequest request) throws Exception
  {
    update(context, request);
    save(context);
  }

  private void save(IClientContext context) throws Exception
  {
    // TODO: validate email(s)
    if (StringUtil.toSaveString(this.address).length() == 0)
    {
      context.setPropertyForRequest(ERROR_PROPERTY, de.tif.jacob.i18n.I18N.localizeLabel("%LABEL_REPORTING_NO_VALID_EMAIL_MSG", context));
      return;
    }
    
    ReportNotifyee notifyee = getReportNotifyee(context);
    if (notifyee != null)
      this.report.setReportNotifyee(notifyee);
    else
      this.report.deleteReportNotifyee(context.getUser());

    this.report.save();
    this.hasSave = false;
  }
  
  public String getNextDates(IClientContext context)
  {
    ReportNotifyee notifyee = getReportNotifyee(context);
    if (notifyee == null)
      return "";

    Locale locale = context.getUser().getLocale();
    CronIterator iter = new CronIterator(notifyee.getCronEntry());
    StringBuffer entry = new StringBuffer();
    for (int i = 0; i < 3; i++)
    {
      if (i > 0)
        entry.append(", ");
      entry.append(DatetimeUtil.convertTimestampOnMinuteBaseToString(new Timestamp(iter.next().getTime()), locale));
    }
    entry.append(", ..");
    return entry.toString();
  }

  private ReportNotifyee getReportNotifyee(IClientContext context)
  {
    if (this.scheduleMode == STANDARD)
    {
      int[] days = new int[7];
      int i = 0;
      if (this.dayOfWeekFlags[Calendar.MONDAY - 1])
        days[i++] = java.util.Calendar.MONDAY;
      if (this.dayOfWeekFlags[Calendar.TUESDAY - 1])
        days[i++] = java.util.Calendar.TUESDAY;
      if (this.dayOfWeekFlags[Calendar.WEDNESDAY - 1])
        days[i++] = java.util.Calendar.WEDNESDAY;
      if (this.dayOfWeekFlags[Calendar.THURSDAY - 1])
        days[i++] = java.util.Calendar.THURSDAY;
      if (this.dayOfWeekFlags[Calendar.FRIDAY - 1])
        days[i++] = java.util.Calendar.FRIDAY;
      if (this.dayOfWeekFlags[Calendar.SATURDAY - 1])
        days[i++] = java.util.Calendar.SATURDAY;
      if (this.dayOfWeekFlags[Calendar.SUNDAY - 1])
        days[i++] = java.util.Calendar.SUNDAY;
      if (i == 0)
      {
        // no weekday selected -> no scheduling
        return null;
      }
      
      int[] selectedDays = new int[i];
      System.arraycopy(days, 0, selectedDays, 0, i);
      return new ReportNotifyee(context.getUser(), address, protocol, mimeType, minute, hour, selectedDays, omitEmpty);
    }
    else if (this.scheduleMode == EXPERT)
    {
      StringBuffer entry = new StringBuffer();
      entry.append(this.everyMinute ? "*" : getCronPart(this.minuteFlags, 0)).append(" ");
      entry.append(this.everyHour ? "*" : getCronPart(this.hourFlags, 0)).append(" ");
      entry.append(this.everyDayOfMonth ? "*" : getCronPart(this.dayOfMonthFlags, 1)).append(" ");
      entry.append(this.everyMonth ? "*" : getCronPart(this.monthFlags, 1)).append(" ");
      entry.append(this.everyDayOfWeek ? "*" : getCronPart(this.dayOfWeekFlags, 0));

      return new ReportNotifyee(context.getUser(), address, protocol, mimeType, new CronEntry(entry.toString()), omitEmpty);
    }

    return null;
  }
  
  private static String getCronPart(boolean[] array, int delta)
  {
    StringBuffer part = new StringBuffer();
    int n = 0;
    for (int i = 0; i < array.length; i++)
    {
      if (array[i])
      {
        if (n++ > 0)
          part.append(",");
        part.append(i + delta);
      }
    }

    // interpret all true or all false as all ;-)
    if (n == 0 || n == array.length)
      return "*";

    return part.toString();
  }

  public String getOwnerFullname()
  {
    try
    {
      IUser user = this.report.getOwner();
      if (user != null)
        return user.getFullName();
    }
    catch (Exception ex)
    {
      // ignore
    }

    // return owner id instead
    return report.getOwnerId() + " (not existing)";
  }

  public String getProtocol(IClientContext context)
  {
    ReportNotifyee rn = report.getReportNotifyee(context.getUser());
    if (rn != null)
    {
      return rn.getProtocol();
    }
    return "email://";
  }

  public String getAddress()
  {
    return this.address;
  }

  public boolean isMimeType(String mime)
  {
    return this.mimeType != null && this.mimeType.endsWith(mime);
  }

  private static boolean isTextMimeType(String mimetype)
  {
    return mimetype.startsWith("text/") && !mimetype.equals("text/csv");
  }

  public boolean isTextMimeType()
  {
    if (this.mimeType != null)
      return isTextMimeType(this.mimeType);
    return false;
  }

  public String getTextMimeType(IClientContext context)
  {
    IUser user = context.getUser();
    // if(report!=null)
    {
      ReportNotifyee rn = report.getReportNotifyee(user);
      if (rn != null)
      {
        if (isTextMimeType(rn.getMimeType()))
          return rn.getMimeType();
      }
      else
      {
        if (isTextMimeType(report.getDefaultMimeType()))
          return report.getDefaultMimeType();
      }
    }
    return "text/plain";
  }

  public boolean isOmitEmpty()
  {
    return this.omitEmpty;
  }

}
