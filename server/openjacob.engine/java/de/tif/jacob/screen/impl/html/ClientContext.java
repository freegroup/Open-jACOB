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
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.i18n.Message;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILinkRenderStrategy;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.dialogs.IExcelDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IMessageDialogCallback;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.IRecordTreeDialog;
import de.tif.jacob.screen.dialogs.IRecordTreeDialogCallback;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.dialogs.IWorkflowEditorCallback;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.html.dialogs.AskDialog;
import de.tif.jacob.screen.impl.html.dialogs.DocumentDialog;
import de.tif.jacob.screen.impl.html.dialogs.ExcelDialog;
import de.tif.jacob.screen.impl.html.dialogs.FormDialog;
import de.tif.jacob.screen.impl.html.dialogs.GridTableDialog;
import de.tif.jacob.screen.impl.html.dialogs.MessageDialog;
import de.tif.jacob.screen.impl.html.dialogs.OkCancelDialog;
import de.tif.jacob.screen.impl.html.dialogs.RecordTreeDialog;
import de.tif.jacob.screen.impl.html.dialogs.TransparentMessageDialog;
import de.tif.jacob.screen.impl.html.dialogs.UploadDialog;
import de.tif.jacob.screen.impl.html.dialogs.UrlDialog;
import de.tif.jacob.screen.impl.html.dialogs.WorkflowEditorDialog;
import de.tif.jacob.security.IUser;
import de.tif.jacob.util.FastStringWriter;

/**
 * The context of the current client guiBrowser.
 * 
 * @author Andreas Herz
 *
 */
public class ClientContext extends HTTPClientContext
{
  transient static public final String RCS_ID = "$Id: ClientContext.java,v 1.21 2010/11/18 11:26:04 freegroup Exp $";
  transient static public final String RCS_REV = "$Revision: 1.21 $";

  transient public String clientBrowser = null;
	transient protected Writer out = null;

  transient protected List         additionalInclude         = new ArrayList();
  transient protected StringBuffer additionalHtml            = new StringBuffer(1024*2);
  transient protected String       foreignFieldHtml          = "";
  transient protected StringBuffer comboboxAdditionalHtml    = new StringBuffer(1024*2);
  transient protected StringBuffer contextmenuAdditionalHtml = new StringBuffer(1024*2);
  transient private StringBuffer onLoadJavascript = null;
  
  public ClientContext(IUser user, HttpServletResponse response, IApplication app, String browserId) throws IOException
  {
    this(user,app, browserId);
    this.out = new PrintWriter(response.getOutputStream());
  }

  public ClientContext(IUser user, Writer w, IApplication app, String browserId) throws IOException
  {
    this(user,app, browserId);
    this.out = new PrintWriter(w);
  }
  
  public ClientContext(IUser user, IApplication app, String browserId) throws IOException
  {
    super(app,user);
    this.clientBrowser = browserId;
    if(getApplication()!=null)
    {
      setDomain((IDomain)((Application)getApplication()).activeDomain);
      if(getDomain()!=null)
      {  
        this.form=getDomain().getCurrentForm(this);
      }
      // Die Gruppe dessen Browser derzeit angezeit wird, wird per default als
      // die 'aktive' gruppe eingetragen.
      if(getForm()!=null && getForm().getCurrentBrowser()!=null)
        setGroup((Group)((SearchBrowser)getForm().getCurrentBrowser()).getRelatedGroup());
    }
  }
  
  public void addOnLoadJavascript(String code)
  {
    if (onLoadJavascript == null)
    {
      onLoadJavascript = new StringBuffer(3*code.length());
    }
    else
    {
      onLoadJavascript.append("\n\t\t\t");
    }
    onLoadJavascript.append(code);
  }

  public String getOnLoadJavascript()
  {
    return onLoadJavascript == null ? "" : onLoadJavascript.toString();
  }
    
  /**
   * Add additional HTML to the request response. Only for internal use
   * This function is used by some dialogs to add there additonal HTML and
   * JavaScript elements.
   * 
   * @param html
   */
  public void addAdditionalHTML(String html)
  {
    additionalHtml.append(html);
  }

  public void addComboboxAdditionalHTML(String string)
  {
  	comboboxAdditionalHtml.append(string);
  }
  
  public String getComboboxAdditionalHTML()
  {
  	return comboboxAdditionalHtml.toString();
  }
  
  public void addContextmenuAdditionalHTML(String string)
  {
  	contextmenuAdditionalHtml.append(string);
  }
  
  public String getContextmenuAdditionalHTML()
  {
  	return contextmenuAdditionalHtml.toString();
  }
 
  /**
   * Add the inline foreignField HTML to the request response. Only for internal use
   * 
   * @param html
   */
  public void setForeignFieldHTML(String html)
  {
    foreignFieldHtml=html;
  }

  /**
   * Add the inline foreignField HTML to the request response. Only for internal use
   * 
   * @param html
   */
  public String getForeignFieldHTML()
  {
    return foreignFieldHtml;
  }

