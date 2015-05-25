package de.tif.jacob.components.flotr;



public class DataSerie
{
  public class MouseConfiguration
  {
    boolean track= false;     // => enable Mouse tracking
    int sensibility= 2;       // => sensibility
    int trackDecimals= 0;     // => Anzahl der angezeigten Nachkommastellen
    String color= null;       // => Farbe des Trackpunkt
    JSONJavaScriptFunction trackFormatter = null; //
    
    public JSONJavaScriptFunction getTrackFormatter()
    {
      return trackFormatter;
    }
    public void setTrackFormatter(String trackFormatter)
    {
      this.trackFormatter = new JSONJavaScriptFunction(trackFormatter);
    }
    
    public boolean isTrack()
    {
      return track;
    }
    public void setTrack(boolean track)
    {
      this.track = track;
    }
    public int getSensibility()
    {
      return sensibility;
    }
    public void setSensibility(int sensibility)
    {
      this.sensibility = sensibility;
    }
    public int getTrackDecimals()
    {
      return trackDecimals;
    }
    public void setTrackDecimals(int trackDecimals)
    {
      this.trackDecimals = trackDecimals;
    }
    public String getColor()
    {
      return color;
    }
    public void setColor(String color)
    {
      this.color = color;
    }
  }
  
  public class BarConfiguration
  {
    boolean show= false;    // => setting to true will show bars, false will hide
    int lineWidth= 2;       // => in pixels
    int barWidth= 1;        // => in units of the x axis
    boolean fill= true;     // => true to fill the area from the line to the x axis, false for (transparent) no fill
    String fillColor= null; // => fill color
    public boolean isShow()
    {
      return show;
    }
    public void setShow(boolean show)
    {
      this.show = show;
    }
    public int getLineWidth()
    {
      return lineWidth;
    }
    public void setLineWidth(int lineWidth)
    {
      this.lineWidth = lineWidth;
    }
    public int getBarWidth()
    {
      return barWidth;
    }
    public void setBarWidth(int barWidth)
    {
      this.barWidth = barWidth;
    }
    public boolean isFill()
    {
      return fill;
    }
    public void setFill(boolean fill)
    {
      this.fill = fill;
    }
    public String getFillColor()
    {
      return fillColor;
    }
    public void setFillColor(String fillColor)
    {
      this.fillColor = fillColor;
    }
  }
  
  public class LineConfiguration
  {
    boolean show= false;    // => setting to true will show lines, false will hide
    int lineWidth= 2;       // => line width in pixels
    boolean fill= false;    // => true to fill the area from the line to the x axis, false for (transparent) no fill
    String fillColor= null; // => fill color
    
    public boolean isShow()
    {
      return show;
    }
    public void setShow(boolean show)
    {
      this.show = show;
    }
    public int getLineWidth()
    {
      return lineWidth;
    }
    public void setLineWidth(int lineWidth)
    {
      this.lineWidth = lineWidth;
    }
    public boolean isFill()
    {
      return fill;
    }
    public void setFill(boolean fill)
    {
      this.fill = fill;
    }
    public String getFillColor()
    {
      return fillColor;
    }
    public void setFillColor(String fillColor)
    {
      this.fillColor = fillColor;
    }
  }
  
  public class PointConfiguration
  {
    boolean show= false;          // => setting to true will show points, false will hide
    int radius= 3;                // => point radius (pixels)
    int lineWidth= 2;             // => line width in pixels
    boolean fill= true;           // => true to fill the points with a color, false for (transparent) no fill
    String fillColor= "#ffffff";  // => fill color
    public boolean isShow()
    {
      return show;
    }
    public void setShow(boolean show)
    {
      this.show = show;
    }
    public int getRadius()
    {
      return radius;
    }
    public void setRadius(int radius)
    {
      this.radius = radius;
    }
    public int getLineWidth()
    {
      return lineWidth;
    }
    public void setLineWidth(int lineWidth)
    {
      this.lineWidth = lineWidth;
    }
    public boolean isFill()
    {
      return fill;
    }
    public void setFill(boolean fill)
    {
      this.fill = fill;
    }
    public String getFillColor()
    {
      return fillColor;
    }
    public void setFillColor(String fillColor)
    {
      this.fillColor = fillColor;
    }
  }
  
  PointConfiguration points = new PointConfiguration();
  BarConfiguration   bars = new BarConfiguration();
  LineConfiguration  lines = new LineConfiguration();
  MouseConfiguration mouse = new MouseConfiguration();
  
  Integer[][]  data = new Integer[0][0];
  String label = "test";
  String color=null;
  
  public DataSerie(String name)
  {
    this.label = name;
  }

  public String getColor()
  {
    return color;
  }
  public void setColor(String color)
  {
    this.color = color;
  }
  
  public DataSerie(String name, DataPoint[] points)
  {
    this.label = name;
    setData(points);
  }
  
  public String getLabel()
  {
    return this.label;
  }
  public Integer[][] getData()
  {
    return data;
  }
  
  public void setData(Integer[] newData)
  {
    this.data = new Integer[newData.length][2];
    for(int i=0; i<newData.length;i++)
    {
      Integer[] fulcrum = new Integer[2];
      fulcrum[0] =new Integer(i);
      fulcrum[1] =newData[i];
      this.data[i]=fulcrum;
    }
  }
  
  public void setData(DataPoint[] newData)
  {
    this.data = new Integer[newData.length][2];
    for(int i=0; i<newData.length;i++)
    {
      Integer[] fulcrum = new Integer[2];
      fulcrum[0] =newData[i].index;
      fulcrum[1] =newData[i].value;
      this.data[i]=fulcrum;
    }
  }

  public MouseConfiguration getMouse()
  {
    return mouse;
  }

  public PointConfiguration getPoints()
  {
    return points;
  }


  public BarConfiguration getBars()
  {
    return bars;
  }

  public LineConfiguration getLines()
  {
    return lines;
  }

  public String toJSON() throws Exception
  {
    JSONObject obj = new JSONObject(this,false);
    return obj.toString().replaceAll("\"", "'");
  }
}
