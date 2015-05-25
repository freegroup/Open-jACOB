<%
    String  browserId  = request.getParameter("browser");
    String  guid       = request.getParameter("guid");
    de.tif.jacob.screen.impl.HTTPClientSession jacobSession = de.tif.jacob.screen.impl.HTTPClientSession.get(request);

    String mimeType = "application/excel";

    response.reset();

    de.tif.jacob.transformer.ITransformer trans = de.tif.jacob.transformer.TransformerFactory.get(mimeType);
    
    String filename = "browser.xls";
    
    response.setContentType(mimeType);
    response.setHeader("Content-Disposition", "attachment; filename="+filename);
    
    de.tif.jacob.screen.impl.html.Application app = (de.tif.jacob.screen.impl.html.Application)jacobSession.getApplication(browserId);
    de.tif.jacob.screen.impl.html.dialogs.ExcelDialog dialog = (de.tif.jacob.screen.impl.html.dialogs.ExcelDialog)jacobSession.getDialog(guid);

    ServletOutputStream stream = response.getOutputStream();
    trans.setName(app.getLabel());
    trans.transform(stream, (String[])dialog.getHeader(), (String[][])dialog.getData());
    stream.close();
    if(true) return;
%>
