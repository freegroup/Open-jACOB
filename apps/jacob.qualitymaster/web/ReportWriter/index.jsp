<html>
<head>
  <script type="text/javascript" src="prototype.js"></script>
  <script type="text/javascript" src="moo.dom.js"></script>
  <script type="text/javascript" src="scriptaculous.js"></script>
  <script type="text/javascript" src="effects.js"></script>
  <script type="text/javascript" src="dragdrop.js"></script>
  <script type="text/javascript" src="moo.fx.js"></script>
  <script type="text/javascript" src="textarea.js"></script>
  <script type="text/javascript" src="designer.js"></script>

<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.report.*" %>
<%@ page import="de.tif.jacob.report.impl.*" %>
<%@ page import="de.tif.jacob.report.impl.castor.*" %>
<%@ page import="de.tif.jacob.report.impl.castor.types.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%@ include file="guid.inc" %>
<%

   if(!UserManagement.isLoggedInUser(request,response))
   {
       return;
   }
   String browserId  = request.getParameter("browser");
   String duration_max   = "";
   ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
   de.tif.jacob.screen.impl.html.Application app = null;
   if(jacobSession!=null)
app=(de.tif.jacob.screen.impl.html.Application)jacobSession.getApplication(browserId);
   Theme theme = ThemeManager.getTheme(app.getTheme());

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

  synchronized(app)
  {
     ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
     Context.setCurrent(context);
     Report report = (Report)context.getProperty(REPORT_GUID);
     CastorLayout layout = report.getLayout_01();
%>

   <link type="text/css" rel="stylesheet" href="../../../../themes/common.css">
   <link type="text/css" rel="stylesheet" href="../../../../<%=theme.getCSSRelativeURL()%>">

<script>
 var browserId = "<%=browserId%>";
 var img = [];

 img["ASCENDING"] ="../../../../<%=theme.getImageURL("sortorder_ascending.png")%>";
 img["DESCENDING"] ="../../../../<%=theme.getImageURL("sortorder_descending.png")%>";
 img["NONE"] ="../../../../<%=theme.getImageURL("sortorder_none.png")%>";
 img["CLOSE"] ="../../../../<%=theme.getImageURL("close.png")%>";
 img["EDIT"] ="edit.png";
 img["PLUS"] ="plus.png";
 img["MINUS"] ="minus.png";
</script>

<style>

.dialog_header
{
 background-color:white;
}

#dialog_edit_column
{
 z-index:1000;
}

body
{
 font-family:sans-serif;
 overflow:hidden;
}

.sortorder
{
 border:1px solid black;
 cursor:pointer;
 vertical-align:middle;
 margin-right:5px;
}

.column_delete_button
{
 vertical-align:middle;
 cursor:pointer;
}

.column_edit_button
{
 vertical-align:middle;
 cursor:pointer;
 margin-left:10px;
 margin-right:10px;
}

.report_column
{
 padding-left:20px;
 padding-right:1px;
 cursor:move;
 border:1px solid gray;
 white-space:nowrap;
 float:left;
 position:relative;
}

.preformated
{
 position:absolute;
 color:transparent !important;
 margin:0 !important;
 padding:0 !important;
 font-family:sans-serif;
}

textarea.autosuggest
{
 position:relative !important;
 margin:0 !important;
 padding:0 !important;
 z-index:3;
 overflow:auto;
 width:100%;
 resize: none;
 border:1px dotted #d0d0d0;
 font-family:sans-serif;
 background-color:rgba(0,0,0,0.1);
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#15000000,endColorstr=#15000000);
}

.collapse_trigger
{
 cursor:pointer;
 padding:0px;
 margin:0px;
 margin-left:5px;
 vertical-align:middle;
}

#content_area
{
 position:absolute;
 top:110px;
 left:10px;
 width:500px;
 overflow-y:auto;
}

#palette
{
 position:absolute;
 top:0px;
 right:0px;
 width:200px;
 height:100%;
 background-color:rgba(0,0,0,0.1);
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#10000000,endColorstr=#10000000);
 zoom: 1;
 overflow:auto;
}

.pallette_heading
{
 padding:10px;
 padding-left:20px;
 background-color:rgba(0,0,0,0.2);
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#20000000,endColorstr=#20000000);
 zoom: 1;
}

.heading
{
 padding-left:20px !important;
}

#resize_handle
{
 position:absolute;
 top:0px;
 height:100%;
 position:absolute;
 width:10px;
 z-index:100;
 cursor:w-resize;
 background: transparent url(divider.png) repeat-y;
}

#grouped_by_fields
{
 list-style: none;
 margin:5px;
 margin:10;
 padding:0;
 padding-left:10px;
 white-space:nowrap;
}

.grouped_by_field
{
 cursor:default;
 white-space:nowrap;
}

#report_fields
{
 list-style: none;
 margin:5px;
 margin:10;
 padding:0;
 padding-left:10px;
}

.report_field
{
 cursor:move;
 white-space:nowrap;
}

.autosuggesttip
{
 color:green;
 font-size:8pt;
}

div.suggestions
{
 z-index:1000;
 background: #f9f8f7;
 border: 1px solid #999;
}

ul.suggestions
{
 list-style: none;
 margin:0px;
 padding:0px;
}

.suggestion
{
 list-style: none;
 margin:0px;
 padding:0px;
 cursor:pointer;
 font-family: verdana;
 font-size:11pt;
}

a.suggestion:hover
{
 background-color:#d0d0d0;
}

#report_detail
{
 background-color:rgba(0,0,0,0.1);
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#15000000,endColorstr=#15000000);
 zoom: 1;
 padding-bottom:10px;
}

#report_columns
{
  margin-top:10px;
  margin-bottom:10px;
  background-color:#f0f0f0;
  border:1px solid #c0c0c0;
}

.report_columns_icon
{
  vertical-align:middle;
  margin-right:5px;
}

#report_columns_container
{
  padding:5px;
}

.report_button
{
 text-decoration:underline;
 color:blue;
 padding:5px;
 font-size:8pt;
 cursor:pointer;
}

#report_columns_header
{
  padding:5px;
  font-size:8pt;
  border-bottom:1px solid #d0d0d0;
}

#group_parent
{
 overflow:hidden;
 opacity:1;
}

.group
{
 border-left:1px dashed #f0f0f0;
 overflow:hidden;
 position:relative;
 left:0px;
 background-color:rgba(0,0,0,0.1);
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#15000000,endColorstr=#15000000);
}

.group_divider
{
 width:15px;
 height:27px;
 cursor:w-resize;
 float:left;
 background: transparent url(handle.png) no-repeat;
}

.header_title
{
  font-size:8pt;
  padding-right:10px;
  padding-left:10px;
}

.footer_title
{
  font-size:8pt;
  padding-right:10px;
  padding-left:10px;
}

.section
{
 margin-left:0px;
 margin-top:10px;
 margin-bottom:10px;
}

.section_header
{
 background: transparent url(sectionbar_bg.png) repeat-x;
 height:27px;
}

.section_delete_button
{
 font-size:8pt;
 border:1px ident silver;
 cursor:pointer;
 vertical-align:middle;
}

</style>

<%@ include file="suggestions.inc" %>


</head>
<body class="contentBody" id="formDiv">
<img border="0" src="ReportWriter_layout.png" style="position: absolute; top: 5px; left: 15px; width: 77px; height: 68px;"/>
<a style="text-align: left; white-space: nowrap; position: absolute; top: 5px; left: 110px; width: 400px; height: 40px; font-size: 20pt;" class="caption_normal_selected">Report Layout</a>
<button class="button_normal" id="save_report"   style="position: absolute; top: 53px; left: 110px; width: 85px; height: 20px;">Save</button>
<button class="button_normal" id="cancel_report" style="position: absolute; top: 53px; left: 210px; width: 85px; height: 20px;">Cancel</button>

<div id="textmetrics" style="color:transparent;padding:0px;margin:0px">ASDFGHJIKLOPQWERTYXCVBNMKLP1234567890</div>
<div id="content_area">
   <div class="section" id="first_page_header">

     <div class="section_header">
       <img  src="minus.png" class="collapse_trigger" id="first_page_header_collapse_trigger"/>
       <span class="header_title">First Page Header</span>
       <span class="autosuggesttip" id="first_page_header_tip">press ctrl+space for autosuggest</span>
     </div>

     <div id="first_page_header_collapse_area">
         <textarea id="first_page_header_textarea"  class="autogrow autosuggest" rows="1" wrap="OFF"><%=layout.getPrologue()!=null?layout.getPrologue().getText():""%></textarea>
     </div>

   </div>


   <div class="section" id="page_header">

     <div class="section_header">
       <img  src="minus.png" class="collapse_trigger" id="page_header_collapse_trigger"/>
       <span class="header_title">Page Header</span>
       <span class="autosuggesttip" id="page_header_tip">press ctrl+space for autosuggest</span>
     </div>

     <div id="page_header_collapse_area">
         <textarea id="page_header_textarea" class="autogrow autosuggest" rows="1" wrap="OFF"><%=layout.getPageHeader()!=null?layout.getPageHeader().getText():""%></textarea>
     </div>

   </div>

   <div id="report_detail" class="section">
     <div id="report_detail_header" class="section_header">
        <span class="header_title">Report Detail</span>
        <span id="add_group" class="report_button">Add Group</span>
     </div>
     <div id="group_parent">

       <% renderLayoutPart(theme, out, layout.getPart(),0); %>

     </div>
   </div>
   <div class="section" id="page_footer">

     <div class="section_header">
       <img src="minus.png" class="collapse_trigger" id="page_footer_collapse_trigger"/>
       <span class="footer_title">Page Footer</span>
       <span class="autosuggesttip" id="page_footer_tip">press ctrl+space for autosuggest</span>
     </div>

     <div id="page_footer_collapse_area">
         <textarea  id="page_footer_textarea"  class="autogrow autosuggest"  rows="1" wrap="OFF"><%=layout.getPageFooter()!=null?layout.getPageFooter().getText():""%></textarea>
     </div>

   </div>


   <div class="section" id="last_page_footer">
     <div class="section_header">
       <img  src="minus.png" class="collapse_trigger" id="last_page_footer_collapse_trigger"/>
       <span class="footer_title">Last Page Footer</span>
       <span class="autosuggesttip" id="last_page_footer_tip">press ctrl+space for autosuggest</span>
     </div>

     <div id="last_page_footer_collapse_area">
         <textarea  id="last_page_footer_textarea"  class="autogrow autosuggest" rows="1" wrap="OFF"><%=layout.getEpilogue()!=null?layout.getEpilogue().getText():""%></textarea>
     </div>
   </div>

</div>

<%@ include file="palette.inc" %>

<div id="resize_handle"></div>

<div>
 <div id="dialog_edit_column" style="display:none;border:1px solid black">
   <div class="dialog_header">&nbsp;&nbsp;&nbsp;&nbsp;Column Properties</div>
   <div class="dialog_content" style="height:300px;width:500px">
     <div style="position:absolute;top:90px;left:60px;width:230px">Label</div>
     <input name="name" type="text" value="" id="column_label" style="position:absolute;top:110px;left:60px;width:230px">
     <div style="position:absolute;top:140px;left:60px;width:230px">Ident</div>
     <input name="name" type="text" value="" id="column_ident" style="position:absolute;top:160px;left:60px;width:100px">
     <div style="position:absolute;top:190px;left:60px;width:230px">Width</div>
     <input name="name" type="text" value="" id="column_width" style="position:absolute;top:210px;left:60px;width:100px">
   </div>
   <div class="dialog_buttonbar">
     <button id="save_column_button">Save</button><button id="close_dialog_button">Close</button>
   </div>
 </div>
</div>

</body>
</html>

<%
}
%>


