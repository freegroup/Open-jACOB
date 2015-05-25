/*
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.decision._enum;

import java.util.Iterator;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.TableAliasModel;

public class TableAliasTreeNode extends TreeParent
{
	private final TableAliasModel alias;
	
  public TableAliasTreeNode(TreeViewer viewer,TreeParent parent, TableAliasModel alias) throws Exception
  {
    super(viewer, parent, alias.getName());
    this.alias =alias;
    addChildren();
  }
  
  public void addChildren() throws Exception
  {
    
  	Iterator iter =alias.getFieldModels().iterator();
    while(iter.hasNext())
  	{
  		FieldModel field = (FieldModel)iter.next();
  		// Die Methode muss boolean zurück geben
  		//
      if(field.getType().equals(FieldModel.DBTYPE_ENUM))
  				addChild(new EnumFieldTreeNode(viewer,this,field.getEnumFieldType()));
  	}
    
  }

  protected TableAliasModel getTableAliasModel()
  {
    return alias;
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