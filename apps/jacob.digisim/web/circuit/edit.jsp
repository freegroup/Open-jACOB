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
    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=login.jsp\"></head><body></body></html>");
        return;
    }
    String browserId  = request.getParameter("browser");
    String pkey       = request.getParameter("pkey");
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
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=login.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);

   IUser user = jacobSession.getUser();
   synchronized(app)
   {
     de.tif.jacob.core.JspContext context = new de.tif.jacob.core.JspContext(app.getApplicationDefinition(),user);
     Context.setCurrent(context);
%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de">
<html>
<head>
  <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
  <title>Digital Circuit Simulator</title>
<script>
  var browserId   = "<%=browserId%>";
  var pkey        = <%=pkey==null?"null":"\""+pkey+"\""%>;
  var fileName    = "blank";
  var isGuestUser = false;
  var appName     ="<%=app.getName()%>";
  var fileUrl     = "../../../../cmdenter?entry=GenericSearch&anchorTableAlias=circuit&user=demo&pwd=&app=digisim&circuit.owner_key=<%=user.getKey()%>"
</script>

    <link rel="stylesheet" type="text/css" href="./css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="./css/xtheme-vista.css" />
    <script type="text/javascript" src="./adapter/yui/yui-utilities.js"></script>
    <script type="text/javascript" src="./adapter/yui/ext-yui-adapter.js"></script>
    <script type="text/javascript" src="ext-all.js"></script>


    <link rel="stylesheet" type="text/css" href="Application.css" />
    <script src="../draw2d/wz_jsgraphics.js"></script>
    <script src="../draw2d/draw2d.js"></script>

    <SCRIPT src="Wire.js"></SCRIPT>
    <script src="FlowMenu.js"></script>
    <script src="ButtonDelete.js"></script>
    <script src="ButtonMoveFront.js"></script>
    <script src="ButtonMoveBack.js"></script>
    <script src="QTips.js"></script>
    <script src="SimulationObjectImage.js"></script>
    <script src="SimulationObjectRectangle.js"></script>
    <script src="ToolDigitalObject.js"></script>
    <script src="Logger.js"></script>
    <script src="ObjectPalette.js"></script>
    <script src="Application.js"></script>
    <script src="HelpManager.js"></script>
    <script src="CircuitSerializer.js"></script>
    <script src="my_part.jsp?browser=<%=browserId%>"></script>

</head>
<body id="body" onselectstart="return false;" scroll="no" style="margin:0px;padding:0px;" onkeydown="">
  <div id="menubar"></div>
  <div id="canvas-panel"  style="position:relative;">
      <div id="canvas-toolbar"></div>
      <div id="canvas-content" class="x-layout-active-content" style="position:relative;">
         <div id="paintarea" style="position:relative;width:3000px;height:3000px;" >
         </div>
      </div>
  </div>
<script>
  var logger = new Logger();
  var workflow  = new Workflow("paintarea");
  workflow.setBackgroundImage(null);

  var menu = new FlowMenu(workflow);
  workflow.addSelectionListener(menu);

  window.setInterval("Application.sendKeepAlive();",<%=Property.KEEPALIVEINTERVAL_APPLICATION.getIntValue()*1000%>);

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
    Object draw2dManager  = ClassProvider.createInstance(app.getApplicationDefinition(),"jacob.common.Draw2dManager");
    Method method = draw2dManager.getClass().getMethod("toDraw2D",new Class[]{InputStream.class});
    Object result = method.invoke(draw2dManager,new Object[]{stream});
    out.println(result.toString());
    out.println("fileName=\""+currentRecord.getSaveStringValue("title")+"\";");
    stream.close();
  }


%>
  /**********************************************************************************
   * 
   * Do the Ext (Yahoo UI) Stuff
   *
   **********************************************************************************/
   Ext.EventManager.onDocumentReady(Application.init, Application, true);

function setupToolbar()
{
   var toolbar = new Ext.Toolbar('canvas-toolbar');
   toolbar.addButton({ text: 'Start', 
                       id:'simlation-start-button',
                       icon:"icons/play.png", 
                       enableToggle:true, 
                       tooltip: {text:'Start or Stop the simulation if the digital circuit', title:'Start/Stop', autoHide:true},
                       cls: 'x-btn-text-icon',
                       handler: function(o, e) 
   {
     if(o.pressed)
	     Application.startSimulation();
     else
	     Application.stopSimulation();
   }});

   toolbar.addSpacer();
   toolbar.addSeparator();
   toolbar.addSpacer();

   toolbar.addButton({  text: 'Snap to Grid', 
                        id:'snap-grid-button',
                        icon:"icons/tool_snap_grid.png", 
                        enableToggle:true, 
                        tooltip: {text:'When activated, graphical elements are automatically aligned on the grid when they are created, moved or resized. When snapping a rectangle, the edges of the rectangle will snap along gridlines', title:'Snap to Grid', autoHide:true},
                        cls: 'x-btn-text-icon',
                        handler: function(o, e)
   {
     if(o.pressed)
     {
	 workflow.setSnapToGrid(true);
         workflow.setBackgroundImage("grid_10.png",true);
     }
     else
     {
	 workflow.setSnapToGrid(false);
         workflow.setBackgroundImage(null);
     }
   }});
 
   toolbar.addButton({  text: 'Snap to Geometry', 
                        id:'snap-geometry-button',
                        icon:"icons/tool_snap_geometry.png", 
                        enableToggle:true, 
                        tooltip: {text:'When activated, graphical elements can be snapped to existing elements. When snapping a rectangle, the edges of the rectangle will snap to edges of other rectangles corresponding to the existing elements.', title:'Snap to Geometry', autoHide:true},
                        cls: 'x-btn-text-icon',
                        handler: function(o, e)
   {
     if(o.pressed)
	workflow.setSnapToGeometry(true);
     else
	workflow.setSnapToGeometry(false);
   }});
   return toolbar;
}


