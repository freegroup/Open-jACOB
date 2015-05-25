package de.tif.jacob.components.flotr;

public class JSONJavaScriptFunction implements JSONString
{
  final String function;
  
  public JSONJavaScriptFunction(String function)
  {
    this.function = function;
  }
  
  public String toJSONString()
  {
    return this.function;
  }
}
