package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Jun 15 16:54:12 CEST 2005
 *
 */
import jacob.common.gui.GenericPrint;
import de.tif.jacob.screen.IClientContext;



 /**
  * The Event handler for the CallPrintFax-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallPrintFax  extends GenericPrint
{
     static public final transient String RCS_ID = "$Id: CallPrintFax.java,v 1.1 2005/06/17 12:07:53 mike Exp $";
     static public final transient String RCS_REV = "$Revision: 1.1 $";

    /* (non-Javadoc)
     * @see jacob.common.gui.GenericPrint#getConstraint(de.tif.jacob.screen.IClientContext)
     */
    public String getConstraint(IClientContext context) throws Exception
    {
        // Für Meldungen ist nur die Dokumentvorlage mit Use_im "Meldung|überall" gültig
        return "Meldung|überall";
    }
}
