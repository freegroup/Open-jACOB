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
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head><body></body></html>");
        return;
    }
    String browserId  = request.getParameter("browser");
    String pkey       = request.getParameter("pkey");

    ClientSession     jacobSession = (ClientSession)session.getAttribute(HTTPConstants.SESSION_KEY);
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
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);

   synchronized(app)
   {
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de">
<html>
<head>
  <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
  <title>Workflow Editor</title>

        <!-- common, all times required, imports -->
	<SCRIPT src="./draw2d/wz_jsgraphics.js"></SCRIPT>
	<SCRIPT src="./draw2d/events.js"></SCRIPT>
	<SCRIPT src="./draw2d/debug.js"></SCRIPT>
	<SCRIPT src="./draw2d/dragdrop.js"></SCRIPT>
	<SCRIPT src="./draw2d/Color.js"></SCRIPT>
	<SCRIPT src="./draw2d/Point.js"></SCRIPT>
	<SCRIPT src="./draw2d/Border.js"></SCRIPT>
	<SCRIPT src="./draw2d/LineBorder.js"></SCRIPT>
	<SCRIPT src="./draw2d/Figure.js"></SCRIPT>
	<SCRIPT src="./draw2d/Label.js"></SCRIPT>
	<SCRIPT src="./draw2d/Oval.js"></SCRIPT>
	<SCRIPT src="./draw2d/Circle.js"></SCRIPT>
	<SCRIPT src="./draw2d/Rectangle.js"></SCRIPT>
	<SCRIPT src="./draw2d/Node.js"></SCRIPT>
	<SCRIPT src="./draw2d/Image.js"></SCRIPT>
	<SCRIPT src="./draw2d/Port.js"></SCRIPT>
	<SCRIPT src="./draw2d/InputPort.js"></SCRIPT>
	<SCRIPT src="./draw2d/OutputPort.js"></SCRIPT>
	<SCRIPT src="./draw2d/Line.js"></SCRIPT>
	<SCRIPT src="./draw2d/Connection.js"></SCRIPT>
	<SCRIPT src="./draw2d/Annotation.js"></SCRIPT>
	<SCRIPT src="./draw2d/ResizeHandle.js"></SCRIPT>
	<SCRIPT src="./draw2d/LineStartResizeHandle.js"></SCRIPT>
	<SCRIPT src="./draw2d/LineEndResizeHandle.js"></SCRIPT>
	<SCRIPT src="./draw2d/Canvas.js"></SCRIPT>
	<SCRIPT src="./draw2d/Workflow.js"></SCRIPT>
	<SCRIPT src="./draw2d/Window.js"></SCRIPT>
	<SCRIPT src="./draw2d/ToolGeneric.js"></SCRIPT>
	<SCRIPT src="./draw2d/ToolPalette.js"></SCRIPT>
	<SCRIPT src="./draw2d/Dialog.js"></SCRIPT>
	<SCRIPT src="./draw2d/InputDialog.js"></SCRIPT>
	<SCRIPT src="./draw2d/PropertyDialog.js"></SCRIPT>
	<SCRIPT src="./draw2d/AnnotationDialog.js"></SCRIPT>
	<SCRIPT src="./draw2d/PropertyWindow.js"></SCRIPT>
	<SCRIPT src="./draw2d/ColorDialog.js"></SCRIPT>
	<SCRIPT src="./draw2d/LineColorDialog.js"></SCRIPT>
	<SCRIPT src="./draw2d/BackgroundColorDialog.js"></SCRIPT>

        <!-- workflow specific imports -->
	<SCRIPT src="autosuggest.js"></SCRIPT>
	<SCRIPT src="WorkflowSerializer.js"></SCRIPT>
	<SCRIPT src="DecisionBoolean.js"></SCRIPT>
	<SCRIPT src="DecisionEnum.js"></SCRIPT>
	<SCRIPT src="Start.js"></SCRIPT>
	<SCRIPT src="BusinessObject.js"></SCRIPT>
	<SCRIPT src="UserExceptionBO.js"></SCRIPT>
	<SCRIPT src="UserInformation.js"></SCRIPT>
	<SCRIPT src="NOP.js"></SCRIPT>
	<SCRIPT src="End.js"></SCRIPT>
	<SCRIPT src="Funnel.js"></SCRIPT>
	<SCRIPT src="Email.js"></SCRIPT>
	<SCRIPT src="FieldModifier.js"></SCRIPT>
	<SCRIPT src="FieldModifierDialog.js"></SCRIPT>
	<SCRIPT src="EmailDialog.js"></SCRIPT>
	<SCRIPT src="DecisionDialog.js"></SCRIPT>
	<SCRIPT src="WorkflowPropertyWindow.js"></SCRIPT>
	<SCRIPT src="EnumDialog.js"></SCRIPT>
	<SCRIPT src="ToolDecisionBoolean.js"></SCRIPT>
	<SCRIPT src="ToolDecisionEnum.js"></SCRIPT>
	<SCRIPT src="ToolNOP.js"></SCRIPT>
	<SCRIPT src="ToolEnd.js"></SCRIPT>
	<SCRIPT src="ToolFunnel.js"></SCRIPT>
	<SCRIPT src="ToolUserExceptionBO.js"></SCRIPT>
	<SCRIPT src="ToolUserInformation.js"></SCRIPT>
	<SCRIPT src="ToolSave.js"></SCRIPT>
	<SCRIPT src="ToolEmail.js"></SCRIPT>
	<SCRIPT src="ToolBusinessObject.js"></SCRIPT>
	<SCRIPT src="ToolAnnotation.js"></SCRIPT>
	<SCRIPT src="ToolFieldModifier.js"></SCRIPT>
	<SCRIPT src="WorkflowPalette.js"></SCRIPT>

</head>
<body onselectstart="return false;" style="margin:0px;padding:0px;" onkeydown="">
<div id="paintarea" style="position:absolute;left:0px;top:0px;width:3000px;height:3000px" ></div>
<script>
  var browserId  = "<%=browserId%>";
  var pkey       = "<%=pkey%>";
  var isGuestUser= <%=context.getUser().hasRole("guest")%>;

  var workflow  = new Workflow("paintarea");

<%
  // it is not possible to cast this to the Draw2DManager!!!!
  // The classloader of the JSP does not knows any about the Draw2DManager of the jACOB application!!!
  //

  IDataAccessor a = context.getDataAccessor().newAccessor();
  IDataTable ruleTable = a.getTable("rule");
  ruleTable.qbeSetValue("pkey",pkey);
  if(ruleTable.search()==1)
  {
    IDataTableRecord currentRecord = ruleTable.getSelectedRecord();
    InputStream stream = new ByteArrayInputStream(currentRecord.getDocumentValue("rule").getContent());

// The hard way! The one and only way in the moment. We must change the jACOB application classloader
//
    Object draw2dManager  = ClassProvider.createInstance(app.getApplicationDefinition(),"de.tif.jacob.ruleengine.Draw2dManager");
    Method method = draw2dManager.getClass().getMethod("toDraw2D",new Class[]{InputStream.class});
    Object result = method.invoke(draw2dManager,new Object[]{stream});
    out.println(result.toString());

// The easy way! Doesn't run because of Classloader problems.
//
//    out.println(Draw2dManager.toDraw2D(stream));
    stream.close();
  }

  }
%>
  // Add a simple PropertyDialog to the Canvas
  // This will display the properties of the current select object
  //
  var dialog = new WorkflowPropertyWindow();
  workflow.showDialog(dialog,400,10);

  // Add the Tool Window to the screen
  //
  var w = new WorkflowPalette();
  workflow.setToolWindow(w);


</script>
</body>
</html>
