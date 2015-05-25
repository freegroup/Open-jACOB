<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ page session="false"%>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
 	<link type="text/css" rel="stylesheet" href="css/main.css" />
<link rel="stylesheet" href="css/tab.css" TYPE="text/css" MEDIA="screen">
    <script type="text/javascript" src="tabber.js" ></script>
 	<script>
function openwindow(url, width, height)
{
    var left     = (screen.width  - width)  / 2;
    var top      = (screen.height - height) / 2;

    property = 'left='+left+', top='+top+', toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width='+width+',height='+height;
    window.open(url, "_blank", property);
}

/* Optional: Temporarily hide the "tabber" class so it does not "flash"
   on the page as plain HTML. After tabber runs, the class is changed
   to "tabberlive" and it will appear.
*/
document.write('<style type="text/css">.tabber{display:none;}<\/style>');

var tabberOptions = {
  'manualStartup':true,
  'addLinkId': true
};

</script>

</head>

<%
 String pkey   = request.getParameter("pkey");

 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
 IDataTable objectTable = context.getDataTable("object");
 objectTable.qbeSetValue("pkey",pkey);
 objectTable.search("object"); // search with the "object" relationset
 IDataTableRecord object = objectTable.getSelectedRecord();
 object.getAccessor().propagateRecord(object,Filldirection.BOTH);
 IDataTableRecord responsibleDesign      = object.getLinkedRecord("responsible_design");
 IDataTableRecord responsibleTechnic     = object.getLinkedRecord("responsible_technic");
 IDataTableRecord responsibleDevelopment = object.getLinkedRecord("responsible_development");
 IDataTableRecord responsibleStandardization= object.getLinkedRecord("responsible_standardization");
 IDataTableRecord responsiblePilot       = object.getLinkedRecord("responsible_pilot");
 IDataTableRecord implementorHMI         = object.getLinkedRecord("implementor_hmi_template");
 IDataTableRecord implementorSPS         = object.getLinkedRecord("implementor_sps");
 IDataTableRecord implementorHardware    = object.getLinkedRecord("implementor_hardware");
 IDataTableRecord implementorHardwaretemplate = object.getLinkedRecord("implementor_hardwaretemplate");
 IDataTableRecord implementorSPS_S       = object.getLinkedRecord("implementor_sps_s");
 IDataTableRecord implementorSPS_F       = object.getLinkedRecord("implementor_sps_f");
 IDataTableRecord implementorRobot       = object.getLinkedRecord("implementor_robot");
 IDataTableRecord objectgroup            = object.getLinkedRecord("objectgroup");
%>


<body>

<div class="header_0">
	<table>
		<tr>
			<td class="title" width="100%">Pr&uuml;fblatt</td>
			<td><a href="#" onClick="openwindow('document.jsp?pkey=<%=pkey%>',800,400)">
				<img src="images/printer.png" border="0">Ausdruck</a>
  		    </td>
  		</tr>
  	</table>
</div>

<div class="object_name"><%=saveValue(object,"name") %></div>
<table><tr>
	<td>
		<div class="object_create_date"><b>angelegt am:</b> <%=saveValue(object,"create_date","-keine Angabe-") %></div>
	</td>
	<td style="width:50px;"></td>
	<td>
		<div class="object_last_change"><b>letzte &Auml;nderung:</b> <%=saveValue(object,"last_change","-keine Angabe-") %></div>
	</td>
</tr></table>	


