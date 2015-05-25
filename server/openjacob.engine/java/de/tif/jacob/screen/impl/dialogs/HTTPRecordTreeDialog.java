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

import java.util.Iterator;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.data.impl.DataRecordTree;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILabelProvider;
import de.tif.jacob.screen.RecordLabelProvider;
import de.tif.jacob.screen.dialogs.IRecordTreeDialog;
import de.tif.jacob.screen.dialogs.IRecordTreeDialogCallback;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPGroup;

/**
 *
 */
public abstract class HTTPRecordTreeDialog extends HTTPGenericDialog implements IRecordTreeDialog
{
  protected static final ILabelProvider DEFAULT_LABEL_PROVIDER = new RecordLabelProvider();

  final IRecordTreeDialogCallback callback;
  TreeNode rootNode;
  protected final IGuiElement  anchor;
  protected HTTPClientContext context;
  
  public HTTPRecordTreeDialog(HTTPClientContext context,IGuiElement anchor, IDataTableRecord record, IRelationSet relationSet, Filldirection filldirection ,IRecordTreeDialogCallback callback) throws Exception
  {
    super((HTTPDomain)context.getDomain(), (HTTPGroup)context.getGroup());
    this.callback = callback;
    this.anchor   = anchor;
    this.context  = context;
    
    addChildren(record,relationSet,filldirection);
  }
  
  /* 
   * @see de.tif.jacob.screen.dialogs.IDialog#show()
   */
  public final void show()
  {
    show(300,250);
  }

  public void setLabelProvider(ILabelProvider provider)
  {
    if(provider==null)
      provider = DEFAULT_LABEL_PROVIDER;
    update(provider,rootNode);
  }
  
  private void update(ILabelProvider provider, TreeNode node)
  {
    node.setLabel(provider.getText(context, node));
    node.setImage(provider.getImage(context, node));
    Iterator iter=node.getChildren().iterator();
    while(iter.hasNext())
      update(provider,(TreeNode)iter.next());
  }
  
  public TreeNode getRootNode()
  {
    return rootNode;
  }
  
  public final void processEvent(IClientContext context,  String event, String value) throws Exception
  {
    if("click".equals(event))
    {
      if(rootNode.getCallbackId().equals(value))
        callback.onSelect(context,rootNode.getInternalRecord());
      else
        onClick(context,rootNode,event,value);
    }
  }
  
  private final boolean onClick(IClientContext context,TreeNode node,  String event, String value) throws Exception
  {
    Iterator iter=node.getChildren().iterator();
    while(iter.hasNext())
    {
      TreeNode child = (TreeNode)iter.next();
      if(child.getCallbackId().equals(value))
      {
        callback.onSelect(context,child.getInternalRecord());
        return true;
      } 
      // check recursive all children of the tree
      //
      if(onClick(context,child,event,value))
        return true;
    }
    return false;
  }

  private void addChildren(IDataTableRecord record, IRelationSet relationSet, Filldirection filldirection) throws Exception
	{
		if (record == null)
			throw new NullPointerException("hands over parameter 'record' of type 'DataTableRecord' is null.");

		// create new accessor to avoid any backlash to calling context
		DataAccessor accessor = (DataAccessor)record.getAccessor().newAccessor();

		DataRecordTree tree = new DataRecordTree(accessor);

    // and write record (recursive call!)
		rootNode = (TreeNode)tree.propagate(record, relationSet, filldirection, TreeNode.class);
	}
  
}
