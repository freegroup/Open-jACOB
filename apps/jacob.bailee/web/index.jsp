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
   <link rel="icon" href="./favicon.png" type="image/png">
   
   <script type="text/javascript" src="js/prototype.js"></script>
   <script type="text/javascript" src="js/scriptaculous.js?load=effects,builder"></script>
   <script type="text/javascript" src="js/lightbox.js"></script>
   <script type="text/javascript" src="js/dialog.js"></script>
   <script type="text/javascript" src="js/editTagDialog.js"></script>

   <link rel="stylesheet" href="css/lightbox.css" type="text/css" media="screen" />
 	<link type="text/css" rel="stylesheet" href="css/main.css" />
 	<script>
String.prototype.trim = function( )
{
  return( this.replace(new RegExp("^([\\s]+)|([\\s]+)$", "gm"), "") );
};

   var createFieldDialog=null;

   function openwindow(url, width, height)
	{
	    var left     = (screen.width  - width)  / 2;
	    var top      = (screen.height - height) / 2;
	
	    property = 'left='+left+', top='+top+', toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width='+width+',height='+height;
	    window.open(url, "_blank", property);
	}

 	</script>
 	
   <!--[if lte IE 8]>
   <style type="text/css">
      .gallery li
      {
         display: inline; /* For IE */
      }
   </style>
   <![endif]-->

</head>

<%
 String searchTerm   = StringUtil.toSaveString(request.getParameter("searchTerm"));

 IApplicationDefinition applicationDef =DeployMain.getApplication("@APPLICATION@","@VERSION@");
 Context context = new de.tif.jacob.core.JspContext(applicationDef);
 Context.setCurrent(context);
 IDataTable tagTable = context.getDataTable("tagging");
 IDataTable documentTable = context.getDataTable("document");
 IDataBrowser browser = context.getDataAccessor().getBrowser("documentBrowser");

 tagTable.qbeClear();
 documentTable.qbeClear();
 
 documentTable.qbeSetValue("thumbnail","!null");
 
 String[] terms= searchTerm.split("[|]");
 String whereClause="";
 
 for(int i=0;i<terms.length;i++)
 {
  if(terms[i].length()>0)
  {
    if(i>0)
      whereClause = whereClause+" AND tag LIKE '%"+terms[i]+"%'";
    else
      whereClause = "tag LIKE '%"+terms[i]+"%'";
  }
 }
 if(whereClause.length()>0)
    browser.searchWhere(IRelationSet.LOCAL_NAME,whereClause);
    
 long countx = tagTable.search();
 
// String location="<a class=\"location_link\" href=\"index.jsp\">Reset</a>";
 String location="";
 for(int i=0;i<terms.length;i++)
 {
    if(terms[i].length()>0)
    {
       String searchPart="";
       for(int ii=0;ii<terms.length;ii++)
       {
        if(terms[ii].length()>0 && !terms[ii].equals(terms[i]))
        {
          if(searchPart.length()>0)
            searchPart= searchPart+"|"+terms[ii];
          else
            searchPart=terms[ii];
        }
       }
       location = location+"<span class=\"filter_tag\">"+terms[i]+"<a href=\"index.jsp?searchTerm="+searchPart+"\"><img title=\"Remove the ["+terms[i]+"] keyword from search filter\" src=\"images/remove_tag.png\"></a></span>";
    }
 }

%>
  
<body>
<div class="location_bar">
Your current filter &gt;&gt; <%=location%>
</div>
<div class="tag_bar" >
<div class="tag_bar_header"><table width="100%"><tr><td width="100%">Navigation Tags</td><td ><a href="#" onclick="openwindow('taghelp.html',400,300)"><img border="0" src="images/help.png"></a></td></tr></table></div>
<%
 Comparator comp = new Comparator() {
      public int compare( Object o1, Object o2) {
        return ((entry)o1).tag.compareTo(((entry)o2).tag);
      }
    };

  SortedSet result = new TreeSet(comp);
  int maxCount = 1;
  int minCount=Integer.MAX_VALUE;
  int w = 8; // Smalles Font Size
  int y=  30;  // Largest Font Size
  
    if(searchTerm.length()==0)
    {
        for(int i=0 ; i<tagTable.recordCount() ; i++)
        {
            IDataTableRecord record = tagTable.getRecord(i);
            String tag = record.getSaveStringValue("tag");
            int count = recordCount(context, whereClause,tag);
            maxCount = Math.max(maxCount, count);
         minCount = Math.min(minCount, count);
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
     int fontSize =  (int)(( ((e.count-minCount) * (y-w)) / (maxCount-minCount) ) + w);
     if(searchTerm.length()==0)
      out.write("<a  href=\"index.jsp?searchTerm="+e.tag+"\" style=\"font-size:"+fontSize+"pt;\" class=\"tag\">"+e.tag+"</a>\n");
     else
      out.write("<a  href=\"index.jsp?searchTerm="+searchTerm+"|"+e.tag+"\" style=\"font-size:"+fontSize+"pt;\" class=\"tag\">"+e.tag+"</a>\n");     
  }
%>
</div>

<div class="document_bar">
   
<%
if(browser.recordCount()>0)
{
   out.write("<ul class=\"gallery\">\n");
   for(int i=0;i<browser.recordCount();i++)
   {
     IDataBrowserRecord record = browser.getRecord(i);
     String filename   = record.getSaveStringValue("browserFile_name");
     String docId      = record.getSaveStringValue("browserPkey");
     String tag      = record.getSaveStringValue("browserTag");
     String createDate = record.getSaveStringValue("browserCreate_date");
     int width = record.getintValue("browserThumbnail_width");
     int height = record.getintValue("browserThumbnail_height");
     String mimeType   = Message.getMimeType(filename);
     
     out.write("<li>");
     out.write("<div class=\"image_button_bar\"><img class=\"image_button\" src=\"images/tag_blue.png\" onclick=\"showEditTagDialog('"+docId+"')\" /></div>");
     out.write("<a href=\"./cache/"+docId+"_normalized.png\" rel=\"lightbox[bailee]\">\n");
     if(width<140 && height<140)
      out.write("<img title=\""+tag+"\" rel=\"lightbox\" class=\"image_optimal\" src=\"./cache/"+docId+"_optimal.png\">\n");
     else
      out.write("<img title=\""+tag+"\"  rel=\"lightbox\" class=\"image\" src=\"./cache/"+docId+"_optimal.png\">\n");
     out.write("</a>\n");
     out.write("</li>\n");
   }
   out.write("</ul>");
}
else
{
   out.write("<div id=\"tag_cloud_hint\">Use the Tag Cloud to browser your images</div>"); 
   out.write("<img id=\"hint_arrow\" src=\"./images/hint_arrow.png\">"); 
}
%>
</div>

<div>
  <div id="dialog_tagging" style="display:none;border:1px solid black">
    <div class="dialog_header">&nbsp;&nbsp;&nbsp;&nbsp;tag</div>
    <div class="dialog_content" style="height:100px;width:350px">
      <div class="caption" style="position:absolute;left:25px;top:20px">Keywords of the Image</div>
      <input name="tag" type="text" value="" id="tag" style="position:absolute;top:50px;left:25px;width:300px">
    </div>
    <div class="dialog_buttonbar">
      <button id="button_close_tagging">Close</button>
    </div>
  </div>
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
