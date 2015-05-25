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
 * Created on Jul 13, 2004
 */
package de.tif.jacob.designer.editor.jacobform.editpart;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowObjectModelHookAction;
import de.tif.jacob.designer.actions.ShowTableAliasEditorAction;
import de.tif.jacob.designer.actions.ShowTableFieldEditorAction;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.editor.jacobform.directedit.GroupEditorLocator;
import de.tif.jacob.designer.editor.jacobform.directedit.TabEditorLocator;
import de.tif.jacob.designer.editor.jacobform.editpolicies.GroupDeletePolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.GroupDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.TabDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.TabStripLayoutEditPolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.XYLayoutEditPolicyGroupImpl;
import de.tif.jacob.designer.editor.jacobform.figures.GroupFigure;
import de.tif.jacob.designer.editor.jacobform.figures.InformBrowserFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TabFigure;
import de.tif.jacob.designer.editor.relationset.figures.ColumnFigure;
import de.tif.jacob.designer.editor.relationset.figures.TableAliasFigure;
import de.tif.jacob.designer.editor.relationset.policy.TableAliasEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.TableContainerEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.TableLayoutEditPolicy;
import de.tif.jacob.designer.editor.relationset.policy.TableNodeEditPolicy;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabModel;
import de.tif.jacob.util.StringUtil;

/**
 * 
 * @author Andreas Herz
 */
public class TabEditPart extends ObjectEditPart
{
  protected IFigure createFigure()
  {
    return new TabFigure();
  }

  /**
	 * Returns the Table model object represented by this EditPart
	 */
	public UITabModel getTabModel()
	{
		return (UITabModel) getModel();
	}

  public void createEditPolicies()
  {
    super.createEditPolicies();
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new TabDirectEditPolicy());
  }

  public void performRequest(Request request)
  {
    if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && !(getParent()instanceof TabPanesEditPart))
    {
      new ExtendedDirectEditManager(this, TextCellEditor.class, new TabEditorLocator((TabFigure) getFigure()), (TabFigure) getFigure()).show();
    }
    else
      super.performRequest(request);
  }


	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent ev)
	{
    if(ev.getSource() instanceof JacobModel)
    {
      if(ev.getPropertyName()==ObjectModel.PROPERTY_TESTRELATIONSET_CHANGED)
      {
        setHighlight((RelationsetModel)ev.getNewValue());
        refreshVisuals();
      }
      if(ev.getPropertyName()==ObjectModel.PROPERTY_TESTRESOURCEBUNDLE_CHANGED)
        refreshVisuals();
    }
    else if (ev.getSource()== getModel())
      refreshVisuals();
    else
      super.propertyChange(ev);
	}

  public void setHighlight(RelationsetModel relationset)
  {
    if (relationset == null)
      getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_NONE);
    else if (relationset.contains(getTabModel().getUIGroupModel().getTableAliasModel()))
      getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_TRUE);
    else
      getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_FALSE);
  }


  /**
	 * Reset the layout constraint, and revalidate the content pane
	 */
	public void refreshVisuals()
	{
		TabFigure tableFigure = (TabFigure) getFigure();
		I18NResourceModel resourcebundle = getTabModel().getJacobModel().getTestResourcebundle();
		String label = getTabModel().getLabel();
		if(resourcebundle!=null && label.startsWith("%"))
		  label = StringUtil.toSaveString(resourcebundle.getValue(label.substring(1)));
		tableFigure.setText(label);

    getObjectFigure().setHook( getTabModel().getJacobModel().getApplicationModel().isEventHandlerLookupByReference() && getTabModel().getHookClassName()!=null);
    // error/warning/info handling
    //
    String text=getTabModel().getError();
    getObjectFigure().setError(text);
    if(text!=null)
      return;
    text = getTabModel().getWarning();
    getObjectFigure().setWarning(text);
    if(text!=null)
      return;
    text = getTabModel().getInfo();
    getObjectFigure().setInfo(text);
  }
  
  
  public DragTracker getDragTracker(Request request)
  {
    TabStripEditPart strip = (TabStripEditPart)getParent();
    strip.setVisible(getTabModel().getUIGroupModel());
    
    return super.getDragTracker(request);
  }
  
}