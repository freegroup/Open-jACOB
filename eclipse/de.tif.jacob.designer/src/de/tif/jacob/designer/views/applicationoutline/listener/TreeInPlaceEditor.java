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
 * Created on 23.11.2004
 *
 */
package de.tif.jacob.designer.views.applicationoutline.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.views.applicationoutline.TreeObject;

/**
 *
 */
public class TreeInPlaceEditor implements Listener
{
	TreeItem lastItem = null;
	long lastClickTime=0;
	
	final Color black = new Color(null,0,0,0);
	final TreeEditor editor;
	final Tree       tree;
	
	public TreeInPlaceEditor(Tree tree)
	{
		this.editor= new TreeEditor(tree);
		this.tree = tree;
    this.tree.addMouseMoveListener(new MouseMoveListener()
    {
      public void mouseMove(MouseEvent e)
      {
        lastItem=null;
      }
    });
	}
	
	public void handleEvent(Event e) 
	{
		final TreeItem item = (TreeItem) e.item;
		// Falls der klick zuvor auf das gleiche ELement ging, wird jetzt ein Editor eingeblendet.
		//
		if ((item != null) && (item == lastItem) && ((System.currentTimeMillis()-lastClickTime)>500) && (item.getData() instanceof TreeObject)&& (((TreeObject)item.getData()).hasDirectEdit())) 
		{
			final Composite composite = new Composite(tree, SWT.NONE);
			composite.setBackground(black);
			final Text text = new Text(composite, SWT.NONE);
			composite.addListener(SWT.Resize, new Listener() 
									{
										public void handleEvent(Event e) 
										{
											Rectangle rect = composite.getClientArea();
											text.setBounds(rect.x + 1, rect.y + 1, rect.width - 2,rect.height - 2);
										}
									});
			
			Listener textListener = new Listener() 
			{
				public void handleEvent(final Event e) {
					switch (e.type) {
					case SWT.FocusOut:
					  try 
					  {
					    	((TreeObject)item.getData()).setName(text.getText());
					  } 
					  catch (Exception e1) 
					  {
							JacobDesigner.showException(e1);
					  }
						composite.dispose();
						break;
					case SWT.Verify:
						String newText = text.getText();
						String leftText = newText.substring(0, e.start);
						String rightText = newText.substring(e.end, newText.length());
						GC gc = new GC(text);
						Point size = gc.textExtent(leftText + e.text + rightText);
						gc.dispose();
						size = text.computeSize(size.x, SWT.DEFAULT);
						editor.horizontalAlignment = SWT.LEFT;
						Rectangle itemRect = item.getBounds(),
						rect = tree.getClientArea();
						editor.minimumWidth = Math.max(size.x, itemRect.width) + 2;
						int left = itemRect.x,
						right = rect.x + rect.width;
						editor.minimumWidth = Math.min(editor.minimumWidth, right	- left);
						editor.layout();
						break;
					case SWT.Traverse:
						switch (e.detail) 
						{
							case SWT.TRAVERSE_RETURN:
							  try 
							  {
							      ((TreeObject)item.getData()).setName(text.getText());
								} 
							  catch (Exception e1) 
							  {
									JacobDesigner.showException(e1);
								}
								//FALL THROUGH
							case SWT.TRAVERSE_ESCAPE:
								composite.dispose();
								e.doit = false;
						}
						break;
					}
				}
			};
			text.addListener(SWT.FocusOut, textListener);
			text.addListener(SWT.Traverse, textListener);
			text.addListener(SWT.Verify, textListener);
			editor.setEditor(composite, item);
			text.setText(((TreeObject)item.getData()).getName());
			text.selectAll();
			text.setFocus();
		}
		lastItem = item;
		lastClickTime= System.currentTimeMillis();
}
}
