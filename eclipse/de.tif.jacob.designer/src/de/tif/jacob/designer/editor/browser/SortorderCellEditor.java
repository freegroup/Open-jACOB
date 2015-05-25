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
 * Created on 14.07.2005
 *
 */
package de.tif.jacob.designer.editor.browser;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.ObjectModel;
/**
 *  
 */
public class SortorderCellEditor extends CellEditor
{
  String value = ObjectModel.SORTORDER_NONE;
  
  private static final int defaultStyle = SWT.NONE;

  public SortorderCellEditor() 
  {
    setStyle(defaultStyle);
}

  /**
   * Creates a new checkbox cell editor parented under the given control. The
   * cell editor value is a boolean value, which is initially <code>false</code>.
   * Initially, the cell editor has no cell validator.
   * 
   * @param parent
   *          the parent control
   */
  public SortorderCellEditor(Composite parent)
  {
    this(parent, defaultStyle);
  }

  /**
   * Creates a new checkbox cell editor parented under the given control. The
   * cell editor value is a boolean value, which is initially <code>false</code>.
   * Initially, the cell editor has no cell validator.
   * 
   * @param parent
   *          the parent control
   * @param style
   *          the style bits
   * @since 2.1
   */
  public SortorderCellEditor(Composite parent, int style)
  {
    super(parent, style);
  }

  /**
   * The <code>CheckboxCellEditor</code> implementation of this
   * <code>CellEditor</code> framework method simulates the toggling of the
   * checkbox control and notifies listeners with
   * <code>ICellEditorListener.applyEditorValue</code>.
   */
  public void activate()
  {
    if(BrowserFieldModel.SORTORDER_NONE.equals(value))
      value = BrowserFieldModel.SORTORDER_ASCENDING;
    else if(BrowserFieldModel.SORTORDER_ASCENDING.equals(value))
      value = BrowserFieldModel.SORTORDER_DESCENDING;
    else
      value = BrowserFieldModel.SORTORDER_NONE;
    
    fireApplyEditorValue();
  }

  /**
   * The <code>CheckboxCellEditor</code> implementation of this
   * <code>CellEditor</code> framework method does nothing and returns
   * <code>null</code>.
   */
  protected Control createControl(Composite parent)
  {
    return null;
  }

  /**
   * The <code>CheckboxCellEditor</code> implementation of this
   * <code>CellEditor</code> framework method returns the checkbox setting
   * wrapped as a <code>Boolean</code>.
   * 
   * @return the Boolean checkbox value
   */
  protected Object doGetValue()
  {
    return value;
  }

  /*
   * (non-Javadoc) Method declared on CellEditor.
   */
  protected void doSetFocus()
  {
    // Ignore
  }

  /**
   * The <code>CheckboxCellEditor</code> implementation of this
   * <code>CellEditor</code> framework method accepts a value wrapped as a
   * <code>Boolean</code>.
   * 
   * @param value
   *          a Boolean value
   */
  protected void doSetValue(Object value)
  {
    Assert.isTrue(value instanceof String);
    this.value = (String) value;
  }
}