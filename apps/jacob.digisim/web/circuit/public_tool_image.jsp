<%
    response.reset();

		String browserId  = request.getParameter("browser");
		String pkey       = request.getParameter("pkey");
		
		de.tif.jacob.screen.impl.html.ClientSession jacobSession = (de.tif.jacob.screen.impl.html.ClientSession)de.tif.jacob.screen.impl.HTTPClientSession.get(request);
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
		
		de.tif.jacob.security.IUser user = jacobSession.getUser();
		de.tif.jacob.core.Context context = new de.tif.jacob.core.JspContext(app.getApplicationDefinition(),user);
    de.tif.jacob.core.Context.setCurrent(context);
  
    de.tif.jacob.core.data.IDataAccessor a = context.getDataAccessor().newAccessor();
    de.tif.jacob.core.data.IDataTable partTable = a.getTable("part");
    partTable.qbeSetKeyValue("pkey", pkey);
    partTable.search();
		de.tif.jacob.core.data.IDataTableRecord partRecord = partTable.getSelectedRecord();
		
    ServletOutputStream stream = response.getOutputStream();
    stream.write(partRecord.getDocumentValue("tool_image").getContent());
    stream.close();
%>