/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.flotr;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.PluginComponentImpl;

public class Flotr extends PluginComponentImpl implements IFlotr
{
  private AxisConfiguration xAxis = new AxisConfiguration();
  private AxisConfiguration yAxis = new AxisConfiguration();
  
  List<DataSerie> points = new ArrayList<DataSerie>();
  boolean zoomEnabled =false;
  
  public boolean isZoomEnabled()
  {
    return zoomEnabled;
  }

  public void setZoomEnabled(boolean zoomEnabled)
  {
    this.zoomEnabled = zoomEnabled;
  }

  @Override
  public void addDataFields(Vector fields)
  {
    super.addDataFields(fields);
  }

  public void addDataSerie(IClientContext context, DataSerie serie) throws Exception
  {
    points.add(serie);
    this.resetCache();
  }
  
  public void resetDataSerie(IClientContext context) throws Exception
  {
    points = new ArrayList<DataSerie>();
    this.resetCache();
  }
  
  public AxisConfiguration getXAxis()
  {
    return xAxis;
  }

  public AxisConfiguration getYAxis()
  {
    return yAxis;
  }

  /**
   * 
   *  <div style="border:1px solid gray; overflow: hidden; width: 341px; height: 26px;background:transparent url(./application/CallExpert/0.1/de.tif.jacob.components.plugin.progressbar/full.png?browser=62f4bc91:11fa8064ecf:-7ffe$1);"/>
   *  <img src="./application/CallExpert/0.1/de.tif.jacob.components.plugin.progressbar/empty.png?browser=62f4bc91:11fa8064ecf:-7ffe$1" style="left: 20px; position: relative; top: 0px;border-left:1px solid gray"/>
   *  </div>
   */
  @Override
  public void calculateHTML(ClientContext context, Writer w) throws Exception
  {
    int width = this.wrapper.getBoundingRect().width; 
    int height = this.wrapper.getBoundingRect().height;
    String id = Integer.toString(this.getId());
    w.write("<div id='container"+id+"' style=\"position:absolute; width: "+width+"px; height: "+height+"px;\"></div>");
    
    w.write("<script type=\"text/javascript\">\n");
    for(int i=0; i<points.size(); i++)
    {
      DataSerie serie  = points.get(i);
      w.write("var d_"+id+"_"+i+" = "+serie.toJSON()+";\n");
    }
    w.write("var data_"+id+"= [");
    for(int i=0; i<points.size(); i++)
    {
      w.write("d_"+id+"_"+i);
      if((i+1)<points.size())
        w.write(", ");
    }
    w.write("];\n");
    w.write("var options_"+id+"={");
    w.write("xaxis:");
    w.write(xAxis.toJSON());
    w.write(",");
    
    if(isZoomEnabled())
      w.write("selection:{mode:'xy'},");

    w.write("yaxis:");
    w.write(yAxis.toJSON());
    w.write("};\n");

    w.write("function drawGraph_"+id+"(opts){\n");
    w.write("  var o = Object.extend(Object.clone(options_"+id+"), opts || {});\n");
    w.write("  var f = Flotr.draw($('container"+id+"'), data_"+id+",o);\n");
    w.write("  f.scrollArea = $('formDiv');\n");
    w.write("}\n");
    
    w.write("document.observe('dom:loaded', function()\n");
    w.write("{\n");
    w.write("   drawGraph_"+id+"();\n");
    w.write("});\n");
    w.write("$('container"+id+"').parentNode.style.overflow='visible';\n");
    if(isZoomEnabled())
    {
      w.write("$('container"+id+"').observe('flotr:select', function(evt){\n");
      w.write("  var area = evt.memo[0];\n");
      w.write("  drawGraph_"+id+"({\n");
      w.write("    xaxis: {min:area.x1, max:area.x2},\n");
      w.write("    yaxis: {min:area.y1, max:area.y2} \n");     
      w.write("  });\n");
      w.write("});\n");
    }
    w.write("</script>\n");

    super.calculateHTML(context, w);
  }

  
  @Override
  public void clear(IClientContext context) throws Exception
  {
    super.clear(context);
  }

  @Override
  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    FlotrChartEventHandler handler=(FlotrChartEventHandler)this.wrapper.getEventHandler(context);
    if(handler!=null)
      handler.onGroupStatusChanged(context,newGroupDataStatus,(IFlotr)this);
  }

  @Override
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    return super.processEvent(context, guid, event, value);
  }

  @Override
  public boolean processParameter(int guid, String value)
  {
    return super.processParameter(guid, value);
  }

  @Override
  public Properties getProperties()
  {
    Properties props = new Properties();
    props.put("zoomEnabled",new Boolean(zoomEnabled));
    return props;
  }

  @Override
  public void setProperties(Properties properties)
  {
    zoomEnabled = ((Boolean)properties.get("zoomEnabled")).booleanValue();
  }

  @Override
  public String[] getRequiredIncludeFiles()
  {
    return new String[]
      {
        "base64.js",
        "canvas2image.js",
        "canvastext.js",
        "excanvas.js",
        "flotr-0.2.0-alpha.js"
//        "flotr.debug-0.2.0-alpha.js"
      };
  }
}
