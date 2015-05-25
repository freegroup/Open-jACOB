package jacob.event.ui.incidentContact;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 06 10:42:14 CEST 2005
 *
 */
import jacob.common.outlook.AbstractContactCopyToOutLook;


 /**
  * The Event handler for the ContactCopyToOutLook-Button.<br>
  * The onAction will be called if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andreas
  */
public class ContactCopyToOutLook extends AbstractContactCopyToOutLook 
{
  /* (non-Javadoc)
   * @see jacob.common.outlook.AbstractContactCopyToOutLook#getOrganizationAlias()
   */
  public String getOrganizationAlias()
  {
    return "incidentOrganization";
  }
}

