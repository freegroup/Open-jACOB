<%@ page import="java.util.*" %>
<%@ page import="de.tif.jacob.i18n.*" %>
<%@ page import="de.tif.jacob.core.*" %>
<%@ page import="de.tif.jacob.core.data.*" %>
<%@ page import="de.tif.jacob.core.definition.*" %>
<%@ page import="de.tif.jacob.core.definition.impl.jad.*" %>
<%@ page import="de.tif.jacob.core.definition.impl.jad.castor.*" %>
<%@ page import="de.tif.jacob.core.definition.guielements.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.security.*" %>
<%@ page import="de.tif.jacob.screen.*" %>
<%@ page import="de.tif.jacob.screen.impl.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.*" %>
<%@ page import="de.tif.jacob.screen.impl.html.dialogs.*" %>
<%@ page import="de.tif.jacob.deployment.*" %>
<%@ page import="de.tif.jacob.util.search.spotlight.*" %>
{
<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
try
{
    String browserId = request.getParameter("browser");
    String searchTerm = request.getParameter("query");

    if(!UserManagement.isLoggedInUser(request,response))
        return;

    out.println("query:'"+searchTerm+"',");
    HTTPClientSession jacobSession = HTTPClientSession.get(request);
    if(jacobSession!=null)
    {
        Application app =(Application)jacobSession.getApplication(browserId);
        ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
        Context.setCurrent(context);
        int lastId=-1;
        String lastMatch="";
        int maxResultItems = 15;
        
        try
        {
           List hits = Spotlight.search(app,searchTerm);
           // Die Trefferlist wird so lange ausgedünnt bis die Anzal der Treffer 15 ist
     	   int percentage = 50;
     	   List newHits = new ArrayList();
     	   while(percentage<=100 && hits.size()>maxResultItems)
     	   {
     	     
     	     for(int i=0;i<hits.size();i++)
     	     {
     	         SpotlightResult hit = (SpotlightResult)hits.get(i);
     	         if(hit.getPercent()>=percentage)
     	            newHits.add(hit);
     	     }
     	     hits=newHits;
     	     newHits = new ArrayList();
     	     percentage++;
     	   }
           
           // Jetzt sind alle Elemente aus der Liste draussen welche <100% sind.
           // Jetzt muss weiter ausgedünnt werden. Aus jeder Treffergruppe elemente Streichen
           // maximal 5 pro gruppe
     	   newHits = new ArrayList();
     	   int maxHitsPerGroup=10;
     	   while(maxHitsPerGroup!=1 && hits.size()>maxResultItems)
     	   {
        	   int groupCount=0;
     	       String lastDomain="";
   	           for(int i=0;i<hits.size();i++)
     	       {
     	          SpotlightResult hit = (SpotlightResult)hits.get(i);
     	          if(i==0 || lastDomain.equals(hit.getDomain().getName()))
     	         	groupCount++;
     	          else
     	            groupCount=0;
     	        
     	          if(groupCount<maxHitsPerGroup)
     	             newHits.add(hit);

     	          lastDomain = hit.getDomain().getName();
     	       }
               hits = newHits;
               newHits = new ArrayList();
               maxHitsPerGroup--;
           }
           
           
           out.println("suggestions:[");
       	   for(int i=0;i<hits.size();i++)
           {
             SpotlightResult hit = (SpotlightResult)hits.get(i);
             if(lastId==hit.getElement().getId() && lastMatch.equals(hit.getMatch()))
                continue;
             lastId=hit.getElement().getId();
             lastMatch=hit.getMatch();
             if(i==0)
             {
                out.println("{match:'"+hit.getMatch()+"', id:'"+hit.getElement().getId()+"', domain:'"+hit.getDomain().getI18NLabel(context.getLocale())+"'}");
             }
             else
             {
                out.println(",{match:'"+hit.getMatch()+"', id:'"+hit.getElement().getId()+"', domain:'"+hit.getDomain().getI18NLabel(context.getLocale())+"'}");
             }
           }
           out.println("]");
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }
}
catch(Exception exc)
{
    exc.printStackTrace();
}
%>
}