  /** 
   * Returns the additional Html 
   * @return
   */
  public String getAdditionalHtml()
  {
   return additionalHtml.toString();
  }

  /** 
   * Returns the additional Include files. 
   * @return
   * @since 2.8.5
   */
  public String getAdditionalIncludes()
  {
    ((Application)this.getApplication()).calculateIncludes(this);
    
    FastStringWriter w = new FastStringWriter();
    Iterator iter = this.additionalInclude.iterator();
    while(iter.hasNext())
    {
      w.write((String)iter.next());
    }
    return w.toString();
  }


  /**
   * @since 2.8.5
   * @param pluginId
   * @param file
   */
  public void addAdditionalIncludes(String pluginId, String file)
  {
    String path;
    if(pluginId==null)
      path = "./application/"+getApplication().getName()+"/"+getApplication().getApplicationDefinition().getVersion().toShortString()+"/"+file;
    else
      path = "./application/"+getApplication().getName()+"/"+getApplication().getApplicationDefinition().getVersion().toShortString()+"/"+pluginId+"/"+file;
    
    String inc="";
    if(file.endsWith(".js"))
       inc = "<script type='text/javascript' src='"+path+"'></script>\n";
    else if (file.endsWith(".css"))
       inc = "<link type=\"text/css\" rel=\"stylesheet\" href=\""+path+"\"/>\n";
    else
      throw new RuntimeException("Unsupported file type ["+file+"]. Only .js and .css are allowed");
    
    // Kein Set/HashSet verwenden. Reihenfolge der Includes muﬂ beibehalten werden
    //
    if(!this.additionalInclude.contains(inc))
      this.additionalInclude.add(inc);
  }

  
  public final void setShowSQL(boolean flag)
  {
    ((Application)getApplication()).setShowSQL(flag);    
  }
  
  public final boolean getShowSQL()
  {
    return  ((Application)getApplication()).isShowSQL();
  }
  
  public IRecordTreeDialog createRecordTreeDialog(IGuiElement anchor, IDataTableRecord record, IRelationSet relationSet, Filldirection filldirection, IRecordTreeDialogCallback callback) throws Exception
  {
    return new RecordTreeDialog(this,anchor, record,relationSet,filldirection,callback);
  }
  
  public IOkCancelDialog createOkCancelDialog( String question,  IOkCancelDialogCallback callback)
  {
    return new OkCancelDialog(this, question, callback);
  }

  public IOkCancelDialog createOkCancelDialog( Message question,  IOkCancelDialogCallback callback)
  {
    return new OkCancelDialog(this, question.print(Context.getCurrent().getLocale()), callback);
  }

  /*
   * @see de.tif.jacob.screen.IGuiElement#createAskDialog(de.tif.jacob.screen.IClientContext, java.lang.String, java.lang.String, de.tif.jacob.screen.IGuiElement, de.tif.jacob.screen.IAskDialogCallback)
   * @author Andreas Herz
   */
  public IAskDialog createAskDialog(String question,boolean multiline,  IAskDialogCallback callback)
  {
    return new AskDialog(this, question,"", callback, multiline);
  }
  
  /*
   * @see de.tif.jacob.screen.IGuiElement#createAskDialog(de.tif.jacob.screen.IClientContext, java.lang.String, java.lang.String, de.tif.jacob.screen.IGuiElement, de.tif.jacob.screen.IAskDialogCallback)
   * @author Andreas Herz
   */
  public IAskDialog createAskDialog(String question,boolean multiline, String defaultValue, IAskDialogCallback callback)
  {
    return new AskDialog(this, question,defaultValue, callback, multiline);
  }
 
  public IUrlDialog createUrlDialog(String url)
  {
    return new UrlDialog(this,url);
  }
  
  public IUrlDialog createUrlDialog(IApplication app, String url)
  {
    return new UrlDialog(this,"application/"+app.getName()+"/"+app.getVersion()+"/"+url);
  }

  public IMessageDialog createMessageDialog(String message)
  {
    return new MessageDialog(this, message,null,null);
  }
  
  public IMessageDialog createMessageDialog(String message, String extendedMessage)
  {
    return new MessageDialog(this, message,extendedMessage,null);
  }
  
  
  public IMessageDialog createMessageDialog(String message, IMessageDialogCallback callback)
  {
    return new MessageDialog(this, message,null, callback);
  }
  
  public IMessageDialog createMessageDialog(String message, String extendedMessage, IMessageDialogCallback callback)
  {
    return new MessageDialog(this, message,extendedMessage, callback);
  }  
  
  /**
   * Transparent messages are the brainchild of Jef Raskin. It's simply a large and 
   * translucent message that's displayed over the contents of your screen. They fade 
   * away when the user takes any action (like typing or moving the mouse). In practice, 
   * the message is both noticeable yet unobtrusive. And because the message is transparent, 
   * you can see what's beneath it.<br>
   * 
   */
  public void showTransparentMessage(String message)
  {
    showTransparentMessage(message,true);
  }

  public void showTransparentMessage(String message, boolean fadeOut)
  {
    new TransparentMessageDialog(this, message, fadeOut).show();
  }


