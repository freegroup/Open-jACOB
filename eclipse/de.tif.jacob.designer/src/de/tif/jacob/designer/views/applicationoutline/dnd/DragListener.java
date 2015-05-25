/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
/*
 * Created on 10.12.2004
 *
 */
package de.tif.jacob.designer.views.applicationoutline.dnd;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import de.tif.jacob.designer.util.ModelTransfer;
import de.tif.jacob.designer.views.applicationoutline.TreeObject;

/**
 *  
 */
public class DragListener implements DragSourceListener
{
	public DragListener(TreeViewer viewer)
	{
	}
	
	private TreeObject[] getDragObjects(DragSourceEvent event)
	{
    List result = new ArrayList();
    try
    {
      if(event.getSource() instanceof org.eclipse.swt.dnd.DragSource)
      {
        org.eclipse.swt.dnd.DragSource source = (org.eclipse.swt.dnd.DragSource)event.getSource();
        if(source.getControl() instanceof org.eclipse.swt.widgets.Tree)
        {
          org.eclipse.swt.widgets.Tree tree = (org.eclipse.swt.widgets.Tree)source.getControl();
          org.eclipse.swt.widgets.TreeItem[] items = tree.getSelection();
          for (int i = 0; i < items.length; i++)
          {
            if(items[i].getData() instanceof TreeObject)
            {
              result.add(items[i].getData());
            }
          }  
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return (TreeObject[])result.toArray(new TreeObject[0]);
	}
	
  /*
   * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
   */
  public void dragStart(DragSourceEvent event)
  {
    System.out.println("dragStart");
    ModelTransfer.setObjects(null);
    TreeObject[] dragObjects = getDragObjects( event);
    if (dragObjects.length>0)
    {
      // das erste Element der selektion bestim ob das Ganze DragDrop fähig ist
      //
      dragObjects[0].dragStart(event);
      
      // Falls das Object ein DragDrop erlaubt, dann wird das enthaltene Model
      // in den Transfer übertragen
      //
      if(event.doit)
        ModelTransfer.setObjects(dragObjects);
    }
  }

  /*
   * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
   */
  public void dragSetData(DragSourceEvent event)
  {
    System.out.println("dragSetData");
    event.data = "juhu";
  }

  /*
   * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
   */
  public void dragFinished(DragSourceEvent event)
  {
    System.out.println("dragFinish");
  }
}
