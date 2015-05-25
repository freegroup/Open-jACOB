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

package de.tif.jacob.screen.impl.html.dialogs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NumberUtils;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.scheduler.TaskContextUser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormActionEmitter;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.impl.IDProvider;
import de.tif.jacob.screen.impl.dialogs.HTTPFormDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Herz
 *
 */
public class FormDialog  extends HTTPFormDialog
{
	class RemovePropertyHandler implements IFormActionEmitter
	{
		final String propertyName;
    final int id = IDProvider.next();
    
		RemovePropertyHandler(String name)
		{
			this.propertyName = name;
		}
		public void onAction(IClientContext context, IFormDialog parent, Map formValues) throws Exception 
		{
			formValues.put(propertyName,null);
			((FormDialog)parent).valueMap.put(propertyName,null);
		}
	}
	
	abstract class FormDialogElement
	{
		public CellConstraints c;
		public String          name;
		FormDialogElement(String name, CellConstraints c)
		{
			this.name = name;
			this.c    = c;
		}
		public abstract String getContent(ClientContext context, Map props);
	}
	class CheckBox extends FormDialogElement
	{
		CheckBox(String name, boolean checked, CellConstraints c)
		{
			super(name, c);
		}
		public String getContent(ClientContext context, Map props) 
		{
	    StringBuffer sb=new StringBuffer(50);
	    sb.append("<input id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\"  formLayout=\"true\" style=\"position:absolute;\" type=\"checkbox\" name=\"");
	    sb.append(name);
	    sb.append("\" ");
	    if(props.get(name)!=null)
	      sb.append(" checked " );
	    sb.append(" />");
	    return sb.toString();
		}
		
	}
	class Label    extends FormDialogElement
	{
		final String label;
		public Label(String label, CellConstraints c) 
		{
			super(null, c);
			this.label = StringUtil.htmlEncode(label);
		}

		public String getContent(ClientContext context, Map props) {
	    StringBuffer sb=new StringBuffer(250);
	    sb.append("<span id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\"  formLayout=\"true\" style=\"position:absolute;\" class=\"caption_normal_search\" style=\"white-space:nowrap;\">");
	    sb.append(label);
	    sb.append("</span>");
			return sb.toString();
		}
		
	}
	class Header   extends FormDialogElement
	{
		final String label;

		public Header(String label, CellConstraints c) 
		{
			super(null, c);
			this.label = StringUtil.htmlEncode(label);
		}

		public String getContent(ClientContext context, Map props) 
		{
	    StringBuffer sb=new StringBuffer(250);
	    sb.append("<table id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\" formLayout=\"true\" style=\"position:absolute;\" ><tr><td width=\"1\"><span class=\"caption_normal\" style=\"white-space:nowrap;\">");
	    sb.append(label);
	    sb.append("</span></td><td><hr/></td></tr></table>");
	    return sb.toString();
		}
		
	}
	class TextField extends FormDialogElement
	{
		final boolean readonly;
		public TextField(String name, boolean readonly,CellConstraints c) 
		{
			super(name, c);
			this.readonly = readonly;
		}

		public String getContent(ClientContext context, Map props) 
		{
			String value = (String)props.get(name);
	    StringBuffer sb=new StringBuffer(250);
	    sb.append("<input x_pos=\"").append(c.getColumn());
	    sb.append("\" y_pos=\"").append(c.getRow());
	    sb.append("\" x_span=\"").append(c.getWidth());
	    sb.append("\" y_span=\"").append(c.getHeight());
	    sb.append("\" formLayout=\"true\" style=\"position:absolute;\" type=\"text\" class=\"text_normal");
      if (!this.readonly)
        sb.append(" editable_inputfield");
	    sb.append("\" name=\"");
	    sb.append(name);
	    sb.append("\"");
	    if(readonly)
	      sb.append(" readonly ");
	    sb.append(" id=\"");
	    sb.append(name);
	    sb.append("\" value=\"");
	    if(value!=null)
	    	sb.append(StringUtil.htmlEncode(value));
	    sb.append("\" >");
	    if(writeSetFocus)
	    {
	      sb.append("<script type=\"text/javascript\">getObj('");
	      sb.append(name);
	      sb.append("').focus();</script>");
	      writeSetFocus=false;
	    }    
	    return sb.toString();
		}
	}
	class PasswordField extends FormDialogElement
	{
		public PasswordField(String name, CellConstraints c) 
		{
			super(name, c);
		}

