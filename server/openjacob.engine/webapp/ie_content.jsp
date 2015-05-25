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

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="j" %><%@ page import="java.util.*"
    import="de.tif.jacob.license.*"
    import="de.tif.jacob.core.*"
    import="de.tif.jacob.util.*"
    import="de.tif.jacob.core.exception.*"
    import="de.tif.jacob.screen.impl.*"
    import="de.tif.jacob.screen.impl.theme.*"
    import="de.tif.jacob.screen.impl.html.*"
    import="de.tif.jacob.security.*" 
    
%><%
long timeIn = System.currentTimeMillis();
final java.util.Vector stop = new java.util.Vector();
String resizeFunction = "";
try
{
    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=logout.jsp\"></head><body></body></html>");
        return;
    }

    String thisPage =  request.getContextPath()+request.getServletPath();
    String thisPath =  thisPage.substring(0,thisPage.lastIndexOf("/")+1);

    String browserId    = request.getParameter("browser");
    String control      = request.getParameter("control");
    String event        = request.getParameter("event");
    String eventValue   = request.getParameter("eventValue");
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    Application app = null;
    if(jacobSession!=null)
        app=(Application)jacobSession.getApplication(browserId);


    // no valid application in the session found...cleanup all data
    // and redirect to the login screen
    //
    if(jacobSession==null || app==null)
    {
        UserManagement.logOutUser(request,response);
        out.clearBuffer();
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=logout.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);

   synchronized(app)
   {
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
/*
    ///// dummy tags to the browser so that connection is maintained /////////
    final java.io.PrintWriter writer = response.getWriter();
    Runnable runnable = new Runnable() {
        public void run() {
            int count = 0;
            while(stop.size()==0) 
            {
              try 
              {
                Thread.sleep(15000);
                if(stop.size()==0)
                {
                  writer.write(" ");
                  writer.flush();
                }
              } 
              catch (Exception e) 
              {
                 // ignore
              }
            }
        }
    };
    new Thread(runnable).start();
*/    
    
    ///////////// End New Code to send dummy tags to browser so connection is maintained /////////

        // Update the values in the different GUI-ELements
        //
        if(!"0".equals(event) && !"0".equals(control))
        {
            app.processParameter(context, request);
        }

        // Manage the event handling of the different control elements in the form
        //
        if(control != null && event != null)
        {
            app.processEvent(context, Integer.parseInt(control), event, eventValue);
            if(app.doForward!=null)
            {
               request.getRequestDispatcher(app.doForward).forward(request,response);
               app.doForward=null;
               return;
            }
        }
        stop.add("any");

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        Theme theme = ThemeManager.getTheme(app.getTheme());
        int marginLeft = theme.getNavigationWidth();
        if(!app.isNavigationVisible())
            marginLeft=0;
        %>
        <HTML>
        <head>
        <META Http-Equiv="Cache-Control" Content="no-cache">
        <META Http-Equiv="Pragma" Content="no-cache">
        <META Http-Equiv="Expires" Content="0">

        <link type="image/x-icon" rel="shortcut icon" href="<j:favIcon applName="<%=app.getApplicationDefinition().getName()%>" applVersion="<%=app.getApplicationDefinition().getVersion().toString()%>"/>">

        <!--REGEXP_START_REMOVE-->
        <script type="text/javascript" src="javascript/calendar.js"></script>
        <script type="text/javascript" src="javascript/calendar-setup.js"></script>
        <script type="text/javascript" src="javascript/debug.js" ></script>
        <script type="text/javascript" src="javascript/popupWindow.js" ></script>
        <script type="text/javascript" src="javascript/autosuggest.js"></script>
        <script type="text/javascript" src="javascript/brwsniff.js"></script>
        <script type="text/javascript" src="javascript/prototype.js"></script>
        <script type="text/javascript" src="javascript/moo.dom.js"></script>
        <script type="text/javascript" src="javascript/moo.fx.js"></script>
        <script type="text/javascript" src="javascript/webdav.js"></script>
        <script type="text/javascript" src="javascript/nicEdit.js"></script>
        <script type="text/javascript" src="javascript/jACOBBehaviour.js"></script>
        <script type='text/javascript' src='javascript/jACOBTable.js'></script>
        <script type='text/javascript' src='javascript/jACOBAlert.js'></script>
        <script type='text/javascript' src='javascript/jACOBTransparentMessage.js'></script>
        <script type='text/javascript' src='javascript/jACOBPrompt.js'></script>
        <script type='text/javascript' src='javascript/jACOBConfirm.js'></script>
        <script type='text/javascript' src='javascript/jACOBFFComboBox.js'></script>
        <script type='text/javascript' src='javascript/jACOBTimeline.js'></script>
        <script type='text/javascript' src='javascript/jACOBCalendar.js'></script>
        <!--REGEXP_END_REMOVE-->
        
        <script type="text/javascript" src="javascript/ie_common.js"></script>
        <script type="text/javascript" src="javascript/lang/calendar-<%=de.tif.jacob.i18n.I18N.getHttpCalendarLanguage(context.getLocale())%>.js"></script>

        <!-- UI-Plugin specific includes -->
        <% out.print(context.getAdditionalIncludes()); %>
        <!-- =========================== -->

        <link type="text/css" rel="stylesheet" href="themes/common.css">
        <link type="text/css" rel="stylesheet" href="<%=theme.getCSSRelativeURL()%>">

        <script id="serverPush"   type="text/javascript" src="javascript/blank.js" ></script>
        <SCRIPT type="text/javascript">

            new PeriodicalExecuter(function(pe) {
              new Ajax.Request("serverPush.jsp", {
                method: 'get',
                parameters: 
                {
                   browser:'<%=browserId%>'
                }
              });
            }, <%=Property.KEEPALIVEINTERVAL_APPLICATION.getIntValue()%>);

            <%String title="jACOB&nbsp;::&nbsp;";
            if( context.getApplication()!=null && context.getDomain()!=null && context.getForm()!=null)
                title= org.apache.commons.lang.StringUtils.capitalise(context.getApplication().getName())+"/"+((Domain)context.getDomain()).getI18NLabel(context.getLocale())+"/"+((HTTPForm)context.getForm()).getI18NLabel(context.getLocale());
            %>
            var MSG_COMMON_MAX_CHARACTER_LIMIT   = '<j:i18n2unicode id="MSG_COMMON_MAX_CHARACTER_LIMIT"/>';
            var BUTTON_LOGIN               = '<j:i18n id="BUTTON_LOGIN"/>';
            var BUTTON_COMMON_OK           = '<j:i18n id="BUTTON_COMMON_OK"/>';
            var BUTTON_COMMON_CLOSE        = '<j:i18n id="BUTTON_COMMON_CLOSE"/>';
            var BUTTON_COMMON_APPLY        = '<j:i18n id="BUTTON_COMMON_APPLY"/>';
            var BUTTON_COMMON_CANCEL       = '<j:i18n id="BUTTON_COMMON_CANCEL"/>';
            var BUTTON_COMMON_SHOW_DETAILS = '<j:i18n id="BUTTON_COMMON_SHOW_DETAILS"/>';
            var BUTTON_COMMON_HIDE_DETAILS = '<j:i18n id="BUTTON_COMMON_HIDE_DETAILS"/>';
            var APPLICATION_TERMINATED     = '<j:i18n id="APPLICATION_TERMINATED"/>';
            var SPOTLIGHT_SHADOW_TEXT  = '<j:i18n2unicode id="SPOTLIGHT_SHADOW_TEXT"/>';
            
            var parentTitle                = '<%=StringUtil.replace(StringUtil.replace(title,"\r",""),"\n","")%>';
            var userTheme                  = '<%=app.getTheme()%>';
            var browserId                  = '<%=browserId%>';
            var marginLeft                 = <%=marginLeft%>;
            var browserVisible             = <%=app.isSearchBrowserVisible(context)|| app.isInReportMode(context)%>;
            var RCS_ID_CONTENT             = "$Id: ie_content.jsp,v 1.36 2011/02/17 13:28:29 ibissw Exp $";
        </script>

        </head>
        <%
            if(app.isSearchBrowserVisible(context)|| app.isInReportMode(context))
                resizeFunction="resizeSearchBrowserCallback()";
            String navigation = theme.getNavigationRelativeURL();
        %>

<body id="body" name="body" onResize="<%=resizeFunction%>" scroll="no" style="overflow:hidden" onload="" class="contentBody">

        <jsp:include page="<%=navigation%>" flush="true"/>

        <DIV ID="calendarPopupDiv" STYLE="visibility:hidden;background-color:white;layer-background-color:white;"></DIV>
        <form id="eventForm" name="eventForm" target="" onkeypress="return formKeyPress(window.event);" action="<%=thisPage%>" method="post" class="submitForm" style="margin-left:<%=marginLeft%>;height:100%;width:100%;">
        <table cellspacing="0" cellpadding="0" height="100%" border="0" width="100%" style="height:100%;width:100%;margin:0px;padding:0px">
        <tr><td valign="top" height="1">
            <table id="searchBrowserTable" name="searchBrowserTable" cellspacing="0" cellpadding="0" border="0"  height="<%=(app.isSearchBrowserVisible(context))?app.getDividerPos():0%>" style="margin:0px;padding:0px;height:<%=(app.isSearchBrowserVisible(context))?app.getDividerPos():0%>;" width="100%" >
                <tr height="1">
                    <td class="toolbar_left" id="toolbar_left">&nbsp;</td>
                    <td class="toolbar"   id="toolbar" oncontextmenu="return false;" >
                        <%
                        if(app.isToolbarVisible())
                            app.renderToolbar(context);
                        else
                            out.write("<div style=\"width:100%;\">&nbsp;</div>");
                         %>
                    </td>
                </tr>
            <%if(app.isSearchBrowserVisible(context)|| app.isInReportMode(context)) {%>
                <tr oncontextmenu="return false;" >
                    <td ></td>
                    <td  class="formTabPane" id="SearchBrowserPlaceholder" valign="top" >
                        <%
                            app.renderSearchBrowserHTML(context);
                        %>
                    </td>
                </tr>
                <tr height="1" oncontextmenu="return false;" >
                    <td ></td>
                    <td class="searchActionFooter" id="searchActionFooter">
                         <%
                         app.renderSearchBrowserActionHTML(context);
                         %>
                    </td>
                </tr>
                <tr oncontextmenu="return false;" >
                    <td ></td>
                    <td class="searchTabFooter" id="searchTabFooter">
                        <%  app.renderSearchBrowserTabHTML(context);  %>
                    </td>
                </tr>
            <%}%>
            </table>
        </td></tr>
        <tr><td id="dividerFiller" name="dividerFiller" >
            <%if(app.isSearchBrowserVisible(context) && context.getForm()!=null){%>
                <hr onMouseOver="Divider_register();this.className='divider_over';" onMouseOut="this.className='divider'" class="divider" id="divider">
            <%}%>
        </td></tr>
        <tr height="100%"><td valign="top">
           <div id="formDiv" name="formDiv" style="position:relative;height:100%;overflow:auto;">
                <input type="hidden" id="dividerPos"         name="dividerPos"        value="<%=(app.isSearchBrowserVisible(context))?app.getDividerPos():0%>">
                <input type="hidden" id="focusElement"       name="focusElement"      value="">
                <input type="hidden" id="browser"            name="browser"           value="">
                <input type="hidden" id="control"            name="control"           value="">
                <input type="hidden" id="event"              name="event"             value="">
                <input type="hidden" id="eventValue"         name="eventValue"        value="">
                <input type="submit" id="submitButton" style="display:none;" name="submit"  >
                <%
                // render the HTML to the stream
                //
                app.calculateHTML(context);
                app.writeHTML(context, out);
                %>
                <%=context.getForeignFieldHTML()%>
                <%=context.getComboboxAdditionalHTML()%>
						    <img title="<j:i18n id="MSG_AUTOSUGGEST_PROVIDER"/>" src="./decorations/content_proposal_cue.gif" id="autosuggestBulb" STYLE="position:absolute;display:none;"/>
            </div>
            </td></tr>
          </table>
          <div  id="notepadShadow" class="notepadShadow" STYLE="display:none;"></div>
          <div id="notepadDiv"  class="notepadDiv" STYLE="z-index:1000;display:none;">
            <table cellspacing="0" cellpadding="0" width="100%" height="100%">
            <tr>
            <td height="1"><img  src="themes/<%=app.getTheme()%>/images/notepad.png" title="<j:i18n id="DIALOG_TITLE_NOTEPAD"/>" ></td>
            <td height="1" width="100%" align="left"><div id="notepadHeader" style="display:none;" >&nbsp;&nbsp;<j:i18n id="DIALOG_TITLE_NOTEPAD"/></div></td>
            <td height="1" align="right"><img id="notepadClose"  style="display:none;" src="themes/<%=app.getTheme()%>/images/close.png"  title="<j:i18n id="BUTTON_COMMON_CLOSE"/>" onClick="zoomOutNotepad()"/></td>
            </tr>
            <tr><td colspan="3" height="100%"><textarea onkeyup="checkMaxLength(this)" maxCharSize="500" name="notepadTextarea" id="notepadTextarea" style="position:absolute;width:100%;height:100%;overflow:auto;display:none;border:0px;"><%=StringUtil.toSaveString(jacobSession.getRuntimeProperty("notepadTextarea"))%></textarea></td></tr>
            </table>
          </div>
          
        </form>
        <img id="notepadIcon" class="notepadIcon"  STYLE="z-index:2000;display:none;" src="themes/<%=app.getTheme()%>/images/notepadicon.png"  onClick="zoomInNotepad()" title="<j:i18n id="DIALOG_TITLE_NOTEPAD"/>" >
      <%=context.getAdditionalHtml()%>
      <%=jacobSession.fetchAsynchronHtml()%>

    <div id="waitDiv" class="waitDiv" style="display:none;">
    <table>
      <tr>
	    <td valign="middle" ><img src="themes/<%=app.getTheme()%>/images/hourglass.gif" ></td>
	    <td class="waitDivText" valign="middle">
           <div><j:i18n id="WAITING_MESSAGE"/></div>
           <div id="statusDivText"></div>
        </td>   
      </tr>
      <tr>
        <td colspan="2" align="right" >
           <div id="abortDivText"  style="display:none;"><j:i18n id="BUTTON_COMMON_CANCEL"/></div>
        </td>
	  </tr>
    </table>
    </div>


    <!-- wird nur benoetigt um die Hoehe des Teasers aus dem Stylesheet zu ermitteln -->
    <div id="teaser"  class="teaser"  STYLE="display:none;"></div>
    <!--  wird als Anchor ben�tigt wenn man aus dem contextmenu einen dialog �ffnet -->
    <div id="dialogAnchor" name="dialogAnchor" STYLE="position:absolute;display:none;"></div>
    <div  id="contextShadow" class="shadow" style=""></div>
    <%=context.getContextmenuAdditionalHTML()%>
	<script>
		function afterPageLoad()
		{
			<%=context.getOnLoadJavascript()%>
			<%=jacobSession.fetchAsynchronOnLoadJavaScript()%>
		}
		<%=jacobSession.fetchAsynchronJavaScript()%>
	</script>
<%
    } // end synchronized(app)
}
catch( LicenseException exc)
{
    out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=licenseError.jsp?errorMessage="+exc.getMessage()+"\"></head></html>");
    return;
}
catch( UserException exc)
{
    out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=login.jsp?errorMessage="+exc.getMessage()+"\"></head></html>");
    return;
}
finally
{
    // clear the current ClientContext from the thread local variable!!!
    //
    Context.setCurrent(null);
    stop.add("any");
}
%>
<script type="text/javascript">
  var serverCalculationTime=<%=System.currentTimeMillis()-timeIn%>;
</script>

<script type="text/javascript">
  <%=resizeFunction%>;
  flipPage();
</script>

</body>
</html>
