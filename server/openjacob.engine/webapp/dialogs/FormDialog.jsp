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
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="de.tif.jacob.screen.dialogs.form.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.io.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.core.data.DataDocumentValue" %>

<% 
    List /* FileItem */ items=null;
    Map parameters = new HashMap();

    // Bei dem ersten (initialen) Aufruf ist der Request noch keine mimetype/multipart. Erst wenn der Benutzter in dem
    // Dialog einen Button drckt (z.B.submit) wird ein multipart Reqeust erzeugt. Es muessen somit beide Fï¿½le  beachtet werden.
    //
    boolean isMultipart = FileUpload.isMultipartContent(request);
    if(isMultipart)
    {
        DiskFileUpload upload = new DiskFileUpload();
        items = upload.parseRequest(request);
        Iterator iter = items.iterator();
        while (iter.hasNext())
        {
            FileItem item = (FileItem) iter.next();
            if (item.isFormField())
            {
                parameters.put(item.getFieldName(),item.getString("ISO-8859-1"));
            }
            else
            {
                String name = item.getFieldName();
                byte[] content = IOUtils.toByteArray( item.getInputStream());
                String fileName = item.getName();
                // remove the path from the filename
                //
                fileName = StringUtils.replace(fileName,"\\",File.separator);
                fileName = StringUtils.replace(fileName,"/",File.separator);
                fileName = FilenameUtils.getName(fileName);
                DataDocumentValue document = DataDocumentValue.create(fileName, content);
                parameters.put(name,document);
            }
        }
    }
    else
    {
        Iterator iter = request.getParameterMap().keySet().iterator();
        while(iter.hasNext())
        {
            String key    = (String)iter.next();
            String value = request.getParameter(key);
            parameters.put(key,value);
        }
    }
    
    String browserId = (String) parameters.get("browser");
    HTTPClientSession jacobSession = HTTPClientSession.get(session, browserId);
    if (jacobSession == null)
    {
        out.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=../login.jsp\"></head></html>");
        return;
    }
    
    String submitId = (String)parameters.get("submitId");
    
    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Close the dialog and submit the data to the dialog callback
    //
    ////////////////////////////////////////////////////////////////////////////////////
    if(submitId!=null && submitId.length()>0)
    {
        String guid = (String)parameters.get("guid");
        // Create a new file upload handler
        FormDialog dialog = (FormDialog)jacobSession.getDialog(guid);

        Iterator iter = parameters.keySet().iterator();
        while (iter.hasNext())
        {
            String item = (String) iter.next();
            dialog.setResponseValue(item,parameters.get(item));
        }
        // Dokumente, welche zuvor hochgeladen worden sind, mssen bertragen werden.
        // Diese werden nicht bei jedem Requestzyklus mitgesendet werden.
        //
        iter = dialog.getTempValues().keySet().iterator();
        while (iter.hasNext())
        {
            String item = (String) iter.next();
            Object value =  dialog.getTempValues().get(item);
            if(value instanceof DataDocumentValue)
	            dialog.setResponseValue(item,value);
        }
        String event=(String)parameters.get("buttonId");
        if(event==null)
            event="ok";
       %>
        <HTML>
        <HEAD>
                    <META Http-Equiv="Cache-Control" Content="no-cache">
                    <META Http-Equiv="Pragma" Content="no-cache">
                    <META Http-Equiv="Expires" Content="0">
    
            <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
        </HEAD>
        <body>
        <script>
            FireEvent('<%=guid%>','<%=event%>');
            window.close()
        </script>
        </body>
        <%
        return;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // The user has not pressed the submit button. We can display the dialog
    // It is possible that the user has pressed a normal form button. 
    // Find the form button and send the event to the callback.
    //
    ////////////////////////////////////////////////////////////////////////////////////
    //
    String        actionEmitter = (String)parameters.get("actionEmitter");
    String        guid          = (String)parameters.get("guid");
    Application   app           = (Application)jacobSession.getApplication(browserId);
    ClientContext context       = new ClientContext(jacobSession.getUser(),out, app, browserId);
    ClientContext.setCurrent(context);
    FormDialog  dialog     = (FormDialog)jacobSession.getDialog(guid);

    // fire the event in the pressed form button
    //
    if(actionEmitter!=null)
    {
        Object emitter=dialog.getActionEmitter(actionEmitter);
        if(emitter instanceof IFormActionEmitter)
        {
            ((IFormActionEmitter)emitter).onAction(context,dialog,parameters);
        }
        else if (emitter instanceof de.tif.jacob.screen.event.IAutosuggestProvider)
        {
        }
    }
    
    
    String submitButtons="<table id=\"buttons\" style=\"position:absolute;\"><tr>";
    String dialogHtml= dialog.getHtml(parameters);
    Iterator iter= dialog.getSubmitButtons().iterator();
    while(iter.hasNext())
    {
        FormDialog.FormDialogButton button=(FormDialog.FormDialogButton)iter.next();
        submitButtons= submitButtons+"<td><input id=\"form_button_"+button.id+"\" style=\"width:100px;\" dialog=\"yes\" type=\"button\" class="+(button.emphasize?"'button_emphasize_normal'":"'button_normal'")+" onClick=\"getObj('submitId').value='true';getObj('buttonId').value='"+button.id+"';getObj('reportForm').submit()\" value=\""+StringUtil.htmlEncode(button.label)+"\"></td>";
    }
    if(dialog.getCancelButton()!=null)
    {
    	submitButtons=submitButtons+"<td><input type=\"button\" style=\"width:100px;\" class=\"button_normal\" onClick=\"window.close();\" value=\""+StringUtil.htmlEncode(dialog.getCancelButton())+"\"></td>";
    }
    submitButtons=submitButtons+"</tr></table>";
    String themeId = app.getTheme();
    Theme theme = ThemeManager.getTheme(themeId);
%>
<html xmlns="http://www.w3.org/1999/xhtml"  >
    <head>

        <title><j:windowTitlePrefix applName="<%=app.getName()%>"/><%=StringUtil.htmlEncode(dialog.getTitle())%></title>
        <script>var userTheme = '<%=app.getTheme()%>';</script>
    
        <script type="text/javascript" src="../javascript/<j:browserType/>_common.js" ></script>
        <link   type="text/css"        rel="stylesheet" href="../themes/common.css" />
        <link  type="text/css"  rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>" />
        

        <script id="serverPush"   type="text/javascript" src="../javascript/blank.js" ></script>
        <SCRIPT type="text/javascript" >
            window.setInterval("loadRemotePage('serverPush.jsp?browser=<%=browserId%>&guid=<%=guid%>')",<%=de.tif.jacob.core.Property.KEEPALIVEINTERVAL_DIALOG.getIntValue()*1000%>);
            window.focus();
        </SCRIPT>
        <script type="text/javascript" src="../javascript/highlight.js"></script>
        <script type="text/javascript">
          initHighlightingOnLoad();
        </script>
<style>

pre code {
}


pre code, 
.ruby .subst 
{
  color: black;
}

.string,
.function .title,
.class .title, 
.tag .attribute .value,
.css .rules .value,
.preprocessor,
.ruby .symbol,
.built_in,
.sql .aggregate,
.django .template_tag,
.django .variable,
.smalltalk .class 
{
  color: #800;
}

.comment,
.java .annotation,
.template_comment 
{
  color: #888;
}

.number,
.regexp,
.javascript .literal,
.smalltalk .symbol,
.smalltalk .char 
{
  color: #080;
}

.javadoc,
.ruby .string,
.python .decorator,
.django .filter .argument,
.smalltalk .localvars,
.smalltalk .array,
.css .attr_selector,
.xml .pi 
{
  color: #88F;
}

.keyword,
.css .id,
.phpdoc,
.function .title,
.class .title,
.vbscript .built_in,
.sql .aggregate,
.rsl .built_in,
.smalltalk .class,
.xml .tag .title 
{
  font-weight: bold;
}

</style>
    </head>
<body id="b" class="dialog" scroll="no" onresize="layout()" onload="layout();">
            <form name="reportForm"  id="reportForm"  ENCTYPE="multipart/form-data" action="FormDialog.jsp" method="POST">
                <input type="hidden" name="buttonId"      id="buttonId"      value="">
                <input type="hidden" name="submitId"      id="submitId"      value="">
                <input type="hidden" name="actionEmitter" id="actionEmitter" value="">
                <input type="hidden" name="browser"                          value="<%=browserId%>">
                <input type="hidden" name="guid"                             value="<%=guid%>">
            <%=dialogHtml%>
            </form>
            <div id="buttonbar" class="dialog_buttonbar" style="position:absolute;">
            </div>
             <%=submitButtons%>
</body>
<script>
var browserId='<%=browserId%>';

var guid='<%=guid%>';

var mustResize = true;

function layout()
{
   var allElements = document.getElementById("reportForm").childNodes;
   var elements = new Array();
   var x=0;
   for(var i=0;i<allElements.length;i++)
   {
	   if(allElements[i].getAttribute)
	   {
	   	if(allElements[i].getAttribute('formLayout')=='true')
	   	{
		   	elements[x]=allElements[i];
		   	// vor dem resize den Elemente die vorberechnete Grï¿½e entfernen und den Elemente
		   	// Ihre selbstberechnete min. Grï¿½e zuweisen.
		   	// 
        var lObj = new LayerObject(allElements[i].id);
        lObj.css.height= "auto";
        lObj.css.width = "auto";
		   	x=x+1;
		  }
		 }	  
   }
   // angeforderte hoehe kopieren
   for(var i=0;i<def_rows.length;i++)
       min_height[i]= def_rows[i];
       
   for(var i=0;i<def_cols.length;i++)
       min_width[i]= def_cols[i];
       
   // Die Grundweite und hoehe der Elemente welche kein SPAN haben ermitteln.
   for(var i=0;i<elements.length;i++)
   {
      var lObj = new LayerObject(elements[i].id);
   		var x_pos  = parseInt(elements[i].getAttribute("x_pos"));
   		var y_pos  = parseInt(elements[i].getAttribute("y_pos"));
   		var x_span = parseInt(elements[i].getAttribute("x_span"));
   		var y_span = parseInt(elements[i].getAttribute("y_span"));
   		// ermitteln der derzeitig zur verfgung stehenden weite
   		min_width[x_pos]=Math.max(min_width[x_pos],parseInt(lObj.width));
   		if(min_width[x_pos]<0)
	   		dumpObject(lObj.obj);
   		// Falls das Elemente eine y_span hat, dann versuchen ob es auf die ganze
   		// hï¿½e rein passt. Wenn nicht wird der Teil der 'grow' angegeben hat verï¿½dert.
   		// Wenn kein Element 'grow' angegeben hat, dann wird das letzte Element verï¿½dert
   		if(y_span>1)
   		{
   			var eHeight = parseInt(lObj.height);         // gewuenschte Element Hoehe
   			var cHeight = getHeight(y_pos,y_pos+y_span); // derzeitige Hoehe
   		  if(cHeight<eHeight)
   		  {
   		    var diff= eHeight-cHeight;
   		    min_height[y_pos+y_span-1] = min_height[y_pos+y_span-1]+diff;
   		  }
   		}
   		else
   		{
	   		min_height[y_pos]=Math.max(min_height[y_pos],parseInt(lObj.height));
	   	}
   }
   
   // Alle Elemente wurde in das minimal Grid gelegt. Jetzt wird das Grid
   // an die Fenstergröße angepasst
   //
   var gridHeight= getGridHeight();
   for(var i=0;i<def_rows.length;i++)
   {
   	if(def_rows[i]==-1) // die Spalte gefunden welche wachsen darf
   	{
		   var canvasHeight = parent.innerHeight-1- 30;
		   if(!canvasHeight)
		      canvasHeight = new LayerObject("b").height-5-30;
		   min_height[i]= min_height[i]+(canvasHeight-gridHeight);
		   mustResize=false;
   	   break;
   	}
   }
   var gridWidth= getGridWidth();
   for(var i=0;i<def_cols.length;i++)
   {
   	if(def_cols[i]==-1) // die Spalte gefunden welche wachsen darf
   	{
		   var canvasWidth = parent.innerWidth-1;
		   if(!canvasWidth)
		      canvasWidth = new LayerObject("b").width-5;
		   min_width[i]= min_width[i]+(canvasWidth-gridWidth);
		   mustResize=false;
   	   break;
   	}
   }

   for(var i=0;i<elements.length;i++)
   {
      var lObj = new LayerObject(elements[i].id);
   		var x_pos  = parseInt(elements[i].getAttribute("x_pos"));
   		var y_pos  = parseInt(elements[i].getAttribute("y_pos"));
   		var x_span = parseInt(elements[i].getAttribute("x_span"));
   		var y_span = parseInt(elements[i].getAttribute("y_span"));
   		var width  = getWidth(x_pos,x_pos+x_span);
   		var height = getHeight(y_pos,y_pos+y_span);
   		lObj.css.width  = width+"px";
   		lObj.css.height = height+"px";
   		lObj.css.top    = getY(y_pos)+"px";
   		lObj.css.left   = getX(x_pos)+"px";
   }
   
   var buttonbar = new LayerObject("buttonbar");
   buttonbar.moveTo(0,getGridHeight()+2);
   buttonbar.css.width=(getGridWidth()+2)+"px";
   var buttons = new LayerObject("buttons");
   buttons.moveTo(getGridWidth()-buttons.width,getGridHeight()+4);
   if(<%=dialog.getDebug()%>)
	   drawGrid();
   
   if(mustResize)
   {
	   var gridHeight= getGridHeight()+30; // +30 wegen der Toolbar
	   var gridWidth = getGridWidth();
	   if(parent.outerWidth)
	   {
		   tmp1 = parent.outerWidth - parent.innerWidth;
		   tmp2 = parent.outerHeight - parent.innerHeight;
		   window.resizeTo(tmp1+gridWidth+1, tmp2+ gridHeight+1);
		 }
		 else
		 {
		   window.resizeBy(-(document.body.offsetWidth-gridWidth)+5,-(document.body.offsetHeight- gridHeight)+5);
	   }
	 }
}

function getX(col )
{
  var r=0;
	for(var i=0;i<col;i++)
	 r= r+min_width[i];
	return r;
}

function getY(row )
{
  var r=0;
	for(var i=0;i<row;i++)
	 r= r+min_height[i];
	return r;
}

function getWidth(from, to)
{
	var r =0;
	for(var i=from;i<to;i++)
	 r= r+min_width[i];
  return r;
}

function getHeight(from, to)
{
	var r =0;
	for(var i=from;i<to;i++)
	 r= r+min_height[i];
  return r;
}

var gridElements= new Array();
function drawGrid()
{
    // alte Linien erstmal entfernen bevor man neue zeichnet
    //
    for(var i=0;i<gridElements.length;i++)
    	removeDiv(gridElements[i]);
    gridElements= new Array();
    
		var gridHeight=getGridHeight();
		var gridWidth=getGridWidth();

    var counter=0;
    
    // draw the cols first
    var x=0;
    for(var i=0;i<=min_width.length;i++)
    {
       var newLine = addDiv();
       gridElements[counter++]=newLine;
       var lobj = new LayerObject(newLine.id);
       lobj.moveTo(x,0);
       lobj.css.height=gridHeight+"px";
       lobj.css.width="1px";
       if(i<min_width.length)
	       x=x+min_width[i];
    }
    
    var y=0;
    for(var i=0;i<=min_height.length;i++)
    {
       var newLine = addDiv();
       gridElements[counter++]=newLine;
       var lobj = new LayerObject(newLine.id);
       lobj.moveTo(0,y);
       lobj.css.height="1px";
       lobj.css.width=gridWidth+"px";
       if(i<min_height.length)
	       y=y+min_height[i];
    }
}

function getGridWidth()
{
    var gridWidth=0;
    for(var i=0;i<min_width.length;i++)
        gridWidth=gridWidth+min_width[i];
    return gridWidth;
}

function getGridHeight()
{
    var gridHeight=0;
    for(var i=0;i<min_height.length;i++)
        gridHeight=gridHeight+min_height[i];
    return gridHeight;
}

var divCounter=0;
function addDiv()
{
    var newDiv = document.createElement('div');
    newDiv.id = "div_"+divCounter;
    newDiv.style.backgroundColor = "red";
    newDiv.style.position = "absolute";
    newDiv.style.fontSize="0pt"; //IE hack. der IE verlangt sonst immer min. die Fonthï¿½e fr das DIV
    document.body.appendChild(newDiv);
    divCounter++;
    
    return newDiv;
}

function removeDiv(element)
{
    document.body.removeChild(element);
}
</script>
</html>

