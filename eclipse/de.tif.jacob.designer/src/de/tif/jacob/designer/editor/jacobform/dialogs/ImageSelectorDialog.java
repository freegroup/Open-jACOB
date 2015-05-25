/*
 * Created on 01.11.2007
 *
 */
package de.tif.jacob.designer.editor.jacobform.dialogs;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import de.tif.jacob.util.file.Directory;

public class ImageSelectorDialog extends Dialog 
{
  private final Font boldFont;
  private final File directory;
  private String filename=null;
  /**
   * InputDialog constructor
   * 
   * @param parent the parent
   */
  public ImageSelectorDialog(Shell parent, File directory) 
  {
    super(parent, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
    this.boldFont = new Font(parent.getDisplay(), "Helvetica", 9, SWT.BOLD);
    this.directory = directory;
  }

  /**
   * Opens the dialog and returns the input
   * 
   * @return String
   */
  public String open() 
  {
    // Create the dialog window
    Shell shell = new Shell(getParent(), getStyle());
    shell.setText(getText());
    createContents(shell);
    shell.pack();
    shell.open();
    Display display = getParent().getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    // Return the entered value, or null
    return filename;
  }

  /**
   * Creates the dialog's contents
   * 
   * @param shell the dialog window
   */
  private void createContents(final Shell shell) 
  {
    FormLayout layout =
      new FormLayout(
        "10dlu,200dlu:grow,50dlu,5dlu,50dlu,10dlu",
        "10dlu,20dlu,100dlu:grow,5dlu,max(20dlu;pref)");
    
    // add rows dynamically
    PanelBuilder builder = new PanelBuilder(shell, layout);
    CellConstraints cc = new CellConstraints();

    
    Label header = new Label(shell,SWT.LEFT);
    header.setFont(boldFont);
    header.setBackground(new Color(null,255,255,240));
    header.setText("Images Selector");
    builder.add(header, cc.xywh(1,1,6,1,cc.FILL,cc.FILL));
    
    StyledText info = new StyledText(shell, SWT.LEFT);
    info.setEditable(false);
    info.setEnabled(false);
    info.setBackground(new Color(null,255,255,240));
    String s1="Images must be store in your ";
    String s2="<PROJECT>/web/";
    String s3=" directory";
    info.setText(s1+s2+s3);
    
    StyleRange style1 = new StyleRange();
    style1.start = s1.length();
    style1.length = s2.length();
    style1.fontStyle = SWT.BOLD;
    info.setStyleRange(style1);

    StyleRange style3 = new StyleRange();
    style3.start = s1.length();
    style3.length = s2.length();
    style3.foreground = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
    info.setStyleRange(style3);

    builder.add(info, cc.xywh(1,2,6,1,cc.FILL,cc.FILL));

    final Button ok =builder.addButton("Ok",SWT.PUSH, cc.xy(3,5));
    ok.setEnabled(false);
    Button cancel =builder.addButton("Cancel",SWT.PUSH, cc.xy(5,5));
  
//    FormDebugUtils.debugLayout(builder.getComposite());

    List files = Directory.getAll(directory,false);
    final Gallery gallery = new Gallery(shell, SWT.V_SCROLL | SWT.MULTI|SWT.BORDER);

    // Renderers
    NoGroupRenderer gr = new NoGroupRenderer();
    gr.setMinMargin(2);
    gr.setItemHeight(60);
    gr.setItemWidth(120);
    gr.setAutoMargin(true);
    gallery.setGroupRenderer(gr);

    gallery.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
         ok.setEnabled(gallery.getSelectionCount()==1);
      }
    
      public void widgetDefaultSelected(SelectionEvent e)
      {
      }
    });
    DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
    gallery.setItemRenderer(ir);

    GalleryItem group = new GalleryItem(gallery, SWT.NONE);

    Iterator iter = files.iterator();
    while(iter.hasNext())
    {
      try
      {
        File imageFile = (File) iter.next();
        if(imageFile.getName().endsWith(".png") || imageFile.getName().endsWith(".gif")|| imageFile.getName().endsWith(".jpg"))
        {
          GalleryItem item = new GalleryItem(group, SWT.NONE);
          item.setImage(new Image(null,new FileInputStream(imageFile)));
          item.setText(imageFile.getName());
        }
      }
      catch(Exception exc)
      {
        
      }
    }
    builder.add(gallery,cc.xywh(1,3,6,1, cc.FILL, cc.FILL));
    
    // Create the OK button and add a handler
    // so that pressing it will set input
    // to the entered value
    ok.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        if(gallery.getSelectionCount()>=1)
          filename = gallery.getSelection()[0].getText();
        shell.close();
      }
    });

    // Create the cancel button and add a handler
    // so that pressing it will set input to null
    cancel.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        filename = null;
        shell.close();
      }
    });

    // Set the OK button as the default, so
    // user can type input and press Enter
    // to dismiss
    shell.setDefaultButton(ok);
  }
}
        