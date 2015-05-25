/*
 * Created on 17.11.2009
 *
 */
package de.tif.jacob.screen.impl.html;

import java.io.Writer;

import de.tif.jacob.core.definition.IToolbarButtonDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ImageDecoration;
import de.tif.jacob.util.StringUtil;

public class CriteriaToolbarButton extends ToolbarButton
{

  public CriteriaToolbarButton(IApplication app, IToolbarButtonDefinition def)
  {
    super(app, def);
  }

  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    if(getCache()==null)
    {
      Writer w = newCache();

        w.write("<button "); 
        if(isEnabled() && !context.isInReportMode())
        {
          w.write("onClick=\"FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("','click')\"");
          w.write(" class=\"toolbarButton_normal search_criteria\"> ");
        }
        else
        {
          w.write("class=\"toolbarButton_disabled search_criteria\">");
        }
        w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
        w.write("</button>");
    }
  }
  
  
}