		public String getContent(ClientContext context, Map props) 
		{
			String value = (String)props.get(name);
	    StringBuffer sb=new StringBuffer(250);
	    sb.append("<input x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\"  formLayout=\"true\" style=\"position:absolute;\" type=password class=\"text_normal\" name=\"");
	    sb.append(name);
	    sb.append("\" id=\"");
	    sb.append(name);
	    sb.append("\" value=\"");
	    if(value!=null)
	    	sb.append(value);
	    sb.append("\" >");
	    if(writeSetFocus)
	    {
	      sb.append("<script type=\"text/javascript\">getObj('");
	      sb.append(name);
	      sb.append("').focus();</script>");
	      writeSetFocus=false;
	    }      
	    return sb.toString();
		}
	}
	class FileUploadField extends FormDialogElement
	{
		final RemovePropertyHandler callback;
		public FileUploadField(String name, CellConstraints c, RemovePropertyHandler handler) 
		{
			super(name, c);
			callback = handler;
		}

		public String getContent(ClientContext context, Map props)
		{
	    StringBuffer sb=new StringBuffer(250);
	    System.out.println(name+":"+props.get(name));
	    if(props.get(name)!=null)
	    {
	    	DataDocumentValue file = (DataDocumentValue)props.get(name);
		    sb.append("<table  cellspacing=\"0\" cellpadding=\"0\"  id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\"  formLayout=\"true\" style=\"position:absolute;\">");
		    sb.append("<tr><td>");
		    sb.append("<input style=\"width:100%;height:100%\" readonly type=\"text\" class=\"text_normal\" value=\"");
		    sb.append(StringUtil.htmlEncode(file.getName()));
		    sb.append("\"/></td><td style=\"cursor:pointer;\" onclick=\"getObj('actionEmitter').value='");
	      sb.append(Integer.toString(callback.hashCode()));
	      sb.append("';getObj('reportForm').submit()\"><img src=\"../");
        sb.append(((ClientSession)context.getSession()).getTheme().getImageURL("close.png"));
        sb.append("\" border=\"0\"></td></tr></table>");
	    }
	    else
	    {
		    sb.append("<input x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\"  formLayout=\"true\" style=\"position:absolute;\" type=\"FILE\" class=\"text_normal\" name=\"");
		    sb.append(name);
		    sb.append("\"");
		    sb.append(" id=\"");
		    sb.append(name);
		    sb.append("\" >");
	    }
	    return sb.toString();
		}
	}
	
	class TextArea extends FormDialogElement
	{
		final boolean readonly;
		final boolean autowordwrap;
		public TextArea(String name,boolean readonly,boolean autowordwrap, CellConstraints c) 
		{
			super(name, c);
			this.readonly = readonly;
			this.autowordwrap = autowordwrap;
		}

		public String getContent(ClientContext context, Map props) 
		{
			String value = (String)props.get(name);
	    StringBuffer sb=new StringBuffer(250);
	    sb.append("<textarea x_pos=\"").append(c.getColumn());
	    sb.append("\" y_pos=\"").append(c.getRow());
	    sb.append("\" x_span=\"").append(c.getWidth());
	    sb.append("\" y_span=\"" + c.getHeight());
      sb.append("\" formLayout=\"true\" style=\"position:absolute;\" class=\"longtextdialog_normal");
      if (!this.readonly)
        sb.append(" editable_inputfield");
      sb.append("\" name=\"");
	    sb.append(name);
	    sb.append("\" ");
	    if(readonly)
	      sb.append("readonly ");
	    if(!autowordwrap)
	      sb.append("wrap=\"off\" ");
	    sb.append("id=\"");
	    sb.append(name);
	    sb.append("\" >");
	    if(value!=null)
	      sb.append(StringUtil.htmlEncode(value));
	    sb.append("</textarea>");
	    if(writeSetFocus)
	    {
	      sb.append("<script type=\"text/javascript\">getObj('");
	      sb.append(name);
	      sb.append("').focus();</script>");
	      writeSetFocus=false;
	    }      
	    return sb.toString();
		}
	}

