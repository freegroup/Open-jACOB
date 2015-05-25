<%--
     This file is part of Open-jACOB
     Copyright (C) 2005-2010 Andreas Herz | FreeGroup
  
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

<%@ page import="de.tif.jacob.screen.impl.tag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.license.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.exception.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*"%>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page contentType="text/html; charset=ISO-8859-1" %>

<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", -1);

try
{
    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../logout.jsp\"></head><body></body></html>");
        return;
    }
    String thisPage =  request.getContextPath()+request.getServletPath();
    String thisPath =  thisPage.substring(0,thisPage.lastIndexOf("/")+1);

    String browserId    = request.getParameter("browser");
    String guid    = request.getParameter("guid");

    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    Application app = null;
    if(jacobSession!=null)
        app=(Application)jacobSession.getApplication(browserId);

    Theme theme = ThemeManager.getTheme(app.getTheme());

    // no valid application in the session found...cleanup all data
    // and redirect to the login screen
    //
    if(jacobSession==null || app==null)
    {
        UserManagement.logOutUser(request,response);
        out.clearBuffer();
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../logout.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);
%>

<html>
<head>
<link type="text/css" rel="stylesheet" href="../themes/common.css">
<link type="text/css" rel="stylesheet" href="../<%=theme.getCSSRelativeURL()%>">
<link  id="css" type="text/css"  rel="stylesheet" href="common_<%=BrowserType.getType(request)%>.css" />
<script type="text/javascript" src="prototype.js"></script>
<script type="text/javascript" src="scriptaculous.js"></script>
<script type="text/javascript" src="flow.js"></script>
<script type="text/javascript" src="Tab.js"></script>
<script type="text/javascript" src="Configuration.js"></script>
<script type="text/javascript" src="UUID.js"></script>
<script type="text/javascript" src="Canvas.js"></script>
<script type="text/javascript" src="PalettePart.js"></script>
<script type="text/javascript" src="Application.js"></script>
<script type="text/javascript" src="./menu/MenuItem.js"></script>
<script type="text/javascript" src="./command/Command.js"></script>
<script type="text/javascript" src="./command/CommandSetProperty.js"></script>
<script type="text/javascript" src="./command/CommandStack.js"></script>
<script type="text/javascript" src="./command/CommandStackEvent.js"></script>
<script type="text/javascript" src="./command/CommandStackEventListener.js"></script>
<script type="text/javascript" src="./block/DropZone.js"></script>
<script type="text/javascript" src="./block/AbstractBlock.js"></script>
<script type="text/javascript" src="./block/math/ControlBlock_AbstractMath.js"></script>
<script type="text/javascript" src="./block/math/ControlBlock_Add.js"></script>
<script type="text/javascript" src="./block/math/ControlBlock_Divide.js"></script>
<script type="text/javascript" src="./block/math/ControlBlock_Max.js"></script>
<script type="text/javascript" src="./block/math/ControlBlock_Min.js"></script>
<script type="text/javascript" src="./block/math/ControlBlock_Multiply.js"></script>
<script type="text/javascript" src="./block/math/ControlBlock_RoundUp.js"></script>
<script type="text/javascript" src="./block/math/ControlBlock_Sqrt.js"></script>
<script type="text/javascript" src="./block/data/ControlBlock_AbstractData.js"></script>
<script type="text/javascript" src="./block/data/ControlBlock_UseField.js"></script>
<script type="text/javascript" src="./block/data/ControlBlock_UseFieldFrom.js"></script>
<script type="text/javascript" src="./block/data/ControlBlock_SetField.js"></script>
<script type="text/javascript" src="./block/data/ControlBlock_UseVariable.js"></script>
<script type="text/javascript" src="./block/data/ControlBlock_DefineVariable.js"></script>
<script type="text/javascript" src="./block/control/ControlBlock_Start.js"></script>
<script type="text/javascript" src="./block/control/ControlBlock_If.js"></script>
<script type="text/javascript" src="./block/dialog/ControlBlock_AbstractUI.js"></script>
<script type="text/javascript" src="./block/dialog/ControlBlock_Alert.js"></script>
<script type="text/javascript" src="./block/dialog/ControlBlock_Prompt.js"></script>
<script type="text/javascript" src="./block/text/ControlBlock_AbstractText.js"></script>
<script type="text/javascript" src="./block/text/ControlBlock_NULL.js"></script>
<script type="text/javascript" src="./block/text/ControlBlock_Text.js"></script>
<script type="text/javascript" src="./block/text/ControlBlock_Join.js"></script>
<script type="text/javascript" src="./xml/ModelXMLSerializer.js"></script>
<script type="text/javascript" src="./xml/ModelXMLDeserializer.js"></script>
<script>

  var app=null;
  var browserId  = '<%=browserId%>';
  var documentId  = '<%=guid%>';

  Event.observe(window, "load", function() 
  {
	app = new flow.Application();    
    resize();
	app.loadDocument(documentId);

     $("cancel_button").observe("click",function()
    {
        if(Prototype.Browser.IE)
          window.location.href= "./../ie_index.jsp?browser="+browserId+"&control="+documentId+"&event=cancel";
        else
          window.location.href= "./../ns_index.jsp?browser="+browserId+"&control="+documentId+"&event=cancel";
    });

    $("save_button").observe("click",function()
    {
        app.saveDocument(documentId, function()
        {
          if(Prototype.Browser.IE)
            window.location.href= "./../ie_index.jsp?browser="+browserId+"&control="+documentId+"&event=ok";
          else
            window.location.href= "./../ns_index.jsp?browser="+browserId+"&control="+documentId+"&event=ok";
        });
    });
    
    $("delete_button").observe("click",function()
    {
        app.deleteDocument(documentId, function()
        {
          if(Prototype.Browser.IE)
            window.location.href= "./../ie_index.jsp?browser="+browserId+"&control="+documentId+"&event=ok";
          else
            window.location.href= "./../ns_index.jsp?browser="+browserId+"&control="+documentId+"&event=ok";
        });
    });

    var MyTabs = new Tabs('tabs', {
          start: Tabs.LAST,
          callback: 
          {
                afterOpen: function(panel) {
                },
                isEmpty: function() {
                }
          }
      });



  });
  
  // send "keep alive" to the server to aviod autologout
  //
  new PeriodicalExecuter(function(pe) {
      new Ajax.Request("../serverPush.jsp", {
        method: 'get',
        parameters: 
        {
            browser:browserId
        }
      });
  }, 20);

  
  function resize()
  {
  var width = 0, height = 0;
  if( typeof( window.innerWidth ) == 'number' ) 
  {
    //Non-IE
    width = window.innerWidth;
    height = window.innerHeight;
  } 
  else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) 
  {
    //IE 6+ in 'standards compliant mode'
    width = document.documentElement.clientWidth;
    height = document.documentElement.clientHeight;
  } 
  else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) 
  {
    //IE 4 compatible
    width = document.body.clientWidth;
    height = document.body.clientHeight;
  }

  var toolbarPanel = $("toolbar");
  var objectPanel = $("object_panel");
  var editorPanel= $("scrollarea");
  
  var tool_height = toolbarPanel.getHeight();
  
  editorPanel.style.width=Math.max(10,width-300)+"px";
  editorPanel.style.height = Math.max(10,height-tool_height)+"px";
  editorPanel.style.top = tool_height+"px";
  editorPanel.style.left = "302px";

  objectPanel.style.width="300px";
  objectPanel.style.height = Math.max(10,height-tool_height)+"px";
  objectPanel.style.top = tool_height+"px";
  objectPanel.style.left = "0px";
}