function setupMenubar()
{
var menu = new Ext.menu.Menu({
    id: 'menubar',
    items: [
        new Ext.menu.Item({
            text: 'New', 
            icon:"icons/fileNew.gif" ,
            tooltip: {text:'Save the current circuit', title:'Save', autoHide:true},
            handler: function(o, e){ Application.fileNew();}})
        ,
        new Ext.menu.Item({
            text: 'Open', 
            icon:"icons/fileOpen.gif" ,
            tooltip: {text:'Save the current circuit', title:'Save', autoHide:true},
            handler: function(o, e){ Application.fileOpen();}})
        ,
        new Ext.menu.Item({
            text: 'Save', 
            icon:"icons/fileSave.gif" ,
            tooltip: {text:'Save the current circuit', title:'Save', autoHide:true},
            handler: function(o, e){ Application.fileSave();}})
        ,
        '-'
        ,
        new Ext.menu.Item({
            text: 'Quit', 
            icon:"icons/cancel.png" ,
            tooltip: {text:'Save the current circuit', title:'Save', autoHide:true},
            handler: function(o, e){ Application.quit();}})
    ]
});
   var toolbar = new Ext.Toolbar('menubar');

   toolbar.add({ text:'File',  menu: menu });
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
   toolbar.addButton({ text: 'Powered By Open-jACOB', 
                       id:'powerd-button',
                       cls: 'x-btn-text',
                       handler: function(o, e) 
   {
       window.open("http://www.openjacob.org");
   }});
   return menu;
}

function setupPalette()
{
  var objectPalette = new ObjectPalette();
<%
      Set<String> insertedElements = new TreeSet<String>();
      a = context.getDataAccessor().newAccessor();
      IDataTable partTable = a.getTable("my_part");
      partTable.search();
      for(int i=0; i<partTable.recordCount(); i++)
      {
         IDataTableRecord currentRecord = partTable.getRecord(i);
         String _name = currentRecord.getSaveStringValue("name");
         String _type = currentRecord.getSaveStringValue("type");
         String _desc = currentRecord.getSaveStringValue("tooltip");
         String _pkey = currentRecord.getSaveStringValue("pkey");
         out.println("objectPalette.addObject('"+_type+"','"+_name+"','"+_pkey+"');");
         insertedElements.add(_name);
      }
      partTable = a.getTable("public_part");
      partTable.search();
      for(int i=0; i<partTable.recordCount(); i++)
      {
         IDataTableRecord currentRecord = partTable.getRecord(i);
         String _name = currentRecord.getSaveStringValue("name");
         String _type = currentRecord.getSaveStringValue("type");
         String _desc = currentRecord.getSaveStringValue("tooltip");
         String _pkey = currentRecord.getSaveStringValue("pkey");
         if(!insertedElements.contains(_name))
         {
            out.println("objectPalette.addObject('"+_type+"','"+_name+"','"+_pkey+"');");
         }
      }
}
%>
  return objectPalette;
}

</script>

    <!-- Load Circuit Diallog Template-->
    <div id="hello-dlg" style="visibility:hidden;">
	    <div class="x-dlg-hd">File open..</div>
	    <div class="x-dlg-bd">
               <div id="file-grid" style="width:100%;height:100%">
               </div>
            </div>
	</div>

    <!-- Help Template-->
   <div id="help-panel" style="visibility:hidden;">
    <div id="help-toolbar"></div>
    <div id="help-content"></div>
   </div>
    <!-- Help Edit InformationTemplate-->
   <div id="help-information" style="visibility:hidden;">
     <div class="x-dlg-hd">Be in Mind...</div>
     <div class="x-dlg-bd">
     	<div id="help-information-content">
<b>Information</b><br>
<i>DigitalSimulator Help</i> is a collaboratively edited help system to which you can contribute. Be in mind that you can
edit any content of the system (at the moment).
<br><br>
<b>Etiquette</b><br>
Avoid reverts and deletions whenever possible, except in cases of clear vandalism.
<br>
<br>
<b>Copyrights</b><br>
Do not submit copyrighted material without permission. When adding information to articles, make sure it is written in your own words. Remember that all information found on the Internet is copyrighted unless the website specifically states otherwise.
</div>
     </div>
   </div>


    <!-- Help Edit InformationTemplate-->
   <div id="start-information" style="visibility:hidden;">
     <div class="x-dlg-hd">Tip of the Day</div>
     <div class="x-dlg-bd">
     	<div id="start-information-content">
        <br>
        <table style="margin:5px">
        <tr><td><img src="images/context_help.png"></td><td>Use the context menu to see additional help of any object.</td></tr></table>
        </div>
     </div>
   </div>

</body>
</html>
