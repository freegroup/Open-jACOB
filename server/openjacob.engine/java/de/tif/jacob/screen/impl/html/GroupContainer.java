/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen.impl.html;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import de.tif.jacob.core.definition.IGroupContainerDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.impl.HTTPGroupContainer;

public class GroupContainer extends GuiHtmlElement implements HTTPGroupContainer
{
  Group activeGroup = null;
  
  public GroupContainer(IApplication app, IGroupContainerDefinition container)
  {
    super(app,container.getName(),"",true, container.getRectangle(), container.getProperties());
  }

  public void addChild(IGuiElement child)
  {
    super.addChild(child);
    activeGroup = (Group)child;
  }

  public IGroup getGroup(int index)
  {
    return (IGroup)getChild(index);
  }


  public List getBrowsers()
  {
    List browsers = new ArrayList();
    Iterator iter = getGroups().iterator();
    while (iter.hasNext())
    {
      IGroup group = (IGroup) iter.next();
      browsers.add(group.getBrowser());
    }
    
    return browsers;
  }

  public ISingleDataGuiElement getFirstElementInTabOrder()
  {
    return activeGroup.getFirstElementInTabOrder();
  }

  public void onHide(IClientContext context) throws Exception
  {
    activeGroup.onHide(context);
  }

  public void onShow(IClientContext context) throws Exception
  {
    activeGroup.onShow(context);
  }

  public IGroup getGroup(String groupName)
  {
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      IGroup element = (IGroup)iter.next();
      if(element.getName().equals(name))
        return element;
    }
    return null;
  }


  public List getGroups()
  {
    return getChildren();
  }


  /**
   * 
   */
  public void addDataFields(Vector fields)
  {
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement)iter.next();
      element.addDataFields(fields);
    }
  }
 

  public String getEventHandlerReference()
  {
    return null;
  }


  /**
   * Return the HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement)iter.next();
      element.calculateHTML(context);
    }
  }
  
  
  /** 
   * ATTENTION: This implementation differs from the super implementation. See the sequenze of the calls
   *            FIRST write me than the childs!!!!
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    activeGroup.writeHTML(context,w);
  }
}










