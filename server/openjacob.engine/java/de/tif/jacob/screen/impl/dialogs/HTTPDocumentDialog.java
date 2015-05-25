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

package de.tif.jacob.screen.impl.dialogs;

import de.tif.jacob.messaging.Message;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public abstract class HTTPDocumentDialog  extends HTTPGenericDialog implements IDocumentDialog
{
  final private String mimeType;
  final private String fileName;
  final private byte[] document;
  
  public HTTPDocumentDialog(HTTPClientContext context,  String mimeType, String filename, byte[] document)
  {
    super(null,null);
    if(mimeType==null)
      this.mimeType = Message.getMimeType(filename);
    else
      this.mimeType = mimeType;
    
    filename = StringUtil.replace(filename," ","_");
    filename = StringUtil.replace(filename,"/","_");
    filename = StringUtil.replace(filename,"\\","_");
    filename = StringUtil.replace(filename,"\"","_");
    filename = StringUtil.replace(filename,"'","_");
    
    this.fileName = filename;
    this.document = document;
  }


  /* 
   * @see de.tif.jacob.screen.dialogs.ICallbackDialog#processEvent(de.tif.jacob.screen.IClientContext, int, java.lang.String, java.lang.String)
   */
  public final void processEvent(IClientContext context, String event, String value) throws Exception
  {
  }


  /* 
   * @see de.tif.jacob.screen.dialogs.IDialog#show()
   */
  public final void show()
  {
    show(200,200);
  }

  /**
   * @return Returns the document.
   */
  public final byte[] getDocument()
  {
    return document;
  }

  /**
   * @return Returns the fileName.
   */
  public final String getFileName()
  {
    return fileName;
  }

  /**
   * @return Returns the mimeType.
   */
  public final String getMimeType()
  {
    return mimeType;
  }
}
