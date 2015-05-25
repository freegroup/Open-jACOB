package jacob.common.media;

import jacob.browser._contact_summaryOrderByDateDECBrowser;
import jacob.model.Contact_summary;
import jacob.model.Customer_contact;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;


public class PhoneMediaChannel implements IMediaChannelHandler
{
    private final static int CLIENT_PORT=47900;

    public void createNew(IClientContext context, IDataTableRecord contactRecord) throws Exception
    {
        String phonenumber = contactRecord.getStringValue(Customer_contact.contact);
        try
        {
            // TODO: remove the IP address
            String host = context==null ? "192.168.1.40" : context.getSession().getClientAddress();

            System.out.println("Calling :" + host + ":" + CLIENT_PORT);
            Socket s = new Socket(host, CLIENT_PORT);
            OutputStream os = s.getOutputStream();
            InputStream is = s.getInputStream();
            try
            {
                DataOutputStream dos = new DataOutputStream(os);

                // TODO: add dynamic conditioner to the phone number
                //
                String outboundcode = "0000";
                phonenumber = "*39" + outboundcode + "0" + phonenumber;
                dos.write(("type=voice&details=" + phonenumber).getBytes("ISO-8859-1"));
                dos.flush();

                DataInputStream dis = new DataInputStream(is);
                String response = dis.readLine();

                if (response == null)
                {
                    throw new UserException("Unable to establish a call to [" + phonenumber + "]","Response string [NULL] from server");
                }

                Map<String, String> map = new HashMap<String, String>();
                String[] params = response.split("&");
                for (String param: params)
                {
                    String[] split = param.split("=");
                    String name  = split[0];
                    String value = split[1];
                    map.put(name, value);
                }
                String type = map.get("type");
                String eduid= map.get("eduid");
                String message= map.get("message");

                if (!"success".equals(type))
                {
                    throw new UserException("Unable to establish call to [" + phonenumber + "]", "Wrong result type=[" + type + "]. Expecting type=[success]\n" + message + "\nRESPONSE:" + response);
                }

                if (eduid == null)
                {
                    throw new UserException("Unable to establsih call to [" + phonenumber + "]", "No [eduid] in response message from server call\nRESPONSE: " + response);
                }

                IDataBrowser contactBrowser = context.getDataBrowser(_contact_summaryOrderByDateDECBrowser.NAME);
                contactBrowser.setMaxRecords(1);
                IDataTable contactTable = context.getDataTable(Contact_summary.NAME);
                contactTable.qbeClear();
                contactTable.qbeSetKeyValue(Contact_summary.eduid, eduid);
                contactBrowser.search(IRelationSet.LOCAL_NAME);
                if (contactBrowser.recordCount() == 1)
                {
                    context.getDataAccessor().propagateRecord(contactBrowser.getRecord(0).getTableRecord(), Filldirection.BOTH);
                }
                else
                {
                    context.createMessageDialog("No Contact Found" + eduid).show();
                }
                ///Backfill
                context.showTransparentMessage("RING........");
            }
            finally
            {
                try { os.close(); } catch (Exception ex) {/*ignore*/}
                try { is.close(); } catch (Exception ex) {/*ignore*/}
                try { s.close(); } catch (Exception ex) {/*ignore*/}
            }
        }
        catch (UserException exc)
        {
            throw exc;
        }
        catch (ConnectException exc)
        {
            UserException e = new UserException("Unable to establish a call to [" + phonenumber + "]");
            e.initCause(exc);
            throw e;
        }
        catch (Exception exc)
        {
            UserException e= new UserException("Unable to establish a call to [" + phonenumber + "]");
            e.initCause(exc);
            throw e;
        }
    }

    public void replyTo(IClientContext context,	IDataTableRecord eventHistoryRecord)
    {
        context.createMessageDialog("function {PhoneMediaChannel.replyTo} not implemented at the moment").show();

    }

}
