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
    Map<String,String> typeMappping = new HashMap<String,String>();
    typeMappping.put("afterNewAction","After New Trigger");
    typeMappping.put("beforeCommitAction","Before Commit Trigger");
    typeMappping.put("afterCommitAction","After Commit Trigger");
    typeMappping.put("afterDeleteAction","After Delete Trigger");

    if(!UserManagement.isLoggedInUser(request,response))
    {
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head><body></body></html>");
        return;
    }
    String browserId  = request.getParameter("browser");
    String pkey       = request.getParameter("pkey");
    String eventType  = typeMappping.get(request.getParameter("type"));

   
    HTTPClientSession jacobSession = HTTPClientSession.get(request);
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
        <SCRIPT src='./draw2d/wz_jsgraphics.js'></SCRIPT>
        <SCRIPT src='./draw2d/mootools.js'></SCRIPT>      
        <SCRIPT src='./draw2d/moocanvas.js'></SCRIPT>      
        <SCRIPT src='./draw2d/draw2d.js'></SCRIPT>

        <SCRIPT src='prototype.js'></SCRIPT>

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
   <SCRIPT src="TwitterStatusDialog.js"></SCRIPT>
   <SCRIPT src="WorkflowPropertyWindow.js"></SCRIPT>
   <SCRIPT src="EnumDialog.js"></SCRIPT>
   <SCRIPT src="TwitterStatus.js"></SCRIPT>
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
   <SCRIPT src="ToolTwitterStatus.js"></SCRIPT>
   <SCRIPT src="ToolDelete.js"></SCRIPT>
   <SCRIPT src="ToolUndo.js"></SCRIPT>
   <SCRIPT src="ToolRedo.js"></SCRIPT>
   <SCRIPT src="WorkflowPalette.js"></SCRIPT>

<style>

.WindowFigure
{
  -moz-box-shadow:5px 5px 5px rgba(0, 0, 0, 0.4);
  -webkit-box-shadow:5px 5px 5px rgba(0, 0, 0, 0.4);
}

button
{
  -moz-border-radius: 8px;
  -webkit-border-radius: 8px;
  background-color:black;
  color:white;
  border:1px solid gray;
}

#toolbar
{
  position:absolute;
  top:0px;
  left:0px;
  width:100%;
  height:40px;
  background-color:#303030;
}
#save_button
{
  position:absolute;
  top:10px;
  left:10px;
  width:100px;
  height:20px;
}

#cancel_button
{
  position:absolute;
  top:10px;
  left:120px;
  width:100px;
  height:20px;
}

.Dialog_buttonbar
{
}

#type_label
{
  position:absolute;
  top:10px;
  right:0px;
  width:100%;
  height:20px;
  color:white;
  text-align:right;
  font-family:Lucida Console, Monaco, monospace;
  font-size:20px;
  letter-spacing:0.5em;
}
</style>
</head>
<body onselectstart="return false;" style="margin:0px;padding:0px;" onkeydown="">
<div id="scrollarea" style="position:absolute;left:0px;top:40px;width:100%;height:400px;overflow:hidden;overflow-x:auto;overflow-y:auto" >
   <div id="paintarea" style="position:absolute;left:0px;top:0px;width:3000px;height:3000px" ></div>
</div>
<script>
function bodyWidth(){ return document.body.offsetWidth || window.innerWidth || document.documentElement.clientWidth || 0; };
function bodyHeight(){ return document.body.offsetHeight || window.innerHeight || document.documentElement.clientHeight || 0; };

(function() {

  Event.observe(window, "resize", function() 
  {
    var height = bodyHeight()-40;
    if(height<0)
       return;
    var domains = $("scrollarea");
    domains.setStyle({height:height+"px"});
  });

  Event.observe(window, "load", function() 
  {
    var height = bodyHeight()-40;
    if(height<0)
       return;
    var domains = $("scrollarea");
    domains.setStyle({height:height+"px"});

    $("cancel_button").observe("click",function()
    {
        if(Prototype.Browser.IE)
          window.location.href= "../../../../ie_index.jsp?browser="+browserId;
        else
          window.location.href= "../../../../ns_index.jsp?browser="+browserId;
    });

    $("save_button").observe("click",function()
    {
      var xml = new WorkflowSerializer().toXML(workflow);
      new Ajax.Request("save.jsp?browser="+browserId+"&pkey="+pkey, 
      {
        method:"post",
        postBody:xml,
        contentType: "text/xml",
        onSuccess: function(transport)
        {
          if(Prototype.Browser.IE)
            window.location.href= "../../../../ie_index.jsp?browser="+browserId;
          else
            window.location.href= "../../../../ns_index.jsp?browser="+browserId;
        }
      });
    });
  });
})();

  var browserId  = "<%=browserId%>";
  var pkey       = "<%=pkey%>";
  var isGuestUser= <%=context.getUser().hasRole("guest")%>;

  // send "keep alive" to the server to aviod autologout
  //
  new PeriodicalExecuter(function(pe) {
      new Ajax.Request("../../../../serverPush.jsp", {
        method: 'get',
        parameters: 
        {
            browser:browserId
        }
      });
  }, 10);

  var workflow  = new Workflow("paintarea");
  workflow.setViewPort("scrollarea");

<%
  // it is not possible to cast this to the Draw2DManager!!!!
  // The classloader of the JSP does not knows any about the Draw2DManager of the jACOB application!!!
  //
  Object draw2dManager  = ClassProvider.createInstance(app.getApplicationDefinition(),"de.tif.jacob.ruleengine.Draw2dManager");

  IDataAccessor a = context.getDataAccessor().newAccessor();
  IDataTable ruleTable = a.getTable("rule");
  ruleTable.qbeSetValue("pkey",pkey);
  if(ruleTable.search()==1)
  {
    IDataTableRecord currentRecord = ruleTable.getSelectedRecord();
    InputStream stream = new ByteArrayInputStream(currentRecord.getDocumentValue("rule").getContent());

// The hard way! The one and only way in the moment. We must change the jACOB application classloader
//
    try
    {
      Method method = draw2dManager.getClass().getMethod("toDraw2D",new Class[]{InputStream.class});
      Object result = method.invoke(draw2dManager,new Object[]{stream});
      out.println(result.toString());
    }
    catch(Exception exc)
    {
       exc.printStackTrace();
    }

// The easy way! Doesn't run because of Classloader problems.
//
//    out.println(Draw2dManager.toDraw2D(stream));
    stream.close();
  }

  }
%>

  // Add the Tool Window to the screen
  //
  var w = new WorkflowPalette();
  workflow.setToolWindow(w);

  $("scrollarea").onscroll=function(){workflow.onScroll();};

</script>

<div id="toolbar">
<span id="type_label"><%=eventType%></span>
<button id="save_button">Save</button> 
<button id="cancel_button">Cancel</button>
</div>
</body>
</html>
