/*
 * Created on 01.11.2007
 *
 */
package test;

import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SWTTest6 {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);

    createContents(shell);
    shell.pack();
    shell.setText("SWTTest6");
    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }

  protected static void createContents(Shell shell) {
    FormLayout layout = new FormLayout(
      "r:max(50dlu;p), 3dlu, 70dlu:grow, 3dlu, r:max(50dlu;p), 3dlu, 70dlu",
      "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");
    CellConstraints cc = new CellConstraints();
    PanelBuilder builder = new PanelBuilder(shell, layout);
    builder.setDefaultDialogBorder();

    builder.addSeparator("General", cc.xywh(1, 1, 7, 1));
    builder.addLabel("Company", cc.xy(1, 3));
    builder.add(new Text(shell, SWT.BORDER), cc.xywh(3, 3, 5, 1));
    builder.addLabel("Contact", cc.xy(1, 5));
    builder.add(new Text(shell, SWT.BORDER), cc.xywh(3, 5, 5, 1));

    builder.addSeparator("Propeller", cc.xywh(1, 7, 7, 1));
    builder.addLabel("PTI [kW]", cc.xy(1, 9));
    builder.add(new Text(shell, SWT.BORDER), cc.xy(3, 9));
    builder.addLabel("Power [kW]", cc.xy(5, 9));
    builder.add(new Text(shell, SWT.BORDER), cc.xy(7, 9));
    builder.addLabel("R [mm]", cc.xy(1, 11));
    builder.add(new Text(shell, SWT.BORDER), cc.xy(3, 11));
    builder.addLabel("D [mm]", cc.xy(5, 11));
    builder.add(new Text(shell, SWT.BORDER), cc.xy(7, 11));
  }
}