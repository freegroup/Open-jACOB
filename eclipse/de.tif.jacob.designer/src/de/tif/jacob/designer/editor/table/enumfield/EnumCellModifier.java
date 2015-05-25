package de.tif.jacob.designer.editor.table.enumfield;
/*
 * Created on 02.02.2009
 *
 */


import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import de.tif.jacob.designer.editor.table.FieldDetailsPageEnum;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.FieldModelTypeEnum;


public class EnumCellModifier implements ICellModifier 
{
  private TableViewer tableViewerExample;
  
  /**
   * Constructor 
   * @param TableViewerExample an instance of a TableViewerExample 
   */
  public EnumCellModifier(TableViewer tableViewerExample) 
  {
    super();
    this.tableViewerExample = tableViewerExample;
  }

  /**
   * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
   */
  public boolean canModify(Object element, String property) 
  {
    return true;
  }

  /**
   * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
   */
  public Object getValue(Object element, String property) 
  {
    // Find the index of the column 
    int columnIndex = FieldDetailsPageEnum.COLUMN_NAMES.indexOf(property);
    int rowIndex = this.tableViewerExample.getTable().getSelectionIndex();

    FieldModelTypeEnum model= ((FieldModel)this.tableViewerExample.getInput()).getEnumFieldType();
    switch (columnIndex) 
    {
     case 0 : 
       return model.getEnumValue(rowIndex);
     case 1 : 
       return model.getLabel(rowIndex);
     default :
    }
    return "";  
  }

  /**
   * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
   */
  public void modify(Object element, String property, Object value) 
  { 
    // Find the index of the column 
    int columnIndex = FieldDetailsPageEnum.COLUMN_NAMES.indexOf(property);
    int rowIndex = this.tableViewerExample.getTable().getSelectionIndex();
    
    FieldModelTypeEnum model= ((FieldModel)this.tableViewerExample.getInput()).getEnumFieldType();
    switch (columnIndex) {
      case 0 : 
        model.setEnumValue(rowIndex,(String) value);
        break;
      case 1 : 
        model.setLabel(rowIndex,(String) value);
        break;
      default :
      }
  }
}