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

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IToolbarButton;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;


/**
 *
 */
public class ContextMenuEntryConfigureToolbar extends ContextMenuEntryServerSide
{
  Toolbar toolbar;
  String newLabel;
  protected ContextMenuEntryConfigureToolbar(IApplication app, Toolbar toolbar)
  {
    super(app, "CONTEXTMENU_CONFIGURE_TOOLBAR");
    
    this.toolbar=toolbar;
  }
  
  public String getI18NLabel(Locale locale)
  {
    if(newLabel==null)
        newLabel=I18N.getCoreLocalized("CONTEXTMENU_CONFIGURE_TOOLBAR",this.getApplicationDefinition(), locale);

    return newLabel;
  }
  
  /**
   * The framework call this method if this event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    // The event is not for this object
    //
    if(guid!=this.getId())
      return false;

    // Determine the count of ToolbarButton childs
    // (Note not all children of a ToolBar are ToolbarButtons
    //
    Iterator iter=toolbar.getChildren().iterator();
    int rows=0;
    while(iter.hasNext())
    {
      if(iter.next() instanceof IToolbarButton)
        rows++;
    }
    
    // create the layout of the dialog
    //
    String rowsString="20dlu,p,13dlu,p,5dlu,";
    for (int i = 0; i < rows; i++)
    {
      rowsString+="p,3dlu,";
    }
    rowsString+="20dlu";
    
    FormLayout  layout = new FormLayout("20dlu,p,10dlu,150dlu,20dlu",  // columns
                                        rowsString);                  // rows
    
    IFormDialog dialog = context.createFormDialog("Einstellungen",layout,new IFormDialogCallback()
    {
      /* 
       * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext, java.lang.String, java.util.Map)
       */
      public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
      {
        Iterator iter = toolbar.getChildren().iterator();
        while(iter.hasNext())
        {
          Object obj = iter.next();
          if(obj instanceof ToolbarButton)
          {
            ToolbarButton button= (ToolbarButton)obj;
            button.setVisible(formValues.get(button.getEtrHashCode())!=null);
            toolbar.resetCache();
          }
        }
      }
    });
    CellConstraints cc= new CellConstraints();
    
    // Add the elements to the dialog
    //
    dialog.addLabel(new CoreMessage("TOOLBAR_BUTTON_VISIBILITY").print(context.getLocale()), cc.xy(1,3));
    dialog.addLabel(new CoreMessage("TOOLBAR_BUTTON_LABEL").print(context.getLocale()),cc.xy(3,3));
    iter=toolbar.getChildren().iterator();
    int i=5;
    while(iter.hasNext())
    {
      Object obj = iter.next();
      if(obj instanceof ToolbarButton)
      {
        ToolbarButton button= (ToolbarButton)obj;
        dialog.addCheckBox(button.getEtrHashCode(),button.isVisible(), cc.xy(1,i));
        dialog.addFormButton(null,button.getI18NLabel(context.getLocale()),cc.xy(3,i));
        i=i+2;
      }
    }
    
    
    dialog.addSubmitButton("ok",new CoreMessage("BUTTON_COMMON_APPLY").print(context.getLocale()), true);
    dialog.setCancelButton(new CoreMessage("BUTTON_COMMON_CANCEL").print(context.getLocale()));
 //   dialog.setDebug(true);
 		dialog.show(500,450);
    return true;
  }
}
