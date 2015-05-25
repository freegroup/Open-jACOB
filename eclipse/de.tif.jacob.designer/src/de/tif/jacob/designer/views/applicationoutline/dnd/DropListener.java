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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.KeyModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIFormGroupModel;
import de.tif.jacob.designer.model.UIFormModel;
import de.tif.jacob.designer.model.UIIFormContainer;
import de.tif.jacob.designer.util.ModelTransfer;
import de.tif.jacob.designer.views.applicationoutline.ITreeFormContainerObject;
import de.tif.jacob.designer.views.applicationoutline.TreeDomainObject;
import de.tif.jacob.designer.views.applicationoutline.TreeExternalFormObject;
import de.tif.jacob.designer.views.applicationoutline.TreeFormGroupObject;
import de.tif.jacob.designer.views.applicationoutline.TreeFormObject;
import de.tif.jacob.designer.views.applicationoutline.TreeKeyObject;
import de.tif.jacob.designer.views.applicationoutline.TreeLinkedFormObject;
import de.tif.jacob.designer.views.applicationoutline.TreeObject;
import de.tif.jacob.designer.views.applicationoutline.TreePhysicalDataModelObject;
import de.tif.jacob.designer.views.applicationoutline.TreeTableFieldObject;
import de.tif.jacob.designer.views.applicationoutline.TreeTableParent;

/**
 *  
 */
public class DropListener implements DropTargetListener
{
	public DropListener(TreeViewer viewer)
	{
	}
	
  /*
   * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
   */
  public void dragEnter(DropTargetEvent event)
  {
    System.out.println("dragEnter");
  }

  /*
   * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
   */
  public void dragLeave(DropTargetEvent event)
  {
    System.out.println("dragLeave");
   
  }

