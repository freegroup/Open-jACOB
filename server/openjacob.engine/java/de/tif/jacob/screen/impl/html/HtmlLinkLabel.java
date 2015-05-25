/*
 * Created on 27.09.2010
 *
 */
package de.tif.jacob.screen.impl.html;

import java.awt.Color;
import java.io.Writer;

import de.tif.jacob.screen.ILinkLabel;
import de.tif.jacob.util.FastStringWriter;

public class HtmlLinkLabel extends ILinkLabel
{
  final String label;
  private boolean bold = false;
  private Color color= Color.BLUE;
  private boolean italic = false;
  private boolean strike = false;
  private boolean underline = true;
  
  public HtmlLinkLabel(String label )
  {
    this.label = label;
  }
  
  public String getHTML() throws Exception
  {
     Writer w = new FastStringWriter(512);
     w.write("<span style=\"");
     if(bold)
         w.write("font-weight:bold;");
     else
       w.write("font-weight:normal;");

    w.write("color:");
    w.write(GuiHtmlElement.toCSSString(this.color));

    if(italic)
      w.write(";font-style:italic;");
    else
     w.write(";font-style:normal;");

    if(strike)
    {
      w.write("text-decoration: line-through;");
    }
    else
    {
      // konflikt mit strike.....aber egal. UNDERLINE gewinnt jetzt halt
      if(underline)
        w.write("text-decoration: underline;");
      else
       w.write("text-decoration: none");
    }
    w.write("\">");
    w.write(this.label);
    w.write("</span>");
    return w.toString();
 }
  
  public void setBold(boolean flag)
  {
    this.bold = flag;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public void setItalic(boolean flag)
  {
   this.italic = flag;
  }

  public void setStrike(boolean flag)
  {
    this.strike = flag;
  }

  public void setUnderline(boolean flag)
  {
    this.underline = flag;
  }
}
