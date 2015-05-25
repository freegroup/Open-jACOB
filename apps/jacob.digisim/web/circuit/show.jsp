<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/Strict.dtd">
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

<%
    IUser user = UserManagement.getUser("digisim","0.1","admin");
    IApplicationDefinition applicationDef =DeployMain.getApplication("digisim","0.1");
  	Context context = new de.tif.jacob.core.JspContext(applicationDef,user);
	  Context.setCurrent(context);
	
    String pkey       = request.getParameter("pkey");
%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de">
<html>
<head>
  <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
  <title>Digital Circuit Simulator</title>
<script>
  var pkey       = "<%=pkey%>";
  var isGuestUser = false;
  var browserId  = null;
  var appName     ="digisim";
</script>

    <link rel="stylesheet" type="text/css" href="./css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="./css/xtheme-vista.css" />
    <link rel="stylesheet" type="text/css" href="./toolbar.css" />
    
    <script type="text/javascript" src="./adapter/yui/yui-utilities.js"></script>
    <script type="text/javascript" src="./adapter/yui/ext-yui-adapter.js"></script>
    <script type="text/javascript" src="ext-all.js"></script>
    

    <link rel="stylesheet" type="text/css" href="Application.css" />
    <script src="../draw2d/wz_jsgraphics.js"></script>
    <script src="../draw2d/draw2d.js"></script>

	<SCRIPT src="Wire.js"></SCRIPT>
	<SCRIPT src="FlowMenu.js"></SCRIPT>
	<SCRIPT src="ButtonDelete.js"></SCRIPT>
	<SCRIPT src="ButtonMoveFront.js"></SCRIPT>
	<SCRIPT src="ButtonMoveBack.js"></SCRIPT>
    <script src="QTips.js"></script>
    <script src="SimulationObjectImage.js"></script>
    <script src="SimulationObjectRectangle.js"></script>
    <script src="ToolDigitalObject.js"></script>
    <script src="Logger.js"></script>
    <script src="ObjectPalette.js"></script>
    <script src="DemoApplication.js"></script>
    <script src="CircuitSerializer.js"></script>
    <script src="public_part.jsp"></script>

</head>
<body id="body" onselectstart="return false;" style="margin:0px;padding:0px;" onkeydown="">
  <div id="toolbar"></div>
  <div id="canvas" class="x-layout-active-content">
      <div id="paintarea" style="position:relative;width:3000px;height:3000px;" >
      </div>
  </div>
<script>
  var logger = new Logger();
  var workflow  = new Workflow("paintarea");

  var menu = new FlowMenu(workflow);
  workflow.addSelectionListener(menu);

<%
  // it is not possible to cast this to the Draw2DManager!!!!
  // The classloader of the JSP does not knows any about the Draw2DManager of the jACOB application!!!
  //

  IDataAccessor a = context.getDataAccessor().newAccessor();
  IDataTable ruleTable = a.getTable("circuit");
  ruleTable.qbeSetValue("pkey",pkey);
  if(ruleTable.search()==1)
  {
    IDataTableRecord currentRecord = ruleTable.getSelectedRecord();
    InputStream stream = new StringBufferInputStream(currentRecord.getSaveStringValue("xml"));

// The hard way! The one and only way in the moment. We must change the jACOB application classloader
//
    Object draw2dManager  = ClassProvider.createInstance(applicationDef,"jacob.common.Draw2dManager");
    Method method = draw2dManager.getClass().getMethod("toDraw2D",new Class[]{InputStream.class});
    Object result = method.invoke(draw2dManager,new Object[]{stream});
    out.println(result.toString());
    stream.close();
  }


%>
   Ext.EventManager.onDocumentReady(Application.init, Application, true);

function setupToolbar()
{
   var toolbar = new Ext.Toolbar('toolbar');
   toolbar.addButton({ text: 'Login', 
                       icon:"icons/login.png", 
                       enableToggle:true, 
                       tooltip: {text:'Login into the digital circuit simulator application', title:'Login', autoHide:true},
                       cls: 'x-btn-text-icon',
                       handler: function(o, e) 
   {
        window.location.href="login.jsp";
   }});

   toolbar.addButton({ text: 'Start', 
                       icon:"icons/play.png", 
                       enableToggle:true, 
                       tooltip: {text:'Start or Stop the simulation of the digital circuit', title:'Start/Stop', autoHide:true},
                       cls: 'x-btn-text-icon',
                       handler: function(o, e) 
   {
     if(o.pressed)
	     Application.startSimulation();
     else
	     Application.stopSimulation();
   }});
   

   toolbar.addFill();
   toolbar.addButton({ text: 'Feedback', 
                       icon:"icons/tool_feedback.gif", 
                       id:'feedback-button',
                       cls: 'x-btn-text-icon',
                       handler: function(o, e) 
   {
       Application.sendFeedback();
   }});

   toolbar.addSpacer();
   
   toolbar.addButton({ text: 'Powered By Open-jACOB', cls: 'x-btn-text',handler: function(o, e) 
   {
       window.open("http://www.openjacob.org");
   }});
}

</script>
</body>
</html>