  class TextCodeArea extends FormDialogElement
  {
    final String language;
    final String value;
    
    public TextCodeArea(String name,String value, String language, CellConstraints c) 
    {
      super(name, c);
      this.language = language;
      this.value    = value;
    }

    public String getContent(ClientContext context, Map props) 
    {
      StringBuffer sb=new StringBuffer(250);
      sb.append("<div x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\" formLayout=\"true\" style=\"position:absolute;overflow:auto;\" class=\"longtextdialog_normal\" name=\"");
      sb.append(name);
      sb.append("\" id=\"");
      sb.append(name);
      sb.append("\" ><pre><code class='"+language+"'>");
      sb.append(StringUtil.htmlEncode(this.value));
      sb.append("</code></pre></div>");
      return sb.toString();
    }
  }

  class ComboBox extends FormDialogElement
	{
		final String[] values;
		public ComboBox(String name, String[] values, CellConstraints c) 
		{
			super(name, c);
			this.values = values;
		}

		public String getContent(ClientContext context, Map props) 
		{
//			String value = (String)props.get(name);
			int selectedIndex = NumberUtils.stringToInt((String)props.get(name+"Index"),-1);
	    StringBuffer sb=new StringBuffer(250);
	    sb.append("\t\t<select id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\" formLayout=\"true\" style=\"position:absolute;\" onChange=\"getObj('"+name+"Index').value=this.selectedIndex;\" name=\"");
	    sb.append(name);
	    sb.append("\">\n");
	    for(int i=0;i<values.length;i++)
	    {
	      if(i==selectedIndex)
	        sb.append("\t\t<option selected>");
	      else 
	        sb.append("\t\t<option>");
	      sb.append(StringUtil.htmlEncode(values[i]));
	      sb.append("</option>\n");
	    }
	    sb.append("\t</select>\n");
	    sb.append("<input type=hidden name='"+name+"Index' id='"+name+"Index' value='"+selectedIndex+"'>");
	    return sb.toString();
		}
	}
	class ComboBoxWithHook extends FormDialogElement
	{
		final String[] values;
		final IFormActionEmitter buttonCallback; 
		public ComboBoxWithHook(String name, IFormActionEmitter buttonCallback, String[] values, CellConstraints c) 
		{
			super(name, c);
			this.values = values;
			this.buttonCallback = buttonCallback;
		}

		public String getContent(ClientContext context, Map props) 
		{
//			String value = (String)props.get(name);
			int selectedIndex = NumberUtils.stringToInt((String)props.get(name+"Index"),-1);
	    StringBuffer sb=new StringBuffer(250);
	    sb.append("\t\t<select id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\"  formLayout=\"true\" style=\"position:absolute;\" name=\"");
	    sb.append(name);
	    sb.append("\" onChange=\"getObj('"+name+"Index').value=this.selectedIndex;getObj('actionEmitter').value='");
	    sb.append(Integer.toString(buttonCallback.hashCode()));
	    sb.append("';getObj('reportForm').submit()\">\n");
	    for(int i=0;i<values.length;i++)
	    {
	      if(i==selectedIndex)
	        sb.append("\t\t<option selected>");
	      else 
	        sb.append("\t\t<option>");
	      sb.append(StringUtil.htmlEncode(values[i]));
	      sb.append("</option>\n");
	    }
	    sb.append("\t</select>\n");
	    sb.append("<input type=\"hidden\" name=\""+name+"Index\" id=\""+name+"Index\" value=\""+selectedIndex+"\">");
	    return sb.toString();
		}
	}
	class ListBox extends FormDialogElement
	{
		final String[] values;
		public ListBox(String name, String[] values, CellConstraints c) 
		{
			super(name, c);
			this.values = values;
		}

