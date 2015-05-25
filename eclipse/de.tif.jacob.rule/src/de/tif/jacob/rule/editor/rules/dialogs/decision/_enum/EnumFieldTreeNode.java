/*
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.decision._enum;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.FieldModelTypeEnum;
import de.tif.jacob.designer.model.TableAliasModel;


public class EnumFieldTreeNode extends TreeObject
{
	private final FieldModelTypeEnum field;
  public EnumFieldTreeNode(TreeViewer viewer,TableAliasTreeNode parent, FieldModelTypeEnum field) throws Exception 
  {
      super(viewer,parent,field.getFieldModel().getName());
      this.field = field;
  }
	
  public boolean sortingEnabled()
  {
    return true;
  }
  
  public String getLabel() 
  {
  	return field.getFieldModel().getName();
	}

	public final Image getImage()
  {
    return JacobDesigner.getImage("BusinessObjectMethod.gif",JacobDesigner.DECORATION_NONE);
  }
  
  protected TableAliasModel getTableAliasModel()
  {
    return ((TableAliasTreeNode)getParent()).getTableAliasModel();
  }
  
  protected FieldModelTypeEnum getFieldModeEnumType()
  {
  	return field;
  }
}