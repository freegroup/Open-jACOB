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
 * Created on Oct 25, 2004
 *
 */
package de.tif.jacob.designer.editor.diagram.activity.contributor;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightRetargetAction;
import org.eclipse.gef.ui.actions.MatchWidthRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;


public class EditorContributor extends ActionBarContributor 
{
	/* 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() 
	{
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());

		addRetargetAction(new DeleteRetargetAction());

		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
	
   	addRetargetAction(new MatchWidthRetargetAction());
   	addRetargetAction(new MatchHeightRetargetAction());

   	addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
	}
	
	protected void declareGlobalActionKeys()
  {
  }
  
  public void contributeToToolBar(IToolBarManager toolBarManager) 
  {
    toolBarManager.add(getActionRegistry().getAction( ActionFactory.DELETE.getId()));

    toolBarManager.add(getActionRegistry().getAction( ActionFactory.UNDO.getId()));

    toolBarManager.add(getActionRegistry().getAction(  ActionFactory.REDO.getId()));
    toolBarManager.add(new Separator());
    
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.ALIGN_LEFT));
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.ALIGN_CENTER));
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.ALIGN_RIGHT));
    toolBarManager.add(new Separator());
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.ALIGN_TOP));
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.ALIGN_MIDDLE));
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.ALIGN_BOTTOM));
    toolBarManager.add(new Separator());
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.MATCH_WIDTH));
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.MATCH_HEIGHT));
    toolBarManager.add(new Separator());
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.ZOOM_IN));
    toolBarManager.add(getActionRegistry().getAction( GEFActionConstants.ZOOM_OUT));

    toolBarManager.add(new Separator());	
  	     	String[] zoomStrings = new String[] {	ZoomManager.FIT_ALL, 
  	     											ZoomManager.FIT_HEIGHT, 
  	     											ZoomManager.FIT_WIDTH	};
  	     	toolBarManager.add(new ZoomComboContributionItem(getPage(), zoomStrings));

  //	toolBarManager.add(new ZoomComboContributionItem(getPage()));
  }
}