		public String getContent(ClientContext context, Map props) 
		{
//			String value = (String)props.get(name);
			int selectedIndex = NumberUtils.stringToInt((String)props.get(name+"Index"),-1);
	    StringBuffer sb=new StringBuffer(250);
	    String selectedItem="null";
	    sb.append("<div id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\" formLayout=\"true\" style=\"position:absolute;\" class=\"listbox_selected\" >\n"); 
	    for(int i=0;i<values.length;i++)
	    {
	      if(i==selectedIndex)
	      {
	        sb.append("\t\t<div id=\""+name+"_"+i+"\" onclick=\"if(lastObj_"+name+")lastObj_"+name+".className='listboxitem';this.className='listboxitem_selected';lastObj_"+name+"=this;getObj('"+name+"Index').value='"+i+"';getObj('"+name+"').value='"+values[i]+"';\" class=\"listboxitem_selected\">");
	        selectedItem="getObj(\""+name+"_"+i+"\")";
	      }
	      else
	      {
	        sb.append("\t\t<div onclick=\"if(lastObj_"+name+")lastObj_"+name+".className='listboxitem';this.className='listboxitem_selected';lastObj_"+name+"=this;getObj('"+name+"Index').value='"+i+"';getObj('"+name+"').value='"+values[i]+"';\" class=\"listboxitem\">");
	      }
	      sb.append(StringUtil.htmlEncode(values[i]));
	      sb.append("</div>\n");
	    }
	    sb.append("</div>\n");
	    if(values.length>0 && selectedIndex>=0)
	    	sb.append("<input type=\"hidden\" name=\""+name+"\" id=\""+name+"\" value=\""+values[selectedIndex]+"\">\n");
	    else
	    	sb.append("<input type=\"hidden\" name=\""+name+"\" id=\""+name+"\" value=\"\">\n");
	    sb.append("<input type=\"hidden\" name=\""+name+"Index\" id=\""+name+"Index\" value=\""+selectedIndex+"\">\n");
	    sb.append("<script>var lastObj_"+name+"="+selectedItem+";</script>");
	    return sb.toString();
		}
	}
	class FormButton extends FormDialogElement
	{
		final String label;
		final IFormActionEmitter buttonCallback; 
		public FormButton(IFormActionEmitter buttonCallback, String label, CellConstraints c) 
		{
			super(null, c);
			this.label = StringUtil.htmlEncode(label);
			this.buttonCallback = buttonCallback;
		}

		public String getContent(ClientContext context, Map props) 
		{
	    StringBuffer sb=new StringBuffer(200);
	    if(buttonCallback!=null)
	    {
		    sb.append("<button id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\"  formLayout=\"true\" style=\"white-space:nowrap;position:absolute;\" type=button onClick=\"getObj('actionEmitter').value='");
		    sb.append(Integer.toString(buttonCallback.hashCode()));
		    sb.append("';getObj('reportForm').submit()\" class=button_normal>");
		    sb.append(label);
		    sb.append("</button>");
		    actionEmitters.put(Integer.toString(buttonCallback.hashCode()),buttonCallback);
	    }
	    else // insert a 'dead' (just for decoration) button
	    {
		    sb.append("<button id=\""+sb.hashCode()+"\" x_pos=\""+c.getColumn()+"\" y_pos=\""+c.getRow()+"\" x_span=\""+c.getWidth()+"\" y_span=\""+c.getHeight()+"\" formLayout=\"true\" style=\"position:absolute;white-space:nowrap;\" type=button class=button_normal>");
		    sb.append(label);
		    sb.append("</button>");
	    }
	    return sb.toString();
		}
	}


  /**
   * The FormDialog buttons in the button bar at the footer of the dialog.
   *
   */
  public static class FormDialogButton
  {
    public final String label;
    public final String id;
    public final boolean emphasize;
    
    /**
     * @param id
     * @param label
     * @param emphasize
     */
    public FormDialogButton(String id, String label, boolean emphasize)
    {
      this.label = label;
      this.id = id;
      this.emphasize = emphasize;
    }
  }
  
  
  private static final String      TEMPLATE_JSP ="FormDialog.jsp";
  
  private String userCancelButtonLabel;
  private boolean userCancelButtonModified;
  
  private final String cancelButtonLabel;
  private final String closeButtonLabel;

