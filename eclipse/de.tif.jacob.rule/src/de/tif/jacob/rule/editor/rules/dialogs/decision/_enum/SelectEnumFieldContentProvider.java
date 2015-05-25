/*
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.decision._enum;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.rule.editor.rules.model.EnumDecisionModel;


public class SelectEnumFieldContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
  private TreeParent invisibleRoot;

  private final TreeViewer viewer;
  EnumDecisionModel initialModel;
	public EnumFieldTreeNode fieldNode = null;
  
  protected SelectEnumFieldContentProvider(TreeViewer viewer, EnumDecisionModel initialModel)
  {
      this.viewer = viewer;
      this.initialModel = initialModel;
  }
  
  public void inputChanged(Viewer v, Object oldInput, Object newInput)
  {
  }

  public void dispose()
  {
  }

  public Object[] getElements(Object parent)
  {
      if (invisibleRoot == null)
        initialize();
      return getChildren(invisibleRoot);
  }

  public Object getParent(Object child)
  {
    if (child instanceof TreeObject)
    {
      return ((TreeObject) child).getParent();
    }
    return null;
  }

  public Object[] getChildren(Object parent)
  {
    if (parent instanceof TreeParent)
    {
      return ((TreeParent) parent).getChildren();
    }
    return new Object[0];
  }

  public boolean hasChildren(Object parent)
  {
    if (parent instanceof TreeParent)
      return ((TreeParent) parent).hasChildren();
    return false;
  }

  /*
   * We will set up a dummy model to initialize tree heararchy. In a real
   * code, you will connect to a real model and expose its hierarchy.
   */
  private void initialize()
  {
    // Alle Knoten finden welche das Interface BusinessObject implementieren
    //
    try 
    {
      invisibleRoot = new TreeParent(viewer,null, "");
      TreeParent x = new TreeParent(viewer,invisibleRoot, "Tables");
      invisibleRoot.addChild(x);
      String aliasName = "";
      String fieldName = "";
      if(initialModel.getParameters().length==2)
      {
        aliasName = initialModel.getParameters()[0];
        fieldName  = initialModel.getParameters()[1];
      }
      
      Iterator iter = JacobDesigner.getPlugin().getModel().getTableAliasModels().iterator();
			while(iter.hasNext())
			{
				TableAliasModel alias = (TableAliasModel)iter.next();

        boolean hasEnum = false;
        Iterator fieldIter = alias.getFieldModels().iterator();
        while(fieldIter.hasNext())
        {
          FieldModel model = (FieldModel)fieldIter.next();
          if(model.getType()==FieldModel.DBTYPE_ENUM)
          {
            hasEnum=true;
            break;
          }
        }
        if(hasEnum)
        {
          TableAliasTreeNode node =new TableAliasTreeNode(viewer,x,alias);
  			  x.addChild(node);
          
          TreeObject[] children = node.getChildren();
          for(int i=0; i<children.length; i++)
          {
            EnumFieldTreeNode child= (EnumFieldTreeNode)children[i];
            if(child.getTableAliasModel().getName().equals(aliasName) && child.getFieldModeEnumType().getFieldModel().getName().equals(fieldName))
              fieldNode = child;
          }
        }
        
			}
		} 
    catch (Exception e) 
		{
			e.printStackTrace();
		}
  }
}