<%!
void renderLayoutPart(Theme theme, javax.servlet.jsp.JspWriter out, CastorLayoutPart part, int index) throws Exception
{
  CastorGroup group = part.getGroup();
  CastorLayoutColumns columns = part.getColumns();

  if(columns!=null)
  {
     out.println("<div id=\"report_columns\">");
     out.println("<div id=\"report_columns_header\">");
     out.print("<img src=\"columns.png\" class=\"report_columns_icon\" />");
     out.print("Report Columns");
     out.print("<span id=\"report_columns_add\" class=\"report_button\">Add Column</span>");
     out.print("</div>");
     out.print("<div id=\"report_columns_container\">");
     for(int i=0; i< columns.getColumnCount();i++)
     {
        CastorLayoutColumn c=columns.getColumn(i);
        String key =c.getTableAlias()+"."+c.getField();
        out.println("<div id=\"report_column_"+key+"\" class=\"caption_normal_search report_column\">");
        out.println(key);
        out.println("<img src=\"edit.png\" class=\"column_edit_button\" id=\"section_"+index+"_edit\"/>");
        out.println("<img src=\"../../../../"+theme.getImageURL("close.png")+"\" class=\"column_delete_button\" />");
        out.println("</div>");
     }

     out.println("<span id=\"float_breaker\" style=\"display: block; clear: left\"></span>");

     out.print("</div>");
     out.print("</div>");

  }
  else
  {
     out.println("<div class=\"group\" id=\"group_"+index+"\" style=\"left:"+part.getIdent()+"px;\">");
     out.println("  <div class=\"section\" id=\"section_"+index+"\">");
     out.println("  <div class=\"section_header\" >");
     out.println("      <span class=\"group_divider\"></span>");
     out.println("      <img src=\"minus.png\" id=\"section_header_"+index+"_collapse_trigger\" class=\"collapse_trigger\"/>");
     out.println("      <span class=\"header_title\" >Grouped by</span>");
     out.println("      <span class=\"group_by report_button\" id=\"group_by_"+index+"\" >"+group.getTableAlias()+"."+group.getField()+"</span>");
     out.println("      <img src=\"../../../../"+theme.getImageURL("close.png")+"\" class=\"section_delete_button\" id=\"section_"+index+"_delete\"/>");
     out.println("      <span id=\"section_header_"+index+"_tip\" class=\"autosuggesttip\">Press ctrl+space for autosuggest</span>");
     out.println("  </div>");
     out.println("  <div class=\"section_collapse_area\" id=\"section_header_"+index+"_collapse_area\">");
     out.print("      <textarea id=\"section_header_"+index+"_textarea\" class=\"autogrow autosuggest section_header_textarea\" rows=\"1\"  wrap=\"OFF\">");
     if(group.getHeader()!=null)
        out.print(group.getHeader().getText());
     out.println("</textarea>");
     out.println("  </div>\n");

     renderLayoutPart(theme, out, group.getPart(), index+1);

     out.println("\n\n  <div class=\"section_header\" >");
     out.println("      <img src=\"minus.png\" id=\"section_footer_"+index+"_collapse_trigger\" class=\"collapse_trigger\"/>");
     out.println("      <span class=\"footer_title\" >Group Footer</span>");
     out.println("      <span id=\"section_footer_"+index+"_tip\" class=\"autosuggesttip\">Press ctrl+space for autosuggest</span>");
     out.println("  </div>");
     out.println("  <div class=\"section_collapse_area\" id=\"section_footer_"+index+"_collapse_area\">");
     out.print("      <textarea id=\"section_footer_"+index+"_textarea\" class=\"autogrow autosuggest section_footer_textarea\" rows=\"1\"  wrap=\"OFF\">");
     if(group.getFooter()!=null)
        out.print(group.getFooter().getText());
     out.println("</textarea>");
     out.println("  </div>");
     out.println("  </div>");
     out.println("</div>");
  }
}
%>