  private List submitButtons  = new ArrayList();
  private Map  actionEmitters = new HashMap();
  private Map  autosuggestProviders = new HashMap();
  // die letzten Vorschläge welche dem Benutzer angezeigt worden sind
  // (wird benötigt falls der benutzer einen davon auswählt)
  private IAutosuggestProvider.AutosuggestItem[] lastProvidedAutosuggestItems; 
  private IAutosuggestProvider lastAutosuggestProvider;

  private String javascriptPopup = "";
  private final String title;
  private boolean debug = false;
  private boolean writeSetFocus = true;
	private boolean autoResize=true;
	
  private final String columns[];
  private final String rows[];

  private final List elements = new ArrayList();
  private Map valueMap = new HashMap();

  
  public FormDialog(ClientContext context, String title, FormLayout layout, IFormDialogCallback callback)
  {
   super(context, callback);
   
   this.cancelButtonLabel = I18N.getLocalized("BUTTON_COMMON_CANCEL", context);
   this.closeButtonLabel = I18N.getLocalized("BUTTON_COMMON_CLOSE", context);
   
    // determine the columns/rows the form table
    //
    this.columns=layout.getColumn().split(",");
    this.rows=layout.getRow().split(",");
    this.title= title;
  }
  
  public FormDialog(String title,  FormLayout layout, IFormDialogCallback callback)
  {
   super(callback);
   this.cancelButtonLabel = "Cancel";
   this.closeButtonLabel = "Close";
   this.title= title;
   
    // determine the columns/rows the form table
    //
    columns=layout.getColumn().split(",");
    rows=layout.getRow().split(",");
  }

  public void setDebug(boolean flag)
  {
    debug=flag;
  }
  
  public boolean getDebug()
  {
    return debug;
  }
  
  public void show()
  {
    show(250,100);
  }
  
  /**
   * Shows an HTML information dialog on the client. 
   * 
   * @param title    The title of the dialog
   * @param question The question of the dialog.
   */
  public void show(int width, int height)
  {
    Context context  = Context.getCurrent();
   
    // the dialog call comes from a GuiCallback (user interaction)
    //
    if(context instanceof ClientContext)
    {  
      ClientContext cc=(ClientContext)context;
	    javascriptPopup = "<script type=\"text/javascript\">new jACOBPopup('dialogs/"+TEMPLATE_JSP+"?browser="+cc.clientBrowser+"&guid="+getId()+"',"+width+","+height+");</script>\n";
	    ((ClientSession)((ClientContext)context).getApplication().getSession()).addDialog(this);
	    cc.addAdditionalHTML(javascriptPopup);
    }
    // the dialog call comes from a scheduled script
    //
    else if(context instanceof TaskContextUser)
    {  
      TaskContextUser tc =(TaskContextUser)context;
	    javascriptPopup = "new jACOBPopup('dialogs/"+TEMPLATE_JSP+"?browser='+browserId+'&guid="+getId()+"',"+width+","+height+");\n";
      ((ClientSession)(tc).getSession()).addDialog(this);
      ((ClientSession)(tc).getSession()).addAsynchronJavaScript(javascriptPopup);
    }
  }

  /**
   * Add a header text to the dialog. A header has a label and a right hand side line.<br>
   * <br>
   * Example:<br>
   * label -------------------------------
   * 
   */
  public void addHeader(String label, CellConstraints c)
  {
    addCellData(new Header(label,c));
  }


  /**
   * Add a generic Label/Caption to the dialog
   * 
   */
  public void addLabel(String label, CellConstraints c)
  {
    addCellData(new Label(label,c));
  }
  
 
  
  /**
   * Add a TextField to the dialog. The name of the TextField represents the returned parameter
   * if the user press [ok] in the dialog.
   * 
   * @param name       The name of the parameter which will be returnd if the user press [ok] in the dialog
   * @param constraint The layout constraint of the element.
   */
  public void addCheckBox(String name, boolean checked, CellConstraints c)
  {
    addCellData(new CheckBox(name,checked,c));
    if(checked)
    	valueMap.put(name,"true");
  }
  
