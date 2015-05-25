package test;

/*
 * Setting the font style, foreground and background colors of StyledText
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Snippet163 {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    StyledText text = new StyledText(shell, SWT.BORDER);
    text.setText("0123456789 ABCDEFGHIJKLM NOPQRSTUVWXYZ");
    // make 0123456789 appear bold
    StyleRange style1 = new StyleRange();
    style1.start = 0;
    style1.length = 10;
    style1.fontStyle = SWT.BOLD;
    text.setStyleRange(style1);
    // make   have a red font
    StyleRange style2 = new StyleRange();
    style2.start = 11;
    style2.length = 13;
    style2.foreground = display.getSystemColor(SWT.COLOR_RED);
    text.setStyleRange(style2);
    // make NOPQRSTUVWXYZ have a blue background
    StyleRange style3 = new StyleRange();
    style3.start = 25;
    style3.length = 13;
    style3.background = display.getSystemColor(SWT.COLOR_BLUE);
    text.setStyleRange(style3);

    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}
