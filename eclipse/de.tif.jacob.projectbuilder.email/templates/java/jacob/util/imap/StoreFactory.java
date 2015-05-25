package jacob.util.imap;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.imap.IMAPStore;

import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.security.IUser;

public class StoreFactory 
{
	public static IMAPStore createStore(String server, String user, String password) throws Exception
	{
    Properties props = System.getProperties();
    Session session = Session.getInstance(props, null);
    session.setDebug(false);
    // Get a Store object
    IMAPStore store = (IMAPStore)session.getStore("imap");
    store.connect(server, user, password);

    return store;
	}
}
