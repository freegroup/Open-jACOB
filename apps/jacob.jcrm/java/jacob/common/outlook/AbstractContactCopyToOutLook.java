package jacob.common.outlook;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Mar 23 14:47:10 CET 2005
 *
 */

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the ContactCopyToOutLook-Button.<br>
 * The onAction will be called if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author mike
 */
public abstract class AbstractContactCopyToOutLook extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: AbstractContactCopyToOutLook.java,v 1.1 2005/10/12 15:19:47 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)   
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * To determine proper organization alias name of the contact.
   * <p>
   * Will be implemented by sub class!
   * 
   * @return organization alias name of the contact
   */
  public abstract String getOrganizationAlias();

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord contact = context.getSelectedRecord();

    StringBuffer vCard = new StringBuffer();
    vCard.append("BEGIN:VCARD\n")
    //Contact Lastname
        .append("N:").append(contact.getSaveStringValue("lastname")).append(";")
        //Contact Firstname
        .append(contact.getSaveStringValue("firstname")).append("\n")
        //      Contact Function
        .append("FN:").append(contact.getSaveStringValue("fullname")).append("\n");
    //      Contact Tel Work
    vCard.append("TEL;WORK;VOICE:");
    vCard.append(contact.getSaveStringValue("phone"));
    vCard.append("\n");
    //      Contact Tel Mobile
    vCard.append("TEL;CELL;VOICE:");
    vCard.append(contact.getSaveStringValue("mobile"));
    vCard.append("\n");
    //      Contact Tel Home
    vCard.append("TEL;HOME;VOICE:");
    vCard.append(contact.getSaveStringValue("privatephone"));
    vCard.append("\n");
    //      Contact Fax
    vCard.append("TEL;WORK;FAX:");
    vCard.append(contact.getSaveStringValue("fax"));
    vCard.append("\n");
    // Contact Position 
    vCard.append("TITLE:");
    vCard.append(contact.getSaveStringValue("position"));
    vCard.append("\n");
    // Contact E-Mail 
    vCard.append("EMAIL;PREF;INTERNET:");
    vCard.append(contact.getSaveStringValue("email"));
    vCard.append("\n");
    
    //Check whether organization exists
    boolean hasorg = contact.hasLinkedRecord(getOrganizationAlias());
    if (hasorg)
    {
      IDataTableRecord organisation = contact.getLinkedRecord(getOrganizationAlias());
      // Organisation Name
      vCard.append("ORG:");
      vCard.append(organisation.getSaveStringValue("name"));
      vCard.append(";");
      vCard.append(contact.getSaveStringValue("department"));
      vCard.append("\n");
      // Organisation Address
      vCard.append("ADR;WORK:;;");
      vCard.append(organisation.getSaveStringValue("address"));
      vCard.append(";");
      vCard.append(organisation.getSaveStringValue("city"));
      vCard.append(";;");
      vCard.append(organisation.getSaveStringValue("zip"));
      vCard.append(";");
      vCard.append(organisation.getSaveStringValue("country"));
      vCard.append("\n");
      //Organisation PostCode
      vCard.append("ADR;POSTAL:;;Postfach ");
      vCard.append(organisation.getSaveStringValue("pobox"));
      vCard.append(";");
      vCard.append(organisation.getSaveStringValue("city"));
      vCard.append(";;");
      vCard.append(organisation.getSaveStringValue("pozip"));
      vCard.append(";");
      vCard.append(organisation.getSaveStringValue("country"));
      vCard.append("\n");
      //Orgfanisation WebSite
      vCard.append("URL;WORK:http://");
      vCard.append(organisation.getSaveStringValue("webpage"));
      vCard.append("\n");

    }

    vCard.append("END:VCARD\n");

    context.createDocumentDialog("text/x-vCard", "contact.vcf", vCard.toString().getBytes()).show();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // You can enable/disable the button in relation to your conditions.
    //
    button.setEnable(status == IGuiElement.SELECTED);
  }
}
