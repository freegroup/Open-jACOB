<%
    response.reset();

    String pkey   = request.getParameter("pkey");

    de.tif.jacob.core.definition.IApplicationDefinition applicationDef =de.tif.jacob.deployment.DeployMain.getApplication("@APPLICATION@","@VERSION@");
    de.tif.jacob.core.Context context = new de.tif.jacob.core.JspContext(applicationDef);
    de.tif.jacob.core.Context.setCurrent(context);
    de.tif.jacob.core.data.IDataTable productsTable = context.getDataTable("document");
    productsTable.qbeSetValue("pkey",pkey);
    productsTable.search();
    de.tif.jacob.core.data.IDataTableRecord doc = productsTable.getSelectedRecord();


    if(doc!=null && doc.getDocumentValue("file")!=null)
    {
      response.setContentType(de.tif.jacob.messaging.Message.getMimeType(doc.getSaveStringValue("file_name")));
      response.setHeader("Content-Disposition", "attachment; filename="+doc.getSaveStringValue("file_name"));

      ServletOutputStream stream = response.getOutputStream();
      stream.write(doc.getDocumentValue("file").getContent());
      stream.close();
    }
%>