  public IExcelDialog createExcelDialog()
  {
    return new ExcelDialog(this);
  }
  
  
  /**
   * @since 2.7.4
   */
  public void showExcelDialog(IDataBrowser browser)
  {
    List browserFields = browser.getBrowserDefinition().getBrowserFields();
    IExcelDialog dialog = createExcelDialog();
    String[] header = new String [browserFields.size()];
    
    for (int i=0;i<browserFields.size();i++)
    {
      IBrowserField column = (IBrowserField)browserFields.get(i);
      header[i]=de.tif.jacob.i18n.I18N.localizeLabel(column.getLabel(),this);
    }
    dialog.setHeader(header);
    
    String[][] data = new String[browser.recordCount()][browserFields.size()];
    for(int row=0;row<browser.recordCount();row++)
    {
      IDataBrowserRecord record = browser.getRecord(row);
      for(int column=0;column<browserFields.size();column++)
      {
        data[row][column]= record.getSaveStringValue(column);
      }
    }
    dialog.setData(data);
    dialog.show();
  }

  public IFormDialog createFormDialog(String title, FormLayout layout,  IFormDialogCallback callback)
  {
    return new FormDialog(this,title,  layout, callback);
  }
  
  public IUploadDialog createUploadDialog(IUploadDialogCallback callback)
  {
    return new UploadDialog(this,callback);
  }
  
  public IUploadDialog createUploadDialog(String title, String description, IUploadDialogCallback callback)
  {
    return new UploadDialog(this,title, description,callback);
  }

  public IGridTableDialog createGridTableDialog(IGuiElement anchor)
  {
    return new GridTableDialog(this,((GuiHtmlElement)anchor).getEtrHashCode()); 
  }
  
  public IGridTableDialog createGridTableDialog(IGuiElement anchor, IGridTableDialogCallback callback)
  {
    return new GridTableDialog(this,((GuiHtmlElement)anchor).getEtrHashCode(), callback); 
  }
  
  public void showWorkflowEditor(String xmlWorkflowDefinition, IWorkflowEditorCallback callback)
  {
      new WorkflowEditorDialog(this, xmlWorkflowDefinition, callback).show(); 
  }
  
  
  /**
   * @since 2.8.0
   */
  public IGridTableDialog createGridTableDialog(IGuiElement anchor,IDataBrowser browser)
  {
    List browserFields = browser.getBrowserDefinition().getBrowserFields();
    IGridTableDialog dialog = createGridTableDialog(anchor);
    String[] header = new String [browserFields.size()];
    
    for (int i=0;i<browserFields.size();i++)
    {
      IBrowserField column = (IBrowserField)browserFields.get(i);
      header[i]=de.tif.jacob.i18n.I18N.localizeLabel(column.getLabel(),this);
    }
    dialog.setHeader(header);
    
    String[][] data = new String[browser.recordCount()][browserFields.size()];
    for(int row=0;row<browser.recordCount();row++)
    {
      IDataBrowserRecord record = browser.getRecord(row);
      for(int column=0;column<browserFields.size();column++)
      {
        data[row][column]= record.getSaveStringValue(column);
      }
    }
    dialog.setData(data);
    return dialog;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.IClientContext#createDocumentDialog(de.tif.jacob.core.data.DataDocumentValue)
   */
  public IDocumentDialog createDocumentDialog(DataDocumentValue document)
  {
    return createDocumentDialog(null, document);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.screen.IClientContext#createDocumentDialog(java.lang.String, de.tif.jacob.core.data.DataDocumentValue)
   */
  public IDocumentDialog createDocumentDialog(String mimeType, DataDocumentValue document)
  {
    return createDocumentDialog( mimeType, document.getName(), document.getContent());
  }
  
  public IDocumentDialog createDocumentDialog(String mimeType, String fileName, byte[] content)
  {
    return new DocumentDialog(this, mimeType, fileName, content);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.IClientContext#createMessageDialog(java.lang.String, de.tif.jacob.core.Message)
	 */
	public IMessageDialog createMessageDialog(Message message)
	{
		return createMessageDialog(message.print(getLocale()));
	}

  public void showTipDialog(IGuiElement anchor, String message)
  {
    long id = System.currentTimeMillis();
    this.addAdditionalHTML("<div class=\"tipDialog\" id=\""+id+"\">"+message+"</div>");
    this.addOnLoadJavascript("adjustTipDialog(\""+id+"\",\""+((GuiHtmlElement)anchor).getEtrHashCode()+"\");");
  }

  public void showTipDialog(String anchorId, String message)
  {
    long id = System.currentTimeMillis();
    this.addAdditionalHTML("<div class=\"tipDialog\" id=\""+id+"\">"+message+"</div>");
    this.addOnLoadJavascript("adjustTipDialog(\""+id+"\",\""+anchorId+"\");");
  }

  public ILinkRenderStrategy createDefaultLinkRenderStrategy()
  {
    return new HtmlLinkRenderStrategy();
  }
  
}
