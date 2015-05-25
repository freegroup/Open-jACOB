package test;


import net.ffxml.swtforms.extras.DefaultFormBuilder;
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
public class SWTTest7
{
	private static Shell shell;
	private static Display display;

	public static void main(String[] args)
	{
		SWTTest7 test = new SWTTest7();
		SWTTest7.display = new Display();
		SWTTest7.shell = new Shell(SWTTest7.display);

		test.createContents(SWTTest7.shell);
		SWTTest7.shell.pack();
		SWTTest7.shell.setText("SWTTest7");
		SWTTest7.shell.open();

		while (!SWTTest7.shell.isDisposed())
		{
			if (!SWTTest7.display.readAndDispatch())
				SWTTest7.display.sleep();
		}
	}

	protected void createContents(Shell shell)
	{
		FormLayout layout =
			new FormLayout(
				"right:max(40dlu;pref), 3dlu, 80dlu, 7dlu, "
					+ "right:max(40dlu;pref), 3dlu, 80dlu",
				"");
		// add rows dynamically
		DefaultFormBuilder builder = new DefaultFormBuilder(shell, layout);
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