  /*
   * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
   */
  public void dragOperationChanged(DropTargetEvent event)
  {
    System.out.println("dragOperationchanged");
  }

  
  /*
   * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
   */
  public void dragOver(DropTargetEvent event)
  {
    event.feedback = DND.FEEDBACK_NONE;
    
    // nur DragDrop von Objekten gleichen Typs sind erlaubt
    //
    if(!ModelTransfer.areObjectsSameType())
      return;
    
    try
    {
      Object dragObject = ModelTransfer.getObjects()[0];
      Object dropObject = ((TreeObject)event.item.getData());
      // Eine JacobForm wird über eine Domain gezogen
      //
      if(dragObject instanceof TreeFormObject)
      {
        if(event.item.getData() instanceof TreeDomainObject)
          event.feedback = DND.FEEDBACK_SELECT;
      }
      
      // Eine ExternalForm wird über eine Domain gezogen
      //
      if(dragObject instanceof TreeExternalFormObject)
      {
        if(event.item.getData() instanceof TreeDomainObject)
          event.feedback = DND.FEEDBACK_SELECT;
      }
      
      // Eine JacobForm wird über eine FormGrop gezogen
      //
      if(dragObject instanceof TreeFormObject)
      {
        if(event.item.getData() instanceof TreeFormGroupObject)
          event.feedback = DND.FEEDBACK_SELECT;
      }
      
      // Eine ExternalForm wird über eine FormGrop gezogen
      //
      if(dragObject instanceof TreeExternalFormObject)
      {
        if(event.item.getData() instanceof TreeFormGroupObject)
          event.feedback = DND.FEEDBACK_SELECT;
      }

      // Es wird ein TableField durch die Gegend gezogen
      //
      else if(dragObject instanceof TreeTableFieldObject)
      {
        // ...welche auf einen Key geworfen wird
        if(dropObject instanceof TreeKeyObject)
        {
          FieldModel fieldModel = ((TreeTableFieldObject)dragObject).getFieldModel();
          KeyModel   keyModel   = ((TreeKeyObject)dropObject).getKeyModel();
          // Feld und Key muessen von der selben Tabelle sein
          if(fieldModel.getTableModel()==keyModel.getTableModel())
            event.feedback = DND.FEEDBACK_SELECT;
        }
      }
      
      // Es wird eine TreeLinkedFormObject durch die Gegend gezogen
      //
      else if(dragObject instanceof TreeLinkedFormObject)
      {
        // eine LinkedForm wird auf eine andere LinkedForm gezogen
        //
        if(dropObject instanceof TreeLinkedFormObject)
        {
          event.feedback = DND.FEEDBACK_INSERT_AFTER;
        }
        // eine LinkedForm wird auf eine Domain gezogen
        //
        else if(dropObject instanceof TreeDomainObject)
        {
          event.feedback = DND.FEEDBACK_SELECT;
        }
        // eine LinkedForm wird auf eine FormGroup gezogen
        //
        else if(dropObject instanceof TreeFormGroupObject)
        {
          event.feedback = DND.FEEDBACK_SELECT;
        }
      }
      // Es wird eine Tabelle hin und her bewegt
      //
      else if(dragObject instanceof TreeTableParent)
      {
        if(dropObject instanceof TreePhysicalDataModelObject)
          event.feedback = DND.FEEDBACK_SELECT;
      }
      // Es wird eine TreeDomainObject durch die Gegend gezogen
      //
      else if(dragObject instanceof TreeDomainObject)
      {
        if(dropObject instanceof TreeDomainObject)
          event.feedback = DND.FEEDBACK_INSERT_AFTER;
      }
      else if(dragObject instanceof TableAliasModel)
      {
        return;
      }
      else if(dragObject instanceof RelationModel)
      {
        return;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  
  /*
   * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
   */
  public void drop(DropTargetEvent event)
  {
    // nur DragDrop von Objekten gleichen Typs sind erlaubt
    //
    if(!ModelTransfer.areObjectsSameType())
      return;

    try
    {
      Object[] objects = ModelTransfer.getObjects();
      for (int i = 0; i < objects.length; i++)
      {
        Object dragObject = objects[i];
        Object dropObject = ((TreeObject)event.item.getData());
        if(dragObject instanceof TreeFormObject)
        {
          // Eine Form wir auf eine Domain fallen gelassen
          //
          if(dropObject instanceof TreeDomainObject)
          {
            TreeDomainObject treeDomain =(TreeDomainObject)dropObject;
            treeDomain.getDomainModel().addElement(((TreeFormObject)dragObject).getFormModel() );
          }
          // Eine Form wird auf eine FormGroup gezogen
          //
          else if(dropObject instanceof TreeFormGroupObject)
          {
            TreeFormGroupObject treeGroup =(TreeFormGroupObject)dropObject;
            treeGroup.getFormGroupModel().addElement(((TreeFormObject)dragObject).getFormModel() );
          }
        }
        else if(dragObject instanceof TreeTableFieldObject)
        {
          // ...welche auf einen Key geworfen wird
          if(dropObject instanceof TreeKeyObject)
          {
            FieldModel fieldModel = ((TreeTableFieldObject)dragObject).getFieldModel();
            KeyModel   keyModel   = ((TreeKeyObject)dropObject).getKeyModel();
            // Feld und Key muessen von der selben Tabelle sein
            if(fieldModel.getTableModel()==keyModel.getTableModel())
            {
              keyModel.addElement(fieldModel);
            }
          }
        }
        // Es wird eine TreeLinkedFormObject durch die Gegend gezogen
        //
        else if(dragObject instanceof TreeLinkedFormObject)
        {
          UIIFormContainer dragDomain=((ITreeFormContainerObject)((TreeLinkedFormObject)dragObject).getParent()).getFormContainerModel();
          UIFormModel dragForm = ((TreeLinkedFormObject)dragObject).getFormModel();
          
          // ....und auf ein anderes TreeLinkedFormObject fallen gelassen
          //
          if(dropObject instanceof TreeLinkedFormObject)
          {
            UIIFormContainer targetDomain=((ITreeFormContainerObject)((TreeLinkedFormObject)dropObject).getParent()).getFormContainerModel();
            UIFormModel targetForm = ((TreeLinkedFormObject)dropObject).getFormModel();
      
            // Element an alter Stelle aushängen
            //
            if(event.operations != DND.DROP_COPY)
              dragDomain.removeElement(dragForm);
  
            // ...und an neuer Stelle wieder einhängen
            //
            targetDomain.addElement(targetForm, dragForm);
          }
          // Ein TreeLinkedFormObject wird auf eine Domain fallen gelassen
          //
          else if(event.item.getData() instanceof TreeDomainObject)
          {
            UIDomainModel targetDomain=((TreeDomainObject)(event.item.getData())).getDomainModel();
  
            // Element an alter Stelle aushängen
            //
            if(event.operations != DND.DROP_COPY)
              dragDomain.removeElement(dragForm);
  
            targetDomain.addElement(dragForm);
          }
          // Eine TreeLinkedForm wird auf eine FormGroup fallen gelassen
          //
          else if(event.item.getData() instanceof TreeFormGroupObject)
          {
            UIFormGroupModel targetGroup=((TreeFormGroupObject)(event.item.getData())).getFormGroupModel();
  
            // Element an alter Stelle aushängen
            //
            if(event.operations != DND.DROP_COPY)
              dragDomain.removeElement(dragForm);
  
            targetGroup.addElement(dragForm);
          }
        }
        // Es wird ein TreeDomainObject durch die Gegend gezogen
        //
        else if(dragObject instanceof TreeDomainObject)
        {
          UIDomainModel  dragModel = ((TreeDomainObject)dragObject).getDomainModel();
          
          // ....und auf ein anderes TreeDomainObject fallen gelassen
          //
          if(dropObject instanceof TreeDomainObject)
          {
            UIDomainModel dropModel = ((TreeDomainObject)(dropObject)).getDomainModel();
      
            if(event.operations == DND.DROP_COPY)
            {
              // ...und an neuer Stelle wieder einhängen
              //
              String newName = "copyOf_"+dragModel.getLabel();
              UIDomainModel copy = new UIDomainModel(dragModel.getApplicationModel().getJacobModel(), newName);
              for(UIFormModel form: dragModel.getFormModels())
              {
                copy.addElement(form);
              }
              dragModel.getApplicationModel().addElement(dropModel, copy);
            }
            else
            {
              dragModel.getApplicationModel().removeElement(dragModel);
              // ...und an neuer Stelle wieder einhängen
              //
              dragModel.getApplicationModel().addElement(dropModel, dragModel);
            }
          }
        }
        // eine Tabelle wird hin und her bewegt
        //
        else if(dragObject instanceof TreeTableParent)
        {
          if(dropObject instanceof TreePhysicalDataModelObject)
          {
            TableModel table = ((TreeTableParent)dragObject).getTableModel();
            table.setDatasource(((TreePhysicalDataModelObject)dropObject).getDatasourceModel().getName());
          }
        }
      }
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
  }

  /*
   * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
   */
  public void dropAccept(DropTargetEvent event)
  {
  }

}