  /**
   * Add a TextField to the dialog. The name of the TextField represents the returned parameter
   * if the user press [ok] in the dialog.
   * 
   * @param name       The name of the parameter which will be returnd if the user press [ok] in the dialog
   * @param constraint The layout constraint of the element.
   */
  public void addTextField(String name, String value, CellConstraints constraint)
  {
    addTextField(name,value,false,constraint);
  	valueMap.put(name,value);
  }
  
  public void addTextField(String name, String value, IAutosuggestProvider provider, CellConstraints c)
  {
    addCellData(new TextField(name,false,c));
    autosuggestProviders.put(name,provider);
  	valueMap.put(name,value);
  }
  
  public void addTextField(String name, String value, boolean readonly, CellConstraints c)
  {
    addCellData(new TextField(name,readonly,c));
    valueMap.put(name,value);
  }
  
  public void addPasswordField(String name, String value,  CellConstraints c)
  {
    addCellData(new PasswordField(name,c));
  	valueMap.put(name,value);
  }
  

  public void addFileUpload(String name, CellConstraints c)
  {
  	RemovePropertyHandler callback = new RemovePropertyHandler(name);
    actionEmitters.put(Integer.toString(callback.hashCode()),callback);
    addCellData(new FileUploadField(name,c, callback));
  	valueMap.put(name,null);
  }
  
  /**
   * Add a TextField to the dialog. The name of the TextField represents the returned parameter
   * if the user press [ok] in the dialog.
   * 
   * @param name       The name of the parameter which will be returnd if the user press [ok] in the dialog
   * @param constraint The layout constraint of the element.
   */
  public void addTextArea(String name, String value, CellConstraints c)
  {
    addTextArea(name,value,false, false,c);
  }
  
  public void addTextArea(String name, String value,boolean readonly, CellConstraints c)
  {
    addTextArea(name,value, readonly, false,c);
  }

  public void addTextArea(String name, String value,boolean readonly, boolean autowordwrap, CellConstraints c)
  {
    addCellData(new TextArea(name,readonly, autowordwrap,c));
  	valueMap.put(name,value);
  }
  
  public void addTextArea(String name, String value, String codeLanguage, CellConstraints constraint)
  {
    addCellData(new TextCodeArea(name,value, codeLanguage,constraint));
  }

  /**
   * Add a TextField to the dialog. The name of the TextField represents the returned parameter
   * if the user press [ok] in the dialog.
   * 
   * @param name       The name of the parameter which will be returnd if the user press [ok] in the dialog
   * @param constraint The layout constraint of the element.
   */
  public void addComboBox(String name, String[] values, int selectedIndex, CellConstraints c)
  {
    addCellData(new ComboBox(name,values,c));
    if(selectedIndex>=0)
    {
    	valueMap.put(name+"Index",""+selectedIndex);
    	valueMap.put(name,values[selectedIndex]);
    }
  }

  /**
   * Add a Form action button to the dialog. The button does apear <b>in</b> the form dialog and
   * not in the toolbar button bar.<br>
   * <br>
   * The does <b>not</b> close if the user press the button. This is usefull if you want make a server
   * request to update dialog data.<br>
   * 
   * @param callback The callback object if the user has clicked the button
   * @param buttonLabel The label of the button
   * @param constraint The layout constraint of the element.
   */
  public void addComboBox(IFormActionEmitter buttonCallback, String name, String[] values, int selectedIndex, CellConstraints c)
  {
    addCellData(new ComboBoxWithHook(name,buttonCallback,values,c));
    actionEmitters.put(Integer.toString(buttonCallback.hashCode()),buttonCallback);
    if(selectedIndex>=0)
    {
    	valueMap.put(name+"Index",""+selectedIndex);
    	valueMap.put(name,values[selectedIndex]);
    }
  }

  /**
   * Add a TextField to the dialog. The name of the TextField represents the returned parameter
   * if the user press [ok] in the dialog.
   * 
   * @param name       The name of the parameter which will be returnd if the user press [ok] in the dialog
   * @param constraint The layout constraint of the element.
   */
  public void addListBox(String name, String[] values, int selectedIndex, CellConstraints c)
  {
    addCellData(new ListBox(name,values,c));
    if(selectedIndex>=0 && values.length>0)
    {
    	valueMap.put(name+"Index",""+selectedIndex);
   		valueMap.put(name,values[selectedIndex]);
    }
  }

