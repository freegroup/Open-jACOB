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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.impl.HTTPUploadDialog;
import de.tif.jacob.screen.impl.dialogs.HTTPGenericDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.screen.impl.html.Domain;
import de.tif.jacob.screen.impl.html.Group;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UploadDialog  extends HTTPGenericDialog implements HTTPUploadDialog
{
  private int expectedFileSize=0;
  private int currentFileSize=0;
  
  private final IUploadDialogCallback callback;
  private final String title;
  private final String description;
  private byte[] uploadedDocumentData=null;
  private String uploadedDocumentName=null;
  
  public UploadDialog(ClientContext context,  IUploadDialogCallback callback)
  {
    super((Domain)context.getDomain(), (Group)context.getGroup());
    
    this.callback = callback;
    this.title = null;
    this.description = null;
  }
  
  public UploadDialog(ClientContext context,String title, String description,  IUploadDialogCallback callback)
  {
    super((Domain)context.getDomain(), (Group)context.getGroup());
    
    this.callback = callback;
    this.title = title;
    this.description = description;
  }

  public void show()
  {
    show(400,350);
  }
  
  /**
   * Shows an HTML information dialog on the client. 
   * 
   * @param title    The title of the dialog
   * @param question The question of the dialog.
   */
  public void show(int width, int height)
  {
    ClientContext context  = (ClientContext)IClientContext.getCurrent();
    String html = "<script type=\"text/javascript\">new jACOBPopup('dialogs/UploadDialog.jsp?browser="+context.clientBrowser+"&guid="+getId()+"',400,170);</script>\n";
    context.addAdditionalHTML(html);
    ((ClientSession)(context).getApplication().getSession()).addDialog(this);
    context = null;
  }

  public void setDocument(String documentName, byte[] documentData)
  {
    uploadedDocumentName = documentName;
    uploadedDocumentData = documentData;
  }
  
  public byte[] getDocumentData()
  {
    return uploadedDocumentData;
  }
    
  public String getDocumentName()
  {
    return uploadedDocumentName;
  }
  
  public void processEvent(IClientContext context, String event, String value) throws Exception
  {
    if(event.equals("ok"))
    {  
      // remove the path from the filename
      //
      uploadedDocumentName = StringUtils.replace(uploadedDocumentName,"\\",File.separator);
      uploadedDocumentName = StringUtils.replace(uploadedDocumentName,"/",File.separator);
      uploadedDocumentName = FilenameUtils.getName(uploadedDocumentName);
      callback.onOk(context,uploadedDocumentName,uploadedDocumentData);
    }
    else if(event.equals("cancel"))
      callback.onCancel(context);
  }
  
  /**
   * @return Returns the currentFileSize.
   */
  public synchronized int getCurrentFileSize()
  {
    return currentFileSize;
  }

  /**
   * @param currentFileSize The currentFileSize to set.
   */
  public synchronized void setCurrentFileSize(int currentFielSize)
  {
    this.currentFileSize = currentFielSize;
  }

  /**
   * @return Returns the expectedFileSize.
   */
  public synchronized int getExpectedFileSize()
  {
    return expectedFileSize;
  }

  /**
   * @param expectedFileSize The expectedFileSize to set.
   */
  public synchronized void setExpectedFileSize(int expectedFileSize)
  {
    this.expectedFileSize = expectedFileSize;
  }

  public String getDescription()
  {
    return description;
  }

  public String getTitle()
  {
    return title;
  }

}
