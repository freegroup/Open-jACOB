<%
    response.reset();

    String browserId  = request.getParameter("browser");
    String pkey   = request.getParameter("pkey");
    de.tif.jacob.screen.impl.html.ClientSession     jacobSession = (de.tif.jacob.screen.impl.html.ClientSession)de.tif.jacob.screen.impl.HTTPClientSession.get(request);
    de.tif.jacob.screen.impl.html.Application app = null;
    if(jacobSession!=null)
        app=(de.tif.jacob.screen.impl.html.Application)jacobSession.getApplication(browserId);

    // no valid application in the session found...cleanup all data
    // and redirect to the login screen
    //
    if(jacobSession==null || app==null)
    {
        de.tif.jacob.security.UserManagement.logOutUser(request,response);
        out.clearBuffer();
        return;
    }

   synchronized(app)
   {
      de.tif.jacob.screen.impl.html.ClientContext context = new de.tif.jacob.screen.impl.html.ClientContext(jacobSession.getUser(),out, app, browserId);
      de.tif.jacob.screen.impl.html.ClientContext.setCurrent(context);
  
      de.tif.jacob.core.data.IDataAccessor a = context.getDataAccessor().newAccessor();
	  de.tif.jacob.core.data.IDataTable partTable = a.getTable("ppt");
	  partTable.qbeSetKeyValue("pkey", pkey);
	  partTable.search();
	  de.tif.jacob.core.data.IDataTableRecord partRecord = partTable.getRecord(0);
			
      ServletOutputStream stream = response.getOutputStream();
      if(partRecord.getDocumentValue("thumbnail_de")!=null)
        stream.write(partRecord.getDocumentValue("thumbnail_de").getContent());
      else
        stream.write(partRecord.getDocumentValue("thumbnail_en").getContent());
      stream.close();
   }

%>