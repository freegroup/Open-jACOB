<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/Strict.dtd">
<html>
<head>
  <script type="text/javascript" src="prototype.lite.js"></script>
  <script type="text/javascript" src="moo.fx.js"></script>
  <script type="text/javascript" src="moo.fx.utils.js"></script>

  <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
  <title></title>
  
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%
    if(!UserManagement.isLoggedInUser(request,response))
    {
        return;
    }
    String browserId  = request.getParameter("browser");
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
    String themeId = app.getTheme();
    Theme theme = ThemeManager.getTheme(themeId);

%>
<link type="text/css" rel="stylesheet" href="../../../themes/common.css" />
<link type="text/css" rel="stylesheet" href="../../.<%=theme.getCSSRelativeURL()%>" />

<style>
div.milestoneheader
{
   background-color:#f0f0f0;
   border:1px solid #d0d0d0;
   padding:5px;
}

h2.milestoneheader
{
   color: #15428B;
   font-size:18px;
   margin:0px;
   padding:0px;
}
span.liefertermin
{
   color: #15428B;
   font-size:15px;
}
span.erfuellung
{
   color: #15428B;
   font-size:15px;
}
span.status
{
   color: #15428B;
   font-size:15px;
}

.incidentPkey
{
   color: #15428B;
   font-size:20px;
   background-color: #fafafa;
}

.incidentSubject
{
   color: #15428B;
   font-size:20px;
   background-color: #fafafa;
   font-style:bold;
}

div.incident
{
   width:100%;
   overflow:hidden;
   height:0px;
}

table.incident
{
   border:1px solid #f0f0f0;
   width:100%;
}

.more
{
  cursor:pointer;
  text-decoration:underline;
  color:#2552dB;
  font-size:8pt;
}

</style>
<script>
function toggleMilestone(/*:HTMLElement*/ divId)
{
   new fx.Height($(divId) , {duration: 800}).toggle();
}
</script>
</head>
<body>
<table>
<tr>
  <td><img border="0" src="form_milestone.png" /></td>
  <td><div class="caption_normal_search" style="font-size:12pt;font-weight:bold;padding-left:10px">Milestones</div></td>
</tr>
</table>
<br>
<%
   synchronized(app)
   {
      Map<String,String> statusCSS = new HashMap<String,String>();

      statusCSS.put("Red","color:red");
      statusCSS.put("Yellow","color:yellow");
      statusCSS.put("Green","color:green");
      statusCSS.put("Done","color:black;font-style:bold");

      ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
      Context.setCurrent(context);

      IDataAccessor a = context.getDataAccessor().newAccessor();

      IDataBrowser milestoneBrowser = a.getBrowser("_milestoneEntryBrowser");
      milestoneBrowser.search(IRelationSet.DEFAULT_NAME);
      for(int i=0;i<milestoneBrowser.recordCount();i++)
      {
        IDataBrowserRecord currentRecord = milestoneBrowser.getRecord(i);
        String pkey     = currentRecord.getSaveStringValue("browserPkey");
        String subject  = currentRecord.getSaveStringValue("browserSubject");
        String planDone = currentRecord.getSaveStringValue("browserPlandone");
        String state    = currentRecord.getSaveStringValue("browserState");

        // Alle Meldungen zu diesem Milestone holen
        //
        IDataAccessor acc = a.newAccessor();
        IDataTable incidentTable = acc.getTable("incident");
        incidentTable.qbeSetKeyValue("milestone_key",pkey);
        int count = incidentTable.search();
        int qaOk=0;
        for(int ii=0; ii<incidentTable.recordCount();ii++)
        {
          IDataTableRecord incident = incidentTable.getRecord(ii);
          String incidentState = incident.getSaveStringValue("state");
          if(incidentState.equals("QA")||incidentState.equals("Rejected") || incidentState.equals("Done"))
            qaOk++;
        }
        int percentage = (int)((100.0/Math.max(1.0,incidentTable.recordCount())*qaOk));

        out.println("<div class=\"milestoneheader\">");
        out.println("<h2  class=\"milestoneheader\">"+subject+"</h2>");
        out.println("<span class=\"liefertermin\">Geplanter Liefertermin: "+planDone+"</span><br>");
        out.println("<span class=\"erfuellung\" >Erf&uuml;llung: "+percentage+" %</span><br>");
        out.println("<span class=\"status\" style=\""+statusCSS.get(state)+"\">Status: "+state+"</span><br>");
        out.println("<span onclick=\"toggleMilestone('"+pkey+"')\"  class=\"more\" >Meldungen...</span>");
        out.println("</div><br>");

        out.println("<div id='"+pkey+"' class=\"incident\">");
        for(int ii=0; ii< count ; ii++)
        {
          IDataTableRecord incidentRecord = incidentTable.getRecord(ii);
          IDataTableRecord categoryRecord = incidentRecord.getLinkedRecord("incidentCategory");
          IDataTableRecord customerRecord = incidentRecord.getLinkedRecord("customer");
          String incidentPkey        = incidentRecord.getStringValue("pkey");
          String incidentSubject     = incidentRecord.getSaveStringValue("subject");
          String incidentDate        = incidentRecord.getSaveStringValue("create_date");
          String incidentState       = incidentRecord.getSaveStringValue("state");
          String incidentDescription = incidentRecord.getSaveStringValue("description");
          String createrName         = customerRecord.getSaveStringValue("fullname");
          %>
          <table style="width:100%">
          <tr>
          <td class="left_border">&nbsp;</td>
          <td>
            <table class="incident" >
            <tr>
              <td class="incidentPkey"><%=incidentPkey%></td>
              <td class="incidentSubject" style="width:auto" colspan="2"><%=incidentSubject%></td>
            </tr>
            <tr>
              <td></td>
            </tr>
            <tr>
              <td></td>
              <td class="caption_normal_search" width="1" >Ersteller:</td>
              <td class="text_normal" ><%=createrName%></td>
            </tr>
            <tr>
              <td></td>
              <td class="caption_normal_search" width="1" >Angelegt am:</td>
              <td class="text_normal" ><%=incidentDate%></td>
            </tr>
            <tr>
              <td></td>
              <td class="caption_normal_search" width="1" >Status:</td>
              <td class="text_normal" ><%=incidentState%></td>
            </tr>
            <tr>
              <td></td>
              <td valign="top" class="caption_normal_search" width="1" >Beschreibung:</td>
              <td valign="top" class="longtext_normal" ><pre style="margin:0px;padding:0px;"><%=incidentDescription%></pre></td>
            </tr>
            <tr>
              <td colspan="3"><br></td>
            </tr>
            </table>
          </td>
          </tr>
          </table>
          <br>

          <%
        }
        out.println("</div><br><br>");
      }
   }
%>
</body>
