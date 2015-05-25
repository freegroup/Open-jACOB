package jacob.common.ui;

import jacob.browser.GlobalcontentBrowser;
import jacob.model.Globalcontent;

import java.io.IOException;
import java.io.Writer;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.IToolbarDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.Form;
import de.tif.jacob.screen.impl.html.Toolbar;

public class SearchToolbar extends Toolbar
{
  String lastSearchString ="";
  
  public SearchToolbar(IApplication app, IToolbarDefinition def)
  {
    super(app, def);
  }

  @Override
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
     w.write("\n<div id=\"searchbar_container\" style=\"text-align:right;width:100%;height:20px\" >");
     w.write("Suchen: <input style=\"\" id=\"searchbar_text\" type=\"text\" value=\""+lastSearchString+"\"></input><button onclick=\"FireEventData('"+this.getId()+"','search',$F('searchbar_text'))\">Search</button>");
     w.write("</div>\n");
     context.addOnLoadJavascript
      (
          "Event.observe($(\"searchbar_text\"),\"keydown\",function(event)\n"+
          "    {\n"+
          "       if(event.keyCode === 13)\n"+
          "       {\n"+
          "          Event.stop(event);\n"+
          "          FireEventData('"+this.getId()+"','search',$F('searchbar_text'));\n"+
          "       }\n"+
          "    });\n"+
          "Event.observe(window,\"resize\",function(event)\n"+
          "    {\n"+
          "       var e= $('searchbar_container');\n"+
          "       e.style.width=($('body').getWidth()-e.parentNode.cumulativeOffset().left)+'px';\n"+
          "     \n"+
          "     \n"+
          "     \n"+
          "    });\n"
          );
      
  }
  
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if (guid != this.getId())
      return super.processEvent(context, guid, event, value);
    lastSearchString = value;
    context.setCurrentForm("documents","documents_search");
    // BUG!!!! bei der SearchToolbar wird in dieser Methode der Context/Accessor nicht richtig gesetzt 
    // Ich hole mir den Accessor dann eben direkt...erstmal
    //
    Form form = (Form)context.getApplication().findByName("documents_search");
    IDataAccessor acc = form.getDataAccessor();
    
    acc.clear();
    IDataTable searchTable = acc.getTable(Globalcontent.NAME);
    IDataBrowser searchBrowser = acc.getBrowser(GlobalcontentBrowser.NAME);
    
    searchTable.qbeSetValue(Globalcontent.contents, value+"*");
    searchBrowser.search(IRelationSet.LOCAL_NAME);
    if(searchBrowser.recordCount()>0)
    {
      context.getGUIBrowser().setSelectedRecordIndex(context, 0);
    }
    return true;
  }
}
