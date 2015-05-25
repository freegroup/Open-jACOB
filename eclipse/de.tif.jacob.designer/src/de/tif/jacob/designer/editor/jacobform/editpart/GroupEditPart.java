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
package de.tif.jacob.designer.editor.jacobform.editpart;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.tools.DeselectAllTracker;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IActionFilter;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowTableEditorAction;
import de.tif.jacob.designer.editor.jacobform.action.ShowContextMenuEditorAction;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.editor.jacobform.directedit.GroupEditorLocator;
import de.tif.jacob.designer.editor.jacobform.editpolicies.GroupDeletePolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.GroupDirectEditPolicy;
import de.tif.jacob.designer.editor.jacobform.editpolicies.XYLayoutEditPolicyGroupImpl;
import de.tif.jacob.designer.editor.jacobform.figures.GroupFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ThinGroupFigure;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableModel;
import de.tif.jacob.designer.model.TreeSelectionObjectModelProvider;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.model.UIDBForeignFieldModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.util.StringUtil;

public class GroupEditPart extends FormElementEditPart implements TreeSelectionObjectModelProvider, IStyleProvider
{
  public void openDBEditor()
  {
    // reuse the object selection contribution to use a common method to oopen
    // the table editor.
    //
    final UIGroupModel model = (UIGroupModel) getModel();
    new ShowTableEditorAction()
    {
      public TableModel getTableModel()
      {
        return model.getTableAliasModel().getTableModel();
      }
    }.run(null);
  }

