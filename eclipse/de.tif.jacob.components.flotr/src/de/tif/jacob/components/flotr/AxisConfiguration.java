/**
 * 
 */
package de.tif.jacob.components.flotr;

public class AxisConfiguration
{
  protected Object[][] ticks = null;
  protected Integer tickDecimals= null;         // => no. of decimals, null means auto
  
  public Integer getTickDecimals()
  {
    return tickDecimals;
  }

  public void setTickDecimals(Integer tickDecimals)
  {
    this.tickDecimals = tickDecimals;
  }

  public Object[][] getTicks()
  {
    return ticks;
  }
  
  public void setTicks(Tick[] newTicks)
  {
    if(newTicks==null)
    {
      this.ticks=null;
      return;
    }
    this.ticks = new Object[newTicks.length][2];
    for(int i=0; i<newTicks.length;i++)
    {
      Object[] fulcrum = new Object[2];
      fulcrum[0] =newTicks[i].index;
      fulcrum[1] =newTicks[i].label;
      this.ticks[i]=fulcrum;
    }
  }
  public String toJSON() throws Exception
  {
    JSONObject obj = new JSONObject(this,false);
    return obj.toString().replaceAll("\"", "'");
  }
}