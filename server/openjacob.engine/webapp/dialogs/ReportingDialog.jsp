<%--
     This file is part of Open-jACOB
     Copyright (C) 2005-2006 Tarragon GmbH
  
     This program is free software; you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation; version 2 of the License.
  
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
  
     You should have received a copy of the GNU General Public License     
     along with this program; if not, write to the Free Software
     Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
     USA
--%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="j" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="java.util.*" 
         import="de.tif.jacob.core.*" 
         import="de.tif.jacob.core.data.*" 
         import="de.tif.jacob.util.*" 
         import="de.tif.jacob.screen.impl.*"
         import="de.tif.jacob.screen.impl.html.*" 
         import="de.tif.jacob.screen.impl.tag.*"
         import="de.tif.jacob.security.*" 
         import="de.tif.jacob.report.*"
         import="de.tif.jacob.report.impl.*"
         import="de.tif.jacob.report.impl.dialog.*" %><%

    response.setCharacterEncoding("ISO-8859-1");
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }

    response.reset();

    String      thisPage     =  request.getContextPath()+request.getServletPath();

    String      browserId    = request.getParameter("browser");
    String      reportId     = request.getParameter("report")!=null?request.getParameter("report"):"";
    String      action       = request.getParameter("action");

    Application   app          = (Application)jacobSession.getApplication(browserId);
    IUser         user         = jacobSession.getUser();

    ClientContext context = new ClientContext(user,app,browserId);
    Context.setCurrent(context);

    ReportDialogBean bean = ReportDialogBean.getBean(context, reportId);
       
    // no valid IApplication in the session found...cleanup all data
    // and redirect to the login screen
    //
    if(app==null)
    {
        UserManagement.logOutUser(request,response);
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }
    else if("show".equals(action) || "showExcel".equals(action))
    {
        try
        {
            String mimeType;
            if("show".equals(action))
            {
                if(bean.getReport().getDefaultMimeType().startsWith("text/"))
                   mimeType = bean.getReport().getDefaultMimeType();
                else
                   mimeType = "text/plain";
                response.setHeader("Content-Disposition", "attachment; filename=report.txt");
            }
            else
            {
                mimeType = "application/excel";
                response.setHeader("Content-Disposition", "attachment; filename=report.xls");
            }
            response.setContentType(mimeType);

            ServletOutputStream stream = response.getOutputStream();
            bean.getReport().render(stream, mimeType);
            stream.flush();
        }
        catch (java.net.SocketException ex)
        {
            // do not write complete stacktrace if client closes socket
            Report.logger.info(ex.toString());
        }
        catch(Throwable th)
        {
            // Tomcat throws a org.apache.catalina.connector.ClientAbortException
            // if the requested report output is aborted by the user
            // in this case we want to avoid to trace an exception in the log file
            // (catalina.out fills up with pseudo errors :-)
            //
            if (!th.getClass().getName().endsWith("ClientAbortException"))
            {
              de.tif.jacob.core.exception.ExceptionHandler.handle(th);
            }
        }
        return;
    }
    else if("edit".equals(action))
    {
        try
        {
            app.startReportMode(context, bean.getReport());
        }
        catch(Throwable th)
        {
            de.tif.jacob.core.exception.ExceptionHandler.handle(th);
        }
        out.println("<html><head><script type='text/javascript' src='../javascript/"+BrowserType.getType(request)+"_common.js' ></script></head><script>FireEvent(window,'close')</script></html>");
        return;
    }
    else if("save".equals(action) && bean!=null)
    {
        bean.save(context, request);
    }
    else if("update".equals(action) && bean!=null)
    {
        bean.update(context, request);
    }
    // delete all handsover reports
    //
    else if("delete".equals(action))
    {
        Enumeration enumeration = request.getParameterNames();
        while(enumeration.hasMoreElements())
        {
            String key   = (String)enumeration.nextElement();
            String value = request.getParameter(key);
    
            if(key.startsWith("delete_"))
            {
                try
                {
                    ReportManager.getReport(value).delete();
                    if(value.equals(reportId))
                    {
                        bean=null;
                        reportId=null;
                    }
                }
                catch(Exception exc)
                {
                    de.tif.jacob.core.exception.ExceptionHandler.handle(exc);
                }
            }
        }
    }
    else if("changeselection".equals(action))
    {
      user.setProperty(SELECTION_MODE, request.getParameter("selectionmode"));
    }
    String themeId = jacobSession.getCurrentTheme();
    Theme theme = ThemeManager.getTheme(themeId);
    %>
<HTML>
<HEAD>
    <title><j:dialogTitle id="DIALOG_TITLE_SHOWREPORT"/></title>
    <META Http-Equiv="Cache-Control" Content="no-cache">
    <META Http-Equiv="Pragma" Content="no-cache">
    <META Http-Equiv="Expires" Content="0">

    <script id="common_js"  type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
    <script id="debug_js"   type="text/javascript" src="../javascript/popupWindow.js" ></script>
    <script id="debug_js"   type="text/javascript" src="../javascript/debug.js" ></script>
    <link   type="text/css" rel="stylesheet" href="../themes/common.css" />
    <link  type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />
<script>
  var userTheme = '<%=app.getTheme()%>';
   function resizeScrollarea()
   {
    new LayerObject("scrollarea").css.height= "100px";
    var yOffset = parseInt(getAnchorPosition('scrollTable').y);
    var height  = parseInt(getAnchorPosition('dialog_buttonbar').y);
    new LayerObject("scrollarea").css.height= (height-yOffset)+"px";
   }
   function showSave()
   {
    new LayerObject('saveAddress').show();
    getObj('nextDates').value='';
   }
