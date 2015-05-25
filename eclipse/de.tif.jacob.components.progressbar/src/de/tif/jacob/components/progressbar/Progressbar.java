/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.progressbar;

import java.io.Writer;
import java.util.Properties;
import java.util.Vector;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.PluginComponentImpl;

public class Progressbar extends PluginComponentImpl implements IProgressbar
{
  // values between 0-100
  int percentage=0;
  
  @Override
  public void addDataFields(Vector fields)
  {
    super.addDataFields(fields);
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
    int width = this.wrapper.getBoundingRect().width-2; // "-2" required for the 1px border around 
    int height = this.wrapper.getBoundingRect().height-2;
    
    int barWidth = (int)((width/100.0)*percentage);
    w.write("<div style=\"position:absolute;border:1px solid gray; overflow: hidden; width: "+width+"px; height: "+height+"px;background:transparent url(");
    w.write(getWebResourceURL(context,"full.png"));
    w.write(")\">");
    w.write("<img src=\""+getWebResourceURL(context,"empty.png")+"\" style=\"left: "+barWidth+"px; position: absolute; top: 0px;border-left:1px solid gray\"/>");
    w.write("</div>");
    w.write("<div style=\"text-align:center;position:absolute;top:0px;left:0px;width:"+width+"px;\" class=\"caption_normal_search\">"+percentage+" %</div>");
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
    ProgressbarEventHandler handler=(ProgressbarEventHandler)this.wrapper.getEventHandler(context);
    if(handler!=null)
      handler.onGroupStatusChanged(context,newGroupDataStatus,(IProgressbar)this);
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

  private String getWebResourceURL(ClientContext context, String file)
  {
    return "./application/"+context.getApplication().getName()+"/"+context.getApplication().getVersion()+"/"+PluginId.ID+"/"+file;
  }

  public void setPrecentage(int percentage) throws IndexOutOfBoundsException
  {
    if(percentage<0 || percentage >100)
      throw new IndexOutOfBoundsException("percentage must be between [0;100]");
    this.wrapper.resetCache();
    this.percentage=percentage;
  }

  @Override
  public Properties getProperties()
  {
    Properties props = new Properties();
    props.put("percentage",new Integer(percentage));
    return props;
  }

  @Override
  public void setProperties(Properties properties)
  {
    percentage = ((Integer)properties.get("percentage")).intValue();
  }
}
