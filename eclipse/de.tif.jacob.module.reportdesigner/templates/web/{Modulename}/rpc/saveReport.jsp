<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.util.*" %>
<%@ page import="de.tif.jacob.report.*" %>
<%@ page import="de.tif.jacob.report.impl.*" %>
<%@ page import="de.tif.jacob.report.impl.castor.*" %>
<%@ page import="de.tif.jacob.report.impl.castor.types.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.theme.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" import="java.math.*, java.io.*, java.text.* " %>

<%@ include file="../guid.inc" %> 
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
try
{
    String browserId = request.getParameter("browser");
    String name = request.getParameter("name");
    String first_page_header = request.getParameter("first_page_header");
    String page_header = request.getParameter("page_header");
    String page_footer = request.getParameter("page_footer");
    String last_page_footer = request.getParameter("last_page_footer");
    String section_header[] = request.getParameterValues("section_header");    
    String section_footer[] = request.getParameterValues("section_footer");    

    if(!UserManagement.isLoggedInUser(request,response))
        return;

    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        de.tif.jacob.screen.impl.html.Application app =(de.tif.jacob.screen.impl.html.Application)jacobSession.getApplication(browserId);
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);

        Context.setCurrent(context);

        Report report = (Report)context.getProperty(REPORT_GUID);
        CastorLayout layout = report.getLayout_01();
       
        CastorCaption prologue = layout.getPrologue();
        CastorCaption pageHeader = layout.getPageHeader();
        CastorCaption pageFooter = layout.getPageFooter();
        CastorCaption epilogue = layout.getEpilogue();

        if(prologue==null)
           layout.setPrologue(prologue=new CastorCaption());

        if(pageHeader==null)
           layout.setPageHeader(pageHeader=new CastorCaption());

        if(pageFooter==null)
           layout.setPageFooter(pageFooter=new CastorCaption());

        if(epilogue==null)
           layout.setEpilogue(epilogue=new CastorCaption());

        prologue.setText(first_page_header);
        pageHeader.setText(page_header);
        pageFooter.setText(page_footer);
        epilogue.setText(last_page_footer);
        
        CastorLayoutPart part = layout.getPart();
        int index=0;
        while(true)
        {
           CastorGroup group = part.getGroup();
           if(group==null)
              break;

           CastorCaption footer = group.getFooter();
           CastorCaption header = group.getHeader();

           if(footer==null)
              group.setFooter(footer=new CastorCaption());

           if(header==null)
              group.setHeader(header=new CastorCaption());

           header.setText(section_header[index]);
           footer.setText(section_footer[index]);
           part = group.getPart();
           index++;
        }

        layout.setMimeType("text/formatted");
        report.setDefaultMimeType("text/formatted");
        
        report.save();

        context.getForm().setVisible(false);

        String return_form = (String)context.getProperty(FORM_GUID);
        context.setCurrentForm(return_form);
        context.getDomain().findByName(return_form).setVisible(true);

        out.println("done");
    }
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
