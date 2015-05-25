<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.util.json.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.zip.*" %>


<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>


<html xmlns="http://www.w3.org/1999/xhtml" style="width:100%;height:100%">
<head>
<script language="javascript" src="javascript/mootools.js" type="text/javascript"></script>
<script language="javascript" src="javascript/newsticker.js" type="text/javascript"></script>

<style type="text/css">
body 
{
height:100%;
width:100%;
overflow: hidden;
}

#NewsTicker
{
width:100%;
height:100%;
margin:0 auto;
overflow: hidden;
}


#NewsTicker h1
{
padding:6px; margin:0; border:0;
background:#dfe9d5;
color:#000000;
font-size:14px;
font-weight:bold;
}
#NewsVertical 
{
height: 100%;
display: block;
overflow: hidden;
position: relative;
}
#controller
{
padding:6px;
font-size:14px;
color:#666;
}
#play_scroll_cont
{
  display:none;
}
/* --------------- */
/* Ticker Vertical */
#TickerVertical 
{
width: 100%;
height: 100%;
display: block;
list-style: none;
margin: 0;
padding: 0;
}

#TickerVertical li 
{
display: block;
color: #333333;
text-align: left;
font-size: 14px;
margin: 0;
padding: 6px;
}

#TickerVertical li .NewsTitle
{
display: block;
color: #000000;
font-size: 14px;
font-weight:bold;
margin-bottom:6px;
}

#TickerVertical li .NewsTitle a:link,
#TickerVertical li .NewsTitle a:Visited 
{
display: block;
color: #000000;
font-size: 14px;
font-weight:bold;
margin-bottom:6px;
text-decoration:none;
}

#TickerVertical li .NewsTitle a:hover 
{
text-decoration:underline;
}
      
#TickerVertical li .NewsHeader
{
display: block;
color: #333333;
font-size: 14px;
margin:6px 0 14px 0;
}

</style>
</head>
<body>

<div id="NewsTicker">
  <table>
    <tr>
      <td><img border="0" src="../form_newsticker.png" /></td>
      <td valign="middle" ><a class="caption_normal_selected" style="margin-left:15px;text-align:left;white-space:nowrap;font-weight:bold;font-size:14pt;">Newsticker</a></td>
    </tr>
  </table>
  <h1><a href="#" target="_blank">In neuem Fenster öffnen</a></h1>
  <div id="controller">

    <div id="stop_scroll_cont"><a id="stop_scroll"><img src="images/stop.png" style="cursor:pointer" width="14" height="14" border="0" align="absmiddle" /></a> Ticker anhalten</div>
    <div id="play_scroll_cont"><a id="play_scroll"><img src="images/play.png" style="cursor:pointer" width="14" height="14" border="0" align="absmiddle"/></a> Ticker starten</div>
    </div>
<div id="NewsVertical" >
     <ul id="TickerVertical" >
<%
   IApplicationDefinition applicationDef = DeployMain.getApplication("@APPLICATION@","@VERSION@");
   Context context = new de.tif.jacob.core.JspContext(applicationDef);
   Context.setCurrent(context);

   IDataAccessor a = context.getDataAccessor().newAccessor();
   StringBuffer content = new StringBuffer("");
   try
   {
      IDataBrowser newsBrowser = a.getBrowser("active_newsBrowser");
      newsBrowser.search(IRelationSet.LOCAL_NAME);
      for(int i=0; i<newsBrowser.recordCount(); i++)
      {
        IDataBrowserRecord record = newsBrowser.getRecord(i);
        out.println("<li class=\"news_item\" style=\"margin-right:15px\">");
        out.println("<div style=\"border-width:1px;border-style:dashed;border-color:#555555;padding:5px\">");
        out.println("<span class=\"NewsHeader\">[");
        out.println(record.getSaveStringValue("browserCreate_date", context.getLocale()));
        out.println("]</span>");
        out.println("<span class=\"NewsTitle\">");
        out.println(StringUtil.htmlEncode(record.getSaveStringValue("browserHeadline")));
        out.println("</span>");
        out.println("<span class=\"NewsText\">");
        out.println(record.getSaveStringValue("browserContent"));
        out.println("</span>");
        out.println("</div>");
        out.println("<p style=\"clear: both;\">");
        out.println("</li>");
        content.append(record.toString());
      }
  }
  catch(Exception exc)
  {
    exc.printStackTrace();
  }
  Context.setCurrent(null);
%>

    </ul>
    </div>
</div>
</body>
<script>
<%
      Checksum checksum = new CRC32();
      byte bytes[] = content.toString().getBytes();
      checksum.update(bytes,0,bytes.length);
      long lngChecksum = checksum.getValue();
%>
      var crc32 = <%=lngChecksum%>;
</script>
</html>