/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 24 14:01:05 CET 2009
 */
package jacob.event.ui.contact_type;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import com.hecc.jacob.validation.contacts.ContactValidators;


/**
 * The event handler for the Contact_typeLoadClassesButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author R.Spoor
 */
public class Contact_typeLoadClassesButton extends IButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: Contact_typeLoadClassesButton.java,v 1.1 2009/02/24 15:56:02 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The name of the JAR file that should include validator classes.
	 */
	private static final String JACOB_VALIDATION_JAR = "jacob-validation.jar";

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param emitter The corresponding button to this event handler
	 */
	@Override
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
	    File jacobRoot = new File(Bootstrap.getApplicationRootPath()).getAbsoluteFile();
	    File commonLib = new File(jacobRoot.getParentFile().getParentFile(), "common/lib");
	    File jacobLib = new File(jacobRoot, "WEB-INF/lib");
	    File jacobClasses = new File(jacobRoot, "WEB-INF/classes");
	    Set<String> loaded = ContactValidators.getValidatorClasses();
	    Set<String> classes = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	    ClassLoader loader = getClass().getClassLoader();
	    if (commonLib.isDirectory())
	    {
	        loadClassesFromJarFolder(commonLib, loaded, classes, loader);
	    }
	    if (jacobLib.isDirectory())
	    {
	        loadClassesFromJarFolder(jacobLib, loaded, classes, loader);
	    }
	    if (jacobClasses.isDirectory())
	    {
	        loadClassesFromClassFolder(jacobClasses, "", loaded, classes, loader);
	    }
	    if (classes.isEmpty() && loaded.isEmpty())
	    {
            String message = I18N.CONTACTTYPE_LOADCLASSES_NOCLASSES.get(context);
            context.createMessageDialog(message).show();
            return;
	    }
        IGridTableDialog dialog = context.createGridTableDialog(emitter);
        String[] header = {
            I18N.CONTACTTYPE_VALIDATION_METHOD_CLASS.get(context),
            I18N.CONTACTTYPE_LOADCLASSES_NEWLYLOADED.get(context),
        };
        dialog.setHeader(header);
        String[][] data = new String[classes.size() + loaded.size()][2];
        int index = 0;
        for (String className: classes)
        {
            data[index][0] = className;
            data[index][1] = Boolean.toString(true);
            index++;
        }
        for (String className: loaded)
        {
            data[index][0] = className;
            data[index][1] = Boolean.toString(false);
            index++;
        }
        dialog.setData(data);
        IGroup group = context.getGroup();
        if (group.getDataStatus() != IGuiElement.SELECTED)
        {
            ISingleDataGuiElement field = (ISingleDataGuiElement)group.findByName("contacttypeValidation_expression");
            dialog.connect(0, field);
        }
        dialog.show();
	}

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 *
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
     * @param emitter The corresponding button to this event handler
     */
    @Override
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
	{
	}

    /**
     * Loads all validator classes in a folder of JAR files.
     *
     * @param folder The folder to look for classes in.
     * @param loaded A set with the names of all previously loaded classes.
     * @param classes A set to store the names of newly loaded classes in.
     * @param loader The class loader to use.
     */
    private static void loadClassesFromJarFolder(File folder,
                                                 Set<String> loaded,
                                                 Set<String> classes,
                                                 ClassLoader loader)
    {
        File[] files = folder.listFiles();
        if (files == null)
        {
            return;
        }
        for (File file: files)
        {
            if (file.isDirectory())
            {
                loadClassesFromJarFolder(file, loaded, classes, loader);
            }
            else if (file.isFile())
            {
                String name = file.getName().toLowerCase();
                // limit to jacob-validation.jar
                // otherwise it will take much too long
                // also loading random unknown classes may have an unknown
                // dangerous impact
                if (name.equalsIgnoreCase(JACOB_VALIDATION_JAR))
                {
                    loadClassesFromJarFile(file, loaded, classes, loader);
                }
            }
        }
    }

    /**
     * Loads all validator classes in a JAR file.
     *
     * @param jar The JAR file to look for classes in.
     * @param loaded A set with the names of all previously loaded classes.
     * @param classes A set to store the names of newly loaded classes in.
     * @param loader The class loader to use.
     */
    private static void loadClassesFromJarFile(File jar, Set<String> loaded,
                                               Set<String> classes,
                                               ClassLoader loader)
    {
        InputStream is = null;
        try
        {
            is = new FileInputStream(jar);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null)
            {
                String name = entry.getName();
                if (name.endsWith(".class"))
                {
                    name = name.substring(0, name.length() - ".class".length());
                }
                name = name.replace('/', '.');
                name = name.replace('\\', '.');
                loadClass(name, loader, loaded, classes);
            }
        }
        catch (IOException e)
        {
            logger.warn("Could not read JAR file " + jar, e);
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    // ignore
                }
            }
        }
    }

    /**
     * Loads all validator classes in a folder.
     *
     * @param folder The folder to look for classes in.
     * @param path The path to append to class names. Initially this should be
     *        the empty string.
     * @param loaded A set with the names of all previously loaded classes.
     * @param classes A set to store the names of newly loaded classes in.
     * @param loader The class loader to use.
     */
    private static void loadClassesFromClassFolder(File folder, String path,
                                                   Set<String> loaded,
                                                   Set<String> classes,
                                                   ClassLoader loader)
    {
        File[] files = folder.listFiles();
        if (files == null)
        {
            return;
        }
        for (File file: files)
        {
            if (file.isDirectory())
            {
                loadClassesFromClassFolder(
                    file, path + file.getName() + ".", loaded, classes, loader
                );
            }
            else if (file.isFile())
            {
                String name = file.getName();
                if (name.endsWith(".class"))
                {
                    name = name.substring(0, name.length() - ".class".length());
                    name = path + name;
                    loadClass(name, loader, loaded, classes);
                }
            }
        }
    }

    /**
     * Loads a class.
     *
     * @param name The name of the class.
     * @param loader The class loader to use.
     * @param loaded A set with the names of all previously loaded classes.
     * @param classes A set to store the names of newly loaded classes in.
     */
    private static void loadClass(String name, ClassLoader loader,
                                  Set<String> loaded, Set<String> classes)
    {
        if (!loaded.contains(name) && !classes.contains(name))
        {
            try
            {
                ContactValidators.getValidator(name, loader);
                classes.add(name);
            }
            catch (Throwable t)
            {
                // ignore
            }
        }
    }
}
