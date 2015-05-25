/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.groupedlist;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ILabelProvider;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.screen.impl.html.PluginComponentImpl;
import de.tif.jacob.util.StringUtil;

public class GroupedListbox extends PluginComponentImpl implements IGroupedListbox
{
  class Entry
  {
    String errorDecoration = null;
    boolean selectable =true;
    boolean selected = true;
    boolean emphazise = false;
    Object data="-no data-";
  }
  
  class Group
  {
    String label="group";
    boolean selected=true;
    boolean expanded =false;
    boolean allowSingleSelection=true;
    List<Entry> entries = new ArrayList<Entry>();
  }
  
  List<Group> groups = new ArrayList<Group>();
  ILabelProvider labelProvider =null;
  int scrollLeft = 0;
  int scrollTop=0;
  boolean editable=true;
  
  @Override
  public void addDataFields(Vector fields)
  {
    super.addDataFields(fields);
  }

  @Override
  public String[] getRequiredIncludeFiles()
  {
    return new String[]{"plugin.js"};
  }

  public void addGroup(String groupHeader, List<Object> groupElements, boolean allowSingleSelection) throws Exception
  {
    Group group = new Group();
    group.label=groupHeader;
    group.allowSingleSelection =allowSingleSelection;
    
    for (Object object : groupElements)
    {
      Entry entry = new Entry();
      entry.data = object;
      group.entries.add(entry);
    }
    groups.add(group);
    this.resetCache();
  }


  public boolean isSelectet(Object obj)
  {
    for (Group group : groups)
    {
      for (Entry entry : group.entries)
      {
        if(entry.data==obj)
          return entry.selected;
      }
    }
    return false;
  }
  
  public ISelection getSelection(IClientContext context) throws Exception
  {
    GroupedListboxSelection selection = new GroupedListboxSelection(this);
    for (Group group : groups)
    {
      for (Entry entry : group.entries)
      {
        if(entry.selected)
          selection.add(entry.data);
      }
    }
    return selection;
  }

  public void resetSelection(IClientContext context) throws Exception
  {
    for (Group group : groups)
    {
      for (Entry entry : group.entries)
      {
        entry.selected=false;
      }
    }
    this.resetCache();
  }

  public void selectAll( boolean selectionFlag)
  {
    for (Group group : groups)
    {
      for (Entry entry : group.entries)
      {
        if(entry.selectable)
          entry.selected=selectionFlag;
      }
      group.selected=selectionFlag;
    }
    this.resetCache();
  }
  
  public void select(List<Object> objs, boolean selectionFlag)
  {
    for (Object object : objs)
    {
      select(object,selectionFlag);
    }
    this.resetCache();
  }

  public void select(Object obj, boolean selectionFlag)
  {
    for (Group group : groups)
    {
      boolean allOfTheGroupSelected=true;
      boolean found =false;
      for (Entry entry : group.entries)
      {
        if(entry.data==obj)
        {
          entry.selected=selectionFlag;
          found=true;
        }
        allOfTheGroupSelected = allOfTheGroupSelected && entry.selected;
      }
      if(found)
      {
        group.selected=allOfTheGroupSelected;
        break;
      }
    }
    this.resetCache();
  }
  
  private void selectGroup(Group group, boolean selectionFlag)
  {
    for (Entry entry : group.entries)
    {
      entry.selected=selectionFlag;
    }
    group.selected = selectionFlag;
    this.resetCache();
  }
  
  public void setEmphasize(Object obj, boolean b)
  {
    for (Group group : groups)
    {
      for (Entry entry : group.entries)
      {
        if(entry.data==obj)
          entry.emphazise=b;
      }
    }
    this.resetCache();
  }
  
  public void setErrorDecoration(Object obj, String message)
  {
    for (Group group : groups)
    {
      for (Entry entry : group.entries)
      {
        if(entry.data==obj)
          entry.errorDecoration=message;
      }
    }
    this.resetCache();
    
  }

