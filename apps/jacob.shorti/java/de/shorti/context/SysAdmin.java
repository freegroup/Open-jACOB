package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author
 * @version 1.0
 */

import de.shorti.context.basis.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.baseknowledge.*;
import de.shorti.util.basic.*;
import de.shorti.message.Message;
import de.shorti.*;
import java.util.*;

public class SysAdmin extends GenericContext
{
    public SysAdmin()
    {
        super("./context/SysAdmin.xml");
    }

	public int onStart2Restart(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        if(isAdmin(m)==false)
            return NO_HIT;
        m.setResponseContext(getContextRegistry());
        m.setStatusSuccess();
        m.setFinishTime();
        Shorti.restart();
        // this will never be reached.....
		return REPLY;
	}

	public int onStart2Rebuild(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        if(isAdmin(m)==false)
            return NO_HIT;
        m.setResponseContext(getContextRegistry());
        m.setStatusSuccess();
        m.setFinishTime();
        Shorti.rebuildDB();
        // this will never be reached.....
		return REPLY;
	}

	public int onStart2Exit(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        if(isAdmin(m)==false)
            return NO_HIT;
        m.setResponseContext(getContextRegistry());
        m.setStatusSuccess();
        m.setFinishTime();
        Shorti.exit();
        // this will never be reached.....
		return REPLY;
	}
}