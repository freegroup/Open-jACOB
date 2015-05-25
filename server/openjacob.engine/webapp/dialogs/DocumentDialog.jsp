<%
    String  browserId  = request.getParameter("browser");
    String  guid       = request.getParameter("guid");
    de.tif.jacob.screen.impl.HTTPClientSession jacobSession = de.tif.jacob.screen.impl.HTTPClientSession.get(request);

    response.reset();

    de.tif.jacob.screen.impl.html.Application app = (de.tif.jacob.screen.impl.html.Application)jacobSession.getApplication(browserId);
    de.tif.jacob.screen.impl.html.dialogs.DocumentDialog dialog = (de.tif.jacob.screen.impl.html.dialogs.DocumentDialog)jacobSession.getDialog(guid);

    if(!dialog.isEnforcePrinting())
    {
	    response.setContentType(dialog.getMimeType());
	    response.setHeader("Content-Disposition", "attachment; filename="+dialog.getFileName());
	
	    ServletOutputStream stream = response.getOutputStream();
	    stream.write(dialog.getDocument());
	    stream.close();
    }
    else
    {
      %>
      <html>
      <body>
      <pre>
      <%=new String(dialog.getDocument()) %>
      </pre>
      </body>
      <script>
      window.print();
      </script>
      </html>
      <%
    }
    if(true) return;
%>