  /**
   * Liefert das Objekt Model zurück welches im ApplicationOutline selektiert werden
   * soll, wenn dieses Element selektiert wird (sync-Mode)
   * 
   * Wenn eine Gruppe im FormEditor selektiert wird, dann wird der zugehörige Alias im
   * ApplicationOutline selektiert.
   */
  public ObjectModel getTreeObjectModel()
  {
    return ((UIGroupModel) getModel()).getTableAliasModel();
  }

  
  public IFigure createFigure()
  {
    if(getParent()instanceof TabPanesEditPart)
      return new ThinGroupFigure();
    
    GroupFigure figure = new GroupFigure();
    figure.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        new de.tif.jacob.designer.actions.ShowContextMenuEditorAction()
        {
          @Override
          public UIGroupModel getGroupModel()
          {
            return GroupEditPart.this.getGroupModel();
          }
        }.run(null);
      }
    });
    return figure;
  }

  
  public void refreshVisuals()
  {
    super.refreshVisuals();
    UIGroupModel model = (UIGroupModel) getModel();
		
    I18NResourceModel resourcebundle = model.getJacobModel().getTestResourcebundle();
		String label =StringUtil.toSaveString( model.getLabel());
		if(resourcebundle!=null && label.startsWith("%"))
		  label = StringUtil.toSaveString(resourcebundle.getValue(label.substring(1)));

    // FREEGROUP: Das ist Schrott. Ordentlich Klassenstruktur anstatt instanceof!
		getObjectFigure().setText(label);
    if(getObjectFigure() instanceof GroupFigure)
    {
      GroupFigure group = (GroupFigure)getObjectFigure(); 
      group.setBorder(model.getHasBorder());
      group.setDecoration(null,false,false); 
//        boolean alignLeft = JacobDesigner.getPluginProperty(JacobDesigner.TEMPLATE_BUTTON_LEFT,false);
//        boolean alignTop = JacobDesigner.getPluginProperty(JacobDesigner.TEMPLATE_BUTTON_TOP,false);
//        group.setDecoration(model.getButtonBoundingRect(),alignLeft, alignTop);
    }
    
    setHighlight(model.getJacobModel().getTestRelationset());
  }

	protected void refreshParentLayout()
	{
    if(getParent()instanceof TabPanesEditPart)
    	return;
    super.refreshParentLayout();
	}
	
  public List getModelChildren()
  {
    return getGroupModel().getElements();
  }

  public UIGroupModel getGroupModel()
  {
    return (UIGroupModel) getModel();
  }

  public void propertyChange(PropertyChangeEvent ev)
  {
    if (ev.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_ADDED)
      refreshChildren();
    else if (ev.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_REMOVED)
      refreshChildren();
    else if (ev.getPropertyName() == ObjectModel.PROPERTY_BORDER_CHANGED)
      refreshVisuals();
    else if (ev.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_CHANGED)
      refreshChildren();
    else if (ev.getPropertyName() == ObjectModel.PROPERTY_VISIBILITY_CHANGED)
    {
      getFigure().setVisible(getModel()==ev.getNewValue());
    }
    else
      super.propertyChange(ev);
  }

  public void createEditPolicies()
  {
    super.createEditPolicies();
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new GroupDeletePolicy());
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicyGroupImpl());
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new GroupDirectEditPolicy());
    installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
  }

  public void performRequest(Request request)
  {
    if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && !(getParent()instanceof TabPanesEditPart))
    {
      // Es gibt nur ein DirectEdit wenn auch ein Rahmen vorhanden ist
      // (ansonsten ist das Label ja nicht sichtbar)
      //
      if(getGroupModel().getHasBorder())
        new ExtendedDirectEditManager(this, TextCellEditor.class, new GroupEditorLocator((GroupFigure) getFigure()), ((GroupFigure) getFigure()).getLabel()).show();
    }
    else
      super.performRequest(request);
  }

  public void setHighlight(RelationsetModel relationset)
  {
    if (relationset == null)
      getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_NONE);
    else if (relationset.contains(((UIGroupModel) getModel()).getTableAliasModel()))
      getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_TRUE);
    else
      getObjectFigure().setHighlight(ObjectFigure.HIGHLIGHT_FALSE);
  }

  public Object getAdapter(Class adapter)
  {
    if (adapter == org.eclipse.ui.IActionFilter.class)
    {
      IActionFilter filter = new IActionFilter()
      {
        public boolean testAttribute(Object target, String name, String value)
        {
          String caption = ((GroupEditPart) target).getGroupModel().getLabel();
          return caption != null && caption.startsWith(value);
        }
      };
      return filter;
    }
    else if (adapter == SnapToHelper.class)
    {
      List snapStrategies = new ArrayList();
      Boolean val = (Boolean) getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
      if (val != null && val.booleanValue())
        snapStrategies.add(new SnapToGuides(this));
      val = (Boolean) getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
      if (val != null && val.booleanValue())
        snapStrategies.add(new SnapToGeometry(this));
      val = (Boolean) getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
      if (val != null && val.booleanValue())
        snapStrategies.add(new SnapToGrid(this));
      if (snapStrategies.size() == 0)
        return null;
      if (snapStrategies.size() == 1)
        return (SnapToHelper) snapStrategies.get(0);
      SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
      for (int i = 0; i < snapStrategies.size(); i++)
        ss[i] = (SnapToHelper) snapStrategies.get(i);
      return new CompoundSnapToHelper(ss);
    }
    return super.getAdapter(adapter);
  }

  public DragTracker getDragTracker(Request request)
  {
    // Falls man bei dem DragDrop einer Gruppe die ALT Taste drückt, dann passiert kein DragDrop
    // sondern es wird der Selectionmodus gestartet (Selektionskästchen aufziehen)
    //
    if (request instanceof SelectionRequest   && ((SelectionRequest)request).isAltKeyPressed())
      return new MarqueeDragTracker();
      
    if(getParent()instanceof TabPanesEditPart)
      return new DragEditPartsTracker(this.getParent().getParent());
    
    if(getParent()instanceof StackContainerEditPart)
      return new DragEditPartsTracker(this.getParent());

    return super.getDragTracker(request);
  }


  public void consumeStyle(Map<String, Object> style)
  {
    UIGroupModel model = getGroupModel();

    Integer height = (Integer)style.get("element.height");
    Integer width = (Integer)style.get("element.width");
    Boolean hasBorder = (Boolean)style.get("element.hasBorder");
    Color borderColor = (Color)style.get("element.border.color");
    Integer borderWidth = (Integer)style.get("element.border.width");
    Color backgroundColor = (Color)style.get("element.background.color");
    Integer x = (Integer)style.get("element.x");
    Integer y = (Integer)style.get("element.y");

    if( x!=null && y !=null)
      model.setLocation(new Point(x,y));
         
    if(borderColor!=null)
      model.setBorderColor(borderColor);
 
    if(borderWidth!=null)
      model.setBorderWith(borderWidth);
 
    if(backgroundColor!=null)
      model.setBackgroundColor(backgroundColor);
 
    if(hasBorder!=null)
      model.setBorder(hasBorder);
    
    if(height!=null || width!=null)
    {
      Rectangle rect = model.getConstraint();
      
      if(height!=null)
        rect.height = height;
      
      if(width!=null)
        rect.width = width;
      
      model.setConstraint(rect);
    }
  }

  public void provideStyle(Map<String, Object> style, boolean withLocation)
  {
    UIGroupModel model = getGroupModel();

    style.put("element.height",model.getConstraint().height);
    style.put("element.width",model.getConstraint().width);
    style.put("element.hasBorder",model.getHasBorder());
    style.put("element.border.width",model.getBorderWidth());
    style.put("element.border.color",model.getBorderColor());
    style.put("element.background.color",model.getBackgroundColor());
 
    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
    }
}
}
