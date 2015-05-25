/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 02 17:14:51 CET 2006
 */
package jacob.event.ui.license;

import jacob.model.License;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;


/**
 *
 * @author andreas
 */
 public class LicenseGroup extends IGroupListenerEventHandler 
 {
	static public final transient String RCS_ID = "$Id: LicenseGroup.java,v 1.1 2006/03/07 19:20:35 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
   * @param status  The new status of the group.
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
    ISingleDataGuiElement licenseIndefinite_flag = (ISingleDataGuiElement) context.getGroup().findByName("licenseIndefinite_flag");
    ISingleDataGuiElement licenseExpiration_date = (ISingleDataGuiElement) context.getGroup().findByName("licenseExpiration_date");
    ISingleDataGuiElement licenseUser_count = (ISingleDataGuiElement) context.getGroup().findByName("licenseUser_count");
    ISingleDataGuiElement licenseLicensee = (ISingleDataGuiElement) context.getGroup().findByName("licenseLicensee");
    ISingleDataGuiElement licenseIs_demo = (ISingleDataGuiElement) context.getGroup().findByName("licenseIs_demo");
    
		if (status == IGuiElement.NEW || status == IGuiElement.UPDATE)
    {
      IDataTableRecord licenseRecord = context.getSelectedRecord();
      
      boolean indefinite = licenseRecord.getintValue(License.indefinite_flag)!=0;
      boolean enhanced = License.type_ENUM._enhanced.equals(licenseRecord.getValue(License.type));
      boolean demo = licenseRecord.getintValue(License.is_demo)!=0;

      licenseIs_demo.setEnable(status == IGuiElement.NEW);
      licenseLicensee.setEnable(status == IGuiElement.NEW);
      
      if (status == IGuiElement.NEW)
      {
        // only enhanced mode supports indefinite license keys
        licenseIndefinite_flag.setEnable(enhanced && !demo);
        
        licenseUser_count.setEnable(!demo);

        // expiration date is required if not indefinite
        licenseExpiration_date.setEnable(!indefinite);
        licenseExpiration_date.setRequired(!indefinite);
      }
      else
      {
        // only enhanced mode supports indefinite license keys
        licenseIndefinite_flag.setEnable(enhanced && !demo && !indefinite);
        
        licenseUser_count.setEnable(!demo && enhanced);

        // expiration date is required if not indefinite
        licenseExpiration_date.setEnable(!indefinite && enhanced);
        licenseExpiration_date.setRequired(!indefinite && enhanced);
      }
    }
    else
    {
      licenseIs_demo.setEnable(status == IGuiElement.SEARCH);
      licenseExpiration_date.setEnable(status == IGuiElement.SEARCH);
      licenseIndefinite_flag.setEnable(status == IGuiElement.SEARCH);
      licenseUser_count.setEnable(status == IGuiElement.SEARCH);
      licenseLicensee.setEnable(status == IGuiElement.SEARCH);
    }
	}
}
