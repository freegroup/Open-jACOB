<%
    response.reset();

    String pkey   = request.getParameter("pkey");

    de.tif.jacob.core.definition.IApplicationDefinition applicationDef =de.tif.jacob.deployment.DeployMain.getApplication("@APPLICATION@","@VERSION@");
    de.tif.jacob.core.Context context = new de.tif.jacob.core.JspContext(applicationDef);
    de.tif.jacob.core.Context.setCurrent(context);
    de.tif.jacob.core.data.IDataTable productsTable = context.getDataTable("herb");
    productsTable.qbeSetValue("pkey",pkey);
    productsTable.search();
    de.tif.jacob.core.data.IDataTableRecord herb = productsTable.getSelectedRecord();

    if(herb!=null && herb.getDocumentValue("image")!=null)
    {
	    ServletOutputStream stream = response.getOutputStream();
	    stream.write(herb.getDocumentValue("image").getContent());
	    stream.close();
    }
%>