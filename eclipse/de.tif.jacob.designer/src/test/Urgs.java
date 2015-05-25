/*
 * Created on 22.09.2006
 *
 */
package test;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
public class Urgs extends EditorPart
{
  public static final String ID = "test.Urgs"; // TODO Needs to be whatever is mentioned in plugin.xml
  private Composite top = null;

  @Override
  public void doSave(IProgressMonitor monitor)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void doSaveAs()
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException
  {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean isDirty()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isSaveAsAllowed()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void createPartControl(Composite parent)
  {
    // TODO Auto-generated method stub
    top = new Composite(parent, SWT.NONE);
    top.setLayout(new GridLayout());
  }

  @Override
  public void setFocus()
  {
    // TODO Auto-generated method stub
  }
}
