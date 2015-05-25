package de.shorti.message.filter;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */

import java.util.*;
import de.shorti.util.basic.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.message.*;

public class SecurityFilter implements IMessageFilter
{
    static TraceDispatcher  trace   = TraceFactory.getTraceDispatcher();


    public boolean onBeginProcess(Message m)
    {
        // the user are locked per administrator.
        //
        if(m.getShortiUser().getAccount().getLockStatus() !=0)
        {
            trace.debug1(m,"user is locked by administrator");
            m.setResponse(SystemMessage.get("USER_IS_LOCKED",m.getPreferedLanguage()));
            m.setStatusSuccess();
            return false;
        }
        return true;
    }


    public boolean onEndProcess(Message m)
    {
        return true;
    }
}