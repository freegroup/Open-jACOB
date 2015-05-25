/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.screen.impl.html;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.definition.IExternalFormDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorExternalFormTargetType;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.HTTPExternalForm;
import de.tif.jacob.screen.impl.HTTPForm;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ExternalForm extends GuiHtmlElement implements HTTPExternalForm
{
  static public final transient String RCS_ID = "$Id: ExternalForm.java,v 1.14 2010/11/18 11:26:04 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.14 $";

  private final IExternalFormDefinition definition;
  private IFormGroup formGroup;
  private boolean oldToolbarFlag=false;
  private IDataAccessor dataAccessor = null;
  private String cachedURL;
  
  public static class ExternalNavigationEntry extends INavigationEntry
  {
    String url;
    String target;
    public ExternalNavigationEntry(ExternalForm form, String label, String url, String target)
    {
      super(form,null,label,null,null);
      this.url = url;
      this.target=target;
      this.emitter = form.getId();
    }

    public String getOnClickAction()
    {
      if(CastorExternalFormTargetType.CURRENT_WINDOW.toString().equals(target))
        return "parent.location.href=\""+this.url+"\"";
      if(CastorExternalFormTargetType.NEW_WINDOW.toString().equals(target))
        return "window.open(\""+this.url+"\",\"_blank\")";
      if(CastorExternalFormTargetType.CONTENT_AREA.toString().equals(target))
        return "FireEventData(\""+Integer.toString(hashCode())+"\",\"show\",\"unused\")";
      
      return "window.open(\""+this.url+"\",\"_blank\")";
    }
  }

  

  public List getGuiRepresentations(String tableAlias, String fieldName)
  {
    return Collections.EMPTY_LIST;
  }


  /**
   * Initial constructor with the castor definition
   * 
   * @param parent
   * @param childs
   * @param name
   * @param name
   */
  protected ExternalForm(IApplication app, IExternalFormDefinition externalForm)
  {
    super(app, externalForm.getName(), externalForm.getLabel(), externalForm.isVisible(), null, externalForm.getProperties());

    definition = externalForm;
  }

  
  public void hideTabStrip(boolean flag)
  {
    // ignore
  }


  public INavigationEntry getNavigationEntry(IClientContext context)
  {
    return new ExternalNavigationEntry(this,this.getI18NLabel(context.getLocale()) , getURL((ClientContext)context),getTarget());
  }


  /**
   * External Forms never had any search browser
   */
  public boolean hasVisibleSearchBrowser()
  {
    return false;
  }


  public boolean isDirty(IClientContext context) throws Exception
  {
    return false;
  }


  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    // this event has 'this' as target
    //
    if(guid==this.getId())
    {
      // handle the 'show' event of an form
      //
      if(event.equals("show"))
      {
        HTTPForm oldForm = (HTTPForm)context.getDomain().getCurrentForm(context);
        if(oldForm!=this)
        {
          context.getDomain().setCurrentForm(context,this);
        }
      }
      return true;
    }
    return false;
  }  

  public void calculateHTML(de.tif.jacob.screen.impl.html.ClientContext context) throws Exception
  {
    if(!isVisible())
      return;

    context.setForm(this);
    
    if(getCache()==null)
    {  
      Writer w = newCache();
      
      w.write("<iframe height=\"100%\" width=\"100%\" frameborder=\"0\" src=\""+getURL(context)+"\"></iframe>\n");
      w.write("<script type=\"text/javascript\">\nfunction markScrollPosForm(divElement){};</script>\n");
      w.write("<input type=\"hidden\" id=\"formScrollPos\" name=\"formScrollPos\" value=\"0\">\n");
    }
    super.calculateHTML(context);
  }
    
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    writeCache(w);
    // Das formdiv hat jetzt keinen Scrollbar. Dies wird dann von dem IFrame gestellt.
    //
    context.addOnLoadJavascript("getObj('formDiv').style.overflow='hidden';\n");
  }
  
  protected void addDataFields(Vector fields)
  {
    // do nothing
  }

  public String getEventHandlerReference()
  {
    return null;
  }
  
  public String getURL(ClientContext context)
  {
    if(cachedURL==null)
    {
      cachedURL =definition.getURL();
      cachedURL = StringUtil.replace(cachedURL,"{APPLICATION_WEBPATH}","./application/"+context.getApplication().getName()+"/"+context.getApplication().getApplicationDefinition().getVersion().toShortString());
      if(!cachedURL.startsWith("http"))
      {  
        if(cachedURL.indexOf("?")==-1)
          cachedURL= cachedURL+"?browser="+context.clientBrowser;
        else
          cachedURL= cachedURL+"&browser="+context.clientBrowser;
      }
    }
    return cachedURL;
  }
  
  public String getTarget()
  {
    return definition.getTarget();
  }

  public void setFormGroup(IFormGroup group)
  {
    this.formGroup = group;
    
  }
  
  public IFormGroup getFormGroup()
  {
    return this.formGroup;
  }


  public IBrowser getCurrentBrowser()
  {
    return null;
  }

  public String getInputFieldValue(String fieldName) throws Exception
  {
    return null;
  }

  public void resetTabOrder()
  {
    throw new RuntimeException("not supported operation");
  }

  public void setCurrentBrowser(IBrowser browser)
  {
    throw new RuntimeException("not supported operation");
  }

  public void setInputFieldValue(String fieldName, String value) throws Exception
  {
    throw new RuntimeException("not supported operation");
  }


  public void setFocus(IGuiElement element)
  {
    throw new RuntimeException("not supported operation");
  }


  public void setFocus(IInFormLongText element, int caretPosition)
  {
    throw new RuntimeException("not supported operation");
  }


  public void setFocus(IText element, int caretPosition)
  {
    throw new RuntimeException("not supported operation");
  }


  public void resetCache()
  {
    cachedURL = null;
    super.resetCache();
  }


  /**
   * Returns the data domain (with cache...) of the current user guiBrowser window.
   * FOR INTERNAL USE ONLY
   * @return
   */
  public IDataAccessor getDataAccessor()
  {
    if(dataAccessor==null)
      dataAccessor = new DataAccessor(getApplicationDefinition());
    return dataAccessor;
  }


  public DataField[] getDataFields()
  {
    return null;
  }


  public void onHide(IClientContext context) throws Exception
  {
    context.getApplication().setToolbarVisible(this.oldToolbarFlag);
  }


  public void onShow(IClientContext context) throws Exception
  {
    this.oldToolbarFlag= context.getApplication().isToolbarVisible();
    context.getApplication().setToolbarVisible(false);
  }

}
