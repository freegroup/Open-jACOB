/*
 * Created on 01.11.2007
 *
 */
package test.nebula;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ImageSelector
{
  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);

    ImageSelectorDialog d = new ImageSelectorDialog(shell);
    d.open();
    
    if (!display.readAndDispatch())
        display.sleep();
    display.dispose();
  }  
}
