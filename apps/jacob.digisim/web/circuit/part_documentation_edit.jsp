<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<!-- TinyMCE -->
<script language="javascript" type="text/javascript" src="../tiny_mce/tiny_mce.js"></script>
<script language="javascript" type="text/javascript">
	tinyMCE.init({
		mode : "textareas",
		theme : "advanced",
		plugins : "table,save,advhr,emotions,insertdatetime,zoom,searchreplace,contextmenu,paste",
		theme_advanced_buttons1_add_before : "save,separator",
		theme_advanced_buttons1_add : "fontselect,fontsizeselect",
		theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,zoom,separator,forecolor,backcolor",
		theme_advanced_buttons2_add_before: "cut,copy,paste,pastetext,pasteword,separator,search,replace,separator",
		theme_advanced_buttons3_add_before : "tablecontrols,separator",
		theme_advanced_buttons3_add : "emotions,advhr,separator,print,separator,ltr,rtl,separator",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "bottom",
		content_css : "example_word.css",
	    plugi2n_insertdate_dateFormat : "%Y-%m-%d",
	    plugi2n_insertdate_timeFormat : "%H:%M:%S",
		external_link_list_url : "example_link_list.js",
		external_image_list_url : "example_image_list.js",
		media_external_list_url : "example_media_list.js",
		file_browser_callback : "fileBrowserCallBack",
		paste_use_dialog : false,
		theme_advanced_resizing : false,
		theme_advanced_resize_horizontal : false,
		theme_advanced_link_targets : "_something=My somthing;_something2=My somthing2;_something3=My somthing3;",
		paste_auto_cleanup_on_paste : true,
		paste_convert_headers_to_strong : false,
		paste_strip_class_attributes : "all",
		paste_remove_spans : false,
		paste_remove_styles : false		
	});

	function fileBrowserCallBack(field_name, url, type, win) {
		// This is where you insert your custom filebrowser logic
		alert("Filebrowser callback: field_name: " + field_name + ", url: " + url + ", type: " + type);

		// Insert new URL, this would normaly be done in a popup
		win.document.forms[0].elements[field_name].value = "someurl.htm";
	}

        function reloadOpener()
        {
           if(window.opener)
           {
              window.opener.HelpManager.show(window.opener.HelpManager.currentType);
           }
        }
</script>
<!-- /TinyMCE -->
</head><body style="margin:0px;padding:0px"  onload="reloadOpener()">
<form method="post" action="part_documentation_edit.jsp">
	<textarea id="elm1" name="comment" rows="15" cols="80" style="width:90%; height:90%"><%
    if(!UserManagement.isLoggedInUser(request,response))
       return;

    String browserId  = request.getParameter("browser");
    String name       = request.getParameter("name");
    String comment    = request.getParameter("comment");

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
        return;
    }
    jacobSession.sendKeepAlive(browserId);

    IUser user = jacobSession.getUser();
   synchronized(app)
   {
      de.tif.jacob.core.JspContext context = new de.tif.jacob.core.JspContext(app.getApplicationDefinition(),user);
      Context.setCurrent(context);

      IDataAccessor a = context.getDataAccessor().newAccessor();
      // Try to find the documention.
      // FIRST: The current user is the owner of the object.
      //        We must load the docuemtnation from the current object version of the user
      //
      IDataTable myPartTable = a.getTable("my_part");
      myPartTable.qbeSetValue("name",name);
      if(myPartTable.search()==1)
      {
         IDataTableRecord currentRecord = myPartTable.getSelectedRecord();
         if(comment!=null)
         {
            IDataTransaction trans = a.newTransaction();
            try
            {
               currentRecord.setValue(trans,"comment",comment);
               trans.commit();
            }
            finally
            {
               trans.close();
            }
         }
         out.println(currentRecord.getSaveStringValue("comment"));
      }
      else
      {
        a = context.getDataAccessor().newAccessor();
        IDataTable partTable = a.getTable("part");
        partTable.qbeSetValue("name",name);
        partTable.qbeSetValue("state","released");
        if(partTable.search()==1)
        {
           IDataTableRecord currentRecord = partTable.getSelectedRecord();
         if(comment!=null)
         {
            IDataTransaction trans = a.newTransaction();
            try
            {
               currentRecord.setValue(trans,"comment",comment);
               trans.commit();
            }
            finally
            {
               trans.close();
            }
         }
           out.println(currentRecord.getSaveStringValue("comment"));
        }
      }
   }
%></textarea>
     <input type="hidden" name="browser" value="<%=browserId%>">
     <input type="hidden" name="name"    value="<%=name%>">
</form></body>
</html>