/*
 * Created on 30.06.2009
 *
 */
package de.tif.jacob.designer.editor.jacobform.misc;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
/**
 * @author "Aurelien Pupier" The spinner Cell Editor to edit Integer value
 *         (priority per example)
 */
public class SpinnerCellEditor extends CellEditor implements KeyListener
{
  /**
   * The spinner of the cell editor.
   */
  private Spinner spinner;

  public SpinnerCellEditor(Composite parent)
  {
    super(parent);
    System.out.println();
  }

  @Override
  protected Control createControl(Composite parent)
  {
    spinner = new Spinner(parent, SWT.RIGHT);
    spinner.addKeyListener(this); // to exit on enter key pressed
    spinner.setMinimum(Integer.MIN_VALUE);
    spinner.setMaximum(Integer.MAX_VALUE);
    return spinner;
  }

  @Override
  protected Object doGetValue()
  {
    return Integer.valueOf(spinner.getSelection()).toString();
  }

  @Override
  protected void doSetFocus()
  {
    spinner.forceFocus();
  }

  @Override
  protected void doSetValue(Object value)
  {
    spinner.setSelection(Integer.parseInt((String) value));
  }

  /*
   * KeyListener implements
   */
  public void keyPressed(KeyEvent keyEvent)
  {
    if (keyEvent.character == '\r')
    { // Return key
      // Enter is handled in handleDefaultSelection.
      // Do not apply the editor value in response to an Enter key event
      // since this can be received from the IME when the intent is -not-
      // to apply the value.
      // See bug 39074 [CellEditors] [DBCS] canna input mode fires bogus event
      // from Text Control
      if (spinner != null && !spinner.isDisposed())
      {
        super.keyReleaseOccured(keyEvent);
      }
      return;
    }
    super.keyReleaseOccured(keyEvent);
  }

  public void keyReleased(KeyEvent keyEvent)
  {
  }
}