</script>
</head>
<body id="body" onresize="resize()" onselectstart="return false;" style="overflow:hidden;margin:0px;padding:0px;" onkeydown="">

<div id="toolbar" style="position:absolute;top:0px;width:100%;" >
<button id="save_button">Save</button>
<button id="cancel_button">Cancel</button>
<button id="delete_button">Delete</button>
</div>


<div id="scrollarea" style="position:absolute;overflow:auto;" >
   <div id="paintarea" style="position:absolute;left:0px;top:0px;width:3000px;height:3000px" >
   </div>
</div>

<div id="object_panel" style="position:absolute" >
<div id="tabs">
        <ul class="tabs">
                <li><a href="#tab-link" rel="target_1">Data</a></li>
                <li><a href="#tab-link" rel="target_2">Control</a></li>
                <li><a href="#tab-link" rel="target_4">UI</a></li>
                <li><a href="#tab-link" rel="target_5">Math</a></li>
                <li><a href="#tab-link" rel="target_6">Text</a></li>
        </ul>
        <div id="target_1">
            <ul class="panel_part">
              <li block="flow.ControlBlock_UseField" class="palette_part ControlBlock_UseField_palette Parameter" ></li>
              <li block="flow.ControlBlock_UseFieldFrom" class="palette_part ControlBlock_UseFieldFrom_palette Parameter" ></li>
              <li block="flow.ControlBlock_SetField" class="palette_part ControlBlock_SetField_palette Next Before" ></li>
              <li block="flow.ControlBlock_UseVariable" class="palette_part ControlBlock_UseVariable_palette Parameter" ></li>
              <li block="flow.ControlBlock_DefineVariable" class="palette_part ControlBlock_DefineVariable_palette Next Before" ></li>
            </ul>
        </div>
        <div id="target_2">
            <ul class="panel_part">
              <li block="flow.ControlBlock_If" class="palette_part ControlBlock_If_palette Next Before" ></li>
            </ul>
        </div>
        <div id="target_4">
            <ul class="panel_part">
              <li block="flow.ControlBlock_Alert" class="palette_part ControlBlock_Alert_palette Next Before"></li>
              <li block="flow.ControlBlock_Prompt" class="palette_part ControlBlock_Prompt_palette Parameter" ></li>
            </ul>
        </div>
        <div id="target_5">
            <ul class="panel_part">
              <li block="flow.ControlBlock_Add" class="palette_part ControlBlock_Add_palette Parameter"></li>
              <li block="flow.ControlBlock_Divide" class="palette_part ControlBlock_Divide_palette Parameter"></li>
              <li block="flow.ControlBlock_Max" class="palette_part ControlBlock_Max_palette Parameter"></li>
              <li block="flow.ControlBlock_Min" class="palette_part ControlBlock_Min_palette Parameter"></li>
              <li block="flow.ControlBlock_Multiply" class="palette_part ControlBlock_Multiply_palette Parameter"></li>
              <li block="flow.ControlBlock_RoundUp" class="palette_part ControlBlock_RoundUp_palette Parameter"></li>
              <li block="flow.ControlBlock_Sqrt" class="palette_part ControlBlock_Sqrt_palette Parameter"></li>
            </ul>
        </div>
        <div id="target_6">
            <ul class="panel_part">
              <li block="flow.ControlBlock_Text" class="palette_part ControlBlock_Text_palette Parameter"></li>
              <li block="flow.ControlBlock_Join" class="palette_part ControlBlock_Join_palette Parameter" ></li>
            </ul>
        </div>
    </div>
</div>


 
</body>

<%
}
catch( Throwable exc)
{
exc.printStackTrace();
    return;
}
finally
{
}
%>
