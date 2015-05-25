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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IWorkflowEditorCallback;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPGroup;
import de.tif.jacob.screen.impl.dialogs.HTTPGenericDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;


/**
 * 
 * @author Andreas Herz
 */
public class WorkflowEditorDialog  extends HTTPGenericDialog
{
  protected String xml;

  protected final IWorkflowEditorCallback callback;
  
  public WorkflowEditorDialog(ClientContext context, String xml, IWorkflowEditorCallback callback)
  {
    super((HTTPDomain)context.getDomain(), (HTTPGroup)context.getGroup());
    this.xml = xml;
    this.callback = callback;
  }

  public String getXml()
  {
    return xml;
  }

  public void setXml(String xml)
  {
    this.xml = xml;
  }

  public List<String> getPredefindVariables(IClientContext context)
  {
    return this.callback.getPredefindVariables(context);
  }
  
  public List<ISingleDataGuiElement> getRelevantGuiElements(IClientContext context) 
  {
     return this.callback.getRelevantGuiElements(context);
  }
  
  public List<ITableField> getRelevantTableFields(IClientContext context) 
  {
     return this.callback.getRelevantTableFields(context);
  }

  public void show(int width, int height)
  {
    this.show();
  }
  
  
  public void show()
  {
    Context context  = Context.getCurrent();
    // the dialog call comes from a GuiCallback (user interaction)
    //
    if(context instanceof ClientContext)
    {  
      ClientContext cc=(ClientContext)context;
     ((ClientSession)cc.getApplication().getSession()).addDialog(this);
      String htmlCode = "parent.window.location.href='./workflow/editor.jsp?browser=" + cc.clientBrowser+"&guid="+getId()+"';";
      cc.addOnLoadJavascript(htmlCode);
    }
    // the dialog comes from the schedule UserTask
    else
    {  
      throw new RuntimeException("Unable to open WorkflowEditor from background task/job");
    }
  }
  
  /*
   * The message dialog has nothing to proceed. Consume the event if this dialog has produce the event. 
   */
  
  public final void processEvent(IClientContext context, String event, String value) throws Exception
  {
    if("ok".equals(event))
      callback.onSave(context,this.xml);
    else
      callback.onCancel(context);
  }
}
