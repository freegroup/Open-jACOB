<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ page session="false"%>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.messaging.*" %>
<%@ page import="de.tif.jacob.core.definition.IRelationSet" %>
<%@ page import="java.util.*" %>

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
 String searchTerm   = StringUtil.toSaveString(request.getParameter("searchTerm"));

 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
 IDataTable tagTable = context.getDataTable("tagging");
 tagTable.qbeClear();
 IDataBrowser browser = context.getDataAccessor().getBrowser("documentBrowser");
 String[] terms= searchTerm.split("[|]");
 String whereClause="";
 String location="<a class=\"location_link\" href=\"index.jsp\">HOME</a>";
 String searchPart="";
 for(int i=0;i<terms.length;i++)
 {
  if(terms[i].length()>0)
  {
    location = location +"-&gt;";
    if(i>0)
    {
      whereClause = whereClause+" AND tag LIKE '%"+terms[i]+"%'";
      searchPart= searchPart+"|"+terms[i];
    }
    else
    {
      whereClause = "tag LIKE '%"+terms[i]+"%'";
      searchPart=terms[i];
    }
    location = location+"<a class=\"location_link\" href=\"index.jsp?searchTerm="+searchPart+"\">"+terms[i]+"</a>";
  }
 }
 browser.searchWhere(IRelationSet.LOCAL_NAME,whereClause);
 long countx = tagTable.search();
%>
  
<body>
<div class="location_bar">
You are Here: <%=location%>
<img class="location_image" src="images/filefind.png">
<a class="location_admin" target="_blank" href="../../../login.jsp?forceApp=docfinder">admin</a>
<a class="location_about" href="about.jsp">about</a>
<a class="location_help" href="help.jsp">help</a>
</div>
<div class="tag_bar" >
<div class="tag_bar_header"><table width="100%"><tr><td width="100%">Navigation Tags</td><td ><a href="#" onclick="openwindow('taghelp.html',400,300)"><img border="0" src="images/help.png"></a></td></tr></table></div>
<%
  List result = new ArrayList();
  int maxCount = 1;
	if(searchTerm.length()==0)
	{
		for(int i=0 ; i<tagTable.recordCount() ; i++)
		{
			IDataTableRecord record = tagTable.getRecord(i);
			String tag = record.getSaveStringValue("tag");
			int count = recordCount(context, whereClause,tag);
			maxCount = Math.max(maxCount, count);
			if(count>0)
				result.add(new entry(count,tag));
		}
	}
	else
	{
		for(int i=0 ; i<tagTable.recordCount() ; i++)
		{
			IDataTableRecord record = tagTable.getRecord(i);
			String tag = record.getSaveStringValue("tag");
			if(searchTerm.indexOf(tag)==-1)
			{
				int count = recordCount(context, whereClause,tag);
				maxCount = Math.max(maxCount, count);
				if(count>0)
					result.add(new entry(count,tag));
			}
		}
  }
  Iterator iter = result.iterator();
  while(iter.hasNext())
  {
  	entry e = (entry)iter.next();
  	double percent = 100+Math.max(25.0,((100.0/maxCount)*e.count));
		if(searchTerm.length()==0)
			out.write("<a title=\""+e.count+" Documents\" href=\"index.jsp?searchTerm="+e.tag+"\" style=\"font-size:"+percent+"%;\" class=\"tag\">"+e.tag+"</a>\n");
		else
			out.write("<a title=\""+e.count+" Documents\" href=\"index.jsp?searchTerm="+searchTerm+"|"+e.tag+"\" style=\"font-size:"+percent+"%;\" class=\"tag\">"+e.tag+"</a>\n");		
  }
%>
</div>

<div class="document_bar">
<%
for(int i=0;i<browser.recordCount();i++)
{
  IDataBrowserRecord record = browser.getRecord(i);
  String filename   = record.getSaveStringValue("browserFile_name");
  String docId      = record.getSaveStringValue("browserPkey");
  String createDate = record.getSaveStringValue("browserCreate_date");
  String mimeType   = Message.getMimeType(filename);
  String s          = (i%2==0)?"even":"odd";
  
  String deleteDate = record.getStringValue("browserRequest_for_delete_date");
  if(deleteDate==null)
  {
	  out.write("<table class=\"document "+s+"\"><tr>");
	  out.write("<td valign=\"top\"><a href=\"document.jsp?pkey="+docId+"&filename="+filename+"\"><img border=0 src=\"images/"+mimetype2image(mimeType)+"\"></a></td>");
	  out.write("<td valign=\"top\">&nbsp</td>");
	  out.write("<td width=\"100%\" ><a href=\"document.jsp?pkey="+docId+"&filename="+filename+"\">"+filename+"</a><img title=\"Click to delete this document!\" onclick=\"openwindow('delete.jsp?pkey="+docId+"',500,350)\" src=\"images/delete.png\"><br><small>"+mimeType+"<br>"+createDate+"</small></td>");
	  out.write("</tr></table>");
	}
	else
	{
	  out.write("<table class=\"document "+s+"\"><tr>");
	  out.write("<td valign=\"top\"><a href=\"document.jsp?pkey="+docId+"&filename="+filename+"\"><img border=0 src=\"images/"+mimetype2image(mimeType)+"\"></a></td>");
	  out.write("<td valign=\"top\">&nbsp</td>");
	  out.write("<td width=\"100%\" ><a href=\"document.jsp?pkey="+docId+"&filename="+filename+"\">"+filename+"</a><img title=\"Document has ben marked as deleteable!\" onclick=\"this.src='images/delete.png';openwindow('undelete.jsp?pkey="+docId+"',500,350)\" src=\"images/undelete.gif\"><br><small>"+mimeType+"<br>"+createDate+"</small></td>");
	  out.write("</tr></table>");
	}
}
%>

</div>
</body>
</html>

<%!
  static Map mimemap = new HashMap();
  static int MAX_FONTSIZE=30;
  
  static
  {
    mimemap.put("application/octet-stream","unknown.png");
    mimemap.put("application/msword","wordprocessing.png");
    mimemap.put("application/pdf","pdf.png");
    mimemap.put("image/jpeg","image.png");
    mimemap.put("image/png","image.png");
    mimemap.put("image/gif","image.png");
  }
  
  class entry
  {
  	public final int count;
  	public final String tag;
  	public entry(int count,String tag)
  	{
  		this.count=count;
  		this.tag=tag;
  	}
  }
  
  String mimetype2image(String mimetype)
  {
    String image = (String)mimemap.get(mimetype);
    if(image==null)
      return (String)mimemap.get("application/octet-stream");
    return image;
  } 

  int recordCount(Context context, String currentSearchTerm, String additionalTag) throws Exception
  {
    IDataBrowser browser = context.getDataAccessor().newAccessor().getBrowser("documentSmallBrowser");
  
    if(currentSearchTerm.length()>0)
            browser.searchWhere(IRelationSet.LOCAL_NAME,currentSearchTerm+" AND tag LIKE '%"+additionalTag+"%'");
    else
            browser.searchWhere(IRelationSet.LOCAL_NAME," tag LIKE '%"+additionalTag+"%'");
            
    return browser.recordCount();
  }
%>
