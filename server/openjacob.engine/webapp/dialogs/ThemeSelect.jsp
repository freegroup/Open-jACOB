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
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="de.tif.jacob.screen.impl.tag.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*" %>
<%@ page import="de.tif.jacob.util.file.Directory" %>
<%@ page contentType="text/html; charset=ISO-8859-1" %>
<% 
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=login.jsp\"></head></html>");
        return;
    }
%>
<%
    String thisApp  =  request.getContextPath();
    String thisPage =  request.getContextPath()+request.getServletPath();
    String thisPath =  thisPage.substring(0,thisPage.lastIndexOf("/")+1);

    String      theme      = request.getParameter("theme");
    String      apply      = request.getParameter("apply");
    String      browserId  = request.getParameter("browser");
    Application app        = (Application)jacobSession.getApplication(browserId);

   
    if(app==null)
    {
        return;
    }
    else
    {
        if(theme==null)
            theme = app.getTheme();
        if(apply!=null)
        {
            app.setTheme(theme);
            out.println("<html><head><script type='text/javascript' src='../javascript/"+BrowserType.getType(request)+"_common.js' ></script></head><script>FireEvent(window,'close')</script></html>");
            return;
        }
    }
    %>
<HTML >
    <title><j:dialogTitle id="DIALOG_TITLE_THEMESELECT"/></title>
    <HEAD>
        <META Http-Equiv="Cache-Control" Content="no-cache">
        <META Http-Equiv="Pragma" Content="no-cache">
        <META Http-Equiv="Expires" Content="0">
        
        <script>var userTheme = '<%=theme%>';</script>
        <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
        <link   type="text/css"        rel="stylesheet" href="../themes/common.css" />
        <link   type="text/css"        rel="stylesheet" href="../themes/<%=theme%>/custom.css" />
    </HEAD>
<body class="dialog">
  <form id="submitForm" name="submitForm" action="<%=thisPage%>" method="POST">
    <input type="hidden" name="browser" value="<%=browserId%>" >
    <table  cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">
    <tr>
        <td  height="100%"  valign="top">
            <div width="100%" height="100%"  class="dialog_content">
                <table width="100%" height="100%" border="0">
                    <tr height="1">
                      <td width="1" colspan="3"  >
                        <span  class="caption_normal_search" style="font-size:19pt;font-weight:bold;"><j:i18n id="LABEL_THEMESELECT_APPLICATION_THEME"/></span>
                      </td>
                    </tr>
                    <tr height="1">
                      <td width="100%" colspan=3  >
                        <hr>
                      </td>
                    </tr>
                    <tr >
                      <td>
                        <span  class="caption_normal_search" style="font-size:12pt;font-weight:bold;"><j:i18n id="LABEL_THEMESELECT_THEME"/></span>
                      </td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td>
                        <span  class="caption_normal_search" style="font-size:12pt;font-weight:bold;"><j:i18n id="LABEL_THEMESELECT_PREVIEW"/></span>
                      </td>
                    </tr>
                    <tr>
                        <td width="1%" valign="top">
                        <select class="listbox_normal" name="theme" size="3" style="widht:100%;height:100%" onchange="getObj('submitForm').submit();" >
                            <%
                            Theme[] themes = ThemeManager.getThemes();
                            for(int i=0;i<themes.length;i++)
                            {
                              
                              if(themes[i].getId().equals(theme))
                                out.println("<option  class=\"listboxitem_selected\" selected >"+themes[i].getDisplayName()+"</option>");
                              else
                                out.println("<option  class=\"listboxitem\" value='"+themes[i].getId()+"'>"+themes[i].getDisplayName()+"</option>");
                            }
                            %>
                           </select>
                        </td>
                        <td width="10%"> </td>
                        <td valign="top" width="100%">
                            <center>    
                            <div style="height:90%;width:90%">  
                            <div class="formTabPane" style="height:100%;">
                            <fieldset class="group_normal">
                            <legend><j:i18n id="LABEL_THEMESELECT_EXAMPLE"/></legend>
                            <br>
                            <table height="100%" width="100%">
                                <tr>
                                    <td valign="right">
                                        <span  class="caption_normal_search" >FAX</span>
                                    </td>
                                    <td>
                                        <input style="width:100%;height:21px;" type="text" value="<j:i18n id="LABEL_THEMESELECT_TEXT_EDITABLE"/>" class="text_normal editable_inputfield">
                                    </td>
                                <tr>
                                <tr>
                                    <td valign="right" >
                                        <span  class="caption_normal_search" >FAX</span>
                                    </td>
                                    <td>
                                        <input style="width:100%;height:21px;" type="text" value="<j:i18n id="LABEL_THEMESELECT_TEXT_READONLY"/>" READONLY class="text_disabled">
                                    </td>
                                <tr>
                                <tr>
                                    <td></td>
                                    <td align="right">
                                        <input style="width:107px;height:22 px;" type="button"  value="<j:i18n id="LABEL_THEMESELECT_NORMAL_BUTTON"/>" class="button_normal" >
                                        <input style="width:107px;height:22 px;" type="button"  value="<j:i18n id="LABEL_THEMESELECT_DISABLED_BUTTON"/>" DISABLED class="button_disabled" >
                                    </td>
                                </tr>
                            </table>    
                            </fieldset>
                            <a href='#' class="downtab"><j:i18n id="LABEL_THEMESELECT_TAB1"/></a><a href='#' class=downtabSelected><j:i18n id="LABEL_THEMESELECT_TAB2"/></a>
                            <br>
                            <br>
                            </div>
                            </div>
                            </center>   
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td  align="right" valign="bottom">
            <div class="dialog_buttonbar">
                <input type=button  value="<j:i18n id="BUTTON_COMMON_APPLY"/>" onClick="document.location.href='<%=thisPage%>?browser=<%=browserId%>&theme=<%=theme%>&apply=true';" class="button_emphasize_normal" >
                <input type=button  value="<j:i18n id="BUTTON_COMMON_CANCEL"/>" onClick="window.close();" class="button_normal" >
            </div>
        </td>
    </tr>
    </table>
    </form>
  <body>
</html>
