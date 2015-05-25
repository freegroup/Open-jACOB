/*
 * Created on 19.09.2005 by mike
 * 
 *
 */
package jacob.common.ui;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import jacob.common.DataUtils;
import jacob.common.ui.castor.Modus;

import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

public abstract class CustomerCallbackyPhone extends IButtonEventHandler
{

    
    public abstract String getPhoneNumber(IClientContext context) throws Exception;
    
    public void onAction(IClientContext context, IGuiElement button) throws Exception
    {
        String phoneNumber = getPhoneNumber(context);
       
        StringBuffer cleanNumber = new StringBuffer();
        for (int i = 0; i < phoneNumber.length(); i++)
        {
            String c =StringUtils.mid(phoneNumber,i,1);
            if (StringUtils.isNumeric(c))
                cleanNumber.append(c);      
        }
        String agentID = (String)context.getUser().getProperty("CTIAgentID");
        agentID = StringUtils.leftPad(agentID,4,"0"); // Outbound AgentID ist 3 stellig z.B. 0020
        String serverURL = DataUtils.getAppprofileValue(context,"outbound_url");
        String daksSwitch = DataUtils.getAppprofileValue(context,"daks_switch");
        URL url = new URL(serverURL+"Ext="+agentID+"&Switch="+daksSwitch+"&Dest=%2b"+cleanNumber.toString());
        InputStream urlStream = url.openStream();
        try
        {
            try
            {
                Modus answer = (Modus)Modus.unmarshalModus(new InputStreamReader(urlStream));
                if ("0".equals(answer.getDatasection().getSP_RET_CODE()))
                {
                    return; // Verbindung wird hergestellt
                }
                else // irgend ein Fehler beim Verbindungsversuch
                {
                    alert(answer.getErrorsection().getERROR_TEXT());
                }
            }
            catch (MarshalException e)
            {
                alert("Fehlerhafte Serverantwort: "+e.getMessage());
            }
            catch (ValidationException e)
            {
                alert("Fehlerhafte Serverantwort: "+e.getMessage());
            }
        }
        finally
        {
            urlStream.close();
        }
// MIKE: sauber mit einer Castor Klasse implementieren
       
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
    {
        if (context.getUser().getProperty("CTIAgentID")==null)
        {
            emitter.setEnable(false);
        }
    }


}
