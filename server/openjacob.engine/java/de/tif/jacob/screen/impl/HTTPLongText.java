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

package de.tif.jacob.screen.impl;

import java.util.Map;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.LongTextEditMode;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ILongText;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public interface HTTPLongText extends ILongText
{
  public ITableField getTableField();

  static class ReadonlyCallback implements IFormDialogCallback
  {
    final IDataTableRecord record;
    final String field;
    
    public ReadonlyCallback(IDataTableRecord record, String field)
    {
      this.record = record;
      this.field  = field;
    }
    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      if ("print".equals(buttonId))
      {
        IDocumentDialog dialog= context.createDocumentDialog("text/plain","document.txt",record.getSaveStringValue(field).getBytes());
        dialog.enforcePrinting(true);
        dialog.show(700,650);
      }
    }
  };
  
  static class EditCallback implements IFormDialogCallback
  {
    final IDataTableRecord record;
    final HTTPLongText element;

    public EditCallback(IDataTableRecord record, HTTPLongText element)
    {
      this.record = record;
      this.element = element;
    }

    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      if ("save".equals(buttonId))
      {
        if (record.getCurrentTransaction() != null)
        {
          element.setValue(StringUtil.toSaveString((String) values.get("text")));
        }
      }
    }
  };
  
  // Da es in Java keine Mehrfachvererbung gibt, erben die Klassem alle Eigenschaften
  // von GuiElement und delegieren Ihre objektspezifischen Aufrufe an die statische Klasse
  // 'Command'. Command ist somit nur für die objektspezifischen Eigenschaften zuständig - nicht
  // für die Allgemeinen!!
  //
  public static class Command
  {
    public static boolean processEvent(HTTPLongText element, IClientContext context, int guid, String event, String value) throws Exception
    {
      IDataTableRecord record = context.getDataTable(element.getGroupTableAlias()).getSelectedRecord();
      if(event.equals("show") || element.isEditable()==false)
      {
          String header = I18N.getCoreLocalized("DIALOG_TITLE_LONGTEXT_SHOW", context);
        
          IFormDialogCallback callback= new ReadonlyCallback(context.getSelectedRecord(),element.getTableField().getName());
          FormLayout layout = new FormLayout("10dlu,grow,10dlu", // 3 columns
                                             "20dlu,grow,20dlu"); // 3 rows
          CellConstraints c = new CellConstraints();

          IFormDialog dialog = context.createFormDialog(header, layout, callback);
          dialog.addTextArea("info", record.getSaveStringValue(element.getTableField().getName()), true, element.hasWordwrap(), c.xy(1, 1));

          dialog.addSubmitButton("print",I18N.getCoreLocalized("BUTTON_COMMON_PRINT",context));
          dialog.setCancelButton(I18N.getCoreLocalized("BUTTON_COMMON_CLOSE",context));
          dialog.show(750,500);
      }
      else if(event.equals("edit"))
      {
        String header = I18N.getCoreLocalized("DIALOG_TITLE_LONGTEXT_"+element.getEditMode().getName(),context);
        
		    CellConstraints c = new CellConstraints();
        IFormDialog dialog=null;
        if(element.getEditMode()==LongTextEditMode.APPEND)
        {
          FormLayout layout = new FormLayout("10dlu,grow,10dlu", // 3 columns
 					                        "20dlu,grow,2dlu,200dlu,20dlu"); // 5 rows
  	      dialog = context.createFormDialog(header, layout, new EditCallback(record, element));

          dialog.addTextArea("text", element.getValue(), false, element.hasWordwrap(), c.xy(1, 3)); // erste eingefügte Element erhält den Eingabefokus
          // Attention: use getSaveStringValue() instead of getOldSaveStringValue() here, since otherwise the text of copied records is not displayed
          dialog.addTextArea("old", record.getSaveStringValue(element.getTableField().getName()), true,element.hasWordwrap(),  c.xy(1, 1));
        }
        else if(element.getEditMode()==LongTextEditMode.FULLEDIT)
        {
          FormLayout layout = new FormLayout("10dlu,grow,10dlu", // 3 columns
                                             "20dlu,grow,20dlu"); // 3 rows
  	      dialog = context.createFormDialog(header, layout, new EditCallback(record, element));

  		    dialog.addTextArea("text", element.getValue(), false, element.hasWordwrap(), c.xy(1, 1)); // erste eingefügte Element erhält den Eingabefokus
        }
        else if(element.getEditMode()==LongTextEditMode.PREPEND)
        {
          FormLayout layout = new FormLayout("10dlu,grow,10dlu", // 3 columns
                        					"20dlu,200dlu,2dlu,grow,20dlu"); // 5 rows
  	      dialog = context.createFormDialog(header, layout, new EditCallback(record, element));
  		    
  		    dialog.addTextArea("text", element.getValue(), false, element.hasWordwrap(), c.xy(1, 1)); // erste eingefügte Element erhält den Eingabefokus
          // Attention: use getSaveStringValue() instead of getOldSaveStringValue() here, since otherwise the text of copied records is not displayed
          dialog.addTextArea("old", record.getSaveStringValue(element.getTableField().getName()), true, element.hasWordwrap(), c.xy(1, 3));
        }
		
		    dialog.addSubmitButton("save", I18N.getCoreLocalized("BUTTON_COMMON_APPLY", context), true);
		    dialog.setCancelButton(I18N.getCoreLocalized("BUTTON_COMMON_CLOSE",context));
		    dialog.show(700,500);
      }
      return true;
    }
  }
}
