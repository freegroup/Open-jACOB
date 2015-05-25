<%
 response.reset();

 String pkey = request.getParameter("pkey");

 de.tif.jacob.core.definition.IApplicationDefinition applicationDef =de.tif.jacob.deployment.DeployMain.getApplication("@APPLICATION@","@VERSION@");
 de.tif.jacob.core.Context context = new de.tif.jacob.core.JspContext(applicationDef);
 de.tif.jacob.core.Context.setCurrent(context);
 de.tif.jacob.core.data.IDataTable objectTable   = context.getDataTable("object");
 
 objectTable.qbeSetValue("pkey",pkey);
 objectTable.search("object");
 de.tif.jacob.core.data.IDataTableRecord object = objectTable.getSelectedRecord();
 object.getAccessor().propagateRecord(object,de.tif.jacob.core.definition.Filldirection.BOTH);
 
 de.tif.jacob.core.data.IDataAccessor accessor = context.getDataAccessor().newAccessor();
 de.tif.jacob.core.data.IDataTable templateTable = accessor.getTable("template");
 templateTable.search();
 de.tif.jacob.core.data.IDataTableRecord template = templateTable.getRecord(0);
 
 de.tif.jacob.core.data.DataDocumentValue document =  de.tif.jacob.letter.LetterFactory.transform(context,object, template.getDocumentValue("document"));

 response.setHeader("Content-Disposition", "attachment; filename=Prozessblatt.rtf");
	
 ServletOutputStream stream = response.getOutputStream();
 stream.write(document.getContent());
 stream.close();
%>