  /**
   * Add a Form action button to the dialog. The button does apear <b>in</b> the form dialog and
   * not in the toolbar button bar.<br>
   * <br>
   * The does <b>not</b> close if the user press the button. This is usefull if you want make a server
   * request to update dialog data.<br>
   * 
   * @param callback The callback object if the user has clicked the button
   * @param buttonLabel The label of the button
   * @param constraint The layout constraint of the element.
   */
  public void addFormButton(IFormActionEmitter buttonCallback, String buttonLabel, CellConstraints c)
  {
    addCellData(new FormButton(buttonCallback, buttonLabel,c));
  }
 
  /**
   * 
   * @param content
   * @param constraint
   */
  private void addCellData(FormDialogElement element)
  {
    elements.add(element);
  }
  
  /**
   * Daten welche eventuell vor dem letzten Submit zum Server gesendet worden sind
   *
   * @return
   */
  public Map getTempValues()
  {
  	return valueMap;
  }
  
  public String getHtml(Map parameters)
  {
    StringBuffer sb=new StringBuffer(1024*2);

    ClientContext context =(ClientContext) Context.getCurrent();
    // parameter im HTML ersetzten
    //neue Werte in die replacemap eintragen
    Map newReplaceMap = new HashMap(valueMap);
    Iterator iter = valueMap.keySet().iterator();
    while (iter.hasNext()) 
    {
			Object element = iter.next();
			if(parameters.get(element)!=null)
				newReplaceMap.put(element, parameters.get(element));
		}
    valueMap = newReplaceMap;

    iter = elements.iterator();
    while (iter.hasNext()) 
    {
    	FormDialogElement element = (FormDialogElement)iter.next();
			sb.append(element.getContent(context, valueMap));
			sb.append("\n");
		}
    iter = autosuggestProviders.keySet().iterator();
    while (iter.hasNext())
    {
      String obj = (String) iter.next();
      sb.append("<div name=\"autosuggest_");
      sb.append(obj);
      sb.append("\" id=\"autosuggest_");
      sb.append(obj);
      sb.append("\" class=\"suggestions\" style=\"visibility:hidden;\" ></div>");
    }
    
    sb.append("<script>\n");
    sb.append("var def_cols  = new Array();\n");
    sb.append("var def_rows  = new Array();\n");
    sb.append("var min_height= new Array();\n");
    sb.append("var min_width = new Array();\n");
    
    
    for(int i=0;i<columns.length;i++)
    {
    	sb.append("def_cols[");
    	sb.append(Integer.toString(i));
    	sb.append("]=");
    	sb.append(getWidthFromDef(columns[i]));
    	sb.append(";\n");
    }
    
    for(int i=0;i<rows.length;i++)
    {
    	sb.append("def_rows[");
    	sb.append(Integer.toString(i));
    	sb.append("]=");
    	sb.append(getWidthFromDef(rows[i]));
    	sb.append(";\n");
    }
    
    iter = autosuggestProviders.keySet().iterator();
    while (iter.hasNext())
    {
      String obj = (String) iter.next();
      sb.append("new AutoSuggestControl(getObj('"+obj+"'), new RemoteDialogSuggestions());\n");
      
    }
    sb.append("</script>\n");
    
    return sb.toString();
  }
  


  private String getWidthFromDef(String def)
  {
    Pattern pattern1 = Pattern.compile("(\\d+)(?:dlu)?");
    Matcher matcher = pattern1.matcher(def);
    if (matcher.find())
      return matcher.group(1);

    Pattern pattern2 = Pattern.compile("p(?:ref)?");
    matcher = pattern2.matcher(def);
    if (matcher.find())
      return "0";

    Pattern pattern3 = Pattern.compile("g(?:row)?");
    matcher = pattern3.matcher(def);
    if (matcher.find())
    {
      autoResize=false;
      return "-1";
    }

    return "0";
  }
  

  
  /**
   * @param userCancelButtonLabel The cancelButton to set.
   */
  public final void setCancelButton(String cancelButton)
  {
    this.userCancelButtonLabel = cancelButton;
    this.userCancelButtonModified = true;
  }

