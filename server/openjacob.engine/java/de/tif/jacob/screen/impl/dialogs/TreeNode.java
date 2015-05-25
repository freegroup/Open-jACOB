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

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.impl.DataRecordTreeNode;
import de.tif.jacob.screen.IClientContext;


public class TreeNode extends DataRecordTreeNode
{
  private String callbackId;
  private String label;
  private String image;
  
  /**
   * 
   */
  public TreeNode()
  {
  }
  
  public void onSetNodeRecord()
  {
    callbackId = Integer.toString(hashCode());
    setLabel(HTTPRecordTreeDialog.DEFAULT_LABEL_PROVIDER.getText((IClientContext)Context.getCurrent(), this));
  }
  
  public String getCallbackId()
  {
    return callbackId;
  }
  
  public String getLabel()
  {
    return label;
  }
  
  public String getImage()
  {
    return image;
  }
  
  void setLabel(String label)
  {
    if(label==null)
      this.label="";
    else
      this.label = label;
  }
  
  void setImage(String image)
  {
     this.image = image;
  }
  
}