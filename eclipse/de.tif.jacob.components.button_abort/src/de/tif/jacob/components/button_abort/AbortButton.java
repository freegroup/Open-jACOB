/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.button_abort;

import java.io.Writer;
import java.util.Properties;
import java.util.Vector;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.impl.html.Browser;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.Group;
import de.tif.jacob.screen.impl.html.PluginComponentImpl;
import de.tif.jacob.util.StringUtil;

public class AbortButton extends PluginComponentImpl implements IActionEmitter, IButton
{
  boolean emphazise = false;
  Icon icon = null;
  String link = null;
  
  @Override
  public void addDataFields(Vector fields)
  {
    super.addDataFields(fields);
  }

  
  public void setEmphasize(boolean flag)
  {
    emphazise = flag;
  }

  public void setIcon(Icon icon)
  {
  this.icon = icon;
  }

  public void setLink(String url)
  {
    this.link = url;
  }

  public boolean isSafeSearch()
  {
    return false;
  }


  public void setSearchMode(boolean safeSearch)
  {
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
    int width = this.wrapper.getBoundingRect().width;
    int height = this.wrapper.getBoundingRect().height;

    w.write("\t<button  style=\"cursor:pointer;position:absolute; width: " + width + "px; height: " + height + "px;\"");

    // Falls der Button 'nur' ein Icon hat aber keinen Text, dann
    // wird versucht das Icon zentrisch im Button darzustellen
    // Da stören eventuell Ränder welche im Style definiert wurden (margin/padding)
    // Diese werden jetzt zurückgesetzt
    //
    if(icon != Icon.NONE && getLabel().length()==0)
    {
      w.write(";margin:0px;padding:0px");
    }
    
    /*
    w.write(";text-align:");
    w.write(this.definition.getHorizontalAlign());
    */
    w.write("\"");
    /*
    if(definition.getTabIndex()>=0)
    {
      w.write(" TABINDEX=\"");
      w.write(Integer.toString(definition.getTabIndex()));
      w.write("\" ");
    }
    */
    w.write(" name=\"");
    w.write(this.wrapper.getEtrHashCode());
    w.write("\" id=\"");
    w.write(this.wrapper.getEtrHashCode());
    w.write("\" ");
    // the access key HTMLfragment must have a space as first character
 //   w.write(accesskey);
    String classAttribute= "";
    if(isEnabled() && !context.isInReportMode() &&!((Browser)getOuterGroup().getBrowser()).isInMultipleUpdate(context))
    {
      w.write(" onClick=\"FireEvent('");
      w.write(Integer.toString(getId()));
      w.write("', 'click')\" ");
      if(this.emphazise)
        classAttribute = " class=\"button_emphasize_normal";
      else
        classAttribute = " class=\"button_normal";
    }
    else
    {
      w.write(" onClick=\"return false;\" ");
      if(this.emphazise)
        classAttribute = " class=\"button_emphasize_disabled";
      else
        classAttribute = " class=\"button_disabled";
    }
    
    /*
    if(isDefaultKey(context))
    {
      w.write(" isDefault=\"true\" ");
      classAttribute=classAttribute + " button_default\" ";
    }
    else
    */
    {
      classAttribute = classAttribute+"\" ";
    }
    
    w.write(classAttribute);
    w.write(">");
    if(icon!=Icon.NONE)
    {
      w.write("<img class=\"buttonicon\" src=\"");
      w.write(icon.getPath(isEnabled()));
      w.write("\"/>");
    }
    w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
    w.write("</button>\n");
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
    setLabel(I18N.getCoreLocalized("BUTTON_COMMON_CANCEL",context));
    if(newGroupDataStatus == IGroup.NEW || newGroupDataStatus == IGroup.UPDATE)
      setEnable(true);
    else
      setEnable(false);
    
    IActionButtonEventHandler handler=(IActionButtonEventHandler)this.wrapper.getEventHandler(context);
    if(handler!=null)
      handler.onGroupStatusChanged(context,newGroupDataStatus,(IActionEmitter)this);
  }


  @Override
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if (guid == this.getId())
    {
      if (event.equals("click"))
      {
        IActionButtonEventHandler handler = (IActionButtonEventHandler) this.wrapper.getEventHandler(context);
        if (handler != null)
        {
          boolean result = ((IActionButtonEventHandler) handler).beforeAction(context, this);
          if (result == false)
            return true;
        }
        // Abort Implementation
        //
        IDataTableRecord selectedRecord = context.getSelectedRecord();
        if (selectedRecord != null)
        {
          if (selectedRecord.isUpdated())
          {
            selectedRecord.getCurrentTransaction().close();
            selectedRecord.getAccessor().propagateRecord(selectedRecord, Filldirection.BACKWARD);
           ((Group) context.getGroup()).setDataStatus(context,IGroup.SELECTED);
          }
          else if(selectedRecord.isNew())
          {
            context.getForm().clear(context);
            ((Group) context.getGroup()).setDataStatus(context,IGroup.SEARCH);
          }
        }
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
 //   percentage = ((Integer)properties.get("percentage")).intValue();
  }
}
