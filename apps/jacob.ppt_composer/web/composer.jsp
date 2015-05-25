<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="j" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*" %>
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
    String compositionName = request.getParameter("name");

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
        out.write("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=../../../login.jsp\"></head></html>");
        return;
    }
    jacobSession.sendKeepAlive(browserId);

   synchronized(app)
   {
      ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
      Context.setCurrent(context);

      IDataAccessor a = context.getDataAccessor().newAccessor();

      // Alle Folien welche bereits in der Composition sind suchen
      //
      IDataBrowser linkBrowser = a.getBrowser("ppt_to_compositionFullBrowser");
      IDataTable linkTable = a.getTable("ppt_to_composition");
      linkTable.qbeSetKeyValue("composition_key",pkey);
      linkBrowser.search("default");
      List linkedPPTPkey = new ArrayList();
      List linkedPPTName = new ArrayList();
      for(int i=0; i<linkBrowser.recordCount(); i++)
      {
        IDataBrowserRecord linkRecord = linkBrowser.getRecord(i);
        String pptName = linkRecord.getSaveStringValue("browserPptName");
        String pptPkey = linkRecord.getSaveStringValue("browserPpt_key");
        linkedPPTName.add(pptName);
        linkedPPTPkey.add(pptPkey);
      }

      // Alle verf�gbaren Folien in der Application suchen
      //
      a = context.getDataAccessor().newAccessor();
      IDataTable pptTable = a.getTable("ppt");
      pptTable.search();
      List availablePPT = new ArrayList();
      for(int i=0; i<pptTable.recordCount(); i++)
      {
        IDataTableRecord pptRecord = pptTable.getRecord(i);
        if(!linkedPPTPkey.contains(pptRecord.getSaveStringValue("pkey")))
          availablePPT.add(pptRecord);
      }
%>

<html  style="height:100%" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>PPT Composer</title>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="./css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="./css/xtheme-aero.css" />
    <link rel="stylesheet" type="text/css" href="composer.css" />
    <script type="text/javascript" src="./javascript/ext-base.js"></script>
    <script type="text/javascript" src="./javascript/ext-all.js"></script>
    <script type="text/javascript" src="./javascript/composer.js"></script>

  <script>
  function initTree(){
  <%
  out.println("var data = null;");
  out.println("var rn = Composer.addComposition('"+compositionName+"');");
    for(int i=0; i<linkedPPTPkey.size(); i++)
    {
      String p  = (String)linkedPPTPkey.get(i);
      String name  = (String)linkedPPTName.get(i);
      out.println("data = {name:\""+name+"\",pkey:"+p+",lastmod:1173178130000,url:\"ppt_image.jsp?browser="+browserId+"&pkey="+p+"\"};");
      
      out.println("Composer.view.prepareData(data);");
      out.println("rn.appendChild(new Ext.tree.TreeNode({");
      out.println("  text: data.name,");
      out.println("  icon: data.url,");
      out.println("  data: data,");
      out.println("  qtip: data.qtip,");
      out.println("  leaf:true,");
      out.println("  cls: 'image-node'");
      out.println("}));");
      
    }
  %>
  rn.expand();
 }
  var browserId = "<%=browserId%>";
  var pkey = "<%=pkey%>";
  </script>
</head>
<body scroll="no">

<div id="buttonbar">
<div id="save_button"/>
<div id="cancel_button"/>
</div>
<%
  } // end synchronize
%>

 </body>
 </html>
