<%@ taglib uri="/WEB-INF/taglib.tld" prefix="j" %><%@ page import="java.util.*"
    import="de.tif.jacob.license.*"
    import="de.tif.jacob.core.*"
    import="de.tif.jacob.util.*"
    import="de.tif.jacob.core.exception.*"
    import="de.tif.jacob.screen.event.*"
    import="de.tif.jacob.screen.impl.*"
    import="de.tif.jacob.screen.impl.html.*"
    import="de.tif.jacob.screen.impl.html.dialogs.*"
    import="de.tif.jacob.security.*" 
    
%><%

try
{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

    String browserId  = request.getParameter("browser");
    String control    = request.getParameter("control");
    String userInput  = request.getParameter("userInput");
    String guid       = request.getParameter("guid");
    String cp = request.getParameter("caretPosition");
    int    caretPosition=0;
    if(cp!=null)
        caretPosition=org.apache.commons.lang.NumberUtils.stringToInt(cp,0);
    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    Application app = null;
    if(jacobSession!=null)
        app=(Application)jacobSession.getApplication(browserId);
    FormDialog  dialog     = (FormDialog)jacobSession.getDialog(guid);


    de.tif.jacob.core.JspContext context = new de.tif.jacob.core.JspContext(app.getApplicationDefinition(), jacobSession.getUser());
    Context.setCurrent(context);

    StringBuffer result = new StringBuffer();
    result.append("[");
    IAutosuggestProvider provider = dialog.getAutosuggestProvider(control);
    if(provider!=null)
    {
	    IAutosuggestProvider.AutosuggestItem[] suggestion=provider.suggest(context,userInput,caretPosition);
	    if(suggestion!=null)
	    {
	      dialog.setLastProvidedAutosuggestItems(provider, suggestion);
		    for(int i=0;i<suggestion.length;i++)
		    {
		    	result.append("\""+suggestion[i].getLabel()+"\"");
		      if((i+1)<suggestion.length)
		      	result.append(",");
		    }
	    }
    }
    result.append("]");
    out.println(result.toString());
}
catch(Exception e)
{
  e.printStackTrace();
}
%>
