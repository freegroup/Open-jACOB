/*
 * Created on 15.01.2010
 *
 */
package de.tif.jacob.components.dot_cloud_3d;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.PluginComponentImpl;

public class Cloud3D extends PluginComponentImpl implements ICloud3D
{
  private static final List<CloudPoint3D> points = new ArrayList<CloudPoint3D>();

  @Override
  public void addDataFields(Vector fields)
  {
    super.addDataFields(fields);
  }

  @Override
  public String[] getRequiredIncludeFiles()
  {
    return new String[]
        {
        "controls.js",
        "effects.js",
        "slider.js",
        "scriptaulous.js",
        "plugin.js",
        "plugin.css"
        };
  }

  public void calculateHTML(ClientContext context, Writer w) throws Exception
  {

    int width = this.wrapper.getBoundingRect().width-2; 
    int height = this.wrapper.getBoundingRect().height-2;
    String id = Integer.toString(this.getId());
    w.write("<div class=\"cloud_3d\" id='"+id+"' style=\"padding:0px;margin:0px;position:absolute;width: "+width+"px;height: "+height+"px;\">");
    
    w.write("</div>\n");
  }
    
  @Override
  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    Cloud3DEventHandler handler=(Cloud3DEventHandler)this.wrapper.getEventHandler(context);
    if(handler!=null)
      handler.onGroupStatusChanged(context,newGroupDataStatus,(ICloud3D)this);
  }

  @Override
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId())
    {
      return true;
    }
    
    return super.processEvent(context, guid, event, value);
  }
  
  @Override
  public boolean processParameter(int guid, String value)
  {
    return super.processParameter(guid, value);
  }

  public void writeHTMLPart(ClientContext context, Writer w, String partId)
  {
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
  
  @Override
  public void addPoint(CloudPoint3D point)
  {
    points.add(point);
  }

  @Override
  public void clear(IClientContext context) throws Exception
  {
    // TODO Auto-generated method stub
    
  }
  
  
}
