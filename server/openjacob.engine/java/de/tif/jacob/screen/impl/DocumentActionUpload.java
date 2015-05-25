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

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;

public class DocumentActionUpload extends DocumentAction
{
  public void execute(IClientContext context, final HTTPDocument document, String value) throws Exception
  {
    context.createUploadDialog(new IUploadDialogCallback()
    {
      public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
      {
        if(fileData!=null)
        {
          document.getDataField().setValue(DataDocumentValue.create(fileName,fileData));
          document.resetCache();
          document.onUpload(context, fileName, fileData);
        }
      }
      
      public void onCancel(IClientContext context) throws Exception
      { 
        
      }
    }).show();
  }

  public String getIcon()
  {
    return "upload.png";
  }

  public String getLabelId()
  {
    return "BUTTON_COMMON_UPLOAD";
  }

  public String getTooltipId()
  {
    return "DOCUMENT_ACTION_TOOLTIP_UPLOAD";
  }
}