</script>
</HEAD>
<body class="dialog" onLoad="resizeScrollarea()" onResize="resizeScrollarea()">
    <table  cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">
    <tr>
        <td  height="100%"  valign="top">
            <div width="100%" height="100%"  class="dialog_content">
                <table  cellspacing="0" cellpadding="0" width="95%" height="100%" border="0">
                    <tr valign=top>
                        <td nowrap width="200" valign="top">
                        <!--
                             the left hand side information pane with the filter options
                             and a selection area for the reports.
                          -->
                        <div class="wizard_info" id="wizard_info" name="wizard_info">
                            <br>
                            <h1 class="wizard_info"><j:i18n id="LABEL_REPORTING_REPORTS"/></h1>
                            <table cellspacing="0" cellpadding="0" width="100%" height="2" border="0">
                                <tr>
                                    <td class="HGradientRulerLeft"></td>
                                    <td class="HGradientRulerRight"></td>
                                </tr>
                            </table>
                            <form name="actionForm" id="actionForm" action="<%=thisPage%>" method="post" style="height:100px;margin:0px;padding:0px;">
                                <input type="hidden" name="browser" id="browser" value="<%=browserId%>"/>
                                <input type="hidden" name="report" id="report" value="<%=reportId%>"/>
                                <input type="hidden" name="action" id="action" value=""/>
                                <select onChange="getObj('action').value='changeselection';getObj('report').value='';getObj('actionForm').submit();" name="selectionmode">
                                  <option <%=getReportSelection(user,"all")%> value="all"><j:i18n id="LABEL_REPORTING_SELECTION_ALL"/></option>
                                  <option <%=getReportSelection(user,"own")%> value="own"><j:i18n id="LABEL_REPORTING_SELECTION_OWN"/></option>
                                  <option <%=getReportSelection(user,"public")%> value="public"><j:i18n id="LABEL_REPORTING_SELECTION_FOREIGN"/></option>
                                </select>
                                <div id="scrollarea" class="listbox_normal" >
                                <table width="100%" id="scrollTable" cellspacing="0" cellpadding="0">
                                <%
                                try
                                {
                                    Object userselection = user.getProperty(SELECTION_MODE);
                                    Iterator iter;
                                    if ("all".equals(userselection))
                                        iter = ReportManager.getAllReports(app.getApplicationDefinition(), user).iterator();
                                    else if ("public".equals(userselection))
                                        iter = ReportManager.getPublicReports(app.getApplicationDefinition(), user).iterator();
                                    else
                                        iter = ReportManager.getReports(app.getApplicationDefinition(), user).iterator();
                                    if(iter.hasNext())
                                    {
                                        while(iter.hasNext())
                                        {
                                          try
                                          {
                                              IReport report = (IReport)iter.next();

                                              if(bean==null)
                                              {
                                                 // Falls der Benutzer kein Report selektiert hat, wird der erste genommen der gefunden wird
                                                 bean = ReportDialogBean.getBean(context, report);
                                                 reportId = report.getGUID();
                                              }
                                              boolean current = report.getGUID().equals(reportId);
                                              String cssClass = "listboxitem";
                                              if(current)
                                                  cssClass = "listboxitem_selected";

                                              out.print("<tr class=\""+cssClass+"\" ><td><input type=\"checkbox\" name='delete_");
                                              out.print(report.getGUID());
                                              out.print("' value='");
                                              out.print(report.getGUID());
                                              if(report.getOwnerId().equals(user.getLoginId()))
                                              {
                                                out.print("'/></td><td>");
                                              }
                                              else
                                              {
                                                out.print("' disabled></td><td>");
                                              }

                                              // check the filter to the current report.
                                              //
                                              if(current==true)
                                              {
                                                out.print("<span nowrap>");
                                                out.print(report.getName());
                                              }
                                              else
                                              {
                                                out.print("<span onClick=\"getObj('report').value='");
                                                out.print(report.getGUID());
                                                out.print("';getObj('actionForm').submit()\">");
                                                out.print(report.getName());
                                              }
                                              out.println("</span></td></tr>");
                                           }
                                           catch(Exception exc)
                                           {
                                                exc.printStackTrace();
                                           }
                                        }
                                        %>
                                        <tr>
                                            <td>
                                                 <img src="images/01.gif">
                                            </td>
                                            <td>
                                                 <button class="button_normal" style="margin-top:0px;padding-top:0px;" onClick="javascript:if(confirm('<j:i18n id="MSG_REPORTING_REALY_DELETE_REPORT"/>')){getObj('action').value='delete';getObj('actionForm').submit()};"><j:i18n id="BUTTON_COMMON_DELETE"/></button>
                                            </td>
                                        </tr>
                                        <%
                                    }
                                }
                                catch(Throwable th)
                                {
                                    th.printStackTrace();
                                }

                                %>
                                </table>
                                </div>
                            </form>
                        </div>
                        </td>
                        <!-- placeholder between the information pane and the report information display -->
                        <td>&nbsp;&nbsp;&nbsp;</td>
    
                        <!--
                             display the properties of the selected report.
                          -->
                        <td valign=top width=100% >
                            <%
                            if(bean!=null)
                            {
                            %>
                            <br>
                            <fieldset>
                            <legend class="wizard_groupheading"><j:i18n id="LABEL_REPORTING_GENERAL_DATA"/></legend>
                            <!-- properties of the report -->
                            <table>
                                <tr>
                                    <td class="caption_normal_update" style="white-space:nowrap;"><j:i18n id="LABEL_REPORTING_REPORTNAME"/></td>
                                    <td class="caption_normal_update"><%=bean.getReport().getName()%></td>
                                </tr>
                                <tr>
                                    <td class="caption_normal_update" style="white-space:nowrap;"><j:i18n id="LABEL_REPORTING_OWNER"/></td>
                                    <td class="caption_normal_update"><%=bean.getOwnerFullname()%></td>
                                </tr>
                                <tr>
                                    <td class="caption_normal_update" style="white-space:nowrap;"><j:i18n id="LABEL_REPORTING_PRIVATEFLAG"/></td>
                                    <% 
                                    	if (bean.getReport().isPrivate())
                                    	{
                                    %>
                                    	<td class="caption_normal_update"><j:i18n id="LABEL_COMMON_YES"/></td>
                                    <% 
                                    	}
                                    	else
                                    	{
                                    %>
                                    	<td class="caption_normal_update"><j:i18n id="LABEL_COMMON_NO"/></td>
                                    <% 
                                    	}
                                    %>
                                </tr>
                                <tr>
                                    <td class="caption_normal_update" valign="middle"><j:i18n id="LABEL_REPORTING_PREVIEW"/></td>
                                    <td>
                                      <div style="overflow:auto;width:500px;">
                                        <table cellspacing="0" cellpadding="0" class="SearchBrowser" style="padding:0px;margin:0px" border="0">
                                        <tr>
                                           <%
                                              IReport.Column[] c =  bean.getReport().getColumns();
                                              for (int i= 0; i < c.length; i++)
                                              {
                                                 out.print("<td class='sbh' nowrap>");
                                                 out.print(StringUtil.htmlEncode(c[i].label));
                                                 out.println("</td>");
                                              }
                                           %>
                                        </tr>
                                        <%
                                        // show report preview
                                        //
                                        try
                                        {
                                           IDataBrowser previewData = bean.getPreviewReportData();
                                           for (int row = 0; row < previewData.recordCount(); row++)
                                           {
                                         	    IDataBrowserRecord previewRecord = previewData.getRecord(row);
                                              String css = (row%2==0)?"sbr_e":"sbr_o";
                                              out.println("<tr class='"+css+"'>");
                                              for (int i = 0; i < c.length; i++)
                                              {
                                                 out.print("<td class='");
                                                 out.print(css);
                                                 out.print("' nowrap>");
                                                 try
                                                 {
                                                    out.print(StringUtil.htmlEncode(previewRecord.getSaveStringValue(i, user.getLocale())));
                                                 }
                                                 catch (Exception ex)
                                                 {
                                                	  out.print("ERR");
                                                 }
                                                 out.println("</td>");
                                              }
                                              out.println("</tr>");
                                           }
                                        }
                                        catch (Exception ex)
                                        {
                                           // in case we cannot fetch report data
                                           de.tif.jacob.core.exception.ExceptionHandler.handle(ex);
                                           out.println("<tr class='sbr_e'>");
                                           out.print("<td class='sbr_e' colspan='" + c.length + "' nowrap>");
                                           out.print(ex.toString());
                                           out.println("</td>");
                                           out.println("</tr>");
                                        }
                                        %>
                                        </table>
                                      </div>
                                    </td>
                                </tr>
                            </table> 
                            </fieldset>
                            <br>

                <fieldset>
                <legend class="wizard_groupheading"><j:i18n id="LABEL_REPORTING_SCHEDULER_DATA"/></legend>
                    <form name="aboForm" id="aboForm" action="<%=thisPage%>" method="post">
                    <input type=submit name="submit" id="submit" value=submit style="display:none">
                    <input type=hidden name="action" id="scheduleAction" value="update">
                    <input type=hidden name="browser" id="browser" value="<%=browserId%>">
                    <input type=hidden name="report" id="report" value="<%=reportId%>">
                    <input type=hidden name="protocol" value="<%=bean.getProtocol(context)%>">
                    <table>
                      <tr>
                        <td nowrap valign="middle"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_MODE"/></span></td>
                        <td>
                            <select onChange="getObj('submit').click()" style="width:180;" name="mode">
                                <option <%=selected(ReportDialogBean.NONE == bean.getScheduleMode())%> value="<%=ReportDialogBean.NONE%>"><j:i18n id="LABEL_REPORTING_MODE_NONE"/></option>
                                <option <%=selected(ReportDialogBean.STANDARD == bean.getScheduleMode())%> value="<%=ReportDialogBean.STANDARD%>"><j:i18n id="LABEL_REPORTING_MODE_STANDARD"/></option>
                                <option <%=selected(ReportDialogBean.EXPERT == bean.getScheduleMode())%> value="<%=ReportDialogBean.EXPERT%>"><j:i18n id="LABEL_REPORTING_MODE_EXPERT"/></option>
                            </select>
                        </td>
                        <td align="right">
                          <input onClick="getObj('scheduleAction').value='save'; getObj('submit').click()" class="button_emphasize_normal" id="saveAddress" name="saveAddress" value="<j:i18n id="BUTTON_COMMON_SAVE"/>" type="button" <% if (!bean.hasSave()) { %> style="display:none" <% } %> />
                        </td>
                      </tr>
                      <%
                      if(ReportDialogBean.NONE != bean.getScheduleMode())
                      {
                      %>
                      <tr>
                        <td nowrap valign="middle"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_FORMAT"/></span></td>
                        <td>
                            <select onChange="showSave()" style="width:180;" name="mimetype">
                                <option <%=selected(bean.isMimeType("application/excel"))%> value="application/excel"><j:i18n id="LABEL_REPORTING_MIMETYPE_EXCEL"/></option>
                                <option <%=selected(bean.isTextMimeType())%> value="<%=bean.getTextMimeType(context)%>"><j:i18n id="LABEL_REPORTING_MIMETYPE_PLAINTEXT"/></option>
                                <option <%=selected(bean.isMimeType("text/csv"))%> value="text/csv"><j:i18n id="LABEL_REPORTING_MIMETYPE_CSV"/></option>
                            </select>
                        </td>
                        <td>
                            <input onClick="showSave()" <%=checked(bean.isOmitEmpty())%> type="checkbox" name="omitEmpty"><span class="caption_normal_update"> <j:i18n id="LABEL_REPORTING_OMIT_EMPTY"/></span>
                        </td>
                      </tr>
                      <tr>
                        <td nowrap valign="middle"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_SEND_TO"/></span></td>
                        <td colspan="2">
                            <input onKeyUp="showSave()" class="text_normal editable_inputfield" style="font-size:10pt;width:100%;" type="text" name="address" value="<%=bean.getAddress()%>"/>
                        </td>
                      </tr>
                      <%
                      if(ReportDialogBean.STANDARD == bean.getScheduleMode())
                      {
                      %>
                      <tr>
                        <td nowrap valign="middle"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_SCHEDULETIME"/></span></td>
                        <td>
                                <%
                                  if ("en".equals(user.getLocale().getLanguage()) && "US".equals(user.getLocale().getCountry()))
                                  {
                                %>
                            <select onChange="showSave()" style="width:90;" name="hour">
                                <option <%=selected(bean.hasHour(0))%> value="0">12 Midnight</option>
                                <option <%=selected(bean.hasHour(1))%> value="1">1 AM</option>
                                <option <%=selected(bean.hasHour(2))%> value="2">2 AM</option>
                                <option <%=selected(bean.hasHour(3))%> value="3">3 AM</option>
                                <option <%=selected(bean.hasHour(4))%> value="4">4 AM</option>
                                <option <%=selected(bean.hasHour(5))%> value="5">5 AM</option>
                                <option <%=selected(bean.hasHour(6))%> value="6">6 AM</option>
                                <option <%=selected(bean.hasHour(7))%> value="7">7 AM</option>
                                <option <%=selected(bean.hasHour(8))%> value="8">8 AM</option>
                                <option <%=selected(bean.hasHour(9))%> value="9">9 AM</option>
                                <option <%=selected(bean.hasHour(10))%> value="10">10 AM</option>
                                <option <%=selected(bean.hasHour(11))%> value="11">11 AM</option>
                                <option <%=selected(bean.hasHour(12))%> value="12">12 Noon</option>
                                <option <%=selected(bean.hasHour(13))%> value="13">1 PM</option>
                                <option <%=selected(bean.hasHour(14))%> value="14">2 PM</option>
                                <option <%=selected(bean.hasHour(15))%> value="15">3 PM</option>
                                <option <%=selected(bean.hasHour(16))%> value="16">4 PM</option>
                                <option <%=selected(bean.hasHour(17))%> value="17">5 PM</option>
                                <option <%=selected(bean.hasHour(18))%> value="18">6 PM</option>
                                <option <%=selected(bean.hasHour(19))%> value="19">7 PM</option>
                                <option <%=selected(bean.hasHour(20))%> value="20">8 PM</option>
                                <option <%=selected(bean.hasHour(21))%> value="21">9 PM</option>
                                <option <%=selected(bean.hasHour(22))%> value="22">10 PM</option>
                                <option <%=selected(bean.hasHour(23))%> value="23">11 PM</option>
                                <%
                                  }
                                  else
                                  {
                                %>
                            <select onChange="showSave()" style="width:50;" name="hour">
                                <option <%=selected(bean.hasHour(0))%> value="0">0</option>
                                <option <%=selected(bean.hasHour(1))%> value="1">1</option>
                                <option <%=selected(bean.hasHour(2))%> value="2">2</option>
                                <option <%=selected(bean.hasHour(3))%> value="3">3</option>
                                <option <%=selected(bean.hasHour(4))%> value="4">4</option>
                                <option <%=selected(bean.hasHour(5))%> value="5">5</option>
                                <option <%=selected(bean.hasHour(6))%> value="6">6</option>
                                <option <%=selected(bean.hasHour(7))%> value="7">7</option>
                                <option <%=selected(bean.hasHour(8))%> value="8">8</option>
                                <option <%=selected(bean.hasHour(9))%> value="9">9</option>
                                <option <%=selected(bean.hasHour(10))%> value="10">10</option>
                                <option <%=selected(bean.hasHour(11))%> value="11">11</option>
                                <option <%=selected(bean.hasHour(12))%> value="12">12</option>
                                <option <%=selected(bean.hasHour(13))%> value="13">13</option>
                                <option <%=selected(bean.hasHour(14))%> value="14">14</option>
                                <option <%=selected(bean.hasHour(15))%> value="15">15</option>
                                <option <%=selected(bean.hasHour(16))%> value="16">16</option>
                                <option <%=selected(bean.hasHour(17))%> value="17">17</option>
                                <option <%=selected(bean.hasHour(18))%> value="18">18</option>
                                <option <%=selected(bean.hasHour(19))%> value="19">19</option>
                                <option <%=selected(bean.hasHour(20))%> value="20">20</option>
                                <option <%=selected(bean.hasHour(21))%> value="21">21</option>
                                <option <%=selected(bean.hasHour(22))%> value="22">22</option>
                                <option <%=selected(bean.hasHour(23))%> value="23">23</option>
                                <%
                                  }
                                %>
                             </select> : 
                             <select onChange="showSave()" style="width:50;" name=minute>
                                <option <%=selected(bean.hasMinute(0))%> value="0">00</option>
                                <option <%=selected(bean.hasMinute(10))%> value="10">10</option>
                                <option <%=selected(bean.hasMinute(20))%> value="20">20</option>
                                <option <%=selected(bean.hasMinute(30))%> value="30">30</option>
                                <option <%=selected(bean.hasMinute(40))%> value="40">40</option>
                                <option <%=selected(bean.hasMinute(50))%> value="50">50</option>
                            </select>
                        </td>
                      </tr>
                      <tr>
                        <td nowrap valign="middle"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_DATES"/></span></td>
                        <td colspan="2">
                            <input onClick="showSave()" <%=checked(bean.hasDayOfWeek(java.util.Calendar.MONDAY))%> type="checkbox" name="monday"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_MONDAY"/></span>
                            <input onClick="showSave()" <%=checked(bean.hasDayOfWeek(java.util.Calendar.TUESDAY))%> type="checkbox" name="tuesday"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_TUESDAY"/></span>
                            <input onClick="showSave()" <%=checked(bean.hasDayOfWeek(java.util.Calendar.WEDNESDAY))%> type="checkbox" name="wednesday"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_WEDNESDAY"/></span>
                            <input onClick="showSave()" <%=checked(bean.hasDayOfWeek(java.util.Calendar.THURSDAY))%> type="checkbox" name="thursday"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_THURSDAY"/></span>
                            <input onClick="showSave()" <%=checked(bean.hasDayOfWeek(java.util.Calendar.FRIDAY))%> type="checkbox" name="friday"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_FRIDAY"/></span>
                            <input onClick="showSave()" <%=checked(bean.hasDayOfWeek(java.util.Calendar.SATURDAY))%> type="checkbox" name="saturday"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_SATURDAY"/></span>
                            <input onClick="showSave()" <%=checked(bean.hasDayOfWeek(java.util.Calendar.SUNDAY))%> type="checkbox" name="sunday"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_SUNDAY"/></span><br>
                        </td>
                      </tr>
                      <%
                      }
                      else
                      {
                      %>
                      <tr>
                        <td nowrap valign="middle"><span class="caption_normal_update">Crontab:</span></td>
                        <td colspan="2">
<table>
<tr>

<td nowrap width="20%">
<div class="box">
<h1 class="caption_normal_search"><big><b><j:i18n id="LABEL_REPORTING_CRON_TITLE_MINUTE"/></b></big></h1>
<div class="radio_normal">
<input onClick="showSave(); getObj('minutes').disabled=1" type="radio" name="every_minute" id="every_minute_every" class="chooser" value="1" <%=checked(bean.isEveryMinute())%> />
<label class="caption_normal_search" for="every_minute_every"><j:i18n id="LABEL_REPORTING_CRON_EVERY_MINUTE"/></label>
<br/>
<input onClick="showSave(); getObj('minutes').disabled=0" type="radio" name="every_minute" id="every_minute_choose" class="chooser" value="0" <%=checked(!bean.isEveryMinute())%> />
<label class="caption_normal_search" for="every_minute_choose"><j:i18n id="LABEL_REPORTING_CRON_CHOOSE"/><small><sup><b>*</b></sup></small></label>
<br/>
</div>
<select onChange="showSave();" name="minutes" id="minutes" style="height:120px;width:90px;" multiple <%=disabled(bean.isEveryMinute())%>>
<option <%=selected(bean.hasMinute(0))%> value="0">0</option>
<option <%=selected(bean.hasMinute(1))%> value="1">1</option>
<option <%=selected(bean.hasMinute(2))%> value="2">2</option>
<option <%=selected(bean.hasMinute(3))%> value="3">3</option>
<option <%=selected(bean.hasMinute(4))%> value="4">4</option>
<option <%=selected(bean.hasMinute(5))%> value="5">5</option>
<option <%=selected(bean.hasMinute(6))%> value="6">6</option>
<option <%=selected(bean.hasMinute(7))%> value="7">7</option>
<option <%=selected(bean.hasMinute(8))%> value="8">8</option>
<option <%=selected(bean.hasMinute(9))%> value="9">9</option>
<option <%=selected(bean.hasMinute(10))%> value="10">10</option>
<option <%=selected(bean.hasMinute(11))%> value="11">11</option>
<option <%=selected(bean.hasMinute(12))%> value="12">12</option>
<option <%=selected(bean.hasMinute(13))%> value="13">13</option>
<option <%=selected(bean.hasMinute(14))%> value="14">14</option>
<option <%=selected(bean.hasMinute(15))%> value="15">15</option>
<option <%=selected(bean.hasMinute(16))%> value="16">16</option>
<option <%=selected(bean.hasMinute(17))%> value="17">17</option>
<option <%=selected(bean.hasMinute(18))%> value="18">18</option>
<option <%=selected(bean.hasMinute(19))%> value="19">19</option>
<option <%=selected(bean.hasMinute(20))%> value="20">20</option>
<option <%=selected(bean.hasMinute(21))%> value="21">21</option>
<option <%=selected(bean.hasMinute(22))%> value="22">22</option>
<option <%=selected(bean.hasMinute(23))%> value="23">23</option>
<option <%=selected(bean.hasMinute(24))%> value="24">24</option>
<option <%=selected(bean.hasMinute(25))%> value="25">25</option>
<option <%=selected(bean.hasMinute(26))%> value="26">26</option>
<option <%=selected(bean.hasMinute(27))%> value="27">27</option>
<option <%=selected(bean.hasMinute(28))%> value="28">28</option>
<option <%=selected(bean.hasMinute(29))%> value="29">29</option>
<option <%=selected(bean.hasMinute(30))%> value="30">30</option>
<option <%=selected(bean.hasMinute(31))%> value="31">31</option>
<option <%=selected(bean.hasMinute(32))%> value="32">32</option>
<option <%=selected(bean.hasMinute(33))%> value="33">33</option>
<option <%=selected(bean.hasMinute(34))%> value="34">34</option>
<option <%=selected(bean.hasMinute(35))%> value="35">35</option>
<option <%=selected(bean.hasMinute(36))%> value="36">36</option>
<option <%=selected(bean.hasMinute(37))%> value="37">37</option>
<option <%=selected(bean.hasMinute(38))%> value="38">38</option>
<option <%=selected(bean.hasMinute(39))%> value="39">39</option>
<option <%=selected(bean.hasMinute(40))%> value="40">40</option>
<option <%=selected(bean.hasMinute(41))%> value="41">41</option>
<option <%=selected(bean.hasMinute(42))%> value="42">42</option>
<option <%=selected(bean.hasMinute(43))%> value="43">43</option>
<option <%=selected(bean.hasMinute(44))%> value="44">44</option>
<option <%=selected(bean.hasMinute(45))%> value="45">45</option>
<option <%=selected(bean.hasMinute(46))%> value="46">46</option>
<option <%=selected(bean.hasMinute(47))%> value="47">47</option>
<option <%=selected(bean.hasMinute(48))%> value="48">48</option>
<option <%=selected(bean.hasMinute(49))%> value="49">49</option>
<option <%=selected(bean.hasMinute(50))%> value="50">50</option>
<option <%=selected(bean.hasMinute(51))%> value="51">51</option>
<option <%=selected(bean.hasMinute(52))%> value="52">52</option>
<option <%=selected(bean.hasMinute(53))%> value="53">53</option>
<option <%=selected(bean.hasMinute(54))%> value="54">54</option>
<option <%=selected(bean.hasMinute(55))%> value="55">55</option>
<option <%=selected(bean.hasMinute(56))%> value="56">56</option>
<option <%=selected(bean.hasMinute(57))%> value="57">57</option>
<option <%=selected(bean.hasMinute(58))%> value="58">58</option>
<option <%=selected(bean.hasMinute(59))%> value="59">59</option>
</select>
</div>
</td>

<td nowrap width="20%">
<div class="box">
<h1 class="caption_normal_search"><big><b><j:i18n id="LABEL_REPORTING_CRON_TITLE_HOUR"/></b></big></h1>
<div class="radio_normal">
<input onClick="showSave(); getObj('hours').disabled=1" type="radio" name="every_hour" id="every_hour_every" class="chooser" value="1" <%=checked(bean.isEveryHour())%> />
<label class="caption_normal_search" for="hour_chooser_every"><j:i18n id="LABEL_REPORTING_CRON_EVERY_HOUR"/></label>
<br/>
<input onClick="showSave(); getObj('hours').disabled=0" type="radio" name="every_hour" id="every_hour_choose" class="chooser" value="0" <%=checked(!bean.isEveryHour())%> />
<label class="caption_normal_search" for="hour_chooser_choose"><j:i18n id="LABEL_REPORTING_CRON_CHOOSE"/><small><sup><b>*</b></sup></small></label>
<br/>
</div>
<select onChange="showSave();" name="hours" id="hours" style="height:120px;width:90px;" multiple <%=disabled(bean.isEveryHour())%>>
<%
  if ("en".equals(user.getLocale().getLanguage()) && "US".equals(user.getLocale().getCountry()))
  {
%>
<option <%=selected(bean.hasHour(0))%> value="0">12 Midnight</option>
<option <%=selected(bean.hasHour(1))%> value="1">1 AM</option>
<option <%=selected(bean.hasHour(2))%> value="2">2 AM</option>
<option <%=selected(bean.hasHour(3))%> value="3">3 AM</option>
<option <%=selected(bean.hasHour(4))%> value="4">4 AM</option>
<option <%=selected(bean.hasHour(5))%> value="5">5 AM</option>
<option <%=selected(bean.hasHour(6))%> value="6">6 AM</option>
<option <%=selected(bean.hasHour(7))%> value="7">7 AM</option>
<option <%=selected(bean.hasHour(8))%> value="8">8 AM</option>
<option <%=selected(bean.hasHour(9))%> value="9">9 AM</option>
<option <%=selected(bean.hasHour(10))%> value="10">10 AM</option>
<option <%=selected(bean.hasHour(11))%> value="11">11 AM</option>
<option <%=selected(bean.hasHour(12))%> value="12">12 Noon</option>
<option <%=selected(bean.hasHour(13))%> value="13">1 PM</option>
<option <%=selected(bean.hasHour(14))%> value="14">2 PM</option>
<option <%=selected(bean.hasHour(15))%> value="15">3 PM</option>
<option <%=selected(bean.hasHour(16))%> value="16">4 PM</option>
<option <%=selected(bean.hasHour(17))%> value="17">5 PM</option>
<option <%=selected(bean.hasHour(18))%> value="18">6 PM</option>
<option <%=selected(bean.hasHour(19))%> value="19">7 PM</option>
<option <%=selected(bean.hasHour(20))%> value="20">8 PM</option>
<option <%=selected(bean.hasHour(21))%> value="21">9 PM</option>
<option <%=selected(bean.hasHour(22))%> value="22">10 PM</option>
<option <%=selected(bean.hasHour(23))%> value="23">11 PM</option>
<%
  }
  else
  {
%>
<option <%=selected(bean.hasHour(0))%> value="0">0</option>
<option <%=selected(bean.hasHour(1))%> value="1">1</option>
<option <%=selected(bean.hasHour(2))%> value="2">2</option>
<option <%=selected(bean.hasHour(3))%> value="3">3</option>
<option <%=selected(bean.hasHour(4))%> value="4">4</option>
<option <%=selected(bean.hasHour(5))%> value="5">5</option>
<option <%=selected(bean.hasHour(6))%> value="6">6</option>
<option <%=selected(bean.hasHour(7))%> value="7">7</option>
<option <%=selected(bean.hasHour(8))%> value="8">8</option>
<option <%=selected(bean.hasHour(9))%> value="9">9</option>
<option <%=selected(bean.hasHour(10))%> value="10">10</option>
<option <%=selected(bean.hasHour(11))%> value="11">11</option>
<option <%=selected(bean.hasHour(12))%> value="12">12</option>
<option <%=selected(bean.hasHour(13))%> value="13">13</option>
<option <%=selected(bean.hasHour(14))%> value="14">14</option>
<option <%=selected(bean.hasHour(15))%> value="15">15</option>
<option <%=selected(bean.hasHour(16))%> value="16">16</option>
<option <%=selected(bean.hasHour(17))%> value="17">17</option>
<option <%=selected(bean.hasHour(18))%> value="18">18</option>
<option <%=selected(bean.hasHour(19))%> value="19">19</option>
<option <%=selected(bean.hasHour(20))%> value="20">20</option>
<option <%=selected(bean.hasHour(21))%> value="21">21</option>
<option <%=selected(bean.hasHour(22))%> value="22">22</option>
<option <%=selected(bean.hasHour(23))%> value="23">23</option>
<%
  }
%>
</select>
</div>
</td>

<td nowrap width="20%">
<div class="box">
<h1 class="caption_normal_search"><big><b><j:i18n id="LABEL_REPORTING_CRON_TITLE_DAY"/></b></big></h1>
<div class="radio_normal">
<input onClick="showSave(); getObj('daysofmonth').disabled=1" type="radio" name="every_dayofmonth" id="every_dayofmonth_every" class="chooser" value="1" <%=checked(bean.isEveryDayOfMonth())%> />
<label class="caption_normal_search" for="every_dayofmonth_every"><j:i18n id="LABEL_REPORTING_CRON_EVERY_DAY"/></label>
<br/>
<input onClick="showSave(); getObj('daysofmonth').disabled=0" type="radio" name="every_dayofmonth" id="every_dayofmonth_choose" class="chooser" value="0" <%=checked(!bean.isEveryDayOfMonth())%> />
<label class="caption_normal_search" for="every_dayofmonth_choose"><j:i18n id="LABEL_REPORTING_CRON_CHOOSE"/><small><sup><b>*</b></sup></small></label>
<br/>
</div>
<select onChange="showSave();" name="daysofmonth" id="daysofmonth" style="height:120px;width:90px;" multiple <%=disabled(bean.isEveryDayOfMonth())%> >
<option <%=selected(bean.hasDayOfMonth(1))%> value="1">1</option>
<option <%=selected(bean.hasDayOfMonth(2))%> value="2">2</option>
<option <%=selected(bean.hasDayOfMonth(3))%> value="3">3</option>
<option <%=selected(bean.hasDayOfMonth(4))%> value="4">4</option>
<option <%=selected(bean.hasDayOfMonth(5))%> value="5">5</option>
<option <%=selected(bean.hasDayOfMonth(6))%> value="6">6</option>
<option <%=selected(bean.hasDayOfMonth(7))%> value="7">7</option>
<option <%=selected(bean.hasDayOfMonth(8))%> value="8">8</option>
<option <%=selected(bean.hasDayOfMonth(9))%> value="9">9</option>
<option <%=selected(bean.hasDayOfMonth(10))%> value="10">10</option>
<option <%=selected(bean.hasDayOfMonth(11))%> value="11">11</option>
<option <%=selected(bean.hasDayOfMonth(12))%> value="12">12</option>
<option <%=selected(bean.hasDayOfMonth(13))%> value="13">13</option>
<option <%=selected(bean.hasDayOfMonth(14))%> value="14">14</option>
<option <%=selected(bean.hasDayOfMonth(15))%> value="15">15</option>
<option <%=selected(bean.hasDayOfMonth(16))%> value="16">16</option>
<option <%=selected(bean.hasDayOfMonth(17))%> value="17">17</option>
<option <%=selected(bean.hasDayOfMonth(18))%> value="18">18</option>
<option <%=selected(bean.hasDayOfMonth(19))%> value="19">19</option>
<option <%=selected(bean.hasDayOfMonth(20))%> value="20">20</option>
<option <%=selected(bean.hasDayOfMonth(21))%> value="21">21</option>
<option <%=selected(bean.hasDayOfMonth(22))%> value="22">22</option>
<option <%=selected(bean.hasDayOfMonth(23))%> value="23">23</option>
<option <%=selected(bean.hasDayOfMonth(24))%> value="24">24</option>
<option <%=selected(bean.hasDayOfMonth(25))%> value="25">25</option>
<option <%=selected(bean.hasDayOfMonth(26))%> value="26">26</option>
<option <%=selected(bean.hasDayOfMonth(27))%> value="27">27</option>
<option <%=selected(bean.hasDayOfMonth(28))%> value="28">28</option>
<option <%=selected(bean.hasDayOfMonth(29))%> value="29">29</option>
<option <%=selected(bean.hasDayOfMonth(30))%> value="30">30</option>
<option <%=selected(bean.hasDayOfMonth(31))%> value="31">31</option>
</select>
</div>
</td>

<td nowrap width="20%">
<div class="box">
<h1 class="caption_normal_search"><big><b><j:i18n id="LABEL_REPORTING_CRON_TITLE_MONTH"/></b></big></h1>
<div class="radio_normal">
<input onClick="showSave(); getObj('months').disabled=1" type="radio" name="every_month" id="every_month_every" class="chooser" value="1" <%=checked(bean.isEveryMonth())%> />
<label class="caption_normal_search" for="every_month_every"><j:i18n id="LABEL_REPORTING_CRON_EVERY_MONTH"/></label>
<br/>
<input onClick="showSave(); getObj('months').disabled=0" type="radio" name="every_month" id="every_month_choose" class="chooser" value="0" <%=checked(!bean.isEveryMonth())%> />
<label class="caption_normal_search" for="every_month_choose"><j:i18n id="LABEL_REPORTING_CRON_CHOOSE"/><small><sup><b>*</b></sup></small></label>
<br/>
</div>
<select onChange="showSave();" name="months" id="months" style="height:120px;width:100px;" multiple <%=disabled(bean.isEveryMonth())%>>
<option <%=selected(bean.hasMonth(0))%> value="0"><j:i18n id="LABEL_REPORTING_JANUARY"/></option>
<option <%=selected(bean.hasMonth(1))%> value="1"><j:i18n id="LABEL_REPORTING_FEBRUARY"/></option>
<option <%=selected(bean.hasMonth(2))%> value="2"><j:i18n id="LABEL_REPORTING_MARCH"/></option>
<option <%=selected(bean.hasMonth(3))%> value="3"><j:i18n id="LABEL_REPORTING_APRIL"/></option>
<option <%=selected(bean.hasMonth(4))%> value="4"><j:i18n id="LABEL_REPORTING_MAY"/></option>
<option <%=selected(bean.hasMonth(5))%> value="5"><j:i18n id="LABEL_REPORTING_JUNE"/></option>
<option <%=selected(bean.hasMonth(6))%> value="6"><j:i18n id="LABEL_REPORTING_JULY"/></option>
<option <%=selected(bean.hasMonth(7))%> value="7"><j:i18n id="LABEL_REPORTING_AUGUST"/></option>
<option <%=selected(bean.hasMonth(8))%> value="8"><j:i18n id="LABEL_REPORTING_SEPTEMBER"/></option>
<option <%=selected(bean.hasMonth(9))%> value="9"><j:i18n id="LABEL_REPORTING_OCTOBER"/></option>
<option <%=selected(bean.hasMonth(10))%> value="10"><j:i18n id="LABEL_REPORTING_NOVEMBER"/></option>
<option <%=selected(bean.hasMonth(11))%> value="11"><j:i18n id="LABEL_REPORTING_DECEMBER"/></option>
</select>
</div>
</td>

<td nowrap width="20%">
<div class="box">
<h1 class="caption_normal_search"><big><b><j:i18n id="LABEL_REPORTING_CRON_TITLE_WEEKDAY"/></b></big></h1>
<div class="radio_normal">
<input onClick="showSave(); getObj('daysofweek').disabled=1" type="radio" name="every_dayofweek" id="every_dayofweek_every" class="chooser" value="1" <%=checked(bean.isEveryDayOfWeek())%> />
<label class="caption_normal_search" for="every_dayofweek_every"><j:i18n id="LABEL_REPORTING_CRON_EVERY_WEEKDAY"/></label>
<br/>
<input onClick="showSave(); getObj('daysofweek').disabled=0" type="radio" name="every_dayofweek" id="every_dayofweek_choose" class="chooser" value="0" <%=checked(!bean.isEveryDayOfWeek())%> />
<label class="caption_normal_search" for="every_dayofweek_choose"><j:i18n id="LABEL_REPORTING_CRON_CHOOSE"/><small><sup><b>*</b></sup></small></label>
<br/>
</div>
<select onChange="showSave();" name="daysofweek" id="daysofweek" style="height:120px;width:120px;" multiple <%=disabled(bean.isEveryDayOfWeek())%> >
<option <%=selected(bean.hasDayOfWeek(java.util.Calendar.MONDAY))%> value="<%=Integer.toString(java.util.Calendar.MONDAY)%>"><j:i18n id="LABEL_REPORTING_MONDAY"/></option>
<option <%=selected(bean.hasDayOfWeek(java.util.Calendar.TUESDAY))%> value="<%=Integer.toString(java.util.Calendar.TUESDAY)%>"><j:i18n id="LABEL_REPORTING_TUESDAY"/></option>
<option <%=selected(bean.hasDayOfWeek(java.util.Calendar.WEDNESDAY))%> value="<%=Integer.toString(java.util.Calendar.WEDNESDAY)%>"><j:i18n id="LABEL_REPORTING_WEDNESDAY"/></option>
<option <%=selected(bean.hasDayOfWeek(java.util.Calendar.THURSDAY))%> value="<%=Integer.toString(java.util.Calendar.THURSDAY)%>"><j:i18n id="LABEL_REPORTING_THURSDAY"/></option>
<option <%=selected(bean.hasDayOfWeek(java.util.Calendar.FRIDAY))%> value="<%=Integer.toString(java.util.Calendar.FRIDAY)%>"><j:i18n id="LABEL_REPORTING_FRIDAY"/></option>
<option <%=selected(bean.hasDayOfWeek(java.util.Calendar.SATURDAY))%> value="<%=Integer.toString(java.util.Calendar.SATURDAY)%>"><j:i18n id="LABEL_REPORTING_SATURDAY"/></option>
<option <%=selected(bean.hasDayOfWeek(java.util.Calendar.SUNDAY))%> value="<%=Integer.toString(java.util.Calendar.SUNDAY)%>"><j:i18n id="LABEL_REPORTING_SUNDAY"/></option>
</select>
</div>
</td>
</tr>

<tr>
<td colspan="5" class="caption_normal_update">
<small><sup><b>*</b></sup></small><j:i18n id="LABEL_REPORTING_SELECTION_HINT"/>
</td>
</tr>
</table>
                        </td>
                      </tr>
                      	<%
                      }
                      %>
                      <tr>
                        <td nowrap valign="middle"><span class="caption_normal_update"><j:i18n id="LABEL_REPORTING_NEXT_DELIVERIES"/></span></td>
                        <td colspan="2">
                            <input readonly class="text_normal" style="font-size:10pt;width:100%;" type="text" id="nextDates" name="nextDates" value="<%=bean.getNextDates(context)%>"/>
                        </td>
                      </tr>
                      <%
                      }
                      %>      
                    </table>
                    </form>     
                </fieldset>  
                <%
                }
                %> 
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td  align="right" valign="bottom">
            <div class="dialog_buttonbar" id="dialog_buttonbar">
                <%
                    if(bean!=null)
                    {%>
                        <input type=button  value='<j:i18n id="BUTTON_COMMON_SHOW"/>' onClick="document.location.href='<%=thisPage%>?browser=<%=browserId%>&report=<%=reportId%>&action=show';" class='button_normal'>
                        <input type=button  value='<j:i18n id="BUTTON_REPORTING_SHOW_AS_EXCEL"/>' onClick="document.location.href='<%=thisPage%>?browser=<%=browserId%>&report=<%=reportId%>&action=showExcel';" class='button_normal'>
                        <% if(bean.getReport().getOwnerId().equals(user.getLoginId())) {%>
                            <input type=button  value='<j:i18n id="BUTTON_COMMON_EDIT"/>' onClick="document.location.href='<%=thisPage%>?browser=<%=browserId%>&report=<%=reportId%>&action=edit';" class='button_normal'>
                    <%} }
                 %>
                <input type=button  value="<j:i18n id="BUTTON_COMMON_CLOSE"/>"  onClick="FireEvent(window,'close');" class="button_normal" >
            </div>
        </td>
    </tr>
    </table>
</body>
<%-- Falls das Fenster zuvor im Hintergrund war, muss dieses jetzt nach vorne gebracht werden --%>
<script>window.focus();</script>
<%
  String errorStr = ReportDialogBean.getError(context);
	if (errorStr != null)
	{
%>
<script>alert("<%=StringUtil.convertToUnicodeString(errorStr)%>")</script>
<%
  }
%>
</html>

<%!
static final String SELECTION_MODE = "REPORT_SELECTION_MODE";

String getReportSelection(IUser user, String selection)
{
	String userselection= (String) user.getProperty(SELECTION_MODE);
	if (userselection == null)
		return "own".equals(selection) ? "selected" : "";
	return userselection.equals(selection) ? "selected" : "";
}

  static String selected(boolean selected)
  {
    return selected ? "selected" : "";
  }

  static String checked(boolean checked)
  {
    return checked ? "checked" : "";
  }

  static String disabled(boolean disabled)
  {
    return disabled ? "disabled" : "";
  }

%>