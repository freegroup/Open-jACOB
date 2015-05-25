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

import javax.swing.ImageIcon;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;

public class ImageActionUpload extends ImageAction
{
  public void execute(IClientContext context, HTTPImage el, String value) throws Exception
  {
    final HTTPImage image = el;
    context.createUploadDialog(new IUploadDialogCallback()
        {
          public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
          {
            DataDocumentValue documentValue = DataDocumentValue.create(fileName, fileData);

            if (documentValue == null)
            {
              // clear image
              image.setRealWidth(-1);
              image.setRealHeight(-1);
            }
            else
            {
              // es ist nur jpg, gif und png erlaubt.
              //
              fileName = fileName.toLowerCase();
              if (!(fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif")))
              {
                context.createMessageDialog(new CoreMessage("MSG_INVALID_IMAGE_TYPE", fileName)).show();
                return;
              }
              
              // calculate image size
              //
              ImageIcon imageIcon = new ImageIcon(fileData);
              image.setRealWidth(imageIcon.getIconWidth());
              image.setRealHeight(imageIcon.getIconHeight());
            }
            
            image.getDataField().setValue(documentValue);
            image.resetCache();
          }

          public void onCancel(IClientContext context) throws Exception{ }
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
    return "IMAGE_ACTION_TOOLTIP_UPLOAD";
  }
}
