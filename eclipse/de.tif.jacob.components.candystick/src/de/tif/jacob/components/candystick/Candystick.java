/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.candystick;

import java.awt.Color;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.PluginComponentImpl;
import de.tif.jacob.util.StringUtil;

public class Candystick extends PluginComponentImpl implements ICandystick
{
  public final static String   PROPERTY_ORIENTATION = "Orientation";
  public final static String   PROPERTY_REFLECTION  = "Reflection Transparency";
  public final static String   PROPERTY_RADIUS      = "Radius";
  public final static String   PROPERTY_TOOLTIP     = "Tooltip";

  public final static String[] orientations         = new String[] { "vertical", "horizontal" };
  public final static String[] reflections          = new String[] { "0.001", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0" };
  public final static String[] radius               = new String[] { "0.001", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0" };
  
  private Collection<Float>    values               = new ArrayList<Float>();
  private Collection<Color>    colors               = new ArrayList<Color>();
  
  private String               i18nTooltip          = null;                                                                                           ;

  public void addValue(double value, Color color)
  {
    this.addValue((float)value,color);
  }

  public void addValue(float value, Color color)
  {
    values.add(value);
    colors.add(color);
    this.wrapper.resetCache();
  }

  public void clear()
  {
    values.clear();
    colors.clear();
    this.wrapper.resetCache();
  }

  /**
   * 
   * <div style="border:1px solid gray; overflow: hidden; width: 341px; height: 26px;background:transparent url(./application/CallExpert/0.1/de.tif.jacob.components.plugin.progressbar/full.png?browser=62f4bc91:11fa8064ecf:-7ffe$1);"
   * /> <img src="./application/CallExpert/0.1/de.tif.jacob.components.plugin.progressbar/empty.png?browser=62f4bc91:11fa8064ecf:-7ffe$1"
   * style
   * ="left: 20px; position: relative; top: 0px;border-left:1px solid gray"/>
   * </div>
   */
  @Override
  public void calculateHTML(ClientContext context, Writer w) throws Exception
  {
    CandystickEventHandler handler = (CandystickEventHandler) this.wrapper.getEventHandler(context);
    boolean onClickHandler = (handler instanceof IOnClickEventHandler);

    int width = this.wrapper.getBoundingRect().width;
    int height = this.wrapper.getBoundingRect().height;
    String id = Integer.toString(this.getId());
    if (onClickHandler)
    {
      w.write("<div title=\"" + getI18NTooltip(context) + "\" onClick=\"FireEvent('" + Integer.toString(getId()) + "', 'click')\"  id=\"container" + id
          + "\" style=\"cursor:pointer;position:absolute; width: " + width + "px; height: " + height + "px;\"></div>");
    }
    else
    {
      w.write("<div title=\"" + getI18NTooltip(context) + "\" id=\"container" + id + "\" style=\"position:absolute; width: " + width + "px; height: " + height + "px;\"></div>");
    }

    w.write("<script type=\"text/javascript\">\n");
    w.write("document.observe('dom:loaded', function()\n");
    w.write("{\n");
    w.write("   candystick.add($('container" + id + "'), {");
    w.write("width:" + width + ", ");
    w.write("height:" + height + ", ");
    w.write("vertical:" + isVertical() + ", "); // true | false
    w.write("reflex: " + getReflection() + ", "); // reflex == FLOAT 0.001 - 1.0
    w.write("radius: " + getRadius() + ", "); // reflex == FLOAT 0.001 - 1.0
    w.write("limit: true, ");
    w.write("scale: 10, ");
    w.write("colors:[");
    boolean first = true;
    for (Color color : colors)
    {
      if (first)
        w.write("'" + toCSSString(color) + "'");
      else
        w.write(", '" + toCSSString(color) + "'");
      first = false;
    }
    w.write("], ");
    w.write("values:[");
    first = true;
    if (values.size() > 0)
    {
      for (Float value : values)
      {
        if (first)
          w.write("" + value);
        else
          w.write(", " + value + "");
        first = false;
      }
    }
    else
    {
      w.write("0");
    }
    w.write(",1]});\n");
    w.write("});\n");
    w.write("</script>\n");

    super.calculateHTML(context, w);
  }

  private boolean isVertical()
  {
    return StringUtil.saveEquals(this.getProperty(PROPERTY_ORIENTATION), orientations[0]);
  }

  private float getReflection()
  {
    try
    {
      return Float.valueOf(this.getProperty(PROPERTY_REFLECTION));
    }
    catch (Exception exc)
    {
      // ignore
    }
    return Float.valueOf(reflections[0]);
  }

  private float getRadius()
  {
    try
    {
      return Float.valueOf(this.getProperty(PROPERTY_RADIUS));
    }
    catch (Exception exc)
    {
      // ignore
    }
    return Float.valueOf(radius[radius.length - 1]);
  }

  private String getI18NTooltip(IClientContext context)
  {
    if (i18nTooltip == null)
    {
      String tooltip = this.getProperty(PROPERTY_TOOLTIP);
      if (StringUtil.emptyOrNull(tooltip))
        return "";

      i18nTooltip = I18N.localizeLabel(tooltip, this.getApplicationDefinition(), context.getLocale());
    }
    return i18nTooltip;
  }

  @Override
  public void clear(IClientContext context) throws Exception
  {
    super.clear(context);
  }

  @Override
  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    CandystickEventHandler handler = (CandystickEventHandler) this.wrapper.getEventHandler(context);
    if (handler != null)
      handler.onGroupStatusChanged(context, newGroupDataStatus, (ICandystick) this);
  }

  @Override
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if (guid == this.getId())
    {
      if (event.equals("click"))
      {
        CandystickEventHandler handler = (CandystickEventHandler) this.wrapper.getEventHandler(context);
        if (handler instanceof IOnClickEventHandler)
          ((IOnClickEventHandler) handler).onClick(context, this);
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

  @Override
  public Properties getProperties()
  {
    Properties props = new Properties();
    // props.put("zoomEnabled",new Boolean(zoomEnabled));
    return props;
  }

  @Override
  public void setProperties(Properties properties)
  {
    // zoomEnabled = ((Boolean)properties.get("zoomEnabled")).booleanValue();
  }

  @Override
  public String[] getRequiredIncludeFiles()
  {
    return new String[] { "candystick.js" };
  }

  private final static String toCSSString(Color c)
  {
    String colorR = "0" + Integer.toHexString(c.getRed());
    colorR = colorR.substring(colorR.length() - 2);
    String colorG = "0" + Integer.toHexString(c.getGreen());
    colorG = colorG.substring(colorG.length() - 2);
    String colorB = "0" + Integer.toHexString(c.getBlue());
    colorB = colorB.substring(colorB.length() - 2);
    return "#" + colorR + colorG + colorB;
  }
}
