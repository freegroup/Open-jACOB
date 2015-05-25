/*
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.commonBo;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

import de.tif.jacob.designer.JacobDesigner;

public class DecisionClassTreeNode extends TreeParent
{
	private final IType type;
	
  public DecisionClassTreeNode(TreeViewer viewer,TreeParent parent, IType type) throws Exception
  {
    super(viewer, parent, type.getTypeQualifiedName());
    this.type =type;
    addChildren();
  }
  
  public void addChildren() throws Exception
  {
  	IMethod[] methods =type.getMethods();
  	for(int i=0;i<methods.length;i++)
  	{
  		IMethod method = methods[i];
  		// Die Methode muss boolean zurück geben
  		//
  		if(Signature.SIG_BOOLEAN.equals(method.getReturnType()))
  		{
  			boolean add = true;
  			String[] types=method.getParameterTypes();
  			for (int mi=0; mi<types.length; mi++) 
  			{
  				String type = Signature.toString(types[mi]);
  				add = add && "String".equals(type);
  			}
  			if(add)
  				addChild(new DecisionMethodTreeNode(viewer,this,method));
  		}
  	}
  }

  public DecisionMethodTreeNode getChild(String methodName)
  {
  	TreeObject[] children = getChildren();
  	for(int i =0;i<children.length;i++)
  	{
  		if(((DecisionMethodTreeNode)children[i]).getBusinessObjectMethod().equals(methodName))
  			return (DecisionMethodTreeNode)children[i];
  	}
  	return null;
  }
 
  public String getDecisionClassName()
  {
  	return type.getFullyQualifiedName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getImage()
   */
  public  Image getImage()
  {
    return JacobDesigner.getImage("DecisionClass.gif",JacobDesigner.DECORATION_NONE);
  }
}