<div class="tabber" id="tab1">
	<div class="tabbertab">
		<h2>Verantwortlichkeit</h2>
        <table>
        	<tr>
        		<td valign="top" class="responsible_design">Planung:</td>
	        	<td><%=saveValue(responsibleDesign,"fullname","-keine Angabe-") %><br>
	        	   (Tel. <%=saveValue(responsibleDesign,"phone","-keine Angabe-") %>)</td>
        	</tr>
        	<tr>
        		<td valign="top" class="responsible_technic">Technik:</td>
	        	<td><%=saveValue(responsibleTechnic,"fullname","-keine Angabe-") %><br>
	        	    (Tel. <%=saveValue(responsibleTechnic,"phone","-keine Angabe-") %>)</td>
        	</tr>
        	<tr>
        		<td valign="top" class="responsible_development">Entwicklung:</td>
	        	<td><%=saveValue(responsibleDevelopment,"fullname","-keine Angabe-") %><br>
	        	    (Tel. <%=saveValue(responsibleDevelopment,"phone","-keine Angabe-") %>)</td>
        	</tr>
        	<tr>
        		<td valign="top" class="responsible_standardization">Standardisierung:</td>
	        	<td><%=saveValue(responsibleStandardization,"fullname","-keine Angabe-") %><br>
	        	    (Tel. <%=saveValue(responsibleStandardization,"phone","-keine Angabe-") %>)</td>
        	</tr>
        	<tr>
        		<td valign="top" class="responsible_pilot">Pilot:</td>
	        	<td><%=saveValue(responsiblePilot,"fullname","-keine Angabe-") %><br>
	        	    (Tel. <%=saveValue(responsiblePilot,"phone","-keine Angabe-") %>)</td>
        	</tr>
        	<tr>
        	<td class="header_2" colspan="2">Bemerkung zur Verantwortlichkeit</td>
        	</tr>
        	<tr>
        	<td colspan="2"><%=saveValue(object,"responsible_remark","-keine Angabe-") %></td>
        	</tr>
       	</table>
	</div>
	
	
	
	<div class="tabbertab">
		<h2>Umsetzung</h2>
        <table>
        	<tr>
        		<td valign="top" class="implementor_sps">SPS Baustein:</td>
	        	<td><%=saveValue(implementorSPS,"name","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_sps_s">SPS Baustein S:</td>
	        	<td><%=saveValue(implementorSPS_S,"name","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_sps_f">SPS Baustein F:</td>
	        	<td><%=saveValue(implementorSPS_F,"name","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_hmi_template">HMI Template:</td>
	        	<td><%=saveValue(implementorHMI,"name","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_hardware">Hardware:</td>
	        	<td><%=saveValue(implementorHardware,"name","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_hardwaretemplate">Hardware Template:</td>
	        	<td><%=saveValue(implementorHardwaretemplate,"name","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_robot">Roboter:</td>
	        	<td><%=saveValue(implementorRobot,"name","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        	<td class="header_2" colspan="2">Bemerkung</td>
        	</tr>
        	<tr>
        	<td colspan="2"><%=saveValue(object,"implementation_remark","-keine Angabe-") %></td>
        	</tr>
        </table>
	</div>
	
	
	
	<div class="tabbertab">
		<h2>Pilotanwendung</h2>
        <table>
        	<tr>
        		<td valign="top" class="implementor_sps">Verantwortlich:</td>
	        	<td><%=saveValue(responsiblePilot,"fullname","-keine Angabe-")%><br>
	        	    (Tel. <%=saveValue(responsiblePilot,"phone","-keine Angabe-") %>)
	        	</td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_sps">Werk:</td>
	        	<td><%=saveValue(object,"pilot_site","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_sps">Geb&auml;ude:</td>
	        	<td><%=saveValue(object,"pilot_building","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_sps">Name:</td>
	        	<td><%=saveValue(object,"pilot_name","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_sps">Umgesetzt:</td>
	        	<td><%=saveValue(object,"pilot_implemented","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        		<td valign="top" class="implementor_sps">Geplant:</td>
	        	<td><%=saveValue(object,"pilot_planed","-keine Angabe-")%></td>
        	</tr>
        	<tr>
        	<td class="header_2" colspan="2">Bemerkung</td>
        	</tr>
        	<tr>
        	<td colspan="2"><%=saveValue(object,"pilot_comment","-keine Angabe-") %></td>
        	</tr>
        </table>
	</div>


</div>

</body>
<script>
tabberAutomatic(tabberOptions);
</script>
</html>



<%!

String saveValue(IDataTableRecord record, String field) throws Exception
{
	if(record==null)
		return "";
		
	return record.getSaveStringValue(field);
}

String saveValue(IDataTableRecord record, String field, String defaultValue) throws Exception
{
	if(record==null)
		return defaultValue;
	
	try
	{	
		if(record.getValue(field)==null)
			return defaultValue;
			
		return record.getSaveStringValue(field);
	}
	catch(Exception exc)
	{
		return "[[no such field]]";
	}
}


%>