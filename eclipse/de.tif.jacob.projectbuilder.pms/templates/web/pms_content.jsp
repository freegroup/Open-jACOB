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
 	<script>
function openwindow(url, width, height)
{
    var left     = (screen.width  - width)  / 2;
    var top      = (screen.height - height) / 2;

    property = 'left='+left+', top='+top+', toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width='+width+',height='+height;
    window.open(url, "_blank", property);
}

 	</script>
</head>

<%
 String pkey   = request.getParameter("pkey");

 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
 IDataTable productsTable = context.getDataTable("process");
 productsTable.qbeSetValue("pkey",pkey);
 productsTable.search("process"); // search with the "process" relatonset
 IDataTableRecord process      = productsTable.getSelectedRecord();
 process.getAccessor().propagateRecord(process,Filldirection.BOTH);
 IDataTableRecord processOwner = process.getLinkedRecord("process_owner");
 IDataTableRecord processResponsible = process.getLinkedRecord("process_responsible");
 IDataTableRecord processOwnerSubstitute = process.getLinkedRecord("process_owner_substitute");
%>

<body>

<div class="header_0">
	<table>
		<tr>
			<td class="title" width="100%">Prozessblatt</td>
			<td><a href="#" onClick="openwindow('createDefect.jsp?pkey=<%=pkey%>',800,400)">
					<img width="20px" src="images/escalation.png" border="0">
			    Feedback</a>
		  </td></tr></table></div>

<div class="process_title"><%=process.getSaveStringValue("title") %></div>
<table><tr>
	<td>
		<div class="process_valid_from"><b>g&uuml;ltig von:</b> <%=saveValue(process,"valid_from","-keine Angabe-") %></div>
	</td>
	<td style="width:50px;"></td>
	<td>
		<div class="process_valid_to"><b>g&uuml;ltig bis:</b> <%=saveValue(process,"valid_to","-keine Angabe-") %></div>
	</td>
</tr></table>	

<div class="header_1">I. Einordnung in die Prozess-Struktur</div>
<%=renderParent(process,"<ul><li class=\"close\">"+saveValue(process,"title")+"</li></ul>")%>


<div class="header_1">II. Ausf&uuml;hrende Gesellschaft/Abteilung</div>
<div class="workgroup_name">
<%
	IDataTable workgroupProcessTable = context.getDataTable("workgroup_process");
	for(int i=0; i<workgroupProcessTable.recordCount();i++)
	{
	   IDataTableRecord current= workgroupProcessTable.getRecord(i);
     out.write(saveValue(current,"workgroup_name")+"<br>\n");
	}
%>
</div>

<div class="header_1">III. Verantwortlichkeit</div>

<div class="process_owner"><b>Prozesseigner:</b>&nbsp;<%=saveValue(processOwner,"name","-keine Angabe-") %>(Tel. <%=saveValue(processOwner,"phone","-keine Angabe-") %>)</div>
<div class="process_owner_substitute"><b>Stellvertretter:</b>&nbsp;<%=saveValue(processOwnerSubstitute,"name","-keine Angabe-") %>(Tel. <%=saveValue(processOwnerSubstitute,"phone","-keine Angabe-") %>)</div>
<div class="process_responsible"><b>Verantwortlicher:</b>&nbsp;<%=saveValue(processResponsible,"name","-keine Angabe-") %>(Tel. <%=saveValue(processResponsible,"phone","-keine Angabe-") %>)</div>

<div class="header_2">Bemerkung zur Verantwortlichkeit</div>
<div class="process_responsible_remark"><%=saveValue(process,"responsible_remark","-keine Angabe-") %></div>

<div class="header_2">Am Prozess beteiligt</div>


<div class="header_1">IV. Aspekte des Prozesses</div>
<div class="process_aspect"><%=saveValue(process,"aspect","-keine Angabe-") %></div>


<div class="header_1">V. Bemerkung zum Prozess</div>
<div class="process_remark"><%=saveValue(process,"remark","-keine Angabe-") %></div>


<div class="header_1">VI. Beschreibung des Prozesses</div>
<div class="process_description"><%=saveValue(process,"description","-keine Angabe-") %></div>


<div class="header_1">VII. Prozessbezogene Vorgaben</div>
<div class="process_remark"><%=saveValue(process,"guideline","-keine Angabe-") %></div>


<div class="header_1">VIII. Meldewesen/Verhalten in Notf&auml;lle</div>
<div class="process_case_of_need"><%=saveValue(process,"case_of_need","-keine Angabe-") %></div>


<div class="header_1">IX. Auditierung</div>
<table class="audit">
<tr><td  class="audit_date"><b>Datum</b></td><td  class="audit_date"><b>Kommentar</b></td></tr>
<%
	IDataTable auditTable = context.getDataTable("audit");
	for(int i=0; i<auditTable.recordCount();i++)
	{
	   IDataTableRecord current= auditTable.getRecord(i);
     out.write("<tr><td class=\"audit_date\">"+saveValue(current,"date")+"</td><td  class=\"audit_comment\">"+saveValue(current,"comment")+"</td></tr>\n");
	}
%>
</table>
<div class="header_1">X. Gefahr- und Risikobeurteilung</div>
<div class="process_risk_evaluation"><%=saveValue(process,"risk_evaluation","-keine Angabe-") %></div>


<div class="header_1">XI. Nachverfolgung</div>
<table class="defect">
<tr><td  class="defect_create_date"><b>Datum</b></td><td  class="defect_subject"><b>Betreff</b></td></tr>
<%
	IDataTable defectTable = context.getDataTable("defect");
	for(int i=0; i<defectTable.recordCount();i++)
	{
	   IDataTableRecord current= defectTable.getRecord(i);
     out.write("<tr><td class=\"defect_create_date\">"+saveValue(current,"create_date")+"</td><td  class=\"defect_subject\">"+saveValue(current,"subject")+"</td></tr>\n");
	}
%>
</table>


</body>
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
		
	if(record.getValue(field)==null)
		return defaultValue;
		
	return record.getSaveStringValue(field);
}

String renderParent(IDataTableRecord record, String content) throws Exception
{
 IDataAccessor acc= record.getAccessor().newAccessor();
 IDataTable processTable = acc.getTable("process");
 processTable.qbeSetKeyValue("pkey",record.getValue("parent_process_key"));
 if(processTable.search()==1)
 {
		IDataTableRecord parent = processTable.getSelectedRecord();
		return renderParent(parent,"<ul>"+
			       "<li class=\"open\">"+
			       "<a href=\"pms_content.jsp?pkey="+saveValue(parent,"pkey")+"\" >"+
			       saveValue(parent,"title")+
			       "</a>"+
			       content+
			       "</li>"+
			       "</ul>") ;
 }
 return content;
}

%>