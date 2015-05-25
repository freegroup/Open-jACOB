package jacob.event.ui.task;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 23 13:54:10 CEST 2005
 *
 */
import jacob.common.gui.GenericPrint;
import de.tif.jacob.screen.IClientContext;



 /**
  * The Event handler for the TaskPrint-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class TaskPrint extends GenericPrint
{
     static public final transient String RCS_ID = "$Id: TaskPrint.java,v 1.1 2005/06/27 12:23:19 mike Exp $";
     static public final transient String RCS_REV = "$Revision: 1.1 $";

    /* (non-Javadoc)
     * @see jacob.common.gui.GenericPrint#getConstraint(de.tif.jacob.screen.IClientContext)
     */
    public String getConstraint(IClientContext context) throws Exception
    {
        // Für Meldungen ist nur die Dokumentvorlage mit Use_im "Meldung|überall" gültig
        return "Auftrag|überall";
    }
}
