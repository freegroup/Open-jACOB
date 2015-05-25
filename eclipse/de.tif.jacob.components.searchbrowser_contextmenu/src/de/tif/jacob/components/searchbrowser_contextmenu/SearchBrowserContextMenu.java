/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.searchbrowser_contextmenu;

import java.io.Writer;
import java.util.Properties;
import java.util.Vector;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.PluginComponentImpl;

public class SearchBrowserContextMenu extends PluginComponentImpl implements ISearchBrowserContextMenu
{

  @Override
  public void addDataFields(Vector fields)
  {
    super.addDataFields(fields);
  }

  @Override
  public String[] getRequiredIncludeFiles()
  {
    return new String[]{"plugin.js"};
  }

  /**
   * 
   *  <div style="border:1px solid gray; overflow: hidden; width: 341px; height: 26px;background:transparent url(./application/CallExpert/0.1/de.tif.jacob.components.progressbar/full.png?browser=62f4bc91:11fa8064ecf:-7ffe$1);"/>
   *  <img src="./application/CallExpert/0.1/de.tif.jacob.components.progressbar/empty.png?browser=62f4bc91:11fa8064ecf:-7ffe$1" style="left: 20px; position: relative; top: 0px;border-left:1px solid gray"/>
   *  </div>
   */
  @Override
  public void calculateHTML(ClientContext context, Writer w) throws Exception
  {
    w.write("<script>\n");
    w.write("document.observe('dom:loaded', function()\n");
    w.write("{\n");
    w.write("   $('searchBrowserTable').observe('contextmenu', function(event){searchBrowserContextMenu(event,'"+this.getId()+"');});\n");
    w.write("});\n");
    w.write("</script>\n");
  }

  
  @Override
  public void clear(IClientContext context) throws Exception
  {
    super.clear(context);
  }

  @Override
  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    SearchBrowserContextMenuEventHandler handler=(SearchBrowserContextMenuEventHandler)this.wrapper.getEventHandler(context);
    if(handler!=null)
      handler.onGroupStatusChanged(context,newGroupDataStatus,(ISearchBrowserContextMenu)this);
  }

  @Override
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId())
    {
      SearchBrowserContextMenuEventHandler handler=(SearchBrowserContextMenuEventHandler)this.wrapper.getEventHandler(context);
      if(handler!=null)
      {
        String s[] = value.split(":");
        int row = Integer.parseInt(s[0]);
        int column = Integer.parseInt(s[1]);
        IDataBrowserRecord record = this.getOuterGroup().getBrowser().getDataRecord(row);
        IBrowserField field = record.getBrowser().getBrowserDefinition().getBrowserField(column);
        handler.executeCommand(context,this.getOuterGroup().getBrowser(),record, field, s[2]);
      }
      return true;
    }
    
    return super.processEvent(context, guid, event, value);
  }

  @Override
  public boolean processParameter(int guid, String value)
  {
    return super.processParameter(guid, value);
  }

  private String getWebResourceURL(ClientContext context, String file)
  {
    return "./application/"+context.getApplication().getName()+"/"+context.getApplication().getVersion()+"/"+PluginId.ID+"/"+file;
  }

  @Override
  public Properties getProperties()
  {
    Properties props = new Properties();
//    props.put("percentage",new Integer(percentage));
    return props;
  }

  @Override
  public void setProperties(Properties properties)
  {
//    percentage = ((Integer)properties.get("percentage")).intValue();
  }
}