  /**
   * @param okButton The okButton to set.
   */
  public final void addSubmitButton(String buttonId, String buttonLabel)
  {
    submitButtons.add(new FormDialogButton(buttonId, buttonLabel, false));
  }

  public final void addSubmitButton(String buttonId, String buttonLabel, boolean emphasize)
  {
    submitButtons.add(new FormDialogButton(buttonId, buttonLabel, emphasize));
  }

  /**
   * @return Returns the cancelButton.
   */
  public final String getCancelButton()
  {
    if (this.userCancelButtonModified)
      return userCancelButtonLabel;

    if (this.submitButtons.size() > 0)
      return this.cancelButtonLabel;
    else
      return this.closeButtonLabel;
  }

  public final IFormActionEmitter getActionEmitter(String guid)
  {
    return (IFormActionEmitter)actionEmitters.get(guid);
  }

  public final IAutosuggestProvider getAutosuggestProvider(String guid)
  {
    return (IAutosuggestProvider)autosuggestProviders.get(guid);
  }

  /**
   * only to test this class
   * 
   * @param args
   */
  public static void main(String[] args) throws IOException
  {
    String header=IOUtils.toString(FormDialog.class.getResourceAsStream("FormDialog_header.txt"));
    String footer=IOUtils.toString(FormDialog.class.getResourceAsStream("FormDialog_footer.txt"));

    FormLayout layout = new FormLayout("5dlu,100dlu,50dlu,pref,5dlu,grow", // 3 columns
    "5dlu,20dlu,5dlu,20dlu,5dlu,20dlu,5dlu,500dlu,20dlu,20dlu"); // 5 rows
    		CellConstraints c=new CellConstraints();
    		

    	  FormDialog dialog=new FormDialog("title",layout,null);
        dialog.addLabel(" Address Book ", c.xy(1,1));
        dialog.addComboBox("addressbook",new String[]{"Personal Address book","Global Address Book"},0, c.xy(1,3));
        dialog.addListBox("name",new String[]{"a","b","c","d","e"},0, c.xywh(1,4,1,4));
        
        dialog.addFormButton(null,"Add To:",c.xy(1,8));
        dialog.addFormButton(null,"Add CC:",c.xy(1,9));
        
        dialog.addLabel("From:",c.xy(3,1));     dialog.addTextField("from"   ,"a.herz@freegroup.de",true,c.xy(5,1));
        dialog.addLabel("To:",c.xy(3,3));       dialog.addTextField("to"     ,"",c.xy(5,3));
        dialog.addLabel("Subject:",c.xy(3,5));  dialog.addTextField("subject","",c.xy(5,5));
        
     		dialog.addTextArea("info", "the quick brown fox \n\tjumps over the lazy dog",c.xy(5,7));
        
    	  dialog.setCancelButton("Close");
    	  dialog.setDebug(true);
    		dialog.show(600,400);
    		dialog.show();
  	try
    {
      
      FileUtils.writeStringToFile(new File("webapp/x.htm"),header+dialog.getHtml(new HashMap())+footer,"ISO-8859-1");
//      Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL webapp/x.htm");
      Runtime.getRuntime().exec("C:\\Program Files\\Mozilla Firefox\\firefox.exe webapp/x.htm");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    System.out.println("done-..");
  }
  
  
  

  /**
   * @return Returns the callback.
   */
  public final boolean hasCallback()
  {
    return callback!=null;
  }

  /**
   * @return Returns the submitButtons.
   */
  public final List getSubmitButtons()
  {
    return submitButtons;
  }
  
  public String getTitle()
  {
    return StringUtil.toSaveString(title);
  }

  public boolean isAutoResize()
  {
    return autoResize;
  }

  public IAutosuggestProvider.AutosuggestItem[] getLastProvidedAutosuggestItems()
  {
    return lastProvidedAutosuggestItems;
  }

  public void setLastProvidedAutosuggestItems(IAutosuggestProvider provider, IAutosuggestProvider.AutosuggestItem[] lastProvidedAutosuggestItems)
  {
    this.lastProvidedAutosuggestItems = lastProvidedAutosuggestItems;
    this.lastAutosuggestProvider      = provider;
  }
}
