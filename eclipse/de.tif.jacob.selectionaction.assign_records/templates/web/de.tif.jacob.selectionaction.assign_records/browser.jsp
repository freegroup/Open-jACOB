<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="j" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="java.util.*" %>

<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%
    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head><body></body></html>");
        return;
    }
    String browserId  = request.getParameter("browser");
    String dataBrowser= request.getParameter("dataBrowser");
    String linkTable= request.getParameter("linkTable");
    String guid= request.getParameter("guid");
   
    HTTPClientSession jacobSession = HTTPClientSession.get(request);
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
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);
    String themeId = app.getTheme();
    Theme theme = ThemeManager.getTheme(themeId);

   synchronized(app)
   {
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
  
        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataBrowser browser = acc.getBrowser(dataBrowser);
        List fields = browser.getBrowserDefinition().getBrowserFields();
        String browserTable = browser.getTableAlias().getName();
%>

<html>
   <head>
      <meta http-equiv="content-type" content="text/html; charset=utf-8" />
      
      <style type="text/css" title="currentStyle">
            #browser
            {
               border-top:1px solid gray;
            }
            tfoot
            {
               border-top:1px solid gray;
            }
            table.display thead th 
            {
                padding:0px !important;
            }
            .dataTables_length  
            {
              float:left;
              width:40%;
            }
            .dataTables_filter  
            {
              float:right;
              text-align:right;
              width:50%;
            }
            table.display  
            {
              clear:both;
              margin:0 auto;
              width:100%;
            }
            #browser_paginate { display:none; }
            #browser_info { display:none; }
      </style>
        <link  type="text/css"  rel="stylesheet" href="../../../../themes/common.css" />
        <link  type="text/css"  rel="stylesheet" href="../../../../<%=theme.getCSSRelativeURL()%>" />

      <script type="text/javascript" language="javascript" src="./js/jquery.js"></script>
      <script type="text/javascript" language="javascript" src="./js/jquery.dataTables.js"></script>
      
      <script type="text/javascript" charset="utf-8">

            var oTable;
            var gaiSelected =  [];
            $(document).ready(function() {

                $(window).resize(function() 
                {
                  window.location.href=window.location.href;
                });


                $('#submit_button').click( function() 
                {
                    $.ajax({
                        url: "rpc/submit.jsp",
                        <% if(linkTable==null) {%>
                           data: ({guid : "<%=guid%>", browser: "<%=browserId%>", pkeys: gaiSelected, browserTable:"<%=browserTable%>" }),
                        <%}else{ %>
                           data: ({guid : "<%=guid%>", browser: "<%=browserId%>", pkeys: gaiSelected, linkTable: "<%=linkTable%>" , browserTable:"<%=browserTable%>" }),
                        <%}%>
                        success: function(msg)
                        {
                            var emitter = opener.parent.frames['jacob_content1'].isVisible==true?opener.parent.frames['jacob_content1']:opener.parent.frames['jacob_content2'];
                            emitter.FireEvent(0,0);
                            window.close();
                        }
                    });
                } );
                $.fn.dataTableExt.oStdClasses.sStripOdd ="sbr_o";
                $.fn.dataTableExt.oStdClasses.sStripEven ="sbr_e";
                $.fn.dataTableExt.oStdClasses.sLength  ="dataTables_length caption_normal_search";
                $.fn.dataTableExt.oStdClasses.sFilter  ="dataTables_filter caption_normal_search";
               
                oTable = $('#browser').dataTable( {
                    "bProcessing": true,
                    "bJQueryUI": false, 
                    "bAutoWidth":true,
                    "bSort": false,
                    "bServerSide": true,
                    "sAjaxSource": "./rpc/data.jsp?browser=<%=browserId%>&dataBrowser=<%=dataBrowser%>",
                    "fnDrawCallback": function ( oSettings ) 
                     {
                         gaiSelected =  [];
                     },
                     "aoColumns": 
                     [
                        { "bVisible": 1 }, /* ID column */
						<%
				        for(int i=1; i<fields.size();i++)
				        {
				          if(i<(fields.size()-1))
				            out.println("null,");
				          else
  				            out.println("null");
				        }
						%>
                     ]
              });

              /* Click event handler */
              $('#browser tbody tr').live('click', function () 
              {
                  var aData = oTable.fnGetData( this );
                  var iId = aData[0];
                  
                  if ( jQuery.inArray(iId, gaiSelected) == -1 )
                  {
                      gaiSelected[gaiSelected.length++] = iId;
                  }
                  else
                  {
                      gaiSelected = jQuery.grep(gaiSelected, function(value) {
                          return value != iId;
                      } );
                  }
                  
                  $(this).toggleClass('sbr_s');
              } );
          } );
      </script>
      
   </head>
   <body style="margin:0px;padding:0px" id="searchBrowserTable">
              <div style="text-align:right"><button class="toolbarButton_normal" style="width:auto !important" id="submit_button"><j:i18n id="BUTTON_COMMON_ADD_TO_BROWSER"/></button></div>
              <table cellpadding="0" cellspacing="0" border="0" class="display" id="browser">
                  <thead>
                      <tr  class="sbh_container">
<%
        for(int i=0; i<fields.size();i++)
        {
          IBrowserField field = (IBrowserField)fields.get(i);
          String name = field.getName();
          String lable = field.getLabel();
          out.println("<th class=\"sbh\">"+lable+"</th>");
        }
%>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <td colspan="5" class="dataTables_empty">Loading data from server</td>
                  </tr>
               </tbody>
            </table>
   </body>
</html>

<%
   } // end synchronize(app)  
%>
