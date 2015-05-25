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

public class SystemAliasFilter implements IMessageFilter
{
    static TraceDispatcher  trace   = TraceFactory.getTraceDispatcher();

    public boolean onBeginProcess(Message m)
    {
        // check the system alias
        //
        SystemAlias alias = SystemAlias.findByAlias(m.getQuestion());
        if(alias!=null)
        {
            trace.debug1(m, "system alias found. replace '"+m.getQuestion()+"' with '"+alias.getQuestion()+"'");
            m.setQuestion(alias.getQuestion());
        }
        return true;
    }


    public boolean onEndProcess(Message m)
    {
        return true;
    }
}