  public void setLabelProvider(ILabelProvider labelProvider) throws Exception
  {
    this.labelProvider = labelProvider;
    this.resetCache();
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
    boolean provideOnClick = hasOnClickOverriden(context);

    int width = this.wrapper.getBoundingRect().width-2; 
    int height = this.wrapper.getBoundingRect().height-2;
    String id = Integer.toString(this.getId());
    w.write("<div class=\"grouped_listbox text_normal\" id='"+id+"' style=\"padding:0px;margin:0px;overflow:auto;position:absolute;width: "+width+"px;height: "+height+"px;\">");
    w.write("<ul class=\"grouped_listbox_root\" style=\"padding:0px;margin:0px;list-style:none\">\n");
    for (Group group : groups)
    {
      w.write("<li>\n");
      w.write("<div class=\"heading\">");
      
      if(group.expanded)
      {
        w.write("<img class=\"grouped_listbox_collapse\" style=\"vertical-align:middle\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("minus.png"));
        w.write("\" expanded=\"true\"/>");
      }
      else
      {
        w.write("<img class=\"grouped_listbox_collapse\" style=\"vertical-align:middle\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("plus.png"));
        w.write("\" expanded=\"false\"/>");
      }
      
      if(this.isEditable())
      {
        if(group.selected)
        {
          w.write("<img class=\"grouped_listbox_checkbox\" style=\"vertical-align:middle\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("checked.png"));
          w.write("\" checked=\"true\"/>");
        }
        else
        {
          w.write("<img class=\"grouped_listbox_checkbox\" style=\"vertical-align:middle\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("unchecked.png"));
          w.write("\" checked=\"false\"/>");
        }
      }
      w.write(StringUtil.htmlEncode(group.label));
      w.write("</div>\n");
      if(group.expanded)
        calculateEntryHTML(context, w, group, provideOnClick);
      w.write("</li>");
    }
    w.write("</ul>\n");
    w.write("</div>\n");
    w.write("<script>Event.observe(window, 'load', function(){var e=$('"+id+"');e.scrollTop="+scrollTop+";e.scrollLeft="+scrollLeft+";});</script>\n");
  }

  public void calculateEntryHTML(ClientContext context, Writer w, Group group, boolean provideOnClick) throws Exception
  {
    w.write("<ul class=\"grouped_listbox_group\" style=\"list-style:none\">\n");
    for (Entry entry : group.entries)
    {
      String label;
      if(labelProvider!=null)
        label =StringUtil.htmlEncode(labelProvider.getText(context,entry.data));
      else
        label = StringUtil.htmlEncode(entry.data.toString());
      if(entry.selected)
      {
        w.write("<li class=\"listboxitem\">");
        
        if(group.allowSingleSelection)
        {
          w.write("<img class=\"grouped_listbox_checkbox\" style=\"vertical-align:middle\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("checked.png"));
          w.write("\" checked=\"true\"/>");
        }
        if(provideOnClick && isEnabled())
        {
          w.write("<span class=\"grouped_listbox_onclick\">");
          w.write(label);
          w.write("</span>");
          
        }
        else
        {
          w.write(label);
        }
        w.write("</li>\n");
      }
      else
      {
        w.write("<li class=\"listboxitem\">");
        if(group.allowSingleSelection)
        {
          w.write("<img class=\"grouped_listbox_checkbox\" style=\"vertical-align:middle\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("unchecked.png"));
          w.write("\" checked=\"false\"/>");
        }
        if(provideOnClick && isEnabled())
        {
          w.write("<span class=\"grouped_listbox_onclick\">");
          w.write(label);
          w.write("</span>");
          
        }
        else
        {
          w.write(label);
        }
        w.write("</li>\n");
      }
    }
    w.write("</ul>\n");
  }
  
  
  @Override
  public void clear(IClientContext context)
  {
    groups  = new ArrayList<Group>();
    scrollLeft = 0;
    scrollTop = 0;
  }


  @Override
  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    GroupedListboxEventHandler handler=(GroupedListboxEventHandler)this.wrapper.getEventHandler(context);
    if(handler!=null)
      handler.onGroupStatusChanged(context,newGroupDataStatus,(IGroupedListbox)this);
  }

  @Override
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId())
    {
      GroupedListboxEventHandler handler=(GroupedListboxEventHandler)this.wrapper.getEventHandler(context);
      if(handler!=null)
      {
      }
      if("click".equals(event))
      {
        String[] index = value.split("[:]");
        int groupIndex=Integer.parseInt(index[0]);
        int entryIndex=Integer.parseInt(index[1]);
        Group group = groups.get(groupIndex);
        Entry entry = group.entries.get(entryIndex);
        if(handler!=null)
        {
          handler.onClick(context,this,entry.data);
        }
      }
      if("check".equals(event))
      {
        String[] index = value.split("[:]");
        int groupIndex=Integer.parseInt(index[0]);
        int entryIndex=Integer.parseInt(index[1]);
        Group group = groups.get(groupIndex);
        if(entryIndex==-1)
        {
          selectGroup(group,true);
        }
        else
        {
          Entry entry = group.entries.get(entryIndex);
          entry.selected=true;
        }
      }
      else if("uncheck".equals(event))
      {
        String[] index = value.split("[:]");
        int groupIndex=Integer.parseInt(index[0]);
        int entryIndex=Integer.parseInt(index[1]);
        Group group = groups.get(groupIndex);
        if(entryIndex==-1)
        {
          selectGroup(group,false);
        }
        else
        {
          Entry entry = group.entries.get(entryIndex);
          entry.selected=false;
        }
      }
      else if("selectAll".equals(event))
      {
        Boolean flag=Boolean.valueOf(value);
        selectAll(flag.booleanValue());
      }
      else if("scroll".equals(event))
      {
        String[] index = value.split("[:]");
        scrollLeft=Integer.parseInt(index[0]);
        scrollTop=Integer.parseInt(index[1]);
        this.resetCache();
      }
      else if("collapse".equals(event))
      {
        int groupIndex=Integer.parseInt(value);
        groups.get(groupIndex).expanded=false;
        this.resetCache();
      }
      else if("expand".equals(event))
      {
        int groupIndex=Integer.parseInt(value);
        groups.get(groupIndex).expanded=true;
        this.resetCache();
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

  public void writeHTMLPart(ClientContext context, Writer w, String partId)
  {
    try
    {
      int groupIndex=Integer.parseInt(partId);
      calculateEntryHTML(context, w,groups.get(groupIndex), hasOnClickOverriden(context));
    }
    catch(Exception exc)
    {
      ExceptionHandler.handle(exc);
    }
  }

  
  @Override
  public boolean isEditable()
  {
    return editable;
  }

  @Override
  public void setEditable(boolean editable)
  {
    this.editable = editable;
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

  public static boolean hasOverriden(Class cls, Method m)
  {
    if (cls == null)
    {
      throw new IllegalArgumentException("The class must not be null");
    }
    return cls == m.getDeclaringClass();
  }

  private boolean hasOnClickOverriden(IClientContext context) throws Exception
  {
    boolean provideOnClick = false;
    GroupedListboxEventHandler handler=(GroupedListboxEventHandler)this.wrapper.getEventHandler(context);
    if(handler!=null)
    {
      Method m = handler.getClass().getMethod("onClick", new Class[]{IClientContext.class, IGroupedListbox.class, Object.class});
      provideOnClick = hasOverriden(handler.getClass(),m);
    }
    return provideOnClick;
  }

}
