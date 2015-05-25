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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import de.tif.jacob.designer.views.applicationoutline.TreeObject;

/**
 *
 */
public class TooltipListener implements Listener
{
	final Tree       tree;
	Shell tip = null;
	Label label = null;	
	
	// Implement a "fake" tooltip
	final Listener labelListener = new Listener () 
	{
		public void handleEvent (Event event) 
		{
			Label label = (Label)event.widget;
			Shell shell = label.getShell ();
			switch (event.type) 
			{
				case SWT.MouseDown:
					Event e = new Event ();
					e.item = (TreeItem) label.getData ("_TREEITEM");
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					tree.setSelection (new TreeItem [] {(TreeItem) e.item});
					tree.notifyListeners (SWT.Selection, e);
					// fall through
				case SWT.MouseExit:
					shell.dispose ();
					break;
			}
		}
	};
	
	public TooltipListener(Tree tree)
	{
		this.tree = tree;
	}
	
	public void handleEvent (Event event) 
	{
		switch (event.type) 
		{
			case SWT.Dispose:
			case SWT.KeyDown:
			case SWT.MouseMove: 
			{
				if (tip == null) break;
				tip.dispose ();
				tip = null;
				label = null;
				break;
			}
			case SWT.MouseHover: 
			{
				TreeItem item = tree.getItem (new Point (event.x, event.y));
				if (item != null && item.getData() instanceof TreeObject) 
				{
					if (tip != null  && !tip.isDisposed ()) 
					  tip.dispose ();
					TreeObject treeObject = (TreeObject)item.getData();
					if(treeObject.getTooltip()==null)
					  return;
					
					tip = new Shell (tree.getShell(), SWT.ON_TOP);
					tip.setLayout (new FillLayout());
					label = new Label (tip, SWT.NONE);
					label.setForeground (tree.getDisplay().getSystemColor (SWT.COLOR_INFO_FOREGROUND));
					label.setBackground (tree.getDisplay().getSystemColor (SWT.COLOR_INFO_BACKGROUND));
					label.setData ("_TREEITEM", item);
					label.setText (treeObject.getTooltip());
					label.addListener (SWT.MouseExit, labelListener);
					label.addListener (SWT.MouseDown, labelListener);
					Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
					Rectangle rect = item.getBounds();
					Point pt = tree.toDisplay (rect.x, rect.y);
					tip.setBounds (pt.x, pt.y, size.x, size.y);
					tip.setVisible (true);
				}
			}
		}
	}
}
