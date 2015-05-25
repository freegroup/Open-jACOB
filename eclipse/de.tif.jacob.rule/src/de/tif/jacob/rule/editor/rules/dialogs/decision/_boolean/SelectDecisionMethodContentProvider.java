/*
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.decision._boolean;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.rule.editor.rules.model.DecisionModel;
import de.tif.jacob.ruleengine.Decision;


public class SelectDecisionMethodContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
  private TreeParent invisibleRoot;

  private final TreeViewer viewer;
  DecisionModel initialModel;
	public DecisionMethodTreeNode methodNode = null;
  
  protected SelectDecisionMethodContentProvider(TreeViewer viewer, DecisionModel initialModel)
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
      TreeParent x = new TreeParent(viewer,invisibleRoot, "Decision Objects");
      invisibleRoot.addChild(x);
      String className = Decision.class.getName();
			IJavaProject project = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
      IType parentType=project.findType(className);
      ITypeHierarchy h=parentType.newTypeHierarchy(project,null);
      IType[] types= h.getAllSubtypes(parentType);
			for (int i=0;i<types.length;i++)
			{
				IType type = types[i];
				if(type.getJavaProject().equals(project) && type.getCompilationUnit()!=null)
				{
					DecisionClassTreeNode node =new DecisionClassTreeNode(viewer,x,type);
			    x.addChild(node);
			    if(node.getDecisionClassName().equals(initialModel.getImplementationClass()))
			    {
			    	DecisionMethodTreeNode m = node.getChild(initialModel.getDecisionMethodName());
			    	if(m!=null)
			    	{
			    		methodNode= m;
			    	}
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
