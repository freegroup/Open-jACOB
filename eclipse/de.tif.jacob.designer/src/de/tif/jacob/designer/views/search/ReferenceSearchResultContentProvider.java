/*
 * Created on 29.06.2007
 *
 */
package de.tif.jacob.designer.views.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.views.search.tree.TreeObject;
import de.tif.jacob.util.clazz.ClassUtil;

class ReferenceSearchResultContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
  private TreeObject invisibleRoot;
  private TreeViewer viewer;
  private List<ObjectModel> references ;
  private ObjectModel model; 
  private Map<ObjectModel, TreeObject> nodeMap = new HashMap<ObjectModel, TreeObject>();
  
  public ReferenceSearchResultContentProvider(TreeViewer viewer,List<ObjectModel> references, ObjectModel model )
  {
    this.viewer= viewer;
    this.model = model;
    this.references = references;
    initialize();
  }

  public void inputChanged(Viewer v, Object oldInput, Object newInput)
  {
  }

  public void dispose()
  {
  }

  public Object[] getElements(Object parent)
  {
    if(parent instanceof IViewSite)
      return getChildren(invisibleRoot);
    return getChildren(parent);
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
    if (parent instanceof TreeObject)
    {
      return ((TreeObject) parent).getChildren();
    }
    return new Object[0];
  }

  public boolean hasChildren(Object parent)
  {
    if (parent instanceof TreeObject)
      return ((TreeObject) parent).hasChildren();
    return false;
  }

  /*
   * We will set up a dummy model to initialize tree heararchy. In a real
   * code, you will connect to a real model and expose its hierarchy.
   */
  private void initialize()
  {
    invisibleRoot = new TreeObject(viewer,null,"",JacobDesigner.getImage("folder.png"));

    if(references.size()==0)
      return;
   
    TreeObject root = new TreeObject(viewer,null,references.get(0).getJacobModel());
    invisibleRoot.addChild(root);
    nodeMap.put(references.get(0).getJacobModel(),root);
    
    for(ObjectModel reference : references)
    {
      getNode(reference);
    }
  }
  
  private TreeObject getNode(ObjectModel ref)
  {
    TreeObject node = nodeMap.get(ref);
    
    if(node!=null)
      return node;
    
    // Vater Element finden und den neuen Knoten dran hängen
    TreeObject parent = getNode(ref.getParent());
    node = new TreeObject(viewer,parent,ref);
    parent.addChild(node);
    nodeMap.put(ref,node);
    
    return node;
  }
}
