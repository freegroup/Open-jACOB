package test;


import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.debug.FormDebugUtils;
import net.ffxml.swtforms.extras.DefaultFormBuilder;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Flo
 *
 */
public class SWTTest8
{
	private static Shell shell;
	private static Display display;

	public static void main(String[] args)
	{
		SWTTest8 test = new SWTTest8();
		SWTTest8.display = new Display();
		SWTTest8.shell = new Shell(SWTTest8.display);

		test.createContents(SWTTest8.shell);
		SWTTest8.shell.pack();
		SWTTest8.shell.setText("SWTTest7");
		SWTTest8.shell.open();

		while (!SWTTest8.shell.isDisposed())
		{
			if (!SWTTest8.display.readAndDispatch())
				SWTTest8.display.sleep();
		}
	}

	protected void createContents(Shell shell)
	{
		FormLayout layout =
			new FormLayout(
				"10dlu,200dlu:grow,10dlu,10dlu,10dlu,100dlu:grow,50dlu,5dlu,50dlu,10dlu",
				"pref,10dlu,pref,10dlu,100dlu:grow,10dlu,pref,10dlu,100dlu:grow,10dlu,max(20dlu;pref)");
		
		// add rows dynamically
		PanelBuilder builder = new PanelBuilder(shell, layout);
		CellConstraints cc = new CellConstraints();
//		builder.add()
		builder.addButton("Ok",SWT.PUSH, cc.xy(7,11));
		builder.addButton("Cancel",SWT.PUSH, cc.xy(9,11));
	
		FormDebugUtils.debugLayout(builder.getComposite());
		/*
		FormLayout layout =
			new FormLayout(
				"right:max(40dlu;pref), 3dlu, 80dlu, 7dlu, right:max(40dlu;pref), 3dlu, 80dlu",
				"");
		
		// add rows dynamically
		PanelBuilder builder = new PanelBuilder(shell, layout);
		builder.setDefaultDialogBorder();
		
		builder.appendSeparator("Flange");

		builder.append("Identifier", new Text(shell, SWT.BORDER));
		builder.nextLine();

		builder.append("PTI [kW]", new Text(shell, SWT.BORDER));
		builder.append("Power [kW]", new Text(shell, SWT.BORDER));

		builder.append("s [mm]", new Text(shell, SWT.BORDER));
		builder.nextLine();

		builder.appendSeparator("Diameters");

		builder.append("da [mm]", new Text(shell, SWT.BORDER));
		builder.append("di [mm]", new Text(shell, SWT.BORDER));

		builder.append("da2 [mm]", new Text(shell, SWT.BORDER));
		builder.append("di2 [mm]", new Text(shell, SWT.BORDER));

		builder.append("R [mm]", new Text(shell, SWT.BORDER));
		builder.append("D [mm]", new Text(shell, SWT.BORDER));

		builder.appendSeparator("Criteria");

		builder.append("Location", buildLocationComboBox(shell));
		builder.append("k-factor", new Text(shell, SWT.BORDER));

		builder.appendSeparator("Bolts");

		builder.append("Material", buildMaterialComboBox(shell));
		builder.nextLine();

		builder.append("Numbers", new Text(shell, SWT.BORDER));
		builder.nextLine();

		builder.append("ds [mm]", new Text(shell, SWT.BORDER));
		*/
	}

	protected Combo buildLocationComboBox(Shell shell)
	{
		Combo locations = new Combo(shell, SWT.BORDER);
		locations.setItems(
			new String[] { "Location 1", "Location 2", "Location 3" });
		return locations;
	}

	protected Combo buildMaterialComboBox(Shell shell)
	{
		Combo materials = new Combo(shell, SWT.BORDER);
		materials.setItems(
			new String[] { "Material 1", "Material 2", "Material 3" });
		return materials;
	}
}