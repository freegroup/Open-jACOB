/*
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.commonBo;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;

import de.tif.jacob.designer.JacobDesigner;


public class BusinessObjectMethodTreeNode extends TreeObject
{
	private final IMethod method;
  public BusinessObjectMethodTreeNode(TreeViewer viewer,BusinessObjectClassTreeNode parent, IMethod method) throws Exception 
  {
      super(viewer,parent,method.getElementName()+ method.getSignature());
      this.method = method;
  }
	
  public boolean sortingEnabled()
  {
    return true;
  }
  
  public String getLabel() 
  {
  	String label =method.getElementName()+"( ";
  	
		try 
		{
			String[] names=method.getParameterNames();
			String[] types=method.getParameterTypes();
			for (int i=0; i<types.length; i++) 
			{
				String type = Signature.toString(types[i]);
				label = label + type +" "+ names[i];
				if((i+1)<types.length)
					label = label+", ";
			}
			label=label+")";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return label;
	}

	public final Image getImage()
  {
    return JacobDesigner.getImage("BusinessObjectMethod.gif",JacobDesigner.DECORATION_NONE);
  }
  
  protected IMethod getIMethod()
  {
  	return method;
  }
  
  public String getBusinessObjectClassName()
  {
  	return ((BusinessObjectClassTreeNode)getParent()).getBusinessObjectClassName();
  }
  
  public String getBusinessObjectMethod()
  {
  	return getName();
  }
}